$(function() {
    $("#btnSave").click(function() {
        var $_businesspriceForm = $("#businesspriceForm");
        if (fieldEmptyValidate($_businesspriceForm)) {
            SQ.tips.ask({
                content: "您当前提交了修改查询业务价格的请求，是否继续？",
                title: "修改查询业务价格提交确认",
                ok: function() {
                    SQ.post({
                        url: $_businesspriceForm.attr("action"),
                        data: $_businesspriceForm.serialize()
                    });
                }
            });
        }
        return false;
    });
});