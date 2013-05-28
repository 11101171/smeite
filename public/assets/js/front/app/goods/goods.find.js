/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: find 页面操作
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function (require, exports) {
    var $ = jQuery = require("jquery");
  /*  require('module/lazyload')($);*/
    $(function () {
       // $(".goods-pic img").lazyload({effect:"fadeIn", threshold:200});

    /*    "ipad" != navigator.userAgent.toLowerCase().match(/ipad/i) ? $(".goods-pic img").lazyload({effect: "fadeIn", threshold: 200, load: function (a) {
            var b = a.height, a = a.width;
           ($(this).css({width: a, height: b}), $(this).parent().css({position: "absolute", height: b, width: a, top: (210 - b) / 2, left: (210 - a) / 2}))
        }}) : $(".goods-pic img").each(function (a) {
            var b = $(this);
            19 < a ? setTimeout(function () {
                b.attr("src", b.data("original"))
            }, 3E3) : b.attr("src", b.data("original"))
        });*/
        /*当鼠标移到goods上的效果*/
        $(".goods").hover(function () {
            var a = $(this);
            a.addClass("cur-goods");
            var b = a.find(".goods-clone");
            if (1 > b.length) {
                b = a.html();
                a.append('<div class="goods-clone">' + b + "</div>");
                var b = a.find(".goods-clone"), c = b.find(".goods-pic img"),
                    d = c.data("original");
                c.css("opacity", 1);
                c.attr("src", d)
            }
            +"\v1" || b.css({border: "1px #dedede solid"});
            b.fadeIn("fast");
            b.slideDown("slow");
            a.find(".ilike-m")[0] && a.find(".ilike-m").show();

        }, function () {
            var a = $(this);
            a.removeClass("cur-goods");
            var b = a.find(".goods-clone");
            1 == b.length && (b.fadeOut("fast"), b.slideUp("slow"));
            a.find(".ilike-m")[0] && a.find(".ilike-m").hide();

        });

        $(".pagin4faxian .current").hover(function () {
            var b = this;
            this.timeout && clearTimeout(this.timeout);
            this.timeout = setTimeout(function () {
                $(b).prev("ul").fadeIn()
            }, 200)
        }, function () {
            var b = this;
            this.timeout && clearTimeout(this.timeout);
            this.timeout = setTimeout(function () {
                $(b).prev("ul").fadeOut()
            }, 200)
        });
        $(".pagin4faxian ul").hover(function () {
            var b = $(".pagin4faxian .current")[0];
            b.timeout && clearTimeout(b.timeout)
        }, function () {
            $(".pagin4faxian .current")[0].timeout = setTimeout(function () {
                $(".pagin4faxian ul").fadeOut()
            }, 200)
        });

    });


});