package models.goods
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-21
 * Time: 下午3:45
 * ***********************
 * description:用于类的说明
 */

case class GoodsShop(
                      id: Option[Long],
                      goodsId:Long, 
                      numIid:Long,
                      shopId:Long,
                      nick:String
                      )

object GoodsShops extends Table[GoodsShop]("goods_shop") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def goodsId = column[Long]("goods_id")
  def numIid = column[Long]("num_iid")
  def shopId = column[Long]("shop_id")
  def nick =column[String]("nick")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ goodsId   ~ numIid ~ shopId ~ nick  <>(GoodsShop, GoodsShop.unapply _)
}
