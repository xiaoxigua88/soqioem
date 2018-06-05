var initSearchOnceBtn = function() {
    if ($("#searchOnceBtn input").length > 0) {
        $("#searchOnceBtn input").click(function() {
            $("#searchOnce").val($(this).data("search_once"));
        });
    }
};
var checkTimeSet = function() {
    if ($("input[type=checkbox][name='timeset']:checked").length == 0) {
        SQ.tips.warn({
            content: "请至少选择一个监控时间点！",
            cancel: true,
            cancelVal: "关闭"
        });
        return false;
    }
    return true;
};

$(function() {
    //批量设置
    $("#batchTimeSet").click(function() {
        // 获取选中的任务的数量 和 多个id拼接的字符串
        var taskCount = SQ.getTaskIdArray().length;
        var taskIds = SQ.getTaskIdArray().join(",");
        if (taskCount == 0) {
            SQ.tips.warn({
                content: "你还没有选择任何记录！",
                cancel: true,
                cancelVal: "关闭"
            });
            return false;
        }
        $.dialog({
            title: "监控时间设置",
            cancel: true,
            content: template("timeSetInformation", {}),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#searchOnceBtn input").click(function() {
                    $("#searchOnce").val($(this).data("search_once"));
                });
            },
            ok: function() {
                var $_importForm = $("#timeSetForm");
                if (fieldEmptyValidate($_importForm)) {
                    if (!checkTimeSet()) {
                        return false;
                    }
                    var cost = taskCount * jsData.price.DefaultGold;
                    SQ.tips.ask({
                        content: "您当前提交了<b class='text-red'>" + taskCount + "</b>个任务，每次查询预计消费<b class='text-red'>" + cost + "</b>个优币，是否继续？",
                        title: "监控时间设置提交确认",
                        ok: function() {
                            SQ.post({
                                url: $_importForm.attr("action"),
                                data: $_importForm.serialize() + "&taskIds=" + taskIds
                            });
                        }
                    });
                }
                // 阻止对话框消失
                return false;
            }
        });
    });
});