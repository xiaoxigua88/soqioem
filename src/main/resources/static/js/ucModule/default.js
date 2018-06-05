$(function() {
    function amountCheck() {
        var _content = "";
        if (jsData.user.UserAccount.TotalAmount < 0) {
            _content = "您的<span class='text-red'>账户金额</span>已欠费，请立即充值！";
        }
        else if (jsData.user.UserAccount.SeoAmountNeed > 0) {
            var _day = Math.ceil(jsData.user.UserAccount.TotalAmount / jsData.user.UserAccount.SeoAmountNeed);
            if (_day <= 5) {
                _content = "您的<span class='text-red'>账户金额</span>预估将在<b class='text-red'>" + _day + "</b>天内消费完毕，请立即充值！";
            }
        }
        if (_content != "") {
            SQ.tips.ask({
                title: "账户金额预警",
                content: _content,
                okVal: "立即充值",
                ok: function() {
                    window.location.href = "/client/finance/recharge.aspx";
                    return false;
                },
                cancel: function() {
                    goldCheck();
                }
            });
        }
        else {
            goldCheck();
        }
        return false;
    };

    function goldCheck() {
        var _content = "";
        if (jsData.user.UserAccount.TotalGold < 0) {
            _content = "您的<span class='text-red'>账户优币</span>已欠费，请立即购买！";
        }
        else if (jsData.user.UserAccount.WatchGoldNeed > 0) {
            var _day = Math.ceil(jsData.user.UserAccount.TotalGold / jsData.user.UserAccount.WatchGoldNeed);
            if (_day <= 5) {
                _content = "您的<span class='text-red'>账户优币</span>预估将在<b class='text-red'>" + _day + "</b>天内消费完毕，请立即购买！";
            }
        }
        if (_content != "") {
            SQ.tips.ask({
                title: "账户优币预警",
                content: _content,
                okVal: "立即购买",
                ok: function() {
                    window.location.href = "/client/finance/gold.aspx";
                    return false;
                },
                cancel: function() {
                    verifyCheck();
                }
            });
        }
        else {
            verifyCheck();
        }
        return false;
    };

    function verifyCheck() {
        if (jsData.user.VerifyType == 0) {
            SQ.tips.ask({
                title: "未实名认证提醒",
                content: "<span class='text-red'>未进行实名认证</span>很多业务将受限使用，建议您立即完成实名认证！",
                okVal: "立即认证",
                cancelVal: "暂不验证",
                ok: function() {
                    window.location.href = "/client/userinfo/verify.aspx";
                    return false;
                }
            });
            return false;
        };
    };
    /* 先检测金额，在检测优币，最后检测认证 */
    amountCheck();
});