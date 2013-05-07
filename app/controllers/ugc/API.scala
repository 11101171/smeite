package controllers.ugc

import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.request.{TaobaokeWidgetItemsConvertRequest, ItemGetRequest}
import play.api.mvc.{Cookie, Action, Controller}
import play.api.libs.json.JsObject._
import play.api.libs.json.JsNumber._
import play.api.libs.json.JsString._
import play.api.libs.json.JsBoolean._
import play.api.libs.json.JsSuccess._
import play.api.libs.json._
import play.api.Play
import controllers.users.Users
import com.taobao.api.domain.ItemImg
import scala.collection.JavaConversions._
import models.goods.dao.GoodsDao
import net.coobird.thumbnailator.{Thumbnails, Thumbnailator}
import models.tag.{TagGoods}
import models.tag.dao.TagDao
import models.user._
import play.api.libs.json.JsArray
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject

import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.user.User
import models.goods.GoodsAssess
import play.api.mvc.Cookie
import dao. UserDao
import utils.{TaobaoConfig, Utils}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-9
 * Time: 上午10:22
 * ***********************
 * description:使用淘宝的API 获取淘宝的商品 并保存在数据库中
 */
case class Product(
                  id:Option[Long],
                  numIid:Long,
                  nick:String,
                  name: String,
                  proComment: Option[String],
                  price:String,
                  pic: String,
                  itemPics: List[String],
                  detailUrl:String,
                 tags:List[String]
                  )

/*
* 用户购买记录
* */
case class BuyRecord(
                    uid:String,
                    goodsId:String,
                    numIid:String,
                    rate:String,
                    nick:String,
                    title:String,
                    location:String,
                    pic:String,
                    price:String,
                    promotionPrice:String,
                    commissionRate:String,
                    volume:Int
                      )


object API extends Controller {

  /*json */
  implicit  object ProductFormat extends Format[Product]{
    def writes(o: Product): JsValue = JsObject(
      List(
        "id"->JsNumber(o.id.getOrElse[Long](0)),
        "numIid"->JsNumber(o.numIid),
        "nick" -> JsString(o.nick),
        "name" -> JsString(o.name),
        "proComment" -> JsString(o.proComment.getOrElse[String]("none")),
        "price" -> JsString(o.price),
        "pic" -> JsString(o.pic),
        "itemPics" -> JsArray(o.itemPics.map(JsString(_))),
        "detailUrl" -> JsString(o.detailUrl),
        "tags" -> JsArray(o.tags.map(JsString(_)))
      )
    )
    def reads(json: JsValue): JsResult[Product] = JsSuccess(Product(
      (json \ "id").asOpt[Long],
      (json \ "numIid").as[Long],
      (json \ "nick").as[String],
      (json \ "name").as[String],
      (json \ "proComment").asOpt[String],
      (json \ "price").as[String],
      (json \ "pic").as[String],
      (json \ "itemPics").as[List[String]],
      (json \ "detailUrl").as[String],
      (json \ "tags").as[List[String]]
    )
    )
  }

  /*json */
  implicit val buyRecordFormat = (
    (__ \ "uid").format[String] and
      (__ \ "goodsId").format[String] and
      (__ \ "numIid").format[String] and
      (__ \ "rate").format[String] and
      (__ \ "nick").format[String] and
      (__ \ "title").format[String] and
      (__ \ "location").format[String] and
      (__ \ "pic").format[String] and
      (__ \ "price").format[String] and
      (__ \ "promotionPrice").format[String] and
      (__ \ "commissionRate").format[String] and
      (__ \ "volume").format[Int]
    )(BuyRecord.apply,unlift(BuyRecord.unapply))




  private def url:String = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_url")).getOrElse("http://gw.api.taobao.com/router/rest")
  private def appkey = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_appkey")).getOrElse("21136607")
  private def secret = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_secret")).getOrElse("b43392b7a08581a8916d2f9fa67003db")



  /*获取分享的宝贝信息
  *  100  成功
  *  105  已存在
  *  104 信息抓取失败，请重试
  *  108 你已分享
  *  110 商家黑名单
  *  400 未登陆
  *  444 禁止登陆
  *  442  频繁分享
  *  445  禁止分享
  * */
  def  findProduct(detailUrl:String)=Users.UserAction {  user => implicit request =>
         if(user.isEmpty) Ok(Json.obj("code"->"400","message"->"你还没有登陆"))
         else if(user.get.status==4)Ok(Json.obj("code"->"444","message"->"禁止登陆"))
         else{
           val idStr =Utils.getURLParam(detailUrl,"id");
           if(idStr.isEmpty)Ok(Json.obj("code"->"104","message"->"不能获取商品的ID"))
             // 下面可能需要判断id.get.toLong 是否为long 的问题
           else {
             val numIid=idStr.get.toLong;
             val goods =GoodsDao.find(numIid)

             if(!goods.isEmpty){
               val product=Product(goods.get.id,goods.get.numIid,goods.get.nick,goods.get.name,None,goods.get.price,goods.get.pic,Nil,goods.get.detailUrl,Nil)
               Ok(Json.obj("code"->"105","product"->Json.toJson(product) ,"message"->"该商品已存在"))
             }
             else{
               val client=new DefaultTaobaoClient(url, appkey, secret);
               val  req=new ItemGetRequest();
               req.setFields("num_iid,nick,title,price,pic_url,detail_url,item_img.url");
               req.setNumIid(numIid);
               val respItem = client.execute(req ).getItem;
               val itemImgs= for(i<-respItem.getItemImgs)yield(i.getUrl)
             val product = Product(
                 None,
               respItem.getNumIid,
               respItem.getNick,
                 respItem.getTitle,
                 None,
                 respItem.getPrice,
                 respItem.getPicUrl,
                 itemImgs.toList,
                 respItem.getDetailUrl,
                 Nil
               )
               Ok(Json.obj("code"->"100","product"->Json.toJson(product)))
             }

           }
         }

  }

