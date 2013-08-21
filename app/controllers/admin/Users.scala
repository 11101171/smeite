package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.user.dao.{UserSQLDao, UserDao}
import models.tag.dao.TagDao

import java.util.Date
import play.api.Play

import scala.collection.JavaConverters._

import java.sql.Timestamp
import models.user.{ShiDouSetting,UserExchangeCredit}


case class UserBatchFormData(action:Int,ids:Seq[Long],url:Option[String])
case  class UserFilterFormData(name:Option[String],status:Option[Int],daren:Option[Int],comeFrom:Option[Int],creditsOrder:String,shiDouOrder:String,idOrder:String,currentPage:Option[Int])
case class FilterExchangeShiDouFormData(status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])
case class ExchangeShiDouFormData(id:Long,uid:Long,name:String,alipay:String,num:Int,handleStatus:Int,handleResult:Int,note:Option[String])

case class FilterInvitePrizeFormData(status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])
case class InvitePrizeFormData(id:Long,uid:Long,name:String,alipay:String,num:Int,handleStatus:Int,handleResult:Int,note:Option[String])


case class FilterUserExchangeCreditFormData(uid:Option[Long],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])
case class UserExchangeCreditFormData(id:Long,uid:Long,name:String,alipay:String,num:Int,handleStatus:Int,handleResult:Int,note:Option[String])

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
     "uid"->longNumber,
     "name"->text,
     "alipay"->text,
     "num"->number,
     "handleStatus"->number,
     "handleResult"->number,
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
      "uid"->longNumber,
      "name"->text,
      "alipay"->text,
      "num"->number,
      "handleStatus"->number,
      "handleResult"->number,
      "note"->optional(text)
    )(InvitePrizeFormData.apply)(InvitePrizeFormData.unapply)
  )




  val filterUserExchangeCreditForm =Form(
    mapping(
      "uid"->optional(longNumber()),
      "status"->optional(number),
      "startDate"->optional(date("yyyy-MM-dd")),
      "endDate"->optional(date("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(FilterUserExchangeCreditFormData.apply)(FilterUserExchangeCreditFormData.unapply)
  )
  val userExchangeCreditForm = Form(
    mapping(
      "id"->longNumber,
      "uid"->longNumber,
      "name"->text,
      "alipay"->text,
      "num"->number,
      "handleStatus"->number,
      "handleResult"->number,
      "note"->optional(text)
    )(UserExchangeCreditFormData.apply)(UserExchangeCreditFormData.unapply)
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
    val (user,ue) = UserDao.findUserExchangeShiDou(id)
    Ok(views.html.admin.users.editExchangeShiDou(manager,exchangeShiDouForm.fill(ExchangeShiDouFormData(ue.id.get,user.id.get,user.name,user.alipay.getOrElse("none"),ue.num,ue.handleStatus,ue.handleResult,ue.note))))
  }

  def saveExchangeShiDou  = Managers.AdminAction{ manager => implicit request =>
    exchangeShiDouForm.bindFromRequest.fold(
      formWithErrors =>BadRequest(views.html.admin.users.editExchangeShiDou(manager,formWithErrors,"出错了")),
      data => {
         UserDao.modifyUserExchangeShiDou(data.id,data.handleStatus,data.handleResult,data.note.getOrElse(""))
        /* 如果处理成功 则修改user withdraw shidou 数量 */
        if(data.handleStatus == 1) UserDao.modifyWithdrawShiDou(data.uid,data.num)
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
        val invitePrize = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,200)
        if(invitePrize.isEmpty){ UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,200)  }
      }
       if(user.credits >=1000 && user.credits < 5000){
         val invitePrize1 = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,200)
         if(invitePrize1.isEmpty){  UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,200)}

         val invitePrize2 = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,300)
         if(invitePrize2.isEmpty){  UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,300)}
       }
       if(user.credits >= 5000 ){
         val invitePrize1 = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,200)
         if(invitePrize1.isEmpty){  UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,200)}

         val invitePrize2 = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,300)
         if(invitePrize2.isEmpty){  UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,300)}

         val invitePrize3 = UserDao.findUserInvitePrize(up.inviteId.get,up.uid,500)
         if(invitePrize3.isEmpty){ UserDao.addUserInvitePrize(up.inviteId.get,up.uid,user.credits,500) }
       }
     }
  }
    Ok(Json.obj("code"->"100","message"->"success"))
  }


  def editInvitePrize(id:Long) = Managers.AdminAction{ manager => implicit request =>
    val (user,ui) = UserDao.findUserInvitePrize(id)
    Ok(views.html.admin.users.editInvitePrize(manager,invitePrizeForm.fill(InvitePrizeFormData(ui.id.get,user.id.get,user.name,user.alipay.getOrElse("none"),ui.num,ui.handleStatus,ui.handleResult,ui.note))))
  }

  def saveInvitePrize  = Managers.AdminAction{ manager => implicit request =>
    invitePrizeForm.bindFromRequest.fold(
      formWithErrors =>BadRequest(views.html.admin.users.editInvitePrize(manager,formWithErrors,"出错了")),
      data => {
        UserDao.modifyUserInvitePrize(data.id,data.handleStatus,data.handleResult,data.note.getOrElse(""))
        /* 如果处理成功 则修改user withdraw credits 数量 */
        if(data.handleStatus == 1) UserDao.modifyWithdrawCredits(data.uid,data.num)
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






  /* 下面 是 user exchange credit */
  def  exchangeCredits(p:Int) = Managers.AdminAction{ manager => implicit request =>
    val page = UserDao.findUserExchangeCredits(p,50)
    Ok(views.html.admin.users.exchangeCredits(manager,page))
  }

  def filterExchangeCredits  = Managers.AdminAction{ manager => implicit request =>
    filterUserExchangeCreditForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong" +formWithErrors.errors.toString),
      data => {
        val page=UserDao.filterUserExchangeCredits(data.uid,data.status,data.startDate,data.endDate,data.currentPage.getOrElse(1),50);
        Ok(views.html.admin.users.filterUserExchangeCredits(manager,page,filterUserExchangeCreditForm.fill(data)))
      }
    )
  }
  def editExchangeCredit(id:Long) = Managers.AdminAction{ manager => implicit request =>
    val (user,ui) = UserDao.findUserExchangeCredit(id)
    Ok(views.html.admin.users.editUserExchangeCredit(manager,userExchangeCreditForm.fill(UserExchangeCreditFormData(ui.id.get,user.id.get,user.name,user.alipay.getOrElse("none"),ui.num,ui.handleStatus,ui.handleResult,ui.note))))
  }

  def saveExchangeCredit  = Managers.AdminAction{ manager => implicit request =>
    userExchangeCreditForm.bindFromRequest.fold(
      formWithErrors =>BadRequest(views.html.admin.users.editUserExchangeCredit(manager,formWithErrors,"出错了")),
      data => {
        UserDao.modifyUserExchangeCredit(data.id,data.handleStatus,data.handleResult,data.note.getOrElse(""))
        /* 如果处理成功 则修改user withdraw credits 数量 */
        if(data.handleStatus == 1) UserDao.modifyWithdrawCredits(data.uid,data.num)
        Ok(views.html.admin.users.editUserExchangeCredit(manager,userExchangeCreditForm.fill(data),"修改成功"))
      }
    )
  }







}
