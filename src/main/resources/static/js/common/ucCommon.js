// 创建SQ表单验证实例
var clearValidateError = SQ.validater.clearValidateError;
var validateShowError = SQ.validater.validateShowError;
var formValidate = SQ.validater.formValidate;
// 非空验证
var fieldEmptyValidate = function(form, remindWords) {
    var isFormFilled = true;
    var $_form = $(form);
    var words = remindWords || "";
    // 清空错误提示
    clearValidateError($_form);
    $_form.find(".necessary").each(function() {
        var $_input = $(this);
        if (!$_input.val() && !$_input.is(":hidden")) {
            validateShowError($_input, $_input.attr("placeholder") || words, $_form);
            isFormFilled = false;
            return false;
        }
    });
    return isFormFilled;
}; 

/*
* 前端模版扩展方法
* 使用template.helper(name, callback)注册公用辅助方法
* 模板中使用的方式：{{time | dateFormat:'yyyy-MM-dd hh:mm:ss'}}
* 支持传入参数与嵌套使用：{{time | say:'cd' | ubb | link}}
*/
$(function() {
    String.prototype.trim = function() {
        return this.replace(/(^\s*)|(\s*$)/g, '');
    };
    String.prototype.trimenter = function() {
        return this.replace(/^\n+|\n+$/g, '');
    };
    /*
    * 工具类
    */
    // 根据秒数转成 X小时X分钟X秒 的展现形式
    template.helper("formatSecond", function(second, maxShowUnit) {
        return SQ.date.transformTime(second, {
            maxShowUnit: maxShowUnit
        });
    });
    // 通用时间格式化方法
    template.helper("formatDate", function(second) {
        if ($.isNumeric(second)) {
            second *= 1000;
        }
        else {
            second = Number(new Date(second));
        }

        return SQ.date.format(second);
    });
    //分割价格,从右到左每三位加逗号分割
    template.helper("getPriceFormat", SQ.number.priceFormat);
    // 在订单编号长度不足6位时，用"0"补齐
    template.helper("orderNumberFormat", SQ.number.dataPatchFormat);
    // 在编号长度不足9位时(可指定其他位数)，用"10"补齐,第一位是1
    template.helper("servicesOrderFormat", function(sources, finalLength) {
        var formatNumber = String(SQ.number.dataPatchFormat(sources, (finalLength || 9)));

        if (formatNumber.charAt(0) == "0") {
            formatNumber = "1" + formatNumber.slice(1);
        }

        return formatNumber;
    });
    // 对数值类 补齐两位小数
    template.helper("toFixed", SQ.number.toFixed);
    // 获取数字的整数部分
    template.helper("getInt", SQ.number.getInt);
    // 获取数字的小数部分
    template.helper("getDecimals", SQ.number.getDecimals);
    // 开放 Math.abs 方法引用
    template.helper("getAbsoluteValue", Math.abs);

    template.helper('dateFormat', function(date, format) {
        if (typeof date === "string") {
            var mts = date.match(/(\/Date\((\d+)\)\/)/);
            if (mts && mts.length >= 3) {
                date = parseInt(mts[2]);
            }
        }
        date = new Date(date);
        if (!date || date.toUTCString() == "Invalid Date") {
            return "";
        }
        var map = {
            "M": date.getMonth() + 1, //月份 
            "d": date.getDate(), //日 
            "h": date.getHours(), //小时 
            "m": date.getMinutes(), //分 
            "s": date.getSeconds(), //秒 
            "q": Math.floor((date.getMonth() + 3) / 3), //季度 
            "S": date.getMilliseconds() //毫秒 
        };

        format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - 2);
                }
                return v;
            }
            else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    });

    // 上传文件的正确地址（用户上传的文件在http://www.youbangyun.com/upload下）
    template.helper("getUploadFileUrl", function(fileUrl) {
        return "http://www.youbangyun.com/upload" + fileUrl;
    });
    // 通用星级格式化方法
    template.helper("formatDegree", function(degree) {
        var r = "";
        for (var i = 0; i < degree; i++) {
            r = r + "★";
        }
        for (var i = 5; i > degree; i--) {
            r = r + "☆";
        }
        return r;
    });
    // 通用排名格式化方法
    template.helper("formatRank", function(rank, okRank, noIndex) {
        var r = "";
        if (rank == noIndex) {
            r = '<b class="text-primary">无索引</b>';
        }
        else if (rank <= 0) {
            r = "-";
        }
        else if (rank <= okRank) {
            r = '<b class="text-red">' + rank + '</b>';
        }
        else {
            r = "五页外";
        }
        return r;
    });
    // 通用是否格式化方法
    template.helper("formatBool", function(v) {
        var r = "";
        if (v) {
            r = '<b class="text-green">√</b>';
        }
        else {
            r = '<b class="text-red">×</b>';
        }
        return r;
    });
    
  //权限勾选置灰格式化
    template.helper("privilegeFormate",function(item2, item){
    	if ((item2.value & item.valueconfig) ==0){
    		return ' disabled="disabled" ';
    	}else if((item2.value & item.defvalue) ==item2.value){
    		return ' checked="checked" ';
    	}
    });
    
    //权限勾选置灰格式化
    template.helper("rolePrivilegeFormate",function(item2, item){
    	if ((item2.value & item.valueconfig) ==0){
    		return ' disabled="disabled" ';
    	}else if((item2.value & item.value) ==item2.value){
    		return ' checked="checked" ';
    	}
    });
    
});

