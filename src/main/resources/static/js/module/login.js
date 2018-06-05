$(function() {
    //创建随机数，实现背景墙随机轮换
    var createStartIndex = function() {
        var indexCeiling = $(".pattern-area").children().length;
        return Math.floor(Math.random() * indexCeiling);
    };

    var fadeInDuration = 800;
    var bgContainer = $(".bg-back");
    var $bgSwitchItem = bgContainer.children();
    patternSwitch($(".pattern-area"), {
        indexContainer: $(".bg-change"),
        switchItemCurrentClass: "switch--current",
        indexPointerCurrentClass: "bg-change--current",
        indexUlClass: "",
        indexPointClass: "",
        switchInterval: 7000,
        fadeInDuration: fadeInDuration,
        startIndex: createStartIndex(),
        beforeSwitchHandle: function(index) {

            $bgSwitchItem.fadeOut(fadeInDuration);

            if (index == 0) {
                fadeInDuration = 200;
            } else {
                fadeInDuration *= 0.6;
            }
            $bgSwitchItem.eq(index).fadeIn(fadeInDuration);

        },
        initSwitchHandle: function(index) {
            $bgSwitchItem.hide();
            $bgSwitchItem.eq(index).show();
        }
    });
    $(".show-captcha")[0].click();

    //表单验证
    $.formValidator.initConfig({
        formID: "loginForm",
        theme: "ArrowSolidBox",
        debug: false,
        submitOnce: true,
        onSuccess: function() {
            SQ.waiting("正在登录，请稍候...");
            $("#loginForm").ajaxSubmit({
                success: function(data) {
                    SQ.hideWaiting();
                    if (data.code=='0') {
                        SQ.success(data.msg, 2, function() { window.location = "/client/main"; });
                    } else {
                        SQ.warn(data.msg, 2, function() {
                            if (data.msg) {
                                $("#" + data.msg).val("");
                                $("#" + data.msg).focus();
                            }
                            $(".show-captcha")[0].click();
                            $("#verifyCode").val("");
                        });
                    }
                },
                error: function() {
                    SQ.hideWaiting();
                    SQ.showBusy();
                }
            });
        }
    });
    $("#userName").formValidator().inputValidator({ min: 4, max: 20 }).regexValidator({ regExp: regexEnum.username });
    $("#password").formValidator().inputValidator({ min: 8, max: 20 }).regexValidator({ regExp: regexEnum.pwd1 });
    //$("#verifyCode").formValidator().inputValidator({ min: 5, max: 5 }).regexValidator({ regExp: regexEnum.letnum });

    //输入框focus样式
    $("#loginForm").on("focus", ".input-focus-handle", function() {
        $(this).parent().addClass("input-outer--focus");
    }).on("blur", ".input-focus-handle", function() {
        $(this).parent().removeClass("input-outer--focus");
    });

    //回车提交
    $('#verifycode').keydown(function(e) {
        if (e.keyCode == 13) {
            $_loginSubmit.click();
            return false;
        }
    });

    // 用户名的邮箱列表自动完成
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
    var usernameInput = $("#userName");
    usernameInput.autocomplete({
        appendTo: "#emailAutoComplete",
        source: function(request, response) {
            var term = request.term;

            // 只有当用户输入非数字（即不使用手机和会员ID），才提醒邮箱后缀列表
            if ($.isNumeric(term)) {
                response([]);
                return;
            }

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
    usernameInput.autocomplete("instance")._renderItem = function(ul, item) {
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