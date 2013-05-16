define(function(require, exports) {
	var $ = jQuery = require("jquery");
	$(function() {
		//解析html,生成内容和缩略图
		$(".col-right").each(function(){
			var $colrt = $(this),
			$picList = $colrt.find(".pic-list"),
			sourceHtml = $colrt.find(".sourceHtml").html(),
			imageJsonStr = $colrt.find(".image-jsonStr").html(),
			videoJsonStr = $colrt.find(".video-jsonStr").html(),
			imageJson = imageJsonStr==""?{}:eval("("+imageJsonStr+")"),
			videoJson = videoJsonStr==""?{}:eval("("+videoJsonStr+")"),
			reg_media = /\[图片(\d+)\]|\[视频(\d+)\]|\[宝贝(\d+)\]/g,
			text = sourceHtml.replace(reg_media,"").replace(/\[\/?(\w+)\]/g,""),
			mediaArr = sourceHtml.match(reg_media);
			text = $.smeite.util.ellipse(text,65);
			$colrt.find(".topic-text").html(text);
			mediaArr = mediaArr==null?[]:mediaArr;
			if(mediaArr.length>3){
				mediaArr = mediaArr.slice(0,3);
			}
			if(mediaArr.length>0){
			$.each(mediaArr,function(index,value){
				var id = value.substr(3,value.length-4);
				if(value.indexOf("图片")!=-1){
					if(imageJson[id]){
					var imgHtml = '<li data-type="image" data-big="'+imageJson[id].bigPic+'"><img src="'+imageJson[id].smallPic+'" alt="" /></li>';
					$picList.append(imgHtml);
					}
				}else if(value.indexOf("宝贝")!=-1){
					if(imageJson[id]){
					var goodsHtml = '<li data-type="goods" data-id="'+id+'"><img src="'+imageJson[id].smallPic+'" alt="" /></li>';
					$picList.append(goodsHtml);
					}
				}else if(value.indexOf("视频")!=-1){
					if(videoJson[id]){
					var videoHtml = '<li data-type="video" data-url="'+videoJson[id]+'"><img src="http://static.guang.com/images/ui/video.png" alt="" /><a class="video-play" href="javascript:;"></a></li>';
					$picList.append(videoHtml);
					}
				}
			});
			}
		});
		//缩略图绑定事件
		$(".topic-preview li").live("click",function(){
			var $this = $(this),
			liType = $this.data("type"),
			$preview = $this.parents(".topic-preview");
			switch (liType){
				case "image":
					$preview.hide();
					var bigUrl = $this.data("big");
					var html = '<div class="media-box media-image">'+
		        					'<div class="media-oper">'+
		        						'<a class="retract" href="javascript:;">收起</a>'+
		        						'<span class="vline5">|</span>'+
		        						'<a class="show-big" href="'+bigUrl+'" target="_blank">查看大图</a>'+
		        					'</div>'+
		        					'<div class="topic-image">'+
		        						'<img src="'+bigUrl+'" alt="" />'+
		        					'</div>'+
		        				'</div>';
					$preview.after(html);
					break;
				case "video":
					$preview.hide();
					var videoUrl = $this.data("url");
					var html = '<div class="media-box media-video">'+
		        					'<div class="media-oper">'+
		        						'<a class="retract" href="javascript:;">收起</a>'+
		        					'</div>'+
		        					'<div class="topic-video">'+
		        						'<embed id="" width="580" height="470" allowscriptaccess="never" pluginspage="http://get.adobe.com/cn/flashplayer/" flashvars="playMovie=true&amp;auto=1" allowfullscreen="true" quality="hight" src="'+videoUrl+'" type="application/x-shockwave-flash" wmode="transparent">'+
		        					'</div>'+
		        				'</div>';
					$preview.after(html);
			}
		});
		//收起
		$(".retract, .topic-image img").live("click",function(){
			var prt = $(this).parent().parent();
			prt.siblings(".topic-preview").show();
			if(prt.hasClass("media-image")||prt.hasClass("media-video")){
				prt.empty();
				prt.remove();
			}else{
				prt.hide();
			}
		});
		
	var getBaobeiHtml = function (json) {
		var baobeiId = json.baobei.id;
		var baobeiUrl = "http://smeite.com/goods/" + baobeiId;
		var baobeiName = json.baobei.name;
		baobeiName = $.smeite.util.getStrLength(baobeiName) > 15 ? $.smeite.util.substring4ChAndEn(baobeiName, 15) + "…" : baobeiName;
		var baobeiPhoto = json.baobei.pic;
		var baobeiRecommend = json.baobei.recommend;
		if (baobeiRecommend) {
			baobeiRecommend = baobeiRecommend.length > 40 ? baobeiRecommend.substring(0, 40) + "…" : baobeiRecommend;
		} else {
			baobeiRecommend = "";
		}
		var baobeiBrand = "";
		if (json.baobei.brandList && json.baobei.brandList.length > 0) {
			baobeiBrand += "<p>品牌：";
			for (var i = 0; i < json.baobei.brandList.length; i++) {
				var brand = json.baobei.brandList[i];
				baobeiBrand += '<span>' + brand.brandName + '</span>';
			}
			baobeiBrand += "</p>";
		}
		var tags = "";
		if (json.baobei.tags && json.baobei.tags.length > 0) {
			for (var i = 0; i < json.baobei.tags.length; i++) {
				var tag = json.baobei.tags[i].tagKeyword;
				var tagUrl = "http://guang.com/xihuan/tag/" + tag;
				tags += '<span>' + tag + '</span>';
			}
		}
		var tagCount = json.baobei.tagCount;
		var worth = json.baobei.summary.worthCount;
		var unworth = json.baobei.summary.unworthCount;
		var Jtotal = worth + unworth;
		var Ctotal = json.baobei.commentNum;
		var priceMin = json.baobei.priceMin / 100;
		var priceMax = json.baobei.priceMax / 100;
		var html = ''
			+'<div class="baobei-pic" rel="big">'
				+'<a href="javascript:;"><img src="' + baobeiPhoto + '" alt="' + baobeiName + '" /></a>'
				+'<span>￥' + priceMin + '</span>'
			+'</div>'
			+'<div class="baobei-text">'
				+'<h4><a target="_blank" href="' + baobeiUrl + '">' + baobeiName + '</a></h4>'
				+'<div class="baobei-info">'
					+'<p>' + baobeiRecommend + '</p>'
					+ baobeiBrand
					+'<p>标签(' + tagCount + ')：'
					+ tags
					+'</p>'
				+'</div>'
				+'<div class="clearfix mt15">'
					+'<a class="ilike-n" rel="like" data-prourl="' + baobeiUrl + '" data-proimgsrc="' + baobeiPhoto + '" data-proname="' + baobeiName + '" data-type="0" data-proid="' + baobeiId + '" href="javascript:;">喜欢</a>'
					+'<div class="stat-box fr">'
						+'鉴定(' + worth + '/' + Jtotal + ')'
						+'<span class="mr5 ml5">|</span>'
						+'评论(' + Ctotal + ')'
					+'</div>'
				+'</div>'
			+'</div>'
			+'<a target="_blank" href="' + baobeiUrl + '" class="baobei-link"></a>';
		return html;
	}
	$(".topic-preview li[data-type=goods]").live("click",function(){
		var $self = $(this),
		preview = $self.parents(".topic-preview");
		if($self.data('hasbig')=='1'){
			var goodsId = $self.data("id");
			preview.hide();
			preview.siblings(".media-goods[data-gid="+goodsId+"]").show();
		}else{
			var goodsId = $self.data("id");
			$.ajax({
				url : "/editor/fetchBaobei",
				type : "post",
				dataType : "json",
				data : {
					id : goodsId
				},
				success : function (json) {
					if (json.code == 100) {
						var html = '<div class="media-box media-goods" data-gid="'+goodsId+'">'+
										'<div class="media-oper">'+
		        							'<a class="retract" href="javascript:;">收起</a>'+
		        							'<span class="vline5">|</span>'+
		        							'<a class="show-big" href="/goods/'+goodsId+'" target="_blank">浏览宝贝</a>'+
		        						'</div>'+
										'<div id="J_Baobei'+goodsId+'" class="baobei clearfix">'+getBaobeiHtml(json)+'</div>'+
									'</div>';
						preview.after(html);
						preview.hide();
						$self.data('hasbig','1');
					}
				}
			});
		}
	});
	$(".media-box img").live("click",function(){
		$(this).parents(".media-box").hide();
		$(this).parents(".media-box").siblings(".topic-preview").show();
	});
	});

});