package models.user

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户状态说明
 */

object HandleStatus extends Enumeration {
  val Handling = Value("处理中...");
  val HandleSuccess = Value("处理成功")
  val HandleFail =Value("处理失败")
}
