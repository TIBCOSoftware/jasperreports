define(["jquery"], function($) {

	var JRUtils = function(o) {

        if (!(this instanceof JRUtils)) {
            return new JRUtils(o);
        }
			
		this.config = {
            width: 300,
            height: 400,
            containerid: 'jivecontainer',
            reporturl: null
		};

		$.extend(this.config, o);
    };

    JRUtils.prototype = {
		/**
		 * Ajax loads the report viewer and places it inside the element with id = settings.containerid
		 */
        loadViewer: function() {
            var it = this,
                container,
                cfg = it.config;

			if (cfg.reporturl && cfg.containerid) {
				container = $('#' + cfg.containerid);
                cfg.width && container.css({ width: cfg.width});
                cfg.height && container.css({ height: cfg.height});
				container.append("<div class='result' style='width:100%; height:100%; overflow:auto;'></div>");
				
				$('div.result', container).load(cfg.reporturl, function(response, status, xhr) {
					if (status === 'error') {
						alert('Error: ' + xhr.status + ' ' + xhr.statusText);
					}
				});
			}
		}
	}

    return JRUtils;
});