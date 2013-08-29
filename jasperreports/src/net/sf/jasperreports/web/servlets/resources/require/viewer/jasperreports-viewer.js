define(["jasperreports-loader", "jasperreports-report", "jquery-1.10.2"], function(Loader, Report, $) {
	var Viewer = function(o) {
        this.config = {
            at: null,
            reporturi: null,
            async: true,
            page: 0,
            toolbar: true,
            applicationContextPath: null
        };

        $.extend(this.config, o);

        this.reportInstance = null;
    };
    
    Loader.prototype._errHandler = function(jqXHR, textStatus, errorThrown) {
    	var jsonMsg = $.parseJSON(jqXHR.responseText);
    	console.error(jsonMsg);
    };

    Viewer.prototype = {
        loadReport: function() {
            var it = this;

            it.reportInstance = new Report({
                reporturi: it.config.reporturi,
                async: it.config.async,
                page: it.config.page
            });

            it.reportInstance.loader.UrlManager.applicationContextPath = it.config.applicationContextPath;

            it._setupEventsForReport(it.reportInstance);

            return it.reportInstance.init();
        },

        // internal functions
        _render: function(htmlOutput) {
            var it = this;
            // place output into container
            var container = $('#' + it.config.at);
            if (container.size() === 0) {
                container = $('.' + it.config.at);
            }
            container.html(htmlOutput);
        },
        _setupEventsForReport: function(report) {
            var it = this;

            report.on("reportHtmlReady", function() {
                it._render(this.html);
            }).on("undo, redo, undoall, action", function() {
                this.gotoPage(0);
            }).on("pageModified", function() {
                this.refreshPage(this.currentpage);
            }).on("componentsRegistered", function() {
            	var components = it.reportInstance.components,
                    uimodules = [],
                    uimodule;

                $.each(components, function(i, componentArray) {
                    if (componentArray.length > 0) {
                        uimodule = componentArray[0].config.uimodule;
                        if (uimodule) {
                            uimodules.push(uimodule);
                        }
                    }
                });

                if(uimodules.length) {
                    require(uimodules, function() {
                        $.each(arguments, function(i, thisModule) {
                            thisModule.init(it.reportInstance);
                        });
                    });
                }
            });

            // temporary bind existing toolbar ui
            var toolbar = $("#toolbar");
            $(".pageNext, .pagePrevious").removeClass("disabledViewerButton");

            toolbar.on("click", function(evt) {
                var target = $(evt.target);
                if (target.is('.pageNext')) {
                    report.gotoPage(parseInt(report.currentpage) + 1);
                } else if (target.is('.pagePrevious')) {
                    report.gotoPage(parseInt(report.currentpage) - 1);
                }
            });
        }
    };

    return Viewer;
});
