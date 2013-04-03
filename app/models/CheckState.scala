package models

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

object CheckState extends Enumeration {
  val UNCHECK = Value("未审核");
  val PASS = Value("审核通过")
  val UNPASS = Value("审核未通过")

}
