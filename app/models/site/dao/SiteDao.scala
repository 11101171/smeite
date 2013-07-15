package models.site.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.site.{Post, Posts, Sites, Site}
import java.sql.{Date, Timestamp}
import models.Page
import models.user.{User,Users}

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
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val msgs:List[Site]=  query.drop(startRow).take(pageSize).list()
    Page[Site](msgs,currentPage,totalPages)
  }

  /* find sites for siteList.scala.html
   *   cid  -1 all
   *   sortBy 1 最新 sortBy 2
    * */
  def findSites(cid:Int,sortBy:Int,currentPage:Int,pageSize:Int):Page[Site] = database.withSession {  implicit session:Session =>
      var query =for(c<-Sites.filter(_.status === 2))yield c
     if(cid != -1) query = query.filter(_.cid === cid)
    if(sortBy == 1) query = query.sortBy(_.addTime desc)
    if(sortBy == 2) query = query.sortBy(_.memberNum desc)
    val totalRows=query.list().length
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val sites:List[Site]=  query.drop(startRow).take(pageSize).list()
    Page[Site](sites,currentPage,totalPages)
  }



    /*  添加新帖子 */
   def addPost(uid:Long,sid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int,extraAttr1:Option[String],extraAttr2:Option[String],extraAttr3:Option[String],extraAttr4:Option[String],extraAttr5:Option[String],extraAttr6:Option[String]) =  database.withSession {  implicit session:Session =>
         Posts.autoInc2.insert(uid,sid,cid,title,pic,content,tags,status,extraAttr1,extraAttr2,extraAttr3,extraAttr4,extraAttr5,extraAttr6)
    }
     /* 修改 帖子 */
  def modifyPost(pid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int,extraAttr1:Option[String],extraAttr2:Option[String],extraAttr3:Option[String],extraAttr4:Option[String],extraAttr5:Option[String],extraAttr6:Option[String]) =  database.withSession {  implicit session:Session =>
       (for( c<- Posts if c.id === pid )yield c.cid~c.title~c.pic~c.content~c.tags~c.status~c.extraAttr1~c.extraAttr2~c.extraAttr3~c.extraAttr4~c.extraAttr5~c.extraAttr6).update((cid,title,pic.getOrElse(""),content,tags.getOrElse(""),status,extraAttr1.getOrElse(""),extraAttr2.getOrElse(""),extraAttr3.getOrElse(""),extraAttr4.getOrElse(""),extraAttr5.getOrElse(""),extraAttr6.getOrElse("")))
     }

  def findPostById(pid:Long)  = database.withSession {  implicit session:Session =>
    (for(c <- Posts if c.id === pid)yield c ).firstOption
  }
  def findPost(pid:Long):(Post,User,Site) = database.withSession {  implicit session:Session =>
    (
      for{
        c<-Posts
        u<-Users
        s<-Sites
        if c.id === pid
        if c.uid === u.id
        if c.sid === s.id
      } yield (c,u,s)).first()
  }

   /* 根据用户查找 帖子 */
   def findPostsByUid(uid:Long,currentPage:Int,pageSize:Int):Page[Post] = database.withSession {  implicit session:Session =>
       val totalRows = Query(Posts.filter(_.uid === uid ).length).first()
     val totalPages=(totalRows + pageSize - 1) / pageSize
     /*获取分页起始行*/
     val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val q=  for(c<-Posts.filter(_.uid === uid ).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
     val msgs:List[Post]=  q.list()
     Page[Post](msgs,currentPage,totalPages)
   }

  /* 根据小镇 查找帖子 sort = 1 最新 sort:2 最热*/
  def findPostsBySid(sid:Long,sortBy:Int,currentPage:Int,pageSize:Int):Page[Post] =  database.withSession {  implicit session:Session =>
    val totalRows = Query(Posts.filter(_.sid === sid ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var query=  for{
      c<-Posts.filter(_.sid === sid )
    }yield c
  if(sortBy == 1) query = query.sortBy(_.addTime desc)
  if(sortBy == 2) query.sortBy(_.viewNum desc)
    val msgs:List[Post]=  query.drop(startRow).take(pageSize).list()
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

  /* find sites for siteList.scala.html
*   cid  -1 all
*   sortBy 1 最新 sortBy 2
 * */
  def findPosts(cid:Int,sortBy:Int,currentPage:Int,pageSize:Int):Page[Post] = database.withSession {  implicit session:Session =>
    var query =for( c<- Posts.filter(_.status === 2) )yield c
    if(cid != -1) query = query.filter(_.cid === cid)
    if(sortBy == 1) query = query.sortBy(_.addTime desc)
    if(sortBy == 1) query = query.sortBy(_.loveNum desc)
    val totalRows=query.list().length
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val posts:List[Post]=  query.drop(startRow).take(pageSize).list()
    Page[Post](posts,currentPage,totalPages)
  }


}
