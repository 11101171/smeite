package models.admin.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import play.api.libs.Codecs
import  models.admin._
import models.Page
import java.sql.{Timestamp,Date }

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-13下午12:27
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object ManagerDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def findById(gid:Long):Manager= database.withSession{   implicit session:Session =>
    {for(c<-Managers if c.id === gid)yield(c)}.first
  }

  def authenticate(email: String, passwd: String): Option[Manager] = database.withSession{  implicit session:Session =>
    { for(c<-Managers if c.email === email && c.passwd===Codecs.sha1("smeite"+passwd))yield(c) }.firstOption
  }

  def addTask(task:ManagerTask) = database.withSession{ implicit  session:Session =>
      ManagerTasks.autoInc.insert(task)
  }
  def countTask:Int = database.withSession{ implicit  session:Session =>
    Query(ManagerTasks.length).first()
  }
  def findTask(id:Long):Option[ManagerTask] = database.withSession{ implicit  session:Session =>
       { for(c<-ManagerTasks if c.id === id) yield c }.firstOption
  }
  def findTasks(currentPage: Int, pageSize: Int): Page[ManagerTask] = database.withSession {  implicit session:Session =>
    val totalRows=Query(ManagerTasks.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{ c<- ManagerTasks.sortBy(_.endDate desc) } yield(c)
    println(" q sql "+q.selectStatement)
    val tasks:List[ManagerTask]=q.list()
    Page[ManagerTask](tasks,currentPage,totalPages);
  }
  def findTasks(status:Int,currentPage: Int, pageSize: Int):List[ManagerTask] = database.withSession {  implicit session:Session =>
    val q=  for{ c<- ManagerTasks.sortBy(_.endDate desc) if c.status === status  } yield(c)
    println(" q sql "+q.selectStatement)
    q.list()

  }
  def modifyTask(task:ManagerTask)= database.withSession {  implicit session:Session =>
    (for(c<-ManagerTasks if c.id === task.id)yield(c)).update(task)
  }
  def modifyTask(taskId:Long,level:Int, leader:String, content:String,startDate:Date, endDate:Date,status:Int,note:String) = database.withSession {  implicit session:Session =>
  { for(c <- ManagerTasks if c.id === taskId )yield c.level ~ c.leader ~ c.content ~ c.startDate ~ c.endDate ~ c.status ~ c.note }.update((level,leader,content,startDate,endDate,status,note))
  }

  def modifyTaskStatus(taskId:Long,status:Int)  = database.withSession {  implicit session:Session =>
  { for(c <- ManagerTasks if c.id === taskId)yield c.status }.update(status)
  }

  def deleteTask(taskId:Long) =  database.withSession {  implicit session:Session =>
    { for(c <- ManagerTasks if c.id === taskId)yield c }.delete
  }

  def filterTasks(level:Option[Int],leader:Option[String],startDate:Option[Date],endDate:Option[Date],status:Option[Int],currentPage: Int ,pageSize:Int) = database.withSession {  implicit session:Session =>
    val currentDate = new Date(System.currentTimeMillis())
    var query =for( c<-ManagerTasks )yield c
    if(!level.isEmpty) query = query.filter(_.level === level.get)
    if(!leader.isEmpty) query = query.filter(_.leader like "%"+leader.get+"%")
    if(!startDate.isEmpty) query = query.filter(_.startDate < currentDate )
    if(!startDate.isEmpty) query = query.filter(_.endDate >  currentDate )
    if(!status.isEmpty) query = query.filter(_.status  === status.get)
    query = query.sortBy(_.addTime desc)
    println("sql " +query.selectStatement)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val tasks:List[ManagerTask]=  query.drop(startRow).take(pageSize).list()
    Page[ManagerTask](tasks,currentPage,totalPages);

  }

}
