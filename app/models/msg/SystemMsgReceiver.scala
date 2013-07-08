package models.msg


import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:15
 * ***********************
 * description:用于类的说明
 */

case class SystemMsgReceiver(
                        id: Option[Long],
                        msgId:Long,
                        receiverId:Long,
                        status:Int,
                        addTime:Option[Timestamp]
                        )
object SystemMsgReceivers extends Table[SystemMsgReceiver]("system_msg_receiver") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def msgId =column[Long]("msg_id")
  def receiverId =column[Long]("receiver_id")
  def status = column[Int]("status")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ msgId ~ receiverId  ~ status ~ addTime.? <>(SystemMsgReceiver, SystemMsgReceiver.unapply _)
  def autoId =id.? ~ msgId ~ receiverId  ~ status~ addTime.?  <>(SystemMsgReceiver, SystemMsgReceiver.unapply _) returning id
  def autoId2 =msgId ~ receiverId   returning id
}
