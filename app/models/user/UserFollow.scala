package models.user



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
 * Time: 下午3:59
 * ***********************
 * description:用于类的说明     用户关注的人
 */

case class UserFollow(
                       id: Option[Long],
                       uid: Long,
                       fansId: Long,
                       addTime: Option[Timestamp]
                       )
object UserFollows extends Table[UserFollow]("user_follow") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def fansId = column[Long]("fans_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ fansId  ~ addTime.?  <> (UserFollow, UserFollow.unapply _)
  def autoInc = uid ~ fansId returning id

  def insert(followId:Long,fansId:Long)(implicit session: Session)={
    UserFollows.autoInc.insert(followId,fansId)
  }
  def find(uid:Long,fansId:Long)(implicit session: Session)={
    (for(c<-UserFollows if c.uid===uid  if c.fansId ===fansId  )yield c).firstOption
  }
  def delete(uid:Long,fansId:Long)(implicit session: Session)={
    (for(c<-UserFollows if c.uid===uid  if c.fansId ===fansId  )yield c).delete
  }
}




