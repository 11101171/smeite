package schedule

import akka.actor.Actor

/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-5-21下午7:52
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
class InvitePrizeActor extends  Actor{
  def staticInvitePrize={

  }
  def receive = {
    case "start" => println("hello,i am job work 10 seconds")
    case _ => println("something wrong")
  }

}
