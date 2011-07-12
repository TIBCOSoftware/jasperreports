jQuery.fn.loadmask = function(options) {
	return this.each(function(){
		if('string' == typeof options) {
			var id = this.id + '_maskDiv';
			switch (options) {
			case 'hide':
				$('#'+id).hide();
				break;
			case 'remove':
				$('#'+id).remove();
				break;
			}
		} else {
			var settings = {
					bgimage : APPLICATION_CONTEXT_PATH + '/jasperreports/images/loadmask.png',
					opacity: 0.3,
					loadinggif: APPLICATION_CONTEXT_PATH + '/jasperreports/images/loading4.gif'
			}
			
			if (options) {
				$.extend(settings, options);
			}
			var id = this.id + '_maskDiv';
			
			// check if the element exists
			if ($('#'+id).size() == 0) {
				$(this).parent().append("<div id='" + id + "'></div>")
			}
	
			$('#'+id).show().css({
				position : 				'absolute',
				backgroundImage : 		"url('" + settings.bgimage + "')",
				opacity : 				settings.opacity,
				width : 				$(this).css('width'),
				height : 				$(this).css('height'),
				top : 					$(this).position().top,
				left : 					$(this).position().left,
				'border-top-width' : 	$(this).css('borderTopWidth'),
				'border-top-style' : 	$(this).css('borderTopStyle'),
				borderBottomWidth : 	$(this).css('borderBottomWidth'),
				borderBottomStyle : 	$(this).css('borderBottomStyle'),
				borderLeftWidth : 		$(this).css('borderLeftWidth'),
				borderLeftStyle : 		$(this).css('borderLeftStyle'),
				borderRightWidth : 		$(this).css('borderRightWidth'),
				borderRightStyle : 		$(this).css('borderRightStyle'),
				'z-index' : 			999999,
				cursor:					'wait'
			});
		}
		
	});
}


function isEmpty(element) {
	if (element == null || element == undefined || element == '') {
		return true;
	}
	return false;
}

function appendScriptElementToDOM(scriptname, scripturi) {
//	console.log('appendScriptElementToDOM scriptName: ' + scriptname + '; window[scriptname] :' + isEmpty(window[scriptname]));
	if (isEmpty(window[scriptname])) {
		window[scriptname] = scripturi;
		$('head').append("<script type='text/javascript' src='" + APPLICATION_CONTEXT_PATH + scripturi + "'></script>");
	}
}

function appendCssElementToDOM(cssname, cssuri) {
	if (isEmpty(window[cssname])) {
		window[cssname] = cssuri;
		$('head').append("<link type='text/css' rel='stylesheet' href='" + APPLICATION_CONTEXT_PATH + cssuri + "'></link>");
	}
}

