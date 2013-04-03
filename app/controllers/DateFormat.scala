package controllers

import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-2-18下午8:21
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object DateFormat {
  def apply(time: Timestamp) = {
    new java.text.SimpleDateFormat("MM-dd HH:mm").format(time)
  }
}
