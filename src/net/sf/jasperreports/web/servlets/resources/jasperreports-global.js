// remove $ varible from global context
jQuery.noConflict();

/**
 * Define 'global' module  in jasperreports namespace
 */
(function(global) {
	if (typeof global.jasperreports !== 'undefined') {
		return;
	}
	
	var jr = {
				global: {
					debugEnabled: false,
					scripts: {},
					styles: {},
					APPLICATION_CONTEXT_PATH: '',
					JQUERY: {
						CORE: '/jquery/js/jquery-1.7.1.min.js',
						UI: '/jquery/js/jquery-ui-1.8.18.custom.min.js'
					},
					dialogsContainerSelector: 'div.jrPage:first',		// 'jrPage' hardcoded in JRXHtmlExporter.java
					reportContainerSelector: 'body'
				}
		},
		jg = jr.global;
	
	jg.isDebugEnabled = function () {
		return jg.debugEnabled;
	};
	
	jg.enableDebug = function (boolEnable) {
		jg.debugEnabled = boolEnable;
	};
	
	jg.createImage = function (imageSrc) {
		var result = new Image();
		result.src = imageSrc;
		return result;
	};
	
	/**
	 * Enhances dest with properties of source
	 * 
	 * @param dest
	 * @param src
	 */
	jg.merge = function (dest, arrSource) {
		var i, j, ln, source;
		dest = dest || {};
		
		for (i = 0, ln = arrSource.length; i < ln; i++) {
			source = arrSource[i];
			for (j in source) {
				if (source.hasOwnProperty(j)) {
						dest[j] = source[j];
				}
			}
		}
		return dest;
	};
	
	jg.extractCallbackFunction = function (callbackFn, context) {
		var result = callbackFn;
		if (typeof callbackFn === 'string') {
			var i, ln, tokens = callbackFn.split('.');
			result = context || global;
			for (i = 0, ln = tokens.length; i < ln; i++) {
				if (result[tokens[i]]) {
					result = result[tokens[i]];
				} else {
					throw new Error('Invalid callback function: ' + callbackFn + '; token: ' + tokens[i]);
				}
			}
		}
		return result;
	};

	jg.extractContext = function (context) {
		var result = context || null;
		if (typeof context === 'string') {
			var i, ln, tokens = context.split('.');
			result = global;
			for (i = 0, ln = tokens.length; i < ln; i++) {
				if (result[tokens[i]]) {
					result = result[tokens[i]];
				} else {
					throw new Error('Invalid context: ' + context + '; token: ' + tokens[i]);
				}
			}
		}
		return result;
	};
	
	/** 
	 * Dynamically loads a js script 
	 */
	jg.loadScript = function (scriptName, scriptUrl, callbackFn, arrCallbackArgs) {
		var gotCallback = callbackFn || false,
			callbackArgs = arrCallbackArgs || [];
		
		// prevent the script tag from being created more than once 
		if (!jg.scripts[scriptName]) {
			var scriptElement = document.createElement('script');
			
			scriptElement.setAttribute('type', 'text/javascript');
			
			if (scriptElement.readyState){ // for IE
				scriptElement.onreadystatechange = function (){
					if (scriptElement.readyState === 'loaded' || scriptElement.readyState === 'complete'){
						scriptElement.onreadystatechange = null;
						if (gotCallback) {
							jg.extractCallbackFunction(callbackFn).apply(null, callbackArgs);
						}
					}
				};
			} else { // for Others - this is not supposed to work on Safari 2
				scriptElement.onload = function (){
					if (gotCallback) {
						jg.extractCallbackFunction(callbackFn).apply(null, callbackArgs);
					}
				};
			}
			
			scriptElement.src = scriptUrl;
			document.getElementsByTagName('head')[0].appendChild(scriptElement);
			jg.scripts[scriptName] = scriptUrl;
		} else if (gotCallback) {
			try {
				jg.extractCallbackFunction(callbackFn).apply(null, callbackArgs);
			} catch(ex) {} // swallow this
		}
	};
	
	/**
	 * Evaluate script string
	 */
	jg.executeScript = function (scriptString, callbackFn) {
		var gotCallback = callbackFn || false;
		if (scriptString) {
			global.eval(scriptString);
			if (gotCallback) {
				callbackFn();
			}
		}
	}	
	
	/** 
	 * Dynamically loads a css file 
	 */
	jg.loadStyle = function (styleName, styleUrl) {
		// prevent the style tag from being created more than once 
		if (!jg.styles[styleName]) {
			var styleElement = document.createElement('link');
			styleElement.setAttribute('rel', 'stylesheet');
			styleElement.setAttribute('type', 'text/css');
			styleElement.setAttribute('href', styleUrl);
			document.getElementsByTagName('head')[0].appendChild(styleElement);
			jg.styles[styleName] = styleUrl;
		}
	};
	
	/**
	 * NOT USED YET: Dynamically loads jQuery core and ui and then uses jQuery stuff
	 */
	jg.init = function () {
		if (typeof jQuery === 'undefined') {
			jg.appendScriptElementToDOM('_jqueryCoreScript', jg.JQUERY.CORE, function () {
				jg.appendScriptElementToDOM('_jqueryUiScript', jg.JQUERY.UI, function () {
					jg.doJqueryStuff();
				});
			});
		}
	};
	
	jg.appendScriptElementToDOM = function (scriptname, scripturi, callbackFn, arrCallbackArgs, isAbsoluteUrl) {
		if (!isAbsoluteUrl) {
			scripturi = jg.APPLICATION_CONTEXT_PATH + scripturi;
		}
		jg.loadScript(scriptname, scripturi, callbackFn, arrCallbackArgs);
	};
	
	jg.appendStyleElementToDOM = function (styleName, styleUrl, isAbsoluteUrl) {
		if (!isAbsoluteUrl) {
			styleUrl = jg.APPLICATION_CONTEXT_PATH + styleUrl;
		}
		jg.loadStyle(styleName, styleUrl);
	};
	
	jg.isEmpty = function(element) {
		if (element == null || element == undefined || element == '') {
			return true;
		}
		return false;
	};
	
	jg.getUrlBase = function (url) {
		if (url.indexOf("?") != -1) {
			return url.substring(0, url.indexOf("?"));
		} else {
			return url;
		}
	};
	
	jg.getUrlParameters = function (url) {
		var result = {};
		if(!jg.isEmpty(url)) {
			var keyValArray = url.slice(url.indexOf("?") + 1).split("&"),
				keyVal,
				i, ln = keyValArray.length;
			
			for (i=0; i< ln; i++) {
				keyVal = keyValArray[i].split("=");
				result[keyVal[0]] = keyVal[1];
			}
		}
		return result;
	};
		
	jg.getUrlParameter = function (url, paramName) {
		if(!jg.isEmpty(url)) {
			var keyValArray = url.slice(url.indexOf("?") + 1).split("&"),
				i, keyVal;
			for (i=0; i< keyValArray.length; i++) {
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
	
	/**
	 * Isolates jQuery dependent functions
	 */
	jg.doJqueryStuff = function () {
		jg.ajaxLoad = function (url, elementToAppendTo, elementToExtract, requestParams, callback, arrCallbackArgs, thisContext, loadMaskTarget) {
			jQuery.ajax(url, 
					{
						type: 'POST',
						
						data: requestParams,
						
						success: function(data, textStatus, jqXHR) {
							var response = jQuery(jqXHR.responseText);
							if (elementToAppendTo) {
								var toExtract = response;

								if (elementToExtract) {
									toExtract = jQuery(elementToExtract, response);
									if (toExtract.size() != 1) { 
										// error on server side
										if (jQuery('#jrInteractiveError', response).size() === 1){
											jg.showError(jqXHR.responseText, loadMaskTarget, 'Jasper Interactive Error', 510, 160);
										} else {
											jg.showError(jqXHR.responseText, loadMaskTarget, 'Error', 1100, 500);
										}
										return;
									}
								}
								
								elementToAppendTo.html(toExtract.html());
								
								// execute script tags from response after appending to DOM because the script may rely on new DOM elements
//								response.filter('script').each(function(idx, elem) {
//									var scriptObj = jQuery(elem);
//									if (!scriptObj.attr('src')) { // only scripts that don't load files are run
//										var scriptString = scriptObj.html();
//										if (scriptString) {
//											global.eval(scriptString);
//										}
//									}
//								});
								
								// execute script tags synchronously
								var scriptTags = response.filter('script'),
						    		sz  = scriptTags.size(),
						    		idx = 0,
						    		scriptName;
	
						    	function iterate() {
						    		if (idx >= sz) {
						    			return;
						    		}
						    		var scriptObj = jQuery(scriptTags.get(idx));
						    		if (scriptObj.attr('src')) {
						    			idx++;
						    			scriptName = scriptObj.attr('data-custname') || scriptObj.attr('src');
						    			jg.loadScript(scriptName, scriptObj.attr('src'), iterate);
						    		} else {
						    			idx++;
						    			jg.executeScript(scriptObj.html(), iterate);
						    		}
						    	}
	
						    	iterate();
								
							}
							
							if (callback) {
								if (!arrCallbackArgs) {
									arrCallbackArgs = [];
								}
								arrCallbackArgs.push(response);
								jg.extractCallbackFunction(callback).apply(jg.extractContext(thisContext), arrCallbackArgs);
							}
							
							loadMaskTarget.loadmask && loadMaskTarget.loadmask('hide');
						},
						
						error: function(jqXHR, textStatus, errorThrown) {
							jg.showError(jqXHR.responseText, loadMaskTarget, 'Error', 1100, 500);
						}
					}
			);
		};
		
		jg.ajaxJson = function (url, requestParams, callback, arrCallbackArgs, loadMaskTarget) {
			jQuery.ajax(url, 
					{
						type: 'POST',
						data: requestParams,
						dataType: 'json',
						success: function(data, textStatus, jqXHR) {
							if (callback) {
								if (!arrCallbackArgs) {
									arrCallbackArgs = [];
								}
								arrCallbackArgs.push(data);
								callback.apply(null, arrCallbackArgs);
							}
							
							loadMaskTarget.loadmask && loadMaskTarget.loadmask('hide');
						},
						error: function(jqXHR, textStatus, errorThrown) {
							jg.showError(jqXHR.responseText, loadMaskTarget, 'Error', 1100, 500);
						}
					}
			);
		};
		
		
		// @Object
		jg.AjaxExecutionContext = function(contextId, requestUrl, target, requestParams, elementToExtract, callback, arrCallbackArgs, thisContext, isJSONResponse) {
			// enforce new
			if (!(this instanceof jg.AjaxExecutionContext)) {
				return new jg.AjaxExecutionContext(contextId, requestUrl, target, requestParams, elementToExtract, callback, arrCallbackArgs, isJSONResponse);
			}
			this.contextId = contextId;
			this.requestUrl = requestUrl;
			this.target = target;
			this.requestParams = requestParams;
			this.elementToExtract = elementToExtract;
			this.callback = callback;
			this.arrCallbackArgs = arrCallbackArgs;
			this.thisContext = thisContext;
			this.isJSONResponse = isJSONResponse;
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
				
				if (parent.size() == 0) {
					parent = jQuery(this.target);
				}
				
				parent.loadmask && parent.loadmask();
				
				if (this.isJSONResponse) {
					jg.ajaxJson(this.requestUrl, this.requestParams, this.callback, this.arrCallbackArgs, parent);
				} else {
					jg.ajaxLoad(this.requestUrl, this.target, this.elementToExtract, this.requestParams, this.callback, this.arrCallbackArgs, this.thisContext, parent);
				}
			}
		};
		
		jg.logObject = function (objName, obj) {
			var objString = [],
				i=0,
				prop;
			for (prop in obj) {
				if (obj.hasOwnProperty(prop)) {
					objString[i] = prop + " = " + obj[prop];
					i++;
				}
			}
			console.log("object: " + objName + " = {" + objString.join(', ') + "}");
		};
		
		jg.debug = function (strTag, strMessage) {
			if (typeof global.console !== 'undefined') {
				console.log(strTag + ': ' + strMessage);
			}
		};
		
		/**
		 * Obtains an execution context based on parameters
		 * 
		 * @param startPoint: a jQuery or DOM object
		 * @param requestedUrl: a string url
		 * @param params: an object with additional parameters that must be appended to requestedUrl 
		 */
		jg.getExecutionContext = function(startPoint, requestedUrl, params) {
			if (!requestedUrl) {
				return null;
			}
			var executionContextElement = jQuery(startPoint).closest('div.executionContext');
			
			if (executionContextElement.size() == 0) {
				executionContextElement = jQuery(startPoint).closest('div.jiveContext');
			}

			if (executionContextElement.size() > 0) {
				var contextUrl = executionContextElement.attr('data-contexturl'),
					contextId = executionContextElement.attr('id'),
					reqUrlBase = jg.getUrlBase(requestedUrl),
					reqParams = jg.getUrlParameters(decodeURIComponent(requestedUrl)),
					contextReqParams = jg.getUrlParameters(decodeURIComponent(contextUrl)),
					newParams = jg.merge({}, [reqParams, params]);
				
				// update context url
				executionContextElement.attr('data-contexturl', jg.extendUrl(reqUrlBase, newParams));
				
				return new jg.AjaxExecutionContext(
					contextId, 
					reqUrlBase, 
					jQuery('div.result', executionContextElement), // target 
					newParams,
					null,
					null,
					null
				);
			}
			
			return new jg.RegularExecutionContext(requestedUrl, params); 
		};
		

		jg.getToolbarExecutionContext = function(startPoint, requestedUrl, params, callback, arrCallbackArgs, thisContext, isJSONResponse) {
//			var executionContextElement = jQuery(startPoint).closest('div.mainReportDiv');
			var executionContextElement = jQuery('div.mainReportDiv:first'); // this could be unpredictable when using embeded reports 
			
			if (executionContextElement && executionContextElement.size() > 0) {
				return new jg.AjaxExecutionContext(
					null,															// contextId
					requestedUrl,													// requestUrl
					jQuery('div.result', executionContextElement).filter(':first'), // target 
					params,															// requestParams
					'div.result',													// elementToExtract
					callback,														// callback
					arrCallbackArgs,												// arrCallbackArgs
					thisContext,													// 'this' context
					isJSONResponse													// isJSONResponse
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
		
		jg.escapeString = function(str) {
			return str.replace(/\\/g,'\\\\').replace(/\"/g, '\\\"');
		};
		
		jg.toJsonString = function(object, boolEscapeStrings) {
			var o2s = Object.prototype.toString.call(object),
				result = '',
				bEscapeStrings = boolEscapeStrings || false,
				i, ln, property;

			switch (o2s) {
				case '[object Array]':
					result += "[";
					for (i = 0, ln = object.length; i < ln; i++) {
						result += jg.toJsonString(object[i], bEscapeStrings);
						if (i < ln -1) {
							result += ",";
						}
					}
					result += "]";
					break;

				case '[object Object]':
					result += "{";
					for (property in object) {
						if (object.hasOwnProperty(property) && object[property] != null) {
							result += "\"" + property + "\":" + jg.toJsonString(object[property], bEscapeStrings) + ",";
						}
					}
					if (result.indexOf(",") != -1) {
						result = result.substring(0, result.lastIndexOf(","));
					}
					result += "}";
					break;

				case '[object String]':
					result += "\"" + (bEscapeStrings ? jg.escapeString(object) : object) + "\"";
					break;

				case '[object Null]':
					result = null;
					break;

				default:
					result += object;
					break;
			}
			return result;
		};

		jg.showError = function(responseText, loadMaskTarget, title, width, height) {
			var errDialogId = 'errDialog',
				errDialog = jQuery('#' + errDialogId);
			if (errDialog.size != 1) {
				errDialog = jQuery("<div id='" + errDialogId + "'></div>");
				jQuery('body').append(errDialog);
			}

			errDialog.html(responseText);
			errDialog.dialog({
				title: title,
				width: width,
				height: height,
				close: function(event, ui) {
					loadMaskTarget.loadmask && loadMaskTarget.loadmask('hide');
				}
			});

			// hide all popup divs
//			jQuery('.popupdiv').hide();
		};

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
					var gm = jasperreports.global,
					settings = {
							bgimage : gm.APPLICATION_CONTEXT_PATH + '/images/loadmask.png',
							loadinggif: gm.APPLICATION_CONTEXT_PATH + '/images/loading4.gif',
							opacity: 0.3
					};
					
					if (options) {
						jQuery.extend(settings, options);
					}
					
					// if the mask element does not exist, create it
					if (jQuery(jQid).size() == 0) {
						jQuery(this).parent().append("<div id='" + id + "'></div>");
					}
					
					jQuery(jQid).show().css({
						position :				'absolute',
						backgroundImage :		"url('" + settings.bgimage + "')",
						opacity :				settings.opacity,
						width :					jQuery(this).css('width'),
						height :				jQuery(this).css('height'),
						top :					jQuery(this).position().top,
						left :					jQuery(this).position().left,
						'border-top-width' :	jQuery(this).css('borderTopWidth'),
						'border-top-style' :	jQuery(this).css('borderTopStyle'),
						borderBottomWidth :		jQuery(this).css('borderBottomWidth'),
						borderBottomStyle :		jQuery(this).css('borderBottomStyle'),
						borderLeftWidth :		jQuery(this).css('borderLeftWidth'),
						borderLeftStyle :		jQuery(this).css('borderLeftStyle'),
						borderRightWidth :		jQuery(this).css('borderRightWidth'),
						borderRightStyle :		jQuery(this).css('borderRightStyle'),
						'z-index' :				1000,
						cursor:					'wait'
					});
				}
				
			});
		};
	};
	
	jg.doJqueryStuff();

	global.jasperreports = jr;
	
	jasperreports.events = {	// FIXMEJIVE consider separating as module
		_events: {},
		Event: function (name) {
			if (!this instanceof jasperreports.events.Event) {
				return new jasperreports.events.Event(name);
			}
			this.status = 'default';
			this.name = name;
			this.subscribers = [];
		},
		registerEvent: function (evtName) {
			if (!this._events[evtName]) {
				this._events[evtName] = new jasperreports.events.Event(evtName);
			}
			return this._events[evtName];
		},
		registerEvents: function (evtNames) {
			var result = [];
			jQuery.each(this._getEventNames(evtNames), function (i, evtName) {
				result.push(jasperreports.events.registerEvent(evtName));
			});
			
			return result;
		},
		/**
		 * options = {name: string, callback: function/string, thisContext: object/string, keep: boolean}
		 */
		subscribeToEvent: function (options) {
			this.registerEvent(options.name).subscribe({
				callback: options.callback,
				args: options.args,
				ctx: options.thisContext,
				keep: options.keep
			});
		},
		/**
		 * options = {names: string, callback: function/string, thisContext: object/string, keep: boolean}
		 */
		subscribeToEvents: function (options) {
			jQuery.each(this.registerEvents(options.names), function (i, evt) {
				evt.subscribe({
					callback: options.callback,
					args: options.args,
					ctx: options.thisContext,
					keep: options.keep
				});
			});
		},
		triggerEvents: function (evtNames) {
			jQuery.each(this._getEventNames(evtNames), function (i, evtName) {
				jasperreports.events.triggerEvent(evtName);
			});
		},
		checkRegistered: function (evtName) {
			if (!this._events[evtName]) {
				throw new Error('Event not registered:' + evtName);
			}
		},
		registerTriggerReset: function (evtNames, response) {
			jQuery.each(this.registerEvents(evtNames), function (i, evt) {
				evt.trigger(response).reset();
			});
		},
		_getEventNames: function (evtNames) {
			var result = [], tokens, i, ln;
			if (evtNames && typeof(evtNames) === 'string') {
				tokens = evtNames.split(',');
				for (i = 0, ln = tokens.length; i < ln; i++) {
					result.push(tokens[i].replace(/^\s+|\s+$/g, ''));
				}
			}
			return result;
		}
	};
	
	jasperreports.events.Event.prototype = {
		getName: function() {
			return this.name;
		},
		getStatus: function() {
			return this.status;
		},
		hasFinished: function() {
			return this.status === 'finished';
		},
		subscribe: function(subscriber) {
			if (!this.hasFinished()) {
				this.subscribers.push(subscriber);
			} else {
				if (subscriber.keep) {
					this.subscribers.push(subscriber);
				}
				this.processSubscriber(subscriber);
			}
		},
		trigger: function(response) {
			var i, subscriber;
			for (i = 0; i < this.subscribers.length; i++) {
				subscriber = this.subscribers[i];
				if (subscriber.args) {
					subscriber.args.push(response);
				} else {
					subscriber.args = [response];
				}
				i = i - this.processSubscriber(subscriber);
			}
			this.status = 'finished';
			return this;
		},
		reset: function() {
			this.status = 'default';
		},
		processSubscriber: function(subscriber) {
			var jg = jasperreports.global, index, i, ln = this.subscribers.length;
			jg.extractCallbackFunction(subscriber.callback).apply(jg.extractContext(subscriber.ctx), subscriber.args);
			if (!subscriber.keep) {
				for (i = 0; i < ln; i++) {
					if (this.subscribers[i] === subscriber) {
						this.subscribers.splice(i,1);
						return 1;
					}
				}
			}
			return 0;
		}
	};

    global.jive = {
        active: false,
        started: false,
		actionBaseData: null,
	    actionBaseUrl: null,
        selectors: {},
        elements: {},
        interactive:{},
        i18n: {
        	keys: {},
        	get: function (key) {
        		if (this.keys.hasOwnProperty(key)) {
        			return this.keys[key];
        		} else {
        			return key;
        		}
        	}
        },
        ui: {
            scaleFactor: 1
        },
        selected: {
            ie: null,  // selected interactive element
            jo: null,  // selected jquery object tied to interactive element
            form: null // selected form defined by interactive element
        },
        viewerReady: false,
        runAction: function (options) {
        	var settings = {
        			actionData: null,
        			startPoint: null,
        			callback: null,
        			arrCallbackArgs: null,
        			thisContext: null,
        			defaultAction: false
        	};
        	
        	jQuery.extend(settings, options);
        	
        	settings.startPoint = settings.startPoint || this.selected.jo;
        	
            this.hide();

        	jasperreports.reportviewertoolbar.runReport({
        			selectedColumn: {
        				actionBaseData: jQuery.parseJSON(this.actionBaseData),
        				actionBaseUrl: this.actionBaseUrl,
        				toolbarId: settings.startPoint ? settings.startPoint.closest('.mainReportDiv').find('.toolbarDiv').attr('id') : null,
        				self: settings.startPoint
        			},
    				actionData: settings.actionData,
    				callback: settings.callback,
    				arrCallbackArgs: settings.arrCallbackArgs,
    				thisContext: settings.thisContext,
    				defaultAction: settings.defaultAction
        	});
    				
        },
        hide: function () {
        	// empty body; overwritten in jive.js
        }
    }
	
} (this));

