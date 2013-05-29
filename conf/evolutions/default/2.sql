 set character_set_client=utf8;
 set character_set_results=utf8;
 set character_set_connection=utf8;




/*插入广告位置*/
 /*首页管理广告位置*/
 insert into advert_position(position,name,code,advert_type)values("index","切换广告","index_flash",0);
 insert into advert_position(position,name,code,advert_type)values("index","最近热门的好玩意儿","index_hot",0);
 insert into advert_position(position,name,code,advert_type)values("index","达人喜欢","index_daren",2);
 insert into advert_position(position,name,code,advert_type)values("index","食美特会员专享","index_member",0);
 insert into advert_position(position,name,code,advert_type)values("index","健康美食left","index_meishi_left",0);
 insert into advert_position(position,name,code,advert_type)values("index","健康美食right","index_meishi_right",1);
 insert into advert_position(position,name,code,advert_type)values("index","特产乡情left","index_techan_left",0);
 insert into advert_position(position,name,code,advert_type)values("index","特产乡情right","index_techan_right",1);
 insert into advert_position(position,name,code,advert_type)values("index","滋补保健left","index_zibu_left",0);
 insert into advert_position(position,name,code,advert_type)values("index","滋补保健right","index_zibu_right",1);
 insert into advert_position(position,name,code,advert_type)values("index","居家生活left","index_jujia_left",0);
 insert into advert_position(position,name,code,advert_type)values("index","居家生活right","index_jujia_right",1);
 insert into advert_position(position,name,code,advert_type)values("index","好玩意left","index_haowanyi_left",0);
 insert into advert_position(position,name,code,advert_type)values("index","好玩意right","index_haowanyi_right",1);
 insert into advert_position(position,name,code,advert_type)values("index","美食品牌","index_pinpai_meishi",0);
 insert into advert_position(position,name,code,advert_type)values("index","创意品牌","index_pinpai_tese",0);





 #index advert
 insert into advert(position_code,name)values("index_flash","切换广告1");
 insert into advert(position_code,name)values("index_flash","切换广告2");
 insert into advert(position_code,name)values("index_flash","切换广告3");
 insert into advert(position_code,name)values("index_flash","切换广告4");
 insert into advert(position_code,name)values("index_flash","切换广告5");

 insert into advert(position_code,name)values("index_hot","最近好玩意1");
 insert into advert(position_code,name)values("index_hot","最近好玩意2");
 insert into advert(position_code,name)values("index_hot","最近好玩意3");
 insert into advert(position_code,name)values("index_hot","最近好玩意4");
 insert into advert(position_code,name)values("index_hot","最近好玩意5");
 insert into advert(position_code,name)values("index_hot","最近好玩意6");


 insert into advert(position_code,name)values("index_daren","达人喜欢1");
 insert into advert(position_code,name)values("index_daren","达人喜欢2");
 insert into advert(position_code,name)values("index_daren","达人喜欢3");
 insert into advert(position_code,name)values("index_daren","达人喜欢4");
  insert into advert(position_code,name)values("index_daren","达人喜欢5");

 insert into advert(position_code,name)values("index_member","会员专享1");
 insert into advert(position_code,name)values("index_member","会员专享2");
 insert into advert(position_code,name)values("index_member","会员专享3");
 insert into advert(position_code,name)values("index_member","会员专享4");
 insert into advert(position_code,name)values("index_member","会员专享5");
 insert into advert(position_code,name)values("index_member","会员专享6");
 insert into advert(position_code,name)values("index_member","会员专享7");

 insert into advert(position_code,name)values("index_meishi_left","健康美食1");
 insert into advert(position_code,name)values("index_meishi_left","健康美食2");
 insert into advert(position_code,name)values("index_meishi_left","健康美食3");
 insert into advert(position_code,name)values("index_meishi_left","健康美食4");
 insert into advert(position_code,name)values("index_meishi_left","健康美食5");
 insert into advert(position_code,name)values("index_meishi_left","健康美食6");
 insert into advert(position_code,name)values("index_meishi_left","健康美食7");
 insert into advert(position_code,name)values("index_meishi_left","健康美食8");
 insert into advert(position_code,name)values("index_meishi_left","健康美食9");

 insert into advert(position_code,name)values("index_meishi_right","健康美食-标签");



 insert into advert(position_code,name)values("index_techan_left","特产乡情1");
 insert into advert(position_code,name)values("index_techan_left","特产乡情2");
 insert into advert(position_code,name)values("index_techan_left","特产乡情3");
 insert into advert(position_code,name)values("index_techan_left","特产乡情4");
 insert into advert(position_code,name)values("index_techan_left","特产乡情5");
 insert into advert(position_code,name)values("index_techan_left","特产乡情6");
 insert into advert(position_code,name)values("index_techan_left","特产乡情7");
 insert into advert(position_code,name)values("index_techan_left","特产乡情8");
 insert into advert(position_code,name)values("index_techan_left","特产乡情9");

 insert into advert(position_code,name)values("index_techan_right","特产乡情-标签");

 insert into advert(position_code,name)values("index_zibu_left","滋补保健1");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健2");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健3");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健4");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健5");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健6");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健7");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健8");
 insert into advert(position_code,name)values("index_zibu_left","滋补保健9");

 insert into advert(position_code,name)values("index_zibu_right","滋补保健-标签");

 insert into advert(position_code,name)values("index_jujia_left","居家生活1");
 insert into advert(position_code,name)values("index_jujia_left","居家生活2");
 insert into advert(position_code,name)values("index_jujia_left","居家生活3");
 insert into advert(position_code,name)values("index_jujia_left","居家生活4");
 insert into advert(position_code,name)values("index_jujia_left","居家生活5");
 insert into advert(position_code,name)values("index_jujia_left","居家生活6");
 insert into advert(position_code,name)values("index_jujia_left","居家生活7");
 insert into advert(position_code,name)values("index_jujia_left","居家生活8");
 insert into advert(position_code,name)values("index_jujia_left","居家生活9");

 insert into advert(position_code,name)values("index_jujia_right","居家生活-标签");

 insert into advert(position_code,name)values("index_haowanyi_left","好玩意1");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意2");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意3");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意4");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意5");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意6");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意7");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意8");
 insert into advert(position_code,name)values("index_haowanyi_left","好玩意9");

 insert into advert(position_code,name)values("index_haowanyi_right","好玩意-标签");

 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌1");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌2");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌3");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌4");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌5");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌6");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌7");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌8");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌9");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌10");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌11");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌12");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌13");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌14");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌15");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌16");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌17");
 insert into advert(position_code,name)values("index_pinpai_meishi","美食品牌18");

 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌1");

 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌2");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌3");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌4");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌5");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌6");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌7");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌8");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌9");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌10");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌11");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌12");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌13");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌14");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌15");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌16");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌17");
 insert into advert(position_code,name)values("index_pinpai_tese","创意品牌18");

