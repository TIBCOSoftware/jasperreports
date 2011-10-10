/**
 * Creates JasperReportsUtils namespace
 * Depends on jQuery library - is loaded dynamically if not present
 */
(function(global) {
	if (typeof global.JasperReportsUtils !== 'undefined') {
		return;
	}
	
	var jru = {
		scripts: {}
	};
	jru.viewer = function(objOptions) {
			
		var settings = {
				width: 300,
				height: 400,
				containerid: 'jivecontainer',
				reporturl: null,
				toolbar: true,
				scriptname: '_jqueryscript',
				jqueryurl: 'jquery/js/jquery-1.6.4.min.js'
		};

		if (objOptions) {
			merge(objOptions, settings);
		}
		
		loadScript(settings.scriptname, settings.jqueryurl, loadReport);
		
		/**
		 * Ajax loads the report and places it inside the element with id = settings.containerid
		 */
		function loadReport() {
			jQuery.noConflict();
			
			if (!isEmpty(settings.reporturl) && !isEmpty(settings.containerid)) {
				var parent = jQuery('#' + settings.containerid),
					params = {
						toolbar: settings.toolbar
					}; 
			
				parent.addClass('jiveContext');
				parent.attr("data-contexturl", settings.reporturl);
				parent.css(
						{
							width: settings.width,
							height: settings.height
						}
				);
				parent.append("<div class='result' style='width:100%; height:100%; overflow:auto;'></div>"); 
				
				jQuery('div.result', parent).load(settings.reporturl, params, function(response, status, xhr) {
					if (status == 'success') {
						try {
							JasperReports.modules.global.isFirstAjaxRequest = false;
						} catch (ex) {
							if (typeof console !== 'undefined') {
								console.log(ex.message);
							}
						}
						
					} else if (status == 'error') {
						alert('Error: ' + xhr.status + ' ' + xhr.statusText);
					}
				});
			}
		}
		
		/**
		 * Enhances dest with properties of source
		 * 
		 * @param dest
		 * @param src
		 */
		function merge(source, dest) {
			var i,
				toStringFn = Object.prototype.toStringFning,
				arrayType = '[object Array]';
			dest = dest || {};
			for (i in source) {
				if (source.hasOwnProperty(i)) {
					if (typeof source[i] === 'object') {
						dest[i] = (toStringFn.call(source[i]) === arrayType) ? [] : {};
						merge(source[i], dest[i]);
					} else {
						dest[i] = source[i];
					}
				}
			}
		}
		
		/**
		 * Checks for empty element
		 * 
		 * @param element
		 * @returns {Boolean}
		 */
		function isEmpty(element) {
			if (element === null || element === undefined || element === '') {
				return true;
			}
			return false;
		};
		
		/**
		 * Loads a js script file and then calls the callback function
		 * 
		 * @param scriptName
		 * @param scriptUrl
		 * @param callbackFn
		 */
		function loadScript(scriptName, scriptUrl, callbackFn) {
			// prevent the script tag from being created more than once 
			if (!jru.scripts[scriptName]) {
				var scriptElement = document.createElement('script'),
					gotCallback = callbackFn || false;
				scriptElement.setAttribute('type', 'text/javascript');
				
				if (scriptElement.readyState){ // for IE
					scriptElement.onreadystatechange = function(){
						if (scriptElement.readyState === 'loaded' || scriptElement.readyState === 'complete'){
							scriptElement.onreadystatechange = null;
							if (gotCallback) {
								callbackFn();
							}
						}
					};
				} else { // forOthers
					scriptElement.onload = function(){
						if (gotCallback) {
							callbackFn();
						}
					};
				}
				
				scriptElement.src = scriptUrl;
				document.getElementsByTagName('head')[0].appendChild(scriptElement);
				jru.scripts[scriptName] = scriptUrl;
			}
		}
	}
	
	global.JasperReportsUtils = jru;
}(this));
