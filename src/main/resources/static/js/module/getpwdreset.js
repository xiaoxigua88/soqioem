$(function() {
    $.formValidator.initConfig({
        formID: "step4Form",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            $(".submit").attr("disabled", true);
            SQ.waiting("正在提交，请稍后……");
            $("#step4Form").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                    if (json.result) {
                        SQ.success(json.text, 2, function() {
                            window.location = 'login.aspx';
                        });
                    } else {
                        SQ.warn(json.text, 2, function() {
                            $(".submit").attr("disabled", false);
                        });
                    }
                }
            });
        }
    });

    //表单验证
    var config = {
        passwordMin: 6,
        passwordMax: 20
    };

    $("#password").formValidator().inputValidator({ min: config.passwordMin, max: config.passwordMax });
    $("#password2").formValidator().inputValidator({ min: config.passwordMin, max: config.passwordMax }).compareValidator({ desID: "password", operateor: "=" });
});

