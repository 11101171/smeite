package controllers.users

import play.api.mvc.Controller
import models.user.dao.UserDao
import play.api.data.Form
import play.api.data.Forms._
import java.sql.Timestamp
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class InvitePrize(num:Int,handleStatus:Int,handleResult:String)
object UsersCommission  extends Controller {
  val exchangeForm = Form (
      "shiDou" ->number(min=1000)
  )
  implicit val userInvitePrizeFormat = (
      (__ \ "num").format[Int] and
      (__ \ "handleStatus").format[Int] and
      (__ \ "handleResult").format[String]
    )(InvitePrize.apply,unlift(InvitePrize.unapply))


/*我的返利*/
 def myCredits(p:Int)= Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      val prizes = UserDao.findUserInvitePrizes(user.get.id.get,p,20)
        Ok(views.html.users.commission.myCredits(user,prizes))
    }
  }
  /*我的食豆*/
  def myShiDou(p:Int) = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
      else {
     var page = UserDao.findUserExchangeShiDous(user.get.id.get,p,20)
      Ok(views.html.users.commission.myShiDou(user,exchangeForm,page) )
    }
  }
  /* 食豆申请 */
  def exchangeShiDou = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      var page = UserDao.findUserExchangeShiDous(user.get.id.get,1,20)
      exchangeForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.users.commission.myShiDou(user,formWithErrors,page))
        } ,
        fields =>{
          if((user.get.shiDou - user.get.withdrawShiDou)< fields){
            Redirect(controllers.users.routes.UsersCommission.myShiDou(1))
         ///   Ok(views.html.users.commission.myShiDou(user,exchangeForm.fill(fields),page,"您申请兑换的食豆数量大于您目前的余额"))
          }
          UserDao.addUserExchangeShiDou(user.get.id.get,fields)
        //  Ok(views.html.users.commission.myShiDou(user,exchangeForm.fill(fields),page,"兑换申请成功，我们将尽快处理，并把处理结果通知您，谢谢您对我们的信任和支持。"))
          Redirect(controllers.users.routes.UsersCommission.myShiDou(1))
        }
      )
    }
  }
  /* user account 用户账户 我的奖品 */
  def myAward = Users.UserAction {    user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else  Ok(views.html.users.commission.myAward(user))

  }

  /*邀请有奖*/
  /* user account 用户账户 邀请好友 */
  def invite(currentPage:Int) = Users.UserAction {  user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      val totalInviters = UserDao.getInviterNum(user.get.id.get)
      val totalReward = UserDao.getInviteReward(user.get.id.get)
      val page =UserDao.getInviters(user.get.id.get,currentPage,20)
      Ok(views.html.users.commission.invite(user,page,totalInviters,totalReward))
    }
  }

  /* 获得 邀请有奖 */
 def  getInvitePrizes(uid:Long,inviteeId:Long)  = Users.UserAction {  user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      val prizes = UserDao.findUserInvitePrizes(uid,inviteeId).map(x=>InvitePrize(x.num,x.handleStatus,models.user.HandleResult(x.handleResult).toString))
      Ok(Json.obj("code"->"100","size"->prizes.length,"prizes"->Json.toJson(prizes)))

    }
  }


}
