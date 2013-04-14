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
 * Since:13-4-14下午6:01
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
class UserOrder(
                 id: Option[Long],
                 uid: Long,
                 goodsId: Long,
                 numIid: Long,
                 nick: String,
                 title: String,
                 location: String,
                 pic: String,
                 price: String,
                 commissionRate: String,
                 credits: Int,
                 status: Int,
                 payTime: Timestamp,
                 createTime: Timestamp
                 )
