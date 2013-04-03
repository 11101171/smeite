package controllers.admin

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.admin.ManagerTask
import models.admin.dao.ManagerDao
import java.sql.Date
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-21
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
case class TaskFormData(
                          id:Option[Long],
                          level: Int,
                          leader:String,
                          content: String,
                          startDate:Date,
                          endDate:Date,
                          status:Int,
                          note:Option[String]
                          )

case class TaskFilterFormData(
                                level:Option[Int],
                                leader:Option[String],
                                startDate:Option[Date],
                                endDate:Option[Date],
                                status:Option[Int],
                                currentPage:Option[Int]
                                )

object Tasks extends Controller {

  val taskForm =Form(
    mapping(
      "id"->optional(longNumber),
      "level" -> number,
      "leader" -> text,
      "content" -> text,
      "startDate" -> sqlDate("yyyy-MM-dd"),
      "endDate"-> sqlDate("yyyy-MM-dd"),
      "status" -> number ,
      "note" -> optional(text)
    )(TaskFormData.apply)(TaskFormData.unapply)
  )

  /*检索标签*/
  val taskFilterForm =Form(
    mapping(
      "level" -> optional(number),
      "leader" -> optional(text),
      "startDate" -> optional(sqlDate("yyyy-MM-dd")) ,
      "endDate"-> optional(sqlDate("yyyy-MM-dd")),
      "status" -> optional(number),
      "currentPage"->optional(number)
    )(TaskFilterFormData.apply)(TaskFilterFormData.unapply)
  )


  def list(p:Int) = Managers.AdminAction{manager => implicit request =>
      val tasks = ManagerDao.findTasks(p,20)
      Ok(views.html.admin.tasks.list(manager,tasks))
  }
  def view(id:Long) = Managers.AdminAction{manager => implicit request =>
    val task=ManagerDao.findTask(id);
    Ok(views.html.admin.tasks.view(manager,task.get))
  }
  def edit(id:Long) = Managers.AdminAction{manager => implicit request =>
    if (id==0) Ok(views.html.admin.tasks.edit(manager,taskForm))
    else {
      val task=ManagerDao.findTask(id);
      if(task.isEmpty) Ok(views.html.admin.tasks.edit(manager,taskForm))
      else  Ok(views.html.admin.tasks.edit(manager,taskForm.fill(TaskFormData(task.get.id,task.get.level,task.get.leader,task.get.content,task.get.startDate,task.get.endDate,task.get.status,task.get.note))))
    }

  }
  def delete(id:Long) = Managers.AdminAction{manager => implicit request =>
      val result =ManagerDao.deleteTask(id)
      if (result >0) Ok(Json.obj( "code" -> "100", "message" -> "删除成功"))
      else Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))

  }
  def save = Managers.AdminAction{manager => implicit request =>
    taskForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.tasks.edit(manager,formWithErrors)),
      task => {
        /*如果group id 为空，则保存数据 ，否则，则update数据*/
        if(task.id.isEmpty){
          ManagerDao.addTask(ManagerTask(None,task.level,task.leader,task.content,task.startDate,task.endDate,task.status,task.note,None))
        }else{
          ManagerDao.modifyTask(ManagerTask(task.id,task.level,task.leader,task.content,task.startDate,task.endDate,task.status,task.note,None))
        }
        Redirect(controllers.admin.routes.Tasks.list(1))
      }
    )
  }
  def filterTasks = Managers.AdminAction{manager => implicit request =>
    taskFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      task => {
        val page=ManagerDao.filterTasks(task.level,task.leader,task.startDate,task.endDate,task.status,task.currentPage.getOrElse(1),20);
        Ok(views.html.admin.tasks.filter(manager,page,taskFilterForm.fill(task)))
      }
    )
  }
}
