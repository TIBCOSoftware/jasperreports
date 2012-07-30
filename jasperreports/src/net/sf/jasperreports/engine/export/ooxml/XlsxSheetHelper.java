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

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.Cut;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.LengthUtil;
import net.sf.jasperreports.engine.export.XlsRowLevelInfo;
import net.sf.jasperreports.engine.export.ooxml.type.PaperSizeEnum;
import net.sf.jasperreports.engine.util.FileBufferedWriter;


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
	private JRPropertiesUtil propertiesUtil;

	/**
	 * 
	 */
	public XlsxSheetHelper(
		JasperReportsContext jasperReportsContext,
		Writer writer, 
		XlsxSheetRelsHelper sheetRelsHelper,
		boolean isCollapseRowSpan
		)
	{
		super(jasperReportsContext, writer);
		
		this.sheetRelsHelper = sheetRelsHelper;
		this.isCollapseRowSpan = isCollapseRowSpan;
		this.propertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
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
		write("<sheetFormatPr/>\n");
	}
	
	/**
	 *
	 */
	public void exportHeader(int rowFreeze, int columnFreeze, JasperPrint jasperPrint)
	{
		exportHeader(0, rowFreeze, columnFreeze, jasperPrint);
	}
	
	/**
	 *
	 */
	public void exportHeader(int scale, int rowFreeze, int columnFreeze, JasperPrint jasperPrint)
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<worksheet\n");
		write(" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"\n");
		write(" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n");
		
		/* the scale factor takes precedence over fitWidth and fitHeight properties */
		boolean noScale = scale < 10 || scale > 400;
		String fitWidth = propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FIT_WIDTH);
		String fitHeight = propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FIT_HEIGHT);
		String fitToPage = noScale && (fitHeight != null || fitWidth != null) ? "<pageSetUpPr fitToPage=\"1\"/>" : "";
		write("<sheetPr><outlinePr summaryBelow=\"0\"/>" + fitToPage + "</sheetPr><dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\"");
		
		if(rowFreeze > 0 || columnFreeze > 0)
		{
			write(">\n<pane" + (columnFreeze > 0 ? (" xSplit=\"" + columnFreeze + "\"") : "") + (rowFreeze > 0 ? (" ySplit=\"" + rowFreeze + "\"") : ""));
			String columnName = propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN) == null 
				? "A" 
				: propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FREEZE_COLUMN);
			write(" topLeftCell=\"" + columnName + (rowFreeze + 1) + "\"");
			String activePane = (rowFreeze > 0 ? "bottom" : "top") + (columnFreeze > 0 ? "Right" : "Left");
			write(" activePane=\"" + activePane + "\" state=\"frozen\"/>\n");
			write("<selection pane=\"" + activePane + "\"");
			write(" activeCell=\"" + columnName + (rowFreeze + 1) + "\"");
			write(" sqref=\"" + columnName + (rowFreeze + 1) + "\"");
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
	public void exportFooter(int index, JasperPrint jasperPrint, boolean isIgnorePageMargins)
	{
		exportFooter(index, jasperPrint, isIgnorePageMargins, null, null);
	}
	
	
	/**
	 *
	 */
	public void exportFooter(int index, JasperPrint jasperPrint, boolean isIgnorePageMargins, String autoFilter)
	{
		exportFooter(index, jasperPrint, isIgnorePageMargins, autoFilter, null);
	}
	
	/**
	 *
	 */
	public void exportFooter(int index, JasperPrint jasperPrint, boolean isIgnorePageMargins, String autoFilter, Integer scale)
	{
		exportFooter(index, jasperPrint, isIgnorePageMargins, autoFilter, null, null, true);
	}


	/**
	 *
	 */
	public void exportFooter(
			int index, 
			JasperPrint jasperPrint, 
			boolean isIgnorePageMargins, 
			String autoFilter,
			Integer scale,
			Integer firstPageNumber,
			boolean firstPageNotSet)
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
		
		write("<pageSetup");	
		
		if (jasperPrint.getOrientationValue() != null)
		{
			write(" orientation=\"" + jasperPrint.getOrientationValue().getName().toLowerCase() + "\"");	
		}
		
		/* the scale factor takes precedence over fitWidth and fitHeight properties */
		if(scale != null && scale > 9 && scale < 401)
		{
			write(" scale=\"" + scale + "\"");	
		}
		else
		{
			String fitWidth = propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FIT_WIDTH);
			if(fitWidth != null && !"1".equals(fitWidth))
			{
				write(" fitToWidth=\"" + fitWidth + "\"");
			}
			String fitHeight = propertiesUtil.getProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FIT_HEIGHT);
			if(fitHeight != null && !"1".equals(fitHeight))
			{
				write(" fitToHeight=\"" + fitHeight + "\"");
			}
		}
		
		byte pSize = getSuitablePaperSize(jasperPrint);
		String paperSize = pSize == PaperSizeEnum.UNDEFINED.getValue() ? "" : " paperSize=\"" + pSize + "\"";
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
		
		if(!firstPageNotSet)
		{
			//TODO: support for customized headers/footers in XLSX
			write("<headerFooter><oddFooter>Page &amp;P</oddFooter></headerFooter>\n");
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
		boolean isAutoFit = yCut.getPropertiesMap().containsKey(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) 
				&& (Boolean)yCut.getPropertiesMap().get(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW);
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
	
	private final byte getSuitablePaperSize(JasperPrint jasP)
	{

		if (jasP == null)
		{
			return -1;
		}
		long width = 0;
		long height = 0;

		if ((jasP.getPageWidth() != 0) && (jasP.getPageHeight() != 0))
		{

			double dWidth = (jasP.getPageWidth() / 72.0);
			double dHeight = (jasP.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by POI Api yet.
			// A3 papersize also not supported by POI Api yet.
			for (int i = 3; i < 6; i++)
			{
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height)))
				{
					return i == 3 ?  PaperSizeEnum.A3.getValue() : (i == 4 ? PaperSizeEnum.A4.getValue() : PaperSizeEnum.A5.getValue());
				}
			}
			
			// ISO 269 sizes - "Envelope DL" (110 � 220 mm)
			if (((width == 110) && (height == 220)) || ((width == 220) && (height == 110)))
			{
				return PaperSizeEnum.ENVELOPE_DL.getValue();
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
			if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
			{
				return PaperSizeEnum.LETTER.getValue();
			}
			// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
			if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
			{
				return PaperSizeEnum.LEGAL.getValue();
			}
			// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
			else if (((width == 190) && (height == 254)) || ((width == 254) && (height == 190)))
			{
				return PaperSizeEnum.EXECUTIVE.getValue();
			}
			// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
			// Not supported by POI Api yet.
				
		}
		return PaperSizeEnum.UNDEFINED.getValue();
	}
	
	// Berechnungsvorschriften f�r die DIN Formate A, B, und C.
	// Die Angabe der Breite/H�he erfolgt in [mm].

	protected final int calculateWidthForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	
}
