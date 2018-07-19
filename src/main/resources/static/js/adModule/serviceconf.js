$(function() {
	SQ.component.initTabs();
	$(".switchBtn input").click(function() {
        var val = $(this).data("status");
        var $_inputStatus = $(this).parent().find("input[name=status]");
        $_inputStatus.val(val);
    });
    // 服务设置提交
    $("#btnSave").click(function() {
        var $_form = $("#serviceConfigForm");
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