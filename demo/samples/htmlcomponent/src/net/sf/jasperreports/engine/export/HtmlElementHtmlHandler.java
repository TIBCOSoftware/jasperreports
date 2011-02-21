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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id$
 */
public class HtmlElementHtmlHandler implements GenericElementHtmlHandler
{
	public String getHtmlFragment(JRHtmlExporterContext exporterContext,
			JRGenericPrintElement element)
	{
		StringBuffer script = new StringBuffer(128);
		String htmlContent = (String) element.getParameterValue(HtmlPrintElement.PARAMETER_HTML_CONTENT);
		
		script.append("<div style='width:" + (element.getWidth() - 0) + "px;height:" + (element.getHeight() - 0) + "px;");
		
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			script.append("background-color: #");
			script.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			script.append("; ");
		}
		script.append("overflow:hidden;'>");
		script.append(htmlContent);
		script.append("</div>");

		return script.toString();
	}

	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
	
}
