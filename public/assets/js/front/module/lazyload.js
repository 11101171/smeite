/*
 * 图片延迟加载
 * Add on 2012-02-16 by @JefferXia
 * modify on 2012-05-29
 * 模块化by heiniu@guang.com 2012-08-15
 */
define("module/lazyload", [],function(require, exports) {
    return function($) {

//(function($) {    
        $.fn.lazyload = function(options) {
            var settings = {
                threshold       : 0,
                failure_limit   : 0,
                event           : "scroll",
                effect          : "show",
                container       : window,
                data_attribute  : "original",
                skip_invisible  : true,
                appear          : null,
                load            : null
            };

            if(options) {
                if (undefined !== options.failurelimit) {
                    options.failure_limit = options.failurelimit;
                    delete options.failurelimit;
                }
                if (undefined !== options.effectspeed) {
                    options.effect_speed = options.effectspeed;
                    delete options.effectspeed;
                }

                $.extend(settings, options);
            }

            var elements = this;
            if (0 == settings.event.indexOf("scroll")) {
                $(settings.container).bind(settings.event, function(event) {
                    var counter = 0;
                    elements.each(function() {
                        $this = $(this);
                        if (settings.skip_invisible && !$this.is(":visible")) return;
                        if ($.abovethetop(this, settings) || $.leftofbegin(this, settings)) {

                        } else if (!$.belowthefold(this, settings) && !$.rightoffold(this, settings)) {
                            $this.trigger("appear");
                        } else {
                            if (++counter > settings.failure_limit) {
                                return false;
                            }
                        }
                    });
                });
            }

            this.each(function() {
                var self = this;
                var $self = $(self);

                self.loaded = false;

                /* When appear is triggered load original image. */
                $self.one("appear", function() {
                    if (!this.loaded) {
                        if (settings.appear) {
                            var elements_left = elements.length;
                            settings.appear.call(self, elements_left, settings);
                        }
                        $("<img />").bind("load", function() {
                            $self.hide().attr("src", $self.data(settings.data_attribute))[settings.effect](settings.effect_speed);
                            self.loaded = true;

                            var temp = $.grep(elements, function(element) {
                                return !element.loaded;
                            });
                            elements = $(temp);

                            if (settings.load) {
                                var elements_left = elements.length;
                                settings.load.call(self, this, elements_left, settings);
                            }
                        }).attr("src", $self.data(settings.data_attribute));
                    };
                });

                if (0 != settings.event.indexOf("scroll")) {
                    $self.bind(settings.event, function(event) {
                        if (!self.loaded) {
                            $self.trigger("appear");
                        }
                    });
                }
            });

            $(window).bind("resize", function(event) {
                $(settings.container).trigger(settings.event);
            });

            $(settings.container).trigger(settings.event);

            return this;

        };


        $.belowthefold = function(element, settings) {
            if (settings.container === undefined || settings.container === window) {
                var fold = $(window).height() + $(window).scrollTop();
            } else {
                var fold = $(settings.container).offset().top + $(settings.container).height();
            }
            return fold <= $(element).offset().top - settings.threshold;
        };

        $.rightoffold = function(element, settings) {
            if (settings.container === undefined || settings.container === window) {
                var fold = $(window).width() + $(window).scrollLeft();
            } else {
                var fold = $(settings.container).offset().left + $(settings.container).width();
            }
            return fold <= $(element).offset().left - settings.threshold;
        };

        $.abovethetop = function(element, settings) {
            if (settings.container === undefined || settings.container === window) {
                var fold = $(window).scrollTop();
            } else {
                var fold = $(settings.container).offset().top;
            }
            return fold >= $(element).offset().top + settings.threshold  + $(element).height();
        };

        $.leftofbegin = function(element, settings) {
            if (settings.container === undefined || settings.container === window) {
                var fold = $(window).scrollLeft();
            } else {
                var fold = $(settings.container).offset().left;
            }
            return fold >= $(element).offset().left + settings.threshold + $(element).width();
        };

        $.inviewport = function(element, settings) {
            return !$.rightofscreen(element, settings) && !$.leftofscreen(element, settings) &&
                !$.belowthefold(element, settings) && !$.abovethetop(element, settings);
        };

        /* Custom selectors for your convenience.   */
        /* Use as $("img:below-the-fold").something() */

        $.extend($.expr[':'], {
            "below-the-fold" : function(a) { return $.belowthefold(a, {threshold : 0, container: window}) },
            "above-the-top"  : function(a) { return !$.belowthefold(a, {threshold : 0, container: window}) },
            "right-of-screen": function(a) { return $.rightoffold(a, {threshold : 0, container: window}) },
            "left-of-screen" : function(a) { return !$.rightoffold(a, {threshold : 0, container: window}) },
            "in-viewport"    : function(a) { return !$.inviewport(a, {threshold : 0, container: window}) },
            /* Maintain BC for couple of versions. */
            "above-the-fold" : function(a) { return !$.belowthefold(a, {threshold : 0, container: window}) },
            "right-of-fold"  : function(a) { return $.rightoffold(a, {threshold : 0, container: window}) },
            "left-of-fold"   : function(a) { return !$.rightoffold(a, {threshold : 0, container: window}) }
        });

//})(jQuery);

    }
});