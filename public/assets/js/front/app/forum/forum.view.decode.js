define(function(require, exports) {
	var $ = jQuery = require("jquery");
	require("smeite");
/*
 * Copyright 2011-2012, Guang.com
 * @contain: 解析帖子内容
 * @depends: jquery.js
 * 使用逛的解决方案
 */

//(function ($) {
	//to do 可以将要decode的html用&##&连接在一起decode,完成后split,这样重复的decode工作变成一次完成，装配html还是那么多次
	if($.smeite.editor){
		//var time = new Date();
		var editor = $.smeite.editor;
		var contentDecode = editor.contentDecode;
		$("#J_PostArtical").each(function(){

			var $this = $(this);
			var html = $this.html();
			html = contentDecode.call(editor,html);

			$this.html(html);
		})
		$("#J_ForumPost .J_PostCon").each(function(){
			var $this = $(this);
			var html = $this.html();
			html = contentDecode.call(editor,html);
			$this.html(html);
		})
		$("#J_ForumPost .post-quoteContent").each(function(){
			var $this = $(this);
			var html = $this.html();
			html = editor.onlyDecodeFace.call(editor,html);
			$this.html(html);
		})
		//var time2 = new Date();
		//alert(time2-time);
		$("#J_ForumPost").show();
		/*if(editor.imgviewDoms){
			editor.imgDecode();
		}*/
		if(editor.baobeiviewDoms){
			editor.baobeiDecode();
		}
	}




//})(jQuery)


});
