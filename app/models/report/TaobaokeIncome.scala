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
                      tradeId:Long,
                      outerCode: String,
                      realPayFee:String,
                      commissionRate:String,
                      commission:String,
                      payPrice:String,
                      itemNum:Long,
                      day:String,
                      createTime:Timestamp,
                      payTime:Timestamp
                      )
object TaobaokeIncomes extends Table[TaobaokeIncome]("taobaoke_income") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def numIid = column[Long]("num_iid")
  def tradeId = column[Long]("trade_id")
  def outerCode = column[String]("outer_code")
  def realPayFee = column[String]("real_pay_fee")
  def commissionRate = column[String]("commission_rate")
  def commission = column[String]("commission")
  def payPrice = column[String]("pay_price")
  def itemNum = column[Long]("item_num")
  def day = column[String]("day")
  def createTime = column[Timestamp]("create_time")
  def payTime = column[Timestamp]("pay_time")
  def * = id.? ~ numIid ~ tradeId ~ outerCode ~ realPayFee ~ commissionRate ~ commission ~ payPrice ~ itemNum ~ day ~ createTime ~ payTime <> (TaobaokeIncome, TaobaokeIncome.unapply _)
  def autoInc = id.? ~ numIid ~ tradeId ~ outerCode ~ realPayFee ~ commissionRate ~ commission ~ payPrice ~ itemNum ~ day ~ createTime ~ payTime  <> (TaobaokeIncome, TaobaokeIncome.unapply _) returning id

}

