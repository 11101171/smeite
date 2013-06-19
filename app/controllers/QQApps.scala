package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.tag.dao.TagDao
import models.goods.dao.{GoodsSQLDao, GoodsDao}
import models.user.dao.{UserSQLDao, UserDao}
import play.api.libs.json.Json

/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-6-5下午10:44
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 *
 *
 */
object QQApps extends Controller {



  /* 名称   tag
  * 全部  反季
  * 女装  反季女装
  * 男装  反季男装
  * 鞋子  反季鞋子
  * 品牌  反季品牌
  * */
  def  fanjijie(tag:String,s:Int,p:Int) = Action{ implicit request =>
    val page = TagDao.findGoodsesByTagName(tag,s,p,10)
    Ok(views.html.qqapps.fanjijie(page,tag,s))
  }

}
