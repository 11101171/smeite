package controllers.admin

import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import play.api.libs.json._
import  models.goods.dao.GoodsDao
import utils.TaobaoConfig

import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import javax.imageio.ImageIO
import java.net.URL
import java.awt.image.BufferedImage
import play.api.mvc.Cookie
import models.goods.ShopDao


case class GoodsFormData(id:Long,isMember:Boolean,loveNum:Int,intro:String,promotionPrice:Option[String],name:String,pic:String,clickUrl:String)
case class GoodsBatchFormData(action:Int,ids:Seq[Long],rates:Seq[Int],url:Option[String])
case  class GoodsFilterFormData(goodsId:Option[Long],status:Option[Int],isMember:Option[Boolean],idOrder:Option[String],collectTimeOrder:Option[String],loveNumOrder:Option[String],currentPage:Option[Int])
case  class GoodsCollectFormData(title:String,numIid:Long,price:String,volume:Int,promotionPrice:Option[String])
case  class AssessFilterFormData(checkState:Option[Int],currentPage:Option[Int])
case class TaobaokeItem(
 title:String,
 picUrl:String,
 numIid:Long,
 volume:Int,
 price:String,
 promotionPrice:String,
 commissionRate:String,
clickUrl:String
                         )

object Goods extends Controller {

  val goodsForm =Form(
    mapping(
      "id"->longNumber,
      "isMember"->boolean,
      "loveNum"->number,
       "intro"->text,
      "promotionPrice"->optional(text),
      "name"->text,
      "pic"->text,
      "clickUrl"->text
    )(GoodsFormData.apply)(GoodsFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "rates"->seq(number),
      "url"->optional(text)
    )(GoodsBatchFormData.apply)(GoodsBatchFormData.unapply)
  )
  /*检索标签*/
  val goodsFilterForm =Form(
    mapping(
      "goodsId"->optional(longNumber),
      "status"->optional(number),
      "isMember"->optional(boolean),
      "idOrder"->optional(text),
      "collectTimeOrder"->optional(text),
      "loveNumOrder"->optional(text),
      "currentPage"->optional(number)
    )(GoodsFilterFormData.apply)(GoodsFilterFormData.unapply)
  )
  val assessFilterForm =Form(
    mapping(
      "checkState"->optional(number),
      "currentPage"->optional(number)
    )(AssessFilterFormData.apply)(AssessFilterFormData.unapply)
  )
  val collectForm =Form(
    mapping(
      "title"->nonEmptyText(),
      "numIid"->longNumber,
      "price"->nonEmptyText(),
      "volume"->number,
      "promotionPrice"->optional(text)
    )(GoodsCollectFormData.apply)(GoodsCollectFormData.unapply)
  )

  /*json */
  implicit  object TaobaokeItemFormat extends Format[TaobaokeItem]{
    def writes(o:TaobaokeItem): JsValue = JsObject(
      List(
        "title" -> JsString(o.title),
        "picUrl" -> JsString(o.picUrl),
        "numIid"->JsNumber(o.numIid),
        "volume" -> JsNumber(o.volume),
        "price" -> JsString(o.price),
        "promotionPrice" ->JsString(o.promotionPrice),
        "commissionRate" -> JsString(o.commissionRate),
        "clickUrl" -> JsString(o.clickUrl)
      )
    )
    def reads(json: JsValue): JsResult[TaobaokeItem] = JsSuccess(TaobaokeItem(
      (json \ "title").as[String],
      (json \ "pic_url").as[String],
      (json \ "num_iid").as[Long],
      (json \ "volume").as[Int],
      (json \ "price").as[String],
      (json \ "promotion_price").as[String],
      (json \ "commission_rate").as[String],
      (json \ "click_url").as[String]
    )
    )
  }

