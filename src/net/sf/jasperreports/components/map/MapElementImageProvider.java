/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
			+ (mapScale == null ? "" : "&scale=" + mapScale)
			+ "&sensor=false";
		
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
		
		//FIXMEMAP there are no scale image, alignment and onError attributes defined for the map element
		printImage.setScaleImage(ScaleImageEnum.CLIP);
		printImage.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		printImage.setVerticalAlignment(VerticalAlignEnum.TOP);
		
		Renderable cacheRenderer = (Renderable)element.getParameterValue(MapPrintElement.PARAMETER_CACHE_RENDERER);

		if(cacheRenderer == null)
		{
			cacheRenderer = RenderableUtil.getInstance(jasperReportsContext).getRenderable(imageLocation, OnErrorTypeEnum.ERROR, false);
			cacheRenderer.getImageData(jasperReportsContext);
			element.setParameterValue(MapPrintElement.PARAMETER_CACHE_RENDERER, cacheRenderer);
		}

		printImage.setRenderable(cacheRenderer);
		
		return printImage;
	}
}
