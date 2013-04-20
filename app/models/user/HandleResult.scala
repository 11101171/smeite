package models.user

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户状态说明
 */

object HandleResult extends Enumeration {
  val Success = Value("成功");
  val Fail = Value("失败，没有支付宝")
  val Fail2 =Value("失败，支付宝不正确")
  val Fail3 =Value("失败，支付过程中出现问题")
  val Fail4 =Value("失败，未知问题")
}
