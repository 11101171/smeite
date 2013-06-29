package models.msg.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object FavorMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

}
