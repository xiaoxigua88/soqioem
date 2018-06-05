$(function() {
    // 初始数据
    var verifyType = jsData.verify.VerifyType || 1;
    var verifyMethod = jsData.verify.VerifyMethod || 1;
    var accountOpenType = jsData.verify.AccountOpenType || 1;

    var hideClass = "hide";
    var selectedClassName = "sq-tab-selected";
    var verifyTypePlaceholderMap = {
        1: "请输入您的真实姓名",
        2: "请输入个体工商户名称",
        3: "请输入企业名称"
    };

    // 认证类型相关变量
    var $_verifyProfileForm = $("#verifyProfileForm");
    var $_verifyChangeContainer = $("#verifyTypeChange");
    var $_verifyTypeInput = $("#verifyTypeInput");
    var $_authNameInput = $("#authNameInput");

    // 认证途径相关变量
    var $_payChangeContainer = $("#verifyMethodChange");
    var $_verifyMethodInput = $("#verifyMethodInput");

    var $_accountOpenTypeChangeContainer = $("#accountOpenTypeChange");
    var $_accountOpenTypeInput = $("#accountOpenTypeInput");

    var $_bankNameBlock = $("#bankNameBlock");
    var $_bankAccountLabels = $(".bank-account-num");
    var $_alipayLabel = $(".alipay-num");
    var $_bankNameInput = $("#bankNameInput");
    var $_bankAccountInput = $("#bankAccountInput");

    // 控制付款方式显示方法
    var changePayView = function(payType) {
        if (payType == 1) {
            $_bankNameBlock.addClass(hideClass);
            $_bankAccountLabels.addClass(hideClass);
            $_alipayLabel.removeClass(hideClass);
            $_bankAccountInput.attr("placeholder", "请输入支付宝账号");
            $_bankAccountInput.focus();
        }
        else {
            $_bankNameBlock.removeClass(hideClass);
            $_bankAccountLabels.removeClass(hideClass);
            $_alipayLabel.addClass(hideClass);
            $_bankAccountInput.attr("placeholder", "请输入银行账号");
            $_bankNameInput.focus();
        }
    };

    // 使用默认参数 初始化SQ.component.tabs
    SQ.component.initTabs();

    // 显示初始化
    // 根据认证类型，触发一次对应的认证类型tab
    $_verifyChangeContainer.find("*[data-verify_type='" + verifyType + "']").trigger("click");
    $("#authNameInput").attr("placeholder", verifyTypePlaceholderMap[verifyType]);

    // 根据认证途径，初始化显示
    $_payChangeContainer.find("*[data-verify_method='" + verifyMethod + "']").addClass(selectedClassName);
    changePayView(verifyMethod);

    // 根据开户类型，初始化显示
    $_accountOpenTypeChangeContainer.find("*[data-account_opentype='" + accountOpenType + "']").addClass(selectedClassName);

    // 认证类型点击切换
    $("#verifyTypeChange input").click(function() {
        // 清空表单
        SQ.dom.clearForm($_verifyProfileForm);
        var $_self = $(this);
        var verifyType = $_self.data("verify_type");
        $_authNameInput.attr("placeholder", verifyTypePlaceholderMap[verifyType]);
        $_authNameInput.focus();
        $_verifyTypeInput.val(verifyType);
        $_verifyMethodInput.val($_payChangeContainer.find("." + selectedClassName).data("verify_method"));
        $_accountOpenTypeInput.val($_accountOpenTypeChangeContainer.find("." + selectedClassName).data("account_opentype"));
    });

    // 付款方式点击切换
    $("#verifyMethodChange input").click(function() {
        var $_self = $(this);
        var payType = $_self.data("verify_method");
        // 按钮select高亮切换
        $_self.addClass(selectedClassName).siblings().removeClass(selectedClassName);
        // 清空账户号
        $_bankNameInput.val("");
        $_bankAccountInput.val("");
        // 填写payType表单
        $_verifyMethodInput.val(payType);
        changePayView(payType);
    });

    // 开户类型点击切换
    $("#accountOpenTypeChange input").click(function() {
        // 按钮select高亮切换
        var $_self = $(this);
        $_self.addClass(selectedClassName).siblings().removeClass(selectedClassName);
        $_accountOpenTypeInput.val($_self.data("account_opentype"));
        // 清空账户号
        $_bankNameBlock.find("input").val("");
        $_bankAccountInput.val("");
    });

    //比对状态
    if (jsData.verify.State == 100 || jsData.verify.State == 101 || jsData.verify.State == 102) {
        var refreshState = function() {
            $.get({
                url: "?action=CompareState&state=" + jsData.verify.State,
                dataType: "json",
                success: function(json) {
                    if (json.result) {
                        setTimeout(refreshState, 5000);
                    }
                    else {
                        window.location.reload();
                    }
                }
            });
        }
        refreshState();
    }

    // 认证资料表单提交
    $("#btnSave").click(function() {
        if (!fieldEmptyValidate($_verifyProfileForm)) return false;
        SQ.post({
            data: $_verifyProfileForm.serialize(),
            url: $_verifyProfileForm.attr("action"),
            successResultFalse: function(json) {
                if (json.reload) {
                    window.location.reload();
                }
                else {
                    validateShowError(json.name, json.text, $_verifyForm);
                }
            }
        });
    });

    // 验证打款金额
    var $_verifyForm = $("#verifyForm");
    $("#btnVerify").click(function() {
        if (!fieldEmptyValidate($_verifyForm)) return false;

        SQ.tips.ask({
            title: "验证打款金额提交确认",
            content: "您输入认证金额为 <b class='text-red'>" + $("#testPayMoneyInput").val() + "元</b>，如果错误会导致本次认证失败，是否继续？",
            ok: function() {
                SQ.post({
                    data: $_verifyForm.serialize(),
                    url: $_verifyForm.attr("action"),
                    successResultFalse: function(json) {
                        if (json.reload) {
                            window.location.reload();
                        }
                        else {
                            validateShowError(json.name, json.text, $_verifyForm);
                        }
                    }
                });
            }
        });
    });
});