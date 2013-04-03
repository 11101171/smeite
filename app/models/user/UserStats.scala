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

case class UserStats(
                      id: Option[Long],
                      uid: Long,
                      uname: String,
                      signInTime: Option[Timestamp],
                      signInDays: Int
                      )



