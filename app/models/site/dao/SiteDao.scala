package models.site.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.site._
import java.sql.{Date, Timestamp}
import models.Page
import models.user.{User,Users}
import models.site.Site
import models.Page
import models.user.User
import models.site.Post

object SiteDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* add site */
  def addSite(uid:Long,cid:Int,title:String,pic:String,intro:String,tags:String) = database.withSession {  implicit session:Session =>
    val id  = Sites.autoInc.insert(uid,cid,title,pic,intro,tags,new Timestamp(System.currentTimeMillis()))
    addSiteMember(id,uid,1)
    id
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

    /* ******************************* site post **************************************** */

    /*  添加新帖子 */
   def addPost(uid:Long,sid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int,extraAttr1:Option[String],extraAttr2:Option[String],extraAttr3:Option[String],extraAttr4:Option[String],extraAttr5:Option[String],extraAttr6:Option[String]) =  database.withSession {  implicit session:Session =>
         Posts.autoInc2.insert(uid,sid,cid,title,pic,content,tags,status,extraAttr1,extraAttr2,extraAttr3,extraAttr4,extraAttr5,extraAttr6)
    }
     /* 修改 帖子 */
  def modifyPost(pid:Long,cid:Int,title:String,pic:Option[String],content:String,tags:Option[String],status:Int,extraAttr1:Option[String],extraAttr2:Option[String],extraAttr3:Option[String],extraAttr4:Option[String],extraAttr5:Option[String],extraAttr6:Option[String]) =  database.withSession {  implicit session:Session =>
       (for( c<- Posts if c.id === pid )yield c.cid~c.title~c.pic~c.content~c.tags~c.status~c.extraAttr1~c.extraAttr2~c.extraAttr3~c.extraAttr4~c.extraAttr5~c.extraAttr6).update((cid,title,pic.getOrElse(""),content,tags.getOrElse(""),status,extraAttr1.getOrElse(""),extraAttr2.getOrElse(""),extraAttr3.getOrElse(""),extraAttr4.getOrElse(""),extraAttr5.getOrElse(""),extraAttr6.getOrElse("")))
     }
  /* 修改帖子的状态 */
  def modifyPostStatus(id:Long,status:Int)= database.withSession {  implicit session:Session =>
    ( for( c <- Posts if c.id === id)yield c.status ).update(status)
  }
  def deletePost(id:Long) = database.withSession {  implicit session:Session =>
    ( for(c<-Posts if c.id === id)yield c ).delete
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

  /* 筛选 sites */
  def filterPosts(uid:Option[Long],sid:Option[Long],title:Option[String],cid:Option[Int],status:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Int,pageSize:Int):Page[Post] =database.withSession {  implicit session:Session =>

    var query =for(c<-Posts )yield c
    if(!uid.isEmpty) query = query.filter(_.uid === uid)
    if(!sid.isEmpty) query = query.filter(_.sid === sid)
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!cid.isEmpty) query = query.filter(_.cid === cid)
    if(!status.isEmpty) query = query.filter(_.status === status)
    if(!startDate.isEmpty) query = query.filter(_.addTime > new Timestamp(startDate.get.getTime) )
    if(!endDate.isEmpty) query = query.filter(_.addTime <  new Timestamp(endDate.get.getTime) )
    val totalRows=query.list().length
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val posts:List[Post]=  query.drop(startRow).take(pageSize).list()
    Page[Post](posts,currentPage,totalPages)
  }


  /* *************************** site member *************************************** */

  /* 添加小镇居民 */
  def addSiteMember(sid:Long,uid:Long,duty:Int)  = database.withSession {  implicit session:Session =>
       SiteSQLDao.updateMemberNum(sid,1)
       SiteMembers.autoInc2.insert(sid,uid,duty)
  }
  /* 删除小镇的居民 */
  def deleteSiteMember(sid:Long,uid:Long)   = database.withSession {  implicit session:Session =>
    SiteSQLDao.updateMemberNum(sid,-1)
    (for (c <- SiteMembers.filter(_.sid === sid).filter(_.uid === uid)) yield c).delete
  }
  /* 检查用户是否存在 */
  def checkSiteMember(sid:Long,uid:Long)  = database.withSession {  implicit session:Session =>
    (for (c <- SiteMembers.filter(_.sid === sid).filter(_.uid === uid)) yield c).firstOption
  }
  /* 统计小站的居民数 */
  def countSiteMember(sid:Long)  = database.withSession{  implicit session:Session =>
    Query(SiteMembers.filter(_.sid === sid).length).first
  }
  /* 修改居民的职位 */
   def modifySiteMemberDuty(sid:Long,uid:Long,duty:Int) = database.withSession{  implicit session:Session =>
    (for (c <- SiteMembers.filter(_.sid === sid).filter(_.uid === uid)) yield c.duty ).update(duty)
  }

  /* ****************************************** site album ****************************************** */
    /* 添加一个小镇 的 相册 */
  def addSiteAlbum(sid:Long,title:String,isTop:Int )  = database.withSession{  implicit session:Session =>
     SiteAlbums.autoInc2.insert(sid,title,isTop)
  }

  /* ****************************************** site album pic ****************************************** */
 /*  添加小镇 图片 */
   def addSiteAlbumPic(sid:Long,aid:Option[Long],intro:Option[String],pic:String,isTop:Int)   = database.withSession{  implicit session:Session =>
    SiteAlbumPics.autoInc2.insert(sid,aid,intro,pic,isTop)
  }
  /* 添加小镇 图片 */
  def addSitePic(sid:Long,pic:String) = database.withSession{  implicit session:Session =>
    SiteAlbumPics.autoInc3.insert(sid,pic)
  }

  /* 删除小镇的图片 */
  def deleteSitePic(id:Long) = database.withSession{  implicit session:Session =>
    (for( c <- SiteAlbumPics.filter(_.id === id) )yield c).delete
  }

}
