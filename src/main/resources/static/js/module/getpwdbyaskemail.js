$(function() {
    $.formValidator.initConfig({
        formID: "step3Form",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            $(".submit").attr("disabled", true);
            SQ.waiting("正在提交，请稍后……");
            $("#step3Form").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                    if (json.result) {
                        SQ.success(json.text, 1, function() {
                            window.location = 'getpwdreset.aspx';
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

    $("#answer").formValidator().inputValidator({ min: 1, max: 15 }).regexValidator({ regExp: "^\\S+$" });
    $("#verifyCode").formValidator().inputValidator({ min: 6, max: 6 });
});