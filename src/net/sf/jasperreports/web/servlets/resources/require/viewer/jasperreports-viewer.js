define(["jasperreports-report", "jquery"], function(Report, $) {
	var Viewer = function(o) {
        this.config = {
            at: null,
            reporturi: null,
            async: true,
            page: 0,
            toolbar: true
        };

        $.extend(this.config, o);

        this.reportInstance = null;
    };

    Viewer.prototype = {
        loadReport: function() {
            var it = this;

            if (it.config.toolbar) {
                // register toolbar
            }

            it.reportInstance = new Report({
                reporturi: it.config.reporturi,
                async: it.config.async,
                page: it.config.page
            });

            it.reportInstance.init().then(
                function(html) {
                    it._render(html);
                    console.log("report rendered");
                },
                function(err) {
                    console.error(err);
                }
            );

            return it.reportInstance;
        },
        _render: function(htmlOutput) {
            var it = this;
            // place output into container
            var container = $('#' + it.config.at);

            if (container.size() === 0) {
                container = $('.' + it.config.at);
            }

            container.html(htmlOutput);
        }
    };

    return Viewer;
});
