package models.theme


import play.api.db._
import play.api.Play.current
import java.sql.{Timestamp }
import scala.slick.driver.MySQLDriver.simple._
import models.Page._
import models.Page
import collection.mutable.ListBuffer
import models.user.UserLoveThemes


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:11
 * ***********************
 * description:主题
 */

case class Theme(
                  id: Option[Long],
                  name: String,
                  intro:Option[String],
                  hotIndex:Int,
                  isRecommend:Boolean,
                  uid:Long,
                  uname:String,
                  cid:Int,
                  tags:Option[String],
                  pic:String,
                  loveNum:Int,
                  replyNum:Int,
                  goodsNum:Int,
                  seoTitle:Option[String],
                  seoKeywords:Option[String],
                  seoDesc:Option[String],
                  modifyTime:Option[Timestamp],
                  addTime:Option[Timestamp]
                  )


object Themes extends Table[Theme]("theme") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def intro = column[String]("intro")
  def hotIndex=column[Int]("hot_index")
  def isRecommend = column[Boolean]("is_recommend")
  def uid = column[Long]("uid")
  def uname = column[String]("uname")
  def cid = column[Int]("cid")
  def tags = column[String]("tags")
  def pic=   column[String]("pic")
  def loveNum = column[Int]("love_num")
  def replyNum = column[Int]("reply_num")
  def goodsNum = column[Int]("goods_num")
  def seoTitle = column[String]("seo_title")
  def seoKeywords = column[String]("seo_keywords")
  def seoDesc = column[String]("seo_desc")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ name ~ intro.? ~ hotIndex ~ isRecommend ~ uid ~ uname ~ cid  ~ tags.? ~ pic ~ loveNum ~ replyNum ~ goodsNum ~ seoTitle.? ~ seoKeywords.? ~ seoDesc.?  ~ modifyTime.? ~ addTime.? <>(Theme, Theme.unapply _)
  def autoInc = id.? ~ name ~ intro.? ~ hotIndex ~ isRecommend ~ uid ~ uname ~ cid  ~ tags.? ~ pic ~ loveNum ~ replyNum~ goodsNum ~ seoTitle.? ~ seoKeywords.? ~ seoDesc.?  ~ modifyTime.? ~ addTime.? <>(Theme, Theme.unapply _) returning id

  def autoInc2 = name ~ uid ~ uname ~ addTime  returning id
  def autoInc3 = name ~ intro.? ~ uid ~ uname ~ cid ~ tags.? ~ addTime returning id

  def count()(implicit session: Session):Int = {
    Query(Themes.length).first()
  }
  def find(id:Long)(implicit  session :Session)={
   (for(c<-Themes.filter(_.id === id))yield(c)).firstOption
  }
  def find(name:String)(implicit  session :Session)={
    (for(c<-Themes.filter(_.name === name))yield(c)).firstOption
  }
  def delete(id:Long)(implicit  session :Session)={
    (for(c<-Themes.filter(_.id === id))yield(c)).delete
  }

}


