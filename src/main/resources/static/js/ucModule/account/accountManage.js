$(function() {
    // 基本信息页面，保存基本信息
    $("#savingInfo").click(function() {
        var $_infoForm = $("#basicInfoForm");
        var actionUrl = $_infoForm.attr("action");
        if (!fieldEmptyValidate($_infoForm)) {
            return false;
        }

        SQ.post({
            url: actionUrl,
            data: $_infoForm.serialize()
        });
    });

    // 保存密码
    $("#savePwd").click(function() {
        var $_pwdForm = $("#changePassword");
        var $_pwdInputs = $(".password-input");
        var $_reminder = $(".error-reminder");
        var $_oldPwdInput = $("#oldPwdInput");
        var $_newPwdInput = $("#newPwdInput");
        var $_rePwdInput = $("#rePwdInput");
        var errorClassName = "error-input";
        var reminderClassName = ".error-reminder";
        var inputNameMap = {
            "oldPassword": $_oldPwdInput,
            "password": $_newPwdInput,
            "password2": $_rePwdInput
        };
        var actionUrl = $_pwdForm.attr("action");
        var frontValidFailCall = function($_input, remindWords) {
            $_input.addClass(errorClassName);
            $_input.parent().find(reminderClassName).html(remindWords);
            $_input.focus();
            isFrontValidateOK = false;
        };
        // 前端验证通过标识
        var isFrontValidateOK = true;
        // 清空警告
        $_pwdInputs.removeClass(errorClassName);
        $_reminder.empty();
        // 表单验证
        if (!$_oldPwdInput.val()) {
            frontValidFailCall($_oldPwdInput, "旧密码不能为空");
            return false;
        } else {
            $.post({
                url: 'ajax.aspx?action=CheckOldPassword',
                dataType: 'json',
                data: { oldPassword: $_oldPwdInput.val() },
                success: function(data) {
                    if (data.result) {
                        // 清空警告
                        $_oldPwdInput.removeClass(errorClassName);
                        $_reminder.empty();

                        if (!$_newPwdInput.val()) {
                            frontValidFailCall($_newPwdInput, "新密码不能为空");
                            return false;
                        }
                        if (!new RegExp('^(?![^a-zA-Z]+$).(?!\D+$).{8,30}$').test($_newPwdInput.val())) {
                            frontValidFailCall($_newPwdInput, "新密码格式不对");
                            return false;
                        }
                        if ($_newPwdInput.val() != $_rePwdInput.val()) {
                            frontValidFailCall($_rePwdInput, "两次密码填写不一致");
                            return false;
                        }
                        // 未通过前端验证则不提交表单
                        if (!isFrontValidateOK) {
                            return;
                        }
                        $_pwdForm.find(".protection-enabled").trigger("click");
                    } else {
                        frontValidFailCall($_oldPwdInput, data.text || "旧密码错误");
                        return false;
                    }
                }
            });
        }
    });

    /**
    * 修改绑定手机
    */
    // step 1
    // 验证原验证码
    var $_modifyMobileForm = $("#modifyMobileForm");
    $("#modifyMobileVerify").click(function() {
        if (!fieldEmptyValidate($_modifyMobileForm)) {
            return false;
        }

        SQ.post({
            data: $_modifyMobileForm.serialize(),
            url: "ajax.aspx?action=CheckMobileCode",
            isCoverSuccess: true,
            success: function(json) {
                if (json.result) {
                    location.href = "mobileset.aspx";
                }
                else {
                    validateShowError("verifyCode", json.text, $_modifyMobileForm);
                }
            }
        });
    });

    // step 2
    // 验证新短信验证码并保存
    var $_setNewMobileForm = $("#setNewMobileForm");
    $("#bindNewMobile").click(function() {
        if (!fieldEmptyValidate($_setNewMobileForm)) {
            return false;
        }

        SQ.post({
            data: $_setNewMobileForm.serialize(),
            url: $_setNewMobileForm.attr("action"),
            successResultFalse: function(json) {
                validateShowError("verifyCode", json.text, $_setNewMobileForm);
            }
        });
    });

    /**
    * 绑定邮箱
    */
    var $_bindEmailForm = $("#bindEmailForm");
    $("#bindEmail").click(function() {
        if (!fieldEmptyValidate($_bindEmailForm)) {
            return false;
        }

        SQ.post({
            data: $_bindEmailForm.serialize(),
            url: $_bindEmailForm.attr("action"),
            successResultFalse: function(json) {
                validateShowError("verifyCode", json.text, $_bindEmailForm);
            }
        });
    });

    /**
    * 修改邮箱验证
    */
    // step 1
    // 验证原邮箱验证码
    var $_modifyEmailForm = $("#modifyEmailForm");
    $("#modifyEmailVerify").click(function() {
        if (!fieldEmptyValidate($_modifyEmailForm)) {
            return false;
        }
        SQ.post({
            data: $_modifyEmailForm.serialize(),
            url: "ajax.aspx?action=CheckEmailCode",
            isCoverSuccess: true,
            success: function(json) {
                if (json.result) {
                    location.href = "emailset.aspx";
                }
                else {
                    validateShowError(json.name, json.text, $_modifyEmailForm);
                }
            }
        });
    });

    // step 2
    // 验证新邮箱验证码并验证保存
    var $_setEmailForm = $("#setNewEmailForm");
    $("#bindNewEmail").click(function() {
        if (!fieldEmptyValidate($_setEmailForm)) {
            return false;
        }
        SQ.post({
            data: $_setEmailForm.serialize(),
            url: $_setEmailForm.attr("action"),
            successResultFalse: function(responseJSON) {
                validateShowError(responseJSON.name, responseJSON.text, $_setEmailForm);
            }
        });
    });

    /**
    * 修改密保
    */
    var $_setNewProtectForm = $("#setNewProtectForm");
    $("#saveNewProtect").click(function() {
        if (!fieldEmptyValidate($_setNewProtectForm)) {
            return false;
        }
        SQ.post({
            data: $_setNewProtectForm.serialize(),
            url: $_setNewProtectForm.attr("action"),
            successResultFalse: function(json) {
                validateShowError(json.name, json.text, $_setNewProtectForm);
            }
        });
    });
});