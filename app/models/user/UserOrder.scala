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
 * Since:13-4-14下午6:01
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
case class UserOrder(
                 id: Option[Long],
                 uid: Long,
                 goodsId: Long,
                 numIid: Long,
                 nick: String,
                 title: String,
                 location: String,
                 pic: String,
                 price: String,
                 withdrawRate: Int,
                 credits: Int,
                 status: Int,
                 payTime: Option[Timestamp],
                 createTime:Option[Timestamp]
                 )

object UserOrders extends Table[UserOrder]("user_order") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def goodsId = column[Long]("goods_id")
  def numIid = column[Long]("num_iid")
  def nick = column[String]("nick")
  def title = column[String]("title")
  def location = column[String]("location")
  def pic = column[String]("pic")
  def price = column[String]("price")
  def withdrawRate = column[Int]("withdraw_rate")
  def credits = column[Int]("credits")
  def status = column[Int]("status")
  def payTime = column[Timestamp]("pay_time")
  def createTime = column[Timestamp]("create_time")
  def * = id.? ~ uid ~ goodsId ~ numIid ~ nick ~ title ~ location~ pic~ price~ withdrawRate~ credits~ status~ payTime.? ~ createTime.? <> (UserOrder, UserOrder.unapply _)
  def autoInc = id.? ~ uid ~ goodsId ~ numIid ~ nick ~ title ~ location~ pic~ price~ withdrawRate~ credits~ status~ payTime.? ~ createTime.? <> (UserOrder, UserOrder.unapply _) returning id



}



