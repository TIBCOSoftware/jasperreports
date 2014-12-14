/*
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 * Licensed under commercial Jaspersoft Subscription License Agreement
 */
 
var args = require('system').args,
        page = require('webpage').create(),
        fileSystem = require('fs'),
        url, outputFileName;

if (args.length != 3) {
    console.log('Usage: div2png output.svg');
    phantom.exit();
}

var url = args[1];
var outputFileName = args[2];

page.onConsoleMessage = function(msg) {
    if (msg == "__quit__") {
        phantom.exit();
    }
    console.log(msg);
};


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
			if ($config.error) $config.error();
			if ($config.debug) console.log('timedout ' + (new Date - $config._start) + 'ms');
			return;
		    }
		
		    if ($config.check()) {
			if ($config.debug) console.log('success ' + (new Date - $config._start) + 'ms');
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
                                    phantom.exit(500);
                                }


                // We will wait for 3 seconds until we don't see an SVG tag, we will exit...
                waitFor({
                            debug: false,  // optional
                            interval: 0,  // optional
                            timeout: 3000,  // optional
                            check: function () {
                                return page.evaluate(function() {
                                    return svgs = document.getElementsByTagName("svg").length > 0;
                                });
                            },
                            success: function () {
                                // we have what we want
                                page.onPageReady();
                            },
                            error: function () {
                                console.log("SCRIPT_ERROR Script did not produce any SVG within 3 seconds. Possible script error.");
                                phantom.exit(500);
                            } // optional
                        });
        
        } catch (ex)
        {
            console.log("SCRIPT_ERROR " + ex);
            phantom.exit(500);
        }
    
};


/**
 * This pageReady will be invoked as we see an SVG tag on the page....
 * 
 * @returns {undefined}
 */
page.onPageReady = function()
{
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

                             console.log("svgs..." + svgs.length);    
                                
                            styles = (styles === undefined) ? "" : styles;

                            for (i=0; i<svgs.length; ++i)
                            {
                              var svg = svgs[i];
                              svg.setAttribute("version", "1.1");
                              
                              var defs_element = document.createElement('defs');
                              var style_element = document.createElement('style');
                              style_element.setAttribute("type","text/css");
                              
                              defs_element.appendChild(style_element);
                              
                              svg.insertBefore(style_element, svg.firstChild);
                              
                       
//                                .insert("defs", ":first-child")
//                                  .attr("class", "svg-crowbar")
//                                .append("style")
//                                  .attr("type", "text/css");

                              // removing attributes so they aren't doubled up
                              svg.removeAttribute("xmlns");
                              svg.removeAttribute("xlink");
                              //svg.removeAttribute("svg");

                              // These are needed for the svg
                              if (!svg.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns")) {
                                svg.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/svg");
                              }

                              if (!svg.hasAttribute("xmlns:xlink")) {
                                svg.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xlink", "http://www.w3.org/1999/xlink");
                              }

                              // These are needed for the svg
//                              if (!svg.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:svg")) {
//                                svg.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:svg", "http://www.w3.org/2000/svg");
//                              }
                              
                              var source = (new XMLSerializer()).serializeToString(svg).replace('</style>', '<![CDATA[' + styles + ']]></style>');
                              console.log("Source is " + source);
                              
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
                    if (sources.length == 0)
                    {
                        throw "No SVG found in this html.";
                    }
                    return sources[0].source;
                    
                } catch (e)
                {
                    console.log("SCRIPT_ERROR " + e);
                    console.log("__quit__");
                }
            });


            if (svgString !== "")
            {
                fileSystem.write(outputFileName, svgString, 'w');
                console.log("SCRIPT_SUCCESS");
                phantom.exit(0);
            }
            else
            {
                console.log("SCRIPT_ERROR Empty SVG created. SVG element not found?");
                phantom.exit(500);
            }	
}


page.open(url);
