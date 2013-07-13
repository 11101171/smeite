package controllers

import play.api.mvc.Controller
import controllers.users.Users
import play.api.data.Form
import play.api.data.Forms._
import models.site.dao.SiteDao

case class SiteFormData(sid:Option[Long],cid:Int,title:String,pic:String,intro:String,tags:String)
case  class PostFormData(pid:Option[Long],sid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int, extraAttr1:Option[String], extraAttr2:Option[String], extraAttr3:Option[String], extraAttr4:Option[String], extraAttr5:Option[String], extraAttr6:Option[String])
object Sites extends Controller {
  val siteFormData =Form(
    mapping(
      "sid"->optional(longNumber),
      "cid" ->number,
      "title" -> nonEmptyText,
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

  /* 小镇编辑创建 */
  def editSite(sid:Long) = Users.UserAction { user => implicit request =>
    if (sid==0) Ok(views.html.sites.editSite(user,siteFormData))
    else {
      val site =SiteDao.findSiteById(sid)
      if(site.isEmpty) Ok(views.html.sites.editSite(user,siteFormData))
      else  Ok(views.html.sites.editSite(user,siteFormData.fill(SiteFormData(site.get.id,site.get.cid,site.get.title,site.get.pic,site.get.intro,site.get.tags))))
    }

  }
  def saveSite = Users.UserAction {user => implicit request =>
    siteFormData.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.sites.editSite(user,formWithErrors)),
      site => {
        if(site.sid.isEmpty){
          SiteDao.addSite(user.get.id.get,site.cid,site.title,site.pic,site.intro,site.tags)
        }else{
          SiteDao.modifySite(site.sid.get,site.cid,site.title,site.pic,site.intro,site.tags)
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
        Ok(views.html.sites.site(user, site.get,posts,id,s))
      }
  }


  /* 小镇 帖子 编辑创建 */
  def editPost(pid:Long,sid:Long) = Users.UserAction { user => implicit request =>
    if (pid==0) Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(None,sid,-1,"",None,"",None,0,None,None,None,None,None,None))))
  else{
      val post = SiteDao.findPostById(pid)
      if(post.isEmpty)Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(None,sid,-1,"",None,"",None,0,None,None,None,None,None,None))))
     else  Ok(views.html.sites.editPost(user,postFormData.fill(PostFormData(post.get.id,post.get.sid,post.get.cid,post.get.title,post.get.pic,post.get.content,post.get.tags,post.get.status,post.get.extraAttr1,post.get.extraAttr2,post.get.extraAttr3,post.get.extraAttr4,post.get.extraAttr5,post.get.extraAttr6))))
    }
  }
  /* 小镇 帖子 保存帖子 */
  def savePost = Users.UserAction {user => implicit request =>
    postFormData.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.sites.editPost(user,formWithErrors)),
      post => {
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
      Ok(views.html.sites.post(user, post))

  }

}
