$(function() {
    $(".detail").click(function() {
        $(".detail").removeClass("active");
        var _this = $(this);
        _this.addClass("active");
        $("#payBtn").prop("disabled", false);
    });
    $("#payBtn").click(function() {
        
    });
});