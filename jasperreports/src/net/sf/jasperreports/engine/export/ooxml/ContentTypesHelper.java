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
 *(at your option) any later version.
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


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class ContentTypesHelper extends BaseHelper
{
	/**
	 * 
	 */
	public ContentTypesHelper(Writer writer)
	{
		super(writer);
	}

	/**
	 *
	 */
	public void exportHeader() throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n");
		writer.write("  <Default Extension=\"gif\" ContentType=\"image/gif\"/>\n");
		writer.write("  <Default Extension=\"jpeg\" ContentType=\"image/jpeg\"/>\n");
		writer.write("  <Default Extension=\"png\" ContentType=\"image/png\"/>\n");
		writer.write("  <Default Extension=\"tiff\" ContentType=\"image/tiff\"/>\n");
		writer.write("  <Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n");
		writer.write("  <Default Extension=\"xml\" ContentType=\"application/xml\"/>\n");
		writer.write("  <Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>\n");
		writer.write("  <Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>\n");
	}
	

	/**
	 * 
	 */
	public void exportSheet(int index) throws IOException
	{
		writer.write("  <Override PartName=\"/xl/worksheets/sheet" + index + ".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>\n");
	}
	

	/**
	 *
	 */
	public void exportFooter() throws IOException
	{
		writer.write("</Types>\n");
	}

}
