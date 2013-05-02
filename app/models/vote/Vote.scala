package models.vote

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-2
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 *
 */
case class Vote(
                        id: Option[Long],
                        voteType: Int,
                        validState: Int,
                        thirdId:Option[Long],
                        intro: Option[String],
                        pic: Option[String],
                        select1: Option[String],
                        select2: Option[String],
                        select3: Option[String],
                        select4: Option[String],
                        select5: Option[String],
                        value1: Option[String],
                        value2: Option[String],
                        value3: Option[String],
                        value4: Option[String],
                        value5: Option[String],
                        addTime: Option[Timestamp]
                        )
object Votes extends Table[Vote]("vote") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def voteType = column[Int]("vote_type")
  def validState = column[Int]("valid_state")
  def thirdId = column[Long]("third_id")
  def intro = column[String]("intro")
  def pic = column[String]("pic")
  def select1 = column[String]("select1")
  def select2 = column[String]("select2")
  def select3 = column[String]("select3")
  def select4 = column[String]("select4")
  def select5 = column[String]("select5")
  def value1 = column[String]("value1")
  def value2 = column[String]("value2")
  def value3 = column[String]("value3")
  def value4 = column[String]("value4")
  def value5 = column[String]("value5")
  def addTime = column[Timestamp]("add_time")
  def * = id.? ~ voteType ~ validState ~ thirdId.? ~ intro.? ~ pic.? ~ select1.? ~ select2.? ~ select3.? ~ select4.? ~ select5.? ~ value1.? ~ value2.? ~ value3.? ~ value4.? ~ value5.? ~ addTime.? <> (Vote, Vote.unapply _)
  def autoInc =  id.? ~ voteType ~ validState ~ thirdId.? ~ intro.? ~ pic.? ~ select1.? ~ select2.? ~ select3.? ~ select4.? ~ select5.? ~ value1.? ~ value2.? ~ value3.? ~ value4.? ~ value5.? ~ addTime.? <> (Vote, Vote.unapply _) returning id
}
