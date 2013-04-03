define(function(require, exports) {
	var $  = require("jquery");
	
$(function(){
	$(".category-nav .more").dropDown({
		classNm: ".topic-dropdown",
		isLocation: true
	});

});
 
       //鼠标移动 topic-item 变色
    $(".topic-item").hover(function() {
            var self = $(this);
                 self.find("img").addClass("hover")
                self.find("h3").addClass("title");
                self.find(".info").css("color","#666");
                self.find("#J_follow").addClass("on");
    }, function() {
            var self = $(this);
                self.find("img").removeClass("hover")
                self.find("h3").removeClass("title");
                self.find(".info").css("color","#999");
                self.find("#J_follow").removeClass("on");
    });

});