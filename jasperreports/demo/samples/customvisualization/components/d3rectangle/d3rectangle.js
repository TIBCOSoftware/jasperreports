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
  * Creation of a SVG rectangle and lines using D3.js
  */ 
 define('d3rectangle', ['d3'], function (d3) {

	return function (instanceData) {

    var w = instanceData.width,   // The width of the element
        h = instanceData.height;  // The height of the element
        bgColor = instanceData.background; 

    // If no property called background has been provided, let's use a default gray background color.
    if (typeof bgColor === 'undefined') {
      bgColor = '#efefef';
    }

    // Add the SVG to the dom...
    var svg = d3.select('#' + instanceData.id).append('svg')
                  .attr('width', w)
                  .attr('height', h);

    // Add the rectangle...
    var rectangle = svg.append('rect')
                  .attr('x', 0)
                  .attr('y', 0)
                  .attr('width', w)
                  .attr('height', h)
                  .attr('fill', bgColor);

    // Define a scale to map the width of the component with the values range
    var hscale = d3.scaleLinear()
            .range([10, instanceData.width-10]) // we leave 10px padding on left and right
            .domain([
                d3.min(instanceData.series[0], function(d) { return +d.value; }),
                d3.max(instanceData.series[0], function(d) { return +d.value; })
              ]);

    // Add the lines
    svg.append("g").selectAll("line")
        .data(instanceData.series[0])
        .enter()
        .append('line')
        .attr('x1', function(d) {  return hscale(+d.value); } )
        .attr('y1', 0)
        .attr('x2', function(d) {  return hscale(+d.value); } )
        .attr('y2', h)
        .attr('stroke', function(d) { return d.color; })
        .attr('stroke-width', 3);             

  };
});