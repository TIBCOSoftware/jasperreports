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

	private int reportIndex = 0;
	private Writer bodyWriter = null;
	private Writer styleWriter = null;
	private boolean isFrame = false;
	

	protected XmlssTableBuilder(
		String name, 
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
		isFrame = true;
		
		this.bodyWriter = bodyWriter;
		this.styleWriter = styleWriter;

	}

	protected XmlssTableBuilder(
		int reportIndex,
		int pageIndex,
		Writer bodyWriter,
		Writer styleWriter
		) 
	{
		isFrame = false;
		
		this.reportIndex = reportIndex;
		this.bodyWriter = bodyWriter;
		this.styleWriter = styleWriter;

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
			bodyWriter.write(" ss:StyleID=\"" + cellStyleID + "\"");
		if (colSpan > 1)
			bodyWriter.write(" ss:MergeAcross=\"" + colSpan + "\"");
		if (rowSpan > 1)
			bodyWriter.write(" ss:MergeDown=\"" + rowSpan + "\"");
		if (formula != null)
			bodyWriter.write(" ss:Formula=\"" + formula + "\"");
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