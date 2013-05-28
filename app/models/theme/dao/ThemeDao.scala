package models.theme.dao

import  java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import models.user._
import models.goods.{Goodses, Goods}
import models.theme._
import models.user.dao.UserSQLDao
import models.user.dao.UserDao



/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-11下午7:29
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object ThemeDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def addTheme(name:String,uid:Long,uname:String)=database.withSession {  implicit session:Session =>
    UserSQLDao.updatePostThemeNum(uid,1)

   val themeId = Themes.autoInc2.insert(name,uid,uname,new Timestamp(System.currentTimeMillis()))
    ThemeStyles.insert(ThemeStyle(themeId,"#F9F9EF","none","no-repeat","center top","scroll","70","#666","#fafafa","none","repeat","center top"))
    /*保存用户动作*/
    UserDao.addTrend(UserTrend(None,uid,"创建了主题",themeId,"/theme/"+themeId,name,None))

    themeId
  }
  def addTheme(name:String,intro:Option[String],uid:Long,uname:String,cid:Int,tags:Option[String])=database.withSession {  implicit session:Session =>
    UserSQLDao.updatePostThemeNum(uid,1)
   val themeId = Themes.autoInc3.insert(name,intro,uid,uname,cid,tags, new Timestamp(System.currentTimeMillis()))
    ThemeStyles.insert(ThemeStyle(themeId,"#F9F9EF","none","no-repeat","center top","scroll","70","#666","#fafafa","none","repeat","center top"))
   /*保存用户动作*/
    UserDao.addTrend(UserTrend(None,uid,"创建了主题",themeId,"/theme/"+themeId,name,None))
	themeId

  }
  def modifyTheme(id:Long,name:String,pic:Option[String],hotIndex:Int,isRecommend:Boolean,seoTitle:Option[String],seoKeywords:Option[String],seoDesc:Option[String])=database.withSession{ implicit session:Session =>
   (for(c<-Themes if c.id === id)yield c.name ~c.pic ~ c.hotIndex ~ c.isRecommend ~ c.seoTitle ~ c.seoKeywords ~ c.seoDesc).update((name,pic.getOrElse("/images/theme/default.jpg"),hotIndex,isRecommend,seoTitle.getOrElse(""),seoKeywords.getOrElse(""),seoDesc.getOrElse("")))
  }
  def modifyTheme(id:Long,name:String,intro:String,cid:Int,tags:String)=database.withSession{ implicit session:Session =>
       (for(c<-Themes if c.id === id )yield(c.name ~ c.intro ~ c.cid ~ c.tags)).update((name,intro,cid,tags))
  }
  def  modifyThemeVisible(themeId:Long,isRecommend:Boolean)=database.withSession {  implicit session:Session =>
     (for (c<-Themes if c.id === themeId)yield c.isRecommend).update(isRecommend)
  }
  /*删除主题的同时，需要把主题的样式给删除掉 theme_goods 删除*/
  def deleteTheme(id:Long)=database.withSession {  implicit session:Session =>
    ThemeStyles.delete(id)
    ThemeGoodses.delete(id)
    (for(c<- ThemeDiscusses if c.themeId === id)yield c).delete
    (for (c<-UserLoveThemes if c.themeId === id)yield c).delete
    Themes.delete(id)
  }

  def countTheme:Int = database.withSession {  implicit session:Session =>
         Themes.count()
  }

  def findById(id:Long):Option[Theme] =  database.withSession {  implicit session:Session =>
        Themes.find(id)
  }
  def findByName(name:String):Option[Theme] =database.withSession {  implicit session:Session =>
        Themes.find(name)
  }
  def findWithStyle(themeId:Long):Option[(Theme,ThemeStyle)] =database.withSession {  implicit session:Session =>
       val query = for{
         t<-Themes.filter(_.id === themeId)
         ts<-ThemeStyles.filter(_.themeId === themeId)
       }yield(t,ts)
       query.firstOption
  }
  /*分页显示*/
  def findAll(currentPage: Int = 1, pageSize: Int = 50): Page[Theme] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Themes.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Themes.sortBy(_.modifyTime desc).drop(startRow).take(pageSize)  ) yield(c)
    //println(" q sql "+q.selectStatement)
    val themes:List[Theme]=  q.list()
    Page[Theme](themes,currentPage,totalPages);
  }

  /* 显示某个类型下的主题 */
  def findCateThemes(cid:Int,sortBy:String,currentPage: Int, pageSize: Int ): Page[((Long,String,String,Int),List[String])] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Themes.filter(_.cid === cid).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }

    /*联合查询*/
    var query =(for{
      t<-Themes.filter(_.isRecommend===true).where(_.cid === cid).drop(startRow).take(pageSize)
      g<-ThemeGoodses
      if t.id === g.themeId
    } yield(t.id,t.name,t.intro,t.loveNum,t.modifyTime,g.goodsPic))
    if(sortBy=="new") query =query.sortBy(_._5 desc)
    if(sortBy=="hot") query = query.sortBy(_._4 desc)
    //println("query id = "+query.selectStatement)
    val themes:List[((Long,String,String,Int),List[String])] =query.list().groupBy(x=>(x._1,x._2,x._3,x._4)).map(x=>(x._1,x._2.take(5).map(y=>y._6))).toList
    Page[((Long,String,String,Int),List[String])](themes,currentPage,totalPages)
  }
  /* 显示某几个类型的主题 */
  def findCatesThemes(from:Int,to:Int,sortBy:String,currentPage: Int , pageSize: Int ): Page[((Long,String,String,Int),List[String])] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Themes.filter(_.cid.between(from,to)).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    /*联合查询*/
    var query =(for{
      t<-Themes.filter(_.isRecommend===true).where(_.cid.between(from,to)).drop(startRow).take(pageSize)
      g<-ThemeGoodses
      if t.id === g.themeId
    } yield(t.id,t.name,t.intro,t.loveNum,t.modifyTime,g.goodsPic))
    if(sortBy=="new") query =query.sortBy(_._5 desc)
    if(sortBy=="hot") query = query.sortBy(_._4 desc)
    //println("query id = "+query.selectStatement)
    val themes:List[((Long,String,String,Int),List[String])] =query.list().groupBy(x=>(x._1,x._2,x._3,x._4)).map(x=>(x._1,x._2.take(5).map(y=>y._6))).toList
    Page[((Long,String,String,Int),List[String])](themes,currentPage,totalPages)
  }

  def findThemes(sortBy:String,currentPage: Int , pageSize: Int ): Page[((Long,String,String,Int),List[String])] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Themes.filter(_.isRecommend===true).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    /*联合查询*/
    var query =(for{
      t<-Themes.filter(_.isRecommend === true).drop(startRow).take(pageSize)
      g<-ThemeGoodses
      if t.id === g.themeId
    } yield(t.id,t.name,t.intro,t.loveNum,t.modifyTime,g.sortNum,g.goodsPic))
    if(sortBy=="new") query =query.sortBy(_._5 desc)
    if(sortBy=="hot") query = query.sortBy(_._4 desc)
     println(query.list().length)
    val themes:List[((Long,String,String,Int),List[String])] =query.list().groupBy(x=>(x._1,x._2,x._3,x._4)).map(x=>(x._1,x._2.sortBy(x=>x._6).take(5).map(y=>y._7))).toList
    Page[((Long,String,String,Int),List[String])](themes,currentPage,totalPages)
  }

  /* 随机推荐  1.s首选寻找最值得推荐的theme 前20个，然后在随机从中挑选数量nums */
  def recommendTheme(hotIndex:Int,nums:Int):List[Theme] = database.withSession {  implicit session:Session =>
    val q=for(c<-Themes if c.hotIndex > hotIndex )yield(c)
    //println("query sql "+q.selectStatement)
    q.sortBy(_.hotIndex desc).take(nums).list()
  }

  /*  theme goods  */
  def addGoods(themeId:Long,goodsId:Long,goodsPic:String):Long =database.withSession {  implicit session:Session =>
    ThemeSQLDao.updateGoodsNum(themeId,1)
    ThemeGoodses.autoInc2.insert(themeId,goodsId,goodsPic)


  }
  /*删除*/
  def deleteGoods(themeId:Long,goodsId:Long)=database.withSession {  implicit session:Session =>
    ThemeSQLDao.updateGoodsNum(themeId,-1)
    ThemeGoodses.delete(themeId,goodsId)
  }
  /*  查找 theme goods  */
  def checkGoods(themeId:Long,goodsId:Long):Option[ThemeGoods]= database.withSession {  implicit session:Session =>
    ThemeGoodses.find(themeId,goodsId)
  }
  /* list by theme*/
  def findGoodses(themeId:Long):List[Goods] = database.withSession {  implicit session:Session =>
    (for {
      c<-ThemeGoodses.filter(_.themeId === themeId).sortBy(_.sortNum desc)
      g<-Goodses
      if c.goodsId === g.id
      }yield(g)).list()
  }

  /* theme style */
  def findStyle(themeId:Long):Option[ThemeStyle] =  database.withSession {  implicit session:Session =>
    (for(c<-ThemeStyles if c.themeId ===themeId)yield(c)).firstOption
  }
  def addStyle(style:ThemeStyle):Long =database.withSession {  implicit session:Session =>
    ThemeStyles.insert(style)

  }

  def modifyStyle(themeId:Long,pageBgColor:String,pageBgImage:String,pageBgRepeat:String,pageBgPosition:String,pageBgAttachment:String)=database.withSession{ implicit session:Session =>
    (for(c<- ThemeStyles if c.themeId === themeId)yield(c.pageBgColor ~ c.pageBgImage ~ c.pageBgRepeat ~ c.pageBgPosition ~ c.pageBgAttachment)).update(pageBgColor,pageBgImage,pageBgRepeat,pageBgPosition,pageBgAttachment)
  }


  /* theme tags */
  def findTags(themeId:Long)=database.withSession { implicit session:Session =>
   ThemeTags.find(themeId)
  }
  def addTag(themeId:Long,themeName:String,tagId:Long,tagName:String)=database.withSession { implicit session:Session =>
    ThemeTags.autoInc2.insert(themeId,themeName,tagId,tagName);
  }

  /*  theme discuss */
  def addDiscuss(themeId:Long,uid:Long,uname:String,content:String,checkState:Int) =database.withSession {  implicit session:Session =>
    ThemeSQLDao.updateReplyNum(themeId,1)
    ThemeDiscusses.autoInc2.insert(themeId,uid,uname,content,checkState)

  }
  /*删除*/
  def deleteDiscuss(id:Long)=database.withSession {  implicit session:Session =>
    ThemeSQLDao.updateReplyNum(id,-1)
    ThemeDiscusses.delete(id)
  }
  def modifyDiscussCheckState(id:Long,checkState:Int)=database.withSession {  implicit session:Session =>
      (for(c<-ThemeDiscusses if c.id === id)yield c.checkState).update(checkState)
  }

  def findAllDiscusses(currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>
    val totalRows= Query(ThemeDiscusses.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{ c<-ThemeDiscusses.sortBy(_.addTime desc).drop(startRow).take(pageSize)} yield(c)
    //println(" q sql "+q.selectStatement)
    val discusses:List[ThemeDiscuss]=  q.list()
    Page[ThemeDiscuss](discusses,currentPage,totalPages);
  }

  /*分页显示*/
  def findDiscusses(themeId:Long,currentPage: Int, pageSize: Int): Page[(User,ThemeDiscuss)] = database.withSession {  implicit session:Session =>
    val totalRows= Query(ThemeDiscusses.filter(_.themeId === themeId).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-ThemeDiscusses.filter(_.themeId === themeId).sortBy(_.addTime desc).drop(startRow).take(pageSize)
      u<-Users
      if c.uid === u.id
    } yield(u,c)
    //println(" q sql "+q.selectStatement)
    val themes:List[(User,ThemeDiscuss)]=  q.list()
    Page[(User,ThemeDiscuss)](themes,currentPage,totalPages);
  }

  def filterThemes(name:Option[String],cid:Option[Int],isRecommend:Option[Boolean],currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>

    var query =for(c<- Themes)yield c
    if(!name.isEmpty) query = query.filter(_.name like "%"+name.get+"%")
    if(!cid.isEmpty) query = query.filter(_.cid === cid.get)
    if(!isRecommend.isEmpty) query = query.filter(_.isRecommend === isRecommend.get)
    query = query.sortBy(_.id desc)
    //println("sql " +query.selectStatement)

    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val themes:List[Theme]=  query.drop(startRow).take(pageSize).list()
    Page[Theme](themes,currentPage,totalPages);

  }

  def filterDiscusses(name:Option[String],checkState:Option[Int],currentPage:Int,pageSize:Int) = database.withSession {  implicit session:Session =>

    var query =for(c<- ThemeDiscusses)yield c
    if(!name.isEmpty) query = query.filter(_.uname like "%"+name.get+"%")
    if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
    query = query.sortBy(_.id desc)
    //println("sql " +query.selectStatement)

    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val discusses:List[ThemeDiscuss]=  query.drop(startRow).take(pageSize).list()
    Page[ThemeDiscuss](discusses,currentPage,totalPages);

  }

  /* find themeGoodses*/
  def findThemeGoodses(themeId:Long):List[ThemeGoods] = database.withSession {  implicit session:Session =>
     (for(c<- ThemeGoodses.sortBy(_.sortNum asc) if c.themeId === themeId)yield c).list()
  }

  def modifyThemeGoods(id:Long,sortNum:Int) = database.withSession {  implicit session:Session =>
    (for(c<-ThemeGoodses if c.id === id)yield c.sortNum).update(sortNum)
  }

  }
