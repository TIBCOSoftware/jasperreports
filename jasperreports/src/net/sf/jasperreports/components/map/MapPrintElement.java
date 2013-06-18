/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * Utility class that creates generic print elements of map type.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class MapPrintElement
{
	/**
	 * The name of map generic elements.
	 */
	public static final String MAP_ELEMENT_NAME = "map";
	
	/**
	 * The qualified type of Flash generic elements. 
	 */
	public static final JRGenericElementType MAP_ELEMENT_TYPE = 
		new JRGenericElementType(JRXmlConstants.JASPERREPORTS_NAMESPACE, MAP_ELEMENT_NAME);
	
	/**
	 * The name of the parameter that provides the latitude.
	 */
	public static final String PARAMETER_LATITUDE = "latitude";
	
	/**
	 * The name of the parameter that provides the longitude.
	 */
	public static final String PARAMETER_LONGITUDE = "longitude";
	
	/**
	 * The name of the parameter that provides the zoom.
	 */
	public static final String PARAMETER_ZOOM = "zoom";
	
	/**
	 * The name of the parameter that provides the map type.
	 */
	public static final String PARAMETER_MAP_TYPE = "mapType";
	
	/**
	 * The name of the parameter that provides the map scale.
	 */
	public static final String PARAMETER_MAP_SCALE = "mapScale";
	
	/**
	 * The name of the parameter that provides the map image format.
	 */
	public static final String PARAMETER_IMAGE_TYPE = "imageType";
	
	/**
	 * The name of the parameter that provides the list of marker objects for the map.
	 */
	public static final String PARAMETER_MARKERS = "markers";
	
	/**
	 * The name of the parameter that provides the map language.
	 */
	public static final String PARAMETER_LANGUAGE = "language";
	
	/**
	 * The cached image renderer.
	 */
	public static final String PARAMETER_CACHE_RENDERER = "cacheRenderer";
	
	/**
	 * The name of the parameter that provides the marker icon url.
	 */
	public static final String PARAMETER_MARKER_ICON = "icon";
	
	/**
	 * The name of the parameter that provides the marker icon url.
	 */
	public static final String PARAMETER_MARKER_ICON_URL = "icon.url";
	
	/**
	 * The name of the parameter that provides the marker size.
	 */
	public static final String PARAMETER_MARKER_SIZE = "size";
	
	/**
	 * The name of the parameter that provides the marker color.
	 */
	public static final String PARAMETER_MARKER_COLOR = "color";
	
	/**
	 * The name of the parameter that provides the Google API map request parameters.
	 */
	public static final String PARAMETER_REQ_PARAMS = "reqParams";
	
	/**
	 *
	 */
	public static final Float DEFAULT_LATITUDE = 0f;
	public static final Float DEFAULT_LONGITUDE = 0f;
	public static final Integer DEFAULT_ZOOM = 0;
	public static final MapTypeEnum DEFAULT_MAP_TYPE = MapTypeEnum.ROADMAP;
	
//	/**
//	 * Creates a map element by copying all base element attributes
//	 * from a template instance.
//	 * 
//	 * @param template the element from which to copy base attributes
//	 * @param latitude the latitude for the map
//	 * @param longitude the longitude for the map
//	 * @param elementParameters additional parameters to be set on the Flash element.
//	 * Hyperlink objects need to be set as element parameters.
//	 * @return a map element
//	 */
//	public static JRGenericPrintElement makeMapElement(JRPrintElement template,
//			Float latitude, Float longitude, Integer zoom, Map<String,Object> elementParameters)
//	{
//		// TODO use JRTemplateGenericElement
//		JRBaseGenericPrintElement mapEl = new JRBaseGenericPrintElement(
//				template.getDefaultStyleProvider());
//		// copy all attribute from the template element
//		mapEl.setX(template.getX());
//		mapEl.setY(template.getY());
//		mapEl.setWidth(template.getWidth());
//		mapEl.setHeight(template.getHeight());
//		mapEl.setStyle(template.getStyle());
//		mapEl.setMode(template.getOwnModeValue());
//		mapEl.setBackcolor(template.getOwnBackcolor());
//		mapEl.setForecolor(template.getOwnForecolor());
//		mapEl.setOrigin(template.getOrigin());
//		mapEl.setKey(template.getKey());
//		
//		mapEl.setGenericType(MAP_ELEMENT_TYPE);
//		mapEl.setParameterValue(PARAMETER_LATITUDE, latitude);
//	mapEl.setParameterValue(PARAMETER_LONGITUDE, longitude);
//	mapEl.setParameterValue(PARAMETER_ZOOM, zoom);
//		
//		if (elementParameters != null && !elementParameters.isEmpty())
//		{
//			for (Iterator<Map.Entry<String,Object>> it = elementParameters.entrySet().iterator(); it.hasNext();)
//			{
//				Map.Entry<String,Object> entry = it.next();
//				String name = entry.getKey();
//				Object value = entry.getValue();
//				mapEl.setParameterValue(name, value);
//			}
//		}
//		
//		return mapEl;
//	}
	
	private MapPrintElement()
	{
	}
}
