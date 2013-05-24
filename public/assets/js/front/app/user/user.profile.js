/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */
define(function(require, exports) {
	var $ = jQuery = require("jquery");
    /*  goods 喜欢 删除 */
    /* 移动到主题图片显示效果*/
    $(".goods-item").hover(function() {
        var self = $(this);
        self.find(".ilike-del").show();
    }, function() {
        var self = $(this);
        self.find(".ilike-del").hide();
    });
    /* 移动到主题图片显示效果*/
    $(".theme-item").hover(function() {
        var self = $(this);
        self.find("img").addClass("hover")
        self.find(".ilike-del").show();
    }, function() {
        var self = $(this);
        self.find("img").removeClass("hover")
        self.find(".ilike-del").hide();
    });
    //删除喜欢的商品
    $(".goods-item .ilike-del").die().live("click",function(){
        var $delBtn = $(this),
            $goodsItem = $delBtn.parents(".goods-item"),
            productId = $delBtn.data("proid"),
            ajaxUrl ="/user/removeGoods",
            dataType = $delBtn.data("type") ,
            ajaxData = {
                goodsId: parseInt(productId),
                dataType:dataType
            };
        $.ajax({
            url: ajaxUrl,
            type:"post",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            data:JSON.stringify(ajaxData),
            success: function(data){
                if(data.code=="100"){

                    $goodsItem.addClass("goods-gray")
                    $delBtn.empty().remove()
                }else{
                    alert(data.message);
                }

                }
        });
    });


    $(".theme-item .ilike-del").die().click(function(){
        var $this = $(this),
            $themeItem = $this.parents(".theme-item"),
            dataType = $this.data("type"),
            themeId = $this.data("themeid");

            var ajaxUrl="";
           if(dataType=="my"){
               ajaxUrl="/theme/delete"
           } else{
               ajaxUrl="/user/remove/loveTheme"
           }
        var confirmCallback = function(){
            $.ajax({
                url:ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({
                    themeId:themeId,
                    dataType:dataType
                }),
                success: function(data){
                    if(data.code=="100"){
                            $themeItem.addClass("goods-gray")
                        $this.empty().remove()
                    }else{
                            alert(data.message);
                    }
                }
            });
        }
        $.smeite.confirmUI("真要删除这个主题吗？删了就没了哦...",confirmCallback,function(){

        });
    });






});