/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

define(["jasperreports-loader",
    "jasperreports-status-checker",
    "jasperreports-event-manager",
    "jquery"], function (Loader, StatusChecker, EventManager, $) {
	var Report = function(o) {

		this.config = {
            reporturi: null,
            async: true,
            page: 0,
            updateInterval: 1000,
            container: null
		};
		$.extend(this.config, o);

        // report state members
        this.currentpage = this.config.page;
		this.html = null;
        this.status = null;

        // events
        this.eventManager = new EventManager();
        this.events = {
            PAGE_MODIFIED: "pageModified",
            REPORT_HTML_READY: "reportHtmlReady",
            REPORT_FINISHED: "reportFinished"
        };

        // utils
        this.loader = new Loader({reporturi: this.config.reporturi, async: this.config.async, eventManager: this.eventManager});
        this.statusChecker = new StatusChecker(this.loader, this.config.updateInterval);

        o.stopOnFinishOnly && (this.loader.config.stopOnFinishOnly = true);
	};

	Report.prototype = {
        init: function(inputParameters) {
            return this.refreshPage(this.currentpage, null, inputParameters);
        },
        refreshPage: function(page, boolNavigate, inputParameters) {
            var it = this;
            it.currentpage = page;

            return it.loader.getHtmlForPage(it.currentpage, boolNavigate, inputParameters)
                .then(function(htmlData, textStatus, jqXHR) {
                    it.status = $.parseJSON(jqXHR.getResponseHeader("jasperreports-report-status"));
                    it.html = htmlData;

                    // an anchor may change the current page index, so try to set it from status
                    if (it.status && it.status.pageIndex != null) {
                        it.currentpage = it.status.pageIndex;
                    }
                    it.config.postProcess && it.config.postProcess.call(it);
                    it.eventManager.triggerEvent(it.events.REPORT_HTML_READY);
                }).then(function() {
                    if ((it.status.pageTimestamp || !it.status.totalPages) && it.status.reportStatus != 'canceled') {
                        it.statusChecker.checkPageModified(it.currentpage, it.status.pageTimestamp).then(function(statusResult) {
                            it.status.originalStatus = statusResult;
                            if(statusResult.status == 'finished') {
                                it.status.totalPages = statusResult.lastPageIndex + 1;
                                it.status.partialPageCount = statusResult.lastPartialPageIndex + 1;
                                it.status.reportStatus = statusResult.status;

                                // final pages may not contain all the report components (e.g. bookmarks, parts), so try to load them
                                if (!it.status.pageFinal) {
                                    it.eventManager.triggerEvent(it.events.REPORT_FINISHED);
                                }
                            } else {
                                if (statusResult.pageModified) {
                                    it.eventManager.triggerEvent(it.events.PAGE_MODIFIED);
                                }
                            }
                        });
                    }
                    return it;
                });
        },
        gotoPage: function(page) {
            this.statusChecker.cancelCheckPageModified();
            return this.refreshPage(page, true);
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
            this.config.debug && console.log("report notified of event: " + evt.name + "; type: " + evt.type);
            this.eventManager.triggerEvent(evt.name, evt);
        }
	};
	
	return Report;
});
