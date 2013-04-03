package controllers

import play.api.mvc.{Action, Controller}
import models.tag.{ TagGoodses}
import models.tag.dao.TagDao
import users.Users

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-7
 * Time: 上午10:10
 *  搜索类
 */

object Search  extends Controller {
    /*
    * keywords 关键词
    * cate :分类：宝贝 、主题、人
    * */
  def search(keyword:String,t:String,p:Int) =Users.UserAction {user => implicit request =>
     if(t=="t"){
      Ok(views.html.search.theme())
     }else if (t =="u"){
       Ok(views.html.search.user())
     }else{
         val page = TagDao.findTagGoodses(keyword,p,54)
         Ok(views.html.search.baobei(user,page,keyword,t))
     }
  }
}
