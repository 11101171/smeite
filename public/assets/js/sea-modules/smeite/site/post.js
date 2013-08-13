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
    $.smeite.postComment = {
        //评论与回复提交前校验
        submit : function($this){
            $this.attr('disabled',true);

            var $postComment = $("#J_postComment");
            var $textarea = $postComment.find("textarea");
            $("#J_quoteContent").val($("#J_postQuote").html())
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
                    $postComment.submit();

                }
            }
        },


        //通用讨论组初始化
        init : function(){
            //评论与回复
            var $postQuote = $("#J_postQuote");
            var $postComment = $("#J_postComment");

            //点击回复
            $(".J_postReply").click(function(){

                var $li = $(this).closest("li");
                var userNick = $li.find(".J_userNick:first").html();
                var commentCon = $li.find(".J_commentCon").html();
                var time = $li.find(".time").html();
                var quoteHtml = "";
                quoteHtml += '<blockquote>';
                quoteHtml += '<span class="info">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
                quoteHtml += '<p>' + $.trim(commentCon) + '</p>';
                quoteHtml +='<a class="close">X</a>';
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
                $.smeite.postComment.submit($(this));
            });

            $postComment.find("textarea").focus(function(){
                $.smeite.dialog.isLogin();
            });

            //回车键提交评论
            $postComment.find("textarea").on("keyup",function(e){
                var $this = $(this);
                $.smeite.util.submitByEnter(e, function(){
                    $.smeite.postComment.submit($("#J_postCommentSubmit"));
                });
            });

        }


    }
    $.smeite.postComment.init();


});