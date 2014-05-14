define(["require", "jquery"], function(require, $) {
	var ComponentRegistrar = function(loader) {

        this.loader = loader;
	};

	ComponentRegistrar.prototype = {
        registerComponents: function(componentsObject, reportInstance) {
            var it = this,
                DFDs = [],
                componentsReady = new $.Deferred(),
                registeredComponents = {},
                container = reportInstance.components;

            $.each(componentsObject, function(key, compMeta) {
                if (compMeta.parentId) {
                    if(registeredComponents[compMeta.parentId]) {
                        registeredComponents[compMeta.parentId].then(function(component) {
                            component.registerPart(compMeta);
                        });
                    } else {
                        console.error("Could not find promise for component with id: " + compMeta.parentId);
                    }
                } else {
					if (compMeta.module) {
						var DFD = new $.Deferred();
						registeredComponents[compMeta.id] = DFD;
						DFDs.push(DFD);

						require([compMeta.module], function(Component) {
							var component = new Component(compMeta);
							component.parent = reportInstance;
							component.loader = it.loader;
							container[compMeta.type] = container[compMeta.type] || [];
							container[compMeta.type].push(component);
							/*
								Resolve deferred when component has loaded its own dependencies, i.e. jive.highcharts
							 */
							if(component.rdy) {
								component.rdy.then(function() {
									DFD.resolve(component);
								});
							} else {
								DFD.resolve(component);
							}
						});
					} else {	// if component does not require a handling module, use a plain object instead
						var component = {config: compMeta};
						container[compMeta.type] = container[compMeta.type] || [];
						container[compMeta.type].push(component);
					}
                }
            });

            $.when.apply($, DFDs).then(function() {
                componentsReady.resolve();
            });

            return componentsReady;
        }
	};
	
	return ComponentRegistrar;
});
