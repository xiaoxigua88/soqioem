$(function() {
    //角色编辑
    $(".role-edit").click(function() {
        var roleid = $(this).data("roleid");
        SQ.post({
            url: "/oemmanager/customer/getRoleInfo",
            data: {
            	roleid: roleid
            },
            isCoverSuccess: true,
            success: function(json) {
                if (json.result == false) {
                    SQ.tips.error({ content: json.text });
                    return;
                }
                $.dialog({
                    title: (typeof json.role != "undefined") ? "角色编辑" : "新增角色",
                    cancel: true,
                    okVal: "保存",
                    cancelVal: "关闭",
                    content: template("editInformation", (typeof json.role != "undefined") ? { role: json.role, privilegeList: json.privilegeList, priEnumList: jsData.priEnumList} : { privilegeList: json.privilegeList, priEnumList: jsData.priEnumList }),
                    init: function() {
                        $('#privilegeList').mergeCell({
                            cols: [0]
                        });
                        $("#pri_chkall").change(function() {
                            var v = $(this).prop("checked");
                            $(".chk_defval").each(function() {
                                if (!$(this).prop("disabled")) {
                                    $(this).prop("checked", v).change();
                                }
                            });
                        });
                        $(".chk_defval").change(function() {
                            var $_self = $(this);
                            var v = 0;
                            $(".defval" + $_self.data("for") + ":checked").each(function() {
                                v += parseInt($(this).val());
                            });
                            $("#v_" + $_self.data("for")).val(v);
                        });
                        $(".chk_defval").change();
                    },
                    ok: function() {
                        var $_form = $("#editForm");
                        if (fieldEmptyValidate($_form)) {
                            SQ.post({
                                url: $_form.attr("action"),
                                contentType: "application/json; charset=utf-8",
                                data: /*$_form.serialize()*/JSON.stringify(getJSON())
                            });
                        }
                        return false;
                    }
                });
            }
        });
        return false;
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
         if(o.length==1){
        	 //如果数组中对象长度为1则无必要生成数组
        	 return t;
         }
         return o;
     };
     
     var getJSON = function(){
    	 var roleData = $("#editForm input[type != hidden]").serializeObject();
    	 var privileges = $("#editForm input[type = hidden]").serializeObject();
    	 roleData.privileges = privileges;
    	 return roleData;
     }
});