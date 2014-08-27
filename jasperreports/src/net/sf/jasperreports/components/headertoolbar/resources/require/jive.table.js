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

define(["jive.column", "jquery"], function(Column, $) {
    var genericProperties = null;

    var Table = function(o) {

        this.config = {
            id: null,

            /**
             * {"1":{"index":"1","label":"Name","uuid":"ace5fd47-03c8-4d26-b2c0-354ca60560e0","visible":false,"interactive":true},..}
             */
            allColumnsData: null
        };
        $.extend(this.config, o);

        if(o.genericProperties) {
            genericProperties = o.genericProperties;
        } else {
            this.config.genericProperties = genericProperties;
        }

        this.parent = null;
        this.columns = [];
        this.columnMap = {}
        this.loader = null;
    };

    Table.prototype = {
        registerPart: function(partConfig) {
            var column = new Column(partConfig);
            column.parent = this;
            column.loader = this.loader;
            this.columns[partConfig.columnIndex] = column;
            this.columnMap[partConfig.id] = column;
        },
        getId: function() {
            return this.config.id;
        },

        // internal functions
        /**
         *
         * @param evt {object} The event object: {type, name, data}
         */
        _notify: function(evt) {
            // bubble the event
            this.parent._notify(evt);
        }
    };

    return Table;
});