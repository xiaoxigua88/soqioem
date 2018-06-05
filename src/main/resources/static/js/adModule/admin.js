$(function() {
    var bind_user_view = function() {
        //用户信息查看
        $('.user-view').unbind("click");
        $(".user-view").click(function() {
            var userId = $(this).data("userid");
            var control = $(this).data("control");
            SQ.post({
                url: jsData.common.domain.uc + "/userinfo/userlist.aspx?action=UserView",
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
                        title: "用户信息概况",
                        cancel: true,
                        cancelVal: "关闭",
                        content: template("userViewTpl", { user: json.user, verify: json.verify, company: json.company, api: json.api, count: json.count, logList: json.logList, accountList: json.accountList, goldList: json.goldList }),
                        init: function() {
                            $(".sq-user-tab").click(function() {
                                $(".sq-user-tab").removeClass("sq-tab--current");
                                $(this).addClass("sq-tab--current");
                                $(".sq-userview").removeClass("hide");
                                $(".sq-userview").hide();
                                $("#" + $(this).attr("control")).show();
                            });
                            if (typeof control != "undefined") {
                                $(".sq-user-tab[control='" + control + "']").trigger("click");
                            }
                            else {
                                $(".sq-user-tab[control='userview']").trigger("click");
                            }
                        }
                    });
                }
            });
            return false;
        });
    };
    bind_user_view();
    // 云站通查看
    $(".btn-webinfoview").click(function() {
        SQ.post({
            url: jsData.common.domain.uc + "/business/siterent/webinfo.aspx?action=View",
            data: {
                webId: $(this).data("webid")
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: "云站通查看",
                    cancel: true,
                    cancelVal: "关闭",
                    content: template("webInfoViewTpl", { site: json.site, orderList: json.orderList, costList: json.costList }),
                    init: function() {
                        $(".sq-webinfo-tab").click(function() {
                            $(".sq-webinfo-tab").removeClass("sq-tab--current");
                            $(this).addClass("sq-tab--current");
                            $(".sq-siterentview").removeClass("hide");
                            $(".sq-siterentview").hide();
                            $("#" + $(this).attr("control")).show();
                        });
                        bind_user_view();
                    }
                });
            }
        });
        return false;
    });
});

$(function(){
    $(".btn-operate-add1").click(function() {
        $.dialog({
            title: "云建站手工添加",
            cancel: true,
            content: template("editInformation", {}),
            init: function() {
                // 初始化sq-btn-group
                SQ.component.initTabs();
                $("#fromapiBtn input").click(function() {
                    if ($(this).data("fromapi")) {
                        $("#readOnlyBtn input").prop("disabled", false);
                    }
                    else {
                        $($("#readOnlyBtn input")[1]).trigger("click");
                        $("#readOnlyBtn input").prop("disabled", true);
                    }
                    $("#fromapi").val($(this).data("fromapi"));
                });
                $("#readOnlyBtn input").click(function() {
                    $("#readOnly").val($(this).data("readonly"));
                });
            },
            ok: function() {
                var $_form = $("#importForm");
                if (fieldEmptyValidate($_form)) {
                    SQ.post({
                        url: $_form.attr("action"),
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
                }
                // 阻止对话框消失
                return false;
            }
        });
    });
})
