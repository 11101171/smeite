package models.msg.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{FavorMsg, FavorMsgs}
import models.Page

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object FavorMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addMsg(loverId:Long,loverName:String,favorType:Int,thirdId:Long,content:String,lovedId:Long):Long = database.withSession {  implicit session:Session =>
    FavorMsgs.autoInc.insert(loverId,loverName,favorType,thirdId,content,lovedId)
}
  def findMsgByLovedId(lovedId:Long,currentPage:Int,pageSize:Int):Page[FavorMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(FavorMsgs.filter(_.lovedId === lovedId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-FavorMsgs.filter(_.lovedId === lovedId).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[FavorMsg]=  q.list()
    Page[FavorMsg](msgs,currentPage,totalPages)
  }

  def findAll(currentPage:Int,pageSize:Int) =  database.withSession {  implicit session:Session =>
    val totalRows=Query(FavorMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-FavorMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[FavorMsg]=  q.list()
    Page[FavorMsg](msgs,currentPage,totalPages)
  }

}
