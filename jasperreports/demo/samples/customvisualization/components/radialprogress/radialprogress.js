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

define('radialprogress', ['d3'], function (d3) {

    return function (instanceData) {

        var w = instanceData.width,
            h = instanceData.height;

        var mergeObject = function (a, b) {
            var c = {};
            for (var attr in a) { c[attr] = a[attr]; }
            for (var attr in a) { if (b.hasOwnProperty(attr)) { c[attr] = b[attr]; } }

            return c;
        };
        var checkBoolean = function (v) {
            if (typeof v !== "boolean") {
                var val = (v + "").toLowerCase();
                return val === "true" || val === "1";
            }

            return v;
        };

        var defaults = {
            value: 1,
            target: 1,
            fgColor: "000000",
            fgOpacity: 1.0,
            bgColor: "#000000",
            bgOpacity: 0.2,
            ringThickness: 4,
            ringPadding: 4,
            showBackground: true,
            hPadding: 10,
            vPadding: 10,
            maxRings: 5,
            valueFormat: ".2f",
            autoFit: true,
            animation: true
        }

        var options = mergeObject(defaults, instanceData);

        // Data conversion
        options.value = parseFloat(options.value);
        options.target = parseFloat(options.target);
        options.fgOpacity = parseFloat(options.fgOpacity);
        options.bgOpacity = parseFloat(options.bgOpacity);
        options.ringThickness = parseFloat(options.ringThickness);
        options.ringPadding = parseFloat(options.ringPadding);
        options.animation = checkBoolean(options.animation);
        options.autoFit = checkBoolean(options.autoFit);
        options.showBackground = checkBoolean(options.showBackground);
        options.maxRings = checkBoolean(options.maxRings);


        // The max diameter
        var diameter = Math.min(w - options.hPadding * 2, h - options.vPadding * 2);

        // Data calculation.
        // Data is based on the value of value, based on target
        if (options.target == 0) {
            options.target = 1;
        }

        options.value = options.value / options.target;

        // Value 1 of value is 100%
        // Let's see which circles do we have to create...
        var rings = [];
        var currentValue = options.value;
        var index = 0;
        var lastRingRadius = (diameter / 2);

        while (currentValue > 0) {
            var ring_item = {};
            ring_item.value = Math.min(currentValue, 1);
            ring_item.index = index;
            ring_item.startAngle = 0;
            ring_item.endAngle = 360 * (ring_item.value) * (Math.PI / 180);
            ring_item.outerRadius = (diameter / 2) - (index * (options.ringThickness + options.ringPadding));
            ring_item.innerRadius = ring_item.outerRadius - options.ringThickness;

            lastRingRadius = ring_item.innerRadius;

            index++;
            currentValue -= 1;

            rings.push(ring_item);

            if (index >= options.maxRings && options.maxRings > 0) break;
        }



        var svg = d3.select("#" + instanceData.id).insert("svg")
            .attr("id", instanceData.id + "svg")
            .attr("width", w)
            .attr("height", h);

        var arcGenerator = function (d) {
            return d3.arc()
                .innerRadius(d.innerRadius)
                .outerRadius(d.outerRadius)
                .startAngle(0)
                .endAngle(d.endAngle)
                ();

        };

        var arcAnimator = function (transition) {
            transition.attrTween("d", function (d) {
                var interpolate = d3.interpolate(0, d.endAngle);
                return function (t) {
                    d.endAngle = interpolate(t);
                    return arcGenerator(d);
                };
            });
        };


        var rings_container_bg = svg.append("g").attr("class", "rings_bg").attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");

        if (options.showBackground) {
            var ring_bg_nodes = rings_container_bg.selectAll("path").data(rings);

            ring_bg_nodes.enter().append("path").attr("d", function (d) {
                return d3.arc()
                    .innerRadius(d.innerRadius)
                    .outerRadius(d.outerRadius)
                    .startAngle(0)
                    .endAngle(Math.PI / 180 * 360)();

            })
                .attr("fill", options.bgColor)
                .attr("fill-opacity", options.bgOpacity);
        }


        var rings_container = svg.append("g").attr("class", "rings").attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");

        var ring_nodes = rings_container.selectAll("path").data(rings);
        ring_nodes = ring_nodes.enter().append("path")
            .attr("fill", options.fgColor)
            .attr("fill-opacity", options.fgOpacity)
            .attr("d", function (d) { return arcGenerator(d); });

        if (options.animation) {

            ring_nodes.transition()
                .duration((options.animation) ? 750 : 0)
                .call(arcAnimator);
        }



        var textFormatter = d3.format(options.valueFormat);

        var txt_g = svg.append("g");
        var txt = txt_g.append("text").text(d3.format(options.valueFormat)(options.value))
            .datum({ value: options.value })
            .attr("x", "0")
            .attr("y", "0")
            .attr("text-anchor", "middle")
            //.attr("alignment-baseline","middle")
            .attr("fill", options.fgColor)
            .attr("fill-opacity", options.fgOpacity);

        // We need to find the baseline correction, since batik does not support alignment-baseline...
        var bbox = txt.node().getBBox();
        var baseline = bbox.height + bbox.y;
        var text_y = ((h - bbox.height) / 2) + bbox.height - baseline;
        var text_x = (w / 2);
        txt_g.attr("transform", "translate(" + text_x + "," + text_y + ")");

        if (options.autoFit) {
            // In this case we need to be sure that the text box is not big as the last ring...
            var box_radius = Math.sqrt(Math.pow(bbox.height / 2, 2) + Math.pow(bbox.width / 2, 2));
            var ratio = lastRingRadius / box_radius;
            text_y = ((h - bbox.height * ratio) / 2) + bbox.height * ratio - baseline * ratio;
            text_x = (w / 2);
            txt_g.attr("transform", "translate(" + text_x + "," + text_y + ") scale(" + ratio + ")");
        }

        if (options.animation) {

            txt.transition()
                .duration(750)
                .tween("text", function (d) {
                    var val = d3.interpolate(0, options.value);
                    return function (t) {
                        this.textContent = textFormatter(val(t));
                    };
                });
        }

    };

});

