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

package net.sf.jasperreports.engine.export.xmlss;

import java.io.IOException;
import java.io.Writer;



/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XmlssContentBuilder
{
	/**
	 *
	 */
	public static final String VERSION = "1.0";

	/**
	 * 
	 */
	private Writer writer = null;
	private Writer styleWriter = null;
	private Writer bodyWriter = null;
	
	/**
	 * 
	 */
	public XmlssContentBuilder(
			Writer writer,
			Writer styleWriter,
			Writer bodyWriter
			)
	{
		this.writer = writer;
		this.styleWriter = styleWriter;
		this.bodyWriter = bodyWriter;
	}
	

	public void build() throws IOException
	{
		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?mso-application progid=\"Excel.Sheet\"?>\n");

		writer.write("<Workbook\n");
		writer.write(" xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
		writer.write(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
		writer.write(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
		writer.write(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n");
		writer.write(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n");
		
		writer.write("<ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n");
		writer.write(" <WindowHeight>9000</WindowHeight>\n");
		writer.write(" <WindowWidth>13860</WindowWidth>\n");
		writer.write(" <WindowTopX>240</WindowTopX>\n");
		writer.write(" <WindowTopY>75</WindowTopY>\n");
		writer.write(" <ProtectStructure>False</ProtectStructure>\n");
		writer.write(" <ProtectWindows>False</ProtectWindows>\n");
		writer.write("</ExcelWorkbook>\n");
		
		writer.write("<ss:Styles>\n");
		writer.flush();
		
		writer.write(styleWriter.toString());
		writer.flush();

		writer.write("</ss:Styles>\n");
		writer.flush();
		
		writer.write(bodyWriter.toString());
		writer.flush();

		writer.write("</Workbook>\n");
		
		writer.flush();
		writer.close();
		
	}

}
