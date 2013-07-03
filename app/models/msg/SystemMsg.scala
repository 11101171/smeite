package models.msg

import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

case class SystemMsg(
                 id: Option[Long],
                 title:String,
                 content: String,
                 status:Int,
                 addTime:Option[Timestamp]
                 )
object SystemMsgs extends Table[SystemMsg]("system_msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def title =column[String]("title")
  def content = column[String]("content")
  def status = column[Int]("status")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~   title ~ content  ~ status  ~ addTime.? <>(SystemMsg, SystemMsg.unapply _)
  def autoId =id.?  ~ title  ~ content  ~ status  ~ addTime.? <>(SystemMsg, SystemMsg.unapply _) returning id
  def autoId2 = title ~ content  returning id
}
