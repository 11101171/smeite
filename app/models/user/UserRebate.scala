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
case class UserRebate(
                     id: Option[Long],
                     uid: Long,
                     num:Int,
                     rebateType:Int,
                     userOrderId:Option[Long],
                     tradeId:Option[Long],
                     handleStatus:Int,
                     handleResult:String,
                     note:Option[String],
                     withdrawTime:Option[Timestamp],
                     handleTime:Option[Timestamp]
                    )

object UserRebates extends Table[UserRebate]("user_rebate") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def num = column[Int]("num")
  def rebateType = column[Int]("rebate_type")
  def userOrderId = column[Long]("user_order_id")
  def tradeId = column[Long]("trade_id")
  def handleStatus = column[Int]("handle_status")
  def handleResult = column[String]("handle_result")
  def note = column[String]("note")
  def withdrawTime = column[Timestamp]("withdraw_time")
  def handleTime = column[Timestamp]("handle_time")
  def * = id.? ~ uid ~ num ~ rebateType ~ userOrderId.? ~ tradeId.? ~ handleStatus ~ handleResult ~ note.? ~ withdrawTime.? ~  handleTime.? <> (UserRebate, UserRebate.unapply _)
  def autoInc = id.? ~ uid ~ num ~ rebateType ~ userOrderId.? ~ tradeId.? ~ handleStatus ~ handleResult ~ note.? ~ withdrawTime.? ~  handleTime.? <> (UserRebate, UserRebate.unapply _) returning id
  def  autoInc2 = uid ~ num ~ rebateType ~ withdrawTime   returning id
  def autoInc3 = uid ~ num ~ rebateType ~ userOrderId ~ tradeId ~ withdrawTime returning id
}
