/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends:
 * Since:13-1-2下午7:07
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */


define(function(require, exports){
    var $  = require("$");
    require("smeite/common/validator")($);
    jQuery(function() {
        $("#setForm").validator();
    });
});