define(["jasperreports-loader"], function(jrLoader) {
	var Report = function(config) {
		this.config = config;
	};
	
	Report.prototype = {
		goToPage: function(config) {
			jrLoader.ajaxLoad({
				url: this.config.url,
				options: {
					"jr.page": config.requestedPage,
					"jr.ctxid": this.config["jr.ctxid"]
				}
			});
		},
		refreshPage: function() {
			
		},
		undo: function() {
			
		},
		redo: function() {
			
		},
		undoAll: function() {
			
		}	
	};
	
	return Report;
});
