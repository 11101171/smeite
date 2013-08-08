package controllers.admin

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.Form
import models.admin.{Manager}
import models.admin.dao.{ManagerSQLDao, ManagerDao}
import java.sql.Timestamp
import models.theme.dao.ThemeDao
import models.goods.dao.GoodsDao
import models.forum.dao.TopicDao
import models.user.dao.UserDao
import utils.TaobaoConfig
import play.api.libs.json.Json
import models.Page
import utils.Utils
import models.site.dao.SiteDao
import models.msg.dao.AtMsgDao

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-10-3
 * Time: 下午4:08
 * Email:zuosanshao@qq.com
 */

object Managers extends Controller {
  val loginForm = Form(
    tuple(
      "email" -> email,
      "passwd" -> nonEmptyText
    ) verifying("Email或者密码错误……", fields => fields match {
      case (email, passwd) => ManagerDao.authenticate(email, passwd).isDefined
    })
  )
  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def AdminAction(f: Manager => Request[AnyContent] => Result) = {
    Action {
      request =>
        val manager: Option[Manager] = request.session.get("manager").map(m => Cache.getOrElse[Manager](m) {
          ManagerDao.findById(m.toLong)
        })
        if (manager.isEmpty)
          Redirect(controllers.admin.routes.Managers.login())
        else
          f(manager.get)(request)
    }
  }

  /* login */
  def login = Action {
    Ok(views.html.admin.login(loginForm))
  }

