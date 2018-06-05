$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    var idDataName = "taskid";

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
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
            var data = { taskIds: taskIds };
            if (taskCount == 0 || checkType == 3) {
                data = $("#searchForm").serialize();
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 条") + "记录？",
                ok: function() {
                    SQ.post({
                        data: { taskIds: taskIds },
                        url: url + (location.search.length > 0 ? "&" + location.search.substr(1) : "")
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));

    // 配置查看
    $(".task-view").click(function() {
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
                    title: "关键词和域名配置",
                    content: template("viewInformation", { searchTypeList: jsData.searchTypeList, model: json }),
                    cancel: true,
                    cancelVal: "关闭"
                });
            }
        });
        return false;
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
                    title: "挖掘结果",
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
                    title: "消费记录",
                    content: template("costInformation", json),
                    cancel: true,
                    cancelVal: "关闭"
                });

            }
        });
        return false;
    });
});