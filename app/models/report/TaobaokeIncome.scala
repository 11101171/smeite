package models.report
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-4-14下午6:03
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
case class TaobaokeIncome(
                      id: Option[Long],
                      numIid:Long,
                      outerCode: String,
                      tradeId:Long,
                      realPayFee:String,
                      commissionRate:Int,
                      commission:Int,
                      createTime:Option[Timestamp],
                      payTime:Option[Timestamp]
                      )
object TaobaokeIncomes extends Table[TaobaokeIncome]("taobaoke_income") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def numIid = column[Long]("num_iid")
  def outerCode = column[String]("outer_code")
  def tradeId = column[Long]("trade_id")
  def realPayFee = column[String]("real_pay_fee")
  def commissionRate = column[Int]("commission_rate")
  def commission = column[Int]("commission")
  def createTime = column[Timestamp]("create_time")
  def payTime = column[Timestamp]("pay_time")
  def * = id.? ~ numIid ~ outerCode ~ tradeId ~ realPayFee ~ commissionRate ~ commission ~ createTime.? ~ payTime.? <> (TaobaokeIncome, TaobaokeIncome.unapply _)
  def autoInc = id.? ~ numIid ~ outerCode ~ tradeId ~ realPayFee ~ commissionRate ~ commission ~ createTime.? ~ payTime.?  <> (TaobaokeIncome, TaobaokeIncome.unapply _) returning id

}

