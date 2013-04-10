package models.goods



import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import java.sql.{ Timestamp}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:11
 * ***********************
 * description: 宝贝
 */

case class Goods(
                  id: Option[Long],
                  numIid:Long,
                  trackIid:Option[String],
                  name: String,
                  intro: String,
                  price:String,
                  pic: String,
                  itemPics: String,
                  nick: String,
                  promotionPrice:Option[String],
                  detailUrl:String,
                  loveNum: Int,
                  volume: Int,
                  status: Int,
                  isMember: Boolean,
                  rate:Int,
                  commissionRate: Option[Int],
                  collectTime:Option[Timestamp],
                  addTime:Option[Timestamp]
                  )

object Goodses extends Table[Goods]("goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def numIid = column[Long]("num_iid")
  def trackIid = column[String]("track_iid")
  def name = column[String]("name")
  def intro = column[String]("intro")
  def price =column[String]("price")
  def pic = column[String]("pic")
  def itemPics = column[String]("item_pics")
  def nick = column[String]("nick")
  def promotionPrice = column[String]("promotion_price")
  def detailUrl = column[String]("detail_url")
  def loveNum = column[Int]("love_num")
  def volume = column[Int]("volume")
  def status = column[Int]("status")
  def isMember = column[Boolean]("is_member")
  def rate     = column[Int]("rate")
  def commissionRate = column[Int]("commission_rate")
  def collectTime=column[Timestamp]("collect_time")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ numIid ~ trackIid.? ~ name ~ intro ~ price ~ pic ~ itemPics ~ nick ~ promotionPrice.? ~ detailUrl ~ loveNum ~ volume ~ status ~ isMember ~ rate ~ commissionRate.? ~ collectTime.? ~ addTime.? <>(Goods, Goods.unapply _)
  def autoInc = numIid  ~ name ~ intro ~ price ~ pic ~ itemPics ~ nick  ~ detailUrl  returning id


}
