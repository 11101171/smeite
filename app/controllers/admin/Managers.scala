package controllers.admin

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.Form
import models.admin.Manager
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
import play.api.Play
import com.taobao.api.{ApiException, DefaultTaobaoClient}
import com.taobao.api.request.ItemGetRequest
import com.taobao.api.domain.Item

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-10-3
 * Time: 下午4:08
 * Email:zuosanshao@qq.com
 */

object Managers extends Controller {

  private def url:String = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_url")).getOrElse("http://gw.api.taobao.com/router/rest")
  private def appkey = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_appkey")).getOrElse("21136607")
  private def secret = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_secret")).getOrElse("b43392b7a08581a8916d2f9fa67003db")


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
      Ok(views.html.admin.managers.index(manager,tasks,totalTaskNum,numMap))
  }

  /*缓存管理*/
  def cache = AdminAction{    manager => implicit request =>
      Ok(views.html.admin.managers.cache(manager))
  }
  /*更新商品管理*/
  def updateGoods = AdminAction{    manager => implicit request =>
    val page = GoodsDao.findAll(1,200)
    var numIids ="";
   for(goods<-page.items){
     val item = getProductInfo(goods.numIid)
     println("xxxxxxxxxxxxxxxxxxxxxxxxx " +item)
      numIids+=item
   }

  //  Ok(Json.obj("code" -> "100", "message" ->"亲，评论成功"))
    Ok(numIids +"  ")
  }
  /* 获取商品信息*/
  def getProductInfo(numIid:Long)={
    val client=new DefaultTaobaoClient(url, appkey, secret)
    val  req=new ItemGetRequest()
    req.setFields("num_iid,list_time,dellist_time,approve_status")
    req.setNumIid(numIid)

      val getRequest=client.execute(req)
    //  println(getRequest.getBody)
      if(getRequest.isSuccess){
      //  "numIid:"+getRequest.getItem.getNumIid +"list_time:"+getRequest.getItem.getListTime+"dellist_time:"+getRequest.getItem.getDelistTime+"approve_status"+getRequest.getItem.getApproveStatus
       if(getRequest.getItem.getDelistTime !=null){
         numIid
       }else{
         0l
       }
      }else{
       numIid
      }

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

    Ok(views.html.admin.managers.updateUserStats(manager))
  }


}
