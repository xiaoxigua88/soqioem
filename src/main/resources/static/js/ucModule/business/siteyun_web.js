$(function() {
    $(".btn-generate").click(function() {
        var _id = $(this).data("id");
        SQ.tips.ask({
            title: "生成确认",
            content: "网站生成需要几秒钟的同步过程，视网络情况而定，请耐心等待！您确定生成吗？",
            ok: function() {
                SQ.get({
                    url: "webinfo.aspx?action=Generate" + "&yunId=" + jsData.site.YunId
                });
            }
        });
        return false;
    });

    $(".btn-save").click(function() {
        var $_form = $("#saveForm");
        if (!fieldEmptyValidate($_form)) {
            return false;
        }

        SQ.post({
            url: $_form.attr("action") + "&yunId=" + jsData.site.YunId,
            data: $_form.serialize()
        });
        return false;
    });

    $(".btn-delete").click(function() {
        var _id = $(this).data("id");
        SQ.tips.ask({
            title: "删除确认",
            content: "一旦删除将不可恢复，您确认执行删除操作吗？",
            ok: function() {
                SQ.get({
                    url: "?action=Delete" + "&yunId=" + jsData.site.YunId + "&id=" + _id
                });
            }
        });
        return false;
    });

    $(".banner_edit").click(function() {
        var _id = $(this).data("id");
        SQ.post({
            url: "?action=GetInfo",
            data: {
                yunId: jsData.site.YunId,
                id: _id
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "云建站管理-BANNER修改",
                    cancel: true,
                    content: template("importInformation", { template: jsData.template, model: json.model }),
                    init: function() {
                    },
                    ok: function() {
                        var $_form = $("#importForm");
                        if (!fieldEmptyValidate($_form)) {
                            return false;
                        }

                        SQ.post({
                            url: $_form.attr("action") + "&yunId=" + jsData.site.YunId,
                            data: $_form.serialize()
                        });
                        // 阻止对话框消失
                        return false;
                    }
                });
            }
        });
        return false;
    });

    $("#banner_import").click(function() {
        $.dialog({
            title: "云建站管理-BANNER添加",
            cancel: true,
            content: template("importInformation", { template: jsData.template }),
            init: function() {
            },
            ok: function() {
                var $_form = $("#importForm");
                if (!fieldEmptyValidate($_form)) {
                    return false;
                }

                SQ.post({
                    url: $_form.attr("action") + "&yunId=" + jsData.site.YunId,
                    data: $_form.serialize()
                });
                // 阻止对话框消失
                return false;
            }
        });
        return false;
    });

    $("#friendlink_import").click(function() {
        $.dialog({
            title: "云建站管理-友情链接导入",
            cancel: true,
            content: template("importInformation", {}),
            init: function() {
            },
            ok: function() {
                var $_form = $("#importForm");
                if (!fieldEmptyValidate($_form)) {
                    return false;
                }

                SQ.post({
                    url: $_form.attr("action") + "&yunId=" + jsData.site.YunId,
                    data: $_form.serialize()
                });
                // 阻止对话框消失
                return false;
            }
        });
        return false;
    });

    //上传图片绑定
    $(document).on("click", ".btn-upload", function() {
        var btnID = $(this).data("btn-id") || "";
        if (btnID === "") {
            while (btnID === "") {
                var randomBtnID = "btn_" + Math.random().toString().replace(".", "");
                if ($("[data-btn-id='" + randomBtnID + "']").length <= 0) {
                    btnID = randomBtnID;
                }
            }
            $(this).attr("data-btn-id", btnID);
        }
        var uploadForm = $(".upload-image-form");
        if (uploadForm.length <= 0) {
            var formHtml = "<form class=\"upload-image-form\" enctype=\"multipart/form-data\" style=\"display:none;\">"
                + '<input type="file" name="upfile" accept="image/jpeg,image/gif,image/png" />'
                + '</form>';
            uploadForm = $(formHtml);
            $(this).parents('form').after(uploadForm);
        }

        var uploadInput = uploadForm.find("input[name='upfile']");
        uploadInput.data("btn-id", $(this).data("btn-id"));
        uploadInput.val("");
        uploadInput.trigger("click");
        return false;
    });
    //上传图片控件
    $(document).on("change", ".upload-image-form input[name='upfile']", function() {
        var uploadInput = $(this);
        var imageFilePath = uploadInput.val();
        if (imageFilePath === "") {
            return false;
        }
        var uploadForm = uploadInput.parent();
        var srcButton = $("[data-btn-id='" + uploadInput.data('btn-id') + "']");
        var srcForm = srcButton.parents("form");
        var uploadPostData = {};
        uploadPostData.uptype = srcButton.data("uptype");
        uploadPostData.ismobi = srcButton.data("ismobi");
        SQ.waiting("正在上传，请稍候...");
        uploadForm.ajaxSubmit({
            type: "post",
            url: "webimage.aspx?action=UploadImage" + "&yunId=" + jsData.site.YunId,
            dataType: "json",
            data: uploadPostData,
            success: function(json) {
                SQ.hideWaiting();
                if (json.result) {
                    var tempImageUrl = json.url || json.text;
                    srcButton.siblings("input[name]").val(tempImageUrl);
                    var temp_jsonurl_arr = tempImageUrl.split('/');
                    srcButton.data("upload-guid", temp_jsonurl_arr[temp_jsonurl_arr.length - 1]);
                    srcButton.text("重新选择");
                }
                else {
                    SQ.warn(json.text || json.state);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                SQ.hideWaiting();
                SQ.showBusy();
            }
        });
        return false;
    });

    //初始化UE编辑器函数
    var initUEditor = function(tabGroupBtn) {
        var ueditorContainer = null;
        if (typeof tabGroupBtn === "undefined") {
            ueditorContainer = $(".ueditor");
            if (ueditorContainer.length <= 0) {
                return;
            }
        }
        else {
            var parent_selector = $(tabGroupBtn).parent().data("parent-selector");
            if (typeof parent_selector === "undefined" || parent_selector === "") {
                return;
            }
            ueditorContainer = $(tabGroupBtn).parents(parent_selector).find(".ueditor");
            if (ueditorContainer.length <= 0 || typeof ueditorContainer.data("sq-ueditor") === "undefined") {
                return;
            }
            else {
                if (ueditorContainer.data("sq-ueditor") === tabGroupBtn.data("tab_group")) {
                    ueditorContainer.parents(".form-group").removeClass("hide");
                }
                else {
                    ueditorContainer.parents(".form-group").addClass("hide");
                    return;
                }
            }
        }
        if (window.sq_ueditor_exists !== 1 && !ueditorContainer.hasClass("ueditor-dialog")) {
            UEDITOR_CONFIG.serverUrl = "webimage.aspx?yunId=" + jsData.site.YunId;

            ueditorContainer.append($("<script type=\"text/plain\" id=\"ueditor\" name=\"content\" style=\"height:450px;\"></script>"));
            var ueeditor_data_content = $("textarea[data-content]");
            var ue = UE.getEditor("ueditor");
            ue.ready(function() {
                if (ueeditor_data_content.length === 1) {
                    ue.setContent(ueeditor_data_content.text());
                }
                $(".sq-panel").css("height", "auto");
            });

            window.sq_ueditor_exists = 1;
            $(".sq-panel").css("height", "auto");
        }

    };
    //绑定数据到隐藏字段
    $(document).on("click", ".sq-btn-group [data-tab_group]", function() {
        var nameInput = $(this).siblings("input[name]");
        nameInput.val($(this).data("tab_group"));
        $("[data-sq-remove-disabled='" + nameInput.attr("name") + "']").prop("disabled", false);
        $("[data-sq-remove-disabled='" + nameInput.attr("name") + "'].trigger-change").addClass("clear-value");
        $("[data-sq-remove-disabled='" + nameInput.attr("name") + "'].trigger-change").trigger("change");
        initUEditor($(this));
        $(".btn-save").prop("disabled", false);
    });



    var askObj = {};
    askObj.dn_generate = { title: "网站生成确认", content: "确定要生成网站吗？", action: "dogeneratedn" };
    askObj.dn_delete = { title: "网站删除确认", content: "一旦删除将不可恢复，您确认删除该网站？", action: "dodeletedn" };
    askObj.article_delete = { title: "文章删除确认", content: "一旦删除将不可恢复，您确认删除该文章？", action: "dodeletearticle" };
    askObj.banner_delete = { title: "BANNER删除确认", content: "一旦删除将不可恢复，您确认删除该BANNER？", action: "dodeletebanner" };
    askObj.column_delete = { title: "栏目删除确认", content: "一旦删除将不可恢复，您确认删除该栏目？", action: "dodeletecolumn" };
    askObj.flink_delete = { title: "友情链接删除确认", content: "一旦删除将不可恢复，您确认删除该友情链接？", action: "dodeletefriendlink" };

    var saveObj = {};
    saveObj.doadddn = {
        fieldValidate: function() {
            if ($("[name='amid']").val() === "") {
                SQ.error("请选择客户！");
                return false;
            }
            if ($("[name='tplgid']").val() === "") {
                SQ.error("请选择模板！");
                return false;
            }
            if ($("[name='domain']").val() === "") {
                SQ.error("请输入域名！");
                return false;
            }
            return true;
        }
    };
    saveObj.dobatchadddn = {
        fieldValidate: function() {
            if ($("[name='amid']").val() === "") {
                SQ.error("请选择客户！");
                return false;
            }
            if ($("[name='tplgid']").val() === "") {
                SQ.error("请选择模板！");
                return false;
            }
            if ($("[name='project']").val() === "") {
                SQ.error("请选择项目！");
                return false;
            }
            if ($("[name='city']").length <= 0 || $("[name='domain']").length <= 0) {
                SQ.error("请构造域名！");
                return false;
            }
            return true;
        }
    };

    var editObj = {};
    editObj.ac_add = { title: "客服开户", template_id: "addInformation", action: "doaddaclerk" };
    editObj.ac_edit = { title: "客服资料编辑", template_id: "editInformation", action: "domodifyacinfo", url: window.ac_edit_url };
    editObj.am_add = { title: "客户开户", template_id: "addInformation", action: "doaddmember" };
    editObj.am_edit = { title: "客户资料编辑", template_id: "editInformation", action: "domodifyaminfo", url: window.am_edit_url };
    editObj.am_acconfig = { title: "分配客服", template_id: "configInformation", action: "dobindactoam", url: window.am_bind_ac_url };
    editObj.am_recharge = {
        title: "客户充值退款", template_id: "rechargeInformation",
        fieldValidate: function() {
            var rechargeAmount = parseInt($(".edit-form [name='amount']").val());
            if (isNaN(rechargeAmount) || rechargeAmount <= 0) {
                SQ.tips.error({ content: "金额为整数且大于0！" });
                return false;
            }
            return true;
        },
        ask: function(okFunction) {
            var rechargeAmount = $(".edit-form [name='amount']").val();
            var act = $(".edit-form [name='act']").val();
            var des = $(".edit-form [data-tab_group='" + act + "']").val();
            SQ.tips.ask({
                title: "账户【" + des + "】确认",
                content: "您将提交【" + des + "】金额<b class='text-red'>￥" + rechargeAmount + "</b>元，是否继续？",
                ok: function() {
                    okFunction();
                }
            });
        }
    };
    editObj.dn_choose_template = {
        title: "选择模板", template_id: "tplInformation", url: window.dn_choose_template_url,
        query: function() {
            var temp_query = [];
            temp_query.push("tplgid=" + ($("[name='tplgid']").val() || ""));
            temp_query.push("tplgid_exclude=" + ($("[data-name='tplgid']").val() || ""));
            temp_query.push("isbatchadd=" + ($("[data-sq-edit='dn_choose_template']").hasClass("batch-add-dn-template") ? 1 : 0));
            temp_query.push("amid=" + ($("[name='amid']").val() || $("[data-name='amid']").val() || ""));
            return temp_query.join("&");
        },
        ok: function() {
            var templateData = $(".tpl-model.selected").data();
            if (typeof templateData === "undefined") {
                return false;
            }
            var templateIDInput = $("[name='tplgid']");
            templateIDInput.val(templateData.value);
            templateIDInput.siblings("b").html("您选择的模板是：<b class='text-red'>" + templateData.name + templateData.funcstr + "</b>");
            var templateChooseBtn = templateIDInput.siblings("[data-sq-edit]");
            templateChooseBtn.text("重新选择模板");
            if (templateChooseBtn.hasClass("trigger-change")) {
                templateChooseBtn.data("template", templateData);
                templateChooseBtn.trigger("change");
            }
        }
    };
    editObj.dn_choose_project = {
        title: "选择项目", template_id: "projectInformation", url: window.dn_choose_project_url, query: function() {
            var temp_query = [];
            temp_query.push("amid=" + ($("[name='amid']").val() || $("[data-name='amid']").val() || ""));
            temp_query.push("project=" + encodeURIComponent($("[name='project']").val() || ""));
            temp_query.push("keywordlv1=" + encodeURIComponent($("[name='keywordlv1']").val() || ""));
            temp_query.push("keywordlv2=" + encodeURIComponent($("[name='keywordlv2']").val() || ""));
            return temp_query.join("&");
        },
        ok: function() {
            var tempData = $("[name='project_radio']:checked").data();
            if (typeof tempData === "undefined") {
                return false;
            }
            var tempProject = "";
            var tempKeywordLv1 = "";
            var tempKeywordLv2 = "";
            if (tempData.value === "") {
                tempProject = $(".sq-new-project").eq(0).val();
                tempKeywordLv1 = $(".sq-new-project").eq(1).val();
                tempKeywordLv2 = $(".sq-new-project").eq(2).val();
                if (tempProject === "" || tempKeywordLv1 === "" || tempKeywordLv2 === "") {
                    return false;
                }
            }
            else {
                tempProject = tempData.value;
            }
            $("[name='keywordlv1']").val(tempKeywordLv1);
            $("[name='keywordlv2']").val(tempKeywordLv2);
            $("[name='project']").val(tempProject);
            $("[name='project']").siblings("b").html("您选择的项目是：<b class='text-red'>" + tempProject + "</b>");
            $("[name='project']").siblings("[data-sq-edit]").text("重新选择项目");
        }
    };
    editObj.dn_build_domain = {
        title: "构造域名", template_id: "domainInformation", getTemplateData: function() {
            var tempCityList = jsData.cityList;
            var tempBuildCityList = [];
            $("[name='city']").each(function() {
                tempBuildCityList.push($(this).val());
            });
            var tempBuildDomainList = [];
            $("[name='domain']").each(function() {
                tempBuildDomainList.push($(this).val());
            });
            return { cityList: jsData.cityList, buildCityList: tempBuildCityList, buildDomainList: tempBuildDomainList };
        },
        ok: function() {
            var cityArray = [];
            var domainArray = [];
            $("[data-city]:checked").each(function() {
                var cityData = $(this).data();
                var domain = $("[data-domain-city='" + cityData.city + "']").val();
                if (domain !== "") {
                    cityArray.push("<input type=\"hidden\" name=\"city\" value=\"" + cityData.city + "\" />");
                    domainArray.push("<input type=\"hidden\" name=\"domain\" value=\"" + domain + "\" />");
                }
            });
            if (cityArray.length <= 0) {
                return false;
            }
            $(".domain-container").html(cityArray.join("") + domainArray.join(""));
            $(".domain-container").siblings("b").html("您生成了<b class='text-red'>" + domainArray.length + "个</b>域名");
            $(".domain-container").siblings("[data-sq-edit]").text("重新生成域名");
        }
    };
    editObj.dn_vps = { title: "更换vps", template_id: "vpsInformation", action: "dochangevps", url: window.dn_vps_url };
    editObj.dn_template = { title: "更换模板", template_id: "templateInformation", action: "dochangetpl", url: window.dn_template_url };
    editObj.dn_ac = { title: "更换客服", template_id: "acInformation", action: "dochangednaclerk", url: window.dn_ac_url };
    editObj.flink_add = { title: "添加友情链接", template_id: "addFriendLink", action: "doaddfriendlink" };
    editObj.flink_edit = { title: "编辑友情链接", template_id: "modifyFriendLink", action: "domodifyfriendlink", url: window.flink_edit_url };

    editObj.dn_batch_draw = { title: "批量提单网站", template_id: "dnBatchDraw", action: "dodnbatch" };
    editObj.dn_batch_generate = { title: "批量生成网站", template_id: "dnBatchGenerate", action: "dodnbatch" };
    editObj.dn_batch_template = { title: "批量更换网站模板", template_id: "dnBatchTemplate", action: "dodnbatch", url: window.dn_batch_url };
    editObj.dn_batch_vps = { title: "批量更换网站Vps", template_id: "dnBatchVps", action: "dodnbatch", url: window.dn_batch_url };
    editObj.dn_batch_ac = { title: "批量更换网站客服", template_id: "dnBatchAc", action: "dodnbatch", url: window.dn_batch_url };
    editObj.dn_batch_delete = { title: "批量删除网站", template_id: "dnBatchDelete", action: "dodnbatch" };
    editObj.dn_batch_info = { title: "批量修改网站详情", template_id: "dnBatchInfo", action: "dodnbatch" };
    editObj.dn_batch_logo = { title: "批量修改网站LOGO", template_id: "dnBatchLogo", action: "dodnbatch" };
    editObj.dn_batch_banner = { title: "批量修改网站BANNER", template_id: "dnBatchBanner", action: "dodnbatch" };
    editObj.dn_batch_switch = { title: "批量修改网站开关", template_id: "dnBatchSwitch", action: "dodnbatch" };

    //编辑对话框函数
    var editDialog = function(tempEditObj, templateData) {
        $.dialog({
            title: tempEditObj.title,
            cancel: true,
            okVal: "保存",
            cancelVal: "关闭",
            content: template(tempEditObj.template_id, templateData),
            init: tempEditObj.init || function() {
                if ($(".sq-btn-group").length > 0) {
                    SQ.component.initTabs();
                }
                if ($(".frm-domain").length > 0) {
                    SQ.component.initDropDown({
                        containerSelector: ".frm-domain"
                    });
                }
                if ($("img.lazy").length > 0) {
                    $("img.lazy").lazyload({ effect: "fadeIn", container: $(".aui_content")[0] });
                }
                $(".tpl-model.selected").trigger("scrollIntoView", true);
                if (tempEditObj.template_id.indexOf("dnBatch") === 0) {
                    var domainArray = [];
                    $("table .list-checkbox:checked").each(function() {
                        domainArray.push($(this).val());
                    });
                    $("textarea.batch-domain").val(domainArray.join("\n"));
                }
            },
            ok: tempEditObj.ok || function() {
                var submitForm = $(".edit-form");
                if (!fieldEmptyValidate(submitForm)) {
                    return false;
                }
                if (typeof tempEditObj.fieldValidate === "function") {
                    if (!tempEditObj.fieldValidate()) {
                        return false;
                    }
                }
                var doActUrl = _do_act;
                if (typeof tempEditObj.action === "function") {
                    doActUrl += "?act=" + tempEditObj.action();
                }
                if (typeof tempEditObj.action === "string") {
                    doActUrl += "?act=" + tempEditObj.action;
                }
                if (typeof tempEditObj.ask === "function") {
                    tempEditObj.ask(function() {
                        SQ.post({
                            url: doActUrl,
                            data: submitForm.serialize()
                        });
                    });
                }
                else {
                    SQ.post({
                        url: doActUrl,
                        data: submitForm.serialize()
                    });
                }
            }
        });
    };

    //显示批处理结果
    window.showBatchResult = function(resultArray) {
        var html = '';
        html += '<table class="sq-table margin-bottom-20" style="width:400px;overflow-y:auto;overflow-x:hidden;">';
        html += '   <tr>';
        html += '       <th style="width:120px;">域名</th>';
        html += '       <th style="width:200px;">结果</th>';
        html += '       <th></th>';
        html += '   </tr>';
        for (var i = 0; i < resultArray.length; i++) {
            html += "   <tr>";
            html += '       <td>' + resultArray[i].domain + '</td>';
            html += '       <td>' + resultArray[i].message + '</td>';
            html += '       <td></td>';
            html += "   </tr>";
        }
        html += '</table>';
        $(".batch-result").css("height", "400px");
        $(".batch-result").css("overflow-y", "auto");
        $(".batch-result").css("over-flow-x", "hidden");
        $(".batch-result").css("height", "400px");
        $(".batch-result").html(html);
    };

    $(document).on("change", ".trigger-change", function() {
        if ($(this).hasClass("batch-add-dn-template")) {
            var templateData = $(this).data("template") || {};
            if ($(this).siblings("[name]").val() === templateData.value) {
                return;
            }
            if ($(this).hasClass("clear-value")) {
                $(this).text($(this).data("init-text") || "");
                $(this).siblings("[name]").val("");
                $(this).siblings("b").html("");
                $(this).data("template", {});
                $(this).removeClass("clear-value");
                $("[data-image-type]").prop("disabled", true);
                $("[data-image-type]").text("选择图片");
                $("[data-image-type]").siblings("[name]").val("");
                $("[data-image-type]").siblings("b").html("");
            }
            else {
                var imageTypeArray = ["logo", "column", "banner", "article"];
                for (var i = 0; i < imageTypeArray.length; i++) {
                    var imageType = imageTypeArray[i];
                    var sizeStr = templateData[imageType + "size"] || "";
                    var mobiSizeStr = templateData["mobi" + imageType + "size"];
                    $("[data-image-type='" + imageType + "']").each(function() {
                        $(this).siblings("[name='" + imageType + "imageurl']").siblings("b").html(sizeStr === "" ? sizeStr : "尺寸：<b class='text-red'>" + sizeStr + "</b>");
                        $(this).siblings("[name='" + imageType + "mobiimageurl']").siblings("b").html(mobiSizeStr === "" ? mobiSizeStr : "尺寸：<b class='text-red'>" + mobiSizeStr + "</b>");
                    });
                }
                if (templateData.funcstr.indexOf("自适应") > -1) {
                    $("[data-image-type]").not("[data-is-mobile]").prop("disabled", false);
                    $("[data-image-type][data-is-mobile]").prop("disabled", true);
                }
                else {
                    $("[data-image-type]").prop("disabled", false);
                }
                $("[data-image-type]").text("选择图片");
                $("[data-image-type]").siblings("[name]").val("");
            }
        }
    });
    //提交form
    $(document).on("click", "[data-btn-save]", function() {
        var submitForm = $(this).parents("form");
        if (!fieldEmptyValidate(submitForm)) {
            return false;
        }
        var action = $(this).data("btn-save");
        var tempSaveObj = saveObj[action];
        if (tempSaveObj && typeof tempSaveObj.fieldValidate === "function") {
            if (!tempSaveObj.fieldValidate()) {
                return false;
            }
        }
        SQ.post({
            url: _do_act + "?act=" + action,
            data: submitForm.serialize()
        });
        return false;
    });
    //操作确认
    $(document).on("click", "[data-sq-ask]", function() {
        var askOp = $(this).data("sq-ask");
        var tempAskObj = askObj[askOp];
        var askQuery = $(this).data("sq-ask-query");
        SQ.tips.ask({
            title: tempAskObj.title,
            content: tempAskObj.content,
            ok: function() {
                SQ.post(_do_act + "?act=" + tempAskObj.action + "&" + askQuery);
            }
        });
        return false;
    });
    //弹窗编辑
    $(document).on("click", "[data-sq-edit]", function() {
        var tempEditObj = editObj[$(this).data("sq-edit")];
        var tempQuery = $(this).data("sq-edit-query") || (tempEditObj.query ? tempEditObj.query() : "") || "";
        if (tempEditObj.url) {
            SQ.get({
                url: tempEditObj.url + (tempQuery === "" ? tempQuery : "?" + tempQuery),
                isCoverSuccess: true,
                success: function(json) {
                    if (json.result === false) {
                        SQ.tips.error({ content: json.text });
                        return;
                    }
                    editDialog(tempEditObj, json);
                }
            });
        }
        else {
            var tempJson = tempEditObj.getTemplateData ? tempEditObj.getTemplateData() : {};
            if (tempQuery.length > 0) {
                var splitArray = tempQuery.split('&');
                for (var i = 0; i < splitArray.length; i++) {
                    var loopItem = splitArray[i];
                    var fi = loopItem.indexOf('=');
                    tempJson[loopItem.substring(0, fi)] = loopItem.substring(fi + 1);
                }
            }
            editDialog(tempEditObj, tempJson);
        }
        return false;
    });
    //弹窗tab页切换
    $(document).on("click", ".sq-tab[data-control]", function() {
        var control = $(this).data("control");
        $.each($(".sq-tab[data-control]"), function(i, n) {
            if (control === $(n).data("control")) {
                $(n).addClass("sq-tab--current");
            }
            else {
                $(n).removeClass("sq-tab--current");
            }
        });
        $.each($(".sq-tab-view[data-control]"), function(i, n) {
            if (control === $(n).data("control")) {
                $(n).removeClass("hide");
            }
            else {
                $(n).addClass("hide");
            }
        });
    });
    //自动聚焦
    $(document).on("focus", "[autofocus]", function() {
        initUEditor();
    });
    //批建项目选择
    $(document).on("click", "[name='project_radio']", function() {
        if ($(this).data("value") === "") {
            $(".sq-new-project").removeAttr("readonly");
            $(".sq-new-project").addClass("necessary");
            $(".sq-new-project").siblings(".necessary-mark").text("*");
        }
        else {
            $(".sq-new-project").attr("readonly", true);
            $(".sq-new-project").val("");
            $(".sq-new-project").siblings(".necessary-mark").html("&nbsp;");
            $(".sq-new-project").removeClass("necessary");
        }
    });
    //勾选城市
    $(document).on("click", "[data-city]", function() {
        var tempDomainInput = $("[data-domain-city='" + $(this).data("city") + "']");
        if ($(this).is(":checked")) {
            tempDomainInput.removeAttr("readonly");
        }
        else {
            tempDomainInput.attr("readonly", true);
            tempDomainInput.val("");
        }
    });
    //构造域名
    $(document).on("click", "[data-batch-build-domain='build']", function() {
        var buildPre = $("[data-batch-build-domain='pre']").val() || "";
        var buildCity = $("[data-batch-build-domain='city'] .sq-drop-active").data("drop") || "";
        var buildSuffix = $("[data-batch-build-domain='suffix']").val() || "";
        var buildRoot = $("[data-batch-build-domain='root']").val() || "";
        var buildCount = parseInt($("[data-batch-build-domain='count']").val() || "0");
        if (buildPre === "" && buildSuffix === "" || buildRoot === "" || isNaN(buildCount) || buildCount <= 0) {
            return false;
        }
        var tempCityArray = $("[data-city]");
        if (buildCount > tempCityArray.length) {
            buildCount = tempCityArray.length;
        }
        $("[data-city]:checked").trigger("click");
        for (var i = 0; i < buildCount; i++) {
            var cityInput = tempCityArray.eq(i);
            var cityData = cityInput.data();
            cityInput.trigger("click");
            $("[data-domain-city='" + cityData.city + "']").val(buildPre + cityData[buildCity] + buildSuffix + buildRoot);
        }
        return false;
    });
    //克隆元素
    $(document).on("click", "[data-batch-add-clone]", function() {
        var tempCloneType = $(this).data("batch-add-clone");
        var tempCloneDiv = $($(this).parents(".div_clone").prop("outerHTML"));

        tempCloneDiv.find("[name]").val("");
        tempCloneDiv.find("[data-image-type]").text("选择图片");
        tempCloneDiv.find("[data-btn-id]").removeAttr("data-btn-id");
        tempCloneDiv.find("[data-textarea-id]").removeAttr("data-textarea-id");
        tempCloneDiv.find("[data-sq-default-value]").each(function() {
            $(this).val($(this).data("sq-default-value"));
        });
        tempCloneDiv.find("[data-sq-default-class]").each(function() {
            $(this).addClass($(this).data("sq-default-class"));
        });
        tempCloneDiv.find(".sq-tab-selected").removeClass("sq-tab-selected");

        $(this).parents(".div_clone").after(tempCloneDiv);
        SQ.component.initTabs();
        $("[data-batch-delete-clone='" + tempCloneType + "']").removeClass("hide");
        return false;
    });
    //删除克隆
    $(document).on("click", "[data-batch-delete-clone]", function() {
        var tempCloneType = $(this).data("batch-delete-clone");
        if ($("[data-batch-delete-clone='" + tempCloneType + "']").length > 1) {
            var tempDeleteBtn = $(this);
            SQ.tips.ask({
                title: "网站" + tempCloneType + "删除确认",
                content: "您确认删除该" + tempCloneType + "吗？",
                ok: function() {
                    tempDeleteBtn.parents(".div_clone").remove();
                    if ($("[data-batch-delete-clone='" + tempCloneType + "']").length <= 1) {
                        $("[data-batch-delete-clone='" + tempCloneType + "']").addClass("hide");
                    }
                }
            });
        }
        else {
            SQ.error("网站" + tempCloneType + "必须保留一个配置");
            $("[data-batch-delete-clone='" + tempCloneType + "']").addClass("hide");
        }
        return false;
    });
    //全选
    $(document).on("click", "table .sq-check-all", function() {
        var checked = $(this).is(":checked");
        $("table .sq-check-all").prop("checked", checked);
        $("table .list-checkbox").prop("checked", checked);
    });
    //选中
    $(document).on("click", "table .list-checkbox", function() {
        $("table .sq-check-all").prop("checked", $("table .list-checkbox").length === $("table .list-checkbox:checked").length);
    });
    //鼠标进入模板
    $(document).on("mouseover", ".tpl-model", function() {
        $(this).children().find(".tpl-mask").removeClass("hide");
        $(this).children().find(".tpl-name").addClass("hide");
    });
    //鼠标离开模板
    $(document).on("mouseleave", ".tpl-model", function() {
        if ($(this).children(".tpl-select").hasClass("hide")) {
            $(this).children().find(".tpl-mask").addClass("hide");
            $(this).children().find(".tpl-name").removeClass("hide");
        }
    });
    //鼠标点击模板
    $(document).on("click", ".tpl-model", function() {
        e = window.event || e;
        if ($(e.srcElement || e.target).is("a")) return;
        $(".tpl-model").removeClass("selected");
        $(".tpl-name").removeClass("hide");
        $(".tpl-mask").addClass("hide");
        $(".tpl-select").addClass("hide");

        $(this).addClass("selected");
        $(this).children().find(".tpl-name").addClass("hide");
        $(this).children().find(".tpl-mask").removeClass("hide");
        $(this).children(".tpl-select").removeClass("hide");
    });
    $(document).on("click", ".btn-group-search ul li a", function() {
        $(this).parents(".btn-group-search").prev().find("span").text($(this).text());
        $(this).parents("ul").siblings("[name]").val($(this).data("st"));
        $(this).parents(".btn-group-search").next().val("");
    });
});