  /*宝贝管理*/
  def list(p:Int) = Managers.AdminAction{ manager => implicit request =>
    val page = GoodsDao.findAll(p,40)
    val timestamp= String.valueOf(System.currentTimeMillis)
    val sign=TaobaoConfig.getSign(timestamp)
   Ok(views.html.admin.goods.list(manager,page)).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))
  }
  /*删除*/
  def delete(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =GoodsDao.deleteGoods(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("删除成功" )))
    else Ok(Json.obj( "code" -> "101", "message" -> ("删除失败" )))
  }
  /* edit goods */
  def edit(id:Long)  = Managers.AdminAction{manager => implicit request =>
     val  goods =GoodsDao.findById(id)
     Ok(views.html.admin.goods.editGoods(manager,goodsForm.fill(GoodsFormData(goods.get.id.get,goods.get.isMember,goods.get.loveNum,goods.get.intro,goods.get.promotionPrice,goods.get.name,goods.get.pic,goods.get.clickUrl.getOrElse("")))))
  }

  /* save goods*/
  def save   = Managers.AdminAction{manager => implicit request =>
    goodsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.goods.editGoods(manager,formWithErrors)),
      goods => {
        GoodsDao.modifyGoods(goods.id,goods.name,goods.isMember,goods.loveNum,goods.intro,goods.promotionPrice,goods.clickUrl)
        Ok(views.html.admin.goods.editGoods(manager,goodsForm.fill(goods),"保存成功"))
      }
    )

  }

  /*宝贝 评价管理*/
  def assess(p:Int)= Managers.AdminAction{ manager => implicit request =>
    val  page =GoodsDao.findAssesses(p,10)
    Ok(views.html.admin.goods.assess(manager,page))
  }

  /*商品筛选*/
  def filterAssesses = Managers.AdminAction{ manager => implicit request =>
    assessFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      assess => {
        val page=GoodsDao.filterAssesses(assess.checkState,assess.currentPage.getOrElse(1),40)
        Ok(views.html.admin.goods.filterAssesses(manager,page,assessFilterForm.fill(assess)))

      }
    )

  }
  /*评价 删除*/
  def deleteAssess(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =GoodsDao.deleteAssess(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("删除成功" )))
    else Ok(Json.obj( "code" -> "101", "message" -> ("删除失败" )))
  }
  /*评价 审核*/
  def checkAssess(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =GoodsDao.modifyAssess(id,1)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("审核成功" )))
    else Ok(Json.obj( "code" -> "101", "message" -> ("审核失败" )))
  }

  /* 店铺管理*/
  def shops(p:Int) = Managers.AdminAction{ manager => implicit request =>
    val page=ShopDao.list(p);
    Ok(views.html.admin.goods.shops(manager,page))
  }
  /*店铺 删除*/
  def deleteShop(id:Long) = Managers.AdminAction{manager => implicit request =>
    val result =ShopDao.deleteShop(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" -> ("删除成功" )))
    else Ok(Json.obj( "code" -> "104", "message" -> ("删除失败" )))
  }

  /* 商品批量处理 */
  def batchGoodses  = Managers.AdminAction{manager => implicit request =>
      batchForm.bindFromRequest.fold(
        formWithErrors =>Ok("something wrong"),
        batch => {
          if(batch.action == 0){
            for(id<-batch.ids){
              GoodsDao.modifyStatus(id,0)
            }
          }else if (batch.action ==1){
            for(id<-batch.ids){
              GoodsDao.modifyStatus(id,1)
            }
          }else if (batch.action ==2){
            for(id<-batch.ids){
              GoodsDao.modifyIsMember(id,true)
            }
          }else if (batch.action ==3){
            for(id<-batch.ids){
              GoodsDao.modifyIsMember(id,false)
            }
          }else if (batch.action ==4){
            for(id<-batch.ids){
              GoodsDao.deleteGoods(id)
            }
          } else if(batch.action ==5){
            for((id,i)<-batch.ids.view.zipWithIndex){
              GoodsDao.modifyRate(id,batch.rates(i))
            }
          } else if(batch.action ==6){
            for((id,i)<-batch.ids.view.zipWithIndex){
              GoodsDao.modifyRate(id,batch.rates(i))
            }
          }
          Redirect(batch.url.getOrElse("/admin/goods/list"))
        }
      )
    }

  /*商品筛选*/
  def filterGoodses = Managers.AdminAction{ manager => implicit request =>
    goodsFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      goods => {
        val page=GoodsDao.filterGoodses(goods.goodsId,goods.status,goods.isMember,goods.idOrder,goods.collectTimeOrder,goods.loveNumOrder,goods.currentPage.getOrElse(1),40);
        val timestamp= String.valueOf(System.currentTimeMillis)
        val sign=TaobaoConfig.getSign(timestamp)
        Ok(views.html.admin.goods.filterGoodses(manager,page,goodsFilterForm.fill(goods))).withCookies(Cookie("timestamp",timestamp,httpOnly=false),Cookie("sign", sign,httpOnly=false))

      }
    )

  }

  /* 商品批量处理 */
  def batchAssesses  = Managers.AdminAction{manager => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            GoodsDao.modifyAssess(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            GoodsDao.modifyAssess(id,1)
          }
        }else if (batch.action ==2){
          for(id<-batch.ids){
            GoodsDao.deleteAssess(id)
          }
        }
        Redirect(batch.url.getOrElse("/admin/goods/assess"))
      }
    )
  }

  /* ajax update collection 淘宝客 */

  def collectGoodses = Action(parse.json) {  implicit request =>
  val items =Json.fromJson[Array[TaobaokeItem]](request.body).get
  for(item <- items){
   // println(item.commissionRate.toFloat.toInt)
    val image:BufferedImage = ImageIO.read(new URL(item.picUrl))
    val height = image.getHeight
    val width = image.getWidth
    val hwRate:Float = height.toFloat/width
 //   println("height: "+height + ",width "+ width+" : " + hwRate)
    GoodsDao.updateTaobaoke(item.numIid,item.title,item.picUrl,item.volume,item.price,item.promotionPrice,item.commissionRate.toFloat.toInt,hwRate,item.clickUrl)
  }
     Ok(Json.obj("code"->"100","msg"->items.length))
  }




}
