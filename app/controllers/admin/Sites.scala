package controllers.admin
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.site.dao.SiteDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 下午9:09
 */
object Sites extends Controller {

  def siteList(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SiteDao.findAll(currentPage,50)
    Ok(views.html.admin.sites.siteList(manager,page))
  }

  def postList(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SiteDao.findAll(currentPage,50)
    Ok(views.html.admin.sites.siteList(manager,page))
  }
}
