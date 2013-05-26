package schedule

import akka.actor.Actor
import models.user.dao.UserDao
import java.sql.Timestamp

/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
*  Since:13-5-21下午7:52
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
class UserInfoStaticActor extends  Actor{

  def userInfoStats(days:Int) = {
    println("user info static schedule start *****")
    val totalUsers = UserDao.countUser(days)
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
    println("user info static schedule end !")
  }
  def receive = {
    case "start" => userInfoStats(1)
    case _ => println("schedule use info static got something wrong")
  }

}
