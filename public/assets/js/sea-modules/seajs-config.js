/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-8
 * Time: 下午9:28
 */
seajs.config({

    // 别名配置
    alias: {
        'es5-safe': 'ucloud/es5-safe/0.9.2/es5-safe',
        'json': 'gallery/json/1.0.3/json',
         '$': 'jquery/jquery/1.10.1/jquery',
        'cookie': 'arale/cookie/1.0.2/cookie',
        'dialog': 'arale/dialog/1.1.2/dialog',
        'confirmbox':'arale/dialog/1.1.2/confirmbox'
    },

    // 预加载项
    preload: [
        Function.prototype.bind ? '' : 'es5-safe',
        this.JSON ? '' : 'json'
    ],

    // 调试模式
    debug: false,

    // Sea.js 的基础路径
    base: '/assets/js/sea-modules',

    // 文件编码
    charset: 'utf-8'
});