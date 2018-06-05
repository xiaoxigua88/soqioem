$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    $("#btnImport").click(function() {
        $.dialog({
            title: "关键词级别库批量提交",
            cancel: true,
            content: template("importInformation", { degreeList: jsData.degreeList }),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#degreeBtn input").click(function() {
                    $("#degree").val($(this).data("degree"));
                });
            },
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    var count = $("#keyword").val().trimenter().split("\n").length;
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + count + "</b>个关键词，是否继续？",
                        title: "关键词级别库批量提交确认",
                        ok: function() {
                            SQ.post({
                                url: $_importForm.attr("action"),
                                data: $_importForm.serialize()
                            });
                        }
                    });
                }
                // 阻止对话框消失
                return false;
            }
        });
    });

    $("#btnCheck").click(function() {
        var _box = $.dialog({
            title: "关键词级别检测批量提交",
            cancel: true,
            content: template("checkInformation", {}),
            ok: function() {
                var $_checkForm = $("#checkForm");
                if (fieldEmptyValidate($_checkForm)) {
                    var count = $("#keyword").val().trimenter().split("\n").length;
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + count + "</b>个关键词，是否继续？",
                        title: "关键词级别检测批量提交确认",
                        ok: function() {
                            SQ.post({
                                url: $_checkForm.attr("action"),
                                data: $_checkForm.serialize(),
                                isCoverSuccess: true,
                                success: function(json) {
                                    if (json.result == false) {
                                        SQ.tips.error({ content: json.text });
                                        return;
                                    }
                                    else if (json.length == 0) {
                                        _box.close();
                                        SQ.tips.success({ content: "您提交的关键词正常通过，无命中的词库！" });
                                        return;
                                    }
                                    else {
                                        _box.close();
                                        $.dialog({
                                            title: "关键词级别检测结果",
                                            cancel: true,
                                            content: template("checkresInformation", { lst: json })
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
                return false;
            }
        });
    });

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchExport = $("#batchExport");
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
    /* beforefun: 执行前调用方法*/
    var createBatchHandle = function(operateName, url, checkType, beforefun) {
        return function() {
            if (typeof beforefun != "undefined") beforefun();
            var taskCount = SQ.getTaskIdArray().length;
            var keyIds = SQ.getTaskIdArray().join(",");
            if (taskCount == 0 && checkType == 1) {
                SQ.tips.warn({
                    content: "你还没有选择任何记录！",
                    cancel: true,
                    cancelVal: "关闭"
                });
                return false;
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 个") + "关键词？",
                ok: function() {
                    SQ.post({
                        data: { keyIds: keyIds },
                        url: url
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));

    // 删除
    $(".keyword-del").click(function() {
        var keyId = $(this).data("keyid");
        SQ.tips.ask({
            title: "关键词删除确认",
            content: "确定删除关键词吗？",
            ok: function() {
                SQ.post({
                    data: { action: "Delete", keyIds: keyId },
                    url: ""
                });
            }
        });
    });

    //关键词级别批量修改
    $("#batchDegreeSet").click(function() {
        var taskCount = SQ.getTaskIdArray().length;
        var keyIds = SQ.getTaskIdArray().join(",");
        if (taskCount == 0 && checkType == 1) {
            SQ.tips.warn({
                content: "你还没有选择任何记录！",
                cancel: true,
                cancelVal: "关闭"
            });
            return false;
        }
        $.dialog({
            title: "关键词级别库批量修改",
            cancel: true,
            content: template("degreeInformation", { degreeList: jsData.degreeList }),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#degreeBtn input").click(function() {
                    $("#degree").val($(this).data("degree"));
                });
            },
            ok: function() {
                var $_degreeSetFormForm = $("#degreeSetForm");
                if ($("#degree").val() == "0") {
                    SQ.tips.warn({
                        content: "你还没有选级别！",
                        cancel: true,
                        cancelVal: "关闭"
                    });
                    return false;
                }
                SQ.tips.ask({
                    content: "您当前提交了<b class='text-red'>" + taskCount + "</b>个关键词的级别修改，是否继续？",
                    title: "关键词级别库批量修改确认",
                    ok: function() {
                        SQ.post({
                            url: $_degreeSetFormForm.attr("action"),
                            data: $_degreeSetFormForm.serialize() + "&keyIds=" + keyIds
                        });
                    }
                });
                return false;
            }
        });
    });
});