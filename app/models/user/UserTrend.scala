package models.user

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import play.api.cache.Cache
import play.api.Play.current

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-9下午7:19
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/


case class UserTrend (
id: Option[Long],
uid: Long,
actionName:String,
actionId:Long,
actionUrl:String,
actionContent:String,
addTime: Option[Timestamp]
)

object UserTrends extends Table[UserTrend]("user_trend") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def actionName = column[String]("action_name")
  def actionId = column[Long]("action_id")
  def actionUrl = column[String]("action_url")
  def actionContent = column[String]("action_content")
  def addTime = column[Timestamp]("add_time")
  def * = id.? ~ uid ~ actionName ~ actionId ~ actionUrl ~ actionContent ~ addTime.? <> (UserTrend, UserTrend.unapply _)
  def autoInc =id.? ~ uid ~ actionName ~ actionId ~ actionUrl ~ actionContent ~ addTime.? <> (UserTrend, UserTrend.unapply _) returning id

  /* 根据id 查找 */
  def findById(id:Long)(implicit session: Session):Option[UserTrend] = {
    (for (c <-UserTrends if c.id === id)yield c).firstOption
  }
  /* count 所有的动态数 */
  def count()(implicit session: Session):Int = {
    Query(UserTrends.length).first()
  }
  /* count uid 的动态数 */
  def count(uid:Long)(implicit session: Session):Int = {
    Query(UserTrends.filter(_.uid === uid).length).first()
  }
  def list(start:Int,num:Int) (implicit session: Session):List[UserTrend] = {
    Query(UserTrends).sortBy(_.addTime desc).drop(start).take(num).list()
  }
  def list(uid:Long,start:Int,num:Int) (implicit session: Session):List[UserTrend] = {
  val query = Query(UserTrends).filter(_.uid === uid).sortBy(_.addTime desc).drop(start).take(num)
    //println(query.selectStatement)
    query.list
  }
  def list(uid:Long)(implicit  session:Session):List[UserTrend] ={
    Query(UserTrends).filter(_.uid === uid).list
  }

}


