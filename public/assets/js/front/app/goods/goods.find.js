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
    require('module/lazyload')($);
    $(function () {
      //  $(".goods-pic img").lazyload({effect:"fadeIn"});
        "ipad" != navigator.userAgent.toLowerCase().match(/ipad/i) ? $(".goods-pic img").lazyload({effect: "fadeIn", threshold: 200, load: function (a) {
            var b = a.height, a = a.width;
            $(this).parents(".big").length ? $(this).parents(".big").hasClass("bigB") ? ($(this).css({width: 210, height: b * (210 / a)}), $(this).parent().css({position: "absolute", height: b * (210 / a), width: 210, top: (210 - b * (210 / a)) / 2, left: 0})) : ($(this).css({width: a, height: b}), $(this).parent().css({position: "absolute", height: b, width: a, top: (460 - b) / 2, left: (460 -
                a) / 2})) : ($(this).css({width: a, height: b}), $(this).parent().css({position: "absolute", height: b, width: a, top: (210 - b) / 2, left: (210 - a) / 2}))
        }}) : $(".goods-pic img").each(function (a) {
            var b = $(this);
            19 < a ? setTimeout(function () {
                b.attr("src", b.data("original"))
            }, 3E3) : b.attr("src", b.data("original"))
        });
        /*当鼠标移到goods上的效果*/
       /* $(".goods").hover(function () {
            var $this = $(this);
            $this.addClass("cur-goods");
            var goodsClone = $this.find(".goods-clone");
            1 > goodsClone.length && (goodsClone = $this.html(), $this.append('<div class="goods-clone">' + goodsClone + "</div>"), goodsClone = $this.find(".goods-clone"));
            goodsClone.fadeIn("fast");
            goodsClone.slideDown("slow");
         //   +"\v1" || $this.css({border: "1px #dedede solid"});
            $this.find(".ilike-m")[0] && $this.find(".ilike-m").show();
            $this.find(".ilike-del")[0] && $this.find(".ilike-del").show();
            $this.find(".ilike-topic")[0] && $this.find(".ilike-topic").show()
        }, function () {
            var $this = $(this);
            $this.removeClass("cur-goods");
            var goodsClone = $this.find(".goods-clone");
            1 == goodsClone.length && (goodsClone.fadeOut("fast"), goodsClone.slideUp("slow"), +"\v1" || goodsClone.css({border: "1px #fff solid"}));
            $this.find(".ilike-m")[0] && $this.find(".ilike-m").hide();
            $this.find(".ilike-del")[0] && $this.find(".ilike-del").hide();
            $this.find(".ilike-topic")[0] && $this.find(".ilike-topic").hide()
        });*/
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
            a.find(".ilike-del")[0] && a.find(".ilike-del").show();
            a.find(".ilike-topic")[0] && a.find(".ilike-topic").show()
        }, function () {
            var a = $(this);
            a.removeClass("cur-goods");
            var b = a.find(".goods-clone");
            1 == b.length && (b.fadeOut("fast"), b.slideUp("slow"));
            a.find(".ilike-m")[0] && a.find(".ilike-m").hide();
            a.find(".ilike-del")[0] && a.find(".ilike-del").hide();
            a.find(".ilike-topic")[0] && a.find(".ilike-topic").hide()
        });
        var c = window.location.href;
        -1 != c.indexOf("find") && 2 < c.split("find")[1].split("/").length && ($("#J_GoodsTitle").offset(), $("html, body").animate({scrollTop: 93}, 120));

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
        c = $(".section:last");
        5 >= c.find(".goods").length && c.height(660)
    });


});