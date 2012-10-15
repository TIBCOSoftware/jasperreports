/**
 * Defines 'map' module in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports.map !== 'undefined') {
		return;
	}
	global.jasperreports.map = {
		data: {},
		initGoogleMaps: function() {
			if (!global.google) {
				jasperreports.global.loadScript('_googleApi', 'http://maps.google.com/maps/api/js?sensor=false&callback=jasperreports.map.init');
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
		showMap: function(canvasId, latitude, longitude, zoom, mapType) {
			var gg = google.maps,
				myOptions = {
					zoom: zoom,
					center: new gg.LatLng(latitude, longitude), 
					mapTypeId: gg.MapTypeId[mapType]
				},
				map = new gg.Map(document.getElementById(canvasId), myOptions);
		},
	};
} (this));