/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

define(function(require){
    var Map = function(config) {
        this.config = config;
        this.parent = null;
        this.loader = null;

        this.infowindow = {};

        this._init();
    };

    Map.prototype = {
        // public API

        // internal API
        _init: function() {
            var it = this,
                instData = it.config.instanceData,
                reqParams = instData.requestParams || "";

            // try to load the Gogle Maps API once, otherwise conflicts will happen
            if (typeof google === 'undefined' || (typeof google !== 'undefined' && typeof google.maps === 'undefined')) {
                require(["async!http://maps.google.com/maps/api/js?sensor=false" + reqParams + "!callback"], function() {
                    it._showMap(it.config.id, instData.latitude, instData.longitude, instData.zoom, instData.mapType, instData.markerList, instData.pathsList);
                });
            } else {
                it._showMap(it.config.id, instData.latitude, instData.longitude, instData.zoom, instData.mapType, instData.markerList, instData.pathsList);
            }

        },
        _configureImage: function (pk, pp, po) {
            var width, height, originX, originY, anchorX, anchorY;

            width = pp[pk + '.width'] ? parseInt(pp[pk + '.width']) : null;
            height = pp[pk + '.height'] ? parseInt(pp[pk + '.height']) : null;

            originX = pp[pk + '.origin.x'] ? parseInt(pp[pk + '.origin.x']) : 0;
            originY = pp[pk + '.origin.y'] ? parseInt(pp[pk + '.origin.y']) : 0;

            anchorX = pp[pk + '.anchor.x'] ? parseInt(pp[pk + '.anchor.x']) : 0;
            anchorY = pp[pk + '.anchor.y'] ? parseInt(pp[pk + '.anchor.y']) : 0;

            po[pk] = {
                url: pp[pk + '.url'],
                size: width && height ? new google.maps.Size(width, height) : null,
                origin: new google.maps.Point(originX,originY),
                anchor: new google.maps.Point(anchorX,anchorY)
            };
        },
        _createInfo: function (pp) {
            if(pp['infowindow.content'] && pp['infowindow.content'].length > 0) {
                var gg= google.maps,
                    po = {
                        content: pp['infowindow.content']
                    };
                if (pp['infowindow.pixelOffset']) po['pixelOffset'] = pp['infowindow.pixelOffset'];
                if (pp['infowindow.latitude'] && pp['infowindow.longitude']) po['position'] = new gg.LatLng(pp['infowindow.latitude'], pp['infowindow.longitude']);
                if (pp['infowindow.maxWidth']) po['maxWidth'] = pp['infowindow.maxWidth'];
                return new gg.InfoWindow(po);
            }
            return null;
        },
        _showMap: function(canvasId, latitude, longitude, zoom, mapType, markers, p) {
            var it = this,
                gg = google.maps,
                myOptions = {
                    zoom: zoom,
                    center: new gg.LatLng(latitude, longitude),
                    mapTypeId: gg.MapTypeId[mapType],
                    autocloseinfo: true
                },
                map = new gg.Map(document.getElementById(canvasId), myOptions);
            if(markers){
                var j;
                for (var i = 0; i < markers.length; i++) {
                    var markerProps = markers[i];
                    var markerLatLng = new gg.LatLng(markerProps['latitude'], markerProps['longitude']);
                    var markerOptions = {
                        position: markerLatLng,
                        map: map
                    };
                    if(markerProps['icon.url'] && markerProps['icon.url'].length > 0) it._configureImage('icon', markerProps, markerOptions);
                    else if (markerProps['icon'] && markerProps['icon'].length > 0) markerOptions['icon'] = markerProps['icon'];
                    else if (markerProps['color'] && markerProps['color'].length > 0) markerOptions['icon'] = 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=' + ((markerProps['label'] && markerProps['label'].length > 0) ? markerProps['label'] : '%E2%80%A2')+ '%7C' + markerProps['color'];
                    else if(markerProps['label'] && markerProps['label'].length > 0) markerOptions['icon'] = 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=' + markerProps['label'] + '%7CFE7569';
                    if(markerProps['shadow.url'] && markerProps['shadow.url'].length > 0) it._configureImage('shadow', markerProps, markerOptions);
                    else if(markerProps['shadow'] && markerProps['shadow'].length > 0) markerOptions['shadow'] = markerProps['shadow'];
                    for (j in markerProps) {
                        if (j.indexOf(".") < 0 && markerProps.hasOwnProperty(j) && !markerOptions.hasOwnProperty(j)) markerOptions[j] = markerProps[j];
                    }
                    var marker = new google.maps.Marker(markerOptions);
                    marker['info'] = it._createInfo(markerProps);
                    gg.event.addListener(marker, 'click', function() {
                        if(map.autocloseinfo && it.infowindow && it.infowindow.close) it.infowindow.close();
                        if(this['info']) {
                            it.infowindow = this['info'];
                            this['info'].open(map, this);
                        } else if (this['url'] && this['url'].length > 0) window.open(this['url'], this['target']);
                    });
                }
            }
            if(p) {
                for(var k=0; k<p.length; k++){
                    var props = p[k],o={},l=[],isPoly = false;
                    for(prop in props){
                        if(prop === 'locations' && props[prop]){
                            var loc = props[prop];
                            for(var j = 0; j<loc.length; j++) {
                                var latln = loc[j];
                                l.push(new google.maps.LatLng(latln['latitude'], latln['longitude']));
                            }
                        } else if (prop === 'isPolygon'){
                            isPoly= it._getBooleanValue(props[prop]);
                        } else if (prop === 'visible' || prop === 'editable' || prop === 'clickable' || prop === 'draggable' || prop === 'geodesic') {
                            o[prop]=it._getBooleanValue(props[prop]);
                        }else{o[prop] = props[prop];}
                    }
                    o['map']=map;
                    if(isPoly){
                        o['paths']=l;
                        new google.maps.Polygon(o);
                    } else {
                        o['path']=l;
                        new google.maps.Polyline(o);
                    }
                }
            }
        },
        _getBooleanValue: function(v){
            return (v === true || v === 'true');
        }
    }

    return Map;
});
