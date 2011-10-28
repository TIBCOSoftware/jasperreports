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
package net.sf.jasperreports.engine.export.ooxml;

import java.io.IOException;
import java.io.Writer;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.Cut;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.XlsRowLevelInfo;
import net.sf.jasperreports.engine.util.FileBufferedWriter;
import net.sf.jasperreports.engine.util.JRProperties;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
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
	public void exportHeader(int rowFreeze, int columnFreeze, JasperPrint jasperPrint)
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<worksheet\n");
		write(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");

		write("<sheetPr><outlinePr summaryBelow=\"0\"/></sheetPr><dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\"");
		if(rowFreeze > 0 || columnFreeze > 0)
		{
			write(">\n<pane xSplit=\"" + columnFreeze + "\" ySplit=\"" + rowFreeze +"\"");
			String columnName = JRProperties.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN) == null 
				? "A" 
				: JRProperties.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN);
			write(" topLeftCell=\"" + columnName + Math.min(65536, rowFreeze +1) + "\"");
			write(" activePane=\"bottomRight\" state=\"frozen\"/>\n");
			write("<selection pane=\"topRight\"/>\n");
			write("<selection pane=\"bottomLeft\"/>\n");
			write("<selection pane=\"bottomRight\"/>\n");
			write("</sheetView>\n</sheetViews>\n");
		}
		else
		{
			write("/></sheetViews>\n");
		}
		write("<sheetFormatPr defaultRowHeight=\"15\"/>\n");
	}
	

	/**
	 *
	 */
	public void exportFooter(int index, JasperPrint jasperPrint, boolean isIgnorePageMargins)
	{
		exportFooter(index, jasperPrint, isIgnorePageMargins, null);
	}


	/**
	 *
	 */
	public void exportFooter(int index, JasperPrint jasperPrint, boolean isIgnorePageMargins, String autoFilter)
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
		
		if(autoFilter != null)
		{
			write("<autoFilter ref=\"" + autoFilter + "\"/>\n");
		}
		
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

		write("<pageMargins left=\"");
		write(String.valueOf(jasperPrint.getLeftMargin() == null ? 0.7f : LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getLeftMargin()))); 
		write("\" right=\"");
		write(String.valueOf(jasperPrint.getRightMargin() == null ? 0.7f : LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getRightMargin()))); 
		write("\" top=\"");
		write(String.valueOf(jasperPrint.getTopMargin() == null ? 0.75f : LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getTopMargin()))); 
		write("\" bottom=\"");
		write(String.valueOf(jasperPrint.getBottomMargin() == null ? 0.75f : LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getBottomMargin()))); 
		write("\" header=\"0.0\" footer=\"0.0\"/>\n");
		if (jasperPrint.getOrientationValue() != null)
		{
			write("<pageSetup orientation=\"" + jasperPrint.getOrientationValue().getName().toLowerCase() + "\"/>\n");		
		}
		write("<drawing r:id=\"rIdDr" + index + "\"/></worksheet>");		
	}


	/**
	 *
	 */
	public void exportColumn(int colIndex, int colWidth, boolean autoFit) 
	{
		try
		{
			//colsWriter.write("<col min=\"" + (colIndex + 1) + "\" max=\"" + (colIndex + 1) + "\" customWidth=\"1\"" + (autoFit ? " bestFit=\"1\"" : (" width=\"" + (3f * colWidth / 18f) + "\"")) + "/>\n");
			//the col autofit does not work even if you comment out this line and use the above one; but you can try again
			colsWriter.write("<col min=\"" + (colIndex + 1) + "\" max=\"" + (colIndex + 1) + "\" customWidth=\"1\"" + (autoFit ? " bestFit=\"1\"" : "") + " width=\"" + (3f * colWidth / 18f) + "\"/>\n");
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 *
	 */
	public void exportRow(int rowHeight, Cut yCut, XlsRowLevelInfo levelInfo) 
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
		//write("<row r=\"" + rowIndex + "\" customHeight=\"1\""  + (yCut.isAutoFit() ? " bestFit=\"1\"" : " ht=\"" + rowHeight + "\""));
		//the row autofit does not work even if you comment out this line and use the above one; but you can try again
		write("<row r=\"" + rowIndex + "\" customHeight=\"1\""  + (yCut.isAutoFit() ? " bestFit=\"1\"" : "") + " ht=\"" + rowHeight + "\"");
		if (levelInfo.getLevelMap().size() > 0)
		{
			write(" outlineLevel=\"" + levelInfo.getLevelMap().size() + "\"");
		}
		write(">\n");
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
