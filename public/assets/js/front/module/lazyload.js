define(function(){return function(b){b.fn.lazyload=function(c){var a={threshold:0,failure_limit:0,event:"scroll",effect:"show",container:window,data_attribute:"original",skip_invisible:!0,appear:null,load:null};c&&(void 0!==c.failurelimit&&(c.failure_limit=c.failurelimit,delete c.failurelimit),void 0!==c.effectspeed&&(c.effect_speed=c.effectspeed,delete c.effectspeed),b.extend(a,c));var e=this;0==a.event.indexOf("scroll")&&b(a.container).bind(a.event,function(){var c=0;e.each(function(){$this=
b(this);if((!a.skip_invisible||$this.is(":visible"))&&!b.abovethetop(this,a)&&!b.leftofbegin(this,a))if(!b.belowthefold(this,a)&&!b.rightoffold(this,a))$this.trigger("appear");else if(++c>a.failure_limit)return!1})});this.each(function(){var c=this,d=b(c);c.loaded=!1;d.one("appear",function(){this.loaded||(a.appear&&a.appear.call(c,e.length,a),b("<img />").bind("load",function(){d.hide().attr("src",d.data(a.data_attribute))[a.effect](a.effect_speed);c.loaded=!0;var f=b.grep(e,function(a){return!a.loaded});
e=b(f);a.load&&a.load.call(c,this,e.length,a)}).attr("src",d.data(a.data_attribute)))});0!=a.event.indexOf("scroll")&&d.bind(a.event,function(){c.loaded||d.trigger("appear")})});b(window).bind("resize",function(){b(a.container).trigger(a.event)});b(a.container).trigger(a.event);return this};b.belowthefold=function(c,a){return(void 0===a.container||a.container===window?b(window).height()+b(window).scrollTop():b(a.container).offset().top+b(a.container).height())<=b(c).offset().top-a.threshold};b.rightoffold=
function(c,a){return(void 0===a.container||a.container===window?b(window).width()+b(window).scrollLeft():b(a.container).offset().left+b(a.container).width())<=b(c).offset().left-a.threshold};b.abovethetop=function(c,a){return(void 0===a.container||a.container===window?b(window).scrollTop():b(a.container).offset().top)>=b(c).offset().top+a.threshold+b(c).height()};b.leftofbegin=function(c,a){return(void 0===a.container||a.container===window?b(window).scrollLeft():b(a.container).offset().left)>=b(c).offset().left+
a.threshold+b(c).width()};b.inviewport=function(c,a){return!b.rightofscreen(c,a)&&!b.leftofscreen(c,a)&&!b.belowthefold(c,a)&&!b.abovethetop(c,a)};b.extend(b.expr[":"],{"below-the-fold":function(c){return b.belowthefold(c,{threshold:0,container:window})},"above-the-top":function(c){return!b.belowthefold(c,{threshold:0,container:window})},"right-of-screen":function(c){return b.rightoffold(c,{threshold:0,container:window})},"left-of-screen":function(c){return!b.rightoffold(c,{threshold:0,container:window})},
"in-viewport":function(c){return!b.inviewport(c,{threshold:0,container:window})},"above-the-fold":function(c){return!b.belowthefold(c,{threshold:0,container:window})},"right-of-fold":function(c){return b.rightoffold(c,{threshold:0,container:window})},"left-of-fold":function(c){return!b.rightoffold(c,{threshold:0,container:window})}})}});