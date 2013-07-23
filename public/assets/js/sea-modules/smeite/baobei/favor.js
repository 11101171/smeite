/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 宝贝喜欢操作
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-26 22:00
 * ModifyContent:删除注释 增加goods瀑布流显示
 * http://www.smeite.com/
 *
 */
define(function(require, exports){
    var $  = require("$");
$("a[rel=followGoods]").on("click",function(){
    if(!$.smeite.dialog.isLogin()){
        return false;
    }
    var $this = $(this);
    var productId = $this.attr("data-proid");
    var $likeCount = $this.next(".like-num").find(".J_FavorNum");

    $.smeite.favor.repeatLoveBaobeiClk = function (b) {
        $("#cmtDialog")[0] && (clearTimeout(e), $("#cmtDialog").remove());
        var html='<div id="cmtDialog" class="c-dialog" style="width:150px;">' +
            '<p class="title clearfix">' +
            '<a class="cmtclose fr" href="javascript:;">x</a>' +
            '>_<喜欢过了~</p>' +
            "</div>";
        $("body").append(html);
      var  b = $.smeite.util.getPosition(b).topMid();
      var  c = $("#cmtDialog").outerWidth();
        var f = $("#cmtDialog").outerHeight();
        $("#cmtDialog").css({left:b.x - c / 2 + "px", top:b.y - f  + "px"}).fadeIn();
      e= setTimeout(function () {
            $("#cmtDialog").fadeOut()
        }, 3E3);

    };
    $.smeite.favor.loveBaobeiCallback = function (b) {

       $("#J_LikeDialog")[0] && (clearTimeout(e), $("#J_LikeDialog").remove());
            var  html = ('<div id="J_LikeDialog" class="c-dialog" style="width:150px;">' +
                '<p class="title clearfix">' +
                '<a class="cmtclose fr" href="javascript:;">x</a>' +
                '喜欢了~</p>' +
                '</div>');
            $("body").append(html);
        /*修改喜欢的数量*/
        var  loveNum = parseInt($likeCount.data("val")) + 1;
        $likeCount.text(loveNum);
        $likeCount.data("val", loveNum);
       /*dialog 显示*/
      var  b = $.smeite.util.getPosition(b).topMid();
      var  c = $("#J_LikeDialog").outerWidth();
        var f = $("#J_LikeDialog").outerHeight();
        $("#J_LikeDialog").css({left:b.x - c / 2 + "px", top:b.y - f  + "px"}).fadeIn();
       setTimeout(function () {
            $("#J_LikeDialog").fadeOut()
        }, 3E3);

    };
    $.smeite.favor.loveBaobeiSubmit($this, parseInt(productId));
})
});