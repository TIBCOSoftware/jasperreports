/**
 * Defines 'map' module in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports.map !== 'undefined') {
		return;
	}
	var jm = {
		callback: {}	// defined in template
	};
	jm.init = function() {
		var googleScriptUri = 'http://maps.google.com/maps/api/js?sensor=false&callback=jasperreports.map.callback';
		
		if (!global.google) {
			jasperreports.global.loadScript('_googleApi', googleScriptUri);
		} else {
			jm.callback();
		}
	};
	global.jasperreports.map = jm;
} (this));