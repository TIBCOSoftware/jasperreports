/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Special thanks to Google 'Summer of Code 2005' program for supporting this development
 * 
 * Contributors:
 * Majid Ali Khan - majidkk@users.sourceforge.net
 * Frank Schönheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class DocumentHelper
{
	/**
	 *
	 */
	public static void exportHeader(Writer writer) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<w:document \r\n");
//		writer.write(" xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" \r\n");
//		writer.write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\" \r\n");
//		writer.write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" \r\n");
//		writer.write(" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" \r\n");
//		writer.write(" xmlns:v=\"urn:schemas-microsoft-com:vml\" \r\n");
//		writer.write(" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" \r\n");
//		writer.write(" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" \r\n");
		writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"> \r\n");
//		writer.write(" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" \r\n");
//		writer.write(" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"> \r\n");
		writer.write(" <w:body> \r\n");
	}
	

	/**
	 *
	 */
	public static void exportFooter(JasperPrint jasperPrint, Writer writer) throws IOException
	{
		writer.write("  <w:sectPr> \r\n");
		writer.write("   <w:pgSz w:w=\"" + Utility.twip(jasperPrint.getPageWidth()) + "\" w:h=\"" + Utility.twip(jasperPrint.getPageHeight()) + "\" /> \r\n");
		writer.write("   <w:pgMar w:top=\"0\" w:right=\"0\" w:bottom=\"0\" w:left=\"0\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" /> \r\n");
//		writer.write("   <w:cols w:space=\"720\" /> \r\n");
		writer.write("   <w:docGrid w:linePitch=\"360\" /> \r\n");
		writer.write("  </w:sectPr> \r\n");
		writer.write(" </w:body> \r\n");
		writer.write("</w:document> \r\n");
	}

}
