package models.msg

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
 /*
 * reply type 0 帖子回复 1 宝贝回复 2 主题回复
 * */
case class ReplyMsg(
                     id: Option[Long],
                     replierId:Long,
                     replierName: String,
                     replyType:Int,
                     thirdId:Long,
                     content:String,
                     ownerId:Long,
                     addTime:Option[Timestamp]
                     )

object ReplyMsgs extends Table[ReplyMsg]("reply_msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def replierId =column[Long]("replier_id")
  def replierName =column[String]("replier_name")
  def replyType = column[Int]("reply_type")
  def thirdId =column[Long]("third_id")
  def content = column[String]("content")
  def ownerId = column[Long]("owner_id")
  def addTime = column[Timestamp]("add_time")
  def * = id.? ~ replierId ~ replierName ~ replyType ~ thirdId  ~ content ~ ownerId   ~ addTime.? <>(ReplyMsg, ReplyMsg.unapply _)
  def autoInc =id.? ~ replierId ~ replierName ~ replyType ~ thirdId  ~ content ~ ownerId   ~ addTime.? <>(ReplyMsg, ReplyMsg.unapply _) returning id
  def autoInc2 =replierId ~ replierName ~ replyType ~ thirdId  ~ content ~ ownerId   returning id
}
