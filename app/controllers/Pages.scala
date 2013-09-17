package controllers
import play.api.mvc.{Action, Controller}
import users.Users
import models.advert.dao.AdvertDao
import models.tag.dao.TagDao
import models.theme.dao.ThemeDao
import models.goods.dao.GoodsDao
import models.Page
import java.util.Calendar
import models.site.dao.SiteDao


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
    val  flashes =AdvertDao.findAdverts("index-flash")
    val  hots = AdvertDao.findAdverts("index-hot")

    val meishi =AdvertDao.findAdverts("index-meishi-right")
    val shicai =AdvertDao.findAdverts("index-shicai-right")
    val jujia =AdvertDao.findAdverts("index-jujia-right")

    val meishiTags = AdvertDao.findAdvert("index-meishi-left")
    val shicaiTags = AdvertDao.findAdvert("index-shicai-left")
    val jujiaTags  = AdvertDao.findAdvert("index-jujia-left")

     val meishiPosts = AdvertDao.findAdvert("index-meishi-posts")
     val shicaiPosts = AdvertDao.findAdvert("index-shicai-posts")
     val jujiaPosts = AdvertDao.findAdvert("index-jujia-posts")
     val sitePosts = AdvertDao.findAdvert("index-site-posts")
     val site = AdvertDao.findAdvert("index-site")

    val meishiBrands =AdvertDao.findAdverts("index-pinpai-meishi")
    val jujiaBrands =AdvertDao.findAdverts("index-pinpai-jujia")

    val adverts = AdvertDao.findAdverts("index-bottom")

    Ok(views.html.pages.index(user,flashes,hots,meishi,shicai,jujia,meishiTags,shicaiTags,jujiaTags,meishiBrands,jujiaBrands,adverts,meishiPosts,shicaiPosts,jujiaPosts,sitePosts,site))
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

    val tags = TagDao.findCateTags(cid,30)
    var page:models.Page[(models.user.User,models.goods.Goods)] = null
    if (tagCode == 0 )  page = TagDao.findSimpleCateGoodses(cid,s,p,48)
    else   page = TagDao.findSimpleTagGoodses(tagCode,s,p,48)
    Ok(views.html.pages.faxian(user,page,cid,tagCode,tags,s))
  }



  /* 主题 */
  def themes =  Users.UserAction { user => implicit request =>
    val  flashes:List[models.advert.Advert] =AdvertDao.findAdverts("themes-flash")
    val meishiThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("themes-meishi",4)
    val shicaiThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("themes-shicai",4)
    val jujiaThemes:List[((Long,String,String,Int),List[String])]=AdvertDao.getThemes("themes-jujia",4)
    Ok(views.html.pages.themes(user,flashes,meishiThemes,shicaiThemes,jujiaThemes))
  }

  /* 主题街下的 主题分类 */
  def themeList(cid:Int,s:Int,p:Int)= Users.UserAction { user => implicit request =>
    val  flashes:List[models.advert.Advert] =AdvertDao.findAdverts("themes-flash")
    val page = ThemeDao.findCateThemes(cid,s,p,24)
    Ok(views.html.pages.themeList(user,flashes,page,cid,s))
  }

  /* 导航小镇  */
  def sites =  Users.UserAction { user => implicit request =>
    val flashes = AdvertDao.findAdverts("sites-flash")
    val sites  = AdvertDao.getSites("sites-recom",6)
    val posts = AdvertDao.getPosts("sites-post-recom",8)
      Ok(views.html.pages.sites(user,flashes,sites,posts))
  }
  /* 发现小镇 推荐小镇
  * s:1 最新
  * s:2 最热
  * */
  def siteList(cid:Int,s:Int,p:Int) =  Users.UserAction { user => implicit request =>
    val flashes = AdvertDao.findAdverts("sites-flash")
    val page = SiteDao.findSites(cid,s,p,10)
      Ok(views.html.pages.siteList(user,flashes,page,cid,s))
  }
  /* 发现帖子 推荐帖子 */
  def postList(cid:Int,s:Int,p:Int) =  Users.UserAction { user => implicit request =>
    val flashes = AdvertDao.findAdverts("sites-flash")
    val page = SiteDao.findPosts(cid,s,p,10)
    Ok(views.html.pages.postList(user,flashes,page,cid,s))
  }


  /* 食谱  */
  def cookbook =  Users.UserAction { user => implicit request =>
    val flashes = AdvertDao.findAdverts("sifangcai-flash")
    val shicai = AdvertDao.getPosts("sifangcai-shicai",12)
    val menu = AdvertDao.getPosts("sifangcai-menu",6)
    Ok(views.html.pages.cookbook(user,flashes,shicai,menu))
  }

  /* 生鲜预售 */
  def presell =  Users.UserAction { user => implicit request =>
    Ok(views.html.pages.presell(user))
  }


}
