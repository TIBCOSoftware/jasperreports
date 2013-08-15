define(["jquery-1.10.2"], function($) {
	var Event = function(name) {
        if (!this instanceof Event) {
            return new Event(name);
        }
        this.status = 'default';
        this.name = name;
        this.subscribers = [];
    };

    Event.prototype = {
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
        trigger: function() {
            var i, ln = this.subscribers.length;
            for (i = 0; i < ln; i++) {
                i = i - this.processSubscriber(this.subscribers[i]);
            }
            this.status = 'finished';
            return this;
        },
        reset: function() {
            this.status = 'default';
        },
        processSubscriber: function(subscriber) {
            var i, ln = this.subscribers.length;
            subscriber.callback.apply(subscriber.ctx, subscriber.args || []);
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


    var EventManager = function(debug) {
		this._events = {};
        this.debug = debug;
    };

    EventManager.prototype = {
        /**
         * options = {name: string, callback: function, thisContext: object, keep: boolean}
         */
        subscribeToEvent: function (o) {
            var it = this,
                evtNames = this._getEventNames(o.name);

            $.each(evtNames, function(i, evtName) {
                it.registerEvent(evtName).subscribe({
                    callback: o.callback,
                    ctx: o.thisContext,
                    keep: o.keep
                });
            });
        },
        registerEvent: function (evtName) {
            if (!this._events[evtName]) {
                this._events[evtName] = new Event(evtName);
            }
            return this._events[evtName];
        },
        triggerEvent: function (evtName) {
            if (this._events[evtName]) {
                this.debug && console.log("triggering event: " + evtName);
                this._events[evtName].trigger();
            }
        },

        // internal functions
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

    return EventManager;
});
