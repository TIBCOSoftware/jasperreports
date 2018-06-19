/*******************************************************************************
 * Copyright (C) 2005 - 2018 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

define('leafletmarkers',['leaflet','leaflet-providers','leaflet-ajax', 'icon'], function (L) {

	 return function (instanceData) {
		 
         // Ignore styles is no longer required if the component is set to be exported directly as PNG
         //window.cvcIgnoreSVGStyles = true;	

         // Flag used by the server side renderer to check when the component is actually ready
         window.componentRendered = false;
         
         // Operations completed counter
         var fully_loaded = 0;

         // When we export a report on the server side, we want to set the flag window.componentRendered to signal that
         // all the rendering is complete and ready to be exported.
         // For this we use a function that increment a counter of the operations that needs to be completed asynchronously
         // such as tiles loading and json data loading
         // The operations are 2, so as the counter reach 2, we are ready to render.
         function loadedCompleted() {
                   fully_loaded++;

                   if (fully_loaded >= 2)
                   {
                       window.componentRendered = true;
                   }
         }
         
         
         // We use the animation flag set by the CVC exporter to determine if the export is performed on server or client.
         // The CVC tells us to avoid animations in case of server side export, so if instanceData.animation and true, we
         // are rendering on the server.
         var serverSideExport = false;
         if (typeof instanceData.animation != 'undefined' && instanceData.animation == false)
         {
            serverSideExport = true;
         }
         
         // Start of the real component implementation ------------------------------------
         
         var map = L.map(instanceData.id, {
                    fadeAnimation: !serverSideExport, // Don't use fade effects on server side export
                    zoomControl: !serverSideExport    // Don't show any control on server side export
                });
         
 
        // Free layer very cool!
        //L.tileLayer.provider('Stamen.Watercolor').addTo(map);
        if (typeof instanceData.provider == 'undefined' || instanceData.provider == "")
        {
        	instanceData.provider = 'OpenTopoMap';
 		}
        
        // The map provider is specified by the user.
        // We store the layer in order to attach a listener that will tell us
        // when the tiles are fully ready.
        var tile_layer = L.tileLayer.provider(instanceData.provider).addTo(map);
        tile_layer.on("load",function() {  
                loadedCompleted();          
        });
         
        // Set the center of the map based on the user preferences...
        map.setView([instanceData.lat, instanceData.lon ], instanceData.zoom);

         
        // Loading the GeoJSON via ajax...
        // "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson"
        // "https://raw.githubusercontent.com/datasets/geo-boundaries-us-110m/master/json/ne_110m_admin_1_states_provinces_shp_scale_rank.geojson"
        if (typeof instanceData.dataUrl != 'undefined' && instanceData.dataUrl != "")
        {
            console.log("Loading geoJSON from: " + instanceData.dataUrl);
            L.Util.ajax(instanceData.dataUrl).then(function(data){
            		L.geoJson(data, {
                        pointToLayer: function(feature, latlng) {
                           return L.marker(latlng, {icon: myIcon()});
                        }
                    }).addTo(map);
                
                    // Markers loaded and displayed
                    loadedCompleted();
            });
        }
        else // If we are not loading anything, we need to mark this process completed.
        {
              // This call will increment the counter of processes completed.
              loadedCompleted();
        }
        
        // If tiles are not loaded in less than 10 seconds, still declare the report to be completed...
        setTimeout(10000, function() {
            loadedCompleted();
        });

		// Add markers from the data series, if it is there
		if (typeof instanceData.series != 'undefined' && instanceData.series[0].length > 0)
        {
                instanceData.series[0].forEach(function(marker) {
                    L.marker([marker.lat, marker.lon], { icon: myIcon() }).addTo(map)
                    .bindPopup(marker.label);
                });
        }
	};
    
    
});

