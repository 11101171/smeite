package models.forum.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.forum._
import models.Page
import  models.user._
import models.user.dao.{UserSQLDao, UserDao}
import utils.ShiDouConfig
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-13下午12:35
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object TopicDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addTopic(topic:Topic):Long =database.withSession {  implicit session:Session =>
   val id = Topics.autoInc.insert(topic)     
   /*保存用户动作*/
    UserDao.addTrend(UserTrend(None,topic.uid,"创建了话题",id,"/forum/view/"+id,topic.title,None))
   /* 用户分享一个商品 获得一个食豆 */
   UserSQLDao.updateShiDou(topic.uid,ShiDouConfig.postTopicShiDou)
	id
  }
  def addTopic(uid:Long,uname:String,title:String,content:String,groupId:Int,typeId:Int)=database.withSession {  implicit session:Session =>
  val id = Topics.insert(uid,uname,title,content,groupId,typeId)
  /*保存用户动作*/
    UserDao.addTrend(UserTrend(None,uid,"创建了话题",id,"/forum/view/"+id,title,None))
  /* 用户分享一个商品 获得一个食豆 */
  UserSQLDao.updateShiDou(uid,ShiDouConfig.postTopicShiDou)

	id
  }

  /*删除topic的同时，需要把topic 下的reply 给删除*/
  def deleteTopic(id:Long)=database.withSession {  implicit session:Session =>
    (for(c<-Topics if c.id === id)yield(c)).delete
    (for(c<-TopicReplies  if c.topicId === id)yield(c)).delete
  }
  def modifyTopic(topic:Topic)= database.withSession{ implicit session:Session =>
    (for(c<-Topics if c.id === topic.id)yield(c)).update(topic)
  }
  def modifyTopicCheckState(id:Long,checkState:Int)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield (c.checkState)).update(checkState)
  }
  def modifyTopicTop(id:Long,isTop:Boolean)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield (c.isTop)).update(isTop)
  }
  def modifyTopicBest(id:Long,isBest:Boolean)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield (c.isBest)).update(isBest)
  }
  def countTopic:Int=database.withSession{implicit session:Session =>
    Query(Topics.length).first()
  }
  /* 根据group 类型 ，分页显示*/
  def findTopics(groupId:Int,currentPage: Int, pageSize: Int): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.groupId === groupId).filter(_.checkState === 1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize) if c.groupId===groupId  if c.checkState === 1 ) yield(c)
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages);
  }

  /*根据user 类型 分页显示*/
  def  findUserTopics(uid:Long,currentPage: Int = 1, pageSize: Int = 10): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize) if c.uid===uid  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages);
  }


  /*查找*/
  def findAll(currentPage: Int , pageSize: Int): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages);
  }
  /*search for 前端的square的 forum eatLocal*/
  def search(typeId:Int,groupId:Int,text:String, currentPage: Int = 1, pageSize: Int = 10) = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.groupId===groupId).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.sortBy(c=>(c.isBest,c.addTime,c.replyNum.desc)).drop(startRow).take(pageSize) if c.groupId===groupId   if c.title.like("%"+text+"%") if c.checkState ===1   ) yield(c)
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages);

  }

  def findById(id:Long):Option[Topic] =  database.withSession {  implicit session:Session =>
    (for(c<-Topics if c.id ===id)yield(c)).firstOption
  }
  /* find byid with user*/
  def findByIdWithUser(topicId:Long)  =  database.withSession {  implicit session:Session =>

    (for{
      c<-Topics.sortBy(_.addTime desc)
      u<-Users
      if c.id === topicId
      if c.uid === u.id
    }yield(u,c)).firstOption
  }



  /*topic 推荐*/
  def recommendTopics(nums:Int):List[(Long, String, String, Long, String, Int, Int, Int)] =database.withSession{implicit session:Session =>
    val query = for{
      c<-Topics.sortBy(_.addTime)
      u<-Users
      if c.uid===u.id
 }yield(u.id~u.name~u.pic~c.id~c.title~c.replyNum~c.loveNum~c.hotIndex)
    //println(query.selectStatement)
    query.take(nums).list
  }

  /*topic 推荐*/
  def recommendTopics(nums:Int,groupId:Int,typeId:Int):List[(Long, String, String, Long, String, Int, Int, Int)] =database.withSession{implicit session:Session =>
    val query = for{
      c<-Topics.sortBy(_.addTime)
      u<-Users
      if c.uid===u.id
      if c.typeId===typeId
      if c.groupId===groupId
    }yield(u.id~u.name~u.pic~c.id~c.title~c.replyNum~c.loveNum~c.hotIndex)
    //println(query.selectStatement)
    query.take(nums).list
  }

  /* topic reply */
  def addReply(reply:TopicReply) =database.withSession {  implicit session:Session =>
    TopicSQLDao.updateReplyNum(reply.topicId)
    TopicReplies.autoInc.insert(reply)

  }
  def addReply(uid:Long,uname:String,topicId:Long,quoteReply:Option[String],content:String,checkState:Int) =database.withSession {  implicit session:Session =>
    TopicSQLDao.updateReplyNum(topicId)
    TopicReplies.insert(uid,uname,topicId,quoteReply,content,checkState)

  }
  def deleteReply(id:Long)=database.withSession {  implicit session:Session =>
    (for(c<-TopicReplies if c.id === id)yield(c)).delete
  }

  def modifyReplyCheckState(id:Long,checkState:Int)=database.withSession{implicit session:Session =>
    (for (c<-TopicReplies if c.id === id)yield (c.checkState)).update(checkState)
  }

  /*根据topic分类 分页显示*/
  def findReplies(topicId:Long,currentPage: Int, pageSize: Int): Page[TopicReply] = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicReplies.filter(_.topicId === topicId).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TopicReplies.drop(startRow).take(pageSize) if c.topicId===topicId  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val replies:List[TopicReply]=  q.list()
    Page[TopicReply](replies,currentPage,totalPages);
  }

  def findTopicReplies(topicId:Long,currentPage:Int,pageSize:Int):Page[(String,String,Long,Long,Option[String],String,Int,Timestamp)]  = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicReplies.filter(_.topicId === topicId).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-TopicReplies
      u<-Users
     if c.uid === u.id
     if c.topicId===topicId
     } yield(u.name,u.pic,c.id,c.uid,c.quoteReply.?,c.content,c.checkState,c.addTime)
    val replies:List[(String,String,Long,Long,Option[String],String,Int,Timestamp)]=  q.sortBy(_._8 desc).drop(startRow).take(pageSize).list()
    Page[(String,String,Long,Long,Option[String],String,Int,Timestamp)](replies,currentPage,totalPages);

  }

  def findAllReplies(currentPage: Int, pageSize: Int): Page[TopicReply] = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicReplies.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TopicReplies.drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val replies:List[TopicReply]=  q.list()
    Page[TopicReply](replies,currentPage,totalPages);

  }

  def filterTopics(name:Option[String],checkState:Option[Int],groupId:Option[Int],typeId:Option[Int],isBest:Option[Boolean],isTop:Option[Boolean],currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
     var query = for(c<-Topics)yield c
     if(!name.isEmpty) query = query.filter(_.title like "%"+name.get+"%")
     if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
     if(!groupId.isEmpty) query = query.filter(_.groupId === groupId.get)
     if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
     if(!isTop.isEmpty) query = query.filter(_.isTop === isTop.get)
     if(!isBest.isEmpty) query = query.filter(_.isBest === isBest.get)
    query = query.sortBy(_.id desc)
     //println("sql " +query.selectStatement)
     val totalRows=query.list().length
     val totalPages=((totalRows + pageSize - 1) / pageSize);
     val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val topics:List[Topic]= query.drop(startRow).take(pageSize).list()
     Page[Topic](topics,currentPage,totalPages);
  }



  def filterReplies(checkState:Option[Int],currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
       var query = for(c<-TopicReplies)yield c
       if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
       query = query.sortBy(_.id desc)
       //println("sql " +query.selectStatement)
       val totalRows=query.list().length
       val totalPages=((totalRows + pageSize - 1) / pageSize);
       val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
       val replies:List[TopicReply]= query.drop(startRow).take(pageSize).list()
       Page[TopicReply](replies,currentPage,totalPages);
  }
}
