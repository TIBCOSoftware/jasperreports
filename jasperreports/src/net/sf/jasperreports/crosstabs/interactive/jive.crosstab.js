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
	var Crosstab = function(o) {
		this.config = {};
		$.extend(this.config, o);
		
        this.parent = null;
        this.loader = null;

        this.events = {
            ACTION_PERFORMED: "action",
            BEFORE_ACTION_PERFORMED: "beforeAction"
        };
	};

	Crosstab.prototype = {
		getId: function() {
			return this.config.crosstabId;
		},
		getFragmentId: function() {
			return this.config.fragmentId;
		},
		sortRowGroup: function(groupIndex, order) {
			var it = this;
			var payload = {
					action: {"actionName":"sortXTabRowGroup",
						"sortData":{
							"crosstabId":this.getId(),
							"order":order,
							"groupIndex":groupIndex}}};
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
			return this.loader.runAction(payload).then(function(jsonData) {
				it._notify({
					name: it.events.ACTION_PERFORMED,
					type: "sortXTabRowGroup",
					data: jsonData});

				return it;
			});
		},
		isDataColumnSortable: function(columnIndex) {
			var dataColumn = this.config.dataColumns[columnIndex - this.config.startColumnIndex];
			return typeof(dataColumn.sortMeasureIndex) == "number";
		},
		getColumnOrder: function(columnIndex) {
			return this.config.dataColumns[columnIndex - this.config.startColumnIndex].order;
		},
		sortByDataColumn: function(columnIndex, order) {
			var it = this;
			var dataColumn = this.config.dataColumns[columnIndex - this.config.startColumnIndex];
			var payload = {
					action: {"actionName":"sortXTabByColumn",
						"sortData":{
							"crosstabId":this.getId(),
							"order":order,
							"measureIndex": dataColumn.sortMeasureIndex,
							"columnValues": dataColumn.columnValues}}};
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
			return this.loader.runAction(payload).then(function(jsonData) {
				it._notify({
					name: it.events.ACTION_PERFORMED,
					type: "sortXTabByColumn",
					data: jsonData});

				return it;
			});
		},

        // internal functions
        /**
         * @param evt {object} The event object: {type, name, data}
         */
        _notify: function(evt) {
            // bubble the event
            this.parent._notify(evt);
        }
	};

	return Crosstab;
});