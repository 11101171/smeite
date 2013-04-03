/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */
define(function(require, exports){
    var $ = jQuery = require("jquery");
    require("app/common/validator")($);
    jQuery(function() {
        $("#J_ModifyPwd").validator();
    });
});
