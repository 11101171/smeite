package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.tag.dao.TagDao
import models.report.dao.TaobaokeIncomeDao
import java.util.Date
import play.api.Play
import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.request.{TaobaokeReportGetRequest, TaobaokeWidgetItemsConvertRequest, ItemGetRequest}
import com.taobao.api.response.TaobaokeReportGetResponse
import com.taobao.api.domain.{TaobaokeReportMember, TaobaokeReport}
import scala.collection.JavaConverters._
import models.report.TaobaokeIncome
import java.sql.Timestamp
import models.user.{UserOrder, UserRebate}

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-12-13
 * Time: 上午8:54
 * To change this template use File | Settings | File Templates.
 */
case class UserBatchFormData(action:Int,ids:Seq[Long],url:Option[String])
case  class UserFilterFormData(name:Option[String],status:Option[Int],daren:Option[Int],comeFrom:Option[Int],creditsOrder:String,shiDouOrder:String,idOrder:String,currentPage:Option[Int])
case class FilterExchangeShiDouFormData(status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])
case class ExchangeShiDouFormData(id:Long,name:String,alipay:String,num:Int,handleStatus:Int,handleResult:String,note:Option[String])

case class FilterInvitePrizeFormData(status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])
case class InvitePrizeFormData(id:Long,name:String,alipay:String,num:Int,handleStatus:Int,handleResult:String,note:Option[String])

case class FilterUserOrderFormData(status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])

