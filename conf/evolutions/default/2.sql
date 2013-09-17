 set character_set_client=utf8;
 set character_set_results=utf8;
 set character_set_connection=utf8;




/*插入广告位置*/
 /* 首页 */
 insert into advert_position(position,name,code,advert_type)values("index","顶部切换广告","index-flash",0);
 insert into advert(position_code,name)values("index-flash","切换广告1");
 insert into advert(position_code,name)values("index-flash","切换广告2");
 insert into advert(position_code,name)values("index-flash","切换广告3");
 insert into advert(position_code,name)values("index-flash","切换广告4");
 insert into advert(position_code,name)values("index-flash","切换广告5");

 insert into advert_position(position,name,code,advert_type)values("index","最近好玩意","index-hot",1);
 insert into advert(position_code,name)values("index-hot","最近好玩意1");
 insert into advert(position_code,name)values("index-hot","最近好玩意2");
 insert into advert(position_code,name)values("index-hot","最近好玩意3");
 insert into advert(position_code,name)values("index-hot","最近好玩意4");
 insert into advert(position_code,name)values("index-hot","最近好玩意5");
 insert into advert(position_code,name)values("index-hot","最近好玩意6");

 insert into advert_position(position,name,code,advert_type)values("index","新-美食-tags","index-meishi-left",1);
 insert into advert_position(position,name,code,advert_type)values("index","新-美食-图片","index-meishi-right",0);
 insert into advert(position_code,name)value("index-meishi-left","新-首页-美食-tags");
 insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-1");
 insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-2") ;
 insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-3");
 insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-4")  ;
 insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-5") ;


 insert into advert_position(position,name,code,advert_type)values("index","新-食材-tags","index-shicai-left",1)  ;
 insert into advert_position(position,name,code,advert_type)values("index","新-食材-图片","index-shicai-right",0) ;
 insert into advert(position_code,name)value("index-shicai-left","新-首页-食材-tags")   ;
 insert into advert(position_code,name)value("index-shicai-right","新-首页-食材-1");
 insert into advert(position_code,name)value("index-shicai-right","新-首页-食材-2");
 insert into advert(position_code,name)value("index-shicai-right","新-首页-食材-3");
 insert into advert(position_code,name)value("index-shicai-right","新-首页-食材-4");
 insert into advert(position_code,name)value("index-shicai-right","新-首页-食材-5");


 insert into advert_position(position,name,code,advert_type)values("index","新-居家-tags","index-jujia-left",1)   ;
 insert into advert_position(position,name,code,advert_type)values("index","新-居家-图片","index-jujia-right",0) ;
 insert into advert(position_code,name)value("index-jujia-left","新-首页-居家-tags") ;
 insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-1")  ;
 insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-2") ;
 insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-3") ;
 insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-4")  ;
 insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-5")  ;


 insert into advert_position(position,name,code,advert_type)values("index","小镇推荐","index-site-recom",6)   ;
 insert into advert_position(position,name,code,advert_type)values("index","好贴推荐","index-post-recom",7);
 insert into advert(position_code,name)value("index-site-recom","新-首页-小镇1") ;
 insert into advert(position_code,name)value("index-site-recom","新-首页-小镇2") ;
 insert into advert(position_code,name)value("index-site-recom","新-首页-小镇3") ;
 insert into advert(position_code,name)value("index-site-recom","新-首页-小镇4") ;
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子1");
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子2");
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子3");
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子4");
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子5");
 insert into advert(position_code,name)values("index-post-recom","新-首页-帖子6");


 insert into advert_position(position,name,code,advert_type)values("index","新-底部-图片","index-bottom",0) ;
 insert into advert(position_code,name)value("index-bottom","新-首页-底部-广告");
 insert into advert(position_code,name)value("index-bottom","新-首页-底部-广告2");

 insert into advert_position(position,name,code,advert_type)values("index","美食品牌","index-pinpai-meishi",0);
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌1");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌2");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌3");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌4");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌5");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌6");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌7");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌8");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌9");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌10");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌11");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌12");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌13");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌14");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌15");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌16");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌17");
 insert into advert(position_code,name)values("index-pinpai-meishi","美食品牌18");

 insert into advert_position(position,name,code,advert_type)values("index","美食品牌","index-pinpai-jujia",0);
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌1");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌2");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌3");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌4");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌5");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌6");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌7");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌8");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌9");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌10");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌11");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌12");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌13");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌14");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌15");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌16");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌17");
 insert into advert(position_code,name)values("index-pinpai-jujia","居家品牌18");







