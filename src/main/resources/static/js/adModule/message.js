$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    var openPostDialog = function() {
        $.dialog({
            title: "消息发布",
            cancel: true,
            content: template("importInformation", {}),
            ok: function() {
                var $_importForm = $("#importForm");
                if (fieldEmptyValidate($_importForm)) {
                    SQ.post({
                        url: $_importForm.attr("action"),
                        data: $_importForm.serialize()
                    });
                }

                // 阻止对话框消失
                return false;
            }
        });
    };

    $(".btn-operate-add").click(function() {
        openPostDialog();
    });
    
    // 消息查看
    $(".message-view").click(function() {
        var messageId = $(this).data("msgid");
        SQ.post({
            url: "?action=View",
            data: {
                messageId: messageId
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "消息查看",
                    content: template("viewInformation", json),
                    cancel: true,
                    cancelVal: "关闭"
                });

            }
        });
        return false;
    });

    var $_mainList = $("#mainList");
    var $_batchDelete = $("#batchDelete");
    var $_batchButtons = $(".batch-operate");
    var listCheckedSelector = ".list-checkbox:checked";
    $(".list-checkbox, #checkAll, #checkAllBottom").change(function() {
        if ($_mainList.find(listCheckedSelector).length > 0) {
            $_batchButtons.attr("disabled", false);
        }
        else {
            $_batchButtons.attr("disabled", true);
        }
    });

    // 获取选中多个id的数组
    var getMessageIdArray = function() {
        var batchIdsArray = [];
        var $_chosenTasks = $_mainList.find(listCheckedSelector);
        $_chosenTasks.each(function() {
            batchIdsArray.push($(this).val());
        });

        return batchIdsArray;
    };

    // 删除消息
    $_batchDelete.click(function() {
        // 获取选中的消息 和 多个id拼接的字符串
        var idArray = getMessageIdArray();
        var messageCount = idArray.length;
        var messageIds = idArray.join(",");
        if (messageCount == 0) {
            SQ.tips.warn({
                content: "你还没有选择任何消息！",
                cancel: true,
                cancelVal: "关闭"
            });
            return false;
        }

        SQ.tips.ask({
            title: "删除确认",
            content: "确认删除选中的 " + messageCount + " 条站内消息？",
            ok: function() {
                SQ.post({
                    url: "?action=Delete",
                    data: { messageIds: messageIds }
                });
            }
        });
    });
});