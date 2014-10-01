var args = require('system').args,
        page = require('webpage').create(),
        fileSystem = require('fs'),
        url, outputFileName;

if (args.length != 3) {
    console.log('Usage: div2png output.svg');
    phantom.exit();
}

url = args[1];
outputFileName = args[2];

page.onConsoleMessage = function(msg) {
    if (msg == "__quit__") {
        phantom.exit();
    }
    console.log(msg);
};

page.onLoadFinished = function() {

    try {
        
        window.onerror = function myErrorHandler(errorMsg) {
            console.log("SCRIPT_ERROR " + errorMsg);
            phantom.exit(500);
        }
                

        var svgString = page.evaluate(function() {

//                if (typeof d3 == 'undefined')
//                {
//                    console.log("SCRIPT_ERROR D3.js is not included in your script... you must include it in your template file to export in formats other than HTML.");
//                    console.log("__quit__");
//                }
                    
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
            
    } catch (ex)
    {
        console.log("SCRIPT_ERROR " + ex);
        phantom.exit(500);
    }
};


page.open(url);






  
  
  