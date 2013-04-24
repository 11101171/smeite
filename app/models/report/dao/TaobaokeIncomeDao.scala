package models.report.dao

import  java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current

import java.util.Date
import models.report.{TaobaokeIncomes, TaobaokeIncome}
import models.Page

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-24
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
object TaobaokeIncomeDao {
  /*从connection pool 中 获取jdbc的connection*/
  implicit val database = Database.forDataSource(DB.getDataSource())

  def addTaobaokeIncome(income:TaobaokeIncome) = database.withSession{  implicit session:Session =>
      TaobaokeIncomes.insert(income)
  }

  def findTaobaokeIncomes(currentPage:Int,pageSize:Int) = database.withSession{  implicit session:Session =>
    val totalRows=Query(TaobaokeIncomes.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val list:List[TaobaokeIncome]=  (for(c<- TaobaokeIncomes.sortBy(_.day desc)) yield(c)).drop(startRow).take(pageSize).list()
    Page[TaobaokeIncome](list,currentPage,totalPages);
  }

  def findTaobaokeIncome(tradeId:Long):Option[TaobaokeIncome] = database.withSession{  implicit session:Session =>
    (for(c<-TaobaokeIncomes if c.tradeId === tradeId) yield c).firstOption
  }

  def filterTaobaokeIncomes(day:Option[String],outerCode:Option[String],currentPage:Int,pageSize:Int) =database.withSession{  implicit session:Session =>
    var query = for(c <- TaobaokeIncomes)yield c
    if(!day.isEmpty) query = query.filter(_.day === day.get)
    if(!outerCode.isEmpty) query = query.filter(_.outerCode === outerCode.get)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val list:List[TaobaokeIncome]=  query.drop(startRow).take(pageSize).list()
    Page[TaobaokeIncome](list,currentPage,totalPages);
  }

}
