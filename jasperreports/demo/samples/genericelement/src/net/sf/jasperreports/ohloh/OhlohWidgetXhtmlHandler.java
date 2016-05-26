/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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
		StringBuilder script = new StringBuilder(128);
		
		script.append("<div");
		String dataAttr = ((net.sf.jasperreports.engine.export.JRXhtmlExporter)context.getExporter()).getDataAttributes(element);
		if (dataAttr != null)
		{
			script.append(dataAttr);
		}
		
		StringBuilder styleBuilder = new StringBuilder();
		appendPositionStyle(context, element.getX(), element.getY(), styleBuilder);
		appendSizeStyle(context, element.getWidth(), element.getHeight(), styleBuilder);
		appendBackcolorStyle(element, styleBuilder);
		
		if (styleBuilder.length() > 0)
		{
			script.append(" style=\"");
			script.append(styleBuilder.toString());
			script.append("\"");
		}

		script.append(">");
		script.append(super.getHtmlFragment(context, element));
		script.append("</div>\n");
		
		return script.toString();
	}
	
	protected void appendPositionStyle(JRHtmlExporterContext context, int x, int y, StringBuilder styleBuilder)
	{
		styleBuilder.append("position:absolute;");
		styleBuilder.append("left:");
		styleBuilder.append(toSizeUnit(context, x));
		styleBuilder.append(";");
		styleBuilder.append("top:");
		styleBuilder.append(toSizeUnit(context, y));
		styleBuilder.append(";");
	}


	protected void appendBackcolorStyle(JRPrintElement element, StringBuilder styleBuilder)
	{
		if (element.getModeValue() == ModeEnum.OPAQUE)
		{
			styleBuilder.append("background-color:#");
			styleBuilder.append(JRColorUtil.getColorHexa(element.getBackcolor()));
			styleBuilder.append(";");
		}
	}

	protected void appendSizeStyle(JRHtmlExporterContext context, int width, int height, StringBuilder styleBuilder)
	{
		styleBuilder.append("width:");
		styleBuilder.append(toSizeUnit(context, width));
		styleBuilder.append(";");

		styleBuilder.append("height:");
		styleBuilder.append(toSizeUnit(context, height));
		styleBuilder.append(";");
	}
	
	protected String toSizeUnit(JRHtmlExporterContext context, int size)
	{
		return ((net.sf.jasperreports.engine.export.JRXhtmlExporter)context.getExporter()).toSizeUnit(size);
	}

	
	
}
