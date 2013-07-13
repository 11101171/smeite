/*
 全站模块启动js
 * Created by zuosanshao.
 * User: Administrator
 * Date: 12-10-22
 * Time: 下午8:58
 * Email:zuosanshao@qq.com
 * @contain: 前端基础功能插件
 * @depends: jquery.js
 * Since: 2011-11-07
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
define(function (require,exports) {
    var $= require("$");
    var Slide = require("slide")
    var Tabs = require("tabs")
    $(function(){
        /* index flash  */
       var indexFlash = new Slide({
            element: '#J_indexFlash',
            panels: '#J_indexFlash .scrollable .items .item',
            triggers:"#J_indexFlash .navi li",
           activeTriggerClass:"active",
            effect: 'fade',
            easing: 'easeOutStrong',
            interval: 2000
        }).render();
        /* 最近好玩意～*/
        $("#J_A-weixin").hover(function () {
            $(this).find("#J_Weixin").fadeIn(300)
        }, function () {
            $(this).find("#J_Weixin").fadeOut(200)
        });
       /* 品牌宣传 */
        var brandTabs  = new Tabs({
            element: '#J_brands',
            triggers: '#J_brands .tabs h3',
            panels: '#J_brands .panes ul',
            activeIndex: 0,
            activeTriggerClass:"current",
            effect: 'fade'
        }).render();

        var advertsSlide = new Slide({
            element: '#J_adverts',
            effect: 'scrollx',
            interval: 2000
        })

        /* 这里需要调整todo  */
        SMEITER.userId.length ? ($("#J_legend").hide(), $("#J_welcome").html('<div class="welcome"><a class="xihuan" href="http://smeite.com/faxian/0" title="发现.喜欢"></a><a class="zhuti" href="http://smeite.com/themes" title="逛主题"></a><a class="pingpai" href="http://smeite.com/sites" title="享特色"></a><a class="fenxiang" href="http://smeite.com/cookbook" title="分享,收获"></a><a class="home" href="http://smeite.com/user/'+SMEITER.userId+'" title="我的小窝~"></a></div>')) : $(".thirdLogin a").click(function () {
            var c = $(this).attr("href");
            jQuery.smeite.util.openWin(c);
            return!1
        });


    })
});
