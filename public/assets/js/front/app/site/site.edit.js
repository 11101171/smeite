/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户创造小镇页面js
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    require("module/imgareaselect");
  var Uploader = require("module/upload")
    $.smeite.photoarea = null;
    $.smeite.rotate = null;
    $.smeite.setting = {
        setAvatar: {
            preview: function(img,selection) {
                var w = $('#photo').width(),h = $('#photo').height();
                if (!selection.width || !selection.height){
                    return false;
                }

                $("#area-x1").val(selection.x1);
                $("#area-y1").val(selection.y1);
                $("#area-x2").val(selection.x1 + selection.width);
                $("#area-y2").val(selection.y1 + selection.height);
            },
            setPhoto: function(path){
                var w,h,x1,y1,x2,y2;
                $('#photo').attr("src","").attr("src",path);
                $("#thumb-path").val(path);
                var image = $('#photo')[0],imageTime;
                if($.browser.msie){
                    if($.browser.version==6.0){
                        image.onreadystatechange = function(){
                            if(image.readyState=="complete" || image.readyState=="loaded"){
                                dosth();
                            }
                        }
                    }else{
                        imageTime = setInterval(function(){
                            if(image.readyState=="complete"){
                                clearInterval(imageTime);
                                dosth();
                            }
                        },200);
                    }
                }else{
                    image.onload = function(){
                        if (image.complete == true){
                            dosth();
                        }
                    }
                }
                var dosth = function(){
                    w = $('#photo').width();
                    h = $('#photo').height();
                    if (w < 180 || h < 180){
                        if(w<h){
                            x1 = 0;
                            y1 = h/2-w/2;
                            x2 = x1+w;
                            y2 = y1+w;
                        }else{
                            y1 = 0;
                            x1 = w/2-h/2;
                            y2 = y1+h;
                            x2 = x1+h;
                        }
                    }else{
                        x1 = w/2-90;
                        y1 = h/2-90;
                        x2 = x1+180;
                        y2 = y1+180;
                    }
                    //图片裁切
                    $("#faceUpload2 .face-submit").show();
                    $.smeite.photoarea = $('#photo').imgAreaSelect({ aspectRatio:'1:1', handles:true,
                        fadeSpeed:200, onSelectChange:$.smeite.setting.setAvatar.preview, instance:true, persistent:true, minWidth:180, minHeight:180 });
                    $.smeite.photoarea.setSelection(x1, y1, x2, y2, true);
                    $.smeite.photoarea.setOptions({ show:true });
                    $.smeite.photoarea.update();
                    var selection = $.smeite.photoarea.getSelection(true);
                    $.smeite.setting.setAvatar.preview($('#photo')[0],selection);

                };
            }

        }
    }


    $(function(){

        $("#J_uploadImage").click(function(){
            if(!$("#photoDialog")[0]){
                var html = "";
                html += '<div id="photoDialog" class="g-dialog photo-dialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>上传图片</h3></div>';
                html += '<div class="bd clearfix">';
                html += '<form id="faceUpload" name="faceUpload" enctype="multipart/form-data" method="post" target="photo-frame" action="/uploadPic/select">';
                html += '<div class="photo-row clearfix">';
                html += '<input type="button" value="上传图片" class="bbl-btn upload-cover" />';
                html += '<input type="file" class="upload-btn" name="filedata" id="J_FilePath" />';
                html += '</div>';
                html += '<div class="photo-row pt10 pb15">';
                html += '<span class="gc6">支持JPG、GIF、PNG格式，且文件小于2M</span>';
                html += '</div>';
                html += '<div class="photo-row">';
                html += '<div class="photo-box">';
                html += '<span><img src="/assets/ui/blank.gif" id="photo" alt="" /> </span>';
                html += '</div>';
                html += '</div>';
                html += '</form>';
                html += '<form class="mt20" id="faceUpload2" name="faceUpload2" enctype="multipart/form-data" method="post" action="/site/doUploadPic">';
                html += '<input type="hidden" value="" name="thumb-path" id="thumb-path" />';
                html += '<input type="hidden" value="" name="area-x1" id="area-x1" />';
                html += '<input type="hidden" value="" name="area-y1" id="area-y1" />';
                html += '<input type="hidden" value="" name="area-x2" id="area-x2" />';
                html += '<input type="hidden" value="" name="area-y2" id="area-y2" />';
                html += '<div class="photo-row face-submit dn">';
                html += '<input type="submit" class="bbl-btn submit" value="保存图片"/>';
                html += '<span id="J_Waiting" class="ml10 gc6 dn"><img src="/assets/ui/loading16.gif">请耐心等待…</span>';
                html += '</div>';
                html += '</form>';
                html += '<iframe style="width:0px;height:0px;padding:0px;" src="" frameborder="0" name="photo-frame"></iframe>';
                html += '</div>';
                html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
                $("body").append(html);
                $("#photoDialog").overlay({
                    top: 50,
                    fixed: false,
                    mask: {
                        color: '#000',
                        loadSpeed: 200,
                        opacity: 0.3
                    },
                    closeOnClick: false,
                    load: true
                });

                $("#J_FilePath").change(function(){
                    $("#faceUpload").submit();
                    $('#photo').attr("src","/assets/ui/loading1.gif");
                });
                $("#faceUpload2").submit(function(){
                    $this = $(this);
                    $("#faceUpload2 input[type=submit]")[0].disabled = "disabled";
                    $("#faceUpload2 input[type=submit]").removeClass("bbl-btn").addClass("disabled");
                    $("#J_Waiting").show();
                    $.post($this.attr("action"),$this.serializeArray(),function(data){
                        $("#J_Waiting").hide();
                        $("#faceUpload2 input[type=submit]")[0].disabled = "";
                        $("#faceUpload2 input[type=submit]").removeClass("disabled").addClass("bbl-btn");
                        if(data.code=="100"){
                            $("#J_uploadImgShow").attr("src",data.src)
                            $(".site-logo").show()
                            $("#faceUpload")[0].reset();
                            $("#faceUpload2")[0].reset();
                            $("#J_uploadImg").val(data.src)
                            $("#faceUpload2 .face-submit").hide();
                             $("#photoDialog").overlay().close();

                        }
                    });
                    return false;
                });
                $("#photoDialog .close").unbind("click").click(function(){
                    $("#faceUpload")[0].reset();
                    $("#faceUpload2")[0].reset();
                    $('#photo').attr("src","");
                    $("#faceUpload2 .face-submit").hide();

                    $("#photoDialog").overlay().close();
                });
            }else{
                $("#photoDialog").data("overlay").load();
            }
        });

          new Uploader({
         trigger: '#J_LocalImgFormSubmit',
         name: 'fileData',
         action: '/site/uploadPic',
         data: {'xsrf': 'hash'}
         }).success(function(data) {
                  $("#J_uploadImgShow").attr("src",data.src)
                  $(".site-logo").show()
                  $("#J_uploadImg").val(data.src)
         }).error(function(file) {
         alert(file);
         });

        //限制输入字数
        $.smeite.wordCount.init($("#J_title"),$("#J_titleNum"),32);
        $.smeite.wordCount.init($("#J_intro"),$("#J_introNum"),200);

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
        var $title = $("#J_title"),
            $intro = $("#J_intro");
        $("#J_editForm").submit(function(){

            var topicIntro = $intro.val();
            if($.trim($title.val())==""){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($title,"标题不能为空~");
                return false;
            }else if($.smeite.util.getStrLength($title.val())>15){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($title,"字数不能超过32字！");
                return false;
            }else if($.smeite.util.getStrLength($title.val())>200){
                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.smeite.tip.show($title,"字数不能超过200字！");
                return false;
            }
        });

        //标签操作
        $("#J_tags").keyup(function(e){
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