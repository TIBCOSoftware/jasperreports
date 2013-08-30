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
        this.undoRedoCounters = {
            undos: 0,
            redos: 0
        };
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
            var it = this,
                toolbar = $("#toolbar");

            report.on("reportHtmlReady", function() {
                it._render(this.html);
                it._updateToolbarPaginationButtons(toolbar);
            }).on("action", function() {
                this.gotoPage(0);
                it.undoRedoCounters.undos++;
                it.undoRedoCounters.redos = 0;
                it._updateUndoRedoButtons(toolbar);
            }).on("undo", function() {
                this.gotoPage(0);
                it.undoRedoCounters.redos ++;
                it.undoRedoCounters.undos --;
                if (it.undoRedoCounters.undos <= 0) {
                    it.undoRedoCounters.undos = 0;
                }
                it._updateUndoRedoButtons(toolbar);
            }).on("redo", function() {
                this.gotoPage(0);
                it.undoRedoCounters.undos ++;
                it.undoRedoCounters.redos --;
                if (it.undoRedoCounters.redos <= 0) {
                    it.undoRedoCounters.redos = 0;
                }
                it._updateUndoRedoButtons(toolbar);
            }).on("pageModified", function() {
                this.refreshPage(this.currentpage);
            }).on("reportFinished", function() {
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

            toolbar.on("click", function(evt) {
                var target = $(evt.target);

                if (target.is('.disabledViewerButton')) {
                    // do nothing
                    return;
                }

                if (target.is('.pageNext')) {
                    report.gotoPage(parseInt(report.currentpage) + 1);
                } else if (target.is('.pagePrevious')) {
                    report.gotoPage(parseInt(report.currentpage) - 1);
                } else if (target.is('.pageFirst')) {
                    report.gotoPage(0);
                } else if (target.is('.pageLast')) {
                    report.gotoPage(report.status.totalPages - 1);
                } else if (target.is('.undo')) {
                    report.undo();
                } else if (target.is('.redo')) {
                    report.redo();
                }
            });
        },
        _updateUndoRedoButtons: function(jqToolbar) {
            var it = this,
                utils = it._toolbarUtils,
                counters = it.undoRedoCounters,
                btnUndo = $('.undo', jqToolbar),
                btnRedo = $('.redo', jqToolbar);

            // undo
            if (counters.undos > 0) {
                utils.enableElem(btnUndo);
            } else {
                utils.disableElem(btnUndo);
            }
            // redo
            if (counters.redos > 0) {
                utils.enableElem(btnRedo);
            } else {
                utils.disableElem(btnRedo);
            }
        },
        _updateToolbarPaginationButtons: function (jqToolbar) {
            var it = this,
                currentPage = it.reportInstance.currentpage,
                totalPages = it.reportInstance.status.totalPages,
                pageFirst = $('.pageFirst', jqToolbar),
                pagePrevious = $('.pagePrevious', jqToolbar),
                pageNext = $('.pageNext', jqToolbar),
                pageLast = $('.pageLast', jqToolbar),
                undo = $('.undo', jqToolbar),
                redo = $('.redo', jqToolbar),
                utils = it._toolbarUtils,
                classEnabled = utils.getClassEnabled(),
                classDisabled = utils.getClassDisabled();

            if (!totalPages) {
                utils.enableElem(pageNext);
                utils.disableElem(pageLast);
            }
            else if (totalPages > 1 && currentPage < totalPages - 1) {
                utils.enablePair(pageNext, pageLast);
            } else {
                utils.disablePair(pageNext, pageLast);
            }

            if (currentPage == 0) {
                utils.disablePair(pageFirst, pagePrevious);
            } else {
                utils.enablePair(pageFirst, pagePrevious);
            }

            if (!(undo.hasClass(classEnabled) || undo.hasClass(classDisabled))) {
                utils.disableElem(undo);
            }
            if (!(redo.hasClass(classEnabled) || redo.hasClass(classDisabled))) {
                utils.disableElem(redo);
            }
        },
        _toolbarUtils: (function() {
            var classEnabled = 'enabledViewerButton',
                classDisabled = 'disabledViewerButton';

            return {
                getClassEnabled: function() {
                    return classEnabled;
                },
                getClassDisabled: function() {
                    return classDisabled;
                },
                enableElem: function(jqElem) {
                    jqElem.removeClass(classDisabled);
                    jqElem.addClass(classEnabled);
                },
                disableElem: function(jqElem) {
                    jqElem.removeClass(classEnabled);
                    jqElem.addClass(classDisabled);
                },
                enablePair: function(jqElem1, jqElem2){
                    this.enableElem(jqElem1);
                    this.enableElem(jqElem2);
                },
                disablePair: function(jqElem1, jqElem2){
                    this.disableElem(jqElem1);
                    this.disableElem(jqElem2);
                }
            };
        }())
    };

    return Viewer;
});
