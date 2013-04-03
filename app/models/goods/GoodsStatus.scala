package models.goods
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-9
 * Time: 下午1:10
 * ***********************
 * description:用于类的说明
 */

object  GoodsStatus extends Enumeration{
  val offSALE=Value("下架");
  val onSALE=Value("上架");


}
