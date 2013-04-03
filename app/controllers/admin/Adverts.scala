package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.advert.dao.AdvertDao
import play.api.libs.json.Json

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-27
 * Time: 下午2:42
 * ***********************
 * description:用于类的说明
 */

object  Adverts  extends  Controller{

  val advertForm =Form(
    tuple(
      "id" ->longNumber,
      "title" -> optional(text),
      "pic"->optional(text),
      "spic"->optional(text),
      "link" -> optional(text),
      "note"->optional(text)
    )
  )

  val textForm = Form(
    tuple(
      "id" ->longNumber,
      "title" -> optional(text),
      "content" -> optional(text),
      "link" -> optional(text),
      "note"->optional(text)
    )
  )



  /*频道管理*/
  def channel(name:String)=Managers.AdminAction{manager => implicit request =>
     val list =AdvertDao.findPositions(name);
    Ok(views.html.admin.adverts.channel(manager,list))
  }



  def list(positionCode:String,advertType:Int) = Managers.AdminAction{ manager => implicit request =>
        val adverts=AdvertDao.findAdverts(positionCode)

        /*0图片广告 1 文字广告  2商品广告 3 用户 4 主题  */
       if (advertType==0) Ok(views.html.admin.adverts.picAdverts(manager,adverts)) ;
       else if(advertType==1)Ok(views.html.admin.adverts.textAdverts(manager,adverts))  ;
       else   Ok(views.html.admin.adverts.productAdverts(manager,adverts,advertType))
  }
    /*编辑图片广告 */
  def edit(id:Long) = Managers.AdminAction{manager => implicit request =>
     val advert =AdvertDao.findById(id);
   Ok(views.html.admin.adverts.edit(manager,advertForm.fill(advert.id.get,advert.title,advert.pic,advert.spic,advert.link,advert.note)))
  }
  /*编辑文字广告*/
  def textEdit(id:Long) = Managers.AdminAction{ manager => implicit request =>
    val advert =AdvertDao.findById(id);
    Ok(views.html.admin.adverts.textEdit(manager,textForm.fill(advert.id.get,advert.title,advert.content,advert.link,advert.note)))
  }

  /*保存*/
  def save = Managers.AdminAction{ manager => implicit request =>
      advertForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.admin.adverts.edit(manager,formWithErrors)),
        fields => {
       //   println("sssssssss               "+fields._2.getOrElse(""))
         AdvertDao.modifyAdvert(fields._1,fields._2.getOrElse(""),fields._3.getOrElse(""),fields._4.getOrElse(""),fields._5.getOrElse(""),fields._6.getOrElse(""))
         Ok(views.html.admin.adverts.edit(manager,advertForm.fill((fields._1,fields._2,fields._3,fields._4,fields._5,fields._6)),"保存成功"))
        }
      )
  }
  /*保存 text*/
  def textSave = Managers.AdminAction{ manager => implicit request =>
    textForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.adverts.textEdit(manager,formWithErrors)),
      fields => {
        //   println("sssssssss               "+fields._2.getOrElse(""))
        AdvertDao.modifyAdvert(fields._1,fields._2.getOrElse(""),fields._3.getOrElse(""),fields._4.getOrElse(""),fields._5.getOrElse(""))
        Ok(views.html.admin.adverts.textEdit(manager,textForm.fill((fields._1,fields._2,fields._3,fields._4,fields._5)),"保存成功"))
      }
    )
  }

  /* 只需要添加商品 user theme 的id即可*/
  def  updateProductAdvert(id:Long,thirdId:Long) =   Managers.AdminAction{ manager => implicit request =>
    println("id "+ id +"  third id "+thirdId)
    val  result =AdvertDao.modifyAdvert(id,thirdId)
    if (result>0)Ok(Json.obj("code"->"100","message"->"success"))
    else Ok(Json.obj("code"->"104","message"->"更新失败"))
  }



}
