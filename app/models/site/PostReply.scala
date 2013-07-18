package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/*
*
*
* */

case class PostReply (
                          id: Option[Long],
                          uid:Long,
                          pid:Long,
                          replyType:Int,
                          quoteReply:Option[String],
                          content:String,
                          checkState:Int,
                          addTime:Option[Timestamp]
                          )

object PostReplies extends Table[PostReply]("post_reply") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def pid = column[Long]("pid")
  def replyType =column[Int]("reply_type")
  def quoteReply = column[String]("quote_reply")
  def content = column[String]("content")
  def checkState =column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ pid ~ replyType  ~ quoteReply.?  ~ content ~ checkState ~ addTime.?  <>(PostReply, PostReply.unapply _)
  def autoInc  = id.? ~ uid  ~ pid ~ replyType  ~ quoteReply.?  ~ content ~ checkState ~ addTime.?  <>(PostReply, PostReply.unapply _) returning id



}
