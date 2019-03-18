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
define('leafletmarkers', ['leaflet', 'icon'], function (L) {

    return function (instanceData) {


        //var L = require('leaflet');
        // Ignore styles is no longer required if the component is set to be exported directly as PNG
        //window.cvcIgnoreSVGStyles = true;	

        // Flag used by the server side renderer to check when the component is actually ready
        window.componentRendered = false;

        // Operations completed counter
        var fully_loaded = 0;

        // When we export a report on the server side, we want to set the flag window.componentRendered to signal that
        // all the rendering is complete and ready to be exported.
        // For this we use a function that increment a counter of the operations that needs to be completed asynchronously
        // such as tiles loading or json data loading if this component would support it.
        // In this case the only async operation executed is the tiles loading,
        // so as soon the counter reach 1, we are ready to render.
        function loadedCompleted() {
            fully_loaded++;

            if (fully_loaded >= 1) {
                window.componentRendered = true;
            }
        }


        // We use the animation flag set by the CVC exporter to determine if the export is performed on server or client.
        // The CVC tells us to avoid animations in case of server side export, so if instanceData.animation and true, we
        // are rendering on the server.
        var serverSideExport = false;
        if (typeof instanceData.animation != 'undefined' && instanceData.animation == false) {
            serverSideExport = true;
        }

        // Start of the real component implementation ------------------------------------

        var map = L.map(instanceData.id, {
            fadeAnimation: !serverSideExport, // Don't use fade effects on server side export
            zoomControl: !serverSideExport    // Don't show any control on server side export
        });


        // Free tile layer provided by OpenTopoMap
        var tile_layer = L.tileLayer(
                'https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png',
                {
                    maxZoom: 17,
                    attribution: 'Map data: {attribution.OpenStreetMap}, <a href="http://viewfinderpanoramas.org">SRTM</a> | Map style: &copy; <a href="https://opentopomap.org">OpenTopoMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'
                }
            ).addTo(map);
        tile_layer.on("load", function () {
            loadedCompleted();
        });

        // Set the center of the map based on the user preferences...
        map.setView([instanceData.lat, instanceData.lon], instanceData.zoom);

        // If tiles are not loaded in less than 10 seconds, still declare the report to be completed...
        setTimeout(10000, function () {
            loadedCompleted();
        });

        // Add markers from the data series, if it is there
        if (typeof instanceData.series != 'undefined' && instanceData.series[0].length > 0) {
            instanceData.series[0].forEach(function (marker) {
                L.marker([marker.lat, marker.lon], { icon: myIcon() }).addTo(map)
                    .bindPopup(marker.label);
            });
        }

    };

});

