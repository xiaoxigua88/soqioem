$(function() {
    // 合同查看
    $(".btn-download").click(function() {
        var contractId = $(this).data("contractid");
        SQ.tips.ask({
            title: "电子合同查看提示",
            content: "电子合同为标准的PDF文件，非标准PDF软件可能无法查看电子印章。<br />谷歌浏览器（Chrome）内核大部分版本能正常显示电子印章。<br />IE内核、火狐等浏览器可能无法显示印章，请保存到本地使用专业的PDF软件查看。<br />推荐使用软件Adobe Reader PDF或Smart PDF软件查看。",
            okVal: "了解，查看",
            ok: function() {
                SQ.post({
                    data: { contractId: contractId },
                    url: "?action=Download"
                });
            }
        });
        return false;
    });
});