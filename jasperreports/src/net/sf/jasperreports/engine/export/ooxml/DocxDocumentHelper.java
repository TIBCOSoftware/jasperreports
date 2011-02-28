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

import java.io.Writer;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class DocxDocumentHelper extends BaseHelper
{
	/**
	 * 
	 */
	public DocxDocumentHelper(Writer writer)
	{
		super(writer);
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
	public void exportFooter(JasperPrint jasperPrint)
	{
		write("  <w:sectPr>\n");
		write("   <w:pgSz w:w=\"" + Utility.twip(jasperPrint.getPageWidth()) + "\" w:h=\"" + Utility.twip(jasperPrint.getPageHeight()) + "\"");
		write(" w:orient=\"" + (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE ? "landscape" : "portrait") + "\"");
		write("/>\n");
		write("   <w:pgMar w:top=\"0\" w:right=\"0\" w:bottom=\"0\" w:left=\"0\" w:header=\"0\" w:footer=\"0\" w:gutter=\"0\" />\n");
//		write("   <w:cols w:space=\"720\" />\n");
		write("   <w:docGrid w:linePitch=\"360\" />\n");
		write("  </w:sectPr>\n");
		write(" </w:body>\n");
		write("</w:document>\n");
	}

}
