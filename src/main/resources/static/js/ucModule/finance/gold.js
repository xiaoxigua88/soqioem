$(function() {
    // 初始化sq-btn-group
    SQ.component.initTabs();
    var $_buyBtn = $("#buyBtn");
    $("#productBtn input").click(function() {
        $("#productId").val($(this).data("product_id"));
        $_buyBtn.attr("disabled", false);
    });
    // 充值提交
    $_buyBtn.click(function() {
        var $_buyForm = $("#buyForm");
        var $_productId = $("#productId").val();
        var $_productName = $("input.sq-tab-selected").val();
        var $_amount = $("input.sq-tab-selected").data("amount");
        if ($_amount > jsData.user.TotalAmount) {
            $.dialog({
                title: "优币购买确认",
                content: "您当前的账户金额不足购买此套餐！",
                cancel: true,
                okVal: "现在充值",
                cancelVal: "换个套餐",
                ok: function() {
                    location.href = "recharge.aspx";
                    return false;
                }
            });
        }
        else {
            SQ.tips.ask({
                title: "优币购买确认",
                content: "您选择了<b class='text-red'>" + $_productName + "</b>套餐，是否确认购买？",
                ok: function() {
                    SQ.post({
                        url: $_buyForm.attr("action"),
                        data: { productId: $_productId },
                        isCoverSuccess: true,
                        success: function(json) {
                            if (json.recharge) {
                                $.dialog({
                                    title: "优币购买确认",
                                    content: json.text,
                                    cancel: true,
                                    okVal: "现在充值",
                                    cancelVal: "换个套餐",
                                    ok: function() {
                                        location.href = "recharge.aspx";
                                        return false;
                                    }
                                });
                            }
                            else if (json.reload) {
                                var jumpDelay = 2000;
                                setTimeout(function() { window.window.location.reload(); }, 2000);
                            }
                            else if (json.url) {
                                SQ.tips.success({ content: json.text });
                                setTimeout(function() { window.location.href = json.url; }, 2000);
                            }
                        }
                    });
                }
            });
        }
    });
});