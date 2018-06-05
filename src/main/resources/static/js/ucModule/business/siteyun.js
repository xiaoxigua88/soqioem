$(function() {
    function userTip() {
        if (jsData.verifyType == 0) {
            SQ.tips.ask({
                title: "未实名认证提醒",
                content: "<span class='text-red'>未进行实名认证</span>的用户不可使用云建站功能。请先完成实名认证！",
                okVal: "立即认证",
                ok: function() {
                    window.location.href = "/client/userinfo/verify.aspx";
                    return false;
                },
                cancel: function() {
                    window.location.href = "/client/";
                    return false;
                }
            });
            return false;
        }
        return true;
    };

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

    if (!userTip()) return;

    if (jsData.lst.length == 0) {
        SQ.tips.ask({
            title: "云建站管理",
            content: "当前<span class='text-red'>尚未购买</span>云建站服务，您要前往<span class='text-red'>创建自己的网站</span>么？",
            ok: function() {
                window.location.href = "create.aspx";
                return false;
            }
        });
        return;
    };
});