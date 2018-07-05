$(function() {
    $(".show-captcha")[0].click();
    //表单验证
    $.formValidator.initConfig({
        formID: "loginForm",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            SQ.waiting("正在登录，请稍候...");
            $("#loginForm").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    if (data.code=='0') {
                        SQ.success(data.msg);
                        var jumpDelay = 2000;
                        setTimeout(function() {
                            window.location.href = "/oemmanager/main";
                        }, jumpDelay);
                    } else {
                        SQ.warn(data.msg);
                        if (data.msg) {
                            $("#" + data.name).val("");
                            $("#" + data.name).focus();
                        }
                        $(".show-captcha")[0].click();
                        $("#verifyCode").val("");
                    }
                },
                error: function() {
                    SQ.hideWaiting();
                    SQ.showBusy();
                }
            });
        }
    });
    $("#userName").formValidator().inputValidator({ min: 4, max: 20 }).regexValidator({ regExp: regexEnum.username });
    $("#password").formValidator().inputValidator({ min: 8, max: 20 }).regexValidator({ regExp: regexEnum.pwd1 });
    //$("#verifyCode").formValidator().inputValidator({ min: 5, max: 5 }).regexValidator({ regExp: regexEnum.letnum });

    //输入框focus样式
    $("#loginForm").on("focus", ".input-focus-handle", function() {
        $(this).parent().addClass("input-outer--focus");
    }).on("blur", ".input-focus-handle", function() {
        $(this).parent().removeClass("input-outer--focus");
    });

    //回车提交
    $('#verifycode').keydown(function(e) {
        if (e.keyCode == 13) {
            $_loginSubmit.click();
            return false;
        }
    });
});