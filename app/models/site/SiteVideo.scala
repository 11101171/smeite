package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteVideo (
                        id: Option[Long],
                        sid:Long,
                        url:String,
                        intro:Option[String],
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )

object SiteVideos extends Table[SiteVideo]("site_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def url = column[String]("url")
  def intro = column[String]("intro")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid ~ url ~ intro.? ~ isTop ~ addTime.?  <>(SiteVideo, SiteVideo.unapply _)
  def autoInc  = id.? ~ sid ~ url ~ intro.? ~ isTop ~ addTime.?  <>(SiteVideo, SiteVideo.unapply _) returning id
  def autoInc2 = sid ~ url ~ intro.? ~ isTop returning id
  def autoInc3 = sid ~ url returning id



}
