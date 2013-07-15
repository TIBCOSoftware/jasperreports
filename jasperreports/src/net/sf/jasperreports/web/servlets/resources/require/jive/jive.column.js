define(["validator", "jquery"], function(Validator, $){
    var Column = function(config) {
        this.config = config;
        this.parent = null;
        this.loader = null;
    };

    Column.prototype = {
        sort: function(parms) {
            Validator.check(parms.order).notNull();
            var payload = {
                action: this.config.headerToolbar['sort' + parms.order + 'Btn'].sortData
            };
            return this.loader.runAction(payload);
        },
        move: function(parms) {
            var payload = {
                action: [{
                    actionName: 'move',
                    moveColumnData: {
                        tableUuid: this.config.tableId,
                        columnToMoveIndex: this.config.columnIndex,
                        columnToMoveNewIndex: parms.index
                    }
                }]
            };
            return this.loader.runAction(payload);
        },
        format: function(parms) {
            return this.loader.runAction(payload);
        },
        filter: function(parms) {
            Validator.check(parms.fieldValueStart).notNull();
            Validator.check(parms.filterTypeOperator).notNull();
            if(parms.filterTypeOperator == 'BETWEEN') Validator.check(parms.fieldValueEnd).notNull();

            $.extend(this.config.filtering.filterData, parms);

            var payload = {
                action: [{
                    actionName: 'filter',
                    filterData: this.config.filtering.filterData
                }]
            };

            return this.loader.runAction(payload);
        },
        hide: function() {
            var payload = {
                action: {
                    actionName: 'hideUnhideColumns',
                    columnData: {
                        hide: true,
                        columnIndexes: [this.config.columnIndex],
                        tableUuid: this.config.tableId
                    }
                }
            };
            return this.loader.runAction(payload);
        },
        unhide: function() {
            var payload = {
                action: {
                    actionName: 'hideUnhideColumns',
                    columnData: {
                        hide: false,
                        columnIndexes: [this.config.columnIndex],
                        tableUuid: this.config.tableId
                    }
                }
            };
            return this.loader.runAction(payload);
        },
        resize: function(parms) {
            var payload = {
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
            return this.loader.runAction(payload);
        }
    }

    return Column;
});