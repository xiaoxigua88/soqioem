//权限分组映射
var privilegeGroupMap = {
    "1001": { className: "", name: "系统设置" },
    "1002": { className: "", name: "内务管理" },
    "1003": { className: "", name: "用户管理" },
    "1004": { className: "", name: "财务管理" },
    "1005": { className: "", name: "消息管理" },
    "1006": { className: "", name: "数据实时查" },
    "1007": { className: "", name: "数据云监控" },
    "1008": { className: "", name: "优帮云排名" },
    "1009": { className: "", name: "优帮云建站" },
    "1010": { className: "", name: "优帮云站通" }
};
SQ.biz.createObjectMapHelper("getPrivilegeGroupClass", privilegeGroupMap);

$(function() {
    $('#mainList').mergeCell({
        cols: [0]
    });
    $(".chk_defval").change(function() {
        var $_self = $(this);
        var v = 0;
        $(".defval" + $_self.data("for") + ":checked").each(function() {
            v += parseInt($(this).val());
        });
        $("#v_" + $_self.data("for")).val(v);
    });
    $.fn.serializeObject = function() {
        var o = [];
        var a = this.serializeArray();
        var t = {};
        $.each(a, function(i,data) {
             if(t[data.name]){
             	 o.push(t);
                 t={};
                 t[data.name] = this.value || '';
             }else{
            	 t[data.name] = this.value || '';
            	 if(i==a.length-1){
            		 o.push(t);
            	 }
             }
         });
         return o;
    };
    $("#btnSave").click(function() {
        var $_form = $("#privilegeForm");
        if (fieldEmptyValidate($_form)) {
            SQ.tips.ask({
                content: "您当前提交了修改权限参数的请求，是否继续？",
                title: "修改权限参数提交确认",
                ok: function() {
                    SQ.post({
                        url: $_form.attr("action"),
                        contentType: "application/json; charset=utf-8",
                        data: /*$_form.serialize()*/JSON.stringify($_form.serializeObject())
                    });
                }
            });
        }
        return false;
    });
    
});
