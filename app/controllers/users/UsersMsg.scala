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
  /*评论 回复 我的*/
      def  comment =Users.UserAction{  user => implicit request =>
        Ok(views.html.users.msg.comment(user))
      }
  /*喜欢我的主题 宝贝*/
  def love = Users.UserAction{    user => implicit request =>
    Ok(views.html.users.msg.love(user))
  }
  /*@我的*/
  def  at =Users.UserAction{     user => implicit request =>
    Ok(views.html.users.msg.at(user))
  }

}
