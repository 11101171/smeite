package models.theme
import java.sql.{Timestamp }
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import play.api.db.DB

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-7
 * Time: 上午11:37
 * ***********************
 * description:用于类的说明 theme 主题装修
 */

case class ThemeStyle (
        themeId:Long, 
        pageBgColor:String,
        pageBgImage:String,
        pageBgRepeat:String,
        pageBgPosition:String,
        pageBgAttachment:String,
        bannerHeight:String,
        bannerColor:String,
        bannerBgColor:String,
        bannerBgImage:String,
        bannerBgRepeat:String,
        bannerBgPosition:String
)
object  ThemeStyles extends  Table[ThemeStyle]("theme_style") {
def themeId  =column[Long]("theme_id")
def pageBgColor = column[String]("page_bg_color")
def pageBgImage = column[String]("page_bg_image")
def pageBgRepeat = column[String]("page_bg_repeat")
def pageBgPosition = column[String]("page_bg_position")
def pageBgAttachment = column[String]("page_bg_attachment")
def bannerHeight = column[String]("banner_height")
def bannerColor = column[String]("banner_color")
def bannerBgColor = column[String]("banner_bg_color")
def bannerBgImage = column[String]("banner_bg_image")
def bannerBgRepeat = column[String]("banner_bg_repeat")
def bannerBgPosition = column[String]("banner_bg_position")
def * = themeId ~ pageBgColor ~ pageBgImage ~ pageBgRepeat ~ pageBgPosition ~ pageBgAttachment ~ bannerHeight ~ bannerColor ~ bannerBgColor ~ bannerBgImage ~ bannerBgRepeat ~ bannerBgPosition  <>(ThemeStyle, ThemeStyle.unapply _)

  def delete(id:Long)(implicit session:Session )={
    (for(c<- ThemeStyles.filter(_.themeId === id ))yield c).delete
  }
  def find(themeId:Long)(implicit  session:Session)={
    (for(c<-ThemeStyles if c.themeId ===themeId)yield(c)).firstOption
  }
  def updateStyle(themeId:Long,pageBgColor:String,pageBgImage:String,pageBgRepeat:String,pageBgPosition:String,pageBgAttachment:String)(implicit session:Session)={
    (for(c<- ThemeStyles if c.themeId === themeId)yield(c.pageBgColor ~ c.pageBgImage ~ c.pageBgRepeat ~ c.pageBgPosition ~ c.pageBgAttachment)).update(pageBgColor,pageBgImage,pageBgRepeat,pageBgPosition,pageBgAttachment)
  }
}

