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
package com.jaspersoft.sample.ofc;

import java.awt.Color;
import java.io.IOException;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.convert.TextElementConverter;
import net.sf.jasperreports.engine.export.GenericElementRtfHandler;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporterContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartPdfHandler.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class ChartRtfHandler implements GenericElementRtfHandler
{
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
	
	public void exportElement(
		JRRtfExporterContext exporterContext,
		JRGenericPrintElement element
		)
	{
		JRRtfExporter exporter = (JRRtfExporter)exporterContext.getExporter();
		
		JRBasePrintText text = new JRBasePrintText(exporterContext.getExportedReport().getDefaultStyleProvider());
		text.setX(element.getX());
		text.setY(element.getY());
		text.setWidth(element.getWidth());
		text.setHeight(element.getHeight());
		text.setTextHeight(element.getHeight());//FIXMEHANDLER maybe use text measurer
		text.setText("[Open Flash Chart Component]");
		text.setMode(JRElement.MODE_OPAQUE);
		text.setBackcolor(Color.lightGray);
		text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
		text.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		text.getLineBox().getPen().setLineWidth(1f);
		text.getLineBox().getPen().setLineColor(Color.black);
		text.getLineBox().getPen().setLineStyle(JRPen.LINE_STYLE_DASHED);
		
		TextElementConverter.measureTextElement(text);
		
		try
		{
			exporter.exportText(text);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
