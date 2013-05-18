define(function(require, exports) {
	var $ = jQuery = require("jquery");
	require("smeite");
/*
 * 讨论组交互js，含品牌、主题、场景
 */

//(function($) {
	//讨论组组件
	$.smeite.forum = {
		forumO : $("#J_Forum"),
		//讨论组id
		forumIdO : $("#J_ForumId"),
		//讨论组话题
		forumTopicIdO : $("#J_ForumTopicId"),
		loverSubmitEnable : true,
		//编辑帖子通过表单校验可提交
		postEditEnable : false,
		
		//通用计数
		count : function(limitNum, $wordCount){
			var limitNum = 35;
			var $countNum = $wordCount.find(".J_CountNum:first");
			var $countTxt = $wordCount.find(".J_CountTxt:first");
			$.smeite.wordCount.conf.errorClk = function(newLen){
				$countNum.text('trimming...  ' + (limitNum - newLen));
			}
			$.smeite.wordCount.init($countTxt, $countNum, limitNum, 20);
		},
	//评论与回复提交前校验
		postCommentSubmit : function($this){
			$this.attr('disabled',true);
			if($.smeite.editor){
				if($.smeite.editor.html2text($.smeite.editor.iframeDocument.body.innerHTML)==""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不能为空哦！");
					$this.attr('disabled',false);
					return;
				}
				if($.smeite.util.getStrLength($.smeite.editor.iframeDocument.body.innerHTML) >= 500){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不得超过500字");
					$this.attr('disabled',false);
					return;
				}
				$("#J_ForumPostCon").val($.smeite.editor.iframeDocument.body.innerHTML);
			}
			var $postCmtArea = $("#J_PostCmtArea");
			var $textarea = $postCmtArea.find("textarea");
           $("#J_replyContent").val($("#J_PostCmtSegment").html())
			if($.smeite.dialog.isLogin()){
				if($.trim($textarea.val()) == ""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"亲，评论内容不能为空哦！");
					$this.attr('disabled',false);
				}else if($.smeite.util.getStrLength($textarea.val()) >= 500){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容小于10000字！");
					$this.attr('disabled',false);
				}else{
				$this.attr('disabled',false);
				$postCmtArea.submit();

				}
			}
		},

		//帖子创建与编辑
		postEditSubmit : function($this){
			var title = $.trim($("#J_ForumPostTitle").val());
			if($.smeite.editor){
				if($.trim($.smeite.editor.html2text($.smeite.editor.iframeDocument.body.innerHTML))==""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"标题和内容不能为空哦！");
					$this.attr('disabled',false);
					return;
				}
				if($.smeite.util.getStrLength($.smeite.editor.iframeDocument.body.innerHTML) >= 1000){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不得超过1000字");
					$this.attr('disabled',false);
					return;
				}

				$("#J_ForumPostCon").val($.smeite.editor.iframeDocument.body.innerHTML);
			}
			var content = $("#J_ForumPostCon").val();
			if(title == "" || $.trim(content) == ""){
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"标题和内容不能为空哦！");
			}else if($.smeite.util.getStrLength(title) > 50 || $.smeite.util.getStrLength(content) >= 1000){
				
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"亲，标题<50字，内容<1000字");
			}else{
                $("#J_ForumPostEditForm").submit();

			}
		},



		//通用讨论组初始化
		init : function(){
			//评论与回复
			var $postCmtSegment = $("#J_PostCmtSegment");
			var $postCmtArea = $("#J_PostCmtArea");
			//评论
			$("#J_PostCmt").click(function(){
				$("html, body").scrollTop($postCmtArea.offset().top - 50);
				if($.smeite.editor){
					$.smeite.editor.iframe.contentWindow.focus();
				}else{
					$postCmtArea.find("textarea").focus();
				}
			});
			
			//评论格式化
			if($(".J_PostCon")[0]){
				$(".J_PostCon").each(function(){
					$(this).html($.smeite.util.trim($(this).html()).replace(/\n/g,"<br/>"));
				});
			}
			
			//回复
			$(".J_PostCmtReply").click(function(){

                var $li = $(this).closest("li");
                var userNick = $li.find(".J_UserNick:first").html();
                var replyCon = $li.find(".J_PostCon").html();
                var time = $li.find(".time").html();
                var segmentHtml = "";
                segmentHtml += '<blockquote>';
                segmentHtml += '<span class="info g-daren">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
                segmentHtml += '<p>' + $.trim(replyCon) + '</p>';
                segmentHtml += '</blockquote>';
                //console.log(segmentHtml);
                $postCmtSegment.html(segmentHtml);

                $("html, body").scrollTop($postCmtArea.offset().top -50);

                //删除回应
                $postCmtSegment.find(".close:first").unbind("click").click(function(){
                    $postCmtSegment.html("");
                });
			});
			
			$("#J_CmtPublishBtn").click(function(event){
				event.preventDefault();
				$.smeite.forum.postCommentSubmit($(this));
			});
			
			$postCmtArea.find("textarea").focus(function(){
				$.smeite.dialog.isLogin();
			});
			
			//回车键提交评论
			$postCmtArea.find("textarea").live("keyup",function(e){
				var $this = $(this);
				$.smeite.util.submitByEnter(e, function(){
					$.smeite.forum.postCommentSubmit($("#J_CmtPublishBtn"));
				});
			});


			//话题创建与编辑
			if($("#J_ForumPostEditBtn")[0]){
				$("#J_ForumPostTitle").focus(function(){
					$.smeite.dialog.isLogin();
				});
				$("#J_ForumPostCon").focus(function(){
					$.smeite.dialog.isLogin();
				});
				$("#J_ForumPostEditBtn").click(function(event){
					event.preventDefault();
					if($.smeite.dialog.isLogin()){
						$.smeite.forum.postEditSubmit($(this));
					}
				});
			}
			


			
			//评论精确定位
		/*	if($("#J_ForumCmtId")[0] && $("#J_ForumCmtId").val() != ""){
				var $cmtLi = $("#J_ForumPost").find("li[data-cmtid=" + $("#J_ForumCmtId").val() + "]");

			}*/
			

			
			//帖子内容格式化
			if($("#J_PostArtical")[0]){
				$("#J_PostArtical").html($("#J_PostArtical").html().replace(/\n/g,"<br/>"));
			}
		}


	}
	$.smeite.forum.init();


	
//})(jQuery);





});