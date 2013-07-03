package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteAlbumPic (
                        id: Option[Long],
                        sid:Long,
                        aid:Long,
                        intro:Option[String],
                        pic:String,
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )

object SiteAlbumPics extends Table[SiteAlbumPic]("site_album_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def aid = column[Long]("aid")
  def intro = column[String]("intro")
  def pic = column[String]("pic")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid ~ aid  ~ intro.?  ~ pic ~ isTop ~ addTime.?  <>(SiteAlbumPic, SiteAlbumPic.unapply _)
  def autoInc  = id.? ~ sid ~ aid  ~ intro.?  ~ pic ~ isTop ~ addTime.?  <>(SiteAlbumPic, SiteAlbumPic.unapply _) returning id



}
