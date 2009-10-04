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

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 *
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;

import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.ExporterNature;
import net.sf.jasperreports.engine.export.JRExporterGridCell;


/**
 * Exports a JasperReports document to Open Document Spreadsheet format. It has character output type
 * and exports the document to a grid-based layout.
 *
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JROdsExporter extends JROpenDocumentExporter
{

	public JROdsExporter()
	{
		super();
		exporterPropertiesPrefix = JROpenDocumentExporter.ODS_EXPORTER_PROPERTIES_PREFIX;
	}


	/**
	 *
	 * @see net.sf.jasperreports.engine.export.oasis.JROpenDocumentExporter#getExporterNature(net.sf.jasperreports.engine.export.ExporterFilter)
	 */
	protected ExporterNature getExporterNature(ExporterFilter filter) {
		return new JROdsExporterNature(filter);
	}

	/**
	 *
	 */
	protected void exportLine(TableBuilder tableBuilder, JRPrintLine line, JRExporterGridCell gridCell) throws IOException//FIXMEODS check line drawing
	{
		tableBuilder.buildCellHeader(null, gridCell.getColSpan(), gridCell.getRowSpan());

		double x1, y1, x2, y2;

		if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
		{
			x1 = Utility.translatePixelsToInches(0);
			y1 = Utility.translatePixelsToInches(0);
			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
			y2 = Utility.translatePixelsToInches(line.getHeight() - 1);
		}
		else
		{
			x1 = Utility.translatePixelsToInches(0);
			y1 = Utility.translatePixelsToInches(line.getHeight() - 1);
			x2 = Utility.translatePixelsToInches(line.getWidth() - 1);
			y2 = Utility.translatePixelsToInches(0);
		}

		tempBodyWriter.write("<text:p>");
		insertPageAnchor();
//		tempBodyWriter.write(
//				"<draw:line text:anchor-type=\"paragraph\" "
//				+ "draw:style-name=\"" + styleCache.getGraphicStyle(line) + "\" "
//				+ "svg:x1=\"" + x1 + "in\" "
//				+ "svg:y1=\"" + y1 + "in\" "
//				+ "svg:x2=\"" + x2 + "in\" "
//				+ "svg:y2=\"" + y2 + "in\">"
//				//+ "</draw:line>"
//				+ "<text:p/></draw:line>"
//				);
		tempBodyWriter.write("</text:p>");
		tableBuilder.buildCellFooter();
	}


	/**
	 *
	 */
	protected void exportEllipse(TableBuilder tableBuilder, JRPrintEllipse ellipse, JRExporterGridCell gridCell) throws IOException
	{
		tableBuilder.buildCellHeader(null, gridCell.getColSpan(), gridCell.getRowSpan());
		tempBodyWriter.write("<text:p>");
		insertPageAnchor();
//		tempBodyWriter.write(
//			"<draw:ellipse text:anchor-type=\"paragraph\" "
//			+ "draw:style-name=\"" + styleCache.getGraphicStyle(ellipse) + "\" "
//			+ "svg:width=\"" + Utility.translatePixelsToInches(ellipse.getWidth()) + "in\" "
//			+ "svg:height=\"" + Utility.translatePixelsToInches(ellipse.getHeight()) + "in\" "
//			+ "svg:x=\"0in\" "
//			+ "svg:y=\"0in\">"
//			+ "<text:p/></draw:ellipse>"
//			);
		tempBodyWriter.write("</text:p>");
		tableBuilder.buildCellFooter();
	}


	protected void exportAnchor(String anchorName) throws IOException //FIXMEODS check anchors
	{
	}
	
	
}

