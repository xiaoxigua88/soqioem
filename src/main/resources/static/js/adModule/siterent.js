$(function() {
    // 初始化全选方法
    SQ.biz.chooseAllInTable("#checkAll", {
        checkboxGroupSelector: "#checkAllBottom",
        parmContainerSelector: "#mainList"
    });
    SQ.biz.chooseAllInTable("#checkAllBottom", {
        parmContainerSelector: "#mainList"
    });

    var idDataName = "webid";

    // 批量操作按钮解除禁用
    var $_mainList = $("#mainList");
    var $_batchPublish = $("#batchPublish");
    var $_batchSeoing = $("#batchSeoing");
    var $_batchStop = $("#batchStop");
    var $_batchResetPrice = $("#batchResetPrice");
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
    // 批量操作按钮 的点击事件生成方法
    /* operateName：操作名称 */
    /*  url：响应地址 */
    /*  checkType：选中类型 1,必须选择；2,对选择有效未选择响应所有,3,不关注是否选择 */
    var createBatchHandle = function(operateName, url, checkType) {
        return function() {
            // 获取选中的任务的数量 和 多个id拼接的字符串
            var taskCount = SQ.getTaskIdArray().length;
            var webIds = SQ.getTaskIdArray().join(",");
            if (taskCount == 0 && checkType == 1) {
                SQ.tips.warn({
                    content: "你还没有选择任何记录！",
                    cancel: true,
                    cancelVal: "关闭"
                });
                return false;
            }
            var data = { webIds: webIds };
            if (taskCount == 0 || checkType == 3) {
                data = $("#searchForm").serialize();
            }

            SQ.tips.ask({
                title: operateName + "确认",
                content: "确认" + operateName + (taskCount == 0 || checkType == 3 ? "所有符合条件的" : "选中的 <b class='text-red'>" + taskCount + "</b> 条") + "记录？",
                ok: function() {
                    SQ.post({
                        data: { webIds: webIds },
                        url: url + (location.search.length > 0 ? "&" + location.search.substr(1) : "")
                    });
                }
            });
        };
    };

    // 批量发布
    $_batchPublish.click(createBatchHandle("发布", "?action=Publish", 1));
    // 批量建设
    $_batchSeoing.click(createBatchHandle("建设", "?action=Seoing", 1));
    // 批量停止
    $_batchStop.click(createBatchHandle("停止", "?action=Stop", 1));
    // 批量重置价格
    $_batchResetPrice.click(createBatchHandle("重置价格", "?action=ResetPrice", 1));

    //删除云站通预约
    $(".btn-visitordel").click(function() {
        var visitorid = $(this).data("visitorid");
        SQ.tips.ask({
            title: "云站通预约删除确认",
            content: "您确定删除该访客的云站通预约？",
            ok: function() {
                SQ.post({
                    data: { visitorid: visitorid },
                    url: "?action=VisitorDel"
                });
            }
        });
    });

    $(".btn-webpic").click(function() {
        $.dialog({
            title: "云站通效果图",
            cancelVal: "关闭",
            content: template("webInfoPicTpl", { path: $(this).data("path"), webId: $(this).data("webid") }),
            cancel: function() {
                if ($("#authPic").val().length > 0) {
                    window.location.reload();
                }
            },
            init: function() {
                // 上传附件照片初始化方法
                /**
                * 封装上传照片初始化方法，为多个可以触发上传动作的元素绑定相同的上传处理逻辑。
                * @param browseButton {string|HTMLElement|jQueryObject} 可以触发上传动作的元素
                * @param previewImg {string|HTMLElement|jQueryObject} 预览图元素选择器
                * @param inputToFill {string|HTMLElement|jQueryObject} 需要填充的表单
                initPlUpload(".upload-attachment-front", "#previewImgFront", "#authPic");
                */
                var initPlUpload = function(browseButton, previewImg, inputToFill) {
                    var $_previewImg = $(previewImg);
                    var $_browseButton = $(browseButton);

                    $_browseButton.each(function() {
                        var $_previewImgParent = $_previewImg.parents(".preview-container");
                        var $_errorReminder = $_previewImgParent.find(".error-reminder");
                        var $_uploadReminder = $_previewImgParent.find(".upload-reminder");
                        var uploader = SQ.plupload.createUploader({
                            // 专用上传地址
                            url: "webinfo.aspx?action=Upload&webId=" + $_browseButton.data("webid"),
                            // this为browseButton集合中的元素
                            browse_button: this,
                            filters: {
                                mime_types: [{ title: "Image files", extensions: "jpg"}],
                                max_file_size: "5mb"
                            },
                            // 监听添加文件的事件
                            onFilesAdded: function(uploader, file) {
                                $_browseButton.attr("disabled", true);
                                $_errorReminder.empty();
                                // 显示loading gif，并显示上传中提示语
                                $_previewImgParent.addClass("loading-show");
                                $_previewImgParent.find(".upload-reminder").html("上传中").addClass("upload-loading");
                            },
                            // 进行中的事件
                            onUploadProgress: function(uploader, file) {
                            },
                            // 上传完成回调
                            onFileUploaded: function(responseJSON, uploader, file, responseObject) {
                                $_previewImgParent.removeClass("loading-show");
                                $_browseButton.attr("disabled", false);
                                if (responseJSON.result) {
                                    // 展示缩略图，替换为返回的图片地址
                                    SQ.plupload.previewImage(file, function(imgsrc) {
                                        $_previewImg.attr("src", imgsrc);
                                    }, {
                                        // ie8使用在线地址进行预览
                                        unpreviewCallback: function(file) {
                                            $_previewImg.attr("src", SQ.constant.UPLOAD_FILE_IMG_PREFIX + responseJSON.url);
                                        }
                                    });
                                    $_uploadReminder.html("更换图片").removeClass("upload-loading");
                                    $_errorReminder.empty();
                                    // 填写表单
                                    $(inputToFill).val(responseJSON.url);
                                }
                                else {
                                    $_errorReminder.html(responseJSON.text);
                                    $_uploadReminder.html("重新上传");
                                }

                            }
                        });
                        // 上传错误回调（500）
                        uploader.bind("Error", function(uploader, errObject) {
                            $_browseButton.attr("disabled", false);
                            $_previewImgParent.removeClass("loading-show");
                            $_uploadReminder.html("重新上传");
                            // 提示 繁忙
                            $_errorReminder.html(SQ.constant.UPLOAD_FILE_ERROR_TEXT);
                        });
                    });

                };
                // 依次初始化点击上传图片触发器
                initPlUpload(".upload-attachment-front", "#previewImgFront", "#authPic");
            }
        });
        return false;
    });

});