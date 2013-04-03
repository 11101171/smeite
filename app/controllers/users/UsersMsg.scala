package controllers.users
import play.api.mvc.{Action, Controller}
/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-23
 * Time: 下午6:09
 * Email:zuosanshao@qq.com
 */

object UsersMsg extends Controller {
  /*系统通知*/
       def system =Users.UserAction{  user => implicit request =>
        Ok(views.html.users.msg.system(user))
       }
  /*评论我的*/
      def  comment =Users.UserAction{  user => implicit request =>
        Ok(views.html.users.msg.comment(user))
      }
  /*回复我的*/
  def  reply =Users.UserAction{    user => implicit request =>
    Ok(views.html.users.msg.reply(user))
  }
  /*@我的*/
  def  at =Users.UserAction{     user => implicit request =>
    Ok(views.html.users.msg.at(user))
  }
  /*我的求鉴定*/
  def  appraisal =Users.UserAction{  user => implicit request =>
    Ok(views.html.users.msg.appraisal(user))
  }
}
