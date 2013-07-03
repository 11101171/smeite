package controllers.admin

import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.msg.dao.{ReplyMsgDao, FavorMsgDao, AtMsgDao, SystemMsgDao}

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 上午12:03
 */
object Msgs extends Controller {


  def systemMsgs(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SystemMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.systemMsgs(manager,page))
  }

  def atMsgs(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
   val page = AtMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.atMsgs(manager,page))
  }
  def favorMsgs(currentPage:Int) =Managers.AdminAction{ manager => implicit request =>
    val page = FavorMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.favorMsgs(manager,page))
  }

  def replyMsgs(currentPage:Int) =Managers.AdminAction{ manager => implicit request =>
    val page = ReplyMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.replyMsgs(manager,page))
  }


}
