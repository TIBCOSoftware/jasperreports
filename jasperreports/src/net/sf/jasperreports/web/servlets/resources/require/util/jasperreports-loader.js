define(["jquery-1.10.2"], function($) {
	
    var Loader = function(o) {

        this.config = {
            reporturi: null,
            async: true
        };

        $.extend(this.config, o);

        this.UrlManager = {
            applicationContextPath: null,
            reportcontexturl: "/servlets/reportcontext",
            reportoutputurl: "/servlets/reportoutput",
            reportactionurl: "/servlets/reportaction",
            reportcomponentsurl: "/servlets/reportcomponents",
            reportpagestatusurl: "/servlets/reportpagestatus"
        };

        // promises
        this.contextIdPromise = null;
    };
	
	Loader.prototype = {
        getContextId: function() {
            return this._getContextIdPromise().then(function(jsonData, textStatus, jqXHR) {
                return jsonData.contextid;
            });
        },

        getHtmlForPage: function(page, boolNavigate) {
            var it = this;
            return it.getContextId().then(function(ctxid) {
                return it._ajaxLoad({
                    url: it._getUrl("reportoutputurl"),
                    params: {
                        jr_ctxid: ctxid,
                        jr_page: page
                    }
                }, 'html');
            });
        },

        getStatusForPage: function(page, timestamp) {
            var it = this;
            return it.getContextId().then(function(ctxid) {
                return it._ajaxLoad({
                    url: it._getUrl("reportpagestatusurl"),
                    params: {
                        jr_ctxid: ctxid,
                        jr_page: page,
                        jr_page_timestamp: timestamp
                    }
                }, 'json');
            });
        },

        getComponentsForPage: function(page) {
            var it = this;
            return it.getContextId().then(function(ctxid) {
                return it._ajaxLoad({
                    url: it._getUrl("reportcomponentsurl"),
                    params: {
                        jr_ctxid: ctxid,
                        jr_page: page
                    }
                }, 'json');
            });
        },

        runAction: function(o) {
            var it = this;
            return it.getContextId().then(function(ctxid) {
                return it._ajaxLoad({
                    url: it._getUrl("reportactionurl"),
                    params: {
                        jr_ctxid: ctxid,
                        jr_action: JSON.stringify(o.action)
                    }
                }, 'json');
            });
        },

        // internal functions
        _getUrl: function(key) {
            var url = this.UrlManager.applicationContextPath + this.UrlManager[key],
                jssParam;
            if (key === 'reportcontexturl') {
                jssParam = this._getUrlParameter('jss_context');
                jssParam && (url += '?jss_context=' + jssParam);
            }
            return url;
        },
        _getUrlParameter: function(paramName) {
            var params = window.location.search ? window.location.search.split('&') : [],
                paramValue = null;
            $.each(params, function(i, param) {
                if (param.indexOf(paramName) === 0) {
                    paramValue = param.split('=')[1];
                    return false; // break each
                }
            });

            return paramValue;
        },
        _getContextIdPromise: function() {
            var it = this;
            if (it.contextIdPromise == null) {
                it.contextIdPromise = it._ajaxLoad({
                    url: it._getUrl("reportcontexturl"),
                    params: {
                        jr_report_uri: it.config.reporturi,
                        jr_async: it.config.async
                    }
                }, 'json');
            }
            return it.contextIdPromise;
        },
		_ajaxLoad: function(o, dataType) {
			return  $.ajax(o.url, {type: 'POST', dataType: dataType, data: o.params}).then(
                null,
                this._errHandler
            );
		},
		_errHandler: function(jqXHR, textStatus, errorThrown) {
			return $.parseJSON(jqXHR.responseText);
		}
	};

    return Loader;
});
