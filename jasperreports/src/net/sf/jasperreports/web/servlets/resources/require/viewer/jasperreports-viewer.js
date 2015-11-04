/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

define(["jasperreports-loader", "jasperreports-report", "jquery.ui", "jasperreports-url-manager"], function(Loader, Report, $, UrlManager) {
	var Viewer = function(o) {
        var it = this;

        it.config = {
            at: null,
            reporturi: null,
            async: true,
            page: 0,
            toolbar: true,
            applicationContextPath: null
        };

        $.extend(it.config, o);

        it.config.applicationContextPath && (UrlManager.applicationContextPath = it.config.applicationContextPath);

        it.reportInstance = null;
        it.container = it._getContainer();
        it.hyperlinkHandlers = {};
        it.undoRedoCounters = {
            undos: 0,
            redos: 0
        };

        it.renderNow = false;
        it.renderReportLater = false;
        it.dfds = {
            'jive.inactive': null
        };

        $('body').on({
            'jive.initialized': function(evt, jive) {
                it.jive = jive;
            },
            'jive.inactive': function() {
                it.dfds['jive.inactive'] && it.dfds['jive.inactive'].resolve();
            }
        });

        it.loadMask = {
            jqMask: null,
            show: function() {
                var c = it.container;
                if (this.jqMask == null) {
                    this.jqMask = $("<div class='_jrLoadMask_' style='position: absolute; display: none; cursor: wait; z-index: 1000'>");
                    this.jqMask.css({
                        backgroundColor:	"grey",
                        opacity:            0.3,
                        borderTopWidth:	    c.css('borderTopWidth'),
                        borderTopStyle:	    c.css('borderTopStyle'),
                        borderBottomWidth:  c.css('borderBottomWidth'),
                        borderBottomStyle:	c.css('borderBottomStyle'),
                        borderLeftWidth:	c.css('borderLeftWidth'),
                        borderLeftStyle:	c.css('borderLeftStyle'),
                        borderRightWidth:	c.css('borderRightWidth'),
                        borderRightStyle:	c.css('borderRightStyle')
                    });
                    c.parent().append(this.jqMask);
                }

                if (c.height()) {
                    this.jqMask.show().css({
                        width:  c.width(),
                        height: c.height(),
                        left:   c.position().left
                    });
                    this.jqMask.offset({top: c.offset().top});
                } else {
                    this.jqMask.show().css({
                        width:  '100%',
                        height:	'100%',
                        left:   0,
                        top:    0
                    });
                }
            },
            hide: function() {
                this.jqMask && this.jqMask.hide();
            }
        };

        it.loadMask.show();

        Loader.prototype._errHandler = function(jqXHR, textStatus, errorThrown) {
            it.loadMask.hide();
            var jsonMsg = $.parseJSON(jqXHR.responseText),
                errDialogId = 'errDialog',
                errDialog = $('#' + errDialogId),
                msg;
            if (errDialog.size != 1) {
                errDialog = $("<div id='" + errDialogId + "'></div>");
                $('body').append(errDialog);
            }
            msg = "<p>" + jsonMsg.msg + "</p>";
            if(jsonMsg.devmsg) {
                msg += "<p>" + jsonMsg.devmsg + "</p>";
            }
            errDialog.html(msg);
            errDialog.dialog({
                title: 'Error',
                width: 530,
                height: 200
            });
        };
    };

    Viewer.prototype = {
        loadReport: function() {
            var it = this;

            it.reportInstance = new Report({
                reporturi: it.config.reporturi,
                async: it.config.async,
                page: it.config.page,
                container: it._getContainer()
            });

            it._setupEventsForReport(it.reportInstance);

            return it.reportInstance.init();
        },

        // internal functions
        _render: function(htmlOutput) {
            // place output into container
            this._getContainer().html(htmlOutput);
        },
        _getContainer: function() {
            if (!this.container) {
                var sel = this.config.at;
                this.container = $(sel);
                if (!this.container.length) {
                    this.container = $('#' + sel)
                    if (!this.container.length) {
                        this.container = $('.' + sel);
                    }
                }

                this.container.addClass('_jr_report_container_');
            }
            return this.container;
        },
        _setupEventsForReport: function(report) {
            var it = this,
                toolbar = $("#toolbar");

            report.on("reportHtmlReady", function() {
                it.renderReportLater = false;
                it.dfds['jive.inactive'] = new $.Deferred();

                if(it.jive) {
                    if(!it.jive.active || it.renderNow) {
                        it.renderNow && it.jive.hide();
                        it._render(this.html);
                        it.dfds['jive.inactive'].resolve();
                    } else {
                        it.renderReportLater = true;
                    }
                } else {
                    it._render(this.html);
                    it.dfds['jive.inactive'].resolve();
                }

                it._updateToolbarPaginationButtons(toolbar);
            }).on("action", function() {
                it.renderNow = false;
                this.gotoPage(0);
                it.undoRedoCounters.undos++;
                it.undoRedoCounters.redos = 0;
                it._updateUndoRedoButtons(toolbar);
            }).on("beforeAction", function() {
                it.loadMask.show();
                this.cancelStatusUpdates();
            }).on("undo", function() {
                it.renderNow = true;
                this.gotoPage(0);
                it.undoRedoCounters.redos ++;
                it.undoRedoCounters.undos --;
                if (it.undoRedoCounters.undos <= 0) {
                    it.undoRedoCounters.undos = 0;
                }
                it._updateUndoRedoButtons(toolbar);
            }).on("redo", function() {
                it.renderNow = true;
                this.gotoPage(0);
                it.undoRedoCounters.undos ++;
                it.undoRedoCounters.redos --;
                if (it.undoRedoCounters.redos <= 0) {
                    it.undoRedoCounters.redos = 0;
                }
                it._updateUndoRedoButtons(toolbar);
            }).on("search", function(data) {
                it.renderNow = false;
                if (data.actionResult.searchResults && data.actionResult.searchResults.length) {
                    var results = data.actionResult.searchResults;

                    results.sort(function(r1, r2) {
                        return r1.page - r2.page;
                    });

                    this.gotoPage(results[0].page).then(function() {
                        $('.jr_search_result').addClass('highlight');
                    });

                } else if (data.actionResult.searchString) {
                    alert("No results for: " + data.actionResult.searchString);
                }
            }).on("pageModified", function() {
                this.refreshPage(this.currentpage);
            }).on("reportFinished", function() {
                if (!this.status.pageFinal) {
                    this.refreshPage(this.currentpage);
                } else {
                    it._updateToolbarPaginationButtons(toolbar);
                }
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

                it.dfds['jive.inactive'].then(function() {
                    it.renderReportLater && it._render(it.reportInstance.html);
                    if(uimodules.length) {
                        require(uimodules, function() {
                            $.each(arguments, function(i, thisModule) {
                                thisModule.init(it.reportInstance);
                            });
                        });
                    }
                });

                /*
                 Handle webfonts
                 */
                if (components.webfonts && components.webfonts.length) {
                    var webFonts = components.webfonts[0].config.webfonts,
                        modules = [],
                        webFontsConfig = {paths: {}},
                        moduleName;

                    $.each(webFonts, function(i, webfont) {
                        moduleName = 'webfont_' + webfont.id;
                        modules.push('csslink!' + moduleName);
                        webFontsConfig.paths[moduleName] = webfont.path;
                    });

                    require.config(webFontsConfig);
                    require(modules, function() {
                        /*
                         IE Webfonts fix
                         */
                        if (/msie/i.test(navigator.userAgent)) {
                            var links = document.querySelectorAll('link.jrWebFont');
                            setTimeout(function() {
                                if (links) {
                                    for (var i = 0; i < links.length; i++) {
                                        links.item(i).href = links.item(i).href;
                                    }
                                }
                            }, 0);
                        }
                    });
                }
                
                /*
                 Handle hyperlinks
                 */
                if (components.hyperlinks && components.hyperlinks.length) {
                    var hyperlinks = components.hyperlinks[0].config.hyperlinks,
                        hyperlinksByType = {};

                    $.each(hyperlinks, function(i, hyperlink) {
                        if (!hyperlinksByType[hyperlink.type]) {
                            hyperlinksByType[hyperlink.type] = [];
                        }

                        hyperlinksByType[hyperlink.type].push(hyperlink);
                    });

                    $.each(hyperlinksByType, function(key, links) {
                        require(["jr." + key], function(HyperLinkHandler) {
                            var hh = new HyperLinkHandler(links);
                            hh.reportInstance = it.reportInstance;
                            hh.reportContainer = it.container;
                            hh.register();

                            it.hyperlinkHandlers[key] = hh;
                        }, function(err) {
                            var failedId = err.requireModules && err.requireModules[0];
                            if (failedId && console && console.error && typeof console.error === 'function') {
                                console.error("Failed to load module: '" + failedId + "' for handling hyperlinks of type: '" + key + "'!");
                            }
                        });
                    });
                }
                
                it.loadMask.hide();
            });

            report.on('hyperlinkInteraction', function(evt) {
                var hlType = evt.data.hyperlink.type;
                if (hlType && it.hyperlinkHandlers[hlType]) {
                    it.hyperlinkHandlers[hlType].handleInteraction(evt);
                }
            });

            toolbar.on("click", function(evt) {
                var target = $(evt.target);

                if (target.is('.disabledViewerButton')) {
                    // do nothing
                    return;
                }

                if (target.is('.pageNext')) {
                    it.renderNow = true;
                    report.gotoPage(parseInt(report.currentpage) + 1);
                } else if (target.is('.pagePrevious')) {
                    it.renderNow = true;
                    report.gotoPage(parseInt(report.currentpage) - 1);
                } else if (target.is('.pageFirst')) {
                    it.renderNow = true;
                    report.gotoPage(0);
                } else if (target.is('.pageLast')) {
                    it.renderNow = true;
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

            if (totalPages == null) {
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