/* 状态枚举映射 */
$(function() {

    //认证类型映射
    var verifyTypeMap = {
        "1": { className: "text-finished", name: "个人认证" },
        "2": { className: "text-primary", name: "个体工商户" },
        "3": { className: "text-red", name: "企业认证" }
    };
    SQ.biz.createObjectMapHelper("getVerifyTypeClass", verifyTypeMap);
    
    //认证途径映射
    var verifyMethodMap = {
        "1": { className: "text-red", name: "支付宝" },
        "2": { className: "text-primary", name: "银行卡" },
        "3": { className: "text-finished", name: "线下认证" }
    };
    SQ.biz.createObjectMapHelper("getVerifyMethodClass", verifyMethodMap);
    
    //认证状态映射
    var verifyStatusMap = {
        "1": { className: "", name: "未提交" },
        "2": { className: "text-primary", name: "审核中" },
        "3": { className: "text-red", name: "未通过" },
        "4": { className: "text-finished", name: "已认证" }
    };
    SQ.biz.createObjectMapHelper("getVerifyStatusClass", verifyStatusMap);
    
    //认证步骤映射
    var verifyStateMap = {
        "1": { className: "text-muted", name: "未提交" },
        "100": { className: "text-primary", name: "待审核" },
        "101": { className: "text-primary", name: "待打款" },
        "102": { className: "text-primary", name: "验资已打" },
        "200": { className: "text-primary", name: "验资到账" },
        "300": { className: "text-red", name: "资料错误" },
        "301": { className: "text-red", name: "打款失败" },
        "400": { className: "text-finished", name: "认证成功" },
        "500": { className: "text-red", name: "认证失败" },
        "600": { className: "text-muted", name: "重申资料" },
        "601": { className: "text-muted", name: "重申账户" }
    };
    SQ.biz.createObjectMapHelper("getVerifyStateClass", verifyStateMap);

    //用户状态映射
    var userStatusMap = {
        "1": { className: "text-finished", name: "正常" },
        "2": { className: "text-primary", name: "待完善" },
        "3": { className: "text-red", name: "锁定" }
    };
    SQ.biz.createObjectMapHelper("getUserStatusClass", userStatusMap);
    
    //代理状态映射
    var oemStatusMap = {
    		"1": { className: "text-finished", name: "正常" },
    		"0": { className: "text-red", name: "锁定" }
    };
    SQ.biz.createObjectMapHelper("getOemStatusClass", oemStatusMap);

    //业务查询映射
    var businessTypeMap = {
        "1001": { className: "", name: "域名SEO实时查" },
        "1002": { className: "", name: "域名备案实时查" },
        "1003": { className: "", name: "网址收录实时查" },
        "1004": { className: "", name: "站点索引挖掘" },
        "1005": { className: "", name: "站点排名挖掘" },
        "1006": { className: "", name: "关键词排名实时查" },
        "1007": { className: "", name: "关键词指数实时查" },
        "1008": { className: "", name: "关键词价格实时查" },
        "2001": { className: "", name: "域名SEO监控" },
        "2002": { className: "", name: "域名备案监控" },
        "2003": { className: "", name: "网址收录监控" },
        "2006": { className: "", name: "关键词排名监控" }
    };
    SQ.biz.createObjectMapHelper("getBusinessTypeClass", businessTypeMap);

    //搜索引擎映射
    var searchTypeMap = {
        "1010": { className: "", name: "百度PC" },
        "1015": { className: "", name: "360 PC" },
        "1030": { className: "", name: "搜狗PC" },
        "7010": { className: "", name: "百度手机" },
        "7015": { className: "", name: "360手机" },
        "7030": { className: "", name: "搜狗手机" },
        "7070": { className: "", name: "神马" }
    };
    SQ.biz.createObjectMapHelper("getSearchTypeClass", searchTypeMap);

    //查询状态映射
    var searchStatusMap = {
        "-1": { className: "state-icon-reject", name: "已删除" },
        "1": { className: "state-icon-waiting", name: "待启动" },
        "2": { className: "state-icon-loading text-primary", name: "查询中" },
        "3": { className: "state-icon-done text-finished", name: "已完成" },
        "4": { className: "state-icon-fail", name: "已停止" }
    };
    SQ.biz.createObjectMapHelper("getSearchStatusClass", searchStatusMap);

    //域名安全状态映射
    var domainSoQQSafeMap = {
        "1": { className: "text-primary", name: "检测中" },
        "2": { className: "text-orange", name: "正常" },
        "3": { className: "text-red", name: "危险" },
        "4": { className: "text-finished", name: "信任" }
    };
    SQ.biz.createObjectMapHelper("getDomainSoQQSafeClass", domainSoQQSafeMap);

    //域名注册状态映射
    var domainRegStateMap = {
        "1": { className: "text-muted", name: "删除中" },
        "2": { className: "text-muted", name: "已注册" },
        "3": { className: "text-red", name: "未注册" },
        "4": { className: "text-red", name: "平台注册" }
    };
    SQ.biz.createObjectMapHelper("getDomainRegStateClass", domainRegStateMap);

    //结算状态映射
    var seoSettleStatusMap = {
        "1": { className: "state-icon-waiting", name: "未结算" },
        "2": { className: "state-icon-fail", name: "未达标" },
        "3": { className: "state-icon-done text-finished", name: "已达标" }
    };
    SQ.biz.createObjectMapHelper("getSeoSettleStatusClass", seoSettleStatusMap);

    //Seo状态映射
    var seoStatusMap = {
        "-1": { className: "state-icon-reject", name: "已删除" },
        "1": { className: "state-icon-loading text-primary", name: "检测中" },
        "2": { className: "state-icon-fail", name: "不可优化" },
        "3": { className: "state-icon-waiting", name: "待付款" },
        "4": { className: "state-icon-done text-finished", name: "优化中" },
        "5": { className: "state-icon-error", name: "已停止" }
    };
    SQ.biz.createObjectMapHelper("getSeoStatusClass", seoStatusMap);

    //SiteRent状态映射
    var siteRentStatusMap = {
        "1": { className: "state-icon-waiting text-primary", name: "待出租" },
        "2": { className: "state-icon-money text-finished", name: "已出租" },
        "3": { className: "state-icon-waiting text-primary", name: "优化中" },
        "4": { className: "state-icon-error  text-red", name: "已暂停" },
        "5": { className: "state-icon-done  text-finished", name: "已预订" }
    };
    SQ.biz.createObjectMapHelper("getSiteRentStatusClass", siteRentStatusMap);

    //网站适配方式映射
    var adaptationMap = {
        "1": { className: "text-finished", name: "适配" },
        "2": { className: "text-primary", name: "自动" }
    };
    SQ.biz.createObjectMapHelper("getAdaptationClass", adaptationMap);

    //SiteRentOrder状态映射
    var siteRentOrderStatusMap = {
        "1": { className: "state-icon-done text-primary", name: "服务中" },
        "2": { className: "state-icon-reject", name: "已停止" }
    };
    SQ.biz.createObjectMapHelper("getSiteRentOrderStatusClass", siteRentOrderStatusMap);

    //网页展示方式映射
    var webDirectionMap = {
        "1": { className: "text-red", name: "等待设置" },
        "2": { className: "text-primary", name: "联系资料" },
        "3": { className: "text-primary", name: "跳转网址" }
    };
    SQ.biz.createObjectMapHelper("getWebDirectionClass", webDirectionMap);

    //支付类型类型映射
    var payTypeMap = {
        "1": { className: "", name: "支付宝" },
        "2": { className: "", name: "微信" },
        "3": { className: "", name: "银行卡" },
        "4": { className: "", name: "现金" }
    };
    SQ.biz.createObjectMapHelper("getPayTypeClass", payTypeMap);

    //交易类型映射
    var tradeTypeMap = {
        "10": { className: "", name: "充值金额" },
        "19": { className: "", name: "购买优币" },
        "21": { className: "", name: "人工扣除" },
        "22": { className: "", name: "人工退款" },
        "31": { className: "", name: "云排名购买" },
        "32": { className: "", name: "云排名消费" },
        "33": { className: "", name: "云排名停止" },
        "41": { className: "", name: "云建站网站购买" },
        "42": { className: "", name: "云建站SEO购买" },
        "43": { className: "", name: "云建站SEO消费" },
        "44": { className: "", name: "云建站SEO停止" },
        "45": { className: "", name: "云建站网站停止" },
        "51": { className: "", name: "云站通购买" },
        "52": { className: "", name: "云站通消费" },
        "53": { className: "", name: "云站通停止" }
    };
    SQ.biz.createObjectMapHelper("getTradeTypeClass", tradeTypeMap);

    //域名备案单位性质
    var unitTypeMap = {
        "1": "身份证",
        "2": "营业执照",
        "3": "法人证书",
        "4": "组织机构代码证",
        "5": "社会团体"
    };
    SQ.biz.createTextMapHelper("getUnitTypeName", unitTypeMap);
    
    //合同业务来源映射
    var contractBusinessFromMap = {
        "1": { className: "text-primary", name: "云站通" }
    };
    SQ.biz.createObjectMapHelper("getContractBusinessFromClass", contractBusinessFromMap);

    //合同签约方式映射
    var contractSignMethodMap = {
        "1": { className: "text-primary", name: "线下签约" },
        "2": { className: "text-red", name: "e签宝" }
    };
    SQ.biz.createObjectMapHelper("getContractSignMethodClass", contractSignMethodMap);

    //合同状态映射
    var contractStatusMap = {
        "1": { className: "text-primary", name: "执行中" },
        "2": { className: "text-red", name: "已结束" }
    };
    SQ.biz.createObjectMapHelper("getContractStatusClass", contractStatusMap);

    //关键词级别映射
    var matchKeywordDegreeMap = {
        "1": { className: "text-green", name: "安全" },
        "2": { className: "text-primary", name: "临界" },
        "3": { className: "text-orange", name: "争议" },
        "4": { className: "text-danger", name: "拒绝" }
    };
    SQ.biz.createObjectMapHelper("getMatchKeywordDegreeClass", matchKeywordDegreeMap);
});

