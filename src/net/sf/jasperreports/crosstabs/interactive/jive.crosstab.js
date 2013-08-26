define(["jquery-1.10.2"], function($) {
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
		sortByColumn: function(columnIndex, order) {
			var it = this;
			var payload = {
					action: {"actionName":"sortXTabByColumn",
						"sortData":{
							"crosstabId":this.getId(),
							"order":order,
							"columnValues": this.config.dataColumns[columnIndex - this.config.startColumnIndex].columnValues}}};
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