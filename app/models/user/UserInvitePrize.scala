package models.user

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-22
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
case class UserInvitePrize (
  id: Option[Long],
  uid: Long,
  inviteeId:Long,
  inviteeCredits:Int,
  num:Int,
  handleStatus:Int,
  handleResult:String,
  note:Option[String],
  createTime:Option[Timestamp],
  handleTime:Option[Timestamp]
  )

  object UserInvitePrizes extends Table[UserInvitePrize]("user_invite_prize") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
    def uid = column[Long]("uid")
    def inviteeId = column[Long]("invitee_id")
    def inviteeCredits = column[Int]("invitee_credits")
    def num = column[Int]("num")
    def handleStatus = column[Int]("handle_status")
    def handleResult = column[String]("handle_result")
    def note = column[String]("note")
    def createTime = column[Timestamp]("create_time")
    def handleTime = column[Timestamp]("handle_time")
    def * = id.? ~ uid ~ inviteeId ~ inviteeCredits ~ num~  handleStatus ~ handleResult ~ note.? ~ createTime.? ~  handleTime.? <> (UserInvitePrize, UserInvitePrize.unapply _)
    def autoInc = id.? ~ uid ~ inviteeId ~ inviteeCredits ~ num~  handleStatus ~ handleResult ~ note.? ~ createTime.? ~  handleTime.? <> (UserInvitePrize, UserInvitePrize.unapply _) returning id

}
