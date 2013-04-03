package controllers.admin
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json.Json
import models.Page
import models.theme.dao.ThemeDao
import models.user.dao.UserSQLDao

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-10-20
 * Time: 下午5:20
 * Email:zuosanshao@qq.com
 */

case class ThemeFormData(
                          id:Long,
                          name:String,
                          pic:Option[String],
                          hotIndex:Int,
                          isRecommend:Boolean,
                          seoTitle:Option[String],
                          seoKeywords:Option[String],
                          seoDesc:Option[String]
                          )

case class ThemeBatchFormData(action:Int,ids:Seq[Long],url:Option[String])
case class ThemeGoodsBatchFormData(action:Int,themeId:Long,ids:Seq[Long],sortNums:Seq[Int])
case  class ThemeFilterFormData(name:Option[String],cid:Option[Int],isRecommend:Option[Boolean],currentPage:Option[Int])
case  class DiscussFilterFormData(name:Option[String],checkState:Option[Int],currentPage:Option[Int])


object Themes extends Controller {



  val themeForm =Form(
    mapping(
      "id"->longNumber,
      "name" -> nonEmptyText,
      "pic" -> optional(text),
      "hotIndex" -> number ,
      "isRecommend"->boolean,
      "seoTitle" -> optional(text) ,
      "seoKeywords" -> optional(text),
      "seoDesc" -> optional(text)
    )(ThemeFormData.apply)(ThemeFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text)
    )(ThemeBatchFormData.apply)(ThemeBatchFormData.unapply)
  )
  val batchThemeGoodsForm =Form(
    mapping(
      "action"->number,
      "themeId"->longNumber,
      "ids"->seq(longNumber),
      "sortNums"->seq(number)
    )(ThemeGoodsBatchFormData.apply)(ThemeGoodsBatchFormData.unapply)
  )
  /*检索 theme*/
 val themeFilterForm =Form(
    mapping(
      "name"->optional(text),
      "cid"->optional(number),
      "isRecommend"->optional(boolean),
      "currentPage"->optional(number)
    )(ThemeFilterFormData.apply)(ThemeFilterFormData.unapply)
  )
  /*检索 theme discuss*/
  val discussFilterForm =Form(
    mapping(
      "name"->optional(text),
      "checkState"->optional(number),
      "currentPage"->optional(number)
    )(DiscussFilterFormData.apply)(DiscussFilterFormData.unapply)
  )

  /*list theme 主题管理*/
  def list(currentPage:Int)=Managers.AdminAction{
    manager => implicit request =>
      val themes=ThemeDao.findAll(currentPage)
      Ok(views.html.admin.themes.list(manager,themes))
  }
  /*theme在后台只能有编辑功能*/
  def editTheme(id:Long)= Managers.AdminAction{manager => implicit request =>
    val theme=ThemeDao.findById(id);
    if(theme.isEmpty) Ok(" 查询 id 为 "+ id +"主题不存在" )
    else  Ok(views.html.admin.themes.editTheme(manager,themeForm.fill(ThemeFormData(theme.get.id.get,theme.get.name,Some(theme.get.pic),theme.get.hotIndex,theme.get.isRecommend,theme.get.seoTitle,theme.get.seoKeywords,theme.get.seoDesc))))
  }
  /*保存theme*/
  def saveTheme = Managers.AdminAction{manager => implicit request =>
    themeForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.themes.editTheme(manager,formWithErrors)),
      themeData => {
          ThemeDao.modifyTheme(themeData.id,themeData.name,themeData.pic,themeData.hotIndex,themeData.isRecommend,themeData.seoTitle,themeData.seoKeywords,themeData.seoDesc)
        Redirect(controllers.admin.routes.Themes.list(1))
      }
    )
  }

  /*删除主题*/
  def deleteTheme(id:Long)=Managers.AdminAction{ manager => implicit request =>
      val result =ThemeDao.deleteTheme(id)
      if (result >0) Ok(Json.obj("code" -> "100", "message" ->"删除成功"))

      else Ok(Json.obj( "code" -> "101", "message" ->"删除失败" ))

  }

  /* 批量处理 */
  def batchThemes=Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            ThemeDao.modifyThemeVisible(id,false)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            ThemeDao.modifyThemeVisible(id,true)
          }
        } else if (batch.action ==2){
          for(id<-batch.ids){
            ThemeDao.deleteTheme(id)
          }
        }
        Redirect(batch.url.getOrElse("/admin/themes/list"))
      }
    )
  }

  /*用户过滤*/
  def filterThemes = Managers.AdminAction{ manager => implicit request =>
    themeFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      theme => {
        val page=ThemeDao.filterThemes(theme.name,theme.cid,theme.isRecommend,theme.currentPage.getOrElse(1),50);
        Ok(views.html.admin.themes.filterThemes(manager,page,themeFilterForm.fill(theme)))
      }
    )
  }

  /* discusses*/
  def discusses(p:Int)  = Managers.AdminAction{ manager => implicit request =>
    val page = ThemeDao.findAllDiscusses(p,50)
    Ok(views.html.admin.themes.discusses(manager,page))
  }

  /*删除讨论*/
  def deleteDiscuss(id:Long)=Managers.AdminAction{ manager => implicit request =>
    val result =ThemeDao.deleteDiscuss(id)
    if (result >0) Ok(Json.obj("code" -> "100", "message" ->"删除成功"))
    else Ok(Json.obj( "code" -> "101", "message" ->"删除失败" ))
  }

  /* 批量处理 */
  def batchDiscusses=Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            ThemeDao.deleteDiscuss(id)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            ThemeDao.modifyDiscussCheckState(id,1)
          }
        } else if (batch.action ==2){
          for(id<-batch.ids){
            ThemeDao.modifyDiscussCheckState(id,2)
          }
        }
        Redirect(batch.url.getOrElse("/admin/themes/discusses"))
      }
    )
  }

  /*用户过滤*/
  def filterDiscusses = Managers.AdminAction{ manager => implicit request =>
    discussFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      theme => {
        val page=ThemeDao.filterDiscusses(theme.name,theme.checkState,theme.currentPage.getOrElse(1),50);
        Ok(views.html.admin.themes.filterDiscusses(manager,page,discussFilterForm.fill(theme)))
      }
    )
  }

  /* list theme goods*/
  def listGoodses(id:Long) = Managers.AdminAction{ manager => implicit request =>
           val goodses =ThemeDao.findThemeGoodses(id)
          Ok(views.html.admin.themes.listGoodses(manager,id,goodses))
  }

  def batchThemeGoodses = Managers.AdminAction{ manager => implicit request =>
    batchThemeGoodsForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for((id,i)<-batch.ids.view.zipWithIndex){
            ThemeDao.modifyThemeGoods(id,batch.sortNums(i))
          }
        }
        Redirect("/admin/themes/listGoodses/"+batch.themeId)
      }
    )
  }
}
