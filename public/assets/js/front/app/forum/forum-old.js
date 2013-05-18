/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 讨论吧页面：
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
/*
 * 讨论组交互js
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
		//评论与回复提交前校验
		postCommentSubmit : function($this){
			$this.attr('disabled',true);
		       var postContent= $(".redactor_editor").html();
				if(postContent==""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不能为空哦！");
					$this.attr('disabled',false);
					return;
				}
				if(postContent >= 200){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容不得超过200字");
					$this.attr('disabled',false);
					return;
				}
				$("#J_ForumPostCon").val(postContent);

			var $postCmtArea = $("#J_PostCmtArea");
			var $textarea = $postCmtArea.find("textarea");

            var $topicId= $postCmtArea.find("#J_topicId")
			if($.smeite.dialog.isLogin()){
				if($.trim($textarea.val()) == ""){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"亲，评论内容不能为空哦！");
					$this.attr('disabled',false);
				}else if($.smeite.util.getStrLength($textarea.val()) >= 200){
					$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
					$.smeite.tip.show($this,"内容大于200字！");
					$this.attr('disabled',false);
				}else{
					$this.attr('disabled',false);
//				//	$postCmtArea.submit();
					$.ajax({
						url :  "/forum/reply",
						type : "post",
						dataType : "json",
						data : {
							content : $textarea.val(),
                            topicId:$topicId.val(),
                            replyContent:$("#J_PostCmtSegment").html()
						},
						success : function(data){
							switch(data.code){
							case "100" : {
                               /* $postCmtArea.hide();
                                var html='<div class="share-box"><div class="share-con"><div class="title"><h4>发表成功~</h4><a href="javascript:;" id="J_CheckCmt">查看</a><span class="vline5">|</span><a href="javascript:;" id="J_ReMore">再回一条</a><a class="close" href="javascript:;"></a></div></div></div>'
                                $(".forum-editor").append(html) ;
*/
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show($this,data.message);
                                window.document.location.reload()
							}break;
							case "101" : {
								$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
								$.smeite.tip.show($this,data.message);
								$this.attr('disabled',false);
							}break;
							}
						}
					});
				}
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
				$postCmtArea.find("textarea").focus();
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
				segmentHtml += '<p>' + $.trim(replyCon).substring(0,100) + '</p>';
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

			
			//话题创建与编辑
			if($("#J_ForumPostEditBtn")[0]){
				$("#J_ForumPostTitle").focus(function(){
					$.smeite.dialog.isLogin();
				});
				$("#J_ForumPostCon").focus(function(){
					$.smeite.dialog.isLogin();
				});
                $("#J_ForumPostEditForm").submit(function(){


                    var content= $(".redactor_editor").contents().html();
                    var title =$("#J_ForumPostTitle").val()
                    var topicId=$("#J_topicId").val()
                    if(title ==null){
                        alert("标题不能为空")
                        return false
                    }
                    if(content ==null){
                        if(topicId>0){
                         window.history.back(-1);
                        }else{
                            alert("内容不能为空")
                            return false
                        }

                    } else $("#J_ForumPostCon").val(content);

                })

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
//设置绑定sns帐号返回函数



});