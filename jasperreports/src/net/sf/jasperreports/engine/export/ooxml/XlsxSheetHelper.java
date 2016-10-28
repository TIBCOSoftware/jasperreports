/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.Cut;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.SheetPrintSettings;
import net.sf.jasperreports.engine.export.XlsRowLevelInfo;
import net.sf.jasperreports.engine.export.ooxml.type.PaperSizeEnum;
import net.sf.jasperreports.engine.util.FileBufferedWriter;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.export.XlsReportConfiguration;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class XlsxSheetHelper extends BaseHelper
{
	private int rowIndex;
	
	private FileBufferedWriter colsWriter = new FileBufferedWriter();
	private FileBufferedWriter mergedCellsWriter = new FileBufferedWriter();
	private FileBufferedWriter hyperlinksWriter = new FileBufferedWriter();
	
	/**
	 *
	 */
	private XlsxSheetRelsHelper sheetRelsHelper;//FIXMEXLSX truly embed the rels helper here and no longer have it available from outside; check drawing rels too
	private final XlsReportConfiguration configuration;
	private final SheetPrintSettings printSettings;
	
	private List<Integer> rowBreaks = new ArrayList<Integer>();

	/**
	 * 
	 */
	public XlsxSheetHelper(
		JasperReportsContext jasperReportsContext,
		Writer writer, 
		XlsxSheetRelsHelper sheetRelsHelper,
		XlsReportConfiguration configuration,
		SheetPrintSettings printSettings
		)
	{
		super(jasperReportsContext, writer);
		
		this.sheetRelsHelper = sheetRelsHelper;
		this.configuration = configuration;
		this.printSettings = printSettings;
	}

	/**
	 *
	 */
	public void exportHeader(
			boolean showGridlines, 
			int scale, 
			int rowFreezeIndex, 
			int columnFreezeIndex, 
			int maxColumnFreezeIndex, 
			JasperPrint jasperPrint, 
			Color tabColor)
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<worksheet\n");
		write(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");
		
		write("<sheetPr>\n");
		if(tabColor != null)
		{
			write("<tabColor rgb=\"FF" + JRColorUtil.getColorHexa(tabColor) + "\"/>\n");
		}
		write("<outlinePr summaryBelow=\"0\"/>\n");
		
		/* the scale factor takes precedence over fitWidth and fitHeight properties */
		if (
			(scale < 10 || scale > 400)
			&& (
				configuration.getFitWidth() != null 
				|| configuration.getFitHeight() != null
				|| Boolean.TRUE == configuration.isAutoFitPageHeight()
				)
			)
		{
			write("<pageSetUpPr fitToPage=\"1\"/>");
		}
		
		write("</sheetPr><dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\"");
		
		if(!showGridlines)
		{
			write(" showGridLines=\"0\"");
		}
		
		if(rowFreezeIndex > 0 || columnFreezeIndex > 0)
		{
			write(">\n<pane" + (columnFreezeIndex > 0 ? (" xSplit=\"" + columnFreezeIndex + "\"") : "") + (rowFreezeIndex > 0 ? (" ySplit=\"" + rowFreezeIndex + "\"") : ""));
			String columnFreezeName = columnFreezeIndex < 0 
					? "A" 
					: JRXlsAbstractExporter.getColumIndexName(columnFreezeIndex, maxColumnFreezeIndex);
			write(" topLeftCell=\"" + columnFreezeName + (rowFreezeIndex + 1) + "\"");
			String activePane = (rowFreezeIndex > 0 ? "bottom" : "top") + (columnFreezeIndex > 0 ? "Right" : "Left");
			write(" activePane=\"" + activePane + "\" state=\"frozen\"/>\n");
			write("<selection pane=\"" + activePane + "\"");
			write(" activeCell=\"" + columnFreezeName + (rowFreezeIndex + 1) + "\"");
			write(" sqref=\"" + columnFreezeName + (rowFreezeIndex + 1) + "\"");
			write("/>\n");
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
	public void exportFooter(
		int index, 
		PrintPageFormat jasperPrint, 
		boolean isIgnorePageMargins, 
		String autoFilter,
		Integer scale,
		Integer firstPageNumber,
		boolean firstPageNotSet, 
		Integer sheetPageCount
		)
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
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getLeftMargin()))); 
		write("\" right=\"");
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getRightMargin()))); 
		write("\" top=\"");
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getTopMargin()))); 
		write("\" bottom=\"");
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getBottomMargin()))); 
		write("0");
		write("\" header=\"");
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getHeaderMargin()))); 
		write("\" footer=\"");
		write(String.valueOf(LengthUtil.inchFloor4Dec(printSettings.getFooterMargin()))); 
		write("\"/>\n");
		write("<pageSetup");	
		
		if (jasperPrint.getOrientation() != null)
		{
			write(" orientation=\"" + jasperPrint.getOrientation().getName().toLowerCase() + "\"");	
		}
		
		/* the scale factor takes precedence over fitWidth and fitHeight properties */
		if(scale != null && scale > 9 && scale < 401)
		{
			write(" scale=\"" + scale + "\"");	
		}
		else
		{
			Integer fitWidth = configuration.getFitWidth();
			if (fitWidth != null && fitWidth !=  1)
			{
				write(" fitToWidth=\"" + fitWidth + "\"");
			}
			Integer fitHeight = configuration.getFitHeight();
			fitHeight = 
				fitHeight == null
				? (Boolean.TRUE == configuration.isAutoFitPageHeight() 
					? sheetPageCount
					: null)
				: fitHeight;
			if (fitHeight != null && fitHeight != 1)
			{
				write(" fitToHeight=\"" + fitHeight + "\"");
			}
		}
		
		PaperSizeEnum pSize = OoxmlUtils.getSuitablePaperSize(jasperPrint);
		String paperSize = pSize == PaperSizeEnum.UNDEFINED ? "" : " paperSize=\"" + pSize.getOoxmlValue() + "\"";
		write(paperSize);	
		
		if(firstPageNumber!= null && firstPageNumber > 0)
		{
			write(" firstPageNumber=\"" + firstPageNumber + "\"");
			write(" useFirstPageNumber=\"1\"/>\n");
		}
		else
		{
			write("/>\n");	
		}
		
		if (rowBreaks.size() > 0)
		{
			write("<rowBreaks count=\"" + rowBreaks.size() + "\" manualBreakCount=\"" + rowBreaks.size() + "\">");
			for (Integer rowBreakIndex : rowBreaks)
			{
				write("<brk id=\"" + (rowBreakIndex + 1) + "\" man=\"1\"/>");
			}
			write("</rowBreaks>");
		}

		if (hasHeaderOrFooter()) 
		{
			write("<headerFooter>");
			if (hasHeader())
			{
				write("<oddHeader>");
				if (StringUtils.isNotBlank(printSettings.getHeaderLeft()))
				{
					write("&amp;L");
					write(StringEscapeUtils.escapeHtml(printSettings.getHeaderLeft()));
				}
				if (StringUtils.isNotBlank(printSettings.getHeaderCenter()))
				{
					write("&amp;C");
					write(StringEscapeUtils.escapeHtml(printSettings.getHeaderCenter()));
				}
				if (StringUtils.isNotBlank(printSettings.getHeaderRight()))
				{
					write("&amp;R");
					write(StringEscapeUtils.escapeHtml(printSettings.getHeaderRight()));
				}
				write("</oddHeader>");
			}
			if (hasFooter())
			{
				write("<oddFooter>");
				if (StringUtils.isNotBlank(printSettings.getFooterLeft()))
				{
					write("&amp;L");
					write(StringEscapeUtils.escapeHtml(printSettings.getFooterLeft()));
				}
				if (StringUtils.isNotBlank(printSettings.getFooterCenter()))
				{
					write("&amp;C");
					write(StringEscapeUtils.escapeHtml(printSettings.getFooterCenter()));
				}
				if (StringUtils.isNotBlank(printSettings.getFooterRight()))
				{
					write("&amp;R");
					write(StringEscapeUtils.escapeHtml(printSettings.getFooterRight()));
				}
				write("</oddFooter>");
			}
			write("</headerFooter>");
		}
		else if(!firstPageNotSet)
		{
			write("<headerFooter><oddFooter>&amp;CPage &amp;P</oddFooter></headerFooter>\n");
		}
		
		write("<drawing r:id=\"rIdDr" + index + "\"/>");
		write("</worksheet>");		
	}
	
	private boolean hasHeaderOrFooter()
	{
		return hasHeader() || hasFooter();
	}

	private boolean hasHeader()
	{
		return StringUtils.isNotBlank(printSettings.getHeaderLeft())
				|| StringUtils.isNotBlank(printSettings.getHeaderCenter()) 
				|| StringUtils.isNotBlank(printSettings.getHeaderRight()); 
	}

	private boolean hasFooter()
	{
		return StringUtils.isNotBlank(printSettings.getFooterLeft())
				|| StringUtils.isNotBlank(printSettings.getFooterCenter()) 
				|| StringUtils.isNotBlank(printSettings.getFooterRight()); 
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
			colsWriter.write("<col min=\"" + (colIndex + 1) + "\" max=\"" + (colIndex + 1) + "\"" + (autoFit ? " customWidth=\"0\" bestFit=\"1\"" : " customWidth=\"1\"") + " width=\"" + (3f * colWidth / 18f) + "\"/>\n");
//			colsWriter.write("<col min=\"" + (colIndex + 1) + "\" max=\"" + (colIndex + 1) + "\" customWidth=\"1\"" + (autoFit ? " bestFit=\"1\"" : " width=\"" + (3f * colWidth / 18f) + "\"")+"/>\n");
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
		boolean isAutoFit = yCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) 
				&& (Boolean)yCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW);
		write("<row r=\"" + rowIndex + "\""  + (isAutoFit ? " customHeight=\"0\" bestFit=\"1\"" : " customHeight=\"1\"") + " ht=\"" + rowHeight + "\"");
		if (levelInfo.getLevelMap().size() > 0)
		{
			write(" outlineLevel=\"" + levelInfo.getLevelMap().size() + "\"");
		}
		write(">\n");
	}
	
	
	/**
	 *
	 */
	public void exportMergedCells(int row, int col, int maxColumnIndex, int rowSpan, int colSpan) 
	{
		rowSpan = configuration.isCollapseRowSpan() ? 1 : rowSpan;
		
		if (rowSpan > 1	|| colSpan > 1)
		{
			String ref = 
				JRXlsAbstractExporter.getColumIndexName(col, maxColumnIndex) + (row + 1)
				+ ":" + JRXlsAbstractExporter.getColumIndexName(col + colSpan - 1, maxColumnIndex) + (row + rowSpan); //FIXMEXLSX reuse this utility method
			
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
	public void exportHyperlink(int row, int col, int maxColumnIndex, String href, boolean isLocal) 
	{
		String ref = 
				JRXlsAbstractExporter.getColumIndexName(col, maxColumnIndex) + (row + 1);
		
		try
		{
			if(isLocal){
				hyperlinksWriter.write("<hyperlink ref=\"" + ref + "\" location=\"" + (href == null ? null : href.replaceAll("\\W", "")) + "\"/>\n");
			} else {
				hyperlinksWriter.write("<hyperlink ref=\"" + ref + "\" r:id=\"rIdLnk" + sheetRelsHelper.getHyperlink(href) + "\"/>\n");
			}
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	public void addRowBreak(int rowIndex)
	{
		rowBreaks.add(rowIndex);
	}
	
}
