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
				if($.smeite.util.getStrLength($.smeite.editor.iframeDocument.body.innerHTML) >= 10000){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不得超过10000字");
					$this.attr('disabled',false);
					return;
				}
				$("#J_ForumPostCon").val($.smeite.editor.contentEncode($.smeite.editor.iframeDocument.body.innerHTML));
			}
			var $postCmtArea = $("#J_PostCmtArea");
			var $textarea = $postCmtArea.find("textarea");
			if($.smeite.dialog.isLogin()){
				if($.trim($textarea.val()) == ""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"亲，评论内容不能为空哦！");
					$this.attr('disabled',false);
				}else if($.smeite.util.getStrLength($textarea.val()) >= 10000){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容小于10000字！");
					$this.attr('disabled',false);
				}else{
					//$this.attr('disabled',false);
//					$postCmtArea.submit();
					$.ajax({
						url : "/forum/validate",
						type : "post",
						dataType : "json",
						data : {
							content : $textarea.val()
						},
						success : function($json){
							switch($json.code){
							case 100 : {
								$postCmtArea.submit();
							}break;
							case 101 : {
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
								$.smeite.tip.show($this,$json.msg);
								$this.attr('disabled',false);
							}break;
							}
						}
					});
				}
			}
		},
		/*//添加相关经验
		userAddExperience : function($this){
			if(!$("#J_ForumExperienceD")[0]){
				var html = '';
				html += '<div id="J_ForumExperienceD" class="g-dialog" style="width:370px;">';
				html += 	'<div class="dialog-content">';
				html += 		'<div class="hd"><h3>相关经验</h3></div>';
				html += 		'<div class="bd clearfix" id="J_ExperienceCount">';
				html += 			'<p>描述你在这方面的经验，让大家更信你说的～</p>';
				html += 			'<textarea class="J_CountTxt b-textarea" placeholder="" style="width:300px;height:35px;margin:10px auto 15px auto;"></textarea>';
				html += 			'<a style="margin:0px 10px 0 0px;" class="bbl-btn J_BtnOk">保存</a><a class="bgr-btn J_BtnCancel">取消</a><p style="float:right;color:#999;">还可以输入<em class="J_CountNum">35</em>字</p>';
				html += 		'</div>';
				html += 	'</div>';
				html += 	'<a class="close" href="javascript:;"></a>';
				html += '</div>';
				$("body").append(html);
				$("#J_ForumExperienceD").overlay({
					top: 'center',
					mask: {
						color: '#000',
						loadSpeed: 200,
						opacity: 0.3
					},
					closeOnClick: false,
					load: true			
				});
			}else{
				$("#J_ForumExperienceD").overlay().load();
			}
			//计数
			$.guang.forum.count(35, $("#J_ExperienceCount"));
			//提交
			var $dialog = $("#J_ForumExperienceD");
			$dialog.find(".J_BtnOk:first").unbind("click").click(function(){
				var content = $.trim($dialog.find("textarea").val());
				if(content == ""){
					$.guang.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.guang.tip.show($(this),"相关经验不能为空哦！");
				}else if($.guang.util.getStrLength(content) > 35){
					$.guang.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.guang.tip.show($(this),"相关经验只能输入35字哦！");
				}else{
					$.ajax({
						url : GUANGER.path + "/forum/userAddExperience",
						type : "post",
						dataType : "json",
						data : {
							forumId : $.guang.forum.forumIdO.val(),
							content : content
						},
						success : function($json){
							switch($json.code){
								case 100 : {
									//相关经验返回值$json.content,有短链接
									$(".J_UserAddExperience").html($json.content);
									$dialog.overlay().close();
								}break;
								case 101 : {
									$.guang.tip.conf.tipClass = "tipmodal tipmodal-error";
									$.guang.tip.show($(this), $json.msg);
								}break;
							}
						},
						error : function(){
							
						}
					});
				}
			});
			$dialog.find(".J_BtnCancel:first").unbind("click").click(function(){
				$dialog.overlay().close();
			});
		},*/
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
				if($.smeite.util.getStrLength($.smeite.editor.iframeDocument.body.innerHTML) >= 10000){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不得超过10000字");
					$this.attr('disabled',false);
					return;
				}
				$("#J_ForumPostCon").val($.smeite.editor.contentEncode($.smeite.editor.iframeDocument.body.innerHTML));
			}
			var content = $("#J_ForumPostCon").val();
			//console.log(content.length);
			//console.log($.trim(content).length);
			if(title == "" || $.trim(content) == ""){
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"标题和内容不能为空哦！");
			}else if($.smeite.util.getStrLength(title) > 50 || $.smeite.util.getStrLength(content) >= 10000){
				
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"亲，标题<50字，内容<10000字");
			}else{
                $("#J_ForumPostEditForm").submit();
				/*if($("#J_UserSns a").length>0){
				var snsArr = [];
				for(var i=0;i<$("#J_UserSns a").length;i++){
					if($("#J_UserSns a").eq(i).data("status")=="on"){
						var snsSiteId = $("#J_UserSns a").eq(i).data("snsid");
						snsArr.push(snsSiteId);
					}
				}
				$("input[name=sites]").val(snsArr.join(","));
				}*/
				/*$.ajax({
					url :"/forum/validate",
					type : "post",
					dataType : "json",
					data : {
						title : title,
						content : content
					},
					success : function($json){
						switch($json.code){
						case 100 : {
							$("#J_ForumPostEditForm").submit();
						}break;
						case 101 : {
							$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
							$.smeite.tip.show($this,$json.msg);
						}break;
						}
					}
				});*/
			}
		},


	/*	lover : function($this){
			//if($.guang.dialog.isLogin()){
				if($this.data("enable") == "disable"){
					return;
				}
				$this.data("enable","disable");
				setTimeout(function(){
					$this.data("enable","enable");
				},2000)			
				$.smeite.favor.loveTopicSubmit($this,{
					url:$this.data("loverurl")
				});
				$.smeite.favor.loveTopicCallback = function(o,data){
					$("#J_LikeCount").text(parseInt($("#J_LikeCount").text()) + 1);
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
					html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>关注了~</p>';
					html += '<a href="' + $this.data("backurl") + '/forum" class="sbl-btn speakmore">参与讨论</a>'
					html += '</div>';
					$("body").append(html);
					var position = $.smeite.util.getPosition(o).topMid();
					var W = $("#cmtDialog").outerWidth(),
					H = $("#cmtDialog").outerHeight();
					$("#cmtDialog").css({
						left: position.x - W/2 + "px",
						top: position.y - H - 12 + "px"
					}).fadeIn();	
					setTimeout(function(){$("#cmtDialog").fadeOut().remove();},3000)
					$this.data("enable","enable");
				}
				$.smeite.favor.repeatLoveTopicClk = function(o,data){
					var html = "";
					html += '<div id="cmtDialog" class="c-dialog" style="width:180px">';
					html += '<p class="title clearfix"><a class="cmtclose fr" href="javascript:;">x</a>&gt;_&lt;关注过了~</p>';
					html += '<a href="' + $this.data("backurl") + '/forum" class="sbl-btn speakmore">参与讨论</a>'
					html += '</div>';
					$("body").append(html);
					var position = $.smeite.util.getPosition(o).topMid();
					var W = $("#cmtDialog").outerWidth(),
					H = $("#cmtDialog").outerHeight();
					$("#cmtDialog").css({
						left: position.x - W/2 + "px",
						top: position.y - H - 12 + "px"
					}).fadeIn();	
					setTimeout(function(){$("#cmtDialog").fadeOut().remove();},3000)
					$this.data("enable","enable");
				}
			//}
		},*/
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
				var replyCon = $li.find(".J_PostCon:first").html();
				if($.smeite.simpleEditor){
					replyCon = $.smeite.simpleEditor.richText2text($li.find(".J_PostCon:first").data("content"));
				}
				if($.smeite.editor){
					replyCon = $.smeite.editor.richText2text($li.find(".J_PostCon:first").attr("data-content"));
				}
				var time = $li.find(".time").html();
				var segmentHtml = "";
				segmentHtml += '<blockquote>';
				segmentHtml += '<input type="hidden" name="commentId" value="' + $li.data("cmtid") + '"/>';
				segmentHtml += '<span class="info g-daren">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
				segmentHtml += '<p>' + $.trim(replyCon).substring(0,40) + '</p>';
				segmentHtml += '<a class="close">X</a>';
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
			/*
			$postCmtArea.find("textarea").live("keyup",function(e){
				var $this = $(this);
				$.guang.util.submitByEnter(e, function(){
					$.guang.forum.postCommentSubmit($("#J_CmtPublishBtn"));
				});
			});
			*/
			

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
			if($("#J_ForumCmtId")[0] && $("#J_ForumCmtId").val() != ""){
				var $cmtLi = $("#J_ForumPost").find("li[data-cmtid=" + $("#J_ForumCmtId").val() + "]");
				//$("html, body").scrollTop($cmtLi.offset().top - 50);
				/*
				$cmtLi.highlight({color:'#ffe5c4',speed:500,complete:function(){
					$cmtLi.highlight({color:'#ffe5c4',speed:500,complete:function(){},iterator:'sinusoidal'});
				},iterator:'sinusoidal'});
				*/
			}
			
			/*//喜欢操作
			$("#J_ForumLover").click(function(){
				$.smeite.forum.lover($(this));
			});*/
			
			//帖子内容格式化
			if($("#J_PostArtical")[0]){
				$("#J_PostArtical").html($("#J_PostArtical").html().replace(/\n/g,"<br/>"));
			}
		}


	}
	$.smeite.forum.init();


	
//})(jQuery);





});