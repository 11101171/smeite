package models.goods

import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import java.sql.{ Timestamp}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-12-4
 * Time: 上午10:48
 * ***********************
 * description:用于类的说明
 */

case class Shop(
                 id: Option[Long],
                 nick: String,
                 title:String,
                 detailUrl:String,
                 itemScore: String,
                 serviceScore: String,
                 deliveryScore: String,
                 created: String,
                 credits: Int,
                grade:String,
                 status: Int,
                 note: String,
                 collectTime:Option[Timestamp]
            )

object Shops extends Table[Shop]("shop") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def nick = column[String]("nick")
  def title = column[String]("title")
  def detailUrl=column[String]("detail_url")
  def itemScore = column[String]("item_score")
  def serviceScore = column[String]("service_score")
  def deliveryScore = column[String]("delivery_score")
  def created = column[String]("created")
  def credits = column[Int]("credits")
  def grade = column[String]("grade")
  def status = column[Int]("status")
  def note =   column[String]("note")
  def collectTime=column[Timestamp]("collect_time")
  def * = id.? ~ nick ~ title ~ detailUrl ~ itemScore ~ serviceScore ~ deliveryScore ~ created ~ credits ~ grade ~ status ~ note  ~ collectTime.? <>(Shop, Shop.unapply _)
  def autoInc = id.? ~ nick ~ title ~ detailUrl ~ itemScore ~ serviceScore ~ deliveryScore ~ created ~ credits ~ grade ~ status ~ note  ~ collectTime.? <>(Shop, Shop.unapply _) returning id

}
object ShopDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  /*根据nick查找 */
 def findByNick(nick:String)=database.withSession {  implicit session:Session =>
    (for(c<-Shops if c.nick===nick)yield(c)).firstOption
  }

/*  /*插入新的shop*/
 def insertShop(nick:String,title:String,detailUrl:String,itemScore:String,serviceScore:String,deliveryScore:String,created:String)=database.withSession {  implicit session:Session =>
     (Shops.nick ~ Shops.title ~ Shops.detailUrl ~ Shops.itemScore ~ Shops.serviceScore ~ Shops.deliveryScore ~ Shops.created).insert(nick,title,detailUrl,itemScore,serviceScore,deliverScore,created)
  }*/

  /*分页显示*/
  def list(currentPage: Int = 1, pageSize: Int = 10): Page[Shop] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Shops.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- Shops.drop(startRow).take(pageSize)  ) yield(c)
    println(" q sql "+q.selectStatement)
    val shops:List[Shop]=  q.list()
    Page[Shop](shops,currentPage,totalPages);
  }

  /* delete */
  def deleteShop(id:Long) = database.withSession {  implicit session:Session =>
    (for(c<-Shops if c.id===id)yield(c)).delete
  }

}
