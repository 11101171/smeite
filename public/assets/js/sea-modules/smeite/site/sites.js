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
            element: '#J_siteFlash',
            panels: '#J_siteFlash  .items .item',
            triggers:"#J_siteFlash .navi li",
            activeTriggerClass:"active",
            effect: 'fade',
            easing: 'easeOutStrong',
            interval: 6000
        }).render();
    })

});