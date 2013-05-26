package schedule

import akka.actor.Actor
import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.request.TaobaokeReportGetRequest
import com.taobao.api.domain.{TaobaokeReportMember, TaobaokeReport}
import models.user.{UserCreditRecord, ShiDouSetting, UserOrder}
import models.user.dao.{UserSQLDao, UserDao}
import java.sql.Timestamp
import models.report.dao.TaobaokeIncomeDao
import models.report.TaobaokeIncome
import scala.collection.JavaConverters._
import utils.Utils

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-23
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
class TaobaokeIncomeActor extends Actor {

  private def incomes(day:String) = {
    val url:String = "http://gw.api.taobao.com/router/rest"
    val appkey = "21136607"
    val secret = "b43392b7a08581a8916d2f9fa67003db"
    val client =new DefaultTaobaoClient(url, appkey, secret);
    val  req:TaobaokeReportGetRequest = new TaobaokeReportGetRequest();
    req.setFields("num_iid,trade_id,outer_code,real_pay_fee,commission_rate,commission,pay_price,item_num,create_time,pay_time");
    req.setDate(day);
    req.setPageSize(100l);
    var p:Long =1l
    var flag:Boolean=true
    while(flag){
      //println("go into while" + p)
      req.setPageNo(p);
      val report:TaobaokeReport = client.execute(req).getTaobaokeReport;
      if(report != null){
        if(report.getTaobaokeReportMembers !=null){
          for(item <- report.getTaobaokeReportMembers.asScala){
            handleTaobaokeIncome(item,day)
          }
        }else {
          flag=false
        }
        p+=1;
      }
    }
  }
  /* -1 表示 淘宝客返回的outer——code 为 null*/
  private def handleTaobaokeIncome(item:TaobaokeReportMember,day:String)={
    /* 处理 user order 与 淘宝客report 的关系
    * 1. 判断outer_code 是否为null
    * 2、根据user_id 和 num_iid 查找 user_order , 如果不为空，判断 list大小，如果list.length=1,直接使用userOrder ,如果list.length >1 则判断userOrder createTime 与 taobaoke的create time 最近的那个 获得userOrder
    *
    * */

    if(item.getOuterCode != null){
      val uid = item.getOuterCode.toLong
      val list:List[UserOrder]=UserDao.findUserOrder(uid,item.getNumIid)
      /* 获取 user order*/
      var order:UserOrder= null;
      if(!list.isEmpty){
        order=  if(list.length == 1) {list.head } else { list.sortBy(x=>(x.createTime.get.getTime-item.getCreateTime.getTime).abs).head }
      }
      val userCommission = if(order !=null ){ (item.getCommission.toFloat*order.withdrawRate).toInt }else { (item.getCommission.toFloat*70/100).toInt }
      val userOrderId:Long = if(order != null){ order.id.get }else{ 0 }
      UserDao.modifyUserOrder(order.id.get,1,new Timestamp(item.getCreateTime.getTime))

      /* 查看 user rebate 是否添加，如果没有，则添加*/
      val rebate = UserDao.findUserRebateByTradeId(item.getTradeId)
      if(rebate.isEmpty){
        /* 赠送额外的 食豆 和 食豆记录,目前所有用户都获得额外的1个食豆*/
        UserDao.modifyShiDou(uid,ShiDouSetting.rebateShiDou1)
        UserDao.addUserCreditRecord(UserCreditRecord(None,uid,1,ShiDouSetting.rebateShiDou1,"购物成功后获赠",new Timestamp(System.currentTimeMillis())))
        UserDao.addUserRebate(uid,userCommission,1,userOrderId,item.getTradeId)
      }

    }
    val income = TaobaokeIncomeDao.findTaobaokeIncome(item.getTradeId)
    if(income.isEmpty){
      val outerCode = if(item.getOuterCode == null){ "-1"} else item.getOuterCode
      TaobaokeIncomeDao.addTaobaokeIncome(TaobaokeIncome(None,item.getNumIid,item.getTradeId,outerCode,item.getRealPayFee,item.getCommissionRate,item.getCommission,item.getPayPrice,item.getItemNum,day,new Timestamp(item.getCreateTime.getTime),new Timestamp(item.getPayTime.getTime)))
    }

  }


  def receive = {
    case "start" => incomes(Utils.timestampFormat(new Timestamp3(System.currentTimeMillis())))
    case _ => println("schedule invite prize got something wrong")
  }
}
