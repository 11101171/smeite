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

object Global extends GlobalSettings {

 /*  When an exception occurs in your application, the onError operation will be called  */
/*  override def onError(request: RequestHeader, ex: Throwable) = {
    InternalServerError(
      views.html.utils.global.error()
    )
  }
    /*If the framework doesn’t find an Action for a request, the onHandlerNotFound operation will be called:*/
  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(
   //   views.html.utils.global.notFound()
      views.html.utils.global.error()
    )
  }

  /* The onBadRequest operation will be called if a route was found, but it was not possible to bind the request parameters */
  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest(
    //  views.html.utils.global.badRequest()
      views.html.utils.global.error()
    )
  }*/


}