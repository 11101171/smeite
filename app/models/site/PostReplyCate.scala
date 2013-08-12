package models.site

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:网站的小镇的分类

 * 新分类 0 随意吐槽 1 提问求解  2 上传成果
 */

object PostReplyCate extends Enumeration {
  val  Life = Value("随意吐槽")
  val  Brand = Value("提问求解")
  val Others =Value("上传成果")
}
