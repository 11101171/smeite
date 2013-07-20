package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteAlbum (
                        id: Option[Long],
                        sid:Long,
                        title:String,
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )

object SiteAlbums extends Table[SiteAlbum]("site_album") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def title = column[String]("title")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid  ~ title ~ isTop ~ addTime.?  <>(SiteAlbum, SiteAlbum.unapply _)
  def autoInc  = id.? ~ sid  ~ title ~ isTop ~ addTime.?  <>(SiteAlbum, SiteAlbum.unapply _) returning id

  def autoInc2 = sid ~ title ~ isTop returning id




}
