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
	var Setup = require("app/common/area");
	
	$("a[rel=get_award]").bind("click",function(){
		var ajaxUrl = "/account/getUserScore.html";
		var $this = $(this);
		var awardDialog = function(){};//兑奖弹出层;
		$.ajax({
		   url: ajaxUrl,
		   type : "post",
		   dataType: "json",
		   data: {
			   id : $this.data("id")
		   },
		   success: function(data){
			   switch(data.code){
					case 100 :
						awardDialog(data);
						break;
					case 101:
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($this, "亲，服务器繁忙，请稍候在试~");
						break;
			   }
		   }
		});
		awardDialog = function(data){
			var validDays = "";
			if(data.isOverdue+""!="true"){
				var endtimearr = data.endDate.split(" ")[0].split("-");
				var starttimearr = data.nowTime.split(" ")[0].split("-");
				var endyear = endtimearr[0];
				var startyear = starttimearr[0];
				var endmonth = endtimearr[1];
				var startmonth = starttimearr[1];
				var endday = endtimearr[2];
				var startday = starttimearr[2];
				var mongthdays = [31,28,31,30,31,30,31,31,30,31,30,31]
				if(endyear==startyear){
					if(endmonth==startmonth){
						validDays = parseInt(endday,10) - parseInt(startday,10);
					}else if(parseInt(endmonth,10)>parseInt(startmonth,10)){
						validDays = (mongthdays[parseInt(startmonth,10)-1]-startday)+parseInt(endday,10)
					}
				}else if(parseInt(endyear,10)>parseInt(startyear,10)){
					//跨年计算coding
				}
				if(validDays==0){
					validDays == 1;
				}
			}
			var enoughScore = ""
				+'<p>奖品需花费<em style="font-size:30px">'+data.needsScore+'</em>积分<br/>'
				+'领奖有效期只剩<em class="rc"> '+validDays+' 天</em></p>'
				+'<p class="mt10">当前积分：'+data.totalScore+'　　'
				+'<a href="http://blog.smeite.com/?p=269">赚积分&gt;</a>'
				+'</p>'
				+'<a class="bbl-btn getbtn mt10" href="javascript:;">下一步</a>';
			var notenoughScore = ""
				+'<p ><em class="rc">你的积分不足！</em><br/>'
				+'奖品需花费<em style="font-size:30px">'+data.needsScore+'</em>积分<br/>'
				+'领奖有效期只剩<em class="rc">'+validDays+'天</em></p>'
				+'<p class="mt10">当前积分：'+data.totalScore+'　　'
				+'<a href="http://blog.smeite.com/?p=269">赚积分&gt;</a></p>'
				+'<a class="bbl-btn closebtn mt10" href="javascript:;">关闭</a>';
			var isOverdue = ""
				+'<p >领奖有效期已到！<br/>'
				+'<a class="bbl-btn closebtn mt10" href="javascript:;">关闭</a>';
			var text = "";
			if(data.isOverdue+""=="true"){
				text = isOverdue;
			}else{
				text = data.totalScore>data.needsScore?enoughScore:notenoughScore;
			}
			var html = ""
			+'<div id="get_award_Dialog" class="g-dialog" style="z-index: 9999; top: 363.5px; left: 643px; position: fixed; display: block; ">'
				+'<div class="dialog-content">'
					+'<div class="hd"><h3>领取奖品</h3></div>'
					+'<div class="bd clearfix tac">'
						+text
					+'</div>'
					+'<a class="close" href="javascript:;"></a>'
				+'</div>'
			+'</div>';
			$("body").append(html);
			$("#get_award_Dialog .getbtn").unbind();
			$("#get_award_Dialog .getbtn").bind("click",function(){
				$("#get_award_Dialog,#exposeMask").remove();
				$.ajax({
				   url:"/account/getAddress.html",
				   type : "post",
				   dataType: "json",
				   data: {
					   id : $this.data("id")
				   },
				   success: function(data){
					   switch(data.code){
							case 100 :
								if(typeof data.address != "undefined"){
									addressDialog(data.address);
								}else{
									data.address = {
										address:null,
										trueName:"",
										addressDetail:"",
										postcode:"",
										cellphone:"",
										alipayAccount:""
									}
									addressDialog(data.address);
								}
								break;
							case 101:
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
								$.smeite.tip.show(o, $json.msg);
								break;
					   }
				   }
				});
			});
			$("#get_award_Dialog").overlay({
				top: 'center',
				mask: {
					color: '#000',
					loadSpeed: 200,
					opacity: 0.3
				},
				closeOnClick: true,
				load: true,
				onClose: function() {
					$("#get_award_Dialog,#exposeMask").remove();
				}				
			});
			$("#get_award_Dialog .closebtn").unbind()
			$("#get_award_Dialog .closebtn").bind("click",function(){
				$("#get_award_Dialog,#exposeMask").overlay().close();
			})
		}
		addressDialog = function(data){
			var address = data.address;
			var pro = "请选择";
			var city = "请选择";
			var getAward = function(){};
			var html = ""
			+'<div id="address_Dialog" class="g-dialog" style="width:560px;z-index: 9999; top: 363.5px; left: 643px; position: fixed; display: block; ">'
				+'<div class="dialog-content">'
					+'<div class="hd"><h3>收货地址</h3></div>'
					+'<div class="bd clearfix">'
						+'<form id="J_Address" method="post" action="/account/saveAddress">'
							+'<div class="form-row">'
								+'<label><span class="rc">*</span>姓名：</label>'
								+'<input type="text" class="base-input" name="trueName" id="J_TrueName" value="'+data.trueName+'" />'
							+'</div>'
							+'<div class="form-row clearfix">'
								+'<label><span class="rc">*</span>所在地：</label>'
								+'<input type="hidden" id="J_province_city" />'
								+'<select id="J_Province" name="province"></select>'
								+'<select id="J_City" name="city"></select>'
							+'</div>'
							+'<div class="form-row">'
								+'<label><span class="rc">*</span>详细地址：</label>'
								+'<textarea class="base-txa intro-txa" name="addressDetail" id="J_AddressDetail" placeholder="请输入50个字以内的详细地址">'+data.addressDetail+'</textarea>'
								+'<span class="tip" style="display: inline; ">请输入50个字以内的详细地址</span>'
							+'</div>'
							+'<div class="form-row">'
								+'<label>邮编：</label>'
								+'<input type="text" class="base-input" name="zipcode" id="J_Postcode" value="'+data.postcode+'" />'
							+'</div>'
							+'<div class="form-row">'
								+'<label><span class="rc">*</span>手机：</label>'
								+'<input type="text" class="base-input" name="cellphone" id="J_Cellphone" value="'+data.cellphone+'" />'
							+'</div>'
							+'<div class="form-row">'
								+'<label><span class="rc">*</span>支付宝：</label>'
								+'<input type="text" class="base-input" name="alipay" id="J_Alipay" value="'+data.alipayAccount+'" />'
								+'<span class="tip" style="display: inline; ">你有可能获得现金奖</span>'
							+'</div>'
							+'<div class="form-row">'
								+'<label>&nbsp;</label>'
								+'<input type="submit" id="J_Submit" class="bbl-btn submit" value="提交" />'
							+'</div>'
						+'</form>'
					+'</div>'
					+'<a class="close" href="javascript:;"></a>'
				+'</div>'
			+'</div>';
			$("body").append(html);
			
			if(address != null){
			  var adds = address.split("|");
			  pro = adds[0];
			  city = adds[1];
			}
			var setup = new Setup(pro,city);
			//setup(pro,city);
			
			$("#address_Dialog").overlay({
				top: 'center',
				mask: {
					color: '#000',
					loadSpeed: 200,
					opacity: 0.3
				},
				closeOnClick: true,
				load: true,
				onClose: function() {
					$("#address_Dialog,#exposeMask").remove();
				}				
			});
			
			$("#J_Address").validator(
				{
					fun:function(vali,inputs){
						var isPass = true;
						$("#J_province_city").data("vali", 1);
						inputs.trigger("blur");
						inputs.each(function(){
							if($(this).data("vali")==0){
								isPass = false;
							}
						});
						if($("#J_City").val().length<2||$("#J_City").val()=="请选择"){
							vali.effects($("#J_City"), "请选择省份和城市", "error");
							$("#address_Dialog").css({width:"560px"})
							$("#J_province_city").data("vali", 0);
						}else{
							$("#J_City").next(".tip").removeClass().addClass("tip correct").html("");
						}
						if(!isPass){
							$("#address_Dialog").css({width:"560px"})
						}else{
							$("#address_Dialog").css({width:"560px"})
							getAward();
							$("#J_province_city").data("vali", 0);
						}
					}
				}
			)
			$("#J_Address").bind("click",function(){
				$("#address_Dialog").css({width:"560px"})
			})
			getAward = function(){
				$.ajax({
				   url:  "/account/saveAndExchange",
				   type : "post",
				   dataType: "json",
				   data: {
					   id : $this.data("id"),
					   trueName : $("#J_TrueName").val(),
					   province : $("#J_Province").val(),
					   city : $("#J_City").val(),
					   addressDetail : $("#J_AddressDetail").val(),
					   postcode : $("#J_Postcode").val(),
					   cellphone : $("#J_Cellphone").val(),
					   alipay : $("#J_Alipay").val()
				   },
				   success: function(data){
					   switch(data.code){
							case 100 :
								if(data.isOverdue==true){
									//过期
									$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
									$.smeite.tip.show($("#J_Submit"), "兑奖时间已过期");
								}
								if(data.isScoreEnough==false){
									//积分不足
									$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
									$.smeite.tip.show($("#J_Submit"), "你的积分不足");
								}
								$("#address_Dialog,#exposeMask").remove();
								if(data.isScoreEnough+""!="false"&&data.isOverdue+""!="true"){
									location.reload();
								}
								break;
							case 101:
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
								$.smeite.tip.show($("#J_Submit"), $json.msg);
								break;
					   }
				   }
				});
			}
		}
	})
	$("a[rel=remind_award]").bind("click",function(){
		$.smeite.tip.conf.tipClass = "tipmodal tipmodal-general-short";
		$.smeite.tip.show($(this), "提醒成功！");
	})
	$("a[rel=view_award]").bind("click",function(){
		var that = $(this);
		var viewdialog = function(){};
		$.ajax({
		   url:"/account/viewAward.html",
		   type : "post",
		   dataType: "json",
		   data: {
			   id : that.data("id")
		   },
		   success: function(data){
			   switch(data.code){
					case 100 :
						viewdialog(data.message);
						break;
					case 101:
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($("#J_Submit"), $json.msg);
						break;
			   }
		   }
		});
		viewdialog = function(xiaoxi){
			var html = ""
			+'<div id="view_award_Dialog" class="g-dialog" style="z-index: 9999; top: 363.5px; left: 643px; position: fixed; display: block; ">'
				+'<div class="dialog-content">'
					+'<div class="hd"><h3>查看奖品</h3></div>'
					+'<div class="bd clearfix">'
						+xiaoxi
						//+'<p>你好，你喜欢的商品价值有点贵，食美特食美特送不起。<br/>'
						//+'现在送你一张彩票，恭祝你中500万哦。<br/>'
						//+'彩票码：<b class="rc" >DFZXCVASASDFASDOIUY</b><br/>'
						//+'请按照这个网址<a href="http://www.caipiao.com" target="_blank">http://www.caipiao.com</a> 提示进行操作。如有疑问请联系食美特食美特，感谢你的支持！</p>'
						+'<a class="bbl-btn mt10" style="margin-left:148px" href="javascript:;">确定</a>'
					+'</div>'
					+'<a class="close" href="javascript:;"></a>'
				+'</div>'
			+'</div>';
			$("body").append(html);
			$("#view_award_Dialog").overlay({
				top: 'center',
				mask: {
					color: '#000',
					loadSpeed: 200,
					opacity: 0.3
				},
				closeOnClick: true,
				load: true,
				onClose: function() {
					$("#view_award_Dialog,#exposeMask").remove();
				}				
			});
			$("#view_award_Dialog .bbl-btn").bind("click",function(){
				$("#view_award_Dialog,#exposeMask").overlay().close();
			});
		}
	})
	var shareAward = function(data){
		var html = ""
		+'<div id="confirm_award_Dialog" class="g-dialog" style="z-index: 9999; top: 363.5px; left: 643px; position: fixed; display: block; ">'
			+'<div class="dialog-content">'
				+'<div class="hd"><h3>恭喜你,收到了食美特食美特送的奖品！</h3></div>'
				+'<div class="bd clearfix">'
					+'<p>把好消息分享给朋友：</p>'
					+'<div class="share-btn">'
						+'<a class="img-sina" href="'+data.sinajs+'">分享到新浪微博</a>'
						+'<a class="img-qq" href="'+data.qqjs+'">分享到腾讯微博</a>'
					+'</div>'
				+'</div>'
				+'<a class="close" href="javascript:;"></a>'
			+'</div>'
		+'</div>';
		$("body").append(html);
		$("#confirm_award_Dialog").overlay({
			top: 'center',
			mask: {
				color: '#000',
				loadSpeed: 200,
				opacity: 0.3
			},
			closeOnClick: true,
			load: true,
			onClose: function() {
				$("#confirm_award_Dialog,#exposeMask").remove();
			}				
		});
	}
	$("a[rel=confirm_award]").bind("click",function(){
		var $this = $(this);
		$.ajax({
		   url:"/account/receiptAward",
		   type : "post",
		   dataType: "json",
		   data: {
			   id : $this.data("id"),
			   productId: $this.data("proid")
		   },
		   success: function(data){
			   switch(data.code){
					case 100 :
						var sinajs = "javascript:void((function(){var title=encodeURIComponent('在食美特上喜欢了一个东西，他们竟然说送我，结果我还真收到了，尼玛真是神奇的网站。传送门');var link='http://www.smeite.com';var pic='"+data.productImgUrl+"';window.open('http://service.t.sina.com.cn/share/share.php?appkey=1207757825&title='+title+'&pic='+pic+'&url='+link);})())";
						var qqjs = "javascript:void((function(){var title=encodeURIComponent('在食美特上喜欢了一个东西，他们竟然说送我，结果我还真收到了，尼玛真是神奇的网站。传送门');var link='http://www.smeite.com';var pic='"+data.productImgUrl+"';window.open('http://v.t.qq.com/share/share.php?appkey=801128565&url='+link+'&title='+title+'&pic='+pic+'&site='+link);})())";
						shareAward({
							sinajs:sinajs,
							qqjs:qqjs
						});
						$this.prev().addClass("ml56");
						$this.remove();
						break;
					case 101:
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($("#J_Submit"), $json.msg);
						break;
			   }
		   }
		});
	})
	$("a[rel=share_award]").bind("click",function(){
		var $this = $(this);
		var sinajs = "",qqjs="";
		sinajs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单#只要你喜欢，就有机会免单。快和我一起来Smeite.com发现“喜欢” － 食美特，发现喜欢');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://service.t.sina.com.cn/share/share.php?appkey=1207757825&title='+title+'&pic='+pic+'&url='+link);})())";
		qqjs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单#只要你喜欢，就有机会免单。快和我一起来smeite.com发现“喜欢” － 食美特，发现喜欢');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://v.t.qq.com/share/share.php?appkey=801128565&url='+link+'&title='+title+'&pic='+pic+'&site='+link);})())";
		if($this.data("type")=="jifenbao"){
			sinajs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单# 我彻底震惊了，在食美特上喜欢了个宝贝，他们竟然直接送全额现金让我自己买。据说现在每天至少10个人会中奖[哈哈]');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://service.t.sina.com.cn/share/share.php?appkey=1207757825&title='+title+'&pic='+pic+'&url='+link);})())";
			qqjs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单# 我彻底震惊了，在食美特上喜欢了个宝贝，他们竟然直接送全额现金让我自己买。据说现在每天至少10个人会中奖[哈哈]');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://v.t.qq.com/share/share.php?appkey=801128565&url='+link+'&title='+title+'&pic='+pic+'&site='+link);})())";
		}
		if($this.data("type")=="quota_jifenbao"){
			sinajs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单# 哇靠，这个活动好给力，直接送我现金了。据说现在每天至少10个人会中奖[哈哈]');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://service.t.sina.com.cn/share/share.php?appkey=1207757825&title='+title+'&pic='+pic+'&url='+link);})())";
			qqjs = "javascript:void((function(){var title=encodeURIComponent('#你喜欢，食美特买单# 哇靠，这个活动好给力，直接送我现金了。据说现在每天至少10个人会中奖[哈哈]');var link='http://smeite.com';var pic='/assets/ui/event2.png';window.open('http://v.t.qq.com/share/share.php?appkey=801128565&url='+link+'&title='+title+'&pic='+pic+'&site='+link);})())";
		}
		shareAward({
			sinajs:sinajs,
			qqjs:qqjs
		});
	})
});