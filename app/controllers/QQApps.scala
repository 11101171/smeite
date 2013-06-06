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
 */
object QQApps extends Controller {

  def fanji(tag:String,s:Int,p:Int) = Action{ implicit request =>
    var page:models.Page[(models.user.User,models.goods.Goods)] = null
   page = TagDao.findSimpleTagGoodses(tag,s,p,48)
    Ok(views.html.qqapps.fanji(page,tag,s))
  }

  def fanjiView(id:Long) = Action { implicit request =>
    val goods=GoodsDao.findById(id)
    if (goods.isEmpty)Ok(views.html.baobei.nofound())
    else{
      if(goods.get.status==0){
        Ok(views.html.baobei.nofound())
      }else{
      val comments = GoodsDao.findGoodsAssesses(id,1,10)
        val firstShareUser=UserDao.findFirstShareUser(id)
        Ok(views.html.qqapps.fanjiView(goods.get,firstShareUser,comments))
      }

    }
  }

  /*  增加喜欢数 */
  def  addLoveNum =  Action(parse.json) {  implicit request =>
    val goodsId = (request.body \ "goodsId").as[Long]
         GoodsSQLDao.updateLoveNum(goodsId,1)
    Ok(Json.obj( "code" -> "100", "msg" ->"成功"))
  }

  def  fanjijie(tag:String,s:Int,p:Int) = Action{ implicit request =>
    var page = TagDao.findGoodsesByTagName(tag,s,p,10)
    Ok(views.html.qqapps.fanjijie(page,tag,s))
  }

}
