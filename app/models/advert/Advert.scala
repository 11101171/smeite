package models.advert

import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import java.sql.{Timestamp }
import models.Page
import models.Page._
import models.goods.Goods
import models.goods.Goodses
import models.user.User
import models.user.Users
import  models.theme.Theme
import  models.theme.Themes

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-27
 * Time: 上午11:49
 * ***********************
 * description:用于类的说明
 */

case class Advert(
                   id:Option[Long]=None,
                   positionCode:String,
                   name: String,
                   thirdId:Option[Long],
                   title:Option[String],
                   content:Option[String],
                   pic:Option[String],
                   spic:Option[String],
                   width:Int,
                   height:Int,
                   link:Option[String],
                   note:Option[String],
                   clickNum:Int,
                   addTime:Option[Timestamp]
                   )
object Adverts extends Table[Advert]("advert") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def positionCode = column[String]("position_code")
  def name = column[String]("name")
  def thirdId = column[Long]("third_id")
  def title = column[String]("title")
  def content = column[String]("content")
  def pic = column[String]("pic")
  def spic = column[String]("spic")
  def width = column[Int]("width")
  def height = column[Int]("height")
  def link = column[String]("link")
  def note=column[String]("note")
  def clickNum=column[Int]("click_num")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ positionCode ~ name ~ thirdId.?   ~ title.? ~ content.? ~ pic.? ~ spic.? ~ width ~ height ~ link.? ~ note.?  ~ clickNum ~ addTime.? <>(Advert, Advert.unapply _)
}

