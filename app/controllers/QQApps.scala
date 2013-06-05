package controllers

import play.api.mvc.Controller
import controllers.users.Users
import models.tag.dao.TagDao
import models.goods.dao.GoodsDao
import models.user.dao.UserDao

/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-6-5下午10:44
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
object QQApps extends Controller {

  def fanji(tag:String,s:Int,p:Int) = Users.UserAction{ user => implicit request =>
    var page:models.Page[(models.user.User,models.goods.Goods)] = null
   page = TagDao.findSimpleTagGoodses(tag,s,p,48)
    Ok(views.html.qqapps.fanji(user,page,tag,s))
  }

  def fanjiView(id:Long) = Users.UserAction {user => implicit request =>
    val goods=GoodsDao.findById(id)
    if (goods.isEmpty)Ok(views.html.baobei.nofound())
    else{
      if(goods.get.status==0){
        Ok(views.html.baobei.nofound())
      }else{
        val firstShareUser=UserDao.findFirstShareUser(id)
        val tags = TagDao.findGoodsTags(goods.get.id.get)
        Ok(views.html.qqapps.fanjiView(user,goods.get,firstShareUser,tags))
      }

    }
  }
}
