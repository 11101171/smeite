package models.user

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户状态说明
 */

object WithdrawType extends Enumeration {
  val Baobei = Value("购物返利");
  val ShiDou = Value("食豆兑换")
  val Invite =Value("邀请有奖")
}
