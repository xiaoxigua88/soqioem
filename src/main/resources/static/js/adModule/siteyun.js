$(function() {
    //任务单个停止
    $(".web-pay").click(function() {
        var _yunId = $(this).data("yunid");
        var _payCount = parseInt($(this).data("paycount"));
        var _createPrice = parseInt($(this).data("createprice"));
        var _renewalPrice = parseInt($(this).data("renewalprice"));
        var _html = "";
        if (_payCount == 0) {
            _html = "该站点首年费用为<span class='text-red'>" + _createPrice + "元</span>，是否支付？";
        }
        else {
            _html = "该网站一年的托管费为<span class='text-red'>" + _renewalPrice + "元</span>，是否续费一年？";
        }
        SQ.tips.ask({
            title: "云建站续费确认",
            content: _html,
            ok: function() {
                SQ.post({
                    data: { action: "Pay", yunId: _yunId },
                    url: ""
                });
            }
        });
        return false;
    });
    
    //任务单个停止
    $(".web-edit").click(function() {
        var _yunId = $(this).data("yunid");
        SQ.post({
            url: "?action=GetInfo",
            data: {
                yunId: _yunId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "云建站编辑",
                    cancel: true,
                    content: template("editInformation", { model: json.model }),
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $("#fromapiBtn input").click(function() {
                            if ($(this).data("fromapi")) {
                                $("#readOnlyBtn input").prop("disabled", false);
                            }
                            else {
                                $($("#readOnlyBtn input")[1]).trigger("click");
                                $("#readOnlyBtn input").prop("disabled", true);
                            }
                            $("#fromapi").val($(this).data("fromapi"));
                        });
                        $("#readOnlyBtn input").click(function() {
                            $("#readOnly").val($(this).data("readonly"));
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
            }
        });
        return false;
    });

/*
    $(document).on('click','a.btn-operate-add',function(){
            $.dialog({
                title: "云建站手工添加",
                cancel: true,
                content: template("editInformation", {}),
                init: function() {
                    // 初始化sq-btn-group
                    SQ.component.initTabs();
                    $("#fromapiBtn input").click(function() {
                        if ($(this).data("fromapi")) {
                            $("#readOnlyBtn input").prop("disabled", false);
                        }
                        else {
                            $($("#readOnlyBtn input")[1]).trigger("click");
                            $("#readOnlyBtn input").prop("disabled", true);
                        }
                        $("#fromapi").val($(this).data("fromapi"));
                    });
                    $("#readOnlyBtn input").click(function() {
                        $("#readOnly").val($(this).data("readonly"));
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
    })
*/

    $(".btn-operate-add").click(function() {
        $.dialog({
            title: "云建站手工添加",
            cancel: true,
            content: template("editInformation", {}),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#fromapiBtn input").click(function() {
                    if ($(this).data("fromapi")) {
                        $("#readOnlyBtn input").prop("disabled", false);
                    }
                    else {
                        $($("#readOnlyBtn input")[1]).trigger("click");
                        $("#readOnlyBtn input").prop("disabled", true);
                    }
                    $("#fromapi").val($(this).data("fromapi"));
                });
                $("#readOnlyBtn input").click(function() {
                    $("#readOnly").val($(this).data("readonly"));
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