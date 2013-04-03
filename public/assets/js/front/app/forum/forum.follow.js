/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: topic关注、取消关注
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */
define(function(require, exports) {
    var $ = jQuery = require("jquery");
    require("app/smeite.favor");
	


    /* 关注 topic*/
    $(".follow-topic").live("click",function(){
        if(!$.smeite.dialog.isLogin()){
            return false;
        }
        var $this = $(this);
        if($this.data("enable") == "disable"){
            return;
        }

        $this.data("enable","disable");
        setTimeout(function(){
            $this.data("enable","enable");
        },1000);
        var topicId = $this.data("topicid");
        var cmtHref ='/topic/'+ topicId +'/#J_PostCmtArea';
        var cmtTarget = "";
        var $likeCount;

        //在主题频道页面
        if(window.location.href.indexOf(topicId) == -1){
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
                top: position.y - H - 12 + "px"
            }).fadeIn();
            o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

            $this.text("已关注");
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
            html += '<a class="sbl-btn speakmore" ' + cmtTarget + ' href="' + cmtHref + '">再说两句</a>'
            html += '</div>';
            //console.log("no-repeat:" + html);
            $("body").append(html);
            var position = $.smeite.util.getPosition(o).topMid();
            var W = $("#cmtDialog").outerWidth(),
                H = $("#cmtDialog").outerHeight();
            $("#cmtDialog").css({
                left: position.x - W/2 + "px",
                top: position.y - H - 12 + "px"
            }).fadeIn();
            var newCount = parseInt($likeCount.text(),10) + 1;
            //console.log(parseInt($likeCount.text(),10))
            $likeCount.text(newCount);
            //$likeCount.data("val",newCount);
            o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

            $this.text("已关注");
        }
        $.smeite.favor.loveThemeSubmit($this, parseInt(themeId));

    });
    /* 取消topic */
    $(".unfollow-topic").click(function(){
        alert("hello")
        var topicId =$(this).data("topicid"),
            $topicItem = $(this).parents(".topic-item");
        $.smeite.confirmUI("真要取消关注这个主题吗？亲，你不在喜欢我了嘛...", $.smeite.favor.removeTopicCallback($topicItem,parseInt(topicId)),function(){

        });
    })
});