  /*邮箱验证并cache*/
  def emailLogin = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.admin.login(formWithErrors)),
        manager => {
          val m:Manager = ManagerDao.authenticate(manager._1, manager._2).get
          Cache.set(m.id.toString, m)
          ManagerSQLDao.loginRecord(m.id.get,request.remoteAddress,1,new Timestamp(System.currentTimeMillis()),m.loginTime)
          Redirect(controllers.admin.routes.Managers.index).withSession("manager" ->m.id.get.toString)
        }
      )
  }

  /* 用户退出  清除缓存*/
  def logout = Action {  implicit request =>
      if (!session.get("manager").isEmpty) {
        Cache.remove(session.get("manager").get)
      }
      Redirect(controllers.admin.routes.Managers.login()).withNewSession
  }

  /*用户设置密码等等*/
  def modify = AdminAction {
    manager => implicit request =>
      Ok("todo")
  }

  /*我的账号*/
  def myAccount = AdminAction {
    manager => implicit request =>
      Ok("todo")
  }


  /*admin 首页*/
  def index = AdminAction { manager => implicit request =>

    val tasks = ManagerDao.findTasks(0,1,10)
     val totalTaskNum = ManagerDao.countTask
    val totalTheme = ThemeDao.countTheme
    val todayTheme = ThemeDao.countTheme(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newTheme = ThemeDao.countTheme(manager.lastLoginTime)

    val totalThemeDiscuss = ThemeDao.countDiscuss
    val todayThemeDiscuss = ThemeDao.countDiscuss(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newThemeDiscuss = ThemeDao.countDiscuss(manager.lastLoginTime)

    val totalGoods = GoodsDao.countGoods
    val todayGoods = GoodsDao.countGoods(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newGoods = GoodsDao.countGoods(manager.lastLoginTime)

    val totalGoodsAssess = GoodsDao.countAssess
    val todayGoodsAssess = GoodsDao.countAssess(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newGoodsAssess = GoodsDao.countAssess(manager.lastLoginTime)

    val totalTopic = TopicDao.countTopic
    val todayTopic = TopicDao.countTopic(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newTopic = TopicDao.countTopic(manager.lastLoginTime)

    val totalTopicReply = TopicDao.countReply
    val todayTopicReply = TopicDao.countReply(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newTopicReply = TopicDao.countReply(manager.lastLoginTime)

    val totalSite = SiteDao.countSite
    val todaySite = SiteDao.countSite(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newSite = SiteDao.countSite(manager.lastLoginTime)

    val totalPost = SiteDao.countPost
    val todayPost = SiteDao.countPost(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newPost = SiteDao.countPost(manager.lastLoginTime)

    val totalPostReply = SiteDao.countReply
    val todayPostReply = SiteDao.countReply(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newPostReply = SiteDao.countReply(manager.lastLoginTime)

    val totalAtMsg = AtMsgDao.countAtMsg
    val todayAtMsg = AtMsgDao.countAtMsg(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newAtMsg = AtMsgDao.countAtMsg(manager.lastLoginTime)


    val totalUser = UserDao.countUser
    val todayUser = UserDao.countUser(Utils.getStartOfDay(new Timestamp(System.currentTimeMillis())))
    val newUser = UserDao.countUser(manager.lastLoginTime)
    val numMap = Map(
      "totalTheme"->totalTheme,
      "todayTheme"->todayTheme,
      "newTheme"->newTheme,
      "totalThemeDiscuss"->totalThemeDiscuss,
      "todayThemeDiscuss"->todayThemeDiscuss,
      "newThemeDiscuss"->newThemeDiscuss,
	  "totalGoods"->totalGoods,
      "todayGoods"->todayGoods,
      "newGoods"->newGoods,
	  "totalGoodsAssess"->totalGoodsAssess,
      "todayGoodsAssess"->todayGoodsAssess,
      "newGoodsAssess"->newGoodsAssess,
	  "totalTopic"->totalTopic,
      "todayTopic"->todayTopic,
      "newTopic"->newTopic,
	  "totalTopicReply"->totalTopicReply,
      "todayTopicReply"->todayTopicReply,
      "newTopicReply"->newTopicReply,
	  "totalSite"->totalSite,
      "todaySite"->todaySite,
      "newSite"->newSite,
	  "totalPost"->totalPost,
      "todayPost"->todayPost,
      "newPost"->newPost,
	  "totalPostReply"->totalPostReply,
      "todayPostReply"->todayPostReply,
      "newPostReply"->newPostReply,
	  "totalAtMsg"->totalAtMsg,
      "todayAtMsg"->todayAtMsg,
      "newAtMsg"->newAtMsg,
	  "totalUser"->totalUser,
      "todayUser"->todayUser,
      "newUser"->newUser
    )
      Ok(views.html.admin.index(manager,tasks,totalTaskNum,numMap))
  }

  /*缓存管理*/
  def cache = AdminAction{    manager => implicit request =>
       Ok("缓存管理，主要是清理缓存 todo")
  }
  /*更新商品管理*/
  def pullGoods = AdminAction{    manager => implicit request =>
    val timestamp= String.valueOf(System.currentTimeMillis)
    val sign=TaobaoConfig.getSign(timestamp)
     Ok(views.html.admin.pullGoods(manager)).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))
  }
   /* 每个页面显示40个 然后 淘宝客  在然后 更新商品管理 */
  def getNumIids(p:Int) =AdminAction{    manager => implicit request =>
    val page:Page[Long] = GoodsDao.getNumiids(p,40)
      Ok(Json.obj("code"->"100","totalPages"->page.totalPages,"nums"->page.items.mkString(",")))

  }

  /*推送消息、信息管理*/
  def pushMsg = AdminAction{    manager => implicit request =>
    Ok("向用户推送消息 todo ")
  }

  def check =AdminAction{    manager => implicit request =>
    Ok("主要检测是否有恶意用户 todo ")
  }

  /* 更新用户的各种数据 */
  def updateUserStats = AdminAction{    manager => implicit request =>
       val totalUsers = UserDao.countUser
       for(i <- 1 to totalUsers){
         val fansNum = UserDao.countUserFans(i)
         val followNum = UserDao.countUserFollows(i)
         val trendNum = UserDao.countUserTrends(i)
         val loveBaobeiNum = UserDao.countUserLoveGoods(i)
         val loveThemeNum = UserDao.countUserLoveTheme(i)
         val loveTopicNum = UserDao.countUserLoveTopic(i)
         val postBaobeiNum = UserDao.countUserShareGoods(i)
         val postThemeNum = UserDao.countUserPostTheme(i)
         val postTopicNum = UserDao.countUserPostTopic(i)
         UserDao.modifyUserStatic(i,fansNum,followNum,trendNum,loveBaobeiNum,loveThemeNum,loveTopicNum,postBaobeiNum,postThemeNum,postTopicNum)
       }

    Ok(views.html.admin.updateUserStats(manager))
  }


}
