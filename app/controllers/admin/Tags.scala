package controllers.admin
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.tag.{Tag, TagGroup}
import models.tag.dao.TagDao


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-3
 * Time: 上午11:03
 * ***********************
 * description:用于类的说明
 */
case class TagGroupFormData(id:Option[Long],name:String,pic:String,cid:Option[Int],hotIndex:Int,isVisible:Boolean, seoTitle:Option[String], seoKeywords:Option[String], seoDesc:Option[String],intro:Option[String])
case class TagFormData(id:Option[Long],name:String,cid:Option[Int],groupId:Option[Long],groupName:Option[String],hotIndex:Int,isTop:Boolean,isHighlight:Boolean,sortNum:Int,checkState:Int, seoTitle:Option[String], seoKeywords:Option[String], seoDesc:Option[String])
case class BatchFormData(action:Int,ids:Seq[Long],nums:Seq[Int],url:Option[String])
case class GroupFilterFormData(name:Option[String],cid:Option[Int],isVisible:Option[Boolean],currentPage:Option[Int])
case  class TagFilterFormData(name:Option[String],cid:Option[Int],groupId:Option[Long],checkState:Option[Int],isTop:Option[Boolean],isHighlight:Option[Boolean],currentPage:Option[Int])
case class TagGoodsFilterFormData(name:Option[String],cid:Option[Int],checkState:Option[Int],currentPage:Option[Int])

case class TagGoodsBatchFormData(action:Int,ids:Seq[Long],tagNames:Seq[String],sortNums:Seq[Int],url:Option[String])

object  Tags extends Controller {
  val groupForm =Form(
    mapping(
      "id"->optional(longNumber),
      "name" -> nonEmptyText,
      "pic" -> nonEmptyText,
      "cid" -> optional(number) ,
      "hotIndex" ->number,
      "isVisible"->boolean,
      "seoTitle" -> optional(text),
      "seoKeywords" -> optional(text),
      "seoDesc" ->optional(text),
      "intro"->optional(text)
    )(TagGroupFormData.apply)(TagGroupFormData.unapply)
  );

