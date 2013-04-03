package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-12-26
 * Time: 上午8:43
 * To change this template use File | Settings | File Templates.
 */
object Adverts extends Controller {

  /*先放到缓存中，每隔一段时间添加到数据库中 todo
   *  */
  def click=Action(parse.json){    implicit  request =>
         val id =  (request.body \ "id").as[Long];
    Ok(Json.obj("code"->"100","message"->"success"))

  }
}
