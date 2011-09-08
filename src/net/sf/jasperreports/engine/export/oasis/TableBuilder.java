/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
 * Frank Sch�nheit - Frank.Schoenheit@Sun.COM
 */
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.export.LengthUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TableBuilder 
{

	private String tableName;
	private int reportIndex;
	private Writer bodyWriter;
	private Writer styleWriter;
	private boolean isFrame;
	private boolean isPageBreak;
	

	protected TableBuilder(
		String name, 
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
		isFrame = true;
		isPageBreak = false;
		
		this.bodyWriter = bodyWriter;
		this.styleWriter = styleWriter;

		this.tableName = "TBL_" + name;
	}

	protected TableBuilder(
		int reportIndex,
		int pageIndex,
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
		isFrame = false;
		isPageBreak = (reportIndex != 0 || pageIndex != 0);
		
		this.reportIndex = reportIndex;
		this.bodyWriter = bodyWriter;
		this.styleWriter = styleWriter;

		this.tableName = "TBL_" + reportIndex + "_" + pageIndex;
	}


	public void buildTableStyle(int width) throws IOException 
	{
		styleWriter.write(" <style:style style:name=\"" + tableName + "\"");//FIXMEODT can we have only one page style per report?
		if (!isFrame)
		{
			styleWriter.write(" style:master-page-name=\"master_" + reportIndex +"\"");
		}
		styleWriter.write(" style:family=\"table\">\n");
		styleWriter.write("   <style:table-properties");		
		styleWriter.write(" table:align=\"left\" style:width=\"" + LengthUtil.inch(width) + "in\"");
		if (isPageBreak)
		{
			styleWriter.write(" fo:break-before=\"page\"");
		}
//		FIXMEODT
//		if (tableWidth != null)
//		{
//			styleWriter.write(" style:width=\""+ tableWidth +"in\"");
//		}
//		if (align != null)
//		{
//			styleWriter.write(" table:align=\""+ align +"\"");
//		}
//		if (margin != null)
//		{
//			styleWriter.write(" fo:margin=\""+ margin +"\"");
//		}
//		if (backGroundColor != null)
//		{
//			styleWriter.write(" fo:background-color=\""+ backGroundColor +"\"");
//		}
		styleWriter.write("/>\n");
		styleWriter.write(" </style:style>\n");
	}
	
	public void buildTableHeader() throws IOException 
	{
		bodyWriter.write("<table:table");
		if (isFrame)
		{
			bodyWriter.write(" is-subtable=\"true\"");
		}
		bodyWriter.write(" table:name=\"");
		bodyWriter.write(tableName);
		bodyWriter.write("\"");
		bodyWriter.write(" table:style-name=\"");
		bodyWriter.write(tableName);
		bodyWriter.write("\"");
		bodyWriter.write(">\n");
	}
	
	public void buildTableFooter() throws IOException 
	{
		bodyWriter.write("</table:table>\n");
	}
	
	public void buildRowStyle(int rowIndex, int rowHeight) throws IOException 
	{
		String rowName = tableName + "_row_" + rowIndex;
		styleWriter.write(" <style:style style:name=\"" + rowName + "\"");
		styleWriter.write(" style:family=\"table-row\">\n");
		styleWriter.write("   <style:table-row-properties");		
		styleWriter.write(" style:use-optimal-row-height=\"false\""); 
//FIXMEODT check this		styleWriter.write(" style:use-optimal-row-height=\"true\""); 
		styleWriter.write(" style:row-height=\"" + LengthUtil.inch(rowHeight) + "in\"");
		styleWriter.write("/>\n");
		styleWriter.write(" </style:style>\n");
	}

	public void buildRowHeader(int rowIndex) throws IOException 
	{
		String rowName = tableName + "_row_" + rowIndex;
		bodyWriter.write("<table:table-row");
		bodyWriter.write(" table:style-name=\"" + rowName + "\"");
		bodyWriter.write(">\n");
	}
	
	public void buildRowFooter() throws IOException 
	{
		bodyWriter.write("</table:table-row>\n");
	}
	
	public void buildColumnStyle(int colIndex, int colWidth) throws IOException 
	{
		String columnName = tableName + "_col_" + colIndex;
		styleWriter.write(" <style:style style:name=\"" + columnName + "\"");
		styleWriter.write(" style:family=\"table-column\">\n");
		styleWriter.write("   <style:table-column-properties");		
		styleWriter.write(" style:column-width=\"" + LengthUtil.inch(colWidth) + "in\"");
		styleWriter.write("/>\n");
		styleWriter.write(" </style:style>\n");
	}

	public void buildColumnHeader(int colIndex) throws IOException 
	{
		String columnName = tableName + "_col_" + colIndex;
		bodyWriter.write("<table:table-column");		
		bodyWriter.write(" table:style-name=\"" + columnName + "\"");
		bodyWriter.write(">\n");
	}

	public void buildColumnFooter() throws IOException 
	{
		bodyWriter.write("</table:table-column>\n");		
	}

	public void buildCellHeader(String cellStyleName, int colSpan, int rowSpan) throws IOException 
	{
		//FIXMEODT officevalue bodyWriter.write("<table:table-cell office:value-type=\"string\"");
		bodyWriter.write("<table:table-cell");
		if (cellStyleName != null)
		{
			bodyWriter.write(" table:style-name=\"" + cellStyleName + "\"");
		}
		if (colSpan > 1)
		{
			bodyWriter.write(" table:number-columns-spanned=\"" + colSpan + "\"");
		}
		if (rowSpan > 1)
		{
			bodyWriter.write(" table:number-rows-spanned=\"" + rowSpan + "\"");
		}
		
		bodyWriter.write(">\n");
	}

	public void buildCellFooter() throws IOException {
		bodyWriter.write("</table:table-cell>\n");
	}
	
}