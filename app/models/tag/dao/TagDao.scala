package models.tag.dao


import models.tag._
import models.goods.{Goods, GoodsAssesses, Goodses}
import models.user.Users
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.Page
import  models.user.User
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-12上午9:43
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object TagDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addTag(name:String) =database.withSession {  implicit session:Session =>
    (Tags.name).insert(name)
  }
  def addTag(tag:Tag) =database.withSession {  implicit session:Session =>
    Tags.insert(tag)
  }
  def deleteTag(id:Long)=database.withSession {  implicit session:Session =>
    Tags.delete(id)
  }
  def deleteTag(name:String)=database.withSession {  implicit session:Session =>
    Tags.delete(name)
  }
  /*修改tag时，同时修改 tag_goods 的 cid*/
  def modifyTag(tag:Tag)= database.withSession{ implicit session:Session =>
    (for( c <- TagGoodses if c.tagName === tag.name )yield c.cid ).update(tag.cid.getOrElse(0))
    (for(c<-Tags if c.id === tag.id)yield(c)).update(tag)
  }
  def modifyTag(id:Long,addNum:Int)=database.withSession{ implicit session:Session =>
    (for(c<-Tags if c.id === id)yield(c.addNum)).update(addNum)
  }
  def modifyTagCheckState(id:Long,checkState:Int)=database.withSession{ implicit session:Session =>
    (for(c<-Tags if c.id === id)yield(c.checkState)).update(checkState)
  }
  def modifyTagSortNum(id:Long,num:Int)=database.withSession{ implicit session:Session =>
    (for(c<-Tags if c.id === id)yield(c.sortNum)).update(num)
  }
  def modifyTagTopState(id:Long,isTop:Boolean)=database.withSession{ implicit session:Session =>
    (for(c<-Tags if c.id === id)yield(c.isTop)).update(isTop)
  }
  def modifyTagHighlightState(id:Long,isHighlight:Boolean)=database.withSession{ implicit session:Session =>
    (for(c<-Tags if c.id === id)yield(c.isHighlight)).update(isHighlight)
  }

  def findById(id:Long):Option[Tag] =  database.withSession {  implicit session:Session =>
    Tags.find(id)
  }
  def findByName(name:String):Option[Tag] =database.withSession {  implicit session:Session =>
    Tags.find(name)
  }

  /* 显示所用的tag */
  def findAll(currentPage: Int , pageSize: Int ): Page[Tag] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Tags.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Tags.sortBy(_.isTop desc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val tags:List[Tag]=  q.list()
    Page[Tag](tags,currentPage,totalPages);

  }
  /* list    tag 主要用于前台页面的tag 展示 */
  def findCateTags(cid:Int,tagNum:Int):List[Tag]= database.withSession{ implicit session:Session =>
      (for(c<-Tags.filter(_.cid===cid).filter(_.isTop===true).sortBy(_.sortNum asc).take(tagNum))yield c).list()
  }

 /* 查找tag下的所有审核过的商品 */
  def findTagGoodses(tagName:String,currentPage:Int,pageSize: Int ):Page[((Int,Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = database.withSession{ implicit session:Session =>
    val totalRows = Query(TagGoodses.filter(_.tagName === tagName ).filter(_.checkState ===1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val q = for{
      (((t,g),a),u)<-TagGoodses.filter(_.tagName === tagName).filter(_.checkState===1).drop(startRow).take(pageSize) innerJoin Goodses on (_.goodsId ===_.id)  leftJoin  GoodsAssesses  on (_._2.id ===_.goodsId) leftJoin Users on (_._2.uid === _.id)
    }yield(t.sortNum,g.id,g.name,g.pic,g.loveNum,g.price,g.promotionPrice.?,g.intro,u.id.?,u.name.?,u.pic.?,a.content.?)

    val list:List[((Int,Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = q.list.groupBy(x=>(x._1,x._2,x._3,x._4,x._5,x._6,x._7,x._8)).map(x=>(x._1,x._2.map(y=>(y._9,y._10,y._11,y._12)))).toList.sortBy(_._1)
    Page(list,currentPage,totalPages)
  }

  /* 查找 tagGroup 下 所有的商品 */
  def findCateGoodses(cid:Int,currentPage:Int,pageSize:Int ):Page[((Int,Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = database.withSession{ implicit session:Session =>
    val totalRows = Query(TagGoodses.filter(_.cid === cid ).filter(_.checkState ===1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    //println(" totalRows  " + totalRows + " totalPages  " + totalPages  + " startRow " +startRow )

    val q = for{
      (((t,g),a),u)<-TagGoodses.filter(_.cid === cid).filter(_.checkState===1).sortBy(_.sortNum desc).drop(startRow).take(pageSize) innerJoin Goodses on (_.goodsId ===_.id) leftJoin  GoodsAssesses  on (_._2.id ===_.goodsId) leftJoin Users on (_._2.uid === _.id)
    }yield(t.sortNum,g.id,g.name,g.pic,g.loveNum,g.price,g.promotionPrice.?,g.intro,u.id.?,u.name.?,u.pic.?,a.content.?)
   //  println(" sq  " +q.list().length)
	 for( x <- q.list()){
	 //println(x._2)
	 }
    val list:List[((Int,Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = q.list.groupBy(x=>(x._1,x._2,x._3,x._4,x._5,x._6,x._7,x._8)).map(x=>(x._1,x._2.map(y=>(y._9,y._10,y._11,y._12)))).toList.sortBy(_._1)
    Page(list,currentPage,totalPages)
  }

  /* 查找tag下的所有审核过的商品 */
  /*
  * 1 推荐
  * 2 最新
  * 3 最热
  * */
  def findSimpleTagGoodses(tagName:String,s:Int,currentPage:Int,pageSize: Int ):Page[(User,Goods)] = database.withSession{ implicit session:Session =>
    val totalRows = Query(TagGoodses.filter(_.tagName === tagName ).filter(_.checkState ===1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize)
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var q = for{
      t <- TagGoodses
      g <- Goodses
      u <- Users
      if t.tagName === tagName
      if t.goodsId === g.id
      if g.uid === u.id
    }yield(u,g,t.sortNum)
    if(s == 1) q = q.sortBy(x=>x._2.isMember.desc)
    if(s == 2) q = q.sortBy(x=>x._2.collectTime.desc)
    if(s == 3 )q = q.sortBy(x=>x._2.loveNum.desc)
    q=q.sortBy(x=>x._3.desc)
    val ug=q.list().distinct.drop(startRow).take(pageSize).map(x =>(x._1,x._2))
    Page(ug,currentPage,totalPages)
  }
  /*
  * 1 推荐
  * 2 最新
  * 3 最热
  * */
  def  findGoodsesByTagName(tagName:String,s:Int,currentPage:Int,pageSize: Int):Page[Goods] = database.withSession{ implicit session:Session =>
    val totalRows = Query(TagGoodses.filter(_.tagName === tagName ).filter(_.checkState ===1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize)
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var q = for{
      t <- TagGoodses
      g <- Goodses
      if t.tagName === tagName
      if t.goodsId === g.id
    }yield(g,t.sortNum)
    if(s == 1) q = q.sortBy(x=>x._1.isMember.desc)
    if(s == 2) q = q.sortBy(x=>x._1.collectTime.desc)
    if(s == 3 )q = q.sortBy(x=>x._1.loveNum.desc)
    q=q.sortBy(x=>x._2.desc)
    val ug=q.list().distinct.drop(startRow).take(pageSize).map(x =>x._1)
    Page(ug,currentPage,totalPages)
  }
   /*
   * 1 推荐
   * 2 最新
   * 3 最热
   * */
  def findSimpleCateGoodses(cid:Int,s:Int,currentPage:Int,pageSize:Int ):Page[(User,Goods)] = database.withSession{ implicit session:Session =>
    val totalRows = Query(TagGoodses.filter(_.cid === cid ).filter(_.checkState ===1).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var q = for{
      t <- TagGoodses
      g <- Goodses
      u <- Users
      if  t.cid === cid
      if  t.goodsId === g.id
      if g.uid === u.id
    }yield(u,g,t.sortNum)

    if(s == 1) q = q.sortBy(x=>x._2.isMember.desc)
    if(s == 2) q = q.sortBy(x=>x._2.collectTime.desc)
    if(s == 3 )q = q.sortBy(x=>x._2.loveNum.desc)
     q=q.sortBy(x=>x._3.desc)
    val ug=q.list().distinct.drop(startRow).take(pageSize).map(x =>(x._1,x._2))
    Page(ug,currentPage,totalPages)
  }

  /* 显示所有的商品*/
  def findAllGoodses(currentPage:Int ,pageSize: Int):Page[((Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = database.withSession{ implicit session:Session =>
    val totalRows =Query(Goodses.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize)
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }

    val q =for {
      ((g,a),u)<- Goodses.sortBy(x=>(x.collectTime desc)).drop(startRow).take(pageSize) leftJoin  GoodsAssesses.where(_.checkState===1)  on (_.id ===_.goodsId) leftJoin Users on (_._2.uid === _.id)
    } yield(g.id,g.name,g.pic,g.loveNum,g.price,g.promotionPrice.?,g.intro,u.id.?,u.name.?,u.pic.?,a.content.?)
    val list:List[((Long,String,String,Int,String,Option[String],String),List[(Option[Long],Option[String],Option[String],Option[String])])] = q.list.groupBy(x=>(x._1,x._2,x._3,x._4,x._5,x._6,x._7)).map(x=>(x._1,x._2.map(y=>(y._8,y._9,y._10,y._11)))).toList
    Page(list,currentPage,totalPages)
  }


  /* tag goods */

  def checkGoods(tagName:String,goodsId:Long):Option[TagGoods] = database.withSession {  implicit session:Session =>
    TagGoodses.find(tagName,goodsId)
  }
  /*保存*/
  def addGoods(tagName:String,goodsId:Long)=database.withSession {  implicit session:Session =>
    val cid =( for( c<- Tags if c.name === tagName ) yield c.cid ).first()
    TagGoodses.insert(tagName,goodsId,cid)
  }
  def modifyGoods(tagName:String,goodsId:Long,addNum:Int)=database.withSession {  implicit session:Session =>
    (for(c<-TagGoodses if c.tagName===tagName if c.goodsId===goodsId)yield(c.addNum) ).update(addNum)
  }
  def modifyGoods(goodsId:Long,tagName:String) =  database.withSession {  implicit session:Session =>
    (for (c<-TagGoodses if c.id === goodsId) yield(c.tagName)).update(tagName)
  }
  def modifyGoods(id:Long,checkState:Int) =  database.withSession {  implicit session:Session =>
    (for (c<-TagGoodses if c.id === id) yield(c.checkState)).update(checkState)
  }
  def modifyTagGoods(id:Long,sortNum:Int) = database.withSession {  implicit session:Session =>
    (for (c<-TagGoodses if c.id === id) yield(c.sortNum)).update(sortNum)
  }
  def deleteGoods(id:Long) = database.withSession {  implicit session:Session =>
    TagGoodses.delete(id)
  }
  /*查找所用的标签商品*/
  def findAllTagGoodses(currentPage: Int , pageSize: Int ): Page[(Long,Long,String,String,String,Int,Int,Int,Int)] = database.withSession {  implicit session:Session =>
    val totalRows=Query(TagGoodses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(t <- TagGoodses.sortBy(_.sortNum asc); g <- Goodses;  if t.goodsId === g.id) yield(t.id~g.id~g.name~g.pic~t.tagName~t.addNum~t.checkState~t.sortNum~t.cid)
    val assesses:List[(Long,Long,String,String,String,Int,Int,Int,Int)]=  q.drop(startRow).take(pageSize).list()
    Page[(Long,Long,String,String,String,Int,Int,Int,Int)](assesses,currentPage,totalPages);
  }

  /* 查找 宝贝的 标签*/
  def findGoodsTags(goodsId:Long):List[TagGoods] = database.withSession {  implicit session:Session =>
    (for(c<-TagGoodses if c.goodsId===goodsId if c.checkState===1)yield c).list()
  }
  /* 查找 宝贝的标签和与此标签相关的商品*/
def findRelativeTagGoodses(goodsId:Long) = database.withSession{ implicit  session:Session =>
    val tags = for{
      c<-TagGoodses if c.goodsId===goodsId
    }yield c.tagName
    val q = for{
      (tg,g)<- TagGoodses.filter(_.tagName  in ( tags.take(5) )) leftJoin  Goodses.sortBy(_.isMember desc)  on (_.goodsId ===_.id)
    }yield(g)

   Page(q.take(36).list().distinct,1,1)
  }
  /* tag group */
  def addGroup(group:TagGroup) =database.withSession {  implicit session:Session =>
    TagGroups.insert(group)
  }
  def deleteGroup(id:Long)=database.withSession {  implicit session:Session =>
    (for(c<-TagGroups if c.id === id)yield(c)).delete
  }

  def modifyGroup(group:TagGroup)= database.withSession{ implicit session:Session =>
    (for(c<-TagGroups if c.id === group.id)yield(c)).update(group)
  }

  def modifyGroup(gid:Long,sortNum:Int)= database.withSession{ implicit session:Session =>
    (for(c<-TagGroups if c.id === gid)yield(c.sortNum)).update(sortNum)
  }
  def modifyGroup(gid:Long,visible:Boolean)= database.withSession{ implicit session:Session =>
    (for(c<-TagGroups if c.id === gid)yield(c.isVisible)).update(visible)
  }


  def findGroup(id:Long):Option[TagGroup] =  database.withSession {  implicit session:Session =>
    (for(c<-TagGroups if c.id ===id)yield(c)).firstOption
  }
  def findGroup(name:String):Option[TagGroup] =database.withSession {  implicit session:Session =>
    (for(c<-TagGroups if c.name === name)yield(c)).firstOption
  }

  def findSimpleGroups(cid:Int):List[(Long, String)]=database.withSession{  implicit session:Session =>
    ( for(c<-TagGroups  if c.cid === cid)yield(c.id~c.name)).list()
  }

  /*分页*/
  def findGroups(currentPage:Int, pageSize: Int): Page[TagGroup] = database.withSession {  implicit session:Session =>
    val totalRows=Query(TagGroups.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TagGroups.sortBy(_.sortNum asc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val groups:List[TagGroup]=  q.list()
    Page[TagGroup](groups,currentPage,totalPages);
  }

  def findGroups(cid:Int,currentPage: Int, pageSize: Int): Page[TagGroup] = database.withSession {  implicit session:Session =>
    val totalRows=Query(TagGroups.length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TagGroups.sortBy(_.sortNum asc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val groups:List[TagGroup]=  q.list()
    Page[TagGroup](groups,currentPage,totalPages);
  }

  def filterGroups(name:Option[String],cid:Option[Int],isVisible:Option[Boolean],currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>

    var query =for(c<-TagGroups)yield c
    if(!name.isEmpty) query = query.filter(_.name like "%"+name.get+"%")
    if(!cid.isEmpty) query = query.filter(_.cid === cid.get)
    if(!isVisible.isEmpty) query = query.filter(_.isVisible === isVisible.get)
     query = query.sortBy(_.sortNum asc)
    //println("sql " +query.selectStatement)

    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val groups:List[TagGroup]=  query.drop(startRow).take(pageSize).list()
    Page[TagGroup](groups,currentPage,totalPages);

  }

  def filterTags(name:Option[String],cid:Option[Int],groupId:Option[Long],checkState:Option[Int],isTop:Option[Boolean],isHighlight:Option[Boolean],currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>
     var query = for (c<-Tags)yield c
     if(!name.isEmpty) query = query.filter(_.name like "%"+name.get+"%")
     if(!cid.isEmpty) query = query.filter(_.cid === cid.get)
     if(!groupId.isEmpty) query = query.filter(_.groupId === groupId.get)
     if(!checkState.isEmpty) query =query.filter(_.checkState === checkState.get)
     if(!isTop.isEmpty) query = query.filter(_.isTop === isTop.get)
     if(!isHighlight.isEmpty) query = query.filter(_.isHighlight === isHighlight.get)
     query = query.sortBy(_.isTop desc)
     //println("sql " +query.selectStatement)
     val totalRows=query.list().length
     val totalPages=((totalRows + pageSize - 1) / pageSize);
     val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val tags:List[Tag]= query.drop(startRow).take(pageSize).list()
     Page[Tag](tags,currentPage,totalPages);

  }

  def filterTagGoodses(name:Option[String],cid:Option[Int],checkState:Option[Int],currentPage:Int, pageSize:Int): Page[(Long,Long,String,String,String,Int,Int,Int,Int)] = database.withSession {  implicit session:Session =>
    var query = for(t <- TagGoodses.sortBy(_.sortNum asc); g <- Goodses;  if t.goodsId === g.id) yield(t.id ~g.id~g.name~g.pic~t.tagName~t.addNum~t.checkState~t.sortNum ~ t.cid)
    if(!name.isEmpty) query =query.filter(_._5 === name.get)
    if(!cid.isEmpty) query =query.filter(_._9 === cid.get)
    if(!checkState.isEmpty) query =query.filter(_._7 === checkState.get)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    //println(" q sql "+query.selectStatement)
    val assesses:List[(Long,Long,String,String,String,Int,Int,Int,Int)]=  query.drop(startRow).take(pageSize).list()
    Page[(Long,Long,String,String,String,Int,Int,Int,Int)](assesses,currentPage,totalPages);
  }


}
