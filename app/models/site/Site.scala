package models.site


import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description: 小镇
 * status 0 未审核  1 通过审核 2 优质
 */

case class Site(
                id: Option[Long],
                uid:Long,
                cid:Int,
                title:String,
                pic:String,
                intro: String,
                tags:String,
                status:Int,
                memberNum:Int,
                permission:Int,
                notice:Option[String],
                modifyTime:Option[Timestamp],
                addTime:Option[Timestamp]
                )


object Sites extends Table[Site]("site") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def cid = column[Int]("cid")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro     =column[String]("intro")
  def tags    =column[String]("tags")
  def status = column[Int]("status")
  def memberNum = column[Int]("member_num")
  def permission = column[Int]("permission")
  def notice     =column[String]("notice")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid ~ cid ~ title ~ pic ~ intro ~ tags ~ status ~ memberNum ~ permission ~ notice.? ~ modifyTime.? ~addTime.? <>(Site, Site.unapply _)
  def autoInc =  uid ~ cid ~ title ~ permission ~ pic ~ intro ~ tags~ addTime   returning id




}


