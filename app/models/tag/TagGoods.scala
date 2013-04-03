package models.tag


import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import models.goods.{Goods, Goodses}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:20
 * ***********************
 * description:tag下的宝贝，
 */

case class TagGoods (
                      id: Option[Long],
                      tagName:String,
                      goodsId:Long,
                      addNum:Int,
                      checkState:Int,
                      sortNum:Int
                      )

object TagGoodses extends Table[TagGoods]("tag_goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def tagName = column[String]("tag_name")
  def goodsId = column[Long]("goods_id")
  def addNum = column[Int]("add_num")
  def checkState =column[Int]("check_state")
  def sortNum = column[Int]("sort_num")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ tagName  ~ goodsId ~ addNum ~ checkState ~ sortNum  <>(TagGoods, TagGoods.unapply _)
  def autoInc  =id.? ~ tagName  ~ goodsId ~ addNum ~ checkState ~ sortNum  <>(TagGoods, TagGoods.unapply _) returning id

  def autoInc2 = tagName  ~ goodsId  returning id

  def find(tagName:String,goodsId:Long)(implicit  session:Session)={
    (for(c<-TagGoodses if c.tagName===tagName if c.goodsId===goodsId)yield(c) ).firstOption
  }
  def insert(tagName:String,goodsId:Long)(implicit  session:Session)={
    TagGoodses.autoInc2.insert(tagName,goodsId)
  }

  def delete(id:Long)(implicit  session:Session) ={
    (for(c<-TagGoodses.filter(_.id===id))yield c).delete
  }
  def delete(tagName:String,goodsId:Long)(implicit  session:Session) ={
    (for(c<-TagGoodses if c.tagName === tagName if c.goodsId === goodsId )yield c).delete
  }

}




