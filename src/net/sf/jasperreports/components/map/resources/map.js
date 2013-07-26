/**
 * Defines 'map' module in jasperreports namespace
 */

(function(global) {
	if (typeof global.jasperreports.map !== 'undefined') {
		return;
	}
	var infowindow;
	global.jasperreports.map = {
		data: {},
		initGoogleMaps: function(rp) {
			if (!global.google) {
				if(rp) {
					jasperreports.global.loadScript('_googleApi', 'http://maps.google.com/maps/api/js?sensor=false&callback=jasperreports.map.init'+rp);
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
		configureImage: function (pk, pp, po) {
			var width, height, originX, originY, anchorX, anchorY;
			
			width = pp[pk + '.width'] ? parseInt(pp[pk + '.width']) : null;
			height = pp[pk + '.height'] ? parseInt(pp[pk + '.height']) : null;
			
			originX = pp[pk + '.origin.x'] ? parseInt(pp[pk + '.origin.x']) : 0;
			originY = pp[pk + '.origin.y'] ? parseInt(pp[pk + '.origin.y']) : 0;

			anchorX = pp[pk + '.anchor.x'] ? parseInt(pp[pk + '.anchor.x']) : 0;
			anchorY = pp[pk + '.anchor.y'] ? parseInt(pp[pk + '.anchor.y']) : 0;
			
			po[pk] = {
				url: pp[pk + '.url'],
				size: width && height ? new google.maps.Size(width, height) : null,
				origin: new google.maps.Point(originX,originY),
				anchor: new google.maps.Point(anchorX,anchorY)
			};
		},
		createInfo: function (pp) {
			if(pp['infowindow.content'] && pp['infowindow.content'].length > 0) {
				var gg= google.maps,
			    po = {
					content: pp['infowindow.content']	
				};
				if (pp['infowindow.pixelOffset']) po['pixelOffset'] = pp['infowindow.pixelOffset'];
				if (pp['infowindow.latitude'] && pp['infowindow.longitude']) po['position'] = new gg.LatLng(pp['infowindow.latitude'], pp['infowindow.longitude']);
				if (pp['infowindow.maxWidth']) po['maxWidth'] = pp['infowindow.maxWidth'];
				return new gg.InfoWindow(po);	
			}
			return null;
		},
		showMap: function(canvasId, latitude, longitude, zoom, mapType, markers) {
			var gg = google.maps,
				jm = global.jasperreports.map,
				myOptions = {
					zoom: zoom,
					center: new gg.LatLng(latitude, longitude), 
					mapTypeId: gg.MapTypeId[mapType],
					autocloseinfo: true
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
				    if(markerProps['icon.url'] && markerProps['icon.url'].length > 0) jm.configureImage('icon', markerProps, markerOptions);
				    else if (markerProps['icon'] && markerProps['icon'].length > 0) markerOptions['icon'] = markerProps['icon'];
				    else if (markerProps['color'] && markerProps['color'].length > 0) markerOptions['icon'] = 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=' + ((markerProps['label'] && markerProps['label'].length > 0) ? markerProps['label'] : '%E2%80%A2')+ '%7C' + markerProps['color'];
				    else if(markerProps['label'] && markerProps['label'].length > 0) markerOptions['icon'] = 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=' + markerProps['label'] + '%7CFE7569';
				    if(markerProps['shadow.url'] && markerProps['shadow.url'].length > 0) jm.configureImage('shadow', markerProps, markerOptions);
				    for (j in markerProps) {
						if (j.indexOf(".") < 0 && markerProps.hasOwnProperty(j) && !markerOptions.hasOwnProperty(j)) markerOptions[j] = markerProps[j];
					}
				    var marker = new google.maps.Marker(markerOptions);
				    marker['info'] = jm.createInfo(markerProps);
					gg.event.addListener(marker, 'click', function() {
						if(map.autocloseinfo && infowindow) infowindow.close();
						if(this['info']) {
							infowindow = this['info'];
							this['info'].open(map, this);
						} else if (this['url'] && this['url'].length > 0) window.open(this['url'], this['target']);
					});	
				}
			}
		}
	};
} (this));
