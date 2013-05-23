package utils

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-12
 * Time: 下午1:45
 * ***********************
 * description:用于类的说明
 */
import java.util.regex.Pattern
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import play.api.libs.Codecs
import play.api.{Play, PlayException}
import  scala.collection.mutable.Map
import java.sql.Timestamp
import java.util.Calendar

object Utils {
  /*正则表达式验证*/
  private  def  isMatch(regex:String, str:String):Boolean={
    Pattern.compile(regex).matcher(str).matches();
/*    val  pattern = Pattern.compile(regex);
    val matcher = pattern.matcher(str);
     matcher.matches();*/
  }
  /*验证是否为图片格式*/
  def isImage(str:String):Boolean={
    val regex:String="(?i).+?\\.(jpg|JPG|jpeg|JPEG|gif|GIF|png|PNG|bmp|BMP)";
    isMatch(regex,str);
  }

 def isNumber(str:String):Boolean={
   val regex:String="[0-9]*"
   isMatch(regex,str)
 }
   /*分析url的参数*/
  def analysisURL(url:String):Map[String, String]={
     val map=  Map[String, String]()
     val params=url.substring(url.indexOf("?")+1).split("&")
    for(param<-params){
      val values:Array[String] = param.split("=")
      map.put(values(0),values(1))
    }
    map.seq
  }
  /*获取URL的params 的Id 值*/
  def getURLParam(url:String,param:String):Option[String]={
    var id:Option[String]=None;
    val params=url.substring(url.indexOf("?")+1).split("&")
    for(param<-params){
      val values:Array[String] = param.split("=")
      if(values(0)=="id")  id =Some(values(1))
    }
    id
  }

  /* 时间 formate */
  def timestampFormat(time:Timestamp)={
    new java.text.SimpleDateFormat("MM-dd HH:mm").format(time)
  }
  def timestampFormat2(time:Timestamp)={
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(time)
  }
  def timestampFormat3(time:Timestamp)={
    new java.text.SimpleDateFormat("yyyyMMdd").format(time)
  }
   /* 截取字符串 */
  def subString(str:String,minLength:Int,maxLength:Int)={
     if(str.length>minLength) str.substring(0,maxLength)
   }

  /* 获得一天的结束时间 */
  def getEndOfDay(date:Timestamp):Timestamp = {
   val calendar:Calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
   new Timestamp(calendar.getTimeInMillis)
  }
   /* 获得 一天的 开始时间 */
  def  getStartOfDay(date:Timestamp):Timestamp ={
   val calendar:Calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    new Timestamp(calendar.getTimeInMillis)
  }
  /* 获取两个时间的间隔天数 */
  def getIntervalDays(currentTime:Timestamp,beforeTime:Timestamp)={
    (currentTime.getTime- beforeTime.getTime())/(24*60*60*1000)
  }
  /* 例如 201305*/
  def getYearMonth(date:Timestamp) ={
    val calendar:Calendar = Calendar.getInstance()
    calendar.setTime(date)
    calendar.get(Calendar.YEAR)*100+calendar.get(Calendar.MONTH)+1
  }
  /* 获得某个时间的 所在月份的天 */
  def getDay(date:Timestamp) ={
    val calendar:Calendar = Calendar.getInstance()
    calendar.setTime(date)
    calendar.get(Calendar.DAY_OF_MONTH)
  }
}
