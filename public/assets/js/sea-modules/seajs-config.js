/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-8
 * Time: 下午9:28
 */
seajs.config({
    // Sea.js 的基础路径
    base: '/assets/js/sea-modules/',
    // 别名配置
    alias: {

        'es5-safe': 'ucloud/es5-safe/0.9.2/es5-safe',
        'json': 'gallery/json/1.0.3/json',
         '$': 'jquery/jquery/1.10.1/jquery',
        'cookie': 'arale/cookie/1.0.2/cookie',
        'mask': 'arale/overlay/1.1.2/mask',
        'overlay': 'arale/overlay/1.1.2/overlay',
        'dialog': 'arale/dialog/1.1.2/dialog',
        'confirmbox':'arale/dialog/1.1.2/confirmbox',
        'position': 'arale/position/1.0.1/position',
        'slide': 'arale/switchable/0.9.15/slide',
        'tabs': 'arale/switchable/0.9.15/tabs',
        'upload': 'arale/upload/1.0.0/upload',
        'zeroclipboard': 'gallery/zeroclipboard/1.1.6/zeroclipboard',
        'imgAreaSelect':'smeite/module/imgAreaSelect',
       'smeite':'smeite/smeite'
    },

    // 预加载项
    preload: [
        Function.prototype.bind ? '' : 'es5-safe', 'smeite',
        this.JSON ? '' : 'json'
    ],

    // 调试模式
    debug: false,



    // 文件编码
    charset: 'utf-8'
});