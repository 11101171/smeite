/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:   跟用户登陆 相关的东西
 * @depends:
 * Includes:
 * Since: 13-5-2  下午1:46
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    var Cookie = require("module/cookie")
    // 判断reffer来源,默认是 qq
    var refererUrl=document.referrer
    var referer="smeite"
    if(refererUrl.indexOf("qq")>0)referer ="qq"
    if(refererUrl.indexOf("taobao")>0 || refererUrl.indexOf("tmall")>0)referer ="taobao"
    if(refererUrl.indexOf("weibo")>0) referer="weibo"
    function addToFav(o){
        var url = "http://smeite.com";
        var title = "食美特，爱美食爱生活";
        if (window.sidebar) { // Mozilla Firefox Bookmark
            window.sidebar.addPanel(title, url,"");
        } else if( window.external&&document.all) { // IE Favorite
            window.external.AddFavorite( url, title);
        } else if(window.opera) { // Opera 7+
            return false; // do nothing
        } else {
            jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
            jQuery.smeite.tip.show($(o),"您的浏览器不支持自动加收藏，请按 ctrl+d 加入收藏。");
        }
    }

    //第三方登录优先级
    var loginNavArr = [
        {"referer":"qq", "snsType":"qzong"},//4
        {"referer":"weibo", "snsType":"sina"},//3
        {"referer":"taobao", "snsType":"taobao"}//8
    ]
    var RefererGuide = function(curReferer, snsType){
        if(!$(".guide")[0]){
            var otherLoginArr = $.grep(loginNavArr, function(value, index){
                return (value.referer != curReferer);
            });
            var otherLoginHtml = "";
            $.map(otherLoginArr, function(value, index){
                otherLoginHtml += '<li><a target="_blank" class="shortcut-login-' + value.referer + '" href="http://smeite.com/user/snsLogin?snsType=' + value.snsType + '">' + value.referer + '</a></li>';
            });
            var HTML = '<div class="guide guide-' + curReferer + '">'
                +'<div class="guide_boby">'
                +'<p><i></i>登录 smeite.com ，发现你的喜欢！<span><a href="/user/regist">注册帐号 ></a></span></p>'
                +'<div class="guide_add">'
                +'<a id="J_RefererLogin" class="referer-login" target="_blank" href="http://smeite.com/user/snsLogin?snsType=' + snsType + '">' + curReferer + '</a>'
                +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                +'<ul class="other-login">' + otherLoginHtml + '</ul>'
                +'</div>'
                +'<div class="del" id="J_CloseGuide">'
                +'<a href="javascript:void(0);">关闭引导</a>'
                +'</div>'
                +'</div>'
                +'</div>';
            $("body").append(HTML);
            $("#J_Favorites").click(function(){
                addToFav(this);
            })
            $("#J_CloseGuide").click(function(){
                Cookie.set("refererGuide","no")    // 点击取消之后，不在出现
                $(".guide").hide();
            })
        }
        var posGuide = function(){
            if($.smeite.util.isIE6()){
                var windowtop = $(window).height()-90;
                $(".guide").css({
                    position:"absolute",
                    top:windowtop
                }).show();
                $(window).bind("scroll",function(){
                    var docScrollTop = $(document).scrollTop();
                    if ($.smeite.util.isIE6()) {
                        $(".guide").css("top", (docScrollTop+windowtop)+"px")
                    }
                });
            }else{
                $(".guide").show().animate({
                    bottom:0
                },500);
            }
        }
        var href = window.location.href;
        if(href.indexOf("/user/login")==-1 && href.indexOf("/user/doEmailLogin")==-1&& href.indexOf("/user/regist")==-1){
            if("no" != Cookie.get("refererGuide") && referer !="smeite"){
                posGuide();
            }
        }

    }

    //不登陆的用户会出现分享条 并且根据referer 引导登录
    if(SMEITER.userId == "" && typeof referer != 'undefined'){
    switch(referer){
        case "smeite":
            break;
        case "taobao" : {
            RefererGuide("taobao", "taobao");
        }break;
        case "weibo" : {
            RefererGuide("weibo", "sina");
        }break;
        case "qq": {
            RefererGuide("qq", "qzone");
        }break;
        /*default : {
         RefererGuide("qq", "qzone");
         }*/
    }
}else{
    $(window).bind("scroll",function(){
        var showguide = function(){
            var win = $(window);
            var HTML = "";
            if($(".guide").length!==1){
                HTML = '<div class="guide guide-share">'
                    +'<div class="guide_boby">'
                    +'<p>如果你喜欢食美特，就把Smeite.com加入收藏夹吧，或者分享给你的朋友~</p>'
                    +'<div class="guide_add" style="margin-right:174px;">'
                    +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                    +'<div class="weibo">'
                    +'<a href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://smeite.com/assets/ui/smeite.jpg\';window.open(\'http://service.t.sina.com.cn/share/share.php?appkey=2610725727&title=\'+title+\'&pic=\'+pic);})())" alt="分享到新浪微博">分享到新浪微博</a>'
                    +'<a style="margin-left:6px; width:130px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://smeite.com/assets/ui/smeite.jpg\';window.open(\'http://v.t.qq.com/share/share.php?appkey=db0de5e94b314972b3e7efd23fa7ce1e&title=\'+title+\'&pic=\'+pic+\'&site=\'+link);})())" alt="分享到腾讯微博"></a>'
                    +'<a style="margin-left:6px; width:118px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);window.open(\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=\'+link);})())" alt="分享到QQ空间"></a>'
                    +'</div>'
                    +'</div>'
                    +'<div class="del" id="J_CloseGuide">'
                    +'<a href="javascript:void(0);">关闭引导</a>'
                    +'</div>'
                    +'</div>'
                    +'</div>';
                $("body").append(HTML);

                $("#J_Favorites").click(function(){
                    addToFav(this);
                })
                $("#J_CloseGuide").click(function(){
                    // 用户取消后，cookie 记录，为了保证不在出现，扰民
                    Cookie.set("showGuide","no")
                    $(".guide").hide();
                })
            }
            if($.smeite.util.isIE6()){
                $(".guide").css({
                    position:"absolute",
                    top:win.scrollTop()+win.height()-90
                })
                if(win.scrollTop()>800){
                    $(".guide").show();
                }
            }else{

                if(win.scrollTop()>800){

                    $(".guide").show().animate({
                        bottom:0
                    },500);
                }
            }
        }
        /*如果用户没有点击取消 则显示 引导*/
        if("no" != Cookie.get("showGuide")){
            showguide();
        }
    });
}
//同步授权登录后关注弹出层
window.followSmeite = function(code,msg,site,flag,refresh){
    if(code==444){
        alert(msg);
        return false;
    }
    if((site!="sina" && site!="qzone" ) || flag=="true" ){
        if(refresh){
            window.location.reload();
        }
        return false;
    }
    var bdClass = "",
        frameHtml = "";
    if(site=="sina"){
        bdClass = "sinaBd";
        frameHtml = '<iframe width="63" height="24" frameborder="0" allowtransparency="true" marginwidth="0" marginheight="0" scrolling="no" border="0" src="http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=63&height=24&uid=1283431903&style=1&btn=red&dpc=1"></iframe>';
    }else if(site=="qzone"){
        bdClass = "qzoneBd";
        frameHtml = '<iframe src="http://open.qzone.qq.com/like?url=http%3A%2F%2Fuser.qzone.qq.com%2F1469909930&type=button&width=400&height=30&style=2" allowtransparency="true" scrolling="no" border="0" frameborder="0" style="width:65px;height:30px;border:none;overflow:hidden;"></iframe>';
    }

    if(!$("#followDialog")[0]){
        var html = '<div id="followDialog" class="g-dialog">';
        html +=	'<div class="dialog-content">';
        html +=	'<div class="hd"><h3></h3></div>';
        html +=	'<div class="bd clearfix '+bdClass+'">';
        html +=	'<div class="btnFrame">';
        html +=	frameHtml;
        html +=	'</div>';
        html +=	'</div>';
        html +=	'<i></i>';
        html +=	'<label><input type="checkbox" class="check" name="noMore" />不再提示</label>';
        html +=	'<a class="close" href="javascript:;"></a>';
        html +=	'</div>';
        html +=	'</div>';
        $("body").append(html);
        if($("#loginDialog:visible")[0]){
            $("#loginDialog").empty().remove();
            $("#exposeMask").empty().remove();
        }
        $("#followDialog").overlay({
            top: 'center',
            mask: {
                color: '#000',
                loadSpeed: 200,
                opacity: 0.3
            },
            closeOnClick: false,
            load: true
        });
        $("#followDialog").overlay().getClosers().bind("click",function(){
            if($("input[name=noMore]")[0].checked){
                Cookie.set("noMoreTip","n");
            }
            if(refresh){
                window.location.reload();
            }
        });
    }
}
/*异步授权登陆后*/
window.refresh=function(){
    window.location.reload();
}

    /* 顶部固定： 如果是find 页面 则 fix tag nav，否则 显示 #nav fixed */
    var href = window.location.href;
  //  if(href.indexOf("/find")==-1){
   //     $(window).bind("scroll",function(){
    //        var docScrollTop = $(document).scrollTop();
            //IOS平台如iphone、ipad、ipod不执行导航滚动
    ///        if(!$.smeite.util.isIOS()){
    //            if(docScrollTop >0){
    //                $("#top").addClass("fixed")

    //            }else{
    //                $("#top").removeClass("fixed")

    //            };
  //          }
   //     });
 //   }else{
    /*    $(window).bind("scroll",function(){
            var docScrollTop = $(document).scrollTop();
            //IOS平台如iphone、ipad、ipod不执行导航滚动
            if(!$.smeite.util.isIOS()){
                if(docScrollTop >123){
                    $("#J_tags").addClass("tag-list-fixed")
                }else{
                    $("#J_tags").removeClass("tag-list-fixed");
                }
            }
        });*/
  //  }


})