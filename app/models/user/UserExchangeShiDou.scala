package models.user

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-22
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
case class UserExchangeShiDou (
  id: Option[Long],
  applyId: Long,
  num:Int,
  handleStatus:Int,
  handleResult:Int,
  note:Option[String],
  applyTime:Option[Timestamp],
  handleTime:Option[Timestamp]
  )

  object UserExchangeShiDous extends Table[UserExchangeShiDou]("user_exchange_shiDou") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
    def applyId = column[Long]("apply_id")
    def num = column[Int]("num")
    def handleStatus = column[Int]("handle_status")
    def handleResult = column[Int]("handle_result")
    def note = column[String]("note")
    def applyTime = column[Timestamp]("apply_time")
    def handleTime = column[Timestamp]("handle_time")
    def * = id.? ~ applyId  ~ num~  handleStatus ~ handleResult ~ note.? ~ applyTime.? ~  handleTime.? <> (UserExchangeShiDou, UserExchangeShiDou.unapply _)
    def autoInc = id.? ~ applyId ~ num~  handleStatus ~ handleResult ~ note.? ~ applyTime.? ~  handleTime.? <> (UserExchangeShiDou,UserExchangeShiDou.unapply _) returning id
    def autoInc2 = applyId  ~ num ~ applyTime  returning id

}
