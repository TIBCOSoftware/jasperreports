/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.components.map.imageprovider;

import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.renderers.Renderable;
import net.sf.jasperreports.renderers.util.RendererUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Color;
import java.util.*;

/**
 * @author Sanda Zaharia (shertage@users.sourceforge.net)
 */
public class MapElementImageProvider extends AbstractMapElementImageProvider
{
	private static final Log log = LogFactory.getLog(MapElementImageProvider.class);

	/**
	 * The character count limit for a static map URL request
	 */
	public static Integer MAX_URL_LENGTH = 8192;

	@Override
	protected Renderable createRenderable(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException
	{
		Renderable cachedRenderable = (Renderable)element.getParameterValue(MapComponent.PARAMETER_CACHE_RENDERER);

		if (cachedRenderable == null)
		{
			OnErrorTypeEnum onErrorType =
					element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE) == null
							? MapComponent.DEFAULT_ON_ERROR_TYPE
							: OnErrorTypeEnum.getByName((String)element.getParameterValue(MapComponent.PARAMETER_ON_ERROR_TYPE));

			String imageLocation = getImageLocation(element);

			cachedRenderable = RendererUtil.getInstance(jasperReportsContext).getNonLazyRenderable(imageLocation, onErrorType);
			element.setParameterValue(MapComponent.PARAMETER_CACHE_RENDERER, cachedRenderable);
		}

		return cachedRenderable;
	}
	protected String getImageLocation(JRGenericPrintElement element) {
		Float latitude = (Float)element.getParameterValue(MapComponent.ITEM_PROPERTY_latitude);
		latitude = latitude == null ? MapComponent.DEFAULT_LATITUDE : latitude;

		Float longitude = (Float)element.getParameterValue(MapComponent.ITEM_PROPERTY_longitude);
		longitude = longitude == null ? MapComponent.DEFAULT_LONGITUDE : longitude;

		Integer zoom = (Integer)element.getParameterValue(MapComponent.PARAMETER_ZOOM);
		zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;

		String mapType = (String)element.getParameterValue(MapComponent.ATTRIBUTE_MAP_TYPE);
		String mapScale = (String)element.getParameterValue(MapComponent.ATTRIBUTE_MAP_SCALE);
		String mapFormat = (String)element.getParameterValue(MapComponent.ATTRIBUTE_IMAGE_TYPE);
		String reqParams = (String)element.getParameterValue(MapComponent.PARAMETER_REQ_PARAMS);
		String markers ="";

		List<Map<String,Object>> markerList = prepareMarkerList(element);
		if(!markerList.isEmpty())
		{
			String currentMarkers = "";
			for(Map<String,Object> map : markerList)
			{
				if(map != null && !map.isEmpty())
				{
					currentMarkers = "&markers=";
					String size = (String)map.get(MapComponent.ITEM_PROPERTY_MARKER_size);
					currentMarkers += size != null && size.length() > 0 ? "size:" + size + "%7C" : "";
					String color = (String)map.get(MapComponent.ITEM_PROPERTY_MARKER_color);
					currentMarkers += color != null && color.length() > 0 ? "color:0x" + color + "%7C" : "";
					String label = (String)map.get(MapComponent.ITEM_PROPERTY_MARKER_label);
					currentMarkers += label != null && label.length() > 0 ? "label:" + Character.toUpperCase(label.charAt(0)) + "%7C" : "";
					String icon = map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url) != null 
							? (String)map.get(MapComponent.ITEM_PROPERTY_MARKER_ICON_url) 
							: (String)map.get(MapComponent.ITEM_PROPERTY_MARKER_icon);
					if(icon != null && icon.length() > 0)
					{
						currentMarkers +="icon:" + icon + "%7C";
					}
					currentMarkers +=map.get(MapComponent.ITEM_PROPERTY_latitude);
					currentMarkers +=",";
					currentMarkers +=map.get(MapComponent.ITEM_PROPERTY_longitude);
					markers += currentMarkers;
				}
			}
		}

