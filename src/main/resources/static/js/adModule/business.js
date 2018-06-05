$(function() {

    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    var regByDomainDlg = function() {
        $.dialog({
            title: "过期域名预订操作批量提交",
            cancel: true,
            content: template("domainDelRegInformation", { }),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#needRegBtn input").click(function() {
                    $("#needReg").val($(this).data("needreg"));
                });
            },
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    var count = $("#domain").val().trimenter().split("\n").length;
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + count + "</b>个域名，是否继续？",
                        title: "过期域名预订操作批量提交确认",
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
        return false;
    };

    $("#btnRegByDomain").click(function() {
        return regByDomainDlg();
    });

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchRestart = $("#batchRestart");
    var $_batchReg = $("#batchReg");
    var $_batchUnReg = $("#batchUnReg");
    var $_batchExport = $("#batchExport");
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
                content: "确认 【" + operateName + "】  " + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 条") + "记录？",
                ok: function() {
                    SQ.post({
                        data: { taskIds: taskIds },
                        url: url + (location.search.length > 0 ? "&" + location.search.substr(1) : "")
                    });
                }
            });
            return false;
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));
    // 批量重查
    $_batchRestart.click(createBatchHandle("重查", "?action=Restart", 1));
    // 批量预订
    $_batchReg.click(createBatchHandle("预订", "?action=Reg", 1));
    // 取消预订
    $_batchUnReg.click(createBatchHandle("取消预订", "?action=UnReg", 1));
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));
});