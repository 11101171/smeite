/*
 * Copyright 2011-2012, Guang.com
 * @contain: textarea编辑器
 * @depends: jquery.js markitup.js
 * 
 * Author: luyao
 * Since: 2012-07-13
 * ModifyTime : 2012-07-13 15:05
 * email: water2212683@gmail.com
 * 
 */
 define(function(require, exports) {
 	var $ = jQuery = require("jquery");
 
//(function($){
	//表情配对表
	var FACEJSON = [
		{key:"[织]",val:"zz2_thumb.gif"},
		{key:"[神马]",val:"horse2_thumb.gif"},
		{key:"[浮云]",val:"fuyun_thumb.gif"},
		{key:"[给力]",val:"geili_thumb.gif"},
		{key:"[围观]",val:"wg_thumb.gif"},
		{key:"[威武]",val:"vw_thumb.gif"},
		{key:"[熊猫]",val:"panda_thumb.gif"},
		{key:"[兔子]",val:"rabbit_thumb.gif"},
		{key:"[奥特曼]",val:"otm_thumb.gif"},
		{key:"[囧]",val:"j_thumb.gif"},
		{key:"[互粉]",val:"hufen_thumb.gif"},
		{key:"[礼物]",val:"liwu_thumb.gif"},
		{key:"[呵呵]",val:"smilea_thumb.gif"},
		{key:"[嘻嘻]",val:"tootha_thumb.gif"},
		{key:"[哈哈]",val:"laugh.gif"},
		{key:"[可爱]",val:"tza_thumb.gif"},
		{key:"[可怜]",val:"kl_thumb.gif"},
		{key:"[挖鼻屎]",val:"kbsa_thumb.gif"},
		{key:"[吃惊]",val:"cj_thumb.gif"},
		{key:"[害羞]",val:"shamea_thumb.gif"},
		{key:"[挤眼]",val:"zy_thumb.gif"},
		{key:"[闭嘴]",val:"bz_thumb.gif"},
		{key:"[鄙视]",val:"bs2_thumb.gif"},
		{key:"[爱你]",val:"lovea_thumb.gif"},
		{key:"[泪]",val:"sada_thumb.gif"},
		{key:"[偷笑]",val:"heia_thumb.gif"},
		{key:"[亲亲]",val:"qq_thumb.gif"},
		{key:"[生病]",val:"sb_thumb.gif"},
		{key:"[太开心]",val:"mb_thumb.gif"},
		{key:"[懒得理你]",val:"ldln_thumb.gif"},
		{key:"[右哼哼]",val:"yhh_thumb.gif"},
		{key:"[左哼哼]",val:"zhh_thumb.gif"},
		{key:"[嘘]",val:"x_thumb.gif"},
		{key:"[衰]",val:"cry.gif"},
		{key:"[委屈]",val:"wq_thumb.gif"},
		{key:"[吐]",val:"t_thumb.gif"},
		{key:"[打哈欠]",val:"k_thumb.gif"},
		{key:"[抱抱]",val:"bba_thumb.gif"},
		{key:"[怒]",val:"angrya_thumb.gif"},
		{key:"[疑问]",val:"yw_thumb.gif"},
		{key:"[馋嘴]",val:"cza_thumb.gif"},
		{key:"[拜拜]",val:"88_thumb.gif"},
		{key:"[思考]",val:"sk_thumb.gif"},
		{key:"[汗]",val:"sweata_thumb.gif"},
		{key:"[困]",val:"sleepya_thumb.gif"},
		{key:"[睡觉]",val:"sleepa_thumb.gif"},
		{key:"[钱]",val:"money_thumb.gif"},
		{key:"[失望]",val:"sw_thumb.gif"},
		{key:"[酷]",val:"cool_thumb.gif"},
		{key:"[花心]",val:"hsa_thumb.gif"},
		{key:"[哼]",val:"hatea_thumb.gif"},
		{key:"[鼓掌]",val:"gza_thumb.gif"},
		{key:"[晕]",val:"dizzya_thumb.gif"},
		{key:"[悲伤]",val:"bs_thumb.gif"},
		{key:"[抓狂]",val:"crazya_thumb.gif"},
		{key:"[黑线]",val:"h_thumb.gif"},
		{key:"[阴险]",val:"yx_thumb.gif"},
		{key:"[怒骂]",val:"nm_thumb.gif"},
		{key:"[心]",val:"hearta_thumb.gif"},
		{key:"[伤心]",val:"unheart.gif"},
		{key:"[猪头]",val:"pig.gif"},
		{key:"[ok]",val:"ok_thumb.gif"},
		{key:"[耶]",val:"ye_thumb.gif"},
		{key:"[good]",val:"good_thumb.gif"},
		{key:"[不要]",val:"no_thumb.gif"},
		{key:"[赞]",val:"z2_thumb.gif"},
		{key:"[来]",val:"come_thumb.gif"},
		{key:"[弱]",val:"sad_thumb.gif"},
		{key:"[蜡烛]",val:"lazu_thumb.gif"},
		{key:"[蛋糕]",val:"cake.gif"},
		{key:"[钟]",val:"clock_thumb.gif"},
		{key:"[话筒]",val:"m_thumb.gif"}
	];
	var textarea = $("#J_Content")[0];
	var faceBtn = $("#J_FaceBtn");
	var faceBtnWrapDom = null;
	var faceIsShow = false;
	var toggleFaceBtns = function(){
		if(!faceBtnWrapDom){
			var html = '<div class="faceWrap"><div class="faceWrapBorder clearfix">'
				for(var i=0;i<FACEJSON.length;i++){
					html += '<a faceval="'+FACEJSON[i].key+'" style="background:#fff url(/assets/emotion/'+FACEJSON[i].val+') 4px 4px no-repeat;" href="javascript:;" title="'+FACEJSON[i].key+'" unselectable="on">'+FACEJSON[i].key+'</a>'
				}
			html += '</div></div>';
			faceBtn.append(html);
			faceBtnWrapDom = $("#J_FaceBtn .faceWrap");
			faceIsShow = true;
		}else{
			if(faceIsShow){
				faceBtnWrapDom.hide();
				faceIsShow = false;
			}else{
				faceBtnWrapDom.show();
				faceIsShow = true;
			}
		}
	}
	var insert = function(block){	
		var s = get();
		if (document.selection) {
			var newSelection = document.selection.createRange();
			newSelection.text = block;
		} else {
			textarea.value =  textarea.value.substring(0, s.caretPosition)  + block + textarea.value.substring(s.caretPosition + s.selection.length, textarea.value.length);
		}
		set(s.caretPosition,block.length);
		faceBtnWrapDom.hide();
		faceIsShow = false;
	}
	// set a selection
	var set = function(start,len){
		if (textarea.createTextRange){
			// quick fix to make it work on Opera 9.5
			if ($.browser.opera && $.browser.version >= 9.5 && len == 0) {
				return false;
			}
			range = textarea.createTextRange();
			range.collapse(true);
			range.moveStart('character', start + len); 
			range.moveEnd('character', 0); 
			range.select();
		} else if (textarea.setSelectionRange ){
			textarea.setSelectionRange(start + len, start + len);
		}
		textarea.focus();
	}
	// get the selection
	var get = function(){
		textarea.focus();
		var selection = null;
		var caretPosition = 0;
		if (document.selection) {
			selection = document.selection.createRange().text;
			if ($.browser.msie) { // ie
				var range = document.selection.createRange(), rangeCopy = range.duplicate();
				rangeCopy.moveToElementText(textarea);
				caretPosition = -1;
				while(rangeCopy.inRange(range)) {
					rangeCopy.moveStart('character');
					caretPosition ++;
				}
			} else { // opera
				caretPosition = textarea.selectionStart;
			}
		} else { // gecko & webkit
			caretPosition = textarea.selectionStart;
			selection = textarea.value.substring(caretPosition, textarea.selectionEnd);
		} 
		return {selection:selection,caretPosition:caretPosition};
	}
	$("body").bind("click",function(){
		if(faceBtnWrapDom){
			faceBtnWrapDom.hide();
			faceIsShow = false;
		}
	})
	faceBtn.bind("click",function(){
		var e = arguments[0] || window.event,
		target = e.srcElement ? $(e.srcElement) : $(e.target),
		faceval = target.attr("faceval"),
		faceBtn = target.attr("faceBtn");
		if(faceval){
			insert(faceval);
		}else if(faceBtn){
			toggleFaceBtns();
		}
		if(e.stopPropagation) {
			e.stopPropagation();
		}else{
			e.cancelBubble = true;
		}
	})
	var data = null;
	var decodeFace = function(html){
		//插入表情数组
		var length = FACEJSON.length;
		if(!data){
			data = {};
			for(var i = 0;i < length;i++){
				data[FACEJSON[i].key] = '<img src="/assets/emotion/'+FACEJSON[i].val+'" unselectable="on" title="'+FACEJSON[i].key+'" alt="'+FACEJSON[i].key+'">';
			}
		}
		html = html.replace(/\[[^\[\]]+\]/gi,function(tag){
			if(data[tag]){
				return data[tag];
			}else{
				return tag;
			}
		})
		return html;
	}
	$("#J_ForumPost .J_PostCon").each(function(){
		var $this = $(this);
		var html = $this.html();
		$this.data("content",html)
		html = decodeFace(html);
		$this.html(html);
	})
	$("#J_ForumPost .post-quoteContent").each(function(){
		var $this = $(this);
		var html = $this.html();
		html = decodeFace(html);
		$this.html(html);
	})
	var SimpleEditor = {
		richText2text:function(val){
			return val.replace(/\<[^\>]+\>/g,"");
		}
	};
	$.smeite.simpleEditor = SimpleEditor;

//})(jQuery)

});