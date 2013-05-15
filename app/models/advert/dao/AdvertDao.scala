package models.advert.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.goods.{Goodses, Goods}
import models.user.{Users, User}
import models.theme.{Themes, Theme}
import models.advert._
import models.forum.{Topic, Topics}

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-13下午12:15
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object AdvertDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def modifyAdvert(id:Long,title:String,pic:String, spic:String, link:String, note:String)= database.withSession {  implicit session:Session =>
    (for(c<-Adverts if c.id === id)yield(c.title~c.pic~c.spic~c.link~c.note)).update((title,pic,spic,link,note))
  }

  def modifyAdvert(id:Long,thirdId:Long)= database.withSession {  implicit session:Session =>

    (for(c<-Adverts if c.id === id)yield(c.thirdId)).update(thirdId)
  }

  def modifyAdvert(id:Long,title:String,content:String,link:String, note:String)= database.withSession {  implicit session:Session =>
    (for(c<-Adverts if c.id === id)yield(c.title~c.content~c.link~c.note)).update((title,content,link,note))
  }

  def findById(id:Long):Advert= database.withSession{   implicit session:Session =>
    {for(c<-Adverts if c.id === id)yield(c)}.first
  }


  def findAdverts(positionCode:String): List[Advert] = database.withSession {  implicit session:Session =>
    ( for (c<-Adverts.sortBy(_.id asc) if c.positionCode === positionCode)yield c).list()
  }
  def findAdvert(positionCode:String):Advert = database.withSession {  implicit session:Session =>
    ( for(c<-Adverts if c.positionCode === positionCode)yield c).first
  }



  /* 获取需要推广的商品*/
  def  getGoods(positionCode:String):List[Goods] = database.withSession {  implicit session:Session =>
    (for{
      c<-Adverts;
      p<-Goodses;
      if c.positionCode === positionCode;
      if c.thirdId === p.id
    }yield(p) ).list()
  }

  /* 获取需要推荐的用户*/
  def  getUsers(positionCode:String):List[User] = database.withSession {  implicit session:Session =>
    (for{
      c<-Adverts;
      u<-Users;
      if c.positionCode === positionCode;
      if c.thirdId === u.id
    }yield(u) ).list()
  }
  /* 获取主题*/
  def getThemes(positionCode:String):List[Theme] = database.withSession {  implicit session:Session =>
    (for{
      c<-Adverts;
      t<-Themes;
      if c.positionCode === positionCode;
      if c.thirdId === t.id
    }yield(t) ).list()
  }

  /*
  *
  *   u<-Users
      if c.uid===u.id
      if c.typeId===typeId
      if c.groupId===groupId
    }yield(u.id~u.name~u.pic~c.id~c.title~c.replyNum~c.loveNum~c.hotIndex)
  *  */
  def getTopics(positionCode:String):List[(Long, String, String, Long, String, Int, Int, Int)] = database.withSession {  implicit session:Session =>
    (for{
      c<-Adverts
      t<-Topics
      u<-Users
      if c.positionCode === positionCode;
      if c.thirdId === t.id
      if t.uid === u.id
    }yield(u.id~u.name~u.pic~t.id~t.title~t.replyNum~t.loveNum~t.hotIndex) ).list()
  }

  def findPositions(position:String):List[AdvertPosition]= database.withSession {  implicit session:Session =>
    (for (c<-AdvertPositions if c.position===position)yield c).list()
  }
}
