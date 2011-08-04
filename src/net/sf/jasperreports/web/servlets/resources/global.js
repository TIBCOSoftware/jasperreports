// remove $ varible from global context
jQuery.noConflict();

/**
 * Define 'global' module  in JasperReports namespace
 */
(function(global) {
	if (typeof global.JasperReports !== 'undefined') {
		return;
	}
	
	var jr = {
			modules: {
				global: {
					scripts: {},
					APPLICATION_CONTEXT_PATH: '' 
				}
			},
		},
		jg = jr.modules.global;
	
	/** 
	 * Dynamically loads a js script 
	 */
	jg.loadScript = function(scriptName, scriptUrl, callbackFn) {
		var gotCallback = callbackFn || false;
		
		// prevent the script tag from being created more than once 
		if (!jg.scripts[scriptName]) {
			var scriptElement = document.createElement('script');
			
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
			} else { // for Others - this is not supposed to work on Safari 2
				scriptElement.onload = function(){
					if (gotCallback) {
						callbackFn();
					}
				};
			}
			
			scriptElement.src = scriptUrl;
			document.getElementsByTagName('head')[0].appendChild(scriptElement);
			jg.scripts[scriptName] = scriptUrl;
		} else if (gotCallback) {
			callbackFn();
		}
	};
	
	jg.appendScriptElementToDOM = function (scriptname, scripturi, callbackFn, isAbsoluteUrl) {
		if (!isAbsoluteUrl) {
			scripturi = jg.APPLICATION_CONTEXT_PATH + scripturi;
		}
		jg.loadScript(scriptname, scripturi, callbackFn);
	};
	
	jg.isEmpty = function(element) {
		if (element == null || element == undefined || element == '') {
			return true;
		}
		return false;
	};
		
	jg.getUrlParameter = function (url, paramName) {
		if(!jg.isEmpty(url)) {
			var keyValArray = url.slice(url.indexOf("?") + 1).split("&");
			var keyVal;
			for (var i=0; i< keyValArray.length; i++) {
				keyVal = keyValArray[i].split("=");
				if (paramName == keyVal[0]) {
					return keyVal[1];
				}
			}
		}
		return null;
	};

	// @Object
	jg.RegularExecutionContext = function (requestUrl, requestParams) {
		// enforce new
		if (!(this instanceof jg.RegularExecutionContext)) {
			return new jg.RegularExecutionContext(requestUrl, requestParams);
		}
		this.requestUrl = requestUrl;
		this.requestParams = requestParams;
	};

	jg.RegularExecutionContext.prototype.run = function() {
		global.location = jg.extendUrl(this.requestUrl, this.requestParams);
	};

	// @Object
	jg.AjaxExecutionContext = function(contextId, requestUrl, target, requestParams, elementToExtract) {
		// enforce new
		if (!(this instanceof jg.AjaxExecutionContext)) {
			return new jg.AjaxExecutionContext(contextId, requestUrl, target, requestParams, elementToExtract);
		}
		this.contextId = contextId;
		this.requestUrl = requestUrl;
		this.target = target;
		this.requestParams = requestParams;
		this.elementToExtract = elementToExtract;
	};
	
	jg.AjaxExecutionContext.prototype = {
		getContextId : function() {
			return this.contextId;
		},
		
		run : function() {
			var parent = jQuery(this.target).closest('div.executionContext');
			
			if (parent.size() == 0) {
				parent = jQuery(this.target).closest('div.jiveContext');
			}
			parent.loadmask();
			
			// FIXME: must know if this is an ajax request, to prevent some resources from reloading
			if (this.requestParams != null) {
				if ('object' == typeof this.requestParams) {
					this.requestParams['isajax'] = true;
					
				} else if('string' == typeof this.requestParams) {
					this.requestParams += '&isajax=true';
				}
			} else if (_requestParams == null) {
				this.requestParams = {
					isajax: true
				};
			}
			
			jQuery(this.target).load(this.requestUrl + (this.elementToExtract!=null ? ' ' +this.elementToExtract : ''), this.requestParams, function(response, status, xhr) {
				parent.loadmask('hide');
				
				if (status == 'success') {
					// add callback here
				} else if (status == 'error') {
				    alert('Error: ' + xhr.status + " " + xhr.statusText)
				    
				} else {
					alert('Unknown: ' + xhr.status + "; " + xhr.statusText);
				}
			});
		}
	};

	jg.getExecutionContext = function(startPoint, requestedUrl, params) {
		var executionContextElement = jQuery(startPoint).closest('div.executionContext');
		
		if (executionContextElement.size() == 0) {
			executionContextElement = jQuery(startPoint).closest('div.jiveContext');
		}

		if (executionContextElement.size() > 0) {
			var contextUrl = executionContextElement.attr('data-contexturl'),
				contextId = executionContextElement.attr('id');
			
			// update context url
			executionContextElement.attr('data-contexturl', requestedUrl + (jg.isEmpty(params) ? '' : params));
			
			return new jg.AjaxExecutionContext(
				contextId, 
				requestedUrl, 
				jQuery('div.result', executionContextElement), // target 
				params,
				null
			);
		}
		
		return new jg.RegularExecutionContext(requestedUrl, params); 
	};
	

	jg.getToolbarExecutionContext = function(startPoint, requestedUrl, params) {
		var executionContextElement = jQuery(startPoint).closest('div.mainReportDiv');
		
		if (executionContextElement && executionContextElement.size() > 0) {
			return new jg.AjaxExecutionContext(
				null, 
				requestedUrl, 
				jQuery('div.result', executionContextElement).filter(':first'), // target 
				params,
				'div.result'
			);
		}
	};

	jg.getContextElement = function(startPoint) {
		var executionContextElement = jQuery(startPoint).closest('div.executionContext');
		if (executionContextElement && executionContextElement.size() > 0) {
			return executionContextElement;
		} 
		return null;
	};

	jg.extendUrl = function(url, parameters) {
		var result = url;
		
		if (parameters != null) {
			if (url.indexOf('?') != -1) {
				result = url + '&' + jQuery.param(parameters);
			} else {
				result = url + '?' + jQuery.param(parameters); 
			}
		}
		
		return result;
	};
	
	jg.initToolbar = function(toolbarId) {
		var toolbar = jQuery('#' + toolbarId);
		
		if (toolbar.attr('data-initialized') == null) {
			toolbar.attr('data-initialized', 'true');
			
			toolbar.css({ opacity: 0.8 });

			toolbar.draggable();
			
			toolbar.bind('click', function(event) {
				var parent = jQuery(this),
					target = jQuery(event.target),
					currentHref = parent.attr('data-url'),
					currentPage = parent.attr('data-currentpage'),
					totalPages = parent.attr('data-totalpages'),
					requestedPage,
					performAction = false,
					pageParam= 'jr.page=',
					ctx;
				
				if (target.is('.pageFirst')) {
					if (currentPage > 0) {
						performAction = true;
						requestedPage = 0;
					}
				} else if (target.is('.pagePrevious')) {
					if (currentPage > 0) {
						performAction = true;
						requestedPage = currentPage - 1;
					}
				} else if (target.is('.pageNext')) {
					if (currentPage < (totalPages -1)) {
						performAction = true;
						requestedPage = currentPage+1;
					}
				} else if (target.is('.pageLast')) {
					if (currentPage < (totalPages -1)) {
						performAction = true;
						requestedPage = totalPages -1;
					}
				}
				
				if (performAction) {
					jg.getToolbarExecutionContext(parent, currentHref, pageParam + requestedPage).run();
					jg.updateCurrentPageForToolbar(parent, requestedPage);
				}
			});
		}
	};

	jg.updateCurrentPageForToolbar = function(jQueryToolbar, newCurrentPage) {
		jQueryToolbar.attr('data-currentpage', newCurrentPage);
	};
	
	global.JasperReports = jr;
	
} (this));

