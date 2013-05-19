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

define(function(require, exports) {
	var $ = require("jquery");
	require("module/highlight")($);
    $(".clearfix li").hover(function() {
            var self = $(this);
                self.find(".follow-btn").addClass("on");
    }, function() {
            var self = $(this);
                self.find(".follow-btn").removeClass("on");
    });


	var $likeCount = $("#J_LikeCount");
	$(".likes").hover(function(){
		$likeCount.text("+1");
	},function(){
		$likeCount.text($likeCount.data("val"));
	});
	var contentDom = $("#J_SideCmtContent");
	contentDom.focus(function(){
		$.smeite.dialog.isLogin();
	});
	$("#J_SideCmtContent").keyup(function(){
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
	$("#J_SideCmt").bind("click",function(){
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
                var ajaxUrl = "/theme/quickReply";
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
		var userHtml = '<a class="ofh" href="/u/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick+'</a>';
		if(userLevel && userLevel == "daren"){
			userHtml = '<a class="g-daren" href="/u/'+SMEITER.userId+'" target="_blank"><em class="ofh">'+SMEITER.nick+'</em><i class="i-daren">达人</i></a>';
		}
		var html = ''
			+'<li>'
				+'<div class="user-pic">'
					+'<a href="/user/'+SMEITER.userId+'" target="_blank">'
					+'<img src="'+SMEITER.userPhoto+'" width="30" height="30" alt="'+SMEITER.nick+'" title="'+SMEITER.nick+'">'
					+'</a>'
				+'</div>'
				+'<div class="user-news">'
				//	+ userHtml + '：'+data.content
                + userHtml + '：'+data
					+'<br/>'
				+'</div>'
			+'</li>';
		$("#J_CmtList").prepend(html);
		var lastLi = $("#J_CmtList li:last-child");
		if($("#J_CmtList li").length==11){
			lastLi.remove();
		}

	}

});
