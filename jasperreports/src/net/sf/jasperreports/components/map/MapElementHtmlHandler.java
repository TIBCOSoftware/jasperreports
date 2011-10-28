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

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.web.servlets.ResourceServlet;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.velocity.VelocityContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class MapElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final MapElementHtmlHandler INSTANCE = new MapElementHtmlHandler();
	
	private static final String RESOURCE_MAP_JS = "net/sf/jasperreports/components/map/resources/map.js";
	private static final String MAP_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/map/resources/templates/MapElementHtmlTemplate.vm";
	
	public static MapElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		Float latitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LATITUDE);
		latitude = latitude == null ? MapPrintElement.DEFAULT_LATITUDE : latitude;

		Float longitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LONGITUDE);
		longitude = longitude == null ? MapPrintElement.DEFAULT_LONGITUDE : longitude;
		
		Integer zoom = (Integer)element.getParameterValue(MapPrintElement.PARAMETER_ZOOM);
		zoom = zoom == null ? MapPrintElement.DEFAULT_ZOOM : zoom;

		VelocityContext velocityContext = new VelocityContext();
		ReportContext reportContext = context.getExporter().getReportContext();
		if (reportContext != null)
		{
			String webResourcesBasePath = JRProperties.getProperty("net.sf.jasperreports.web.resources.base.path");
			if (webResourcesBasePath == null)
			{
				webResourcesBasePath = ResourceServlet.DEFAULT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=";
			}
			velocityContext.put("resourceMapJs", webResourcesBasePath + MapElementHtmlHandler.RESOURCE_MAP_JS);
		}
		velocityContext.put("gotReportContext", reportContext != null);
		velocityContext.put("latitude", latitude);
		velocityContext.put("longitude", longitude);
		velocityContext.put("zoom", zoom);
//		velocityContext.put("divId", element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.id"));
//		velocityContext.put("divClass", element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.class"));
		if(context.getExporter() instanceof JRXhtmlExporter)
		{
			velocityContext.put("xhtml", "xhtml");
			velocityContext.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
		}
		else
		{
			velocityContext.put("elementX", ((JRHtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRHtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
		}
		velocityContext.put("elementWidth", element.getWidth());
		velocityContext.put("elementHeight", element.getHeight());
		
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
		}
		return VelocityUtil.processTemplate(MAP_ELEMENT_HTML_TEMPLATE, velocityContext);
	}

	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
}
