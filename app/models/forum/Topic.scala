package models.forum



import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import models.user.{Users, UserLoveTopics}


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:16
 * ***********************
 * description:用于类的说明
 */

case class Topic(
                 id: Option[Long],
                 uid: Long,
                 uname: String,
                 title: String,
                 content: String,
                 intro:String,
                 pics:Option[String],
                 groupId:Int,
                 typeId:Int,
                 isBest:Boolean,
                 isTop:Boolean,
                 hotIndex:Int,
                 replyNum:Int,
                 viewNum:Int,
                 loveNum:Int,
                 checkState:Int,
                 addTime:Option[Timestamp]
                 )

object Topics extends Table[Topic]("topic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def uname = column[String]("uname")
  def title = column[String]("title")
  def content = column[String]("content")
  def intro =column[String]("intro")
  def pics =column[String]("pics")
  def groupId = column[Int]("group_id")
  def typeId = column[Int]("type_id")
  def isBest = column[Boolean]("is_best")
  def isTop = column[Boolean]("is_top")
  def hotIndex = column[Int]("hot_index")
  def replyNum = column[Int]("reply_num")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ uid ~ uname ~ title ~ content ~ intro ~ pics.? ~ groupId ~ typeId ~ isBest ~ isTop ~ hotIndex ~ replyNum ~ viewNum ~ loveNum ~ checkState ~ addTime.? <>(Topic, Topic.unapply _)
  def autoInc = id.? ~ uid ~ uname ~ title ~ content ~ intro ~ pics.? ~ groupId ~ typeId ~ isBest ~ isTop ~ hotIndex ~ replyNum ~ viewNum ~ loveNum ~ checkState ~ addTime.? <>(Topic, Topic.unapply _) returning id

  def autoInc2 = uid~uname~ title ~ content ~ groupId ~ typeId ~ checkState returning id

  def insert(uid:Long,uname:String,title:String,content:String,groupId:Int,typeId:Int)(implicit  session:Session)={
    Topics.autoInc2.insert(uid,uname,title,content,groupId,typeId,1)
  }
}




