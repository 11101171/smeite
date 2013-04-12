package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.libs.json.Json
import models.tag.dao.TagDao

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-9
 * Time: 上午10:46
 * ***********************
 * description:用于类的说明
 */

object Infos extends Controller {

  /*用户注册页面 注册条款*/
  def agreement = Users.UserAction {
    user => implicit request =>
      Ok(views.html.infos.agreement(user))
  }
  /*关于我们*/
  def aboutUs = Users.UserAction {
    user => implicit request =>
      Ok(views.html.infos.aboutUs(user))
  }
  /*联系我们*/
  def contactUs = Users.UserAction {
    user => implicit request =>
      Ok(views.html.infos.contactUs(user))
  }
  /*网站地图*/
  def siteMap = Users.UserAction { user => implicit request =>
    val tags = TagDao.filterTags(None,None,None,Some(1),None,None,1,100)
      Ok(views.html.infos.siteMap(user,tags))
  }
  /* 友情链接*/
  def friends = Users.UserAction {
    user => implicit request =>
      Ok(views.html.infos.friends(user))
  }

 /* 商品信息 纠错提醒 todo */
  def jiucuo =Action {
    Ok(Json.obj("code"->"100","message"->"success"))
 }

  /*集分宝介绍*/
  def jifenbao = Users.UserAction { user => implicit request =>
      Ok(views.html.infos.jifenbao(user))
  }
  /*食豆介绍*/
  def shiDou = Users.UserAction { user => implicit request =>
    Ok(views.html.infos.shiDou(user))
  }

  // 淘宝客的验证
  def xtaoAuth =Action{
    Ok(views.html.infos.xtaoAuth())
  }
  /*微博验证*/
  def weiboAuth =Action{
    Ok(views.html.infos.weiboAuth())
  }
  /*qzone 验证*/
  def qzone =Action{
    Ok(views.html.infos.qzone())
  }

  /* baidu sitemap*/
  def baiduSiteMap=Action{
    Ok(views.html.infos.baiduSiteMap())
  }

}
