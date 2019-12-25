/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
define('cv-component',["require"], function(require){
    var cvComponent = function(config) {
        this.config = config;
        this._init();
    };

    cvComponent.prototype = {
        // internal API
        _init: function() {
            var self = this;

            // Cleanup the DIV...
            // This is due to a bug in the interactive viewer which
            // invokes the component twice.
            var element = document.getElementById(self.config.id);
            if (element)
            {
                var currentSvgTags = element.getElementsByTagName("svg");
                if (currentSvgTags.length > 0) {
                    element.removeChild(currentSvgTags[0]);
                };
            }

            require([self.config.renderer], function(renderer) {
                renderer(self.config.instanceData);
            });

        }
    }

    return cvComponent;
});
