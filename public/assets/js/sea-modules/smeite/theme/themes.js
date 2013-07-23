define(function(require, exports) {
	var $  = require("$");
    var Slide = require("slide")

    $(function(){

        var themesFlash = new Slide({
            element: '#J_themeFlash',
            panels: '#J_themeFlash  .items .item',
            triggers:"#J_themeFlash .navi li",
            activeTriggerClass:"active",
            effect: 'fade',
            easing: 'easeOutStrong',
            interval: 6000
        }).render();
    })

});