package models.msg.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{SystemMsgReceivers, SystemMsg, SystemMsgs}
import models.Page
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-2
 * Time: 下午10:43
 */
object SystemMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

   def addMsg(title:String,content:String):Long = database.withSession {  implicit session:Session =>
         SystemMsgs.autoId2.insert(title,content)
   }
   def modifyMsg(id:Long,title:String,content:String) = database.withSession {  implicit session:Session =>
     (for(c <- SystemMsgs if c.id === id )yield c.title ~ c.content).update((title,content))
   }
  def deleteMsg(id:Long) = database.withSession {  implicit session:Session =>
    (for(c <- SystemMsgs if c.id === id) yield  c).delete
  }
  def modifyMsgStatus(id:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for(c <- SystemMsgs if c.id === id)yield c.status).update(status)
  }

  def findMsg(id:Long):Option[SystemMsg] = database.withSession {  implicit session:Session =>
    (for(c <- SystemMsgs if c.id === id)yield c ).firstOption
  }

  def findAll(currentPage:Int,pageSize:Int):Page[SystemMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(SystemMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-SystemMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[SystemMsg]=  q.list()
    Page[SystemMsg](msgs,currentPage,totalPages)
  }

  def filterMsgs(title:Option[String],currentPage:Int,pageSize:Int):Page[SystemMsg] = database.withSession {  implicit session:Session =>
    var query =for(c<-SystemMsgs)yield c
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val msgs:List[SystemMsg]=  query.drop(startRow).take(pageSize).list()
    Page[SystemMsg](msgs,currentPage,totalPages);
  }


  /* msg receiver */
  def addMsgReceiver(msgId:Long,receiverId:Long):Long = database.withSession {  implicit session:Session =>
      SystemMsgReceivers.autoId2.insert(msgId,receiverId)
  }

  def modifyMsgReceiverStatus(id:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers if c.id === id )yield c.status ).update(status)
  }

  def modifyMsgReceiverStatus(msgId:Long,receiverId:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c.status ).update(status)
  }

  def deleteMsgReceiver(id:Long) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers if c.id === id  )yield c).delete
  }

  def deleteMsgReceiver(msgId:Long,receiverId:Long) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c ).delete
  }
 /* 查找所有的 system msg receiver */
  def findAllMsgReceivers(currentPage:Int,pageSize:Int):Page[(Long,Long,Int,String,String,Timestamp)] = database.withSession {  implicit session:Session =>
    val totalRows=Query(SystemMsgReceivers.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-SystemMsgReceivers
      sm <- SystemMsgs
      if sm.id === c.msgId
    }yield c.id ~ c.receiverId ~ c.status ~ sm.title ~ sm.content ~ c.addTime
    val msgs:List[(Long,Long,Int,String,String,Timestamp)]=  q.sortBy(_._1 desc).drop(startRow).take(pageSize).list()
    Page[(Long,Long,Int,String,String,Timestamp)](msgs,currentPage,totalPages)
  }

  /* 查找某个receiver 收到的 信息 */
    def findReceiverMsgs(receiverId:Long,currentPage:Int,pageSize:Int):Page[(Long,Long,Int,String,String,Timestamp)] =  database.withSession {  implicit session:Session =>
    val totalRows=Query(SystemMsgReceivers.filter(_.receiverId === receiverId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-SystemMsgReceivers
      sm <- SystemMsgs
     if c.receiverId === receiverId
      if sm.id === c.msgId
    }yield c.id ~ c.receiverId ~ c.status ~ sm.title ~ sm.content ~c.addTime
    val msgs:List[(Long,Long,Int,String,String,Timestamp)]=  q.sortBy(_._1 desc).drop(startRow).take(pageSize).list()
    Page[(Long,Long,Int,String,String,Timestamp)](msgs,currentPage,totalPages)
  }
}
