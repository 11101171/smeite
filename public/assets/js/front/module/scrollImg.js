define(function (require,exports) {
    return function (c) {
        function h(d, b) {
            var i = d.find(".items:eq(0)"), h = i.find(".item").length, e = 0, j = null, m = c(b.navis), n = function (a) {
                i.stop();
                var c = e + 1 == h ? 0 : e + 1;
                a + 1 && (e = a);
                i.animate({left:a + 1 ? -(b.width * a) : -(b.width * c)}, b.speed, function () {
                    -1 == a && (e = c, k(e))
                })
            }, l = function (a) {
                "left" == b.direction && n(a)
            }, f = function () {
                g();
                j = setTimeout(function () {
                    l(-1);
                    f()
                }, b.timer)
            }, g = function () {
                null != j && clearTimeout(j)
            }, k = function (a) {
                c("." + b.naviClass).removeClass(b.naviClass);
                m.eq(a).addClass(b.naviClass)
            };
            m.each(function (a) {
                c(this).hover(function () {
                    k(a);
                    g();
                    l(a)
                }, f)
            });
            d.hover(g, f);
            var o = {playlol:l, autoPlay:f, stopAuto:g, changeClass:k};
            null != b.startHandle ? b.startHandle(o) : f()
        }

        c.fn.scrollImg = function (d) {
            d = c.extend({speed:500, timer:3E3, direction:"left", navis:".navi li", naviClass:"active", eventName:"hover", width:this.width(), startHandle:null}, d || {});
            return this.each(function () {
                new h(c(this), d)
            })
        }
    }
});
