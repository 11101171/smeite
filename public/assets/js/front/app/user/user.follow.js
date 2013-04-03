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
    var $ = require("jquery");
	
	var confirmUI = function(txt,fun,cancelfun){
		if($("#J_ConfirmD")[0]){
			$("#J_ConfirmD").remove();
		}
		var html = "";
		html += '<div id="J_ConfirmD" class="g-dialog tip-dialog">';
		html += '<div class="dialog-content">';
		html += '<div class="hd"><h3>提示</h3></div>';
		html += '<div class="bd clearfix">';
		html += '<p class="pt10 tac">'+txt+'</p>';
		html += '<div class="act-row"><p class="inlineblock"><a class="bbl-btn mr10" id="J_Confirm" href="javascript:;">确定</a><a class="bgr-btn" id="J_Cancel" href="javascript:;">取消</a></p></div>';
		html += '</div>';
		html += '<a class="close" href="javascript:;"></a>';
		html += '</div>';
		html += '</div>';
		$("body").append(html);
		$("#J_ConfirmD").overlay({
			top: 'center',
			mask: {
				color: '#000',
				loadSpeed: 200,
				opacity: 0.3
			},
			closeOnClick: true,
			load: true,
			onClose: function() {
				cancelfun();
				$("#commentDialog,#exposeMask").remove();
			}				
		});
		$("#J_Cancel").unbind("click").click(function(){
			$("#J_ConfirmD").overlay().close();
		});
		$("#J_Confirm").unbind("click").click(function(){
			$("#J_ConfirmD").overlay().close();
			fun();
		});
	}
	//加关注
	$("a[rel=follow]").live("click",function(){
		if($.smeite.dialog.isLogin()){
			if($(this).data("enable")=="false") return;
			$(this).data("enable","false");
			$this = $(this);
			$.ajax({
				url:"/user/addFollow",
				type : "get",
				dataType: "json",
				data: {
					userId : $(this).data("userid")
				},
				jQueryDom:$this,
				success: function(data){
					switch(data.code){
						case("100"):
							this.jQueryDom.data("enable","true");
							$.smeite.addFollowCallback(data,this.jQueryDom);
						break;
						case("101"):
							$this.data("enable","true");
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							$.smeite.tip.show($this,"参数错误");
						break;
						case("103"):
							$this.data("enable","true");
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							$.smeite.tip.show($this,"重复关注了你的朋友！");
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
	
	//移除粉丝
	$.smeite.removeFansCallback = function(o){
		o.parents(".section").remove();
	}
	$("a[rel=removefans]").live("click",function(){
		if($.smeite.dialog.isLogin()){
			if($(this).data("enable")=="false") return;
			$(this).data("enable","false");
			$this = $(this);
			var txt = "确定要移除粉丝“"+$this.data("usernick")+"”？";
			var confirmCallback = function(){
				$.ajax({
					url:"/user/removeFans",
					type : "get",
					dataType: "json",
					data: {
						userId : $this.data("userid")
					},
					success: function(data){
						switch(data.code){
							case("100"):
								$this.data("enable","true");
								$.smeite.removeFansCallback($this,data);
							break;
							case("101"):
								$this.data("enable","true");
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
								$.smeite.tip.show($this,data.msg);
							break;
							case("300"):
								$.smeite.dialog.login();
							break;
						}  
					}
				});
			}
			confirmUI(txt,confirmCallback,function(){
				$this.data("enable","true");
			});
		}
	})
	
	//取消关注
	$.smeite.removeFollowCallback = function(){
		o.parents(".section").remove();
	}
	$("a[rel=removefollow]").live("click",function(){
		if($.smeite.dialog.isLogin()){
			if($(this).data("enable")=="false") return;
			$(this).data("enable","false");
			var $this = $(this)
			var txt = "确定不再关注“"+$this.data("usernick")+"”？";
			var confirmCallback = function(){
				$.ajax({
					url:"/user/removeFollow",
					type : "get",
					dataType: "json",
					data: {
						userId : $this.data("userid")
					},
					success: function(data){
						switch(data.code){
							case("100"):
								$this.data("enable","true");
								$.smeite.removeFollowCallback(data,$this);
							break;
							case("101"):
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
			};
			confirmUI(txt,confirmCallback,function(){
				$this.data("enable","true");
			})
		}
	})
	//data-followtype: 0边栏信息 1粉丝 2关注 3你可以能喜欢的人 4去其他人的关注列表和粉丝列表 5通用用户关注  6
	$.smeite.addFollowCallback = function(data,o){
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
	$.smeite.removeFollowCallback = function(data,o){
		
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