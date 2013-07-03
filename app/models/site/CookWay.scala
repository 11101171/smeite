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

object CookWay extends Enumeration {
  val  Chao = Value("炒")
  val  Zheng = Value("蒸")
  val   Zhu =Value("煮")
  val   Dun =Value("炖")
  val   Ban =Value("拌")
  val   Shao =Value("烧")
  val   Jian =Value("煎")
  val   Zha =Value("炸")
  val   Kao =Value("烤")
  val   Bao =Value("煲")
  val   Men =Value("焖")
  val   Yan =Value("腌")
  val   Lu =Value("卤")
  val   Jiang =Value("酱")
  val   Hui =Value("烩")
  val   Qiang =Value("炝")
  val   Liu =Value("熘")
  val   Xun =Value("熏")
  val   Ju =Value("焗")
  val   Wei =Value("煨")
  val   Hongbei=Value("烘焙")
  val   Basi =Value("拔丝")
  val   TangCu =Value("糖醋")
  val   Other =Value("其他")
}
