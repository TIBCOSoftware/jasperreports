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

import java.io.IOException;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementXmlHandler;
import net.sf.jasperreports.engine.export.JRXml4SwfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartPdfHandler.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class ChartXml4SwfHandler extends BaseChartHandler implements GenericElementXmlHandler
{
	public void exportElement(
		JRXmlExporterContext exporterContext,
		JRGenericPrintElement element
		)
	{
		JRXml4SwfExporter exporter = (JRXml4SwfExporter)exporterContext.getExporter();
		
		JRPrintText text = getTextElementReplacement(exporterContext, element);
		
		try
		{
			exporter.exportText(text);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
