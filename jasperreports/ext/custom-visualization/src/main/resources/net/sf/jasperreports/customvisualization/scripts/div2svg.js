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
 
var args = require('system').args,
    page = require('webpage').create(),
    fileSystem = require('fs'),
    url, outputFileName;


function printUsage() {
    console.log('Usage: div2svg [--output-format=svg|png] [--timeout==ms] [--zoom-factor=val] input.html output.svg');
}

if (args.length < 2) {
    printUsage();
    phantom.exit();
}

var url = null;
var outputFileName = null;
var outputFormat = null;
var timeout = 3000;
var zoomFactor = 2.0;

args.forEach(function(arg, index) {

    if (index == 0) return;
    
    if (arg.lastIndexOf("--output-format=") == 0) {
        outputFormat = arg.substring(16);
        if (outputFormat != "svg" && outputFormat != "png") {
            console.log("Invalid output format. Supported formats are svg and png");
            printUsage();
            phantom.exit();
        }
    } else if (arg.lastIndexOf("--timeout=") == 0) {
        timeout = parseInt(arg.substring(10));
        if (outputFormat != "svg" && outputFormat != "png") {
            console.log("Invalid output format. Supported formats are svg and png");
            printUsage();
            phantom.exit();
        }
    } else if (arg.lastIndexOf("--zoom-factor=") == 0) {
        zoomFactor = parseFloat(arg.substring(14));
    } else if (url == null) {
        url = arg;
    } else if (outputFileName == null) {
        outputFileName = arg;
    } else {
        console.log("Unexpected parameter " + arg);
        printUsage();
        phantom.exit();
    }
});

page.onConsoleMessage = function(msg) {
    if (msg == "__quit__") {
        phantom.exit();
    }
    console.log(msg);
};

var onErrorFunction = function(msg, trace) {

  var msgStack = ['ERROR: ' + msg];

  if (trace && trace.length) {
    msgStack.push('TRACE:');
    trace.forEach(function(t) {
      msgStack.push(' -> ' + t.file + ': ' + t.line + (t.function ? ' (in function "' + t.function +'")' : ''));
    });
  }

  console.log("SCRIPT_ERROR " + msgStack.join('\n'));
  phantom.exit(503);
};

page.onError = onErrorFunction;
phantom.onError = onErrorFunction;


/**
 * As the page is loaded this function will be call by PhantomJS
 * @returns {undefined}
 */
page.onLoadFinished = function() {
    /**
     * Function taken from the PhantomJS examples to wait for something
     * to happen inside the page. In our case we will check if the svg has
     * been actually laoded.
     * This is somehow risky, because the script may fail and we will end
     * up waiting for nothing.
     */
	function waitFor($config) {
        $config._start = $config._start || new Date();

        if ($config.timeout && new Date - $config._start > $config.timeout) {
            if ($config.error) {
                $config.error();
            }

            if ($config.debug) {
                console.log('timedout ' + (new Date - $config._start) + 'ms');
            }

            return;
        }

        if ($config.check()) {
            if ($config.debug) {
                console.log('success ' + (new Date - $config._start) + 'ms');
            }
            return $config.success();
        }

        setTimeout(waitFor, $config.interval || 0, $config);
    }
		
	try {
        /**
         * Error handler, this will make phantomJS exit with code 500
         */
        window.onerror = function myErrorHandler(errorMsg) {
            console.log("SCRIPT_ERROR " + errorMsg);
            phantom.exit(501);
        };

        // We will wait for 3 seconds until we don't see an SVG tag, we will exit...
        waitFor({
            debug: false,  // optional
            interval: 25,  // optional
            timeout: timeout,  // optional
            check: function() {
                return page.evaluate(function() {
                    if (typeof window.componentRendered !== 'undefined')
                    {
                        if (window.componentRendered === true) {
                            return true;
                        }

                        return false;
                    }

                    return document.getElementsByTagName("svg").length > 0;
                });
            },
            success: function() {
                // we have what we want
                page.onPageReady();
            },
            error: function() {
                console.log("SCRIPT_ERROR Script timeout producing " + outputFormat + " within " + (timeout/1000).toFixed(0) + " seconds. Possible script error.");
                phantom.exit(502);
            } // optional
        });
    } catch (ex) {
        console.log("SCRIPT_ERROR " + ex);
        phantom.exit(503);
    }
    
};

/**
 * This pageReady will be invoked as we see an SVG tag on the page....
 * 
 * @returns {undefined}
 */
