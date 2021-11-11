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

    private static final String PIPE = "%7C";

    /**
     * The character count limit for a static map URL request
     */
    public static final int  MAX_URL_LENGTH = 8192;

    /**
     * Zoom-style constants. This is a work around for the jasperreports schema not supporting map style
     * parameter.
     */
    // Environmental style that shows natural features and attractions with transport icons hidden.
    public static final int ZOOM_STYLE_ENVIRONMENT = 30;
    public static final String STYLE_ENVIRONMENT = "&style=feature:poi%7Cvisibility:off&style=feature:poi.attraction%7Cvisibility:on&style=feature:poi.park%7Cvisibility:on&style=feature:transit%7Celement:labels.icon%7Cvisibility:off";
    // Flood style that shows natural features with transport icons hidden.
    public static final int ZOOM_STYLE_FLOOD = 31;
    public static final String STYLE_FLOOD = "&style=feature:poi%7Cvisibility:off&style=feature:poi.park%7Cvisibility:on&style=feature:transit%7Celement:labels.icon%7Cvisibility:off";

    /**
     * Shared class to abstract URL creation to allow applications to determine in advance whether the URL limit will
     * be hit by pre-checking the URL length.
     */
    public static class MapImageUrlArgs {
        private final Float latitude;
        private final Float longitude;
        private final Integer zoom;

        private final String mapType;
        private final String mapScale;
        private final String mapFormat;
        private final String reqParams;

        private final List<Map<String, Object>> markerList;
        private final List<Map<String, Object>> pathList;

        private final int elementWidth;
        private final int elementHeight;

        public MapImageUrlArgs(Float latitude, Float longitude, Integer zoom, String mapType, String mapScale, String mapFormat, String reqParams, List<Map<String, Object>> markerList, List<Map<String, Object>> pathList, int elementWidth, int elementHeight) {
            this.latitude = latitude == null ? MapComponent.DEFAULT_LATITUDE : latitude;
            this.longitude = longitude == null ? MapComponent.DEFAULT_LONGITUDE : longitude;
            this.zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;
            this.mapType = mapType;
            this.mapScale = mapScale;
            this.mapFormat = mapFormat;
            this.reqParams = reqParams;
            this.markerList = markerList;
            this.pathList = pathList;
            this.elementWidth = elementWidth;
            this.elementHeight = elementHeight;
        }

        public Float getLatitude() {
            return latitude;
        }

        public Float getLongitude() {
            return longitude;
        }

        public Integer getZoom() {
            return zoom;
        }

        public String getMapType() {
            return mapType;
        }

        public String getMapScale() {
            return mapScale;
        }

        public String getMapFormat() {
            return mapFormat;
        }

        public String getReqParams() {
            return reqParams;
        }

        public List<Map<String, Object>> getMarkerList() {
            return markerList;
        }

        public List<Map<String, Object>> getPathList() {
            return pathList;
        }

        public int getElementWidth() {
            return elementWidth;
        }

        public int getElementHeight() {
            return elementHeight;
        }
    }

    /**
     * Local utility to facilitate de-duplication of repeated marker configurations in URL
     */
    private static class MarkerProperties {

        // See anchor values at https://developers.google.com/maps/documentation/maps-static/start#CustomIcons
        private static final String ANCHOR_TOP = "top";
        private static final String ANCHOR_LEFT = "left";
        private static final String ANCHOR_RIGHT = "right";
        private static final String ANCHOR_CENTER = "center";
        private static final String ANCHOR_TOP_LEFT = "topleft";
        private static final String ANCHOR_TOP_RIGHT = "topright";
        private static final String ANCHOR_BOTTOM_LEFT = "bottomleft";
        private static final String ANCHOR_BOTTOM_RIGHT = "bottomright";
        private static final String ANCHOR_BOTTOM = null; // null because it is the default, though the docs allow 'bottom'.

        private final String size;
        private final String color;
        private final String label;
        private final String icon;
        private final String anchor;

        public MarkerProperties(String size, String color, String label, String icon, Integer anchorX, Integer anchorY) {
            this.size = size;
            this.color = color;
            this.label = label;
            this.icon = icon;
            this.anchor = determineAnchor(anchorX, anchorY);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MarkerProperties that = (MarkerProperties) o;
            return Objects.equals(size, that.size) && Objects.equals(color, that.color) &&
                    Objects.equals(label, that.label) && Objects.equals(icon, that.icon) &&
                    Objects.equals(anchor, that.anchor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(size, color, label, icon, anchor);
        }

        /**
         * LandClan custom implementation to enable support for custom marker 'anchor' property.
         * (The official Jasper Reports CE exposes Anchor X and Anchor Y but does not use them.).
         * The integer values x and y translate into the anchor strings as per the following diagram:
         *
         * -1, 1    0, 1    1, 1  =>  topleft       top                 topright
         *
         * -1, 0    0, 0    1, 0  =>  left          center              right
         *
         * -1,-1    0,-1    1,-1  =>  bottomleft    (default = null)    bottomright
         * @param x
         * @param y
         * @return
         */
        private String determineAnchor(Integer x, Integer y) {
            if ((x == null || y == null) || (x == 0 && y == -1)) {
                return null;
            }
            if (x == -1) {
                return y == 1 ? ANCHOR_TOP_LEFT : y == 0 ? ANCHOR_LEFT : ANCHOR_BOTTOM_LEFT;
            } else if (x == 0) {
                return y == 1 ? ANCHOR_TOP : y == 0 ? ANCHOR_CENTER : ANCHOR_BOTTOM;
            } else if (x == 1) {
                return y == 1 ? ANCHOR_TOP_RIGHT : y == 0 ? ANCHOR_RIGHT : ANCHOR_BOTTOM_RIGHT;
            }
            // Unrecognised values
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static JRPrintImage getImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException {

        Renderable cacheRenderer = (Renderable) element.getParameterValue(MapComponent.PARAMETER_CACHE_RENDERER);

        OnErrorTypeEnum onErrorType =
                element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE) == null
                        ? MapComponent.DEFAULT_ON_ERROR_TYPE
                        : OnErrorTypeEnum.getByName((String) element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE));

        if (cacheRenderer == null) {

            MapImageUrlArgs args = new MapImageUrlArgs(
                    (Float) element.getParameterValue(MapComponent.ITEM_PROPERTY_latitude),
                    (Float) element.getParameterValue(MapComponent.ITEM_PROPERTY_longitude),
                    (Integer) element.getParameterValue(MapComponent.PARAMETER_ZOOM),
                    (String) element.getParameterValue(MapComponent.ATTRIBUTE_MAP_TYPE),
                    (String) element.getParameterValue(MapComponent.ATTRIBUTE_MAP_SCALE),
                    (String) element.getParameterValue(MapComponent.ATTRIBUTE_IMAGE_TYPE),
                    (String) element.getParameterValue(MapComponent.PARAMETER_REQ_PARAMS),
                    (List<Map<String, Object>>) element.getParameterValue(MapComponent.PARAMETER_MARKERS),
                    (List<Map<String, Object>>) element.getParameterValue(MapComponent.PARAMETER_PATHS),
                    element.getWidth(), element.getHeight());

            String imageLocation = buildMapImageUrl(args, true);
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

    @SuppressWarnings("unchecked")
    public static String buildMapImageUrl(MapImageUrlArgs args, boolean keepWithinMaxUrlLength) {
        Float latitude = args.getLatitude();
        latitude = latitude == null ? MapComponent.DEFAULT_LATITUDE : latitude;

        Float longitude = args.getLongitude();
        longitude = longitude == null ? MapComponent.DEFAULT_LONGITUDE : longitude;

        Integer zoom = args.getZoom();
        zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;

        String mapType = args.getMapType();
        String mapScale = args.getMapScale();
        String mapFormat = args.getMapFormat();
        String reqParams = args.getReqParams();
        StringBuilder markers = new StringBuilder();

        List<Map<String, Object>> markerList = args.getMarkerList();

        // Round to up to 5 d.p. which is approx. 1m precision. 6 d.p. is approx 10cm precision - too much info.
        // Keeping the URL short to support more data is more important than >1m precision.
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        if (markerList != null && !markerList.isEmpty()) {
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
                                    : (String) map.get(MapComponent.ITEM_PROPERTY_MARKER_icon),
                            (Integer) map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_ANCHOR_x),
                            (Integer) map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_ANCHOR_y));
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
                String anchor=markerGroup.getKey().anchor;
                currentMarkers += anchor != null ? "anchor:" + anchor + PIPE : "";
                String size = markerGroup.getKey().size;
                currentMarkers += size != null && size.length() > 0 ? "size:" + size + PIPE : "";
                String color = markerGroup.getKey().color;
                currentMarkers += color != null && color.length() > 0 ? "color:0x" + color + PIPE : "";
                String label = markerGroup.getKey().label;
                currentMarkers += label != null && label.length() > 0 ? "label:" +
                        Character.toUpperCase(label.charAt(0)) + PIPE : "";
                String icon = markerGroup.getKey().icon;
                if (icon != null && icon.length() > 0) {
                    currentMarkers += "icon:" + icon + PIPE;
                }
                currentMarkers += String.join(PIPE, markerGroup.getValue());
                markers.append(currentMarkers);
            }
        }

        List<Map<String, Object>> pathList = args.getPathList();
        StringBuilder currentPaths = new StringBuilder();
        if (pathList != null && !pathList.isEmpty()) {
            for (Map<String, Object> pathMap : pathList) {
                if (pathMap != null && !pathMap.isEmpty()) {
                    currentPaths.append("&path=");
                    String color = (String) pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeColor);
                    if (color != null && color.length() > 0) {
                        //adding opacity to color
                        color = JRColorUtil.getColorHexa(JRColorUtil.getColor(color, Color.BLACK));
                        color += pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity) == null ||
                                pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString().length() == 0
                                ? "ff"
                                : Integer.toHexString((int) (255 * Double.parseDouble(
                                        pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString())));
                    }
                    currentPaths.append(color != null && color.length() > 0 ? "color:0x" + color.toLowerCase() + PIPE : "");
                    boolean isPolygon = pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon) != null &&
                            Boolean.parseBoolean(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon).toString());
                    if (isPolygon) {
                        String fillColor = (String) pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillColor);
                        if (fillColor != null && fillColor.length() > 0) {
                            //adding opacity to fill color
                            fillColor = JRColorUtil.getColorHexa(JRColorUtil.getColor(fillColor, Color.WHITE));
                            fillColor += pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity) == null || pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity).toString().length() == 0
                                    ? "00"
                                    : Integer.toHexString((int) (256 * Double.parseDouble(
                                            pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillOpacity).toString())));
                        }
                        currentPaths.append(fillColor != null && fillColor.length() > 0 ? "fillcolor:0x" + fillColor.toLowerCase() + PIPE : "");
                    }
                    String weight = pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeWeight) == null ? null :
                            pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeWeight).toString();
                    currentPaths.append(weight != null && weight.length() > 0 ? "weight:" + Integer.valueOf(weight) + PIPE : "");
                    List<Map<String, Object>> locations = (List<Map<String, Object>>) pathMap.get(MapComponent.PARAMETER_PATH_LOCATIONS);
                    Map<String, Object> location;
                    if (locations != null && !locations.isEmpty()) {
                        for (int i = 0; i < locations.size(); i++) {
                            location = locations.get(i);
                            currentPaths.append(decimalFormat.format(location.get(MapComponent.ITEM_PROPERTY_latitude)));
                            currentPaths.append(",");
                            currentPaths.append(decimalFormat.format(location.get(MapComponent.ITEM_PROPERTY_longitude)));
                            currentPaths.append(i < locations.size() - 1 ? PIPE : "");
                        }
                        if (isPolygon) {
                            currentPaths.append(PIPE);
                            currentPaths.append(decimalFormat.format(locations.get(0).get(MapComponent.ITEM_PROPERTY_latitude)));
                            currentPaths.append(",");
                            currentPaths.append(decimalFormat.format(locations.get(0).get(MapComponent.ITEM_PROPERTY_longitude)));
                        }
                    }
                }
            }
        }

        String imageLocation = "https://maps.googleapis.com/maps/api/staticmap?";
        // Hide the POI markers by default (would be nice to have this supported upstream as a map component feature)
        String styles = "&style=feature:poi%7Cvisibility:off";

        if (Math.abs(latitude) > 0.0001 && Math.abs(longitude) > 0.0001) {
            // Use normal positioning:
            imageLocation += "center="
                    + latitude
                    + ","
                    + longitude
                    + "&zoom="
                    + zoom
                    + "&";
        } else {
            String zoomStyle = determineStylesFromZoomUpperRegister(zoom);
            if (zoomStyle != null) {
                styles = zoomStyle;
            }
        }

        imageLocation += "size="
                + args.getElementWidth()
                + "x"
                + args.getElementHeight()
                + (mapType == null ? "" : "&maptype=" + mapType)
                + (mapFormat == null ? "" : "&format=" + mapFormat)
                + (mapScale == null ? "" : "&scale=" + mapScale)
                + styles;
        String params = (reqParams == null || reqParams.trim().length() == 0 ? "" : "&" + reqParams);

        if (!keepWithinMaxUrlLength) {
            // To check if all the data can be supported by the URL. Can use this response to
            // re-structure data and potentially split the data across multiple maps.
            return markers + currentPaths.toString() + params;
        }
        //a static map url is limited to 8192 characters
        imageLocation += imageLocation.length() + markers.length() + currentPaths.length() + params.length() < MAX_URL_LENGTH
                ? markers + currentPaths.toString() + params
                : imageLocation.length() + markers.length() + params.length() < MAX_URL_LENGTH ? markers + params : params;

        return imageLocation;
    }

    /**
     * This is a hack to expose support for map styles which are not exposed by the Jasper Reports public xsd and
     * we don't have time right now to re-write the xml integration of Jaspersoft Studio. Zoom greater than 21 or so
     * makes no sense with google maps. We therefore start arbitrarily at 30.
     * @param zoom
     * @return
     */
    private static String determineStylesFromZoomUpperRegister(int zoom) {

        if (zoom < 30) {
            return null;
        }

        switch (zoom) {
            case ZOOM_STYLE_ENVIRONMENT:
                return STYLE_ENVIRONMENT;
            case ZOOM_STYLE_FLOOD:
                return STYLE_FLOOD;
            default:
                return null;
        }
    }

    private static void landclanLog(String msg) {
        log.info("[" + Thread.currentThread().getName() + "] " + msg);
    }
}
