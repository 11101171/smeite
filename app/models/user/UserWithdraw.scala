package models.user
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-4-14下午6:01
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
case class UserWithdraw(
                     id: Option[Long],
                     uid: Long,
                     num:Int,
                     userOrderId:Long,
                     tradeId:Long,
                     handleStatus:Int,
                     handleResult:String,
                     note:String,
                     withdrawTime:Option[Timestamp],
                     handleTime:Option[Timestamp]
                    )

object UserWithdraws extends Table[UserWithdraw]("user_withdraw") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def num = column[Int]("num")
  def userOrderId = column[Long]("user_order_id")
  def tradeId = column[Long]("trade_id")
  def handleStatus = column[Int]("handle_status")
  def handleResult = column[String]("handle_result")
  def note = column[String]("note")
  def withdrawTime = column[Timestamp]("withdraw_time")
  def handleTime = column[Timestamp]("handle_time")
  def * = id.? ~ uid ~ num ~ userOrderId ~ tradeId ~ handleStatus ~ handleResult ~ note ~ withdrawTime.? ~  handleTime.? <> (UserWithdraw, UserWithdraw.unapply _)
  def autoInc = id.? ~ uid ~ num ~ userOrderId ~ tradeId ~ handleStatus ~ handleResult ~ note ~ withdrawTime.? ~  handleTime.? <> (UserWithdraw, UserWithdraw.unapply _) returning id

}
