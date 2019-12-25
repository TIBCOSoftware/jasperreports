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

define('figures', ['d3'], function (d3) {

	return function (instanceData) {

		// Chart configuration

		var w = instanceData.width,
			h = instanceData.height;

		var mergeObject = function (a, b) {
			var c = {};
			for (var attr in a) { c[attr] = a[attr]; }
			for (var attr in a) { if (b.hasOwnProperty(attr)) { c[attr] = b[attr]; } }

			return c;
		};

		var defaults = {
			figure: "Male",
			itemsCount: 1,
			itemsValue: 1,
			fgColor: "#000000",
			fgOpacity: "1",
			bgColor: "#0000000",
			bgOpacity: "0.2",
			showBackground: true,
			rows: 0,
			columns: 0,
			vAlign: "top",  // top, bottom, middle
			hAlign: "left",   // left, right, center
			hPadding: 10,
			vPadding: 10,
			customPath: "",
			maximizeColumnNumber: true, //
		}

		var options = mergeObject(defaults, instanceData);


		// Man or Women (default Man)
		var man = options.figure != "Female";
		var items_count = parseFloat(options.itemsCount); // The number of items.
		var items_value = parseFloat(options.itemsValue);

		options.fgOpacity = parseFloat(options.fgOpacity);
		options.bgOpacity = parseFloat(options.bgOpacity);
		options.rows = parseFloat(options.rows);
		options.columns = parseFloat(options.columns);
		options.hPadding = parseFloat(options.hPadding);
		options.vPadding = parseFloat(options.vPadding);


		var checkBoolean = function (v) {
			if (typeof v !== "boolean") {
				var val = (v + "").toLowerCase();
				return val === "true" || val === "1";
			}

			return v;
		};

		// Converting boolean from strings...
		options.showBackground = checkBoolean(options.showBackground);
		options.maximizeColumnNumber = checkBoolean(options.maximizeColumnNumber);

		var showPhantom = options.showBackground;


		if (items_count < items_value) {
			items_value = items_count;
		}

		// Rows to be used.
		var rows = options.rows;
		var columns = options.columns;


		if (items_count <= 0) {
			items_count = Math.max(Math.ceil(items_value), 1);
		}

		var svg = d3.select("#" + instanceData.id).insert("svg")
			.attr("id", instanceData.id + "svg")
			.attr("width", w)
			.attr("height", h);



		// Let' prepare the background...
		var defs = svg.append("defs");

		// This method allows to get the bbox of an element even if not rendered yet.
		// An svg will be created with this path, rendered and remove immediately...
		var getbbox2 = function (path) {
			var svg2 = d3.select("#" + instanceData.id).insert("svg")
				.attr("id", "toremove")
				.attr("width", w)
				.attr("height", h);
			var p = svg2.append("path")
				.attr("d", path)
				.attr("fill", "#CC0000");

			var bbox = p.node().getBBox();
			svg2.remove();

			return bbox;

		}

		var figureBbox = { x: 0, y: 0, width: 0, height: 0 };

		var figurePath = "";

		if (options.customPath != "") {
			figurePath = options.customPath;
		}
		else if (man) {
			figurePath = "M 67.142578,0 A 38.571423,38.571423 0 0 0 28.572266,38.572266 38.571423,38.571423 0 0 0 67.142578,77.142578 38.571423,38.571423 0 0 0 105.71484,38.572266 38.571423,38.571423 0 0 0 67.142578,0 Z M 23.927734,81.455078 C 10.672174,81.455078 0,92.127252 0,105.38281 l 0,143.57422 c 0,13.25556 10.672174,23.92774 23.927734,23.92774 l 0.357422,0 0,-144.28516 c 0,-1.58286 1.274562,-2.85742 2.857422,-2.85742 1.58285,0 2.857422,1.27456 2.857422,2.85742 l 0,144.28516 0.02734,0 0,153.54882 c 0,10.49967 7.325181,18.95313 16.425781,18.95313 9.1006,0 16.427734,-8.45346 16.427734,-18.95313 l 0,-153.54882 4.261719,0 4.261719,0 0,153.54882 c 0,10.49967 7.327134,18.95313 16.427734,18.95313 9.1006,0 16.425783,-8.45346 16.425783,-18.95313 l 0,-153.54882 0.0273,0 0,-144.28516 c 0,-1.58286 1.27457,-2.85742 2.85742,-2.85742 1.58286,0 2.85742,1.27456 2.85742,2.85742 l 0,144.28516 0.35742,0 c 13.25556,0 23.92774,-10.67218 23.92774,-23.92774 l 0,-143.57422 c 0,-13.255558 -10.67218,-23.927732 -23.92774,-23.927732 l -43.214846,0 -43.214844,0 z";
		}
		else {
			figurePath = "M 111.16211,7.0859375 C 89.859805,7.0865511 72.591331,24.355899 72.591797,45.658203 c 6.14e-4,21.301541 17.268771,38.569699 38.570313,38.570313 21.3023,4.63e-4 38.57165,-17.268011 38.57227,-38.570313 4.6e-4,-21.303066 -17.2692,-38.5727289 -38.57227,-38.5722655 z m 0,81.4550785 -43.214844,0 c -13.25556,0 -20.201482,11.206694 -23.927735,23.927734 L 8.078125,235.17383 c -3.726252,12.72104 3.328424,36.68164 16.583984,36.68164 l 0.357422,0 43.285157,-136.16992 c 0.479517,-1.50848 1.274561,-2.85743 2.857421,-2.85743 1.58285,0 2.857422,1.27457 2.857422,2.85743 l 0,24.35156 -43.503906,149.72266 43.688997,-0.28002 -0.157747,124.03978 c -0.01335,10.49966 7.325182,18.95313 16.425781,18.95313 9.100601,0 16.427734,-8.45346 16.427734,-18.95313 l 0,-123.73222 8.88086,0.0279 0,123.25898 c 0,10.49967 7.32713,18.95312 16.42773,18.95312 9.1006,0 16.42579,-8.45345 16.42579,-18.95312 l 0,-123.40879 43.53125,0.28001 -43.50391,-150.35364 0,-24.35157 c 0,-1.58286 1.27457,-2.85742 2.85742,-2.85742 1.58286,0 2.3779,1.34894 2.85742,2.85742 l 43.28516,136.16993 0.35742,0 c 13.25556,0 20.31025,-23.9606 16.58399,-36.68164 L 178.66211,112.02344 C 174.93585,99.302397 167.83295,86.061677 154.73438,88.095703 Z";
		}

		var bbox = getbbox2(figurePath);

		// Add some minimal path padding.
		bbox.width += 10;
		bbox.height += 10;

		// Set a function to append the figure to an element (g?)

		var appendFigure = function (parent, color, opacity) {

			var pattern = parent.append("g");
			var figure = pattern.append("path");

			figure.attr("d", figurePath);
			figure.attr("fill", color);
			figure.attr("fill-opacity", opacity);

			// Center the path...
			figure.attr("transform", "translate(" + (-bbox.x + 5) + "," + (-bbox.y + 5) + ")");

			return pattern;
		};




		var pattern_ratio = bbox.width / bbox.height;

		// Calculate the # of columns based on rows...
		if (rows > 0 && columns <= 0 || (rows > 0 && columns > 0 && rows * columns < items_count)) {
			// Let's see how many items fit, considering that row height..
			var ih = (h - (options.vPadding * (rows - 1))) / rows;
			var iw = (pattern_ratio * ih) + options.hPadding;

			// Items per row...
			if (options.maximizeColumnNumber) {
				columns = Math.max(Math.floor((w + options.hPadding) / iw), Math.ceil(items_count / rows));
			}
			else {
				columns = Math.min(Math.floor((w + options.hPadding) / iw), Math.ceil(items_count / rows));
			}

		}
		// Calculate the # of rows based on columns...
		else if (rows <= 0 && columns > 0) {
			// Let's see how many items fit, considering that column width...
			var iw = (w - (options.hPadding * (columns - 1))) / columns;
			var ih = iw / pattern_ratio + options.vPadding;

			rows = Math.max(Math.floor((h + options.vPadding) / ih), Math.ceil(items_count / columns));
		}
		else if (rows <= 0 && columns <= 0) {
			// We need to find the best number of columns and rows.
			// The optimal solition is the one that minimizes the extra space.
			// Since we know in advance the ratio of our rectangles, we can do this calculation by
			// testing several solutions adding rows once a time...
			var t_rows = 1;
			var t_cols = items_count;

			rows = t_rows;
			columns = t_cols;

			var lastBlankArea = w * h;

			while (true) {


				var iw = Math.max(1, (w - (options.hPadding * (t_cols - 1)))) / t_cols;
				var ih = Math.max(1, (h - (options.vPadding * (t_rows - 1)))) / t_rows;

				// Recalculate item_w and item_h proportionally...
				//console.log("Testing: " + t_cols + " x "  + t_rows);


				// Must be at least one pixel per column...
				if (t_cols > 1 && (options.hPadding * (t_cols - 1) + t_cols) > w) {
					t_cols--;
					t_rows = Math.ceil(items_count / t_cols);
					continue;
				}

				// Must be at least one pixel per column...
				if (t_rows > 1 && (options.vPadding * (t_rows - 1) + t_rows) > h) {
					rows = Math.max(0, t_rows--);
					columns = t_cols;
					break;
				}


				// Find the bunding box of the path used for the pattern
				// and find its proportion (ratio).
				if (iw / pattern_ratio > ih) {
					iw = pattern_ratio * ih;
				}
				else {
					ih = iw / pattern_ratio;
				}

				var remaining_left = Math.max(1, (w - iw * t_cols - options.hPadding * (t_cols - 1))) * parseFloat(h);
				var remaining_height = Math.max(1, (h - ih * t_rows - options.vPadding * (t_rows - 1))) * parseFloat(w);

				if (t_cols == 1 || lastBlankArea < (remaining_left + remaining_height))  // || w < iw*columns || 
				{
					//console.log("Giving up " + (lastBlankArea < (remaining_left + remaining_height)));
					break;
				}
				else {
					lastBlankArea = remaining_left + remaining_height;

					rows = t_rows;
					columns = t_cols;

					t_cols--;
					t_rows = Math.ceil(items_count / t_cols);

				}
			}
		}
		// Calculate the size of each small rectangle based on with and height of the chart area...
		var item_w = Math.max(1, (w - (options.hPadding * (columns - 1)))) / columns;
		var item_h = Math.max(1, (h - (options.vPadding * (rows - 1)))) / rows;

		// Recalculate item_w and item_h proportionally...

		// Find the bunding box of the path used for the pattern
		// and find its proportion (ratio).
		if (item_w / pattern_ratio > item_h) {
			item_w = pattern_ratio * item_h;
		}
		else {
			item_h = item_w / pattern_ratio;
		}

		var scale_factor = item_w / bbox.width;

		//console.log("Cols: " + columns + " Rows:" + rows);
		//console.log("item_w: " + item_w + " item_h:" + item_h);


		// The items need to be converted to a whole width...
		var total_val_in_points = bbox.width * items_value;

		var item_num = 0;


		var figures = svg.append("g");

		for (var j = 0; j < rows; ++j) {
			var figures_row = figures.append("g")
				.attr("class", "figures-row");

			var count_row_items = 0;
			for (var i = 0; i < columns; ++i) {
				item_num++;

				var phantom = null;

				if (item_num <= items_count && total_val_in_points < bbox.width && showPhantom) {



					phantom = figures_row.append("g")
						.attr("transform", "translate(" + (i * options.hPadding) + ",0) scale(" + scale_factor + ")");

					//phantom = phantom.append("rect")
					//		   .attr("x", i * bbox.width)
					//		   .attr("y", j * bbox.height)
					//		   .attr("width", bbox.width)
					//		   .attr("height", bbox.height);

					phantom = appendFigure(phantom, options.bgColor, options.bgOpacity);
					//.attr("fill","url(#figure_gray)");
					//.style("stroke","#000")
					//.style("stroke-width","0px"); 	
					phantom.attr("transform", "translate(" + (i * bbox.width) + "," + j * bbox.height + ")");

					count_row_items++;
				}

				if (total_val_in_points > 0) {

					count_row_items++;
					var item_width = Math.min(total_val_in_points, bbox.width);
					total_val_in_points -= bbox.width;

					var itemFigure = figures_row.append("g")
						.attr("transform", "translate(" + (i * options.hPadding) + ",0) scale(" + scale_factor + ")");

					//itemFigure = itemFigure.append("rect")
					//		   .attr("x", i * bbox.width)
					//		   .attr("y", j * bbox.height)
					//		   .attr("width", bbox.width)
					//		   .attr("height", bbox.height);

					itemFigure = appendFigure(itemFigure, options.fgColor, options.fgOpacity);
					itemFigure.attr("transform", "translate(" + (i * bbox.width) + "," + j * bbox.height + ")");

					//.attr("fill","url(#figure)");


					if (item_width < bbox.width) {
						// The phantom figure must be clipped...
						defs.append("clipPath")
							.attr("id", "figure-clip")
							//.append("g")
							//.attr("transform", "translate(" + (i * options.hPadding) + ",0) scale(" + scale_factor + ")")
							.append("rect")
							.attr("x", 0)
							.attr("y", 0)
							.attr("width", item_width)
							.attr("height", bbox.height);
						//						    .attr("transform","translate(" + (i * bbox.width) + "," + j * bbox.height +")");

						itemFigure.attr("clip-path", "url(#figure-clip)");
					}

					if (showPhantom && phantom != null &&
						total_val_in_points < 0) {
						// The phantom figure must be clipped...
						defs.append("clipPath")
							.attr("id", "phantom-clip")
							.append("rect")
							.attr("x", 0 + item_width)
							.attr("y", 0)
							.attr("width", bbox.width - item_width)
							.attr("height", bbox.height);

						phantom.attr("clip-path", "url(#phantom-clip)");


					}
				}
			}

			if (count_row_items == 0 && options.rows > 0) {
				var itemFigure = figures_row.append("g")
					.attr("transform", "translate(0,0) scale(" + scale_factor + ")");
				itemFigure.append("rect")
					.attr("x", 0)
					.attr("y", j * bbox.height)
					.attr("width", bbox.width)
					.attr("height", bbox.height)
					.attr("fill-opacity", "0");
			}
		}


		var shift_x = 0;
		var shift_y = 0;

		// Align the figures...
		svg.selectAll('g.figures-row')  //here's how you get all the nodes
			.each(function (d, i) {

				var row = d3.select(this);
				var box = row.node().getBBox();
				if (options.hAlign == "center") {
					shift_x = ((w - box.width) / 2);
				}
				else if (options.hAlign == "right") {
					shift_x = (w - box.width);
				}

				row.attr("transform", "translate(" + shift_x + ", " + (i * options.vPadding) + ")");
			});


		var figures_box = figures.node().getBBox();

		if (options.vAlign == "middle") {
			shift_y = (h - figures_box.height) / 2;
		}
		else if (options.vAlign == "bottom") {
			shift_y = (h - figures_box.height);
		}

		figures.attr("transform", "translate(0," + shift_y + ")");


	};

});