/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @contain: 用于 联系我们、关于我们等底部信息页
 * @depends:
 * Includes:
 * Since: 12-12-24  上午11:35
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
define(function(require, exports) {
    var $ = jQuery = require("$");
    var Slide = require("slide")
    var Tabs = require("tabs")

    var siteFlash = new Slide({
        element: '#J_flash',
        panels: '#J_flash  .items .item',
        triggers:"#J_flash .navi li",
        activeTriggerClass:"active",
        effect: 'fade',
        easing: 'easeIn',
        interval: 6000
    }).render();

    var seasonTabs  = new Tabs({
        element: '#J_seasonShicai',
        triggers: '#J_seasonShicai .tabs li',
        panels: '#J_seasonShicai .panel',
        activeIndex: 0,
        activeTriggerClass:"active",
        effect: 'fade'
    }).render();
})