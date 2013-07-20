package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/*
*   duty 0 居民 1 镇长 2 村长
* */
case class SiteMember (
                        id: Option[Long],
                        sid:Long,
                        uid:Long,
                        duty:Int,
                        addTime:Option[Timestamp]
                        )

object SiteMembers extends Table[SiteMember]("site_member") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def uid = column[Long]("uid")
  def duty =column[Int]("duty")
  def addTime=column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid  ~ uid ~ duty ~ addTime.?  <>(SiteMember, SiteMember.unapply _)
  def autoInc  = id.? ~ sid  ~ uid ~ duty ~ addTime.?  <>(SiteMember, SiteMember.unapply _) returning id

  def autoInc2 = sid ~ uid ~ duty  returning id



}
