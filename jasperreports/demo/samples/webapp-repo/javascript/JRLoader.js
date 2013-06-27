/**
 * Created with IntelliJ IDEA.
 * User: gtruong
 * Date: 6/27/13
 * Time: 6:08 AM
 * To change this template use File | Settings | File Templates.
 */
define(['jquery/amd/jqueryui-1.8.18', 'jasperreports'], function($, JR){
    var loader = new function() {

        this.open = function(reportUrl) {
            $.ajax(reportUrl, {
                type: 'POST',
                success: function(data, textStatus, jqXHR) {
                    console.info(data);

                    new JR({
                        ctxid: '',
                        url: '',
                        html: data
                    });
                }
            });
        }
    };

    return loader;
});
