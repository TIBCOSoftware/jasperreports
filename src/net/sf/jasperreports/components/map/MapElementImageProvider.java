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

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class MapElementImageProvider
{
	/**
	 * The character count limit for a static map URL request
	 */
	public static Integer MAX_URL_LENGTH = 2048;
	
	/**
	 * @deprecated Replaced by {@link #getImage(JasperReportsContext, JRGenericPrintElement)}.
	 */
	public static JRPrintImage getImage(JRGenericPrintElement element) throws JRException
	{
		return getImage(DefaultJasperReportsContext.getInstance(), element);
	}
		
	public static JRPrintImage getImage(JasperReportsContext jasperReportsContext, JRGenericPrintElement element) throws JRException
	{
		
		Float latitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LATITUDE);
		latitude = latitude == null ? MapPrintElement.DEFAULT_LATITUDE : latitude;

		Float longitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LONGITUDE);
		longitude = longitude == null ? MapPrintElement.DEFAULT_LONGITUDE : longitude;
		
		Integer zoom = (Integer)element.getParameterValue(MapPrintElement.PARAMETER_ZOOM);
		zoom = zoom == null ? MapPrintElement.DEFAULT_ZOOM : zoom;

		String mapType = (String)element.getParameterValue(MapPrintElement.PARAMETER_MAP_TYPE);
		String mapScale = (String)element.getParameterValue(MapPrintElement.PARAMETER_MAP_SCALE);
		String mapFormat = (String)element.getParameterValue(MapPrintElement.PARAMETER_IMAGE_TYPE);
		String reqParams = (String)element.getParameterValue(MapPrintElement.PARAMETER_REQ_PARAMS);
		String markers ="";
		
		List<Map<String,Object>> markerList = (List<Map<String,Object>>)element.getParameterValue(MapPrintElement.PARAMETER_MARKERS);
		if(markerList != null && !markerList.isEmpty())
		{
			String currentMarkers = "";
			for(Map<String,Object> map : markerList)
			{
				if(map != null && !map.isEmpty())
				{
					currentMarkers = "&markers=";
					String size = (String)map.get(MapPrintElement.PARAMETER_MARKER_SIZE);
					currentMarkers += size != null && size.length() > 0 ? "size:" + size + "%7C" : "";
					String color = (String)map.get(MapPrintElement.PARAMETER_MARKER_COLOR);
					currentMarkers += color != null && color.length() > 0 ? "color:0x" + color + "%7C" : "";
					String label = (String)map.get(MapPrintElement.PARAMETER_MARKER_LABEL);
					currentMarkers += label != null && label.length() > 0 ? "label:" + label.charAt(0) + "%7C" : "";
					String icon = map.get(MapPrintElement.PARAMETER_MARKER_ICON_URL) != null 
							? (String)map.get(MapPrintElement.PARAMETER_MARKER_ICON_URL) 
							: (String)map.get(MapPrintElement.PARAMETER_MARKER_ICON);
					if(icon != null && icon.length() > 0)
					{
						currentMarkers +="icon:" + icon + "%7C";
					}
					currentMarkers +=map.get(MapPrintElement.PARAMETER_LATITUDE);
					currentMarkers +=",";
					currentMarkers +=map.get(MapPrintElement.PARAMETER_LONGITUDE);
					markers += currentMarkers;
				}
			}
		}

		String imageLocation = 
			"http://maps.google.com/maps/api/staticmap?center=" 
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
		String params = "&sensor=false" + reqParams;

		//a static map url is limited to 2048 characters
		imageLocation += (imageLocation.length() + markers.length() + params.length() < MAX_URL_LENGTH) ? markers + params : params;
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
		printImage.setLazy(false);
		
		//FIXMEMAP there are no scale image and alignment attributes defined for the map element
		printImage.setScaleImage(ScaleImageEnum.RETAIN_SHAPE);
		printImage.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		printImage.setVerticalAlignment(VerticalAlignEnum.TOP);
		
		Renderable cacheRenderer = (Renderable)element.getParameterValue(MapPrintElement.PARAMETER_CACHE_RENDERER);

		OnErrorTypeEnum onErrorType = element.getParameterValue(MapPrintElement.PARAMETER_ON_ERROR_TYPE) == null 
				? MapPrintElement.DEFAULT_ON_ERROR_TYPE  
				: OnErrorTypeEnum.getByName((String)element.getParameterValue(MapPrintElement.PARAMETER_ON_ERROR_TYPE));
		printImage.setOnErrorType(onErrorType);
		
		if(cacheRenderer == null)
		{
			cacheRenderer = RenderableUtil.getInstance(jasperReportsContext).getRenderable(imageLocation, onErrorType, false);
			if(cacheRenderer != null){
				cacheRenderer.getImageData(jasperReportsContext);
				element.setParameterValue(MapPrintElement.PARAMETER_CACHE_RENDERER, cacheRenderer);
			}
		}

		printImage.setRenderable(cacheRenderer);
		
		return printImage;
	}
	
}
