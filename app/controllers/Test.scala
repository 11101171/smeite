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
   // val result =GoodsSQLDao.updateLoveNum(14)
   val result =UserDao.findLoveThemes(1)
   for(item <- result.items){
     for (pic <- item._2) print(pic)
   }
    Ok(result.toString)
  }

}
