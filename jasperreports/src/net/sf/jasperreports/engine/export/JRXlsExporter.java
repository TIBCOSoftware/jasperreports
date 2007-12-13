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
 * Contributors:
 * Wolfgang - javabreak@users.sourceforge.net
 * Mario Daepp - mdaepp@users.sourceforge.net
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.collections.ReferenceMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


/**
 * Exports a JasperReports document to XLS format. It has binary output type and exports the document to
 * a grid-based layout.
 * <p>
 * Since classic AWT fonts can be sometimes very different from system fonts (which are used by XLS viewers),
 * a font mapping feature was added. By using the {@link net.sf.jasperreports.engine.JRExporterParameter#FONT_MAP} parameter, a logical
 * font like "sansserif" can be mapped to a system specific font, like "Comic Sans MS". Both map keys and values are strings.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsExporter extends JRXlsAbstractExporter
{

	private static Map hssfColorsCache = new ReferenceMap();

	protected Map loadedCellStyles = new HashMap();

	/**
	 *
	 */
	protected HSSFWorkbook workbook = null;
	protected HSSFSheet sheet = null;
	protected HSSFRow row = null;
	protected HSSFCell cell = null;
	protected HSSFCellStyle emptyCellStyle = null;

	/**
	 *
	 */
	protected short whiteIndex = (new HSSFColor.WHITE()).getIndex();
	protected short blackIndex = (new HSSFColor.BLACK()).getIndex();
	
	protected short backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;
	
	protected HSSFDataFormat dataFormat = null;
	protected Map formatPatternsMap = null;

	protected ExporterNature nature = null;

	
	protected void setParameters()
	{
		super.setParameters();
		
		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);

		nature = new JRXlsExporterNature(filter, isIgnoreGraphics);
	}


	protected void setBackground()
	{
		if (!isWhitePageBackground)
		{
			backgroundMode = HSSFCellStyle.NO_FILL;
		}
	}


	protected void openWorkbook(OutputStream os)
	{
		workbook = new HSSFWorkbook();
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor((new HSSFColor.WHITE()).getIndex());
		emptyCellStyle.setFillPattern(backgroundMode);
		dataFormat = workbook.createDataFormat();
	}
	
	protected void createSheet(String name)
	{
		sheet = workbook.createSheet(name);
	}
	
	protected void closeWorkbook(OutputStream os) throws JRException
	{
		try
		{
			workbook.write(os);
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void setColumnWidth(int col, int width)
	{
		sheet.setColumnWidth((short)col, (short)width);
	}

	protected void setRowHeight(int rowIndex, int lastRowHeight)
	{
		row = sheet.getRow(rowIndex);		
		if (row == null)
		{
			row = sheet.createRow(rowIndex);
		}
		
		row.setHeightInPoints(lastRowHeight);
	}

	protected void setCell(int colIndex, int rowIndex)
	{
		HSSFCell emptyCell = row.getCell((short)colIndex);
		if (emptyCell == null)
		{
			emptyCell = row.createCell((short)colIndex);
			emptyCell.setCellStyle(emptyCellStyle);
		}
	}

	protected void removeColumn(int colIndex)
	{
		sheet.setColumnHidden((short)colIndex, true);
//		sheet.setColumnGroupCollapsed((short)colIndex, true);
//		for(int rowIndex = sheet.getLastRowNum(); rowIndex >= 0; rowIndex--)
//		{
//			HSSFRow row = sheet.getRow(rowIndex);
//			HSSFCell cell = row.getCell((short)colIndex);
//			if (cell != null)
//			{
//				row.removeCell(cell);
//			}
//		}
	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		cell = row.createCell((short) colIndex);
		
		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getCellBackcolor()).getIndex();
		}
		
		short forecolor = blackIndex;
		if (gridCell.getForecolor() != null)
		{
			forecolor = getNearestColor(gridCell.getForecolor()).getIndex();
		}

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP,
				(short)0, 
				getLoadedFont(getDefaultFont(), forecolor),
				gridCell
				);
		
		cell.setCellStyle(cellStyle);
	}

	/**
	 *
	 */
	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		short forecolor = getNearestColor(line.getLinePen().getLineColor()).getIndex();

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				HSSFCellStyle.SOLID_FOREGROUND,
				forecolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP, 
				(short)0,
				getLoadedFont(getDefaultFont(), forecolor),
				gridCell
				);
		
		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell((short)colIndex);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		//cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		short forecolor = getNearestColor(element.getLinePen().getLineColor()).getIndex();

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getCellBackcolor()).getIndex();
		}

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP,
				(short)0, 
				getLoadedFont(getDefaultFont(), forecolor),
				gridCell
				);
		
		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell((short)colIndex);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		//cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}
	


	protected void exportText(JRPrintText textElement, JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
		JRStyledText styledText = getStyledText(textElement);

		if (styledText == null)
		{
			return;
		}

		short forecolor = getNearestColor(textElement.getForecolor()).getIndex();

		TextAlignHolder textAlignHolder = getTextAlignHolder(textElement);
		short horizontalAlignment = getHorizontalAlignment(textAlignHolder);
		short verticalAlignment = getVerticalAlignment(textAlignHolder);
		short rotation = getRotation(textAlignHolder);

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getCellBackcolor()).getIndex();
		}

		StyleInfo baseStyle = 
			new StyleInfo(
				mode,
				backcolor, 
				horizontalAlignment, 
				verticalAlignment,
				rotation, 
				getLoadedFont(textElement, forecolor),
				gridCell
				);
		
		createTextCell(textElement, gridCell, colIndex, rowIndex, styledText, baseStyle);
	}


	protected void createTextCell(JRPrintText textElement, final JRExporterGridCell gridCell, final int colIndex, final int rowIndex, JRStyledText styledText, final StyleInfo baseStyle) throws JRException
	{
		String textStr = styledText.getText();
		if (isDetectCellType)
		{
			TextValue value = getTextValue(textElement, textStr);
			value.handle(new TextValueHandler()
			{
				public void handle(StringTextValue textValue)
				{
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					setStringCellValue(textValue.getText());
					endCreateCell(cellStyle);
				}

				public void handle(NumberTextValue textValue)
				{
					if (textValue.getPattern() != null)
					{
						baseStyle.setDataFormat(
							dataFormat.getFormat(
								getConvertedPattern(textValue.getPattern())
								)
							);
					}

					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					}
					else
					{
						cell.setCellValue(textValue.getValue().doubleValue());
					}
					endCreateCell(cellStyle);
				}

				public void handle(DateTextValue textValue)
				{
					baseStyle.setDataFormat(
						dataFormat.getFormat(
							getConvertedPattern(textValue.getPattern())
							)
						);
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					}
					else
					{
						cell.setCellValue(textValue.getValue());
					}
					endCreateCell(cellStyle);
				}

				public void handle(BooleanTextValue textValue)
				{
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					}
					else
					{
						cell.setCellValue(textValue.getValue().booleanValue());
					}
					endCreateCell(cellStyle);
				}
				
				/**
				 * This method is intended to modify a given format pattern so to include 
				 * only the accepted proprietary format characters. The resulted pattern 
				 * will possibly truncate the original pattern
				 * @param pattern
				 * @return pattern converted to accepted proprietary formats
				 */
				private String getConvertedPattern(String pattern)
				{
					if (formatPatternsMap != null && formatPatternsMap.containsKey(pattern))
					{
						return (String) formatPatternsMap.get(pattern);
					}
					return pattern;
				}

			});
		}
		else if (isAutoDetectCellType)
		{
			HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
			try
			{
				cell.setCellValue(Double.parseDouble(textStr));
			}
			catch(NumberFormatException e)
			{
				setStringCellValue(textStr);
			}
			endCreateCell(cellStyle);
		}
		else
		{
			HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
			setStringCellValue(textStr);
			endCreateCell(cellStyle);
		}
	}


	protected HSSFCellStyle initCreateCell(JRExporterGridCell gridCell, int colIndex, int rowIndex, StyleInfo baseStyle)
	{
		HSSFCellStyle cellStyle = getLoadedCellStyle(baseStyle);
		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);
		cell = row.createCell((short)colIndex);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		return cellStyle;
	}

	protected void endCreateCell(HSSFCellStyle cellStyle)
	{
		cell.setCellStyle(cellStyle);
	}

	protected final void setStringCellValue(String textStr)
	{
		cell.setCellValue(JRStringUtil.replaceDosEOL(textStr));
	}

	
	protected void createMergeRegion(JRExporterGridCell gridCell, int colIndex, int rowIndex, HSSFCellStyle cellStyle)
	{
		if (gridCell.getColSpan() > 1 || gridCell.getRowSpan() > 1)
		{
			if (isCollapseRowSpan)
			{
				sheet.addMergedRegion(new Region(rowIndex, (short)colIndex, rowIndex, (short)(colIndex + gridCell.getColSpan() - 1)));
			}
			else
			{
				sheet.addMergedRegion(new Region(rowIndex, (short)colIndex, (rowIndex + gridCell.getRowSpan() - 1), (short)(colIndex + gridCell.getColSpan() - 1)));
			}

			for(int i = 0; i < gridCell.getRowSpan(); i++)
			{
				HSSFRow spanRow = sheet.getRow(rowIndex + i); 
				if (spanRow == null)
				{
					spanRow = sheet.createRow(rowIndex + i);
				}
				for(int j = 0; j < gridCell.getColSpan(); j++)
				{
					HSSFCell spanCell = spanRow.getCell((short)(colIndex + j));
					if (spanCell == null)
					{
						spanCell = spanRow.createCell((short)(colIndex + j));
					}
					spanCell.setCellStyle(cellStyle);
				}
			}
		}
	}

	private short getHorizontalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.horizontalAlignment)
		{
			case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
				return HSSFCellStyle.ALIGN_RIGHT;
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				return HSSFCellStyle.ALIGN_CENTER;
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				return HSSFCellStyle.ALIGN_JUSTIFY;
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
			default:
				return HSSFCellStyle.ALIGN_LEFT;
		}
	}

	private short getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				return HSSFCellStyle.VERTICAL_BOTTOM;
			case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				return HSSFCellStyle.VERTICAL_CENTER;
			case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
				return HSSFCellStyle.VERTICAL_JUSTIFY;
			case JRAlignment.VERTICAL_ALIGN_TOP:
			default:
				return HSSFCellStyle.VERTICAL_TOP;
		}
	}

	private short getRotation(TextAlignHolder alignment)
	{
		switch (alignment.rotation)
		{
			case JRTextElement.ROTATION_LEFT:
				return 90;
			case JRTextElement.ROTATION_RIGHT:
				return -90;
			case JRTextElement.ROTATION_UPSIDE_DOWN:
			case JRTextElement.ROTATION_NONE:
			default:
				return 0;
		}
	}
	
	/**
	 *
	 */
	protected static HSSFColor getNearestColor(Color awtColor)
	{
		HSSFColor color = (HSSFColor) hssfColorsCache.get(awtColor);

		if (color == null)
		{
			Map triplets = HSSFColor.getTripletHash();
			if (triplets != null)
			{
				Collection keys = triplets.keySet();
				if (keys != null && keys.size() > 0)
				{
					Object key = null;
					HSSFColor crtColor = null;
					short[] rgb = null;
					int diff = 0;
					int minDiff = 999;
					for (Iterator it = keys.iterator(); it.hasNext();)
					{
						key = it.next();

						crtColor = (HSSFColor) triplets.get(key);
						rgb = crtColor.getTriplet();

						diff = Math.abs(rgb[0] - awtColor.getRed()) + Math.abs(rgb[1] - awtColor.getGreen()) + Math.abs(rgb[2] - awtColor.getBlue());

						if (diff < minDiff)
						{
							minDiff = diff;
							color = crtColor;
						}
					}
				}
			}
			
			hssfColorsCache.put(awtColor, color);
		}

		return color;
	}


	/**
	 *
	 */
	protected HSSFFont getLoadedFont(JRFont font, short forecolor)
	{
		HSSFFont cellFont = null;
		
		String fontName = font.getFontName();
		if (fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = (String) fontMap.get(fontName);
		}

		for (int i = 0; i < loadedFonts.size(); i++)
		{
			HSSFFont cf = (HSSFFont)loadedFonts.get(i);
			
			short fontSize = (short)font.getFontSize();
			if (isFontSizeFixEnabled)
				fontSize -= 1;
			
			if (
				cf.getFontName().equals(fontName) &&
				(cf.getColor() == forecolor) &&
				(cf.getFontHeightInPoints() == fontSize) &&
				((cf.getUnderline() == HSSFFont.U_SINGLE)?(font.isUnderline()):(!font.isUnderline())) &&
				(cf.getStrikeout() == font.isStrikeThrough()) &&
				((cf.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD)?(font.isBold()):(!font.isBold())) &&
				(cf.getItalic() == font.isItalic())
				)
			{
				cellFont = cf;
				break;
			}
		}
		
		if (cellFont == null)
		{
			cellFont = workbook.createFont();

			cellFont.setFontName(fontName);
			cellFont.setColor(forecolor);

			short fontSize = (short)font.getFontSize();
			if (isFontSizeFixEnabled)
				fontSize -= 1;
			
			cellFont.setFontHeightInPoints(fontSize);

			if (font.isUnderline())
			{
				cellFont.setUnderline(HSSFFont.U_SINGLE);
			}
			if (font.isStrikeThrough())
			{
				cellFont.setStrikeout(true);
			}
			if (font.isBold())
			{
				cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			}
			if (font.isItalic())
			{
				cellFont.setItalic(true);
			}
			
			loadedFonts.add(cellFont);
		}
			
		return cellFont;
	}


	protected HSSFCellStyle getLoadedCellStyle(StyleInfo style)
	{
		HSSFCellStyle cellStyle = (HSSFCellStyle) loadedCellStyles.get(style);		
		if (cellStyle == null)
		{
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(style.backcolor);
			cellStyle.setFillPattern(style.mode);
			cellStyle.setAlignment(style.horizontalAlignment);
			cellStyle.setVerticalAlignment(style.verticalAlignment);
			cellStyle.setRotation(style.rotation);
			cellStyle.setFont(style.font);
			cellStyle.setWrapText(true);
			
			if (style.hasDataFormat())
			{
				cellStyle.setDataFormat(style.getDataFormat());
			}
			
			if (!isIgnoreCellBorder)
			{
				BoxStyle box = style.box;
				cellStyle.setBorderTop(box.topBorder);
				cellStyle.setTopBorderColor(box.topBorderColour);
				cellStyle.setBorderLeft(box.leftBorder);
				cellStyle.setLeftBorderColor(box.leftBorderColour);
				cellStyle.setBorderBottom(box.bottomBorder);
				cellStyle.setBottomBorderColor(box.bottomBorderColour);
				cellStyle.setBorderRight(box.rightBorder);
				cellStyle.setRightBorderColor(box.rightBorderColour);
			}
			
			loadedCellStyles.put(style, cellStyle);
		}
		return cellStyle;
	}

	protected HSSFCellStyle getLoadedCellStyle(
			short mode, 
			short backcolor, 
			short horizontalAlignment, 
			short verticalAlignment,
			short rotation,
			HSSFFont font,
			JRExporterGridCell gridCell
			)
	{
		StyleInfo style = new StyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, gridCell);		
		return getLoadedCellStyle(style);
	}


	/**
	 *
	 */
	protected static short getBorder(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			switch (pen.getLineStyle().byteValue())
			{
				case JRPen.LINE_STYLE_DASHED :
				{
					return HSSFCellStyle.BORDER_DASHED;
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					if (lineWidth >= 2f)
					{
						return HSSFCellStyle.BORDER_THICK;
					}
					else if (lineWidth >= 1f)
					{
						return HSSFCellStyle.BORDER_MEDIUM;//FIXMEBORDER there is also HSSFCellStyle.BORDER_MEDIUM_DASHED available
					}
					else if (lineWidth >= 0.5f)
					{
						return HSSFCellStyle.BORDER_THIN;
					}

					return HSSFCellStyle.BORDER_HAIR;
				}
			}
		}
		
		return HSSFCellStyle.BORDER_NONE;
	}

	protected void exportImage(JRPrintImage image, JRExporterGridCell gridCell, int colIndex, int rowIndex, int emptyCols)
	{
		//nothing
	}


	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int x, int y)
	{		
		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(frame.getBackcolor()).getIndex();
		}
		
		short forecolor = getNearestColor(frame.getForecolor()).getIndex();

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP,
				(short)0, 
				getLoadedFont(getDefaultFont(), forecolor),
				gridCell
				);

		createMergeRegion(gridCell, x, y, cellStyle);

		cell = row.createCell((short)x);		
		cell.setCellStyle(cellStyle);
	}
	

	protected ExporterNature getNature()
	{
		return nature;
	}
			

	protected static class BoxStyle
	{
		protected short topBorder;
		protected short bottomBorder;
		protected short leftBorder;
		protected short rightBorder;
		protected short topBorderColour;
		protected short bottomBorderColour;
		protected short leftBorderColour;
		protected short rightBorderColour;
		private int hash;

		public BoxStyle()
		{
			hash = computeHash();
		}

		public void setBox(JRLineBox box)
		{
			topBorder = getBorder(box.getTopPen());
			topBorderColour = getNearestColor(box.getTopPen().getLineColor()).getIndex();
			
			bottomBorder = getBorder(box.getBottomPen());
			bottomBorderColour = getNearestColor(box.getBottomPen().getLineColor()).getIndex();
			
			leftBorder = getBorder(box.getLeftPen());
			leftBorderColour = getNearestColor(box.getLeftPen().getLineColor()).getIndex();
			
			rightBorder = getBorder(box.getRightPen());
			rightBorderColour = getNearestColor(box.getRightPen().getLineColor()).getIndex();
			
			hash = computeHash();
		}

		public void setPen(JRPen pen)
		{
			if (
				topBorder == HSSFCellStyle.BORDER_NONE
				&& leftBorder == HSSFCellStyle.BORDER_NONE
				&& bottomBorder == HSSFCellStyle.BORDER_NONE
				&& rightBorder == HSSFCellStyle.BORDER_NONE
				)
			{
				topBorder = getBorder(pen);
				topBorderColour = getNearestColor(pen.getLineColor()).getIndex();

				bottomBorder = topBorder;
				bottomBorderColour = topBorderColour;

				leftBorder = topBorder;
				leftBorderColour = topBorderColour;

				rightBorder = topBorder;
				rightBorderColour = topBorderColour;
			}

			hash = computeHash();
		}

		private int computeHash()
		{
			int hashCode = topBorder;
			hashCode = 31*hashCode + topBorderColour;
			hashCode = 31*hashCode + bottomBorder;
			hashCode = 31*hashCode + bottomBorderColour;
			hashCode = 31*hashCode + leftBorder;
			hashCode = 31*hashCode + leftBorderColour;
			hashCode = 31*hashCode + rightBorder;
			hashCode = 31*hashCode + rightBorderColour;
			return hashCode;
		}
		
		public int hashCode()
		{
			return hash;
		}
		
		public boolean equals(Object o)
		{
			BoxStyle b = (BoxStyle) o;
			
			return 
				b.topBorder == topBorder &&
				b.topBorderColour == topBorderColour &&
				b.bottomBorder == bottomBorder &&
				b.bottomBorderColour == bottomBorderColour &&
				b.leftBorder == leftBorder &&
				b.leftBorderColour == leftBorderColour &&
				b.rightBorder == rightBorder &&
				b.rightBorderColour == rightBorderColour;
		}
		
		public String toString()
		{
			return "(" +
				topBorder + "/" + topBorderColour + "," +
				bottomBorder + "/" + bottomBorderColour + "," +
				leftBorder + "/" + leftBorderColour + "," +
				rightBorder + "/" + rightBorderColour + ")";
		}
	}

	
	protected static class StyleInfo
	{
		protected final short mode; 
		protected final short backcolor; 
		protected final short horizontalAlignment; 
		protected final short verticalAlignment;
		protected final short rotation;
		protected final HSSFFont font;
		protected final BoxStyle box = new BoxStyle();
		private short dataFormat = -1;
		private int hashCode;
		
		public StyleInfo(short mode, short backcolor, short horizontalAlignment, short verticalAlignment, short rotation, HSSFFont font, JRExporterGridCell gridCell)
		{
			this.mode = mode;
			this.backcolor = backcolor;
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font;

			JRLineBox lineBox = gridCell.getBox();
			if (lineBox != null)
				box.setBox(lineBox);
			JRPrintElement element = gridCell.getElement();
			if (element instanceof JRCommonGraphicElement)
				box.setPen(((JRCommonGraphicElement)element).getLinePen());
			
			hashCode = computeHash();
		}

		protected int computeHash()
		{
			int hash = mode;
			hash = 31*hash + backcolor;
			hash = 31*hash + horizontalAlignment;
			hash = 31*hash + verticalAlignment;
			hash = 31*hash + rotation;
			hash = 31*hash + (font == null ? 0 : font.getIndex());
			hash = 31*hash + (box == null ? 0 : box.hashCode());
			hash = 31*hash + dataFormat;
			return hash;
		}
		
		public void setDataFormat(short dataFormat)
		{
			this.dataFormat = dataFormat;
			hashCode = computeHash();
		}
		
		public boolean hasDataFormat()
		{
			return dataFormat != -1;
		}
		
		public short getDataFormat()
		{
			return dataFormat;
		}
		
		public int hashCode()
		{
			return hashCode;
		}
		
		public boolean equals(Object o)
		{
			StyleInfo s = (StyleInfo) o;
			
			return s.mode == mode
					&& s.backcolor == backcolor
					&& s.horizontalAlignment == horizontalAlignment
					&& s.verticalAlignment == verticalAlignment
					&& s.rotation == rotation
					&& (s.font == null ? font == null : (font != null && s.font.getIndex() == font.getIndex()))
					&& (s.box == null ? box == null : (box != null && s.box.equals(box)))
					&& s.rotation == rotation;
		}
		
		public String toString()
		{
			return "(" +
				mode + "," + backcolor + "," + 
				horizontalAlignment + "," + verticalAlignment + "," + 
				rotation + "," + font + "," +
				box + "," + dataFormat + ")";
		}
	}
}

