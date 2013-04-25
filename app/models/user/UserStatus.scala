package models.user

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户状态说明
 */

object UserStatus extends Enumeration {
  val  NEWMAN   = Value("newMan")
  val  NORMAL   = Value("Normal")
  val ACTIVE  = Value("active")
  val DAREN = Value("daren")
  val BLACK = Value("black")
}
