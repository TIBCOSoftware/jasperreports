/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */

define(['jquery'], function($) {
    var ReportExecution = function(arrHyperlinks) {
        this.hyperlinks = arrHyperlinks;
        this.reportInstance = null;
        this.reportContainer = null;
    };

    ReportExecution.prototype = {
        register: function() {
            var it = this;
            $(it.hyperlinks[0].selector).on('click', function(evt) {
                var hlData = it._getHyperlinkData($(this).attr('data-id'));
                if (hlData) {
                    it._handleHyperlinkClick(hlData);
                }
            }).css('cursor', 'pointer');
        },
        handleInteraction: function(evt) {
            if ('hyperlinkClicked' == evt.type) {
                var hlData = this._getHyperlinkData(evt.data.hyperlink.id);
                if (hlData) {
                    this._handleHyperlinkClick(hlData);
                }
            }
        },

        // internal functions
        _getHyperlinkData: function(id) {
            var hlData = null;
            $.each(this.hyperlinks, function(i, hl) {
                if (id === hl.id) {
                    hlData = hl;
                    return false; //break each
                }
            });
            return hlData;
        },
        _handleHyperlinkClick: function(hyperlink) {
            if (hyperlink.targetValue) {
                window.open(hyperlink.href, hyperlink.targetValue);
            } else {
                window.location = hyperlink.href;
            }
        }
    };

    return ReportExecution;
});