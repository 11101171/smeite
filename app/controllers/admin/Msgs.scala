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

case class SystemMsgFormData(id:Option[Long],title:String,content:String)

case class SystemMsgFilterFormData(title:Option[String],currentPage:Option[Int])

case class MsgBatchFormData(action:Int,ids:Seq[Long])

object Msgs extends Controller {
  val systemMsgForm =Form(
    mapping(
      "id"->optional(longNumber),
      "title" -> nonEmptyText,
      "content" -> nonEmptyText
    )(SystemMsgFormData.apply)(SystemMsgFormData.unapply)
  )

  val systemMsgFilterForm =Form(
    mapping(
      "title"->optional(text),
      "currentPage"->optional(number)
    )(SystemMsgFilterFormData.apply)(SystemMsgFilterFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber)
    )(MsgBatchFormData.apply)(MsgBatchFormData.unapply)
  )



  def systemMsgs(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SystemMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.systemMsgs(manager,page))
  }
  /*  编辑 系统站内信 */
  def editSystemMsg(id:Long) = Managers.AdminAction{ manager => implicit request =>
    if (id==0) Ok(views.html.admin.msgs.editSystemMsg(manager,systemMsgForm))
    else {
      val systemMsg=SystemMsgDao.findMsg(id)
      if(systemMsg.isEmpty) Ok(views.html.admin.msgs.editSystemMsg(manager,systemMsgForm))
      else  Ok(views.html.admin.msgs.editSystemMsg(manager,systemMsgForm.fill(SystemMsgFormData(systemMsg.get.id,systemMsg.get.title,systemMsg.get.content))))
    }
  }
  def saveSystemMsg = Managers.AdminAction{ manager => implicit request =>
    systemMsgForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.msgs.editSystemMsg(manager,formWithErrors)),
      systemMsg => {
        /*如果group id 为空，则保存数据 ，否则，则update数 */
        if(systemMsg.id.isEmpty){
          SystemMsgDao.addMsg(systemMsg.title,systemMsg.content)
        }else{
          SystemMsgDao.updateMsg(systemMsg.id.get,systemMsg.title,systemMsg.content)
        }
   //     Redirect(request.headers.get("REFERER").getOrElse("/admin/msgs/systemMsgs"))
        Redirect("/admin/msgs/systemMsgs")
      }
    )
  }

  /* 筛选 系统站内信 */
   def filterSystemMsgs = Managers.AdminAction{ manager => implicit request =>
    systemMsgFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      msg => {
        val page=SystemMsgDao.filterMsgs(msg.title,msg.currentPage.getOrElse(1),50)
        Ok(views.html.admin.msgs.filterSystemMsgs(manager,page,systemMsgFilterForm.fill(msg)))
      }
    )

  }
  /* 批量处理站内信 */
  def batchSystemMsgs  = Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            SystemMsgDao.updateMsgStatus(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            SystemMsgDao.updateMsgStatus(id,1)
          }
        }else if(batch.action==2){
          for(id<-batch.ids){
            SystemMsgDao.deleteMsg(id)
          }
        }
        Redirect(request.headers.get("REFERER").getOrElse("/admin/msgs/systemMsgs"))
      }
    )
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