/* 3月23日 */
/* 天天特价*/
  insert into advert_position(position,name,code,advert_type)values("tejia","天天特价","tejia",2);
 insert into advert(position_code,name)value("tejia","天天特价1") ;
 insert into advert(position_code,name)value("tejia","天天特价2") ;
 insert into advert(position_code,name)value("tejia","天天特价3") ;
 insert into advert(position_code,name)value("tejia","天天特价4") ;
 insert into advert(position_code,name)value("tejia","天天特价5") ;
 insert into advert(position_code,name)value("tejia","天天特价6") ;
 insert into advert(position_code,name)value("tejia","天天特价7") ;
 insert into advert(position_code,name)value("tejia","天天特价8") ;
 insert into advert(position_code,name)value("tejia","天天特价9") ;
 insert into advert(position_code,name)value("tejia","天天特价10") ;




/* 签到 广告 */
 insert into advert_position(position,name,code,advert_type)values("checkIn","签到推荐","checkIn",2);
 insert into advert(position_code,name,third_id)value("checkIn","签到推荐1",2) ;
 insert into advert(position_code,name)value("checkIn","签到推荐2");
 insert into advert(position_code,name)value("checkIn","签到推荐3");
 insert into advert(position_code,name)value("checkIn","签到推荐4");
 insert into advert(position_code,name)value("checkIn","签到推荐5");
 insert into advert(position_code,name)value("checkIn","签到推荐6");


/* forum 广告*/
 insert into advert_position(position,name,code,advert_type)values("forum","精彩活动","forum_activity",0) ;
 insert into advert(position_code,name)value("forum_activity","精彩活动") ;
 insert into advert_position(position,name,code,advert_type)values("forum","热门问答","hot_question",5);
 insert into advert(position_code,name)value("hot_question","热门问答1") ;
 insert into advert(position_code,name)value("hot_question","热门问答2") ;
 insert into advert(position_code,name)value("hot_question","热门问答3") ;
 insert into advert(position_code,name)value("hot_question","热门问答4") ;

   insert into advert_position(position,name,code,advert_type)values("forum","精华知识","hot_knowledge",5);
 insert into advert(position_code,name)value("hot_knowledge","精华知识1") ;
 insert into advert(position_code,name)value("hot_knowledge","精华知识2") ;
 insert into advert(position_code,name)value("hot_knowledge","精华知识3") ;
 insert into advert(position_code,name)value("hot_knowledge","精华知识4") ;


/* # themes 广告 */

 insert into advert_position(position,name,code,advert_type)values("themes","顶部切换广告","themes-flash",0);
 insert into advert(position_code,name)value("themes-flash","图片广告1") ;
 insert into advert(position_code,name)value("themes-flash","图片广告2") ;
 insert into advert(position_code,name)value("themes-flash","图片广告3") ;
 insert into advert(position_code,name)value("themes-flash","图片广告4") ;
 insert into advert(position_code,name)value("themes-flash","图片广告5") ;

  insert into advert_position(position,name,code,advert_type)values("themes","美食主题","themes-meishi",4);
 insert into advert(position_code,name)value("themes-meishi","美食主题1") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题2") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题3") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题4") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题5") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题6") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题7") ;
 insert into advert(position_code,name)value("themes-meishi","美食主题8") ;

 insert into advert_position(position,name,code,advert_type)values("themes","食材主题","themes-shicai",4);
 insert into advert(position_code,name)value("themes-shicai","食材主题1");
 insert into advert(position_code,name)value("themes-shicai","食材主题2");
 insert into advert(position_code,name)value("themes-shicai","食材主题3");
 insert into advert(position_code,name)value("themes-shicai","食材主题4");
 insert into advert(position_code,name)value("themes-shicai","食材主题5");
 insert into advert(position_code,name)value("themes-shicai","食材主题6");
 insert into advert(position_code,name)value("themes-shicai","食材主题7");
 insert into advert(position_code,name)value("themes-shicai","食材主题8");



 insert into advert_position(position,name,code,advert_type)values("themes","居家主题","themes-jujia",4);
 insert into advert(position_code,name)value("themes-jujia","居家主题1");
 insert into advert(position_code,name)value("themes-jujia","居家主题2");
 insert into advert(position_code,name)value("themes-jujia","居家主题3");
 insert into advert(position_code,name)value("themes-jujia","居家主题4");
 insert into advert(position_code,name)value("themes-jujia","居家主题5");
 insert into advert(position_code,name)value("themes-jujia","居家主题6");
 insert into advert(position_code,name)value("themes-jujia","居家主题7");
 insert into advert(position_code,name)value("themes-jujia","居家主题8");


