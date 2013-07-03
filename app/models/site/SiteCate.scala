package models.site

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:网站的小镇的分类

 * 新分类 1、生活  2 品牌站 3 其他
 */

object SiteCate extends Enumeration {
  val  Life = Value("生活")
  val  Brand = Value("品牌站")
  val Others =Value("其他")
}
