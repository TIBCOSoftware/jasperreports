/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class MapElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final MapElementHtmlHandler INSTANCE = new MapElementHtmlHandler();
	
	public static MapElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		Float latitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LATITUDE);
		Float longitude = (Float)element.getParameterValue(MapPrintElement.PARAMETER_LONGITUDE);
		
		String elementX = ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX());
		String elementY = ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY());
//		int elementWidth = element.getWidth();
//		int elementHeight = element.getY();
		
		StringBuffer script = new StringBuffer(128);
		script.append("<script type=\"text/javascript\" src=\"http://maps.google.com/maps/api/js?sensor=false\"></script>");
		script.append("<div id=\"" + element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.id") 
				+ "\" class=\"" + element.getPropertiesMap().getProperty("net.sf.jasperreports.export.html.class") 
//				+ "\" style='padding-left:10px;padding-top:10px;width:" + (element.getWidth() - 0) + "px;height:" + (element.getHeight() - 0) + "px;'>");
				+ "\" style='position: absolute;left: " + elementX + ";top:" + elementY + ";width:" + (element.getWidth() - 0) + "px;height:" + (element.getHeight() - 0) + "px;");
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			script.append("background-color: #");
			script.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			script.append("; ");
		}
		script.append("'>");
		//script.append("<div id=\"map_canvas\" style=\"width:" + (element.getWidth() - 20) + "px;height:" + (element.getHeight() - 20) + "px;\"></div>" +
		//script.append("<div id=\"map_canvas\" style=\"padding:10px;width:100%;height:100%;\"></div>" +
		script.append("<div id=\"map_canvas\" style=\"width:100%;height:100%;overflow:auto\"></div>");
		script.append("</div>");
		script.append("<script type=\"text/javascript\">");
		script.append("    var latlng = new google.maps.LatLng(" + latitude + ", " + longitude + ");");
		script.append("    var myOptions = {");
		script.append("      zoom: 8,");
		script.append("      center: latlng,");
		script.append("      mapTypeId: google.maps.MapTypeId.ROADMAP");
		script.append("    };");
		script.append("    var map = new google.maps.Map(document.getElementById(\"map_canvas\"),");
		script.append("        myOptions);");
		script.append("</script>");
		return script.toString();
	}

	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
}
