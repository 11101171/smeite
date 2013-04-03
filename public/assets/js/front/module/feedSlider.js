define(function (require,exports) {
    return function (d) {
        function f(c, b) {
            var a = c.find("ul:eq(0)"), g = a.children(), f = d(g[0]).outerWidth(), h = b.step * f, i = function () {
                a.animate({opacity:1}, b.timer, function () {
                    a.find("li").eq(b.limit).css("opacity", 0.5);
                    a.animate({marginLeft:-1 * h}, b.speed, function () {
                        a.find("li").eq(b.limit).css("opacity", 1);
                        a.find("li:first").appendTo(a);
                        a.css({marginLeft:0})
                    });
                    e()
                })
            }, j = function () {
                a.animate({opacity:1}, b.timer, function () {
                    a.animate({marginLeft:h}, b.speed, function () {
                        a.find("li:last").hide().prependTo(a).fadeIn();
                        a.css({marginLeft:0})
                    });
                    e()
                })
            }, e = function () {
                "left" == b.direction ? i() : j()
            };
            g.length > b.length && e();
            a.hover(function () {
                a.stop()
            }, e)
        }

        d.fn.feedSlider = function (c) {
            c = d.extend({speed:"normal", step:1, length:7, timer:3E3, direction:"left", limit:6}, c || {});
            return this.each(function () {
                new f(d(this), c)
            })
        }
    }
});
