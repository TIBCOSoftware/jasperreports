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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class DocxDocumentHelper
{
	/**
	 *
	 */
	public static void exportHeader(Writer writer) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<w:document\n");
		writer.write(" xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"\n");
		writer.write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
		writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"\n");
		writer.write(" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"\n");
		writer.write(" xmlns:v=\"urn:schemas-microsoft-com:vml\"\n");
		writer.write(" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"\n");
		writer.write(" xmlns:w10=\"urn:schemas-microsoft-com:office:word\"\n");
		writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"\n");
		writer.write(" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"\n");
		writer.write(" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"\n");
		writer.write(" xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">\n"); 
		writer.write(" <w:body>\n");
	}
	

	/**
	 *
	 */
	public static void exportFooter(JasperPrint jasperPrint, Writer writer) throws IOException
	{
		writer.write("  <w:sectPr>\n");
		writer.write("   <w:pgSz w:w=\"" + Utility.twip(jasperPrint.getPageWidth()) + "\" w:h=\"" + Utility.twip(jasperPrint.getPageHeight()) + "\"");
		writer.write(" w:orient=\"" + (jasperPrint.getOrientation() == JRReport.ORIENTATION_LANDSCAPE ? "landscape" : "portrait") + "\"");
		writer.write("/>\n");
		writer.write("   <w:pgMar w:top=\"0\" w:right=\"0\" w:bottom=\"0\" w:left=\"0\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" />\n");
//		writer.write("   <w:cols w:space=\"720\" />\n");
		writer.write("   <w:docGrid w:linePitch=\"360\" />\n");
		writer.write("  </w:sectPr>\n");
		writer.write(" </w:body>\n");
		writer.write("</w:document>\n");
	}

}
