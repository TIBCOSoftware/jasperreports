define(["jasperreports-report", "jquery-1.10.2"], function(Report, $) {
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

            it.reportInstance = new Report({
                reporturi: it.config.reporturi,
                async: it.config.async,
                page: it.config.page
            });

            it._setupEventsForReport(it.reportInstance);

            return it.reportInstance.init();
        },

        /*
         Series of test that can be called from the console.
         */
        test: function(testName) {
            var Report = this.reportInstance;
            console.info(Report);

            switch(testName) {
                case 'goto':
                    Report.gotoPage(1);

                    return 'Test description: navigate to 2nd page.';
                    break;
                case 'sort':
                    Report.components.table[0].column[0].sort({order: 'Asc'});

                    return 'Test description: sort 1st column in ascending order.';
                    break;
                case 'move':
                    Report.components.table[0].column[1].move({index: 3});

                    return 'Test description: move 2nd column to 4th position.';
                    break;
                case 'resize':
                    Report.components.table[0].column[0].resize({width: 200});

                    return 'Test description: resize 1st column to 200.';
                    break;
                case 'filter':
                    Report.components.table[0].column[1].filter({fieldValueStart: 'Robert', filterTypeOperator: 'CONTAINS'});

                    return 'Test description: filter 2nd column for "Roberts"';
                    break;
                case 'hide':
                    Report.components.table[0].column[1].hide();

                    return 'Test description: hide 2nd column';
                    break;
                case 'unhide':
                    Report.components.table[0].column[1].unhide();

                    return 'Test description: unhide 2nd column';
                    break;
            }
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
            	var components = it.reportInstance.components;
                var modules = [];

                /*
                    Load and initialize jive modules
                 */
                if(components.table && components.table.length && components.table[0].columns && components.table[0].columns.length) {
                    modules.push('jive.interactive.column');
                }

                if(modules.length) {
                    require(modules, function() {
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
