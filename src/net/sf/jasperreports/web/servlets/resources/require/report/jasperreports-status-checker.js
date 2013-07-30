define(["jquery"], function($) {
	var StatusChecker = function(loader) {
        this.loader = loader;

        // timers
        this.timeoutId = null;
	};

	StatusChecker.prototype = {

        checkPageModified: function(page, pageTimestamp) {
            var it = this,
                deferred = $.Deferred();

            it._timedCheckPageModified(false, page, pageTimestamp, deferred);

            return deferred.promise();
        },
        cancelCheckPageModified: function() {
            clearTimeout(this.timeoutId);
        },

        // internal functions
        _timedCheckPageModified: function(booleanDone, pageNo, pageTimestamp, deferredObject) {
            var it = this;
            if (!booleanDone) {
                it.timeoutId = setTimeout(
                    (function(page, timestamp, deferred) {
                        return function() {
                            it._getPageModifiedStatus(page, timestamp, deferred);
                        };
                    }(pageNo, pageTimestamp, deferredObject)), 3000);
            } else {
                deferredObject.resolve();
            }
        },
        _getPageModifiedStatus: function(page, pageTimestamp, deferredObject) {
            var it = this;
            return it.loader.getStatusForPage(page, pageTimestamp)
                .then(function(jsonData, textStatus, jqHXR) {
                    it._timedCheckPageModified(jsonData.result.pageModified, page, pageTimestamp, deferredObject);
                });
        }
	};
	
	return StatusChecker;
});
