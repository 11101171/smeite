package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.forum.dao.TopicDao

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-11
 * Time: 上午1:20
 * ***********************
 * description:用于类的说明
 */
case class TopicBatchFormData(action:Int,ids:Seq[Long],typeIds:Seq[Int],url:Option[String])
case  class TopicFilterFormData(name:Option[String],checkState:Option[Int],groupId:Option[Int],typeId:Option[Int],isBest:Option[Boolean],isTop:Option[Boolean],currentPage:Option[Int])
case  class ReplyFilterFormData(checkState:Option[Int],currentPage:Option[Int])

object Forums extends Controller {
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "typeIds"->seq(number),
      "url"->optional(text)
    )(TopicBatchFormData.apply)(TopicBatchFormData.unapply)
  )
  /*检索标签*/
  val topicFilterForm =Form(
      mapping(
        "name"->optional(text),
        "checkState"->optional(number),
        "groupId"->optional(number),
        "typeId"->optional(number),
        "isBest"->optional(boolean),
        "isTop"->optional(boolean),
        "currentPage"->optional(number)
      )(TopicFilterFormData.apply)(TopicFilterFormData.unapply)
    )
  /*检索标签*/
  val replyFilterForm =Form(
    mapping(
      "checkState"->optional(number),
      "currentPage"->optional(number)
    )(ReplyFilterFormData.apply)(ReplyFilterFormData.unapply)
  )


  def list(p:Int) = Managers.AdminAction{manager => implicit request =>
      val page=TopicDao.findAll(p,50);
      Ok(views.html.admin.forums.list(manager,page))
  }
  
  def delete(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =TopicDao.deleteTopic(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" ->"删除成功"))
    else Ok(Json.obj("code" -> "101", "message" -> "删除失败" ))
  }
  
  def check(id:Long,checkState:Int) = Managers.AdminAction{manager => implicit request =>
    val result =TopicDao.modifyTopicCheckState(id,checkState)
    if (result >0) Ok(Json.obj("code" -> "100", "message" ->"审核成功" ))
    else Ok(Json.obj("code" -> "101", "message" ->"审核不通过，请直接删除"))
  }

  def view(id:Long)= Managers.AdminAction{manager => implicit request =>
    val topic =TopicDao.findById(id)
    Ok(views.html.admin.forums.view(manager,topic.get))
  }
  
  def replies(p:Int)=  Managers.AdminAction{manager => implicit request =>
    val page=TopicDao.findAllReplies(p,50)
    Ok(views.html.admin.forums.replies(manager,page))
  }
  def deleteReply(id:Long)  = Managers.AdminAction{manager => implicit request =>
    val result =TopicDao.deleteReply(id)
    if (result >0) Ok(Json.obj("code" -> "100", "message" ->"删除成功"))
    else Ok(Json.obj("code" -> "101", "message" ->"删除失败" ))
  }

  def checkReply(id:Long,state:Int)= Managers.AdminAction{manager => implicit request =>
    val result =TopicDao.modifyReplyCheckState(id,state)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" ->"审核成功" ))
    else Ok(Json.obj("code" -> "101", "message" -> "审核不通过，请直接删除"))
  }

  /* 批量处理 */
  def batchTopics=Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            TopicDao.modifyTopicCheckState(id,1)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            TopicDao.modifyTopicCheckState(id,2)
          }
        }else if (batch.action ==2){
          for(id<-batch.ids){
            TopicDao.modifyTopicTop(id,true)
          }
        }else if (batch.action ==3){
          for(id<-batch.ids){
            TopicDao.modifyTopicTop(id,false)
          }
        }else if (batch.action ==4){
          for(id<-batch.ids){
            TopicDao.modifyTopicBest(id,true)
          }
        }else if (batch.action ==5){
          for(id<-batch.ids){
            TopicDao.modifyTopicBest(id,false)
          }
        }else if (batch.action ==6){
          for(id<-batch.ids){
            TopicDao.deleteTopic(id)
          }
        }

        Redirect(batch.url.getOrElse("/admin/forums/list"))
      }
    )
  }

  /*用户过滤*/
  def filterTopics = Managers.AdminAction{ manager => implicit request =>
    topicFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      topic => {
        val page=TopicDao.filterTopics(topic.name,topic.checkState,topic.groupId,topic.typeId,topic.isTop,topic.isBest,topic.currentPage.getOrElse(1),50);
        Ok(views.html.admin.forums.filterTopics(manager,page,topicFilterForm.fill(topic)))
      }
    )

  }

  /* 批量处理 */
  def batchReplies=Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            TopicDao.modifyReplyCheckState(id,1)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            TopicDao.modifyReplyCheckState(id,2)
          }
        }else if(batch.action==2){
          for(id<-batch.ids){
            TopicDao.deleteReply(id)
          }
        }

        Redirect(batch.url.getOrElse("/admin/forums/replies"))
      }
    )
  }

  /*用户过滤*/
  def filterReplies = Managers.AdminAction{ manager => implicit request =>
    replyFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      reply => {
        val page=TopicDao.filterReplies(reply.checkState,reply.currentPage.getOrElse(1),50);
        Ok(views.html.admin.forums.filterReplies(manager,page,replyFilterForm.fill(reply)))
      }
    )

  }



}
