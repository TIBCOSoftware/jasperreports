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
