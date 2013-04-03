package models.theme
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import play.api.db.DB
import models.Page
import models.Page._
import play.api.Play.current
import models.goods.{Goodses, Goods}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-2
 * Time: 下午10:53
 * ***********************
 * description:用于类的说明
 */

case class ThemeGoods(
                       id: Option[Long],
                       themeId:Long,
                       goodsId:Long,
                       goodsPic:String,
                        sortNum:Int
                       )


object ThemeGoodses extends  Table[ThemeGoods]("theme_goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def themeId  =column[Long]("theme_id")
  def goodsId  =column[Long]("goods_id")
  def goodsPic  =column[String]("goods_pic")
  def sortNum = column[Int]("sort_num")

  def * =id.? ~ themeId ~ goodsId ~ goodsPic~sortNum <> (ThemeGoods, ThemeGoods.unapply _)
  def autoInc = id.? ~ themeId ~ goodsId ~ goodsPic~sortNum <> (ThemeGoods, ThemeGoods.unapply _)  returning id
  def autoInc2 = themeId ~ goodsId ~ goodsPic  returning id

  def delete(themeId:Long)(implicit  session:Session)={
    (for (c<-ThemeGoodses.filter(_.themeId === themeId))yield c).delete
  }
  def delete(themeId:Long,goodsId:Long)(implicit  session:Session)={
    (for(c<-ThemeGoodses if c.themeId === themeId if c.goodsId===goodsId)yield c).delete
  }
  def insert(themeId:Long,goodsId:Long,goodsPic:String)(implicit session:Session)={
       ThemeGoodses.autoInc2.insert(themeId,goodsId,goodsPic)
  }
  def find(themeId:Long,goodsId:Long)(implicit session:Session)={
    (for(c<-ThemeGoodses if c.themeId === themeId if c.goodsId === goodsId)yield c).firstOption
  }
}
