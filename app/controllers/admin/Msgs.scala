package controllers.admin

import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.msg.dao.SystemMsgDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 上午12:03
 */
object Msgs extends Controller {


  def system(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SystemMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.system(manager,page))
  }
}
