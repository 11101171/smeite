package models.user
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-4-14下午6:01
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
class UserWithdraw(
                    id: Option[Long],
                    uid: Long,
                    withdrawType:Int,
                     withdrawNum:Int,
                     handleResult:Int,
                     note:String,
                     withdrawTime:Timestamp,
                      handleTime:Timestamp
                    )
