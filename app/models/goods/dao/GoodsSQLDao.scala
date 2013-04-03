package models.goods.dao

import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import models.goods.Goods
import play.api.db._
import play.api.Play.current

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-26下午7:16
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object GoodsSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())


  def updateLoveNum(goodsId:Long,num:Int)=database.withSession {
   sqlu"update goods set love_num =love_num+$num where id =$goodsId".first

  }
}
