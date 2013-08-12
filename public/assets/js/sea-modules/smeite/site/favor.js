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
    var judgeSiteFollowState=function(sid,o){

        $.ajax({
            url:"/site/checkSiteLoveState",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"sid": sid }),
            success: function(data){
                if(data.code=="100"){
               //     o.removeClass("follow-btn").addClass("followed-btn").text("已关注");
                    o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowSite'data-title="+o.data("title")+"  data-sid='"+o.data("sid")+"' href='javascript:;'>取消</a></div>") ;
                    o.remove();
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

        //加关注
        $("a[rel=followSite]").on("click",function(){
            if($.smeite.dialog.isLogin()){
              //  if($(this).data("enable")=="false") return;
              //  $(this).data("enable","false");
                $this = $(this);
                var sid = $this.data("sid")
                $.ajax({
                    url:"/site/addFollow",
                    type : "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data: JSON.stringify({"sid": sid }),
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
                                $this.data("enable","true");
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                $.smeite.repeatFollowCallback(data,$this);
                                break;
                            case("111"):
                                $this.data("enable","true");
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


        $("a[rel=removeFollowSite]").on("click",function(){

            if($.smeite.dialog.isLogin()){
              //  if($(this).data("enable")=="false") return;
             //   $(this).data("enable","false");
                var $this = $(this)
                var sid = $this.data("sid")
                var txt = "确定不再关注“"+$this.data("title")+"”？";
                alert("hello")
            //    var confirmCallback = function(){
                        $.ajax({
                        url:"/site/removeFollow",
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({"sid": sid }),
                        success: function(data){
                            switch(data.code){
                                case("100"):
                                    $this.data("enable","true");
                                    $.smeite.removeFollowCallback(data,$this);
                                    break;
                                case("104"):
                                    $this.data("enable","true");
                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
                                    $.smeite.tip.show($this,"参数错误");
                                    break;
                                case("300"):
                                    $.smeite.dialog.login();
                                    break;
                            }
                        }
                    });
             //   };
               /* $.smeite.confirmUI(txt,confirmCallback,function(){
                    $this.data("enable","true");
                })*/
            }
        })

        $.smeite.addFollowCallback = function(data,o){
               o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removeFollowSite'data-title="+o.data("title")+"  data-sid='"+o.data("sid")+"' href='javascript:;'>取消</a></div>")
               o.remove();


        }
        $.smeite.removeFollowCallback = function(data,o){
                o.parent().after("<a rel='followSite' href='javascript:;' class='follow-btn' data-title="+o.data("title")+"   data-sid="+o.data("sid")+">加关注</a>")
                o.parent().remove();


        }
        $.smeite.repeatFollowCallback = function(data,o){
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