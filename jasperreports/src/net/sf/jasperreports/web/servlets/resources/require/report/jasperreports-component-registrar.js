/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

define(["require", "jquery"], function(require, $) {
	var ComponentRegistrar = function(loader) {

        this.loader = loader;
	};

	ComponentRegistrar.prototype = {
        registerComponents: function(componentsObject, reportInstance, container) {
            var it = this,
                DFDs = [],
                componentsReady = new $.Deferred(),
                registeredComponents = {};

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
