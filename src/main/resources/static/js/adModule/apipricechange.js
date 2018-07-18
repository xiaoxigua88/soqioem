$(function() {
	if($(".operate").length>0){
		$(".savebutton").removeClass("hide");
	}else{
		$(".savebutton").addClass("hide");
	}
	$("#addrow,#removerow").click(function() {
		var operatetype = $(this).data("operatetype");
		if(operatetype == "add"){
			var length = $(".operate").length + 1;
			var node = "<div class='form-group operate'>" +
					"<div class='col-xs-7 sq-form-control'>" +
					"<label class=\"search-group-label\">价格区间：</label>" +
					"<span id=\"pricefirst_" + length + "\"><input type=\"text\" name=\"pricefirst_" + length + "\" class=\"sq-input-reset sq-label-mini necessary\" placeholder=\"\" autocomplete=\"off\" /></span>" +
					"<label class=\"search-group-label\">至：</label>" +
					"<span id=\"pricelast_" + length + "\"><input type=\"text\" name=\"pricelast_" + length + "\" class=\"sq-input-reset sq-label-mini necessary\" placeholder=\"\" autocomplete=\"off\" /></span>" +
					"<label class=\"search-group-label\">浮动：</label>" +
					"<span id=\"range_" + length + "\"><input type=\"text\" name=\"range_" + length + "\" class=\"sq-input-reset sq-label-mini necessary\" placeholder=\"\" autocomplete=\"off\" /></span>" +
					"</div>" +
					"</div>" ;
			$("#operateArea").append(node);
		}else if(operatetype == "remove"){
			$(".operate:last").remove();
		}
		if($(".operate").length>0){
			$(".savebutton").removeClass("hide");
		}else{
			$(".savebutton").addClass("hide");
		}
		$("#groupLength").val($(".operate").length);
    });
    // 保存密码
    $("#saveApiPrice").click(function() {
        var $_form = $("#saveApiPriceForm");
        var actionUrl = $_form.attr("action");
        if (!fieldEmptyValidate($_form)) {
            return false;
        }
        SQ.post({
            url: actionUrl,
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
        return false;
    });
});