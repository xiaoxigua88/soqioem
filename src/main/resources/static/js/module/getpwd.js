$(function() {
    $(".show-captcha")[0].click();
    $.formValidator.initConfig({
        formID: "mainForm",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            $(".submit").attr("disabled", true);
            SQ.waiting("正在提交，请稍后……");
            $("#mainForm").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                    if (json.result) {
                        SQ.success(json.text, 1, function() {
                            window.location = 'getpwdmethod.aspx';
                        });
                    } else {
                        $(".show-captcha")[0].click();
                        SQ.warn(json.text, 2, function() {
                            $(".submit").attr("disabled", false);
                        });
                    }
                }
            });
        }
    });
    $("#username").formValidator().inputValidator({ min: 4, max: 20 }).regexValidator({ regExp: regexEnum.username });
    $("#verifyCode").formValidator().inputValidator({ min: 5, max: 5 }).regexValidator({ regExp: regexEnum.letnum });
});