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
package net.sf.jasperreports.components.iconlabel;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.export.GenericElementCsvHandler;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterContext;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class IconLabelElementCsvHandler implements GenericElementCsvHandler
{
	private static final IconLabelElementCsvHandler INSTANCE = new IconLabelElementCsvHandler();
	
	public static IconLabelElementCsvHandler getInstance()
	{
		return INSTANCE;
	}

	public String getTextValue(JRCsvExporterContext exporterContext, JRGenericPrintElement element)
	{
		JRPrintText labelPrintText = (JRPrintText)element.getParameterValue(IconLabelElement.PARAMETER_LABEL_TEXT_ELEMENT);
		if (labelPrintText == null)
		{
			return null;
		}
		
		String text = null;
		
		JRStyledText styledText = ((JRCsvExporter)exporterContext.getExporterRef()).getStyledText(labelPrintText);
		if (styledText == null)
		{
			text = "";
		}
		else
		{
			text = styledText.getText();
		}
		
		return text;
	}

	public boolean toExport(JRGenericPrintElement element) 
	{
		return true;
	}
	
}
