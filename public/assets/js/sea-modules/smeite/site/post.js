/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户创造小镇页面js
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $  = require("$");

   var PostComment = {
        //评论与回复提交前校验
        submit : function($this){
            $this.attr('disabled',true);
            var $postCommentForm = $("#J_postCommentForm");
            var $textarea = $postCommentForm.find("textarea");
          //  $("#J_quoteContent").val($("#J_postQuote").html())

            var comment = {

                "pid": parseInt($("#J_pid").val()),
                "cid": parseInt($("#J_cid").val()),
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
                              html += '<div class="share-avt">';
                              html +='<a class="fl" href="/user/'+SMEITER.userId+'"target="_blank">';
                              html +='<img class="avt32 fl" src="'+SMEITER.userPhoto+'" width="38" height="38">';
                              html +='</a>';
                              html +="</div>";
                              html +=' <span class="arrow"></span>';
                              html +=' <div class="share-user">';
                              html +='<h3> <a class="J_userNick" href="/user/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick+'</a></h3>';
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
            var $postCommentForm = $("#J_postCommentForm");

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
                $("html, body").scrollTop($postCommentForm.offset().top -50);
                //删除引用回复
                $postQuote.find(".close:first").unbind("click").click(function(){
                    $postQuote.html("");
                });
            });

            $("#J_postCommentSubmit").click(function(event){
                event.preventDefault();
                PostComment.submit($(this));
            });

            $postCommentForm.find("textarea").focus(function(){
                $.smeite.dialog.isLogin();
            });

            //回车键提交评论
            $postCommentForm.find("textarea").on("keyup",function(e){
                var $this = $(this);
                $.smeite.util.submitByEnter(e, function(){
                    PostComment.submit($("#J_postCommentSubmit"));
                });
            });

        }


    }

   $(function(){
       $.ajax({
           url: "/post/getComments",
           type : "GET",
           dataType:"html",
           data:{pid:parseInt($("#J_pid").val())},
           success: function(data){
               $("#J_commentList").append(data);
           }
       });
       $(document).on("click","a.commentPage",function(){
           var p =$(this).data("page");
           var pid = $(this).data("pid");
           $.ajax({
               url:"/post/getComments?pid="+pid+"&p="+p,
               type:"get",
               dataType:"html",
               success:function (data) {
                   $("#J_commentList").html(data)
               }})
       })

       PostComment.init();
       var pid =parseInt($("#J_pid").val())
       $.ajax({
           url: "/post/addViewNum",
           type : "POST",
           contentType:"application/json; charset=utf-8",
           dataType: "json",
           data: JSON.stringify({"pid": pid }),
           success: function(data){

           }
       });
   })



});