page.onPageReady = function() {
    // If requested, just take a screenshot...
    if (outputFormat == "png") {
        page.onConsoleMessage = function(msg) {
            var d = JSON.parse(msg);
            page.viewportSize = {width: d.w, height: d.h};
            page.zoomFactor = zoomFactor;
            console.log("Set the zoom (" + zoomFactor + "), rendering now...to " + outputFileName);
            page.render(outputFileName, {format: 'png'});
            console.log("SCRIPT_SUCCESS");
            phantom.exit();
        }

        page.evaluate(function() {
            var body = document.getElementsByTagName('body')[0];

            body.style.marginTop = '0px';
            body.style.marginLeft = '0px';
            var element = body.children[0];

            console.log(JSON.stringify({w: Number(element.offsetWidth), h: Number(element.offsetHeight)}));
        });
    } else {
        var svgString = page.evaluate(function() {

            function getStyles(doc) {
                var styles = "",
                    styleSheets = doc.styleSheets;

                if (styleSheets) {
                    for (var i = 0; i < styleSheets.length; i++) {
                        processStyleSheet(styleSheets[i]);
                    }
                }

                function processStyleSheet(ss) {
                    if (ss.cssRules && ss.cssRules !== null) {
                        for (var i = 0; i < ss.cssRules.length; i++) {
                            var rule = ss.cssRules[i];
                            if (rule.type === 3) {
                                // Import Rule
                                processStyleSheet(rule.styleSheet);
                            } else {
                                // hack for illustrator crashing on descendent selectors
                                if (rule.selectorText) {
                                    if (rule.selectorText.indexOf(">") === -1) {
                                        styles += "\n" + rule.cssText;
                                    }
                                }
                            }
                        }
                    }
                }

                return styles;
            }

            function getSources(doc, styles) {
                var doctype = '<?xml version="1.0" encoding="UTF-8" standalone="no"?><!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">';
                var svgInfo = [],
                    svgs = doc.getElementsByTagName("svg"); //  d3.select(doc).selectAll("svg");

                // console.log("svgs..." + svgs.length);

                styles = (styles === undefined) ? "" : styles;

                for (i=0; i<svgs.length; ++i) {
                    var svg = svgs[i];
                    svg.setAttribute("version", "1.1");

                    // removing attributes so they aren't doubled up
                    svg.removeAttribute("xmlns");

                    var defsTags = svg.getElementsByTagName("defs")
                    var defs_element = null;

                    if (defsTags.length == 0) {
                        defs_element = document.createElement('defs');

                        if (defs_element.hasAttribute("xmlns")) {
                            defs_element.removeAttribute("xmlns");
                        }

                        svg.insertBefore(defs_element, svg.firstChild);
                    } else {
                      defs_element = defsTags[0];
                    }

                    var innerDefs = defs_element.innerHTML;

                    if (typeof window.cvcIgnoreSVGStyles !== 'undefined' && window.cvcIgnoreSVGStyles == true) {
                      // Don't add any style, since the batik may not like it.
                    } else {
                        innerDefs += '<style type="text/css"><![CDATA[' + styles + ']]></style>';
                        defs_element.innerHTML = innerDefs;
                    }

                    var source = (new XMLSerializer()).serializeToString(svg).replace('<defs xmlns="http://www.w3.org/1999/xhtml">','<defs>'); //.replace('</style>', '<![CDATA[' + styles + ']]></style>');
                    var rect = svg.getBoundingClientRect();

                    svgInfo.push({
                        top: rect.top,
                        left: rect.left,
                        width: rect.width,
                        height: rect.height,
                        class: svg.getAttribute("class"),
                        id: svg.getAttribute("id"),
                        childElementCount: svg.childElementCount,
                        source: [doctype + source]
                    });
                }

                return svgInfo;
            }

            try {
                var styles = getStyles(document);
                var sources = getSources(document, styles);

                // This should never be true...
                if (sources.length == 0) {
                    throw "No SVG found in this html.";
                }

                return sources[0].source;
            } catch (e) {
                console.log("SCRIPT_ERROR " + e);
                console.log("__quit__");
            }
        });

        if (svgString !== "") {
            fileSystem.write(outputFileName, svgString, 'w');
            console.log("SCRIPT_SUCCESS");
            phantom.exit(0);
        } else {
            console.log("SCRIPT_ERROR Empty SVG created. SVG element not found?");
            phantom.exit(504);
        }
    }
}

page.onResourceError = function(resourceError) {
    console.log('SCRIPT_ERROR Unable to load resource (#' + resourceError.id + 'URL:' + resourceError.url + ') Error code: ' + resourceError.errorCode + '. Description: ' + resourceError.errorString);
    phantom.exit(505);
};


page.open(url, function(status) {
    if (status == 'fail') {
        console.log('SCRIPT_ERROR Unable to open the temporary file');
        phantom.exit(506);
    }
});