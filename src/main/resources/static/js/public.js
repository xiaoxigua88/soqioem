/**
 * Created by Administrator on 2018/5/4.
 */
// 封装工具类方法
$(function() {
    window.SQ = window.SQ || {};

    // add feedback tips: warn/success/error
    if ($.dialog && $.dialog.tips) {
        var DEFAULT_TIPS_SHOW_DURATION = 3;
        var tipsTypeList = ["warn", "success", "error", "loading"];
        var tipsTypeMap = {
            warn: "alert"
        };

        $.each(tipsTypeList, function(i, tipsType) {
            var basicMethodType = tipsTypeMap[tipsType] || tipsType;

            window.SQ[tipsType] = function(text, duration, callback) {
                duration = duration || DEFAULT_TIPS_SHOW_DURATION;
                return $.dialog.tips(text, duration, basicMethodType, callback);
            };
        });
        var loading;
        SQ.waiting = function(c) {
            loading = $.dialog.tips(c, 600, 'loading');
        };
        SQ.hideWaiting = function() {
            loading.close();
        };
        SQ.showBusy = function(duration, callback) {
            return SQ.warn("服务器繁忙，请稍后重试！", duration, callback);
        };
    }

    // 封装回车键事件响应方法
    SQ.enterKey = function(element, handler, options) {
        options = options || {};
        var eventType = options.eventType || "keypress";
        var eventData = options.eventData;
        var isCtrlKey = options.isCtrlKey;
        var isShiftKey = options.isShiftKey;
        var isAltKey = options.isAltKey;

        var isBoolean = function(param) {
            return (typeof param === "boolean");
        };
        // 尽在按下回车键 且组合键符合设置时 才触发回调事件
        var myHandler = function(e) {
            var keyCode = e.which;
            var that = this;

            if ((keyCode == 10) || (keyCode == 13)) {
                // 如果指定了Ctrl、Shift、Alt等，则严格匹配相应组合键
                if (isBoolean(isCtrlKey) && (isCtrlKey !== e.ctrlKey)) {
                    return;
                }
                else if (isBoolean(isShiftKey) && (isShiftKey !== e.shiftKey)) {
                    return;
                }
                else if (isBoolean(isAltKey) && (isAltKey !== e.altKey)) {
                    return;
                }

                handler.call(that, e);
            }
        };

        // 相当于 将【$(element).keypress(eventData, myHandler);】中的keypress换成变量
        return $(element)[eventType](eventData, myHandler);
    };
});

