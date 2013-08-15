package controllers.admin
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.site.dao.SiteDao
import java.sql.{Date, Timestamp}
import models.msg.dao.SystemMsgDao
import models.site.{PostReply, Post}
import play.api.libs.json.Json
import models.tag.dao.TagDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 下午9:09
 */

case class SiteFilterFormData(uid:Option[Long],title:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])

case class PostFilterFormData(uid:Option[Long],sid:Option[Long],title:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])

case class PostReplyFilterFormData(uid:Option[Long],pid:Option[Long],content:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])


case class SiteBatchFormData(action:Int,ids:Seq[Long])

object Sites extends Controller {

  val siteFilterForm =Form(
    mapping(
      "uid"->optional(longNumber),
      "title"->optional(text),
      "cid"->optional(number),
      "status"->optional(number),
      "startDate" -> optional(sqlDate("yyyy-MM-dd")),
      "endDate"-> optional(sqlDate("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(SiteFilterFormData.apply)(SiteFilterFormData.unapply)
  )

  val postFilterForm =Form(
    mapping(
      "uid"->optional(longNumber),
      "sid"->optional(longNumber),
      "title"->optional(text),
      "cid"->optional(number),
      "status"->optional(number),
      "startDate" -> optional(sqlDate("yyyy-MM-dd")),
      "endDate"-> optional(sqlDate("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(PostFilterFormData.apply)(PostFilterFormData.unapply)
  )

  val postReplyFilterForm =Form(
    mapping(
      "uid"->optional(longNumber),
      "pid"->optional(longNumber),
      "content"->optional(text),
      "cid"->optional(number),
      "status"->optional(number),
      "startDate" -> optional(sqlDate("yyyy-MM-dd")),
      "endDate"-> optional(sqlDate("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(PostReplyFilterFormData.apply)(PostReplyFilterFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber)
    )(SiteBatchFormData.apply)(SiteBatchFormData.unapply)
  )


  def siteList(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SiteDao.findAllSites(currentPage,50)
    Ok(views.html.admin.sites.siteList(manager,page))
  }

  def filterSites = Managers.AdminAction{ manager => implicit request =>
    siteFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      site => {
        val page=SiteDao.filterSites(site.uid,site.title,site.cid,site.status,site.startDate,site.endDate,site.currentPage.getOrElse(1),50)
        Ok(views.html.admin.sites.filterSites(manager,page,siteFilterForm.fill(site)))
      }
    )

  }
  /* 批量处理站内信 */
  def batchSites  = Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            SiteDao.modifySiteStatus(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            SiteDao.modifySiteStatus(id,1)
            SystemMsgDao.addMsgReceiver(id,SiteDao.findSiteById(id).get.uid)
          }
        }else if(batch.action==3){
          for(id<-batch.ids){
            SiteDao.deleteSite(id)
          }
        }
        Redirect(request.headers.get("REFERER").getOrElse("/admin/sites/siteList"))
      }
    )
  }



  def postList(sid:Long,currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    var page:Page[Post] = null
  if(sid==0){
    page=SiteDao.findAllPosts(currentPage,50)
  }else{
    page=SiteDao.findPostsBySid(sid,1,currentPage,50)
  }

    Ok(views.html.admin.sites.postList(manager,page))
  }

  def filterPosts = Managers.AdminAction{ manager => implicit request =>
    postFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      post => {
        val page=SiteDao.filterPosts(post.uid,post.sid,post.title,post.cid,post.status,post.startDate,post.endDate,post.currentPage.getOrElse(1),50)
        Ok(views.html.admin.sites.filterPosts(manager,page,postFilterForm.fill(post)))
      }
    )
  }

  def batchPosts = Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            SiteDao.modifyPostStatus(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            SiteDao.modifyPostStatus(id,1)

          }
        }else if(batch.action==3){
          for(id<-batch.ids){
            SiteDao.deletePost(id)
          }
        }
        Redirect(request.headers.get("REFERER").getOrElse("/admin/sites/PostList"))
      }
    )
  }

  def post(pid:Long)=Managers.AdminAction{ manager => implicit request =>
     val post = SiteDao.findPostById(pid)
     val tags = SiteDao.findPostExtraTags(pid)
    Ok(views.html.admin.sites.post(manager,post.get,tags))
  }

  /***************** post reply *********************  */
  def postReplies(pid:Long,currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    var page:Page[PostReply] = null
    if(pid==0){
      page=SiteDao.findAllPostReplies(currentPage,50)
    }else{
      page=SiteDao.findPostRepliesByPid(pid,currentPage,50)
    }

    Ok(views.html.admin.sites.postReplies(manager,page))

  }

  def filterPostReplies = Managers.AdminAction{ manager => implicit request =>
    postReplyFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      reply => {
        val page=SiteDao.filterPostReplies(reply.uid,reply.pid,reply.content,reply.cid,reply.status,reply.startDate,reply.endDate,reply.currentPage.getOrElse(1),50)
        Ok(views.html.admin.sites.filterPostReplies(manager,page,postReplyFilterForm.fill(reply)))
      }
    )
  }

  def batchPostReplies  = Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            SiteDao.modifyPostReplyStatus(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            SiteDao.modifyPostReplyStatus(id,1)

          }
        }else if(batch.action==3){
          for(id<-batch.ids){
            SiteDao.deletePostReply(id)
          }
        }
        Redirect(request.headers.get("REFERER").getOrElse("/admin/sites/PostReplies"))
      }
    )
  }

  /* post extra tag */
  def addPostExtraTags = Action(parse.json) {implicit request =>
    val pid = (request.body \ "pid").asOpt[Long]
    val tags = (request.body \ "tags").asOpt[String]
    if(tags.isEmpty){
      Ok(Json.obj("code"->"103","message"->"tags 为空"))
    }else{
      for (name <- tags.get.trim().split(" ")){
          val postExtraTag = SiteDao.findPostExtraTag(pid.get,name)
          if (postExtraTag.isEmpty) SiteDao.addPostExtraTag(pid.get,name)
      }
      Ok(Json.obj("code"->"100","message"->"success"))
    }
  }

}
