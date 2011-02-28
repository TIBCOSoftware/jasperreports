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
package net.sf.jasperreports.components.ofc;

import java.awt.Color;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ChartHtmlHandler.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class ChartHtmlHandler implements GenericElementHtmlHandler
{

	public static final String PARAMETER_CHART_DATA = "ChartData";

	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}

	public String getHtmlFragment(JRHtmlExporterContext exporterContext, JRGenericPrintElement element)
	{
		String divID = "ofc" + System.identityHashCode(element);
		int width = element.getWidth();
		int height = element.getHeight();
		Color backcolor = element.getBackcolor();
		String swfLocation = "openflashchart/open-flash-chart.swf";
		String chartData = (String) element.getParameterValue(PARAMETER_CHART_DATA);
		String chartEncodedData = JRStringUtil.htmlEncode(chartData);

		return 
			"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"" +
				width +
				"\" height=\"" +
				height +
				"\" id=\"" +
				divID +
				"\" align=\"middle\"><param name=\"allowScriptAccess\" value=\"sameDomain\" /> <param name=\"movie\" value=\"" +
				swfLocation +
				"?width=" +
				width +
				"&height=" +
				height +
				"&inline_data=" +
				chartEncodedData +
				"\" /> <param name=\"quality\" value=\"high\" /><param name=\"bgcolor\" value=\"" +
				"#" + JRColorUtil.getColorHexa(backcolor) +
				"\" /> <embed src=\"" +
				swfLocation +
				"?width=" +
				width +
				"&height=" +
				height +
				"&inline_data=" +
				chartEncodedData +
				"\" quality=\"high\" bgcolor=\"" +
				"#" + JRColorUtil.getColorHexa(backcolor) +
				"\" width=\"" +
				width +
				"\" height=\"" +
				height +
				"\" name=\"open-flash-chart\" align=\"middle\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /> </object> ";
	}
}
