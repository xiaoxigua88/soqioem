$(function() {
    // 充值提交
    $("#rechargeBtn").click(function() {
        var $_rechargeForm = $("#rechargeForm");
        var $_checkedRadio = $("input[name=payType]:checked");
        var $_rechargeAmount = $("#rechargeInput");
        var payMinValue = parseInt($("#payMinInput").val());

        var reg = /^[0-9]*$/;

        // 表单验证
        clearValidateError($_rechargeForm);
        if (!reg.test($_rechargeAmount.val())) {
            validateShowError($_rechargeAmount, "金额为数字且为整数", $_rechargeForm);
            return;
        }
        if (!$_rechargeAmount.val()) {
            validateShowError($_rechargeAmount, "请输入正确的金额", $_rechargeForm);
            return;
        }
        if (parseInt($_rechargeAmount.val()) < payMinValue) {
            validateShowError($_rechargeAmount, "最低起充金额为 " + payMinValue + " 元", $_rechargeForm);
            return;
        }
        
        // 如果选择微信支付则弹窗让用户扫描二维码，其他支付方式则打开新窗口同步提交表单
        if ($_checkedRadio.val() == "weixin") {
            var checkWeixinPayed = null;
            $_rechargeForm = $("#rechargeForm");
            SQ.post({
                url: $_rechargeForm.attr("action"),
                data: $_rechargeForm.serialize(),
                isCoverSuccess: true,
                success: function(json) {
                    if (json.result == false) {
                        SQ.tips.error({ content: json.text });
                        return;
                    }
                    var dlg = $.dialog.open(json.url, {
                        title: "微信扫码支付",
                        width: 600,
                        height: 400,
                        // 弹窗后执行函数
                        init: function() {
                            var $_iframeDocument = $(this.iframe.contentWindow.document);
                            // 定时检测是否支付成功
                            checkWeixinPayed = setInterval(function() {
                                $.ajax({
                                    url: json.url + "&action=CheckOrderStatus",
                                    dataType: "json",
                                    success: function(json2) {
                                        if (json2.result == true) {
                                            clearInterval(checkWeixinPayed);
                                            dlg.close();
                                            SQ.success(json2.text, 3, function() {
                                                window.location.reload();
                                            });
                                        }
                                    },
                                    error: function(data) {
                                        SQ.showBusy();
                                    }
                                });
                            }, 2000);
                        },
                        ok: false,
                        cancel: true,
                        cancelVal: "取消支付",
                        close: function() {
                            // 关闭窗口后清除轮询
                            clearInterval(checkWeixinPayed);
                        }
                    });
                }
            });
            return;
        }
        else {
            $_rechargeForm.submit();
        }
    });
});