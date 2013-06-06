/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-6-6  下午2:43
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
var  getPosition = function(ele){
    var top = ele.offset().top,
        left = ele.offset().left,
        bottom = top + ele.outerHeight(),
        right = left + ele.outerWidth(),
        lmid = left + ele.outerWidth()/2,
        vmid = top + ele.outerHeight()/2;
    var position = {
        leftTop: function(){
            return {x: left, y: top}
        },
        leftMid: function(){
            return {x: left, y: vmid}
        },
        leftBottom: function(){
            return {x: left, y: bottom}
        },
        topMid: function(){
            return {x: lmid, y: top}
        },
        rightTop: function(){
            return {x: right, y: top}
        },
        rightMid: function(){
            return {x: right, y: vmid}
        },
        rightBottom: function(){
            return {x: right, y: bottom}
        },
        MidBottom: function(){
            return {x: lmid, y: bottom}
        },
        middle: function(){
            return {x: lmid, y: vmid}
        }
    };
    return position;
}
$.fn.shareToThird=function(){
    var $this = $(".share-link");
    var  shareTxt = encodeURIComponent($this.data("sharetxt")),
        shareLink = $this.data("sharelink"),
        sharePic = encodeURIComponent($this.data("sharepic"));
    shareLink = encodeURIComponent("http://smeite.com" + shareLink);
   /* if(shareLink.indexOf("http://smeite.com")==-1){
        shareLink = encodeURIComponent("http://smeite.com" + shareLink);
    }*/
    $(".share-link a").click(function(){
        var type = $(this).data("type");
        switch(type){
            case "s-sina":
                window.open('http://service.t.sina.com.cn/share/share.php?appkey=1464940004&title='+shareTxt+'&pic='+sharePic+'&url='+shareLink);
                break;
            case "s-tencent":
                window.open('http://v.t.qq.com/share/share.php?appkey=100344510&url='+shareLink+'&title='+shareTxt+'&pic='+sharePic+'&site='+shareLink);
                break;
            case "s-douban":
                window.open('http://www.douban.com/recommend/?url='+shareLink+'&title='+shareTxt);
                break;
            case "s-qzone":
                window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+shareLink);
                break;
            case "s-renren":
                window.open('http://share.renren.com/share/buttonshare.do?link='+shareLink+'&title='+shareTxt);
                break;
            case "s-163":
                window.open('http://t.163.com/article/user/checkLogin.do?link='+shareLink+'&source=&info='+shareTxt+'&images='+sharePic);
        }
    })
}

$(function(){
    $(".btn-share").shareToThird()
//鼠标enter,leave到item上显示可点击的喜欢,喜欢数
    $('.like-common .like').hover(function(){
        $(this).parent().children('.like-num').find('.J_scrollUp').animate({ top:"-24" }, 600)
    },function(){
        $(this).parent().children('.like-num').find('.J_scrollUp').animate({  top:"0" },  600)

    })
    $('.like-state .ico-likes').hover(function(){
        $(this).closest(".like-state").find(".J_scrollUp").animate({top:'-24'},600)
    },function(){
        $(this).closest(".like-state").find(".J_scrollUp").animate({top:'0'},600)
    })



    $("a[rel=followGoods]").click(function(){
        var $this = $(this);

        var goodsId =$this.data("proid")
        $.ajax({
            url:"/fanji/addLoveNum",
            type : "post",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({"goodsId": goodsId }),
            success: function(data){
                if(data.code=="100"){
                    $("#J_LikeDialog")[0] && ( $("#J_LikeDialog").remove());
                    var  html = ('<div id="J_LikeDialog" class="c-dialog" style="width:150px;">' +
                        '<p class="title clearfix">' +
                        '<a class="cmtclose fr" href="javascript:;">x</a>' +
                        '喜欢了~</p>' +
                        '</div>');
                    $("body").append(html);
                    var $likeCount = $this.next(".like-num").find(".J_FavorNum");
                    var  loveNum = parseInt($likeCount.data("val")) + 1;
                    $likeCount.text(loveNum);
                    $likeCount.data("val", loveNum);
                    /*dialog 显示*/
                    var top = $this.offset().top,
                        left = $this.offset().left,
                        x = left + $this.outerWidth()/2,
                        y = top + $this.outerHeight()/2;
                     var  c = $("#J_LikeDialog").outerWidth();
                     var f = $("#J_LikeDialog").outerHeight();
                    $("#J_LikeDialog").css({left:x - c / 2 + "px", top:y - f  + "px"}).fadeIn();
                  setTimeout(function () {
                        $("#J_LikeDialog").fadeOut()
                    }, 3E3);
                }
            }
        });
    })

})

