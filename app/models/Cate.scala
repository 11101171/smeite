package models

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:网站的主题分类，适用于主题分类、标签组分类
 * 主要分为5大类 1、健康美食 2、地理特产 3、滋补保健 4、居家生活 5 好玩意
 * 由于主题分类比较明确、固定，因此，可以写死在程序中，以替代themeCategory
 */

object Cate extends Enumeration {
  val relaxFood = Value("零食")
  val localFood = Value("特产")
  val drinkFood = Value("滋补")
  val JuJia = Value("居家")
  val ChinaStyle = Value("中国风")
  val Funny = Value("好玩意")
  val Others =Value("其他")
}
