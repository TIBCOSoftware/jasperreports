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

define(["jquery"], function($){
    var api = {
        sort: {},
        move: {},
        format: {},
        filter: {},
        hide: {},
        unhide: {},
        resize: {}
    };

    var Column = function(config) {
        this.config = config;
        this.parent = null;
        this.loader = null;

        this.events = {
            ACTION_PERFORMED: "action",
            BEFORE_ACTION_PERFORMED: "beforeAction"
        };

        this.api = api;
    };

    Column.prototype = {
        sort: function(parms) {
            var it = this,
                payload = {
                    action: this.config.headerToolbar['sort' + parms.order + 'Btn'].sortData
                };
            payload.action.sortData.tableUuid = it.config.parentId;
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "sort",
                    data: jsonData
                });
                return it;
            });
        },
        move: function(parms) {
            var it = this,
                payload = {
                    action: {
                        actionName: 'move',
                        moveColumnData: {
                            tableUuid: it.config.parentId,
                            columnToMoveIndex: it.config.columnIndex,
                            columnToMoveNewIndex: parms.index
                        }
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "move",
                    data: jsonData
                });

                return it;
            });
        },
        format: function(parms) {
            var it = this,
                payload = {
                    action: parms
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "format",
                    data: jsonData
                });

                return it;
            });
        },
        filter: function(parms) {
            var it = this,
                filterParms = $.extend({}, it.config.filtering.filterData, parms),
                payload = {
                    action: {
                        actionName: 'filter',
                        filterData: filterParms
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "filter",
                    data: jsonData
                });

                return it;
            });
        },
        hide: function() {
            var it = this,
                payload = {
                    action: {
                        actionName: 'hideUnhideColumns',
                        columnData: {
                            tableUuid: it.config.parentId,
                            hide: true,
                            columnIndexes: [this.config.columnIndex]
                        }
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "hide",
                    data: jsonData
                });

                return it;
            });
        },
        unhide: function(columnIds) {
            var it = this,
                payload = {
                    action: {
                        actionName: 'hideUnhideColumns',
                        columnData: {
                            tableUuid: it.config.parentId,
                            hide: false,
                            columnIndexes: columnIds ? columnIds : [this.config.columnIndex]
                        }
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "unhide",
                    data: jsonData
                });

                return it;
            });
        },
        resize: function(parms) {
            var it = this,
                payload = {
                    action: {
                        actionName: 'resize',
                        resizeColumnData: {
                            tableUuid: it.config.parentId,
                            columnIndex: this.config.columnIndex,
                            direction: "right",
                            width: parms.width
                        }
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "resize",
                    data: jsonData
                });

                return it;
            });
        },

        // internal functions
        _notify: function(evt) {
            // bubble the event
            this.parent._notify(evt);
        }
    }

    return Column;
});
