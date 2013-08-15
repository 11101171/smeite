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
    var ConfirmBox = require("confirmbox")
     var judgeFollowTopicState=function(topicId,o){
        $.ajax({
            url:"/forum/checkTopicLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"topicId": topicId }),
            success: function(data){
                if(data.code=="100"){
                    var html ="<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowTopic' href='javascript:;'data-title="+o.data("title")+" data-id='"+ topicId +"'>取消</a></div>"
                    o.replaceWith(html)
                }
            }
        });

    }
     $(function(){
         /*先判断状态 */
         if(SMEITER.userId !=""){
             $("a[rel=followTopic]").each(function(){
                 var id =$(this).data("id")
                 judgeFollowTopicState(id,$(this))
             })
         }

         $(document).on("click","a[rel=followTopic]",function(){
             if($.smeite.dialog.isLogin()){
                 //  if($(this).data("enable")=="false") return;
                 //  $(this).data("enable","false");
               var  $this = $(this);
                 var topicId = $this.data("id")
                 $.ajax({
                     url:"/forum/addFollow",
                     type : "post",
                     contentType:"application/json; charset=utf-8",
                     dataType: "json",
                     data: JSON.stringify({"topicId": topicId }),
                     success: function(data){
                         switch(data.code){
                             case("100"):
                                 //   $this.data("enable","true");
                                 $.smeite.addFollowTopicCallback(data,$this);
                                 break;
                             case("104"):
                                 //    $this.data("enable","true");
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.tip.show($this,"参数错误");
                                 break;
                             case("103"):
                                 //     $this.data("enable","true");
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.repeatFollowTopicCallback(data,$this);
                                 break;
                             case("111"):
                                 //    $this.data("enable","true");
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.tip.show($this,"抱歉，你的关注已达上限。");
                                 break;
                             case("300"):
                                 $.smeite.dialog.login();
                                 break;
                             case("444"):
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.tip.show($this,"你已被禁止关注好友！");
                         }
                     }
                 });
             }
         })

         // 取消关注
         $(document).on("click","a[rel=removeFollowTopic]",function(){

             if($.smeite.dialog.isLogin()){
                 //  if($(this).data("enable")=="false") return;
                 //   $(this).data("enable","false");
                 var $this = $(this)
                 var topicId = $this.data("id")
                 var txt = "确定不再关注“"+$this.data("title")+"”？";
                 ConfirmBox.confirm(txt, '亲，你确信不要我了吗？', function() {

                     $.ajax({
                         url:"/forum/removeFollow",
                         type : "post",
                         contentType:"application/json; charset=utf-8",
                         dataType: "json",
                         data: JSON.stringify({"topicId": topicId }),
                         success: function(data){
                             switch(data.code){
                                 case("100"):
                                     // $this.data("enable","true");
                                     $.smeite.removeFollowTopicCallback(data,$this);
                                     break;
                                 case("104"):
                                     //   $this.data("enable","true");
                                     $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                     $.smeite.tip.show($this,"参数错误");
                                     break;
                                 case("300"):
                                     $.smeite.dialog.login();
                                     break;
                             }
                         }
                     });
                 });

             }
         })

         $.smeite.addFollowTopicCallback = function(data,o){
             o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowTopic'data-title="+o.data("title")+"  data-id='"+o.data("id")+"' href='javascript:;'>取消</a></div>")
             o.remove();


         }
         $.smeite.removeFollowTopicCallback = function(data,o){
             o.parent().after("<a rel='followTopic' href='javascript:;' class='follow-btn' data-title="+o.data("title")+"   data-id="+o.data("id")+">+关注</a>")
             o.parent().remove();


         }
         $.smeite.repeatFollowTopicCallback = function(data,o){
             //  o.data("enable","enable");
             var $cmtDialog = $("#cmtDialog");
             if($cmtDialog[0]){
                 $cmtDialog.remove();
                 clearTimeout(parseInt(o.data("timeout"),10));
             }
             var html = "";
             html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
             html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;已经关注了~</p>';
             html += '</div>';
             $("body").append(html);
             var position = $.smeite.util.getPosition(o).topMid();
             var W = $cmtDialog.outerWidth(),  H = $cmtDialog.outerHeight();
             $cmtDialog.css({
                 left: position.x - W/2 + "px",
                 top: position.y - H + 80+ "px"
             }).fadeIn();
             o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

             $this.removeClass("follow-btn").addClass("followed-btn").text("已关注");
         }
     })

});