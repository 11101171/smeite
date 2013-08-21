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
 * ModifyContent: 用户发放积分
 * http://www.smeite.com/
 */
case class UserExchangeCredit(
                     id: Option[Long],
                     uid: Long,
                     num:Int,
                     handleStatus:Int,
                     handleResult:Int,
                     note:Option[String],
                     withdrawTime:Option[Timestamp],
                     handleTime:Option[Timestamp]
                    )

object UserExchangeCredits extends Table[UserExchangeCredit]("user_exchange_credit") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def num = column[Int]("num")
  def handleStatus = column[Int]("handle_status")
  def handleResult = column[Int]("handle_result")
  def note = column[String]("note")
  def withdrawTime = column[Timestamp]("withdraw_time")
  def handleTime = column[Timestamp]("handle_time")
  def * = id.? ~ uid ~ num  ~ handleStatus ~ handleResult ~ note.? ~ withdrawTime.? ~  handleTime.? <> (UserExchangeCredit, UserExchangeCredit.unapply _)
  def autoInc = id.? ~ uid ~ num  ~ handleStatus ~ handleResult ~ note.? ~ withdrawTime.? ~  handleTime.? <> (UserExchangeCredit, UserExchangeCredit.unapply _) returning id
  def  autoInc2 = uid ~ num  ~ withdrawTime   returning id
}
