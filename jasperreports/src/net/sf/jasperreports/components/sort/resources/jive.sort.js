define(["jquery-1.10.2"], function($){
    var Sort = function(config) {
        this.config = config;
        this.parent = null;
        this.loader = null;

        this.events = {
            ACTION_PERFORMED: "action",
            BEFORE_ACTION_PERFORMED: "beforeAction"
        };
    };

    Sort.prototype = {
        sort: function(parms) {
            var it = this,
                payload = {
                    action: this.config.sortData
                };
            payload.action.sortData.tableUuid = it.config.datasetUuid;
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "sortica",
                    data: jsonData
                });
                return it;
            });
        },
        filter: function(parms) {
            var it = this,
                filterParms = $.extend({}, it.config.filterData, parms),
                payload = {
                    action: {
                        actionName: 'filterica',
                        filterData: filterParms
                    }
                };
            it._notify({name: it.events.BEFORE_ACTION_PERFORMED});
            return this.loader.runAction(payload).then(function(jsonData) {
                it._notify({
                    name: it.events.ACTION_PERFORMED,
                    type: "filterica",
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

    return Sort;
});
