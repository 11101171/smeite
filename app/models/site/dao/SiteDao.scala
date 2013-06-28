package models.site.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.site.{Sites,Site}
import java.sql.Timestamp

object SiteDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* add site */
  def addSite(uid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
         Sites.autoInc.insert(uid,cid,title,pic,intro,tags)
  }

  def updateSite(sid:Long,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
    (for(s <- Sites if s.id === sid )yield s.title~s.pic~s.intro~s.tags).update((title,pic,intro,tags))
  }

  def modifySite(sid:Long,status:Int) = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s.status ).update(status)
  }

  /* 查找 site by id */
  def findSiteById(sid:Long):Option[Site] = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s ).firstOption
  }
}
