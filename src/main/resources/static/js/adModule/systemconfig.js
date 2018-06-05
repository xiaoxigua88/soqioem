$(function() {
    $("#btnSave").click(function() {
        var $_form = $("#systemConfigForm");
        if (fieldEmptyValidate($_form)) {
            SQ.tips.ask({
                content: "您当前提交了修改系统参数的请求，是否继续？",
                title: "系统参数修改提交确认",
                ok: function() {
                    SQ.post({
                        url: $_form.attr("action"),
                        data: $_form.serialize()
                    });
                }
            });
        }
        return false;
    });
});