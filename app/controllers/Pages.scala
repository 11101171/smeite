package controllers
import play.api.mvc.{Action, Controller}
import users.Users
import models.advert.dao.AdvertDao
import models.tag.dao.TagDao
import models.theme.dao.ThemeDao
import models.goods.dao.GoodsDao
import models.Page
import java.util.Calendar


/**
   * Created by zuosanshao.
   * email:zuosanshao@qq.com
   * Date: 12-7-27
   * Time: 下午2:26
   * ***********************
   * description:前端频道页面的显示：例如首页、其他页面等
   */

  object Pages extends Controller {



  /*首页*/
  def index=Users.UserAction{ user => implicit request =>
    val  flashes =AdvertDao.findAdverts("index_flash");
    val  hots = AdvertDao.findAdverts("index_hot");

    val meishi =AdvertDao.findAdverts("index-meishi-right")
    val techan =AdvertDao.findAdverts("index-techan-right")
    val jujia =AdvertDao.findAdverts("index-jujia-right")
    val haowanyi =AdvertDao.findAdverts("index-haowanyi-right")

    val meishiTags = AdvertDao.findAdvert("index-meishi-left")
    val techanTags = AdvertDao.findAdvert("index-techan-left")
    val jujiaTags  = AdvertDao.findAdvert("index-jujia-left")
    val haowanyiTags  = AdvertDao.findAdvert("index-haowanyi-left")

    val meishiBrands =AdvertDao.findAdverts("index_pinpai_meishi");
    val teseBrands =AdvertDao.findAdverts("index_pinpai_tese");

    val bottom = AdvertDao.findAdvert("index-bottom")
    Ok(views.html.pages.index(user,flashes,hots,meishi,techan,jujia,haowanyi,meishiTags,techanTags,jujiaTags,haowanyiTags,meishiBrands,teseBrands,bottom))
  }


  /*天天特价*/
  def choice  = Users.UserAction {    user => implicit request =>
     val list= AdvertDao.getGoods("tejia")
     val cl:Calendar = Calendar.getInstance();
     val month=cl.get(Calendar.MONTH)
      val day =cl.get(Calendar.DAY_OF_MONTH)

      Ok(views.html.pages.choice(user,month,day,list))
  }


  /*发现
  * 1.首先判断 cate tag 是否存在，不存在，则返回到/find 页面    滋补保健 特产的标签放在美食中
  * */
  def find(cate:String,tag:String,p:Int) = Users.UserAction{ user => implicit request =>
    val cid:Int = cate match {
      case "美食" => 0
      case "特产" => 1
      case "滋补" => 2
      case "居家" =>3
      case "礼物" => 4
      case "好玩意" => 5
      case "其他" =>6
      case _ => 0
    }
    val tags = TagDao.findCateTags(cid,30)
     var page:models.Page[((Int,Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = null
      if (tag =="tag")  page = TagDao.findCateGoodses(cid,p,48);
      else   page = TagDao.findTagGoodses(tag,p,48)
    Ok(views.html.pages.find(user,page,cate,tag,tags))
  }



  /* 美食街*/
  def jie(s:String,cid:Int,p:Int) = Users.UserAction { user => implicit request =>
    var page:Page[((Long,String,String,Int),List[String])] =null
    if (cid <0|| cid >3){
      page = ThemeDao.findCatesThemes(0,2,s,p,24)
    }else {
      page = ThemeDao.findCateThemes(cid,s,p,24)
    }

    Ok(views.html.pages.jie(user,page,cid,s))
  }
  /* 创意主题街  特色馆*/
  def miss(s:String,cid:Int,p:Int)  = Users.UserAction { user => implicit request =>
    var page:Page[((Long,String,String,Int),List[String])] =null
    if (cid <3 || cid >5){
      page = ThemeDao.findCatesThemes(3,5,s,p,24)
    }else {
      page = ThemeDao.findCateThemes(cid,s,p,24)
    }
    Ok(views.html.pages.miss(user,page,cid,s))
  }
  /* 主题街下的 主题分类 */
  def gallery(cid:Int,s:String,p:Int)= Users.UserAction { user => implicit request =>
    val page = ThemeDao.findCateThemes(cid,s,p,24)
    Ok(views.html.pages.gallery(user,page,cid,s))
  }


}
