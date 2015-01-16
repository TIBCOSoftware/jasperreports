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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.Writer;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.type.OrientationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DocxDocumentHelper extends BaseHelper
{
	protected static int DEFAULT_LINE_PITCH = 360;
	/**
	 * 
	 */
	public DocxDocumentHelper(JasperReportsContext jasperReportsContext, Writer writer)
	{
		super(jasperReportsContext, writer);
	}
	
	/**
	 *
	 */
	public void exportHeader()
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<w:document\n");
		write(" xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"\n");
		write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"\n");
		write(" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"\n");
		write(" xmlns:v=\"urn:schemas-microsoft-com:vml\"\n");
		write(" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"\n");
		write(" xmlns:w10=\"urn:schemas-microsoft-com:office:word\"\n");
		write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"\n");
		write(" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"\n");
		write(" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"\n");
		write(" xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n"); 
		write(" <w:body>\n");
	}

	/**
	 *
	 */
	public void exportSection(PrintPageFormat pageFormat, boolean lastPage)
	{
		if (!lastPage)
		{
			write("    <w:p>\n");
			write("    <w:pPr>\n");
		}
		write("  <w:sectPr>\n");
		write("   <w:pgSz w:w=\"" + LengthUtil.twip(pageFormat.getPageWidth()) + "\" w:h=\"" + LengthUtil.twip(pageFormat.getPageHeight()) + "\"");
		write(" w:orient=\"" + (pageFormat.getOrientation() == OrientationEnum.LANDSCAPE ? "landscape" : "portrait") + "\"");
		write("/>\n");
		//the height of the first row and the width of the first and last column should be at least 1; see how are they calculated in DocxTableHelper
		int top = pageFormat.getTopMargin() != null ? Math.max(LengthUtil.twip(pageFormat.getTopMargin() - 1), 0) : 0;
		int left = pageFormat.getLeftMargin() != null ? Math.max(LengthUtil.twip(pageFormat.getLeftMargin() - 1), 0) : 0;
		int right = pageFormat.getRightMargin() != null ? Math.max(LengthUtil.twip(pageFormat.getRightMargin() - 1), 0) : 0;
		int bottom = pageFormat.getBottomMargin() != null && pageFormat.getBottomMargin() > 0? Math.max(LengthUtil.twip(pageFormat.getBottomMargin()) - DEFAULT_LINE_PITCH, 20) : 0;

		write("   <w:pgMar w:top=\"" + top + "\" w:right=\"" + right + "\" w:bottom=\"" + bottom + "\" w:left=\"" + left + "\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" />\n");
//		write("   <w:cols w:space=\"720\" />\n");
		write("   <w:docGrid w:linePitch=\"" + DEFAULT_LINE_PITCH + "\" />\n");
		write("  </w:sectPr>\n");
		if (!lastPage)
		{
			write("    </w:pPr>\n");
			write("    </w:p>\n");
		}
	}
	/**
	 *
	 */
	public void exportFooter()
	{
		write(" </w:body>\n");
		write("</w:document>\n");
	}

}
