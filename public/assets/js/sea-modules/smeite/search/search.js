/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-7-23  下午3:56
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function (require, exports) {
    var $  = require("$");

    $(function () {


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