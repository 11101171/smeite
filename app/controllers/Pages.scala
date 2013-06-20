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
  def worth  = Users.UserAction {    user => implicit request =>
     val list= AdvertDao.getGoods("tejia")
     val cl:Calendar = Calendar.getInstance()
     val month=cl.get(Calendar.MONTH)
      val day =cl.get(Calendar.DAY_OF_MONTH)
      Ok(views.html.pages.worth(user,month,day,list))
  }


  /*发现
  * 1.首先判断 cate tag 是否存在，不存在，则返回到/find 页面    滋补保健 特产的标签放在美食中
  * */

  def faxian(cid:Int,tagCode:Int,s:Int,p:Int) = Users.UserAction{ user => implicit request =>
    val ccid = cid -1
    val tags = TagDao.findCateTags(ccid,30)
    var page:models.Page[(models.user.User,models.goods.Goods)] = null
    if (tagCode == 0 )  page = TagDao.findSimpleCateGoodses(ccid,s,p,48)
    else   page = TagDao.findSimpleTagGoodses(tagCode,s,p,48)
    Ok(views.html.pages.faxian(user,page,cid,tagCode,tags,s))
  }

  /* 主题街下的 主题分类 */
  def gallery(cid:Int,s:Int,p:Int)= Users.UserAction { user => implicit request =>
    val  flashes:List[models.advert.Advert] =AdvertDao.findAdverts("miss_flash")
    val page = ThemeDao.findCateThemes(cid,s,p,24)
    Ok(views.html.pages.gallery(user,flashes,page,cid,s))
  }

  /* 主题 */
  def themes =  Users.UserAction { user => implicit request =>
    val  flashes:List[models.advert.Advert] =AdvertDao.findAdverts("miss_flash")
    val meishiThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("miss_meishi_theme",4)
    val techanThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("miss_techan_theme",4)
    val zibuThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("miss_zibu_theme",4)
    val jujiaThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("miss_jujia_theme",4)
    val haowanyiThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("miss_haowanyi_theme",4)
    Ok(views.html.pages.themes(user,flashes,meishiThemes,techanThemes,zibuThemes,jujiaThemes,haowanyiThemes))
  }



  /* 导航小站  */
  def site =  Users.UserAction { user => implicit request =>
      Ok(views.html.pages.site(user))
  }
 /* 导航小站  发现精彩小站 */
  def findSite = Users.UserAction { user => implicit request =>
   Ok("todo")
 }

  /* 食谱  */
  def cookbook =  Users.UserAction { user => implicit request =>
    Ok(views.html.pages.cookbook(user))
  }

  /* 生鲜预售 */
  def presell =  Users.UserAction { user => implicit request =>
    Ok(views.html.pages.presell(user))
  }


}
