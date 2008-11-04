/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package com.jaspersoft.sample.ofc;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class ChartHtmlHandler implements GenericElementHtmlHandler
{

	public static final String PARAMETER_CHART_DATA = "ChartData";
	
	private final ThreadLocal lastContext = new ThreadLocal();
	
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}

	public String getHtmlFragment(JRHtmlExporterContext exporterContext, JRGenericPrintElement element)
	{
		String divID = "ofc" + System.identityHashCode(element);
	    int width = element.getWidth();
	    int height = element.getHeight();
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div id=\"");
		sb.append(divID);
		sb.append("\"></div>\n");
		
		if (!sameAsLast(exporterContext))
		{
			sb.append("<script language=\"JavaScript\" src=\"openflashchart/swfobject.js\"></script>\n");
		}
		
		sb.append("<script language=\"JavaScript\">\n");
		sb.append("swfobject.embedSWF(\"openflashchart/open-flash-chart.swf\", \"");
		sb.append(divID);
		sb.append("\",\"");
		sb.append(width);
		sb.append("\", \"");
		sb.append(height);
		sb.append("\", \"9.0.0\", \"openflashchart/expressInstall.swf\",{\"get-data\":\"open_flash_chart_data");
		sb.append(divID);
		sb.append("\"});\n");
		sb.append("function open_flash_chart_data");
		sb.append(divID);
		sb.append("()\n");
		sb.append("{return '");
		String chartData = (String) element.getParameterValue(PARAMETER_CHART_DATA);
		sb.append(chartData);
		sb.append("';}\n");
		sb.append("</script>\n");
		
		return sb.toString();
	}

	protected boolean sameAsLast(JRHtmlExporterContext exporterContext)
	{
		Reference lastRef = (Reference) lastContext.get();
		JRHtmlExporterContext last = (JRHtmlExporterContext) (lastRef == null ? null : lastRef.get());
		if (last != null && last.equals(exporterContext))
		{
			return true;
		}
		
		WeakReference ref = new WeakReference(exporterContext);
		lastContext.set(ref);
		return false;
	}
}
