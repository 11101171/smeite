package controllers.users

import play.api.mvc.Controller
import models.user.dao.UserDao

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-10
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
object UsersCommission  extends Controller {

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
      Ok(views.html.users.commission.myShiDou(user) )
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
