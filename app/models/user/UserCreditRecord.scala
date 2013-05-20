package models.user

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-20
 * Time: 下午2:34
 * To change this template use File | Settings | File Templates.
 */
case class UserCreditRecord(
                             id: Option[Long],
                             uid: Long,
                             creditType: Int,
                             num: Int,
                             content: String,
                             addTime: Timestamp
                             )
object UserCreditRecords extends Table[UserCreditRecord]("user_credit_record") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def creditType = column[Int]("credit_type")
  def num = column[Int]("num")
  def content = column[String]("content")
  def addTime = column[Timestamp]("add_time")
  def * = id.? ~ uid ~ creditType ~ num  ~ content ~ addTime <> (UserCreditRecord, UserCreditRecord.unapply _)
  def autoInc =  id.? ~ uid ~ creditType ~ num  ~ content ~ addTime <> (UserCreditRecord, UserCreditRecord.unapply _) returning id
}


