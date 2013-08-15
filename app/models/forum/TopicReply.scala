package models.forum



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:话题回复
 */

case class TopicReply (
                        id: Option[Long],
                        uid: Long,
                        uname: String,
                        topicId: Long,
                        quoteContent: Option[String],
                        content: String,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )
object TopicReplies extends Table[TopicReply]("topic_reply") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def uname = column[String]("uname")
  def topicId = column[Long]("topic_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ uid ~ uname ~ topicId ~ quoteContent.? ~ content ~ checkState ~ addTime.? <>(TopicReply, TopicReply.unapply _)
  def autoInc = id.? ~ uid ~ uname ~ topicId ~ quoteContent.? ~ content ~ checkState ~ addTime.? <>(TopicReply, TopicReply.unapply _) returning id

  def autoInc2 = uid ~ uname ~ topicId ~ quoteContent.? ~ content ~ checkState returning id


}


