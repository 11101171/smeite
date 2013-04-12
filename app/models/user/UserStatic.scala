package models.user

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:57
 * ***********************
 * description:用于类的说明    用户的统计   暂不实现
 */



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

case class UserStatic(
                      id: Option[Long],
                      uid: Long,
                      fansNum: Int,
                      followNum: Int,
                      trendNum: Int,
                      loveBaobeiNum: Int,
                      loveThemeNum: Int,
                      loveTopicNum: Int,
                      postBaobeiNum: Int,
                      postThemeNum: Int,
                      postTopicNum: Int
                      )
object UserStatics extends Table[UserStatic]("user_static") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def fansNum = column[Int]("fans_num")
  def followNum = column[Int]("follow_num")
  def trendNum = column[Int]("trend_num")
  def loveBaobeiNum = column[Int]("love_baobei_num")
  def loveThemeNum = column[Int]("love_theme_num")
  def loveTopicNum = column[Int]("love_topic_num")
  def postBaobeiNum = column[Int]("post_baobei_num")
  def postThemeNum = column[Int]("post_theme_num")
  def postTopicNum = column[Int]("post_topic_num")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid ~ fansNum ~ followNum ~ trendNum ~ loveBaobeiNum ~ loveThemeNum ~ loveTopicNum ~ postBaobeiNum ~ postThemeNum ~ postTopicNum <>(UserStatic, UserStatic.unapply _)
  def autoInc = uid  returning id

  /* 根据用户id 查找 */
  def findByUid(uid:Long)(implicit session: Session):Option[UserStatic] = {
    Query(UserStatics).filter(_.uid === uid).firstOption
  }

}



