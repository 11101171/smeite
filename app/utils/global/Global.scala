package utils.global

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-2-18下午7:19
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import akka.actor.Props
import schedule._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext


object Global extends GlobalSettings {

  override def onStart(app: Application) {
   val schedule = Play.maybeApplication.flatMap(_.configuration.getString("schedule")).getOrElse("off")
     if(schedule=="on"){

       /* 邀请有奖，每天夜里3点-4点统计 发布的时候注意修改时间 */
      val invitePrizeActor = Akka.system.actorOf(Props[InvitePrizeActor], name = "invitePrizeActor")
     Akka.system.scheduler.schedule(5 minutes, 8 hours, invitePrizeActor, "start")
       /* 淘宝客收入 每天晚上11:30左右统计 发布的时候注意修改时间 */
         val taobaokeIncomeActor = Akka.system.actorOf(Props[TaobaokeIncomeActor], name = "taobaokeIncomeActor")
          Akka.system.scheduler.schedule(5 minutes, 3 hours, taobaokeIncomeActor, "start")

       /*  每天夜里3-4点更新商品*/
    //      val pullGoodsActor = Akka.system.actorOf(Props[PullGoodsActor], name = "pullGoodsActor")
     //     Akka.system.scheduler.schedule(1 seconds, 30 seconds, pullGoodsActor, "start")

       /* 用户信息统计 每天夜里3点-4点统计 发布的时候注意修改时间，只修改24小时有变化的用户  这里可能不需要每天都操作*/
         //  val userInfoActor = Akka.system.actorOf(Props[UserInfoStaticActor], name = "userInfoStaticActor")
        //   Akka.system.scheduler.schedule(1 seconds, 30 seconds, userInfoActor, "start")
     }

  }
//  When an exception occurs in your application, the onError operation will be called
 override def onError(request: RequestHeader, ex: Throwable) = {
    InternalServerError(
      views.html.common.global.error()
    )
  }
 //   If the framework doesn’t find an Action for a request, the onHandlerNotFound operation will be called:
  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(
   //   views.html.common.common.notFound()
      views.html.common.global.error()
    )
  }

 //  The onBadRequest operation will be called if a route was found, but it was not possible to bind the request parameters
  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest(
    //  views.html.common.global.badRequest()
      views.html.common.global.error()
    )
  }


}