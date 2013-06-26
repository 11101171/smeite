package models.site

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-26
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
case  class Post(
                  id: Option[Long],
                  uid:Long,
                  sid:Long,
                  cid:Int,
                  title:String,
                  pic:String,
                  content: String,
                  tags:Option[String],
                  status:Int,
                  isTop:Int,
                  viewNum:Int,
                  extraAttr1:Option[String],
                  extraAttr2:Option[String],
                  extraAttr3:Option[String],
                  extraAttr4:Option[String],
                  extraAttr5:Option[String],
                  extraAttr6:Option[String],
                  addTime:Option[Timestamp]
                  )

object Posts extends Table[Post]("post") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def sid = column[Long]("sid")
  def cid = column[Int]("cid")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def content     =column[String]("content")
  def tags    =column[String]("tags")
  def status = column[Int]("status")
  def isTop = column[Int]("is_top")
  def viewNum = column[Int]("view_num")
  def extraAttr1 = column[Int]("extra_attr1")
  def extraAttr2 = column[Int]("extra_attr2")
  def extraAttr3 = column[Int]("extra_attr3")
  def extraAttr4 = column[Int]("extra_attr4")
  def extraAttr5 = column[Int]("extra_attr5")
  def extraAttr6 = column[Int]("extra_attr6")
  def addTime=column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid ~ sid ~ cid ~ title ~ pic ~ content ~ tags.? ~ status ~ isTop ~ viewNum ~ extraAttr1.? ~ extraAttr2.? ~ extraAttr3.? ~ extraAttr4.? ~ extraAttr5.? ~ extraAttr6.?  ~addTime.? <>(Post, Post.unapply _)
  def autoInc = id.? ~ uid ~ sid ~ cid ~ title ~ pic ~ content ~ tags.? ~ status ~ isTop ~ viewNum ~ extraAttr1.? ~ extraAttr2.? ~ extraAttr3.? ~ extraAttr4.? ~ extraAttr5.? ~ extraAttr6.?  ~addTime.? <>(Post, Post.unapply _) returning id


}