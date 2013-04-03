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
    /* theme  删除主题 */

    $(".topic-item .ilike-del").die().click(function(){
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
    /* 移动到主题图片显示效果*/
    $(".topic-item").hover(function() {
        var self = $(this);
        self.find("img").addClass("hover")
        self.find(".ilike-del").show();
    }, function() {
        var self = $(this);
        self.find("img").removeClass("hover")
        self.find(".ilike-del").hide();
    });





});