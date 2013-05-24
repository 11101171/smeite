define(function(require, exports) {
	var $ = jQuery = require("jquery");

//(function($) { 
	$.smeite.goods = {
		conf: {
			distance: 400,
    		page: 1,
    		container: ".goods-block",
    		colArray: [],
    		containerW: 980,
    		columns: 4,
    		columnWidthInner: 210,
    		columnMargin: 20,
    		columnPadding: 20,
    		columnWidthOuter: 250,
    		ajaxUrl:"/goods/guessUserLikes",
    		loadFlag:false,
    		ajaxData :{
    			spage:null,
    			bpage:null,
    			cateId:null,
    			tagId:null,
    			userId:null,
    			pubTime:null,
    			sort:null
    		},
    		justFlow: false, //判断是否只需要画报流化，无需其他操作
    		isGrid : false,//非画报流排列，判断是否是table一样的正规网格形式排列
    		isAjaxLoad : "true"//是否采用ajax，true采用ajax；false不采用ajax，而是采用hiddenshow方式
		},
		init: function(){			
			var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
			conf.columnWidthOuter = conf.columnWidthInner + conf.columnMargin + conf.columnPadding;
        	if(conf.colArray.length==0){
        		for (var i = 0; i < conf.columns; i++) {
        			conf.colArray[i] = 0;
        		}
        	}
        	var ele = $(".goods-wall").find(".goods");
        	if(!conf.justFlow){//画报流展现
        		if(conf.isGrid){//非画报流展现
        			ele = $(".goods-wall-news").find(".goods-news");
            		$(".goods-wall-news").each(function(){
            			var $this = $(this);
            			$this.find(".goods-news").css("height",$this.outerHeight() - 20);
            		});
            		$(".page-box").show();
            	}else{

            		if(ele.length>0 && conf.isAjaxLoad == "true"){
            			self.flowGoods(ele);
            		}else{
            			self.ajaxLoad();
            		}
            	}
        		$(window).bind("scroll", self.lazyLoad);
        	}else{//普通画报流展现
        		self.flowGoods(ele);
        		self.fill();
        	}
       		

		},
		isLoading: false,
		lazyLoad: function() {
			var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
			var distanceToBottom = $(document).height() - $(window).scrollTop() - $(window).height();
            if (!self.isLoading && distanceToBottom < conf.distance) {
            	self.isLoading = true;
            	self.ajaxLoad();
            }
        },
    	ajaxLoad: function() {
        	var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
        	
        	$.smeite.goods.conf.ajaxData.spage = conf.page;
        	$(".goods-loading").show();
        	if(conf.isAjaxLoad == "false"){
        		//不采用ajax，采用hiddenshow
        		var $goodsWall = $("#J_GoodsShow");
	        	var $img;
	        	var minNum = (conf.page - 1)*20; 
	        	var maxNum = conf.page*20;
	        	var curGoodsArr = $goodsWall.find('.goods').slice(minNum, maxNum);
				
	        	var notCurGoodsArr=$goodsWall.find('.goods').slice(maxNum,goodsPage.sumGoodsNum);
	        	notCurGoodsArr.each(function(i){
	        		var notCur= $(this)[0];
	        		notCur.style.display="none";
	        	});
    			curGoodsArr.each(function(i){
                	var d = $(this)[0],
                	c = jQuery.inArray(Math.min.apply(Math, conf.colArray), conf.colArray),
                	f = conf.colArray[c];
                	d.style.top = f + "px";
                	d.style.left = c * conf.columnWidthOuter + "px";
                	d.style.display="block";
                	if(conf.page > 1){
                		$img = $(this).find("img:first");
                		$img.attr("src", $img.attr("data-src"));
                	}      
                	conf.colArray[c] = f + d.offsetHeight + conf.columnMargin;
            	}).animate({
                	opacity: "1"
            	},
            	500)
            	if($(".goods-wall")[0]){
            		$(".goods-wall")[0].style.height = Math.max.apply(Math, conf.colArray) + "px";
            	}
            	conf.page += 1;
            	self.isLoading = false;
				if(conf.page == 6 || curGoodsArr.length < 20 || $goodsWall.find('.goods').length <= 20){
					self.fill();
					$(".goods-loading").remove();
					$(".page-box").show();
					$(window).unbind("scroll", self.lazyLoad);
					if(conf.loadFlag=true){
						jQuery.smeite.fallGoods.init();
					}
				}   			
        	}else{	
        		//采用ajax
            	$.get(conf.ajaxUrl,  conf.ajaxData, function(d) {
                	var colEle = $("<div>" + d + "</div>").find(".goods");
            		var hiddenSpage = $("<div>" + d + "</div>").find(".J_HiddenSpage:last").val();
                	var hiddenIsEnd = $("<div>" + d + "</div>").find(".J_HiddenIsEnd:last").val();
                	var hiddenLastPubTime = $("<div>" + d + "</div>").find(".J_HiddenLastPubTime:last").val();
                	$.smeite.goods.conf.ajaxData.pubTime = hiddenLastPubTime;
                	self.flowGoods(colEle);
                
                	conf.page += 1;
                	self.isLoading = false;
    				if(conf.page == 6 || hiddenIsEnd == "true" || hiddenSpage == "5"){
    					self.fill();
    					$(".goods-loading").remove();
    					$(".page-box").show();
    					$(window).unbind("scroll", self.lazyLoad);
    				}
            	});        		
        	}
    	},
    	flowGoods: function(a) {
    		var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
    		if(!$(".goods-block")[0]){
    			$(".goods-wall").append('<div class="goods-block"></div>');
    		}

        	a.each(function(i){
            	var d = $(this)[0],
            	c = jQuery.inArray(Math.min.apply(Math, conf.colArray), conf.colArray),
            	f = conf.colArray[c];
            	d.style.top = f + "px";
            	d.style.left = c * conf.columnWidthOuter + "px";
            	$(conf.container + ":last").append(d);
            	conf.colArray[c] = f + d.offsetHeight + conf.columnMargin;
        	});
        	if($(".goods-wall")[0]){
        		$(".goods-wall")[0].style.height = Math.max.apply(Math, conf.colArray) + "px";
        	}

        	self.showGoods();
    	},
    	showGoods: function() {
    		var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
    		if(conf.page>2){
        	$(conf.container + ":last").animate({
            	opacity: "1"
        	},
        	100)
        	}else{
        		$(conf.container + ":last").css("opacity", "1");
        	}
    	},
    	fill: function() {
    		var self = $.smeite.goods,
    		conf = $.smeite.goods.conf;
    		var colMaxH = Math.max.apply(Math, conf.colArray),
    		index = jQuery.inArray(colMaxH, conf.colArray);
    		for(var i=0;i<conf.columns;i++){
    			if(i != index){
    				$(conf.container + ":last").append('<div class="goods-fill" style="top:' + conf.colArray[i] + 'px;left:' + i* conf.columnWidthOuter +'px;height:' + (colMaxH - conf.colArray[i] - conf.columnMargin) + 'px"></div>');
    			}
    		}
    	}
	}
//})(jQuery);


//重写画报流
//(function($) { 
	$.smeite.fallGoods={
			conf: {
				distance: 400,
	    		page: 1,
	    		limitNum:100,
	    		container: ".goods-new-block",
	    		colArray: [],
	    		containerW: 980,
	    		columns: 4,
	    		columnWidthInner: 210,
	    		columnMargin: 13,
	    		columnPadding: 20,
	    		columnWidthOuter: 243
			},
			init: function(){			
				var self = $.smeite.fallGoods,
	    		conf = self.conf;
				conf.columnWidthOuter = conf.columnWidthInner + conf.columnMargin + conf.columnPadding;
	        	if(conf.colArray.length==0){
	        		for (var i = 0; i < conf.columns; i++) {
	        			conf.colArray[i] = 0;
	        		}
	        	}

	       		self.load();
	       		$(window).bind("scroll", self.lazyLoad);
			},
			
			load:function(){
				var self = $.smeite.fallGoods,
	    		conf = self.conf;
				var $goodsWall = $(conf.container);
	        	var $img;
	        	var minNum = (conf.page - 1)*20; 
	        	var maxNum = conf.page*20;
	        	var curGoodsArr = $goodsWall.find('.goods').slice(minNum, maxNum);
	        	var notCurGoodsArr=$goodsWall.find('.goods').slice(maxNum,conf.limitNum);
	        	notCurGoodsArr.each(function(i){
	        		var notCur= $(this)[0];
	        		notCur.style.display="none";
	        	});
    			curGoodsArr.each(function(i){
                	var d = $(this)[0],
                	c = jQuery.inArray(Math.min.apply(Math, conf.colArray), conf.colArray),
                	f = conf.colArray[c];
                	d.style.top = f + "px";
                	d.style.left = c * conf.columnWidthOuter + "px";
                	d.style.display="block";
                	if(conf.page > 1){
                		$img = $(this).find("img:first");
                		$img.attr("src", $img.attr("data-src"));
                	}      
                	conf.colArray[c] = f + d.offsetHeight + conf.columnMargin;
            	}).animate({
                	opacity: "1"
            	},
            	100)
            	$goodsWall.height( Math.max.apply(Math, conf.colArray));
            	conf.page += 1;
				if(conf.page == 6 || curGoodsArr.length < 20 || $goodsWall.find('.goods').length <= 20){
					self.fill();
					$(window).unbind("scroll", self.lazyLoad);
				} 
				
				
			},
			
			fill: function() {
				var self = $.smeite.fallGoods,
	    		conf = self.conf;
	    		var colMaxH = Math.max.apply(Math, conf.colArray),
	    		index = jQuery.inArray(colMaxH, conf.colArray);
	    		for(var i=0;i<conf.columns;i++){
	    			if(i != index){
	    				$(conf.container).append('<div class="goods-fill" style="top:' + conf.colArray[i] + 'px;left:' + i* conf.columnWidthOuter +'px;height:' + (colMaxH - conf.colArray[i] - conf.columnMargin) + 'px"></div>');
	    			}
	    		}
	    	},
	    	
	    	lazyLoad: function() {
	    		var self = $.smeite.fallGoods,
	    		conf = self.conf;
				var distanceToBottom = $(document).height() - $(window).scrollTop() - $(window).height();
	            if (!self.isLoading && distanceToBottom < conf.distance) {
	            	self.load();
	            }
	        }
	}
//})(jQuery);


//等高画报流 add by zhuangsh@iphonele.com 2012-07-11
//(function($) { 
$.smeite.gridGoods = {
	conf: {
		container:$(".goods-grid-wall"),
		uinitContainer:$(".goods-wall-news1"),
		goods:$(".goods-grid-wall").find(".goods-news"),
		goodsName:"goods-news",
		distance: 400,
		page:1,
		pageLimit:6,
		loadUnit:3,
		loadMaxUint:16
	},
	init:function(){
		var self=$.smeite.gridGoods;
		var conf=self.conf;
		conf.uinitContainer.each(function(){
			var $this = $(this);
			$this.find("."+conf.goodsName).css("height",$this.outerHeight() - 20);
		});
		self.load();
		$(window).bind("scroll", self.lazyLoad);
		
	},
	
	load:function(){
		var self=$.smeite.gridGoods;
		var conf=self.conf;
		var minUnitNum=(conf.page - 1) * conf.loadUnit;
		var maxUintNum=conf.page * conf.loadUnit;
		var curUnit=conf.uinitContainer.slice(minUnitNum,maxUintNum);
		var notCurUnit=conf.uinitContainer.slice(maxUintNum,conf.loadMaxUint);
		curUnit.each(function(i){
				var self=$(this);
				self[0].style.display = "block";
				if (conf.page > 1) {
					var img = self.find("img");
					img.each(function(){
						var me=$(this);
						var src=me.attr("data-src");
						me.attr("src", src);
					});
					
				}
				
			});
		notCurUnit.each(function(i){
			var self=$(this);
			self[0].style.display = "none";
			
        	});
			conf.page += 1;
			if(conf.page == conf.pageLimit || curUnit.length < conf.loadUnit || conf.uinitContainer.length <= conf.loadUnit){
				$(window).unbind("scroll", self.lazyLoad);
			}   
		
		 
	},
	lazyLoad:function(){
		var self=$.smeite.gridGoods;
		var conf=self.conf;
		var distanceToBottom = $(document).height() - $(window).scrollTop() - $(window).height();
        if (distanceToBottom < conf.distance) {
        	self.load();
        }
		
	}
}
//})(jQuery);

});