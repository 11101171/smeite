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
	window.ZeroClipboard = require("module/zeroclipboard");
	
	var clip = new ZeroClipboard.Client();
	clip.setHandCursor( true );
	clip.addEventListener('mouseover', function (client) {
		clip.setText($('#clip-text').val());
	});
	clip.glue('J_Clip');
	document.getElementById("J_Clip").onmouseover = function(){
		clip.reposition(this);
	}
		
	jQuery(function() {	
		var snsSentEnable = true;
		var SnsInvitatinData = {
			sina:{
				status:true,
				startindex:15
			},
			tencent:{
				status:false,
				startindex:0
			}
		}
		$(".weibo_invite li input").live("click",function(){
			var txt = $("#sns-text").val();
			if(this.checked){
				if((" "+txt+" ").indexOf(" @"+this.value+" ")==-1){
					txt = "@"+this.value+" "+jQuery.trim(txt)
				}
			}else{
				if((" "+txt+" ").indexOf(" @"+this.value+" ")>=0){
					txt = jQuery.trim((" "+txt+" ").replace((" @"+this.value+" ")," "))
				}
			}
			$("#sns-text").val(txt);
			snsTextLimit();
		})
		$(".weibo_invite li p,.weibo_invite li img").live("click",function(){
			var checkbox = $(this).siblings("input")[0];
			if(checkbox.checked==true){
				checkbox.checked=false;
			}else{
				checkbox.checked=true;
			}
			var txt = $("#sns-text").val();
			if(checkbox.checked){
				if((" "+txt+" ").indexOf(" @"+checkbox.value+" ")==-1){
					txt = "@"+checkbox.value+" "+jQuery.trim(txt)
				}
			}else{
				if((" "+txt+" ").indexOf(" @"+checkbox.value+" ")>=0){
					txt = jQuery.trim((" "+txt+" ").replace((" @"+checkbox.value+" ")," "))
				}
			}
			$("#sns-text").val(txt);
			snsTextLimit();
		})
		$("#sns-text").keyup(function(){
			snsTextLimit();
		})
		$("#J_Publishweibo").click(function(){
			if(snsSentEnable){
				if(SnsInvitatinData.tencent.status){
					publishWeibo($("#sns-text").val(),"tec_weibo",$(this))
				}
				if(SnsInvitatinData.sina.status){
					publishWeibo($("#sns-text").val(),"sina",$(this))
				}
			}
		})
		
		//提示可剩余编辑的文字数量
		var snsTextLimit = function(){
			var maxlength = 140;
			var curlength = Math.floor(jQuery.smeite.util.getStrLength($("#sns-text").val())+0.5);
			if(curlength-maxlength>0){
				snsSentEnable = false;
				$("#J_SnsTextLimit").html("超出<font>"+(curlength-maxlength)+"</font>个字")
			}else{
				snsSentEnable = true;
				$("#J_SnsTextLimit").html("还可输入<font>"+(maxlength-curlength)+"</font>个字")
			}
		}
		snsTextLimit();
		
		//发布微博
		var publishWeibo = function(message,type,o){
			jQuery.ajax({
			   url: "/publishWeibo",
			   type : "post",
			   dataType: "json",
			   data:{
					message:message,
					type:type
			   },
			   success: function(data){
				   switch(data.code){
						case 100 :
							$("#sns-text").val(sharetextUrl)
							jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-general-short";
							jQuery.smeite.tip.show(o, "发送邀请成功！");
						case 101:
							jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							jQuery.smeite.tip.show(o, data.msg);
							break;
						case 300 : 
							jQuery.smeite.dialog.login();
							break;
				   }
			   }
			});	
		}
		
		//换一批
		var changeSnsFriends = function(){
			var ajaxUrl = "/snsfriends";
			var type = "sina";//新浪3  腾讯5
			var startindex = SnsInvitatinData.sina.startindex;
			if(SnsInvitatinData.tencent.status){
				type = "tec_weibo";////新浪3  腾讯5
				startindex = SnsInvitatinData.tencent.startindex;
				if(!hasTencent)return;
			}
			if(SnsInvitatinData.sina.status){
				if(!hasSina)return;
			}
			jQuery.ajax({
			   url:  ajaxUrl,
			   type : "post",
			   dataType: "json",
			   data: {
				   startindex : startindex,
				   type : type
			   },
			   success: function(data){
				   switch(data.code){
						case 100 :
							var HTML = "";
							if(data.tencFriends==null||data.tencFriends.length==0){
								if(SnsInvitatinData.tencent.status){
									if(SnsInvitatinData.tencent.startindex == 0){
										$("#J_SnsQQList .items").html('<ul><p><a class="pt20 pb20" href="/snsfriend">你在腾讯微博还没有好友 或者 你的好友都已经加入了食美特哦~</a></p></ul>');
										$("#J_ChangeQList").hide();
										return "nofriends";
									}
									SnsInvitatinData.tencent.startindex = 0;
								}
								if(SnsInvitatinData.sina.status){
									if(SnsInvitatinData.sina.startindex == 0){
										return "nofriends";
									}
									SnsInvitatinData.sina.startindex = 0;
								}
								changeSnsFriends();
								return;
							}
							if(SnsInvitatinData.tencent.status){
								for(var i=0;i<data.tencFriends.length&&i<15;i++){
									HTML += "<li><input type='checkbox' value='"+data.tencFriends[i].name+"'>";
									HTML += "<img alt='"+data.tencFriends[i].nick+"' src='"+data.tencFriends[i].headurl+"' width='50' height='50'/>"
									HTML += "<p class='ofh'>"+data.tencFriends[i].nick+"</p>"
									HTML += "</li>"
								}
								SnsInvitatinData.tencent.startindex = SnsInvitatinData.tencent.startindex+15 ;
								$("#J_SnsQQList .items").html('<ul>'+HTML+'</ul>');
							}
							if(SnsInvitatinData.sina.status){
								for(var i=0;i<data.tencFriends.length&&i<15;i++){
									HTML += "<li><input type='checkbox' value='"+data.tencFriends[i].name+"'>";
									HTML += "<img alt='"+data.tencFriends[i].name+"' src='"+data.tencFriends[i].profileImageURL+"' width='50' height='50'/>"
									HTML += "<p class='ofh'>"+data.tencFriends[i].name+"</p>"
									HTML += "</li>"
								}
								SnsInvitatinData.sina.startindex = SnsInvitatinData.sina.startindex+15;
								$("#J_SnsSinaList .items").html('<ul>'+HTML+'</ul>');
							}
							break;
						case 101:
							jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
							jQuery.smeite.tip.show(o, $json.msg);
							break;
						case 300 : 
							jQuery.smeite.dialog.login();
							break;
				   }
			   }
			});	
		}
		
		$("#J_ChangeQList").bind("click",changeSnsFriends);
		
		$("#J_TabSnsQQ").click(function(){
			//$("#sns-text").val("$!sharetext$!url")
			if(!$(this).parent().hasClass("on")){
				$("#J_TabSnsSina").parent().removeClass("on");
				$(this).parent().addClass("on");
				$("#J_SnsSinaList").hide();
				$("#J_SnsQQList").show();
				SnsInvitatinData.tencent.status = true;
				SnsInvitatinData.sina.status = false;
				changeSnsFriends();	
			}
		})
		
		$("#J_TabSnsSina").click(function(){
			//$("#sns-text").val("$!sharetext$!url")
			if(!$(this).parent().hasClass("on")){
				$("#J_TabSnsQQ").parent().removeClass("on");
				$(this).parent().addClass("on");
				$("#J_SnsQQList").hide();
				$("#J_SnsSinaList").show();
				SnsInvitatinData.tencent.status = false;
				SnsInvitatinData.sina.status = true;
			}
		})
		
		if(!hasSina){
		$("#J_SnsSinaList").scrollable({circular:true,disabledClass:"disable"});
		$("#J_SnsQQList").hide();
		}
		if(!hasTencent){
				SnsInvitatinData.tencent.status = true;
				SnsInvitatinData.sina.status = false;
				changeSnsFriends();
		}
	});
});
