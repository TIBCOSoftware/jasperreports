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


/**
 * Creation of a SVG rectangle using pure JavaScript
 */
define('simplerectangle', [], function () {

	return function (instanceData) {

		var w = instanceData.width,   // The width of the element
        h = instanceData.height;  // The height of the element

   
    var container = window.document.getElementById(instanceData.id);

    // Create the SVG Document
    var svgDocument = window.document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svgDocument.setAttributeNS(null, "width", w);
    svgDocument.setAttributeNS(null, "height", h);

    // Create the SVG Rectangle
    var rectangle = window.document.createElementNS("http://www.w3.org/2000/svg", "rect");
    rectangle.setAttributeNS(null, "x", 0);
    rectangle.setAttributeNS(null, "y", 0);
    rectangle.setAttributeNS(null, "width",  w);
    rectangle.setAttributeNS(null, "height",  h);
    rectangle.setAttributeNS(null, "fill", "red");

    // Add the rectangle to the SVG
    svgDocument.appendChild( rectangle );
   
    // Add the SVG to the container
    container.appendChild( svgDocument );
  };
});

