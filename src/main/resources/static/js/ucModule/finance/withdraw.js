$(function () {
	// 收款方式点击切换
	var accountIdDataName = "id";
	$ (".payment-choices").click(function(){
		var $_self = $(this);
		$_self.addClass("methods-chosen").siblings().removeClass("methods-chosen");
		$("#accountIdInput").val($_self.data(accountIdDataName));
	});
	// 填写账号id表单
	$("#accountIdInput").val($(".methods-chosen").data(accountIdDataName));

	// 提现一系列费用计算
	// 手续费率
	var POUNDAGE_RATE = $("#poundageRate").html() / 100;
	// 提现金额梯度
	var MULTI = parseInt($("#withdrawMultiple").html());
	// 最小提现输入金额
	var MIN_MONEY = parseInt($("#withdrawMin").html());
	// 最小手续费
	var MIN_POUNDAGE = 1;

	var $_withdrawInput = $("#withdrawAmountInput");
	$_withdrawInput.keyup(function(e, isMulti){
		var $_self = $(this);
		var withdraw = parseInt($_self.val());
		if (!withdraw) {
			withdraw = MIN_MONEY;
		}

		if (isMulti) {
			if (withdraw < MIN_MONEY) {
				withdraw = MIN_MONEY;
			}

			var remainder = withdraw % MULTI;
			if (remainder != 0) {
				withdraw = withdraw - remainder;
				// 下句取消注释则向上一个梯度取整
				// withdraw += MULTI;
			}
		}

		var poundageInit = withdraw * POUNDAGE_RATE;
		if (poundageInit < MIN_POUNDAGE && poundageInit != 0) {
			poundageInit = MIN_POUNDAGE;
		}

		var poundageMoney = (Math.round(poundageInit * 100) / 100).toFixed(2);
		// 当输入为数字和backspace, ← → 键之外的值时，提现值不变
		if (e.which != 37 && e.which != 39 && e.which != 8 ) {
			$_self.val(withdraw);
		}

		// 手续费和实际到账填写
		$("#poundageContainer").html(poundageMoney);
		var actualMoney = (withdraw - poundageMoney).toFixed(2);
		if (actualMoney < 0) {
			actualMoney = 0;
		}
		$("#actualContainer").html(actualMoney);
	})
	.blur(function () {
		$(this).trigger("keyup", [true]);
	});

	// 初始化sq-btn-group
	SQ.component.initTabs();

	// 提交表单
	$("#applyWithdrawal").click(function () {
		if (Number(jsData.withdrawals.money) < MIN_MONEY) {
			SQ.warn("您的可提现金额不足");
			return false;
		}

		var $_applyForm = $("#paymentForm");
		// 表单验证
		clearValidateError($_applyForm);
		if (!$_withdrawInput.val()) {
			validateShowError($_withdrawInput, "该项不能为空", $_applyForm);
			return false;
		}
	});

	// banklist 删除账户
	$(".delete-account").click(function () {
		var $_self = $(this);
		SQ.tips.ask({
			title: "删除账户",
			content: '您确定要删除 <span class="text-stress">' + $_self.data("account_id") + '</span> ？',
			subTipContent: "确认后该账户将删除",
			ok: function () {
				SQ.post({
					data: {
						ID : $_self.data("encode_id")
					},
					url: "/user/verify/delbank.html"
				});
			}
		});
	});
});