package schedule

import akka.actor.Actor
import utils.TaobaoConfig
import play.api.mvc.{Action, Cookie}
import play.api.mvc.Results.Ok


/* 好像没法实现 nnd */
class PullGoodsActor  extends  Actor{

  def pullGoods ={
    println("schedule pull goods start")
    val timestamp= String.valueOf(System.currentTimeMillis)
    val sign=TaobaoConfig.getSign(timestamp)
  //  Ok(views.html.admin.schedulePullGoods()).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))
   // Ok("success")
  }
  def receive = {
    case "start" => pullGoods
    case _ => println("something wrong")
  }
}
