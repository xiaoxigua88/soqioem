$(function() {
    // 激活API功能
    $("#openApi").click(function() {
        // 开启API功能
        SQ.tips.ask({
            content: "您尚未激活API功能！",
            subTipContent: "您要立即激活API功能吗？",
            ok: function() {
                $("#openForm").find(".protection-enabled").trigger("click");
                return false;
            }
        });
    });
    // 关闭API功能
    $("#closeApi").click(function() {
        SQ.tips.ask({
            content: "Api功能关闭可以再次开启，是否继续？",
            title: "Api密钥重置确认",
            ok: function() {
                $("#closeForm").find(".protection-enabled").trigger("click");
                return false;
            }
        });
    });
    // 保存通知地址
    $("#saveNotify").click(function() {
        SQ.tips.ask({
            content: "如果使用通知地址，请确保地址访问的连通性，是否继续？",
            title: "通知地址修改确认",
            ok: function() {
                $("#saveNotifyForm").find(".protection-enabled").trigger("click");
                return false;
            }
        });
    });
    // 重置API TOKEN
    $("#initApi").click(function() {
        SQ.tips.ask({
            content: "Api密钥重置成功后，请及时更新程序中正在使用的Api密钥，是否继续？",
            title: "Api密钥重置确认",
            ok: function() {
                $("#resetForm").find(".protection-enabled").trigger("click");
                return false;
            }
        });
    });
    if (jsData.user.VerifyType == 0) {
        SQ.tips.ask({
            title: "实名认证提醒",
            content: "<span class='text-red'>未实名认证</span>的用户暂不可使用API功能。您要前往认证么？",
            ok: function() {
                window.location.href = "/client/userinfo/verify.aspx";
                return false;
            },
            close: function() {
                window.location.href = jsData.common.backUrl;
            }
        });
        return;
    };
    if (jsData.userApi.ApiToken == "") {
        $("#openApi").trigger("click");
    }
});