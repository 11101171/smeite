package controllers.admin

import play.api.mvc.Controller
import models.vote.dao.VoteDao
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import play.api.libs.json.Json
import models.vote.Vote
import java.util.Date


case class VoteFormData(id:Option[Long],voteType:Int,validState:Int,thirdId:Option[Long],intro:Option[String],pic:Option[String],select1:Option[String],select2:Option[String],select3:Option[String],select4:Option[String],select5:Option[String],value1:Option[Int],value2:Option[Int],value3:Option[Int],value4:Option[Int],value5:Option[Int])
case class VoteBatchFormData(action:Int,ids:Seq[Long],url:Option[String])
case  class VoteFilterFormData(validState:Option[Int],startDate:Option[Date],endDate:Option[Date],currentPage:Option[Int])

object Votes extends Controller {
  val voteForm =Form(
    mapping(
      "id"->optional(longNumber),
      "voteType" -> number,
      "validState" -> number,
      "thirdId" -> optional(longNumber),
      "intro"->optional(text),
      "pic"->optional(text),
      "select1"->optional(text),
      "select2"->optional(text),
      "select3"->optional(text),
      "select4"->optional(text),
      "select5"->optional(text),
      "value1"->optional(number),
      "value2"->optional(number),
      "value3"->optional(number),
      "value4"->optional(number),
      "value5"->optional(number)
    )(VoteFormData.apply)(VoteFormData.unapply)
  )
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text)
    )(VoteBatchFormData.apply)(VoteBatchFormData.unapply)
  )
  val voteFilterForm =Form(
    mapping(
      "validState"->optional(number),
      "startDate"->optional(date("yyyy-MM-dd")),
      "endDate"->optional(date("yyyy-MM-dd")),
      "currentPage"->optional(number)
    )(VoteFilterFormData.apply)(VoteFilterFormData.unapply)
  )
  def votes(p:Int) = Managers.AdminAction{manager => implicit request =>
    val page =VoteDao.findVotes(p,20)
    Ok(views.html.admin.votes.votes(manager,page))
  }

  /* 添加 */
  def edit(id:Long) = Managers.AdminAction{manager => implicit request =>
    if(id == 0){
      Ok(views.html.admin.votes.edit(manager,voteForm))
    }else{
      val vote = VoteDao.findVote(id)
      Ok(views.html.admin.votes.edit(manager,voteForm.fill(VoteFormData(vote.id,vote.voteType,vote.validState,vote.thirdId,vote.intro,vote.pic,vote.select1,vote.select2,vote.select3,vote.select4,vote.select5,vote.value1,vote.value2,vote.value3,vote.value4,vote.value5))))
    }

  }
  def save = Managers.AdminAction{manager => implicit request =>
    voteForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.votes.edit(manager,formWithErrors)),
      data => {
        /*如果data id 为空，则保存数据 ，否则，则update数据*/
        if(data.id.isEmpty){
          VoteDao.addVote(Vote(None,data.voteType,data.validState,data.thirdId,data.intro,data.pic,data.select1,data.select2,data.select3,data.select4,data.select5,data.value1,data.value2,data.value3,data.value4,data.value5,None))
        }else{
          VoteDao.modifyVote(Vote(data.id,data.voteType,data.validState,data.thirdId,data.intro,data.pic,data.select1,data.select2,data.select3,data.select4,data.select5,data.value1,data.value2,data.value3,data.value4,data.value5,None))
        }
        Ok(views.html.admin.votes.edit(manager,voteForm.fill(data),"保存成功"))
      }
    )

  }
  def delete(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =VoteDao.deleteVote(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> "删除成功"))
    else Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))
  }
  def batch = Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      data => {
         if(data.action == 0){
          for(id<-data.ids){
            VoteDao.modifyVoteState(id,0)
          }
        }else if(data.action == 1){
          for(id<-data.ids){
            VoteDao.modifyVoteState(id,1)
          }
        }else if(data.action == 2){
          for(id<- data.ids){
            VoteDao.modifyVoteState(id,2)
          }
        }else if(data.action == 3){
          for(id<-data.ids){
            VoteDao.deleteVote(id)
          }
        }
        Redirect(data.url.getOrElse("/admin/votes/votes"))
      }
    )
  }

  def filter = Managers.AdminAction{manager => implicit request =>
    voteFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      data => {
        val page=VoteDao.filterVotes(data.validState,data.startDate,data.endDate,data.currentPage.getOrElse(1),50);
        Ok(views.html.admin.votes.filterVotes(manager,page,voteFilterForm.fill(data)))
      }
    )
  }

}