  val tagForm =Form(
    mapping(
      "id"->optional(longNumber),
      "name" -> nonEmptyText,
      "cid" -> optional(number),
      "groupId" -> optional(longNumber),
      "groupName"->optional(text),
      "hotIndex" ->number,
      "isTop"->boolean,
      "isHighlight"->boolean,
      "sortNum"->number,
      "checkState"->number,
      "seoTitle" -> optional(text),
      "seoKeywords" -> optional(text),
      "seoDesc" ->optional(text)
    )(TagFormData.apply)(TagFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "nums"->seq(number) ,
      "url"->optional(text)
    )(BatchFormData.apply)(BatchFormData.unapply)
  )
  val tagGoodsBatchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "tagNames"->seq(text) ,
      "sortNums"->seq(number) ,
      "url"->optional(text)
    )(TagGoodsBatchFormData.apply)(TagGoodsBatchFormData.unapply)
  )
      /*检索标签组*/
  val groupFilterForm =Form(
    mapping(
      "name"->optional(text),
      "cid"->optional(number),
      "isVisible"->optional(boolean),
     "currentPage"->optional(number)
    )(GroupFilterFormData.apply)(GroupFilterFormData.unapply)
  )

  /*检索标签*/
  val tagFilterForm =Form(
    mapping(
      "name"->optional(text),
      "cid"->optional(number),
      "groupId"->optional(longNumber),
      "checkState"->optional(number),
      "isTop"->optional(boolean),
      "isHighlight"->optional(boolean),
      "currentPage"->optional(number)
    )(TagFilterFormData.apply)(TagFilterFormData.unapply)
  )

  /*检索 标签 产品*/
  val tagGoodsFilterForm =Form(
    mapping(
      "name"->optional(text),
      "cid"->optional(number),
      "checkState"->optional(number),
      "currentPage"->optional(number)
    )(TagGoodsFilterFormData.apply)(TagGoodsFilterFormData.unapply)
  )

  /*标签组过滤管理*/
 def filterGroups = Managers.AdminAction{ manager => implicit request =>
    groupFilterForm.bindFromRequest.fold(
        formWithErrors =>Ok("something wrong"),
        group => {
       //   println(group.cid + "   " +group.name + "    " +group.isVisible )
          val page=TagDao.filterGroups(group.name,group.cid,group.isVisible,group.currentPage.getOrElse(1),50);
          Ok(views.html.admin.tags.filterGroups(manager,page,groupFilterForm.fill(group)))
        }
    )

  }

  /* group action 管理*/
  def batchGroups = Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      group => {
           if(group.action == 0){
             for((id,i)<-group.ids.view.zipWithIndex){
                 TagDao.modifyGroup(id,group.nums(i))
             }
           }else if(group.action == 1){
             for(id<-group.ids){
               TagDao.modifyGroup(id,true)
             }
           }else if(group.action == 2){
             for(id<-group.ids){
               TagDao.modifyGroup(id,false)
             }
           }
          Redirect(group.url.getOrElse("/admin/tags/groupList"))
      }
    )
  }


  /*标签组管理 */
  def groupList(currentPage:Int)=Managers.AdminAction{ manager => implicit request =>
      val page=TagDao.findGroups(currentPage,50);
      Ok(views.html.admin.tags.groupList(manager,page))
  }
  /*编辑标签组*/
  def editGroup(gid:Long)=Managers.AdminAction{ manager => implicit request =>
      if (gid==0) Ok(views.html.admin.tags.editGroup(manager,groupForm))
      else {
        val tagGroup=TagDao.findGroup(gid);
        if(tagGroup.isEmpty) Ok(views.html.admin.tags.editGroup(manager,groupForm))

        else  Ok(views.html.admin.tags.editGroup(manager,groupForm.fill(TagGroupFormData(tagGroup.get.id,tagGroup.get.name,tagGroup.get.pic,tagGroup.get.cid,tagGroup.get.hotIndex,tagGroup.get.isVisible,tagGroup.get.seoTitle,tagGroup.get.seoKeywords,tagGroup.get.seoDesc,tagGroup.get.intro))))
      }
  }
  /*保存标签组*/
  def saveGroup=Managers.AdminAction{ manager => implicit request =>
      groupForm.bindFromRequest.fold(
    formWithErrors => BadRequest(views.html.admin.tags.editGroup(manager,formWithErrors)),
    group => {
      /*如果group id 为空，则保存数据 ，否则，则update数据*/
      if(group.id.isEmpty){
        TagDao.addGroup(TagGroup(None,group.name,group.pic,group.intro,group.cid,group.hotIndex,group.isVisible,0,group.seoTitle,group.seoKeywords,group.seoDesc,None,None))
      }else{
        TagDao.modifyGroup(TagGroup(group.id,group.name,group.pic,group.intro,group.cid,group.hotIndex,group.isVisible,0,group.seoTitle,group.seoKeywords,group.seoDesc,None,None))
      }
      Redirect(controllers.admin.routes.Tags.groupList(1))
    }
  )
  }
  /*删除Tag group*/
  def deleteGroup(gid:Long)=Managers.AdminAction{
    manager => implicit request =>
      val result =TagDao.deleteGroup(gid)
      if (result >0) Ok(Json.obj( "code" -> "100", "message" -> "删除成功"))
      else Ok(Json.obj( "code" -> "101", "message" -> "删除失败" ))

  }

  /* 修改 */
  def updateGroup(gid:Long,num:Int)=Managers.AdminAction{  manager => implicit request =>
      val result =TagDao.modifyGroup(gid,num)
      if (result >0) Ok(Json.obj( "code" -> "100", "message" -> "更新成功"))
      else Ok(Json.obj( "code" -> "101", "message" -> "更新失败" ))

  }

  /*标签管理*/
  def list(currentPage:Int)=Managers.AdminAction{
    manager => implicit request =>
      val page=TagDao.findAll(currentPage,50);
      Ok(views.html.admin.tags.list(manager,page))
  }

  /* 批量处理 tag*/
  def batchTags=Managers.AdminAction{ manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for((id,i)<-batch.ids.view.zipWithIndex){
            TagDao.modifyTagSortNum(id,batch.nums(i))
          }
        }else if(batch.action == 1){
          for(id<-batch.ids){
            TagDao.modifyTagCheckState(id,1)
          }
        }else if(batch.action == 2){
          for(id<-batch.ids){
            TagDao.modifyTagCheckState(id,2)
          }
        }else if(batch.action == 3){
          for(id<-batch.ids){
            TagDao.modifyTagTopState(id,true)
          }
        }else if(batch.action == 4){
          for(id<-batch.ids){
            TagDao.modifyTagTopState(id,false)
          }
        }else if(batch.action == 5){
          for(id<-batch.ids){
            TagDao.modifyTagHighlightState(id,true)
          }
        } else if(batch.action == 6){
          for(id<-batch.ids){
            TagDao.modifyTagHighlightState(id,false)
          }
        }else if(batch.action == 7){
          for(id<-batch.ids){
            TagDao.deleteTag(id)
          }
        }
        Redirect(batch.url.getOrElse("/admin/tags/list"))
      }
    )

  }
  /* 过滤处理 tag */
  def filterTags=Managers.AdminAction{ manager => implicit request =>
    tagFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      tag => {
        val page=TagDao.filterTags(tag.name,tag.cid,tag.groupId,tag.checkState,tag.isTop,tag.isHighlight,tag.currentPage.getOrElse(1),50);
        Ok(views.html.admin.tags.filterTags(manager,page,tagFilterForm.fill(tag)))
      }
    )

  }
  def getSimpleGroups(cid:Int)=Managers.AdminAction{manager => implicit request =>
      val groups=TagDao.findSimpleGroups(cid)
      //  for(g<-groups){println(g._1 +" : " +g._2)}
     Ok(views.html.admin.tags.simpleGroups(groups))
  }
  /*编辑标签 先判断标签是否存在*/
  def editTag(tid:Long)=Managers.AdminAction{ manager => implicit request =>
      if (tid==0) Ok(views.html.admin.tags.editTag(manager,tagForm))
      else {

        val tag=TagDao.findById(tid);
        if(tag.isEmpty) Ok(views.html.admin.tags.editTag(manager,tagForm))

        else  Ok(views.html.admin.tags.editTag(manager,tagForm.fill(TagFormData(tag.get.id,tag.get.name,tag.get.cid,tag.get.groupId,tag.get.groupName,tag.get.hotIndex,tag.get.isTop,tag.get.isHighlight,tag.get.sortNum,tag.get.checkState,tag.get.seoTitle,tag.get.seoKeywords,tag.get.seoDesc))))
      }
  }
  /*保存标签组*/
  def saveTag=Managers.AdminAction{
    manager => implicit request =>
      tagForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.admin.tags.editTag(manager,formWithErrors)),
        tag => {
          /*如果tag id 为空，则保存数据 ，否则，则update数据*/
          if(tag.id.isEmpty){
            TagDao.addTag(Tag(None,tag.name,tag.cid,tag.groupId,tag.groupName,tag.hotIndex,1,tag.isTop,tag.isHighlight,tag.sortNum,tag.checkState,tag.seoTitle,tag.seoKeywords,tag.seoDesc,None,None))
          }else{
            TagDao.modifyTag(Tag(tag.id,tag.name,tag.cid,tag.groupId,tag.groupName,tag.hotIndex,1,tag.isTop,tag.isHighlight,tag.sortNum,tag.checkState,tag.seoTitle,tag.seoKeywords,tag.seoDesc,None,None))
          }
          Redirect(controllers.admin.routes.Tags.list(1))
        }
      )
  }
  /*删除Tag group*/
  def deleteTag(tid:Long)=Managers.AdminAction{
    manager => implicit request =>
      val result =TagDao.deleteTag(tid)
      if (result >0) Ok(Json.obj("code" -> "100", "message" ->"删除成功" ))
      else Ok(Json.obj("code" -> "101", "message" ->"删除失败" ))

  }
  /*标签 宝贝管理*/
  def tagGoodses(p:Int)= Managers.AdminAction{ manager => implicit request =>
    val page =TagDao.findAllTagGoodses(p,50)
    Ok(views.html.admin.tags.tagGoodses(manager,page))
  }
  /*删除*/
  def deleteTagGoods(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =TagDao.deleteGoods(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("删除成功" )))
    else Ok(Json.obj( "code" -> "101", "message" -> ("删除失败" )))
  }
  def checkTagGoods(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =TagDao.modifyGoods(id,1)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("审核成功" )))
    else Ok(Json.obj( "code" -> "101", "message" -> ("审核失败" )))
  }
  /* 标签宝贝 批量管理*/
  def batchTagGoodses= Managers.AdminAction{ manager => implicit request =>
    tagGoodsBatchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            TagDao.deleteGoods(id)
          }
        }else if(batch.action == 1){
          for(id<-batch.ids){
            TagDao.modifyGoods(id,1)
          }
        }else if(batch.action == 2){
          for(id<-batch.ids){
            TagDao.modifyGoods(id,2)
          }
        }else if (batch.action == 3){
          for((id,i)<-batch.ids.view.zipWithIndex){
            TagDao.modifyGoods(id,batch.tagNames(i))
          }
        }else if (batch.action == 4){
          for((id,i)<-batch.ids.view.zipWithIndex){
            TagDao.modifyTagGoods(id,batch.sortNums(i))
          }
        }
        Redirect(batch.url.getOrElse("/admin/tags/tagGoods"))
      }
    )
  }

  /* 标签宝贝 检索管理*/
  def filterTagGoodses= Managers.AdminAction{ manager => implicit request =>
    tagGoodsFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      data => {
       val page=TagDao.filterTagGoodses(data.name,data.cid,data.checkState,data.currentPage.getOrElse(1),50);
       Ok(views.html.admin.tags.filterTagGoodses(manager,page,tagGoodsFilterForm.fill(data)))

      }
    )
  }


}
