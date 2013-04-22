package controllers.users

import play.api.mvc.Controller
import models.user.dao.UserDao
import play.api.data.Form
import play.api.data.Forms._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-10
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
object UsersCommission  extends Controller {
  val exchangeForm = Form (
      "shiDou" ->number(min=1000)
  )
/*我的返利*/
 def myCredits= Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
        Ok(views.html.users.commission.myCredits(user))
    }
  }
  /*我的食豆*/
  def myShiDou = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      Ok(views.html.users.commission.myShiDou(user,exchangeForm) )
    }
  }
  def exchangeShiDou = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      exchangeForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.users.commission.myShiDou(user,formWithErrors))
        } ,
        fields =>{
       //  UserDao.modifyAlipay(user.get.id.get,fields._1.getOrElse(""),fields._2.getOrElse(""))
          Ok(views.html.users.commission.myShiDou(user,exchangeForm.fill(fields),"兑换申请成功，我们将尽快处理，并把处理结果通知您，谢谢您对我们的信任和支持。"))

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


}
