package models.admin.dao

import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-27下午8:05
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object ManagerSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())



  def loginRecord(mid:Long,ip:String,num:Int,loginTime:Timestamp,lastLoginTime:Timestamp)=database.withSession {
    sqlu"update manager set login_ip=$ip ,login_num = login_num+$num ,login_time = $loginTime ,last_login_time =$lastLoginTime  where id =$mid".first
  }


}
