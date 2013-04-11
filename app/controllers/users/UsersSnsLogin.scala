package controllers.users
import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import models.user.User
import  models.user.dao.UserDao
import play.api.cache.Cache
import play.api.Play.current
import play.api.libs.json.Json._
import play.api.libs.json.Json
import play.api.Play
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils


/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-15上午9:15
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/

object UsersSnsLogin extends Controller {
  private def taobaoAppkey = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_appkey")).getOrElse("21136607")

  /*
  * 第三方登录 目前只开发qq weibo taobao
  * */
  def snsLogin(snsType:String,backType:String)=Action{ implicit  request=>
    if(snsType=="taobao"){
       Redirect("https://oauth.taobao.com/authorize?response_type=code&client_id=21136607&redirect_uri=http://smeite.com/user/taobao/registered/"+backType)
    }else if(snsType=="qzone"){
      Redirect(" https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=100344510&state=qq&redirect_uri=http://smeite.com/user/qzone/registered/"+backType)
    }else if (snsType=="sina"){
      Redirect("https://api.weibo.com/oauth2/authorize?client_id=1464940004&response_type=code&redirect_uri=http://smeite.com/user/sina/registered/"+backType)
    }
    else{
      Ok("亲，我们只支持淘宝帐号登录 qq帐号登录 和 新浪微博登陆哦…… ")
    }

  }
  def  registered(snsType:String,code:String,backType:String)=Action{   implicit request =>
     if(snsType=="taobao"){
       /*第一步 获取token,淘宝的第三方登录比较成熟，可以一步到位，token和用户信息一起返回*/
        val post = new HttpPost("https://oauth.taobao.com/token?grant_type=authorization_code&client_id=21136607&client_secret=c03472fbe94e9fa882c44948e8709320&code="+code+"&redirect_uri=http://smeite.com/user/taobao/getToken")
         val client =new DefaultHttpClient();
         val resp= client.execute(post)
         val entity=resp.getEntity;
       //println("ssssssssssssssssssssssssssss "+EntityUtils.getContentCharSet(entity))

         val r=EntityUtils.toString(entity,"UTF-8")
       //println("ssdfdf         " +r);
         val json=Json.parse(r)
         val nick=(json \ "taobao_user_nick").as[String];
         val openId = (json \ "taobao_user_id").as[String];
         /*
         * 第二步 查找用户是否存在，存在则缓存，不存在则保存 再缓存
         *
         * */
         val user=UserDao.checkSnsUser(3,openId);
         var uid:Long = 0;
        if (user.isEmpty){
             UserDao.addSnsUser( java.net.URLDecoder.decode(nick,"UTF-8"),3,openId,"/images/user/default.jpg");
          val u=UserDao.checkSnsUser(3,openId);
              Cache.set(u.get.id.get.toString,u);
          uid = u.get.id.get
        }else {
          Cache.set(user.get.id.get.toString,user);
          uid = user.get.id.get
        }
       /*
       * 第三步 返回result
       * */
       if (backType=="asyn"){
         Ok(views.html.users.snsLogin.asynLogin("qzone")).withSession("user" -> uid.toString)
       }else{
         Redirect(controllers.users.routes.Users.home(uid)).withSession("user" -> uid.toString)
       }
     }
      /*
      * qzone 登录 第一步获取token 第二步 获取用户的openId  第三步 获取用户信息  第四步 处理用户信息
      * */
     else if(snsType=="qzone"){
       val get = new HttpGet("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=100344510&client_secret=da726b47d8660a016d2e334ea27844fc&code="+code+"&redirect_uri=http://smeite.com/user/qzone/registered&state=qq")
       val client =new DefaultHttpClient();
       val resp= client.execute(get)
       val entity=resp.getEntity;
       val r=EntityUtils.toString(entity)
    //   val json=Json.parse(r)
       val token =r.split("&").head.split("=").last


       val getMe = new HttpGet("https://graph.qq.com/oauth2.0/me?access_token="+token)
       val client3 =new DefaultHttpClient();
       val resp3 = client3.execute(getMe)
       val entity3=resp3.getEntity;
       val me=EntityUtils.toString(entity3)
       val json =Json.parse(me.split(" ")(1))
       val openId = (json \ "openid").as[String]

       val getInfo = new HttpGet("https://graph.qq.com/user/get_user_info?access_token="+token+"&oauth_consumer_key=100344510&openid="+openId+"&format=json")
       getInfo.addHeader("charset","UTF-8")
       val client2 =new DefaultHttpClient();
       val resp2= client2.execute(getInfo)
   //    resp2.getAllHeaders.foreach(x=>println (x.getName  + " : " +x.getValue))

       val entity2=resp2.getEntity;
       //println("ssssssssssssssssssssssssssss"+EntityUtils.getContentCharSet(entity2))
       val info=EntityUtils.toString(entity2,"UTF-8")

       val json2 =Json.parse(info)
       val nickName= (json2 \ "nickname").as[String]
       val pic = (json2 \ "figureurl_1").as[String]


       /*查找用户信息*/
       val user =UserDao.checkSnsUser(1,openId);
       var uid:Long = 0;
       if (user.isEmpty){
         UserDao.addSnsUser(nickName,1,openId,pic)
         val u=UserDao.checkSnsUser(1,openId);
         Cache.set(u.get.id.get.toString,u);
         uid = u.get.id.get
       }else {
         Cache.set(user.get.id.get.toString,user);
         uid = user.get.id.get
       }
        /* 处理result */
       if (backType=="asyn"){
         Ok(views.html.users.snsLogin.asynLogin("qzone")).withSession("user" -> uid.toString)
       }else{
         Redirect(controllers.users.routes.Users.home(uid)).withSession("user" -> uid.toString)
       }
     }
     /* 新浪微博登陆 */
     else if(snsType=="sina"){
       val get = new HttpPost("https://api.weibo.com/oauth2/access_token?client_id=1464940004&client_secret=52cb22ab45f9764a3b329578c70b89df&grant_type=authorization_code&redirect_uri=http://smeite.com/user/sina/registered&state=sina&code="+code)
       val client =new DefaultHttpClient();
       val resp= client.execute(get)
       val entity=resp.getEntity;
       val r=EntityUtils.toString(entity)
       val json =Json.parse(r);
       val assessToken=(json \ "access_token").as[String];
       val openId =(json \ "uid").as[String];
       val getInfo = new HttpGet("https://api.weibo.com/2/users/show.json?access_token="+assessToken+"&uid="+openId)
       val client2 =new DefaultHttpClient();
       val resp2= client2.execute(getInfo)
       val entity2=resp2.getEntity;
       //println("ssssssssssssssssssssssssssss "+EntityUtils.getContentCharSet(entity2))
       val r2=EntityUtils.toString(entity2)
       //println(r2)
       val json2 =Json.parse(r2)
       val nickName = (json2 \ "name").as[String]
       val pic = (json2 \ "profile_image_url").as[String]
        /* 查找用户信息 */
        /*查找用户信息*/
        val user =UserDao.checkSnsUser(2,openId);
       var uid:Long = 0;
       if (user.isEmpty){
         UserDao.addSnsUser(nickName,2,openId,pic)
         val u=UserDao.checkSnsUser(2,openId);
         Cache.set(u.get.id.get.toString,u);
         uid = u.get.id.get
       }else {
         Cache.set(user.get.id.get.toString,user);
         uid = user.get.id.get
       }
       /* 处理result */
       if (backType=="asyn"){
         Ok(views.html.users.snsLogin.asynLogin("sina")).withSession("user" -> uid.toString)
       }else{
         Redirect(controllers.users.routes.Users.home(uid)).withSession("user" -> uid.toString)
       }

     } else{
       Ok("可能获取token出错了")
     }

  }


}
