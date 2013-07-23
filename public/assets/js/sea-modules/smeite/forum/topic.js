define(function(require, exports) {
	var $  = require("$");


    var getBaobeiHtml = function (json) {
        var baobeiId = json.baobei.goodsId;
        var baobeiUrl = "/goods/" + baobeiId;
        var baobeiName = json.baobei.name;
        baobeiName = $.smeite.util.getStrLength(baobeiName) > 15 ? $.smeite.util.substring4ChAndEn(baobeiName, 15) + "..." : baobeiName;
        var baobeiPhoto = json.baobei.pic;
        var baobeiIntro = json.baobei.intro;
        var baobeiLoveNum = json.baobei.loveNum;
        if (baobeiIntro) {
            baobeiIntro = baobeiIntro.length > 40 ? baobeiIntro.substring(0, 40) + "..." : baobeiIntro;
        } else {
            baobeiIntro = "";
        }
        var baobeiPrice = json.baobei.price;
        var baobeiPromotionPrice = json.baobei.promotionPrice
        var baobeiJifenbao = json.baobei.jifenbao
        var baobeiJifenbaoValue = json.baobei.jifenbaoValue
        var baobeiVolume = json.baobei.volume
        var html = '<div class="baobei clearfix"><div class="baobei-pic">' +
            '<a target="_blank" href="' + baobeiUrl + '">' +
            '<img src="' + baobeiPhoto + '" alt="' + baobeiName + '" />' +
            '</a>' +
            '<span>￥' + baobeiPrice + '</span>' +
            '</div>' +
            '<div class="baobei-text">' +
            '<h4>' +
            '<a target="_blank" href="' + baobeiUrl + '">' + baobeiName + '</a>' +
            '</h4>' +
            '<div class="baobei-info">' +
            '<p>' + baobeiIntro + '</p>' +
            '</div>' +
            '<div class="price">价格：￥<span class="normalPrice">'+baobeiPrice+'</span><span class="promotionPrice">'+baobeiPromotionPrice+'</span><span class="volume">30天已售 '+baobeiVolume+'</span></div>'+
        //    '<div class="credits">返利:&nbsp;<span>'+baobeiJifenbao+'</span>&nbsp;个集分宝，值<span>'+baobeiJifenbaoValue+'</span>元</div>'+
            '<div class="clearfix mt15">' +
            '<a class="ilike-n" data-prourl="' + baobeiUrl + '" data-proimgsrc="' + baobeiPhoto + '" data-proname="' + baobeiName + '" data-type="0" data-proid="' + baobeiId + '" href="javascript:;">喜欢</a>' +
            '<div class="stat-box fr"><span class="mr5 ml5"><a href="'+baobeiUrl+'">去看看&gt;</a></span></div>' +
            '</div>' +
            '</div>' +
            '<a target="_blank" class="baobei-link"  href="' + baobeiUrl + '"></a></div>';
        return html;
    }
   var fetchBaobei=function(id,$elm){
        $.ajax({
            url:"/editor/fetchBaobei",
            type:"get",
            dataType:"json",
            data:{   id:parseInt(id)  },
            success:function (json) {
                    if (json.code == 100) {
                        var html = getBaobeiHtml(json);
                         $elm.replaceWith(html)
                    }

            }
        });
    }


   $(".img-goods").each(function(){
       var id =$(this).data("goodsid")
         fetchBaobei(id,$(this))
      })


		$("#J_ForumPost").show();







//})(jQuery)


});
