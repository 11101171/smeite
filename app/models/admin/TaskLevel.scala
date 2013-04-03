package models.admin

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户级别说明
 *  0 表示未审核
 *  1 表示审核通过
 *  2 表示审核未通过
 */

object TaskLevel extends Enumeration {
  val QingHuan = Value("轻-缓");
  val QingJi = Value("轻-急")
  val ZhongHuan = Value("重-缓")
  val ZhongJi = Value("重-急")

}
