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
package net.sf.jasperreports.components.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.HtmlReportConfiguration;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.VelocityUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class MapElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final MapElementHtmlHandler INSTANCE = new MapElementHtmlHandler();
	
	private static final String MAP_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/map/resources/templates/MapElementHtmlTemplate.vm";
	
	private static final String FIRST_ATTEMPT_PARAM = "exporter_first_attempt";

	public static MapElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
        ReportContext reportContext = context.getExporterRef().getReportContext();
		Map<String, Object> contextMap = new HashMap<String, Object>();

        contextMap.put("mapCanvasId", "map_canvas_" + element.hashCode());

        Exporter<ExporterInput, ? extends HtmlReportConfiguration, ? extends HtmlExporterConfiguration, HtmlExporterOutput> exporter = context.getExporterRef();
        HtmlExporter htmlExporter2 = exporter instanceof HtmlExporter ? (HtmlExporter)exporter : null;
        if (htmlExporter2 == null)
        {
            xhtmlExport(exporter, contextMap, element);
        }
        else
        {
            contextMap.put("elementX", htmlExporter2.toSizeUnit((float)element.getX()));
            contextMap.put("elementY", htmlExporter2.toSizeUnit((float)element.getY()));
        }

        contextMap.put("elementWidth", element.getWidth());
        contextMap.put("elementHeight", element.getHeight());

        if (element.getModeValue() == ModeEnum.OPAQUE)
        {
            contextMap.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
        }

        contextMap.put("gotReportContext", reportContext != null);

        if (reportContext == null)
        {
            Float latitude = (Float)element.getParameterValue(MapComponent.ITEM_PROPERTY_latitude);
            latitude = latitude == null ? MapComponent.DEFAULT_LATITUDE : latitude;

            Float longitude = (Float)element.getParameterValue(MapComponent.ITEM_PROPERTY_longitude);
            longitude = longitude == null ? MapComponent.DEFAULT_LONGITUDE : longitude;

            Integer zoom = (Integer)element.getParameterValue(MapComponent.PARAMETER_ZOOM);
            zoom = zoom == null ? MapComponent.DEFAULT_ZOOM : zoom;

            String mapType = (String)element.getParameterValue(MapComponent.ATTRIBUTE_MAP_TYPE);
            mapType = (mapType == null ? MapComponent.DEFAULT_MAP_TYPE.getName() : mapType).toUpperCase();

            contextMap.put("latitude", latitude);
            contextMap.put("longitude", longitude);
            contextMap.put("zoom", zoom);
            contextMap.put("mapType", mapType);

            List<Map<String,Object>> markerList = (List<Map<String,Object>>)element.getParameterValue(MapComponent.PARAMETER_MARKERS);
            String markers = markerList == null || markerList.isEmpty() ? "[]" : JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(markerList);
            contextMap.put("markerList", markers);

            List<Map<String,Object>> pathList = (List<Map<String,Object>>)element.getParameterValue(MapComponent.PARAMETER_PATHS);
            String paths = pathList == null || pathList.isEmpty() ? "[]" : JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(pathList);
            contextMap.put("pathsList", paths);

            String reqParams = (String)element.getParameterValue(MapComponent.PARAMETER_REQ_PARAMS);
            if(reqParams != null)
            {
                contextMap.put(MapComponent.PARAMETER_REQ_PARAMS, reqParams);
            }

            if (context.getValue(FIRST_ATTEMPT_PARAM) == null)
            {
                context.setValue(FIRST_ATTEMPT_PARAM, true);

                //FIXME: support for parametrized http://maps.google.com/maps/api/js script (see MapElementHtmlTemplate.vm)
                contextMap.put("exporterFirstAttempt", true);
            }
        }
		
		return VelocityUtil.processTemplate(MAP_ELEMENT_HTML_TEMPLATE, contextMap);
	}

	public boolean toExport(JRGenericPrintElement element)
    {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void xhtmlExport(
		Exporter<ExporterInput, ? extends HtmlReportConfiguration, ? extends HtmlExporterConfiguration, HtmlExporterOutput> exporter,
		Map<String, Object> contextMap,
		JRGenericPrintElement element
		)
	{
		net.sf.jasperreports.engine.export.JRXhtmlExporter xhtmlExporter = 
			exporter instanceof net.sf.jasperreports.engine.export.JRXhtmlExporter 
			? (net.sf.jasperreports.engine.export.JRXhtmlExporter)exporter 
			: null; 
		if (xhtmlExporter == null)
		{
			net.sf.jasperreports.engine.export.JRHtmlExporter htmlExporter = 
				exporter instanceof net.sf.jasperreports.engine.export.JRHtmlExporter 
				? (net.sf.jasperreports.engine.export.JRHtmlExporter)exporter 
				: null; 
			contextMap.put("elementX", htmlExporter.toSizeUnit(element.getX()));
			contextMap.put("elementY", htmlExporter.toSizeUnit(element.getY()));
		}
		else
		{
			contextMap.put("xhtml", "xhtml");
			contextMap.put("elementX", xhtmlExporter.toSizeUnit(element.getX()));
			contextMap.put("elementY", xhtmlExporter.toSizeUnit(element.getY()));
		}
	}
}
