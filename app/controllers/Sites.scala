package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.data.Form
import play.api.data.Forms._
import models.site.dao.SiteDao
import play.api.cache.Cache
import models.user.dao.UserDao


import play.api.Play.current

import play.api.libs.json._
import play.api.libs.functional.syntax._

import models.user.User
import java.sql.Timestamp
import org.jsoup.Jsoup

case class SiteFormData(sid:Option[Long],cid:Int,title:String,permission:Int,pic:String,intro:String,tags:String)
case  class PostFormData(pid:Option[Long],sid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int, extraAttr1:Option[String], extraAttr2:Option[String], extraAttr3:Option[String], extraAttr4:Option[String], extraAttr5:Option[String], extraAttr6:Option[String])
case class PostComment(pid:Long,cid:Int,quoteContent:Option[String],content:String)

object Sites extends Controller {
  val siteFormData =Form(
    mapping(
      "sid"->optional(longNumber),
      "cid" ->number,
      "title" -> nonEmptyText,
      "permission" ->number,
      "pic" -> nonEmptyText,
      "intro" -> nonEmptyText,
      "tags" -> text
    )(SiteFormData.apply)(SiteFormData.unapply)
  )
  val postFormData =Form(
    mapping(
      "pid"->optional(longNumber),
      "sid"->longNumber,
      "cid" ->number,
      "title" -> nonEmptyText,
      "pic" -> optional(text),
      "content" -> nonEmptyText,
      "tags" -> optional(text),
      "status" ->number,
      "extraAttr1" -> optional(text),
      "extraAttr2" -> optional(text),
      "extraAttr3" -> optional(text),
      "extraAttr4" -> optional(text),
      "extraAttr5" -> optional(text),
      "extraAttr6" -> optional(text)
    )(PostFormData.apply)(PostFormData.unapply)
  )
  implicit  val replyFormat = (
      (__ \ "pid").format[Long] and
      (__ \ "cid").format[Int] and
      (__ \ "quoteContent").format[Option[String]] and
      (__ \ "content").format[String]
    )(PostComment.apply,unlift(PostComment.unapply))

