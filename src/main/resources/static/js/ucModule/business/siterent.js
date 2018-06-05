$(function() {
    function userTip() {
        if (jsData.common.userID <= 0) {
            SQ.tips.ask({
                title: "用户提醒",
                content: "你还没有登录用户中心，请先登录！如果还没有优帮云账户，请先注册！<br />客服电话：<span class='text-red'>0571-88730320</span> 咨询QQ：<span class='text-red'>9781482</span>",
                ok: false,
                cancel: false,
                button: [{
                    name: "没账户？注册",
                    callback: function() { location.href = jsData.common.domain.uc + "/register.aspx"; },
                    focus: true
                },{
                    name: "有账户！登录",
                    callback: function() { location.href = jsData.common.domain.uc + "/login.aspx"; },
                },  {
                    name: "不！我再看看"
                }]
            });
            return false;
        }
        else if (jsData.verifyType != 2 && jsData.verifyType != 3) {
            SQ.tips.ask({
                title: "实名认证提醒",
                content: "<span class='text-red'>未进行【企业】或【个体工商户】实名认证</span>的用户不可使用云站通功能。您要前往认证么？",
                okVal: "立即认证",
                ok: function() {
                    window.location.href = "/client/userinfo/verify.aspx";
                    return false;
                }
            });
            return false;
        }
        return true;
    }

    //云站通关注
    $(".btn-follow").click(function() {
        if (!userTip()) return;
        SQ.post({
            url: "follow.aspx?action=SetFollow",
            data: { webId: $(this).data("webid"), add: $(this).data("add") }
        });
        return;
    });

    //云站通预订
    $(".btn-reserve").click(function() {
        if (!userTip()) return;
        SQ.post({
            url: "Reserve.aspx?action=SetReserve",
            data: { webId: $(this).data("webid"), add: $(this).data("add") }
        });
        return;
    });

    //云站通配置
    $(".btn-config").click(function() {
        if (!userTip()) return;
        var orderId = $(this).data("orderid");
        SQ.post({
            url: "?action=GetConfig",
            data: { orderId: orderId },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                var _dlg = $.dialog({
                    title: "云站通-展现方式配置",
                    content: template("configTemplate", { model: json }),
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    ok: function() {
                        var $_configForm = $("#configForm");
                        if (fieldEmptyValidate($_configForm)) {
                            SQ.waiting("正在保持，请稍候...");
                            $_configForm.ajaxSubmit({
                                type: 'post',
                                success: function(data) {
                                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                                    SQ.hideWaiting();
                                    if (json.result) {
                                        SQ.success(json.text);
                                        var jumpDelay = 2000;
                                        setTimeout(function() {
                                            window.location.reload();
                                        }, jumpDelay);
                                    } else {
                                        SQ.warn(json.text);
                                    }
                                },
                                error: function() {
                                    SQ.hideWaiting();
                                    SQ.showBusy();
                                }
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
                        $(".btn-upload").click(function() {
                            $("#" + $(this).data("inputid")).trigger("click");
                        });
                        $(".input-upload").change(function() {
                            $("#" + $(this).data("btnid")).text("重新选择");
                        });
                        $(".btn-delete").click(function() {
                            var $_btn = $(this);
                            var imgType = $_btn.data("imgtype")
                            SQ.tips.ask({
                                title: "网站自定义图片确认",
                                content: "本操作将永久删除图片，是否继续？",
                                ok: function() {
                                    SQ.post({
                                        url: "?action=DeleteImg",
                                        data: { orderId: orderId, imgType: imgType },
                                        isCoverSuccess: true,
                                        success: function(json) {
                                            if (json.result == false) {
                                                SQ.tips.error({ content: json.text });
                                                return;
                                            }
                                            else {
                                                $("#" + $_btn.data("btnid")).text("选择图片");
                                                $_btn.remove();
                                            }
                                        }
                                    });
                                }
                            });
                        });
                    }
                });
            }
        });
        return false;
    });

    //云站通提交订单
    $("#siterent_submit").click(function() {
        if (!userTip()) return;
        SQ.tips.ask({
            title: "网站租赁提交确认",
            content: "当前网站租赁提单需要冻结金额 <b class='text-red'>￥" + $("#amount_total").text() + "</b>，是否继续？",
            ok: function() {
                SQ.post({
                    data: { webId: $("#webId").val() },
                    url: "?action=Pay",
                    isCoverSuccess: true,
                    success: function(json) {
                        if (json.result == false) {
                            if (json.text == "ContractView") {
                                SQ.post({
                                    data: { webId: $("#webId").val() },
                                    url: "?action=ContractView",
                                    isCoverSuccess: true,
                                    success: function(json) {
                                        if (json.result == false) {
                                            SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } });
                                        }
                                        else {
                                            $.dialog({
                                                title: "云站通租用合同签订",
                                                width: 800,
                                                content: json.text,
                                                cancel: true,
                                                okVal: "我同意",
                                                cancelVal: "不同意",
                                                ok: function() {
                                                    SQ.post({
                                                        data: { webId: $("#webId").val() },
                                                        url: "?action=ContractSign"
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else {
                                SQ.tips.error({ content: json.text, close: function() { window.location.reload(); } });
                            }
                            return;
                        }
                        else {
                            SQ.tips.success({ content: json.text, close: function() { location.href = "orderinfo.aspx"; } });
                        }
                    }
                });
            }
        });
        return;
    });

    //停止云站通
    $(".btn-stoporder").click(function() {       
        if (!userTip()) return;
        var orderId = $(this).data("orderid");
        SQ.tips.ask({
            title: "订单停止确认",
            content: "订单停止后网站将自动进入云站通市场，其他用户可自由租赁，您是否继续？",
            ok: function() {
                $(".protection-enabled").trigger("click");
            }
        });
    });
    
    $(".order-export").click(function() {
        var orderId = $(this).data("orderid");
		SQ.tips.ask({
			title:  "云站通排名报表导出确认",
			content: "确认导出该站所有关键词排名报表吗？",
			ok: function () {
				SQ.post({
					data: {orderId: orderId},
					url: "?action=Export"
				});
			}
		});
    });

    //关键词消费记录查看
    $(".keyword-view").click(function() {
        if (!userTip()) return;
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

    //调节云站通租用提交的展示
    if ($(".sq-panel-siterent-apply").length > 0) {
        var reset_sqpanelbody_width = function() {
            $(".sq-panel-body").width($(".sq-panel").width() - 260);
        };
        reset_sqpanelbody_width();
        $(window).resize(function() {
            reset_sqpanelbody_width();
        });
    };
    //云站通订单列表如果有未设置的
    if ($(".sq-panel-siterent-orderinfo").length > 0 && jsData.notSetCount > 0) {
        $($(".btn-config")[0]).trigger("click");
    };
    //云站通订单详情页面展示
    if ($(".sq-panel-siterent-ordercost").length > 0) {
        var w = $(".keywordlist").width();
        var c = Math.floor(w / 455);
        if (c > 0) {
            jQuery(".keywordlist li").width(Math.floor(w / c));
        }

        $(".sq-tab").click(function() {
            $(".sq-tab").removeClass("sq-tab--current");
            $(this).addClass("sq-tab--current");
            $(".oc-overview").removeClass("hide");
            $(".oc-overview").hide();
            $("#" + $(this).attr("control")).show();
            $(".sq-panel").height($(".sq-panel-body").height() + 80);
            resize_sqpanel();
        });
    };
    if (!userTip()) return;
});