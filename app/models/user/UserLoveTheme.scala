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
 * Time: 下午2:05
 * ***********************
 * description:用户和主题分享（喜欢）之间 是多对多的关系，这是个中间类
 */

case class UserLoveTheme (
                           id: Option[Long],
                           uid:Long,
                           themeId:Long,
                           addTime:Option[Timestamp]
                           )

object UserLoveThemes extends Table[UserLoveTheme]("user_love_theme") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def themeId = column[Long]("theme_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ themeId ~ addTime.?  <>(UserLoveTheme, UserLoveTheme.unapply _)

  def autoInc = uid  ~ themeId   returning id

  def insert(uid:Long,themeId:Long)(implicit session: Session)={
    UserLoveThemes.autoInc.insert(uid,themeId)
  }
  def find(uid:Long,themeId:Long)(implicit session: Session)={
    (for(c<-UserLoveThemes if c.uid===uid  if c.themeId ===themeId  )yield c).firstOption
  }
  def delete(uid:Long,themeId:Long)(implicit session: Session)={
    (for(c<-UserLoveThemes if c.uid===uid  if c.themeId ===themeId  )yield c).delete
  }

}