// 公共属性 转义处理
$(function() {
    // 对 common、filter（主要是searchKey）、protect（主要是userQuestion） 中的属性进行转义
    if (window.jsData) {
        jsData.common = SQ.util.encodeHtmlTag(jsData.common);
        jsData.filter = SQ.util.encodeHtmlTag(jsData.filter);
        jsData.protect = SQ.util.encodeHtmlTag(jsData.protect);
    }
});

// 页面公共模块初始化及事件绑定
$(function() {
    // 没有 .sq-main 元素的则为 iframe 页面，直接return
    if (!$(".sq-main").length) {
        return;
    }

    // 头部信息填充
    if (window.jsData) {
        $("#helloUserName").html(jsData.username);

        // 填充消息数量
        /*var unReadTotal = jsData.common.unReadTotal;
        var hideClass = "hide";
        var $_messageCount = $(".header-message-count, .message-count");
        if (unReadTotal > 0) {
            $_messageCount.removeClass(hideClass);
            $_messageCount.html(unReadTotal);
        }
        else {
            $_messageCount.addClass(hideClass);
        }*/
    }

    /*
    * 菜单侧收控制
    */
    var $_body = $("body");
    var graceMenuClassName = "graceful-menu";
    var menuCollapseCookieName = "menuCollapsed";
    if ($.cookie(menuCollapseCookieName) && $.cookie(menuCollapseCookieName) == "y") {
        $_body.addClass(graceMenuClassName);
    }
    else {
        $.removeCookie(menuCollapseCookieName, { path: "/" });
    }

    $("#menuControl").click(function() {
        if ($_body.hasClass(graceMenuClassName)) {
            $_body.removeClass(graceMenuClassName);
            $.removeCookie(menuCollapseCookieName, { path: "/" });
        }
        else {
            $_body.addClass(graceMenuClassName);
            $.cookie(menuCollapseCookieName, "y", { expires: 180, path: "/" });
        }
    });
    // 侧收时 一级菜单不响应点击事件（加上menu-linkable类的链接除外）
    $("html").on("click", ".graceful-menu .upper-menu", function() {
        if (!$(this).hasClass("menu-linkable")) {
            return false;
        }
    });
});

