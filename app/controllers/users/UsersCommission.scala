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

 def cashBack= Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      if(user.get.email.isEmpty){
        Redirect(controllers.users.routes.UsersRegLogin.before())
      }else {
        Ok(views.html.users.commission.cashBack()
      }
    }

  }
}
