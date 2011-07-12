(function() {
	window.JiveUtils = {
		viewer: function(objOptions) {
			var settings = {
					width: 300,
					height: 400,
					containerid: 'jivecontainer',
					reporturl: null,
					toolbar: true
			};
			
			var action = {
				loadScript: function(scriptName, scriptUri) {
					// prevent the script tag from being created more than once
					if (!window[scriptName]) {
						window[scriptName] = scriptUri;
						var element = document.createElement('script');
						element.setAttribute('type', 'text/javascript');
						element.setAttribute('src', scriptUri);
						var head = document.getElementsByTagName('head');
						head[0].appendChild(element);
					}
				},
				
				run : function() {
					if (typeof jQuery === 'function') {
	//					console.log('jQuery is a function');
						jQuery.noConflict();
						
						// Now that we have jQuery context, proceed
						if (objOptions) {
							jQuery.extend(settings, objOptions);
						}
						
						if (!isEmpty(settings.reporturl) && !isEmpty(settings.containerid)) {
							var parent = jQuery('#' + settings.containerid); 
							parent.addClass('jiveContext');
							parent.attr("data-contexturl", settings.reporturl);
							parent.css(
									{
										width: settings.width,
										height: settings.height
									}
							);
							
							parent.append("<div class='result' style='width:100%; height:100%; overflow:auto;'></div>"); 
							
							var params = new Object();
							params['toolbar'] = settings.toolbar;
							jQuery('div.result', parent).load(settings.reporturl, params, function(response, status, xhr) {
								if (status == 'error') {
									alert('Error: ' + xhr.status + " " + xhr.statusText);
									
								}
							});
						}
					} else {
	//					console.log('jQuery has not loaded yet');
						setTimeout(this.run, 200);
					}
				},
				loadJQuery: function() {
	//				console.log('loading jQuery');
					if (typeof jQuery !== 'function') {
	//					console.log('jQuery is not defined...loading script');
						this.loadScript('jqueryscript', 'resources/js/jquery-1.4.4.min.js');
						setTimeout(this.run, 200);
					} else {
						this.run();
					}
				}
			};
			
			action.loadJQuery();
		}
	};
	
	window.isEmpty=function (element) {
		if (element === null || element === undefined || element === '') {
			return true;
		}
		return false;
	};
}());