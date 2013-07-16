define(["require", "jasperreports-loader", "jasperreports-status-checker", "jasperreports-component-registrar", "jasperreports-event-manager", "jquery"], function(require, Loader, StatusChecker, ComponentRegistrar, EventManager, $) {
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

	Report.prototype = {
        init: function() {
            return this.refreshPage(this.currentpage);
        },
        refreshPage: function(page) {
            var it = this;

            return it.loader.getHtmlForPage(page)
                .then(function(htmlData, textStatus, jqXHR) {
                    it.status = $.parseJSON(jqXHR.getResponseHeader("jasperreports-report-status"));
                    it.html = htmlData;
                    return it.loader.getComponentsForPage(page);
                }).then(function(componentsObject, textStatus, jqXHR) {
                    it.components = {};
                    return it.componentRegistrar.registerComponents(componentsObject, it);
                }).then(function() {
                    if (it.status.pageTimestamp) {
                        it.statusChecker.checkPageModified(page, it.status.pageTimestamp).then(function() {
                            it.eventManager.triggerEvent(it.events.PAGE_MODIFIED);
                        });
                    }
                    it.eventManager.triggerEvent(it.events.REPORT_HTML_READY);
                    it.currentpage = page;
                    return it;
                });
        },
        gotoPage: function(page) {
            this.statusChecker.cancelCheckPageModified();
            return this.refreshPage(page);
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
        },

        on: function(evtName, callback) {
            this.eventManager.subscribeToEvent({
                name: evtName,
                callback: callback,
                thisContext: this,
                keep: true
            });

            return this;
        },


        // internal functions
        /**
         *
         * @param evt The event object: {name, type, data}
         * @private
         */
        _notify: function(evt) {
            console.log("report notified of event: " + evt.name + "; type: " + evt.type);
            this.eventManager.triggerEvent(evt.name);
        }
	};
	
	return Report;
});
