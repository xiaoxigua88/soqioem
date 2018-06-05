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
    var $_batchDelete = $("#batchDelete");
    var $_batchExport = $("#batchExport");
    var $_batchStart = $("#batchStart");
    var $_batchStop = $("#batchStop");
    var $_batchPay = $("#batchPay");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";

    //$_mainList.mergeCell({ cols: [2] });

    $(".list-checkbox, #checkAll, #checkAllBottom").change(function() {
        var canDelete = 0;
        var canStart = 0;
        var canPay = 0;
        var canStop = 0;
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") != 4) {
                canDelete++;
            }
            if ($(this).data("status") == 5) {
                canStart++;
            }
            if ($(this).data("status") == 3) {
                canPay++;
            }
            if ($(this).data("canstop") == 1) {
                canStop++;
            }
        });
        if (canDelete <= 0) {
            $_batchDelete.attr("disabled", true);
        }
        else {
            $_batchDelete.attr("disabled", false);
        };
        if (canStart <= 0) {
            $_batchStart.attr("disabled", true);
        }
        else {
            $_batchStart.attr("disabled", false);
        };
        if (canStop <= 0) {
            $_batchStop.attr("disabled", true);
        }
        else {
            $_batchStop.attr("disabled", false);
        };
        if (canPay <= 0) {
            $_batchPay.attr("disabled", true);
        }
        else {
            $_batchPay.attr("disabled", false);
        };
    });

    // 批量操作按钮 的点击事件生成方法
    /* operateName：操作名称 */
    /*  url：响应地址 */
    /*  checkType：选中类型 1,必须选择；2,对选择有效未选择响应所有,3,不关注是否选择 */
    /* beforefun: 执行前调用方法*/
    var createBatchHandle = function(operateName, url, checkType, beforefun) {
        return function() {
            if (typeof beforefun != "undefined") beforefun();
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
                content: "确认<span class='text-red'>" + operateName + "</span>" + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 个") + "关键词？",
                ok: function() {
                    SQ.post({
                        data: { yunId: jsData.site.YunId, taskIds: taskIds },
                        url: url
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") == 4) {
                $(this).attr("checked", false).trigger("change");
            }
        });
    }));
    // 批量启动
    $_batchStart.click(createBatchHandle("启动", "?action=Start", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") != 5) {
                $(this).attr("checked", false).trigger("change");
            }
        });
    }));
    // 批量停止
    $_batchStop.click(createBatchHandle("停止", "?action=Stop", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("canstop") == 0) {
                $(this).attr("checked", false).trigger("change");
            }
        });
    }));
    // 批量购买
    $_batchPay.click(createBatchHandle("购买", "?action=Pay", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") != 3) {
                $(this).attr("checked", false).trigger("change");
            }
        });
    }));
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));

    //任务信息查看
    $(".task-view").click(function() {
        var taskId = $(this).data("taskid");
        SQ.post({
            url: "?action=View",
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
                    title: "任务信息概况",
                    cancel: true,
                    cancelVal: "关闭",
                    content: template("taskViewTemplate", { priceList: json.PriceList, balanceList: json.BalanceList })
                });
            }
        });
        return false;
    });

    //任务单个启动
    $(".task-start").click(function() {
        var taskId = $(this).data("taskid");
        SQ.tips.ask({
            title: "关键词SEO优化启动确认",
            content: "系统将冻结<b class='text-red'>" + $("#freezeDay").val() + "</b>天达标费用，确认<span class='text-red'>启动</span>该关键词的SEO优化吗？",
            ok: function() {
                SQ.post({
                    data: { action: "Start", yunId: jsData.site.YunId, taskIds: taskId },
                    url: ""
                });
            }
        });
    });

    //任务单个停止
    $(".task-stop").click(function() {
        var taskId = $(this).data("taskid");
        SQ.tips.ask({
            title: "关键词SEO优化停止确认",
            content: "确认<span class='text-red'>停止</span>该关键词的SEO优化吗？",
            ok: function() {
                SQ.post({
                    data: { action: "Stop", yunId: jsData.site.YunId, taskIds: taskId },
                    url: ""
                });
            }
        });
    });

    $(".btn-add-diff").click(function() {
        $.dialog({
            title: "不同价关键词批量提交",
            cancel: true,
            content: template("importDiffInformation", { searchTypeList: jsData.searchTypeList, site: jsData.site }),
            init: function() {
                $(".chk_st").change(function() {
                    var _e = $(this);
                    if (_e.prop("checked")) {
                        $(".sq-torank-" + _e.val()).prop("disabled", false);
                        $(".sq-torank-" + _e.val()).addClass("necessary");

                        $(".sq-price-" + _e.val()).prop("disabled", false);
                        $(".sq-price-" + _e.val()).addClass("necessary");
                    }
                    else {
                        $(".sq-torank-" + _e.val()).prop("disabled", true);
                        $(".sq-torank-" + _e.val()).removeClass("necessary");

                        $(".sq-price-" + _e.val()).prop("disabled", true);
                        $(".sq-price-" + _e.val()).removeClass("necessary");
                    }
                });
            },
            ok: function() {
                if ($(".chk_st:checked").length == 0) {
                    SQ.tips.warn({
                        content: "你至少选择一个搜索引擎！",
                        cancel: true,
                        cancelVal: "关闭"
                    });
                    return false;
                }
                var $_form = $("#importForm");
                if (fieldEmptyValidate($_form)) {
                    SQ.post({
                        url: $_form.attr("action"),
                        data: $_form.serialize(),
                        successResultFalse: function(json) {
                            if (json.reload) {
                                window.location.reload();
                            }
                            else {
                                validateShowError(json.name, json.text, $_form);
                            }
                        }
                    });
                }
                // 阻止对话框消失
                return false;
            }
        });
    });

    $(".btn-add-same").click(function() {
        $.dialog({
            title: "相同价关键词批量提交",
            cancel: true,
            content: template("importSameInformation", { searchTypeList: jsData.searchTypeList, site: jsData.site }),
            init: function() {
                $(".chk_st").change(function() {
                    var _e = $(this);
                    if (_e.prop("checked")) {
                        $("input[name='torank1_" + _e.val() + "']").prop("disabled", false);
                        $("input[name='torank2_" + _e.val() + "']").prop("disabled", false);
                        $("input[name='torank1_" + _e.val() + "']").addClass("necessary");

                        $("input[name='price1_" + _e.val() + "']").prop("disabled", false);
                        $("input[name='price2_" + _e.val() + "']").prop("disabled", false);
                        $("input[name='price1_" + _e.val() + "']").addClass("necessary");
                    }
                    else {
                        $("input[name='torank1_" + _e.val() + "']").prop("disabled", true);
                        $("input[name='torank2_" + _e.val() + "']").prop("disabled", true);
                        $("input[name='torank1_" + _e.val() + "']").removeClass("necessary");

                        $("input[name='price1_" + _e.val() + "']").prop("disabled", true);
                        $("input[name='price2_" + _e.val() + "']").prop("disabled", true);
                        $("input[name='price1_" + _e.val() + "']").removeClass("necessary");
                    }
                });
            },
            ok: function() {
                var $_form = $("#importForm");
                if (fieldEmptyValidate($_form)) {
                    SQ.post({
                        url: $_form.attr("action"),
                        data: $_form.serialize(),
                        successResultFalse: function(json) {
                            if (json.reload) {
                                window.location.reload();
                            }
                            else {
                                validateShowError(json.name, json.text, $_form);
                            }
                        }
                    });
                }
                // 阻止对话框消失
                return false;
            }
        });
    });
});