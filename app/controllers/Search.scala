package controllers

import play.api.mvc.{Cookie, Action, Controller}
import models.tag.{TagGoodses}
import models.tag.dao.TagDao
import users.Users
import utils.{TaobaoConfig, Utils}
import models.user.User
import models.user.dao.UserDao

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-7
 * Time: 上午10:10
 * 搜索类
 */

object Search extends Controller {
  /*
  * keywords 关键词
  * cate :分类：宝贝 、主题、人
  * */
  def search(keyword: String, p: Int) = Users.UserAction { user => implicit request =>
      if (Utils.isNumber(keyword)) {
        Ok("heee" + keyword)
      } else {
        val page = TagDao.findTagGoodses(keyword, p, 54)
        Ok(views.html.search.baobei(user, page, keyword))
      }
  }
  /* 淘宝返利 convert*/
  def convertProduct(numIid:Long) = Users.UserAction { user => implicit request =>
    val id= if(!user.isEmpty) user.get.id.get else 0
    val timestamp= String.valueOf(System.currentTimeMillis)
    val sign=TaobaoConfig.getSign(timestamp)
    Ok(views.html.ugc.api.convertProduct(numIid,id)).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))


  }
}
