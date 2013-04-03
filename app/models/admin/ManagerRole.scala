package models.admin

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:26
 * ***********************
 * description:用于类的说明
 */

case class ManagerRole (
                         id:Option[Long]=None,
                         email: String,
                         passwd: String,
                         name:String,
                         department:String,
                         phone:String,
                         loginTime:Timestamp,
                         addTime:Timestamp,
                         roleId:Int,
                         roleName:String

                         )

