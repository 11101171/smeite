define(function(require, exports) {
	var $  = require("$");

	//讨论组组件
	$.smeite.forum = {

	//评论与回复提交前校验
		commentSubmit : function($this){
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
				$("#J_commentContent").val($.smeite.editor.iframeDocument.body.innerHTML);
			}
            var comment = {
                "topicId":parseInt($("#J_topicId").val()),
                "quoteContent": $("#J_postQuote").html(),
                "content": $("#J_commentContent").val()
            };
			var $postCommentForm = $("#J_postCommentForm");
			var $textarea = $postCommentForm.find("textarea");
           $("#J_quoteContent").val($("#J_postQuote").html())
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

                    $.ajax({
                        url: $("#J_postCommentForm").attr("action"),
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
                                html +='<a class="img" href="/user/'+SMEITER.userId+'" target="_blank"><img src="'+SMEITER.userPhoto+'" width="80" height="80"></a>';
                                html +='<span class="info"><a class="J_UserNick" href="/user/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick +'</a>/ <span class="time">刚刚</span> </span>';
                                html +=' <div class="post">';
                                if($.trim(comment.quoteContent)!=''){
                                    html +='<div class="post-reply">'+comment.quoteContent+'</div>';
                                }

                                html +=' <div class="J_PostCon wordbreak">'+comment.content+'</div>';
                                html +='</div>';
                                html +='<p class="oper"><a class="J_postReply">回复</a></p>';
                                html +='</li>';

                                $("#J_forumPost").append(html);

                            }
                        }
                    });
                    return false

				}
			}
		},

		//帖子创建与编辑
		editSubmit : function($this){
			var title = $.trim($("#J_postTitle").val());
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

				$("#J_commentContent").val($.smeite.editor.iframeDocument.body.innerHTML);
			}
			var content = $("#J_commentContent").val();
			if(title == "" || $.trim(content) == ""){
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"标题和内容不能为空哦！");
			}else if($.smeite.util.getStrLength(title) > 50 || $.smeite.util.getStrLength(content) >= 10000){
				
				$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
				$.smeite.tip.show($this,"亲，标题<50字，内容<10000字");
			}else{
                $("#J_ForumPostEditForm").submit();

			}
		},

		//通用讨论组初始化
		init : function(){
			//评论与回复
			var $postCmtSegment = $("#J_postQuote");
			var $postCommentForm = $("#J_postCommentForm");

			//评论格式化
			if($(".J_PostCon")[0]){
				$(".J_PostCon").each(function(){
					$(this).html($.smeite.util.trim($(this).html()).replace(/\n/g,"<br/>"));
				});
			}
			
			//回复
            $(document).on("click",".J_postReply",function(){

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

                $("html, body").scrollTop($postCommentForm.offset().top -50);

                //删除回应
                $postCmtSegment.find(".close:first").unbind("click").click(function(){
                    $postCmtSegment.html("");
                });
			});
			
			$("#J_postCommentSubmit").click(function(event){
				event.preventDefault();
				$.smeite.forum.commentSubmit($(this));
			});
			
			$postCommentForm.find("textarea").focus(function(){
				$.smeite.dialog.isLogin();
			});
			
			//回车键提交评论
			$postCommentForm.find("textarea").on("keyup",function(e){
				var $this = $(this);
				$.smeite.util.submitByEnter(e, function(){
					$.smeite.forum.commentSubmit($("#J_postCommentSubmit"));
				});
			});


			//话题创建与编辑
			if($("#J_postEditBtn")[0]){
				$("#J_postTitle").focus(function(){
					$.smeite.dialog.isLogin();
				});
				$("#J_commentContent").focus(function(){
					$.smeite.dialog.isLogin();
				});
				$("#J_postEditBtn").click(function(event){
					event.preventDefault();
					if($.smeite.dialog.isLogin()){
						$.smeite.forum.editSubmit($(this));
					}
				});
			}
			



			

			
			//帖子内容格式化
			if($("#J_PostArtical")[0]){
				$("#J_PostArtical").html($("#J_PostArtical").html().replace(/\n/g,"<br/>"));
			}
		}


	}
	$.smeite.forum.init();



    $(".btn-share").shareToThird()





});