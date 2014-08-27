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

define(["jquery"], function($) {
	var StatusChecker = function(loader, updateInterval) {
        this.loader = loader;
        // timers
        this.timeoutId = null;

        this.updateInterval = updateInterval;
	};

	StatusChecker.prototype = {

        checkPageModified: function(page, pageTimestamp) {
            var it = this,
                deferred = $.Deferred();

            it._timedCheckPageModified(false, page, pageTimestamp, deferred, null);

            return deferred.promise();
        },

        cancelCheckPageModified: function() {
            clearTimeout(this.timeoutId);
        },

        // internal functions
        _timedCheckPageModified: function(booleanDone, pageNo, pageTimestamp, deferredObject, statusResult) {
            var it = this;
            if (!booleanDone) {
            	it.timeoutId = setTimeout(function() {
        			it._getPageModifiedStatus(pageNo, pageTimestamp, deferredObject);
        		}, it.updateInterval);
            } else {
                deferredObject.resolve(statusResult);
            }
        },

        _getPageModifiedStatus: function(page, pageTimestamp, deferredObject) {
            var it = this;
            return it.loader.getStatusForPage(page, pageTimestamp)
                .then(function(jsonData, textStatus, jqHXR) {
                    var booleanDone;
                    if(it.loader.config.stopOnFinishOnly) {
                        booleanDone = (jsonData.result.status == "finished");
                    } else {
                        booleanDone = (jsonData.result.pageModified || jsonData.result.status == "finished");
                    }
                    !booleanDone && it.loader.setPageUpdateStatus && it.loader.setPageUpdateStatus(jsonData);
                    it._timedCheckPageModified(booleanDone, page, pageTimestamp, deferredObject, jsonData.result);
                });
        }
	};
	
	return StatusChecker;
});