/*  #私房菜 广告 */
 insert into advert_position(position,name,code,advert_type)values("sifangcai","顶部切换广告","sifangcai-flash",0);
 insert into advert(position_code,name)value("sifangcai-flash","图片广告1") ;
 insert into advert(position_code,name)value("sifangcai-flash","图片广告2") ;
 insert into advert(position_code,name)value("sifangcai-flash","图片广告3") ;
 insert into advert(position_code,name)value("sifangcai-flash","图片广告4") ;
 insert into advert(position_code,name)value("sifangcai-flash","图片广告5") ;

 insert into advert_position(position,name,code,advert_type)values("sifangcai","应季食材","sifangcai-shicai",7);
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材1") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材2") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材3") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材4") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材5") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材6") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材7") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材8") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材9") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材10") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材11") ;
 insert into advert(position_code,name)value("sifangcai-shicai","应季食材12") ;


 insert into advert_position(position,name,code,advert_type)values("sifangcai","应季私房菜菜谱","sifangcai-menu",7);
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐1");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐2");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐3");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐4");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐5");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐6");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐7");
 insert into advert(position_code,name)value("sifangcai-menu","私房菜菜谱推荐8");

 /* 小镇 广告 */
 insert into advert_position(position,name,code,advert_type)values("sites","顶部切换广告","sites-flash",0);
 insert into advert(position_code,name)value("sites-flash","图片广告1") ;
 insert into advert(position_code,name)value("sites-flash","图片广告2") ;
 insert into advert(position_code,name)value("sites-flash","图片广告3") ;
 insert into advert(position_code,name)value("sites-flash","图片广告4") ;
 insert into advert(position_code,name)value("sites-flash","图片广告5") ;

 insert into advert_position(position,name,code,advert_type)values("sites","小镇推荐","sites-recom",6);
 insert into advert(position_code,name)value("sites-recom","小镇推荐1") ;
 insert into advert(position_code,name)value("sites-recom","小镇推荐2") ;
 insert into advert(position_code,name)value("sites-recom","小镇推荐3") ;
 insert into advert(position_code,name)value("sites-recom","小镇推荐4") ;
 insert into advert(position_code,name)value("sites-recom","小镇推荐5") ;
 insert into advert(position_code,name)value("sites-recom","小镇推荐6") ;



 insert into advert_position(position,name,code,advert_type)values("sites","帖子推荐","sites-post-recom",7);
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐1");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐2");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐3");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐4");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐5");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐6");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐7");
 insert into advert(position_code,name)value("sites-post-recom","帖子推荐8");



 /* 20130704 内置系统站内信 */
 insert into system_msg(id,title,content)value(1,"小镇审核","小镇审核内容todo");
 insert into system_msg(id,title,content)value(2,"未定2","未定2todo");
 insert into system_msg(id,title,content)value(3,"未定3","未定3todo");
 insert into system_msg(id,title,content)value(4,"未定4","未定4todo");
 insert into system_msg(id,title,content)value(5,"未定5","未定5todo");
 insert into system_msg(id,title,content)value(6,"未定6","未定6todo");
 insert into system_msg(id,title,content)value(7,"未定7","未定7todo");
 insert into system_msg(id,title,content)value(8,"未定8","未定8todo");
 insert into system_msg(id,title,content)value(9,"未定9","未定9todo");


 /* 20130816  友情链接*/
 insert into advert_position(position,name,code,advert_type)values("friends","合作伙伴","friends-partners",1)   ;
 insert into advert_position(position,name,code,advert_type)values("friends","友情链接","friends-links",1) ;
 insert into advert(position_code,name)value("friends-partners","合作伙伴") ;
 insert into advert(position_code,name)value("friends-links","友情链接") ;


 /*20130916 首页 */
 insert into advert_position(position,name,code,advert_type)values("index","新-首页-美食-帖子","index-meishi-posts",1)   ;
 insert into advert(position_code,name)value("index-meishi-posts","新-首页-美食-posts") ;

  insert into advert_position(position,name,code,advert_type)values("index","新-首页-食材-帖子","index-shicai-posts",1)   ;
 insert into advert(position_code,name)value("index-shicai-posts","新-首页-食材-posts") ;

 insert into advert_position(position,name,code,advert_type)values("index","新-首页-居家-帖子","index-jujia-posts",1)   ;
 insert into advert(position_code,name)value("index-jujia-posts","新-首页-居家-posts") ;

 insert into advert_position(position,name,code,advert_type)values("index","新-首页-小镇-帖子","index-site-posts",1)   ;
 insert into advert(position_code,name)value("index-site-posts","新-首页-小镇-posts") ;

 insert into advert_position(position,name,code,advert_type)values("index","新-首页-小镇","index-site",1)   ;
 insert into advert(position_code,name)value("index-site","新-首页-小镇") ;




