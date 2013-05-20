package controllers.users


import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import play.api.cache.Cache
import java.io.File
import net.coobird.thumbnailator.Thumbnails
import java.net.URL
import play.api.libs.json.Json
import models.user.{UserCreditRecord, User}
import models.user.dao.{UserSQLDao, UserDao}
import play.api.Play.current
import utils.Utils
import java.sql.Timestamp


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-21
 * Time: 上午10:35
 * ***********************
 * description:用于类的说明
 */
case  class BaseFormData(
                              nickName:String,
                              email:Option[String],
                              sex:Int,
                              year:Option[String],
                              month:Option[String],
                              day:Option[String],
                              blog:Option[String],
                              weixin:Option[String],
                              intro:Option[String]
                              )
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
  mapping(
    "nickname"-> text,
    "email"->optional(text),
    "sex" -> number,
    "year"->optional(text),
    "month"->optional(text),
    "day"->optional(text),
   "blog"->optional(text),
  "weixin"->optional(text),
  "intro" ->optional(text)
  )(BaseFormData.apply)(BaseFormData.unapply)
  )

  val alipayForm = Form (
  tuple(
    "alipay" ->optional(text),
    "phone" ->optional(text),
    "weixin" ->optional(text)
  )
  )


  /* user account 用户账户 基本信息 */
  def base = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
     else {
      val profile =UserDao.findProfile(user.get.id.get)
      Ok(views.html.users.account.base(user,baseForm.fill(BaseFormData(user.get.name,user.get.email,profile.gender,profile.birth,None,None,profile.blog,profile.weixin,profile.intro))))
    }

  }
  /* user account 用户账号 基本信息 保存*/
  def saveBase =Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      baseForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.users.account.base(user,formWithErrors)) ,
      data =>{
        /*uid:Long,name:String,intro:String,email:String,gender:Int, birth:String, province:String, city:String, blog:String, weixin:String*/
        val birth:String=data.year.getOrElse("")+"|"+data.month.getOrElse("")+"|"+data.day.getOrElse("")
        UserDao.modifyBase(user.get.id.get,data.nickName,data.email.getOrElse(""),data.intro.getOrElse(""),data.sex,birth,data.blog.getOrElse(""),data.weixin.getOrElse(""))
        Ok(views.html.users.account.base(user,baseForm.fill(data),"基本资料保存成功"))
      }
      )
      
    }
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



  /* 设置支付宝 */
  def payment = Users.UserAction {    user => implicit request =>
   if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
   else {
     val profile =UserDao.findProfile(user.get.id.get)
     Ok(views.html.users.account.payment(user,alipayForm.fill((profile.alipay,profile.phone,profile.weixin))))
   }
  }
  /* 修改支付宝 */
   def modifyPayment = Users.UserAction {    user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      alipayForm.bindFromRequest.fold(
        formWithErrors => {

          BadRequest(views.html.users.account.payment(user,formWithErrors))
        } ,
        fields =>{
          UserDao.modifyAlipay(user.get.id.get,fields._1.getOrElse(""),fields._2.getOrElse(""),fields._3.getOrElse(""))
          Ok(views.html.users.account.payment(user,alipayForm.fill(fields._1,fields._2,fields._3),"支付宝账号保存成功"))

        }
      )
    }
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
 /* 用户放弃 新人有礼 */
 def giveUpGift = Users.UserAction {   user => implicit request =>
   if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
   else {
     UserDao.modifyStatus(user.get.id.get,1)
     Ok(Json.obj("code" -> "100","message" -> "成功" ))
   }
 }
 /* 用户获得 新人有礼 */
  def giveGift = Action(parse.json) {  implicit request =>
   val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
   if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
   else {
     val num = (request.body \ "num").as[Int]
  //   println("num " +num)
     UserSQLDao.updateCredits(user.get.id.get,num)
     UserDao.addUserRebate(user.get.id.get,num,1)
     UserDao.modifyStatus(user.get.id.get,1)
     UserDao.addUserCreditRecord(UserCreditRecord(None,user.get.id.get,0,num,"新人见面礼",new Timestamp(System.currentTimeMillis())))    // 添加用户获得集分宝记录
     Ok(Json.obj("code" -> "100","message" -> "成功" ))
   }
 }

}
