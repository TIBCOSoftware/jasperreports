define(['jquery/amd/jquery-1.7.1', 'jasperreports-loader', 'validator'], function($, Loader, Validator){
    var JRInteractiveColumn = function(config) {
        this.config = config;
    };

    JRInteractiveColumn.prototype = {
        sort: function(parms) {
            var payload = {
                'jr.run': true,
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify(this.config.headerToolbar['sort' + parms.order + 'Btn'].sortData)
            };

            return Loader.runAction(this.config.url, payload);
        },
        move: function(parms) {
            var payload = {
                'jr.run': true,
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify([{
                    actionName: 'move',
                    "moveColumnData": {
                        tableUuid: this.config.tableId,
                        columnToMoveIndex: this.config.columnIndex,
                        columnToMoveNewIndex: parms.index
                    }
                }])
            };

            return Loader.runAction(this.config.url, payload);
        },
        format: function(parms) {
            return loader.runAction(this.config.url, payload);
        },
        filter: function(parms) {
            Validator.check(parms.fieldValueStart).notNull();
            Validator.check(parms.filterTypeOperator).notNull();
            if(parms.filterTypeOperator == 'BETWEEN') Validator.check(parms.fieldValueEnd).notNull();

            $.extend(this.config.filtering.filterData, parms);

            var payload = {
                'jr.run': 'true',
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify([{
                    actionName: 'filter',
                    "filterData": this.config.filtering.filterData
                }])
            };

            return Loader.runAction(this.config.url, payload);
        },
        hide: function() {
            var payload = {
                'jr.run': 'true',
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify({
                    actionName: 'hideUnhideColumns',
                    columnData: {
                        hide: true,
                        columnIndexes: [this.config.columnIndex],
                        tableUuid: this.config.tableId
                    }
                })
            };
            return Loader.runAction(this.config.url, payload);
        },
        unhide: function() {
            var payload = {
                'jr.run': 'true',
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify({
                    actionName: 'hideUnhideColumns',
                    columnData: {
                        hide: false,
                        columnIndexes: [this.config.columnIndex],
                        tableUuid: this.config.tableId
                    }
                })
            };
            return Loader.runAction(this.config.url, payload);
        },
        resize: function(parms) {
            var payload = {
                'jr.run': 'true',
                'jr.ctxid': this.config.ctxid,
                'jr.action': JSON.stringify({
                    actionName: 'resize',
                    resizeColumnData: {
                        "tableUuid": this.config.tableId,
                        "columnIndex": this.config.columnIndex,
                        "direction": "right",
                        "width": parms.width
                    }
                })
            };
            return Loader.runAction(this.config.url, payload);
        }
    }

    return JRInteractiveColumn;
});