window.SQ = window.SQ || {};
SQ.constant = (function() {
    var a = "id";
    var b = "name";
    return {
        DEFAULT_BUSY_TEXT: "服务器繁忙，请稍后重试！",
        DEFAULT_TIPS_TITLE: "消息",
        DEFAULT_TIPS_ERROR_TITLE: "错误提醒",
        DEFAULT_TIPS_LOADING_TITLE: "处理中",
        UPLOAD_FILE_ERROR_TEXT: "文件格式不符合或文件大小超限",
        UPLOAD_FILE_IMG_PREFIX: "http://www.youbangyun.com/upload/",
        DATE_NORMAL_FORMAT: "yyyy-M-dd HH:mm:ss",
        DATA_ARRAY2MAP_DEFAULT_KEYNAME: a,
        DATA_ARRAY2MAP_DEFAULT_VALUENAME: b,
        DEFAULT_TIPS_SHOW_DURATION: 3,
        DEFAULT_KEYNAME: a,
        DEFAULT_VALUENAME: b
    }
})();
SQ.regEx = (function() {
    return {
        passwordRegEx: "^(?=.*[0-9])(?=.*[a-zA-Z])^[\\w\\(\\)\\~\\!\\@\\#\\$\\%\\^\\&\\*\\-\\+\\=\\|\\{\\}\\[\\]\\:\\;\\<\\>\\,\\.\\?/]{8,30}$"
    }
})();
$(function() {
    SQ.constant.STATIC_SOURCES_PATH = "http://oem.soqi.com:8000";//jsData.common.domain["static"] || "http://www.youbangyun.com/upload"
});
SQ.date = (function() {
    var a = function(c) {
        return $.format.date(c, SQ.constant.DATE_NORMAL_FORMAT)
    };
    var b = function(i, m) {
        var h = {
            all: -1,
            year: 0,
            month: 1,
            day: 2,
            hour: 3,
            minute: 4,
            second: 5,
            none: 6
        };
        var e = $.extend({
            unitYear: "年",
            unitMonth: "个月",
            unitDay: "天",
            unitHour: "小时",
            unitMinute: "分钟",
            unitSecond: "秒",
            invalidTimeText: "-",
            isShowEmptyUnit: true,
            padZeroLevel: "minute",
            maxShowUnit: "all",
            useMillisecond: false
        },
        m);
        if ((!$.isNumeric(i)) || (i <= 0)) {
            return e.invalidTimeText
        }
        var j = i;
        if (e.useMillisecond) {
            j = Math.round(i / 1000)
        }
        var k = [];
        var l = [31536000, 2592000, 86400, 3600, 60, 1];
        var c = [e.unitYear, e.unitMonth, e.unitDay, e.unitHour, e.unitMinute, e.unitSecond];
        var g = h[e.padZeroLevel];
        var d = h[e.maxShowUnit];
        $.each(l,
        function(o, p) {
            if (o < d) {
                return
            }
            if ((j < p) && (k.length == 0)) {
                return
            }
            var n = Math.floor(j / p);
            j = j % p;
            if ((o >= g) && (n < 10)) {
                n = "0" + n
            }
            if ((Number(n) === 0) && !e.isShowEmptyUnit) {
                return
            }
            k.push(n + c[o])
        });
        var f = k.join("");
        if (!f) {
            f = e.invalidTimeText
        } else {
            if (f.indexOf("0") === 0) {
                f = f.slice(1)
            }
        }
        return f
    };
    return {
        format: a,
        now: function() {
            return a(new Date())
        },
        transformTime: b,
        getIncrementTime: function(d, c) {
            return Number(new Date(c || new Date())) + Number(d || 0)
        },
        getDecrementTime: function(d, c) {
            return Number(new Date(c || new Date())) - Number(d || 0)
        }
    }
})();
SQ.number = {
    priceFormat: function(f) {
        var c = String(parseInt(f));
        var b = "";
        var h = c.length;
        var e = 0;
        while (e < h) {
            var g;
            if (e == 0) {
                g = c.slice(-e - 3)
            } else {
                g = c.slice(-e - 3, -e) + ","
            }
            b = g + b;
            e += 3
        }
        var d = String(f);
        var a = "";
        if (d.indexOf(".") !== -1) {
            a = "." + d.split(".")[1]
        }
        return b + a
    },
    dataPatchFormat: function(c, e, b) {
        if (!c) {
            return c
        }
        var d = String(c) || "";
        e = e || 6;
        if (e < d.length) {
            return c
        }
        var f = SQ.string.createRepeatChar((b || "0"), e);
        var a = (f + c).slice(-e);
        return a
    },
    toFixed: function(a, b) {
        return Number(a).toFixed(b || 2)
    },
    getInt: function(a) {
        return parseInt(a)
    },
    getDecimals: function(a, b) {
        b = b || 2;
        return this.toFixed(a, b).slice(-(b + 1))
    }
};
SQ.string = {
    isEqual: function(b, a, c) {
        b = String(b);
        a = String(a);
        if (c !== false) {
            b = b.toLowerCase();
            a = a.toLowerCase()
        }
        return b == a
    },
    isEmpty: function(a, c) {
        var b = false;
        switch (a) {
            case "":
                b = true;
                break;
            case undefined:
            case null:
            case false:
                b = !c;
                break;
            default:
                b = false
        }
        return b
    },
    removeInsideSpace: function(a) {
        return String(a || "").replace(/\s+/g, "")
    },
    initial: function(a) {
        a = String(a || "");
        return a.charAt(0).toUpperCase() + a.slice(1)
    },
    breakLineToArray: function(a) {
        a = String(a || "");
        return a.split(/\r?\n/g)
    },
    createRepeatChar: function(b, a) {
        a = (Number(a) + 1) || 0;
        return new Array(a).join(b)
    }
};
SQ.util = (function() {
    var b = function(h) {
        return Array.prototype.slice.call(h)
    };
    var g = function() {
        var h = $.map(b(arguments),
        function(i) {
            return $(i)[0]
        });
        return $(h[0])
    };
    var c = function(i) {
        var h = $(i).prop("tagName") || "";
        return h.toLowerCase()
    };
    var f = function(i, h) {
        return SQ.string.isEqual(c(i), h)
    };
    var d = function(j, i) {
        var h = g($(i), $("#postForm"), $("form"));
        return h.find("[name=" + j + "]")
    };
    var e = (function() {
        var h = {};
        return function(j, m, l) {
            var i = b(arguments);
            var k = i.shift();
            clearTimeout(h[k]);
            return h[k] = window.setTimeout.apply(null, i)
        }
    })();
    var a = function(i) {
        if (!i) {
            return i
        }
        var h = function(k) {
            return k.replace(/</mg, "&lt;").replace(/>/mg, "&gt;")
        };
        var j = null;
        if (typeof i == "string") {
            j = h(i)
        } else {
            j = {};
            $.each(i,
            function(k, l) {
                if (typeof l == "string") {
                    j[k] = h(l)
                } else {
                    j[k] = l
                }
            })
        }
        return j
    };
    return {
        argumentsToArray: b,
        setTimeout: e,
        getFirstInCollection: g,
        getFieldByName: d,
        getTagName: c,
        isTagName: f,
        encodeHtmlTag: a
    }
})();
SQ.data = {
    arrayToMap: function(d, b, c) {
        d = d || [];
        b = b || SQ.constant.DATA_ARRAY2MAP_DEFAULT_KEYNAME;
        c = c || SQ.constant.DATA_ARRAY2MAP_DEFAULT_VALUENAME;
        var a = {};
        $.each(d,
        function(e, f) {
            a[f[b]] = f[c]
        });
        return a
    },
    stringToArray: function(b, a, c) {
        b = String(b || "");
        a = a || ",";
        c = (c !== false);
        if (c) {
            b = SQ.string.removeInsideSpace(b)
        }
        return b.split(a)
    }
};
SQ.feedback = (function() {
    var a = (function() {
        var k = null;
        var n = "ui-feedback";
        var i = "ui-feedback-icon";
        var f = "ui-feedback-text";
        var o = "ui-feedback-close";
        var e = "click.closeFeedback";
        var h = "feedback";
        var g = "slideDown";
        var l = 400;
        var m = {
            type: "success",
            text: "操作成功",
            duration: 3,
            closedCallback: $.noop
        };
        var j = function(s) {
            if (!k) {
                k = $("#nyFeedback")
            }
            if (!k.length) {
                console.warn("找不到 #nyFeedback 元素");
                return
            }
            var r = k.data(h);
            if (r && (r !== this)) {
                r.hide()
            }
            var w = k;
            var q = w.find("." + n);
            var v = q.find("." + o);
            var u = $.extend({},
            m, s);
            var p = this;
            var t = null;
            q.removeClass().addClass(n + " " + n + "-" + u.type);
            q.find("." + f).html(u.text);
            if (u.duration > 0) {
                t = setTimeout(function() {
                    p.hide()
                },
                u.duration * 1000 + l)
            }
            v.bind(e,
            function() {
                p.hide()
            });
            w.data(h, p);
            this.show = function() {
                w.show();
                q.hide();
                q[g](l);
                return p
            };
            this.hide = function() {
                w.stop(true, true).hide();
                u.closedCallback.call(q);
                p.destory();
                return p
            };
            this.destory = function() {
                clearTimeout(t);
                v.unbind(e);
                w.data(h, null);
                delete p.show;
                delete p.hide;
                delete p.destory;
                w = null;
                q = null;
                v = null;
                u = null;
                p = null;
                t = null
            }
        };
        return j
    })();
    var b = 3;
    var c = ["success", "warn", "info", "error"];
    var d = {};
    $.each(c,
    function(f, e) {
        d[e] = function(j, i, h) {
            var g = new a({
                type: e,
                text: j,
                duration: $.isNumeric(i) ? i : b,
                closedCallback: h || $.noop
            });
            g.show();
            return g
        }
    });
    return d
})();
SQ.tips = (function() {
    var b = {};
    var a = ["success", "warn", "error", "info"];
    $.each(a, function(d, c) {
        b[c] = function(f) {
            var h = $.extend(true, {
                icon: "uc_" + c,
                title: "提示信息",
                content: "",
                subTipContent: "",
                padding: "48px 80px 40px 10px",
                time: SQ.constant.DEFAULT_TIPS_SHOW_DURATION,
                close: $.noop
            }, f);
            if (h.subTipContent) {
                var e = ['<p class="sub-tip">', h.subTipContent, "</p>"].join("");
                if (typeof h.content == "string") {
                    h.content += e
                } else {
                    var g = $(h.content).wrap("<div>").parent();
                    g.append(e);
                    h.content = g[0]
                }
            }
            if (h.ok || h.cancel || h.button) {
                h.time = f.time || 0
            }
            delete h.subTipContent;
            return window.parent.$.dialog(h)
        }
    });
    b.showBusy = function(c) {
        return b.error($.extend({
            content: SQ.constant.DEFAULT_BUSY_TEXT
        }, c))
    };
    b.ask = function(c) {
        return b.success($.extend({
            icon: "uc_ask",
            title: "确认？",
            cancel: true
        }, c))
    };
    return b
})();
(function() {
    var a = ["success", "warn", "error", "showBusy"];
    $.each(a,
    function(c, b) {
        SQ[b] = function(g, f, e) {
            var d = {
                content: g,
                time: f,
                close: e
            };
            return SQ.tips[b](d)
        }
    })
})();
$(function() {
    var a = $("#nyWaitingTip");
    SQ.waiting = function(c, b) {
        a.find(".loading-tip-text").html(c);
        if (b) {
            a.find(".sq-loading-mask").show()
        } else {
            a.find(".sq-loading-mask").hide()
        }
        a.show()
    };
    SQ.hideWaiting = function() {
        a.hide()
    }
});
SQ.dom = (function() {
    var a = function(c, d) {
        var b = $(c);
        var e = $(d || "form");
        if (!e.length) {
            return false
        }
        b.find("[data-name]").each(function(g, f) {
            var m = $(f);
            var l = m.data("name");
            var k = m.data("value") || m.html();
            var j = e.find("[name=" + l + "]");
            if (!m.length || !j.length) {
                return
            }
            var h = m.data("type") || m.attr("type");
            switch (h) {
                case "radio":
                case "checkbox":
                    j[0].checked = m[0].checked;
                    break;
                case "text":
                    j.html($.trim(k));
                    break;
                default:
                    j.val($.trim(k))
            }
        })
    };
    return {
        scrollToBottom:
        function(b) {
            var c = $(b);
            c.scrollTop(c[0].scrollHeight)
        },
        setChecked: function(b) {
            var c = $(b);
            c.prop("checked", true)
        },
        setUnchecked: function(b) {
            var c = $(b);
            c.prop("checked", false)
        },
        fillFormByContainer: a,
        fillFormByTr: function(b, c) {
            var d = $(b);
            if (!SQ.util.isTagName(d, "tr")) {
                d = d.parents("tr")
            }
            if (!d.length) {
                return false
            }
            a(d, c)
        },
        createRowByForm: function(g, b, d) {
            var c = $(b || "form");
            if (!c.length) {
                return false
            }
            var f = null;
            var e = {};
            c.find("[name]").each(function(l, n) {
                var p = $(n);
                var h = p.data("type") || p.attr("type");
                var j = p.attr("name");
                var o = p.val();
                var k = SQ.util.getTagName(p);
                if (!o && (k != "input") && (k != "select")) {
                    o = p.html()
                }
                if ((h == "checkbox") || (h == "radio")) {
                    var m = n.checked ? p.data("checked_value") : p.data("unchecked_value");
                    o = m
                }
                e[j] = o
            });
            if ($.isFunction(d)) {
                f = d.call(e, e) || e
            } else {
                if (d) {
                    f = {};
                    f[d] = e
                } else {
                    f = e
                }
            }
            return template(g, f)
        },
        resetForm: function(c, d) {
            var b = SQ.util.getFirstInCollection($(c), $("form"));
            d = d || "reset_value";
            if (!b.length) {
                return
            }
            b[0].reset();
            b.find("[name]").each(function() {
                var f = $(this);
                var g = f.data(d);
                if (g !== undefined) {
                    var e = f.data("type") || f.attr("type");
                    if (e == "text") {
                        f.html(g)
                    } else {
                        f.val(g)
                    }
                }
            })
        },
        clearForm: function(c) {
            var b = SQ.util.getFirstInCollection($(c), $("form"));
            if (!b.length) {
                return
            }
            b.find("[name]").each(function() {
                var d = this.type;
                var e = $(this);
                switch (d) {
                    case "button":
                    case "submit":
                    case "reset":
                    case "image":
                        return;
                    case "radio":
                    case "checkbox":
                        e.prop("checked", false);
                        break;
                    default:
                        e.val("");
                        break
                }
            })
        },
        createSelectOptions: function(c, b, d) {
            c = c || [];
            b = b || SQ.constant.DEFAULT_KEYNAME;
            d = d || SQ.constant.DEFAULT_VALUENAME;
            return $.map(c,
            function(g, e) {
                var f = $("<option>").text(g[d]).val(g[b]);
                f.data("source", g);
                return f
            })
        },
        fillSelectOptions: function(c, e, b, f) {
            var d = $(c);
            d.append(this.createSelectOptions(e, b, f))
        },
        createCountDown: function(f, e) {
            var c = $(f);
            var h = $.extend({
                time: 60,
                waitingText: "{%t}秒后重新发送",
                finalContent: "点击再次发送",
                disabledClassName: "disabled",
                intervalSecond: 1,
                callback: function() { }
            },
            e);
            var i = h.finalContent;
            var d = h.disabledClassName;
            var g = h.intervalSecond;
            var b = function(j) {
                return h.waitingText.replace("{%t}", j)
            };
            c.each(function(k) {
                var m = $(this);
                var l = function(r) {
                    var q = "";
                    if (m.prop("nodeName").toUpperCase() == "INPUT") {
                        q = m.val(r).val()
                    } else {
                        q = m.html(r).html()
                    }
                    return q
                };
                var o = h.time;
                var n = l();
                var p = function() {
                    l(b(o));
                    m.prop("disabled", true);
                    m.addClass(d)
                };
                var j = setInterval(function() {
                    if (o > 1) {
                        o--;
                        p()
                    } else {
                        l(i || n);
                        m.prop("disabled", false);
                        m.removeClass(d);
                        clearInterval(j);
                        h.callback.call(m, h)
                    }
                },
                g * 1000);
                p()
            })
        }
    }
})();
SQ.event = (function() {
    var b = function(f, l, m) {
        m = m || {};
        var d = m.eventType || "keypress";
        var j = m.eventData;
        var h = m.isCtrlKey;
        var g = m.isShiftKey;
        var i = m.isAltKey;
        var e = function(n) {
            return (typeof n === "boolean")
        };
        var k = function(p) {
            var o = p.which;
            var n = this;
            if ((o == 10) || (o == 13)) {
                if (e(h) && (h !== p.ctrlKey)) {
                    return
                } else {
                    if (e(g) && (g !== p.shiftKey)) {
                        return
                    } else {
                        if (e(i) && (i !== p.altKey)) {
                            return
                        }
                    }
                }
                l.call(n, p)
            }
        };
        return $(f)[d](j, k)
    };
    var a = function(d) {
        return function(f, g, e) {
            e = e || {};
            e.eventType = d;
            return b(f, g, e)
        }
    };
    var c = function(i, e) {
        var g = $.extend({
            isLimitInt: false,
            min: 1,
            max: undefined
        },
        e);
        var h = g.isLimitInt;
        var f = g.min;
        var d = g.max;
        $(i).keydown(function(q) {
            var p = $(this);
            var o = q.which;
            if (q.shiftKey) {
                return false
            }
            var l = ((o == 110) || (o == 190));
            if (h && l) {
                return false
            }
            var m = (((o >= 48) && (o <= 57)) || ((o >= 96) && (o <= 105)));
            var k = ((o == 8) || (o == 37) || (o == 38) || (o == 39) || (o == 40) || (o == 35) || (o == 36) || (o == 46));
            if (!l && !m && !k) {
                return false
            }
            var j = p.val();
            var n = j.indexOf(".");
            if ((n != -1) && l) {
                return false
            }
            if ((n >= 0) && ((j.length - n) > 2) && m) {
                return false
            }
        }).blur(function() {
            var l = $(this);
            var j = parseFloat(l.val());
            if (!$.isNumeric(j)) {
                l.val(f);
                return
            }
            var k = Math.max(f, j);
            if (d) {
                k = Math.min(d, k)
            }
            l.val(k)
        })
    };
    return {
        enterPress: b,
        enterUp: a("keyup"),
        enterDown: a("keydown"),
        inputNumberLimit: c
    }
})();
SQ.ajax = function(b, a) {
    if (typeof b === "object") {
        a = b;
        b = undefined
    }
    var d = (a.isShowWaitTip !== false);
    var c = $.extend({
        url: b,
        type: "post",
        dataType: "json",
        error: function() {
            SQ.showBusy()
        },
        beforeSend: function() {
            SQ.waiting("处理中，请稍后...", true)
        },
        complete: function(e, f) {
            if (d) {
                SQ.hideWaiting()
            }
        }
    },
    a);
    delete c.isCoverSuccess;
    delete c.successResultFalse;
    delete c.isSuccessShowTip;
    delete c.isSuccessJump;
    delete c.isResultFalseWarn;
    delete c.waitText;
    delete c.isShowWaitTip;
    delete c.isShowWaitMask;
    delete c.waitMaskStyle;
    if (a.isCoverSuccess !== true) {
        c.success = function(i, l, j) {
            var h = this;
            var f = i.text;
            var e = i.time;
            if (!i.result) {
                var k = function() {
                    var m = a.successResultFalse;
                    if ($.isFunction(m)) {
                        m.call(h, i, l, j)
                    }
                };
                if (a.isResultFalseWarn !== false) {
                    SQ.warn(f, e,
                    function() {
                        k()
                    })
                } else {
                    k()
                }
                return
            }
            var g = function() {
                var m = a.success;
                var n = true;
                if ($.isFunction(m)) {
                    n = m.call(h, i, l, j)
                }
                if ((a.isSuccessJump !== false) && (n !== false)) {
                    if (i.url) {
                        window.location.href = i.url
                    } else {
                        if (i.reload) {
                            window.location.reload()
                        }
                    }
                }
            };
            if ((a.isSuccessShowTip !== false) && f) {
                SQ.success(f, e,
                function() {
                    g()
                })
            } else {
                g()
            }
        }
    }
    if (d) {
        SQ.waiting(a.waitText, a.isShowWaitMask)
    }
    return $.ajax(c)
};
$.each(["get", "post"],
function(a, b) {
    SQ[b] = function(d, c) {
        c = c || {};
        c.type = b;
        if (b == "get") {
            c.cache = false
        }
        return SQ.ajax(d, c)
    }
});
SQ.jsonp = function(b, a) {
    if (typeof b === "object") {
        a = b;
        b = undefined
    }
    var c = $.extend(true, {
        url: b,
        dataType: "jsonp",
        data: {
            format: "jsonp"
        }
    },
    a);
    var d = (c.url.indexOf("?") == -1) ? "?" : "&";
    c.url += d + "jsoncallback=?";
    return SQ.ajax(c)
};
SQ.mAjax = function(b, a) {
    if (typeof b === "object") {
        a = b;
        b = undefined
    }
    var d = (a.isShowWaitTip !== false);
    var c = $.extend({
        url: b,
        type: "post",
        dataType: "json",
        error: function() {
            mui.nyTip(SQ.constant.DEFAULT_BUSY_TEXT, 3, "error")
        },
        beforeSend: function() {
            mui.nyTip("处理中...", 100, "loading")
        },
        complete: function(e, f) {
            if (d) {
                $(".sq-show-loading").parent().remove()
            }
        }
    },
    a);
    delete c.isCoverSuccess;
    delete c.successResultFalse;
    delete c.isSuccessShowTip;
    delete c.isSuccessJump;
    delete c.isResultFalseWarn;
    delete c.waitText;
    delete c.isShowWaitTip;
    delete c.isShowWaitMask;
    delete c.waitMaskStyle;
    if (a.isCoverSuccess !== true) {
        c.success = function(i, l, j) {
            var h = this;
            var f = i.text || "操作成功!";
            var e = i.time || 1;
            if (!i.result) {
                var k = function() {
                    var m = a.successResultFalse;
                    if ($.isFunction(m)) {
                        m.call(h, i, l, j)
                    }
                };
                if (a.isResultFalseWarn !== false) {
                    mui.alert(f, " ",
                    function() {
                        k()
                    })
                } else {
                    k()
                }
                return
            }
            var g = function() {
                var m = a.success;
                var n = true;
                if ($.isFunction(m)) {
                    n = m.call(h, i, l, j)
                }
                if ((a.isSuccessJump !== false) && (n !== false)) {
                    if (i.url) {
                        window.location.href = i.url
                    } else {
                        if (i.reload) {
                            window.location.reload()
                        }
                    }
                }
            };
            if ((a.isSuccessShowTip !== false) && f) {
                mui.nyTip(f, e, "success",
                function() {
                    g()
                })
            } else {
                g()
            }
        }
    }
    return $.ajax(c)
};
$.each(["mGet", "mPost"],
function(a, b) {
    SQ[b] = function(d, c) {
        c = c || {};
        c.type = b;
        if (b == "get") {
            c.cache = false
        }
        return SQ.mAjax(d, c)
    }
});
SQ.biz = {
    createTextMapHelper: function(c, e, b, d) {
        var a = $.isArray(e) ? SQ.data.arrayToMap(e, b, d) : e;
        template.helper(c,
        function(f) {
            return a[f]
        })
    },
    createObjectMapHelper: function(d, a, c) {
        var b = a[c || "__default__"] || {};
        template.helper(d,
        function(f, e) {
            var g = a[f] || b;
            return g[e]
        })
    },
    multiUpload: function(k, h) {
        var c = $.extend(true, {
            maxAttachments: 5,
            attachmentItemHtml: template("attachment", {}),
            containerSelector: ".added-img-container",
            addButtonSelector: ".add-attachment-btn",
            deleteButtonSelector: ".delete-attachment",
            uploadingClassName: "attachment-uploading",
            doneClassName: "attachment-done",
            errorCall: function(m, l) {
                SQ.feedback.error(SQ.constant.UPLOAD_FILE_ERROR_TEXT, 5)
            }
        },
        k);
        var g = c.maxAttachments;
        var a = c.attachmentItemHtml;
        var f = $(c.containerSelector);
        var i = $(c.addButtonSelector);
        var j = c.deleteButtonSelector;
        var e = c.uploadingClassName;
        var b = c.doneClassName;
        var d = c.errorCall;
        i.each(function() {
            var n = i.parent();
            var l = $.extend(true, {
                browse_button: this,
                multi_selection: true,
                onFilesAdded: function(t, q) {
                    var o = f.children().length;
                    var r = g - o;
                    var s = Math.min(q.length, r);
                    for (var p = 0; p < s; p++) {
                        f.append(a)
                    }
                    if ((o + q.length) >= g) {
                        n.hide()
                    }
                },
                onFileUploaded: function(t, r, q, p) {
                    var o = f.find("." + e).first();
                    if (!o.length) {
                        return
                    }
                    var s = o.find("img");
                    o.removeClass(e);
                    o.addClass(b);
                    s.data("file_path", t.url);
                    SQ.plupload.previewImage(q,
                    function(u) {
                        s.attr("src", u)
                    },
                    {
                        unpreviewCallback: function(u) {
                            s.attr("src", SQ.constant.UPLOAD_FILE_IMG_PREFIX + t.url)
                        }
                    })
                },
                onError: d
            },
            h);
            var m = SQ.plupload.createUploader(l);
            f.on("click", j,
            function() {
                var o = $(this).parent();
                o.fadeOut(function() {
                    o.remove();
                    n.show()
                })
            })
        })
    },
    getMenuSet: function(a) {
        a = a || $("#crumbNavContainer").data("nav");
        if (!a) {
            return null
        }
        var h = $(".sq-menu");
        var c = h.find("[data-named='" + a + "']");
        var e = c.parents(".panel");
        var f = null;
        var i = null;
        var b = null;
        var g = e.index();
        var d = -1;
        if (c.hasClass("upper-menu")) {
            f = c
        } else {
            f = e.find(".upper-menu");
            b = c;
            i = b.parents("[role=tabpanel]");
            d = b.parent().index()
        }
        return {
            nyMenuContainer: h,
            upperMenuContainer: e,
            upperMenuItem: f,
            upperMenuIndex: g,
            subMenuContainer: i,
            subMenuItem: b,
            subMenuIndex: d
        }
    },
    setMenuHighlight: function() {
        var c = SQ.biz.getMenuSet();
        if (c) {
            var b = c.nyMenuContainer;
            var a = "clear-transition";
            b.addClass(a);
            c.upperMenuItem.addClass("menu-active");
            if (c.subMenuContainer) {
                c.subMenuContainer.addClass("in");
                c.subMenuItem.addClass("sub-menu-active")
            }
            setTimeout(function() {
                b.removeClass(a)
            },
            0)
        }
    },
    getListTable: function() {
        var a = $(".sq-table");
        return a.length ? a : null
    },
    useNoneDataFallback: function(f, a, d) {
        var c = $(f);
        if (!jsData.pager || jsData.pager.total) {
            return
        }
        if (!c.length) {
            c = SQ.biz.getListTable()
        }
        if (c == null) return;
        var b = (jsData.filter && (!SQ.string.isEmpty(jsData.filter.searchKey))) ? true : false;
        var e = b ? (d || "noneSearchRecordTdTpl") : (a || "nonedataTdTpl");
        var g = 0;
        c.find("tr").first().children().each(function(k, j) {
            g += Number($(j).attr("colspan")) || 1
        });
        c.find("tfoot").hide();
        var h = SQ.util.getFirstInCollection(c.find("tbody"), c);
        h.append(template(e, {
            cols: g,
            text: c.data("nonedata_text") || "暂无符合条件的记录",
            searchKey: (b ? jsData.filter.searchKey : ""),
            subtext: c.data("nonedata_subtext"),
            subtextLink: c.data("nonedata_subtext_link")
        }))
    },
    makeListTableSortable: function(o) {
        var j = SQ.util.getFirstInCollection($(o), SQ.biz.getListTable());
        if (!j.length) {
            return
        }
        var l = "order_name";
        var d = "order_type";
        var m = "desc";
        var g = "asc";
        var b = m;
        var i = "filter-icon";
        var n = "filter-desc";
        var f = "filter-asc";
        var a = "th-sortable";
        var h = '<a href="#a_null" class="' + i + '"></a>';
        var k = jsData.filter || {};
        var c = k.orderName;
        var e = k.orderType;
        j.find("thead th[data-" + l + "]").each(function() {
            var q = $(this);
            var p = q.data(l);
            var r = $(h);
            if (p == c) {
                if (e == m) {
                    r.addClass(n);
                    q.data(d, m)
                } else {
                    if (e == g) {
                        r.addClass(f)
                    }
                }
            }
            q.append(r).addClass(a);
        });
        j.find("thead").on("click", "th[data-" + l + "]",
        function() {
            var q = $(this);
            var p = q.data(l);
            var r = (q.data(d) == m) ? g : m;
            var s = $.param($.extend(k, {
                orderName: p,
                orderType: r
            }));
            window.location = window.location.pathname + "?" + s
        })
    },
    chooseAllInTable: function(f, b) {
        var c = $.extend({
            checkboxGroupSelector: "",
            parmContainerSelector: ""
        },
        b);
        var a = $(f);
        var d = $(c.parmContainerSelector);
        if (!d.length) {
            d = a.parents("table")
        }
        if (!a.length || !d.length) {
            return
        }
        var e1 = d.find("input:checkbox:enabled");
        if (c.checkboxGroupSelector != "") {
            var e2 = $(c.checkboxGroupSelector);
        }
        a.change(function() {
            var g = $(this).prop("checked");
            e1.prop("checked", g);
            if (typeof e2 != "undefined") {
                e2.prop("checked", g);
            }
        })
    },
    refreshSearchStatus: function(fromLoad, checkurl) {
        $.get({
            url: checkurl,
            dataType: "json",
            success: function(json) {
                if (json.result) {
                    if (!fromLoad) {
                        SQ.tips.success({
                            content: "您提交的查询已经全部执行完毕！",
                            ok: true,
                            okVal: "确定",
                            ok: function() {
                                window.location.reload();
                            }
                        });
                    }
                }
                else {
                    setTimeout(SQ.biz.refreshSearchStatus, 5000, false, checkurl);
                }
            }
        });
    }
};
SQ.plupload = (function() {
    var c = function(e) {
        e = e || {};
        var h = $.noop;
        var j = $.extend({
            pluploadBasePath: SQ.constant.STATIC_SOURCES_PATH + "/lib/plugin/plupload/",
            isAutoInit: true,
            isAutoUpload: true,
            isEasyGetFile: false,
            onFilesAdded: h,
            onUploadProgress: h,
            onFileUploaded: h,
            onError: h,
            isParseResponseJSON: true,
            previewImgElement: null
        },e);
    delete e.pluploadBasePath;
    delete e.isAutoInit;
    delete e.isAutoUpload;
    delete e.isEasyGetFile;
    delete e.onFilesAdded;
    delete e.onUploadProgress;
    delete e.onFileUploaded;
    delete e.onError;
    delete e.isParseResponseJSON;
    delete e.previewImgElement;
    var d = j.pluploadBasePath;
    var g = $.extend(true, {
        browse_button: "uploadFileButton",
        url: "/user/upload/",
        runtimes: "html5,flash,silverlight,html4",
        flash_swf_url: d + "Moxie.swf",
        silverlight_xap_url: d + "Moxie.xap",
        multi_selection: false,
        filters: {
            mime_types: [{
                title: "Image files",
                extensions: "jpg,png"}],
                max_file_size: "1mb",
                prevent_duplicates: false
            }
        },
    e);
        var i = new plupload.Uploader(g);
        if (j.isAutoInit) {
            i.init();
            var f = $(j.previewImgElement);
            var k = !!f.length;
            i.bind("FilesAdded",
        function(n, l) {
            var m = (g.multi_selection) ? l : l[0];
            if (j.isEasyGetFile && (l.length == 1)) {
                m = l[0]
            }
            j.onFilesAdded.call(this, n, m);
            if (k && !g.multi_selection) {
                a(m, f)
            }
            if (j.isAutoUpload) {
                n.start()
            }
        });
            i.bind("UploadProgress", j.onUploadProgress);
            i.bind("FileUploaded",
        function(o, n, m) {
            var l = m.response;
            if (j.isParseResponseJSON) {
                l = $.parseJSON(m.response)
            }
            j.onFileUploaded.call(this, l, o, n, m)
        });
            i.bind("Error", j.onError)
        }
        return i
    };
    var b = function(g, j, f) {
        var h = $.extend({
            isDownsize: true,
            downsizeWidth: 200,
            downsizeHeight: 200,
            unpreviewCallback: function(k) { }
        },
    f);
        if (!g || !/image\//.test(g.type)) {
            return
        }
        var i = g.getSource();
        if (!g.loaded || !i.size) {
            h.unpreviewCallback(g);
            return
        }
        if (g.type == "image/gif") {
            var e = new mOxie.FileReader();
            e.onload = function() {
                if (j) {
                    j(e.result)
                }
                e = null
            };
            e.readAsDataURL(g.getSource())
        } else {
            var d = new mOxie.Image();
            d.onload = function() {
                if (h.isDownsize) {
                    d.downsize(h.downsizeWidth, h.downsizeHeight)
                }
                var k = (d.type == "image/jpeg") ? d.getAsDataURL("image/jpeg", 80) : d.getAsDataURL();
                if (j) {
                    j(k)
                }
                d.destroy && d.destroy();
                d = null
            };
            d.load(i)
        }
    };
    var a = function(d, e) {
        b(d,
    function(f) {
        $(e).attr("src", f)
    })
    };
    return {
        createUploader: c,
        previewImage: b,
        setPreviewImage: a
    }
})();
SQ.zeroClipboard = (function() {
    var c = false;
    var b = 100000;
    var a = function(d) {
        var e = $.extend(true, {
            config: {
                moviePath: SQ.constant.STATIC_SOURCES_PATH + "/lib/plugin/zeroClipBoard/ZeroClipboard.swf"
            },
            elementSelector: ".copy-text",
            clipboardTextAttrName: "data-clipboard-text",
            isSetTitle: true,
            elementTitle: "点击复制",
            copySuccessText: "复制成功",
            successTextDelay: 2000,
            onCopyComplete: $.noop
        },
    d);
        if (!c) {
            ZeroClipboard.config(e.config);
            c = true
        }
        $(e.elementSelector).each(function() {
            var i = $(this);
            var h = e.clipboardTextAttrName;
            if (!i.attr(h)) {
                i.attr(h, i.html())
            }
            var f = "";
            if (e.copySuccessText) {
                f = i.attr("title");
                i.attr("title", e.copySuccessText);
                i.tooltip({
                    trigger: "manual"
                });
                i.removeAttr("title")
            }
            if (e.isSetTitle) {
                i.attr("title", f || e.elementTitle)
            }
            var g = new ZeroClipboard(this);
            g.on("load",
        function(j) {
            j.on("complete",
            function(l, m) {
                e.onCopyComplete.call(this, m.text, l, m);
                if (e.copySuccessText) {
                    var n = $(this);
                    var k = n.data("timeoutKey");
                    if (!k) {
                        k = b++;
                        n.data("timeoutKey", k)
                    }
                    n.tooltip("show");
                    SQ.util.setTimeout(k,
                    function() {
                        n.tooltip("hide")
                    },
                    e.successTextDelay)
                }
            })
        })
        })
    };
    return {
        init: a
    }
})();
SQ.component = (function() {
    var a = function(l, k) {
        var h = $(l);
        if (!h.length) {
            return
        }
        var t = "page--current";
        var m = "disabled";
        var j = 3;
        var r = j * 2 + 1;
        var n = 1;
        var v = h.find(".page-number");
        var o = v.children().first().clone();
        var p = jsData.pager || {};
        var s = p.page;
        var u = Math.max(Math.ceil(p.total / p.size), n);
        var i = Math.min(r, u);
        var g = [{
            button: h.find(".pager-first"),
            targetPageNumber: 1
        },
        {
            button: h.find(".pager-last"),
            targetPageNumber: u
        },
        {
            button: h.find(".pager-prev"),
            incrementPageNumber: -1
        },
        {
            button: h.find(".pager-next"),
            incrementPageNumber: 1
}];
            var q = function(x) {
                var y = 0;
                if (x.targetPageNumber) {
                    y = x.targetPageNumber
                } else {
                    if (x.incrementPageNumber) {
                        var w = Number(v.find("." + t).html());
                        y = w + Number(x.incrementPageNumber)
                    }
                }
                return y
            };
            var f = function(w) {
                var B = Math.max(w - j, 1);
                var A = Math.min(u, w + j);
                if ((A - B + 1) < i) {
                    if (B < j) {
                        A = i
                    }
                    if (u - w < j) {
                        B = Math.max(A - (r - 1), 1)
                    }
                }
                v.empty();
                for (var x = B; x <= A; x++) {
                    var z = x;
                    var y = (z == w) ? t : "";
                    o.clone().html(z).addClass(y).appendTo(v)
                }
                $.each(g,
        function(D, C) {
            var F = C.button;
            var E = q(C);
            if (!E) {
                F.addClass(m);
                return
            }
            if (C.targetPageNumber) {
                if (E === w) {
                    F.addClass(m)
                } else {
                    F.removeClass(m)
                }
            } else {
                if (C.incrementPageNumber) {
                    if ((E < 1) || (E > u)) {
                        F.addClass(m)
                    } else {
                        F.removeClass(m)
                    }
                }
            }
        })
            };
            f(s);
            k = k || $.noop;
            var e = function(x, w) {
                x = Number(x);
                var z = [x].concat(SQ.util.argumentsToArray(w));
                var y = k.apply(this, z);
                if (y !== false) {
                    f(x)
                }
            };
            v.on("click", ".page-button:not(." + t + ")",
    function(y) {
        var w = $(this);
        var x = w.html();
        e.call(this, x, arguments)
    });
            $.each(g,
    function(x, w) {
        var y = w.button;
        y.click(function() {
            if (y.hasClass(m)) {
                return
            }
            var z = q(w);
            e.call(this, z, arguments)
        })
    })
        };
        var c = function(g) {
            var i = $.extend(true, {
                containerSelector: ".sq-tab-container",
                tabGroupDataName: "tab_group",
                selectedClassName: "sq-tab-selected",
                controlClassNamePre: "tab-group-"
            }, g);
            var j = $(i.containerSelector);
            if (j.length) {
                var e = i.tabGroupDataName;
                var f = i.selectedClassName;
                var h = i.controlClassNamePre;
                j.each(function() {
                    var k = $(this);
                    var m = k.find("[data-" + e + "]");
                    k.addClass(h + m.first().data(e));
                    var l = "";
                    m.each(function() {
                        l += (h + $(this).data(e) + " ")
                    });
                    m.click(function() {
                        var n = $(this);
                        n.addClass(f).siblings().removeClass(f);
                        k.removeClass(l);
                        k.addClass(h + n.data(e))
                    })
                })
            }
        };
        var d = function(m) {
            var g = $.extend(true, {
                containerSelector: ".sq-dropdown-container",
                containerBindDataName: "field_name",
                dropItemDataName: "drop",
                activeClassName: "sq-drop-active",
                defaultOptionClassName: "sq-default-option",
                defaultOptionValue: "",
                selectedContentClassName: "selected-content"
            }, m);
            var h = $(g.containerSelector);
            var j = g.containerBindDataName;
            var e = g.dropItemDataName;
            var l = g.activeClassName;
            var f = g.defaultOptionClassName;
            var i = g.defaultOptionValue;
            var k = g.selectedContentClassName;
            h.each(function() {
                var r = $(this);
                var p = r.find("." + k);
                var s = $("[name=" + r.data(j) + "]");
                var t = r.find("[data-" + e + "]");
                if (i) {
                    r.find("[data-" + e + "=" + i + "]").addClass(f)
                }
                var n = r.find("." + f);
                if (n.length) {
                    n.addClass(l);
                    p.html(n.html());
                    s.val(n.data(e))
                } else {
                    t.first().addClass(l);
                    p.html(t.first().html());
                    s.val(t.first().data(e))
                }
                var w = r.find(".dropdown-toggle");
                var v = r.find(".dropdown-menu");
                v.show();
                var o = w.outerWidth();
                var u = v.outerWidth();
                var q = 0;
                if (u > o) {
                    q = u + 18
                } else {
                    q = o + 8
                }
                w.outerWidth(q);
                v.outerWidth(q);
                t.click(function() {
                    var x = $(this);
                    t.removeClass().removeClass(l);
                    x.addClass(l);
                    p.html(x.html());
                    s.val(x.data(e));
                    s.trigger("change")
                })
            })
        };
        var b = function(f) {
            var g = {
                inputSelector: ".sq-number-input",
                min: 1,
                max: 10000,
                step: 1,
                unit: ""
            };
            var e = $(g.inputSelector);
            e.each(function() {
                var n = $(this);
                var k = $.extend(true, {},
        g, {
            min: n.data("num_min"),
            max: n.data("num_max"),
            step: n.data("num_step"),
            unit: n.data("num_unit")
        },
        f);
                var l = parseInt(k.min);
                var q = parseInt(k.max);
                var h = parseInt(k.step);
                var r = k.unit;
                var p = $(['<div class="sq-number-container">', '<span class="number-input-box">', '<span class="sq-number-unit">' + r + "</span>", "</span>", '<span class="sq-number-control">', '<span class="number-control-up"></span>', '<span class="number-control-down"></span>', "</span>", "</div>"].join(""));
                n.before(p);
                p.find(".number-input-box").prepend(n.clone());
                n.remove();
                n = p.find(".sq-number-input");
                var j = function(s) {
                    return $(s).parents(".sq-number-container").find(k.inputSelector)
                };
                var i = function(t) {
                    var v = t ? j(t) : n;
                    var s = v.val();
                    var u = Math.max(Math.min(s, q), l);
                    v.val(isNaN(u) ? l : u)
                };
                i();
                n.change(function() {
                    i(this)
                });
                var m = p.find(".number-control-up");
                var o = p.find(".number-control-down");
                m.click(function() {
                    var s = j(this);
                    var t = parseInt(s.val());
                    t += h;
                    s.val(Math.min(t, q));
                    s.trigger("change")
                });
                o.click(function() {
                    var s = j(this);
                    var t = parseInt(s.val());
                    t -= h;
                    s.val(Math.max(t, l));
                    s.trigger("change")
                });
                $(".number-input-box").click(function() {
                    $(this).find(n).focus()
                })
            })
        };
        return {
            initPagination: a,
            initTabs: c,
            initDropDown: d,
            initNumber: b
        }
    })();
