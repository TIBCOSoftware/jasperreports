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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class MapElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final MapElementHtmlHandler INSTANCE = new MapElementHtmlHandler();
	
	private static final String RESOURCE_MAP_JS = "net/sf/jasperreports/components/map/resources/map.js";
	private static final String MAP_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/map/resources/templates/MapElementHtmlTemplate.vm";
	
	private static class CustomJRExporterParameter extends JRExporterParameter{

		protected CustomJRExporterParameter(String name) {
			super(name);
		}
	}
	
	private CustomJRExporterParameter param = new MapElementHtmlHandler.CustomJRExporterParameter("exporter_first_attempt");

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

		String mapType = (String)element.getParameterValue(MapPrintElement.PARAMETER_MAP_TYPE);
		mapType = (mapType == null ? MapPrintElement.DEFAULT_MAP_TYPE.getName() : mapType).toUpperCase();
		
		String language = (String)element.getParameterValue(MapPrintElement.PARAMETER_LANGUAGE);

		List<Map<String,Object>> markerList = (List<Map<String,Object>>)element.getParameterValue(MapPrintElement.PARAMETER_MARKERS);
		
		Map<String, Object> contextMap = new HashMap<String, Object>();
		ReportContext reportContext = context.getExporter().getReportContext();

		if (reportContext != null)
		{
			contextMap.put("resourceMapJs", WebUtil.getInstance(context.getJasperReportsContext()).getResourcePath(MapElementHtmlHandler.RESOURCE_MAP_JS));
		}
		contextMap.put("mapCanvasId", "map_canvas_" + element.hashCode());
		contextMap.put("gotReportContext", reportContext != null);
		contextMap.put("latitude", latitude);
		contextMap.put("longitude", longitude);
		contextMap.put("zoom", zoom);
		contextMap.put("mapType", mapType);
//		contextMap.put("markerList", markers);
		String markers = markerList == null || markerList.isEmpty() ? "[]" : JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(markerList);
		contextMap.put("markerList", markers);
		if(language != null)
		{
			contextMap.put("language", language);
		}

//		velocityContext.put("divId", element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.id"));
//		velocityContext.put("divClass", element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.class"));
		if (context.getExporter() instanceof JRXhtmlExporter)
		{
			contextMap.put("xhtml", "xhtml");
			contextMap.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			contextMap.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
		}
		else if (context.getExporter() instanceof JRHtmlExporter)
		{
			contextMap.put("elementX", ((JRHtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			contextMap.put("elementY", ((JRHtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
		}
		else
		{
			contextMap.put("elementX", ((HtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			contextMap.put("elementY", ((HtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
		}
		contextMap.put("elementWidth", element.getWidth());
		contextMap.put("elementHeight", element.getHeight());
		
		if (!(context.getExportParameters().containsKey(param))) {
			context.getExportParameters().put(param, true);
			
			//FIXME: support for parametrized http://maps.google.com/maps/api/js script (see MapElementHtmlTemplate.vm)
			contextMap.put("exporterFirstAttempt", true);
		}
		
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			contextMap.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
		}
		return VelocityUtil.processTemplate(MAP_ELEMENT_HTML_TEMPLATE, contextMap);
	}

	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
}
