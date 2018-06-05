$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll");
    //检查业务是否查询完毕
    SQ.biz.refreshSearchStatus(true, '?action=CheckRefresh');

    var idDataName = "taskid";

    var openPostDialog = function() {
        $.dialog({
            title: "站点索引挖掘创建",
            content: template("importInformation", { searchTypeList: jsData.searchTypeList }),
            cancel: true,
            okVal: "保存",
            cancelVal: "关闭",
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    var wordCount = $("#keyword").val().trimenter().split("\n").length;
                    var urlCount = $("#url").val().trimenter().split("\n").length;
                    var cost = wordCount * urlCount * jsData.price.DefaultGold;
                    SQ.tips.ask({
                        title: "站点索引挖掘创建确认",
                        content: "您当前提交了<b class='text-red'>" + wordCount + "</b>个关键词，<b class='text-red'>" + urlCount + "</b>个域名，预计需要消费<b class='text-red'>" + cost + "</b>个优币，是否继续？",
                        ok: function() {
                            SQ.post({
                                url: $_importForm.attr("action"),
                                data: $_importForm.serialize(),
                                success: function() {
                                    window.location.href = '?';
                                }
                            });
                        }
                    });
                }
                return false;
            },
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#allsnapeBtn input").click(function() {
                    $("#allsnape").val($(this).data("snape_type"));
                });
                $("#searchTypeBtn input").click(function() {
                    $("#searchType").val($(this).data("search_type"));
                });
            }
        });
    };
    //创建
    $(".btn-operate-add").click(function() {
        openPostDialog();
    });

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";
    $(".list-checkbox, #checkAll").change(function() {
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
            var taskCount = SQ.getTaskIdArray().length;
            var taskIds = SQ.getTaskIdArray().join(",");
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
                        data: { taskIds: taskIds },
                        url: url
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));

    // 修改配置
    $(".task-edit").click(function() {
        SQ.post({
            url: "?action=View",
            data: {
                taskId: $(this).data(idDataName)
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "站点索引挖掘修改",
                    content: template("importInformation", { searchTypeList: jsData.searchTypeList, model: json }),
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    ok: function() {
                        var $_importForm = $("#importForm");
                        if (fieldEmptyValidate($_importForm)) {
                            SQ.tips.ask({
                                content: "本次修改不会自动提交挖掘，如需挖掘请在保存后点击“重启”，是否保存修改？",
                                title: "站点索引挖掘修改确认",
                                ok: function() {
                                    SQ.post({
                                        url: $_importForm.attr("action"),
                                        data: $_importForm.serialize(),
                                        success: function() {
                                            window.location.reload();
                                        }
                                    });
                                }
                            });
                        }
                        return false;
                    },
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $("#allsnapeBtn input").click(function() {
                            $("#allsnape").val($(this).data("snape_type"));
                        });
                        $("#searchTypeBtn input").click(function() {
                            $("#searchType").val($(this).data("search_type"));
                        });
                    }
                });
            }
        });
        return false;
    });
    //重启挖掘
    $(".task-restart").click(function() {
        var taskId = $(this).data(idDataName);
        SQ.tips.ask({
            title: "站点索引挖掘重启查询确认",
            content: "重启挖掘需要再次消费，你是否确定要重启挖掘操作？",
            ok: function() {
                SQ.post({
                    url: "?action=Restart",
                    data: {
                        taskId: taskId
                    },
                    success: function(json) {
                        if (json.result == false) {
                            SQ.tips.error({ content: json.text });
                            return;
                        }
                        else {
                            window.window.location.reload();
                        }
                    }
                });
            }
        });
    });
    // 挖掘结果
    $(".task-result").click(function() {
        var taskId = $(this).data(idDataName);
        SQ.post({
            url: "?action=ResultList",
            data: {
                taskId: taskId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "站点索引挖掘结果",
                    content: template("resultInformation", json),
                    cancel: true,
                    okVal: "导出",
                    cancelVal: "关闭",
                    ok: function() {
                        SQ.post({
                            data: { taskId: taskId },
                            url: "?action=ResultExport"
                        });
                    }
                });

            }
        });
        return false;
    });
    // 消费记录
    $(".task-cost").click(function() {
        var taskId = $(this).data(idDataName);
        SQ.post({
            url: "?action=CostList",
            data: {
                taskId: taskId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "站点索引挖掘消费记录",
                    content: template("costInformation", json),
                    cancel: true,
                    cancelVal: "关闭"
                });

            }
        });
        return false;
    });
});