define(["jasperreports-loader", "jasperreports-status-checker",
    "jasperreports-component-registrar", "jasperreports-event-manager", "jasperreports-report-processor",
    "jquery-1.10.1"], function (Loader, StatusChecker, ComponentRegistrar, EventManager, Processor, $) {

    var Report = function(o) {

        this.config = {
            reporturi: null,
            async: true,
            page: 0
        };
        $.extend(this.config, o);

        // report state members
        this.currentpage = this.config.page;
        this.html = null;
        this.status = null;
        this.components = null;


        // utils
        this.loader = new Loader({reporturi: this.config.reporturi, async: this.config.async});
        this.statusChecker = new StatusChecker(this.loader);
        this.componentRegistrar = new ComponentRegistrar(this.loader);

        // events
        this.eventManager = new EventManager();
        this.events = {
            ACTION_PERFORMED: "action",
            UNDO_PERFORMED: "undo",
            UNDO_ALL_PERFORMED: "undoall",
            REDO_PERFORMED: "redo",
            PAGE_MODIFIED: "pageModified",
            REPORT_HTML_READY: "reportHtmlReady"
        };
    };

    var api = {
        gotoPage: function(page) {
            this.statusChecker.cancelCheckPageModified();
            return this.refreshPage(page, true);
        },
        undo: function() {
            var it = this;
            return this.loader.runAction({action: {actionName: "undo"}})
                .then(function(jsonData) {
                    it._notify({
                        name: it.events.UNDO_PERFORMED,
                        type: "undo",
                        data: jsonData
                    });

                    return it;
                });
        },
        redo: function() {
            var it = this;
            return this.loader.runAction({action: {actionName: "redo"}})
                .then(function(jsonData) {
                    it._notify({
                        name: it.events.REDO_PERFORMED,
                        type: "redo",
                        data: jsonData
                    });

                    return it;
                });
        },
        undoAll: function() {
            var it = this;
            return this.loader.runAction({action: {actionName: "undoAll"}})
                .then(function(jsonData) {
                    it._notify({
                        name: it.events.UNDO_ALL_PERFORMED,
                        type: "undoall",
                        data: jsonData
                    });

                    return it;
                });
        }
    };

    $.extend(Report.prototype, api);

    var privateMethods = {
        _notify: function(evt) {
            console.log("report notified of event: " + evt.name + "; type: " + evt.type);
            this.eventManager.triggerEvent(evt.name);
        }
    }

    $.extend(Report.prototype, privateMethods);

    $.extend(Report.prototype, {
        init: function() {
            return this.refreshPage(this.currentpage);
        },
        refreshPage: function(page, boolNavigate) {
            var it = this;

            return it.loader.getHtmlForPage(page, boolNavigate)
                .then(function(htmlData, textStatus, jqXHR) {
                    it.status = $.parseJSON(jqXHR.getResponseHeader("jasperreports-report-status"));
                    it.html = htmlData;

                    Processor.processReport(it);

                    it.eventManager.triggerEvent(it.events.REPORT_HTML_READY);

                    if (it.status.isComponentMetadataEmbedded) {
                        return $.parseJSON($(htmlData).find("#reportComponents").text());
                    } else {
                        return it.loader.getComponentsForPage(page);
                    }
                }).then(function(componentsObject) {
                    it.components = {};
                    return it.componentRegistrar.registerComponents(componentsObject, it);
                }).then(function() {
                    it.eventManager.triggerEvent('componentsRegistered');

                    if (it.status.pageTimestamp) {
                        it.statusChecker.checkPageModified(page, it.status.pageTimestamp).then(function() {
                            it.eventManager.triggerEvent(it.events.PAGE_MODIFIED);
                        });
                    }
                    it.currentpage = page;
                    return it;
                });
        },
        on: function(evtName, callback) {
            this.eventManager.subscribeToEvent({
                name: evtName,
                callback: callback,
                thisContext: this,
                keep: true
            });

            return this;
        }
    });

    return Report;
});