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
        Redirect(controllers.ugc.routes.API.convertProduct(keyword.toLong))
      } else {
        val page = TagDao.findTagGoodses(keyword, p, 54)
        Ok(views.html.search.baobei(user, page, keyword))
      }
  }



}
