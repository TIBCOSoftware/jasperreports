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

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.util.FileBufferedWriter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: StyleBuilder.java 2908 2009-07-21 14:32:01Z teodord $
 */
public class XlsxSheetHelper extends BaseHelper
{
	private int rowIndex;
	
	private FileBufferedWriter colsWriter = new FileBufferedWriter();
	private FileBufferedWriter mergedCellsWriter = new FileBufferedWriter();
	private FileBufferedWriter hyperlinksWriter = new FileBufferedWriter();
	
	private boolean isCollapseRowSpan;
	
	/**
	 *
	 */
	private XlsxSheetRelsHelper sheetRelsHelper;//FIXMEXLSX truly embed the rels helper here and no longer have it available from outside; check drawing rels too

	/**
	 * 
	 */
	public XlsxSheetHelper(
		Writer writer, 
		XlsxSheetRelsHelper sheetRelsHelper,
		boolean isCollapseRowSpan
		)
	{
		super(writer);
		
		this.sheetRelsHelper = sheetRelsHelper;
		this.isCollapseRowSpan = isCollapseRowSpan;
	}

	/**
	 *
	 */
	public void exportHeader()
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<worksheet\n");
		write(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");

		write("<dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\"/></sheetViews>\n");
		write("<sheetFormatPr defaultRowHeight=\"15\"/>\n");
	}
	

	/**
	 *
	 */
	public void exportFooter(int index)
	{
		if (rowIndex > 0)
		{
			write("</row>\n");
		}
		else
		{
			if (!colsWriter.isEmpty())
			{
				write("<cols>\n");
				colsWriter.writeData(writer);
				write("</cols>\n");
			}
			write("<sheetData>\n");
		}
		write("</sheetData>\n");
		if (!mergedCellsWriter.isEmpty())
		{
			write("<mergeCells>\n");
			mergedCellsWriter.writeData(writer);
			write("</mergeCells>\n");
		}
		if (!hyperlinksWriter.isEmpty())
		{
			write("<hyperlinks>\n");
			hyperlinksWriter.writeData(writer);
			write("</hyperlinks>\n");
		}
		write("<pageMargins left=\"0.7\" right=\"0.7\" top=\"0.75\" bottom=\"0.75\" header=\"0.3\" footer=\"0.3\"/>\n");
		//write("<pageSetup orientation=\"portrait\" r:id=\"rId1\"/>\n");		
		write("<drawing r:id=\"rIdDr" + index + "\"/></worksheet>");		
	}


	/**
	 *
	 */
	public void exportColumn(int colIndex, int colWidth) 
	{
		try
		{
			colsWriter.write("<col min=\"" + (colIndex + 1) + "\" max=\"" + (colIndex + 1) + "\" customWidth=\"1\" width=\"" + (3f * colWidth / 18f) + "\"/>\n");
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 *
	 */
	public void exportRow(int rowHeight) 
	{
		if (rowIndex > 0)
		{
			write("</row>\n");
		}
		else
		{
			if (!colsWriter.isEmpty())
			{
				write("<cols>\n");
				colsWriter.writeData(writer);
				write("</cols>\n");
			}
			write("<sheetData>\n");
		}
		rowIndex++;
		write("<row r=\"" + rowIndex + "\" customHeight=\"1\" ht=\"" + rowHeight + "\">\n");
	}
	
	/**
	 *
	 */
	public void exportMergedCells(int row, int col, int rowSpan, int colSpan) 
	{
		rowSpan = isCollapseRowSpan ? 1 : rowSpan;
		
		if (rowSpan > 1	|| colSpan > 1)
		{
			String ref = 
				XlsxCellHelper.getColumIndexLetter(col) + (row + 1)
				+ ":" + XlsxCellHelper.getColumIndexLetter(col + colSpan - 1) + (row + rowSpan); //FIXMEXLSX reuse this utility method
			
			try
			{
				mergedCellsWriter.write("<mergeCell ref=\"" + ref + "\"/>\n");
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
		}
	}

	/**
	 *
	 */
	public void exportHyperlink(int row, int col, String href) 
	{
		String ref = 
			XlsxCellHelper.getColumIndexLetter(col) + (row + 1);
		
		try
		{
			hyperlinksWriter.write("<hyperlink ref=\"" + ref + "\" r:id=\"rIdLnk" + sheetRelsHelper.getHyperlink(href) + "\"/>\n");
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}

}
