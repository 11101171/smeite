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
                        receiverName:String,
                        status:Int
                        )
object SystemMsgReceivers extends Table[SystemMsgReceiver]("system_msg_receiver") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def msgId =column[Long]("msg_id")
  def receiverId =column[Long]("receiver_id")
  def receiverName =column[String]("receiver_name")
  def status = column[Int]("status")
  def * = id.? ~ msgId ~ receiverId ~ receiverName  ~ status <>(SystemMsgReceiver, SystemMsgReceiver.unapply _)
  def autoId =id.? ~ msgId ~ receiverId ~ receiverName ~ status  <>(SystemMsgReceiver, SystemMsgReceiver.unapply _) returning id
  def autoId2 =msgId ~ receiverId ~ receiverName  returning id
}
