package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteBaobei (
                        id: Option[Long],
                        sid:Long,
                        goodsId:Long,
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )

object SiteBaobeis extends Table[SiteBaobei]("site_baobei") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def goodsId = column[Long]("goods_id")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid  ~ goodsId ~ isTop ~ addTime.?  <>(SiteBaobei, SiteBaobei.unapply _)
  def autoInc  = id.? ~ sid  ~ goodsId ~ isTop ~ addTime.?  <>(SiteBaobei, SiteBaobei.unapply _) returning id



}
