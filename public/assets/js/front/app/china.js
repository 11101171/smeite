define(function(require, exports) {
	var $  = require("jquery");
     $(".tag-logo").hover(function(){
         $(this).find("default").hide();
         $(this).find("hover").show() ;
     },function(){
         $(this).find("default").show() ;
         $(this).find("hover").hide();
     })
});