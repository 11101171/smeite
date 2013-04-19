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
import models.user.dao.UserSQLDao


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-21
 * Time: 上午10:38
 * ***********************
 * description:用于类的说明
 */

object UsersRegLogin extends Controller {

  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText
    ) verifying ("Email或者密码错误……", fields => fields match {
      case (email, password) => UserDao.authenticate(email, password).isDefined
    })
  )
  val regForm=Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText,
      "inviteId" ->longNumber
    ) verifying ("嘿，Email已存在了", fields => fields match {
      case (email,password,_) => UserDao.findByEmail(email).isEmpty
    })
  )
  val verifyEmailForm =Form(
  tuple(
  "id"-> longNumber,
  "email" ->email
  )
  )

  /*用户email登录页面*/
  def login =Action{   implicit request =>
    Ok(views.html.users.regLogin.login(loginForm))
  }
  /*用户email 登录 */
  def doEmailLogin=Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.users.regLogin.login(formWithErrors)),
      user =>{
        val u=UserDao.authenticate(user._1,user._2).get;
        Cache.set(u.id.get.toString,u);
        /*记录登陆信息*/
        UserSQLDao.loginRecord(u.id.get,request.remoteAddress,1)

        Redirect(controllers.users.routes.Users.home(u.id.get)).withSession("user" -> u.id.get.toString) }
    )

  }
  /* 用户退出  清除缓存*/
  def logout=Action{ implicit  request =>
 //   Cache.remove(session.get("user"))
    Redirect(controllers.routes.Pages.index).withNewSession
  }

  /*用户overlay dialog email 登陆*/

  def dialogEmailLogin= Action{ implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Ok(Json.obj("code" -> "101", "message" -> "密码或者email错误……")),
      user =>{
        val u=UserDao.authenticate(user._1,user._2).get;
        Cache.set(u.id.get.toString,u);
        /*记录登陆信息*/
        UserSQLDao.loginRecord(u.id.get,request.remoteAddress,1)
        Ok(Json.obj( "code" -> "100", "message" ->"success")).withSession("user" -> u.id.get.toString) }
    )
  }


  /*用户登录页面 重置密码*/
  def resetPasswd=Users.UserAction{   user => implicit request =>
    Ok(views.html.users.regLogin.resetPasswd(user))
  }

  /*用户注册页面 */
  def regist(inviteId:Long) =Users.UserAction{   user => implicit request =>
     if(user.isEmpty) Ok(views.html.users.regLogin.regist(regForm,inviteId))
     else Redirect(controllers.users.routes.Users.home(user.get.id.get))
  }
  /*用户注册页面 邮箱已发送*/
  def doRegist=Action{  implicit request =>
    regForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.users.regLogin.regist(formWithErrors,formWithErrors.get._3)),
      user =>{
        /*发送邮件 ,邮件激活后，改变user的status todo */
        /* xxx */
        val email =user._1;
        val password =user._2;
        val inviteId=user._3;
        val name =email.split("@").head;
        val gotoEmail:String=email.split("@").last match {
          case  "gmail.com" => "http://mail.google.com"
          case  _ =>"http://mail."+email.split("@").last
        }
     //   User.insertUser(name,email,user._2);
      UserDao.addSmeiteUser(name,password,email,inviteId,request.remoteAddress)
        val u=UserDao.authenticate(email,password);
        Cache.set(u.get.id.get.toString,u);
        Ok(views.html.users.regLogin.doRegist(u,email,gotoEmail)).withSession("user" -> u.get.id.get.toString) }
    )
  }

  /*重新发送验证邮件*/
  def reSendEmail=Action {  implicit  request =>
    Ok("todo")
  }


   /*检查email 是否存在*/
  def checkEmailExist =Action(parse.json) { implicit  request =>
     val email = (request.body \ "email").as[String]
     val user = UserDao.findByEmail(email)
     if (user.isEmpty) Ok(Json.obj("code" -> "0", "message" ->"no exist" ))
     else  Ok(Json.obj("code" -> "1", "message" ->"exist" ))
  }


  /* user account 开放账号登录后，需要注册一个邮箱 才能修改其他账号信息 */
  def before = Users.UserAction { user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else   Ok(views.html.users.regLogin.before(user,verifyEmailForm))

  }

  /* sns 修改帐号信息 先要验证邮箱 */
    def doVerifyEmail =Users.UserAction{   user => implicit request =>
    verifyEmailForm.bindFromRequest.fold(
      formWithErrors =>  BadRequest(views.html.users.regLogin.before(user,formWithErrors)),
      fields =>{
         UserDao.modifyEmail(fields._1,fields._2);
        val email =fields._2;
        val gotoEmail:String=email.split("@").last match {
          case  "gmail.com" => "http://mail.google.com"
          case  _ =>"http://mail."+email.split("@").last
        }
        Ok(views.html.users.regLogin.doVerifyEmail(user,email,gotoEmail))
      }
    )

  }

}
