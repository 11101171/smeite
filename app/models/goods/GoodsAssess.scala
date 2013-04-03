package models.goods

import java.sql.Timestamp
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.user.{User,Users}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:10
 * ***********************
 * description:宝贝的评论
 */

case class GoodsAssess(
                        id: Option[Long],
                        goodsId:Long,
                        uid:Long,
                        uname: String,
                        content: String,
                        isWorth:Boolean,
                        isBought: Boolean,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )

object GoodsAssesses extends Table[GoodsAssess]("goods_assess") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def goodsId = column[Long]("goods_id")
  def uid = column[Long]("uid")
  def uname = column[String]("uname")
  def content = column[String]("content")
  def isWorth =column[Boolean]("is_worth")
  def isBought =column[Boolean]("is_bought")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ goodsId ~ uid ~ uname ~ content ~ isWorth ~ isBought ~ checkState ~ addTime.? <>(GoodsAssess, GoodsAssess.unapply _)
  def autoInc = id.? ~ goodsId ~ uid ~ uname ~ content ~ isWorth ~ isBought ~ checkState ~ addTime.? <>(GoodsAssess, GoodsAssess.unapply _) returning id
  def autoInc2  = goodsId ~ uid ~ uname ~ content ~ checkState  returning id
}

