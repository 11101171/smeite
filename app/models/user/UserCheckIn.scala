package models.user
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-2
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
case class UserCheckIn(
                        id: Option[Long],
                        uid: Long,
                        credits: Int,
                        days: Int,
                        month: Int,
                        history: String,
                        addTime: Timestamp
                        )
object UserCheckIns extends Table[UserCheckIn]("user_check_in") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def credits = column[Int]("credits")
  def days = column[Int]("days")
  def month = column[Int]("month")
  def history = column[String]("history")
  def addTime = column[Timestamp]("add_time")
  def * = id.? ~ uid ~ credits ~ days ~ month ~ history ~ addTime <> (UserCheckIn, UserCheckIn.unapply _)
  def autoInc =  id.? ~ uid ~ credits ~ days ~ month ~ history ~ addTime <> (UserCheckIn, UserCheckIn.unapply _) returning id
}
