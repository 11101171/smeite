package models.vote.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.Page
import models.vote.{Votes, Vote}
import java.sql.Timestamp

object VoteDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  /* 添加 vote */
  def addVote(vote:Vote) =database.withSession {  implicit session:Session =>
    Votes.autoInc.insert(vote)
  }
  /* 修改 vote */
  def modifyVote(vote:Vote)=database.withSession {  implicit session:Session =>
    (for(c<-Votes if c.id === vote.id.get)yield c).update(vote)
  }
  /* 删除 vote */
  def deleteVote(id:Long) =database.withSession {  implicit session:Session =>
    (for(c<-Votes if c.id === id)yield c).delete
  }

  def findVote(id:Long) =database.withSession {  implicit session:Session =>
    (for(c<-Votes if c.id === id)yield c ).first()
  }
  def findVotes(currentPage:Int,pageSize:Int) =database.withSession {  implicit session:Session =>
    val totalRows=Query(Votes.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Votes.sortBy(_.id desc).drop(startRow).take(pageSize)  ) yield(c)
    val votes:List[Vote]=  q.list()
    Page[Vote](votes,currentPage,totalPages);
  }
  def findValidVotes(validState:Int) =database.withSession {  implicit session:Session =>
    (for(c<-Votes if c.validState === validState)yield c).list()
  }
}
