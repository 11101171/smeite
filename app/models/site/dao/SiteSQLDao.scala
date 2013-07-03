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
}
