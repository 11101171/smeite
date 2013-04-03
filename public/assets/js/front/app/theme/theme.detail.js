/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 主题页面：主题美化操作 删除主题  添加商品  goods显示等操作
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-26 22:00
 * ModifyContent:删除注释 增加goods瀑布流显示
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
	var $ = require("jquery");
	require("module/jquery.colorpicker")($);
   require("app/smeite.goods")
var UserTheme = {
	page : $("#J_Page"),
	themeBanner : $("#J_ThemeBanner"),
	themeId : $("#J_ThemeId").val(),
	//删除主题
	deleteUserTheme : function(){
		var $del = $("#J_UserThemeDel");
		$.ajax({
			url:"/theme/delete?id="+UserTheme.themeId,
			dataType : "json",
			type : "get",
			success : function(data){

				switch(data.code){
					case "100" :
						var html = "";
						html += '<p class="success-text"><span class="correct">主题已删除成功！</span></p>';
						html += '<p class="clearfix"><span class="fl mr10 l30">5秒后将</span><a class="bbl-btn goCheck" href="/user/' + SMEITER.userId + '/theme">返回我的主题</a>';
						//html += '<a class="bgr-btn closeD ml10" href="javascript:;">关闭</a></p>';
						$.smeite.tipForOper.conf.html = html;
						$.smeite.tipForOper.init();
						//5秒后自动跳转到我的主题
						setTimeout(function(){
							window.location.href ='/user/' + SMEITER.userId + '/theme';
						},5000)
						break;
					case "101" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($del,"请求失败了，重新提交试下！");
						break;
					case "105" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($del,"您没有权限删除此主题哦！");
				}
			}
		});
	},
	//添加商品
	addProduct : function(proId,proPic){
		var $btn = $("#J_UserAddProduct"), 
		$userProductUrl = $("#J_UserProductUrl"),
		$goodsWall = $("#GoodsWall");
      var  ajaxData = {
            themeId: parseInt(UserTheme.themeId),
            goodsId: parseInt(proId),
              pic:proPic
        };
		//提交商品		
			$.ajax({
				url : "/theme/addGoods",
                type:"post",
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                data:JSON.stringify(ajaxData),
				success : function(data){
                    $("#J_UserProductUrl").val()
					switch(data.code){
						case "100" :
							var goodsHtml = '';
							goodsHtml += '<div class="goods">';
							goodsHtml += 	'<div class="goods-pic" ><a href="/goods/' + data.product.id + '" target="_blank">';
							goodsHtml += 		'<img src="' + data.product.pic + '" alt="' + data.product.name + '" /></a>';
							goodsHtml += 		'<a class="ilike-del" href="javascript:;" data-proid="' + data.product.id + '" style="display: none; ">删除</a>';
							goodsHtml += 		'<a class="ilike-m" href="javascript:;" data-type="0" data-proid="' + data.product.id + '" style="display:none;">喜欢</a>';
							goodsHtml += 		'<a class="ilike-topic" href="javascript:;" data-proid="' + data.product.id + '" style="display:none;">加入主题</a>';
							goodsHtml += 	'</div>';
							goodsHtml +=	'<div class="comments-top">';
							goodsHtml +=	'<span class="like-num"><em class="like-count">' + 0 + '</em></span>';
							goodsHtml +=    	'<span class="like-comments"><em class="like-count">' + 0 + '</em></span>';
							goodsHtml +=    	'<span class="like-price">￥' + data.product.price + '</span>';
							goodsHtml += 	'</div>';
						//	goodsHtml += 	'<h3>' + data.product.proComment + '</h3>';
							goodsHtml += '</div>';

							$("#J_GoodsContent").append(goodsHtml);
							$goodsWall.find(".goods-fill").remove();
							$.smeite.goods.conf.colArray = [0,0,0,$(".side").outerHeight()+13];
							$.smeite.goods.justFlow = true;
							$.smeite.goods.isAjaxLoad = "false";
							$.smeite.goods.init();
							$userProductUrl.val("");
							break;
						case "101" :
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
							$.smeite.tip.show($btn,"请求出错！");
							break;
						case "102" :
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
							$.smeite.tip.show($btn,"此商品不存在！");
							break;
                        case "104" :
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show($btn,"商品已经在本主题中哦！");
                            break;
						case "105" :
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
							$.smeite.tip.show($btn,"您没有权限添加商品哦！");
							break;
					}
				},
				error : function(){
					$.smeite.dialog.isLogin();
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($btn,"亲，您可能没登录，刷新页面试试！");
				}
			});		
	},
	removeProduct : function($this){
			
	},
	//通用选择底色功能
	bgColor : function($this, type, clk){
		var defaultColor = $this.data("colorval");
		if(!$("#J_" + type + "ColorPickerD")[0]){
			var html = "";
			html += '<div id="J_' + type + 'ColorPickerD" class="g-dialog sg-dialog tip-dialog cp-dialog">';
			html += '<div class="content">';
			html += '<input id="J_' + type + 'ColorVal" type="text" class="base-input" value="' + defaultColor  + '" style="background-color:' + defaultColor + '"/>';
			html += '<div id="J_' + type + 'ColorPicker">';
			html += '</div>';
			html += '<div class="tipbox-up"><em>◆</em><span>◆</span></div>';
			html += '<a class="close" href="javascript:;"></a>';
			html += '</div>';
			html += '</div>';
			$("body").append(html);
		}
		$(".tip-dialog").css("display", "none");
		var $dialog = $("#J_" + type + "ColorPickerD");
		$dialog.css({
			"left" : $this.offset().left - 30,
			"top" : $this.offset().top + $this.outerHeight() + 10
		}).fadeIn("fast");
		$dialog.find(".close").click(function(){
			$dialog.fadeOut("fast");
		});
		//取色器
		var $inputColor = $dialog.find("#J_" + type + "ColorVal");
		var picker = $.farbtastic("#J_" + type + "ColorPicker");  //picker variable
		picker.setColor(defaultColor);
		picker.linkTo(onColorChange); //link to callback
		function onColorChange(color) {
			clk(color);
			$inputColor.val(color).css("backgroundColor",color);
		}
		$inputColor.blur(function(){
			var color = $(this).val();
			picker.setColor(color);
			clk(color);
			$(this).css("backgroundColor",color);
		});
		//点击非选框区可直接关闭选框
		document.onclick = function (event) {
			var $box  = $("#J_" + type + "ColorPickerD");
			var e = event || window.event; //兼容ie和非ie的event
			var aim = e.srcElement || e.target; //兼容ie和非ie的事件源
			if (e.srcElement) {
		        var aim = e.srcElement;
		        if (($(aim).closest("#J_" + type + "ColorPickerD").length == 0) && (aim.tagName != "I") && (aim.id != "J_" + type + "Color")) {
		            $box.hide();
		        }
		    } else {
		        var aim = e.target;
		        if (($(aim).closest("#J_" + type + "ColorPickerD").length == 0) && (aim.tagName != "I") && (aim.id != "J_" + type + "Color")) {
		            $box.hide();
		        }
		    }	
		}
	},
	//通用上传图片功能
	picUpload: function($this, type){
    	var picFileVal = "";
    	if(!$("#J_" + type + "PicUploadD")[0]){
			var html = "";
			html += '<div id="J_' + type + 'PicUploadD" class="g-dialog sg-dialog tip-dialog upload-dialog">';
			html += '<div class="content">';
			html += $("#J_" + type + "PicUploadCon").html();
			html += '<div class="tipbox-up"><em>◆</em><span>◆</span></div>';
			html += '<a class="close" href="javascript:;"></a>';
			html += '</div>';
			html += '</div>';
			$("body").append(html);
		}
		$(".tip-dialog").css("display", "none");
		var $dialog = $("#J_" + type + "PicUploadD");
		$dialog.css({
			"left" : $this.offset().left - 4,
			"top" : $this.offset().top + $this.outerHeight() + 10,
			"height" : "120px"
		}).fadeIn("fast");
		$dialog.find(".close").click(function(){
			$dialog.fadeOut("fast");
		});

        function validata(){
            if (!/\.(gif|jpg|png|jpeg|bmp)$/i.test(picFileVal)) {
                alert('请上传标准图片文件,支持gif,jpg,png,jpeg.');
                return false;
            }
            return true;
        }
        //返回提交成功后的操作
		window.publishPicSuccess =  function(code, picType, picSrc){

			switch(code){
				case "100" : //成功
					//图片获取成功
					switch(picType){
						case "themeBanner" :
							UserTheme.themeBanner.css("backgroundImage", 'url("' + picSrc + '")');
							$("#J_HeaderPicBtn").data("src", picSrc);
							break;
						case "themeBg" :
							UserTheme.page.css("backgroundImage", 'url("' + picSrc + '")');
							$("#J_PagePicBtn").data("src", picSrc);
							break;
					}
					$("#J_" + type + "PicUploadD").fadeOut("fast");
					break;
				case "101" : //程序异常
					alert("亲,上传失败了, 重新提交试试！");
					break;
			}
		}
        window.submitPic = function(obj, rangeType){
    		if ($(obj).data("isSubmit") != 1){
    			submitRun();
    		}
    		function submitRun(){
    			$(obj).data("isSubmit", 1);
    			var $picUploadTarget = $("#" + rangeType + "picUploadTarget");
    	        picFileVal = $(obj).val();
    			$(obj).closest('form').submit();
    			
    	        if (validata()) {
    	            $(obj).closest('form').submit();
    	        }
    	        $(obj).data("isSubmit", 0);
    	        return false;		
    		}
    	}
	},
	bgRepeat : function($this,$clkObj){
		if($this.val() == "1"){
			$clkObj.css("backgroundRepeat","repeat");
			$this.val("0");
		}else{
			$clkObj.css("backgroundRepeat","no-repeat");
			$this.val("1");
		}
	},
	bgPosition : function($this, $clkObj){
		$clkObj.css("backgroundPosition","center " + $this.val());
		$this.data("val", $this.val());
	},
	photoDel : function($this, $clkObj, $picBtn){
		$clkObj.css('backgroundImage', 'url("/assets/ui/placeholder.png")');
		$picBtn.data("src","");
	},
	style : {
		pageBgColor : function(){
			$("#J_PageBgColor").click(function(){
				var $this = $(this);
				UserTheme.bgColor($(this),"PageBg",function(color){
					UserTheme.page.css("backgroundColor",color);
					$this.data("colorval",color);
					$this.find("i").css("backgroundColor", color);
				});
			});
		},
		pageBgRepeat : function(){
			$("#J_PageBgRepeat").click(function(){
				UserTheme.bgRepeat($(this), UserTheme.page);
			});
		},
		pageBgAttachment : function(){
			$("#J_PageBgAttachment").click(function(){
				var $this = $(this);
				if($this.val() == "1"){
					UserTheme.page.css("backgroundAttachment","fixed");
					$this.val("0");
				}else{
					UserTheme.page.css("backgroundRepeat","scroll");
					$this.val("1");
				}
			});
		},
		pageBgPosition : function(){
			var $pageBgPosition = $("#J_PageBgPosition");
			$pageBgPosition.val($pageBgPosition.data("val"));
			$pageBgPosition.change(function(){
				UserTheme.bgPosition($(this), UserTheme.page);
			});
		},
		pagePhotoDel : function(){
			$("#J_PagePhotoDel").click(function(){
				UserTheme.photoDel($(this), UserTheme.page, $("#J_PagePicBtn"));
			});
		},
		init : function(){

			UserTheme.style.pageBgColor();
			UserTheme.style.pageBgRepeat();
			UserTheme.style.pageBgAttachment();
			UserTheme.style.pageBgPosition();
			UserTheme.style.pagePhotoDel();
		}
	},
	//保存theme 美化设置
	userThemeStyleSave : function($this){
		var styleData = {
			"themeId" : parseInt(UserTheme.themeId),
            "pageBgColor" : $("#J_PageBgColor").data("colorval"), //背景颜色
            "pageBgImage" : $("#J_PagePicBtn").data("src"),//背景图片 移除空值"",第一次是/tmp/topic/xxx.jpg
            "pageBgRepeat" : ($("#J_PageBgRepeat").val() == 1) ? "no-repeat" : "repeat", //
            "pageBgAttachment" : ($("#J_PageBgAttachment").val() == 1) ? "scroll" : "fixed", //"scroll" || "fixed" 页面背景锁定
            "pageBgPosition" : $("#J_PageBgPosition").val()	//"left" || "center" || "right" 页面背景位置

		}
		//console.log(styleData)
		//console.log(JSON.stringify(styleData))
		$.ajax({
			url :"/theme/editStyle",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(styleData),
			success : function(data){
				switch(data.code){
					case "100" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-ok";
						$.smeite.tip.show($this,"保存成功！");
					//	$("#J_HeaderPicBtn").data("src", data.headerPic);
						$("#J_PagePicBtn").data("src", data.pagePic);
						break;
					case "101" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($this,"保存不成功，重新提交试试！");
						break;
					case "102" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($this,"参数错误！");
						break;
					case "105" :
						$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
						$.smeite.tip.show($this,"亲，您没权限哦！");
				}
				
			},
			error : function(){
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"服务器欧巴乘凉去了，重新提交试试！");
			}
		});
	},
	//美化页面关闭与打开
	userThemeStyleClose : function(){
		var $style = $("#J_UserThemeStyle");
		$("#J_UserThemeStyleOpenBtn").click(function(event){

			event.preventDefault();
			if($style.css("display") == "block"){
				$style.slideUp();
			}else{
				$style.slideDown();
			}
			
		});
		$("#J_UserThemeStyleCloseBtn").click(function(event){
			event.preventDefault();
			$style.slideUp();
		});
	},
	//初始化
	init : function(){
		$("#J_SubTitle").html($.trim($("#J_SubTitle").html()).replace(/\[br\]/g,"<br/>"));
		
		//删除主题
		$("#J_UserThemeDel").click(function(event){
			event.preventDefault();
			//确认框
            $.smeite.confirmUI("真要删除这个主题吗？删了就没了哦...",UserTheme.deleteUserTheme,function(){
				
			});
		});
		//删除商品
		$("#GoodsWall .ilike-del").die().live("click",function(){
			var $delBtn = $(this),
			$goodsItem = $delBtn.parents(".goods"),
			productId = $delBtn.data("proid"),
			ajaxUrl ="/theme/removeGoods",
			ajaxData = {
				themeId: parseInt(UserTheme.themeId),
				goodsId: parseInt(productId)
			};
			$.ajax({
				   url: ajaxUrl,
                type:"post",
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                data:JSON.stringify(ajaxData),
				   success: function(data){
					   switch(data.code){
					       case("100"):
						       $goodsItem.addClass("goods-gray");
						       break;
					       case("101"):
						       alert(data.msg);
					   }  
				   }
			});
		});
		
		UserTheme.userThemeStyleClose();
		
		$("#J_PagePicBtn").click(function(){
			UserTheme.picUpload($(this), "Page");
		});
		
		/*页面美化*/
		UserTheme.style.init();

		/*保存设置*/
		$("#J_ThemeStyleSave").click(function(){

			UserTheme.userThemeStyleSave($(this));
		});
		
		//分享外站商品
		$("#J_UserProductUrl").focus(function(){

            if(!$.smeite.dialog.isLogin()){
                return false;
            }
			if(SMEITER.isBlack=="true"){
				alert("您的分享功能已被禁用");
				return false;
			}
			var $this = $(this),
			$submitBtn = $("#J_UserAddProduct");
			if(!$("#J_ShareGoodsTipD")[0]){
				var html = "";
				html += '<div id="J_ShareGoodsTipD" class="g-dialog sg-dialog">';
				html += '<div class="content">';
				html += '<div class="sg-text-tip"></div>';
				html += '<div class="sg-source">';
				html += '<p class="pt5 pb5">已支持网站（<a href="http://smeite.com/contactUs" target="_blank">商家申请加入</a>）：</p>';
				html += '<div class="source-list clearfix">';
				html += '<a class="icon-source icon-taobao" href="http://www.taobao.com/" target="_blank">淘宝网</a>';
				html += '<a class="icon-source icon-tmall" href="http://www.tmall.com/" target="_blank">天猫商城</a>';
				html += '</div>';
				html += '</div>';
				html += '</div>';
				html += '</div>';
				$("body").append(html);
				var $sgTipD = $("#J_ShareGoodsTipD");
				$("#J_UserProductUrl").blur(function(){
					$sgTipD.hide();
				});
				$sgTipD.click(function(){
					$sgTipD.show();
				});
				$submitBtn.click(function(){
					var url = $.trim($this.val()),
					$textTip = $(".sg-text-tip");
					$sgTipD.show();
					if(url==""){
						$textTip.html('<span class="errc">宝贝网址不能为空~</span>').show();
					}else if(!$.smeite.util.validSite(url)){
						$textTip.html('<span class="errc">暂时还不支持这个网站呢~</span>').show();
					}else{
						$.ajax({
				  	 		url:"/ugc/api/findProduct",
				   			type : "get",
				   			dataType: "json",
				   			data: {
				   				url: url
				   			},
				   			beforeSend: function(){
				   				$textTip.html('<span class="gc6">宝贝信息抓取中…</span>').show();
								$submitBtn.disableBtn("bbl-btn");
				   			},
				  		 	success: function(data){
					    		if(data.code==100){
    								$sgTipD.hide();
                                    $("#J_UserProductUrl").attr("value","")
                                    $.smeite.ugc.goodspub(data.product,UserTheme.addProduct);
    							}else if(data.code==105 || data.code==108){
    								$sgTipD.hide();
                                    $("#J_UserProductUrl").attr("value","")
                                    $.smeite.ugc.goodsExist(data.product,UserTheme.addProduct);
    							}else if(data.code==101 || data.code==106){
    								$textTip.html('<span class="errc">宝贝信息抓取失败，请重试…</span>').show();
    							}else if(data.code==107){
    								$textTip.html('<span class="errc">暂时还不支持这个宝贝…</span>').show();
    							}else if(data.code==110){
    								$textTip.html('<span class="errc">亲，该商品所在商家已列入黑名单，申诉请联系service@smeite.com</span>').show();
    							}else if(data.code==444){
    								alert("你已被禁止登录！");
    								window.location.href="http://smeite.com/logout";
    							}else if(data.code==442){
    								alert("亲，请不要频繁推荐同一店铺商品");
    							}else if(data.code==443){
    								alert("由于过度推荐同店铺商品，账户已冻结，如有疑问请联系 service@smeite.com");
    							}else if(data.code==445){
    								alert("城管大队怀疑你恶意发广告，将禁止你发布商品的权利，申诉请联系service@smeite.com");
    							}
    							$submitBtn.enableBtn("bbl-btn");
				   			}
						});
					}
				});
			}else{
				$(".sg-text-tip").html("");
			}
			var position = $.smeite.util.getPosition($this).leftBottom(),
			dLeft = position.x,
			dTop = position.y + 5;
			$("#J_ShareGoodsTipD").css({	
				right: "auto",			
				left: dLeft + "px",
				top: dTop + "px"
			}).fadeIn("fast");
		});
	}
}
UserTheme.init();

    /* 排列商品*/
    //画报
    $.smeite.goods.conf.colArray = [0,0,0,$(".side").outerHeight()+13];
	$.smeite.goods.conf.justFlow = true;
    $.smeite.goods.init();
    /*  分享 */
    $(".btn-share").shareBtn()
});