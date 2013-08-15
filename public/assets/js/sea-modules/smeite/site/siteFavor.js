/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-6-22  上午10:22
 * ModifyTime :
 * ModifyContent:  for  site favor
 * http://www.smeite.com/
 *
 */
define(function(require, exports){
    var $  = require("$");
    var ConfirmBox = require("confirmbox")
    var judgeFollowSiteState=function(siteId,o){

        $.ajax({
            url:"/site/checkSiteLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"siteId": siteId }),
            success: function(data){
                if(data.code=="100"){
               //     o.removeClass("follow-btn").addClass("followed-btn").text("已关注");
                    o.after("<div class='followed-btn'>已加入<span class='mr5 ml5'>|</span><a rel='removeFollowSite'data-title="+o.data("title")+"  data-id='"+o.data("id")+"' href='javascript:;'>退出</a></div>") ;
                    o.remove();
                }
            }
        });

    }

    $(function(){
        /*先判断状态 */
        if(SMEITER.userId !=""){
            $("a[rel=followSite]").each(function(){
                var id =$(this).data("id")
                judgeFollowSiteState(id,$(this))
            })
        }

        //加关注
        $(document).on("click","a[rel=followSite]",function(){
            if($.smeite.dialog.isLogin()){
              //  if($(this).data("enable")=="false") return;
              //  $(this).data("enable","false");
              var  $this = $(this);
                var siteId = $this.data("id")
                $.ajax({
                    url:"/site/addFollow",
                    type : "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data: JSON.stringify({"siteId": siteId }),
                    success: function(data){
                        switch(data.code){
                            case("100"):
                             //   $this.data("enable","true");
                                $.smeite.addFollowSiteCallback(data,$this);
                                break;
                            case("104"):
                            //    $this.data("enable","true");
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                $.smeite.tip.show($this,"参数错误");
                                break;
                            case("103"):
                           //     $this.data("enable","true");
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                $.smeite.repeatFollowSiteCallback(data,$this);
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
        $(document).on("click","a[rel=removeFollowSite]",function(){

            if($.smeite.dialog.isLogin()){
              //  if($(this).data("enable")=="false") return;
             //   $(this).data("enable","false");
                var $this = $(this)
                var siteId = $this.data("id")
                var txt = "确定不再关注“"+$this.data("title")+"”？";
                ConfirmBox.confirm(txt, '亲，你要和我分手吗？', function() {

                    $.ajax({
                        url:"/site/removeFollow",
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({"siteId": siteId }),
                        success: function(data){
                            switch(data.code){
                                case("100"):
                               // $this.data("enable","true");
                                    $.smeite.removeFollowSiteCallback(data,$this);
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

        $.smeite.addFollowSiteCallback = function(data,o){
               o.after("<div class='followed-btn'>已加入<span class='mr5 ml5'>|</span><a rel='removeFollowSite'data-title="+o.data("title")+"  data-id='"+o.data("id")+"' href='javascript:;'>退出</a></div>")
               o.remove();


        }
        $.smeite.removeFollowSiteCallback = function(data,o){
                o.parent().after("<a rel='followSite' href='javascript:;' class='follow-btn' data-title="+o.data("title")+"   data-id="+o.data("id")+">+加入小镇</a>")
                o.parent().remove();


        }
        $.smeite.repeatFollowSiteCallback = function(data,o){
         //  o.data("enable","enable");
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
            o.data("timeout",setTimeout(function(){$("#cmtDialog").fadeOut()},3000));

            $this.removeClass("follow-btn").addClass("followed-btn").text("已加入");
        }
    })


});