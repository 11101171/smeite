package controllers

import play.api.mvc.Controller
import controllers.users.Users


object Sites extends Controller {
  /* 小站编辑创建 */
  def editSite(id:Long) = Users.UserAction { user => implicit request =>
    Ok(views.html.sites.editSite(user))
  }

  /* 小站  */
  def site(id:Long) = Users.UserAction { user => implicit request =>
    Ok(views.html.sites.site(user))
  }

  /* 小站 帖子 编辑创建 */
  def editPost(id:Long) = Users.UserAction { user => implicit request =>
    Ok(views.html.sites.editPost(user))
  }
  /* 小站帖子 */
  def post(sid:Long,pid:Long)= Users.UserAction { user => implicit request =>
    Ok(views.html.sites.post(user))
  }

}
