/**
 * Defines 'map' module in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports.map !== 'undefined') {
		return;
	}
	global.jasperreports.map = {
		data: {},
		initGoogleMaps: function(language) {
			if (!global.google) {
				if(language) {
					jasperreports.global.loadScript('_googleApi', 'http://maps.google.com/maps/api/js?sensor=false&callback=jasperreports.map.init&language='+language);
				} else {
					jasperreports.global.loadScript('_googleApi', 'http://maps.google.com/maps/api/js?sensor=false&callback=jasperreports.map.init');
				}
			}
		},
		init: function() {
			jasperreports.events.registerEvent('jasperreports.map.init').trigger();
		},
		addMapData: function(canvasId, latitude, longitude, zoom) {
			this.data[canvasId] = {
					lat: latitude,
					long: longitude,
					zoom: zoom
			};
		},
		showMap: function(canvasId, latitude, longitude, zoom, mapType, markers) {
			var gg = google.maps,
				myOptions = {
					zoom: zoom,
					center: new gg.LatLng(latitude, longitude), 
					mapTypeId: gg.MapTypeId[mapType]
				},
				map = new gg.Map(document.getElementById(canvasId), myOptions);
			if(markers){
				var j;
				for (var i = 0; i < markers.length; i++) {
				    var markerProps = markers[i];
				    var markerLatLng = new gg.LatLng(markerProps['latitude'], markerProps['longitude']);
				    var markerOptions = {
					        position: markerLatLng,
					        map: map
					    };
				    
				    if(markerProps['icon.url'] && markerProps['icon.url'].length > 0) {
				    	var width, height, originX, originY, anchorX, anchorY;
				    	var iconSize;
				    	var iconOrigin, iconAnchor;
				    	var iconUrl = markerProps['icon.url'];
				    	
				    	if(markerProps['icon.width']) {
				    		width = new Number(markerProps['icon.width']);
				    		height = 0;
				    	}
				    	if(markerProps['icon.height']) {
				    		height = new Number(markerProps['icon.height']);
				    		if(!width) {
				    			width = 0;
				    		}
				    	}
				    	if(markerProps['icon.origin.x']) {
				    		originX = new Number(markerProps['icon.origin.x']);
				    		originY = 0;
				    	}
				    	if(markerProps['icon.origin.y']) {
				    		originY = new Number(markerProps['icon.origin.y']);
				    		if(!originX) {
				    			originX = 0;
				    		}
				    	}
				    	if(markerProps['icon.anchor.x']) {
				    		anchorX = new Number(markerProps['icon.anchor.x']);
				    		anchorY = 0;
				    	}
				    	if(markerProps['icon.anchor.y']) {
				    		anchorY = new Number(markerProps['icon.anchor.y']);
				    		if(!anchorX) {
				    			anchorX = 0;
				    		}
				    	}
				    	if(width || height) {
				    		iconSize = new google.maps.Size(width,height);
				    	}
				    	if(originX || originY) {
				    		iconOrigin = new google.maps.Point(originX,originY);
				    	}
				    	if(anchorX || anchorY) {
				    		iconAnchor = new google.maps.Point(anchorX,anchorY);
				    	}
				    	var customIcon = {
				    			url: iconUrl,
				    			size: iconSize,
				    			origin: iconOrigin,
				    			anchor: iconAnchor
				    	};
				    	markerOptions['icon'] = customIcon;
				    }
				    
				    if(markerProps['shadow.url'] && markerProps['shadow.url'].length > 0) {
				    	var width, height, originX, originY, anchorX, anchorY;
				    	var shadowSize;
				    	var shadowOrigin, shadowAnchor;
				    	var shadowUrl = markerProps['shadow.url'];
				    	
				    	if(markerProps['shadow.width']) {
				    		width = new Number(markerProps['shadow.width']);
				    		height = 0;
				    	}
				    	if(markerProps['shadow.height']) {
				    		height = new Number(markerProps['shadow.height']);
				    		if(!width) {
				    			width = 0;
				    		}
				    	}
				    	if(markerProps['shadow.origin.x']) {
				    		originX = new Number(markerProps['shadow.origin.x']);
				    		originY = 0;
				    	}
				    	if(markerProps['shadow.origin.y']) {
				    		originY = new Number(markerProps['shadow.origin.y']);
				    		if(!originX) {
				    			originX = 0;
				    		}
				    	}
				    	if(markerProps['shadow.anchor.x']) {
				    		anchorX = new Number(markerProps['shadow.anchor.x']);
				    		anchorY = 0;
				    	}
				    	if(markerProps['shadow.anchor.y']) {
				    		anchorY = new Number(markerProps['shadow.anchor.y']);
				    		if(!anchorX) {
				    			anchorX = 0;
				    		}
				    	}
			    		if(width || height) {
			    			shadowSize = new google.maps.Size(width,height);
			    		}
			    		if(originX || originY) {
			    			shadowOrigin = new google.maps.Point(originX,originY);
			    		}
			    		if(anchorX || anchorY) {
			    			shadowAnchor = new google.maps.Point(anchorX,anchorY);
			    		}
			    		var customShadow = {
				    		url: shadowUrl,
				    		size: shadowSize,
				    		origin: shadowOrigin,
				    		anchor: shadowAnchor
				    	};
				    	markerOptions['shadow'] = customShadow;
				    }
				    
				    for (j in markerProps) {
						if (
							j.indexOf("icon.") < 0 
							&& j.indexOf("shadow.") < 0
							&& markerProps.hasOwnProperty(j) 
							&& !markerOptions.hasOwnProperty(j)
							) {
								markerOptions[j] = markerProps[j];
						}
					}
				    var marker = new gg.Marker(markerOptions);
				}
			}
		},
	};
} (this));