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
		configureImage: function (parentKey, parentProps, parentOptions) {
			var width, height, originX, originY, anchorX, anchorY, pp = parentProps, pk = parentKey;
			
			width = pp[pk + '.width'] ? parseInt(pp[pk + '.width']) : null;
			height = pp[pk + '.height'] ? parseInt(pp[pk + '.height']) : null;
			
			originX = pp[pk + '.origin.x'] ? parseInt(pp[pk + '.origin.x']) : 0;
			originY = pp[pk + '.origin.y'] ? parseInt(pp[pk + '.origin.y']) : 0;

			anchorX = pp[pk + '.anchor.x'] ? parseInt(pp[pk + '.anchor.x']) : 0;
			anchorY = pp[pk + '.anchor.y'] ? parseInt(pp[pk + '.anchor.y']) : 0;
			
			parentOptions[pk] = {
				url: pp[pk + '.url'],
				size: width && height ? new google.maps.Size(width, height) : null,
				origin: new google.maps.Point(originX,originY),
				anchor: new google.maps.Point(anchorX,anchorY)
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
				    	this.configureImage('icon', markerProps, markerOptions);
				    }
				    if(markerProps['shadow.url'] && markerProps['shadow.url'].length > 0) {
				    	this.configureImage('shadow', markerProps, markerOptions);
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
				    if(markerOptions['url']) {
						google.maps.event.addListener(marker, 'click', function() {
							switch(markerOptions['target']) {
								case '_self': 
									window.self.location.href = markerOptions['url'];
									break;
								case '_parent': 
									window.parent.location.href = markerOptions['url'];
									break;
								case '_top': 
									window.top.location.href = markerOptions['url'];
									break;
								default:
									if(frames[markerOptions['target']]) {
										frames[markerOptions['target']].location.href = markerOptions['url'];
									} else {
										window.location.href = markerOptions['url'];
									}
							}
						});	
					}				        
				}
			}
		}
	};
} (this));

