/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.export.JRGridLayout.ExporterElements;
import net.sf.jasperreports.engine.fill.JRPrintFrame;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.collections.ReferenceMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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

	protected List loadedCellStyles = new ArrayList();

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

		HSSFFont cellFont = getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				HSSFCellStyle.SOLID_FOREGROUND,
				forecolor, 
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP, 
				(short)0,
				cellFont,
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

		HSSFFont cellFont = getLoadedFont(getDefaultFont(), forecolor);

		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT, 
				HSSFCellStyle.VERTICAL_TOP,
				(short)0, 
				cellFont,
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
	protected void exportText(JRPrintText textElement, JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		JRStyledText styledText = getStyledText(textElement);

		if (styledText == null)
		{
			return;
		}

		JRFont font = textElement;//.getFont();FIXME STYLE test this
//		if (font == null)
//		{
//			font = getDefaultFont();
//		}

		short forecolor = getNearestColor(textElement.getForecolor()).getIndex();

		HSSFFont cellFont = getLoadedFont(font, forecolor);

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
		
		HSSFCellStyle cellStyle = 
			getLoadedCellStyle(
				mode,
				backcolor, 
				horizontalAlignment, 
				verticalAlignment,
				rotation, 
				cellFont,
				gridCell
				);

		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell((short)colIndex);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (isAutoDetectCellType)
		{
			try
			{
				cell.setCellValue(Double.parseDouble(styledText.getText()));
			}
			catch(NumberFormatException e)
			{
				cell.setCellValue(JRStringUtil.replaceDosEOL(styledText.getText()));
			}
		}
		else
		{
			cell.setCellValue(JRStringUtil.replaceDosEOL(styledText.getText()));
		}
		cell.setCellStyle(cellStyle);
	}


	protected void createMergeRegion(JRExporterGridCell gridCell, int colIndex, int rowIndex, HSSFCellStyle cellStyle)
	{
		if (gridCell.colSpan > 1 || gridCell.rowSpan > 1)
		{
			sheet.addMergedRegion(new Region(rowIndex, (short)colIndex, (rowIndex + gridCell.rowSpan - 1), (short)(colIndex + gridCell.colSpan - 1)));

			for(int i = 0; i < gridCell.rowSpan; i++)
			{
				HSSFRow spanRow = sheet.getRow(rowIndex + i); 
				if (spanRow == null)
				{
					spanRow = sheet.createRow(rowIndex + i);
				}
				for(int j = 0; j < gridCell.colSpan; j++)
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

			if (
				cf.getFontName().equals(fontName) &&
				(cf.getColor() == forecolor) &&
				(cf.getFontHeightInPoints() == (short)font.getFontSize()) &&
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
			cellFont.setFontHeightInPoints((short)font.getFontSize());

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


	/**
	 *
	 */
	protected HSSFCellStyle getLoadedCellStyle(
		short mode, 
		short backcolor, 
		short horizontalAlignment, 
		short verticalAlignment,
		short rotation,
		HSSFFont font,
		short topBorder,
		short topBorderColor,
		short leftBorder,
		short leftBorderColor,
		short bottomBorder,
		short bottomBorderColor,
		short rightBorder,
		short rightBorderColor
		)
	{
		HSSFCellStyle cellStyle = null;

		if (loadedCellStyles != null && loadedCellStyles.size() > 0)
		{
			HSSFCellStyle cs = null;
			for (int i = 0; i < loadedCellStyles.size(); i++)
			{
				cs = (HSSFCellStyle)loadedCellStyles.get(i);
				
				if (
					cs.getFillPattern() == mode 
					&& cs.getFillForegroundColor() == backcolor 
					&& cs.getAlignment() == horizontalAlignment 
					&& cs.getVerticalAlignment() == verticalAlignment 
					&& cs.getRotation() == rotation 
					&& cs.getFontIndex() == font.getIndex()
					&& cs.getBorderTop() == topBorder 
					&& cs.getTopBorderColor() == topBorderColor 
					&& cs.getBorderLeft() == leftBorder 
					&& cs.getLeftBorderColor() == leftBorderColor 
					&& cs.getBorderBottom() == bottomBorder 
					&& cs.getBottomBorderColor() == bottomBorderColor 
					&& cs.getBorderRight() == rightBorder 
					&& cs.getRightBorderColor() == rightBorderColor 
					)
				{
					cellStyle = cs;
					break;
				}
			}
		}
		
		if (cellStyle == null)
		{
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(backcolor);
			cellStyle.setFillPattern(mode);
			cellStyle.setAlignment(horizontalAlignment);
			cellStyle.setVerticalAlignment(verticalAlignment);
			cellStyle.setRotation(rotation);
			cellStyle.setFont(font);
			cellStyle.setWrapText(true);
			
			cellStyle.setBorderTop(topBorder);
			cellStyle.setTopBorderColor(topBorderColor);
			cellStyle.setBorderLeft(leftBorder);
			cellStyle.setLeftBorderColor(leftBorderColor);
			cellStyle.setBorderBottom(bottomBorder);
			cellStyle.setBottomBorderColor(bottomBorderColor);
			cellStyle.setBorderRight(rightBorder);
			cellStyle.setRightBorderColor(rightBorderColor);
			
			loadedCellStyles.add(cellStyle);
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
		short topBorder = HSSFCellStyle.BORDER_NONE;
		short topBorderColor = backcolor;
		short leftBorder = HSSFCellStyle.BORDER_NONE;
		short leftBorderColor = backcolor;
		short bottomBorder = HSSFCellStyle.BORDER_NONE;
		short bottomBorderColor = backcolor;
		short rightBorder = HSSFCellStyle.BORDER_NONE;
		short rightBorderColor = backcolor;
		
		JRBox gridBox = gridCell.getBox();
		if (gridBox != null)
		{
			short gridForecolor = gridCell.getForecolor() == null ? blackIndex : getNearestColor(gridCell.getForecolor()).getIndex();
			
			topBorder = getBorder(gridBox.getTopBorder());
			topBorderColor = gridBox.getTopBorderColor() == null ? gridForecolor : getNearestColor(gridBox.getTopBorderColor()).getIndex();
			leftBorder = getBorder(gridBox.getLeftBorder());
			leftBorderColor = gridBox.getLeftBorderColor() == null ? gridForecolor : getNearestColor(gridBox.getLeftBorderColor()).getIndex();
			bottomBorder = getBorder(gridBox.getBottomBorder());
			bottomBorderColor = gridBox.getBottomBorderColor() == null ? gridForecolor : getNearestColor(gridBox.getBottomBorderColor()).getIndex();
			rightBorder = getBorder(gridBox.getRightBorder());
			rightBorderColor = gridBox.getRightBorderColor() == null ? gridForecolor : getNearestColor(gridBox.getRightBorderColor()).getIndex();	
		}

		return getLoadedCellStyle(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, 
				topBorder, topBorderColor, 
				leftBorder, leftBorderColor, 
				bottomBorder, bottomBorderColor,
				rightBorder, rightBorderColor);
	}

	/**
	 *
	 */
	private static short getBorder(byte pen)
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

	protected ExporterElements getExporterElements()
	{
		return JRGridLayout.NO_IMAGES_EXPORTER;
	}


	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int x, int y) throws JRException
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
}
