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

object PostCate extends Enumeration {
  val  SiFangCai = Value("菜谱百科")
  val ShiCai  = Value("食材百科")
  val CommonSense = Value("常识")
  val WenDa = Value("问答")
  val Others =Value("其他")
}
