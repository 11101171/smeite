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
	var $ = require("$");
var UserTheme = {
	themeId : $("#J_themeId").val(),
	//删除主题
	deleteUserTheme : function(){
		var $del = $("#J_UserThemeDel"),
            dataType=$del.data("type"),
            themeId =$del.data("id");
		$.ajax({
			url:"/theme/delete",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                themeId:themeId,
                dataType:dataType
            }),
			success : function(data){
				switch(data.code){
					case "100" :
						var html = "";
						html += '<p class="success-text"><span class="correct">主题已删除成功！</span></p>';
						html += '<p class="clearfix"><span class="fl mr10 l30">5秒后将</span><a class="bbl-btn goCheck" href="/user/' + SMEITER.userId + '/theme">返回我的主题</a>';
						html += '<a class="bgr-btn closeD ml10" href="javascript:;">关闭</a></p>';
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
							goodsHtml += 		'<a class="ilike-del" href="javascript:;" data-id="' + data.product.id + '" style="display: none; ">删除</a>';
							goodsHtml += 		'<a class="ilike-m" href="javascript:;" data-type="0" data-id="' + data.product.id + '" style="display:none;">喜欢</a>';
							goodsHtml += 		'<a class="ilike-topic" href="javascript:;" data-id="' + data.product.id + '" style="display:none;">加入主题</a>';
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
	//初始化
	init : function(){

		//删除主题
		$("#J_UserThemeDel").click(function(event){
			event.preventDefault();
			//确认框
            $.smeite.confirmUI("真要删除这个主题吗？删了就没了哦...",UserTheme.deleteUserTheme,function(){
				
			});
		});
		//删除商品
		$("#GoodsWall .ilike-del").off().on("click",function(){
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
						       alert(data.message);
					   }  
				   }
			});
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
   var ThemeDiscuss = {
        //评论与回复提交前校验
        submit : function($this){
            $this.attr('disabled',true);
            var $postComment = $("#J_postComment");
            var $textarea = $postComment.find("textarea");
             $("#J_quoteContent").val($("#J_postQuote").html())

            var comment = {
                "themeId":parseInt($("#J_themeId").val()),
                "quoteContent": $("#J_postQuote").html(),
                "content": $("#J_commentContent").val()
            };

            if($.smeite.dialog.isLogin()){
                if($.trim($textarea.val()) == ""){
                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.smeite.tip.show($this,"亲，评论内容不能为空哦！");
                    $this.attr('disabled',false);
                }else if($.smeite.util.getStrLength($textarea.val()) >= 140){
                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.smeite.tip.show($this,"内容小于140字！");
                    $this.attr('disabled',false);
                }else{
                    $this.attr('disabled',false);
                       alert(comment.themeId)
                    $.ajax({
                        url: $("#J_postComment").attr("action"),
                        type : "POST",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify(comment),
                        beforeSend: function(){
                            $this.disableBtn("bbl-btn");
                        },
                        success: function(data){
                            if(data.code=="100"){
                                $this.enableBtn("bbl-btn");

                                var html ='<li>';
                                html += '<div class="share-avt">';
                                html +='<a class="fl" href="/user/'+SMEITER.userId+'"target="_blank">';
                                html +='<img class="avt32 fl" src="'+SMEITER.userPhoto+'" width="38" height="38">';
                                html +='</a>';
                                html +="</div>";
                                html +=' <span class="arrow"></span>';
                                html +=' <div class="share-user">';
                                html +='<h3> <a class="J_userNick" href="/user/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick+'</a> <p class="user-title"></p></h3>';
                                html +=' <p class="quote-content">'+comment.quoteContent+'</p>';
                                html +=' <p class="content J_commentCon">'+comment.content +'</p>';
                                html +='<div class="item-doing"> <a class="reply J_postReply"  href="javascript:;">回复</a><span class="time">刚刚</span> </div>';
                                html +='</div>';
                                html +='</li>';

                                $("#J_commentList").append(html);

                            }
                        }
                    });
                    return false
                }
            }
        },


        //通用讨论组初始化
        init : function(){
            //评论与回复
            var $postQuote = $("#J_postQuote");
            var $postComment = $("#J_postComment");

            //点击回复
            $(document).on("click",".J_postReply",function(){

                var $li = $(this).closest("li");
                var userNick = $li.find(".J_userNick:first").html();
                var commentCon = $li.find(".J_commentCon").html();
                var time = $li.find(".time").html();
                var quoteHtml = "";
                quoteHtml += '<blockquote>';
                quoteHtml += '<span class="info">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
                quoteHtml += '<p>' + $.trim(commentCon) + '</p>';
                // quoteHtml +='<a class="close">X</a>';
                quoteHtml += '</blockquote>';

                $postQuote.html(quoteHtml);
                $("html, body").scrollTop($postComment.offset().top -50);
                //删除引用回复
                $postQuote.find(".close:first").unbind("click").click(function(){
                    $postQuote.html("");
                });
            });

            $("#J_postCommentSubmit").click(function(event){
                event.preventDefault();
                ThemeDiscuss.submit($(this));
            });

            $postComment.find("textarea").focus(function(){
                $.smeite.dialog.isLogin();
            });

            //回车键提交评论
            $postComment.find("textarea").on("keyup",function(e){
                var $this = $(this);
                $.smeite.util.submitByEnter(e, function(){
                    ThemeDiscuss.submit($("#J_postCommentSubmit"));
                });
            });

        }


    }

    $(function(){
        UserTheme.init();
        ThemeDiscuss.init()
        $.ajax({
            url: "/theme/getComments",
            type : "GET",
            dataType:"html",
            data:{themeId:parseInt($("#J_themeId").val())},
            success: function(data){
                $("#J_commentList").append(data);
            }
        });
        $(document).on("click","a.commentPage",function(){
            var p =$(this).data("page");
            var themeId = $(this).data("themeid");
            $.ajax({
                url:"/theme/getComments?themeId="+themeId+"&p="+p,
                type:"get",
                dataType:"html",
                success:function (data) {
                    $("#J_commentList").html(data)
                }})
        })





        /*  分享 */
        $(".btn-share").shareToThird()
    })

});