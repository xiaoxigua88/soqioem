$(function() {
    // 导出
    $("#batchExport").click(function() {
        SQ.tips.ask({
            title: "关键词优化导出确认",
            content: "确定要导出关键词优化报表吗？",
            ok: function() {
                SQ.post({
                    data: { action: "Export", webId: $("#webId").val() },
                    url: ""
                });
            }
        });
    });
});