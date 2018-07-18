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
    
    //用户编辑
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
    //用户添加
    $("#user-add").click(function() {
    	var oemid = $(this).data("oemid");
    	$.dialog({
            title: "用户添加",
            cancel: true,
            okVal: "保存",
            cancelVal: "关闭",
            content: template("addInformation", {oemid}),
            init: function() {
            },
            ok: function() {
                var $_addForm = $("#addForm");
                SQ.post({
                    url: $_addForm.attr("action"),
                    data: $_addForm.serialize()
                });
                return false;
            }
        });
    })
    //用户实名认证
    $(".user-verify").click(function() {
        return false;
    });
    //关键词调价
    $(".user-pricechange").click(function() {
    	var userid = $(this).data("userid");
    	SQ.post({
            url: "/oemmanager/userinfo/seopricechange",
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
                    title: "关键词价格调整",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("seopricechangeInformation", {searchTypeList: json.searchTypeList,userid:userid, lst:json.lst}),
                    init: function() {
                    	SQ.component.initTabs();
                    	$(".searchtypes input").click(function() {
                    		var searchtype = $(this).parent().data("searchtype");
                    		var discounttype_searchtype = $(this).data("discounttype_" + searchtype);
                    		$("#discounttype_" + searchtype).val(discounttype_searchtype);
                    		 if(discounttype_searchtype=='1'){
                    			 $("#fixprice_" + searchtype).removeClass("hide");
                    			 $("#minprice_" + searchtype).addClass("hide");
                    			 $("#maxprice_" + searchtype).addClass("hide");
                    			 $("#ratio_" + searchtype).addClass("hide");
                            }else if(discounttype_searchtype=='2'){
                            	$("#fixprice_" + searchtype).addClass("hide");
                    			 $("#minprice_" + searchtype).removeClass("hide");
                    			 $("#maxprice_" + searchtype).removeClass("hide");
                    			 $("#ratio_" + searchtype).removeClass("hide");
                            }
                        });
                    	// 保存密码
                        /*$("#savepricetempl").click(function() {
                            var $_form = $("#savepriceform");
                            var actionUrl = $_form.attr("action");
                            if (!fieldEmptyValidate($_form)) {
                                return false;
                            }
                            SQ.post({
                                url: actionUrl,
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
                            return false;
                        });*/
                    },
                    ok: function() {
                        var $_form = $("#savepriceform");
                        if (!fieldEmptyValidate($_form)) {
                            return false;
                        }
                        SQ.post({
                            url: $_form.attr("action"),
                            data: $_form.serialize()
                        });
                        return false;
                    }
                });
            }
        });
        return false;
    });
    
    //账户充值
    $(".user-recharge").click(function() {
    	var userid = $(this).data("userid");
    	$.dialog({
            title: "账户充值",
            cancel: true,
            okVal: "充值",
            cancelVal: "关闭",
            content: template("rechargeInformation", {userid}),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $(".recharge-diy").hide();
            },
            ok: function() {
                var $_rechargeForm = $("#rechargeForm");
                var $_rechargeInput = $("#rechargeInput");
                var $_rechargeMemoInput = $("#rechargeMemoInput");
                var _rechargeAmount = parseInt($_rechargeInput.val());
                var content = "";
                if ($_rechargeInput.val().length > 0 && isNaN(_rechargeAmount)) {
                    SQ.tips.error({ content: "充值金额为数字且为整数！" });
                    return false;
                }
                if(_rechargeAmount < 0 && $.trim($_rechargeMemoInput.val()) == '') {
                	SQ.tips.error({ content: "账户充值请求被拒绝！原因：充值金额小于0时，必须备注原因！" });
                    return false;
                }
                if ($_rechargeInput.val().length > 0 && !isNaN(_rechargeAmount)) {
                    content += "充值金额<b class='text-red'>￥" + _rechargeAmount + "</b>元，"
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
        return false;
    });
    //重置密码
    $(".user-pwd").click(function() {
        var userid = $(this).data("userid");
        SQ.tips.ask({
            title: "重置用户密码确认",
            content: "确认为用户重置密码？",
            ok: function() {
                SQ.post({
                    data: { action: "InitPwd", userid: userid },
                    url: "/oemmanager/userinfo/userinitpwd"
                });
            }
        });
    });
});