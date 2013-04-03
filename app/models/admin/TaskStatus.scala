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

object TaskStatus extends Enumeration {
  val Prosessing = Value("进行中");
  val Complete = Value("已完成")
  val Postpone = Value("推迟延期")
  val Incomplete = Value("未完成")

}
