package models.theme


import  java.sql.Timestamp
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:12
 * ***********************
 * description:主题标签
 * 2012.11.04 如果在theme中增加tags， 这个类还有必要存在吗？思考中……
 */

case class ThemeTag (
                      id: Option[Long],
                      themeId:Long,
                      themeName: String,
                      tagId:Long,
                      tagName: String,
                      addTime:Option[Timestamp]
                      )

object ThemeTags extends Table[ThemeTag]("theme_tag") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def themeId = column[Long]("theme_id")
  def themeName = column[String]("theme_name")
  def tagId =column[Long]("tag_id")
  def tagName = column[String]("tag_name")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ themeId ~ themeName ~ tagId ~ tagName  ~ addTime.? <>(ThemeTag, ThemeTag.unapply _)
  def autoInc = id.? ~ themeId ~ themeName ~ tagId ~ tagName  ~ addTime.? <>(ThemeTag, ThemeTag.unapply _) returning id
  def autoInc2 = themeId ~ themeName ~ tagId ~ tagName returning id

  def delete(themeId:Long,tagName:String)(implicit session:Session)={
    (for (c<- ThemeTags if c.themeId === themeId if c.tagName === tagName)yield c).delete
  }
  def find(themeId:Long,tagName:String)(implicit session:Session)={
    (for (c<- ThemeTags if c.themeId === themeId if c.tagName === tagName)yield c).firstOption
  }
  def find(themeId:Long)(implicit  session:Session)={
    (for (c<- ThemeTags if c.themeId === themeId if c.tagName === tagName)yield c).list
  }

}


