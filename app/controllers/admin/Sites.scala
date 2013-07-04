package controllers.admin
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.site.dao.SiteDao
import java.sql.{Date, Timestamp}

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 下午9:09
 */

case class SiteFilterFormData(uid:Option[Long],title:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])

case class SiteBatchFormData(action:Int,ids:Seq[Long])

object Sites extends Controller {

  val siteFilterForm =Form(
    mapping(
      "uid"->optional(longNumber),
      "title"->optional(text),
      "cid"->optional(number),
      "status"->optional(number),
      "startTime" -> optional(sqlDate("yyyy-MM-dd")),
      "endTime"-> optional(sqlDate("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(SiteFilterFormData.apply)(SiteFilterFormData.unapply)
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



  def postList(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
    val page=SiteDao.findAllPosts(currentPage,50)
    Ok(views.html.admin.sites.postList(manager,page))
  }
}
