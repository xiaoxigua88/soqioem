$(function() {
    var w = jQuery(".keywordlist").width();
    var c = Math.floor(w / 455);
    if (c > 0) {
        jQuery(".keywordlist li").width(Math.floor(w / c));
    };

    $(".sq-tab").click(function() {
        $(".sq-tab").removeClass("sq-tab--current");
        $(this).addClass("sq-tab--current");
        $(".oc-overview").removeClass("hide");
        $(".oc-overview").hide();
        $("#" + $(this).attr("control")).show();
        $(".sq-panel").height($(".sq-panel-body").height() + 80);
        resize_sqpanel();
    });

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
    var $_batchStop = $("#batchStop");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";
    $(".list-checkbox, #checkAll, #checkAllBottom").change(function() {
        var canStop = 0;
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") != 2) {
                canStop++;
            }
        });
        if (canStop <= 0) {
            $_batchStop.attr("disabled", true);
        }
        else {
            $_batchStop.attr("disabled", false);
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
            // 获取选中的任务的数量 和 多个id拼接的字符串
            var orderCount = SQ.getTaskIdArray().length;
            var orderIds = SQ.getTaskIdArray().join(",");
            if (orderCount == 0 && checkType == 1) {
                SQ.tips.warn({
                    content: "你还没有选择任何记录！",
                    cancel: true,
                    cancelVal: "关闭"
                });
                return false;
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (orderCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + orderCount + "</b> 个") + "订单？",
                ok: function() {
                    SQ.post({
                        data: { orderIds: orderIds },
                        url: url
                    });
                }
            });
        };
    };
    // 批量停止
    $_batchStop.click(createBatchHandle("停止", "?action=Stop", 1, function() {
        $_mainList.find(listCheckedSelector).each(function() {
            if ($(this).data("status") == 2) {
                $(this).attr("checked", false);
            }
        });
    }));

    $(".btn-config").click(function() {
        SQ.post({
            url: "?action=GetConfig",
            data: {
                orderId: $(this).data("orderid")
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "云站通",
                    content: template("configTemplate", { model: json }),
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    ok: function() {
                        var $_configForm = $("#configForm");
                        if (fieldEmptyValidate($_configForm)) {
                            SQ.post({
                                url: $_configForm.attr("action"),
                                data: $_configForm.serialize()
                            });
                        }
                        return false;
                    },
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $("#webdirectBtn input").click(function() {
                            var $_locationBlock = $(".config-location-group");
                            var $_contactBlock = $(".config-contact-group");
                            var _v = $(this).data("webdirect_type");
                            $("#webdirect").val(_v);
                            if (_v == 3) {
                                $_contactBlock.addClass("hide");
                                $_locationBlock.removeClass("hide");
                                $("#cfg_location").focus();
                            }
                            else {
                                $_locationBlock.addClass("hide");
                                $_contactBlock.removeClass("hide");
                                $("#cfg_company").focus();
                            }
                        });
                    }
                });
            }
        });
        return false;
    });

    $(".btn-stoporder").click(function() {
        var orderId = $(this).data("orderid");
        SQ.tips.ask({
            title: "订单停止确认",
            content: "订单停止后网站将自动进入云站通市场，其他用户可自由租赁，您是否继续？",
            ok: function() {
                SQ.post({
                    url: "orderinfo.aspx?action=Stop",
                    data: {
                        orderIds: orderId
                    }
                });
            }
        });
        return false;
    });

    //关键词消费记录查看
    $(".keyword-view").click(function() {
        var keyword = $(this).data("keyword");
        var orderId = $(this).data("orderid");
        SQ.post({
            url: "?action=KeywordView",
            data: {
                keyword: keyword,
                orderId: orderId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "关键词消费记录",
                    cancel: true,
                    cancelVal: "关闭",
                    content: template("keywordViewTpl", { balanceList: json.balanceList })
                });
            }
        });
        return false;
    });
});