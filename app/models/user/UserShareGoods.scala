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
 * Time: 下午2:08
 * ***********************
 * description:用于类的说明  用户发布的商品，一个商品可以由多个用户发布，一个用户也可以发布多少商品，多对多关系
 */

case class UserShareGoods(
                           id: Option[Long],
                           uid:Long,
                           goodsId:Long
                            )

object UserShareGoodses extends Table[UserShareGoods]("user_share_goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def goodsId = column[Long]("goods_id")
  def * = id.? ~ uid  ~ goodsId  <>(UserShareGoods, UserShareGoods.unapply _)

  def autoInc = uid  ~ goodsId   returning id

  def insert(uid:Long,goodsId:Long)(implicit session: Session)={
    UserLoveGoodses.autoInc.insert(uid,goodsId)
  }
  def find(uid:Long,goodsId:Long)(implicit session: Session)={
    (for(c<-UserShareGoodses if c.uid===uid  if c.goodsId ===goodsId  )yield c).firstOption
  }
  def delete(uid:Long,goodsId:Long)(implicit session: Session)={
    (for(c<-UserShareGoodses if c.uid===uid  if c.goodsId === goodsId  )yield c).delete
  }
}
