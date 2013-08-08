package models.msg.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{AtMsg, AtMsgs}
import models.Page
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 上午9:25
 */
object AtMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addMsg( senderId:Long,senderName:String,content:String,receiverId:Long,receiverName:String):Long = database.withSession {  implicit session:Session =>
      AtMsgs.autoInc2.insert(senderId,senderName,content,receiverId,receiverName)
  }

  def deleteMsg(id:Long) = database.withSession {  implicit session:Session =>
    (for(c <- AtMsgs if c.id ===id)yield c).delete
  }

  def countAtMsg = database.withSession{implicit session:Session =>
    Query(AtMsgs.length).first()
  }
  def countAtMsg(time:Timestamp)=database.withSession{implicit session:Session =>
    Query(AtMsgs.filter(_.addTime > time ).length).first()
  }

  def findAll(currentPage:Int,pageSize:Int):Page[AtMsg]=database.withSession {  implicit session:Session =>
    val totalRows=Query(AtMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-AtMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }

  def findMsgBySender(senderId:Long,currentPage:Int,pageSize:Int):Page[AtMsg]=database.withSession {  implicit session:Session =>
    val totalRows=Query(AtMsgs.filter(_.senderId === senderId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-AtMsgs.filter(_.senderId === senderId ).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }
  def findMsgByReceiver(receiverId:Long,currentPage:Int,pageSize:Int):Page[AtMsg]=database.withSession {  implicit session:Session =>
    val totalRows=Query(AtMsgs.filter(_.receiverId === receiverId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-AtMsgs.filter(_.receiverId === receiverId ).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }
  }
