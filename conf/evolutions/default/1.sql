# --- First database schema

# --- !Ups

 /*
   宝贝baobei 与商品goods 是有区别的，商品与宝贝的关系 是一对多
   只有多对多的关系中，才需要中间表，同时，为了性能，需要做合适的冗余
 */
/************************************************************
 * 用户是个大的结构  主要有以下几个表
 * user表                User常用的基本信息，包括常用的统计信息
 * user_profile 表        User不常用的信息、例如生日、性别、所在位置等等
 * user_stats             用户统计信息
 * user_tag 表            用户的标签
 * user_follow 表         用户关注的人
 * user_love_baobei 表     用户喜欢的宝贝
 * user_love_theme 表    用户喜欢的主题
 * user_love_topic 表    用户喜欢的话题
 * user_share_baobei 表    用户分享的宝贝
 * user_trend     表    用户动态，类似于用户的操作记录，记录用户的动作

 * user_bind  表         用户绑定的第三方账号  这个待定，目前对第三方登陆接口的开发和使用 还不清楚
 **************************************************************/
 /*数据表  User  用户的基本信息和常用信息
     id       用户ID
     name     用户昵称
     passwd   用户密码
     email    用户邮箱
     intro     简介           取消   移动到profile中了
     pic      用户头像
     credit   用户积分
     email_state  用户邮箱状态 验证、未验证、即将验证  ,   取消，放在status中
     is_daren  是否是达人                               取消，放在status中
     is_blank  是否被拉黑                 取消     如果判断用户是商家用户，用户会被拉黑，不允许分享商品
     level    用户级别                   取消
     daren_title  达人头衔               取消 用daren enum 实现
     status      用户状态 例如正常状态、拉黑状态、活跃用户等    0:邮箱未验证 1：正常用户 2 拉黑 3 活跃
     comeFrom    新增，用户的来源：0 食美特 1 qq 2 新浪微博 3 淘宝
     province    所在省份            取消 放在profile中
     hot_index  指数
     tags        标签，以,隔开
     fans_num  粉丝数
     follow_num 关注人数
     trend_num  用户动态数量
     love_baobei_num                  喜欢的宝贝数量
     love_theme_num                   喜欢的主题数量
     love_topic_num                   喜欢的话题数量
     post_baobei_num                  发布的宝贝数量
     post_theme_num                   发布的主题数量
     post_topic_num                   发布的话题数量
  */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `name`                varchar(64) NOT NULL ,
  `passwd`              varchar(64) NOT NULL default '0',
  `email`               varchar(128),
  `credits`              smallint(10) not null default '0',
  `pic`                 varchar(255) NOT NULL default '/images/user/default.jpg',
  `daren`               tinyint not null default '0',
  `status`                tinyint    not null default '0',
  `come_from`              tinyint    not null default '0',
  `open_id`               varchar(64),
  `hot_index`              smallint    not null default '1',
  `tags`                    varchar(250) ,
  `fans_num`                  smallint(11) DEFAULT '0',
  `follow_num`                smallint(11) DEFAULT '0',
  `trend_num`                 smallint(10) DEFAULT '0',
  `love_baobei_num`           smallint(11) DEFAULT '0',
  `love_theme_num`            smallint(11) DEFAULT '0',
  `love_topic_num`           smallint(11) DEFAULT '0',
  `post_baobei_num`          smallint(10) DEFAULT '0',
  `post_theme_num`            smallint(10) DEFAULT '0',
  `post_topic_num`           smallint(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


  /*数据表  User  用户的基本信息和常用信息
     id               表D
     uid             用户ID
     invite_uid      邀请者ID   -1:qq -2 微博 -3 淘宝

     invite_uname    邀请者昵称
     regist_time     注册时间
     login_time     上次登录时间
     login_ip    用户登录的IP
     gender          用户性别  0 女  1 男 2 保密
     province         所在省份
     city         所在城市
     town         所在镇、县
     street       所在具体的街道
     post_code    邮编
     phone        联系手机

     blog        个人博客、微博、qq空间


  */
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE IF NOT EXISTS `user_profile` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `invite_id`           int(10) ,
  `invite_name`         varchar(50),
  `gender`              tinyint(4) not null DEFAULT '2',
  `intro`               varchar(200),
  `birth`                varchar(16) ,
  `weixin`                varchar(64) ,
  `alipay`                varchar(64) ,
  `receiver`            varchar(20) ,
  `province`            varchar(20),
  `city`               varchar(20) ,
  `town`                varchar(20),
  `street`              varchar(50) ,
  `post_code`           varchar(16) ,
  `phone`               char(32) ,
  `blog`                varchar(200) ,
  `login_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `login_num`           smallint(10) NOT NULL default '1',
  `login_ip`            varchar(32) DEFAULT '0',
  `regist_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
/*

  -- 表的结构 `user_stats`
     id                               表的ID
     uid                              用户ID
     uname                            用户的名称


     `signIn_time`                   用户签订时间
      signIn_days`                   用户连续签订的天数
--
*/

DROP TABLE IF EXISTS `user_stats`;
CREATE TABLE `user_stats` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `fans_num`                  smallint(11) DEFAULT '0',
  `follow_num`                smallint(11) DEFAULT '0',
  `trend_num`                 smallint(10) DEFAULT '0',
  `love_baobei_num`           smallint(11) DEFAULT '0',
  `love_theme_num`            smallint(11) DEFAULT '0',
  `love_topic_num`           smallint(11) DEFAULT '0',
  `post_baobei_num`          smallint(10) DEFAULT '0',
  `post_theme_num`            smallint(10) DEFAULT '0',
  `post_topic_num`           smallint(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_follow`
     id 表的ID
     uid  用户ID
     fans_id 粉丝ID
     add_time  添加时间
--
*/

DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow` (
  `id`                int(10) NOT NULL AUTO_INCREMENT,
  `uid`               int(10) NOT NULL DEFAULT '0',
  `fans_id`           int(10) NOT NULL DEFAULT '0',
  `add_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_tag`
     id 表的ID
     uid 用户的id
     tag_name 标签名称
     match_index 匹配指数
     add_time  添加时间
--
*/

-- --------------------------------------------------------
/* DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag` (
  `id`          int(10) NOT NULL AUTO_INCREMENT,
  `uid`         int(10) NOT NULL DEFAULT '0',
  `tag_name`    int(10) NOT NULL DEFAULT '0',
  `match_index`  tinyint NOT NULL DEFAULT '1',
  `add_time`    timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/




-- --------------------------------------------------------
/*
  -- 表的结构 `user_trend`
     id 表的ID
     uid 用户的id
     uname 用户的昵称
     action_type 行为动作，例如 0、喜欢的商品 1、关注主题 2、发布宝贝 3、创作主题 4、评论主题 5、回复话题 6 认为值得
     action_id 行为的名称
     action_url   链接的url
     action_content 行为的内容
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_trend`;
CREATE TABLE `user_trend` (
  `id`                 int(10) NOT NULL AUTO_INCREMENT,
  `uid`                int(10) NOT NULL DEFAULT '0',
  `action_name`       varchar(32) not null default 'love',
  `action_id`         int(10) NOT NULL,
  `action_url`         varchar(128) NOT NULL DEFAULT 'http://smeite.com',
  `action_content`     varchar(128) NOT NULL DEFAULT 'smeite',
  `add_time`           timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_baobei`
     id 表的ID
     uid 用户的id
     goods_id 商品的Id
     goods_name 商品的名称
     goods_img  商品的主图
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_love_goods`;
CREATE TABLE `user_love_goods` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL ,
  `goods_id` int(10) NOT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_theme`
     id 表的ID
     uid 用户的id
     theme_id 主题的Id
     theme_name 主题的名称
     add_time  添加时间
--
*/

-- --------------------------------------------------------

 DROP TABLE IF EXISTS `user_love_theme`;
CREATE TABLE `user_love_theme` (
  `id`   int(10) NOT NULL AUTO_INCREMENT,
  `uid`   int(10) not null,
  `theme_id` int(10) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_topic`
     id 表的ID
     uid 用户的id
     topic_id 话题的Id
     topic_name 话题的名称
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_love_topic`;
CREATE TABLE `user_love_topic` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL ,
  `topic_id` int(10) NOT NULL ,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------
/*
  -- 表的结构 `user_share_goods`
     id 表的ID
     uid 用户的id
     goods_id 商品的Id
     goods_name 商品的名称
     goods_img  商品的主图
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `user_share_goods`;
 CREATE TABLE `user_share_goods` (
   `id` int(10) NOT NULL AUTO_INCREMENT,
   `uid` int(10) NOT NULL DEFAULT '0',
   `goods_id` int(20) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/************************************************************
 * forum  论坛：主要有话题、话题回复     板块》话题》回复，讨论话题与goods的评论、theme的讨论分开设计，这就是一个独立的模块
 * topic表                话题
 * topic_group 表         话题组：即论坛的板块        暂不实现，都是统一的板块之下食美特:0，以后扩展的时候，在添加
 * topic_reply 表         话题的回复，与话题是多对一的关系
 * topic_type 表          话题的类型，例如普通、问答、知识、经验、精华、文化等等，目前暂不写在数据库中，直接enum枚举在程序中实现

 **************************************************************/
 -- --------------------------------------------------------
/*
  -- 表的结构 `topic 讨论话题`
     id 表的ID
     uid           用户的id
     uname         用户的名称
     title         讨论的话题名称
     content       讨论的话题内容
     intro         简介
     pics          图片
     topic_group   所属板块，0：讨论吧 1 创意吧
     topic_type    话题的类型 0 普通 2、问答 3 知识 4、经验、5、文化故事
     is_best      是否精华
     is_top       是否置顶
     hot_index     排序指数
     check_state   审核状态
     add_time     添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE IF NOT EXISTS `topic`(
   `id`                int(10) NOT NULL AUTO_INCREMENT,
  `uid`                int(10) NOT NULL ,
  `uname`              varchar (32) NOT NULL ,
  `title`              varchar (128) NOT NULL ,
  `content`            text    NOT NULL ,
  `intro`             varchar(200)   NOT NULL ,
  `pics`              text ,
  `group_id`         tinyint NOT NULL DEFAULT '0',
  `type_id`          tinyint NOT NULL DEFAULT '0',
  `is_best`            tinyint(1) NOT NULL DEFAULT '0',
  `is_top`            tinyint(1) NOT NULL DEFAULT '0',
  `hot_index`           smallint(10)  default '0',
  `reply_num`           smallint(10) default '0',
  `view_num`           smallint(10)  default '1',
  `love_num`           smallint(10)  default '0',
  `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- --------------------------------------------------------
/*
  -- 表的结构 `topic_reply 话题讨论回复`
     id                  表的ID
     uid                  用户的id
     uname               用户的名称
     topic_id            话题名称
     quote_reply          引用的内容
     content             回复的内容
     is_delete           是否删除
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `topic_reply`;
CREATE TABLE IF NOT EXISTS `topic_reply`(
    `id`                     int(10) NOT NULL AUTO_INCREMENT,
    `uid`                    int(10) NOT NULL ,
   `uname`                   varchar (32) NOT NULL ,
  `topic_id`                 int(10) NOT NULL ,
  `quote_reply`              varchar (255),
  `content`                 varchar (200) ,
   `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/************************************************************
 * theme发布的宝贝主题 ，主要有
 * theme_category            主题组，可以有多级分类，一般是三级分类
 * theme 表               主题
 * theme_discuss 表        主题的评价
 * theme_goods 表         主题下的宝贝，与商品goods 是多对多的关系
 * theme_tag 表           主题的标签      取消2012、11、6
 * theme_style 表          主题装修

 **************************************************************/
 -- --------------------------------------------------------
/*
  -- 表的结构 `theme_category `
     id 表的ID
     name            名称
    parent_id        父ID
    parent_name      名称
    hot_index          推荐指数 用于排序
    is_visible        是否显示
    seo_title        用户的名称
    seo_keywords     seo 关键词
    seo_desc          seo 描述
    modify_time      修改
    add_time  添加时间
--
*/
-- ------------------------------------------------------------
/* DROP TABLE IF EXISTS `theme_category`;
CREATE TABLE IF NOT EXISTS `theme_category`(
   `id`                     smallint(10) NOT NULL AUTO_INCREMENT,
  `name`                    varchar(32) not null default '',
*//*  `parent_id`               smallint(10) not null default '0',
  `parent_name`             varchar(32) not null  default '',*//*
  `hot_index`              smallint(10)  not null default '1',
  `is_visible`              tinyint(1)  not null default '1',
  `seo_title`               varchar(128) not null default '',
  `seo_keywords`            varchar(255) not null  default '',
  `seo_desc`                varchar(255) not null  default '',
  `modify_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/


 -- --------------------------------------------------------
/*
  -- 表的结构 `theme `
     id                 表的ID
     name              专题组名称
     intro             介绍
     hot_index         关注指数，可以根据这个值的大小 desc or asc 排序
     is_visible       是否显示
     uid         作者id
     uname       作者名称
     cid          分类
     cname        分类名称
     tags              标签组      2012.11.04 新增，用于简化处理theme_tag,
     love_nums        喜欢的人
     pic               主题封面
     seo_title       用户的名称
     seo_keywords     seo 关键词
     seo_desc          seo 描述
     modify_time      修改
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `theme`;
CREATE TABLE IF NOT EXISTS `theme`(
      `id`                      int(10) NOT NULL AUTO_INCREMENT,
      `name`                    varchar(128) not null default '',
      `intro`                   varchar(255) ,
      `hot_index`               smallint(10) not null default '0',
      `is_visible`              tinyint(1) not null default '0',
      `uid`                int(10) not null default '0',
      `uname`              varchar(32) not null default '',
      `pic`                varchar(128) not null default '/images/theme/default.jpg',
      `cid`                tinyint default '5' ,
       `tags`                   varchar(128),
       `love_num`              smallint(10) not null default '1' ,
       `reply_num`              smallint(10) not null default '0' ,
  `goods_num`              smallint(10) not null default '0' ,
  `seo_title`               varchar(128) ,
      `seo_keywords`            varchar(255) ,
      `seo_desc`                varchar(255) ,
      `modify_time`             timestamp,
      `add_time`                timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- --------------------------------------------------------
/*
  -- 表的结构 `theme_discuss `
     id                 表的ID
     content            评论内容
     theme_id              专题ID
     theme_name            专题名称
     uid                    评论人ID
     uname                 评论人昵称
     is_delete              是否删除
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `theme_discuss`;
CREATE TABLE IF NOT EXISTS `theme_discuss`(
      `id`                          int(10) NOT NULL AUTO_INCREMENT,
     `theme_id`                      int(10) NOT NULL ,
     `uid`                           int(10) NOT NULL ,
     `uname`                         varchar (32) ,
     `content`                       varchar (255),
      `check_state`          tinyint NOT NULL DEFAULT '0',
     `add_time`                      timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- --------------------------------------------------------
/*
  -- 表的结构 `theme_goods `
     id                 表的ID
     theme_id              专题ID
     goods_id              商品名称
     pic                   商品图片

     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `theme_goods`;
CREATE TABLE IF NOT EXISTS `theme_goods`(
          `id`                   int(10) NOT NULL AUTO_INCREMENT,
         `theme_id`                      int(10) NOT NULL ,
         `goods_id`                      int (10) NOT NULL ,
         `goods_pic`                     varchar(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `theme_style `
    <style>
body{
	background-color:#F9F9EF;
	 backgroup-image:url();
	background-repeat:no-repeat;
	background-position:center top;
	background-attachment:fixed;
}
.banner {
	height:70px;
	background-color:#fafafa;
	 background-image:url();
	 	background-repeat:repeat;
	background-position:center top;
	max-height:600px;
	min-heihgt:80px;
	overflow:hidden;
}
.banner .banner-title .subtitle {
	color : #666;
}
</style>
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `theme_style`;
CREATE TABLE IF NOT EXISTS `theme_style`(
  `theme_id`                      int(10) NOT NULL ,
  `page_bg_color`                      varchar (16) NOT NULL default'#F9F9EF',
  `page_bg_image`                      varchar (128) NOT NULL default '',
  `page_bg_repeat`                      varchar (32) NOT NULL default 'no-repeat',
  `page_bg_position`                      varchar (32) NOT NULL default 'center top',
  `page_bg_attachment`                      varchar (16) NOT NULL  default 'scroll',
  `banner_height`                      varchar (16) NOT NULL  default '70',
  `banner_color`                      varchar (16) NOT NULL  default '#f666',
  `banner_bg_color`                      varchar (16) NOT NULL default '#fafafa',
  `banner_bg_image`                      varchar (128) NOT NULL  default '',
  `banner_bg_repeat`                      varchar (32) NOT NULL default 'no-repeat',
  `banner_bg_position`                      varchar (32) NOT NULL  default 'scroll',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`theme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- --------------------------------------------------------
/*
  -- 表的结构 `theme_tag `
     id                 表的ID
     theme_id              专题ID
     theme_name            专题名称
     tag_id               标签ID
     tag_name              标签名称
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
/*DROP TABLE IF EXISTS `theme_tag`;
CREATE TABLE IF NOT EXISTS `theme_tag`(
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `theme_id`                      int(10) NOT NULL ,
  `theme_name`                    varchar(128) not null default '',
  `tag_id`                      int(10) NOT NULL ,
  `tag_name`                    varchar(128) not null default '',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/










/************************************************************
 * goods的宝贝 ，主要有
 * goods 表                   宝贝的常用的、基本信息
 * goods_profile 表           宝贝的详细信息
 * goods_assess 表            宝贝的评价、鉴定等
 * goods_tag 表               宝贝的标签
 *   shop   表                宝贝店铺
  * goods_shop 表              可以购买的宝贝店铺，一个宝贝可以有从多个店铺购买

 **************************************************************/
  -- --------------------------------------------------------
/*
  -- 表的结构 `goods `
     id                 表的ID
	 num_iid             商品ID
	 track_iid            跟踪ID  商品ID 和 跟踪Id 至少有一个存在，如果track_iid 存在，则优先使用。
     name              商品名称
     intro              商品介绍
     desc               商品详情  主要为合作商户使用
     price              价格
     pic                 主图片
	 item_pics            所有图片
	 nick                  卖家昵称
	 food_security         生产许可号|产品标准号|配料表|储藏方法|保质期|食品添加剂|供货商|生产日期
	 detail_url           商品的销售网址
     love_num           喜欢的人数
	 volume            最近30天销售额
     `status`            商品状态、上架 、下架 、审核
     `is_member`            是否是会员专享
     `hot_index`            热门指数
      collect_time        采集时间
      add_time            修改时间
--
*/
/*
*  每个用户都有个默认的shop，在发布时即可获取。但是考虑到淘宝同质化的产品很多，即同一个产品在不同店销售，去购买的shop很多，需要人工编辑，通过后台使用goods_shop 编辑。
*  当然了，这个人工处理量很大。
*/
-- ------------------------------------------------------------
   DROP TABLE IF EXISTS `goods`;
CREATE TABLE IF NOT EXISTS `goods`(
   `id`                   int(10) NOT NULL AUTO_INCREMENT,
   `num_iid`               bigint (20) not null,
   `track_iid`             varchar(32),
  `name`                    varchar(128) not null  ,
  `intro`                    varchar(255) not null ,
  `desc`                     text ,
  `price`                    varchar(16) not null ,
  `pic`                      varchar(128) not null ,
  `item_pics`                varchar(500),
  `nick`                     varchar(32),
  `food_security`            varchar(500),
  `detail_url`               varchar(128),
  `love_num`                 smallint(10) not null  DEFAULT '1',
   `volume`                  smallint(10) default '0',
  `status`                   tinyint  not null default '1'   ,
  `is_member`                  tinyint(1) not null default '0',
  `hot_index`                   smallint(10) not null default '0',
  `collect_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                 timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  -- --------------------------------------------------------
/*
  -- 表的结构 `goods_profile `
     id                 表的ID
     gid               商品ID
     stock             库存量
     buy_num           最近购买量
     province          所在省份
     city              所在城市
     食品安全属性
prd_license_no	 	   生产许可证号
design_code	    	   产品标准号
factory	         	   厂名
factory_site	 	   厂址
contact		 	       厂家联系方式
mix	  	                配料表
plan_storage		           储藏方法 常温
period	    	        保质期
food_additive	        食品添加剂 例如磷脂 、膨松剂
supplier	 	        供货商   深圳岸通商贸有限公司
product_date_start	 	生产开始日期    	2012-06-01


     seo_title         seo 名称
     seo_keywords      seo 关键词
     seo_desc          seo 描述


--
*/
-- ------------------------------------------------------------

DROP TABLE IF EXISTS `goods_profile`;
/*
CREATE TABLE IF NOT EXISTS `goods_profile`(
     `id`                      int(10) NOT NULL AUTO_INCREMENT,
     `gid`                     int(10) not null,
    `stock`                    smallint(10) not null default '0' ,
    `buy_num`                  smallint(10) not null default '0' ,
     `province`                varchar(16) not null default '' ,
     `city`                    varchar(16) not null default '' ,
  `prd_license_no`             varchar(64) not null default '' ,
  `design_code`                varchar(64) not null default '' ,
  `factory`                    varchar(64) not null default '' ,
  `contact`                    varchar(32) not null default '' ,
  `mix`                        varchar(128) not null default '' ,
  `plan_storage`               varchar(32) not null default '' ,
  `period`                      varchar(32) not null default '' ,
  `food_additive`               varchar(128) not null default '' ,
  `supplier`                    varchar(128) not null default '' ,
  `product_date_start`          varchar(128) not null default '' ,
  `seo_title`                   varchar(128) not null default '' ,
  `seo_keywords`                varchar(128) not null default '' ,
  `seo_desc`                    varchar(255) not null default '' ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

*/
-- --------------------------------------------------------
/*    会员专享商品 特价推荐商品
  -- 表的结构 `member_goods `
    id
    gid              商品的ID
    pic              商品的图片
    price            原价
    discountPrice    现价
    startDate        开始时间
    endDate          结束时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `member_goods`;
/*
CREATE TABLE IF NOT EXISTS `member_goods`(
  `id`                       int(10) NOT NULL AUTO_INCREMENT,
  `gid`                       int(10) not null,
  `src`                        varchar(128) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/
   -- --------------------------------------------------------
/*
  -- 表的结构 `goods_shop `
   `id`
    gid       商品的ID
    shop_id   卖家名称

--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `goods_shop`;
CREATE TABLE IF NOT EXISTS `goods_shop`(
      `id`                      int(10) NOT NULL AUTO_INCREMENT,
  `goods_id`                 int(10) not null,
      `num_iid`                 bigint(20) not null,
  `shop_id`                 int(10) not null,
      `nick`                       varchar (32) not null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    -- --------------------------------------------------------
/*
  -- 表的结构 `shop `
   `id`
   nick	        	卖家昵称
   title	     	店铺标题
   item_score	 String	 否	60	商品描述评分
   service_score	 String	 否	100	服务态度评分
   delivery_score	 String	 否	90	发货速度评分
   created	         Date	 	   开店时间。格式：yyyy-MM-dd HH:mm:ss
   credits            卖家信用
   grade              店铺级别
   note               备注
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `shop`;
CREATE TABLE IF NOT EXISTS `shop`(
      `id`                  int(10) NOT NULL AUTO_INCREMENT,
      `nick`                 varchar(32) not null ,
      `title`               varchar(128) ,
     `detail_url`               varchar(128) ,
      `item_score`           varchar(10)  not null default '0',
      `service_score`        varchar(10)  not null default '0',
      `delivery_score`       varchar(10)  not null default '0',
      `created`             varchar(32) ,
      `credits`             smallint(10),
     `grade`              varchar(10),
      `status`              tinyint default '0',
      `note`               varchar (250),
  `collect_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


   -- --------------------------------------------------------
/*
  -- 表的结构 `goods_assess `
     id                 表的ID
     gid               商品名称
     uid               用户的ID
     uname             用户的名称
     content           评论内容
     is_value          鉴定是否值得
     is_buy           是否购买过
     add_time         添加时间
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `goods_assess`;
CREATE TABLE IF NOT EXISTS `goods_assess`(
   `id` int(10) NOT NULL AUTO_INCREMENT,
  `goods_id`                       int(10) not null,
  `uid`                          int(10) not null,
  `uname`                       varchar (32) not null,
  `content`                       varchar (255) not null,
  `is_worth`                      tinyint(1) not null default '0',
  `is_bought`                      tinyint(1) not null default '0',
  `check_state`             tinyint  not null default '0',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

   -- --------------------------------------------------------
/*
  -- 表的结构 `goods_tag `
     id                 表的ID
     gid               商品名称
     tag_id
     tag_name
     tag_num            选择这个标签的人
--
*/
-- ------------------------------------------------------------
/* DROP TABLE IF EXISTS `goods_tag`;
CREATE TABLE IF NOT EXISTS `goods_tag`(
      `id`                      int(10) NOT NULL AUTO_INCREMENT,
      `goods_id`                int(10) not null default '0',
      `tag_id`                  int(10) not null default '0',
      `tag_name`                varchar(32) not null default '',
      `tag_num`                smallint (10) not null default '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/





 /************************************************************
 * tag ，标签是购物分享系统的核心，标签直接与宝贝关联，所有需要展示宝贝的列表页，都需要通过它自身的标签关联到宝贝上
 * tag  表                    标签
 * tag_group 表               标签组
 * tag_goods 表               标签下的宝贝 ，为了保证网站信息的质量，标签下的商品都需要通过审核，审核通过的商品才能在前端，显示某个标签下
 **************************************************************/
 -- --------------------------------------------------------
/*
  -- 表的结构 `tag `
     id                 表的ID
     name               标签名称
     intro              简介
     tag_group_id       标签组id
     tag_group_name     标签组名称
     hot_index          推荐指数
     goods_num          产品的数量
     add_num          添加次数  用户添加标签时，同样的标签记录一次
     is_top             是否置顶
     is_highlight        是否强调
     sort_num            排序
     check_state         审核状态
     seo_title          seo
     seo_keywords       seo 关键词
     seo_desc           seo 描述
     modify_time        修改
     add_time           添加时间


--
*/
-- ------------------------------------------------------------

DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag`(
  `id`                   int(10) NOT NULL AUTO_INCREMENT,
  `name`                       varchar(32) not null ,
  `cid`                   tinyint (4) ,
  `group_id`                   int(10) ,
  `group_name`                 varchar(32),
   `hot_index`                 smallint(10) not null default '1',
   `add_num`                 smallint(10) not null default '1',
  `is_top`                 tinyint(1) not null default '0',
  `is_highlight`                 tinyint(1) not null default '0',
  `sort_num`                 smallint(10) not null default '0',
  `check_state`                 tinyint(4) not null default '0',
  `seo_title`                  varchar(128)  ,
  `seo_keywords`               varchar(128)  ,
  `seo_desc`                   varchar(255)  ,
  `modify_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


 -- --------------------------------------------------------

/*  -- 表的结构 `tag_group `
     id                 表的ID
     name               标签名称
     category_id         theme 分类 id
     category_name         theme 分类名称
     hot_index         推荐指数 与排序有关
     intro
     is_visible
      seo_title          用户的名称
     seo_keywords       seo 关键词
     seo_desc           seo 描述
     modify_time        修改
     add_time           添加时间
     11月3日
     tag_group 需要跟theme_group进行关联,一个theme_group 对应多个tag_group
--*/
-- ------------------------------------------------------------
  DROP TABLE IF EXISTS `tag_group`;
CREATE TABLE IF NOT EXISTS `tag_group`(
       `id`                     int(10) NOT NULL AUTO_INCREMENT,
      `name`                    varchar(32) not null default '',
  `pic`                       varchar(128) not null default '/assets/ui/tag.jpg',
      `intro`                   varchar(128) not null default '',
      `cid`                     smallint(10) NOT NULL,
      `hot_index`               smallint(10) NOT NULL default '0',
     `is_visible`               tinyint(1) not null default '1',
  `sort_num`                 smallint(10) not null default '0',
    `seo_title`                 varchar(128) not null default '' ,
    `seo_keywords`              varchar(128) not null default '' ,
    `seo_desc`                  varchar(255) not null default '' ,
    `modify_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
    `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 -- --------------------------------------------------------
/*
  -- 表的结构 `tag_goods `
     id                 表的ID
     tag_name           标签名称
     check_state        审核状态  未审核、审核通过 审核未通过
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
    DROP TABLE IF EXISTS `tag_goods`;
CREATE TABLE IF NOT EXISTS `tag_goods`(
       `id`                 int(10) NOT NULL AUTO_INCREMENT,
  `tag_name`                varchar(32) not null ,
  `goods_id`                int (10)  not null ,
  `add_num`                 int(10) not null default '1',
  `check_state`             tinyint  not null default '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




  /************************************************************
 * 站内信、短消息
 * msg 表               用户发的msg
 * msg_receiver 表      用户发的msg接受者
 **************************************************************/
   -- --------------------------------------------------------
/*
  -- 表的结构 `msg `
     id                 表的ID
     title              标题
     content           内容
     sender_id          发信者名称
     sender_name        发信者名称
     check_state        是否审核
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
      DROP TABLE IF EXISTS `msg`;
CREATE TABLE IF NOT EXISTS `msg`(
       `id` int(10) NOT NULL AUTO_INCREMENT,
  `title`                varchar(64) not null default '',
  `content`                varchar(255) not null default '',
  `sender_id`                int(10) not null default '0',
  `sender_name`                varchar(32) not null default '',
  `check_state`               tinyint not null default '1',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   -- --------------------------------------------------------
/*
  -- 表的结构 `msg_receiver `
     id                 表的ID
     receiver_id          发信者名称
     receiver_name        发信者名称
     is_delete        是否删除
     is_read          是否已读
     msg_id
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `msg_receiver`;
CREATE TABLE IF NOT EXISTS `msg_receiver`(
        `id`                       int(10) NOT NULL AUTO_INCREMENT,
       `msg_id`                    int(10) not null default '1',
       `receiver_id`                int(10) not null default '0',
       `receiver_name`             varchar(32) not null default '',
      `is_delete`                tinyint(1) not null default '0',
     `is_read`                    tinyint(1) not null default '0',
      `add_time`                     timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



   /************************************************************
 * 管理员
 * admin  表             管理员
 * admin_role            管理员角色
 **************************************************************/
    /*
  数据表：user  用户（使用的人）*/
    DROP TABLE IF EXISTS `manager`;
    create table manager(
      `id`                 smallint (10) not null  auto_increment ,
      `email`             varchar(64) not null,
      `passwd`             varchar(64) not null,
      `name`          varchar(32) not null default '',
      `department`         varchar(16) not null default '',
      `phone`              varchar(30) not null default '',
      `login_time`         timestamp default '2012-5-12 14:18:00',
      `add_time`            timestamp default '2012-5-12 14:18:00',
      `role_id`        smallint  not null default '1',
      `role_name`      varchar(32) not null default '',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    /*
     * 数据表  userRole   用户角色
    */
    DROP TABLE IF EXISTS `manager_role`;
    create table manager_role(
      id             int  unsigned primary  key  auto_increment ,
      name           varchar(32) not null,
      note           varchar(128) not null default '',
      modify_time     timestamp  ,
      add_time       timestamp default '2012-5-12 14:18:00'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    /*
     数据表permission  资源列表
     sid 自定义ID
    */
/*DROP TABLE IF EXISTS `permission`;
    create table permission(
      id               smallint      unsigned primary  key  auto_increment ,
      sid              varchar(32)   not null,
      name             varchar (64)  not null,
      note             varchar(128)  not null default '',
      operator         varchar(32)   not null default 'smeite',
      modify_time     timestamp  ,
      add_date          timestamp default '2012-5-12 14:18:00'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `admin_role_permission`;
    create table admin_role_permission(
      userRole_id              int,
      userRole_name            varchar (32) not null,
      permission_id            int    ,
      permission_name          varchar (64)  not null,
      PRIMARY KEY (`userRole_id`,`permission_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

/*
 数据表manager_task  用户工作列表
 id          自定义ID
 level       重要程度
  leader      负责人
 content     工作内容
 startDate   开始时间
 endDate     结束时间
 state       状态:进行中  未完成 已完成  推迟
 note        备注
 addTime     添加时间
*/
DROP TABLE IF EXISTS `manager_task`;
create table manager_task(
  id              smallint (10) not null  auto_increment ,
  level          tinyint     default 0,
  leader         varchar(32)   not null,
  content        varchar(250)   not null,
  start_date        timestamp ,
  end_date         timestamp ,
  status           tinyint   not null default 0,
  note              text ,
  add_time          timestamp default '2012-5-12 14:18:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*广告组*/
DROP TABLE IF EXISTS `advert_position`;
create table advert_position(
  id              smallint (10) not null  auto_increment ,
  position        varchar(16)   not null,
  name            varchar (64)  not null,
  code            varchar(32)   not null,
  advert_type       tinyint default 0,
  add_time          timestamp default '2012-5-12 14:18:00',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*广告
* name  位置名称
* title 广告名称
*/
DROP TABLE IF EXISTS `advert`;
create table advert(
  id              smallint (10) not null  auto_increment ,
  position_code   varchar(32)  not null,
  third_id          int,
  name            varchar (64)  not null,
  title           varchar(128),
  content         text ,
  pic             varchar(128),
  spic           varchar(128),
  width           smallint(10) default '0',
  height          smallint(10) default '0',
  link            varchar(128),
 note              varchar(200) default 'note',
  click_num       smallint(10) default '1',
  add_time          timestamp default '2012-5-12 14:18:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






/* ***************************************************** */

/*2013 1 13  服务器未改 */
alter table  user change pic pic  varchar(255) NOT NULL default '/images/user/default.jpg';

/* 2013  1 6  */
alter table  user_profile change login_nums login_num  smallint(10);
alter table tag  add cid tinyint ;

/*2013 1 19*/
alter table goods change status status tinyint  not null default '1';
alter table goods change is_member is_member tinyint  not null default '0';

alter table  user_profile change login_num login_num  smallint(10) not null default '1';


alter table theme change pic  `pic`  varchar(128) not null default '/images/theme/default.jpg';

/*2013 3 6*/
alter table user change hot_index shi_dou smallint not null  default 0;
update goods set food_security=null ;
alter table goods change food_security promotion_price varchar(16);

/*2013-3-15  */
alter table theme change is_visible is_recommend   tinyint(1) not null default '0';
alter table theme change modify_time modify_time timestamp ;
alter  table theme change  add_time   add_time timestamp ;

/*2013-3-25*/
alter table theme_goods add sort_num smallint(10) not null default '0';
alter table tag_goods add  sort_num  smallint(10) not null default '0';

/*2013-4-9*/
alter table  goods change hot_index commission smallint(10) not null default '0';
alter table  goods change commission commission_rate  smallint(10);
alter table goods  add  rate  tinyint not null default '70';
/*2013-4-10*/
alter table goods drop column `desc ;
alter table goods add come_from  tinyint not null default '0';

/*2013-4-12*/
DROP TABLE IF EXISTS `user_stats`;
CREATE TABLE `user_static` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `fans_num`                  smallint(11) DEFAULT '0',
  `follow_num`                smallint(11) DEFAULT '0',
  `trend_num`                 smallint(10) DEFAULT '0',
  `love_baobei_num`           smallint(11) DEFAULT '0',
  `love_theme_num`            smallint(11) DEFAULT '0',
  `love_topic_num`           smallint(11) DEFAULT '0',
  `post_baobei_num`          smallint(10) DEFAULT '0',
  `post_theme_num`            smallint(10) DEFAULT '0',
  `post_topic_num`           smallint(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

insert into user_static(uid,fans_num,follow_num,trend_num,love_baobei_num,love_theme_num,love_topic_num,post_baobei_num,post_theme_num,post_topic_num) select id,fans_num,follow_num,trend_num,love_baobei_num,love_theme_num,love_topic_num,post_baobei_num,post_theme_num,post_topic_num from user ;

alter table user drop column fans_num;
alter table user drop column follow_num;
alter table user drop column trend_num;
alter table user drop column love_baobei_num;
alter table user drop column love_theme_num;
alter table user drop column love_topic_num;
alter table user drop column post_baobei_num;
alter table user drop column post_theme_num;
alter table user drop column post_topic_num;

alter table user add  withdraw_credits  smallint(10) not null default '0';
alter table user add  withdraw_shi_dou  smallint(10) not null default '0';

/*2013-4-14*/
/*
* 用户购买记录
*/
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `goods_id`           int(10) NOT NULL ,
  `num_iid`             bigint(10) NOT NULL ,
  `nick`               varchar(32),
  `title`              varchar(128),
  `location`           varchar(32),
  `pic`                   varchar(128),
  `price`                     varchar(16),
  `withdraw_rate`         smallint(10) ,
  `credits`            smallint ,
  `status`          tinyint not null default '0',
  `volume`          varchar(16) ,
  `pay_time`          timestamp,
  `create_time`          timestamp,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/* 用户提现表
* withdraw_type 0 购物返利 1食豆兑换 2 邀请有奖
* relative_id      宝贝ID   邀请用户Id
*/
DROP TABLE IF EXISTS `user_withdraw`;
CREATE TABLE `user_withdraw` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `num`                 smallint(10) ,
  `user_order_id`           int(10) ,
  `handle_result`      varchar(128),
  `handle_status`          tinyint not null default '0',
  `note`    varchar(200) ,
  `withdraw_time`          timestamp,
  `handle_time`          timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/* 淘宝客报表记录*/
DROP TABLE IF EXISTS `taobaoke_income`;
CREATE TABLE `taobaoke_income` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `num_iid`          bigint(10) ,
  `outer_code`          varchar(32) ,
  `trade_id`           int(10) NOT NULL ,
  `real_pay_fee`       varchar(16) ,
  `commission_rate`    smallint(10) ,
  `commission`          smallint(10) ,
  `create_time`               timestamp,
  `pay_time`                   timestamp,
  `add_time`          timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*4月22日*/
/*用户邀请有奖*/
DROP TABLE IF EXISTS `user_invite_prize`;
CREATE TABLE `user_invite_prize` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) not null ,
  `invitee_id`         int(10) not null,
  `invitee_credits`      smallint(10) ,
  `num`                    tinyint (4) ,
  `handle_time`          timestamp,
  `create_time`          timestamp,
  `handle_result`    varchar(128) default '' ,
  `handle_status`          tinyint not null default '0',
  `note`    varchar(200) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
/* 食豆兑换申请 */
DROP TABLE IF EXISTS `user_exchange_shiDou`;
CREATE TABLE `user_exchange_shiDou` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `apply_id`                 int(10) ,
  `num`              smallint (10) ,
  `handle_time`          timestamp,
  `apply_time`          timestamp,
  `handle_result`    varchar(128) default '',
  `handle_status`          tinyint not null default '0',
  `note`    varchar(200) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

alter table user_profile change invite_id invite_id int(10) default '0'