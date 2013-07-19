/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 主题关注、取消关注
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */
define(function(require, exports){
	var $  = require("$");
    var judgeThemeFollowState=function(themeId,$elm){
        $.ajax({
            url:"/theme/checkThemeLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"themeId": themeId }),
            success: function(data){
                if(data.code=="100"){
                    $elm.removeClass("follow").addClass("followed").text("已关注");
                }
            }
        });

    }

    $(function(){
        /*先判断状态 */
        if(SMEITER.userId !=""){
            $("a[rel=followTheme]").each(function(){
                var id =$(this).data("themeid")
                judgeThemeFollowState(id,$(this))
            })
        }
        /* 主题关注操作*/
        $("a[rel=followTheme]").on("click",function(){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
            var $this = $(this);
            var themeId = $this.data("themeid");
            var cmtHref ='/theme/'+ themeId +'#J_cmtForm';
            var cmtTarget = "";
            var $likeCount;
            //在主题频道页面
            if(window.location.href.indexOf(themeId) == -1){
                cmtTarget = 'target="_blank"';
                $likeCount = $this.parent().find(".J_LikeCount:first");
            }else{
                $likeCount = $("#J_LikeCount");
            }

            $.smeite.favor.repeatLoveThemeClk = function(o){
                o.data("enable","enable");
                if($("#cmtDialog")[0]){
                    $("#cmtDialog").remove();
                    clearTimeout(parseInt(o.data("timeout"),10));
                }
                var html = "";
                html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
                html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;关注过了~</p>';
                html += '<a class="sbl-btn speakmore" ' + cmtTarget + ' href="' + cmtHref + '">再说两句</a>'
                html += '</div>';
                //console.log("repeat:" + html);
                $("body").append(html);
                var position = $.smeite.util.getPosition(o).topMid();
                var W = $("#cmtDialog").outerWidth(),
                    H = $("#cmtDialog").outerHeight();
                $("#cmtDialog").css({
                    left: position.x - W/2 + "px",
                    top: position.y - H + 80+ "px"
                }).fadeIn();
                o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

                $this.removeClass("follow").addClass("followed").text("已关注");
            }

            $.smeite.favor.loveThemeCallback = function(o){
                o.data("enable","enable");
                //主题频道页中可同时喜欢多个主题
                if($("#cmtDialog")[0]){
                    $("#cmtDialog").remove();
                    clearTimeout(parseInt(o.data("timeout"),10));
                }
                var html = "";
                html += '<div id="cmtDialog" class="c-dialog">';
                html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>关注了~</p>';
                html += '<a class="sbl-btn speakmore" ' + cmtTarget + ' href="' + cmtHref + '">去说两句</a>'
                html += '</div>';
                //console.log("no-repeat:" + html);
                $("body").append(html);
                var position = $.smeite.util.getPosition(o).topMid();
                var W = $("#cmtDialog").outerWidth(),
                    H = $("#cmtDialog").outerHeight();
                $("#cmtDialog").css({
                    left: position.x - W/2 + "px",
                    top: position.y - H +80 + "px"
                }).fadeIn();
                var newCount = parseInt($likeCount.text(),10) + 1;
                $likeCount.text(newCount);
                o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));
                $this.removeClass("follow").addClass("followed").text("已关注");
            }
            $.smeite.favor.loveThemeSubmit($this, parseInt(themeId));

        });

        /* 取消主题关注操作*/
        $("a[rel=removeThemeFollow]").click(function(){
            var themeId =$(this).data("themeid"),
                $themeItem = $(this).parents(".followed-btn");
            $.smeite.favor.removeThemeCallback($themeItem,parseInt(themeId));
        })


    })


});