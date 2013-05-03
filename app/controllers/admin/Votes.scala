package controllers.admin

import play.api.mvc.Controller
import models.vote.dao.VoteDao
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import play.api.libs.json.Json


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-3
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
object Votes extends Controller {

  def votes(p:Int) = Managers.AdminAction{manager => implicit request =>
    val page =VoteDao.findVotes(p,20)
    Ok(views.html.admin.votes.votes(manager,page))
  }

  /* 添加 */
  def edit(id:Long) = Managers.AdminAction{manager => implicit request =>
      Ok(views.html.admin.votes.edit(manager))
  }
  def save = Managers.AdminAction{manager => implicit request =>
   Ok("success")
  }
  def delete(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =VoteDao.deleteVote(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> "删除成功"))
    else Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))
  }
  def batch = Managers.AdminAction{manager => implicit request =>
    Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))
  }

  def filter = Managers.AdminAction{manager => implicit request =>
    Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))
  }

}