function getUrlParameter(url, paramName) {
	if(!isEmpty(url)) {
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
}

// @Object
function RegularExecutionContext(requestUrl, requestParams) {
	var _requestUrl = requestUrl;
	var _requestParams = requestParams;
	
	return {
		setOnLoadCompleteHandler : function(onLoadCompleteHandler, onLoadCompleteHandlerParams) {
		},
		
		run : function() {
			window.location = extendUrl(_requestUrl, _requestParams);
		}
	}
}

// @Object
function AjaxExecutionContext(contextId, requestUrl, target, requestParams, elementToExtract) {
	var _contextId = contextId;
	var _requestUrl = requestUrl;
	var _target = target;
	var _requestParams = requestParams;
	var _onLoadCompleteHandler;
	var _onLoadCompleteHandlerParams;
	var _elementToExtract = elementToExtract;
	
	return {
		getContextId : function () {
			return _contextId;
		},
		
		setOnLoadCompleteHandler : function(onLoadCompleteHandler, onLoadCompleteHandlerParams) {
			_onLoadCompleteHandler = onLoadCompleteHandler;
			_onLoadCompleteHandlerParams = onLoadCompleteHandlerParams;
		},
		
		run : function() {
			var parent = $(_target).closest('div.executionContext');
			
			if (parent.size() == 0) {
				parent = $(_target).closest('div.jiveContext');
			}
			parent.loadmask();
			
			// FIXME: must know if this is an ajax request, to prevent some resources from reloading
			if (_requestParams != null) {
				if ('object' == typeof _requestParams) {
					_requestParams['isajax'] = true;
					
				} else if('string' == typeof _requestParams) {
					_requestParams += '&isajax=true';
				}
			} else if (_requestParams == null) {
				_requestParams = new Object();
				_requestParams['isajax'] = true;
			}
			
			$(_target).load(_requestUrl + (_elementToExtract!=null ? ' ' +_elementToExtract : ''), _requestParams, function(response, status, xhr) {
				parent.loadmask('hide');
				
				if (status == 'success') {
					// execute function _onLoadCompleteHandler
					if (!isEmpty(_onLoadCompleteHandler) && !isEmpty(window[_onLoadCompleteHandler])) {
						window[_onLoadCompleteHandler].call(window, _onLoadCompleteHandlerParams);
					} 
					
				} else if (status == 'error') {
				    alert('Error: ' + xhr.status + " " + xhr.statusText)
				    
				} else {
					alert('Unknown: ' + xhr.status + "; " + xhr.statusText);
				}
			});
		}
	}
}

function getExecutionContext(startPoint, requestedUrl, params) {
	var executionContextElement = $(startPoint).closest('div.executionContext');
	
	if (executionContextElement.size() == 0) {
		executionContextElement = $(startPoint).closest('div.jiveContext');
	}

	if (executionContextElement.size() > 0) {
		var contextUrl = $(executionContextElement).attr('data-contexturl');
		var contextId = $(executionContextElement).attr('id');
		
		// update context url
		$(executionContextElement).attr('data-contexturl', requestedUrl + (isEmpty(params) ? '' : params));
		
		return AjaxExecutionContext(
					contextId, 
					requestedUrl, 
					$('div.result', executionContextElement), // target 
					params,
					null
				);
	}
	
	return RegularExecutionContext(requestedUrl, params); 
}

function getToolbarExecutionContext(startPoint, requestedUrl, params) {
	var executionContextElement = $(startPoint).closest('div.mainReportDiv');
	
	if (executionContextElement && executionContextElement.size() > 0) {
		return AjaxExecutionContext(
				null, 
				requestedUrl, 
				$('div.result', executionContextElement).filter(':first'), // target 
				params,
				'div.result'
		);
	}
}

function getContextElement(startPoint) {
	var executionContextElement = $(startPoint).closest('div.executionContext');
	if (executionContextElement && executionContextElement.size() > 0) {
		return executionContextElement;
	} 
	return null;
}

function extendUrl(url, parameters) {
	var result = url;
	
	if (parameters != null) {
		if (url.indexOf('?') != -1) {
			result = url + '&' + $.param(parameters);
		} else {
			result = url + '?' + $.param(parameters); 
		}
	}
	
	return result;
}

function initToolbar(toolbarId) {
	var toolbar = $('#' + toolbarId);
	
	if (toolbar.attr('data-initialized') == null) {
		toolbar.attr('data-initialized', 'true');
		
		toolbar.css(
				{
					opacity: 0.8
				}
		)

		toolbar.draggable();
		
		$('.pageFirst', toolbar).bind('click', function() {
			var parent = $(this).parent();
			var currentHref = parent.attr('data-url');
			var currentPage = parent.attr('data-currentpage');
			if (currentPage > 0) {
				var ctx = getToolbarExecutionContext(this, currentHref, 'page=0');
				if (ctx) {
					ctx.run();
					updateCurrentPageForToolbar(parent, 0);
				}		
			}
		});

		$('.pagePrevious', toolbar).bind('click', function() {
			var parent = $(this).parent();
			var currentHref = parent.attr('data-url');
			var currentPage = parent.attr('data-currentpage');
			if (currentPage > 0) {
				var ctx = getToolbarExecutionContext(this, currentHref, 'page=' + (currentPage-1));
				if (ctx) {
					ctx.run();
					updateCurrentPageForToolbar(parent, currentPage-1);
				}		
			}
		});
		
		$('.pageNext', toolbar).bind('click', function() {
			var parent = $(this).parent();
			var currentHref = parent.attr('data-url');
			var currentPage = parent.attr('data-currentpage');
			var totalPages = parent.attr('data-totalpages');
			if (currentPage < (totalPages -1)) {
				var ctx = getToolbarExecutionContext(this, currentHref, 'page=' + (currentPage+1));
				if (ctx) {
					ctx.run();
					updateCurrentPageForToolbar(parent, currentPage+1);
				}		
			}
		});
		
		$('.pageLast', toolbar).bind('click', function() {
			var parent = $(this).parent();
			var currentHref = parent.attr('data-url');
			var totalPages = parent.attr('data-totalpages');
			var currentPage = parent.attr('data-currentpage');
			if (currentPage < (totalPages -1)) {
				var ctx = getToolbarExecutionContext(this, currentHref, 'page=' + (totalPages-1));
				if (ctx) {
					ctx.run();
					updateCurrentPageForToolbar(parent, totalPages-1);
				}		
			}
		});
		
	}
}

function updateCurrentPageForToolbar(jQueryToolbar, newCurrentPage) {
	jQueryToolbar.attr('data-currentpage', newCurrentPage);
}