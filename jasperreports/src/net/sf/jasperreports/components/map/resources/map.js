/**
 * Defines 'map' module in JasperReports namespace
 */
(function(global) {
	if (typeof global.JasperReports.modules.map !== 'undefined') {
		return;
	}
	var jm = {
		callback: {}	// defined in template
	};
	jm.init = function() {
		var googleScriptUri = 'http://maps.google.com/maps/api/js?sensor=false&callback=JasperReports.modules.map.callback';
		
		if (!global.google) {
			JasperReports.modules.global.loadScript('_googleApi', googleScriptUri);
		}
	};
	global.JasperReports.modules.map = jm;
} (this));