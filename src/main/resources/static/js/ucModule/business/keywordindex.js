$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll");
    //检查业务是否查询完毕
    SQ.biz.refreshSearchStatus(true, '?action=CheckRefresh');

    var openPostDialog = function() {
        $.dialog({
            title: "关键词指数查询批量提交",
            cancel: true,
            content: template("importInformation", {}),
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    var count = $("#keyword").val().trimenter().split("\n").length;
                    var cost = count * jsData.price.DefaultGold;
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + count + "</b>个关键词，预计需要消费<b class='text-red'>" + cost + "</b>个优币，是否继续？",
                        title: "关键词指数查询提交确认",
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
    };

    $(".btn-operate-add").click(function() {
        openPostDialog();
    });

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchExport = $("#batchExport");
    var $_batchClear = $("#batchClear");
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
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));
    // 清空
    $_batchClear.click(createBatchHandle("清空", "?action=Clear", 3));

});