case class GetTaobaokeIncomeFormData(day:String)
case class FilterTaobaokeIncomeFormData(outerCode:Option[String],day:Option[String],currentPage:Option[Int])
object Users  extends Controller {
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text)
    )(UserBatchFormData.apply)(UserBatchFormData.unapply)
  )
  /*检索标签*/
  val userFilterForm =Form(
    mapping(
      "name"->optional(text),
      "status"->optional(number),
      "daren"->optional(number),
      "comeFrom"->optional(number),
      "creditsOrder"->nonEmptyText(),
      "shiDouOrder"->nonEmptyText(),
      "idOrder"->nonEmptyText(),
      "currentPage"->optional(number)
    )(UserFilterFormData.apply)(UserFilterFormData.unapply)
  )
  val filterExchangeShiDouForm =Form(
    mapping(
      "status"->optional(number),
      "startDate"->optional(date("yyyy-MM-dd")),
      "endDate"->optional(date("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(FilterExchangeShiDouFormData.apply)(FilterExchangeShiDouFormData.unapply)
  )

  val exchangeShiDouForm = Form(
   mapping(
     "id"->longNumber,
     "name"->text,
     "alipay"->text,
     "num"->number,
     "handleStatus"->number,
     "handleResult"->text,
     "note"->optional(text)
   )(ExchangeShiDouFormData.apply)(ExchangeShiDouFormData.unapply)
  )

  val filterInvitePrizeForm =Form(
    mapping(
      "status"->optional(number),
      "startDate"->optional(date("yyyy-MM-dd")),
      "endDate"->optional(date("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(FilterInvitePrizeFormData.apply)(FilterInvitePrizeFormData.unapply)
  )

  val invitePrizeForm = Form(
    mapping(
      "id"->longNumber,
      "name"->text,
      "alipay"->text,
      "num"->number,
      "handleStatus"->number,
      "handleResult"->text,
      "note"->optional(text)
    )(InvitePrizeFormData.apply)(InvitePrizeFormData.unapply)
  )

  val getTaobaokeIncomeForm = Form(
     "day"->nonEmptyText
  )
  val filterTaobaokeIncomeForm =Form(
    mapping(
      "outerCode"->optional(text),
      "day"->optional(text),
      "currentPage"->optional(number)
    )(FilterTaobaokeIncomeFormData.apply)(FilterTaobaokeIncomeFormData.unapply)
  )

  val filterUserOrderForm =Form(
    mapping(
      "status"->optional(number),
      "startDate"->optional(date("yyyy-MM-dd")),
      "endDate"->optional(date("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(FilterUserOrderFormData.apply)(FilterUserOrderFormData.unapply)
  )

  /*用户管理*/
def list(p:Int) = Managers.AdminAction{manager => implicit request =>
    val page =UserDao.findAll(p,20)
  Ok(views.html.admin.users.list(manager,page))
}
   /* 用户拉黑处理 */
  def black(uid:Long)= Managers.AdminAction{ manager => implicit request =>
       val result = UserDao.modifyStatus(uid,4)
     if (result>0)Ok(Json.obj("code"->"100","message"->"success"))
     else Ok(Json.obj("code"->"104","message"->"更新失败"))
  }

  /* 用户信息修改 */
  def edit(uid:Long)=Managers.AdminAction{manager => implicit request =>
       val user =UserDao.findById(uid)
    Ok("succsess")
  }
  /* 用户信息查看*/
  def view(uid:Long)=Managers.AdminAction{manager => implicit request =>
    val user =UserDao.findWithProfile(uid)
    val trends = UserDao.findUserTrends(uid)
    Ok(views.html.admin.users.view(manager,user,trends))
  }


  /* 批量处理 */
  def batchUsers=Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 4){
          for(id<-batch.ids){
            UserDao.modifyStatus(id,4)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            UserDao.modifyStatus(id,1)
          }
        }
        Redirect(batch.url.getOrElse("/admin/users/list"))
      }
    )
  }

  /*用户过滤*/
  def filterUsers = Managers.AdminAction{ manager => implicit request =>
    userFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      user => {
        //   println(group.cid + "   " +group.name + "    " +group.isVisible )
        val page=UserDao.filterUsers(user.name,user.status,user.daren,user.comeFrom,user.creditsOrder,user.shiDouOrder,user.idOrder,user.currentPage.getOrElse(1),50);
        Ok(views.html.admin.users.filterUsers(manager,page,userFilterForm.fill(user)))
      }
    )
  }

  /*用户申请兑换食豆*/
 def exchangeShiDous(p:Int) = Managers.AdminAction{ manager => implicit request =>
       val page = UserDao.findUserExchangeShiDous(p,50)
    Ok(views.html.admin.users.exchangeShiDous(manager,page))
  }

def filterExchangeShiDou = Managers.AdminAction{ manager => implicit request =>
  filterExchangeShiDouForm.bindFromRequest.fold(
    formWithErrors =>Ok("something wrong" +formWithErrors.errors.toString),
    data => {
     val page=UserDao.filterExchangeShiDous(data.status,data.startDate,data.endDate,data.currentPage.getOrElse(1),50);
      Ok(views.html.admin.users.filterExchangeShiDous(manager,page,filterExchangeShiDouForm.fill(data)))
    }
  )
}
  def editExchangeShiDou(id:Long) = Managers.AdminAction{ manager => implicit request =>
    val (user,up,ue) = UserDao.findUserExchangeShiDou(id);
    Ok(views.html.admin.users.editExchangeShiDou(manager,exchangeShiDouForm.fill(ExchangeShiDouFormData(ue.id.get,user.name,up.alipay.getOrElse("none"),ue.num,ue.handleStatus,ue.handleResult,ue.note))))
  }

  def saveExchangeShiDou  = Managers.AdminAction{ manager => implicit request =>
    exchangeShiDouForm.bindFromRequest.fold(
      formWithErrors =>BadRequest(views.html.admin.users.editExchangeShiDou(manager,formWithErrors,"出错了")),
      data => {
         UserDao.modifyUserExchangeShiDou(data.id,data.handleStatus,data.handleResult,data.note.getOrElse(""))
      Ok(views.html.admin.users.editExchangeShiDou(manager,exchangeShiDouForm.fill(data),"修改成功"))
      }
    )
  }

  def invitePrizes(p:Int)  =  Managers.AdminAction{ manager => implicit request =>
     val page = UserDao.findUserInvitePrizes(p,50)
    Ok(views.html.admin.users.invitePrizes(manager,page))

  }

  /* 批量更新 邀请有奖 */
  def batchInvitePrizes  =  Managers.AdminAction{ manager => implicit request =>
      val totalRows = UserDao.getInviterNum

      val totalPages=((totalRows + 100 - 1) / 100).toInt;
      for(i<- 1 to totalPages ){
      val userProfiles= UserDao.getInviters(i,100);
     for(up<- userProfiles ){
      val user = UserDao.findById(up.uid)
      if(user.credits >=100 && user.credits < 1000){
        val invitePrize = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,2)
        if(invitePrize.isEmpty){
          UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,2)
        }
      }
       if(user.credits >=1000 && user.credits < 3000){
         val invitePrize = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,3)
         if(invitePrize.isEmpty){
           UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,3)
         }
       }
       if(user.credits >=3000 ){
         val invitePrize = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,5)
         if(invitePrize.isEmpty){
           UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,5)
         }
       }
     }
  }
    Ok(Json.obj("code"->"100","message"->"success"))
  }


  def editInvitePrize(id:Long) = Managers.AdminAction{ manager => implicit request =>
    val (user,up,ui) = UserDao.findUserInvitePrize(id)
    Ok(views.html.admin.users.editInvitePrize(manager,invitePrizeForm.fill(InvitePrizeFormData(ui.id.get,user.name,up.alipay.getOrElse("none"),ui.num,ui.handleStatus,ui.handleResult,ui.note))))
  }

  def saveInvitePrize  = Managers.AdminAction{ manager => implicit request =>
    invitePrizeForm.bindFromRequest.fold(
      formWithErrors =>BadRequest(views.html.admin.users.editInvitePrize(manager,formWithErrors,"出错了")),
      data => {
        UserDao.modifyUserInvitePrize(data.id,data.handleStatus,data.handleResult,data.note.getOrElse(""))
      Ok(views.html.admin.users.editInvitePrize(manager,invitePrizeForm.fill(data),"修改成功"))
      }
    )
  }
  def filterInvitePrize = Managers.AdminAction{ manager => implicit request =>
    filterInvitePrizeForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong" +formWithErrors.errors.toString),
      data => {
        val page=UserDao.filterInvitePrizes(data.status,data.startDate,data.endDate,data.currentPage.getOrElse(1),50);
        Ok(views.html.admin.users.filterInvitePrizes(manager,page,filterInvitePrizeForm.fill(data)))
      }
    )
  }


  /*下面是 User orders */
  def orders(p:Int) = Managers.AdminAction{ manager => implicit request =>
         val page = UserDao.findUserOrders(p,50)
    Ok(views.html.admin.users.orders(manager,page))
  }

  def filterOrders = Managers.AdminAction{ manager => implicit request =>
    filterUserOrderForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong" +formWithErrors.errors.toString),
      data => {
        val page=UserDao.filterUserOrders(data.status,data.startDate,data.endDate,data.currentPage.getOrElse(1),50);
        Ok(views.html.admin.users.filterUserOrders(manager,page,filterUserOrderForm.fill(data)))
      }
    )
  }





  /* 下面 是 user rebate */
  def  rebates(p:Int) = Managers.AdminAction{ manager => implicit request =>
    val page = UserDao.findUserRebates(p,50)
    Ok(views.html.admin.users.rebates(manager,page))
  }


    /*  下面是taobaoke 报表*/
    /* taobaoke  */
  def taobaokeIncomes(p:Int) = Managers.AdminAction{ manager => implicit request =>
      val page = TaobaokeIncomeDao.findTaobaokeIncomes(p,50)
   Ok(views.html.admin.users.taobaokeIncomes(manager,page))
  }

  def filterTaobaokeIncomes = Managers.AdminAction{ manager => implicit request =>
    filterTaobaokeIncomeForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      data => {
        val page=TaobaokeIncomeDao.filterTaobaokeIncomes(data.day,data.outerCode,data.currentPage.getOrElse(1),50);
        Ok(views.html.admin.users.filterTaobaokeIncomes(manager,page,filterTaobaokeIncomeForm.fill(data)))
      }
    )
  }




  /* 查询淘宝客，获取收入 */
  def getIncomes = Managers.AdminAction{ manager => implicit request =>
    getTaobaokeIncomeForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      data => {
         incomes(data)
        Redirect(controllers.admin.routes.Users.taobaokeIncomes(1))
      }
    )
  }
  /*
  *  首先判断 report
  * */
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
      println("go into while" + p)
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
        UserDao.addUserRebate(uid,userCommission,0,userOrderId,item.getTradeId)
      }

    }
       val income = TaobaokeIncomeDao.findTaobaokeIncome(item.getTradeId)
     if(income.isEmpty){
       val outerCode = if(item.getOuterCode == null){ "-1"} else item.getOuterCode
        TaobaokeIncomeDao.addTaobaokeIncome(TaobaokeIncome(None,item.getNumIid,item.getTradeId,outerCode,item.getRealPayFee,item.getCommissionRate,item.getCommission,item.getPayPrice,item.getItemNum,day,new Timestamp(item.getCreateTime.getTime),new Timestamp(item.getPayTime.getTime)))
     }

  }
}
