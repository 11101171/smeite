
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

define(function(require, exports){
	var $ = jQuery = require("jquery");
	jQuery(function() {	
		var dataCode = "$!code";
		if(dataCode==101){
			var html1 = "";
				html1 += '<div id="J_TipD" class="g-dialog tip-dialog">';
				html1 += '<div class="dialog-content">';
				html1 += '<div class="hd"><h3>提示</h3></div>';
				html1 += '<div class="bd clearfix">';
				html1 += '<p class="pt10 tac">绑定失败，请重试~</p>';
				html1 += '</div>';
				html1 += '<a class="close" href="javascript:;"></a>';
				html1 += '</div>';
				html1 += '</div>';
				$("body").append(html1);
				$("#J_TipD").overlay({
					top: 'center',
					closeOnClick: false,
					load: true			
				});
		}	

		$(".relieve").click(function(){
			var $this = $(this),
			snsNameV = $(this).attr("data-snsName"),
			snsTypeV = $(this).attr("data-snsType");
			if(!$("#J_ConfirmD")[0]){
				var html = "";
				html += '<div id="J_ConfirmD" class="g-dialog tip-dialog">';
				html += '<div class="dialog-content">';
				html += '<div class="hd"><h3>提示</h3></div>';
				html += '<div class="bd clearfix">';
				html += '<p class="pt10 tac">你确定要解除绑定的帐号吗？</p>';
				html += '<div class="act-row"><p class="inlineblock"><a class="bbl-btn mr10" id="J_Confirm" href="javascript:;">解除绑定</a><a class="bgr-btn" id="J_Cancel" href="javascript:;">取消</a></p></div>';
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
					closeOnClick: false,
					load: true			
				});
				
				$("#J_Cancel").unbind("click").click(function(){
					$("#J_ConfirmD").overlay().close();
				});
			}else{
				$("#J_ConfirmD").data("overlay").load();
			}
			$("#J_Confirm").unbind("click").click(function(){
				$("#J_ConfirmD").overlay().close();
					jQuery.post("/user/account/removeBind", {
						snsName : snsNameV,
						snsType : snsTypeV
					}, function(data) {
						if(data.code==100){
							//var newhtml='<a href= "/account/snsLogin?snsType='+snsTypeV+'">绑定帐号</a> 和好友分享你的发现。';
							//$this.parent().html(newhtml);
							window.location.href= "/account/sns";
						}else{
							jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
							jQuery.smeite.tip.show($this,">_< 去除绑定不成功，稍后再试！");
						}
					});
				});
			
		});
		$.ajax({
			url: "/snsfriend_count",
			type : "post",
			dataType: "json",
			success: function(data){
				switch(data.code){
					case(100):
						if(data.sinaCount){
							$("#J_bindSina .sync-row").before("<p>共有 "+data.sinaCount+" 位好友也在食美特，<a href='/snsfriend?siteType=sina'>去打招呼～</a></p>");
						}
						if(data.tencentCount){
							$("#J_bindTencent .sync-row").before("<p>共有 "+data.tencentCount+" 位好友也在食美特，<a href=' /snsfriend?siteType=tec_weibo'>去打招呼～</a></p>");
						}
						if(data.renrenCount){
							$("#J_bindRenren").append("<p>共有 "+data.renrenCount+" 位好友也在食美特，<a href='/snsfriend?siteType=renren'>去打招呼～</a></p>");
						}
					break;
					case(101):
						alert($json.msg);
					break;
				}  
			}
		});
	    
	    $("#goJifenbao").click(function(){          
	        $.ajax({
			url: "/account/alipayRecord",
			type : "post",
			dataType: "json",
	        success:function(data){
	            if(data.code==100)
	            {
	                if(data.canGo==true){
	                 				$("#goJifenbao").hide();
	                                window.open("http://jf.etao.com/?signIn=https://hi.alipay.com/campaign/normal_campaign.htm?campInfo=f8TFC%2B0iCwuQiPUpnvPiD7zMoy1VtWKh&from=jfb&sign_from=3000");
	                                   
	                }else{
	                                
	                                $("#J_jifengbao")[0].innerHTML="你已经领取过集分宝~";

	                }
	            
	            }
	        }
	        
		});
		});
		
		//同步复选框操作
		$(".syncChk").click(function(){
			var $this = $(this),
			id = $this.attr("id"),
			jsonData = {};
			switch(id){
				case "syncSina":
					jsonData.userSite = "sina";
					break;
				case "syncQQ":
					jsonData.userSite = "qzone";
					break;
				case "syncTenc":
					jsonData.userSite = "tec_weibo";	
			}
			jsonData.synchro = $this[0].checked?"on":"off";
			if(!$("#J_Confirm1D")[0]){
				var html = "";
				html += '<div id="J_Confirm1D" class="g-dialog tip-dialog">';
				html += '<div class="dialog-content">';
				html += '<div class="hd"><h3>提示</h3></div>';
				html += '<div class="bd clearfix">';
				html += '<p class="pt10 tac">你确定要修改SNS同步设置吗？</p>';
				html += '<div class="act-row"><p class="inlineblock"><a class="bbl-btn mr10" id="J_Confirm1" href="javascript:;">确定</a><a class="bgr-btn" id="J_Cancel1" href="javascript:;">取消</a></p></div>';
				html += '</div>';
				html += '<a class="close" href="javascript:;"></a>';
				html += '</div>';
				html += '</div>';
				$("body").append(html);
				$("#J_Confirm1D").overlay({
					top: 'center',
					mask: {
						color: '#000',
						loadSpeed: 200,
						opacity: 0.3
					},
					closeOnClick: false,
					load: true			
				});
				
				$("#J_Cancel1").unbind("click").click(function(){
					$("#J_Confirm1D").overlay().close();
					$this[0].checked = !$this[0].checked;
				});
			}else{
				$("#J_Confirm1D").data("overlay").load();
			}
			$("#J_Confirm1").unbind("click").click(function(){
			$("#J_Confirm1D").overlay().close();
			$.ajax({
				url: "/user/account/setSynchro",
				type: "post",
				dataType: "json",
				data: jsonData,
				success: function(d){
					if(d.code==100){
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-ok";
						$.smeite.tip.show($this,"同步设置成功！");
					}
				}
			});
			});
			
		});
		
	});
});
