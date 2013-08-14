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
    var ConfirmBox = require("confirmbox")
    var judgeFollowThemeState=function(themeId,$elm){
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
                var id =$(this).data("id")
                judgeFollowThemeState(id,$(this))
            })
        }
        /* 主题关注操作*/
        $(document).on("click","a[rel=followTheme]",function(){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
            var $this = $(this);
            var themeId = $this.data("id");

            $.ajax({
                url:"/theme/addFollow",
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"themeId": themeId }),
                success: function(data){
                    switch(data.code){
                        case("100"):
                            //   $this.data("enable","true");
                            $.smeite.addFollowCallback(data,$this);
                            break;
                        case("104"):
                            //    $this.data("enable","true");
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                            $.smeite.tip.show($this,"参数错误");
                            break;
                        case("103"):
                            //     $this.data("enable","true");
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                            $.smeite.repeatFollowCallback(data,$this);
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

        });

       //  取消主题关注操作
        $(document).on("click","a[rel=removeFollowTheme]",function(){

            if($.smeite.dialog.isLogin()){
                //  if($(this).data("enable")=="false") return;
                //   $(this).data("enable","false");
                var $this = $(this)
                var themeId = $this.data("id")
                var txt = "确定不再关注“"+$this.data("title")+"”？";
                ConfirmBox.confirm(txt, '亲，你确信不要我了吗？', function() {

                    $.ajax({
                        url:"/theme/removeFollow",
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({"themeId": themeId }),
                        success: function(data){
                            switch(data.code){
                                case("100"):
                                    // $this.data("enable","true");
                                    $.smeite.removeFollowCallback(data,$this);
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

        $.smeite.addFollowCallback = function(data,o){

         var themeId = o.data("id");
         var cmtHref ='/theme/'+ themeId +'#J_cmtForm';
         var cmtTarget = "";
         var $likeCount;

         //在主题频道页面
         if(window.location.href.indexOf(themeId) == -1){
         cmtTarget = 'target="_blank"';
         $likeCount = o.parent().find(".J_LikeCount:first");
         }else{
         $likeCount = $("#J_LikeCount");
         }
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
         var W = $("#cmtDialog").outerWidth(),  H = $("#cmtDialog").outerHeight();
            $("#cmtDialog").css({
         left: position.x - W/2 + "px",
         top: position.y - H +80 + "px"
         }).fadeIn();
         var newCount = parseInt($likeCount.text(),10) + 1;
         $likeCount.text(newCount);
         o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));
    //     o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowTheme'data-title="+o.data("title")+"  data-id='"+o.data("id")+"' href='javascript:;'>取消</a></div>")
      //   o.remove();

            o.removeClass("follow").addClass("followed").text("已关注");

        }
        $.smeite.removeFollowCallback = function(data,o){
            o.parent().after("<a rel='followTheme' href='javascript:;' class='follow-btn' data-title="+o.data("title")+"   data-id="+o.data("id")+">+关注</a>")
            o.parent().remove();


        }
        $.smeite.repeatFollowCallback = function(data,o){
            //  o.data("enable","enable");
            var themeId = o.data("id");
            var cmtHref ='/theme/'+ themeId +'#J_cmtForm';
            var cmtTarget = "";
            var $likeCount;

            //在主题频道页面
            if(window.location.href.indexOf(themeId) == -1){
                cmtTarget = 'target="_blank"';
                $likeCount = o.parent().find(".J_LikeCount:first");
            }else{
                $likeCount = $("#J_LikeCount");
            }
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
            var W = $("#cmtDialog").outerWidth(),H =$("#cmtDialog").outerHeight();
            $("#cmtDialog").css({
                left: position.x - W/2 + "px",
                top: position.y - H + 80+ "px"
            }).fadeIn();
            o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

            o.removeClass("follow").addClass("followed").text("已关注");
        }

    })


});