$(function() {
    //表单验证
    $.formValidator.initConfig({
        formID: "register",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            $(".reg-submit").attr("disabled", true);
            SQ.waiting("正在提交，请稍后……");
            $("#register").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    var json = (typeof data == "string") ? eval('(' + data + ')') : data;
                    if (json.result) {
                        SQ.success(json.text, 2, function() {
                            window.location = 'registerok.aspx';
                        });
                    } else {
                        SQ.warn(json.text, 2, function() {
                            $(".reg-submit").attr("disabled", false);
                        });
                    }
                }
            });
        },
        ajaxPrompt: '有数据正在异步验证，请稍等...'
    });

    //资料填写验证
    $("#callName").formValidator({
        onShow: "请输入您的称呼",
        onFocus: "请输入您的称呼",
        onCorrect: "填写正确"
    }).inputValidator({
        min: 4,
        max: 20,
        onError: "请输入2~10个汉字"
    }).regexValidator({
        regExp: regexEnum.chinese,
        onError: "请填写中文名称"
    });
    $("#qq").formValidator({
        onShow: "请输入您的QQ号码",
        onFocus: "请输入您的QQ号码",
        onCorrect: "填写正确"
    }).regexValidator({
        regExp: regexEnum.qq,
        onError: "你输入的QQ号码不正确"
    });
    var $email = $("#email");
    $email.formValidator({
        onShow: "邮箱用于登录，激活账号、及密码找回", onFocus: "邮箱用于登录，激活账号、及密码找回", onCorrect: "填写正确"
    }).inputValidator({
        min: 6, max: 100, onError: "邮箱填写错误"
    }).regexValidator({
        regExp: regexEnum.email,
        onError: "你输入的邮箱格式不正确"
    }).ajaxValidator({
        type: "POST",
        dataType: "json",
        async: true,
        url: "ajax.aspx?action=CheckEmail",
        success: function(data) {
            if (data.result) {
                return true;
            } else {
                return false;
            }
            return false;
        },
        buttons: $(".reg-submit"),
        error: function(jqXHR, textStatus, errorThrown) {
            SQ.showBusy();
        },
        onError: "该邮箱不可用",
        onWait: "请稍候..."
    });
    $("#answer").formValidator({
        onShow: "请输入您的密码保护答案",
        onFocus: "请输入您的密码保护答案",
        onCorrect: "填写正确"
    }).inputValidator({
        min: 1,
        max: 15,
        onError: "答案格式填写不正确"
    }).regexValidator({
        regExp: "^\\S+$",
        onError: "答案不能包含空格"
    });

    //自动完成
    var suggestDomainList = [
		"@qq.com",
		"@163.com",
		"@126.com",
		"@sina.com",
		"@sina.cn",
		"@gmail.com",
		"@hotmail.com",
		"@yeah.net",
		"@sohu.com",
		"@21cn.com",
		"@sogou.com",
		"@51uc.com",
		"@outlook.com",
		"@live.com"
	];
    $email.autocomplete({
        source: function(request, response) {
            var term = request.term;

            var termSplit = term.split("@");
            var username = termSplit[0];
            var domainName = termSplit[1] || "";


            var dataList = $.map(suggestDomainList, function(suggestDomain) {
                if (suggestDomain.indexOf(domainName) != -1) {
                    return {
                        term: term,
                        inputUsername: username,
                        inputDomainName: domainName,
                        // value 为了在input中显示
                        value: username + suggestDomain,
                        // cut '@'
                        suggestDomain: suggestDomain.slice(1)
                    };
                }
                else {
                    return;
                }
            });


            response(dataList.slice(0, 10));
        }
    });

    var TEXT_HIGHLIGHT_CLASSNAME = "ui-autocomplete-text-highlight";
    $email.autocomplete("instance")._renderItem = function(ul, item) {
        var $_li = $("<li>");
        var userInputValue = item.term;
        var inputDomainName = item.inputDomainName;
        var suggestDomain = item.suggestDomain;

        // append highlight username
        var $_inputUsernameSpan = $("<span>").addClass(TEXT_HIGHLIGHT_CLASSNAME).html(item.inputUsername);
        $_li.append($_inputUsernameSpan);

        var $_atSpan = $("<span>").html("@");
        // 当用户输入'@'时，将@也高亮
        if (userInputValue.indexOf("@") != -1) {
            $_atSpan.addClass(TEXT_HIGHLIGHT_CLASSNAME);
        }
        $_li.append($_atSpan);

        // 为 suggestDomain 部分 进行高亮设置
        $.each(suggestDomain.split(""), function(i, charValue) {
            var $_charSpan = $("<span>").html(charValue);

            if (inputDomainName.indexOf(charValue) != -1) {
                $_charSpan.addClass(TEXT_HIGHLIGHT_CLASSNAME);
            }

            $_li.append($_charSpan);
        });
        $_li.appendTo(ul);

        return $_li;
    };
});

