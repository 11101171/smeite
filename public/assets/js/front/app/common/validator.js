/* 通用表单校验 add by @Jeffer on 2012-05-29 */
define(function(require, exports) {
	var $ = jQuery = require("jquery");
//(function($) { 
	$.smeite.validator = {
		conf: {
			infoEvent: 'focus',	
			valiEvent: 'blur',	
			speed: 100,
			fun:function(){}
		},
		message: {
						
		},
		fn: function(matcher, msg, type, fn) {
			if ($.isFunction(msg)) {
				fn = msg;
				msg = "";
			} 

			fns.push([matcher, msg, type, fn]);
		},
		effects: function(el, msg, type) {
			if(!el.next("span")[0]){
				el.after('<span class="tip"></span>');
				var position = $.smeite.util.getPosition(el).rightTop();
				el.next("span").css({
					left: position.x + 10 + "px",
					top: position.y + "px"
				});
			}
			switch(type){
				case "info":
					el.next(".tip").removeClass().addClass("tip").html(msg).fadeIn();
					break;
				case "require":
					el.next(".tip").removeClass().addClass("tip error").html(msg).fadeIn();
					el.unbind("focus");
					break;
				case "empty":
					if(msg==""){
						el.next(".tip").hide();
					}else{
						el.next(".tip").removeClass().addClass("tip error").html(msg).fadeIn();
					}
					break;
				case "error":
					el.next(".tip").removeClass().addClass("tip error").html(msg).fadeIn();
					el.unbind("focus");
					break;
				case "ajax":
					el.next(".tip").removeClass().addClass("tip ajaxvali").html("").fadeIn();
					el.unbind("focus");
					break;
				case "correct":
					el.next(".tip").removeClass().addClass("tip correct").html("").fadeIn();
					el.unbind("focus");
			}
		}
	}
	
	var fns = [],
	vali = $.smeite.validator;
	vali.fn("input[name=email]", "请输入你的常用Email", "info");
	vali.fn("input[name=email]", "Email格式不正确", "error", function(el, v) {
		return $.smeite.util.isEmail(v);
	});
	vali.fn("input[name=email]", "请填写Email", "require", function(el, v) {
		return $.smeite.util.isEmpty(v);
	});
	vali.fn("input[name=nickname]", "4-30个字符，支持中英文、数字、“_”和减号", "info");
	vali.fn("input[name=nickname]", "仅支持中英文、数字、“_”和减号", "error", function(el, v) {
		return $.smeite.util.isNick(v);
	});
	vali.fn("input[name=nickname]", "最长不能超过30个字符", "require", function(el, v) {
		return $.smeite.util.nickMax(v);
	});
	vali.fn("input[name=nickname]", "请输入至少4个字符", "require", function(el, v) {
		return $.smeite.util.nickMin(v);
	});
	vali.fn("input[name=nickname]", "请填写昵称", "require", function(el, v) {
		return $.smeite.util.isEmpty(v);
	});
	vali.fn("input[name=password]", "6-16个半角字符，支持字母、数字、符号，区分大小写", "info");
	vali.fn("input[name=password]", "密码中不能包含空格", "error", function(el, v) {
		return /^\S+$/.test(v);
	});
	vali.fn("input[name=password]", "密码太长了，最多16位", "require", function(el, v) {
		return v.length > 16 ? false : true;
	});
	vali.fn("input[name=password]", "密码太短了，最少6位", "require", function(el, v) {
		return $.smeite.util.tooShort(v,6);
	});
	vali.fn("input[name=password]", "请输入密码", "require", function(el, v) {
		return v.length == 0 ? false : true;
	});
	vali.fn("input[name=password2]", "请重复输入一次密码", "info");
	vali.fn("input[name=password2]", "两次输入的密码不一致，请重新输入", "error", function(el, v) {
		return v == $("#password").val();
	});
	vali.fn("input[name=password2]", "请重复输入一次密码", "require", function(el, v) {
		return $.smeite.util.isEmpty(v);
	});
	vali.fn("input[name=blog]", "如：http://blog.smeite.com/smeite", "info");
	vali.fn("input[name=blog]", "请填写正确的网站地址", "empty", function(el, v) {
		return v=="" ? true : new RegExp("^http:\\/\\/([\\w-]+(\\.[\\w-]+)+(\\/[\\w-   .\\/\\?%@&+=\\u4e00-\\u9fa5]*)?)?$").test(v);
	});
	vali.fn("textarea[name=intro]", "最长70个字", "info");
	vali.fn("textarea[name=intro]", "最长不能超过70个字", "empty", function(el, v) {
		return $.smeite.util.getStrLength(v) > 70 ? false : true;
	});
	vali.fn("input[name=trueName]", "请输入真实的收货人姓名", "error", function(el, v) {
		return v.length == 0 ? false : true;
	});
	vali.fn("textarea[name=addressDetail]", "详细地址不能超过50个字符", "error", function(el, v) {
		return $.smeite.util.getStrLength(v) > 50 ? false : true;
	});
	vali.fn("textarea[name=addressDetail]", "详细地址不能为空", "require", function(el, v) {
		return v.length == 0 ? false : true;
	});
	vali.fn("textarea[name=addressDetail]", "请填写50个字以内的详细街道名，小区名等", "info");
	vali.fn("input[name=phone]", "请输入正确的手机号码", "error", function(el, v) {
		return /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/.test(v);
	});
	vali.fn("input[name=phone]", "手机号码不能为空，否则返利成功后，无法提醒您", "require", function(el, v) {
		return v.length == 0 ? false : true;
	});
	vali.fn("input[name=phone]", "请填写11位手机号码", "info");
	vali.fn("input[name=alipay]", "请输入正确的支付宝账号", "error", function(el, v) {
		return $.smeite.util.isEmail(v)||/^(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/.test(v);
	});
	vali.fn("input[name=alipay]", "支付宝账号不能为空，否则不能成功返利", "require", function(el, v) {
		return v.length == 0 ? false : true;
	});
	
	function Validator(form, conf){
		var self = this,
		inputs = form.find(":input").not(":button, :image, :reset, :submit");
						
		$.each(fns, function() {
			var fn = this, match = fn[0], msg = fn[1], type = fn[2];
			if (inputs.filter($(match))) {
				var ele = $(match);
				ele.data("vali", 0);
				if(type == "info"){
					ele.bind(conf.infoEvent, function() {
						vali.effects(ele, msg, type);
					});
				}else{
					ele.bind(conf.valiEvent, function() {
						var returnValue = fn[3].call(self, ele, ele.val());
						if(!returnValue){
							ele.data("vali", 0);
							vali.effects(ele, msg, type);
						}else{
							if(type == "error"){
								ele.data("vali", 1);
								vali.effects(ele, "OK", "correct");
							}else if(type == "empty"){
								ele.data("vali", 1);
								if(ele.val()==""){
									vali.effects(ele, "", "empty");
								}else{
									vali.effects(ele, "OK", "correct");
								}
							}
						}
					});
				}
			}
			
		});
		
		if($("input[name=email]")[0]){
			$("input[name=email]").bind("blur",function(){
				var $this = $(this);
				if($this.data("vali")==1 && $this.data("vali")!=11){
					$this.data("vali", 0);
					vali.effects($this, "", "ajax");
					/*var param = "";
					if($this.data("opt")=="sns"){
						param = "?sns=sns";
					}*/
					$.ajax({
                        url: '/user/checkEmailExist',
                        type : "post",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({"email": $(this).val() }),
                    	success: function(data){ 
                    		if(data.code=="0"){
                    			$this.data("vali", 11);
                    			vali.effects($this, "OK", "correct");
                    		}else if(data.code=="1"){
                    			$this.data("vali", 0);
                    			vali.effects($this, "此Email已被注册", "error");
                    		}else{
                    			$this.data("vali", 0);
                    			vali.effects($this, "系统出错了，请稍后再试…", "error");
                    		}
                    	}
                    });
				}
			});
		}
        /*判断用户名是否重复，smeite.com 需要判断用户名是否重复吗？*/
	/*	if($("input[name=nickname]")[0]){
			$("input[name=nickname]").bind("blur",function(){
				var $this = $(this);
				if($this.data("vali")==1 && $this.data("vali")!=11){
					if($this.val()==SMEITER.nick){
						return false;
					}
					$this.data("vali", 0);
					vali.effects($this, "", "ajax");
					$.ajax({
						type: 'post',  
                    	url: '/checkEmailExist',
                    	dataType: 'json',  
                    	data: "nickname="+$this.val(),
                    	success: function(data){ 
                    		if(data.code=="0"){
                    			$this.data("vali", 11);
                    			vali.effects($this, "OK", "correct");
                    		}else if(data.code=="2"){
                    			$this.data("vali", 0);
                    			vali.effects($this, "此昵称已被注册", "error");
                    		}else{
                    			$this.data("vali", 0);
                    			vali.effects($this, "系统出错了，请稍后再试…", "error");
                    		}
                    	}
                    });
				}
			});
		}*/
		if($("input.check")[0]){
		$("input.check[name!=subscription]").click(function(){
			if($(this)[0].checked==false){
				$("input[type=submit]")[0].disabled = "disabled";
				$("input[type=submit]").removeClass("bbl-btn").addClass("disabled");
			}else{
				$("input[type=submit]")[0].disabled = "";
				$("input[type=submit]").removeClass("disabled").addClass("bbl-btn");
			}				
		});
		}
		form.submit(function(){
			var isPass = true;
			conf.fun(vali,inputs);
			inputs.each(function(){
				if($(this).data("vali")==0){
					isPass = false;
				}
			});
			if(!isPass){
				inputs.trigger("blur");
			}
			if($("input.check[name!=subscription]")[0] && $("input.check[name!=subscription]")[0].checked==false){
				isPass = false;				
			}
			return isPass;
		});
	}
	
	return function($){
	$.fn.validator = function(conf) {
		var instance = this.data("validator");
		if (instance) {
			instance.destroy();
			this.removeData("validator");
		}

		conf = $.extend(true, {}, $.smeite.validator.conf, conf);

		if (this.is("form")) {
			return this.each( function() {
				var form = $(this);
				instance = new Validator(form, conf);
				form.data("validator", instance);
			});
		} else {
			instance = new Validator(this.eq(0).closest("form"), conf);
			return this.data("validator", instance);
		}

	};
	}
//})(jQuery);
});