package models.user.dao

import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-27下午8:05
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object UserSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* 添加 一个创建 theme num */
  def updatePostThemeNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set post_theme_num =post_theme_num+$num where id =$uid".first
  }
  def updateLoveThemeNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set love_theme_num =love_theme_num+$num where id =$uid".first
  }
  def updatePostBaobeiNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set post_baobei_num =post_baobei_num+$num where id =$uid".first
  }
  def updateLoveBaobeiNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set love_baobei_num =love_baobei_num+$num where id =$uid".first
  }
  def updatePostTopicNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set post_topic_num =post_topic_num+$num where id =$uid".first
  }
  def updateLoveTopicNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set love_topic_num =love_topic_num+$num where id =$uid".first
  }

  def updateFansNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set fans_num = fans_num+$num where id =$uid".first
  }
  def updateFollowNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set follow_num =follow_num+$num where id =$uid".first
  }

  def updateTrendNum(uid:Long,num:Int)=database.withSession {
    sqlu"update user_static set trend_num = trend_num+$num where id =$uid".first
  }

  def loginRecord(uid:Long,ip:String,num:Int)=database.withSession {
    sqlu"update user_profile  set login_ip=$ip ,login_num = login_num+$num where uid =$uid".first
  }

  def updateCredits(uid:Long,num:Int)=database.withSession{
    sqlu"update user set credits = credits+$num where id =$uid".first
  }
  def updateWithdrawCredits(uid:Long,num:Int)=database.withSession{
    sqlu"update user set withdraw_credits = withdraw_credits+$num where id =$uid".first
  }
  def updateShiDou(uid:Long,num:Int)=database.withSession{
    sqlu"update user set shi_dou = shi_dou+$num where id =$uid".first
  }
  def updateWithdrawShiDou(uid:Long,num:Int)=database.withSession{
    sqlu"update user set withdraw_shi_dou = withdraw_shi_dou+$num where id =$uid".first
  }

}
