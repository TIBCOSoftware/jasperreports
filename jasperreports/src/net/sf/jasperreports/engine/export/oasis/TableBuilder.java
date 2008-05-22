/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
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
package net.sf.jasperreports.engine.export.oasis;

import java.io.IOException;
import java.io.Writer;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class TableBuilder 
{

	private String tableName = null;
	private int reportIndex = 0;
	private Writer bodyWriter = null;
	private Writer styleWriter = null;
	private boolean isFrame = false;
	private boolean isPageBreak = false;
	

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


	public void buildTableStyle() throws IOException 
	{
		styleWriter.write(" <style:style style:name=\"" + tableName + "\"");//FIXMEODT can we have only one page style per report?
		if (!isFrame)
		{
			styleWriter.write(" style:master-page-name=\"master_" + reportIndex +"\"");
		}
		styleWriter.write(" style:family=\"table\">\n");
		styleWriter.write("   <style:table-properties");		
		if (isPageBreak)
			styleWriter.write(" fo:break-before=\"page\"");
//		FIXMEODT
//		if (tableWidth != null)
//			styleWriter.write(" style:width=\""+ tableWidth +"in\"");
//		if (align != null)
//			styleWriter.write(" table:align=\""+ align +"\"");
//		if (margin != null)
//			styleWriter.write(" fo:margin=\""+ margin +"\"");
//		if (backGroundColor != null)
//			styleWriter.write(" fo:background-color=\""+ backGroundColor +"\"");
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
		styleWriter.write(" style:row-height=\"" + Utility.translatePixelsToInches(rowHeight) + "in\"");
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
		styleWriter.write(" style:column-width=\"" + Utility.translatePixelsToInches(colWidth) + "in\"");
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
			bodyWriter.write(" table:style-name=\"" + cellStyleName + "\"");
		if (colSpan > 1)
			bodyWriter.write(" table:number-columns-spanned=\"" + colSpan + "\"");
		if (rowSpan > 1)
			bodyWriter.write(" table:number-rows-spanned=\"" + rowSpan + "\"");
		
		bodyWriter.write(">\n");
	}

	public void buildCellFooter() throws IOException {
		bodyWriter.write("</table:table-cell>\n");
	}
	
}