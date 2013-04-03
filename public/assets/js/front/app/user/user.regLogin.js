/**
 * Created by zuosanshao.
 * User: Administrator
 * Date: 12-10-2
 * Time: 下午6:52
 * Email:zuosanshao@qq.com
 */
define(function (require,exports) {
    var $ =jQuery= require("jquery");
    $.tools.validator.localize("cn", {
        ':email'  		: 'email 格式不对',
        ':number' 		: '请输入数字',
        '[max]'	 		: '必须小于 $1',
        '[min]'	 		: '必须大于 $1',
        '[required]' 	: '必填'
    });
    $(function () {
        $("form").validator({lang:'cn'});

    })
});