/**
 * A jQuery plugin that displays an overlapping image for a specified element 
 * (based on element's id)
 */
jQuery.fn.loadmask = function(options) {
	return this.each(function(){
		var id = this.id + '_maskDiv',
			jQid = '#' + id;
		if('string' == typeof options) {
			switch (options) {
			case 'hide':
				jQuery(jQid).hide();
				break;
			case 'remove':
				jQuery(jQid).remove();
				break;
			}
		} else {
			var gm = JasperReports.modules.global,
				settings = {
				bgimage : gm.APPLICATION_CONTEXT_PATH + '/jasperreports/images/loadmask.png',
				loadinggif: gm.APPLICATION_CONTEXT_PATH + '/jasperreports/images/loading4.gif',
				opacity: 0.3
			};
			
			if (options) {
				jQuery.extend(settings, options);
			}

			// if the mask element does not exist, create it
			if (jQuery(jQid).size() == 0) {
				jQuery(this).parent().append("<div id='" + id + "'></div>")
			}
	
			jQuery(jQid).show().css({
				position : 				'absolute',
				backgroundImage : 		"url('" + settings.bgimage + "')",
				opacity : 				settings.opacity,
				width : 				jQuery(this).css('width'),
				height : 				jQuery(this).css('height'),
				top : 					jQuery(this).position().top,
				left : 					jQuery(this).position().left,
				'border-top-width' : 	jQuery(this).css('borderTopWidth'),
				'border-top-style' : 	jQuery(this).css('borderTopStyle'),
				borderBottomWidth : 	jQuery(this).css('borderBottomWidth'),
				borderBottomStyle : 	jQuery(this).css('borderBottomStyle'),
				borderLeftWidth : 		jQuery(this).css('borderLeftWidth'),
				borderLeftStyle : 		jQuery(this).css('borderLeftStyle'),
				borderRightWidth : 		jQuery(this).css('borderRightWidth'),
				borderRightStyle : 		jQuery(this).css('borderRightStyle'),
				'z-index' : 			999999,
				cursor:					'wait'
			});
		}
		
	});
};
