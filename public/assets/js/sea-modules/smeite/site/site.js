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
define(function(require, exports) {
    var $  = require("$");
    var ConfirmBox = require("confirmbox")

   var isPermission=function(siteId,o){

       $.ajax({
           url:"/site/checkSitePermission",
           type : "post",
           contentType:"application/json; charset=utf-8",
           dataType: "json",
           data: JSON.stringify({"siteId": siteId }),
           success: function(data){
               switch(data.code){
                   case("100"):
                       permissionState=true
                       break;
                   case("104"):
                       $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                       $.smeite.tip.show(o,"参数错误");
                       break;
                   case("103"):
                       permissionState=false
                       break;
                   case("300"):
                       $.smeite.dialog.login();
                       break;

               }
           }
       });

       return permissionState
   }
    var isFollowed=function(siteId){

        $.ajax({
            url:"/site/checkSiteLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"siteId": siteId }),
            success: function(data){
                switch(data.code){
                    case("100"):
                        followState=true

                        break;
                }
            }
        });

       return followState;
    }
     /* 点击发帖操作*/
    $("#J_postEdit").click(function(){
      var  $this = $(this);

        var siteId =$this.data("siteid");
        var editUrl ="/post/edit?sid="+siteId;
        /* 1 判断用户是否登录，登录 检查checkPermission */
        if($.smeite.dialog.isLogin()){
            /* 如果只允许小镇居民发帖，判断是否加入小镇，没有则加入，跳转到post edit */
           if(!isPermission(siteId,$this)){

             if(!isFollowed(siteId)){
                 var txt = "亲，只有加入 “"+$this.data("sitetitle")+" 小镇才能发帖哦”？";
                 ConfirmBox.confirm(txt, '亲，只有加入小镇才能发帖哦', function() {
                     $.ajax({
                         url:"/site/addFollow",
                         type : "post",
                         contentType:"application/json; charset=utf-8",
                         dataType: "json",
                         data: JSON.stringify({"siteId": siteId }),
                         success: function(data){
                             switch(data.code){
                                 case("100"):

                                     addFollowCallback(data,$this,editUrl);
                                     break;
                                 case("104"):

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


             }else{
                 window.location=editUrl
             }
           }else{
               window.location=editUrl
           }
        }
    })


   var addFollowCallback=function(data,o,editUrl){
       var $cmtDialog = $("#cmtDialog");
       if($cmtDialog[0]){
           $cmtDialog.remove();
           clearTimeout(parseInt(o.data("timeout"),10));
       }
       var html = "";
       html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
       html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;已经加入了~</p>';
       html += '</div>';
       $("body").append(html);
       var position = $.smeite.util.getPosition(o).topMid();
       var W = $cmtDialog.outerWidth(),  H = $cmtDialog.outerHeight();
       $cmtDialog.css({
           left: position.x - W/2 + "px",
           top: position.y - H + 80+ "px"
       }).fadeIn();
       o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut();  window.location=editUrl},3000));

   }

});