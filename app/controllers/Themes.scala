package controllers

import controllers.users.Users
import play.api.data.Forms._
import play.api.data.Form
import models.tag.dao.TagDao
import play.api.libs.json.Json
import ugc.Product
import ugc.API.ProductFormat
import play.api.mvc.{Action, Controller}
import models.theme._
import models.theme.dao.{ThemeSQLDao, ThemeDao}
import models.user.{User,  UserStatus}
import play.api.cache.Cache
import play.api.Play.current
import models.goods.dao.GoodsDao
import models.user.dao.{UserSQLDao, UserDao}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-22
 * Time: 下午4:08
 * ***********************
 * description:用于类的说明
 */



object Themes extends Controller {
  val themeForm =Form(
    tuple(
      "id"->optional(longNumber),
      "name" -> nonEmptyText,
      "intro" -> optional(text) ,
      "cid"->optional(number),
      "tags" -> optional(text)
    )
  )


  /*创作主题*/
  def edit(id:Long)= Users.UserAction {user => implicit request =>
    if (user.isEmpty){
        Redirect(controllers.users.routes.UsersRegLogin.login)
    } else{
      if(id ==0)Ok(views.html.themes.edit(user,themeForm))
      else {
        val theme=ThemeDao.findById(id);
        if (theme.isEmpty)Ok(views.html.themes.edit(user,themeForm))
        else Ok(views.html.themes.edit(user,themeForm.fill((theme.get.id,theme.get.name,theme.get.intro,Some(theme.get.cid),theme.get.tags))))
      }
    }

  }