/*广场*/
 insert into advert_position(position,name,code,advert_type)values("square","顶部flash","square_flash",0);
 insert into advert_position(position,name,code,advert_type)values("square","精彩活动","square_activity",0);
 insert into advert_position(position,name,code,advert_type)values("square","大家正在讨论的宝贝","square_goods",2);
 insert into advert_position(position,name,code,advert_type)values("square","大家喜欢的主题","square_theme",4);
 insert into advert_position(position,name,code,advert_type)values("square","达人秀","square_daren",3);

 insert into advert(position_code,name)values("square_flash","切换广告1");
 insert into advert(position_code,name)values("square_flash","切换广告2");
 insert into advert(position_code,name)values("square_flash","切换广告3");
 insert into advert(position_code,name)values("square_flash","切换广告4");
 insert into advert(position_code,name)values("square_flash","切换广告5");
 insert into advert(position_code,name)values("square_flash","切换广告6");

 insert into advert(position_code,name)values("square_activity","精彩活动1");
 insert into advert(position_code,name)values("square_activity","精彩活动2");

 insert into advert(position_code,name)values("square_goods","大家讨论的商品1");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品2");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品3");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品4");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品5");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品6");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品7");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品8");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品9");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品10");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品11");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品12");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品13");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品14");
 insert into advert(position_code,name)values("square_goods","大家讨论的商品15");

 insert into advert(position_code,name)values("square_theme","大家喜欢的主题1");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题2");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题3");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题4");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题5");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题6");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题7");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题8");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题9");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题10");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题11");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题12");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题13");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题14");
 insert into advert(position_code,name)values("square_theme","大家喜欢的主题15");

 insert into advert(position_code,name)values("square_daren","达人秀1");
 insert into advert(position_code,name)values("square_daren","达人秀2");
 insert into advert(position_code,name)values("square_daren","达人秀3");
 insert into advert(position_code,name)values("square_daren","达人秀4");
 insert into advert(position_code,name)values("square_daren","达人秀5");


/* forum 广告*/
 insert into advert_position(position,name,code,advert_type)values("forum","精彩活动","forum_activity",0)
 insert into advert(position_code,name)value("forum_activity","精彩活动") ;
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

/* new 首页*/
insert into advert_position(position,name,code,advert_type)values("index","新-美食-tags","index-meishi-left",1);
insert into advert_position(position,name,code,advert_type)values("index","新-美食-图片","index-meishi-right",0);
insert into advert(position_code,name)value("index-meishi-left","新-首页-美食-tags");
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-1");
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-2") ;
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-3");
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-4")  ;
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-5") ;
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-6");
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-7") ;
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-8");
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-9")  ;
insert into advert(position_code,name)value("index-meishi-right","新-首页-美食-10") ;

