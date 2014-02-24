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

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.web.util.JacksonUtil;
import net.sf.jasperreports.web.util.VelocityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class MapElementJsonHandler implements GenericElementJsonHandler
{
	private static final MapElementJsonHandler INSTANCE = new MapElementJsonHandler();

	private static final String MAP_ELEMENT_JSON_TEMPLATE = "net/sf/jasperreports/components/map/resources/templates/MapElementJsonTemplate.vm";

	public static MapElementJsonHandler getInstance()
	{
		return INSTANCE;
	}

	public String getJsonFragment(JsonExporterContext context, JRGenericPrintElement element)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("mapCanvasId", "map_canvas_" + element.hashCode());

        Float latitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LATITUDE);
        latitude = latitude == null ? MapPrintElement.DEFAULT_LATITUDE : latitude;

        Float longitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LONGITUDE);
        longitude = longitude == null ? MapPrintElement.DEFAULT_LONGITUDE : longitude;

        Integer zoom = (Integer)element.getParameterValue(MapPrintElement.PARAMETER_ZOOM);
        zoom = zoom == null ? MapPrintElement.DEFAULT_ZOOM : zoom;

        String mapType = (String)element.getParameterValue(MapPrintElement.PARAMETER_MAP_TYPE);
        mapType = (mapType == null ? MapPrintElement.DEFAULT_MAP_TYPE.getName() : mapType).toUpperCase();

        contextMap.put("latitude", latitude);
        contextMap.put("longitude", longitude);
        contextMap.put("zoom", zoom);
        contextMap.put("mapType", mapType);

        List<Map<String,Object>> markerList = (List<Map<String,Object>>)element.getParameterValue(MapPrintElement.PARAMETER_MARKERS);
        String markers = markerList == null || markerList.isEmpty() ? "[]" : JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(markerList);
        contextMap.put("markerList", markers);

        List<Map<String,Object>> pathList = (List<Map<String,Object>>)element.getParameterValue(MapPrintElement.PARAMETER_PATHS);
        String paths = pathList == null || pathList.isEmpty() ? "[]" : JacksonUtil.getInstance(context.getJasperReportsContext()).getJsonString(pathList);
        contextMap.put("pathsList", paths);

        String reqParams = (String)element.getParameterValue(MapPrintElement.PARAMETER_REQ_PARAMS);
        if(reqParams != null)
        {
            contextMap.put(MapPrintElement.PARAMETER_REQ_PARAMS, reqParams);
        }

		return VelocityUtil.processTemplate(MAP_ELEMENT_JSON_TEMPLATE, contextMap);
	}

	public boolean toExport(JRGenericPrintElement element)
    {
		return true;
	}

}
