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
    // LandClan added: Logger
    private static final Log log = LogFactory.getLog(MapElementImageProvider.class);

    /**
     * The character count limit for a static map URL request
     */
    // LandClan removed:
    // public static Integer MAX_URL_LENGTH = 2048;
    // LandClan added: Updated to 8192 in accordance with current documentation.
    // See https://developers.google.com/maps/documentation/maps-static/start#url-size-restriction
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

    public static JRPrintImage getImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException {

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
        String markers = "";

        List<Map<String, Object>> markerList = (List<Map<String, Object>>) element.getParameterValue(MapComponent.PARAMETER_MARKERS);
        // TODO: Add the markers more efficiently so that those with matching configuration are added to the
        //  same "markers" parameter.

        Renderable cacheRenderer = (Renderable) element.getParameterValue(MapComponent.PARAMETER_CACHE_RENDERER);

        OnErrorTypeEnum onErrorType =
                element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE) == null
                        ? MapComponent.DEFAULT_ON_ERROR_TYPE
                        : OnErrorTypeEnum.getByName((String) element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE));

        if (cacheRenderer == null) {

            if (markerList != null && !markerList.isEmpty()) {
                // LandClan: Added logging
                landclanLog("markerList.size() = " + markerList.size());
                String currentMarkers = "";
                for (Map<String, Object> map : markerList) {
                    if (map != null && !map.isEmpty()) {
                        currentMarkers = "&markers=";
                        String size = (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_size);
                        currentMarkers += size != null && size.length() > 0 ? "size:" + size + "%7C" : "";
                        String color = (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_color);
                        currentMarkers += color != null && color.length() > 0 ? "color:0x" + color + "%7C" : "";
                        String label = (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_label);
                        currentMarkers += label != null && label.length() > 0 ? "label:" + Character.toUpperCase(label.charAt(0)) + "%7C" : "";
                        String icon = map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url) != null
                                ? (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url)
                                : (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_icon);
                        if (icon != null && icon.length() > 0) {
                            currentMarkers += "icon:" + icon + "%7C";
                        }
                        currentMarkers += map.get(MapComponent.ITEM_PROPERTY_latitude);
                        currentMarkers += ",";
                        currentMarkers += map.get(MapComponent.ITEM_PROPERTY_longitude);
                        markers += currentMarkers;
                    }
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
                                currentPaths += location.get(MapComponent.ITEM_PROPERTY_latitude);
                                currentPaths += ",";
                                currentPaths += location.get(MapComponent.ITEM_PROPERTY_longitude);
                                currentPaths += i < locations.size() - 1 ? "%7C" : "";
                            }
                            if (isPolygon) {
                                currentPaths += "%7C";
                                currentPaths += locations.get(0).get(MapComponent.ITEM_PROPERTY_latitude);
                                currentPaths += ",";
                                currentPaths += locations.get(0).get(MapComponent.ITEM_PROPERTY_longitude);
                            }
                        }
                    }
                }
            }

            // LandClan: Updated - added logic to support implicit positioning
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
                    + (mapScale == null ? "" : "&scale=" + mapScale);
            String params = (reqParams == null || reqParams.trim().length() == 0 ? "" : "&" + reqParams);

            //a static map url is limited to 8192 characters
            imageLocation += imageLocation.length() + markers.length() + currentPaths.length() + params.length() < MAX_URL_LENGTH
                    ? markers + currentPaths + params
                    : imageLocation.length() + markers.length() + params.length() < MAX_URL_LENGTH ? markers + params : params;

            // LandClan added: logging of URL
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

    // LandClan added logging with Thread identification
    private static void landclanLog(String msg) {
        log.info("[" + Thread.currentThread().getName() + "] " + msg);
    }

}
