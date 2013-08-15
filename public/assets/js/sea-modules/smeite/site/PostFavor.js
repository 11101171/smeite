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
     var judgeFollowPostState=function(pid,o){
        $.ajax({
            url:"/post/checkPostLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"pid": pid }),
            success: function(data){
                if(data.code=="100"){
                    var html ="<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowTopic' href='javascript:;'data-title="+o.data("title")+" data-id='"+ pid +"'>取消</a></div>"
                    o.replaceWith(html)
                }
            }
        });

    }
     $(function(){
         /*先判断状态 */
        /* if(SMEITER.userId !=""){
             $("a[rel=followPost]").each(function(){
                 var id =$(this).data("id")
                 judgeFollowPostState(id,$(this))
             })
         }*/

         $(document).on("click","a[rel=followPost]",function(){
             if($.smeite.dialog.isLogin()){

                var $this = $(this);
                 var pid = $this.data("id")
                 $.ajax({
                     url:"/post/addFollow",
                     type : "post",
                     contentType:"application/json; charset=utf-8",
                     dataType: "json",
                     data: JSON.stringify({"pid": pid }),
                     success: function(data){
                         switch(data.code){
                             case("100"):
                                 //   $this.data("enable","true");
                                 $.smeite.addFollowPostCallback(data,$this);
                                 break;
                             case("104"):
                                 //    $this.data("enable","true");
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.tip.show($this,"参数错误");
                                 break;
                             case("103"):
                                 //     $this.data("enable","true");
                                 $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                 $.smeite.repeatFollowPostCallback(data,$this);
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
         $(document).on("click","a[rel=removeFollowPost]",function(){

             if($.smeite.dialog.isLogin()){
                 //  if($(this).data("enable")=="false") return;
                 //   $(this).data("enable","false");
                 var $this = $(this)
                 var pid = $this.data("id")
                 var txt = "确定不再关注“"+$this.data("title")+"”？";
                 ConfirmBox.confirm(txt, '亲，你确信不要我了吗？', function() {

                     $.ajax({
                         url:"/post/removeFollow",
                         type : "post",
                         contentType:"application/json; charset=utf-8",
                         dataType: "json",
                         data: JSON.stringify({"pid": pid }),
                         success: function(data){
                             switch(data.code){
                                 case("100"):
                                     // $this.data("enable","true");
                                     $.smeite.removeFollowPostCallback(data,$this);
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

         $.smeite.addFollowPostCallback = function(data,o){
             var $likeCount = o.next(".like-num").find(".J_FavorNum");

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
             var  position = $.smeite.util.getPosition(o).topMid();
             var  W = $("#J_LikeDialog").outerWidth();
             var H = $("#J_LikeDialog").outerHeight();
             $("#J_LikeDialog").css({left:position.x - W / 2 + "px", top:position.y - H  + "px"}).fadeIn();
             setTimeout(function () {
                 $("#J_LikeDialog").fadeOut()
             }, 3E3);


         }
         $.smeite.removeFollowPostCallback = function(data,o){
             var $likeCount = o.next(".like-num").find(".J_FavorNum");
             var  loveNum = parseInt($likeCount.data("val")) - 1;
             $likeCount.text(loveNum);
             $likeCount.data("val", loveNum);


         }
         $.smeite.repeatFollowPostCallback = function(data,o){
             $("#cmtDialog")[0] && (clearTimeout(e), $("#cmtDialog").remove());
             var html='<div id="cmtDialog" class="c-dialog" style="width:150px;">' +
                 '<p class="title clearfix">' +
                 '<a class="cmtclose fr" href="javascript:;">x</a>' +
                 '>_<喜欢过了~</p>' +
                 "</div>";
             $("body").append(html);
             var  position = $.smeite.util.getPosition(o).topMid();
             var  W = $("#cmtDialog").outerWidth();
             var H = $("#cmtDialog").outerHeight();
             $("#cmtDialog").css({left:position.x - W / 2 + "px", top:position.y - H  + "px"}).fadeIn();
             e= setTimeout(function () {
                 $("#cmtDialog").fadeOut()
             }, 3E3);
         }
     })

});