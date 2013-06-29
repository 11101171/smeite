package models.goods.dao
import models.goods._
import models.user.{UserShareGoodses, UserLoveGoodses, User, Users}
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.Page
import models.tag.TagGoodses
import models.theme.ThemeGoodses
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-12下午11:39
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object GoodsDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  /*保存*/
  def addGoods(uid:Long,numIid:Long,name: String,intro: String,price:String,pic: String,itemPics: String,nick:String,clickUrl:String,hwRate:Float):Long=database.withSession {  implicit session:Session =>
    Goodses.autoInc.insert(uid,numIid,name,intro,price,pic,itemPics,nick,clickUrl,hwRate)

  }
  /* delete 删除goods 需要把相关的信息删除 */
  def deleteGoods(id:Long) = database.withSession {  implicit session:Session =>
    (for(c<-GoodsAssesses if c.goodsId ===id)yield c).delete
    (for(c<-TagGoodses if c.goodsId ===id)yield c).delete
    (for(c<-ThemeGoodses if c.goodsId ===id)yield c).delete
    (for(c<-GoodsShops if c.goodsId ===id)yield c).delete
    (for(c<-UserLoveGoodses if c.goodsId ===id)yield c).delete
    (for(c<-UserShareGoodses if c.goodsId ===id)yield c).delete
    (for(c<-Goodses if c.id===id)yield(c)).delete
  }
  def modifyStatus(goodsId:Long,status:Int) = database.withSession {  implicit session:Session =>
    (for (c<-Goodses if c.id === goodsId)yield c.status ).update(status)
  }

  def modifyClickUrl(goodsId:Long,clickUrl:String)= database.withSession {  implicit session:Session =>
    (for (c<-Goodses if c.id === goodsId)yield c.clickUrl ).update(clickUrl)
  }
 def modifyIsMember(goodsId:Long,isMember:Boolean) =  database.withSession {  implicit session:Session =>
   (for (c<-Goodses if c.id === goodsId)yield c.isMember ).update(isMember)
 }
  def modifyGoods(goodsId:Long,goodsName:String,isMember:Boolean,loveNum:Int,intro:String,promotionPrice:Option[String],clickUrl:String) = database.withSession {  implicit session:Session =>
    (for (c<-Goodses if c.id === goodsId)yield c.name~c.isMember~c.loveNum~c.intro~c.promotionPrice~c.clickUrl ).update((goodsName,isMember,loveNum,intro,promotionPrice.getOrElse(""),clickUrl))
  }
  /* 修改价格*/
  def updateTaobaoke(numIid:Long,name:String,pic:String,volume:Int,price:String,promotionPrice:String,commissionRate:Int,hwRate:Float,clickUrl:String) = database.withSession {  implicit session:Session =>
    (for (c<-Goodses if c.numIid === numIid )yield c.name ~ c.pic ~ c.volume ~ c.price ~c.promotionPrice ~ c.commissionRate ~ c.hwRate ~ c.clickUrl ~ c.collectTime).update((name,pic,volume,price,promotionPrice,commissionRate,hwRate,clickUrl,new Timestamp(System.currentTimeMillis())))
  }


  def findById(id:Long):Option[Goods]=database.withSession {  implicit session:Session =>
    (for(c<-Goodses if c.id===id)yield(c)).firstOption
  }
  /*根据num_iid 查找*/
  def find(numIid:Long):Option[Goods] = database.withSession {  implicit session:Session =>
    (for(c<-Goodses if c.numIid===numIid)yield(c)).firstOption
  }
  def countGoods:Int = database.withSession {  implicit session:Session =>
    Query(Goodses.length).first()
  }
  /*分页查找*/
  /*分页显示*/
  def findAll(currentPage: Int, pageSize: Int): Page[Goods] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Goodses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Goodses.sortBy(_.id desc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val goodses:List[Goods]=  q.list()
    Page[Goods](goodses,currentPage,totalPages);
  }

  /*  goods assess */
  def addAssess(assess:GoodsAssess):Long=database.withSession {  implicit session:Session =>
    GoodsAssesses.autoInc.insert(assess)
  }
  def addAssess(goodsId:Long,uid:Long,uname:String,content:String,checkState:Int)=database.withSession {  implicit session:Session =>
    GoodsAssesses.autoInc2.insert(goodsId,uid,uname,content,checkState);
  }
  /* delete */
  def deleteAssess(id:Long) = database.withSession {  implicit session:Session =>
    (for(c<-GoodsAssesses if c.id===id)yield(c)).delete
  }
  /*check state */
  def modifyAssess(id:Long,checkState:Int)= database.withSession {  implicit session:Session =>
    (for(c<-GoodsAssesses if c.id===id)yield(c.checkState)).update(checkState)
  }

  /*分页查找*/
  /*分页显示*/
  def findAssesses(currentPage: Int , pageSize: Int ): Page[GoodsAssess] = database.withSession {  implicit session:Session =>
    val totalRows=Query(GoodsAssesses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-GoodsAssesses.sortBy(_.addTime desc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val assesses:List[GoodsAssess]=  q.list()
    Page[GoodsAssess](assesses,currentPage,totalPages);
  }
  /* 根据goods 显示分页 只显示 */
  def findGoodsAssesses(goodsId:Long,currentPage: Int , pageSize: Int ): Page[(User,GoodsAssess)] = database.withSession {  implicit session:Session =>
    val totalRows=Query(GoodsAssesses.filter(_.goodsId === goodsId).filter(_.checkState === 1).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for( c<-GoodsAssesses;u<-Users; if c.uid === u.id ;if c.goodsId === goodsId ; if c.checkState === 1  )yield(u,c)

    val assesses:List[(User,GoodsAssess)]=  q.drop(startRow).take(pageSize).list()
    Page[(User,GoodsAssess)](assesses,currentPage,totalPages);
  }


  /* 筛选goods */
  def filterGoodses(goodsId:Option[Long],status:Option[Int],isMember:Option[Boolean],idOrder:Option[String],collectTimeOrder:Option[String],loveNumOrder:Option[String],currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
    var query = for(c<- Goodses)yield c
    if(!goodsId.isEmpty) query =query.filter(_.id === goodsId.get)
    if(!status.isEmpty) query =query.filter(_.status === status.get)
    if(!isMember.isEmpty) query = query.filter(_.isMember === isMember.get)
    if(!idOrder.isEmpty){
      if(idOrder.get=="desc") query = query.sortBy(_.id desc ) else query = query.sortBy(_.id asc)
    }
    if(!collectTimeOrder.isEmpty){
      if(collectTimeOrder.get=="desc") query = query.sortBy(_.collectTime desc ) else query = query.sortBy(_.collectTime asc)
    }
    if(!loveNumOrder.isEmpty){
      if(loveNumOrder.get=="desc") query = query.sortBy(_.loveNum desc) else query = query.sortBy(_.loveNum asc)
    }

    //println("sql " +query.selectStatement)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val goodses:List[Goods]=  query.drop(startRow).take(pageSize).list()
    Page[Goods](goodses,currentPage,totalPages);
  }
  
  /* 小编推荐（会员） 的商品 */
  def  findMemberGoods(currentPage:Int,pageSize:Int):Page[Goods] = database.withSession {  implicit session:Session =>
	val totalRows =Query(Goodses.filter(_.isMember===true).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }

    val query =for(c<-Goodses if c.isMember === true)yield c
    //println("q select sql "+query.selectStatement)
    val list:List[Goods] = query.drop(startRow).take(pageSize).list()
    Page(list,currentPage,totalPages)
  }

  /* 显示用户可能喜欢的商品 */
  /*
  * 目前 只是推荐喜欢比较多的商品
  * 以后 根据用户的tag标签和商品标签 推荐用户可能喜欢的商品
  * */

  def  guessUserLikes(currentPage: Int, pageSize: Int): Page[Goods] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Goodses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Goodses.filter(_.isMember===true).sortBy(_.collectTime desc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val goodses:List[Goods]=  q.list()
    Page[Goods](goodses,currentPage,totalPages);
  }


  /*filter goods assess*/
  def filterAssesses(checkState:Option[Int],currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>
    var query = for (c<-GoodsAssesses)yield c
    if(!checkState.isEmpty) query =query.filter(_.checkState === checkState.get)
    query = query.sortBy(_.addTime desc)
    //println("sql " +query.selectStatement)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val tags:List[GoodsAssess]= query.drop(startRow).take(pageSize).list()
    Page[GoodsAssess](tags,currentPage,totalPages);
  }

  /* 获取numiids*/
  def getNumiids(currentPage:Int,pageSize:Int):Page[Long]= database.withSession {  implicit session:Session =>
    val totalRows=Query(Goodses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val items:List[Long]= (for(c<-Goodses)yield c.numIid ).drop(startRow).take(pageSize).list()

    Page[Long](items,currentPage,totalPages)


  }
}
