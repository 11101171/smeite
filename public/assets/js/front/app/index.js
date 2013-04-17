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
    var $ = jQuery = require("jquery");
    require("module/feedSlider")($);
    require("module/scrollImg")($);
    require("module/lazyload")($);
    require("module/jquery.tools.tabs")($)
    $(function () {
        /*顶部slide show*/
        (function (a) {
            for (var b = $.find("ul"), d = b.children().length, e = 1; e < d; e++)b.find("li:eq(" + (d - 1 - e) + ")").appendTo(b);
            $.feedSlider({direction:"right"})
        })

        $(".scrollable").scrollImg({timer:1E4, startHandle:function (c) {
            c && setTimeout(function () {
                c.playlol(0);
                c.changeClass(0);
                c.autoPlay()
            }, 1E4);
            var b = null;
            $(".navi li").each(function (d) {
                $(this).unbind();
                $(this).hover(function () {
                    b != null && clearTimeout(b);
                    b = setTimeout(function () {
                        c.changeClass(d);
                        c.stopAuto();
                        c.playlol(d)
                    }, 200)
                }, function () {
                    b != null && clearTimeout(b);
                    c.autoPlay()
                })
            })
        }});
     /* 最近好玩意～*/
        $("#J_A-weixin").hover(function () {
            $(this).find("#J_Weixin").fadeIn(300)
        }, function () {
            $(this).find("#J_Weixin").fadeOut(200)
        });
        /*懒加载*/
        $(".category img").lazyload({effect:"fadeIn", threshold:200});
        /* 品牌大全 美食大全*/

            $("div.tabs").tabs(".panes > ul",{
                event: 'mouseover'
            })

        SMEITER.userId.length ? ($("#J_legend").hide(), $("#J_welcome").html('<div class="welcome"><a class="xihuan" href="http://smeite.com/find" title="发现.喜欢"></a><a class="zhuti" href="http://smeite.com/jie" title="逛美食"></a><a class="pingpai" href="http://smeite.com/miss" title="享特色"></a><a class="fenxiang" href="http://smeite.com/square" title="分享,收获"></a><a class="home" href="http://smeite.com/user/' +
            SMEITER.userId + '" title="我的小窝~"></a></div>')) : $(".thirdLogin a").click(function () {
            var c = $(this).attr("href");
            jQuery.smeite.util.openWin(c);
            return!1
        });


    })
});
