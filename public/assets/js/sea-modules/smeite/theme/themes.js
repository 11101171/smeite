define(function(require, exports) {
	var $  = require("$");
    var Slide = require("slide")

    $(function(){

        var themesFlash = new Slide({
            element: '#J_themeFlash',
            panels: '#J_themeFlash .scrollable .items .item',
            triggers:"#J_themeFlash .navi li",
            activeTriggerClass:"active",
            effect: 'fade',
            easing: 'easeOutStrong',
            interval: 2000
        }).render();
    })

});