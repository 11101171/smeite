package controllers.users

import play.api.mvc.{Action, Controller}
import models.user.dao.{UserSQLDao, UserDao}
import play.api.libs.json._
import java.sql.Timestamp

import models.user.User

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.advert.dao.AdvertDao

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-23
 * Time: 下午6:09
 * Email:zuosanshao@qq.com
 */
case class RecommendGoods(
                      goodsId:Long,
                      numIid:Long,
                      rate:Int,
                      pic:String,
                      title:String,
                      price:String,
                      jifenbao:String,
                      jifenbaoValue:String
                      )

object UsersCheckIn extends Controller {

  implicit val recommendGoodsFormat = (
    (__ \ "goodsId").format[Long] and
      (__ \ "numIid").format[Long] and
      (__ \ "rate").format[Int] and
      (__ \ "pic").format[String] and
      (__ \ "title").format[String] and
      (__ \ "price").format[String] and
      (__ \ "jifenbao").format[String] and
      (__ \ "jifenbaoValue").format[String]
    )(RecommendGoods.apply,unlift(RecommendGoods.unapply))
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
      /* todo 以后根据用户的喜好推荐 */
      val goods = AdvertDao.getGoods("checkIn").head
      val jifenbao=(goods.promotionPrice.get.toDouble*goods.commissionRate.get*goods.rate*0.0001).toInt
      val jifenbaoValue = jifenbao/100.0
      val recommendGoods = new RecommendGoods(goods.id.get,goods.numIid,goods.rate,goods.pic+"_160x160.jpg",goods.name,goods.promotionPrice.get,jifenbao.toString,jifenbaoValue.toString)
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
        Ok(Json.obj("goods"->Json.toJson(recommendGoods),"code" -> "100","userCheckInDays" ->"1","userScore"->(user.get.shiDou+shiDou).toString ))
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
            Ok(Json.obj("goods"->Json.toJson(recommendGoods),"code" -> "100","userCheckInDays" ->"1","userScore"->(user.get.shiDou+shiDou).toString ))
          }
      }
    }
  }



}