insert into advert_position(position,name,code,advert_type)values("index","新-特产-tags","index-techan-left",1)  ;
insert into advert_position(position,name,code,advert_type)values("index","新-特产-图片","index-techan-right",0) ;
insert into advert(position_code,name)value("index-techan-left","新-首页-特产-tags")   ;
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-1");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-2");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-3");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-4");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-5");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-6");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-7");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-8");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-9");
insert into advert(position_code,name)value("index-techan-right","新-首页-特产-10");

insert into advert_position(position,name,code,advert_type)values("index","新-居家-tags","index-jujia-left",1)   ;
insert into advert_position(position,name,code,advert_type)values("index","新-居家-图片","index-jujia-right",0) ;
insert into advert(position_code,name)value("index-jujia-left","新-首页-居家-tags") ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-1")  ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-2") ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-3") ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-4")  ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-5")  ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-6")  ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-7") ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-8") ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-9")  ;
insert into advert(position_code,name)value("index-jujia-right","新-首页-居家-10")  ;

insert into advert_position(position,name,code,advert_type)values("index","新-好玩意-tags","index-haowanyi-left",1) ;
insert into advert_position(position,name,code,advert_type)values("index","新-好玩意-图片","index-haowanyi-right",0);
insert into advert(position_code,name)value("index-haowanyi-left","新-首页-haowanyi-tags");
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-1") ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-2")  ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-3")  ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-4") ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-5") ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-6") ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-7")  ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-8")  ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-9") ;
insert into advert(position_code,name)value("index-haowanyi-right","新-首页-haowanyi-10") ;

insert into advert_position(position,name,code,advert_type)values("index","新-底部-图片","index-bottom",0) ;
insert into advert(position_code,name)value("index-bottom","新-首页-底部-广告");


/*5 月 4 */
 insert into advert_position(position,name,code,advert_type)values("checkIn","签到推荐","checkIn",2);
 insert into advert(position_code,name,third_id)value("checkIn","签到推荐1",2) ;
 insert into advert(position_code,name)value("checkIn","签到推荐2");
 insert into advert(position_code,name)value("checkIn","签到推荐3");
 insert into advert(position_code,name)value("checkIn","签到推荐4");
 insert into advert(position_code,name)value("checkIn","签到推荐5");
 insert into advert(position_code,name)value("checkIn","签到推荐6");

/*5月 15 日 */
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


 #5月29日
 insert into advert_position(position,name,code,advert_type)values("miss","顶部切换广告","miss_flash",0);
 insert into advert(position_code,name)value("miss_flash","图片广告1") ;
 insert into advert(position_code,name)value("miss_flash","图片广告2") ;
 insert into advert(position_code,name)value("miss_flash","图片广告3") ;
 insert into advert(position_code,name)value("miss_flash","图片广告4") ;
 insert into advert(position_code,name)value("miss_flash","图片广告5") ;

  insert into advert_position(position,name,code,advert_type)values("miss","美食主题","miss_meishi_theme",4);
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题1") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题2") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题3") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题4") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题5") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题6") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题7") ;
 insert into advert(position_code,name)value("miss_meishi_theme","美食主题8") ;

 insert into advert_position(position,name,code,advert_type)values("miss","特产主题","miss_techan_theme",4);
 insert into advert(position_code,name)value("miss_techan_theme","特产主题1");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题2");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题3");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题4");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题5");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题6");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题7");
 insert into advert(position_code,name)value("miss_techan_theme","特产主题8");

  insert into advert_position(position,name,code,advert_type)values("miss","滋补主题","miss_zibu_theme",4);
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题1");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题2");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题3");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题4");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题5");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题6");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题7");
 insert into advert(position_code,name)value("miss_zibu_theme","滋补主题8");

  insert into advert_position(position,name,code,advert_type)values("miss","居家主题","miss_jujia_theme",4);
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题1");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题2");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题3");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题4");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题5");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题6");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题7");
 insert into advert(position_code,name)value("miss_jujia_theme","居家主题8");

   insert into advert_position(position,name,code,advert_type)values("miss","中国风主题","miss_china_theme",4);
 insert into advert(position_code,name)value("miss_china_theme","中国风主题1");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题2");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题3");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题4");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题5");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题6");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题7");
 insert into advert(position_code,name)value("miss_china_theme","中国风主题8");

  insert into advert_position(position,name,code,advert_type)values("miss","好玩意主题","miss_haowanyi_theme",4);
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题1");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题2");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题3");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题4");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题5");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题6");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题7");
 insert into advert(position_code,name)value("miss_haowanyi_theme","好玩意主题8");
