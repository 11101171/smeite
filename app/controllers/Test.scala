package controllers

import play.api.mvc.{Action, Controller}
import models.msg.{Msg, MsgDao}

import play.api.Play.current
import models.tag.dao.TagDao
import models.user._
import models.user.dao.UserDao
import play.api.cache.Cache
import models.user.User
import models.goods.dao.GoodsSQLDao
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-24下午9:52
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object Test extends Controller {

  def test=Action{
 //  val result =MsgDao.testInsert(Msg(None,"sss","content",1,"zuosanshao",1,None));
//   val result =MsgDao.testAutoIdInsert(Msg(None,"sss","content",2,"sanshao",1,None))
 //   val result =MsgDao.testAutoId2("hello world","hello,fangjing,i hate you",2,"smeite")
 //   val result =MsgDao.testCount
 //   val result2 =MsgDao.testCount2
 //   val result3 =MsgDao.testCount
   /*

    for(trend <- list){
     println(trend.actionId + " : " + trend.actionContent + " : " + trend.addTime.get)
    }*/
 //   val result = UserDao.findById(1)
    val result = UserDao.findByEmail("smeite@qq.com")
    val user =Cache.getAs[Option[User]]("user_1")

    val result2 = UserDao.findById(1)

    Ok("insert result is "+result.get.name +"  :  " )


  }
  def  testEmail = Action {
  /*  val mail = use[MailerPlugin].email
    mail.setSubject("mailer")
    mail.addRecipient("Peter Hausel Junior <bestava@hotmail.com>","zuosanshao@qq.com")
    mail.addFrom("Peter Hausel <smeite@smeite.com>")

    mail.send( "text", "<html>html</html>")*/


    Ok("send email success" )
  }
  def testJDBC  =Action {
 /*  // val result =GoodsSQLDao.updateLoveNum(14)
   val userCheckIn =UserDao.isCheckedIn(2)
    val isChecked =   if(userCheckIn.isEmpty){ false } else {
  userCheckIn.get.addTime.after(utils.Utils.getStartOfDay( new Timestamp(System.currentTimeMillis())))
    }

     val days =utils.Utils.getIntervalDays(userCheckIn.get.addTime,new Timestamp(System.currentTimeMillis()))
    println("is checked " + isChecked + " days " +days)
*/

//   Ok(userCheckIn.toString)
    Ok(utils.Utils.getYearMonth(new Timestamp(System.currentTimeMillis())).toString +"    ssss  " + utils.Utils.getDay(new Timestamp(System.currentTimeMillis())))
  }



}
