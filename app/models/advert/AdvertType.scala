package models.advert

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-16上午11:36
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object AdvertType extends Enumeration {
  val PIC = Value("图片")
  val TEXT =Value("文字")
  val PRODUCT =Value("商品")
  val USER =Value("用户")
  val Theme = Value("主题")
  val TOPIC = Value("话题")
  val SITE  = Value("小镇")
  val POST = Value("帖子")
}
