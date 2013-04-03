/*
 全站模块启动js
 * Created by zuosanshao.
 * User: Administrator
 * Date: 12-10-22
 * Time: 下午8:58
 * Email:zuosanshao@qq.com
 * @contain: 前端基础功能插件
 * @depends: jquery.js
 * Since: 2011-11-07
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */


//封装加载文件方法
define(function(require, exports) {
  exports.load = function(filename) {
	if (!('forEach' in Array.prototype)) {//ie6-ie8不支持forEach
	    Array.prototype.forEach= function(action, that /*opt*/) {
	        for (var i= 0, n= this.length; i<n; i++)
	            if (i in this)
	                action.call(that, this[i], i, this);
	    };
	}
    filename.split(',').forEach(function(modName) {
        if (modName) {
            require.async(modName, function(mod) {
                if (mod && mod.init) {
                    mod.init();
                }
            });
        }
    });
  };
  
  //全站都需要加载的通用模块

});