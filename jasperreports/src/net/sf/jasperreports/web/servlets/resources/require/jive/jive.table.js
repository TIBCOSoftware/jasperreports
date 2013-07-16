define(["jive.column", "jquery"], function(Column, $) {
	var Table = function(o) {

        this.config = {
            id: null,

            /**
             * {"1":{"index":"1","label":"Name","uuid":"ace5fd47-03c8-4d26-b2c0-354ca60560e0","visible":false,"interactive":true},..}
             */
            allColumnsData: null
        };
        $.extend(this.config, o);

        this.parent = null;
        this.column = {};
        this.loader = null;
	};

	Table.prototype = {
        registerPart: function(partConfig) {
            var column = new Column(partConfig);
            column.parent = this;
            column.loader = this.loader;
            this.column[partConfig.columnIndex] = column;
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
