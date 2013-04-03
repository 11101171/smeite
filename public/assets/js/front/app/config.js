/**
 * Created by zuosanshao.
 * User: smeite.com
 * Email:zuosanshao@qq.com
 * @description:
 * @depends:
 * Includes:
 * Since: 13-2-26  下午4:07
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
seajs.config({
    //配置路径别名
    alias: {
       "jquery" : "jquery/tools/1.2.7/jquery.tools.min.js",
        "json" : "module/json",
        "redactor":"module/jquery.redactor.js",
        "smeite" : "app/smeite"
    },
    //预加载基础文件
    preload: [
        "jquery","smeite"
    ],
    debug : false,
    map: [
        [ /^(.*\.(?:css|js))(.*)$/i, '$1?t=' + SMEITER.staticVersion + '.js' ]//时间戳
    ],
  plugins: ['shim'],

    // Configure shim for non-CMD modules
    shim: {
        'jquery': {
            exports: 'jQuery'
        },
        'jquery-plugins': {
            match: /jquery\.[a-z].*\.js/,
            deps: ['jquery']
        }
    },
    base: '/assets/js/front/'

});