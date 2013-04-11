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

object Utils {
  private def secret: String= Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_secret")).getOrElse("c03472fbe94e9fa882c44948e8709320")

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

  def isValideUrl(str:String):Boolean={
    if(str.indexOf("tmall.com/item.htm?")!= -1 || str.indexOf("item.taobao.com/item.htm?") != -1) true
    else  false
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
   /* 截取字符串 */
  def subString(str:String,minLength:Int,maxLength:Int)={
     if(str.length>minLength) str.substring(0,maxLength)
   }
}
