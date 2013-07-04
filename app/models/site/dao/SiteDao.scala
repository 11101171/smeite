package models.site.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.site.{Post, Posts, Sites, Site}
import java.sql.{Date, Timestamp}
import models.Page

object SiteDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* add site */
  def addSite(uid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
         Sites.autoInc.insert(uid,cid,title,pic,intro,tags)
  }

  def modifySite(sid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
    (for(s <- Sites if s.id === sid )yield s.cid~s.title~s.pic~s.intro~s.tags).update((cid,title,pic,intro,tags))
  }

  def modifySiteStatus(sid:Long,status:Int) = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s.status ).update(status)
  }

  def deleteSite(id:Long) = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === id)yield s ).delete
  }

  /* 查找 site by id */
  def findSiteById(sid:Long):Option[Site] = database.withSession {  implicit session:Session =>
    ( for(s<-Sites if s.id === sid)yield s ).firstOption
  }

  /* 查找 所有site */
  def findAllSites(currentPage:Int,pageSize:Int):Page[Site] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Sites.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Sites.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[Site]=  q.list()
    Page[Site](msgs,currentPage,totalPages)
  }

  /* 筛选 sites */
  def filterSites(uid:Option[Long],title:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Int,pageSize:Int):Page[Site] =database.withSession {  implicit session:Session =>

    var query =for(c<-Sites)yield c
    if(!uid.isEmpty) query = query.filter(_.uid === uid)
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!cid.isEmpty) query = query.filter(_.cid === cid)
    if(!status.isEmpty) query = query.filter(_.status === status)
    if(!startDate.isEmpty) query = query.filter(_.addTime > new Timestamp(startDate.get.getTime) )
    if(!endDate.isEmpty) query = query.filter(_.addTime <  new Timestamp(endDate.get.getTime) )
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val msgs:List[Site]=  query.drop(startRow).take(pageSize).list()
    Page[Site](msgs,currentPage,totalPages);
  }

    /*  添加新帖子 */
   def addPost(uid:Long,sid:Long,cid:Int,title:String,pic:String,content:String,tags:String,extraAttr1:String,extraAttr2:String,extraAttr3:String,extraAttr4:String,extraAttr5:String,extraAttr6:String) =  database.withSession {  implicit session:Session =>
         Posts.autoInc2.insert(uid,sid,cid,title,pic,content,tags,extraAttr1,extraAttr2,extraAttr3,extraAttr4,extraAttr5,extraAttr6)
    }

  def findPostById(id:Long) = database.withSession {  implicit session:Session =>
    (for(c<-Posts if c.id === id ) yield c).firstOption
  }


   def findPostsById(uid:Long,currentPage:Int,pageSize:Int):Page[Post] = database.withSession {  implicit session:Session =>
       val totalRows = Query(Posts.filter(_.uid === uid ).length).first()
     val totalPages=(totalRows + pageSize - 1) / pageSize
     /*获取分页起始行*/
     val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val q=  for(c<-Posts.filter(_.uid === uid ).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
     val msgs:List[Post]=  q.list()
     Page[Post](msgs,currentPage,totalPages)
   }

  def findAllPosts(currentPage:Int,pageSize:Int):Page[Post] = database.withSession {  implicit session:Session =>
    val totalRows = Query(Posts.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Posts.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[Post]=  q.list()
    Page[Post](msgs,currentPage,totalPages)
  }



}
