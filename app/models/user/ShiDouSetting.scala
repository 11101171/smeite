package models.user

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-19
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
object ShiDouSetting {
    /* 连续签到 获得额外食豆 */
  val checkIn7Days:Int = 15
  val checkIn15Days:Int = 30
  val checkIn30Days:Int = 60
  val checkIn60Days:Int = 100
  val checkIn100Days:Int = 200
  val checkIn200Days:Int = 400
  val checkIn365Days:Int = 1000

  /*发布优质主题 10*/
  val postHotTheme:Int = 10
  /*发布优质商品 10*/
  val postHotBaobei:Int = 5
  /*创建优质话题 10*/
  val postHotTopic:Int = 10

}
