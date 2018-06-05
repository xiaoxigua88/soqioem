$(function () {
	// 新增工单
	// 根据初始问题分类值，初始化dropDown组件
	SQ.component.initDropDown({
		defaultOptionValue: $("#initialClassId").html()
	});
	// 产品IP下拉菜单
	SQ.component.initDropDown({
		containerSelector: ".product-ip-drop"
	});

	$("#productTypeChange input").click(function () {
		$("#productTypeInput").val($(this).data("product_type"));
	});

	$("#typeIdInput").change(function () {
		var $_productBlock = $(".product-info-group");
		if ($(this).val() > 1) {
			$_productBlock.addClass("hide");
		}
		else {
			$_productBlock.removeClass("hide");
		}
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

	// 提交工单
	$("#submitWorkorder").click(function () {

		var visibleInputSelector = "input[type=text]:visible";
		var inputReminderSelector = ".input-reminder";
		var $_workorderApplyForm = $("#workorderApplyForm");
		var $_visibleInputs = $_workorderApplyForm.find(visibleInputSelector);
		var $_textarea = $_workorderApplyForm.find("textarea");
		var $_attachmentInput = $("#attachmentInput");
		clearValidateError($_workorderApplyForm);
		$(inputReminderSelector).removeClass("hide");

		// 填写附件表单
		var attachmentsPathArray = [];
		$(".attachment-item img").each(function () {
			var filePath = $(this).data("file_path");
			// 如果没有成功的获取图片路径，则不填入数组
			if (filePath) {
				attachmentsPathArray.push(filePath);
			}
		});
		$_attachmentInput.val(attachmentsPathArray.join(","));

		// 前端表单验证
		var isWorkorderFilled = true;

		$_visibleInputs.each(function () {
			var $_self = $(this);
			if (!$_self.val()) {
				$_self.parents(".validate-control").find(inputReminderSelector).addClass("hide");
				validateShowError($_self, "该项不能为空", $_workorderApplyForm);
				isWorkorderFilled = false;
				return false;
			}
		});

		if (!$_textarea.val()) {
			validateShowError($_textarea, "该项不能为空", $_workorderApplyForm);
			isWorkorderFilled = false;
		}

		if (isWorkorderFilled) {
			SQ.post({
				data: $_workorderApplyForm.serialize(),
				url: $_workorderApplyForm.attr("action")
			});
		}

	});
});