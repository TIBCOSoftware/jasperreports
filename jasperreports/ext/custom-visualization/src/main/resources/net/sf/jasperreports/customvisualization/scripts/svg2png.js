/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
var webpage = require('webpage')
        , URL, FILENAME, W, H;
;
var args = require('system').args;

if (args.length < 3 || args.length > 5) {
    console.log('Usage: svg2png source.svg output.png [width||?] [height||?]');
    phantom.exit();
}
else {
    URL = args[1];
    FILENAME = args[2];
    console.log("Converting: " + FILENAME);
    W = Number(args[3]);
    H = Number(args[4]);
    if (isNaN(W))
        W = undefined;
    if (isNaN(H))
        H = undefined;
    peek(URL, grabSize);
}

function peek(url, fn) {
    var page = webpage.create();
    page.onConsoleMessage = function(msg) {
        console.log(msg);
    };
    page.open(url, function(status) {
        if (status !== 'success')
            console.log('Unable to load ' + url);
        else
            fn(page);
    });
}

function grabSize(page) {
    page.onConsoleMessage = function(msg) {
        var d = JSON.parse(msg);
        if (!W && !H) {
            W = d.w;
            H = d.h;
        }
        if (W && !H) {
            H = W * d.h / d.w;
        }
        if (!W && H) {
            W = H * d.w / d.h;
        }
        screenshot(URL, FILENAME, W, H);
    };

    page.evaluate(function() {
        var svg = document.documentElement
                , w = svg.getAttribute('width')
                , h = svg.getAttribute('height');
        
        console.log(JSON.stringify({w: Number(w), h: Number(h)}));
    });
}

function screenshot(url, filename, w, h) {
    var page = webpage.create();
    page.viewportSize = {width: w, height: h};
    page.open(url, function(status) {
        if (status !== 'success') {
            console.log('Unable to load ' + url);
            phantom.exit();
        }
        else {
            window.setTimeout(function() {
                page.zoomFactor = 4.0;
                page.render(filename);
                console.log("SCRIPT_SUCCESS");
                phantom.exit();
            }, 200);
        }
    });
}
