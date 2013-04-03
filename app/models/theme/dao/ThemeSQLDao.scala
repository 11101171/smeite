package models.theme.dao


import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-12上午8:54
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object ThemeSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* 添加 一个创建 theme num */
  def updateGoodsNum(themeId:Long,num:Int)=database.withSession {
    sqlu"update theme set goods_num =goods_num+$num where id =$themeId".first
  }
  def updateLoveNum(themeId:Long,num:Int)=database.withSession {
    sqlu"update theme set love_num =love_num+$num where id =$themeId".first
  }
  def updateReplyNum(themeId:Long,num:Int)=database.withSession {
    sqlu"update theme set reply_num =reply_num+$num where id =$themeId".first
  }
}
