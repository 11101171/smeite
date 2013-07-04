package models.msg.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{SystemMsgReceivers, SystemMsg, SystemMsgs}
import models.Page

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
   def updateMsg(id:Long,title:String,content:String) = database.withSession {  implicit session:Session =>
     (for(c <- SystemMsgs if c.id === id )yield c.title ~ c.content).update((title,content))
   }
  def deleteMsg(id:Long) = database.withSession {  implicit session:Session =>
    (for(c <- SystemMsgs if c.id === id) yield  c).delete
  }
  def updateMsgStatus(id:Long,status:Int) = database.withSession {  implicit session:Session =>
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
  def addMsgReceiver(msgId:Long,receiverId:Long,receiverName:String):Long = database.withSession {  implicit session:Session =>
      SystemMsgReceivers.autoId2.insert(msgId,receiverId,receiverName)
  }

  def updateMsgReceiverStatus(id:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers if c.id === id )yield c.status ).update(status)
  }

  def updateMsgReceiverStatus(msgId:Long,receiverId:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c.status ).update(status)
  }

  def deleteMsgReceiver(id:Long) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers if c.id === id  )yield c).delete
  }

  def deleteMsgReceiver(msgId:Long,receiverId:Long) = database.withSession {  implicit session:Session =>
    (for( c<-SystemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c ).delete
  }



}
