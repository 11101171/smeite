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
    var $ = require("$");
     var judgeTopicFollowState=function(topicId,$elm){
        $.ajax({
            url:"/forum/checkTopicLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"topicId": topicId }),
            success: function(data){
                if(data.code=="100"){
                    var html ="<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeTopicFollow' href='javascript:;' data-topicid='"+ topicId +"'>取消</a></div>"
                    $elm.replaceWith(html)
                }
            }
        });

    }
     $(function(){
         /*先判断状态 */
         if(SMEITER.userId !=""){
             $("a[rel=topicFollow]").each(function(){
                 var id =$(this).data("topicid")
                 judgeTopicFollowState(id,$(this))
             })
         }

         /* 关注 topic*/
         $("a[rel=topicFollow]").on("click",function(){
             if(!$.smeite.dialog.isLogin()){
                 return false;
             }
             var $this = $(this);
             var topicId = $this.data("topicid");
             $.smeite.favor.repeatLoveTopicClk = function(o){
                     o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeTopicFollow' data-followtype='1' data-topictitle="+o.data("topictitle")+" data-topicid='"+o.data("topicid")+"' href='javascript:;'>取消</a></div>")
                     o.remove();
             }
             $.smeite.favor.loveTopicCallback = function(o){
                     o.after("<div class='unfollow-topic followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeTopicFollow' data-followtype='1' data-topictitle="+o.data("topictitle")+" data-topicid='"+o.data("topicid")+"' href='javascript:;'>取消</a></div>")
                     o.remove();
             }

             $.smeite.favor.loveTopicSubmit($this, parseInt(topicId));

         });
         /* 取消topic */
         $("a[rel=removeTopicFollow]").on("click",function(){
             var topicId =$(this).data("topicid");
             $.smeite.favor.removeTopicCallback($(this).parent("div"),parseInt(topicId))
         //    $.smeite.confirmUI("真要取消关注这个话题吗？亲，你不在喜欢我了嘛...", $.smeite.favor.removeTopicCallback($(this).parent("div"),parseInt(topicId)),function(){ });
         })

     })

});