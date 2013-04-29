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
package net.sf.jasperreports.components.iconlabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class IconLabelElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final IconLabelElementHtmlHandler INSTANCE = new IconLabelElementHtmlHandler();
	
	public static IconLabelElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		JRPrintText labelPrintText = (JRPrintText)element.getParameterValue(IconLabelElement.PARAMETER_LABEL_TEXT_ELEMENT);
		JRGenericPrintElement iconGenericElement = (JRGenericPrintElement)element.getParameterValue(IconLabelElement.PARAMETER_ICON_GENERIC_ELEMENT);
//		JRPrintText iconPrintText = (JRPrintText)iconGenericElement.getParameterValue("iconTextElement");

		List<JRPrintElement> elements = new ArrayList<JRPrintElement>();
		elements.add(labelPrintText);
//		elements.add(iconPrintText);
		elements.add(iconGenericElement);
		
		HtmlExporter exporter = (HtmlExporter)context.getExporter();
		
		try
		{
			exporter.exportElements(elements);
			
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return "";
	}

	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
}
