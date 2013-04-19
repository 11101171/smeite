/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户关注、取消关注
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */

define(function(require, exports){
	var $ = jQuery = require("jquery");
	window.ZeroClipboard = require("module/zeroclipboard");
	
	var clip = new ZeroClipboard.Client();
	clip.setHandCursor( true );
	clip.addEventListener('mouseover', function (client) {
		clip.setText($('#clip-text').val());
	});
	clip.glue('J_Clip');
	document.getElementById("J_Clip").onmouseover = function(){
		clip.reposition(this);
	}
		

});
