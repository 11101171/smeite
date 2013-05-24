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
    $(".baobei .ilike-del").die().live("click",function(){
        var $delBtn = $(this),
            $goodsItem = $delBtn.parents(".goods"),
            productId = $delBtn.data("proid"),
            ajaxUrl ="/theme/removeGoods",
            ajaxData = {
                themeId: parseInt(UserTheme.themeId),
                goodsId: parseInt(productId)
            };
        $.ajax({
            url: ajaxUrl,
            type:"post",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            data:JSON.stringify(ajaxData),
            success: function(data){
                switch(data.code){
                    case("100"):
                        $goodsItem.addClass("goods-gray");
                        break;
                    case("101"):
                        alert(data.msg);
                }
            }
        });
    });


    $(".theme-item .ilike-del").die().click(function(){
        var $delBtn = $(this),
            $topicItem = $delBtn.parents(".topic-item"),
            themeId = $delBtn.data("themeid");
        var confirmCallback = function(){
            $.ajax({
                url: "/theme/delete?id="+themeId,
                type : "get",
                dataType: "json",
                success: function(data){
                    var $json = data;
                    switch($json.code){
                        case "100":
                            $topicItem.addClass("goods-gray");
                            $delBtn.empty().remove();
                            break;
                        case "101":
                            alert($json.message);
                    }
                }
            });
        }
        $.smeite.confirmUI("真要删除这个主题吗？删了就没了哦...",confirmCallback,function(){

        });
    });






});