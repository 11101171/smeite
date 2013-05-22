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
import schedule.{TestActor, InvitePrizeActor}
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext


object Global extends GlobalSettings {

  override def onStart(app: Application) {
   val invitePrizeSchedule = Play.maybeApplication.flatMap(_.configuration.getString("invitePrize.schedule")).getOrElse("off")
     if(invitePrizeSchedule=="on"){
    //   val testActor = Akka.system.actorOf(Props[TestActor], name = "testActor")
  //     Akka.system.scheduler.schedule(1 seconds, 60 seconds, testActor, "start")
       val invitePrizeActor = Akka.system.actorOf(Props[InvitePrizeActor], name = "invitePrizeActor")
       Akka.system.scheduler.schedule(1 seconds, 30 seconds, invitePrizeActor, "start")
     }

  }
//  When an exception occurs in your application, the onError operation will be called
 override def onError(request: RequestHeader, ex: Throwable) = {
    InternalServerError(
      views.html.utils.global.error()
    )
  }
 //   If the framework doesn’t find an Action for a request, the onHandlerNotFound operation will be called:
  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(
   //   views.html.utils.global.notFound()
      views.html.utils.global.error()
    )
  }

 //  The onBadRequest operation will be called if a route was found, but it was not possible to bind the request parameters
  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest(
    //  views.html.utils.global.badRequest()
      views.html.utils.global.error()
    )
  }


}