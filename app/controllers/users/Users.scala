package controllers.users


import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.forum.Topic
import models.forum.dao.TopicDao
import models.theme.Theme
import models.theme.dao.ThemeDao
import models.goods.Goods
import models.theme.Theme
import models.user.User
import models.user.dao.UserDao
import models.Page


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-19
 * Time: 上午9:37
 * ***********************
 * description:用户页面显示，采用类似于权限控制的规则，对外显示的页面和用户能够看到的页面一致，只是在局部上 某些功能有额外的处理
 */

object Users extends Controller {



  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def UserAction(f:Option[User] => Request[AnyContent] => Result) = {
      Action{ request =>
       val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        f(user)(request)
      }
  }





  /*user 首页*/
  def home(id:Long) =UserAction { user => implicit request =>
    val author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    val goodses =UserDao.findShareGoodses(id,1,9);
    val  page =UserDao.findLoveThemes(author.id.get,1,9)
    val  users = UserDao.findFollows(id,1,24)
    Ok(views.html.users.home(user,author,goodses,page,users,authorStatic) )
  }

  /*user trend 动态*/
  def trend(id:Long) = UserAction{ user => implicit request =>
    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    Ok(views.html.users.trend(user,author,authorStatic))
  }

  /*user baobei  喜欢的宝贝*/
  def baobei(id:Long,t:String,p:Int) = UserAction{ user => implicit request =>
    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    if(t=="share"){
     val page =UserDao.findShareGoodses(id,p,9);
      Ok(views.html.users.baobei(user,author,page,t,authorStatic))
    }else{
      val page =UserDao.findLoveGoodses(id,p,9);
      Ok(views.html.users.baobei(user,author,page,t,authorStatic))
    }


  }
  /*user   square 广场 */
  def square(id:Long,t:String,p:Int) = UserAction{ user => implicit request =>

    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    val   topics:List[(Long, String, String, Long, String, Int, Int, Int)]=TopicDao.recommendTopics(8)
     if (t=="love"){
         val page =UserDao.findLoveTopics(id,p)
       Ok(views.html.users.square(user,author,page,t,topics,authorStatic))
     }
    else{
      val page=TopicDao.findUserTopics(id,p)
      Ok(views.html.users.square(user,author,page,t,topics,authorStatic))
    }
   
  }

  /*user theme 主题*/
  def theme(id:Long,t:String,p:Int) = UserAction{ user => implicit request =>

    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    if(t=="my"){
      val  page =UserDao.findPostThemes(author.id.get,p)
      Ok(views.html.users.theme(user,author,page,t,authorStatic))
    } else {
      val  page =UserDao.findLoveThemes(author.id.get,p)
      Ok(views.html.users.theme(user,author,page,t,authorStatic))
    }

  }
  /*user fans 粉丝*/
  def fans(id:Long,p:Int) = UserAction{ user => implicit request =>
    val num:Int=8;   //默认是查找大于0的推荐用户
    val credtis=0;
    val authorStatic=UserDao.findStatic(id);
    val  author=UserDao.findById(id)
    val page=UserDao.findFans(id,p,10)
    val users:List[User]= if(user.isEmpty)UserDao.recommendUser(credtis,num)else UserDao.findInterestedUser(user.get.id.get,num);
    Ok(views.html.users.fans(user,author,page,users,authorStatic))
  }

  /*user 我关注的
  * @p :页
  * @cate： 类型 主题 话题 人
  * */
  def follow(id:Long,p:Int) = UserAction{ user => implicit request =>
    val num:Int =8;
    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    val page=UserDao.findFollows(id,p,10)
    val users:List[User]= if(user.isEmpty)UserDao.recommendUser(0,num)else UserDao.findInterestedUser(user.get.id.get,num);
    Ok(views.html.users.follow(user,author,page,users,authorStatic))
  }

  /*添加 关注人
  * code :100 成功 101 参数错误  103 重复关注 111 关注人数已达到上限 300 用户未登录 444 被禁止关注
  * */
  def addFollow(followId:Long) = UserAction{ user => implicit request =>
        if(user.isEmpty)  Ok(Json.obj("code" -> "300", "isYourFans"->"false","message" -> "亲，你还没有登录呢" ))
       else if(user.get.status ==4) Ok(Json.obj("code" -> "444", "isYourFans"->"false","message" -> "亲，你被禁止了" ))
       else {
          val follow =UserDao.checkFollow(followId,user.get.id.get);
          if(!follow.isEmpty) Ok(Json.obj("code" -> "103", "isYourFans"->"false","message" -> "重复关注了"  ))
          else {
            UserDao.addFollow(followId,user.get.id.get)
            val followed= UserDao.checkFollow(followId,user.get.id.get)
            if(followed.isEmpty) Ok(Json.obj("code" -> "100", "isYourFans"->"false","message" -> "关注成功" ))
             else  Ok(Json.obj("code" -> "100", "isYourFans"->"true","message" -> "关注成功"  ))
          }
        }

  }
  /* 取消关注 code 100 成功  101 参数错误 300 用户未登录*/
  def removeFollow(followId:Long) =  UserAction{ user => implicit request =>
    if(user.isEmpty)  Ok(Json.obj("code" -> "300", "isYourFans"->"false","message" -> "亲，你还没有登录呢" ))
    else{
      UserDao.deleteFollow(followId,user.get.id.get)
     Ok(Json.obj("code" -> "100","message" -> "取消成功" ))
    }
  }
  /* 取消关注 code 100 成功  101 参数错误 300 用户未登录*/
  def removeFans(fansId:Long) =  UserAction{ user => implicit request =>
    if(user.isEmpty)  Ok(Json.obj("code" -> "300", "isYourFans"->"false","message" -> "亲，你还没有登录呢"))
    else{
      UserDao.deleteFans(user.get.id.get,fansId)
      Ok(Json.obj("code" -> "100","message" ->"取消成功"))
    }
  }

  /* 用户可能感兴趣的人 这里有比较复杂的算法就先不实现了*/
  def  getUserInterested(uid:Long) = UserAction{ user => implicit request =>

       Ok("todo")
    }

  /* 猜测用户可能感兴趣的 人 组 ,例如换一组可能感兴趣的人 这里有比较复杂的算法就先不实现了*/
 def changeFriends = UserAction{ user => implicit request =>
   Ok("todo")
 }

  /*我的求鉴定*/
  def appraisal(id:Long,p:Int) = UserAction{ user => implicit request =>
    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    val page =Page[Goods](Nil,p,1);
    val userLoveGoods =UserDao.findLoveGoodses(id,1,6)
    Ok(views.html.users.appraisal(user,author,page,userLoveGoods,authorStatic))
  }

  /*用户的集分宝 动态*/
  def credits(id:Long) = UserAction{ user => implicit request =>
    val  author=UserDao.findById(id)
    val authorStatic=UserDao.findStatic(id);
    Ok(views.html.users.credits(user,author,authorStatic))
  }

}
