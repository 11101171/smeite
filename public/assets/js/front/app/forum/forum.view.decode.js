define(function(require, exports) {
	var $ = jQuery = require("jquery");
	require("smeite");
    require("app/smeite.editor");
/*
 * Copyright 2011-2012, Guang.com
 * @contain: 解析帖子内容
 * @depends: jquery.js
 * 使用逛的解决方案
 */

//(function ($) {
	//to do 可以将要decode的html用&##&连接在一起decode,完成后split,这样重复的decode工作变成一次完成，装配html还是那么多次
	if($.smeite.Editor){
		//var time = new Date();
		var editor = $.smeite.Editor;
		var contentDecode = editor.contentDecode;
		$("#J_PostArtical").each(function(){
			var $this = $(this);
			var html = $this.html();
			html = contentDecode.call(editor,html);
			$this.html(html);
		})
		$("#J_ForumPost .J_PostCon").each(function(){
			var $this = $(this);
			var html = $this.html();
			html = contentDecode.call(editor,html);
			$this.html(html);
		})
		$("#J_ForumPost .post-quoteContent").each(function(){
			var $this = $(this);
			var html = $this.html();
			html = editor.onlyDecodeFace.call(editor,html);
			$this.html(html);
		})
		//var time2 = new Date();
		//alert(time2-time);
		$("#J_ForumPost").show();
		if(editor.imgviewDoms){
			editor.imgDecode();
		}
		if(editor.baobeiviewDoms){
			editor.baobeiDecode();
		}
	}

    $(".ilike-n").live("click",function(){
        var $this = $(this);
        if($this.data("enable") == "disable"){
            return;
        }
        $this.data("enable","disable");
        setTimeout(function(){
            $this.data("enable","enable");
        },1000)
        var commentType = $this.attr("data-type");
        var productId = $this.attr("data-proid");
        var $likeCount = $this.find(".ilikeCount");
        var likeTimeout = null;

        $.smeite.favor.repeatLoveBaobeiClk = function (b) {
            $this.data("enable","enable");
            $("#cmtDialog")[0] && (clearTimeout(e), $("#cmtDialog").remove());
            var html='<div id="cmtDialog" class="c-dialog" style="width:150px;">' +
                '<p class="title clearfix">' +
                '<a class="cmtclose fr" href="javascript:;">x</a>' +
                '>_<喜欢过了~</p>' +
                "</div>";
            $("body").append(html);
            b = $.smeite.util.getPosition(b).topMid();
            c = $("#cmtDialog").outerWidth();
            var f = $("#cmtDialog").outerHeight();
            $("#cmtDialog").css({left:b.x - c / 2 + "px", top:b.y - f - 12 + "px"}).fadeIn();
            e = setTimeout(function () {
                $("#cmtDialog").fadeOut()
            }, 3E3);

        };
        $.smeite.favor.loveBaobeiCallback = function (b) {
            $this.data("enable", "enable");
            $("#J_LikeDialog")[0] && (clearTimeout(e), $("#J_LikeDialog").remove());
            var  html = ('<div id="J_LikeDialog" class="c-dialog" style="width:150px;">' +
                '<p class="title clearfix">' +
                '<a class="cmtclose fr" href="javascript:;">x</a>' +
                '喜欢了~</p>' +
                '</div>');
            $("body").append(html);
            /*修改喜欢的数量*/
            var  c = parseInt($likeCount.data("val")) + 1;
            $likeCount.text("喜欢(" +  c + ")");
            $likeCount.data("val", c);
            /*dialog 显示*/
            b = $.smeite.util.getPosition(b).topMid();
            c = $("#J_LikeDialog").outerWidth();
            var f = $("#J_LikeDialog").outerHeight();
            $("#J_LikeDialog").css({left:b.x - c / 2 + "px", top:b.y - f - 12 + "px"}).fadeIn();
            var e = setTimeout(function () {
                $("#J_LikeDialog").fadeOut()
            }, 3E3);

        };
        $.smeite.favor.loveBaobeiSubmit($this, parseInt(productId));
    })


/*
	$(".ilike-n").live("click",function(){
		//if($.guang.dialog.isLogin()){
			var $this = $(this);
			if($this.data("enable") == "disable"){
				return;
			}
			$this.data("enable","disable");
			setTimeout(function(){
				$this.data("enable","enable");
			},1000)
			var commentType = $this.attr("data-type");
			var productId = $this.attr("data-proid");
			var likeTimeout = null;
			$.smeite.repeatLoveBaobeiClk = function(o, commentType){
				$this.data("enable","enable");
				if($("#cmtDialog")[0]){
					clearTimeout(likeTimeout);
					$("#cmtDialog").remove();
				}
				var html = "";
				html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
				html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;喜欢过了~</p>';
				html += '<a id="J_CommentTag" class="sbl-btn speakmore" data-type="3" data-proid="'+ productId +'">再说两句</a>'
				html += '</div>';
				$("body").append(html);
				$(".cmt-txa").focus(function(){
					var T = $("#cmtDialog").offset().top;
					$("#cmtDialog").css({
						top: T - 50 + "px",
						height: 104
					});
					$(this).height(50);
					$(".cmt-txa").unbind("focus");
				});
				var position = $.smeite.util.getPosition(o).topMid();
				var W = $("#cmtDialog").outerWidth(),
				H = $("#cmtDialog").outerHeight();
				$("#cmtDialog").css({
					left: position.x - W/2 + "px",
					top: position.y - H - 12 + "px"
				}).fadeIn();
				likeTimeout = setTimeout(function(){$("#cmtDialog").fadeOut().remove();},3000)
				$("#J_CommentTag").unbind("click").bind("click",function(){
					$.smeite.cmt.commentAndAddTagsSubmitOkClk = function(cmtContent,callbackPramToUI){
					}
					var from = "fromNonDetail";
					$this.attr("data-type","3");
					$.smeite.cmt.commentAndAddTags($this,"","","","","",[],from);
				})
			}
			$.smeite.favor.loveBaobeiCallback = function(o, commentType){
				$this.data("enable","enable");
				if(SMEITER.userId==""){
						var likeProduct= $.cookie('productFavor');
						if(likeProduct!=null&&likeProduct!=""){
							var likeProductLength = likeProduct.split(",").length;
							$(".unlogin-like-text .count").text(likeProductLength);
							$(".unlogin-like").show();
						}
					}
				var html = "";
				html += '<div id="cmtDialog" class="c-dialog">';
				html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>喜欢了~</p>';
				html += '<a id="J_CommentTag" class="sbl-btn speakmore" data-type="0" data-proid="'+ productId +'">再说两句</a>'
				html += '</div>';
				$("body").append(html);
				$(".cmt-txa").focus(function(){
					var T = $("#cmtDialog").offset().top;
					$("#cmtDialog").css({
						top: T - 50 + "px",
						height: 104
					});
					$(this).height(50);
					$(".cmt-txa").unbind("focus");
				});
				var position = $.smeite.util.getPosition(o).topMid();
				var W = $("#cmtDialog").outerWidth(),
				H = $("#cmtDialog").outerHeight();
				$("#cmtDialog").css({
					left: position.x - W/2 + "px",
					top: position.y - H - 12 + "px"
				}).fadeIn();
				likeTimeout = setTimeout(function(){$("#cmtDialog").fadeOut().remove();},3000)
				$("#J_CommentTag").unbind("click").bind("click",function(){
					$.smeite.cmt.commentAndAddTagsSubmitOkClk = function(cmtContent,callbackPramToUI){
					}
					var from = "fromNonDetail";
					$.smeite.cmt.commentAndAddTags($this,"","","","","",[],from);
				})
			}
			$.smeite.judgement.identityOper($this, productId, commentType);
			isLikeEnable = "true";
		//}
	});
*/


//})(jQuery)


});
