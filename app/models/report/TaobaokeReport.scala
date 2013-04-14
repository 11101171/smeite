package models.report
import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-4-14下午6:03
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
class TaobaokeReport(
                      id: Option[Long],
                      outerCode: String,
                      tradeId:Int,
                      numIid:Int,
                      realPayFee:String,
                      commissionRate:String,
                      commission:String,
                      createTime:String,
                      payTime:String,
                      addTime:Timestamp
                      )