$(document).ready(function() {
    $.ajax({
        url: "http://console.youbangyun.com/client/ajax.aspx?action=UserStatus&jsonpcallback=?",
        type: "GET",
        dataType: "jsonp",
        jsonp: "jsonpcallback",
        success: function(responseData) {
            if (!responseData.Status) {
                return;
            }
            var $_userinfoContainer = $("#userinfoContainer");
            var $_usernameText = $("#usernameText");
            var $_usernameShow = $("#usernameShow");
            var $_userId = $("#userId");

            $_usernameShow.html(responseData.CallName);
            $_userId.html("(ID:" + responseData.UserId + ")");
            $("#serverCount").html(responseData.ServerCount);
            $("#orderCount").html(responseData.OrderCount);
            $("#msgCount").html(responseData.MsgCount);

            var money = responseData.TotalAmount || "0";
            if (String(money).indexOf(".") == -1) {
                money += ".00";
            }
            $("#userMoney").html(money);

            var verifyClassNameMap = {
                "0": "auth-icon-unauth",
                "1": "auth-icon-personal",
                "2": "auth-icon-company"
            };
            $_usernameText.addClass(verifyClassNameMap[responseData.VerifyType]);

            // 初始化数据之后，显示 用户面板
            $_userinfoContainer.addClass("welcome-user");

            // 动画效果
            var PANEL_EXTEND_CLASSNAME = "userinfo-container--extend";
            var EXTEND_DURATION = 300;
            var $_usernameContainer = $("#usernameContainer");
            var $_userPanel = $("#userPanel");
            var panelHeight = $_userPanel.css("height");
            $_userinfoContainer.on("mouseenter", ".userinfo-container", function() {
                $_userinfoContainer.addClass(PANEL_EXTEND_CLASSNAME);

                // 取消动画列队，不完成当前动画
                $_userPanel.stop(true, false);
                $_userPanel.css("height", 0);
                $_userPanel.animate({ height: panelHeight }, EXTEND_DURATION);
            });
            $_userinfoContainer.on("mouseleave", ".userinfo-container", function() {
                // 取消动画列队，不完成当前动画
                $_userPanel.stop(true, false);
                $_userPanel.animate({ height: 0 }, EXTEND_DURATION, function() {
                    $_userinfoContainer.removeClass(PANEL_EXTEND_CLASSNAME);
                });
            });
        }
    });

    //返回顶部按钮
    var $toTop = $("#toTop");
    $toTop.hide();
    $(window).scroll(function() {
        if ($(window).scrollTop() > 100) {
            if ($toTop.is(":hidden")) {
                $toTop.stop().fadeIn(500);
            }
        }
        else {
            if ($toTop.is(":visible")) {
                $toTop.stop().fadeOut(500);
            }
        }
    });
    $toTop.click(function() {
        $('body,html').stop().animate({ scrollTop: 0 }, 300);
        return false;
    });

    //二级菜单交互效果
    var $headerNavUl = $(".header-nav>ul");
    $(".header-nav-li").mouseenter(function() {
        $headerNavUl.removeClass();
        $headerNavUl.addClass("nav-bar-" + ($(this).index() + 1));
        $(this).addClass("header-nav-li--active").siblings().removeClass("header-nav-li--active");
    });
    $(".header-nav").mouseleave(function() {
        $(".header-nav-li").removeClass("header-nav-li--active");
        $headerNavUl.removeClass();
    });
    //“首页”不触发二级菜单
    $(".nav-1").hover(function() {
        $(".header-nav").addClass("hide-pop-list");
    }, function() {
        $(".header-nav").removeClass("hide-pop-list");
    });

    /*footer部分的脚本*/
    //侧边栏弹出
    $(".suspension-tel, .suspension-qrcode").hover(function() {
        $(this).children(".pop-detail").fadeIn(300);
    }, function() {
        $(this).children(".pop-detail").fadeOut(100);
    });

    $(".official-plat ul li:first-child").hover(function() {
        $(".weixin").show();
        $(".weibo").hide();
    });
    $("li[title='点击打开官方微博']").hover(function() {
        $(".weixin").hide();
        $(".weibo").show();
    });

    //href="#a_null"的统一设置为无效链接
    $("a[href='#a_null']").click(function() {
        return false;
    });

    //全图可点型banner添加打开链接方法
    $(".link-banner").each(function() {
        var $_self = $(this);
        var openURL = $_self.data("url");

        if (openURL) {
            $_self.click(function() {
                var openTarget = $_self.data("target") || "self";
                window.open(openURL, openTarget);
            });
        }
    });
    //PIE 统一设置
    if (window.PIE) {
        $('.pie-for-element').each(function() {
            PIE.attach(this);
        });
        $('.pie-for-children').children().each(function() {
            PIE.attach(this);
        });
        $('.pie-for-tree').find("*").each(function() {
            PIE.attach(this);
        });
    }

    // 图形验证码显示 及  点击刷新事件
    var captchaImgSelector = ".show-captcha";
    var $_showCaptcha = $(captchaImgSelector);
    if ($_showCaptcha.length) {
        var refreshCaptchaImg = (function() {
            var createCaptchaSrc = function() {
                return "/verifyimg?" + "rnd=" + Math.random();
            };

            return function(captchaImg) {
                $(captchaImg).attr("src", createCaptchaSrc());
            };
        })();

        // 绑定图片及按钮的 点击刷新验证码事件
        $_showCaptcha.click(function() {
            refreshCaptchaImg(this);
        });
        $(".refresh-captcha").click(function() {
            refreshCaptchaImg($(this).parent().find(captchaImgSelector));
        });
    };

    //发送消息弹出验证码确认
    var captchaCheck = function(url, data, callback) {
        // 刷新验证码
        $(".show-captcha").trigger("click");
        $("#captcha").val("");
        var _box = $.dialog({
            content: $("#captchaCheck")[0],
            title: '请输入图片验证码',
            width: 380,
            height: 110,
            okVal: '确认发送',
            ok: function() {
                var verifycode = $.trim($("#captcha").val());
                if (verifycode.length == 0) {
                    $("#captcha").focus();
                    return false;
                }
                SQ.waiting("正在发送中，请稍后...");
                $.post({
                    //验证码验证接口
                    url: url + "&verifycode=" + $("#captcha").val(),
                    cache: false,
                    data: data,
                    dataType: "json",
                    success: function(json) {
                        SQ.hideWaiting();
                        _box.close();
                        if (json.result) {
                            _box.close();
                            SQ.success(json.text, 5);
                            callback();
                        } else {
                            SQ.error(json.text, 3);
                        }
                    },
                    error: function() {
                        SQ.hideWaiting();
                        _box.close();
                        SQ.showBusy();
                    }
                });
                return false;
            },
            cancel: true
        });
    };

    // 验证码发送后倒计方法
    var sendCountdown = function(sendCodeBtn, countdownTime) {
        if (countdownTime > 0) {
            sendCodeBtn.attr("disabled", true);
            sendCodeBtn.addClass("send-captcha-disabled");
            sendCodeBtn.val((countdownTime--) + "秒后重新发送");
            var timer = setTimeout(function() {
                sendCountdown(sendCodeBtn, countdownTime);
            }, 1000);
        } else {
            sendCodeBtn.attr("disabled", false);
            sendCodeBtn.removeClass("send-captcha-disabled");
            sendCodeBtn.val("点击再次发送");
            countdownTime = 60;
        }
    };

    // 发送手机验证码按钮 倒计时控制 及 点击发送事件
    var $_sendSMSCaptcha = $("#sendSMSCaptcha");
    $_sendSMSCaptcha.click(function() {
        var data = {};
        var $_input = $("#mobile");
        if ($_input.length > 0) {
            var _mobileVal = $.trim($_input.val());
            if (_mobileVal == "") {
                $_input.focus();
                return false;
            };
            if (!$("#mobileTip").hasClass("onCorrect")) {
                SQ.warn("请先填写正确的手机号码", 1.5);
                return false;
            };
            data = { mobile: _mobileVal };
        }
        captchaCheck("ajax.aspx?action=SendMobileCode", data, function() {
            sendCountdown($_sendSMSCaptcha, 60);
            $("#verifyCode").focus();
        });
    });

    // 发送邮件验证码按钮 倒计时控制 及 点击发送事件 方法
    var $_sendEmailCaptcha = $("#sendEmailCaptcha");
    $_sendEmailCaptcha.click(function() {
        var data = {};
        var $_input = $("#email");
        if ($_input.length > 0) {
            var _emailVal = $.trim($_input.val());
            if (_emailVal == "") {
                $_input.focus();
                return false;
            }
            if (!$("#emailTip").hasClass("onCorrect")) {
                SQ.warn("请先填写正确的邮箱账户", 1.5);
                return false;
            };
            data = { email: _emailVal };
        }
        captchaCheck("ajax.aspx?action=SendEmailCode", data, function() {
            sendCountdown($_sendEmailCaptcha, 60);
            $("#verifyCode").focus();
        });
    });
});

//波浪动画
$(function() {
    var marqueeScroll = function(id1, id2, id3, timer) {
        var $parent = $("#" + id1);
        var $goal = $("#" + id2);
        var $closegoal = $("#" + id3);
        $closegoal.html($goal.html());
        function Marquee() {
            if (parseInt($parent.scrollLeft()) - $closegoal.width() >= 0) {
                $parent.scrollLeft(parseInt($parent.scrollLeft()) - $goal.width());
            }
            else {
                $parent.scrollLeft($parent.scrollLeft() + 1);
            }
        }
        setInterval(Marquee, timer);
    }
    var marqueeScroll1 = new marqueeScroll("marquee-box", "wave-list-box1", "wave-list-box2", 20);
    var marqueeScroll2 = new marqueeScroll("marquee-box3", "wave-list-box4", "wave-list-box5", 40);
});
// 清除焦点虚线
$(function() {
    $("a").attr("hidefocus", true);
});