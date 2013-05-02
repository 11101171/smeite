/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-4-28  下午2:30
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    var checkInTimeout = null;
    //0or1,0未签，1已签
    var changeCheckInIcon = function(status){
        if(status){
            $("a[rel=signIn]").addClass("checked").text("已签");
        }else{
            $("a[rel=signIn]").removeClass("checked").text("签到");
        }
    }
    var singinOnClickCallBack = function(o,data,show){
        $("#checkin_intro").unbind().remove();
        var userCheckinDays = (data.userCheckinDays+'').length==1?('0'+data.userCheckinDays):data.userCheckinDays;
        var HTML = ""
            +'<div id="checkin_intro">'
            +'连签：<b class="checkin_days">'+userCheckinDays+'</b>&nbsp;天<br/>'
            +'积分：<b id="jifen">'+data.userScore+'</b>&nbsp;分<br/>'
            //+'<a href="/huodong/event3" target="_blank"><img src="http://static.guang.com/images/ui/qiandao.png" alt="签到有奖" /></a>'
            +'<p>'
            +'签到：10积分/天<br/>'
            +'连签7天：送100<br/>'
            +'连签15天：送300<br/>'
            +'连签22天：送1000<br/>'
            +'<a href="/account/invitation" target="_blank">邀请可获更多积分</a>'
            +'</p>'
            +'</div>';
        o.after(HTML);
        $("#checkin_intro").hover(function(){
            if(checkInTimeout != null){
                clearTimeout(checkInTimeout);
            }
        },function(){
            checkInTimeout = setTimeout(function(){
                $("#checkin_intro").remove();
            },500);
        })
        var jifenShow = function(start,end,length){
            if(start>=end&&$("#jifen")[0]){
                $("#jifen").html(end)
                return;
            }
            $("#jifen").html(start+length)
            setTimeout(function(){
                jifenShow(start+length,end,length)
            },50)
        }
        if(show=="show"){
            var minlength = (data.userScore/20<1)?1:Math.floor(data.userScore/20);
            jifenShow(data.userScore/2,data.userScore,minlength);
        }
    }

    //签到轮询函数
    var timeJudgment = function(){
        if(SMEITER.userId.length<=1){
            return "Not login";
        }
        $.ajax({
            url:"/user_score",
            type : "post",
            dataType: "json",
            success: function(data){
                if(data.code==100){
                    changeCheckInIcon(data.status);
                }else if(data.code==101){
                    //积分获取失败
                    //$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error3";
                    //$.smeite.tip.show($this,">_<积分获取失败");
                }else if(data.code==300){
                    //未登录
                    changeCheckInIcon(false);
                }
            }
        });
    }
    timeJudgment();

    $("a[rel=signIn]").click(function(){
        if(!$.smeite.dialog.isLogin()){
            return false;
        }
        if($("a[rel=signIn]").hasClass("checked")){
            return false;
        }
        var $this = $(this);
        $.ajax({
            url: "/user_checkin",
            type : "post",
            dataType: "json",
            success: function(data){
                if(data.code==100){
                    singinOnClickCallBack($this,data,"show")
                    changeCheckInIcon(true);
                }else if(data.code==101){
                    //签到失败
                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error3";
                    $.smeite.tip.show($this,">_< 签到失败！");
                }else if(data.code==103){
                    //已签到
                    singinOnClickCallBack($this,data)
                    changeCheckInIcon(true);
                }else if(data.code==300){
                    //未登录
                    $.smeite.dialog.login();
                }
            }
        });
    })

    $("a[rel=signIn]").hover(function(){
        if(SMEITER.userId.length<=1){
            return "Not login";
        }
        $this = $(this);
        $.ajax({
            url: "/user_score",
            type : "post",
            dataType: "json",
            success: function(data){
                if(data.code==100){
                    singinOnClickCallBack($this,data);
                }else if(data.code==101){
                    //积分获取失败
                    data.userScore = 0;
                    data.userCheckinDays = 0;
                    singinOnClickCallBack($this,data)
                }else if(data.code==300){
                    //未登录
                    $.smeite.dialog.login();
                }
            }
        });
    },function(){
        if($("#checkin_intro")[0]){
            checkInTimeout = setTimeout(function(){
                $("#checkin_intro").remove();
            },500);
        }
    });



})