  /* 小镇编辑创建 */
  def editSite(sid:Long) = Users.UserAction { user => implicit request =>
    if (sid==0) Ok(views.html.sites.editSite(user,siteFormData))
    else {
      val site =SiteDao.findSiteById(sid)
      if(site.isEmpty) Ok(views.html.sites.editSite(user,siteFormData))
      else  Ok(views.html.sites.editSite(user,siteFormData.fill(SiteFormData(site.get.id,site.get.cid,site.get.title,site.get.permission,site.get.pic,site.get.intro,site.get.tags))))
    }

  }
  def saveSite = Users.UserAction {user => implicit request =>
    siteFormData.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.sites.editSite(user,formWithErrors)),
      site => {
        if(site.sid.isEmpty){
          SiteDao.addSite(user.get.id.get,site.cid,site.title,site.permission,site.pic,site.intro,site.tags)
        }else{
          SiteDao.modifySite(site.sid.get,site.cid,site.title,site.permission,site.pic,site.intro,site.tags)
        }
         Ok(views.html.sites.addSuccess(user))
      }
    )

  }

  /* 小镇  */
  def site(id: Long,s:Int,p:Int) = Users.UserAction {  user => implicit request =>
      val site = SiteDao.findSiteById(id)
      if (site.isEmpty) {
        Ok(" site is not existed  todo todo ")
      } else {
       val posts = SiteDao.findPostsBySid(id,s,p,20)

        val sitePic = SiteDao.findSitePic(id)
        val siteVideo = SiteDao.findSiteVideo(id)
        Ok(views.html.sites.site(user, site.get,posts,id,s,sitePic,siteVideo))
      }
  }


  /*用户点击喜欢操作*/
  def addFollow  = Action(parse.json) { implicit request =>
    val user:Option[User] =request.session.get("user").map( u=> Cache.getOrElse[User](u){
      UserDao.findById(u.toLong)
    })
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"你还没有登录" ))
    else {
      val siteId = (request.body \ "siteId").asOpt[Long]
      if(siteId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        val siteMember=SiteDao.checkSiteMember(siteId.get,user.get.id.get)
        if(!siteMember.isEmpty) Ok(Json.obj("code" -> "103", "message" ->"你已经喜欢了"))
        else {
          SiteDao.addSiteMember(siteId.get,user.get.id.get,0)
          Ok(Json.obj("code" -> "100", "message" ->"成功"))
        }

      }

    }
  }

  def removeFollow = Action(parse.json) {implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"你还没有登录"))
    else {
      val siteId = (request.body \ "siteId").asOpt[Long]
      if(siteId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        SiteDao.deleteSiteMember(siteId.get,user.get.id.get)
        Ok(Json.obj("code" -> "100", "message" ->"成功"))
      }


    }
  }

  def checkSiteLoveState = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if(user.isEmpty)  Ok(Json.obj("code" -> "300","message" -> "亲，你还没有登录呢" ))
    else{
      val siteId=(request.body \ "siteId").asOpt[Long];
      if (siteId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        val siteMember=SiteDao.checkSiteMember(siteId.get,user.get.id.get)
        if(!siteMember.isEmpty)  Ok(Json.obj("code" -> "100","message" -> "已关注" ))
        else Ok(Json.obj("code" -> "101","message" -> "未关注" ))
      }

    }
  }
  def checkSitePermission = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if(user.isEmpty)  Ok(Json.obj("code" -> "300","message" -> "亲，你还没有登录呢" ))
    else{
      val siteId=(request.body \ "siteId").asOpt[Long];
      if (siteId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        val permission=SiteDao.getSitePermission(siteId.get)
        if(permission == 0)  Ok(Json.obj("code" -> "100","message" -> "所有用户" ))
        else Ok(Json.obj("code" -> "103","message" -> "居民用户" ))
      }

    }
  }

  /* ************************ front site post **************************************** */


  /* 小镇 帖子 编辑创建 */
  def editPost(pid:Long,sid:Long) = Users.UserAction { user => implicit request =>
    if (pid==0) Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(None,sid,2,"",None,"",None,0,None,None,None,None,None,None))))
  else{
      val post = SiteDao.findPostById(pid)
      if(post.isEmpty)Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(None,sid,2,"",None,"",None,0,None,None,None,None,None,None))))
     else  Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(post.get.id,post.get.sid,post.get.cid,post.get.title,post.get.pic,post.get.content,post.get.tags,post.get.status,post.get.extraAttr1,post.get.extraAttr2,post.get.extraAttr3,post.get.extraAttr4,post.get.extraAttr5,post.get.extraAttr6))))
    }
  }
  /* 小镇 帖子 保存帖子 */
  def savePost = Users.UserAction {user => implicit request =>
    postFormData.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.sites.editPost(user,formWithErrors)),
      post => {
        /* 处理content 中的 img  flash ，goods */
        val  doc =Jsoup.parseBodyFragment(post.content)
        val images = doc.select("img")
        val videos = doc.select("embed")
        /* 保存 images */
        val it = images.iterator()
        while(it.hasNext){
          val pic = it.next.attr("src")
        if(SiteDao.checkSitePic(post.sid,pic).isEmpty){
          SiteDao.addSitePic(post.sid,pic)
        }

        }
        /* 保存视频 */
        val vt = videos.iterator()
        while(vt.hasNext){
     //  println( " flash    " + vt.next.attr("src"))
          val video = vt.next.attr("src")
          if(SiteDao.checkSiteVideo(post.sid,video).isEmpty){
            SiteDao.addSiteVideo(post.sid,video)
          }
        }
        var id:Long=0L
        if(post.pid.isEmpty){
        id = SiteDao.addPost(user.get.id.get,post.sid,post.cid,post.title,post.pic,post.content,post.tags,post.status,post.extraAttr1,post.extraAttr2,post.extraAttr3,post.extraAttr4,post.extraAttr5,post.extraAttr6)
        }else{
          SiteDao.modifyPost(post.pid.get,post.cid,post.title,post.pic,post.content,post.tags,post.status,post.extraAttr1,post.extraAttr2,post.extraAttr3,post.extraAttr4,post.extraAttr5,post.extraAttr6)
         id = post.pid.get
        }

        Redirect(controllers.routes.Sites.post(id))
      }
    )
  }

  /* 小镇帖子 */
  def post(pid:Long)= Users.UserAction { user => implicit request =>
    val post = SiteDao.findPost(pid)
     val userPosts = SiteDao.findPostsByUid(post._2.id.get,5)
     val userSites =SiteDao.findJoinedSites(post._2.id.get,5)
     val sitePosts = SiteDao.findPostsBySid(post._3.id.get,5)
     val siteMembers = SiteDao.findSiteMembers(post._3.id.get,5)
      Ok(views.html.sites.post(user, post,userPosts,userSites,sitePosts,siteMembers))

  }

  /* 小镇回复 */
  def addComment =  Action(parse.json) {  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty) Ok(Json.obj("code" -> "300", "message" -> "你还没有登录"))

  val comment =Json.fromJson[PostComment](request.body).get
    if(comment.content ==""){
      Ok(Json.obj("code" -> "104", "message" -> "内容不能为空"))
    }else {
      SiteDao.addPostReply(user.get.id.get,comment.pid,comment.cid,comment.quoteContent,comment.content)
      Ok(Json.obj("code" -> "100", "message" -> "success"))
    }

  }

  /* 获取 post 评论*/
  def getComments(pid:Long,p:Int)=Action{    implicit  request =>
    val page = SiteDao.findPostReplies(pid,p,10)
    Ok(views.html.sites.getComments(page,pid))
  }

}
