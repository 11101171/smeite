package schedule

import akka.actor.Actor

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-21
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
class TestActor  extends  Actor{
  def receive = {
    case "start" => println("hello,i am job work 10 seconds")
    case _ => println("something wrong")
  }
}
