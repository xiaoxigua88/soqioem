$(function() {
    //初始化省的信息
    var $_provinceSelect = $("#province");
    var $_citySelect = $("#city");
    var provinceData = [];
    //取cityData.js中的cityData;
    $.each(cityData, function(name, value) {
        provinceData.push({
            id: name,
            name: name
        });
    });

    SQ.dom.fillSelectOptions($_provinceSelect, provinceData);
    // 填充默认的省数据
    $_provinceSelect.val(jsData.address.Province || 0);

    //初始化城市信息
    fillCity();

    $_provinceSelect.change(function() {
        $_citySelect.empty().val("");
        fillCity();
    });
    // 填充默认的市数据
    $_citySelect.val(jsData.address.City || 0);

    function fillCity() {
        var selectCityData = [];
        $.each(cityData[$_provinceSelect.val()], function(i, value) {
            selectCityData.push({
                id: value,
                name: value
            });
        });
        SQ.dom.fillSelectOptions($_citySelect, selectCityData);
    };

    $("#saveAddressInfo").click(function() {
        var $_addressInfo = $("#addressInfo");
        if (fieldEmptyValidate($_addressInfo)) {
            SQ.post({
                url: $_addressInfo.attr("action"),
                data: $_addressInfo.serialize()
            });
        }
    });
});