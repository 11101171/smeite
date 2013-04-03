/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 主题讨论页面：
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports){
	var $ = jQuery = require("jquery");
	require('module/highlight')($);
	
$(function(){
	var $postCmtArea = $("#J_PostCmtArea");
	var $allComment = $("#J_AllComment");
	var cmtAction = function(){
		$("#J_PostCmtSegment").html("");
		setTimeout(
			function(){
				$("html, body").scrollTop($postCmtArea.offset().top - 60);
				$("#J_Content").highlight({color:'#e5e5e5',speed:300,complete:function(){
					$("#J_Content").highlight({color:'#e5e5e5',speed:500,complete:function(){
						$("#J_Content").css("background-color","#fff");
						$("#J_Content").focus();
					},iterator:'sinusoidal'})
				},iterator:'sinusoidal'})
			},
			100
		)
	}
	$("#J_JoinCmt,#J_JoinCmt2,.icomment").bind("click",function(){
		cmtAction();
	})
	$(".speakmore").live("click",function(){
		cmtAction();
	})
	if(location.href.indexOf("#cmtlist")==-1&&location.href.indexOf("#cmt")>=0&&$postCmtArea.length==1){
		cmtAction();
	}
	if((location.href.indexOf("#cmtlist")>=0||location.href.indexOf("pageNo")>=0)&&$allComment.length==1){
		setTimeout(function(){
			$("html, body").scrollTop($allComment.offset().top - 55);
		},100);
	}

/* 评论 */
    var contentDom = $("#J_Content");
    contentDom.focus(function(){
        $.smeite.dialog.isLogin();
    });
    $("#J_Content").keyup(function(){
        var content = contentDom.val();
        var text = Math.floor($.smeite.util.getStrLength(content)+0.5)+"/140"
        if($.smeite.util.getStrLength(content)>140){
            $("#J_WordCount").css("color","red");
            $("#J_WordCount").data("error","true");
        }else{
            if($("#J_WordCount").data("error")=="true"){
                $("#J_WordCount").css("color","#666");
            }
        }
        $("#J_WordCount").text(text);
    });
    $("#J_CmtPublishBtn").bind("click",function(){
        if($.smeite.dialog.isLogin()){
            var $this = $(this);
            var content = contentDom.val();
            if(content.length==0||$.smeite.util.getStrLength(content)>200){
                contentDom.highlight({color:'#ed8585',speed:500,complete:function(){},iterator:'sinusoidal'})
                if(content.length!=0){
                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.smeite.tip.show($this,"内容小于200字！");
                }
            }else{
                contentDom.val("");
                var  ajaxData={
                    themeId:parseInt($("#J_ThemeId").val()),
                    content:content
                }
                var ajaxUrl = "/theme/reply";
                $.ajax({
                    url:  ajaxUrl,
                    type : "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data: JSON.stringify(ajaxData),
                    success: function(data){
                        switch(data.code){
                            case "100":
                                cmtSucessCallback(data.content);
                                break;
                            case "101" ://错误
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show($this,data.message);
                                break;
                            case "200" : //未登录
                                $.smeite.dialog.login();
                                break;
                            case "440" : //冻结评论
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show($this,"你已被禁言！");
                                break;
                            case "441" : //评论警告
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show($this,"亲,操作过快了哦,休息片刻！");
                                break;
                            case "444" : //冻结登录
                                alert("你已被禁止登录！");
                                window.location.href="http://smeite.com/user/logout";
                                break;
                        }
                    }
                });
            }
        }
    })
    var cmtSucessCallback = function(data, userLevel){
        var html ='<li data-cmtid="">'
           + '<a class="img" href="/user/'+SMEITER.userId+'" target="_blank"><img src="'+SMEITER.userPhoto+'" width="50px" height="50px"></a>'
           + '<span class="info">'
           + '<a class="J_UserNick" href="/user/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick+'</a>'
        //   + '<span class="time">&nbsp;/&nbsp; </span>'
           + '</span>'
           +  '<div class="post"> '
           +   '<p class="J_PostCon wbbw">'+ data +'</p>'
           + '</div>'
           + '<span class="post-floor"></span>'
           + '<p class="oper" data-cmtid="">'
           +  ' <a class="J_PostCmtReply">回复</a>'
           +'</p>'
           +  '</li>'
        $("#J_ForumPost").prepend(html);
        var lastLi = $("#J_ForumPost li:last-child");
        if($("#J_ForumPost li").length==11){
            lastLi.remove();
        }

    }
});

});