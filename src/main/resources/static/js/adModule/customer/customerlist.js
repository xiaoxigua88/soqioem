$(function() {

    //用户新增
    $("#btnAdd").click(function() {
        $.dialog({
            title: "员工资料新增",
            cancel: true,
            okVal: "保存",
            cancelVal: "关闭",
            content: template("editInformation", { statusList: jsData.statusList, roleList: jsData.roleList }),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#statusBtn input").click(function() {
                    $("#status").val($(this).data("status"));
                });
            },
            ok: function() {
                var $_form = $("#editForm");
                if (fieldEmptyValidate($_form)) {
                    SQ.post({
                        url: $_form.attr("action"),
                        data: $_form.serialize()
                    });
                }
                return false;
            }
        });
        return false;
    });

    //用户编辑
    $(".customer-edit").click(function() {
        var customerid = $(this).data("customerid");
        SQ.post({
            url: "/oemmanager/customer/customerinfo",
            data: {
            	customerid: customerid
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "员工资料编辑",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("editInformation", { customer: json.customer, statusList: jsData.statusList, roleList: json.roleList}),
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $("#statusBtn input").click(function() {
                            $("#status").val($(this).data("status"));
                        });
                    },
                    ok: function() {
                        var $_form = $("#editForm");
                        if (fieldEmptyValidate($_form)) {
                            SQ.post({
                                url: $_form.attr("action"),
                                data: $_form.serialize()
                            });
                        }
                        return false;
                    }
                });
            }
        });
        return false;
    });

    //重置密码
    $(".customer-pwd").click(function() {
        var customerid = $(this).data("customerid");
        SQ.tips.ask({
            title: "重置员工密码确认",
            content: "确认为员工重置密码？",
            ok: function() {
                SQ.post({
                    data: { action: "InitPwd", customerid: customerid },
                    url: "/oemmanager/customer/customerinitpwd"
                });
            }
        });
    });
});