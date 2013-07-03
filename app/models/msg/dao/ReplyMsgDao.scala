package models.msg.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{ReplyMsg, ReplyMsgs}
import models.Page

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object ReplyMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addMsg( replierId:Long,replierName:String,replyType:Int,thirdId:Long,content:String,ownerId:Long):Long = database.withSession {  implicit session:Session =>
    ReplyMsgs.autoInc2.insert(replierId,replierName,replyType,thirdId,content,ownerId)
}
  def findMsgByLovedId(ownerId:Long,currentPage:Int,pageSize:Int):Page[ReplyMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(ReplyMsgs.filter(_.ownerId === ownerId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-ReplyMsgs.filter(_.ownerId === ownerId).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[ReplyMsg]=  q.list()
    Page[ReplyMsg](msgs,currentPage,totalPages)
  }
  def findAll(currentPage:Int,pageSize:Int):Page[ReplyMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(ReplyMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-ReplyMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[ReplyMsg]=  q.list()
    Page[ReplyMsg](msgs,currentPage,totalPages)
  }
}
