package models.user

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:08
 * ***********************
 * description:用户
 */
import  java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._


case class User(
                 id: Option[Long],
                 name: String,
                 passwd: String,
                 email: Option[String],
                 credits: Int,
                 pic: String,
                 daren:Int,
                 status:Int,
                 comeFrom:Int,
                 openId:Option[String],
                 shiDou:Int,
                 tags:Option[String],
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


object Users extends Table[User]("user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def passwd = column[String]("passwd")
  def email = column[String]("email")
  def credits = column[Int]("credits")
  def pic = column[String]("pic")
  def daren= column[Int]("daren")
  def status = column[Int]("status")
  def comeFrom = column[Int]("come_from")
  def openId = column[String]("open_id")
  def shiDou = column[Int]("shi_dou")
  def tags     = column[String]("tags")
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
  def * = id.? ~ name ~ passwd ~ email.? ~ credits ~ pic  ~ daren  ~ status ~ comeFrom ~ openId.? ~ shiDou ~ tags.? ~ fansNum ~ followNum ~ trendNum ~ loveBaobeiNum ~ loveThemeNum ~ loveTopicNum ~ postBaobeiNum ~ postThemeNum ~ postTopicNum <>(User, User.unapply _)
  def autoInc = id.? ~ name ~ passwd ~ email.? ~ credits ~ pic  ~ daren  ~ status ~ comeFrom ~ openId.? ~ shiDou ~ tags.? ~ fansNum ~ followNum ~ trendNum ~ loveBaobeiNum ~ loveThemeNum ~ loveTopicNum ~ postBaobeiNum ~ postThemeNum ~ postTopicNum <>(User, User.unapply _) returning id
  def autoInc2 = name ~ passwd ~ email returning id
  def autoInc3 = name ~ comeFrom ~ openId ~ pic returning id
  /* count  */
  def count()(implicit session: Session):Int = {
    Query(Users.length).first()
  }
  /* count  */
  def count(comeFrom:Int)(implicit session: Session):Int = {
    Query(Users.filter(_.comeFrom === comeFrom).length).first()
  }
  /* 验证用户登陆email 和 密码是否正确 */
  def authenticate(email: String, passwd: String)(implicit session: Session): Option[User] = {
    (for(u<-Users if u.email === email && u.passwd===Codecs.sha1("smeite"+passwd))yield(u) ).firstOption
  }
  /* 根据用户id 查找 */
  def findById(uid:Long)(implicit session: Session):Option[User] = {
    Query(Users).filter(_.id === uid).firstOption
  }
  /* 根据用户email 查找 */
  def findByEmail(email:String)(implicit session: Session):Option[User] = {
    Query(Users).filter(_.email === email).firstOption
  }
  /*验证第三方登陆用户*/
  def findSnsUser(comeFrom:Int,openId:String)(implicit session: Session): Option[User] = {
    (for(u<-Users if u.comeFrom === comeFrom && u.openId=== openId )yield(u) ).firstOption
  }



}




