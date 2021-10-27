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
package net.sf.jasperreports.components.map;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalImageAlignEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalImageAlignEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.util.RendererUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class MapElementImageProvider {

    private static final Log log = LogFactory.getLog(MapElementImageProvider.class);

    /**
     * The character count limit for a static map URL request
     */
    public static Integer MAX_URL_LENGTH = 8192;

    /**
     * Local utility to facilitate de-duplication of repeated marker configurations in URL
     */
    private static class MarkerProperties {
        private String size;
        private String color;
        private String label;
        private String icon;

        public MarkerProperties(String size, String color, String label, String icon) {
            this.size = size;
            this.color = color;
            this.label = label;
            this.icon = icon;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MarkerProperties that = (MarkerProperties) o;
            return Objects.equals(size, that.size) && Objects.equals(color, that.color) &&
                    Objects.equals(label, that.label) && Objects.equals(icon, that.icon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(size, color, label, icon);
        }
    }

    // TODO: Make a PR on the main TIBCO repo for these changes, minus the logging
    @SuppressWarnings("unchecked")
    public static JRPrintImage getImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException {

        Renderable cacheRenderer = (Renderable) element.getParameterValue(MapComponent.PARAMETER_CACHE_RENDERER);

        OnErrorTypeEnum onErrorType =
                element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE) == null
                        ? MapComponent.DEFAULT_ON_ERROR_TYPE
                        : OnErrorTypeEnum.getByName((String) element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE));

        if (cacheRenderer == null) {

            // TODO: Add the markers more efficiently so that those with matching configuration are added to the
            //  same "markers" parameter.

            Float latitude = (Float) element.getParameterValue(MapComponent.ITEM_PROPERTY_latitude);
            latitude = latitude == null ? MapComponent.DEFAULT_LATITUDE : latitude;

            Float longitude = (Float) element.getParameterValue(MapComponent.ITEM_PROPERTY_longitude);
            longitude = longitude == null ? MapComponent.DEFAULT_LONGITUDE : longitude;

            Integer zoom = (Integer) element.getParameterValue(MapComponent.PARAMETER_ZOOM);
            zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;

            String mapType = (String) element.getParameterValue(MapComponent.ATTRIBUTE_MAP_TYPE);
            String mapScale = (String) element.getParameterValue(MapComponent.ATTRIBUTE_MAP_SCALE);
            String mapFormat = (String) element.getParameterValue(MapComponent.ATTRIBUTE_IMAGE_TYPE);
            String reqParams = (String) element.getParameterValue(MapComponent.PARAMETER_REQ_PARAMS);
            StringBuilder markers = new StringBuilder();

            List<Map<String, Object>> markerList =
                    (List<Map<String, Object>>) element.getParameterValue(MapComponent.PARAMETER_MARKERS);

            // Round to up to 5 d.p. which is approx. 1m precision. 6 d.p. is approx 10cm precision - too much info.
            // Keeping the URL short to support more data is more important than >1m precision.
            DecimalFormat decimalFormat = new DecimalFormat("#.#####");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);

            if (markerList != null && !markerList.isEmpty()) {
                landclanLog("markerList.size() = " + markerList.size());
                // Each unique marker configuration (other than lat/lon) can be passed as one parameter
                Map<MarkerProperties, List<String>> markerGroups = new HashMap<>();

                for (Map<String, Object> map : markerList) {
                    if (map != null && !map.isEmpty()) {
                        MarkerProperties markerProperties = new MarkerProperties(
                                (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_size),
                                (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_color),
                                (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_label),
                                map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url) != null
                                        ? (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url)
                                        : (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_icon));
                        List<String> groupLocations;
                        if (!markerGroups.containsKey(markerProperties)) {
                            groupLocations = new ArrayList<>();
                        } else {
                            groupLocations = markerGroups.get(markerProperties);
                        }
                        groupLocations.add(decimalFormat.format(map.get(MapComponent.ITEM_PROPERTY_latitude)) + "," +
                                decimalFormat.format(map.get(MapComponent.ITEM_PROPERTY_longitude)));
                        markerGroups.put(markerProperties, groupLocations);
                    }
                }

                String currentMarkers = "";
                for (Map.Entry<MarkerProperties, List<String>> markerGroup : markerGroups.entrySet()) {
                    currentMarkers = "&markers=";
                    String size = markerGroup.getKey().size;
                    currentMarkers += size != null && size.length() > 0 ? "size:" + size + "%7C" : "";
                    String color = markerGroup.getKey().color;
                    currentMarkers += color != null && color.length() > 0 ? "color:0x" + color + "%7C" : "";
                    String label = markerGroup.getKey().label;
                    currentMarkers += label != null && label.length() > 0 ? "label:" +
                            Character.toUpperCase(label.charAt(0)) + "%7C" : "";
                    String icon = markerGroup.getKey().icon;
                    if (icon != null && icon.length() > 0) {
                        currentMarkers += "icon:" + icon + "%7C";
                    }
                    currentMarkers += String.join("%7C", markerGroup.getValue());
                    markers.append(currentMarkers);
                }
            }

            List<Map<String, Object>> pathList = (List<Map<String, Object>>) element.getParameterValue(MapComponent.PARAMETER_PATHS);
            String currentPaths = "";
            if (pathList != null && !pathList.isEmpty()) {
                for (Map<String, Object> pathMap : pathList) {
                    if (pathMap != null && !pathMap.isEmpty()) {
                        currentPaths += "&path=";
                        String color = (String) pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeColor);
                        if (color != null && color.length() > 0) {
                            //adding opacity to color
                            color = JRColorUtil.getColorHexa(JRColorUtil.getColor(color, Color.BLACK));
                            color += pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity) == null || pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString().length() == 0
                                    ? "ff"
                                    : Integer.toHexString((int) (255 * Double.valueOf(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString())));
                        }
                        currentPaths += color != null && color.length() > 0 ? "color:0x" + color.toLowerCase() + "%7C" : "";
                        Boolean isPolygon = pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon) == null ? false : Boolean.valueOf(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon).toString());
                        if (isPolygon) {
                            String fillColor = (String) pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillColor);
                            if (fillColor != null && fillColor.length() > 0) {
                                //adding opacity to fill color
                                fillColor = JRColorUtil.getColorHexa(JRColorUtil.getColor(fillColor, Color.WHITE));
                                fillColor += pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity) == null || pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity).toString().length() == 0
                                        ? "00"
                                        : Integer.toHexString((int) (256 * Double.valueOf(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity).toString())));
                            }
                            currentPaths += fillColor != null && fillColor.length() > 0 ? "fillcolor:0x" + fillColor.toLowerCase() + "%7C" : "";
                        }
                        String weight = pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeWeight) == null ? null : pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeWeight).toString();
                        currentPaths += weight != null && weight.length() > 0 ? "weight:" + Integer.valueOf(weight) + "%7C" : "";
                        List<Map<String, Object>> locations = (List<Map<String, Object>>) pathMap.get(MapComponent.PARAMETER_PATH_LOCATIONS);
                        Map<String, Object> location = null;
                        if (locations != null && !locations.isEmpty()) {
                            for (int i = 0; i < locations.size(); i++) {
                                location = locations.get(i);
                                currentPaths += decimalFormat.format(location.get(MapComponent.ITEM_PROPERTY_latitude));
                                currentPaths += ",";
                                currentPaths += decimalFormat.format(location.get(MapComponent.ITEM_PROPERTY_longitude));
                                currentPaths += i < locations.size() - 1 ? "%7C" : "";
                            }
                            if (isPolygon) {
                                currentPaths += "%7C";
                                currentPaths += decimalFormat.format(locations.get(0).get(MapComponent.ITEM_PROPERTY_latitude));
                                currentPaths += ",";
                                currentPaths += decimalFormat.format(locations.get(0).get(MapComponent.ITEM_PROPERTY_longitude));
                            }
                        }
                    }
                }
            }

            String imageLocation = "https://maps.googleapis.com/maps/api/staticmap?";

            if (Math.abs(latitude) > 0.0001 && Math.abs(longitude) > 0.0001) {
                // Use normal positioning:
                imageLocation += "center="
                        + latitude
                        + ","
                        + longitude
                        + "&zoom="
                        + zoom
                        + "&";
            }

            imageLocation += "size="
                    + element.getWidth()
                    + "x"
                    + element.getHeight()
                    + (mapType == null ? "" : "&maptype=" + mapType)
                    + (mapFormat == null ? "" : "&format=" + mapFormat)
                    + (mapScale == null ? "" : "&scale=" + mapScale)
                    // Hide the POI markers (would be nice to have this supported upstream as a map component feature)
                    + "&style=feature:poi%7Cvisibility:off";
            String params = (reqParams == null || reqParams.trim().length() == 0 ? "" : "&" + reqParams);
            landclanLog("params = " + params);

            //a static map url is limited to 8192 characters
            imageLocation += imageLocation.length() + markers.length() + currentPaths.length() + params.length() < MAX_URL_LENGTH
                    ? markers + currentPaths + params
                    : imageLocation.length() + markers.length() + params.length() < MAX_URL_LENGTH ? markers + params : params;

            landclanLog("Requesting renderable from URL: " + imageLocation);
            cacheRenderer = RendererUtil.getInstance(jasperReportsContext).getNonLazyRenderable(imageLocation, onErrorType);
            if (cacheRenderer != null) {
                element.setParameterValue(MapComponent.PARAMETER_CACHE_RENDERER, cacheRenderer);
            }
        }

        JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());

        printImage.setUUID(element.getUUID());
        printImage.setX(element.getX());
        printImage.setY(element.getY());
        printImage.setWidth(element.getWidth());
        printImage.setHeight(element.getHeight());
        printImage.setStyle(element.getStyle());
        printImage.setMode(element.getModeValue());
        printImage.setBackcolor(element.getBackcolor());
        printImage.setForecolor(element.getForecolor());

        //FIXMEMAP there are no scale image and alignment attributes defined for the map element
        printImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
        printImage.setHorizontalImageAlign(HorizontalImageAlignEnum.LEFT);
        printImage.setVerticalImageAlign(VerticalImageAlignEnum.TOP);

        printImage.setOnErrorType(onErrorType);

        printImage.setRenderer(cacheRenderer);

        return printImage;
    }

    private static void landclanLog(String msg) {
        log.info("[" + Thread.currentThread().getName() + "] " + msg);
    }
}