  /*
  * 保存分享的宝贝
  * 
  * */ 
  def  saveProduct = Action(parse.json) {  implicit request =>
        val product =Json.fromJson[Product](request.body).get
        /* 处理图片*/
        val mainPic =product.itemPics.head
        val images:StringBuffer= new StringBuffer();

        // 直接采用淘宝上的图片
        for (img <-product.itemPics){
            images.append(img+"&")
        }
        /*保存goods*/
       val goodsId= GoodsDao.addGoods(product.numIid,product.name,product.proComment.getOrElse("none"),product.price,mainPic,images.toString,product.nick,product.detailUrl)
       /*保存tags 首先查看tags 是否存在，不存在则保存*/
         for(name<-product.tags){
           /*处理tag*/
          val tag= TagDao.findByName(name)
           if (tag.isEmpty)TagDao.addTag(name)
           else TagDao.modifyTag(tag.get.id.get,tag.get.addNum+1)
           /*处理 tag goods*/
           TagDao.addGoods(name,goodsId)
         }
        /*处理 user share goods*/
        val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        UserDao.addShareGoods(user.get.id.get,goodsId)

    Ok(Json.obj("code"->"100","productId"->goodsId ,"pic"->product.pic , "message"->"success"))
  }

  /*对于已经存在的product，则需要update*/
  def updateProduct = Action(parse.json) {  implicit request =>
    val product =Json.fromJson[Product](request.body).get

    /*保存tags 首先查看tags 是否存在，不存在则保存*/
    for(name<-product.tags){
      /*处理tag*/
      val tag= TagDao.findByName(name)
      if (tag.isEmpty)TagDao.addTag(name)
      else TagDao.modifyTag(tag.get.id.get,tag.get.addNum+1)
      /*处理 tag goods*/
      val tagGoods = TagDao.checkGoods(name,product.id.get);
      if (tagGoods.isEmpty) TagDao.addGoods(name,product.id.get)
      else TagDao.modifyGoods(name,product.id.get,tagGoods.get.addNum+1)
    }
    /*处理评论 */
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
       GoodsDao.addAssess(GoodsAssess(None,product.id.get,user.get.id.get,user.get.name,product.proComment.getOrElse("none"),false,false,1,None))
    Ok(Json.obj("code"->"100","message"->"success"))
  }



  /* 淘宝客 go to taobao */
  def gotoTaobao(numIid:Long,goodsId:Long,rate:Int) =Users.UserAction { user => implicit request =>
      val uid:Long = if(!user.isEmpty){ user.get.id.get }else{ 0 }
      val timestamp= String.valueOf(System.currentTimeMillis)
      val sign=TaobaoConfig.getSign(timestamp)
      Ok(views.html.ugc.api.gotoTaobao(numIid,uid,goodsId,rate)).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))
  }
  /*
  * uid:Long,goodsId:Long,numIid:Long,nick:String,title:String,location:String,pic:String,price:String,withdrawRate:Int,credits:Int
  * */
  def ajaxRecord = Action(parse.json) {  implicit request =>
  val record =Json.fromJson[BuyRecord](request.body).get
      val uid = record.uid.toLong
      val numIid=record.numIid.toLong
      val goodsId=record.goodsId.toLong
      val rate = record.rate.toInt
      val price =if(record.promotionPrice!="")record.promotionPrice else record.price
      val withdrawRate= (rate*0.01*record.commissionRate.toFloat).toInt
      val credits = (price.toFloat*withdrawRate*0.01).toInt
      UserDao.addUserOrder(uid,goodsId,numIid,record.nick,record.title,record.location,record.pic,price,withdrawRate,credits,record.volume.toString)

  Ok(Json.obj("code"->"100","message"->"success"))
  }

  /* 查询淘宝客返利 */
  def rebateProduct(detailUrl:String) = Users.UserAction { user => implicit request =>
    val numIid =Utils.getURLParam(detailUrl,"id").get.toLong;
    Redirect(controllers.routes.Search.convertProduct(numIid))
  }
}
