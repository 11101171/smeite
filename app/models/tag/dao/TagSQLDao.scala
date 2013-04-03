package models.tag.dao

import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
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
object TagSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())


}
