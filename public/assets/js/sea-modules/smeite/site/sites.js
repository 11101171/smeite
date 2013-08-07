/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-6-22  上午10:22
 * ModifyTime :
 * ModifyContent:  for  site scala html
 * http://www.smeite.com/
 *
 */
define(function(require, exports) {
    var $  = require("$");
    var Slide = require("slide")

    $(function(){

        var siteFlash = new Slide({
            element: '#J_flash',
            panels: '#J_flash  .items .item',
            triggers:"#J_flash .navi li",
            activeTriggerClass:"active",
            effect: 'fade',
            easing: 'easeIn',
            interval: 6000
        }).render();
    })

});