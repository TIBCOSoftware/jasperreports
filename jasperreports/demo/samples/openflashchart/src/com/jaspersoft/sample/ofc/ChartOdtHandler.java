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
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.export.ElementGridCell;
import net.sf.jasperreports.engine.export.ElementWrapper;
import net.sf.jasperreports.engine.export.JRExporterGridCell;
import net.sf.jasperreports.engine.export.oasis.GenericElementOdtHandler;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporterContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: ChartPdfHandler.java 3031 2009-08-27 11:14:57Z teodord $
 */
public class ChartOdtHandler implements GenericElementOdtHandler
{
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
	}
	
	public void exportElement(
		JROdtExporterContext exporterContext,
		JRGenericPrintElement element,
		JRExporterGridCell gridCell
		)
	{
		JROdtExporter exporter = (JROdtExporter)exporterContext.getExporter();
		
		JRBasePrintText text = new JRBasePrintText(exporterContext.getExportedReport().getDefaultStyleProvider());
		text.setText("[Open Flash Chart Component]");
		text.setMode(JRElement.MODE_OPAQUE);
		text.setBackcolor(Color.lightGray);
		text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
		text.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		text.getLineBox().getPen().setLineWidth(1f);
		text.getLineBox().getPen().setLineColor(Color.black);
		text.getLineBox().getPen().setLineStyle(JRPen.LINE_STYLE_DASHED);
		
		JRExporterGridCell newGridCell = 
			new ElementGridCell(
				new ElementWrapper(text, null, null), 
				gridCell.getWidth(), 
				gridCell.getHeight(),
				gridCell.getColSpan(),
				gridCell.getRowSpan()
				);
		
		newGridCell.setBox(text.getLineBox());
		
		try
		{
			exporter.exportText(exporterContext.getTableBuilder(), text, newGridCell);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
}
