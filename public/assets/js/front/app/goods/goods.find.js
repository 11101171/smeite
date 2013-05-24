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

    if(location.href.indexOf("find")!=-1){
        $(".goods-pic img").lazyload({
            effect:"fadeIn",
            threshold : 200,
            load:function(img){
                var h = img.height;
                var w = img.width;
                var small = 210;
                $(this).parent().css({
                    position:'absolute',
                    height:h,
                    width:w,
                    top:(small-h)/2,
                    left:(small-w)/2
                })
            }
        });
    }else{
        $(".goods-pic img").lazyload({effect:"fadeIn"});
    }



    $(function () {


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