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
                  hwRate:Float,
                  name: String,
                  intro: String,
                  price:String,
                  pic: String,
                  itemPics: String,
                  nick: String,
                  promotionPrice:Option[String],
                  content:Option[String],
                  loveNum: Int,
                  volume: Int,
                  status: Int,
                  isMember: Boolean,
                  location:String,
                  foodSecurity: Option[String],
                  uid:Long,
                  collectTime:Option[Timestamp],
                  clickUrl:String
                  )

object Goodses extends Table[Goods]("goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def numIid = column[Long]("num_iid")
  def hwRate = column[Float]("hw_rate")
  def name = column[String]("name")
  def intro = column[String]("intro")
  def price =column[String]("price")
  def pic = column[String]("pic")
  def itemPics = column[String]("item_pics")
  def nick = column[String]("nick")
  def promotionPrice = column[String]("promotion_price")
  def content = column[String]("content")
  def loveNum = column[Int]("love_num")
  def volume = column[Int]("volume")
  def status = column[Int]("status")
  def isMember = column[Boolean]("is_member")
  def location     = column[String]("location")
  def foodSecurity = column[String]("food_security")
  def uid = column[Long]("uid")
  def collectTime=column[Timestamp]("collect_time")
  def clickUrl=column[String]("click_url")
  def * = id.? ~ numIid ~ hwRate ~ name ~ intro ~ price ~ pic ~ itemPics ~ nick ~ promotionPrice.? ~ content.? ~ loveNum ~ volume ~ status ~ isMember ~ location ~  foodSecurity.? ~ uid ~ collectTime.? ~ clickUrl <>(Goods, Goods.unapply _)
  def autoInc = uid ~ numIid  ~ name ~ intro ~ price ~ pic ~ itemPics ~ nick  ~ clickUrl ~ foodSecurity.? ~ location ~ hwRate  returning id


}
