/**
 * Created by zuosanshao.
 * User: Administrator
 * Date: 12-10-22
 * Time: 下午8:58
 * Email:zuosanshao@qq.com
 * @contain: 前端基础功能插件，包含基础库、功能库（定位、浮层、校验、滚动、提示框） 用户登录、分享宝贝
 * @depends: jquery.tools.js,json2.js
 * Includes jquery.tools.js,json2.js
 * Since: 2012-11-11
 * ModifyTime : 2012-12-25
 * ModifyContent: 增加、删除、清理注释
 * ModifyTime : 2012-12-26 21:00
 * ModifyContent: 当创建主题时，判断用户是否登录
 * http://www.smeite.com/
 *
 */

define(function(require, exports) {
    var $ = require("$");
    require("json");
  var Overlay = require("overlay");
  var Mask = require("mask");
    var Cookie = require("cookie");
    var Dialog = require("dialog") ;

    $.smeite = $.smeite || {
        version: "v1.0.0"
    };
    /* 基础库*/
    $.extend($.smeite, {
        util: {
            //获取url参数名
            getUrlParam : function(paramName){
                var reg = new RegExp("(^|\\?|&)"+ paramName +"=([^&]*)(\\s|&|$)", "i");
                if (reg.test(location.href)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";
            },
            getUrlParam : function(url,paramName){
                var reg = new RegExp("(^|\\?|&)"+ paramName +"=([^&]*)(\\s|&|$)", "i");
                if (reg.test(url)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";
            },
            //判断是否ie6
            isIE6: function() {
                return navigator.userAgent.indexOf("MSIE 6.0") !== -1
            },
            //判断是否是苹果手持设备
            isIOS: function(){
                return /\((iPhone|iPad|iPod)/i.test(navigator.userAgent)
            },
            //去掉首尾空字符
            trim: function(str) {
                return str.replace(/(^\s*)|(\s*$)/g,"");
            },
            //去掉首空字符
            lTrim: function(str){
                return str.replace(/(^\s*)/g, "");
            },
            //去掉尾空字符
            rTrim: function(str){
                return str.replace(/(\s*$)/g, "");
            },
            //获取字符串中文长度
            getStrLength: function(str) {
                str = $.smeite.util.trim(str);
                var theLen = 0,
                    strLen = str.replace(/[^\x00-\xff]/g,"**").length;
                theLen = parseInt(strLen/2)==strLen/2 ? strLen/2 : parseInt(strLen/2)+0.5;
                return theLen;
            },
            //截取一定长度的中英文字符串并转全角
            substring4ChAndEn: function(str,maxLength){
                var strTmp = str.substring(0,maxLength*2);
                while($.smeite.util.getStrLength(strTmp)>maxLength){
                    strTmp = strTmp.substring(0,strTmp.length-1);
                }
                return strTmp;
            },
            //将<>"'&符号转换成全角
            htmlToTxt: function(str){
                var RexStr = /\<|\>|\"|\'|\&/g;
                str = str.replace(RexStr, function(MatchStr) {
                    switch (MatchStr) {
                        case "<":
                            return "＜";
                            break;
                        case ">":
                            return "＞";
                            break;
                        case "\"":
                            return "＼";
                            break;
                        case "'":
                            return "＇";
                            break;
                        case "&":
                            return "＆";
                            break;
                        default:
                            break;
                    }
                })
                return str;
            },
            //截取一定长度的字符串
            ellipse: function(str, len) {
                var boolLimit = $.smeite.util.getStrLength(str)*2 > len;
                if(str && boolLimit){
                    return str.replace(new RegExp("([\\s\\S]{"+len+"})[\\s\\S]*"),"$1…");
                }
                return str;
            },
            //校验是否为空
            isEmpty: function(v) {
                return $.smeite.util.trim(v)=="" ? false : true;
            },
            //校验邮箱是否合法
            isEmail: function(v) {
                return /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(v);
            },
            /*判断是否是url*/
            isUrl:function(v){ return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(v));
            },
            //校验昵称是否合法
            isNick: function(v){
                return /^[a-zA-Z\d\u4e00-\u9fa5_-]*$/.test(v);
            },
            nickMin: function(v){
                var l = $.smeite.util.getStrLength(v)*2;
                return l < 4 ? false : true;
            },
            nickMax: function(v){
                var l = $.smeite.util.getStrLength(v)*2;
                return l > 30 ? false : true;
            },
            //校验字符长度不超过某个长度
            tooShort: function(v,l) {
                return v.length<l ? false : true;
            },
            //校验是否包含网址
            noLink: function(v){
                var matchURL = v.match(/(http[s]?:\/\/)?[a-zA-Z0-9-]+(\.[a-zA-Z0-9]+)+/);
                return matchURL==null ? true : false;
            },
            //获取dom对象的当前位置
            getPosition: function(ele){
                var top = ele.offset().top,
                    left = ele.offset().left,
                    bottom = top + ele.outerHeight(),
                    right = left + ele.outerWidth(),
                    lmid = left + ele.outerWidth()/2,
                    vmid = top + ele.outerHeight()/2;
                var position = {
                    leftTop: function(){
                        return {x: left, y: top}
                    },
                    leftMid: function(){
                        return {x: left, y: vmid}
                    },
                    leftBottom: function(){
                        return {x: left, y: bottom}
                    },
                    topMid: function(){
                        return {x: lmid, y: top}
                    },
                    rightTop: function(){
                        return {x: right, y: top}
                    },
                    rightMid: function(){
                        return {x: right, y: vmid}
                    },
                    rightBottom: function(){
                        return {x: right, y: bottom}
                    },
                    MidBottom: function(){
                        return {x: lmid, y: bottom}
                    },
                    middle: function(){
                        return {x: lmid, y: vmid}
                    }
                };
                return position;
            },
            //获取根域名
            getDomain: function(url){
                var domain = "null";
                var regex = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
                var match = regex.exec(url);
                if (typeof match != "undefined" && null != match) {
                    domain = match[0];
                }
                return domain;
            },
            //校验合法网站
            validSite: function(url){
                var domain = $.smeite.util.getDomain(url);
                if(url.indexOf("smeite.com") != -1){
                    return (domain == "smeite.com" && url.indexOf("goods/") != -1) ? "smeite" : false;
                }else if (url.indexOf("tmall.com") != -1) {
                    var curBool=true, bool1, bool2, bool3, boo4;
                    bool3 = (domain == "detail.tmall.com" || domain == "item.tmall.com") && (url.indexOf("item.htm?") != -1);
                    bool4 = (domain == "detail.tmall.com" || domain == "item.tmall.com") && (url.indexOf("spu_detail.htm?") != -1);
                    switch(curBool){
                        case bool3 : {return "tmall3"} break;
                        case bool4 : {return "tmall4"} break;
                        default : {return false;} break;
                    }
                }else if (url.indexOf("taobao.com") != -1) {
                    var curBool=true, bool1, bool2, bool3;
                    bool1 = (domain == "item.taobao.com" || domain == "item.beta.taobao.com" || domain == "item.lp.taobao.com") && (url.indexOf("item.htm?") != -1);
                    switch(curBool){
                        case bool1 : {return "taobao"} break;
                        default : {return false;} break;
                    }
                } else {
                    return false;
                }
            },
            openWin: function(url){
                var top=190;
                var whichsns = url.substr(url.lastIndexOf("snsType=")+8,1);
                if(whichsns==4 || whichsns==5){
                    var left=document.body.clientWidth>820 ? (document.body.clientWidth-820)/2 : 0;
                    window.open(url, 'connect_window', 'height=700, width=820, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns==8){
                    var left=(document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=620, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns==9){
                    var left=document.body.clientWidth>900 ? (document.body.clientWidth-900)/2 : 0;
                    window.open(url, 'connect_window', 'height=550, width=900, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else{
                    var left=(document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=420, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }

            },
            moveEnd : function(obj){
                obj.focus();
                var len = obj.value.length;
                if (document.selection) {
                    var sel = obj.createTextRange();
                    sel.moveStart('character',len);
                    sel.collapse();
                    sel.select();
                } else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
                    obj.selectionStart = obj.selectionEnd = len;
                }
            },
            submitByEnter : function(e, clk) {
                e = e || window.event;
                var key = e ? (e.charCode || e.keyCode) : 0;
                if(key == 13) {
                    clk();
                }
            }
        }
    });
    /*功能库*/
    $.fn.extend({
        //返回顶部
        returntop: function(){
            if(!this[0]){
                return;
            }
            var backToTopEle = this.click( function() {
                $("html, body").animate({
                    scrollTop: 0
                }, 500);
                var topH = $(window).height()+80+"px";

                backToTopEle.data("isClick",true);
                backToTopEle.css("bottom",topH);
            });


            var showEle = function(){
                if(backToTopEle.data("isClick")){
                }else{
                    backToTopEle.css({"opacity":1,"bottom":"200px"});
                }
            };
            var timeDelay = null;
            var backToTopFun = function() {
                var docScrollTop = $(document).scrollTop();
                var winowHeight = $(window).height();
                (docScrollTop > 0)? showEle(): backToTopEle.css({"opacity":0,"bottom":"-200px"}).data("isClick",false);
                //IE6下的定位
                if ($.smeite.util.isIE6()) {
                    backToTopEle.hide();
                    clearTimeout(timeDelay);
                    timeDelay = setTimeout(function(){
                        backToTopEle.show();
                        clearTimeout(timeDelay);
                    },1000);
                    backToTopEle.css("top", docScrollTop + winowHeight - 125);

                }
            };
            $(window).bind("scroll", backToTopFun);
        },
        //等比例缩放图片
        resizeImage: function(width,height){
            this.each(function(){
                var obj = $(this)[0];
                var w = obj.width;
                var h = obj.height;
                if (w <= width && h <= height) {
                    return;
                } else if (w <= width && h > height) {
                    obj.width = w * height / h;
                    obj.height = height;
                } else if (w > width && h <= height) {
                    obj.width = width;
                    obj.height = h * width / w;
                } else {
                    obj.width = width;
                    obj.height = h * width / w;
                    var temp=h * width / w;
                    if(temp>height) {
                        obj.width = w * height / h;
                        obj.height = height;
                    }
                }
            });
        },
        //文本框高度自适应
        textareaAutoHeight: function(){
            var obj = this;
            var h = obj.outerHeight();
            var func = function(){
                h < 0 && (h = obj.outerHeight());
                if($.browser.mozilla || $.browser.safari){
                    obj.height(h);
                }
                var sh = obj[0].scrollHeight,
                    autoHeight = sh < h ? h: sh,
                    autoHeight = autoHeight < h * 1.5 ? h: sh;
                obj.height(autoHeight);
            }
            obj.bind("keyup input propertychange focus",func);
        },
        //按钮置为灰色
        disableBtn: function(str){
            var $btn = this;
            $btn[0].disabled = "disabled";
            $btn.removeClass(str).addClass("disabled");
        },
        //开启按钮
        enableBtn: function(str){
            var $btn = this;
            $btn[0].disabled = "";
            $btn.removeClass("disabled").addClass(str);
        },
        //下拉菜单
        dropDown: function(options){
            var settings = {
                event: "mouseover",
                classNm: ".dropdown",
                timer: null,
                fadeSpeed: 100,
                duration: 500,
                offsetX: 82,
                offsetY: 8,
                isLocation: false
            };
            if(options) {
                $.extend(settings, options);
            }

            var triggers = this,
                $dropDown = $(settings.classNm);
            triggers.each(function() {
                $this = $(this);
                $this.hover(function(){
                    clearTimeout(settings.timer);
                    $(".dropdown:not("+settings.classNm+")").hide();
                    if(settings.isLocation){
                        var position = $.smeite.util.getPosition($(this)).rightBottom();
                        $dropDown.css({
                            left: position.x - settings.offsetX + "px",
                            top: position.y + settings.offsetY + "px"
                        });
                    }
                    $dropDown.fadeIn(settings.fadeSpeed);
                },function(){
                    settings.timer = setTimeout(function(){
                        $dropDown.fadeOut(settings.fadeSpeed);
                    },settings.duration);
                });
                $dropDown.hover(function(){
                    clearTimeout(settings.timer);
                    $dropDown.show();
                },function(){
                    settings.timer = setTimeout(function(){
                        $dropDown.fadeOut(settings.fadeSpeed);
                    },settings.duration);
                });
            });
        }
    });


    /* 对话框：用户登陆 和 判断是否为新用户 */
    $.smeite.dialog = {
        /*判断是否登陆*/
        isLogin: function(){
            if(SMEITER.userId == ""){
                $.smeite.dialog.login();
                return false;
            }
            return true;
        },
        login: function(){
           if(!$("#loginDialog")[0]){
                var html = "";
                html += '<div id="loginDialog" class="g-dialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>登录</h3></div>';
                html += '<div class="bd clearfix"><div class="bd-l">';
                html += '<form id="J_loginDialogForm" action="/user/dialogEmailLogin" method="POST">';
                html += '<div class="error-row"><p class="error"></p></div>';
                html += '<div class="form-row"><label>Email：</label>';
                html += '<input type="text" class="base-input" name="email" id="email" value="" placeholder="" />';
                html += '</div>';
                html += '<div class="form-row"><label>密码：</label>';
                html += '<input type="password" class="base-input" name="password" id="password" value="" />';
                html += '</div>';
                html += '<div class="form-row"><label>&nbsp;</label>';
                html += '<input type="checkbox" class="check" name="remember" value="1" checked="checked" />';
                html += '<span>两周内自动登录</span>';
                html += '</div>';
                html += '<div class="form-row act-row clearfix"><label>&nbsp;</label>';
                html += '<input type="submit" class="bbl-btn login-submit" value="登录" />';
                html += '<a class="ml10 l30" href="/user/resetPasswd">忘记密码？</a></div>';
                html += '</form></div>';
                html += '<div class="bd-r">';
                html += '<p>你也可以使用这些帐号登录</p>';
                html += '<div class="snslogin mt15 clearfix"><ul class="fl mr20 outlogin-b">';
                html += '<li><a class="l-qq" href="/user/snsLogin?snsType=qzone&backType=asyn&i=0">QQ帐号登录</a></li>';
                html += '<li><a class="l-sina" href="/user/snsLogin?snsType=sina&backType=asyn&i=0">新浪微博登录</a></li>';
                html += '<li><a class="l-tao" href="/user/snsLogin?snsType=taobao&backType=asyn&i=0">淘宝帐号登录</a></li>';
                html += '</ul>';
                html += '</div>';
                html += '</div>';
                html += '<div class="clear"></div>';
                html += '<div class="noaccount">还没有帐号？<a href="/user/regist">免费注册一个</a></div>';
                html += '</div>';
                html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
                loginOverlay = new Overlay({
                   template:html,
                   width: 600,
                   zIndex: 9999,
                   align: {
                       selfXY: [ "50%", "50%" ],
                       baseXY: [ "50%", "50%" ]
                   }
               });
               loginOverlay.show();
               Mask.set({ backgroundColor:'#000', opacity:0.3 }).show();
            }else{
                loginOverlay = new Overlay({
                   template:'#loginDialog',
                   width: 600,
                   zIndex: 9999,
                   align: {
                       selfXY: [ "50%", "50%" ],
                       baseXY: [ "50%", "50%" ]
                   }
               });
               loginOverlay.show();
               Mask.show()
           }

            $("#loginDialog .close").bind("click",function(){
                loginOverlay.hide()
                Mask.hide()
                $("#J_loginDialogForm")[0].reset();
                $("#loginDialog").find(".error-row").hide();
            });

            $("#J_loginDialogForm").submit(function(){
                var  $this = $(this);
                $.ajax({
                    type: "POST",
                    url:$this.attr("action"),
                    data:$this.serializeArray(),
                    dataType:"json",
                    success:function(data){
                        if(data.code==100){
                            //    loginDialog.hide()
                            window.location.reload();
                        }else if(data.code==101){
                            $("#loginDialog").find(".error-row").fadeIn();
                            $("#loginDialog").find(".error").html(data.message);
                            $("#loginDialog input[name=password]").val("");
                        }
                    }
                });
                return false;
            });

            $(".snslogin a").unbind("click").click(function(){
                var snsurl = $(this).attr("href");
                $.smeite.util.openWin(snsurl);
                return false;
            });

        },

        /* 判断是否为新用户 */
        isNew:function(){
            if(SMEITER.status == "0" && SMEITER.userId !=""){
                if("close"!=Cookie.get("newGift")){
                    $.smeite.dialog.newGift();
                }

                return false;
            }
            return true;
        },
        /*新用户有礼*/
        newGift:function(){

                var html = "";
                html += '<div id="J_newGiftDialog" class="g-dialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>新人见面礼</h3></div>';
                html += '<div class="bd clearfix">';
                html += '<p class="fs14">';
                html +='亲，作为食美特的新人，来玩个“见面礼”小游戏吧，您将获得<strong class="rc">1-99</strong>个不等的集分宝，抽得的集分宝会马上打到您的支付宝账号哦！'
               html +='</p>';
               html +='<div class="gift"> ';
                         //  这里是抽奖区域
                html +='<div class="gift_area">';
                html +='<div class="gift_bg show"><img src="/assets/img/ui/new_gift.png"> </div>';
                html +='<div class="gift_roll hide"><img src="/assets/img/ui/new_gift_roll.gif"> </div>';
                html +='<div class="gift_left_value show" id="J_leftValue"> 0 </div>';
                html +='<div class="gift_right_value show" id="J_rightValue"> 0 </div>';
                html += '</div>';
                html += '</div>';

                html +='<div class="gift_handle" id="J_giftHandle"> ';
                html += '<span id="J_start">开始抽奖</span>';
                html += '</div>';
                html +='<div class="gift_handle_result" id="J_giftHandleResult"> ';

                html += '</div>';
                html +='<div class="gift_giveUp show" id="J_giftGiveUp"> ';
                html +='<a>我放弃见面礼</a>';
                html += '</div>';
                html += '</div>';
                html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
          //      $("body").append(html);
             var   newGiftOverlay = new Overlay({
                    template:html,
                    width: 600,
                    zIndex: 9999,
                    align: {
                        selfXY: [ "50%", "50%" ],
                        baseXY: [ "50%", "50%" ]
                    }
                });
                newGiftOverlay.show();
                Mask.set({ backgroundColor:'#000', opacity:0.3 }).show();

            $("#J_newGiftDialog .close").bind("click",function(){
                newGiftOverlay.hide()
                Mask.hide()

            });

                var prize=0;
                $("#J_start").click(function(){
                    $this = $(this);
                    $this.html("抽奖中……")
                    $(".show").hide()
                    $(".hide").show()

                    setTimeout(function() {
                            var leftValue = 1 + Math.floor(Math.random() * 2);
                            var rightValue = 1 + Math.floor(Math.random() * 3);
                             prize =leftValue*10+rightValue
                            $('#J_leftValue').text(leftValue);
                            $('#J_rightValue').text(rightValue);
                            $(".show").show()
                            $(".hide").hide()
                            $("#J_giftHandle").html('<span >抽奖结束</span>')
                            var msg ="恭喜您获得<strong class='rc'>"+prize+"</strong>个集分宝，去完善<span class='rc'  id='J_gotoPayment'>支付宝</span>信息吧，马上就会打入支付宝中哦"
                            $("#J_giftHandleResult").html(msg)
                        },
                        2500)

                });
                 $("#J_giftGiveUp").click(function(){
                     $.ajax({
                         url: "/user/giveUpGift",
                         type : "post",
                         success: function(data){
                             if(data.code=="100"){
                                 Cookie.set("newGift",'close')
                           //      window.location.reload();
                             }
                         }
                     });
                     newGiftOverlay.hide()
                     Mask.hide()
                 })

                $(document).on("click","#J_gotoPayment",function(){
                    $.ajax({
                        url: "/user/giveGift",
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({num:prize}),
                        success: function(data){
                            if(data.code=="100"){
                                Cookie.set("newGift",'close')
                                window.location="/user/account/payment";
                            }
                        }
                    });
                })




        }

    }

    /*
     * 字数统计限制
     * $.smeite.wordCount.init($wordCon,$wordNum,10)
     */
    $.smeite.wordCount = {
        conf : {
            okClk : function(){},
            errorClk : function(){}
        },
        init : function($wordCon, $wordNum, limitNum, speed) {
            //判断是否存在评论框，不存在则返回
            var thelimit = limitNum;
            var charDelSpeed = speed || 15;
            var toggleCharDel = speed != -1;
            var toggleTrim = true;
            var that = $wordCon[0];
            var getLen = function(){
                var wordConVal = $.trim($wordCon.val());
                return $.smeite.util.getStrLength(wordConVal);
            }
            updateCounter();
            function updateCounter(){
                $wordNum.text(thelimit - parseInt(getLen()));
            };

            $wordCon.bind("keypress", function(e) {
                if (getLen() >= thelimit && e.charCode != '0') e.preventDefault()
            });
            $wordCon.bind("keyup", function(e) {
                updateCounter();
                if (getLen() >= thelimit && toggleTrim) {
                    if (toggleCharDel) {
                        that.value = that.value.substr(0, thelimit * 2 + 100);
                        var init = setInterval(function() {
                                if (getLen() <= thelimit) {
                                    init = clearInterval(init);
                                    updateCounter()
                                } else {
                                    that.value = that.value.substring(0, that.value.length - 1);
                                    $.smeite.wordCount.conf.errorClk(that.value.length);
                                    //$wordNum.text('trimming...  ' + (thelimit - that.value.length));
                                };
                            },
                            charDelSpeed);
                    } else {
                        $wordCon[0].value = that.value.substr(0, thelimit);
                    }
                }
            }).focus(function() {
                    updateCounter();
                });
        }
    }

    /*
     * 需功能性操作的成功提示 例如删除主题
     */
    $.smeite.tipForOper = {
        conf : {
            html : ""
        },
        init: function(){
            if(!$("#J_tipForOperate")[0]){
                var html = "";
                html += '<div id="J_tipForOperate" class="tip-for-oper">';
                html += '<div class="dialog-content">';
                html += '<div class="bd clearfix">';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                $("body").append(html);
                var tipForOper = new Dialog({
                    classPrefix: "g-dialog",
                    effect: 'fade',
                    content: html
                }).show();
            }else{
                var tipForOper = new Dialog({
                    classPrefix: "g-dialog",
                    effect: 'fade',
                    content: '#J_tipForOperate'
                }).show();
            }
            $("#J_tipForOperate").find(".bd").html($.smeite.tipForOper.conf.html);

        }
    }

    /*
     * 提示框
     */
    $.smeite.tip = {
        conf: {
            timer: null,
            timerLength: 3000,
            tipClass : ""
        },
        show: function(o,text){
            clearTimeout($.smeite.tip.conf.timer);
            var position = $.smeite.util.getPosition(o).topMid();
            if(!$(".tipbox")[0]){
                $("body").append('<div class="tipbox"></div>');
            }
            $(".tipbox").attr("class",'tipbox ' + $.smeite.tip.conf.tipClass)
            $(".tipbox").html(text);
            var W = $(".tipbox").outerWidth(),
                H = $(".tipbox").outerHeight();
            $(".tipbox").css({
                left: position.x - W/2 + "px",
                top: position.y - H - 10 + "px"
            }).fadeIn();
            $.smeite.tip.conf.timer = setTimeout(function(){
                $(".tipbox").fadeOut();
            },$.smeite.tip.conf.timerLength);
        }
    }



    /* 用户分享宝贝 */
      /*用户生成的内容*/
    $.smeite.ugc = {
        pubJson: {

        },
        getCmt: function(str){
            var cmtV = $.smeite.util.trim($(str+" textarea[name=proComment]").val());
            if($.smeite.util.getStrLength(cmtV)>245){
                $(str+" .goods-act").find(".errc").show().html("评论数不能超过245字");
                return false;
            }else if(cmtV==""){
                return "none";
            }else{
                return cmtV;
            }
        },
        //组装标签为json串以及校验
        getTags: function(str){
            var tagsV =  $.smeite.util.trim($(str+" input[name=tags]").val()),
                tagsVArr = tagsV.replace(/&/g,"＆").replace(/\//g,"／").replace(/#/g,"＃").replace(/\，|\s+/g,",").split(","),
                tagsArr = [];
            for(var i=0,len=tagsVArr.length;i<len;i++){
                if(tagsVArr[i]!=""){
              //      var json = {"tagKeyword":tagsVArr[i]};
                    var json =tagsVArr[i]
                    tagsArr.push(json);
                }
            }
            if($.inArray("精品", tagsVArr)!=-1){
                $(str+" .goods-act").find(".errc").show().html("标签中不能包含“精品”");
                return false;
            }else if($.smeite.util.getStrLength(tagsV)>200){
                $(str+" .goods-act").find(".errc").show().html("标签不能超过200字");
                return false;
            }else{
                return tagsArr;
            }
        },
        //分享宝贝成功弹出层
        pubSuccess: function(){
            if(!$("#J_pubSuccessDialog")[0]){
                var html = "";
                html += '<div id="J_pubSuccessDialog">';
                html += '<div class="dialog-content">';
                html += '<div class="bd clearfix">';
                html += '<p class="success-text"><span class="correct">宝贝发布成功！</span></p>';
                html += '<p class="clearfix"><a class="bbl-btn goCheck" href="/user/baobei">前往查看宝贝</a>';
                html += '<a class="bgr-btn closeD ml10" href="javascript:;">关闭</a></p>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                $("body").append(html);
                var pubSuccessDialog = new Dialog({
                    classPrefix: "g-dialog",
                    effect: 'fade',
                    content: html
                }).show();
            }else{
                var  pubSuccessDialog = new Dialog({
                    classPrefix: "g-dialog",
                    effect: 'fade',
                    content: '#J_pubSuccessDialog'
                }).show();
            }


        },
        //生成同步分享按钮

        //宝贝已存在
        goodsExist: function(jsonObj,_callback){
            $.smeite.ugc.pubJson = {
                "id": jsonObj.id,
                "numIid": jsonObj.numIid,
                "nick": jsonObj.nick,
                "name": jsonObj.name,
                "proComment": "",
                "price": jsonObj.price,
                "pic": jsonObj.pic,
                "itemPics":[],
                "clickUrl":jsonObj.clickUrl,
                "tags": null,
                "location":jsonObj.location
            };
            //判断商品图片
            var mainPic = jsonObj.pic+"_80x80.jpg"
   //         if(!$("#J_goodsExistDialog")[0]){
                var html = "";
                html += '<div id="J_goodsExistDialog" class=" ugc-dialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>食美特上已经有这个宝贝啦</h3></div>';
                html += '<div class="bd clearfix">';
                html += '<form id="J_goodsExistForm" action="/ugc/api/updateProduct" method="POST">';
                html += '<div class="clearfix">';
                html += '<div class="goods-avatar">';
                html += '<a href="/goods/'+ jsonObj.id + '" target="_blank" title="' + jsonObj.name + '"><img src="' + mainPic + '" alt="' + jsonObj.name + '" /></a>';
                html += '</div>';
                html += '<div class="goods-info">';
                html += '<p class="goodsNm"><a href="/goods/' + jsonObj.id + '" target="_blank" title="' + jsonObj.name + '">' + jsonObj.name + '</a></p>';
                html += '<p class="pb5">评论一下：</p>';
                html += '<p><textarea class="base-txa" name="proComment" placeholder="喜欢它什么呢？"></textarea></p>';
                html += '<p class="pt10 pb5">宝贝标签：</p>';
                html += '<p><input type="text" rel="tagsInput" class="base-input" name="tags" value="" /></p>';
                html += '<p class="pt5 gc">多个标签用空格、中文或英文逗号隔开</p>';
                html += '</div>';
                html += '</div>';
                html += '<div class="goods-act">';
                html += '<div class="clearfix"><a class="bbl-btn" id="J_goodsSave" href="javascript:;">确定</a>';
               /* html += '<label class="fl mt15 ml15 gc6"><input type="checkbox" name="tomyfav" /> 加入我喜欢的宝贝</label>';*/
              /*  html += '<div class="sns-sync"><span class="gc">同步分享：</span><ul></ul><span><a href="/account/sns" target="_blank">设置</a></span></div></div>';*/
                html += '<div class="errc mt10"></div>';
                html += '</div>';
                html += '</form>';
                html += '</div>';
                html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
            //    $("body").append(html);
          var  goodsExistOverlay = new Overlay({
                template:'#J_goodsExistDialog',
                width: 600,
                zIndex: 9999,
                align: {
                    selfXY: [ "50%", "50%" ],
                    baseXY: [ "50%", "50%" ]
                }
            });
            goodsExistOverlay.show();
            Mask.show() ;
            $("#J_goodsExistDialog .close").bind("click",function(){
                goodsExistOverlay.hide()
                Mask.hide()

            });
                $("#J_goodsSave").unbind().bind("click",function(){
                    $this = $(this);
                    if($this.hasClass("disabled")){
                        return false;
                    }
                    var comment = $.smeite.ugc.getCmt("#J_goodsExistDialog");
                    if(comment=="none"){
                        comment = "";
                    }else if(!comment){
                        return false;
                    }
                    var tagJson = $.smeite.ugc.getTags("#J_goodsExistDialog");
                    if(!tagJson){
                        return false;
                    }
                    $.smeite.ugc.pubJson.proComment = comment;
                    $.smeite.ugc.pubJson.tags = tagJson;

                    $.ajax({
                        url: $("#J_goodsExistForm").attr("action"),
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify($.smeite.ugc.pubJson),
                        beforeSend: function(){
                            $this.disableBtn("bbl-btn");
                            $(".goods-act").find(".errc").show().html('<p class="ajaxvali pl20">保存中...</p>');
                        },
                        success: function(data){
                            if(data.code=="100"){
                                goodsExistDialog.hide()
                        //        $("#J_goodsExistDialog").empty().remove();
                                if(window.location.href.indexOf("/share")!=-1){
                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-ok";
                                    $.smeite.tip.show($this,"宝贝发布成功！");
                                    window.location.href = window.location.href;
                                }else{
                                    if(_callback){
                                        _callback($.smeite.ugc.pubJson.id,$.smeite.ugc.pubJson.pic);
                                    }else{
                                        $.smeite.ugc.pubSuccess();
                                    }
                                }
                            }else if(data.code=="101"){
                                $("#J_goodsExistDialog").find(".errc").show();
                                $("#J_goodsExistDialog").find(".errc").html("出错了，请重试…");
                                $this.enableBtn("bbl-btn");
                            }else if(data.code=="102"){
                                $(".text-tip").html('<span class="errc">输入的标签或评论过长</span>').show();
                                $this.enableBtn("bbl-btn");
                            }
                        }
                    });
                    return false;
                });
                $("#J_goodsExistForm").submit(function(){
                    return false;
                });


        },
        goodspub: function(jsonObj,_callback){
            $.smeite.ugc.pubJson = {
                "id": jsonObj.id,
                "numIid": jsonObj.numIid,
                "nick": jsonObj.nick,
                "name": jsonObj.name,
                "proComment": "",
                "price": jsonObj.price,
                "pic": jsonObj.pic,
                "itemPics":[],
                "clickUrl":jsonObj.clickUrl,
                "tags": null,
                "location":jsonObj.location
            };

       //     if(!$("#J_goodsPubDialog")[0]){
                var html = "";
                html += '<div id="J_goodsPubDialog" class=" ugc-dialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>嗯~ 就是它吧</h3></div>';
                html += '<div class="bd clearfix">';
                html += '<form id="J_goodsPubForm" action="/ugc/api/saveProduct" method="POST">';
                html += '<div class="form-row"><label>宝贝名称：</label>';
                html += '<span class="goodsNm">' + jsonObj.name + '</span>';
                html += '</div>';
                html += '<div class="form-row"><label>介绍一下：</label>';
                html += '<textarea class="base-txa" name="proComment" placeholder="喜欢它什么呢？"></textarea>';
                html += '</div>';
                html += '<div class="form-row"><label>宝贝标签：</label>';
                html += '<div class="inlineblock"><input type="text" rel="tagsInput" class="base-input" name="tags" value="" />';
                html += '<p class="pt5 gc">多个标签用空格、中文或英文逗号隔开</p></div>';
                html += '</div>';
                html += '<div class="form-row clearfix"><label>宝贝图片：</label>';
                html += '<div class="goods-gallery">';
                html += '<div class="gallery-bd">';
                html += '<div class="items">';
                html += '</div>';
                html += '</div>';
                html += '<div class="gallery-ft clearfix">';
                html += '<span class="status">已选 <em>0</em> 张</span><span class="errc"></span>';
               html += '<div class="gallery-pagin">';
              //   html += '<a href="javascript:;" class="sgr-btn prev">上页</a>';
              //  html += '<span class="num-box"><em class="curP">1</em><em class="totalP"></em></span>';
             //  html += '<a href="javascript:;" class="sgr-btn next">下页</a>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
                html += '<div class="goods-act">';
                html += '<div class="clearfix"><a class="bbl-btn" id="J_goodsPub" href="javascript:;">发布</a>';
                html += '<div class="errc mt10"></div>';
                html += '</div>';
                html += '</form>';
                html += '</div>';
        //        html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
             //   $("body").append(html);
            var  goodsPubOverlay = new Overlay({
                template:'#J_goodsPubDialog',
                width: 600,
                zIndex: 9999,
                align: {
                    selfXY: [ "50%", "50%" ],
                    baseXY: [ "50%", "50%" ]
                }
        });
            goodsPubOverlay.show();
            Mask.show() ;
            $("#J_goodsPubDialog .close").bind("click",function(){
                goodsPubOverlay.hide()
                Mask.hide()

            });
                //宝贝图片
                var photoArr = jsonObj.itemPics,
                    photoHtml = "";
                if(photoArr.length>0){
                    photoHtml += '<ul>';
                        for(var i=0,length=photoArr.length;i<length;i++){
                            photoHtml += '<li><a href="javascript:;"><img src="' + photoArr[i] + '" alt="" /></a><i></i></li>';
                        }
                    photoHtml += '</ul>';
                    $("#J_goodsPubDialog .items").append(photoHtml);

                }
                //图片选择操作
            $("#J_goodsPubDialog li a, #J_goodsPubDialog li i").off("click").on("click",function(e){
                e.preventDefault();
                $("#J_goodsPubDialog .gallery-ft").find(".status").show();
                $("#J_goodsPubDialog .gallery-ft").find(".errc").hide();
                var selectedFlag = $(this).parent("li").hasClass("selected");
                if(selectedFlag){
                    $(this).parent("li").removeClass("selected");
                }else{
                    $(this).parent("li").addClass("selected");
                }
                $("#J_goodsPubDialog .status em").text($("#J_goodsPubDialog li.selected").length);
            })

                //宝贝发布
                $("#J_goodsPub").unbind().bind("click",function(){

                    $this = $(this);
                    if($this.hasClass("disabled")){
                        return false;
                    }
                    var selectedArr = [];
                    $("#J_goodsPubDialog li.selected").each(function(){
                        selectedArr.push($(this).find("img").attr("src"));
                    });
                    var comment = $.smeite.ugc.getCmt("#J_goodsPubDialog");
                    if(comment=="none"){
                        comment = "";
                    }else if(!comment){
                        return false;
                    }
                    var tagJson = $.smeite.ugc.getTags("#J_goodsPubDialog");
                    if(!tagJson){
                        return false;
                    }
                    $.smeite.ugc.pubJson.proComment = comment;
                    $.smeite.ugc.pubJson.tags = tagJson;
                    $.smeite.ugc.pubJson.itemPics = selectedArr;
                   /* $.smeite.ugc.pubJson.favor = $("#J_goodsPubForm input[name=tomyfav]")[0].checked?"true":"false";*/
                    if(selectedArr.length==0){
                        $("#J_goodsPubDialog .gallery-ft").find(".status").hide();
                        $("#J_goodsPubDialog .gallery-ft").find(".errc").show();
                        $("#J_goodsPubDialog .gallery-ft").find(".errc").html("至少要选一张哦");
                        return false;
                    }

                    $.ajax({
                        url: $("#J_goodsPubForm").attr("action"),
                        type : "POST",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify($.smeite.ugc.pubJson),
                        beforeSend: function(){
                            $this.disableBtn("bbl-btn");
                            $(".goods-act").find(".errc").show().html('<p class="ajaxvali pl20">发布中...</p>');
                        },
                        success: function(data){
                            if(data.code=="100"){
                                goodsPubDialog.hide()
                                if(window.location.href.indexOf("/share")!=-1){
                                    $.smeite.tip.conf.tipClass = "tipmodal tipmodal-ok";
                                    $.smeite.tip.show($this,"宝贝发布成功！");
                                    window.location.href = window.location.href;
                                }else{
                                    if(_callback){
                                        _callback(data.productId,$.smeite.ugc.pubJson.itemPics[0]);
                                    }else{
                                        $.smeite.ugc.pubSuccess();
                                    }
                                }
                            }else if(data.code=="101"){
                                $("#J_goodsPubDialog .goods-act").find(".errc").show();
                                $("#J_goodsPubDialog .goods-act").find(".errc").html("出错了，请重试…");
                                $this.enableBtn("bbl-btn");
                            }else if(data.code=="102"){
                                $("#J_goodsPubDialog .goods-act").find(".errc").html("输入的标签或评论过长").show();
                                $this.enableBtn("bbl-btn");
                            }else if(data.code=="112"){
                                $("#J_goodsPubDialog .goods-act").find(".errc").html("请认真填写商品评论").show();
                                $this.enableBtn("bbl-btn");
                            }
                        }
                    });
                    return false;
                });
                $("#J_goodsPubForm").submit(function(){
                    return false;
                });


        }
    }
   /*用户喜欢操作*/
    $.smeite.favor = {
        //喜欢第一次提交
        loveBaobeiCallback : function(o){},
        //喜欢重复提交
        repeatLoveBaobeiClk : function(o){},
        //提交按钮操作
        loveBaobeiSubmit:function(o, productId){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
            var ajaxUrl = "/goods/favor";
            //   alert(productId)
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"id": productId }),
                success: function(data){
                    switch(data.code){
                        case "100" :
                            $.smeite.favor.loveBaobeiCallback(o);
                            break;
                        case "101":
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o, data.message);
                            //	$.smeite.favor.loveBaobeiCallback(o);
                            break;
                        case "103" : //喜欢、值得、不值得 重复提交后数据不做操作
                            $.smeite.favor.repeatLoveBaobeiClk(o);
                            break;
                        case 666 : //中奖了！
                          //  $.smeite.favor.awardClk(o);
                            break;
                    }
                }
            });
        },

        //喜欢帖子成功
        loveTopicCallback : function(o, commentType, desirableType){},
        //喜欢帖子重复
        repeatLoveTopicClk : function(o, commentType, desirableType){},
        //喜欢帖子操作
        loveTopicSubmit : function(o,topicId){
            var ajaxUrl = "/forum/addFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"topicId": topicId }),
                success: function(data){
                    //console.log(data.code)
                    switch(data.code){
                        case "100":
                            //console.log("no-repeate")
                            $.smeite.favor.loveTopicCallback(o,data);
                            break;
                        case "101" ://错误
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o,data.msg);
                            break;
                        case "103" : //喜欢重复
                            //console.log("repeate")
                            $.smeite.favor.repeatLoveTopicClk(o,data);
                            break;
                        case "200" : //未登录
                            $.smeite.dialog.login();
                            break;
                    }
                }
            });
        },
        // 取消喜欢帖子
        removeTopicCallback:function(o,topicId){
            var ajaxUrl = "/forum/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"topicId": topicId }),
                success: function(data){
                   if(data.code =="100"){
                            var html ="<a rel='followTopic'class='follow-btn' href='javascript:;' data-topicid='"+ topicId +"'>+ 关注</a>"
                            o.replaceWith(html)
                    }
                }
            });
        },
        //喜欢主题成功
        loveThemeCallback : function(o, commentType, desirableType){},
        //喜欢主题重复
        repeatLoveThemeClk : function(o, commentType, desirableType){},
        //喜欢主题操作
        loveThemeSubmit : function(o,themeId){
            var ajaxUrl = "/theme/addFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"themeId": themeId }),
                success: function(data){
                    //console.log(data.code)
                    switch(data.code){
                        case "100":
                            //console.log("no-repeate")
                            $.smeite.favor.loveThemeCallback(o,data);
                            break;
                        case "101" ://错误
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o,data.msg);
                            break;
                        case "103" : //喜欢重复
                            //console.log("repeate")
                            $.smeite.favor.repeatLoveThemeClk(o,data);
                            break;
                        case "200" : //未登录
                            $.smeite.dialog.login();
                            break;
                    }
                }
            });
        },
        /*取消喜欢的主题*/
        removeThemeCallback : function(o,themeId){
            var ajaxUrl = "/theme/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"themeId": themeId }),
                success: function(data){
                    if(data.code=="100"){
                        var html ="<a rel='themeFollow'class='follow-btn' href='javascript:;' data-themeid='"+ themeId +"'>+ 关注</a>"
                        o.replaceWith(html)
                    }
                }
            });
        },
        //喜欢小镇成功
        loveSiteCallback : function(o, commentType, desirableType){},
        //喜欢小镇重复
        repeatLoveSiteClk : function(o, commentType, desirableType){},
        //喜欢小镇操作
        loveSiteSubmit : function(o,sid){
            var ajaxUrl = "/site/addFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"sid": sid }),
                success: function(data){

                    switch(data.code){
                        case "100":
                            //console.log("no-repeate")

                            $.smeite.favor.loveSiteCallback(o,data);
                            break;
                        case "101" ://错误
                            $.smeite.tip.conf.tipClass = "tipmodal tipmodal-error";
                            $.smeite.tip.show(o,data.msg);
                            break;
                        case "103" : //喜欢重复
                            //console.log("repeate")
                            $.smeite.favor.repeatLoveSiteClk(o,data);
                            break;
                        case "200" : //未登录
                            $.smeite.dialog.login();
                            break;
                    }
                }
            });
        },
        /*取消喜欢的小镇*/
        removeSiteCallback : function(o,sid){
            var ajaxUrl = "/site/removeFollow";
            $.ajax({
                url:  ajaxUrl,
                type : "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({"sid": sid }),
                success: function(data){
                    if(data.code=="100"){
                        var html ="<a rel='siteFollow'class='follow-btn' href='javascript:;' data-sid='"+ sid +"'>+ 关注</a>"
                        o.replaceWith(html)
                    }
                }
            });
        }
    };


    /* 分享按钮 */
    $.fn.shareBtn = function(){
        if(!$(".share-dropdown")[0]){
            var html = '';
            html += '<ul class="share-link share-dropdown">';
            html += '<li><a class="s-sina" href="javascript:;">分享到新浪微博</a></li>';
            html += '<li><a class="s-qzone" href="javascript:;">分享到QQ空间</a></li>';
            html += '<li><a class="s-tencent" href="javascript:;">分享到腾讯微博</a></li>';
            html += '<li><a class="s-douban" href="javascript:;">分享到豆瓣</a></li>';
            html += '<li><a class="s-renren" href="javascript:;">分享到人人网</a></li>';
            html += '<li><a class="s-163" href="javascript:;">分享到网易微博</a></li>';
            html += '</ul>';
            $("body").append(html);
        }
        return this.each(function() {
            var $this = $(this);
            $this.bind("mouseover",function(){
                $(".share-dropdown a").unbind("click").click(function(){
                    var type = $(this).attr("class"),
                        shareTxt = encodeURIComponent($this.data("sharetxt")),
                        shareLink = $this.data("sharelink"),
                        sharePic = encodeURIComponent($this.data("sharepic"));
                    if(shareLink.indexOf("http://smeite.com")==-1){
                        shareLink = encodeURIComponent("http://smeite.com" + shareLink);
                    }
                    switch(type){
                        case "s-sina":
                            window.open('http://service.t.sina.com.cn/share/share.php?appkey=1464940004&title='+shareTxt+'&pic='+sharePic+'&url='+shareLink);
                            break;
                        case "s-tencent":
                            window.open('http://v.t.qq.com/share/share.php?appkey=100344510&url='+shareLink+'&title='+shareTxt+'&pic='+sharePic+'&site='+shareLink);
                            break;
                        case "s-douban":
                            window.open('http://www.douban.com/recommend/?url='+shareLink+'&title='+shareTxt);
                            break;
                        case "s-qzone":
                            window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+shareLink);
                            break;
                        case "s-renren":
                            window.open('http://share.renren.com/share/buttonshare.do?link='+shareLink+'&title='+shareTxt);
                            break;
                        case "s-163":
                            window.open('http://t.163.com/article/user/checkLogin.do?link='+shareLink+'&source=&info='+shareTxt+'&images='+sharePic);
                    }
                });
            });
            $this.dropDown({
                classNm: ".share-dropdown",
                offsetX: 122,
                offsetY: 0,
                isLocation: true
            });
        });
    };

    $.fn.shareToThird=function(){
        var $this = $(".share-link");
      var  shareTxt = encodeURIComponent($this.data("sharetxt")),
            shareLink = $this.data("sharelink"),
            sharePic = encodeURIComponent($this.data("sharepic"));
        if(shareLink.indexOf("http://smeite.com")==-1){
            shareLink = encodeURIComponent("http://smeite.com" + shareLink);
        }
        $(".share-link a").click(function(){
            var type = $(this).data("type");
            switch(type){
                case "s-sina":
                    window.open('http://service.t.sina.com.cn/share/share.php?appkey=1464940004&title='+shareTxt+'&pic='+sharePic+'&url='+shareLink);
                    break;
                case "s-tencent":
                    window.open('http://v.t.qq.com/share/share.php?appkey=100344510&url='+shareLink+'&title='+shareTxt+'&pic='+sharePic+'&site='+shareLink);
                    break;
                case "s-douban":
                    window.open('http://www.douban.com/recommend/?url='+shareLink+'&title='+shareTxt);
                    break;
                case "s-qzone":
                    window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+shareLink);
                    break;
                case "s-renren":
                    window.open('http://share.renren.com/share/buttonshare.do?link='+shareLink+'&title='+shareTxt);
                    break;
                case "s-163":
                    window.open('http://t.163.com/article/user/checkLogin.do?link='+shareLink+'&source=&info='+shareTxt+'&images='+sharePic);
            }
        })
    }



    //广告位点击数
    $.fn.countAdNum=function(obj){
        var $this=$(obj);
        var data={
            id: $this.attr("data")
        }
        $.ajax({
            url:"/advert/clickAdv",
            type : "POST",
            contentType:"application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(data),
            success:function(data){

            }
        });
    }


    /*用户的签到功能*/
    $.smeite.checkIn={
          checkInState:{
           shiDou:0,
           checkInDays:0
          } ,
        changeCheckInIcon :function(status){
            if(status){
                $("a[rel=checkIn]").addClass("checked").text("已签");
            }else{
                $("a[rel=checkIn]").removeClass("checked").text("签到");
            }
        },
        checkInIntro:function(o,data){
            $("#checkin_intro").unbind().remove();
            var checkInDays = (data.checkInDays+'').length==1?('0'+data.checkInDays):data.checkInDays;
            var HTML = ""
                +'<div id="checkin_intro">'
                +'连签：<b class="checkin_days">'+checkInDays+'</b>&nbsp;天<br/>'
                +'集分宝：<b id="jifen">'+data.userScore+'</b>&nbsp;分<br/>'
                +'<p>'
                +'签到：送集分宝，每次1-20个集分宝，随机赠送<br/>'
                +'连签7天：额外送15个<br/>'
                +'连签15天：额外送30个<br/>'
                +'连签30天：额外送60个<br/>'
                +'连签60天：额外送100个<br/>'
                +'连签100天：额外送200个<br/>'
                +'<a href="/user/account/invite" target="_blank">邀请可获更多集分宝</a>'
                +'</p>'
                +'</div>';
            o.after(HTML);

        },

        /* 判断用户是否已经签到*/
         judgeCheckInState:function(){
                 if(SMEITER.userId ==""){
                     return "Not login";
                 }
                 $.ajax({
                     url:"/checkInState",
                     type : "post",
                     dataType: "json",
                     success: function(data){
                         if(data.code=="100"){
                             $.smeite.checkIn.changeCheckInIcon(true);
                             $.smeite.checkIn.checkInState.checkInDays=data.checkInDays
                         }else if(data.code=="104"){
                             //未签到
                             //$.smeite.tip.conf.tipClass = "tipmodal tipmodal-error3";
                             //$.smeite.tip.show($this,">_<积分获取失败");
                         }else if(data.code=="404"){
                             //未登录
                             $.smeite.checkIn.changeCheckInIcon(false);
                         }
                     }
                 });

         },
        /* 签到过程 */
        checkInProcess:function(){

                var html = "";
                html += '<div id="J_checkInDialog">';
                html += '<div class="dialog-content">';
                html += '<div class="hd"><h3>亲，签到送集分宝哦</h3></div>';
                html += '<div class="bd clearfix">';
                html += '<p class="fs14" id="J_checkInMsg">';
                html +='亲，您将获得<strong class="rc">1-20</strong>个不等的<a href="/jifenbao">集分宝</a>,连签7天：额外送15个,连签30天：额外送60个,连签30天：额外送60个……';
                html +='</p>';
                html +='<div class="checkIn" id="J_checkIn"> ';
                //  这里是抽奖区域
                html +='<div class="checkIn_area">';
                html +='<div class="checkIn_bg hide"><img src="/assets/img/ui/new_gift.png"> </div>';
                html +='<div class="checkIn_roll show"><img src="/assets/img/ui/new_gift_roll.gif"> </div>';
                html +='<div class="checkIn_left_value hide" id="J_leftValue"> 0 </div>';
                html +='<div class="checkIn_right_value hide" id="J_rightValue"> 0 </div>';
                html += '</div>';
                html += '</div>';
                html += '</div>';
           //     html += '<a class="close" href="javascript:;"></a>';
                html += '</div>';
                html += '</div>';
          //      $("body").append(html);
                var checkInDialog = new Dialog({
                    classPrefix: "g-dialog",
                    effect: 'fade',
                    content: html
                }).show();

                    setTimeout(function() {
                          //  var leftValue = 0;
                         //   var rightValue = 1 + Math.floor(Math.random() * 3);
                        //    $.smeite.checkIn.checkInState.shiDou =leftValue*10+rightValue

                            $.ajax({
                                type : "POST",
                                url: "/ajaxCheckIn",
                                success: function(data){
                                    if(data.code=="100"){
                                        $('#J_leftValue').text(data.leftValue);
                                        $('#J_rightValue').text(data.rightValue);
                                        $(".show").hide()
                                        $(".hide").show()
                                        var shiDouGiftMsg =""
                                        if(data.shiDouGift!=0) giftMsg="，额外获得"+data.shiDouGift
                                        var msg ='恭喜您获得<strong class="rc">'+(data.leftValue*10+data.rightValue)+'</strong>个集分宝，连续签到<span style="color:#9dc62c;font-size:20px;">'+ data.checkInDays +'</span> 天'+shiDouGiftMsg+'。向您推荐~ '
                                        $("#J_checkInMsg").html(msg)
                                        $.smeite.checkIn.changeCheckInIcon(true);
                                        $.smeite.checkIn.recommendProcess(data.goods)
                                    }else if(data.code=="404"){
                                        //未登录
                                        $.smeite.dialog.login();
                                    }
                                }
                            });

                        },
                        2500)

        },
        /* 推荐 */
        recommendProcess:function(data){
            var html = "";
            html +='<div class="goods-area clearfix">';
            html +='<div class="goods-pic fl"><a href="' +data.clickUrl+' "> <img src="'+data.pic+'" width="150" height="150" ></a> </div>';
            html +='<div class="buy-box clearfix fl">';
            html +='<dl class="buy-meta">';
            html +='<dt>'+data.title+'</dt>';
            html +='<dd class="clearfix"> <div class="price">价格：<span class="price-promotion"> '+data.price+'</span></div></dd>';

            html +="</dl>";
            html +='<a class="buy-btn" href="'+data.clickUrl+'"> 去看看&gt; </a>';
            html += '</div>';
            html += '</div>';
            $("#J_checkIn").html(html);

        },
         addToFav:function(o){
        var url = "http://smeite.cn";
        var title = "食美特，不一样的生活";
        if (window.sidebar) { // Mozilla Firefox Bookmark
            window.sidebar.addPanel(title, url,"");
        } else if( window.external&&document.all) { // IE Favorite
            window.external.AddFavorite( url, title);
        } else if(window.opera) { // Opera 7+
            return false; // do nothing
        } else {
            jQuery.smeite.tip.conf.tipClass = "tipmodal tipmodal-error2";
            jQuery.smeite.tip.show($(o),"您的浏览器不支持自动加收藏，请按 ctrl+d 加入收藏。");
        }
    }

    }



    /* 初始化加载中的内容*/
    $(function(){

        //使用弹窗方式登录
        if($("a[rel=loginD]")[0]){
            $("a[rel=loginD]").click(function(event){
                event.preventDefault();
                $.smeite.dialog.login();
            });
        }

        /*分享网络商品*/
        if($("a[rel=shareGoods]")[0]){
            $("a[rel=shareGoods]").click(function(){
                if(!$.smeite.dialog.isLogin()){
                    return false;
                }
                if(SMEITER.isBlack=="true"){
                    alert("您的分享功能已被禁用");
                    return false;
                }
                var $this = $(this);
                if(!$("#J_ShareGoodsD")[0]){
                    var html = "";
                    html += '<div id="J_ShareGoodsD" class="g-dialog sg-dialog">';
                    html += '<div class="content">';
                    html += '<p class="title">将宝贝网址粘贴到下面框中：</p>';
                    html += '<form class="sg-form" name="shareGoods" action="/ugc/api/findProduct">';
                    html += '<div class="clearfix"><input class="base-input sg-input" name="url" value="" placeholder="http://" autocomplete="off" />';
                    html += '<input type="submit" id="J_GoodsUrlSubmit" class="bbl-btn url-sub" value="确定" /></div>';
                    html += '<div class="text-tip"></div>';
                    html += '</form>';
                    html += '<div class="sg-source">';
                    html += '<p class="pt5 pb5 fl">已支持网站：</p>';
                    html += '<div class="source-list fl">';
                    html += '<a class="icon-source icon-taobao" href="http://www.taobao.com/" target="_blank">淘宝网</a>';
                    html += '<a class="icon-source icon-tmall" href="http://www.tmall.com/" target="_blank">天猫商城</a>';
                    html += '</div>';
                    html += '<div class="clear"></div>';
                    html += '<p class="contact"><strong>不欢迎商家分享，合作请<a href="http://smeite.com/contactUs" target="_blank">点击此处</a>。</strong></p>';
                    html += '</div>';
                    html += '<div class="tipbox-up"><em>◆</em><span>◆</span></div>';
                    html += '<a class="close" href="javascript:;"></a>';
                    html += '</div>';
                    html += '</div>';
                    $("body").append(html);
                    $(".sg-dialog .close").click(function(){
                        $("#J_GoodsUrlSubmit").enableBtn("bbl-btn");
                        $("#J_ShareGoodsD").fadeOut("fast");
                    });
                    $(".sg-form").submit(function(){
                        var $this = $(this);
                        var url = $.smeite.util.trim($(".sg-input").val());
                        if(url==""){
                            $(".text-tip").html('<span class="errc">宝贝网址不能为空~</span>').show();
                        }else if(!$.smeite.util.validSite(url)){
                            $(".text-tip").html('<span class="errc">暂时还不支持这个网站呢~</span>').show();
                        }else{
                            $(".text-tip").html('<span class="gc6">宝贝信息抓取中…</span>').show();
                            $("#J_GoodsUrlSubmit").disableBtn("bbl-btn");
                            $.get($this.attr("action"),$this.serializeArray(),function(data){
                                $("#J_GoodsUrlSubmit").enableBtn("bbl-btn");
                                if(data.code=="100"){
                                    $("#J_ShareGoodsD").hide();
                                    $.smeite.ugc.goodspub(data.product);
                                }else if(data.code=="105"){
                                    $("#J_ShareGoodsD").hide();
                                    $.smeite.ugc.goodsExist(data.product);
                                }else if(data.code=="104"){
                                    $(".text-tip").html('<span class="errc">宝贝信息抓取失败，请重试…</span>').show();
                                }else if(data.code=="107"){
                                    $(".text-tip").html('<span class="errc">暂时还不支持这个宝贝…</span>').show();
                                }else if(data.code=="108"){
                                    $(".text-tip").html('<span class="errc">你已经分享过这个宝贝啦…</span>').show();
                                }else if(data.code=="110"){
                                    $(".text-tip").html('<span class="errc">亲，该商品所在商家已列入黑名单，申诉请联系service@smeite.com</span>').show();
                                }else if(data.code=="400"){
                                    alert("亲，你还没有登录！");
                                    window.location.href="/user/login";
                                } else if(data.code=="444"){
                                    alert("你已被禁止登录！");
                                    window.location.href="/user/logout";
                                }else if(data.code=="442"){
                                    alert("请不要频繁分享同一店铺的商品，否则帐户可能会被冻结。如果你是优质商户，请联系我们（bd@smeite.com）");
                                }else if(data.code=="443"){
                                    alert("由于你频繁分享同一店铺商品，分享功能已被禁用。有疑问请联系 GCTU@smeite.com（注：邮件附上用户名）");
                                }else if(data.code=="445"){
                                    alert("城管大队怀疑你恶意发广告，将禁止你发布商品的权利，申诉请联系GCTU@smeite.com");
                                }
                            });
                        }
                        return false;
                    });
                }else{
                    $(".sg-input").val("");
                    $(".text-tip").html("");
                }

                if($this.hasClass("hd-share-goods")){
                    $(".shareIt").append($("#J_ShareGoodsD"));
                }else{
                    $("body").append($("#J_ShareGoodsD"));
                }
                var position = $.smeite.util.getPosition($this).leftBottom();
                var W = $("#J_ShareGoodsD").outerWidth(),
                    H = $("#J_ShareGoodsD").outerHeight(),
                    btnW = $this.outerWidth(),
                    dLeft = position.x,
                    tipLeft = btnW/2 - 8;
                if((position.x + W) > 960){
                    dLeft = position.x - (W - btnW);
                    tipLeft = W - btnW/2 - 8;
                }
                $("#J_ShareGoodsD .tipbox-up").css({
                    left: tipLeft + "px"
                });
                if($this.hasClass("hd-share-goods")){
                    $("#J_ShareGoodsD").css({
                        left: "auto",
                        right: "0",
                        top: "33px"
                    }).fadeIn("fast");
                }else{
                    $("#J_ShareGoodsD").css({
                        right: "auto",
                        left: dLeft + "px",
                        top: position.y + 10 + "px"
                    }).fadeIn("fast");
                }
                $(".sg-input").focus();
            });
        }



        //标签输入框自动转换“,”
        $(document).on("keyup","input[rel=tagsInput]",function(){
            //限制每个标签的中文长度
            var MaxSingleTagLength = 14,
                MaxAllTagsLength = 64,
                thisVal = $(this).val();
            if($.smeite.util.getStrLength($.smeite.util.htmlToTxt(thisVal))<=MaxAllTagsLength){
                var $this = $(this);
                thisVal = thisVal.replace(/\uff0c|\s+/g,",");
                while(thisVal.indexOf(',,')>=0){
                    thisVal = thisVal.replace(',,',',');
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
        })

        /*创建主题  先判断是否登录 2012-12-26 */
        if($("a[rel=createTheme]")[0]){
            $("a[rel=createTheme]").click(function(){
                if(!$.smeite.dialog.isLogin()){
                    return false;
                }
                if(SMEITER.isBlack=="true"){
                    alert("您的分享功能已被禁用");
                    return false;
                }

            })
        }
        /* 判断用户是否为新用户 如果是新用户 则显示见面有礼*/
        $.smeite.dialog.isNew()

       /* 用户签到 */
        $.smeite.checkIn.judgeCheckInState()
        $("a[rel=checkIn]").click(function(){
            if(!$.smeite.dialog.isLogin()){
                return false;
            }
            if($("a[rel=checkIn]").hasClass("checked")){
                return false;
            }
            /* 显示用户签到过程 */
            $.smeite.checkIn.checkInProcess()

        })


        /* 下拉框 */
        $(".gohome").dropDown({
            classNm: ".set-dropdown"
        });
        /*分享好东西*/
        $(".btn-sg").dropDown({
            classNm: ".shareit-dropdown"
        });
        $(".btn-checkIn").dropDown({
            classNm: ".checkIn-dropdown"
        });
        /*消息*/
        $(".xiaoxi").dropDown({
            classNm: ".xiaoxi-dropdown"
        });


        /* 搜索框效果 header 搜索框*/
        $(".header-search-button").bind("click",function(){
            var self=$(this);
            var form=self.closest("form");
            var input=$(".search-input-keyword");
            var inputVal=input.val();
            inputVal=$.smeite.util.trim(inputVal);
            if(inputVal=="请输入您想要搜索关键词"){
                input.val("");
                input.focus();
                return false;
            }
            if(inputVal==""){
                input.focus();
                return false;
            }
            form.submit(function(){});
        });

        $(".search-input-keyword").bind("click",function(){
            $("#search").addClass("typing")
            var self=$(this);
            var inputVal=$.smeite.util.trim(self.val());
            if(inputVal=="请输入您想要搜索关键词"){
                self.val("");
            }
        });

        $(".search-input-keyword").bind("keydown",function(event){
            var keycode=event.which;
            var self=$(this);
            var form=self.closest("form");
            var inputVal=self.val();
            inputVal=$.smeite.util.trim(inputVal);
            if(keycode=="13"){
                if(inputVal==""){
                    self.focus();
                    return false;
                }

                form.submit(function(){});
            }
        });

        $(".search-input-keyword").bind("blur",function(){
            $("#search").removeClass("typing")
            var self=$(this);
            var inputVal=$.smeite.util.trim(self.val());
            if(inputVal==""){
                self.val("请输入您想要搜索关键词");
            }
        });

        /* 返回顶部 */
        $("#returnTop").returntop();

        $('.like-common .like').hover(function(){
            $(this).parent().children('.like-num').find('.J_scrollUp').animate({ top:"-24" }, 600)
        },function(){
            $(this).parent().children('.like-num').find('.J_scrollUp').animate({  top:"0" },  600)

        })
        //鼠标enter,leave到item上显示可点击的喜欢,喜欢数
        $('.like-state .ico-likes').hover(function(){
            $(this).closest(".like-state").find(".J_scrollUp").animate({top:'-24'},600)
        },function(){
            $(this).closest(".like-state").find(".J_scrollUp").animate({top:'0'},600)
        })




        //第三方登录优先级
        var refererUrl=document.referrer
        var referer="smeite"
        if(refererUrl.indexOf("qq")>0)referer ="qq"
        if(refererUrl.indexOf("taobao")>0 || refererUrl.indexOf("tmall")>0)referer ="taobao"
        if(refererUrl.indexOf("weibo")>0) referer="weibo"
        var loginNavArr = [
            {"referer":"qq", "snsType":"qzong"},//4
            {"referer":"weibo", "snsType":"sina"},//3
            {"referer":"taobao", "snsType":"taobao"}//8
        ]
        var RefererGuide = function(curReferer, snsType){
            if(!$(".guide")[0]){
                var otherLoginArr = $.grep(loginNavArr, function(value, index){
                    return (value.referer != curReferer);
                });
                var otherLoginHtml = "";
                $.map(otherLoginArr, function(value, index){
                    otherLoginHtml += '<li><a target="_blank" class="shortcut-login-' + value.referer + '" href="http://smeite.com/user/snsLogin?snsType=' + value.snsType + '">' + value.referer + '</a></li>';
                });
                var HTML = '<div class="guide guide-' + curReferer + '">'
                    +'<div class="guide_boby">'
                    +'<p><i></i>登录 smeite.com ，发现你的喜欢！<span><a href="/user/regist">注册帐号 ></a></span></p>'
                    +'<div class="guide_add">'
                    +'<a id="J_RefererLogin" class="referer-login" target="_blank" href="http://smeite.com/user/snsLogin?snsType=' + snsType + '">' + curReferer + '</a>'
                    +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                    +'<ul class="other-login">' + otherLoginHtml + '</ul>'
                    +'</div>'
                    +'<div class="del" id="J_CloseGuide">'
                    +'<a href="javascript:void(0);">关闭引导</a>'
                    +'</div>'
                    +'</div>'
                    +'</div>';
                $("body").append(HTML);
                $("#J_Favorites").click(function(){
                    $.smeite.addToFav(this);
                })
                $("#J_CloseGuide").click(function(){
                    Cookie.set("refererGuide","no")    // 点击取消之后，不在出现
                    $(".guide").hide();
                })
            }
            var posGuide = function(){
                if($.smeite.util.isIE6()){
                    var windowtop = $(window).height()-90;
                    $(".guide").css({
                        position:"absolute",
                        top:windowtop
                    }).show();
                    $(window).bind("scroll",function(){
                        var docScrollTop = $(document).scrollTop();
                        if ($.smeite.util.isIE6()) {
                            $(".guide").css("top", (docScrollTop+windowtop)+"px")
                        }
                    });
                }else{
                    $(".guide").show().animate({
                        bottom:0
                    },500);
                }
            }
            var href = window.location.href;
            if(href.indexOf("/user/login")==-1 && href.indexOf("/user/doEmailLogin")==-1&& href.indexOf("/user/regist")==-1){
                if("no" != Cookie.get("refererGuide") && referer !="smeite"){
                    posGuide();
                }
            }

        }

        //不登陆的用户会出现分享条 并且根据referer 引导登录
        if(SMEITER.userId == "" && typeof referer != 'undefined'){
            switch(referer){
                case "smeite":
                    break;
                case "taobao" : {
                    RefererGuide("taobao", "taobao");
                }break;
                case "weibo" : {
                    RefererGuide("weibo", "sina");
                }break;
                case "qq": {
                    RefererGuide("qq", "qzone");
                }break;
                /*default : {
                 RefererGuide("qq", "qzone");
                 }*/
            }
        }else{
            $(window).bind("scroll",function(){
                var showguide = function(){
                    var win = $(window);
                    var HTML = "";
                    if($(".guide").length!==1){
                        HTML = '<div class="guide guide-share">'
                            +'<div class="guide_boby">'
                            +'<p>如果你喜欢食美特，就把Smeite.com加入收藏夹吧，或者分享给你的朋友~</p>'
                            +'<div class="guide_add" style="margin-right:174px;">'
                            +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                            +'<div class="weibo">'
                            +'<a href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://smeite.com/assets/img/ui/smeite.jpg\';window.open(\'http://service.t.sina.com.cn/share/share.php?appkey=2610725727&title=\'+title+\'&pic=\'+pic);})())" alt="分享到新浪微博">分享到新浪微博</a>'
                            +'<a style="margin-left:6px; width:130px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://smeite.com/assets/img/ui/smeite.jpg\';window.open(\'http://v.t.qq.com/share/share.php?appkey=db0de5e94b314972b3e7efd23fa7ce1e&title=\'+title+\'&pic=\'+pic+\'&site=\'+link);})())" alt="分享到腾讯微博"></a>'
                            +'<a style="margin-left:6px; width:118px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。食美特：http://smeite.com\');var link=encodeURIComponent(window.location.href);window.open(\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=\'+link);})())" alt="分享到QQ空间"></a>'
                            +'</div>'
                            +'</div>'
                            +'<div class="del" id="J_CloseGuide">'
                            +'<a href="javascript:void(0);">关闭引导</a>'
                            +'</div>'
                            +'</div>'
                            +'</div>';
                        $("body").append(HTML);

                        $("#J_Favorites").click(function(){
                            $.smeite.addToFav(this);
                        })
                        $("#J_CloseGuide").click(function(){
                            // 用户取消后，cookie 记录，为了保证不在出现，扰民
                            Cookie.set("showGuide","no")
                            $(".guide").hide();
                        })
                    }
                    if($.smeite.util.isIE6()){
                        $(".guide").css({
                            position:"absolute",
                            top:win.scrollTop()+win.height()-90
                        })
                        if(win.scrollTop()>800){
                            $(".guide").show();
                        }
                    }else{

                        if(win.scrollTop()>800){

                            $(".guide").show().animate({
                                bottom:0
                            },500);
                        }
                    }
                }
                /*如果用户没有点击取消 则显示 引导*/
                if("no" != Cookie.get("showGuide")){
                    showguide();
                }
            });
        }
//同步授权登录后关注弹出层
        window.followSmeite = function(code,msg,site,flag,refresh){
            if(code==444){
                alert(msg);
                return false;
            }
            if((site!="sina" && site!="qzone" ) || flag=="true" ){
                if(refresh){
                    window.location.reload();
                }
                return false;
            }
            var bdClass = "",
                frameHtml = "";
            if(site=="sina"){
                bdClass = "sinaBd";
                frameHtml = '<iframe width="63" height="24" frameborder="0" allowtransparency="true" marginwidth="0" marginheight="0" scrolling="no" border="0" src="http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=63&height=24&uid=1283431903&style=1&btn=red&dpc=1"></iframe>';
            }else if(site=="qzone"){
                bdClass = "qzoneBd";
                frameHtml = '<iframe src="http://open.qzone.qq.com/like?url=http%3A%2F%2Fuser.qzone.qq.com%2F1469909930&type=button&width=400&height=30&style=2" allowtransparency="true" scrolling="no" border="0" frameborder="0" style="width:65px;height:30px;border:none;overflow:hidden;"></iframe>';
            }

            if(!$("#followDialog")[0]){
                var html = '<div id="followDialog" class="g-dialog">';
                html +=	'<div class="dialog-content">';
                html +=	'<div class="hd"><h3></h3></div>';
                html +=	'<div class="bd clearfix '+bdClass+'">';
                html +=	'<div class="btnFrame">';
                html +=	frameHtml;
                html +=	'</div>';
                html +=	'</div>';
                html +=	'<i></i>';
                html +=	'<label><input type="checkbox" class="check" name="noMore" />不再提示</label>';
                html +=	'<a class="close" href="javascript:;"></a>';
                html +=	'</div>';
                html +=	'</div>';
                $("body").append(html);
                if($("#loginDialog:visible")[0]){
                    $("#loginDialog").empty().remove();
                    $("#exposeMask").empty().remove();
                }
                $("#followDialog").overlay({
                    top: 'center',
                    mask: {
                        color: '#000',
                        loadSpeed: 200,
                        opacity: 0.3
                    },
                    closeOnClick: false,
                    load: true
                });
                $("#followDialog").overlay().getClosers().bind("click",function(){
                    if($("input[name=noMore]")[0].checked){
                        Cookie.set("noMoreTip","n");
                    }
                    if(refresh){
                        window.location.reload();
                    }
                });
            }
        }
        /*异步授权登陆后*/
        window.refresh=function(){
            window.location.reload();
        }


    });



});

