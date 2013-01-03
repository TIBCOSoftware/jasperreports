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
				    for (j in markerProps) {
						if (markerProps.hasOwnProperty(j) && !markerOptions.hasOwnProperty(j)) {
								markerOptions[j] = markerProps[j];
						}
					}
				    var marker = new gg.Marker(markerOptions);
				}
			}
		},
	};
} (this));