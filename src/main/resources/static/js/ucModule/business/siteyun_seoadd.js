$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll");

    var reload = function() {
        window.location.reload();
    };

    var importDialog = function() {
        $.dialog({
            title: "关键词购买批量提交",
            cancel: true,
            content: template("importInformation", { searchTypeList: jsData.searchTypeList, site: jsData.site }),
            init: function() {
            },
            ok: function() {
                var $_form = $("#importForm");
                if (fieldEmptyValidate($_form)) {
                    captchaCheck($_form.attr("action"), $_form.serialize(), function() {
                        setTimeout(reload, 5000);
                    });
                }
                // 阻止对话框消失
                return false;
            }
        });
    };

    $(".btn-operate-add").click(function() {
        importDialog();
    });

    // 批量操作按钮解除禁用
    var $_payBtn = $("#payBtn");
    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchClear = $("#batchClear");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";

    //$_mainList.mergeCell({ cols: [2] });

    $(".list-checkbox, #checkAll").change(function() {
        if ($_mainList.find(listCheckedSelector).length > 0) {
            $_batchButtons.attr("disabled", false);
        }
        else {
            $_batchButtons.attr("disabled", true);
        }
        computePay();
    });

    var computePay = function() {
        var amount = 0;
        var c = 0;
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") == 3) {
                amount += parseFloat($(this).data("price"));
                c++;
            }
        });
        $("#keyword_count").text(c + "个");
        $("#amount_perday").text(amount.toFixed(2));
        $("#amount_total").text((amount * 90).toFixed(2));
        if (amount <= 0) {
            $_payBtn.attr("disabled", true);
        }
        else {
            $_payBtn.attr("disabled", false);
        }
    };

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
                content: "确认" + operateName + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 个") + "优化记录？",
                ok: function() {
                    SQ.post({
                        data: { taskIds: taskIds, yunId: jsData.site.YunId },
                        url: url
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));
    // 清空
    $_batchClear.click(createBatchHandle("清空", "?action=Clear", 3));

    //立即购买
    $_payBtn.click(createBatchHandle("购买", "?action=Pay", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") != 3) {
                $(this).attr("checked", false);
            }
        });
    }));

    var refreshSeoCheckStatus = function(taskId) {
        $.get({
            url: "?action=GetSeoStatus&taskId=" + taskId,
            dataType: "json",
            success: function(json) {
                if (json.status == 1) {
                    setTimeout(refreshSeoCheckStatus, 3000, taskId);
                }
                else if (json.status == 2) {
                    $("#td_rank_" + taskId).html("<span class=\"sq-state-icon state-icon-fail\"></span>");
                    $("#td_costby_" + taskId).html("<span class=\"sq-state-icon state-icon-fail\"></span>");
                    $("#td_price_" + taskId).html("<span class=\"sq-state-icon state-icon-fail\"></span>");
                    $("#td_status_" + taskId).html("<span class=\"sq-state-icon state-icon-fail\"></span>");
                }
                else if (json.status == 3) {
                    $("#td_costby_" + taskId).html("前" + json.toRank + "名按天");
                    $("#td_price_" + taskId).html("￥" + json.price);
                    $("#td_status_" + taskId).html("<span class=\"sq-state-icon sq-state-text state-icon-waiting\">待付款</span>");
                }
                else {
                    $("#tr_" + taskId).remove();
                }
                if (json.rankLast > 50) {
                    $("#td_rank_" + taskId).html("<span class=\"text-muted\">五页外</span>");
                }
                else if (json.rankLast > 0) {
                    $("#td_rank_" + taskId).html(json.rankLast);
                }
                $("#chk_" + taskId).data("status", json.status);
                $("#chk_" + taskId).data("price", json.price);
            },
            error: function() {
                setTimeout(refreshSeoCheckStatus, 3000, taskId);
            }
        });
    };
    $(".list-checkbox").each(function() {
        var _status = $(this).data("status");
        var _taskId = $(this).val();
        if (_status == "1") {
            setTimeout(refreshSeoCheckStatus, 3000, _taskId);
        }
    });

    if (jsData.lst.length == 0) {
        importDialog();
    }
    $(".sq-panel-body").width($(".sq-panel").width() - 260);
});