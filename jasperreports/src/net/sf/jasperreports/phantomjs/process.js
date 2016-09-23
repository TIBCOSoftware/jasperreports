(function () {
	"use strict";
	
	var	server = require('webserver').create(),
		system = require('system'),
		args,
		mapArguments,
		listenAddress,
		confirmMessage,
		listening;

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

	args = mapArguments();
	listenAddress = args.listenAddress;
	confirmMessage = args.confirmMessage;
	
	if (!listenAddress) {
		console.log('Usage: phantomjs process.js listen_address');
		phantom.exit(1);
	} else {
		console.log('starting server on ' + listenAddress);
		listening = server.listen(listenAddress, function(request, response) {
			var	requestArgs,
				handler;
			try {
				console.log("got request");
				requestArgs = JSON.parse(request.postRaw || request.post);
				console.log("calling " + requestArgs.script);
				handler = require("./" + requestArgs.script);
				handler.perform({request: request, response: response, requestArgs: requestArgs});
			} catch (e) {
				console.log('got error ' + e);
				
				response.statusCode = 500;
				response.setHeader('Content-Type', 'text/plain');
				response.setHeader('Content-Length', msg.length);
				response.write(msg);
				response.close();
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