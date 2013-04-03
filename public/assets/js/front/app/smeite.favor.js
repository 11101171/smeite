/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 功能JS：喜欢 、关注 等操作
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-26 22:00
 * ModifyContent:删除注释 增加goods瀑布流显示
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ =jQuery= require("jquery");
 
//(function($) { 
	//喜欢、值得、不值得等操作
	$.smeite.favor = {
		//喜欢第一次提交
		loveBaobeiCallback : function(o){},
		//喜欢重复提交
		repeatLoveBaobeiClk : function(o){},
		//提交按钮操作
		awardClk : function(o){
			var html = ""
			+'<div id="awardDialog" class="g-dialog" style="width:400px;z-index: 9999; top: 363.5px; left: 643px; position: fixed; display: block; ">'
				+'<div class="dialog-content">'
					+'<div class="hd"><h3>你喜欢，我买单</h3></div>'
					+'<div class="bd clearfix tac">'
						+'<span style="color:#E26;font-size:16px;font-weight:bold;line-height:30px;">恭喜，你中奖了</span><br/>'
						+'<span style="font-size:14px;line-height:30px;">请在规定时间内领取神秘奖品</span><br/>'
						+'<a href="/account/award" target="_blank" class="bbl-btn award-tag mt20" style="margin-left:148px;cursor:pointer;">查看</a>'
					+'</div>'
					+'<a class="close" href="javascript:;"></a>'
				+'</div>'
			+'</div>';
			$("body").append(html);
			$("#awardDialog").overlay({
				top: 'center',
				mask: {
					color: '#000',
					loadSpeed: 200,
					opacity: 0.3
				},
				closeOnClick: true,
				load: true,
				onClose: function() {
					$("#awardDialog,#exposeMask").remove();
				}				
			});
			$(".award-tag").unbind();
			$(".award-tag").bind("click",function(){
				$("#awardDialog,#exposeMask").overlay().close();
				return true;
			})	
		},
		loveBaobeiSubmit:function(o, productId){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
			var ajaxUrl = "/goods/favor";
         //   alert(productId)
			$.ajax({
			   url:  ajaxUrl,
			   type : "post",
                contentType:"application/json; charset=utf-8",
			   dataType: "json",
			   data: JSON.stringify({"id": productId }),
			   success: function(data){
				   switch(data.code){
				   		case "100" :

				   			$.smeite.favor.loveBaobeiCallback(o);
				   			break;
				   		case "101":
				   			$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				   			$.smeite.tip.show(o, data.message);
				   		//	$.smeite.favor.loveBaobeiCallback(o);
				   			break;
				   		case "103" : //喜欢、值得、不值得 重复提交后数据不做操作
				   			$.smeite.favor.repeatLoveBaobeiClk(o);
				   			break;
						case 666 : //中奖了！
				   			$.smeite.favor.awardClk(o);
				   			break;
				   }
			   }
			});			
		},

		//喜欢帖子成功
        loveTopicCallback : function(o, commentType, desirableType){},
        //喜欢帖子重复
        repeatLoveTopicClk : function(o, commentType, desirableType){},
        //喜欢帖子操作
        loveTopicSubmit : function(o,topicId){
            var ajaxUrl = "/topic/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"topicId": topicId }),
                success: function(data){
                    //console.log(data.code)
                    switch(data.code){
                        case "100":
                            //console.log("no-repeate")
                            $.smeite.favor.loveTopicCallback(o,data);
                            break;
                        case "101" ://错误
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o,data.msg);
                            break;
                        case "103" : //喜欢重复
                            //console.log("repeate")
                            $.smeite.favor.repeatLoveTopicClk(o,data);
                            break;
                        case "200" : //未登录
                            $.smeite.dialog.login();
                            break;
                    }
                }
            });
        },
        // 取消喜欢帖子
        removeTopicCallback:function(o,topicId){
            var ajaxUrl = "/topic/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"topicId": topicId }),
                success: function(data){
                    switch(data.code){
                        case "100":

                            break;
                        case "101":
                            alert(data.message);
                    }
                }
            });
        },
        //喜欢主题成功
        loveThemeCallback : function(o, commentType, desirableType){},
        //喜欢主题重复
        repeatLoveThemeClk : function(o, commentType, desirableType){},
        //喜欢主题操作
        loveThemeSubmit : function(o,themeId){
            var ajaxUrl = "/theme/addFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"themeId": themeId }),
                success: function(data){
                    //console.log(data.code)
                    switch(data.code){
                        case "100":
                            //console.log("no-repeate")
                            $.smeite.favor.loveThemeCallback(o,data);
                            break;
                        case "101" ://错误
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o,data.msg);
                            break;
                        case "103" : //喜欢重复
                            //console.log("repeate")
                            $.smeite.favor.repeatLoveThemeClk(o,data);
                            break;
                        case "200" : //未登录
                            $.smeite.dialog.login();
                            break;
                    }
                }
            });
        },
        /*取消喜欢的主题*/
        removeThemeCallback : function(o,themeId){
            var ajaxUrl = "/theme/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"themeId": themeId }),
                success: function(data){
                    switch(data.code){
                        case "100":
                            o.addClass("goods-gray");
                            break;
                        case "101":
                            alert(data.message);
                    }
                }
            });
        }
	};
	
	


});