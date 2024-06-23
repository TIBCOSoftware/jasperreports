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
package net.sf.jasperreports.components.map;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.velocity.util.VelocityUtil;

import org.apache.velocity.VelocityContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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

	@Override
	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
        ReportContext reportContext = context.getExporterRef().getReportContext();
		Map<String, Object> contextMap = new HashMap<>();

        contextMap.put("mapCanvasId", "map_canvas_" + element.hashCode());
        contextMap.put("elementWidth", element.getWidth());
        contextMap.put("elementHeight", element.getHeight());

        if (element.getMode() == ModeEnum.OPAQUE)
        {
            contextMap.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
        }

        contextMap.put("gotReportContext", reportContext != null);

        if (reportContext == null)
        {
            contextMap.put("jasperreportsMapApiScript", VelocityUtil.processTemplate(MapUtils.MAP_API_SCRIPT, (VelocityContext) null));
            MapUtils.prepareContextForVelocityTemplate(contextMap, context.getJasperReportsContext(), element);

            if (context.getValue(FIRST_ATTEMPT_PARAM) == null)
            {
                context.setValue(FIRST_ATTEMPT_PARAM, true);

                //FIXME: support for parametrized http://maps.google.com/maps/api/js script (see MapElementHtmlTemplate.vm)
                contextMap.put("exporterFirstAttempt", true);
            }
        }
		
		return VelocityUtil.processTemplate(MAP_ELEMENT_HTML_TEMPLATE, contextMap);
	}

	@Override
	public boolean toExport(JRGenericPrintElement element)
    {
		return true;
	}
}
