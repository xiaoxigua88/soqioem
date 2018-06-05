$(function () {
	// 产品IP下拉菜单
	SQ.component.initDropDown({
		containerSelector: ".product-ip-drop"
	});

	// 用户默认号码填写
	var $_defaultMobileSign = $("#defaultMobileSign");
	var $_defaultQQSign = $("#defaultQQSign");

	$(".set-default-number").click(function () {
		var $_self = $(this);
		var connectType = $_self.data("default_type");
		var $_parent = $_self.parent();
		$_parent.find("input").val($_parent.find(".default-user-number").html());

		if (connectType == 1) {
			$_defaultMobileSign.val(1);
		}
		else {
			$_defaultQQSign.val(1);
		}
	});

	// 当手机/QQ输入框值改变时，对应输入类型改为用户输入
	$("#userMobile").change(function () {
		$_defaultMobileSign.val(0);
	});
	$("#userQq").change(function () {
		$_defaultQQSign.val(0);
	});

	// 初始化附件上传触发器
	SQ.biz.multiUpload({
		addButtonSelector: "#addAttachment"
	});

	$("#submitFeedback").click(function () {

		var inputReminderSelector = ".input-reminder";
		var $_feedbackForm = $("#feedbackForm");
		var $_neededInputs = $(".validate-control input");
		var $_textarea = $_feedbackForm.find("textarea");
		// 清空错误提示
		clearValidateError($_feedbackForm);
		$(inputReminderSelector).removeClass("hide");
		// 前端表单验证
		var isFormFilled = true;
		$_neededInputs.each(function () {
			var $_self = $(this);
			if (!$_self.val()) {
				$_self.parents(".validate-control").find(inputReminderSelector).addClass("hide");
				validateShowError($_self, "该项不能为空", $_feedbackForm);
				isFormFilled = false;
				return false;
			}
		});
		if (!$_textarea.val()) {
			validateShowError($_textarea, "请填写内容", $_feedbackForm);
			isFormFilled = false;
		}

		if (isFormFilled) {
			SQ.post({
				data: $_feedbackForm.serialize(),
				url: $_feedbackForm.attr("action")
			});
		}

	});
});