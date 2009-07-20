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


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
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


//	public void buildTableStyle() throws IOException 
//	{
//		styleWriter.write(" <style:style style:name=\"" + tableName + "\"");//FIXMEODT can we have only one page style per report?
//		if (!isFrame)
//		{
//			styleWriter.write(" style:master-page-name=\"master_" + reportIndex +"\"");
//		}
//		styleWriter.write(" style:family=\"table\">\n");
//		styleWriter.write("   <style:table-properties");		
//		if (isPageBreak)
//			styleWriter.write(" fo:break-before=\"page\"");
////		FIXMEODT
////		if (tableWidth != null)
////			styleWriter.write(" style:width=\""+ tableWidth +"in\"");
////		if (align != null)
////			styleWriter.write(" table:align=\""+ align +"\"");
////		if (margin != null)
////			styleWriter.write(" fo:margin=\""+ margin +"\"");
////		if (backGroundColor != null)
////			styleWriter.write(" fo:background-color=\""+ backGroundColor +"\"");
//		styleWriter.write("/>\n");
//		styleWriter.write(" </style:style>\n");
//	}
	
	public void buildTableHeader() throws IOException 
	{
		bodyWriter.write("  <w:tbl> \r\n");
//		if (isFrame)
//		{
//			bodyWriter.write(" is-subtable=\"true\"");
//		}
		bodyWriter.write("   <w:tblPr> \r\n");
		bodyWriter.write("    <w:tblLayout w:type=\"fixed\"/> \r\n");
//		bodyWriter.write("    <w:tblStyle w:val=\"TableNormal\" /> \r\n");
//		bodyWriter.write("    <w:tblW w:w=\"0\" w:type=\"pct\" /> \r\n");
//		bodyWriter.write("    <w:tblLook w:val=\"04A0\" /> \r\n");
		bodyWriter.write("   </w:tblPr> \r\n");
//		bodyWriter.write("   <w:tblGrid> \r\n");
//		for(int j = 1; j < size; j++)
//		{
//			bodyWriter.write("    <w:gridCol/> \r\n");
//		}
//		bodyWriter.write("   </w:tblGrid> \r\n");
		bodyWriter.flush();//FIXME check all these
	}
	
	public void buildTableFooter() throws IOException 
	{
		bodyWriter.write("  </w:tbl> \r\n");
		bodyWriter.flush();
	}
	
//	public void buildRowStyle(int rowIndex, int rowHeight) throws IOException 
//	{
//		String rowName = tableName + "_row_" + rowIndex;
//		styleWriter.write(" <style:style style:name=\"" + rowName + "\"");
//		styleWriter.write(" style:family=\"table-row\">\n");
//		styleWriter.write("   <style:table-row-properties");		
//		styleWriter.write(" style:use-optimal-row-height=\"false\""); 
////FIXMEODT check this		styleWriter.write(" style:use-optimal-row-height=\"true\""); 
//		styleWriter.write(" style:row-height=\"" + Utility.translatePixelsToInches(rowHeight) + "in\"");
//		styleWriter.write("/>\n");
//		styleWriter.write(" </style:style>\n");
//	}

	public void buildRowHeader(int rowHeight) throws IOException 
	{
		bodyWriter.write("   <w:tr> \r\n");
		bodyWriter.write("    <w:trPr> \r\n");
		bodyWriter.write("     <w:trHeight w:hRule=\"exact\" w:val=\"" +  + Utility.translatePointsToTwips(rowHeight) + "\" /> \r\n");
		bodyWriter.write("    </w:trPr> \r\n");
		bodyWriter.flush();
	}
	
	public void buildRowFooter() throws IOException 
	{
		bodyWriter.write("   </w:tr> \r\n");
		bodyWriter.flush();
	}
	
	public void buildColumnStyle(int colIndex, int colWidth) throws IOException 
	{
//		String columnName = tableName + "_col_" + colIndex;
//		styleWriter.write(" <style:style style:name=\"" + columnName + "\"");
//		styleWriter.write(" style:family=\"table-column\">\n");
//		styleWriter.write("   <style:table-column-properties");		
//		styleWriter.write(" style:column-width=\"" + Utility.translatePixelsToInches(colWidth) + "in\"");
//		styleWriter.write("/>\n");
//		styleWriter.write(" </style:style>\n");
		bodyWriter.write("    <w:gridCol w:w=\"" + Utility.translatePointsToTwips(colWidth) + "\"/> \r\n");
//		bodyWriter.write("    <w:gridCol w:type=\"dxa\" w:w=\"" + Utility.translatePointsToTwips(colWidth) + "\"/> \r\n");
	}

//	public void buildColumnHeader(int colIndex) throws IOException 
//	{
//		String columnName = tableName + "_col_" + colIndex;
//		bodyWriter.write("<table:table-column");		
//		bodyWriter.write(" table:style-name=\"" + columnName + "\"");
//		bodyWriter.write(">\n");
//	}

//	public void buildColumnFooter() throws IOException 
//	{
//		bodyWriter.write("</table:table-column>\n");		
//	}

	public void buildCellHeader(String cellStyleName, int colSpan, int rowSpan) throws IOException 
	{
		bodyWriter.write("    <w:tc> \r\n");
		bodyWriter.write("     <w:tcPr> \r\n");
//		bodyWriter.write("      <w:tcW w:w=\"" + Utility.translatePointsToTwips(emptyCellWidth) + "\" w:type=\"dxa\" /> \r\n");
		if (colSpan > 1)
		{
			bodyWriter.write("      <w:gridSpan w:val=\"" + colSpan +"\" /> \r\n");
		}
		bodyWriter.write("     </w:tcPr> \r\n");
		bodyWriter.flush();
	}

	public void buildCellFooter() throws IOException {
		bodyWriter.write("    </w:tc> \r\n");
		bodyWriter.flush();
	}
	
}