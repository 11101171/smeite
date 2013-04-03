package models.forum.dao

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
object TopicSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())


  def updateReplyNum(topicId:Long)=database.withSession {
   sqlu"update topic set reply_num =reply_num+1 where id =$topicId".first

  }
}
