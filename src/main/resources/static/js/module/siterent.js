var center_ad_t = 0; //定时器
function center_ad_init() {
    //定位
    var left_val = ($(window).width() - $('.center_ad').width()) / 2;
    var top_val = ($(window).height() - $('.center_ad').height()) / 2;
    $('.center_ad').css({
        left: left_val,
        top: top_val
    });
}
$(".rent_close").click(function() {
    $(".center_ad").hide();
});
center_ad_init();
$(window).resize(center_ad_init);