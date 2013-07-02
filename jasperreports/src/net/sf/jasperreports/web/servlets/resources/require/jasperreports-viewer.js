define(["jasperreports-loader", "jasperreports-report", "jquery/amd/jquery-1.7.1"], function(jr, Report, $) {
	return {
        resultViewer: $('div.result'),
        reportInstance: null,
		load: function(objOptions) {
            var it = this;

			var settings = {
					reporturl: null,
					serializedParams: '',
					
					width: 300,
					height: 400,
					containerid: 'jivecontainer',
					toolbar: true,
					async: true
			};
			
			$.extend(settings, objOptions);
            /*
                Serialized Parameters contain jr.report & jr.ctxid parameters
             */
			var reportParams = $.parseJSON(settings.serializedParams);

            $.ajax(settings.reporturl, {
                type: 'POST',
                data: reportParams,
                success: function(data, textStatus, jqXHR) {
                    reportParams.url = settings.reporturl;
                    reportParams.html = data;

                    it.reportInstance = new Report(reportParams);
                    it.render();
                }
            }); 
		},

        /*
            Method to parse script.jasperreports tags. Allows for scoping Report object to code in HTML output.
         */
        parse: function() {
            var Report = this.reportInstance;
            /*
                Parse thru HTML and extract script tags.
             */
            Report.scripts = [];

            $(Report.config.html).each(function() {
                this.tagName == 'SCRIPT' && Report.scripts.push(this.innerHTML);
            });

            /*
                Remove script tags from HTML so that they are not evaluated during DOM injection.
             */
            var dom = $('<div>' + Report.config.html + '</div>');
            dom.remove('script.jasperreports');
            Report.config.html = dom.html();
        },

        /*
            Method to eval() scripts sent with report HTML.
         */
        postProcess: function() {
            var DFDs = [];
            var Report = this.reportInstance;
            var onComponentsReady = new $.Deferred();

            $.each(Report.scripts, function(i, script) {
                /*
                    Notes:
                        - Report and DFD variables must be in scope for eval(). Both variables are used in
                          HeaderToolbarElementHtmlTemplate.vm.
                        - Refactoring JIVE code to Report.register() wrapped in require().
                 */
                var DFD = new $.Deferred();
                eval(script);
                DFDs.push(DFD);
            });

            $.when.apply($, DFDs).then(function() {
                onComponentsReady.resolve();
            });
            return onComponentsReady;
        },

        /*
            Method to parse HTML, inject report HTML into DOM and eval scripts.
         */
        render: function() {
            var dfd = $.Deferred();
            var Report = this.reportInstance;

            this.parse(Report);
            this.resultViewer.html(Report.config.html);
            this.postProcess(Report).then(function() {
                console.info('ready');
                dfd.resolve();
            });

            return dfd;
        },

        /*
            Series of test that can be called from the console.
         */
        test: function(testName) {
            var it = this;
            var Report = this.reportInstance;

            switch(testName) {
                case 'goto':
                    Report.goToPage({requestedPage: 2}).then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });
                    return 'Test description: navigate to 2nd page.';
                    break;
                case 'sort':
                    Report.components.column[0].sort({order: 'Asc'}).then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: sort 1st column in ascending order.';
                    break;
                case 'move':
                    Report.components.column[1].move({index: 3}).then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: move 2nd column to 4th position.';
                    break;
                case 'resize':
                    Report.components.column[0].resize({width: 200}).then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: resize 1st column to 200.';
                    break;
                case 'filter':
                    Report.components.column[1].filter({fieldValueStart: 'Robert', filterTypeOperator: 'CONTAINS'}).then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: filter 2nd column for "Roberts"';
                    break;
                case 'hide':
                    Report.components.column[1].hide().then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: hide 2nd column';
                    break;
                case 'unhide':
                    Report.components.column[1].unhide().then(function(data) {
                        Report.config.html = data;
                        it.render();
                    });

                    return 'Test description: unhide 2nd column';
                    break;
            }
        }
	};
});
