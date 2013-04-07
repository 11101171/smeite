package models.msg

import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import java.sql.{ Timestamp}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:16
 * ***********************
 * description:用于类的说明
 */

case class Msg (
            id: Option[Long],
            title: String,
            content: String,
            senderId: Long,
            senderName:String,
            checkState:Int,
            addTime:Option[Timestamp]
            )

object Msgs extends Table[Msg]("msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def title = column[String]("title")
  def content = column[String]("content")
  def senderId =column[Long]("sender_id")
  def senderName =column[String]("sender_name")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = id.?  ~ title ~ content ~ senderId ~ senderName ~ checkState  ~ addTime.? <>(Msg, Msg.unapply _)
  def autoId =id.?  ~ title ~ content ~ senderId ~ senderName ~ checkState  ~ addTime.? <>(Msg, Msg.unapply _) returning id
  def autoId2 =title ~ content ~ senderId ~ senderName  returning id
}

object MsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def testInsert(msg:Msg)=database.withSession {  implicit session:Session =>
      Msgs.insert(msg)
  }
  def testAutoIdInsert(msg:Msg)=database.withSession {  implicit session:Session =>
    Msgs.autoId.insert(msg)
  }
  def testAutoId2(title:String,content:String,senderId:Long,senderName:String  )=database.withSession {  implicit session:Session =>
    Msgs.autoId2.insert(title,content,senderId,senderName)
  }

  def testCount = database.withSession {  implicit session:Session =>
   val q =Query(Msgs.length)
   //println("the count sql : "+q.selectStatement)
    q.first
  }
  def testCount2 = database.withSession {  implicit session:Session =>
  val q2 =(for (c<-Msgs )yield c.senderName)
    q2.first
  }
}


