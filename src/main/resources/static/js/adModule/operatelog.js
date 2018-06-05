$(function() {
    var changeModule = function(business) {
        $.post({
            url: "?action=GetModuleList",
            data: { business: business },
            dataType: 'json',
            success: function(json) {
                var html = '';
                $.each(json.moduleList, function(j, k) {
                    html += '<li><a href="#a_null" class="' + (jsData.filter.module == k ? "sq-default-option" : "") + '" data-drop="' + k + '">' + k + '</a></li>';
                });
                $("#moduleList").html(html);
                SQ.component.initDropDown({
                    containerSelector: ".log-module-drop"
                });
            }
        });
    };
    changeModule(jsData.filter.business);
    $("#txtBusiness").change(function() {
        changeModule($(this).val());
    });
});