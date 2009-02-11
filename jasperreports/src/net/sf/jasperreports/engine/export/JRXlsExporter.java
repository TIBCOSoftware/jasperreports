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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;


/**
 * Exports a JasperReports document to XLS format. It has binary output type and exports the document to
 * a grid-based layout.
 * <p>
 * Since classic AWT fonts can be sometimes very different from system fonts (which are used by XLS viewers),
 * a font mapping feature was added. By using the {@link net.sf.jasperreports.engine.JRExporterParameter#FONT_MAP} parameter, a logical
 * font like "sansserif" can be mapped to a system specific font, like "DejaVu Serif". Both map keys and values are strings.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsExporter extends JRXlsAbstractExporter
{

	private static final Log log = LogFactory.getLog(JRXlsAbstractExporter.class);
	
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

	protected HSSFPatriarch patriarch = null;

	protected void setParameters()
	{
		super.setParameters();

		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);

		nature = new JRXlsExporterNature(filter, isIgnoreGraphics, isIgnorePageMargins);
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
		patriarch = sheet.createDrawingPatriarch();
		sheet.getPrintSetup().setLandscape(jasperPrint.getOrientation() == JRReport.ORIENTATION_LANDSCAPE);
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
		sheet.setColumnWidth(col, width);
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
		HSSFCell emptyCell = row.getCell(colIndex);
		if (emptyCell == null)
		{
			emptyCell = row.createCell(colIndex);
			emptyCell.setCellStyle(emptyCellStyle);
		}
	}

	protected void removeColumn(int colIndex)
	{
		sheet.setColumnHidden(colIndex, true);
//      sheet.setColumnGroupCollapsed((short)colIndex, true);
//      for(int rowIndex = sheet.getLastRowNum(); rowIndex >= 0; rowIndex--)
//      {
//          HSSFRow row = sheet.getRow(rowIndex);
//          HSSFCell cell = row.getCell((short)colIndex);
//          if (cell != null)
//          {
//              row.removeCell(cell);
//          }
//      }
	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		cell = row.createCell(colIndex);

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
				getLoadedFont(getDefaultFont(), forecolor, null),
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

		int side = BoxStyle.TOP;
		float ratio = line.getWidth() / line.getHeight();
		if (ratio > 1)
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				side = BoxStyle.TOP;
			}
			else
			{
				side = BoxStyle.BOTTOM;
			}
		}
		else
		{
			if (line.getDirection() == JRLine.DIRECTION_TOP_DOWN)
			{
				side = BoxStyle.LEFT;
			}
			else
			{
				side = BoxStyle.RIGHT;
			}
		}
		BoxStyle boxStyle = new BoxStyle(side, line.getLinePen());

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
				getLoadedFont(getDefaultFont(), forecolor, null),
				boxStyle
				);

		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell(colIndex);
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
				getLoadedFont(getDefaultFont(), forecolor, null),
				gridCell
				);

		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell(colIndex);
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
				getLoadedFont(textElement, forecolor, null),
				gridCell
				);

		createTextCell(textElement, gridCell, colIndex, rowIndex, styledText, baseStyle, forecolor);
	}


	protected void createTextCell(final JRPrintText textElement, final JRExporterGridCell gridCell, final int colIndex, final int rowIndex, final JRStyledText styledText, final StyleInfo baseStyle, final short forecolor) throws JRException
	{
		String formula = textElement.getPropertiesMap().getProperty(JRAbstractExporter.PROPERTY_CELL_FORMULA);
		String textStr = styledText.getText();
		
		if(formula != null)
		{	
			formula = formula.trim();
			if(formula.startsWith("="))
			{
				formula = formula.substring(1);
			}
			try
			{
				TextValue value = getTextValue(textElement, textStr);
				
				if (value instanceof NumberTextValue && ((NumberTextValue)value).getPattern() != null)
				{
					baseStyle.setDataFormat(
						dataFormat.getFormat(
							getConvertedPattern(((NumberTextValue)value).getPattern())
							)
						);
				}
				else if (value instanceof DateTextValue && ((DateTextValue)value).getPattern() != null)
				{
					baseStyle.setDataFormat(
							dataFormat.getFormat(
								getConvertedPattern(((DateTextValue)value).getPattern())
								)
							);
					
				}
				
				HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(formula);
				endCreateCell(cellStyle);
				return;
			}
			catch(Exception e)
			{
				if(log.isWarnEnabled())
				{
					log.warn(e.getMessage());
				}
			}
		}
		
		

		if (isDetectCellType)
		{
			TextValue value = getTextValue(textElement, textStr);
			value.handle(new TextValueHandler()
			{
				public void handle(StringTextValue textValue)
				{
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					setRichTextStringCellValue(styledText, forecolor, textElement);
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
				setRichTextStringCellValue(styledText, forecolor, textElement);
			}
			endCreateCell(cellStyle);
		}
		else
		{
			HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
			setRichTextStringCellValue(styledText, forecolor, textElement);
			endCreateCell(cellStyle);
		}
	}


	protected HSSFCellStyle initCreateCell(JRExporterGridCell gridCell, int colIndex, int rowIndex, StyleInfo baseStyle)
	{
		HSSFCellStyle cellStyle = getLoadedCellStyle(baseStyle);
		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);
		cell = row.createCell(colIndex);
		return cellStyle;
	}

	protected void endCreateCell(HSSFCellStyle cellStyle)
	{
		cell.setCellStyle(cellStyle);
	}
	
/*
	protected final void setStringCellValue(String textStr)
	{
		//cell.setCellValue(JRStringUtil.replaceDosEOL(textStr));
		cell.setCellValue(textStr);
	}
*/
	
	protected final void setRichTextStringCellValue(JRStyledText styledText, short forecolor, JRFont defaultFont)
	{	
		if(styledText != null)
		{
			cell.setCellValue(getRichTextString(styledText, forecolor, defaultFont));
		}
	}

	protected HSSFRichTextString getRichTextString(JRStyledText styledText, short forecolor, JRFont defaultFont)
	{
		String text = styledText.getText();
		HSSFRichTextString richTextStr = new HSSFRichTextString(text);
		int runLimit = 0;
		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Map attributes = iterator.getAttributes();
			JRFont runFont = attributes.isEmpty()? defaultFont : new JRBaseFont(attributes);
			short runForecolor = attributes.get(TextAttribute.FOREGROUND) != null ? 
					getNearestColor((Color)attributes.get(TextAttribute.FOREGROUND)).getIndex() :
					forecolor;
			HSSFFont font = getLoadedFont(runFont, runForecolor, attributes);
			richTextStr.applyFont(iterator.getIndex(), runLimit, font);
			iterator.setIndex(runLimit);
		}
		return richTextStr;
	}
	protected void createMergeRegion(JRExporterGridCell gridCell, int colIndex, int rowIndex, HSSFCellStyle cellStyle)
	{
		if (gridCell.getColSpan() > 1 || gridCell.getRowSpan() > 1)
		{
			if (isCollapseRowSpan)
			{
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, colIndex, (colIndex + gridCell.getColSpan() - 1)));
			}
			else
			{
				sheet.addMergedRegion(new CellRangeAddress(rowIndex, (rowIndex + gridCell.getRowSpan() - 1), colIndex, (colIndex + gridCell.getColSpan() - 1)));
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
					HSSFCell spanCell = spanRow.getCell((colIndex + j));
					if (spanCell == null)
					{
						spanCell = spanRow.createCell((colIndex + j));
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
	protected HSSFFont getLoadedFont(JRFont font, short forecolor, Map attributes)
	{
		HSSFFont cellFont = null;

		String fontName = font.getFontName();
		if (fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = (String) fontMap.get(fontName);
		}
		short superscriptType = HSSFFont.SS_NONE;
		
		if( attributes != null && attributes.get(TextAttribute.SUPERSCRIPT) != null)
		{
			Object value = attributes.get(TextAttribute.SUPERSCRIPT);
			if(TextAttribute.SUPERSCRIPT_SUPER.equals(value))
			{
				superscriptType = HSSFFont.SS_SUPER;
			}
			else if(TextAttribute.SUPERSCRIPT_SUB.equals(value))
			{
				superscriptType = HSSFFont.SS_SUB;
			}
			
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
				(cf.getItalic() == font.isItalic()) &&
				(cf.getTypeOffset() == superscriptType)
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
			if (font.isItalic())
			{
				cellFont.setItalic(true);
			}

			cellFont.setTypeOffset(superscriptType);
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
				cellStyle.setBorderTop(box.borderStyle[BoxStyle.TOP]);
				cellStyle.setTopBorderColor(box.borderColour[BoxStyle.TOP]);
				cellStyle.setBorderLeft(box.borderStyle[BoxStyle.LEFT]);
				cellStyle.setLeftBorderColor(box.borderColour[BoxStyle.LEFT]);
				cellStyle.setBorderBottom(box.borderStyle[BoxStyle.BOTTOM]);
				cellStyle.setBottomBorderColor(box.borderColour[BoxStyle.BOTTOM]);
				cellStyle.setBorderRight(box.borderStyle[BoxStyle.RIGHT]);
				cellStyle.setRightBorderColor(box.borderColour[BoxStyle.RIGHT]);
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

	protected HSSFCellStyle getLoadedCellStyle(
		short mode,
		short backcolor,
		short horizontalAlignment,
		short verticalAlignment,
		short rotation,
		HSSFFont font,
		BoxStyle box
		)
	{
		StyleInfo style = new StyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, box);
		return getLoadedCellStyle(style);
	}

	/**
	 *
	 */
	protected static short getBorderStyle(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();

		if (lineWidth > 0f)
		{
			switch (pen.getLineStyle().byteValue())
			{
				case JRPen.LINE_STYLE_DOUBLE :
				{
					return HSSFCellStyle.BORDER_DOUBLE;
				}
				case JRPen.LINE_STYLE_DOTTED :
				{
					return HSSFCellStyle.BORDER_DOTTED;
				}
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

	protected void exportImage(JRPrintImage element, JRExporterGridCell gridCell, int colIndex, int rowIndex, int emptyCols) throws JRException
	{
		try
		{
			int topPadding =
				Math.max(element.getLineBox().getTopPadding().intValue(), getImageBorderCorrection(element.getLineBox().getTopPen()));
			int leftPadding =
				Math.max(element.getLineBox().getLeftPadding().intValue(), getImageBorderCorrection(element.getLineBox().getLeftPen()));
			int bottomPadding =
				Math.max(element.getLineBox().getBottomPadding().intValue(), getImageBorderCorrection(element.getLineBox().getBottomPen()));
			int rightPadding =
				Math.max(element.getLineBox().getRightPadding().intValue(), getImageBorderCorrection(element.getLineBox().getRightPen()));

			//pngEncoder.setImage( null );

			int availableImageWidth = element.getWidth() - leftPadding - rightPadding;
			availableImageWidth = availableImageWidth < 0 ? 0 : availableImageWidth;

			int availableImageHeight = element.getHeight() - topPadding - bottomPadding;
			availableImageHeight = availableImageHeight < 0 ? 0 : availableImageHeight;

			JRRenderable renderer = element.getRenderer();

			if (
				renderer != null &&
				availableImageWidth > 0 &&
				availableImageHeight > 0
				)
			{
				if (renderer.getType() == JRRenderable.TYPE_IMAGE)
				{
					// Image renderers are all asked for their image data and dimension at some point.
					// Better to test and replace the renderer now, in case of lazy load error.
					renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, element.getOnErrorType());
					if (renderer != null)
					{
						renderer = JRImageRenderer.getOnErrorRendererForDimension(renderer, element.getOnErrorType());
					}
				}
			}
			else
			{
				renderer = null;
			}

			if (renderer != null)
			{
				int normalWidth = availableImageWidth;
				int normalHeight = availableImageHeight;

				Dimension2D dimension = renderer.getDimension();
				if (dimension != null)
				{
					normalWidth = (int) dimension.getWidth();
					normalHeight = (int) dimension.getHeight();
				}

				float xalignFactor = 0f;
				switch (element.getHorizontalAlignment())
				{
					case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
					{
						xalignFactor = 1f;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_CENTER:
					{
						xalignFactor = 0.5f;
						break;
					}
					case JRAlignment.HORIZONTAL_ALIGN_LEFT:
					default:
					{
						xalignFactor = 0f;
						break;
					}
				}

				float yalignFactor = 0f;
				switch (element.getVerticalAlignment())
				{
					case JRAlignment.VERTICAL_ALIGN_BOTTOM:
					{
						yalignFactor = 1f;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_MIDDLE:
					{
						yalignFactor = 0.5f;
						break;
					}
					case JRAlignment.VERTICAL_ALIGN_TOP:
					default:
					{
						yalignFactor = 0f;
						break;
					}
				}

				BufferedImage bi = new BufferedImage(element.getWidth(), element.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D grx = bi.createGraphics();

				switch (element.getScaleImage())
				{
					case JRImage.SCALE_IMAGE_CLIP:
					{
						int xoffset = (int) (xalignFactor * (availableImageWidth - normalWidth));
						int yoffset = (int) (yalignFactor * (availableImageHeight - normalHeight));

						Shape oldClipShape = grx.getClip();

						grx.clip(
							new Rectangle(
								leftPadding,
								topPadding,
								availableImageWidth,
								availableImageHeight
								)
							);

						try
						{
							renderer.render(
								grx,
								new Rectangle(
									xoffset + leftPadding,
									yoffset + topPadding,
									normalWidth,
									normalHeight
									)
								);
						}
						finally
						{
							grx.setClip(oldClipShape);
						}

						break;
					}
					case JRImage.SCALE_IMAGE_FILL_FRAME:
					{
						renderer.render(
							grx,
							new Rectangle(
								leftPadding,
								topPadding,
								availableImageWidth,
								availableImageHeight
								)
							);

						break;
					}
					case JRImage.SCALE_IMAGE_RETAIN_SHAPE:
					default:
					{
						if (element.getHeight() > 0)
						{
							double ratio = (double) normalWidth / (double) normalHeight;

							if (ratio > (double) availableImageWidth / (double) availableImageHeight)
							{
								normalWidth = availableImageWidth;
								normalHeight = (int) (availableImageWidth / ratio);
							}
							else
							{
								normalWidth = (int) (availableImageHeight * ratio);
								normalHeight = availableImageHeight;
							}

							int xoffset = leftPadding + (int) (xalignFactor * (availableImageWidth - normalWidth));
							int yoffset = topPadding + (int) (yalignFactor * (availableImageHeight - normalHeight));

							renderer.render(
								grx,
								new Rectangle(
									xoffset,
									yoffset,
									normalWidth,
									normalHeight
									)
								);
						}

						break;
					}
				}

				short mode = backgroundMode;
				short backcolor = whiteIndex;
				if (gridCell.getCellBackcolor() != null)
				{
					mode = HSSFCellStyle.SOLID_FOREGROUND;
					backcolor = getNearestColor(gridCell.getCellBackcolor()).getIndex();
				}

				short forecolor = getNearestColor(element.getLineBox().getPen().getLineColor()).getIndex();

				if(element.getMode() == JRElement.MODE_OPAQUE ){
					backcolor = getNearestColor(element.getBackcolor()).getIndex();
				}

				HSSFCellStyle cellStyle =
					getLoadedCellStyle(
						mode,
						backcolor,
						HSSFCellStyle.ALIGN_LEFT,
						HSSFCellStyle.VERTICAL_TOP,
						(short)0,
						getLoadedFont(getDefaultFont(), forecolor, null),
						gridCell
						);

				createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

				cell = row.createCell(colIndex);
				cell.setCellStyle(cellStyle);

				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, 
						(short) colIndex, rowIndex, 
						(short) (colIndex + gridCell.getColSpan()), rowIndex + gridCell.getRowSpan());
				anchor.setAnchorType(2);
				//pngEncoder.setImage(bi);
				//int imgIndex = workbook.addPicture(pngEncoder.pngEncode(), HSSFWorkbook.PICTURE_TYPE_PNG);
				int imgIndex = workbook.addPicture(JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_PNG), HSSFWorkbook.PICTURE_TYPE_PNG);
				patriarch.createPicture(anchor, imgIndex);
			}
		}
		catch (Exception ex)
		{
			throw new JRException("The cell cannot be added", ex);
		}
		catch (Error err)
		{
			throw new JRException("The cell cannot be added", err);
		}
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
				getLoadedFont(getDefaultFont(), forecolor, null),
				gridCell
				);

		createMergeRegion(gridCell, x, y, cellStyle);

		cell = row.createCell(x);
		cell.setCellStyle(cellStyle);
	}


	protected ExporterNature getNature()
	{
		return nature;
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

	protected static class BoxStyle
	{
		protected static final int TOP = 0;
		protected static final int LEFT = 1;
		protected static final int BOTTOM = 2;
		protected static final int RIGHT = 3;

		protected short[] borderStyle = new short[4];
		protected short[] borderColour = new short[4];
		private int hash;

		public BoxStyle(int side, JRPen pen)
		{
			borderStyle[side] = getBorderStyle(pen);
			borderColour[side] = getNearestColor(pen.getLineColor()).getIndex();

			hash = computeHash();
		}

		public BoxStyle(JRExporterGridCell gridCell)
		{
			JRLineBox lineBox = gridCell.getBox();
			if (lineBox != null)
				setBox(lineBox);
			JRPrintElement element = gridCell.getElement();
			if (element instanceof JRCommonGraphicElement)
				setPen(((JRCommonGraphicElement)element).getLinePen());

			hash = computeHash();
		}

		public void setBox(JRLineBox box)
		{
			borderStyle[TOP] = getBorderStyle(box.getTopPen());
			borderColour[TOP] = getNearestColor(box.getTopPen().getLineColor()).getIndex();

			borderStyle[BOTTOM] = getBorderStyle(box.getBottomPen());
			borderColour[BOTTOM] = getNearestColor(box.getBottomPen().getLineColor()).getIndex();

			borderStyle[LEFT] = getBorderStyle(box.getLeftPen());
			borderColour[LEFT] = getNearestColor(box.getLeftPen().getLineColor()).getIndex();

			borderStyle[RIGHT] = getBorderStyle(box.getRightPen());
			borderColour[RIGHT] = getNearestColor(box.getRightPen().getLineColor()).getIndex();

			hash = computeHash();
		}

		public void setPen(JRPen pen)
		{
			if (
				borderStyle[TOP] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[LEFT] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[BOTTOM] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[RIGHT] == HSSFCellStyle.BORDER_NONE
				)
			{
				short style = getBorderStyle(pen);
				short colour = getNearestColor(pen.getLineColor()).getIndex();

				borderStyle[TOP] = style;
				borderStyle[BOTTOM] = style;
				borderStyle[LEFT] = style;
				borderStyle[RIGHT] = style;

				borderColour[TOP] = colour;
				borderColour[BOTTOM] = colour;
				borderColour[LEFT] = colour;
				borderColour[RIGHT] = colour;
			}

			hash = computeHash();
		}

		private int computeHash()
		{
			int hashCode = borderStyle[TOP];
			hashCode = 31*hashCode + borderColour[TOP];
			hashCode = 31*hashCode + borderStyle[BOTTOM];
			hashCode = 31*hashCode + borderColour[BOTTOM];
			hashCode = 31*hashCode + borderStyle[LEFT];
			hashCode = 31*hashCode + borderColour[LEFT];
			hashCode = 31*hashCode + borderStyle[RIGHT];
			hashCode = 31*hashCode + borderColour[RIGHT];
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
				b.borderStyle[TOP] == borderStyle[TOP] &&
				b.borderColour[TOP] == borderColour[TOP] &&
				b.borderStyle[BOTTOM] == borderStyle[BOTTOM] &&
				b.borderColour[BOTTOM] == borderColour[BOTTOM] &&
				b.borderStyle[LEFT] == borderStyle[LEFT] &&
				b.borderColour[LEFT] == borderColour[LEFT] &&
				b.borderStyle[RIGHT] == borderStyle[RIGHT] &&
				b.borderColour[RIGHT] == borderColour[RIGHT];
		}

		public String toString()
		{
			return "(" +
				borderStyle[TOP] + "/" + borderColour[TOP] + "," +
				borderStyle[BOTTOM] + "/" + borderColour[BOTTOM] + "," +
				borderStyle[LEFT] + "/" + borderColour[LEFT] + "," +
				borderStyle[RIGHT] + "/" + borderColour[RIGHT] + ")";
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

		public StyleInfo(
			short mode,
			short backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			HSSFFont font,
			JRExporterGridCell gridCell
			)
		{
			this(
				mode,
				backcolor,
				horizontalAlignment,
				verticalAlignment,
				rotation,
				font,
				new BoxStyle(gridCell)
				);
		}

		public StyleInfo(
			short mode,
			short backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			HSSFFont font,
			BoxStyle box
			)
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
