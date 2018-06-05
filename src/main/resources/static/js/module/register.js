$(function() {
    //判断密码强度
    $("#password").keyup(function(e) {
        var strongRegex = new RegExp(regexEnum.pwd3, "g");
        var mediumRegex = new RegExp(regexEnum.pwd2, "g");
        var weakRegex = new RegExp(regexEnum.pwd1, "g");
        var $pwdLevelBar = $(".pwd-strength li");
        var $pwdLevelTxt = $(".show-pwd-level");
        if ($(this).val()) {
            if (false == weakRegex.test($(this).val())) {
                $pwdLevelBar.removeClass();
                $pwdLevelTxt.addClass("hide");
                $(".pwd-short").removeClass("hide");
            } else if (strongRegex.test($(this).val())) {
                $pwdLevelBar.removeClass();
                $pwdLevelBar.eq(0).addClass("levelbar-strong");
                $pwdLevelBar.eq(1).addClass("levelbar-strong");
                $pwdLevelBar.eq(2).addClass("levelbar-strong");
                $pwdLevelTxt.addClass("hide");
                $(".pwd-strong").removeClass("hide");
                return true;
            } else if (mediumRegex.test($(this).val())) {
                $pwdLevelBar.removeClass();
                $pwdLevelBar.eq(0).addClass("levelbar-medium");
                $pwdLevelBar.eq(1).addClass("levelbar-medium");
                $pwdLevelTxt.addClass("hide");
                $(".pwd-medium").removeClass("hide");
            } else {
                $pwdLevelBar.removeClass();
                $pwdLevelBar.eq(0).addClass("levelbar-weak");
                $pwdLevelTxt.addClass("hide");
                $(".pwd-weak").removeClass("hide");
            }
            return false;
        } else {
            $pwdLevelBar.removeClass();
            $pwdLevelTxt.addClass("hide");
            $(".pwd-null").removeClass("hide");
        }
    });

    $("#agreementCheckbox").change(function() {
        var checkboxTrigger = $("#checkboxTrigger");
        var submitBtn = $("#submitBtn");
        if ($(this).prop("checked")) {
            checkboxTrigger.addClass("checkbox-icon-checked");
            submitBtn.addClass("reg-submit").attr("disabled", false);
        } else {
            checkboxTrigger.removeClass("checkbox-icon-checked");
            submitBtn.removeClass("reg-submit").attr("disabled", true);
        }
    });

    //表单验证
    var config = {
        passwordMin: 8,
        passwordMax: 20
    };
    $.formValidator.initConfig({
        formID: "register",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            $(".reg-submit").attr("disabled", true);
            SQ.waiting("正在提交，请稍后……");
            $("#register").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                    if (json.result) {
                        SQ.success(json.text, 2, function() { window.location = 'profile.aspx'; });
                    } else {
                        SQ.warn(json.text, 2, function() {
                            $(".reg-submit").attr("disabled", false);
                        });
                    }
                },
                error: function() {
                    SQ.hideWaiting();
                    SQ.showBusy();
                }
            });
        },
        ajaxPrompt: '有数据正在异步验证，请稍等...'
    });

    $("#mobile").formValidator({
        onShow: "手机号码可用于登录、激活账号、密码找回",
        onFocus: "手机号码可用于登录、激活账号、密码找回",
        onCorrect: "填写正确"
    }).inputValidator({ min: 11, max: 11, onError: "手机号码填写错误" }).regexValidator({
        regExp: regexEnum.mobile,
        onError: "手机号码格式不正确"
    }).ajaxValidator({
        type: "POST",
        dataType: "json",
        async: true,
        //手机号码是否可用验证接口
        url: "ajax.aspx?action=CheckMobile",
        success: function(data) {
            return data.result;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            SQ.showBusy();
        },
        onError: function() {
            return "该手机号码不可用";
        },
        onWait: "请稍候..."
    });

    $("#password").formValidator({
        onShow: "密码由" + config.passwordMin + "-" + config.passwordMax + "个字符组成，至少包含大写字母、小写字母、数字任意两种组合",
        onFocus: "密码由" + config.passwordMin + "-" + config.passwordMax + "个字符组成，至少包含大写字母、小写字母、数字任意两种组合",
        onCorrect: "密码填写正确"
    }).inputValidator({ min: config.passwordMin, max: config.passwordMax, onError: "密码填写错误" }).regexValidator({
        regExp: regexEnum.pwd2,
        onError: "密码强度不够，至少包含大写字母、小写字母、数字任意两种组合"
    });

    $("#password2").formValidator({ onShow: "请再次输入密码", onFocus: "请再次输入密码", onCorrect: "填写正确" }).inputValidator({
        min: 1,
        onError: "请再次输入密码"
    }).compareValidator({ desID: "password", operateor: "=", onError: "两次输入的密码不一致" });

    $("#verifyCode").formValidator({
        onShow: "请输入收到的6位验证码",
        onFocus: "请输入收到的6位验证码",
        onCorrect: "验证码填写正确"
    }).inputValidator({
        min: 6,
        max: 6,
        onError: "验证码填写错误"
    }).ajaxValidator({
        type: "POST",
        dataType: "json",
        async: true,
        //手机验证码接口
        url: "ajax.aspx?action=CheckMobileCode",
        success: function(data) {
            return data.result;
        },
        buttons: $("#button"),
        error: function(jqXHR, textStatus, errorThrown) { alert("服务器没有返回数据！"); },
        onError: "验证码错误",
        onWait: "验证码校验中，请稍候..."
    });
});

