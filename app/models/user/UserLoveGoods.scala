package models.user

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.goods.{Goodses, Goods}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:06
 * ***********************
 * description:用户和商品 分享（喜欢）之间是多对多的关系
 */

case class UserLoveGoods (
                            id: Option[Long],
                            uid:Long,
                            goodsId:Long
                            )
object UserLoveGoodses extends Table[UserLoveGoods]("user_love_goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def goodsId = column[Long]("goods_id")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ goodsId  <>(UserLoveGoods, UserLoveGoods.unapply _)

  def autoInc = uid  ~ goodsId  returning id

  def find(uid:Long,goodsId:Long)(implicit session: Session)={
    (for (c<-UserLoveGoodses if c.uid===uid if c.goodsId===goodsId) yield(c) ).firstOption
  }

  def insert(uid:Long,goodsId:Long)(implicit session: Session)={
     UserLoveGoodses.autoInc.insert(uid,goodsId)
  }

  def delete(uid:Long,goodsId:Long)(implicit session: Session)={
    (for (c<-UserLoveGoodses if c.uid===uid if c.goodsId===goodsId) yield(c) ).delete
  }
}

