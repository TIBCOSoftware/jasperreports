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
				container.append("<div class='result' style='position: relative; width:100%; height:100%; overflow:auto; z-index: 1001'></div>");

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