// 前端模版渲染相关
$(function() {
    // 主内容区 模板渲染
    var $_contentBox = $("#contentBox");
    // 使用数据 渲染模版页面内容
    if ($_contentBox.length && $("#contentTemplate").length && jsData) {
        // 当data-render不等于"false"时进行模板渲染
        if ($_contentBox.data("render") !== false) {
            // renderContent方法中，会默认进行组件初始化（包括设置菜单高亮）
            SQ.proxyRenderer.renderContent($_contentBox);
        }
    }
    else {
        // 自动对没有contentBox的“静态”页面，初始化面包屑组件 并设置菜单高亮
        SQ.proxyRenderer.renderCrumb();
        SQ.biz.setMenuHighlight();
    }
});

//验证码校验
var captchaCheck;
// 模块事件绑定
$(function() {
    var $_body = $("body");
    // 不响应 href="#a_null" 的链接点击事件
    $_body.on("click", "a", function(e) {
        if ($(this).attr("href") == "#a_null") {
            e.preventDefault();
        }
    });

    // 页面刷新
    $_body.on("click", ".window-reload", function() {
        window.location.reload();
    });

    // 图形验证码显示及点击刷新事件
    var captchaImgSelector = ".show-captcha";
    var $_showCaptcha = $(captchaImgSelector);
    if ($_showCaptcha.length) {
        var refreshCaptchaImg = (function() {
            var createCaptchaSrc = function() {
                return "/VerifyImg.aspx?" + "rnd=" + Math.random();
            };

            return function(captchaImg) {
                $(captchaImg).attr("src", createCaptchaSrc());
            };
        })();

        // 绑定图片及按钮的 点击刷新验证码事件
        $_showCaptcha.click(function() {
            refreshCaptchaImg(this);
        });
        $(".refresh-captcha").click(function() {
            refreshCaptchaImg($(this).parent().find(captchaImgSelector));
        });
    };

    //发送消息弹出验证码确认
    captchaCheck = function(url, data, callback) {
        // 刷新验证码
        $(".show-captcha").trigger("click");
        $("#captcha").val("");
        var _box = $.dialog({
            content: $("#captchaCheck")[0],
            title: '请输入验证码',
            width: 380,
            height: 110,
            okVal: '确认提交',
            ok: function() {
                var verifycode = $.trim($("#captcha").val());
                if (verifycode.length == 0) {
                    $("#captcha").focus();
                    return false;
                }
                $.post({
                    //验证码验证接口
                    url: url + "&verifycode=" + $("#captcha").val(),
                    cache: false,
                    data: data,
                    dataType: "json",
                    beforeSend: function() {
                        SQ.waiting("处理中，请稍后...", true);
                    },
                    complete: function() {
                        SQ.hideWaiting();
                        _box.close();
                    },
                    success: function(json) {
                        if (json.result) {
                            SQ.success(json.text, 5);
                            callback();
                        } else {
                            SQ.error(json.text, 3);
                            if (json.name) {
                                validateShowError(json.name, json.text);
                            }
                        }
                    },
                    error: function() {
                        SQ.showBusy();
                    }
                });
                return false;
            },
            cancel: true
        });
    };

    // 发送手机验证码按钮 倒计时控制 及 点击发送事件
    var $_sendSMSBtn = $("#sendSMSCaptcha");
    var asyncSendSMSCaptcha = function(btn) {
        var $_sendBtn = $(btn);
        var $_mobileLeftTime = $("#mobileLeftTime");
        var sendSMSLeftTime = $_mobileLeftTime.html();

        // 当剩余时间大于0时页面保持倒计时
        if (sendSMSLeftTime > 0) {
            SQ.dom.createCountDown($_sendBtn, {
                time: sendSMSLeftTime
            });
        }
        $_sendBtn.click(function() {
            var data = {};
            if ($("#mobile").length > 0) {
                var _mobileVal = $.trim($("#mobile").val());
                if (_mobileVal == "") {
                    validateShowError("mobile");
                    return false;
                }
                data = { mobile: _mobileVal };
            }
            captchaCheck("/client/userinfo/ajax.aspx?action=SendMobileCode", data, function() {
                SQ.dom.createCountDown($_sendBtn);
            });
        });
    };
    if ($_sendSMSBtn.length) {
        asyncSendSMSCaptcha($_sendSMSBtn);
    };
    // 发送邮件验证码按钮 倒计时控制 及 点击发送事件 方法
    var $_sendEmailCaptcha = $("#sendEmailCaptcha");
    var asyncSendEmailCaptcha = function(btn) {
        var $_sendBtn = $(btn);
        var waitingTime = 20;
        $_sendBtn.click(function() {
            var data = {};
            if ($("#email").length > 0) {
                var _emailVal = $.trim($("#email").val());
                if (_emailVal == "") {
                    validateShowError("email");
                    return false;
                }
                data = { email: _emailVal };
            }
            captchaCheck("/client/userinfo/ajax.aspx?action=SendEmailCode", data, function() {
                SQ.dom.createCountDown($_sendBtn);
            });
        });
    };
    if ($_sendEmailCaptcha.length) {
        asyncSendEmailCaptcha($_sendEmailCaptcha);
    };

    // 数据处理，将验证方式字符串数组化
    if (jsData.protect) {
        jsData.protect.itemArray = SQ.data.stringToArray(jsData.protect.items);
    }

    // 操作保护控制
    var protectEnableSelector = ".protection-enabled";
    $(protectEnableSelector).each(function() {
        var $_protectEnable = $(this);
        // 查找提交按钮所在父元素的表单
        var $_submitForm = $_protectEnable.parents("form");
        $_submitForm.on("click", protectEnableSelector, function() {
            var protectDialog = $.dialog({
                title: "操作保护",
                content: template("operateProtectTpl", jsData),
                okVal: "验证并提交",
                cancel: true,
                // 弹窗内容初始化
                init: function() {
                    // 初始化sq-btn-group
                    SQ.component.initTabs();
                    // 绑定发送手机验证码方法
                    asyncSendSMSCaptcha($("#sendSMSCaptcha"));
                    // 绑定发送邮箱验证码方法
                    asyncSendEmailCaptcha($("#sendEmailCaptcha"));
                    // 验证信息表单域
                    var verifyInputsHtml = [
						'<input type="hidden" name="protectMethod" />',
						'<input type="hidden" name="verifyCode" />'
					].join("");
                    var protectMethodSelector = "input[name=protectMethod]";
                    // 自动在需要提交的form中生成一次验证表单域
                    if ($_submitForm.find(protectMethodSelector).length == 0) {
                        $_submitForm.append(verifyInputsHtml);
                    }

                    // 为弹窗中的验证方式切换按钮绑定点击事件，点击时填充表单域的值
                    var $_protectMethodInput = $(protectMethodSelector);
                    var $_changeVerifyMethodsBtns = $("#protectMethodToggle a");
                    var $_captchaCodeInput = $("#captchaCodeInput");
                    $_changeVerifyMethodsBtns.click(function() {
                        var $_self = $(this);
                        $_protectMethodInput.val($_self.data("method"));
                        $_captchaCodeInput.val("");
                    });
                    // 触发第一个按钮的click事件，初始化填写一次表单
                    $_changeVerifyMethodsBtns.first().trigger("click");
                },
                ok: function() {
                    // 查找当前对话框中可见的表单，填写到verifyCodeInput
                    var visibleInputSelector = "input:visible";
                    var $_verifyCodeInput = $("input[name=verifyCode]");
                    if (!$(visibleInputSelector).val()) {
                        SQ.warn("请填写验证内容");
                    }
                    else {
                        $_verifyCodeInput.val($(".protect-block").find(visibleInputSelector).val());
                        SQ.post({
                            data: $_submitForm.serialize(),
                            url: $_submitForm.attr("action"),
                            success: function(json) {
                                protectDialog.close();

                                // 读取按钮上绑定的回调函数
                                var closeCallback = $_protectEnable.data("closeCallback");
                                if (closeCallback) {
                                    closeCallback(json);
                                }
                            }
                        });
                    }
                    return false;
                }
            });
        });
    });

});
var resize_sqpanel = function() {
    if ($(".sq-panel").length == 1) {
        var minHeight = $(window).height() - $(".sq-panel").offset().top - 22;
        if ($(".sq-panel").height() < minHeight) {
            $(".sq-panel").height(minHeight);
        }
    }
};
//将主体的高度设置全屏模式
$(function() {
    resize_sqpanel();
});