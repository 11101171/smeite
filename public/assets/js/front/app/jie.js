define(function(require, exports) {
	var $  = require("jquery");
    require("module/scrollImg")($);
    $(".scrollable").scrollImg({timer:1E4, startHandle:function (c) {
        c && setTimeout(function () {
            c.playlol(0);
            c.changeClass(0);
            c.autoPlay()
        }, 1E4);
        var b = null;
        $(".navi li").each(function (d) {
            $(this).unbind();
            $(this).hover(function () {
                b != null && clearTimeout(b);
                b = setTimeout(function () {
                    c.changeClass(d);
                    c.stopAuto();
                    c.playlol(d)
                }, 200)
            }, function () {
                b != null && clearTimeout(b);
                c.autoPlay()
            })
        })
    }});
});