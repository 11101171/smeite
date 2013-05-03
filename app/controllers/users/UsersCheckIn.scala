package controllers.users

import play.api.mvc.{Action, Controller}
import models.user.dao.{UserSQLDao, UserDao}
import play.api.libs.json.Json
import java.sql.Timestamp
import models.user.User

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-23
 * Time: 下午6:09
 * Email:zuosanshao@qq.com
 */

object UsersCheckIn extends Controller {


  /* 用户签到 */
  def checkIn = Users.UserAction{   user => implicit request =>
    Ok(views.html.users.checkIn.checkIn(user))
  }

  /* 判断用户是否签到 */
  def checkInState = Users.UserAction {   user => implicit request =>
    if(user.isEmpty)     Ok(Json.obj("code" -> "404","message" -> "用户未登陆" ))
    else {
      val userCheckIn = UserDao.findUserCheckIn(user.get.id.get)
      val isChecked =   if(userCheckIn.isEmpty){ false } else { userCheckIn.get.addTime.after(utils.Utils.getStartOfDay( new Timestamp(System.currentTimeMillis()))) }
      if(isChecked){
        Ok(Json.obj("code" -> "100","userCheckInDays" -> userCheckIn.get.days.toString,"userScore"->user.get.shiDou.toString ))
      }else{
        Ok(Json.obj("code" -> "104","message" -> "未签到" ))
      }
    }
  }

  /* checkIn 食豆  用户签到后获取的食豆 */
  def ajaxCheckIn = Action(parse.json) { implicit  request =>
    val shiDou = (request.body \ "shiDou").as[Int]
    val user:Option[User] =request.session.get("user").map(u=> UserDao.findById(u.toLong) )
    if(user.isEmpty) {Ok(Json.obj("code" -> "404", "message" ->"用户未登陆" ))}
     else {
      val timestamp =  new Timestamp(System.currentTimeMillis())
      val month =utils.Utils.getYearMonth(timestamp)
      val currentDay = utils.Utils.getDay(timestamp)
      val userCheckIn =UserDao.findUserCheckIn(user.get.id.get)
      /*
      * 首先判断checkIn 是否存在，不存在 在添加（用户第一次签到）
      *  如果存在
      *  0、判断间隔时间,如果当前时间与上次签到时间间隔天数为0 获得 签到天数+1 或者 days=0
      *  1、 判断用户本月是否签到  如果本月签到，直接在 history 加上 本天的签到记录,如果不是，则直接添加一条记录
      * */
      if(userCheckIn.isEmpty){
        val history =currentDay+":"+shiDou
        UserDao.addUserCheckIn(user.get.id.get,shiDou,1,month,history,timestamp)
        UserDao.modifyShiDou(user.get.id.get,shiDou)
        Ok(Json.obj("code" -> "100", "message" ->"success","userCheckInDays" ->"1","userScore"->(user.get.shiDou+shiDou).toString ))
      } else{
        val isChecked =   userCheckIn.get.addTime.after(utils.Utils.getStartOfDay( new Timestamp(System.currentTimeMillis())))
          if(isChecked){
            Ok(Json.obj("code" -> "104", "message" ->"用户已签到" ))
          }else{
            val days = if(utils.Utils.getIntervalDays(timestamp,userCheckIn.get.addTime) ==0 ) userCheckIn.get.days+1 else{ 1 }
            if(month == userCheckIn.get.month){
              val history = userCheckIn.get.history+","+currentDay+":"+shiDou
              UserDao.modifyUserCheckIn(userCheckIn.get.id.get,shiDou,days,history)
            }else {
              val history =currentDay+":"+shiDou
              UserDao.addUserCheckIn(user.get.id.get,shiDou,days,month,history,timestamp)
            }
            UserDao.modifyShiDou(user.get.id.get,shiDou)
            Ok(Json.obj("code" -> "100","userCheckInDays" -> days.toString,"userScore"->(user.get.shiDou+shiDou).toString ))
          }

      }
    }

  }


}
