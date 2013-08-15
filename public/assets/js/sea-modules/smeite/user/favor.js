/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户关注、取消关注
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

	//加关注
	$("a[rel=followUser]").on("click",function(){
		if($.smeite.dialog.isLogin()){
			//if($(this).data("enable")=="false") return;
			//$(this).data("enable","false");
			$this = $(this);
            var userId = $this.data("userid")
			$.ajax({
				url:"/user/addFollow",
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"userId": userId }),
				success: function(data){
					switch(data.code){
						case("100"):
						//	$this.data("enable","true");
							$.smeite.addFollowUserCallback(data,$this);
						break;
						case("104"):
						//	$this.data("enable","true");
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							$.smeite.tip.show($this,"参数错误");
						break;
						case("103"):
						//	$this.data("enable","true");
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							$.smeite.tip.show($this,"重复关注了你的朋友！");
						break;
						case("111"):
						//	$this.data("enable","true");
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
	
	//移除粉丝
	$.smeite.removeFansCallback = function(o){
		o.parents(".section").remove();
	}

	$(document).on("click","a[rel=removeFans]",function(){
		if($.smeite.dialog.isLogin()){
		//	if($(this).data("enable")=="false") return;
		//	$(this).data("enable","false");
			$this = $(this);
            var userId = $this.data("userid")
			var txt = "确定要移除粉丝“"+$this.data("usernick")+"”？";
            ConfirmBox.confirm(txt, '亲，你确定不喜欢我了吗？', function() {
				$.ajax({
					url:"/user/removeFans",
                    type : "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data: JSON.stringify({"userId": userId }),
					success: function(data){
						switch(data.code){
							case("100"):
							//	$this.data("enable","true");
								$.smeite.removeFansCallback($this,data);
							break;
							case("104"):
							//	$this.data("enable","true");
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
								$.smeite.tip.show($this,"参数出错");
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
	
	//取消关注
	$.smeite.removeFollowUserCallback = function(){
		o.parents(".section").remove();
	}
	$(document).on("click","a[rel=removeFollow]",function(){
		if($.smeite.dialog.isLogin()){
		//	if($(this).data("enable")=="false") return;
		//	$(this).data("enable","false");
			var $this = $(this)
            var userId = $this.data("userid")
			var txt = "确定不再关注“"+$this.data("usernick")+"”？";
            ConfirmBox.confirm(txt, '亲，你要和我分手吗？', function() {
				$.ajax({
					url:"/user/removeFollow",
                    type : "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data: JSON.stringify({"userId": userId }),
					success: function(data){
						switch(data.code){
							case("100"):
							//	$this.data("enable","true");
								$.smeite.removeFollowUserCallback(data,$this);
							break;
							case("104"):
							//	$this.data("enable","true");
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
								$.smeite.tip.show($this,"参数错误");
							break;
							case("300"):
								$.smeite.dialog.login();
							break;
						}  
					}
				});

            })
		}
	})
	//data-followtype: 0边栏信息 1粉丝 2关注 3你可以能喜欢的人 4去其他人的关注列表和粉丝列表 5通用用户关注  6
	$.smeite.addFollowUserCallback = function(data,o){
		if(o.attr("data-followtype")=="0"){
			if(data.isYouFollowed){
				o.after("<div class='followed-btn-big'>互相关注<span class='mr5 ml5'>|</span><a rel='removefollow' data-followtype='0' data-usernick="+o.data("usernick")+" data-userid='"+o.data("userid")+"' href='javascript:;'>取消</a></div>")
			}else{
				o.after("<div class='followed-btn-big'>已关注<span class='mr5 ml5'>|</span><a rel='removefollow' data-followtype='0' data-usernick="+o.data("usernick")+" data-userid='"+o.data("userid")+"' href='javascript:;'>取消</a></div>")
			}
			o.remove();
		}
		if(o.attr("data-followtype")=="1"){
			o.after("<div class='followed-btn'>互相关注<span class='mr5 ml5'>|</span><a rel='removefollow' data-followtype='1' data-usernick="+o.data("usernick")+" data-userid='"+o.data("userid")+"' href='javascript:;'>取消</a></div>")
			o.remove();
		}
		if(o.attr("data-followtype")=="4"){
			if(data.isYouFollowed){
				o.after("<div class='followed-btn'>互相关注</div>")
			}else{
				o.after("<div class='followed-btn'>已关注</div>")
			}
			o.remove();
		}
		if(o.attr("data-followtype")=="5"){
			if(data.isYouFollowed){
				o.after("<div class='followed-btn'>互相关注<span class='mr5 ml5'>|</span><a rel='removefollow' data-followtype='5' data-usernick="+o.data("usernick")+" data-userid='"+o.data("userid")+"' href='javascript:;'>取消</a></div>")
			}else{
				o.after("<div class='followed-btn'>已关注<span class='mr5 ml5'>|</span><a rel='removefollow' data-followtype='5' data-usernick="+o.data("usernick")+" data-userid='"+o.data("userid")+"' href='javascript:;'>取消</a></div>")
			}
			o.remove();
		}
	}
	$.smeite.removeFollowUserCallback = function(data,o){
		
		if(o.attr("data-followtype")=="0"){
			o.parent().after("<a rel='follow' href='javascript:;' class='follow-btn-big' data-followtype='0' data-usernick="+o.data("usernick")+" data-userid="+o.data("userid")+">加关注</a>")
			o.parent().remove();
		}
		if(o.attr("data-followtype")=="1"){
			o.parent().after("<a rel='follow' href='javascript:;' class='follow-btn' data-followtype='1' data-usernick="+o.data("usernick")+" data-userid="+o.data("userid")+">加关注</a>")
			o.parent().remove();
		}
		if(o.attr("data-followtype")=="5"){
			o.parent().after("<a rel='follow' href='javascript:;' class='follow-btn' data-followtype='5' data-usernick="+o.data("usernick")+" data-userid="+o.data("userid")+">加关注</a>")
			o.parent().remove();
		}
		if(o.attr("data-followtype")=="2"){
			o.parents(".section").remove();
			//需要即时将导航上的数据-1
		}
	}
});