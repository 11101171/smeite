package models.user.dao

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
import models.theme.{Theme, Themes, ThemeGoodses}
import models.forum.{Topic,Topics}
import models.theme.dao.ThemeSQLDao
import models.goods.dao.GoodsDao
import models.goods.dao.GoodsSQLDao
import models.theme.dao.ThemeDao
import models.forum.dao.TopicDao
import utils.ShiDouConfig


/*
*
* Dao 访问数据，可能来自数据库 也可能来自缓存
* */

object UserDao {

  /*从connection pool 中 获取jdbc的connection*/
  implicit val database = Database.forDataSource(DB.getDataSource())
  /* 验证 */
  def authenticate(email: String, passwd: String): Option[User] = database.withSession{  implicit session:Session =>
    val user = Users.authenticate(email,passwd)
    if(!user.isEmpty){
        Cache.set("user_"+user.get.id.get,user.get)
    }
    user

  }
  /*find By id*/
  def findById(uid:Long):User = database.withSession{  implicit session:Session =>
    Cache.getOrElse[User]("user_"+uid) {
      //println("get it from db")
      val user = Users.findById(uid)
      if(!user.isEmpty){
        Cache.set("user_"+uid,user.get)
      }
      user.get
    }
  }
  def findStatic(uid:Long):UserStatic = database.withSession{  implicit session:Session =>
    Cache.getOrElse[UserStatic]("user_static"+uid) {
      //println("get it from db")
      val userStatic = UserStatics.findByUid(uid)
      if(!userStatic.isEmpty){
        Cache.set("user_static"+uid,userStatic.get)
      }
      userStatic.get
    }
  }
  /* count user */
  def countUser:Int = database.withSession{  implicit session:Session =>
       Query(Users.length).first
  }
  /*find by email */
  def findByEmail(email: String):Option[User] = database.withSession{ implicit session:Session =>
    val user =Users.findByEmail(email)
    if(!user.isEmpty){
      Cache.set("user_"+user.get.id.get,user.get)
    }
    user
  }
  /* 检查第三方用户是否存在 */
  /*查找sns 帐号的用户*/
  def checkSnsUser(comeFrom:Int,openId:String):Option[User]=database.withSession{ implicit  session:Session =>
    val user = Users.findSnsUser(comeFrom,openId)
    if(!user.isEmpty){
      Cache.set("user_"+user.get.id.get,user)
    }
    user
  }
 /* 第三方用户初次登陆 */
  def addSnsUser(name:String,comeFrom:Int,openId:String,pic:String) =database.withSession{ implicit  session:Session =>
    val id = Users.autoInc3.insert(name,comeFrom,openId,pic)
    /* 添加积分 */
 //   UserSQLDao.updateCredits(id,ShiDouConfig.regCredits)
    UserStatics.autoInc.insert(id)
    UserProfiles.autoInc.insert(id,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),"sns")
  }
  /*用户通过网站注册 * */
  def addSmeiteUser(name:String, passwd:String, email:String,ip:String)=database.withSession{ implicit  session:Session =>
    val id = Users.autoInc2.insert(name,Codecs.sha1("smeite"+passwd),email)
    /* 添加积分 */
  //  UserSQLDao.updateCredits(id,ShiDouConfig.regCredits)
    UserStatics.autoInc.insert(id)
    UserProfiles.autoInc.insert(id,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),ip)
  }
  /*修改密码*/
  def modifyPasswd(uid:Long, passwd:String) =database.withSession{ implicit  session:Session =>
    Cache.remove("user_"+uid)
    (for(c<-Users if c.id === uid)yield c.passwd).update(Codecs.sha1("smeite"+passwd))
  }
  /*保存地址*/
  def modifyAddr(uid:Long, receiver:String,province:String, city:String, street:String, postCode:String, phone:String,alipay:String)= database.withSession{  implicit session:Session =>
    Cache.remove("user_"+uid)
    (for(c<-UserProfiles if c.uid === uid) yield c.receiver~c.province~c.city~c.street~c.postCode~c.phone~c.alipay).update((receiver,province,city,street,postCode,phone,alipay))
  }
  /*修改user pic*/
  def modifyPic(uid:Long,pic:String)= database.withSession{  implicit session:Session =>
    Cache.remove("user_"+uid)
    (for(c<-Users if c.id===uid)yield c.pic).update(pic)
  }
  /*修改 user email*/
  def modifyEmail(uid:Long,email:String)= database.withSession{  implicit session:Session =>
    Cache.remove("user_"+uid)
    (for(c<-Users if c.id===uid)yield c.email ).update(email)
  }
  /*保存基本信息*/
  def modifyBase(uid:Long,name:String,intro:String, gender:Int, birth:String, province:String, city:String, blog:String, weixin:String)   = database.withSession{  implicit session:Session =>
    Cache.remove("user_"+uid)
    (for(c <-Users if c.id === uid)yield c.name ).update(name)
    (for(c<-UserProfiles if c.uid === uid)yield c.intro ~ c.birth ~ c.province ~ c.city ~ c.blog ~ c.weixin).update((intro,birth,province,city,blog,weixin))
  }
  /* 修改用户状态 */
  def modifyStatus(uid:Long,status:Int)= database.withSession {  implicit session:Session =>
    (for(u<-Users if u.id === uid) yield u.status ).update(status)
  }


  /*  list  user */
  def findAll(currentPage: Int, pageSize: Int ): Page[User] = database.withSession {  implicit session:Session =>
    val totalRows=Users.count()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var q=  for(c<-Users.sortBy(_.id desc).drop(startRow).take(pageSize)) yield(c)

    //println(" q sql "+q.selectStatement)
    val users:List[User]=  q.list()
    Page[User](users,currentPage,totalPages);
  }
  /*
   * 1 推荐一位用户可能感兴趣的人
   * 2 目前简单处理，只推荐 hotIndex比较高的 且 不是 用户的关注的
   * */
  def  findInterestedUser(uid:Long,nums:Int):List[User] = database.withSession {  implicit session:Session =>
    val query =  for(c<-Users  if c.id notIn (for (f<-UserFollows if f.fansId === uid)yield f.uid ))yield c
    //println("query sql : "+query.selectStatement)
    query.sortBy(_.credits).take(nums).list()
  }
  /*
 * 1 推荐一位用户可能感兴趣的人
 * 2 目前简单处理，只推荐 hotIndex比较高的 且 不是 用户的关注的
 * */
  def changeFriends(uid:Long, ids:List[Long], nums:Int =5):List[User] = database.withSession {  implicit session:Session =>
    val query =  for(c<-Users  if c.id  notIn (for (f<-UserFollows if f.fansId === uid)yield f.uid ))yield c
    //println("query sql : "+query.selectStatement)
    query.sortBy(_.credits desc).take(nums).list
  }
  /* 推荐 用户*/
  def recommendUser(credits:Int,nums:Int):List[User] = database.withSession {  implicit session:Session =>
    val query =  for(c<-Users if c.credits > credits )yield c
    //println("query sql : "+query.selectStatement)
    query.sortBy(_.credits desc).take(nums).list()
  }

  /* find by id with profile */
  def findWithProfile(uid:Long) = database.withSession{  implicit session:Session =>
    (for{
      u<-Users
      p<-UserProfiles
      if u.id === p.uid
      if u.id === uid }yield(u,p)).first
  }
  /* find profile*/
  def findProfile(uid:Long):UserProfile=database.withSession{  implicit session:Session =>
    {for( c <-UserProfiles  if c.uid === uid )yield(c)}.first
  }



 /* user follow */
  def addFollow(followId:Long,fansId:Long)=database.withSession {  implicit session:Session =>
   UserSQLDao.updateFansNum(followId,1)
   UserSQLDao.updateFollowNum(fansId,1)
   val follower =findById(followId)
   /*保存用户动作*/
  addTrend(UserTrend(None,fansId,"关注了",follower.id.get,"/user/"+follower.id.get,follower.name,None))
  UserFollows.insert(followId,fansId)

  }

  def checkFollow(uid:Long,fansId:Long):Option[UserFollow] =database.withSession {  implicit session:Session =>
   UserFollows.find(uid,fansId)
  }

  def deleteFollow(followId:Long,uid:Long) =database.withSession {  implicit session:Session =>
    UserSQLDao.updateFollowNum(uid,-1)
    UserSQLDao.updateFansNum(followId,-1)
	 val follower =findById(followId)
   /*保存用户动作*/
    addTrend(UserTrend(None,uid,"取消关注",follower.id.get,"/user/"+follower.id.get,follower.name,None))
     UserFollows.delete(followId,uid)

  }
  def deleteFans(uid:Long,fansId:Long) =database.withSession {  implicit session:Session =>
    UserSQLDao.updateFansNum(uid,-1)
    UserFollows.delete(uid,fansId)

  }
  /* 查找fans*/
  def findFans(uid:Long, currentPage: Int, pageSize: Int): Page[User] = database.withSession {  implicit session:Session =>
    val totalRows=Query(UserFollows.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      uf<-UserFollows.filter(_.uid === uid).sortBy(_.addTime desc).drop(startRow).take(pageSize)
      c<-Users
      if uf.fansId===c.id
    } yield(c)
    //println(" q sql "+q.selectStatement)
    val users:List[User]=q.list()
    Page[User](users,currentPage,totalPages);
  }

  /*查找关注的人*/
  def findFollows(uid:Long, currentPage: Int, pageSize: Int): Page[User] = database.withSession {  implicit session:Session =>
    val totalRows=Query(UserFollows.filter(_.fansId === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      uf<-UserFollows.filter(_.fansId === uid).drop(startRow).take(pageSize)
      c<-Users
      if uf.uid===c.id
    } yield(c)
    //println(" q sql "+q.selectStatement)
    val users:List[User]=  q.list()
    Page[User](users,currentPage,totalPages);
  }

  /* user love goods*/

  def checkLoveGoods(uid:Long,goodsId:Long):Option[UserLoveGoods]=database.withSession{  implicit session:Session =>
      UserLoveGoodses.find(uid,goodsId)
  }

  def addLoveGoods(uid:Long,goodsId:Long) =database.withSession{  implicit session:Session =>
   GoodsSQLDao.updateLoveNum(goodsId,1)
    UserSQLDao.updateLoveBaobeiNum(uid,1)
	 val goods =GoodsDao.findById(goodsId)
   /*保存用户动作*/
    addTrend(UserTrend(None,uid,"喜欢了宝贝",goods.get.id.get,"/goods/"+goods.get.id.get,goods.get.name,None))
	
    UserLoveGoodses.insert(uid,goodsId)

  }
  def deleteLoveGoods(uid:Long,goodsId:Long) =database.withSession{  implicit session:Session =>
    UserSQLDao.updateLoveBaobeiNum(uid,-1)
    val goods =GoodsDao.findById(goodsId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"取消了喜欢的宝贝",goods.get.id.get,"/goods/"+goods.get.id.get,goods.get.name,None))

    UserLoveGoodses.delete(uid,goodsId)

  }

  /*  获取用户喜欢的商品*/
  def findLoveGoodses(uid:Long,currentPage: Int = 1, pageSize: Int = 10 ):Page[Goods] =database.withSession{  implicit session:Session =>
    val totalRows=Query(UserLoveGoodses.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q = for{
      c<- UserLoveGoodses.filter(_.uid === uid).drop(startRow).take(pageSize)
      g<- Goodses
      if c.goodsId === g.id
    }yield(g)
    val list:List[Goods] =q.list()
    Page[Goods](list,currentPage,totalPages)
  }

  /*  user love theme */
  /*根据userid 和 theme id */
  def checkLoveTheme(uid:Long,themeId:Long):Option[UserLoveTheme] =database.withSession {  implicit session:Session =>
   UserLoveThemes.find(uid,themeId)
  }

  def addLoveTheme(uid:Long, themeId:Long)  =database.withSession {  implicit session:Session =>
    UserSQLDao.updateLoveThemeNum(uid,1)
    ThemeSQLDao.updateLoveNum(themeId,1)
    val theme =ThemeDao.findById(themeId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"喜欢了主题",theme.get.id.get,"/theme/"+theme.get.id.get,theme.get.name,None))

    UserLoveThemes.insert(uid,themeId)

  }
  def deleteLoveTheme(uid:Long,themeId:Long)=database.withSession {  implicit session:Session =>
    UserSQLDao.updateLoveThemeNum(uid,-1)
    ThemeSQLDao.updateLoveNum(themeId,-1)
    val theme =ThemeDao.findById(themeId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"取消了喜欢的主题",theme.get.id.get,"/theme/"+theme.get.id.get,theme.get.name,None))
   UserLoveThemes.delete(uid,themeId)

  }
  def findThemeUsers(themeId:Long,currentPage:Int,pageSize:Int =50):Page[User] =database.withSession {  implicit session:Session =>
    val totalRows=Query(UserLoveThemes.filter(_.themeId === themeId).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-UserLoveThemes.filter(_.themeId === themeId).sortBy(_.addTime).drop(startRow).take(pageSize)
      u<-Users
      if c.uid===u.id
    } yield(u)
    Page[User](q.list(),currentPage,totalPages)
  }
  /*  查找用户喜欢的主题 */
  def findLoveThemes(uid:Long,currentPage: Int = 1, pageSize: Int = 10 ):Page[((Long,String,String,Int),List[Option[String]])] =database.withSession{  implicit session:Session =>
    val totalRows=Query(UserLoveThemes.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val query = for{
       ((ut,t),tg) <- UserLoveThemes.filter(_.uid === uid).drop(startRow).take(pageSize).sortBy(_.addTime desc) leftJoin Themes on (_.themeId === _.id) leftJoin ThemeGoodses on (_._2.id  === _.themeId)

   //   (t,tg)  <- Themes leftJoin  ThemeGoodses.filter(_.themeId in ( for( ut<-UserLoveThemes.filter(_.uid === uid).sortBy(_.addTime desc).drop(startRow).take(pageSize) )yield ut.themeId))    on(_.id === _.themeId)
     }yield(t.id,t.name,t.intro,t.loveNum,tg.goodsPic.?)
    //println(" love themes " +query.selectStatement )
    val themes:List[((Long,String,String,Int),List[Option[String]])] =query.list().groupBy(x=>(x._1,x._2,x._3,x._4)).map(x=>(x._1,x._2.take(5).map(y=>y._5))).toList
    Page[((Long,String,String,Int),List[Option[String]])](themes,currentPage,totalPages)
  }
  /*显示用户创建的theme*/
  def  findPostThemes(uid:Long,currentPage: Int = 1, pageSize: Int = 10): Page[((Long, String, String,Int),List[Option[String]])] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Themes.filter(_.uid===uid).length).first
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val query = for {
      (t,tg) <- Themes.filter(_.uid===uid).sortBy(_.addTime desc).drop(startRow).take(pageSize) leftJoin ThemeGoodses  on (_.id === _.themeId)
    }yield(t.id,t.name,t.intro,t.loveNum,tg.goodsPic.?)

    val themes:List[((Long, String,String,Int),List[Option[String]])] =query.list.groupBy(x=>(x._1,x._2,x._3,x._4)).map(x=>(x._1,x._2.take(5).map(y=>y._5))).toList.drop(startRow).take(pageSize)
    Page[((Long, String,String,Int),List[Option[String]])](themes,currentPage,totalPages)
  }

  def findSimplePostThemes(uid:Long):List[Theme] = database.withSession {  implicit session:Session =>
    (for(c<-Themes if c.uid === uid)yield (c)).list()
  }

  /* user love topic */
  def checkLoveTopic(uid:Long, topicId:Long):Option[UserLoveTopic] =database.withSession {  implicit session:Session =>
    UserLoveTopics.find(uid,topicId)
  }
  def addLoveTopic(uid:Long,topicId:Long) =database.withSession {  implicit session:Session =>
    UserSQLDao.updateLoveTopicNum(uid,1)
    val topic =TopicDao.findById(topicId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"喜欢了话题",topic.get.id.get,"/forum/view/"+topic.get.id.get,topic.get.title,None))
    UserLoveTopics.autoInc.insert(uid,topicId)

  }
  /*删除*/
  def deleteLoveTopic(uid:Long,topicId:Long)=database.withSession {  implicit session:Session =>
    UserSQLDao.updateLoveTopicNum(uid,-1)
    val topic =TopicDao.findById(topicId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"取消了喜欢的话题",topic.get.id.get,"/forum/view/"+topic.get.id.get,topic.get.title,None))
    UserLoveTopics.delete(uid,topicId)

  }
  /* 查找用户喜欢的主题 */
  def findLoveTopics(uid:Long,currentPage: Int = 1, pageSize: Int = 10 ):Page[Topic] =database.withSession{  implicit session:Session =>
    val totalRows=Query(UserLoveTopics.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val query = for{
        ult <- UserLoveTopics.filter(_.uid === uid).sortBy(_.addTime).drop(startRow).take(pageSize)
        t <- Topics
      if ult.topicId === t.id
    }yield(t)
    //println("query " +query.selectStatement)
    val topics:List[Topic]= query.list()
    Page[Topic](topics,currentPage,totalPages)
  }

  /*  user share goods */

  def checkShareGoods(uid:Long,goodsId:Long):Option[UserShareGoods]=database.withSession{  implicit session:Session =>
    UserShareGoodses.find(uid,goodsId)
  }

  def addShareGoods(uid:Long,goodsId:Long) =database.withSession{  implicit session:Session =>
    UserSQLDao.updatePostBaobeiNum(uid,1)
    val goods =GoodsDao.findById(goodsId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"发布了宝贝",goods.get.id.get,"/goods/"+goods.get.id.get,goods.get.name,None))
    /* 用户分享一个商品 获得一个食豆 */
    UserSQLDao.updateShiDou(uid,ShiDouConfig.postBaobeiShiDou)
    UserShareGoodses.autoInc.insert(uid,goodsId)

  }
  def deleteShareGoods(uid:Long,goodsId:Long) =database.withSession{  implicit session:Session =>
    UserSQLDao.updatePostBaobeiNum(uid,-1)
    val goods =GoodsDao.findById(goodsId)
    /*保存用户动作*/
    addTrend(UserTrend(None,uid,"取消了发布的宝贝",goods.get.id.get,"/goods/"+goods.get.id.get,goods.get.name,None))
    UserShareGoodses.delete(uid,goodsId)

  }

  /*  获取用户喜欢的商品*/
  def findShareGoodses(uid:Long,currentPage: Int = 1, pageSize: Int = 10 ):Page[Goods] =database.withSession{  implicit session:Session =>
    val totalRows=Query(UserShareGoodses.filter(_.uid === uid).length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q = for{
      c<- UserShareGoodses.filter(_.uid === uid).sortBy(_.id desc).drop(startRow).take(pageSize)
      g<- Goodses
      if c.goodsId === g.id
    }yield(g)
    val list:List[Goods] =q.list()
    Page[Goods](list,currentPage,totalPages)
  }
  /* 查找第一个分享的用户*/
  def findFirstShareUser(goodsId:Long):User =database.withSession {  implicit session:Session =>
  (for{
    c<- UserShareGoodses.filter(_.goodsId === goodsId).sortBy(_.id asc)
    u<- Users
    if c.uid === u.id
  }yield(u)).first()

  }

  /* user trend */
  def addTrend(userTrend:UserTrend)=database.withSession {  implicit session:Session =>
    UserSQLDao.updateTrendNum(userTrend.uid,1)
    UserTrends.autoInc.insert(userTrend)
  }

  def findTrend(id:Long ):Option[UserTrend] = database.withSession {  implicit session:Session =>
      UserTrends.findById(id)
    }
  def findUserTrends(uid:Long):List[UserTrend] = database.withSession{ implicit  session:Session =>
    UserTrends.list(uid)
  }
  def findUserTrends(uid:Long,start:Int,num:Int):List[UserTrend] = database.withSession {  implicit session:Session =>
    UserTrends.list(uid,start,num)
  }



   /*用户筛选*/
  def filterUsers(name:Option[String],status:Option[Int],daren:Option[Int],comeFrom:Option[Int],creditsOrder:String,shiDouOrder:String,idOrder:String,currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
    var query =for(u<-Users)yield u
    if(!name.isEmpty) query = query.filter(_.name like "%"+name.get+"%")
     if(!status.isEmpty) query =query.filter(_.status === status.get)
     if(!comeFrom.isEmpty) query =query.filter(_.comeFrom === comeFrom.get)
    if(idOrder == "desc") query =query.sortBy(_.id desc ) else query =query.sortBy(_.id asc )
    if(creditsOrder=="desc") query =query.sortBy(_.credits desc ) else query =query.sortBy(_.credits asc )
    if(shiDouOrder=="desc") query =query.sortBy(_.shiDou desc ) else query =query.sortBy(_.shiDou asc )
    //println("sql " +query.selectStatement)
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val groups:List[User]=  query.drop(startRow).take(pageSize).list()
    Page[User](groups,currentPage,totalPages);
   }



  /*user order*/
  def addUserOrder(uid:Long,goodsId:Long,numIid:Long, nick:String, title:String,location:String,pic:String,price:String,withdrawRate:Int,credits:Int,volume:String)=  database.withSession {  implicit session:Session =>
    UserOrders.autoInc2.insert(uid,Some(goodsId),numIid,nick,title,location,pic,price,withdrawRate,credits,volume,new Timestamp(System.currentTimeMillis()))
  }

  def findUserOrders(uid:Long,currentPage:Int,pageSize:Int,status:Int): Page[(String,List[(Timestamp,Long,Long,String,String,String,String,String,Int,Int,Int,String)])] =  database.withSession {  implicit session:Session =>
  // val totalRows=Query(UserOrders.filter(_.uid === uid).length)
    var countQuery =(for(c<-UserOrders if c.uid === uid) yield c)
     if(status != -1) countQuery.filter(_.status === status)
     val totalRows= Query(countQuery.length).first()
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    var query=for(c<- UserOrders if c.uid===uid)yield (c.createTime,c.uid,c.numIid,c.nick,c.title,c.location,c.pic,c.price,c.withdrawRate,c.credits,c.status,c.volume)
    if(status != -1) query.filter( _._11 === status )
   val list:List[(String,List[(Timestamp,Long,Long,String,String,String,String,String,Int,Int,Int,String)])]= query.drop(startRow).take(pageSize).list().map(x=>( utils.Utils.timestampFormat2(x._1),x._1,x._2,x._3,x._4,x._5,x._6,x._7,x._8,x._9,x._10,x._11,x._12)).groupBy(x=>x._1).map(x=>(x._1,x._2.map(y=>(y._2,y._3,y._4,y._5,y._6,y._7,y._8,y._9,y._10,y._11,y._12,y._13)))).toList

    Page[(String,List[(Timestamp,Long,Long,String,String,String,String,String,Int,Int,Int,String)])](list,currentPage,totalPages)
  }

  def findUserOrders(num:Int):List[UserOrder]  =  database.withSession {  implicit session:Session =>
    (for( c<-UserOrders.sortBy(_.createTime desc)) yield c).take(num).list()
  }
}
