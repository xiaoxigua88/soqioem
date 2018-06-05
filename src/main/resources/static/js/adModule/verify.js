$(function() {
    //设置资料状态
    function setInfoStatus(userId, status) {
        SQ.post({
            url: "?action=SetInfoStatus",
            data: { userId: userId, status: status }
        });
        return;
    }
    //设置打款状态
    function setPaymentstatus(userId, status) {
        SQ.post({
            url: "?action=SetPaymentStatus",
            data: { userId: userId, status: status }
        });
        return;
    };
    //认证记录
    $(".verify-log").click(function() {
        var userId = $(this).data("userid");
        SQ.post({
            url: "?action=GetLog",
            data: { userId: userId },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "认证记录查看",
                    cancel: true,
                    cancelVal: "关闭",
                    content: template("logInformation", { logList: json.logList })
                });
            }
        });
        return false;
    });
    //资料初审
    $(".verify-review").click(function() {
        var userId = $(this).data("userid");
        SQ.post({
            url: "?action=GetInfo",
            data: { userId: userId },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } }); return;
                }
                else if (json.verify.VerifyState != 100) {
                    SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } }); return;
                }
                var _content = "认证类型：<span class='text-primary'>" + (json.user.VerifyType == 1 ? "个人" : (json.user.VerifyType == 2 ? "个体户" : "企业")) + "</span><br />"
                             + "认证名称：<span class='text-primary'>" + json.verify.AuthName + "</span><br />"
                             + "证件号码：<span class='text-primary'>" + json.verify.AuthNumber + "</span><br />"
                             + "法定代表：<span class='text-primary'>" + json.verify.LegalPersonName + "</span><br /><br />"
                             + "以上信息如果正确，请点击“通过”按钮，错误请点击“失败”按钮。";
                SQ.tips.ask({
                    title: "认证资料初审确认",
                    content: _content,
                    ok: false,
                    cancel: false,
                    button: [{
                        name: "通过",
                        callback: function() {
                            setInfoStatus(userId, 1);
                        },
                        focus: true
                    }, { name: "失败",
                        callback: function() {
                            setInfoStatus(userId, -1);
                        }
                    }, { name: "取消"}]
                });
            }
        });
        return false;
    });
    //打款
    $(".verify-payment").click(function() {
        var userId = $(this).data("userid");
        SQ.post({
            url: "?action=GetInfo",
            data: { userId: userId },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } }); return;
                }
                else if (json.verify.VerifyState != 101) {
                    SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } }); return;
                }
                var _content = "开户名称：" + (json.verify.AccountOpenType == 1 ? json.verify.AuthName : json.verify.LegalPersonName) + "<br />"
                             + "开户银行：" + json.verify.BankName + "<br />"
                             + "收款账户：" + json.verify.BankAccount + "<br />"
                             + "验证金额：￥" + json.verify.TestMoney + "<br /><br />"
                             + "请按照以上信息进行打款，如果打款完毕，请点击“已打款”按钮。";
                SQ.tips.ask({
                    title: "认证金额打款确认",
                    content: _content,
                    ok: false,
                    cancel: false,
                    button: [{
                        name: "已打款",
                        callback: function() {
                            setPaymentstatus(userId, 0);
                        },
                        focus: true
                    }, { name: "账户错",
                        callback: function() {
                            setPaymentstatus(userId, -1);
                        }
                    }, { name: "未打款"}]
                });
            }
        });
        return false;
    });
    //打款状态
    $(".verify-paymentstatus").click(function() {
        var userId = $(this).data("userid");
        SQ.tips.ask({
            title: "认证金额打款状态",
            content: "请确认认证金额打款是否成功？",
            ok: false,
            cancel: false,
            button: [{
                name: "成功",
                callback: function() {
                    setPaymentstatus(userId, 1);
                },
                focus: true
            }, { name: "失败",
                callback: function() {
                    setPaymentstatus(userId, -1);
                }
            }, { name: "关闭"}]
        });
    });
    //线下认证
    $(".verify-offline").click(function() {
        var userId = $(this).data("userid");
        SQ.post({
            url: "?action=GetInfo",
            data: { userId: userId },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "实名认证-线下认证",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("offlineInformation", { user: json.user }),
                    init: function() {
                        SQ.component.initTabs();
                        $("#verifyTypeBtn input").click(function() {
                            $("#verifyType").val($(this).data("verify_type"));
                            if ($(this).data("verify_type") == 1) {
                                $("#legalPersonNamePanel").hide();
                                $("#legalPersonNameInput").removeClass("necessary");
                                $("#authNumberTypePanel").hide();
                            }
                            else {
                                $("#legalPersonNamePanel").show();
                                $("#legalPersonNameInput").addClass("necessary");
                                $("#authNumberTypePanel").show();
                            }
                        });
                        $("#authNumberTypeBtn input").click(function() {
                            $("#authNumberType").val($(this).data("authnumber_type"));
                        });
                    },
                    ok: function() {
                        var $_offineForm = $("#offlineForm");
                        if (!fieldEmptyValidate($_offineForm)) {
                            return false;
                        }
                        SQ.post({
                            url: $_offineForm.attr("action"),
                            data: $_offineForm.serialize()
                        });
                    }
                });
            }
        });
        return false;
    });
    //重置实名认证
    $(".verify-reset").click(function() {
        var userId = $(this).data("userid");
        SQ.tips.ask({
            title: "重置实名认证确认",
            content: "确认为用户<span class='text-red'>重置实名认证</span>？",
            subTipContent: "确认后将置空用户认证资料",
            ok: function() {
                SQ.post({
                    url: "?action=Reset",
                    data: { userId: userId }
                });
            }
        });
    });
});