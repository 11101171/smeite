define(function (require, exports) {
    var $  = require("$");

    /*
     * Copyright 2011-2012, Guang.com
     * @contain: 富文本编辑器
     * @depends: jquery.js
     * smeite.com 采用guang的富文本编辑器  ,去掉了vedio ,修改了goods decode ，感谢guang的解决方案    图片直接上传到编辑器中。
     * zuosanshao@qq.com
     */

//(function ($) {
    var COLOR = [
        {
            key:"333333",
            val:"灰色-80%"
        },
        {
            key:"666666",
            val:"灰色-60%"
        },
        {
            key:"999999",
            val:"灰色-40%"
        },
        {
            key:"cccccc",
            val:"灰色-20%"
        },
        {
            key:"bb0000",
            val:"深红"
        },
        {
            key:"dd0000",
            val:"红色"
        },
        {
            key:"ee4488",
            val:"粉红"
        },
        {
            key:"ff66dd",
            val:"淡紫"
        },
        {
            key:"333399",
            val:"深蓝"
        },
        {
            key:"0066cc",
            val:"蓝色"
        },
        {
            key:"0099cc",
            val:"天蓝"
        },
        {
            key:"66cccc",
            val:"淡蓝"
        },
        {
            key:"336600",
            val:"深绿"
        },
        {
            key:"999900",
            val:"深黄"
        },
        {
            key:"cccc33",
            val:"淡黄"
        },
        {
            key:"77cc33",
            val:"淡绿"
        },
        {
            key:"663300",
            val:"咖啡"
        },
        {
            key:"cc6633",
            val:"褐色"
        },
        {
            key:"ff9900",
            val:"橙黄"
        },
        {
            key:"ffcc33",
            val:"黄色"
        }
    ];
    var FONTSIZE = [1, 2, 3, 4];
    //RGB(0,0,0)转#000000
    var RGB2HEX = {
        "_515151":"333333",
        "_102102102":"666666",
        "_153153153":"999999",
        "_204204204":"cccccc",
        "_18700":"bb0000",
        "_22100":"dd0000",
        "_23868136":"ee4488",
        "_255102221":"ff66dd",
        "_5151153":"333399",
        "_0102204":"0066cc",
        "_0153204":"0099cc",
        "_102204204":"66cccc",
        "_511020":"336600",
        "_1531530":"999900",
        "_20420451":"cccc33",
        "_11920451":"77cc33",
        "_102510":"663300",
        "_20410251":"cc6633",
        "_2551530":"ff9900",
        "_25520451":"ffcc33"
    };
    //表情配对表
    var FACEJSON = [
        {
            key:"[织]",
            val:"zz2_thumb.gif"
        },
        {
            key:"[神马]",
            val:"horse2_thumb.gif"
        },
        {
            key:"[浮云]",
            val:"fuyun_thumb.gif"
        },
        {
            key:"[给力]",
            val:"geili_thumb.gif"
        },
        {
            key:"[围观]",
            val:"wg_thumb.gif"
        },
        {
            key:"[威武]",
            val:"vw_thumb.gif"
        },
        {
            key:"[熊猫]",
            val:"panda_thumb.gif"
        },
        {
            key:"[兔子]",
            val:"rabbit_thumb.gif"
        },
        {
            key:"[奥特曼]",
            val:"otm_thumb.gif"
        },
        {
            key:"[囧]",
            val:"j_thumb.gif"
        },
        {
            key:"[互粉]",
            val:"hufen_thumb.gif"
        },
        {
            key:"[礼物]",
            val:"liwu_thumb.gif"
        },
        {
            key:"[呵呵]",
            val:"smilea_thumb.gif"
        },
        {
            key:"[嘻嘻]",
            val:"tootha_thumb.gif"
        },
        {
            key:"[哈哈]",
            val:"laugh.gif"
        },
        {
            key:"[可爱]",
            val:"tza_thumb.gif"
        },
        {
            key:"[可怜]",
            val:"kl_thumb.gif"
        },
        {
            key:"[挖鼻屎]",
            val:"kbsa_thumb.gif"
        },
        {
            key:"[吃惊]",
            val:"cj_thumb.gif"
        },
        {
            key:"[害羞]",
            val:"shamea_thumb.gif"
        },
        {
            key:"[挤眼]",
            val:"zy_thumb.gif"
        },
        {
            key:"[闭嘴]",
            val:"bz_thumb.gif"
        },
        {
            key:"[鄙视]",
            val:"bs2_thumb.gif"
        },
        {
            key:"[爱你]",
            val:"lovea_thumb.gif"
        },
        {
            key:"[泪]",
            val:"sada_thumb.gif"
        },
        {
            key:"[偷笑]",
            val:"heia_thumb.gif"
        },
        {
            key:"[亲亲]",
            val:"qq_thumb.gif"
        },
        {
            key:"[生病]",
            val:"sb_thumb.gif"
        },
        {
            key:"[太开心]",
            val:"mb_thumb.gif"
        },
        {
            key:"[懒得理你]",
            val:"ldln_thumb.gif"
        },
        {
            key:"[右哼哼]",
            val:"yhh_thumb.gif"
        },
        {
            key:"[左哼哼]",
            val:"zhh_thumb.gif"
        },
        {
            key:"[嘘]",
            val:"x_thumb.gif"
        },
        {
            key:"[衰]",
            val:"cry.gif"
        },
        {
            key:"[委屈]",
            val:"wq_thumb.gif"
        },
        {
            key:"[吐]",
            val:"t_thumb.gif"
        },
        {
            key:"[打哈欠]",
            val:"k_thumb.gif"
        },
        {
            key:"[抱抱]",
            val:"bba_thumb.gif"
        },
        {
            key:"[怒]",
            val:"angrya_thumb.gif"
        },
        {
            key:"[疑问]",
            val:"yw_thumb.gif"
        },
        {
            key:"[馋嘴]",
            val:"cza_thumb.gif"
        },
        {
            key:"[拜拜]",
            val:"88_thumb.gif"
        },
        {
            key:"[思考]",
            val:"sk_thumb.gif"
        },
        {
            key:"[汗]",
            val:"sweata_thumb.gif"
        },
        {
            key:"[困]",
            val:"sleepya_thumb.gif"
        },
        {
            key:"[睡觉]",
            val:"sleepa_thumb.gif"
        },
        {
            key:"[钱]",
            val:"money_thumb.gif"
        },
        {
            key:"[失望]",
            val:"sw_thumb.gif"
        },
        {
            key:"[酷]",
            val:"cool_thumb.gif"
        },
        {
            key:"[花心]",
            val:"hsa_thumb.gif"
        },
        {
            key:"[哼]",
            val:"hatea_thumb.gif"
        },
        {
            key:"[鼓掌]",
            val:"gza_thumb.gif"
        },
        {
            key:"[晕]",
            val:"dizzya_thumb.gif"
        },
        {
            key:"[悲伤]",
            val:"bs_thumb.gif"
        },
        {
            key:"[抓狂]",
            val:"crazya_thumb.gif"
        },
        {
            key:"[黑线]",
            val:"h_thumb.gif"
        },
        {
            key:"[阴险]",
            val:"yx_thumb.gif"
        },
        {
            key:"[怒骂]",
            val:"nm_thumb.gif"
        },
        {
            key:"[心]",
            val:"hearta_thumb.gif"
        },
        {
            key:"[伤心]",
            val:"unheart.gif"
        },
        {
            key:"[猪头]",
            val:"pig.gif"
        },
        {
            key:"[ok]",
            val:"ok_thumb.gif"
        },
        {
            key:"[耶]",
            val:"ye_thumb.gif"
        },
        {
            key:"[good]",
            val:"good_thumb.gif"
        },
        {
            key:"[不要]",
            val:"no_thumb.gif"
        },
        {
            key:"[赞]",
            val:"z2_thumb.gif"
        },
        {
            key:"[来]",
            val:"come_thumb.gif"
        },
        {
            key:"[弱]",
            val:"sad_thumb.gif"
        },
        {
            key:"[蜡烛]",
            val:"lazu_thumb.gif"
        },
        {
            key:"[蛋糕]",
            val:"cake.gif"
        },
        {
            key:"[钟]",
            val:"clock_thumb.gif"
        },
        {
            key:"[话筒]",
            val:"m_thumb.gif"
        }
    ];
    //魔法开始
    var Editor = function (config) {
        return new Editor.fn.init(config);
    };
    Editor.prototype = Editor.fn = {
        config:{
            //将编辑器组装好后插入到textarea后面，textarea的ID
            textareaID:"J_ForumPostCon",
            formId:"J_ForumPostEditForm",
            toolbarId:"J_GuangEditorToolbar",

            //按钮参数配置
            btnFontSize:{
                cssName:"font-size",
                visible:true,
                exec:function (self) {
                    if (!self.FontSizeWrapDom || self.FontSizeWrapDom.length === 0) {
                        var html = '<div class="fontSizeWrap"><a btntype="btnFontSizeAction" size="1" style="font-size:12px;" href="javascript:;" title="小号" unselectable="on">小号</a><a btntype="btnFontSizeAction" size="2" style="font-size:14px;" href="javascript:;" title="标准" unselectable="on">标准</a><a btntype="btnFontSizeAction" size="3" style="font-size:16px;" href="javascript:;" title="大号" unselectable="on">大号</a><a btntype="btnFontSizeAction" size="4" style="font-size:18px;" href="javascript:;" title="特大" unselectable="on">特大</a></div>';
                        var config = self.config;
                        $('#' + config.toolbarId).append(html);
                        self.FontSizeWrapDom = $('#' + config.toolbarId + ' .fontSizeWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.FontSizeWrapDom;
                    } else {
                        if (self.curVisiableDom === self.FontSizeWrapDom) {
                            self.FontSizeWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom) {
                                self.curVisiableDom.hide();
                            }
                            self.FontSizeWrapDom.show();
                            self.curVisiableDom = self.FontSizeWrapDom;
                        }
                    }
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var val = null;
                    var reg_css = /size/i;
                    val = $(curElm).attr("size") || null;
                    if (!val && $parents) {
                        var length = $parents.length;
                        for (var i = 0; i < length; i++) {
                            val = $parents.eq(i).attr("size") || null;
                            if (val) {
                                break;
                            }
                        }
                    }
                    if (!self.curFontSize) {
                        self.curFontSize = null;
                    }
                    if (val != self.curFontSize) {
                        if (val == null) {
                            $('#' + self.config.toolbarId + ' .font-size a').text("标准");
                            self.curFontSize = null;
                        } else {
                            $('#' + self.config.toolbarId + ' .font-size a').text(self.config.btnFontSize.data["f" + val]);
                            self.curFontSize = val;
                        }
                    }
                },
                data:{
                    "f1":"小号",
                    "f2":"标准",
                    "f3":"大号",
                    "f4":"特大"
                },
                html:"<div class='font-btns font-size'><a href='javascript:;' btntype='btnFontSize' title='字号' unselectable='on'>标准</a></div>"
            },
            btnFontSizeAction:{
                exec:function (self, $srcElement) {
                    var size = $srcElement.attr("size");
                    self.execCommand("fontsize", size);
                    $('#' + self.config.toolbarId + ' .font-size a').text($srcElement.attr("title"));
                    self.curFontSize = size;
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            },
            btnFontBold:{
                cssName:"font-bold",
                visible:true,
                exec:function (self, $srcElement) {
                    if ($srcElement.parent().hasClass("font-bold-active")) {
                        $('#' + self.config.toolbarId + ' .font-bold').removeClass("font-bold-active");
                    } else {
                        $('#' + self.config.toolbarId + ' .font-bold').addClass("font-bold-active");
                    }
                    self.execCommand("bold", "");
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var reg_tagName = {
                        "b":true,
                        "strong":true
                    };
                    var reg_css = /bold/i;
                    var outerHTML = curElm.outerHTML.match(/\<[^\>]+\>/)[0];
                    var val = false;

                    if (reg_tagName[tagName] || reg_css.test(outerHTML)) {
                        val = true;
                    }
                    if (!val && $parents) {
                        var length = $parents.length
                        for (var i = 0; i < length; i++) {
                            var curDom = $parents[i];
                            var tagName = curDom.nodeName.toLowerCase();
                            var outerHTML = curDom.outerHTML.match(/\<[^\>]+\>/)[0];
                            if (reg_tagName[tagName] || reg_css.test(outerHTML)) {
                                val = true;
                                break;
                            }
                        }
                    }

                    var btnDom = $('#' + self.config.toolbarId + ' .font-bold');
                    var hasBold = btnDom.hasClass("font-bold-active");
                    if (val && !hasBold) {
                        $('#' + self.config.toolbarId + ' .font-bold').addClass("font-bold-active");
                    }
                    if (!val && hasBold) {
                        $('#' + self.config.toolbarId + ' .font-bold').removeClass("font-bold-active");
                    }
                },
                html:"<div class='font-btns font-bold'><a href='javascript:;' btntype='btnFontBold' title='粗体' unselectable='on'>粗体</a></div>"
            },
            btnFontColo:{
                cssName:"font-color",
                visible:true,
                exec:function (self) {
                    if (!self.FontColoWrapDom || self.FontColoWrapDom.length == 0) {
                        var html = '<div class="fontColoWrap">';
                        var length = COLOR.length;
                        for (var i = 0; i < length; i++) {
                            html += '<a btntype="btnFontColoAction" coloval="#' + COLOR[i].key + '" style="background-color:#' + COLOR[i].key + ';" href="javascript:;" title="' + COLOR[i].val + '" unselectable="on">#' + COLOR[i].key + '</a>'
                        }
                        html += '</div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.FontColoWrapDom = $('#' + self.config.toolbarId + ' .fontColoWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.FontColoWrapDom;
                    } else {
                        if (self.curVisiableDom == self.FontColoWrapDom) {
                            self.FontColoWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.FontColoWrapDom.show();
                            self.curVisiableDom = self.FontColoWrapDom;
                        }
                    }
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var val = null;
                    var reg_css = /color\:/i;
                    var reg_rgb = /rgb\(\s?(\d{1,3})\,\s?(\d{1,3})\,\s?(\d{1,3})\)/i;
                    var outerHTML = curElm.outerHTML.match(/\<[^\>]+\>/)[0];
                    var attrColor = $(curElm).attr("color");
                    if (attrColor) {
                        val = attrColor;
                    } else if (reg_css.test(outerHTML)) {
                        var rgbArr = outerHTML.match(reg_rgb);
                        if (rgbArr) {
                            var hex = RGB2HEX["_" + rgbArr[1] + rgbArr[2] + rgbArr[3]];
                            val = "#" + hex;
                        }
                        //to do reg hex
                    }
                    if (!val && $parents) {
                        var length = $parents.length
                        for (var i = 0; i < length; i++) {
                            var curDom = $parents[i];
                            var tagName = curDom.nodeName.toLowerCase();
                            var outerHTML = curDom.outerHTML.match(/\<[^\>]+\>/)[0];
                            var attrColor = $(curDom).attr("color");
                            if (attrColor) {
                                val = attrColor;
                            } else if (reg_css.test(outerHTML)) {
                                var rgbArr = outerHTML.match(reg_rgb);
                                if (rgbArr) {
                                    var hex = RGB2HEX["_" + rgbArr[1] + rgbArr[2] + rgbArr[3]];
                                    val = "#" + hex;
                                }
                                //to do reg style hex
                            }
                            if (val) {
                                break;
                            }
                        }
                    }
                    if (!self.curFontColor) {
                        self.curFontColor = null;
                    }
                    if (val != self.curFontColor) {
                        if (val == null) {
                            $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", "#333333");
                            self.curFontColor = null;
                        } else {
                            $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", val);
                            self.curFontColor = val;
                        }
                    }
                },
                html:"<div class='font-btns font-colo'><a href='javascript:;' btntype='btnFontColo' title='前景色' unselectable='on'><span btntype='btnFontColo' unselectable='on'><i btntype='btnFontColo' unselectable='on'></i></span></a></div>"
            },
            btnFontColoAction:{
                exec:function (self, $srcElement) {
                    var color = $srcElement.attr("coloval");
                    self.execCommand("forecolor", color);
                    $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", $srcElement.attr("coloval"));
                    self.curFontColor = color;
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            },
            btnFace:{
                visible:true,
                exec:function (self) {
                    if (!self.FaceWrapDom || self.FaceWrapDom.length == 0) {
                        var html = '<div class="faceWrap"><div class="faceWrapBorder clearfix">'
                        for (var i = 0; i < FACEJSON.length; i++) {
                            html += '<a btntype="btnFaceAction" faceval="' + FACEJSON[i].val + '" style="background:#fff url(/assets/img/emotion/' + FACEJSON[i].val + ') 4px 4px no-repeat;" href="javascript:;" title="' + FACEJSON[i].key + '" unselectable="on">' + FACEJSON[i].key + '</a>'
                        }
                        html += '</div></div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.FaceWrapDom = $('#' + self.config.toolbarId + ' .faceWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.FaceWrapDom;
                    } else {
                        if (self.curVisiableDom == self.FaceWrapDom) {
                            self.FaceWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.FaceWrapDom.show();
                            self.curVisiableDom = self.FaceWrapDom;
                        }
                    }
                },
                html:"<div class='media-btns face'><a href='javascript:;' btntype='btnFace' title='表情' unselectable='on'>表情</a></div>"
            },
            btnFaceAction:{
                exec:function (self, $srcElement) {
                    if (self.isIE678) {
                        self.insertHTML("<img unselectable='on' src='/assets/img/emotion/" + $srcElement.attr("faceval") + "' title='" + $srcElement.attr("title") + "' alt='" + $srcElement.attr("title") + "'/>");
                    } else {
                        var imgDom = self.iframeDocument.createElement("img");
                        imgDom.src = '/assets/img/emotion/' + $srcElement.attr("faceval");
                        imgDom.setAttribute("unselectable", "on")
                        imgDom.setAttribute("title", $srcElement.attr("title"))
                        imgDom.setAttribute("alt", $srcElement.attr("title"))
                        self.insertHTML(imgDom);
                    }
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            },
            btnBaobei:{
                visible:true,
                exec:function (self) {
                    if (!self.BaobeiWrapDom || self.BaobeiWrapDom.length == 0) {
                        var html = '<div class="baobeiWrap"><div class="content"><p class="title">将宝贝网址粘贴到下面框中：</p><div class="sg-form"><div class="clearfix"><input class="base-input sg-input" id="J_BaobeiUrl" name="url" value="" placeholder="http://" autocomplete="off"><input type="button" class="bbl-btn" id="J_InsertBaobei" value="确定"></div><div class="text-tip"></div></div><div class="sg-source"><p class="pt5 pb5 fl">已支持网站：</p><div class="source-list pt5 fl clearfix"><a class="icon-source icon-taobao" href="http://www.taobao.com/" target="_blank">淘宝网</a><a class="icon-source icon-tmall" href="http://www.tmall.com/" target="_blank">天猫商城</a></div></div><div class="tipbox-up"><em>◆</em><span>◆</span></div></div></div>';
                        var getBaobei = function (id) {
                      //      alert(id)
                            $.ajax({
                                url:"/editor/fetchBaobei",
                                type:"get",
                                dataType:"json",
                                data:{  id:id  },
                                success:function (json) {
                                    switch (json.code) {
                                        case 100:
                                        {
                                            json.id = id;
                                            json.pic = json.baobei.pic;
                                            json.name = json.baobei.name;
                                            if (self.isIE678) {
                                                self.insertHTML("<img unselectable='on' class='img-goods'data-goodsid='"+id+"' src='" + json.pic + "'/>");
                                            } else {


                                                var imgDom = self.iframeDocument.createElement("img");
                                                imgDom.src = json.pic;
                                                imgDom.setAttribute("class","img-goods")
                                                imgDom.setAttribute("data-goodsid",id)
                                                imgDom.setAttribute("unselectable", "on")
                                                //    imgDom.setAttribute("title", $srcElement.attr("title"))
                                                //     imgDom.setAttribute("alt", $srcElement.attr("title"))

                                                self.insertHTML(imgDom);
                                            }
                                            self.btnBaobeiUrl.val("");
                                            if (self.curVisiableDom) {
                                                self.curVisiableDom.hide();
                                                self.curVisiableDom = null;
                                            }
                                        }
                                            break;
                                        case 101:
                                        {
                                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                            $.smeite.tip.show(self.btnBaobei, json.msg);
                                        }
                                            break;
                                    }
                                }
                            });
                        }
                        var shareBaobei = function (url) {
                            var localUrl = "smeite.com";
                            if (url.indexOf(localUrl) != -1) {
                                var reg = /baobei\/(\d{1,12})/i;
                                var regResult = url.match(reg);
                                if (regResult && regResult[1]) {
                                    getBaobei(regResult[1]);
                                } else {
                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                    $.smeite.tip.show(self.BaobeiInerstSubmit, "站内宝贝链接错误");
                                }
                            } else {
                                if (url == "") {
                                    $(".text-tip").html('<span class="errc">宝贝网址不能为空~</span>').show();
                                } else if (!$.smeite.util.validSite(url)) {
                                    $(".text-tip").html('<span class="errc">暂时还不支持这个网站呢~</span>').show();
                                } else {
                                    $(".text-tip").html('<span class="gc6">宝贝信息抓取中…</span>').show();
                                    self.BaobeiInerstSubmit.disableBtn("bbl-btn");
                                    $.get(
                                        '/ugc/api/findProduct', {
                                            url:url
                                        },
                                        function (data) {
                                            if (data.code =="100") {
                                                $(".text-tip").html('');
                                                $.smeite.ugc.goodspub(data.product,getBaobei);
                                            } else if (data.code =="105") {
                                                $(".text-tip").html('');
                                                getBaobei(data.product.id);
                                            } else if (data.code == "101" || data.code == "106") {
                                                $(".text-tip").html('<span class="errc">宝贝信息抓取失败，请重试…</span>').show();
                                            } else if (data.code == "107") {
                                                $(".text-tip").html('<span class="errc">暂时还不支持这个宝贝…</span>').show();
                                            } else if (data.code == "108") {
                                                $(".text-tip").html('');
                                                getBaobei(data.product.id);
                                            } else if (data.code == "110") {
                                                $(".text-tip").html('<span class="errc">亲，该商品所在商家已列入黑名单，申诉请联系service@smeite.com</span>').show();
                                            } else if (data.code == "444") {
                                                alert("你已被禁止登录！");
                                                window.location.href = "/user/logout";
                                            } else if (data.code == "442") {
                                                alert("亲，请不要频繁推荐同一店铺商品");
                                            } else if (data.code == "443") {
                                                alert("由于过度推荐同店铺商品，账户已冻结，如有疑问请联系 service@smeite.com");
                                            } else if (data.code == "445") {
                                                alert("城管大队怀疑你恶意发广告，将禁止你发布商品的权利，申诉请联系service@smeite.com");
                                            }
                                            self.BaobeiInerstSubmit.enableBtn("bbl-btn");
                                        });
                                }
                            }
                        }
                        $('#' + self.config.toolbarId).append(html);
                        self.btnBaobeiUrl = $("#J_BaobeiUrl");
                        self.btnBaobei = $('#' + self.config.toolbarId + ' .baobei');
                        self.BaobeiInerstSubmit = $('#J_InsertBaobei');
                        self.BaobeiInerstSubmit.bind("click", function () {
                            shareBaobei($.trim(self.btnBaobeiUrl.val()));
                        })
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.BaobeiWrapDom = $('#' + self.config.toolbarId + ' .baobeiWrap');
                        self.curVisiableDom = self.BaobeiWrapDom;
                    } else {
                        if (self.curVisiableDom == self.BaobeiWrapDom) {
                            self.BaobeiWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.BaobeiWrapDom.show();
                            self.curVisiableDom = self.BaobeiWrapDom;
                        }
                    }
                },
                html:"<div class='media-btns baobei'><a href='javascript:;' btntype='btnBaobei' title='商品' unselectable='on'>商品</a></div>"
            },
            btnImg:{
                visible:true,
                exec:function (self) {
                    if (!self.ImgWrapDom || self.ImgWrapDom.length == 0) {
                        var html = '<div class="imgWrap"><form method="post" enctype="multipart/form-data" class="uploadImg clearfix" action="/editor/uploadPic" target="photo-frame" id="J_LocalImgForm"><input type="button" value="上传本地图片" class="bbl-btn upload-cover"><input type="file" class="upload-btn" id="J_LocalImgFormSubmit" name="filedata"><span class="fl gc pt5 pl5">支持GIF、JPG、PNG,大小不超过2M</span></form><div class="netImg clearfix"><p>插入网络图片：</p><input class="base-input" id="J_InsertNetImgInput" value="" placeholder="http://" autocomplete="off"/><input type="button" id="J_InsertNetImgSubmit" class="bbl-btn" value="确定"></div><div class="tipbox-up"><em>◆</em><span>◆</span></div><iframe style="width:0px;height:0px;padding:0px;" src="" frameborder="0" name="photo-frame"></iframe></div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.ImgUploadSubmitDom = $('#J_LocalImgFormSubmit');
                        self.ImgUploadFormDom = $('#J_LocalImgForm');
                        self.ImgUploadSubmitDom.change(function () {
                            var file = self.ImgUploadSubmitDom.val();
                            var reg_file = /\.(?:jpg|png|gif)$/i;
                            if (reg_file.test(file)) {
                                self.ImgUploadFormDom.submit();
                            } else {
                                self.ImgUploadSubmitDom.val("");
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show(self.ImgUploadSubmitDom, "只支持GIF、JPG、PNG,大小不超过2M");
                            }
                        });
                        self.uploadImgCallback = function (success, data) {
                            if (success) {
                                var media = {};
                                media.id = data;
                                media.pic = data;
                                media.name = "上传图片";
                             //   self.insertMedia(media, "img");
                                if (self.isIE678) {
                                    self.insertHTML("<img unselectable='on'class='img-upload'  src='" +data + "'/>");
                                } else {
                                    var imgDom = self.iframeDocument.createElement("img");
                                    imgDom.src = data;
                                    imgDom.setAttribute("class","img-upload")
                                    imgDom.setAttribute("unselectable", "on")
                                //    imgDom.setAttribute("title", $srcElement.attr("title"))
                               //     imgDom.setAttribute("alt", $srcElement.attr("title"))
                                    self.insertHTML(imgDom);
                                }
                                self.ImgUploadSubmitDom.val("");
                            } else {
                                self.ImgUploadSubmitDom.val("");
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show(self.ImgUploadSubmitDom, data);
                            }
                            self.curVisiableDom.hide();
                            self.curVisiableDom = null
                        }
                        self.insertNetImgInputDom = $('#J_InsertNetImgInput');
                        self.insertNetImgSubmitDom = $('#J_InsertNetImgSubmit');
                        self.insertNetImgSubmitDom.click(function () {
                            var url = $.trim(self.insertNetImgInputDom.val());
                            var reg_url = /^https?\:\/\//i;
                            if (url.length > 200) {
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show(self.insertNetImgSubmitDom, "请输入一个正确的图片网址");
                                return "image src is too long!";
                            }
                            if (reg_url.test(url)) {
                                var media = {};
                                media.id = url;
                                media.pic = url;
                                media.name = "网络图片";
                             //   self.insertMedia(media, "img");
                                if (self.isIE678) {
                                    self.insertHTML("<img unselectable='on'class='img-upload'  src='" + url + "'/>");
                                } else {
                                    var imgDom = self.iframeDocument.createElement("img");
                                    imgDom.src = url;
                                    imgDom.setAttribute("class","img-upload")
                                    imgDom.setAttribute("unselectable", "on")
                                    //    imgDom.setAttribute("title", $srcElement.attr("title"))
                                    //     imgDom.setAttribute("alt", $srcElement.attr("title"))
                                    self.insertHTML(imgDom);
                                }
                                self.insertNetImgInputDom.val("");
                                self.curVisiableDom.hide();
                                self.curVisiableDom = null
                            } else {
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show(self.insertNetImgSubmitDom, "请输入一个正确的图片网址(带http://)");
                            }
                        })
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.ImgWrapDom = $('#' + self.config.toolbarId + ' .imgWrap');
                        self.curVisiableDom = self.ImgWrapDom;
                    } else {
                        if (self.curVisiableDom == self.ImgWrapDom) {
                            self.ImgWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.ImgWrapDom.show();
                            self.curVisiableDom = self.ImgWrapDom;
                        }
                    }
                },
                html:"<div class='media-btns img'><a href='javascript:;' btntype='btnImg' title='图片' unselectable='on'>图片</a></div>"
            },
            btnVideo:{
                visible:true,
                exec:function (self) {
                    if (!self.VideoWrapDom || self.VideoWrapDom.length == 0) {
                        var html = '<div class="videoWrap sg-dialog"><div class="content"><p class="title">输入视频播放页网址：</p><form class="sg-form" name="shareGoods" action=""><div class="clearfix"><input class="base-input sg-input" id="J_InsertVideoInput" name="url" value="" placeholder="http://" autocomplete="off"><input type="button" id="J_InsertVideo" class="bbl-btn" value="确定"></div></form><div class="sg-source"><p>已支持网站：</p><div class="source-list clearfix"><a class="icon-youku" href="http://www.youku.com/" target="_blank">优酷网</a><a class="icon-tudou" href="http://www.tudou.com/" target="_blank">土豆网</a><a class="icon-sinavideo" href="http://video.sina.com.cn/" target="_blank">新浪视频</a></div></div><div class="tipbox-up"><em>◆</em><span>◆</span></div></div></div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.insertVideoSubmitDom = $('#J_InsertVideo');
                        self.VideoInputDom = $('#J_InsertVideoInput');
                        self.insertVideoSubmitDom.bind("click", function () {
                            var videoUrl = $.trim(self.VideoInputDom.val());
                            var reg_url = /^https?\:\/\//i;
                            var reg_youku = /^https?\:\/\/v\.youku\.com\//i;
                            var reg_sinavideo = /^https?\:\/\/[^\/]+sina\.com\.cn\//i;
                            var reg_tudou = /^https?\:\/\/[^\/]+tudou\.com\//i;
                            if (reg_url.test(videoUrl)) {
                                if (reg_youku.test(videoUrl) || reg_sinavideo.test(videoUrl) || reg_tudou.test(videoUrl)) {
                                    self.VideoInputDom.val("");
                                    $.ajax({
                                        url:"/editor/getVideo",
                                        type:"post",
                                        dataType:"json",
                                        data:{
                                            url:videoUrl
                                        },
                                        success:function (json) {
                                            switch (json.code) {
                                                case 100:
                                                {
                                                    self.curVisiableDom.hide();
                                                    self.curVisiableDom = null;
                                                    json.id = json.swf;
                                                    json.name = json.title;
                                                 //   self.insertMedia(json, "video");
                                                    alert("提示输入video")
                                                }
                                                    break;
                                                case 101:
                                                {
                                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                                    $.smeite.tip.show(self.btnBaobei, json.msg);
                                                    self.curVisiableDom.hide();
                                                    self.curVisiableDom = null;
                                                }
                                                    break;
                                            }
                                        }
                                    });
                                } else {
                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                    $.smeite.tip.show(self.insertVideoSubmitDom, "不支持该站视频");
                                }
                            } else {
                                $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.smeite.tip.show(self.insertVideoSubmitDom, "请输入一个正确的视频网页地址(带http://)");
                            }
                        });
                        self.VideoWrapDom = $('#' + self.config.toolbarId + ' .videoWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.VideoWrapDom;
                    } else {
                        if (self.curVisiableDom == self.VideoWrapDom) {
                            self.VideoWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.VideoWrapDom.show();
                            self.curVisiableDom = self.VideoWrapDom;
                        }
                    }
                },
                html:"<div class='media-btns video'><a href='javascript:;' btntype='btnVideo' title='视频' unselectable='on'>视频</a></div>"
            },
            btnSplit:{
                visible:true,
                html:"<span class='split'></span>"
            },
            //按钮按顺序加载
            btnsLoadOrder:['btnFontSize', 'btnFontBold', 'btnFontColo', 'btnSplit', 'btnFace', 'btnBaobei', 'btnImg','btnVideo']
        },
        isIE678:!+"\v1",
        iframe:null,
        iframeDocument:null,
        setConfig:function (conf) {
            return $.extend(true, this.config, conf || {});
        },
        init:function (conf) {
            var self = this;
            //更新配置
            this.setConfig(conf)
            //加载Editor
            this.insertEditor();
            this.setEditor();

            this.id = 0;
        },
        insertEditor:function () {
            var self = this;
            var html = "<div class='guang-editor-wrap'><div class='guang-editor'><div class='edit-btns' id='" + self.config.toolbarId + "'>";
            $.each(self.config.btnsLoadOrder, function () {
                var btn = self.config[this];
                if (btn.visible) {
                    html += btn.html;
                    if (btn.cssName) {
                        if (!self.selectionStyleFuns) {
                            self.selectionStyleFuns = {};
                        }
                        self.selectionStyleFuns[btn.cssName] = btn.selectionStyleFun;
                    }
                }
            }) ;
            html += "</div>"  ;
            html += "<div class='iframeWrap'><iframe frameborder='0' id='J_GuangEditorIframe'></iframe></div>"  ;
            html += "</div></div>" ;
            $("#" + self.config.textareaID).after(html).hide();
            this.iframe = $("#J_GuangEditorIframe")[0];
            this.iframeDocument = this.iframe.contentDocument || this.iframe.contentWindow.document;
        },
        setToolbar:function () {
            var self = this;
            $("body").bind("click", function () {
                if (self.curVisiableDom) {
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            })
            $('#' + self.config.toolbarId).bind("click", function () {
                var e = arguments[0] || window.event,
                    target = e.srcElement ? $(e.srcElement) : $(e.target),
                    btnType = target.attr("btntype");
                if (e.stopPropagation) {
                    e.stopPropagation();
                } else {
                    e.cancelBubble = true;
                }
                if (btnType) {
                    self.config[btnType].exec(self, target);
                } else {
                    //self.execCommand("","");
                }
            });
        },
        setEditor:function () {
            var self = this;
            //给按钮添加功能
            self.setToolbar();
            //填充iframe内容，主要功能是使用户在多行输入的时候，iframe自动增高
            self.iframeDocument.designMode = "on";
            self.iframeDocument.open();
            if (self.isIE678) {
                self.iframeDocument.write('<html><head><style type="text/css">html,body{height:100%;width:100%;margin:0;padding:0;border:0;overflow:auto;background:#fff;cursor:text;font-size:14px;word-wrap:break-word;}p{padding:0;margin:0;}*{line-height:160%;}body{font-family:Arial,Helvetica,Sans-Serif;font-size:14px;text-align:left;} p{margin:10px 0;} em{font-style:italic;} img{border:0;max-width:100%;cursor:default;}.img-goods,.img-upload { display: block;max-width: 200px; max-height: 250px; _width: 200px;background:  url(/assets/css/global/images/editor-img.gif) no-repeat bottom right transparent;}</style></head></html>');
            } else {
                self.iframeDocument.write('<html><head><style type="text/css">html,body{height:100%;width:100%;margin:0;padding:0;border:0;overflow:auto;background:#fff;cursor:text;font-size:14px;word-wrap:break-word;}p{padding:0;margin:0;}*{line-height:160%;}html{height:1px;overflow:visible;} body{overflow:hidden;font-family:Arial,Helvetica,Sans-Serif;font-size:14px;text-align:left;} p{margin:10px 0;} em{font-style:italic;} img{border:0;max-width:100%;}.img-goods,.img-upload { display: block;max-width: 200px; max-height: 250px; _width: 200px;background:  url(/assets/css/global/images/editor-img.gif) no-repeat bottom right transparent;}</style></head></html>');
            }
            self.iframeDocument.close();
            var textareaVal = $("#" + self.config.textareaID).val();
            if (textareaVal != "") {
              self.iframeDocument.body.innerHTML =textareaVal;
                self.iframe.contentWindow.focus();
                $(self.iframe).height($(self.iframeDocument).height());
            }
            //当用户使用鼠标在文本上操作的时候，获得该文本区域的样式，使工具栏样式联动
            $(self.iframeDocument).bind("mouseup click", function () {
                var e = arguments[0] || window.event,
                    curElm,
                    nodeName;
                $.smeite.dialog.isLogin();
                //时间涉及选中和点击，选中有可能只在某个节点内，那么会同时触发点击
                //判断是否选中文本
                if (e.type == "mouseup") {
                    var range = self.getRange();
                    if (self.isIE678) {
                        if (range.text.length != 0) {
                            curElm = self.selectionTextContainer = range.parentElement();
                        } else {
                            self.selectionTextContainer = null;
                        }
                    } else {
                        if (range.endContainer != range.startContainer) {
                            if (range.commonAncestorContainer.nodeType == 3) {
                                curElm = self.selectionTextContainer = range.commonAncestorContainer.parentNode;
                            } else {
                                curElm = self.selectionTextContainer = range.commonAncestorContainer;
                            }
                        } else {
                            self.selectionTextContainer = null;
                        }
                    }
                    if (self.selectionTextContainer) {
                        for (var i in self.selectionStyleFuns) {
                            var parents = $(self.selectionTextContainer).parents("font,b,span,p,div");
                            if (parents.length == 0) {
                                parents = null;
                            }
                            self.selectionStyleFuns[i](self, self.selectionTextContainer, parents);
                        }
                    }
                    //未选中文本
                } else if (self.selectionTextContainer == null) {
                    curElm = e.srcElement ? e.srcElement : e.target;
                    if (self.curVisiableDom) {
                        self.curVisiableDom.hide();
                        self.curVisiableDom = null;
                    }
                    for (var i in self.selectionStyleFuns) {
                        var parents = $(curElm).parents();
                        if (parents.length == 0) {
                            parents = null;
                        }
                        self.selectionStyleFuns[i](self, curElm, parents);
                    }
                }
            })
            $(self.iframeDocument).bind("keyup", function (event) {
                try {
                    var range = self.getRange();
                    var funs = self.selectionStyleFuns;
                    var length = self.selectionStyleFuns.length;
                    for (var i = 0; i < length; i++) {
                        funs[i](self, self.isIE678 ? range.parentElement() : range.endContainer.parentNode);
                    }
                } catch (e) {
                    alert(e)
                }
                $(self.iframe).height($(self.iframeDocument).height());
                //当工具栏被滚动到看不见的时候...
                if (!self.toolbarBindScrollEvent) {
                    $(window).bind("scroll", function () {
                        self.toolbarBindScrollEvent = true;
                        var docScrollTop = $(document).scrollTop();
                        if (!self.toolbarOffsetTop) {
                            self.toolbarOffsetTop = $('#' + self.config.toolbarId).offset().top;
                        }
                        if (self.toolbarOffsetTop <= docScrollTop) {
                            if ($.smeite.util.isIE6()) {
                                //to do
                            } else {
                                if (!self.toolbarPositionFixed) {
                                    self.toolbarPositionFixed = true;
                                    $('#' + self.config.toolbarId).css({
                                        position:"fixed",
                                        top:"38px",
                                        width:$('#' + self.config.toolbarId).width() + "px"
                                    })
                                }
                            }
                        } else {
                            if (self.toolbarPositionFixed) {
                                self.toolbarPositionFixed = false;
                                $('#' + self.config.toolbarId).css({
                                    position:"relative",
                                    top:"0"
                                })
                            }
                        }
                    });
                }
            });
            //IE下光标会丢失
            if ($.browser.msie) {
                var addEvent = function (el, type, fn) {
                    el['e' + type + fn] = fn;
                    el.attachEvent('on' + type, function () {
                        el['e' + type + fn]();
                    });
                }
                var bookmark;
                //记录IE的编辑光标
                addEvent(self.iframe, "beforedeactivate", function () { //在文档失去焦点之前
                    var range = self.iframeDocument.selection.createRange();
                    bookmark = range.getBookmark();
                });
                //恢复IE的编辑光标
                addEvent(self.iframe, "activate", function () {
                    if (bookmark) {
                        var range = self.iframeDocument.body.createTextRange();
                        range.moveToBookmark(bookmark);
                        range.select();
                        bookmark = null;
                    }
                });
            }
        },
        getRange:function () {
            var contentWindow = this.iframe.contentWindow;
            var selection = null;
            var range = null;
            if (this.isIE678) { // ie6,7,8 not ie9
                selection = contentWindow.document.selection;
                range = selection.createRange();
            } else { // 标准
                selection = contentWindow.getSelection();
                range = selection.getRangeAt(0);
            }
            return range;
        },
        //insertHTML 向编辑器插入html代码
        //@param html (String||Node @@如果是ie678则传字符串，如果是标准浏览器，则传node)
        insertHTML:function (html) {
            var contentWindow = this.iframe.contentWindow;
            contentWindow.focus();
            var range = this.getRange(0);
            var selection = null;
            if (this.isIE678) {
                range.pasteHTML(html);
            } else {
                range.insertNode(html);
                range.setEndAfter(html);
                range.setStartAfter(html);
                selection = contentWindow.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
            }
        },
        execCommand:function (cmd, val) {
            try {
                this.iframeDocument.execCommand(cmd, false, val);
                this.iframe.contentWindow.focus();
            } catch (e) {
            }
        },

        //richText2text 将"[b]文字[/b]"转成"文本"供回复的回复使用
        //@param html (String)
        richText2text:function (html) {
            if (html) {
                var val = html;
                val = val.replace(/\[[^\]]+\]/g, "");
                return val;
            } else {
                return "";
            }
        },
        //html2text 将"<b>文字</b>"转成"文本",为了判断编辑框是否有输入内容
        //@param html (String)
        html2text:function (html) {
            if (html) {
                var val = html;
                if (val.indexOf("<img") != -1) {
                    return "hasConent";
                } else {
                    val = val.replace(/\<[^\>]+\>/g, "");
                    val = val.replace(/\&nbsp\;/g, "");
                }
                return $.trim(val);
            } else {
                return "";
            }
        },
        //onlyDecodeFace 作用于回复的回复，该模块只让表情显示
        //@param html (String)
        onlyDecodeFace:function (html) {
            var data;
            if (!this.faceTagsData) {
                data = this.faceTagsData = {};
                for (var i = 0; i < FACEJSON.length; i++) {
                    data[FACEJSON[i].key] = '<img src="/assets/img/emotion/' + FACEJSON[i].val + '" unselectable="on" title="' + FACEJSON[i].key + '" alt="' + FACEJSON[i].key + '">';
                }
            } else {
                data = this.faceTagsData;
            }
            html = html.replace(/\[[^\[\]]+\]/gi, function (tag) {
                if (data[tag]) {
                    return data[tag];
                } else {
                    return "";
                }
            })
            return html;
        }
    }
    Editor.fn.init.prototype = Editor.fn;
    $.smeite.Editor = Editor;

//}
});