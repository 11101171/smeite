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
  val unProcess = Value("还没有处理结果");
  val Success = Value("发放成功，(*^__^*) 嘻嘻……");
  val Fail0 = Value("发放失败，没有支付宝")
  val Fail1 = Value("发放失败，支付宝未通过实名认证")
  val Fail2 =Value("发放失败，支付宝账号未激活")
  val Fail3 =Value("发放失败，支付宝错误或者无法收款")
  val Fail4 =Value("发放失败，支付宝账号冻结")
}
