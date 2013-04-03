package controllers.users


import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import play.api.cache.Cache
import java.io.File
import net.coobird.thumbnailator.Thumbnails
import java.net.URL
import play.api.libs.json.Json
import models.user.User
import  models.user.dao.UserDao
import play.api.Play.current
import utils.Utils


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-21
 * Time: 上午10:35
 * ***********************
 * description:用于类的说明
 */

object UsersAccount  extends Controller {

  val passwdForm = Form(
    tuple(
      "oldPassword" ->nonEmptyText,
      "password" -> nonEmptyText,
      "password2" -> nonEmptyText
    ) verifying ("两次密码输入不一致……", fields => fields._2 == fields._3)

  )
  val addrForm =Form(
   tuple(
     "trueName" ->optional(text),
     "province" -> optional(text),
     "city" -> optional(text),
     "addressDetail" ->optional(text),
     "postcode" -> optional(text),
     "cellphone" ->optional(text),
     "alipay" ->optional(text)
   )
  )

  val baseForm =Form(
  tuple(
    "nickname"-> nonEmptyText,
    "sex" -> number,
    "year"->text,
    "month"->text,
    "day"->text,
    "province" -> text,
    "city"->text,
   "blog"->text,
  "weixin"->text,
  "intro" ->text
  
  )
  )


  /* user account 用户账户 基本信息 */
  def base = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
     else {
      if(user.get.email.isEmpty){
        Redirect(controllers.users.routes.UsersRegLogin.before())
      }else {
      val profile =UserDao.findProfile(user.get.id.get)
      Ok(views.html.users.account.base(user,baseForm.fill((user.get.name,profile.gender,profile.birth.getOrElse(""),"","",profile.province.getOrElse(""),profile.city.getOrElse(""),profile.blog.getOrElse(""),profile.weixin.getOrElse(""),profile.intro.getOrElse("")))))
    }
    }

  }
  /* user account 用户账号 基本信息 保存*/
  def saveBase =Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      baseForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.users.account.base(user,formWithErrors)) ,
      fields =>{
        val birth:String=fields._3+"|"+fields._4+"|"+fields._5 
        UserDao.modifyBase(user.get.id.get,fields._1,fields._10,fields._2,birth,fields._6,fields._7,fields._8,fields._9)
        /*处理缓存*/
        Cache.remove(session.get("user").get)
        var u:User=UserDao.findById(user.get.id.get)
        Cache.set(u.id.get.toString,u);
        Ok(views.html.users.account.base(Some(u),baseForm.fill((fields._1,fields._2,fields._3,fields._4,fields._5,fields._6,fields._7,fields._8,fields._9,fields._10)),"基本资料保存成功"))
      }
      )
      
    }
  }

  /* user account 用户账户 账号绑定 */
  def bind = Users.UserAction {  user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else  Ok(views.html.users.account.bind(user))

  }

  /* user account 用户账户 修改密码 */
  def passwd = Users.UserAction { user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else   Ok(views.html.users.account.passwd(user,passwdForm))
  }
  /* user account 用户账号 修改密码 保存*/
  def modifyPasswd  = Users.UserAction { user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else  {
      passwdForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.users.account.passwd(user,formWithErrors)),
        fields =>{
          /*判断旧密码是否正确*/
          var oldUser =UserDao.authenticate(user.get.email.get,fields._1)
          if(oldUser.isEmpty) Ok(views.html.users.account.passwd(user,passwdForm,"当前密码不正确"))
         else {
            UserDao.modifyPasswd(user.get.id.get,fields._2)
            Ok(views.html.users.account.passwd(user,passwdForm,"密码修改成功"))
          }
        }
      )
    }
  }

  /* user account 用户账户 邀请好友 */
  def invite = Users.UserAction {  user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else   Ok(views.html.users.account.invite(user))
  }

  /* user account 用户账户 我的奖品 */
  def award = Users.UserAction {    user => implicit request =>
   if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
   else  Ok(views.html.users.account.award(user))

  }

  /* user account 用户账户 收货地址 */
  def address = Users.UserAction {   user => implicit request =>
   if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
   else {
      val profile =UserDao.findProfile(user.get.id.get)
     Ok(views.html.users.account.address(user,addrForm.fill((profile.receiver,profile.province,profile.city,profile.street,profile.postCode,profile.phone,profile.alipay))))

   }
  }
  /* user account 用户账户 收货地址  保存*/
  def saveAddress= Users.UserAction {   user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else  {
      addrForm.bindFromRequest.fold(
        formWithErrors => {
          val profile =UserDao.findProfile(user.get.id.get)
          BadRequest(views.html.users.account.address(user,formWithErrors.fill((profile.receiver,profile.province,profile.city,profile.street,profile.postCode,profile.phone,profile.alipay))))
        } ,
        fields =>{

             UserDao.modifyAddr(user.get.id.get, fields._1.getOrElse(""),fields._2.getOrElse(""),fields._3.getOrElse(""),fields._4.getOrElse(""),fields._5.getOrElse(""),fields._6.getOrElse(""),fields._7.getOrElse(""))

            Ok(views.html.users.account.address(user,addrForm.fill(fields._1,fields._2,fields._3,fields._4,fields._5,fields._6,fields._7),"收货地址保存成功"))

        }
      )
    }
  }




}
