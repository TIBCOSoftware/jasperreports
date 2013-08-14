define(["jquery-1.10.2"], function($) {
	var StatusChecker = function(loader) {
        this.loader = loader;

        // timers
        this.timeoutId = null;
	};

	StatusChecker.prototype = {

        checkPageModified: function(page, pageTimestamp) {
            var it = this,
                deferred = $.Deferred();

            it._timedCheckPageModified(false, page, pageTimestamp, deferred, null);

            return deferred.promise();
        },
        cancelCheckPageModified: function() {
            clearTimeout(this.timeoutId);
        },

        // internal functions
        _timedCheckPageModified: function(booleanDone, pageNo, pageTimestamp, deferredObject, statusResult) {
            var it = this;
            if (!booleanDone) {
            	it.timeoutId = setTimeout(function() {
        			it._getPageModifiedStatus(pageNo, pageTimestamp, deferredObject);
        		}, 3000);
            } else {
                deferredObject.resolve(statusResult);
            }
        },
        _getPageModifiedStatus: function(page, pageTimestamp, deferredObject) {
            var it = this;
            return it.loader.getStatusForPage(page, pageTimestamp)
                .then(function(jsonData, textStatus, jqHXR) {
                    it._timedCheckPageModified(jsonData.result.pageModified || jsonData.result.status == "finished", page, pageTimestamp, deferredObject, jsonData.result);
                });
        }
	};
	
	return StatusChecker;
});
