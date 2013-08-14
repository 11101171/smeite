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
                          cid:Int,
                          quoteContent:Option[String],
                          content:String,
                          status:Int,
                          addTime:Option[Timestamp]
                          )

object PostReplies extends Table[PostReply]("post_reply") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def pid = column[Long]("pid")
  def cid =column[Int]("cid")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def status =column[Int]("status")
  def addTime=column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ pid ~ cid  ~ quoteContent.?  ~ content ~ status ~ addTime.?  <>(PostReply, PostReply.unapply _)
  def autoInc  = id.? ~ uid  ~ pid ~ cid  ~ quoteContent.?  ~ content ~ status ~ addTime.?  <>(PostReply, PostReply.unapply _) returning id
  def autoInc2 = uid  ~ pid ~ cid  ~ quoteContent.?  ~ content  returning id


}
