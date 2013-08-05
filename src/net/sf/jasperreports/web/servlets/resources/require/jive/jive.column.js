define(["validator", "jquery-1.10.1"], function(Validator, $){
    var Column = function(config) {
        this.config = config;
        this.parent = null;
        this.loader = null;

        this.events = {
            ACTION_PERFORMED: "action"
        };
    };

    Column.prototype = {
        sort: function(parms) {
            Validator.check(parms.order).notNull();
            var it = this,
                payload = {
                    action: this.config.headerToolbar['sort' + parms.order + 'Btn'].sortData
                };
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
                            tableUuid: this.config.tableId,
                            columnToMoveIndex: this.config.columnIndex,
                            columnToMoveNewIndex: parms.index
                        }
                    }
                };
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
            Validator.check(parms.fieldValueStart).notNull();
            Validator.check(parms.filterTypeOperator).notNull();
            if(parms.filterTypeOperator == 'BETWEEN') Validator.check(parms.fieldValueEnd).notNull();

            $.extend(this.config.filtering.filterData, parms);

            var it = this,
                payload = {
                    action: {
                        actionName: 'filter',
                        filterData: this.config.filtering.filterData
                    }
                };

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
                            hide: true,
                            columnIndexes: [this.config.columnIndex],
                            tableUuid: this.config.tableId
                        }
                    }
                };
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "hide",
                    data: jsonData
                });

                return it;
            });
        },
        unhide: function() {
            var it = this,
                payload = {
                    action: {
                        actionName: 'hideUnhideColumns',
                        columnData: {
                            hide: false,
                            columnIndexes: [this.config.columnIndex],
                            tableUuid: this.config.tableId
                        }
                    }
                };
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
                            tableUuid: this.config.tableId,
                            columnIndex: this.config.columnIndex,
                            direction: "right",
                            width: parms.width
                        }
                    }
                };
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