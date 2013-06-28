package controllers

import play.api.mvc.Controller
import controllers.users.Users
import play.api.data.Form
import play.api.data.Forms._
import models.site.dao.SiteDao

case class SiteFormData(sid:Option[Long],cid:Int,title:String,pic:String,intro:String,tags:String)

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
  /* 小站编辑创建 */
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
          SiteDao.updateSite(site.sid.get,site.cid,site.title,site.pic,site.intro,site.tags)
        }

         Ok(views.html.sites.addSuccess(user))
      }
    )

  }

  /* 小站  */
  def site(id:Long) = Users.UserAction { user => implicit request =>
    Ok(views.html.sites.site(user))
  }

  /* 小站 帖子 编辑创建 */
  def editPost(id:Long) = Users.UserAction { user => implicit request =>
    Ok(views.html.sites.editPost(user))
  }
  /* 小站帖子 */
  def post(pid:Long)= Users.UserAction { user => implicit request =>
    Ok(views.html.sites.post(user))
  //  Ok(views.html.sites.menu(user))
  }

}
