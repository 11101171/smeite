/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户创造小站页面js
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");



        function validata(){
            if (!/\.(gif|jpg|png|jpeg|bmp)$/i.test(picFileVal)) {
                alert('请上传标准图片文件,支持gif,jpg,png,jpeg.');
                return false;
            }
            return true;
        }
        //返回提交成功后的操作
        window.publishPicSuccess =  function(code, picType, picSrc){

            switch(code){
                case "100" : //成功
                    //图片获取成功
                    switch(picType){
                        case "themeBanner" :
                            UserTheme.themeBanner.css("backgroundImage", 'url("' + picSrc + '")');
                            $("#J_HeaderPicBtn").data("src", picSrc);
                            break;
                        case "themeBg" :
                            UserTheme.page.css("backgroundImage", 'url("' + picSrc + '")');
                            $("#J_PagePicBtn").data("src", picSrc);
                            break;
                    }

                    break;
                case "101" : //程序异常
                    alert("亲,上传失败了, 重新提交试试！");
                    break;
            }
        }
        window.submitPic = function(obj, rangeType){
            if ($(obj).data("isSubmit") != 1){
                submitRun();
            }
            function submitRun(){
                $(obj).data("isSubmit", 1);
                var $picUploadTarget = $("#" + rangeType + "picUploadTarget");
                picFileVal = $(obj).val();
                $(obj).closest('form').submit();

                if (validata()) {
                    $(obj).closest('form').submit();
                }
                $(obj).data("isSubmit", 0);
                return false;
            }
        }

    $(function(){
        //限制输入字数
        $.smeite.wordCount.init($("#J_TopicTitle"),$("#J_TitleNum"),32);
        $.smeite.wordCount.init($("#J_TopicIntro"),$("#J_IntroNum"),200);

        /* select 选择*/
        $(".type").click(function(){
            var $this =$(this)
            $(this).siblings().removeClass("active")
            $(this).addClass("active")
            $("#J_cid").val($(this).data("id"))
            if($this.data("type")=="sifangcai"){
                $this.siblings(".sub-item").show()
            }else{
                $this.siblings(".sub-item").hide()
            }
        })

        //表单校验
        var $topicTitle = $("#J_TopicTitle"),
            $topicIntro = $("#J_TopicIntro");
        $("#J_TopicEditFm").submit(function(){

            var topicIntro = $topicIntro.val();
            if($.trim($topicTitle.val())==""){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($topicTitle,"标题不能为空~");
                return false;
            }else if($.smeite.util.getStrLength($topicTitle.val())>15){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($topicTitle,"字数不能超过32字！");
                return false;
            }else if($.smeite.util.getStrLength($topicTitle.val())>200){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($topicTitle,"字数不能超过200字！");
                return false;
            }
        });

        //标签操作
        $("#J_TopicTags").keyup(function(e){
            //限制每个标签的中文长度
            var MaxSingleTagLength = 14,
                MaxAllTagsLength = 64,
                thisVal = $(this).val();
            if($.smeite.util.getStrLength($.smeite.util.htmlToTxt(thisVal))<=MaxAllTagsLength){
                var $this = $(this);
                thisVal = thisVal.replace(/\uff0c|\s+/g,",");
                while(thisVal.indexOf(',,')>=0){
                    thisVal = thisVal.replace(',,',',')
                }
                var thisValueArr = thisVal.split(","),
                    thisValueArrIndex = 0,
                    istoolong = false;
                for(;thisValueArrIndex<thisValueArr.length;thisValueArrIndex++){
                    var val = thisValueArr[thisValueArrIndex]
                    if($.smeite.util.htmlToTxt(val).length>MaxSingleTagLength){
                        istoolong = true;
                        thisValueArr[thisValueArrIndex] = $.smeite.util.substring4ChAndEn(val,MaxSingleTagLength);
                    }
                }
                if(istoolong){
                    thisVal = thisValueArr.join(",");
                }
                if(thisVal != this.value){
                    this.value = thisVal;
                }
            }else{
                this.value = $.smeite.util.substring4ChAndEn(thisVal,64);
            }
        });


    });


});