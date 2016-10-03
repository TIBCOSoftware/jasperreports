(function () {
	"use strict";
	
	var	server = require('webserver').create(),
		system = require('system'),
		args,
		mapArguments,
		listenAddress,
		confirmMessage,
		listening,
		call,
		requestStart;

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
	
	function Call(request, response) {
		this.request = request;
		this.response = response;
	}
	
	Call.prototype.parseRequest = function() {
		this.requestArgs = JSON.parse(this.request.postRaw || this.request.post);
	}
	
	Call.prototype.sendResponse = function(data) {
		this.response.statusCode = 200;
		this.response.write(data);
		this.response.close();
	}
	
	Call.prototype.sendError = function(error) {
		var msg = "Error";
		if (error) {
			msg += ": " + error;
		}
		this.response.statusCode = 500;
		this.response.setHeader('Content-Type', 'text/plain');
		this.response.write(msg);
		this.response.close();
	}

	args = mapArguments();
	listenAddress = args.listenAddress;
	confirmMessage = args.confirmMessage;
	
	if (!listenAddress) {
		console.log('Usage: phantomjs process.js listen_address');
		phantom.exit(1);
	} else {
		console.log('starting server on ' + listenAddress);
		listening = server.listen(listenAddress, function(request, response) {
			console.log("got request");
			call = new Call(request, response);
			requestStart = Date.now();
			
			var	requestArgs,
				handler;
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
				console.log('got error ' + e);
				
				call.sendError(e);
			}
		});
		
		if (listening) {
			console.log('server listening');
			console.log(confirmMessage);
		} else {
			console.log('failed to listen on ' + listenAddress);
			phantom.exit(1);
		}
	}
}());