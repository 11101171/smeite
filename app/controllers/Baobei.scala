package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.goods._
import play.api.libs.json._
import models.theme._
import  models.theme.dao.ThemeDao
import models.tag.dao.TagDao
import play.api.libs.json.JsSuccess
import models.theme.ThemeGoods
import play.api.libs.json.JsString
import scala.Some
import play.api.libs.json.JsNumber
import models.user.User
import models.goods.GoodsAssess
import play.api.libs.json.JsObject
import models.user.dao.UserDao
import models.goods.dao.GoodsSQLDao
import models.goods.dao.GoodsDao
import models.advert.dao.AdvertDao

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-24
 * Time: 上午9:54
 * ***********************
 * description:用于类的说明
 */
/*simple theme for json*/
case class SimpleTheme(id:Long,name:String)
object Baobei extends Controller {
  /*json*/
  implicit  object SimpleThemeFormat extends Format[SimpleTheme]{
    def writes(o: SimpleTheme): JsValue = JsObject(
      List(
        "id"->JsNumber(o.id),
        "name"->JsString(o.name)
      )
    )
    def reads(json: JsValue): JsResult[SimpleTheme] = JsSuccess(SimpleTheme(
      (json \ "id").as[Long],
      (json \ "name").as[String]
    )
    )
  }


  /*宝贝下架*/
  def offSell = Users.UserAction {
    user => implicit request =>
      Ok(views.html.baobei.offSell(user))
  }

  /*宝贝喜欢*/
  def addFollow =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else{

      val goodsId=(request.body \ "goodsId").asOpt[Long]
      if (goodsId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else {
        val userGoods=UserDao.checkLoveGoods(user.get.id.get,goodsId.get)
        if (userGoods.isEmpty){
           UserDao.addLoveGoods(user.get.id.get,goodsId.get)
          Ok(Json.obj("code"->"100","message"->"喜欢成功了"))
        } else{
          Ok(Json.obj("code"->"103","message"->"你已经喜欢了"))
        }
      }
    }

  }
  def removeFollow =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else{

      val goodsId=(request.body \ "goodsId").asOpt[Long]
      if (goodsId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else {
          UserDao.removeLoveGoods(user.get.id.get,goodsId.get)
          Ok(Json.obj("code"->"100","message"->"喜欢成功了"))
    }

  }
  }

  /*获取用户的themes*/
  def getUserThemes =Users.UserAction{ user => implicit request =>
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else {
      val themes=UserDao.findSimplePostThemes(user.get.id.get).map(x=>SimpleTheme(x.id.get,x.name));
      Ok(Json.obj("code"->"100","userThemes"->Json.toJson(themes)))
    }
  }

  /*创建新的themes*/
  def createTheme=Action(parse.json){ implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else {
      (request.body \ "name").asOpt[String].map { name =>
        val id = ThemeDao.addTheme(name,user.get.id.get,user.get.name)
        Ok(Json.obj("code"->"100","themeId"->id))
      }.getOrElse {
        Ok(Json.obj("code"->"104","message"->"name参数不存在"))
      }

    }
  }
  /* 喜欢时，添加到theme中 并发表评论  */
  def  addAssess = Action(parse.json){ implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong))
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else{
     val productId =  (request.body \ "productId").asOpt[Long];
     val themeIds=(request.body \ "themeIds").asOpt[String];
     val userComment= (request.body \ "userComment").asOpt[String];
    val  productPic = (request.body \ "productPic").asOpt[String];
      if (productId.isEmpty || themeIds.isEmpty)  Ok(Json.obj("code"->"104","message"->"product Id is empty"))
     else{
        val ids=themeIds.get.split(",").map(x=>x.toLong);
        for (id<-ids) ThemeDao.addGoods(id,productId.get,productPic.get)
      }
    if(!userComment.isEmpty) GoodsDao.addAssess(productId.get,user.get.id.get,user.get.name,userComment.get,1)

      Ok(Json.obj("code"->"100","message"->"success"))
    }
  }

  /* 在输入框中评论
  *
  * */
  def addComment =Action(parse.json){  implicit  request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if (user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登录"))
    else {
      if (user.get.status==4)Ok(Json.obj("code"->"444","message"->"您被禁止登陆"))
      val productId = (request.body \ "productId").asOpt[Long]
      val content = (request.body \ "content").asOpt[String]
      val tags = (request.body \ "tags").as[String]
      val worth = (request.body \ "worth").as[Int]
      val bought = (request.body \ "bought").as[Int]
      if (productId.isEmpty || content.isEmpty) Ok(Json.obj("code"->"104","message"->"评论内容不能为空"))
      else {
        /*处理tags 这里需要处理下tag goods  */
       for (name <- tags.split(",")){
         val tag= TagDao.findByName(name)
         if (tag.isEmpty){
           TagDao.addTag(name)
         } else {
           val tagGoods = TagDao.checkGoods(name,productId.get)
           if (tagGoods.isEmpty) TagDao.addGoods(name,productId.get)
           TagDao.modifyTag(tag.get.id.get,tag.get.addNum+1)
         }

       }

        val isWorth = if (worth==1) true else false;
        val isBought = if (bought==1) true else false;
        GoodsDao.addAssess(GoodsAssess(None,productId.get,user.get.id.get,user.get.name,content.get,isWorth,isBought,1,None))
        Ok(Json.obj("code"->"100","message"->"SUCCESS"))
      }
    }
  }
  /* 获取goods的评论*/
  def getComments(goodsId:Long,p:Int)=Action{    implicit  request =>
      val page = GoodsDao.findGoodsAssesses(goodsId,p,10)
    Ok(views.html.baobei.getComments(page,goodsId))
  }

  /*  获取你可能喜欢的商品
  *  前期仅仅是简单的根据喜欢数量的多少进行显示
  * */
  def guessUserLikes = Action{    implicit  request =>
    val page=GoodsDao.guessUserLikes(1,24);
    Ok(views.html.baobei.guessUserLikes(page))
  }

  /* 获取跟本商品相关的标签商品*/
  def findRelativeBaobeis(goodsId:Long) =Action{    implicit  request =>
     val page =TagDao.findRelativeTagGoodses(goodsId)
     Ok(views.html.baobei.guessUserLikes(page))
  }


  def view(id:Long) = Users.UserAction {user => implicit request =>
    val goods=GoodsDao.findById(id)
    if (goods.isEmpty)Ok(views.html.baobei.nofound())
    else{
    //  if(goods.get.status==0){
    //    Ok(views.html.baobei.nofound())
   //   }else{
        val firstShareUser=UserDao.findFirstShareUser(id)
        val tags = TagDao.findGoodsTags(goods.get.id.get)
        Ok(views.html.baobei.view(user,goods.get,firstShareUser,tags))
   //   }

    }
  }




}
