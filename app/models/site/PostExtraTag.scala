package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/*
*
* 为帖子添加额外的标签，便于查找到帖子
* */

case class PostExtraTag (
                          id: Option[Long],
                          pid:Long,
                          tagName:String,
                          addTime:Option[Timestamp]
                          )

object PostExtraTags extends Table[PostExtraTag]("post_extra_tag") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def pid = column[Long]("pid")
  def tagName = column[String]("tag_name")
  def addTime=column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ pid  ~ tagName   ~ addTime.?  <>(PostExtraTag, PostExtraTag.unapply _)
  def autoInc  = id.? ~ pid  ~ tagName  ~ addTime.?  <>(PostExtraTag, PostExtraTag.unapply _) returning id
  def autoInc2 = pid ~ tagName returning id



}
