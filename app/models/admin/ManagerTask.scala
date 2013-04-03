package models.admin

import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import java.sql.{Timestamp,Date }

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-21
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
case class ManagerTask (
                     id:Option[Long]=None,
                     level: Int,
                     leader:String,
                     content: String,
                     startDate:Date,
                     endDate:Date,
                     status:Int,
                     note:Option[String],
                     addTime:Option[Timestamp]
                     )


object ManagerTasks extends Table[ManagerTask]("manager_task") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def level = column[Int]("level")
  def leader = column[String]("leader")
  def content = column[String]("content")
  def startDate = column[Date]("start_date")
  def endDate = column[Date]("end_date")
  def status = column[Int]("status")
  def note = column[String]("note")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ level ~ leader ~ content ~ startDate ~ endDate ~ status ~ note.? ~ addTime.?  <>(ManagerTask, ManagerTask.unapply _)
  def autoInc = id.? ~ level ~ leader ~ content ~ startDate ~ endDate ~ status ~ note.? ~ addTime.?  <>(ManagerTask, ManagerTask.unapply _) returning id

  def count()(implicit session: Session):Int = {
    Query(ManagerTasks.length).first()
  }


}
