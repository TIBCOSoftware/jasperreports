/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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

(function () {
	"use strict";
	
	var	server = require('webserver').create(),
		system = require('system'),
		args,
		mapArguments,
		listenAddress,
		confirmMessage,
		idleTimeout,
		listening,
		idleExit,
		idleTimer = null,
		startIdle,
		endIdle;

	mapArguments = function () {
		var map = {},
			i;
		for (i = 0; i < system.args.length; i += 1) {
			if (system.args[i].charAt(0) === '-') {
				map[system.args[i].substr(1, i.length)] = system.args[i + 1];
			}
		}
		return map;
	};
	
	idleExit = function() {
		console.log("Idle timeout reached");
		phantom.exit();
	}
	
	startIdle = function() {
		if (idleTimeout > 0) {
			idleTimer = window.setTimeout(idleExit, idleTimeout);
		}
	}
	
	endIdle = function() {
		if (idleTimer != null) {
			window.clearTimeout(idleTimer);
			idleTimer = null;
		}
	}
	
	function Call(request, response) {
		this.request = request;
		this.response = response;
	}
	
	Call.prototype.parseRequest = function() {
		this.requestArgs = JSON.parse(this.request.postRaw || this.request.post);
	}
	
	Call.prototype.sendResponse = function(data) {
		try {
			this.response.statusCode = 200;
			this.response.write(data);
			this.response.close();
		} finally {
			startIdle();
		}
	}
	
	Call.prototype.sendError = function(error) {
		try {
			var msg = "Error";
			if (error) {
				msg += ": " + error;
			}
			this.response.statusCode = 500;
			this.response.setHeader('Content-Type', 'text/plain');
			this.response.write(msg);
			this.response.close();
		} finally {
			startIdle();
		}
	}

	args = mapArguments();
	listenAddress = args.listenAddress;
	confirmMessage = args.confirmMessage;
	idleTimeout = args.idleTimeout ? parseInt(args.idleTimeout) : 0;
	
	if (!listenAddress) {
		console.log('Usage: phantomjs process.js listen_address');
		phantom.exit(1);
	} else {
		console.log('starting server on ' + listenAddress);
		listening = server.listen(listenAddress, function(request, response) {
			var	call,
				handler;
			console.log("got request");
			endIdle();
			
			call = new Call(request, response);
			try {
				call.parseRequest();
				
				if (call.requestArgs.echo) {
					call.sendResponse(call.requestArgs.echo);
				} else {
					console.log("calling " + call.requestArgs.script);
					handler = require("./" + call.requestArgs.script);
					handler.perform(call);
				}
			} catch (e) {
				system.stderr.writeLine('Error processing request ' + e);
				call.sendError(e);
			}
		});
		
		if (listening) {
			console.log('server listening');
			console.log(confirmMessage);
			startIdle();
		} else {
			system.stderr.writeLine('Failed to listen on ' + listenAddress);
			phantom.exit(1);
		}
	}
}());