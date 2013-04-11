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
    // 激活 shim 插件
    plugins: ['shim'],
    // shim 配置项
    alias: {
       'jquery' : {
          src:'jquery/tools/1.2.7/jquery.tools.min.js',
           exports:'jQuery'
       },
        'redactor':{
           src: 'module/jquery.redactor.js',
           deps:['jquery']
        },
        'smeite': 'app/smeite',
        'json' : 'module/json'
    },
    //预加载基础文件
    preload: [
       'smeite',
        this.JSON ? '' : 'json'
    ],
    debug : false,
    base: '/assets/js/front/'
});