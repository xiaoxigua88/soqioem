$(function() {

    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchExport = $("#batchExport");
    var $_batchPaid = $("#batchPaid");
    var $_batchFree = $("#batchFree");
    var $_batchDelete = $("#batchDelete");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";
    $(".list-checkbox, #checkAll, #checkAllBottom").change(function() {
        if ($_mainList.find(listCheckedSelector).length > 0) {
            $_batchButtons.attr("disabled", false);
        }
        else {
            $_batchButtons.attr("disabled", true);
        }
    });
    // 批量操作按钮 的点击事件生成方法
    /* operateName：操作名称 */
    /*  url：响应地址 */
    /*  checkType：选中类型 1,必须选择；2,对选择有效未选择响应所有,3,不关注是否选择 */
    var createBatchHandle = function(operateName, url, checkType) {
        return function() {
            // 获取选中的任务的数量 和 多个id拼接的字符串
            var keyCount = SQ.getTaskIdArray().length;
            var keyIds = SQ.getTaskIdArray().join(",");
            if (keyCount == 0 && checkType == 1) {
                SQ.tips.warn({
                    content: "你还没有选择任何记录！",
                    cancel: true,
                    cancelVal: "关闭"
                });
                return false;
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (keyCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + keyCount + "</b> 个") + "关键词？",
                ok: function() {
                    SQ.post({
                        data: { keyIds: keyIds, webId: $("#webId").val() },
                        url: url
                    });
                }
            });
        };
    };
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));
    // 批量设置付费
    $_batchPaid.click(createBatchHandle("设置付费", "?action=Paid", 1));
    // 批量设置免费
    $_batchFree.click(createBatchHandle("设置免费", "?action=Free", 1));
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));

    var keywordImportDialog = function() {
        $.dialog({
            title: "优帮云站通-关键词批量提交",
            cancel: true,
            content: template("importTemplate", {}),
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    if ($("#keyword_paid").val().trimenter() == "" && $("#keyword_free").val().trimenter() == "") {
                        return false;
                    }
                    var paidCount = 0;
                    var freeCount = 0;
                    if ($("#keyword_paid").val().trimenter() != "") {
                        paidCount = $("#keyword_paid").val().trimenter().split("\n").length;
                    }
                    if ($("#keyword_free").val().trimenter() != "") {
                        freeCount = $("#keyword_free").val().trimenter().split("\n").length;
                    }
                    if (paidCount == 0 && freeCount == 0) {
                        return false;
                    }
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + paidCount + "</b>个收费关键词，<b class='text-red'>" + freeCount + "</b>个免费关键词，是否继续？",
                        title: "关键词批量提交确认",
                        ok: function() {
                            SQ.post({
                                url: $_importForm.attr("action"),
                                data: $_importForm.serialize()
                            });
                        }
                    });
                }
                return false;
            }
        });
    };

    $(".btn-operate-add").click(function() {
        keywordImportDialog();
    });

    // 修改
    $(".keyword-edit").click(function() {
        var keyId = $(this).data("keyid");
        SQ.post({
            url: "?action=View",
            data: {
                keyId: keyId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "关键词修改",
                    content: template("editTemplate", json),
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    ok: function() {
                        var $_saveForm = $("#saveForm");
                        if (fieldEmptyValidate($_saveForm)) {
                            SQ.post({
                                url: $_saveForm.attr("action"),
                                data: $_saveForm.serialize()
                            });
                        };
                    },
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $("#giveawayBtn input").click(function() {
                            $("#giveaway").val($(this).data("giveaway_type"));
                        });
                    }
                });

            }
        });
        return false;
    });

    // 删除
    $(".keyword-del").click(function() {
        var keyId = $(this).data("keyid");
        SQ.tips.ask({
            title: "关键词确认",
            content: "确定删除关键词吗？",
            ok: function() {
                SQ.post({
                    data: { action: "Delete", keyIds: keyId },
                    url: ""
                });
            }
        });
    });
});