		List<Map<String,Object>> pathList = (List<Map<String,Object>>)element.getParameterValue(MapComponent.PARAMETER_PATHS);
		String currentPaths = "";
		if(pathList != null && !pathList.isEmpty())
		{
			for(Map<String,Object> pathMap : pathList)
			{
				if(pathMap != null && !pathMap.isEmpty())
				{
					currentPaths += "&path=";
					String color = (String)pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeColor);
					if(color != null && color.length() > 0){
						//adding opacity to color
						color = JRColorUtil.getColorHexa(JRColorUtil.getColor(color, Color.BLACK));
						color += pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity) == null || pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString().length() == 0
								? "ff"
								: Integer.toHexString((int) (255 * Double.valueOf(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_strokeOpacity).toString())));
					}
					currentPaths += color != null && color.length() > 0 ? "color:0x" + color.toLowerCase() + "%7C" : "";
					Boolean isPolygon = pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon) == null ? false : Boolean.valueOf(pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_isPolygon).toString());
					if(isPolygon){
						String fillColor = (String)pathMap.get(MapComponent.ITEM_PROPERTY_STYLE_fillColor);
						if(fillColor != null && fillColor.length() > 0){
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
					List<Map<String,Object>> locations = (List<Map<String,Object>>)pathMap.get(MapComponent.PARAMETER_PATH_LOCATIONS);
					Map<String,Object> location = null;
					if(locations != null && !locations.isEmpty()) {
						for(int i = 0; i < locations.size(); i++) {
							location = locations.get(i);
							currentPaths += location.get(MapComponent.ITEM_PROPERTY_latitude);
							currentPaths += ",";
							currentPaths += location.get(MapComponent.ITEM_PROPERTY_longitude);
							currentPaths += i < locations.size() - 1 ? "%7C":"";
						}
						if(isPolygon){
							currentPaths += "%7C";
							currentPaths += locations.get(0).get(MapComponent.ITEM_PROPERTY_latitude);
							currentPaths += ",";
							currentPaths += locations.get(0).get(MapComponent.ITEM_PROPERTY_longitude);
						}
					}
				}
			}
		}

		String imageLocation = 
			"https://maps.googleapis.com/maps/api/staticmap?center=" 
			+ latitude 
			+ "," 
			+ longitude 
			+ "&size=" 
			+ element.getWidth() 
			+ "x" 
			+ element.getHeight() 
			+ "&zoom="
			+ zoom
			+ (mapType == null ? "" : "&maptype=" + mapType)
			+ (mapFormat == null ? "" : "&format=" + mapFormat)
			+ (mapScale == null ? "" : "&scale=" + mapScale);
		String params = (reqParams == null || reqParams.trim().length() == 0 ? "" : "&" + reqParams);

		//a static map url is limited to 2048 characters
		imageLocation += imageLocation.length() + markers.length() + currentPaths.length() + params.length() < MAX_URL_LENGTH
				? markers + currentPaths + params
				: imageLocation.length() + markers.length() + params.length() < MAX_URL_LENGTH ? markers + params : params;

		if (log.isTraceEnabled()) {
			log.trace("Produced map imageLocation: " + imageLocation);
		}

		return imageLocation;
	}

	List<Map<String, Object>> prepareMarkerList(JRGenericPrintElement element)
	{
		Map<String, Object> markerSeries = (Map<String, Object>)element.getParameterValue(MapComponent.PARAMETER_MARKERS);
		List<Map<String, Object>> markerList = new ArrayList<>();
		if (markerSeries != null && !markerSeries.isEmpty())
		{
			for (Object seriesConfig: markerSeries.values())
			{
				Map<String, Object> markerSingleSeriesConfiguration = (Map<String, Object>) seriesConfig;
				if (markerSingleSeriesConfiguration.containsKey(MapComponent.PARAMETER_MARKERS))
				{
					List<Map<String, Object>> markerSeriesItems =
							(List<Map<String, Object>>) markerSingleSeriesConfiguration.get(
									MapComponent.PARAMETER_MARKERS);
					markerList.addAll(markerSeriesItems);
				}
			}
		}

		return markerList;
	}

}
