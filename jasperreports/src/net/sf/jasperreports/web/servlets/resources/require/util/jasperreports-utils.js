/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

define(["jquery"], function(jQuery) {
	var jasperreports = {
			APPLICATION_CONTEXT_PATH: '',
			dialogsContainerSelector: 'div.jrPage:first',		// 'jrPage' hardcoded in JRXHtmlExporter.java
			reportContainerSelector: 'body'
		},
		jr = jasperreports;

	jr.isEmpty = function(element) {
		if (element == null || element == undefined || element == '') {
			return true;
		}
		return false;
	};
	
	jr.escapeString = function(str) {
		return str.replace(/\\/g,'\\\\').replace(/\"/g, '\\\"');
	};
	
	jr.toJsonString = function(object, boolEscapeStrings) {
		var o2s = Object.prototype.toString.call(object),
			result = '',
			bEscapeStrings = boolEscapeStrings || false,
			i, ln, property;

		switch (o2s) {
			case '[object Array]':
				result += "[";
				for (i = 0, ln = object.length; i < ln; i++) {
					result += jr.toJsonString(object[i], bEscapeStrings);
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
						result += "\"" + property + "\":" + jr.toJsonString(object[property], bEscapeStrings) + ",";
					}
				}
				if (result.indexOf(",") != -1) {
					result = result.substring(0, result.lastIndexOf(","));
				}
				result += "}";
				break;

			case '[object String]':
				result += "\"" + (bEscapeStrings ? jr.escapeString(object) : object) + "\"";
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
	
	jr.extractCallbackFunction = function (callbackFn, context) {
		var result = callbackFn;
		if (typeof callbackFn === 'string') {
			var i, ln, tokens = callbackFn.split('.');
			result = context || window;
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
	
	jr.extractContext = function (context) {
		var result = context || null;
		if (typeof context === 'string') {
			var i, ln, tokens = context.split('.');
			result = window;
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
				var settings = {
						bgimage : jr.APPLICATION_CONTEXT_PATH + '/images/loadmask.png',
						loadinggif: jr.APPLICATION_CONTEXT_PATH + '/images/loading4.gif',
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
	
	jasperreports.events = {
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
			var i, ln = this.subscribers.length;
			jr.extractCallbackFunction(subscriber.callback).apply(jr.extractContext(subscriber.ctx), subscriber.args || []);
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
	
	return jasperreports;
});
