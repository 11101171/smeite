package models.theme



import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp
import models.Page
import models.Page._
import models.user.User
import  models.user.Users
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:14
 * ***********************
 * description:主题评论
 */

case class ThemeDiscuss(
                        id: Option[Long],
                        themeId:Long,
                        uid:Long,
                        uname:String,
                        content:String,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )
object ThemeDiscusses  extends  Table[ThemeDiscuss]("theme_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def themeId  =column[Long]("theme_id")
  def uid = column[Long]("uid")
  def uname =column[String]("uname")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")
  
  def * =id.? ~ themeId ~ uid ~ uname~ content ~ checkState ~ addTime.?   <>(ThemeDiscuss, ThemeDiscuss.unapply _)
  def autoInc =id.? ~ themeId ~ uid ~ uname ~ content ~ checkState ~ addTime.?   <>(ThemeDiscuss, ThemeDiscuss.unapply _) returning id

  def autoInc2 = themeId ~ uid  ~ uname ~ content ~ checkState returning id

  def delete(id:Long)(implicit session:Session)={
    (for(c <-ThemeDiscusses if c.id === id)yield c).delete
  }


}



