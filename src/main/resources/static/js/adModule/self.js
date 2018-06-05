$(function() {
    // 基本信息页面，保存基本信息
    $("#savingInfo").click(function() {
        var $_form = $("#basicInfoForm");
        var actionUrl = $_form.attr("action");
        if (!fieldEmptyValidate($_form)) {
            return false;
        }
        
        $_form.find(".protection-enabled").trigger("click");
        return false;
    });

    // 保存密码
    $("#savePwd").click(function() {
        var $_form = $("#changePassword");
        var actionUrl = $_form.attr("action");
        if (!fieldEmptyValidate($_form)) {
            return false;
        }

        SQ.post({
            url: actionUrl,
            data: $_form.serialize()
        });
        return false;
    });
});