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
package net.sf.jasperreports.ohloh;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;

/**
 * @deprecated To be removed.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class OhlohWidgetXhtmlHandler extends OhlohWidgetHtmlHandler
{

	
	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		StringBuffer script = new StringBuffer(128);
		
		script.append("<div");
		String dataAttr = ((net.sf.jasperreports.engine.export.JRXhtmlExporter)context.getExporter()).getDataAttributes(element);
		if (dataAttr != null)
		{
			script.append(dataAttr);
		}
		
		StringBuffer styleBuffer = new StringBuffer();
		appendPositionStyle(context, element.getX(), element.getY(), styleBuffer);
		appendSizeStyle(context, element.getWidth(), element.getHeight(), styleBuffer);
		appendBackcolorStyle(element, styleBuffer);
		
		if (styleBuffer.length() > 0)
		{
			script.append(" style=\"");
			script.append(styleBuffer.toString());
			script.append("\"");
		}

		script.append(">");
		script.append(super.getHtmlFragment(context, element));
		script.append("</div>\n");
		
		return script.toString();
	}
	
	protected void appendPositionStyle(JRHtmlExporterContext context, int x, int y, StringBuffer styleBuffer)
	{
		styleBuffer.append("position:absolute;");
		styleBuffer.append("left:");
		styleBuffer.append(toSizeUnit(context, x));
		styleBuffer.append(";");
		styleBuffer.append("top:");
		styleBuffer.append(toSizeUnit(context, y));
		styleBuffer.append(";");
	}


	protected void appendBackcolorStyle(JRPrintElement element, StringBuffer styleBuffer)
	{
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			styleBuffer.append("background-color:#");
			styleBuffer.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			styleBuffer.append(";");
		}
	}

	protected void appendSizeStyle(JRHtmlExporterContext context, int width, int height, StringBuffer styleBuffer)
	{
		styleBuffer.append("width:");
		styleBuffer.append(toSizeUnit(context, width));
		styleBuffer.append(";");

		styleBuffer.append("height:");
		styleBuffer.append(toSizeUnit(context, height));
		styleBuffer.append(";");
	}
	
	protected String toSizeUnit(JRHtmlExporterContext context, int size)
	{
		return ((net.sf.jasperreports.engine.export.JRXhtmlExporter)context.getExporter()).toSizeUnit(size);
	}

	
	
}
