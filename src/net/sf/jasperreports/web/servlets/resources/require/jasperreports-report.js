define(["jasperreports-loader"], function(jrLoader) {
	var Report = function(config) {
		this.config = config;
        this.components = {};
	};
	
	Report.prototype = {
		goToPage: function(config) {
			return jrLoader.ajaxLoad({
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
			
		},
        register: function(component, DFD) {
            /*
                Add report properties to component object for reference. Used in ajax request.
             */
            component.config.url = this.config.url;
            component.config.ctxid = this.config['jr.ctxid'];

            /*
                Create component array.
             */
            this.components[component.config.type] = this.components[component.config.type] || [];
            this.components[component.config.type].push(component);

            /*
                Create component map.
             */
            var mapName = component.config.type + 'Map';
            this.components[mapName] = this.components[mapName] || {};
            this.components[mapName][component.config.id] = component;

            /*
                Deferred used to synchronize component registration which happens asynchronously due to require().
             */
            DFD.resolve();
        }
	};
	
	return Report;
});