SQ.validater = (function() {
    var f = ".validate-control";
    var b = ".error-reminder";
    var e = "error-input";
    var g = function(i) {
        var h = $(i);
        h.find(f + " ." + e).removeClass(e);
        h.find(b).empty()
    };
    var d = function(i, l, j, k) {
        var h = null;
        if (typeof i == "string") {
            h = SQ.util.getFieldByName(i, $(j))
        } else {
            h = $(i)
        }
        g(j);
        h.addClass(e).focus().parents(f).find(b).html(l);
        if (k !== false) {
            h.blur(function() {
                if ($(this).val()) {
                    g(j)
                }
            })
        }
    };
    var c = function(k, j) {
        var i = $(k);
        var h = true;
        if (!j) {
            return true;
        }
        $.each(j,
            function(p, q) {
                var l = SQ.util.getFieldByName(p, i);
                var o = l.val();
                var m = new RegExp(q.regexp) || {};
                var n = q.remindWords || "该项输入不合法";
                if (q.required) {
                    if (!o) {
                        h = false;
                        d(l, n, i)
                    }
                }
                if (!m.test(o)) {
                    h = false;
                    d(l, n, i)
                }
            });
        return h;
    };
    var a = function(k, j) {
        var i = $(k);
        var h = true;
        if (!j) {
            return true
        }
        $.each(j,
    function(p, q) {
        var l = SQ.util.getFieldByName(p, i);
        var o = l.val();
        var m = new RegExp(q.regexp) || {};
        var n = q.remindWords || "该项输入不合法";
        if (q.required) {
            if (!o) {
                h = false;
                d(l, n, i);
                mui.alert(n)
            }
        }
        if (!m.test(o)) {
            h = false;
            mui.alert(n)
        }
    });
        return h
    };
    return {
        clearValidateError: g,
        validateShowError: d,
        formValidate: c,
        mobileFormValidate: a
    }
})();
SQ.proxyRenderer = {
    renderContent: function(a, d, c) {
        var b = a || $("#contentBox");
        d = d || jsData;
        c = c || "contentTemplate";
        b.html(template(c, d));
        SQ.proxyRenderer.renderCrumb();
        SQ.biz.setMenuHighlight();
        SQ.proxyRenderer.renderPagination(); // 翻页
        SQ.proxyRenderer.pageSizeChange(); // 页大小修改
        SQ.component.initTabs(); // 单选按钮
        SQ.component.initDropDown(); // 下拉菜单
        SQ.biz.makeListTableSortable(); //排序列头
        SQ.biz.useNoneDataFallback(); // 无数据渲染
    },
    renderCrumb: function() {
        var h = $("#crumbNavContainer");
        if (!h.length) {
            return
        }
        var g = SQ.biz.getMenuSet();
        var e = g.upperMenuItem;
        var f = g.subMenuItem;
        var d = (h.data("inside") ? $(".sq-panel-title").html() : "");
        var c = "";
        var a = "";
        if (f) {
            c = f.html();
            a = (d ? f.attr("href") : "")
        }
        var b = {
            upperMenuText: e.html(),
            subMenuText: c,
            subMenuLink: a,
            insideTitle: d
        };
        h.html(template("crumbNavTpl", b))
    },
    renderPagination: function(a) {
        var b = $(a || "#pager");
        if (!b.length) {
            return
        }
        if (!jsData.pager || (jsData.pager.total == 0)) {
            b.hide();
            return
        }
        SQ.component.initPagination(b,
        function(c, d) {
            var f = $.param($.extend({
                page: c
            },
            jsData.filter));
            window.location = window.location.pathname + "?" + f
        })
    },
    pageSizeChange: function() {
        if (!jsData.pager) return;
        $(".pager-set").each(function() {
            $(this).click(function() {
                $.cookie(jsData.pager.cookieName, $(this).text(), { expires: 30, path: '/' });
                location.href = location.href.replace('page=', '');
            });
            if (jsData.pager.size == $(this).text()) {
                $(this).addClass("page--current");
            }
        });
    }
};
// 获取选中多个id的数组
SQ.getTaskIdArray = function(a, b) {
    var batchIdsArray = [];
    var $_mainList = (typeof(a) == 'undefined')? $("#mainList") : a;
    var listCheckedSelector = (typeof(b) == 'undefined')? ".list-checkbox:checked" : b;
    $_mainList.find(listCheckedSelector).each(function() {
        batchIdsArray.push($(this).val());
    });
    return batchIdsArray;
};