  /*保存主题*/
  def save = Users.UserAction {user => implicit request =>
      themeForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.themes.edit(user,formWithErrors)),
        theme => {
          /*如果theme id 为空，则保存数据 ，否则，则update数据*/
          /*处理tag标签 如果此标签库里没有这个标签，则添加一个新的标签，如果有，则在这个标签中 记录一次*/
          if(!theme._5.isEmpty){
            val tags = theme._5.get.split(",")
            for(t<-tags){
              val tag=TagDao.findByName(t);
              if (tag.isEmpty) TagDao.addTag(t)
              else TagDao.modifyTag(tag.get.id.get, tag.get.addNum+1);
            }
          }
          if(theme._1.isEmpty){
           val id= ThemeDao.addTheme(theme._2,theme._3,user.get.id.get,user.get.name,theme._4.getOrElse(5),theme._5)
            Redirect(controllers.routes.Themes.view(id))
          }else{
            ThemeDao.modifyTheme(theme._1.get,theme._2,theme._3.getOrElse("none"),theme._4.getOrElse(5),theme._5.getOrElse("none"))
            Redirect(controllers.routes.Themes.view(theme._1.get))
          }

        }
      )

  }

  /*查看主题
  * 需要判断主题是否存在，如果不存在，则跳转到theme list页面
  * */
  def view(id:Long) = Users.UserAction { user => implicit request =>
     val theme=ThemeDao.findById(id)
     if (theme.isEmpty) Redirect(controllers.routes.Pages.miss)
     else {
       val themeStyle=ThemeDao.findStyle(id)
      val goodses =ThemeDao.findGoodses(id);

       /*1 判定当前用户是否存在 不存在则查询*/
       /*2 判定theme的uid 与当前的用户是否一致，不一致则需要查询theme的作者，传到前台页面，一致，则把当前用户直接传到前台页面*/
       if (user.isEmpty){
          val author =  UserDao.findById(theme.get.uid)
         val themes = UserDao.findSimplePostThemes(author.id.get)
         Ok(views.html.themes.view(user,author,theme.get,themeStyle.get,goodses,themes))
       }else{
         if (user.get.id.get == theme.get.uid){
           val themes = UserDao.findSimplePostThemes(user.get.id.get)
           Ok(views.html.themes.view(user,user.get,theme.get,themeStyle.get,goodses,themes))
         } else {
           val author =  UserDao.findById(theme.get.uid)
           val themes = UserDao.findSimplePostThemes(author.id.get)
           Ok(views.html.themes.view(user,author,theme.get,themeStyle.get,goodses,themes))
         }
       }
     }
  }
  /*显示该主题下的讨论*/
  def board(id:Long,currentPage:Int)= Users.UserAction { user => implicit request =>
    val theme=ThemeDao.findById(id)
    if (theme.isEmpty) Redirect(controllers.routes.Pages.miss)
    else {
      /*1 判定当前用户是否存在 不存在则查询*/
      /*2 判定theme的uid 与当前的用户是否一致，不一致则需要查询theme的作者，传到前台页面，一致，则把当前用户直接传到前台页面*/
      val themeStyle=ThemeDao.findStyle(id)
      val list=ThemeDao.findDiscusses(id,currentPage,10);
      val users=UserDao.findThemeUsers(id,1,16);
      if (user.isEmpty){
        val author =  UserDao.findById(theme.get.uid)
        Ok(views.html.themes.board(user,author,theme.get,themeStyle.get,list,users))
      }else{
        if (user.get.id.get == theme.get.uid){
          Ok(views.html.themes.board(user,user.get,theme.get,themeStyle.get,list,users))
        } else {
          val author =  UserDao.findById(theme.get.uid)
          Ok(views.html.themes.board(user,author,theme.get,themeStyle.get,list,users))
        }
      }

    }
  }
  /*显示该主题下的喜欢的人*/
  def likes(id:Long,currentPage:Int)= Users.UserAction { user => implicit request =>
    val theme=ThemeDao.findById(id)
    if (theme.isEmpty) Redirect(controllers.routes.Pages.miss)
    else {
      val themeStyle=ThemeDao.findStyle(id)
      val page=UserDao.findThemeUsers(id,currentPage)
      if (user.isEmpty){
        val author =  UserDao.findById(theme.get.uid)
        Ok(views.html.themes.likes(user,author,theme.get,themeStyle.get,page))
      }else{
        if (user.get.id.get == theme.get.uid){
          Ok(views.html.themes.likes(user,user.get,theme.get,themeStyle.get,page))
        } else {
          val author =  UserDao.findById(theme.get.uid)
          Ok(views.html.themes.likes(user,author,theme.get,themeStyle.get,page))
        }
      }

    }
  }



  /*删除主题 *100 删除成功 * 101 请求失败 * 103 主题不存在 104 你还没有登录 105 没有权限删除  * */
    def delete = Action(parse.json) {  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if(user.isEmpty)  Ok(Json.obj("code" -> "104", "message" -> "你还没有登录" ))
    else {
      val id = (request.body \ "themeId").as[Long]
      val dataType =(request.body \ "dataType").as[String]
      if(dataType=="my"){
        val theme=ThemeDao.findById(id);
        if(theme.isEmpty) Ok(Json.obj("code" -> "103", "message" -> "主题不存在"))
        else if(user.get.id.get != theme.get.uid) Ok(Json.obj("code" -> "104", "message" ->"你没有权限删除"))
        else {
          val result = ThemeDao.deleteTheme(id)
            /* 删除用户相关数据 */
          UserSQLDao.updatePostThemeNum(user.get.id.get,-1)
          if (result>0)Ok(Json.obj( "code" -> "100", "message" ->"删除成功"))
          else Ok(Json.obj("code" -> "101", "message" ->"数据库请求删除失败"))
        }
      }else {
        val loveTheme=UserDao.checkLoveTheme(user.get.id.get,id)
        if(loveTheme.isEmpty) Ok(Json.obj("code" -> "103", "message" -> "喜欢的主题不存在"))
        else {
          val result = UserDao.deleteLoveTheme(user.get.id.get,id)
          if (result>0)Ok(Json.obj( "code" -> "100", "message" ->"删除成功"))
          else Ok(Json.obj("code" -> "101", "message" ->"数据库请求删除失败"))
        }
      }
    }
  }

  /*主题美化 100 成功 101保存不成功*/
  def editStyle =Action(parse.json) {implicit request =>
     val themeId =(request.body \ "themeId").asOpt[Long]
    val  pageBgColor = (request.body \ "pageBgColor").asOpt[String]
     val pageBgImage  = (request.body \ "pageBgImage").asOpt[String]
     val pageBgRepeat  = (request.body \ "pageBgRepeat").asOpt[String]
     val pageBgPosition  = (request.body \ "pageBgPosition").asOpt[String]
     val pageBgAttachment  = (request.body \ "pageBgAttachment").asOpt[String]

     if(themeId.isEmpty){
       Ok(Json.obj( "code" -> "101", "message" ->"theme id 为空，不能保存"))
     }else{
       ThemeDao.modifyStyle(themeId.get,pageBgColor.getOrElse("#F9F9EF"),pageBgImage.getOrElse(""),pageBgRepeat.getOrElse("no-repeat"),pageBgPosition.getOrElse("center top"),pageBgAttachment.getOrElse("scroll"))

       Ok(Json.obj("code" -> "100", "pagePic"->pageBgImage, "message" ->"保存不成功，请重新提交试试"))
     }



  }
  /* 快速评价*/
 def quickReply =Action(parse.json){  implicit request =>
         val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
         if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
         else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止评论"))
         else {
           val themeId = (request.body \ "themeId").asOpt[Long]
           val  content =(request.body \ "content").asOpt[String]
           if(content.isEmpty || themeId.isEmpty ){
           Ok(Json.obj("code" -> "101", "message" ->"亲，是不是没有输入内容？请重新提交试试"))
           }else{
             ThemeDao.addDiscuss(themeId.get,user.get.id.get,user.get.name,content.getOrElse("none"),1)
             Ok(Json.obj("code" -> "100","status"->UserStatus(user.get.status).toString,"content"->content.get, "message" ->"亲，评论成功"))
           }
         }

 }
  /*评价*/
  def reply =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止评论"))
    else{
      val themeId = (request.body \ "themeId").asOpt[Long]
      val  content =(request.body \ "content").asOpt[String]
      if (content.isEmpty || themeId.isEmpty ){
        Ok(Json.obj("code" -> "101", "message" ->"亲，是不是没有输入内容？请重新提交试试") )
      } else{
        ThemeDao.addDiscuss(themeId.get,user.get.id.get,user.get.name,content.getOrElse("none"),1)
        Ok(Json.obj("code" -> "100","status"->UserStatus(user.get.status).toString,"content"->content.get, "message" ->"亲，评论成功"))
      }
    }
  }


  /*用户点击喜欢操作*/
  /*删除主题 *100 删除成功 * 101 请求失败 * 103 喜欢重复 200 没有登录  * */
  def addFollow  = Action(parse.json) { implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>Cache.getOrElse[User](u){
      UserDao.findById(u.toLong)
    })
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"你还没有登录" ))
    else {
      val themeId = (request.body \ "themeId").asOpt[Long]
         if(themeId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
        else{
           val loveTheme=UserDao.checkLoveTheme(user.get.id.get,themeId.get);
           if(!loveTheme.isEmpty) Ok(Json.obj("code" -> "103", "message" ->"你已经喜欢了"))
           else { UserDao.addLoveTheme(user.get.id.get,themeId.get);   Ok(Json.obj("code" -> "100", "message" ->"成功"))}

         }

    }
  }
  
  def removeFollow = Action(parse.json) {implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>Cache.getOrElse[User](u){
      UserDao.findById(u.toLong)
    })
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"你还没有登录"))
    else {
      val themeId = (request.body \ "themeId").asOpt[Long]
      if(themeId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        UserDao.deleteLoveTheme(user.get.id.get,themeId.get);
        Ok(Json.obj("code" -> "100", "message" ->"成功"))
      }


    }
  }
  /* 添加主题下的商品*/
  def addGoods =Action(parse.json) { implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>Cache.getOrElse[User](u){
      UserDao.findById(u.toLong)
    })
    if(user.isEmpty) Ok(Json.obj("code" -> "200", "message" -> "你还没有登录"))
    else {
      val themeId = (request.body \ "themeId").as[Long]
      val goodsId = (request.body \ "goodsId").as[Long]
      val pic = (request.body \ "pic").as[String]
      val themeGoods = ThemeDao.checkGoods(themeId,goodsId);
      val goods =GoodsDao.findById(goodsId)
      val product=Product(goods.get.id,goods.get.numIid,goods.get.nick,goods.get.name,None,goods.get.price,goods.get.pic+"_210x210.jpg",Nil,goods.get.detailUrl,Nil)
      if (themeGoods.isEmpty){
        ThemeDao.addGoods(themeId,goodsId,pic)
        Ok(Json.obj("code" -> "100","product"->Json.toJson(product), "message" ->"成功"))
      }else  Ok(Json.obj( "code" -> "104", "message" -> "主题已经有这个商品了"))

    }
  }
  /* 删除主题下的商品
  * tothink 1 考虑是否验证商品是否属于某个用户的专题商品   2 考虑是否验证themeid 和 goodsId ？？
  *
  * */
  def removeGoods= Action(parse.json) { implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>Cache.getOrElse[User](u){
      UserDao.findById(u.toLong)
    })
    if(user.isEmpty) Ok(Json.obj("code" -> "200", "message" -> "你还没有登录"))
    else {
      val themeId = (request.body \ "themeId").as[Long]
      val goodsId = (request.body \ "goodsId").as[Long]
      ThemeDao.deleteGoods(themeId,goodsId)
      Ok(Json.obj("code" -> "100", "message" -> "成功"))
    }
  }

  def checkThemeLoveState = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if(user.isEmpty)  Ok(Json.obj("code" -> "300","message" -> "亲，你还没有登录呢" ))
    else{
      val themeId=(request.body \ "themeId").asOpt[Long];
      if (themeId.isEmpty)Ok(Json.obj("code"->"104","message"->"param id is empty"))
      else{
        val loveTheme=UserDao.checkLoveTheme(user.get.id.get,themeId.get);
        if(!loveTheme.isEmpty)  Ok(Json.obj("code" -> "100","message" -> "已关注" ))
        else Ok(Json.obj("code" -> "101","message" -> "未关注" ))
      }

    }
  }


 }
