package models.site.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.site.{Posts, Sites, Site}
import java.sql.Timestamp
import models.Page

object SiteDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* add site */
  def addSite(uid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
         Sites.autoInc.insert(uid,cid,title,pic,intro,tags)
  }

  def updateSite(sid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
    (for(s <- Sites if s.id === sid )yield s.cid~s.title~s.pic~s.intro~s.tags).update((cid,title,pic,intro,tags))
  }

  def modifySite(sid:Long,status:Int) = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s.status ).update(status)
  }

  /* 查找 site by id */
  def findSiteById(sid:Long):Option[Site] = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s ).firstOption
  }

  /* 查找 所有*/
  def findAll(currentPage:Int,pageSize:Int):Page[Site] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Sites.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Sites.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[Site]=  q.list()
    Page[Site](msgs,currentPage,totalPages)
  }

    /*  添加新帖子 */
      def addPost(uid:Long,sid:Long,cid:Int,title:String,pic:String,content:String,tags:String,extraAttr1:String,extraAttr2:String,extraAttr3:String,extraAttr4:String,extraAttr5:String,extraAttr6:String) =  database.withSession {  implicit session:Session =>
         Posts.autoInc2.insert(uid,sid,cid,title,pic,content,tags,extraAttr1,extraAttr2,extraAttr3,extraAttr4,extraAttr5,extraAttr6)
    }

}
