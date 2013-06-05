package controllers

import play.api.mvc.Controller
import controllers.users.Users


object Sites extends Controller {
    /* 小站首页 */
  def view(id:Long) = Users.UserAction { user => implicit request =>
   Ok("todo")
  }
  /* 小站帖子 */
  def post(sid:Long,pid:Long)= Users.UserAction { user => implicit request =>
      Ok("todo")
  }

}
