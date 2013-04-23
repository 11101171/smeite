package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.tag.dao.TagDao
import java.util.Date

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

  /*用户管理*/
def list(p:Int) = Managers.AdminAction{manager => implicit request =>
    val page =UserDao.findAll(p,20)
  Ok(views.html.admin.users.list(manager,page))
}
   /* 用户拉黑处理 */
  def black(uid:Long)= Managers.AdminAction{manager => implicit request =>
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

}
