/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 商品详细页 js效果，包括 商品图片切换效果，tab 效果
 * @depends:  jquery.tools.js  smeite.js smeite.goods.js
 * Includes
 * Since: 2012-11-11
 * ModifyTime : 2012-12-25
 * ModifyContent: 增加、删除、清理注释
 * http://www.smeite.com/
 *
 */

define(function(require, exports){
    var $  = require("$");



    /*判断评价的内容长短*/
    function L(a, b) {
        if ("" != b && 2 <= $.smeite.util.getStrLength(b)) {
            if (1E3 < $.smeite.util.getStrLength(b))return $.smeite.tip.conf.tipClass = "tipmodal tipmodal-general", $.smeite.tip.show(a, ">_< 评论内容不能超过200个汉字！"),
                !1
        } else return $.smeite.tip.conf.tipClass = "tipmodal tipmodal-general", $.smeite.tip.show(a, ">_< 你的评论太短啦，再多说几句吧！"), !1;
        return!0
    }
   /* *//*图片显示*//*
    $(".thumb-list li").mouseover(function () {
        if (!$(this).hasClass("cur")) {
            var a = $(this).find("img").attr("data-src");
            $(".pic-box img").attr("src", a);
            $(".thumb-list li.cur").removeClass("cur");
            $(this).addClass("cur")
        }
    });*/

    /*  分享 */
    $(".btn-share").shareToThird()

    $("#J_JiuCuo").click(function () {
        $(this).html("<span>已反馈，谢谢^_^</span>");
        /*$.get("/jiucuo",function(data){
          暂时不实现
        })*/
    });
    /* product love*/
  //  var x = $("#J_LikeCount"), t = {};
   // var  w = commentParameter.productId;
    $(".pic-box").hover(function () {
        $(".ilike-m").show()
    }, function () {
        $(".ilike-m").hide()
    });

    /* get comment 评论处理 */
    $("#J_CommentTxa").focus(function () {
        if(!$.smeite.dialog.isLogin()){
            return false;
        }
       $("#J_CmtHiddenForm").show();
      //  $(this).height(50)
    });

    $(".detail-usedTags-row li").click(function () {
        var a = $("#J_CommentTagsDetail").val().replace(/^[，\,]+/, "").replace(/[，\,]+$/, ""), b = "," + a + ",", e = $(this);
        if (e.hasClass("selected")) {
            a = "," + e.text() + ",";
            for (b = b.replace(a, ","); 0 <= b.indexOf(a);)b = b.replace(a, ",");
            b = b.replace(/^[，\,]+/, "").replace(/[，\,]+$/, "");
            $("#J_CommentTagsDetail").val(b);
            e.removeClass("selected")
        } else b = a.length ? a + "," + e.text() : e.text(), 64 >= $.smeite.util.getStrLength($.smeite.util.htmlToTxt(b)) ? ($("#J_CommentTagsDetail").val(b), e.addClass("selected")) : ($.smeite.tip.conf.tipClass = "tipmodal tipmodal-general", $.smeite.tip.show(e, ">_< 标签内容不能超过64个汉字！"))
    });
    $("#J_CommentTagsDetail").keyup(function () {
        var a = this.value;
        if (64 >= $.smeite.util.getStrLength($.smeite.util.htmlToTxt(a))) {
            $(this);
            for (a = a.replace(/，|\s+/g, ","); 0 <= a.indexOf(",,");)a = a.replace(",,", ",");
            for (var b = a.split(","), e = 0, d = !1; e < b.length; e++) {
                var c = b[e];
                14 < $.smeite.util.htmlToTxt(c).length && (d = !0, b[e] = $.smeite.util.substring4ChAndEn(c, 14))
            }
            d && (a = b.join(","));
            if (a != this.value)this.value = a;
            $(".detail-usedTags-row li").each(function () {
                var b = $(this);
                0 <= ("," + a + ",").indexOf("," + b.text() + ",") ? b.hasClass("selected") ||
                    b.addClass("selected") : b.hasClass("selected") && b.removeClass("selected")
            })
        } else this.value = $.smeite.util.substring4ChAndEn(a, 64)
    });
    $("#J_HiddenForm").click(function () {
        $("#J_CmtHiddenForm").hide();
        $("#J_CommentTxa").height(80)
    });


    $(".appraisal .jd-radio").on("click", function () {
        var a = $(this);
        a.hasClass("worth-radioclick-on") ? a.removeClass("worth-radioclick-on").addClass("worth-radioclick-off") : ($(".worth-radioclick-on").removeClass("worth-radioclick-on").addClass("worth-radioclick-off"), a.removeClass("worth-radioclick-off").addClass("worth-radioclick-on"))
    });

    $("#J_PublishBtn").click(function () {
        if(!$.smeite.dialog.isLogin()){
            return false;
        }
            var a = $(this),worth=1,bought=0;
            var b = $.trim($("#J_CommentTxa").val());

            if (0 < $(".appraisal .worth-radioclick-on").length) {
                  worth = parseInt($(".worth-radioclick-on").eq(0).attr("data-type"));
            }

            if ($("#J_Bought1")[0] && $("#J_Bought1")[0].checked){
                 bought = 1;
            }

            if (L(a, b)) {
                var d = {
                    productId:parseInt($("#J_productId").val()),
                    content:b,
                    tags:$("#J_CommentTagsDetail").val(),
                    worth:worth,
                    bought:bought
                };
                $.ajax({
                    url:"/goods/addComment",
                    type:"post",
                    contentType:"application/json; charset=utf-8",
                    dataType:"json",
                    data:JSON.stringify(d),
                    success:function (data) {

                    switch (data.code) {
                        case "100":
                            $("#J_CommentTxa").val("").height(80);
                            $("#J_CmtHiddenForm").show();
                            $("#J_CommentTagsDetail").val("");
                            $(".detail-usedTags-row li").each(function () {
                                $(this).removeClass("selected")
                            });

                            var html ='<div class="cmt clearfix cmt-noworth" data-cmtid="" data-proid="">'
                              + '<div class="user-pic">'
                                + '  <a href="#">'
                                + ' <img src="'+photo+'" alt="">'
                                + '  </a> '
                                + ' </div>'
                                + ' <div class="cmt-doc"> '
                                + '  <div class="cmt-info clearfix">'
                                + '     <a class="fl" href="#" title="" target="_blank">'+SMEITER.nick+'</a> '
                                + '    <span class="fl gc"></span> '
                                + '   <span class="fr gc">来自  ' + '   Smeite.com ' + '   </span>   '
                                + '   <span class="fr gc mr5"> 刚刚 </span> '
                                + ' </div>'
                                + ' <p class="cmt-content clearfix"><span>'+b +'</span>  '
                            //    + ' <a class="J_CmtReplyBtn btn">回复<em class="J_ReplyCount" data-count="0"></em></a>   '
                            //    + ' <a class="usefulBtn ">有用<em class="usefulCount" data-count="0"></em></a> '
                                + ' </p>       '
                                + '  </div>   '
                                + '</div> ';

                           $("#J_ShowResult").prepend(html)
                            break;
                        case "104":
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(a, data.msg);
                            break;
                        case "441":
                            alert("亲,操作过快了哦,休息片刻！");
                            break;
                        case "444":
                            alert("你已被禁止登录！");
                            window.location.href = "http://smeite.com/logout";
                            break;
                        case "440":
                            alert("你已被禁言！")
                    }
                }})
            }

    });

    /* 获取商品的评论*/
 //   function C(a) {
        $.ajax({
            url:"/goods/getComments",
            type:"get",
            dataType:"html",
            data:{goodsId:parseInt($("#J_productId").val())},
            success:function (data) {
             $("#J_comment").html(data)
        }})
    /* 商品评论翻页*/
    $("#goodsComments a.commentPage").on("click",function(){
       var p =$(this).data("page");
       var goodsId = $(this).data("goodsid");
        $.ajax({
            url:"/goods/getComments?goodsId="+goodsId+"&p="+p,
            type:"get",
            dataType:"html",
            success:function (data) {
                $("#J_comment").html(data)
            }})
    })



});