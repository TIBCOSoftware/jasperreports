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

import net.sf.jasperreports.engine.util.JRStringUtil;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class XmlssTableBuilder 
{

//	private int reportIndex;
	private Writer bodyWriter;
//	private Writer styleWriter;
//	private boolean isFrame = false;
	

	protected XmlssTableBuilder(
		String name, 
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
//		isFrame = true;
		
		this.bodyWriter = bodyWriter;
//		this.styleWriter = styleWriter;

	}

	protected XmlssTableBuilder(
		int reportIndex,
		int pageIndex,
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
//		isFrame = false;
		
//		this.reportIndex = reportIndex;
		this.bodyWriter = bodyWriter;
//		this.styleWriter = styleWriter;

	}
	
	public void buildTableHeader() throws IOException 
	{
		bodyWriter.write("<Table>\n");
	}
	
	public void buildTableFooter() throws IOException 
	{
		bodyWriter.write("</Table>\n");
	}
	
	public void buildRowHeader(int rowIndex, int rowHeight) throws IOException 
	{
		bodyWriter.write("<Row");
		bodyWriter.write(" ss:AutoFitHeight=\"0\"");
		bodyWriter.write(" ss:Height=\"" + rowHeight + "\"");
		bodyWriter.write(">\n");
	}
	
	public void buildRowFooter() throws IOException 
	{
		bodyWriter.write("</Row>\n");
	}

	public void buildColumnTag(int colIndex, int colWidth) throws IOException 
	{
		bodyWriter.write("<Column");		
		bodyWriter.write(" ss:AutoFitWidth=\"0\"");
		bodyWriter.write(" ss:Width=\"" + colWidth + "\"");
		bodyWriter.write("/>\n");
	}

	public void buildCellHeader(String cellStyleID, int colSpan, int rowSpan, String hyperlinkURL, String tooltip, String formula) throws IOException 
	{
		bodyWriter.write("<Cell");
		if (cellStyleID != null)
		{
			bodyWriter.write(" ss:StyleID=\"" + cellStyleID + "\"");
		}
		if (colSpan > 1)
		{
			bodyWriter.write(" ss:MergeAcross=\"" + colSpan + "\"");
		}
		if (rowSpan > 1)
		{
			bodyWriter.write(" ss:MergeDown=\"" + rowSpan + "\"");
		}
		if (formula != null)
		{
			bodyWriter.write(" ss:Formula=\"" + formula + "\"");
		}
		if(hyperlinkURL != null)
		{
			bodyWriter.write(" ss:HRef=\"" + hyperlinkURL + "\"");
			if(tooltip != null)
			{
				bodyWriter.write(" x:HRefScreenTip=\"" + JRStringUtil.xmlEncode(tooltip) + "\"");
			}
		}
		bodyWriter.write(">\n");
	}

	public void buildCellFooter() throws IOException {
		bodyWriter.write("</Cell>\n");
	}

	public void buildEmptyCell(int emptyCellColSpan, int emptyCellRowSpan) throws IOException
	{
		bodyWriter.write("<Cell");
		if (emptyCellColSpan > 1)
		{
			bodyWriter.write(" ss:MergeAcross=\"" + (emptyCellColSpan - 1) + "\"");
		}
		if (emptyCellRowSpan > 1)
		{
			bodyWriter.write(" ss:MergeDown=\"" + (emptyCellRowSpan - 1) + "\"");
		}
		bodyWriter.write("/>\n");
	}
	
}