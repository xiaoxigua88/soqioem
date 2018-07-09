$(function() {
	SQ.component.initTabs();
	$(".searchtypes input").click(function() {
		var searchtype = $(this).parent().data("searchtype");
		var discounttype_searchtype = $(this).data("discounttype_" + searchtype);
		$("#discounttype_" + searchtype).val(discounttype_searchtype);
		 if(discounttype_searchtype=='1'){
			 $("#fixprice_" + searchtype).removeClass("hide");
			 $("#minprice_" + searchtype).addClass("hide");
			 $("#maxprice_" + searchtype).addClass("hide");
			 $("#ratio_" + searchtype).addClass("hide");
        }else if(discounttype_searchtype=='2'){
        	$("#fixprice_" + searchtype).addClass("hide");
			 $("#minprice_" + searchtype).removeClass("hide");
			 $("#maxprice_" + searchtype).removeClass("hide");
			 $("#ratio_" + searchtype).removeClass("hide");
        }
        /*$("#discountType").val($(this).data("discounttype"));
        if($(this).data("discounttype")=='1'){
        	$("#discountPrice").attr('placeholder',"大于0的有效数字比如：2.5");
        }else if($(this).data("discounttype")=='2'){
        	$("#discountPrice").attr('placeholder',"大于0小于1的小数比如：0.85");
        }
        $("#discountPrice").val('');
        $("#discountPrice").focus();*/
    });
    // 保存密码
    $("#savepricetempl").click(function() {
        var $_form = $("#savepriceform");
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