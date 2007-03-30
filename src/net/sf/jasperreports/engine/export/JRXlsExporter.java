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
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
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

	
	protected void setParameters()
	{
		super.setParameters();
		
		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);
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

	protected void setColumnWidth(short index, short width)
	{
		sheet.setColumnWidth(index, width);
	}

	protected void setRowHeight(int rowIndex, int lastRowHeight)
	{
		row = sheet.getRow((short)rowIndex);		
		if (row == null)
		{
			row = sheet.createRow((short)rowIndex);
		}
		
		row.setHeightInPoints((short)lastRowHeight);
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

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		cell = row.createCell((short) colIndex);
		
		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (gridCell.getBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getBackcolor()).getIndex();
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
		short forecolor = getNearestColor(line.getForecolor()).getIndex();

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
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
		cell.setCellStyle(cellStyle);
	}


	/**
	 *
	 */
	protected void exportRectangle(JRPrintElement element, JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		short forecolor = getNearestColor(element.getForecolor()).getIndex();

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (element.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(element.getBackcolor()).getIndex();
		}
		else if (gridCell.getBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getBackcolor()).getIndex();
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
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("");
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
		if (textElement.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(textElement.getBackcolor()).getIndex();
		}
		else if (gridCell.getBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getNearestColor(gridCell.getBackcolor()).getIndex();
		}

		StyleInfo baseStyle = getStyleInfo(
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
			sheet.addMergedRegion(new Region(rowIndex, (short)colIndex, (rowIndex + gridCell.getRowSpan() - 1), (short)(colIndex + gridCell.getColSpan() - 1)));

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
			
			BoxStyle box = style.box;
			cellStyle.setBorderTop(box.topBorder);
			cellStyle.setTopBorderColor(box.topBorderColour);
			cellStyle.setBorderLeft(box.leftBorder);
			cellStyle.setLeftBorderColor(box.leftBorderColour);
			cellStyle.setBorderBottom(box.bottomBorder);
			cellStyle.setBottomBorderColor(box.bottomBorderColour);
			cellStyle.setBorderRight(box.rightBorder);
			cellStyle.setRightBorderColor(box.rightBorderColour);
			
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
		StyleInfo style = getStyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, gridCell);		
		return getLoadedCellStyle(style);
	}


	protected StyleInfo getStyleInfo(short mode, short backcolor, short horizontalAlignment, short verticalAlignment, short rotation, HSSFFont font, JRExporterGridCell gridCell)
	{
		short gridForecolor = gridCell.getForecolor() == null ? blackIndex : getNearestColor(gridCell.getForecolor()).getIndex();
		BoxStyle boxStyle = new BoxStyle(gridCell.getBox(), backcolor, gridForecolor);
		StyleInfo style = new StyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, boxStyle);
		return style;
	}

	/**
	 *
	 */
	protected static short getBorder(byte pen)
	{
		short border = HSSFCellStyle.BORDER_NONE;
		
		switch (pen)
		{
			case JRGraphicElement.PEN_DOTTED :
			{
				border = HSSFCellStyle.BORDER_DASHED;
				break;
			}
			case JRGraphicElement.PEN_4_POINT :
			{
				border = HSSFCellStyle.BORDER_THICK;
				break;
			}
			case JRGraphicElement.PEN_2_POINT :
			{
				border = HSSFCellStyle.BORDER_THICK;
				break;
			}
			case JRGraphicElement.PEN_THIN :
			{
				border = HSSFCellStyle.BORDER_THIN;
				break;
			}
			case JRGraphicElement.PEN_NONE :
			{
				border = HSSFCellStyle.BORDER_NONE;
				break;
			}
			case JRGraphicElement.PEN_1_POINT :
			default :
			{
				border = HSSFCellStyle.BORDER_MEDIUM;
				break;
			}
		}
		
		return border;
	}

	protected void exportImage(JRPrintImage image, JRExporterGridCell gridCell, int colIndex, int rowIndex)
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
		return JRXlsExporterNature.getInstance();
	}
			

	protected static class BoxStyle
	{
		protected final short topBorder;
		protected final short bottomBorder;
		protected final short leftBorder;
		protected final short rightBorder;
		protected final short topBorderColour;
		protected final short bottomBorderColour;
		protected final short leftBorderColour;
		protected final short rightBorderColour;
		private final int hash;

		public BoxStyle(JRBox box, short backcolor, short forecolor)
		{
			if(box != null && box.getTopBorder() != JRGraphicElement.PEN_NONE)
			{
				topBorder = getBorder(box.getTopBorder());
				topBorderColour = box.getTopBorderColor() == null ? forecolor : getNearestColor(box.getTopBorderColor()).getIndex();
			}
			else
			{
				topBorder = HSSFCellStyle.BORDER_NONE;
				topBorderColour = backcolor;
			}
			
			if(box != null && box.getBottomBorder() != JRGraphicElement.PEN_NONE)
			{
				bottomBorder = getBorder(box.getBottomBorder());
				bottomBorderColour = box.getBottomBorderColor() == null ? forecolor : getNearestColor(box.getBottomBorderColor()).getIndex();
			}
			else
			{
				bottomBorder = HSSFCellStyle.BORDER_NONE;
				bottomBorderColour = backcolor;
			}
			
			if(box != null && box.getLeftBorder() != JRGraphicElement.PEN_NONE)
			{
				leftBorder = getBorder(box.getLeftBorder());
				leftBorderColour = box.getLeftBorderColor() == null ? forecolor : getNearestColor(box.getLeftBorderColor()).getIndex();
			}
			else
			{
				leftBorder = HSSFCellStyle.BORDER_NONE;
				leftBorderColour = backcolor;
			}
			
			if(box != null && box.getRightBorder() != JRGraphicElement.PEN_NONE)
			{
				rightBorder = getBorder(box.getRightBorder());
				rightBorderColour = box.getRightBorderColor() == null ? forecolor : getNearestColor(box.getRightBorderColor()).getIndex();
			}
			else
			{
				rightBorder = HSSFCellStyle.BORDER_NONE;
				rightBorderColour = backcolor;
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
		protected final BoxStyle box;
		private short dataFormat = -1;
		private int hashCode;
		
		public StyleInfo(short mode, short backcolor, short horizontalAlignment, short verticalAlignment, short rotation, HSSFFont font, BoxStyle box)
		{
			this.mode = mode;
			this.backcolor = backcolor;
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font;
			this.box = box;
			
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

