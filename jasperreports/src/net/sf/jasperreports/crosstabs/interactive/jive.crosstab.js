define(["jquery"], function($) {
	var Crosstab = function(o) {
		this.config = {};
		$.extend(this.config, o);
		
        this.parent = null;
        this.loader = null;
	};

	Crosstab.prototype = {
		getId: function() {
			return this.config.id;
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
			return this.loader.runAction(payload).then(function(jsonData) {
				it._notify({
					name: "action",
					type: "sortXTabByColumn",
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
			return this.loader.runAction(payload).then(function(jsonData) {
				it._notify({
					name: "action",
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