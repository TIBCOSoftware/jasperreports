define(["require", "jasperreports-loader", "jasperreports-status-checker", "jasperreports-component-registrar", "jquery"], function(require, Loader, StatusChecker, ComponentRegistrar, $) {
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
        this.components = {};


        // utils
        this.loader = new Loader({reporturi: this.config.reporturi, async: this.config.async});
        this.statusChecker = new StatusChecker(this.loader);
        this.componentRegistrar = new ComponentRegistrar(this.loader);
	};

	Report.prototype = {
        init: function() {
            return this.refreshPage(this.currentpage);
        },

        refreshPage: function(page) {
            var it = this;

            return it.loader.getHtmlForPage(page).then(function(htmlData, textStatus, jqXHR) {
                    it.status = $.parseJSON(jqXHR.getResponseHeader("jasperreports-report-status"));
                    it.html = htmlData;
                    return it.loader.getComponentsForPage(page);
                }).then(function(componentsObject, textStatus, jqXHR) {
                    return it.componentRegistrar.registerComponents(componentsObject, it);
                }).then(function() {
                    if (it.status.pageTimestamp) {
                        it.statusChecker.checkPageModified(page, it.status.pageTimestamp).then(function() {
                            console.info("page done");
                        });
                    } else {
                        console.info("page done");
                    }
                    return it.html;
                });
        },

        gotoPage: function(page) {
            this.statusChecker.cancelCheckPageModified();
            return this.refreshPage(page);
        },
        undo: function() {
            return this.loader.runAction({action: {actionName: "undo"}});
        },
        redo: function() {
            return this.loader.runAction({action: {actionName: "redo"}});
        },
        undoAll: function() {
            return this.loader.runAction({action: {actionName: "undoAll"}});
        }
	};
	
	return Report;
});
