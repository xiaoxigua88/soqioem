$(function() {
    //角色编辑
    $(".role-edit").click(function() {
        var roleid = $(this).data("roleid");
        SQ.post({
            url: "/oemmanager/customer/getRoleInfo",
            data: {
            	roleid: roleid
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: (typeof json.role != "undefined") ? "角色编辑" : "新增角色",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("editInformation", (typeof json.role != "undefined") ? { role: json.role, privilegeList: json.privilegeList, priEnumList: jsData.priEnumList} : { privilegeList: json.privilegeList, priEnumList: jsData.priEnumList }),
                    init: function() {
                        $('#privilegeList').mergeCell({
                            cols: [0]
                        });
                        $("#pri_chkall").change(function() {
                            var v = $(this).prop("checked");
                            $(".chk_defval").each(function() {
                                if (!$(this).prop("disabled")) {
                                    $(this).prop("checked", v).change();
                                }
                            });
                        });
                        $(".chk_defval").change(function() {
                            var $_self = $(this);
                            var v = 0;
                            $(".defval" + $_self.data("for") + ":checked").each(function() {
                                v += parseInt($(this).val());
                            });
                            $("#v_" + $_self.data("for")).val(v);
                        });
                        $(".chk_defval").change();
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
});