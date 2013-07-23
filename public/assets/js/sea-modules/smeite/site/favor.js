/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-6-22  上午10:22
 * ModifyTime :
 * ModifyContent:  for  site scala html
 * http://www.smeite.com/
 *
 */
define(function(require, exports){
    var $  = require("$");
    var judgeSiteFollowState=function(sid,$elm){
        $.ajax({
            url:"/site/checkSiteLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"sid": sid }),
            success: function(data){
                if(data.code=="100"){
                    $elm.removeClass("follow-btn").addClass("followed-btn").text("已关注");
                }
            }
        });

    }

    $(function(){
        /*先判断状态 */
        if(SMEITER.userId !=""){
            $("a[rel=followSite]").each(function(){
                var id =$(this).data("sid")
                judgeSiteFollowState(id,$(this))
            })
        }
        /* 主题关注操作*/
        $("a[rel=followSite]").on("click",function(){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
            var $this = $(this);
            var sid = $this.data("sid");

            var $likeCount =$("#J_siteMember");

            $.smeite.favor.repeatLoveSiteClk = function(o){
                o.data("enable","enable");
                if($("#cmtDialog")[0]){
                    $("#cmtDialog").remove();
                    clearTimeout(parseInt(o.data("timeout"),10));
                }
                var html = "";
                html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
                html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;已经加入了~</p>';

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

                $this.removeClass("follow-btn").addClass("followed-btn").text("已关注");
            }

            $.smeite.favor.loveSiteCallback = function(o){
                o.data("enable","enable");
                //主题频道页中可同时喜欢多个主题
                if($("#cmtDialog")[0]){
                    $("#cmtDialog").remove();
                    clearTimeout(parseInt(o.data("timeout"),10));
                }
                var html = "";
                html += '<div id="cmtDialog" class="c-dialog">';
                html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>加入了~</p>';

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
                $this.removeClass("follow-btn").addClass("followed-btn").text("已关注");
            }


            $.smeite.favor.loveSiteSubmit($this, parseInt(sid));

        });

        /* 取消主题关注操作*/
        $("a[rel=removeSiteFollow]").click(function(){
            var sid =$(this).data("sid"),
                $siteItem = $(this).parents(".followed-btn");
            $.smeite.favor.removeSiteCallback($siteItem,parseInt(sid));
        })


    })


});