$(function() {
    function createChat() {
        var _lr = document.createElement('script');
        _lr.type = 'text/javascript';
//        _lr.async = true;
//        _lr.charset = 'utf-8';
        _lr.src = 'http://pbt.zoosnet.net/LR/olist1.aspx?id=PBT27999282&lng=cn';
//        var root_lr = document.getElementsByTagName('script')[0].parentNode.insertBefore(_lr, root_lr);
//        root_lr.parentNode.insertBefore(_lr, root_lr);

        var _js = document.createElement('script');
        _js.type = 'text/javascript';
//        _js.async = true;
//        _js.charset = 'utf-8';
        _js.src = 'http://pbt.zoosnet.net/JS/LsJS.aspx?siteid=PBT27999282&float=0&lng=cn';
//        var root_js = document.getElementsByTagName('script')[0].parentNode.insertBefore(_js, root_js);
        //        root_js.parentNode.insertBefore(_js, root_js);

        document.body.appendChild(_lr);
        document.body.appendChild(_js);
    };
    createChat();
});