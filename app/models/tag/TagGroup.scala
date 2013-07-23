package models.tag



import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp
import models.Page
import models.Page._


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 1cid2-9-20
 * Time: 上午11:19
 * ***********************
 * description:标签分组
 */

case class TagGroup(
                     id: Option[Long],
                     name: String,
                     pic:String,
                     intro:Option[String],
                     cid:Option[Int],
                     code:Int,
                     isVisible:Boolean,
                     sortNum:Int,
                     seoTitle:Option[String],
                     seoKeywords:Option[String],
                     seoDesc:Option[String],
                     modifyTime:Option[Timestamp],
                     addTime:Option[Timestamp]
                     )

// Definition of the theme_group table
object TagGroups extends Table[TagGroup]("tag_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def pic = column[String]("pic")
  def intro = column[String]("intro")
  def cid     =column[Int]("cid")
  def code     =column[Int]("code")
  def isVisible = column[Boolean]("is_visible")
  def sortNum = column[Int]("sort_num")
  def seoTitle = column[String]("seo_title")
  def seoKeywords = column[String]("seo_keywords")
  def seoDesc = column[String]("seo_desc")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ name ~ pic ~ intro.? ~ cid.?  ~ code ~ isVisible ~ sortNum ~ seoTitle.? ~ seoKeywords.? ~ seoDesc.?  ~ modifyTime.? ~addTime.? <>(TagGroup, TagGroup.unapply _)
}




