package models.site.dao

import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current

object SiteSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())


  def updateMemberNum(sid:Long,num:Int)=database.withSession {
    sqlu"update site set member_num =member_num+$num where id =$sid".first
  }

  def updatePostLoveNum(pid:Long,num:Int)=database.withSession {
    sqlu"update post set love_num =love_num+$num where id =$pid".first
  }
  def updatePostReplyNum(pid:Long,num:Int)=database.withSession {
    sqlu"update post set reply_num =reply_num+$num where id =$pid".first
  }
  def updatePostViewNum(pid:Long,num:Int)=database.withSession {
    sqlu"update post set view_num =view_num+$num where id =$pid".first
  }
}
