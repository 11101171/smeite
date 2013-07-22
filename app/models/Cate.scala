package models

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:网站的主题分类，适用于主题分类、标签组分类
 * 主要分为5大类 1、健康美食 2、地理特产 3、滋补保健 4、居家生活 5 好玩意   6 其他
 * 新分类 1、零食  2 食材 3 居家   4 其他
 * 由于主题分类比较明确、固定，因此，可以写死在程序中，以替代themeCategory
 */

object Cate extends Enumeration {
  val relaxFood = Value("美食")
  val  materialFood = Value("食材")
  val  liveHome =Value("居家")
  val Others =Value("其他")

}
