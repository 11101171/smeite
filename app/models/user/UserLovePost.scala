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

case class UserLovePost (
                           id: Option[Long],
                           uid:Long,
                           pid:Long,
                           addTime:Option[Timestamp]
                           )
object UserLovePosts extends Table[UserLovePost]("user_love_post") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def pid = column[Long]("pid")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ pid  ~ addTime.?  <>(UserLovePost, UserLovePost.unapply _)

  def autoInc = uid  ~ pid   returning id

  def insert(uid:Long,pid:Long)(implicit session: Session)={
    UserLovePosts.autoInc.insert(uid,pid)
  }
  def find(uid:Long,pid:Long)(implicit session: Session)={
    (for(c<-UserLovePosts if c.uid===uid  if c.pid ===pid  )yield c).firstOption
  }
  def delete(uid:Long,pid:Long)(implicit session: Session)={
    (for(c<-UserLovePosts if c.uid===uid  if c.pid === pid  )yield c).delete
  }
}




