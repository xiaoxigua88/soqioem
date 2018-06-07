$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll");

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchExport = $("#batchExport");
    var $_batchClear = $("#batchClear");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";
    $(".list-checkbox, #checkAll").change(function() {
        if ($_mainList.find(listCheckedSelector).length > 0) {
            $_batchButtons.attr("disabled", false);
        }
        else {
            $_batchButtons.attr("disabled", true);
        }
    });
    // 批量操作按钮 的点击事件生成方法
    /* operateName：操作名称 */
    /*  url：响应地址 */
    /*  checkType：选中类型 1,必须选择；2,对选择有效未选择响应所有,3,不关注是否选择 */
    var createBatchHandle = function(operateName, url, checkType) {
        return function() {
            // 获取选中的任务的数量 和 多个id拼接的字符串
            var taskCount = SQ.getTaskIdArray().length;
            var taskIds = SQ.getTaskIdArray().join(",");
            if (taskCount == 0 && checkType == 1) {
                SQ.tips.warn({
                    content: "你还没有选择任何记录！",
                    cancel: true,
                    cancelVal: "关闭"
                });
                return false;
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 个") + "记录？",
                ok: function() {
                    SQ.post({
                        data: { taskIds: taskIds },
                        url: url
                    });
                }
            });
        };
    };
    // 批量删除
    $_batchDelete.click(createBatchHandle("删除", "?action=Delete", 1));
    // 批量导出
    $_batchExport.click(createBatchHandle("导出", "?action=Export", 2));
    // 清空
    $_batchClear.click(createBatchHandle("清空", "?action=Clear", 3));
    
    //代理编辑
    $(".user-edit").click(function() {
        var userid = $(this).data("userid");
        SQ.post({
            url: "/oemmanager/userinfo/useredit",
            data: {
            	userid: userid
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "用户资料编辑",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("editInformation", { user: json.user }),
                    init: function() {
                    },
                    ok: function() {
                        var $_editForm = $("#editForm");
                        SQ.post({
                            url: $_editForm.attr("action"),
                            data: $_editForm.serialize()
                        });
                        return false;
                    }
                });
            }
        });
        return false;
    });
    //代理添加
    $("#user-add").click(function() {
    	var oemid = $(this).data("oemid");
    	$.dialog({
            title: "代理添加",
            cancel: true,
            okVal: "保存",
            cancelVal: "关闭",
            content: template("addInformation", {oemid}),
            init: function() {
            },
            ok: function() {
                var $_addForm = $("#addForm");
                if (fieldEmptyValidate($_addForm)) {
	                SQ.post({
	                    url: $_addForm.attr("action"),
	                    data: $_addForm.serialize()
	                });
                }
                return false;
            }
        });
    })
    //用户实名认证
    $(".user-verify").click(function() {
        return false;
    });
    //账户充值
    $(".user-recharge").click(function() {
        var userId = $(this).data("userid");
        SQ.post({
            url: "?action=Recharge",
            data: {
                userId: userId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "账户充值",
                    cancel: true,
                    okVal: "充值",
                    cancelVal: "关闭",
                    content: template("rechargeInformation", { user: json.user, goldList: json.goldList }),
                    init: function() {
                        // 初始化sq-btn-group
                        SQ.component.initTabs();
                        $(".recharge-diy").hide();
                        $("#productBtn input").click(function() {
                            var productId = parseInt($(this).data("product_id"));
                            $("#product").val(productId);
                            if (productId == 0) {
                                $(".recharge-diy").show();
                            }
                            else {
                                $(".recharge-diy").hide();
                            }
                        });
                        $("#goldAmountInput").keyup(function() {
                            var _goldAmount = parseInt($(this).val());
                            if (isNaN(_goldAmount)) {
                                $("#oriGoldInput").val("");
                            }
                            else {
                                $("#oriGoldInput").val(json.goldRate * _goldAmount);
                            }
                            $("#giftGoldInput").val("");
                        });
                    },
                    ok: function() {
                        var $_rechargeForm = $("#rechargeForm");
                        var $_rechargeInput = $("#rechargeInput");
                        var $_goldAmountInput = $("#goldAmountInput");
                        var _rechargeAmount = parseInt($_rechargeInput.val());
                        var _product = parseInt($("#product").val());
                        var _goldAmount = parseInt($_goldAmountInput.val());
                        var _oriGold = parseInt($("#oriGoldInput").val());
                        var _giftGold = parseInt($("#giftGoldInput").val());
                        var content = "";
                        if ($_rechargeInput.val().length > 0 && isNaN(_rechargeAmount)) {
                            SQ.tips.error({ content: "充值金额为数字且为整数！" });
                            return false;
                        }
                        if (_product == 0) {
                            if (isNaN(_goldAmount)) {
                                SQ.tips.error({ content: "优币购买金额为数字且为整数！" });
                                return false;
                            }
                            if (isNaN(_oriGold)) {
                                SQ.tips.error({ content: "原始优币数量必须为整数！" });
                                return false;
                            }
                            if (isNaN(_giftGold)) {
                                SQ.tips.error({ content: "赠送优币数量必须为整数！" });
                                return false;
                            }
                        }
                        if ($_rechargeInput.val().length > 0 && !isNaN(_rechargeAmount)) {
                            content += "充值金额<b class='text-red'>￥" + _rechargeAmount + "</b>元，"
                        }
                        if (_product == 0) {
                            content += "自定义购买优币金额<b class='text-red'>￥" + _goldAmount + "</b>元，优币数量<b class='text-red'>"
                            + _oriGold + "</b> + <b class='text-red'>" + _giftGold + "</b> = <b class='text-red'>" + (_oriGold + _giftGold) + "</b>个，";
                        }
                        else if (_product > 0) {
                            content += "优币套餐<b class='text-red'>" + $("input.sq-tab-selected").val() + "</b>，";
                        }
                        if (content == "") {
                            SQ.tips.error({ content: "你尚未填写账户充值数据！" });
                            return false;
                        }
                        SQ.tips.ask({
                            title: "账户充值确认",
                            content: "您将提交" + content + "是否继续？",
                            ok: function() {
                                SQ.post({
                                    url: $_rechargeForm.attr("action"),
                                    data: $_rechargeForm.serialize()
                                });
                            }
                        });
                        return false;
                    }
                });
            }
        });
        return false;
    });
    //重置密码
    $(".user-pwd").click(function() {
        var userId = $(this).data("userid");
        SQ.tips.ask({
            title: "重置用户密码确认",
            content: "确认为用户重置密码？",
            ok: function() {
                SQ.post({
                    data: { action: "InitPwd", userId: userId },
                    url: ""
                });
            }
        });
    });
});