package models.user


import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
import models.Page
import models.Page._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:36
 * ***********************
 * description:用于类的说明  用户喜欢的话题
 */

case class UserLoveTopic (
                           id: Option[Long],
                           uid:Long,
                           topicId:Long,
                           addTime:Option[Timestamp]
                           )
object UserLoveTopics extends Table[UserLoveTopic]("user_love_topic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def topicId = column[Long]("topic_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ topicId  ~ addTime.?  <>(UserLoveTopic, UserLoveTopic.unapply _)

  def autoInc = uid  ~ topicId   returning id

  def insert(uid:Long,topicId:Long)(implicit session: Session)={
    UserLoveTopics.autoInc.insert(uid,topicId)
  }
  def find(uid:Long,topicId:Long)(implicit session: Session)={
    (for(c<-UserLoveTopics if c.uid===uid  if c.topicId ===topicId  )yield c).firstOption
  }
  def delete(uid:Long,topicId:Long)(implicit session: Session)={
    (for(c<-UserLoveTopics if c.uid===uid  if c.topicId === topicId  )yield c).delete
  }
}




