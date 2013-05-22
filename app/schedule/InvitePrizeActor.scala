package schedule

import akka.actor.Actor
import models.user.dao.UserDao

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

  def invitePrize(days:Int) ={
    println("invite prize schedule start *****")
     val  possibleInvitePrizes= UserDao.findPossibleInvitePrizes(99,days)
     for((uid,credits,inviteId) <- possibleInvitePrizes){
       if(credits <1000){
         val invitePrize = UserDao.findUserInvitePrize(inviteId,uid,200)
         if(invitePrize.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,200)

         val invitePrize2 = UserDao.findUserInvitePrize(inviteId,uid,300)
         if(invitePrize2.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,300)

       } else if(credits >= 5000) {
         val invitePrize1 = UserDao.findUserInvitePrize(inviteId,uid,200)
         if(invitePrize1.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,200)

         val invitePrize2 = UserDao.findUserInvitePrize(inviteId,uid,300)
         if(invitePrize2.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,300)

         val invitePrize = UserDao.findUserInvitePrize(inviteId,uid,500)
         if(invitePrize.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,500)

       } else {
         val invitePrize1 = UserDao.findUserInvitePrize(inviteId,uid,200)
         if(invitePrize1.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,200)

         val invitePrize = UserDao.findUserInvitePrize(inviteId,uid,300)
         if(invitePrize.isEmpty)UserDao.addUserInvitePrize(inviteId,uid,credits,300)
       }
     }
    println("invite prize schedule end !")
  }
  def receive = {
    case "start" => invitePrize(1)
    case _ => println("schedule invite prize got something wrong")
  }

}
