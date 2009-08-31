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
import net.sf.jasperreports.engine.JasperPrint;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Exports a JasperReports document to XLSX format. It has binary output type and exports the document to
 * a grid-based layout.
 * <p>
 * Since classic AWT fonts can be sometimes very different from system fonts (which are used by XLSX viewers),
 * a font mapping feature was added. By using the {@link net.sf.jasperreports.engine.JRExporterParameter#FONT_MAP} parameter, a logical
 * font like "sansserif" can be mapped to a system specific font, like "DejaVu Serif". Both map keys and values are strings.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JRXlsxExporter extends JRXlsAbstractExporter
{

	private static final Log log = LogFactory.getLog(JRXlsAbstractExporter.class);
	
	private static Map xssfColorsCache = new ReferenceMap();

	protected Map loadedCellStyles = new HashMap();

	/**
	 *
	 */
	protected XSSFWorkbook workbook = null;
	protected Sheet sheet = null;
	protected Row row = null;
	protected Cell cell = null;
	protected XSSFCellStyle emptyCellStyle = null;

	/**
	 *
	 */
	protected XSSFColor whiteColor = new XSSFColor(Color.WHITE);
	protected short backgroundMode = CellStyle.SOLID_FOREGROUND;

	protected DataFormat dataFormat = null;
	protected Map formatPatternsMap = null;

	protected ExporterNature nature = null;

	protected Drawing patriarch = null;
	
	protected String password = null;

	protected void setParameters()
	{
		super.setParameters();

		formatPatternsMap = (Map)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);

		nature = new JRXlsExporterNature(filter, isIgnoreGraphics, isIgnorePageMargins);
		
		password = 
			getStringParameter(
				JExcelApiExporterParameter.PASSWORD,
				JExcelApiExporterParameter.PROPERTY_PASSWORD
				);
		
	}


	protected void setBackground()
	{
		if (!isWhitePageBackground)
		{
			backgroundMode = CellStyle.NO_FILL;
		}
	}


	protected void openWorkbook(OutputStream os)
	{
		workbook = new XSSFWorkbook();
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor(whiteColor);
		emptyCellStyle.setFillPattern(backgroundMode);
		dataFormat = workbook.getCreationHelper().createDataFormat();
	}


	protected void createSheet(String name)
	{
		sheet = workbook.createSheet(name);
		patriarch = sheet.createDrawingPatriarch();
		sheet.getPrintSetup().setLandscape(jasperPrint.getOrientation() == JRReport.ORIENTATION_LANDSCAPE);
		short paperSize = getSuitablePaperSize(jasperPrint);

		if(paperSize != -1)
		{
			sheet.getPrintSetup().setPaperSize(paperSize);
		}
		if(password != null)
		{
			//sheet.protectSheet(password);
		}
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
		Cell emptyCell = row.getCell(colIndex);
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
		XSSFColor backcolor = whiteColor;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = CellStyle.SOLID_FOREGROUND;
			backcolor = getXSSFColor(gridCell.getCellBackcolor());
		}

		XSSFColor forecolor = new XSSFColor(Color.BLACK);
		if (gridCell.getForecolor() != null)
		{
			forecolor = getXSSFColor(gridCell.getForecolor());
		}

		XSSFCellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				XSSFCellStyle.ALIGN_LEFT,
				XSSFCellStyle.VERTICAL_TOP,
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
		XSSFColor forecolor = getXSSFColor(line.getLinePen().getLineColor());

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
		XSSFColor backcolor = whiteColor;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = CellStyle.SOLID_FOREGROUND;
			backcolor = getXSSFColor(gridCell.getCellBackcolor());
		}

		CellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				CellStyle.ALIGN_LEFT,
				CellStyle.VERTICAL_TOP,
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
		XSSFColor forecolor = getXSSFColor(element.getLinePen().getLineColor());

		short mode = backgroundMode;
		XSSFColor backcolor = whiteColor;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = CellStyle.SOLID_FOREGROUND;
			backcolor = getXSSFColor(gridCell.getCellBackcolor());
		}

		CellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				CellStyle.ALIGN_LEFT,
				CellStyle.VERTICAL_TOP,
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

		XSSFColor forecolor = getXSSFColor(textElement.getForecolor());

		TextAlignHolder textAlignHolder = getTextAlignHolder(textElement);
		short horizontalAlignment = getHorizontalAlignment(textAlignHolder);
		short verticalAlignment = getVerticalAlignment(textAlignHolder);
		short rotation = getRotation(textAlignHolder);

		short mode = backgroundMode;
		XSSFColor backcolor = whiteColor;
		if (gridCell.getCellBackcolor() != null)
		{
			mode = CellStyle.SOLID_FOREGROUND;
			backcolor = getXSSFColor(gridCell.getCellBackcolor());
		}

		XSSFStyleInfo baseStyle =
			new XSSFStyleInfo(
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


	protected void createTextCell(final JRPrintText textElement, final JRExporterGridCell gridCell, final int colIndex, final int rowIndex, final JRStyledText styledText, final XSSFStyleInfo baseStyle, final XSSFColor forecolor) throws JRException
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
				
				CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				cell.setCellFormula(formula);
				//workbook.getCreationHelper().createFormulaEvaluator().evaluateFormulaCell(cell);
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
					CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
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

					CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(Cell.CELL_TYPE_BLANK);
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
					CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(Cell.CELL_TYPE_BLANK);
					}
					else
					{
						cell.setCellValue(textValue.getValue());
					}
					endCreateCell(cellStyle);
				}

				public void handle(BooleanTextValue textValue)
				{
					CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (textValue.getValue() == null)
					{
						cell.setCellType(Cell.CELL_TYPE_BLANK);
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
			CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
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
			CellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
			setRichTextStringCellValue(styledText, forecolor, textElement);
			endCreateCell(cellStyle);
		}
	}


	protected CellStyle initCreateCell(JRExporterGridCell gridCell, int colIndex, int rowIndex, XSSFStyleInfo baseStyle)
	{
		CellStyle cellStyle = getLoadedCellStyle(baseStyle);
		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);
		cell = row.createCell(colIndex);
		return cellStyle;
	}

	protected void endCreateCell(CellStyle cellStyle)
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
	
	protected final void setRichTextStringCellValue(JRStyledText styledText, XSSFColor forecolor, JRFont defaultFont)
	{	
		if(styledText != null)
		{
			cell.setCellValue(getRichTextString(styledText, forecolor, defaultFont));
		}
	}

	protected RichTextString getRichTextString(JRStyledText styledText, XSSFColor forecolor, JRFont defaultFont)
	{
		String text = styledText.getText();
		CreationHelper createHelper = workbook.getCreationHelper();
		RichTextString richTextStr = createHelper.createRichTextString(text);
		int runLimit = 0;
		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Map attributes = iterator.getAttributes();
			JRFont runFont = attributes.isEmpty()? defaultFont : new JRBaseFont(attributes);
			XSSFColor runForecolor = attributes.get(TextAttribute.FOREGROUND) != null ? 
					getXSSFColor((Color)attributes.get(TextAttribute.FOREGROUND)) :
					forecolor;
			XSSFFont font = getLoadedFont(runFont, runForecolor, attributes);
			richTextStr.applyFont(iterator.getIndex(), runLimit, font);
			iterator.setIndex(runLimit);
		}
		return richTextStr;
	}
	protected void createMergeRegion(JRExporterGridCell gridCell, int colIndex, int rowIndex, CellStyle cellStyle)
	{
		int rowSpan = isCollapseRowSpan ? 1 : gridCell.getRowSpan();
		if (gridCell.getColSpan() > 1 || rowSpan > 1)
		{
			sheet.addMergedRegion(new CellRangeAddress(rowIndex, (rowIndex + rowSpan - 1), 
					colIndex, (colIndex + gridCell.getColSpan() - 1)));

			for(int i = 0; i < rowSpan; i++)
			{
				Row spanRow = sheet.getRow(rowIndex + i);
				if (spanRow == null)
				{
					spanRow = sheet.createRow(rowIndex + i);
				}
				for(int j = 0; j < gridCell.getColSpan(); j++)
				{
					Cell spanCell = spanRow.getCell((colIndex + j));
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
				return CellStyle.ALIGN_RIGHT;
			case JRAlignment.HORIZONTAL_ALIGN_CENTER:
				return CellStyle.ALIGN_CENTER;
			case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
				return CellStyle.ALIGN_JUSTIFY;
			case JRAlignment.HORIZONTAL_ALIGN_LEFT:
			default:
				return CellStyle.ALIGN_LEFT;
		}
	}

	private short getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
			case JRAlignment.VERTICAL_ALIGN_BOTTOM:
				return CellStyle.VERTICAL_BOTTOM;
			case JRAlignment.VERTICAL_ALIGN_MIDDLE:
				return CellStyle.VERTICAL_CENTER;
			case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
				return CellStyle.VERTICAL_JUSTIFY;
			case JRAlignment.VERTICAL_ALIGN_TOP:
			default:
				return CellStyle.VERTICAL_TOP;
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
	protected static XSSFColor getXSSFColor(Color awtColor)
	{
		XSSFColor color =  (XSSFColor)xssfColorsCache.get(awtColor);
		if (color == null)
		{
			color = new XSSFColor();
			color.setRgb(new byte[] {(byte)awtColor.getAlpha(),(byte)awtColor.getRed(),(byte)awtColor.getGreen(),(byte)awtColor.getBlue()});
			xssfColorsCache.put(awtColor, color);
		}
		return color;
		
	}


	/**
	 *
	 */
	protected XSSFFont getLoadedFont(JRFont font, XSSFColor forecolor, Map attributes)
	{
		XSSFFont cellFont = null;

		String fontName = font.getFontName();
		if (fontMap != null && fontMap.containsKey(fontName))
		{
			fontName = (String) fontMap.get(fontName);
		}
		short superscriptType = Font.SS_NONE;
		
		if( attributes != null && attributes.get(TextAttribute.SUPERSCRIPT) != null)
		{
			Object value = attributes.get(TextAttribute.SUPERSCRIPT);
			if(TextAttribute.SUPERSCRIPT_SUPER.equals(value))
			{
				superscriptType = Font.SS_SUPER;
			}
			else if(TextAttribute.SUPERSCRIPT_SUB.equals(value))
			{
				superscriptType = Font.SS_SUB;
			}
			
		}
		for (int i = 0; i < loadedFonts.size(); i++)
		{
			XSSFFont cf = (XSSFFont)loadedFonts.get(i);

			short fontSize = (short)font.getFontSize();
			if (isFontSizeFixEnabled)
				fontSize -= 1;

			if (
				cf.getFontName().equals(fontName) &&
				(cf.getXSSFColor().equals(forecolor)) &&
				(cf.getFontHeightInPoints() == fontSize) &&
				((cf.getUnderline() == Font.U_SINGLE)?(font.isUnderline()):(!font.isUnderline())) &&
				(cf.getStrikeout() == font.isStrikeThrough()) &&
				((cf.getBoldweight() == Font.BOLDWEIGHT_BOLD)?(font.isBold()):(!font.isBold())) &&
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
			cellFont = (XSSFFont)workbook.createFont();

			cellFont.setFontName(fontName);
			cellFont.setColor(forecolor);

			short fontSize = (short)font.getFontSize();
			if (isFontSizeFixEnabled)
				fontSize -= 1;

			cellFont.setFontHeightInPoints(fontSize);

			if (font.isUnderline())
			{
				cellFont.setUnderline(Font.U_SINGLE);
			}
			if (font.isStrikeThrough())
			{
				cellFont.setStrikeout(true);
			}
			if (font.isBold())
			{
				cellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
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


	protected XSSFCellStyle getLoadedCellStyle(XSSFStyleInfo style)
	{
		XSSFCellStyle cellStyle = (XSSFCellStyle) loadedCellStyles.get(style);
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

	protected XSSFCellStyle getLoadedCellStyle(
			short mode,
			XSSFColor backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			XSSFFont font,
			JRExporterGridCell gridCell
			)
	{
		XSSFStyleInfo style = new XSSFStyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, gridCell);
		return getLoadedCellStyle(style);
	}

	protected XSSFCellStyle getLoadedCellStyle(
		short mode,
		XSSFColor backcolor,
		short horizontalAlignment,
		short verticalAlignment,
		short rotation,
		XSSFFont font,
		BoxStyle box
		)
	{
		XSSFStyleInfo style = new XSSFStyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, box);
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
					return CellStyle.BORDER_DOUBLE;
				}
				case JRPen.LINE_STYLE_DOTTED :
				{
					return CellStyle.BORDER_DOTTED;
				}
				case JRPen.LINE_STYLE_DASHED :
				{
					return CellStyle.BORDER_DASHED;
				}
				case JRPen.LINE_STYLE_SOLID :
				default :
				{
					if (lineWidth >= 2f)
					{
						return CellStyle.BORDER_THICK;
					}
					else if (lineWidth >= 1f)
					{
						return CellStyle.BORDER_MEDIUM;//FIXMEBORDER there is also CellStyle.BORDER_MEDIUM_DASHED available
					}
					else if (lineWidth >= 0.5f)
					{
						return CellStyle.BORDER_THIN;
					}

					return CellStyle.BORDER_HAIR;
				}
			}
		}

		return CellStyle.BORDER_NONE;
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
				XSSFColor backcolor = whiteColor;
				if (gridCell.getCellBackcolor() != null)
				{
					mode = CellStyle.SOLID_FOREGROUND;
					backcolor = getXSSFColor(gridCell.getCellBackcolor());
				}

				XSSFColor forecolor = getXSSFColor(element.getLineBox().getPen().getLineColor());

				if(element.getMode() == JRElement.MODE_OPAQUE ){
					backcolor = getXSSFColor(element.getBackcolor());
				}

				CellStyle cellStyle =
					getLoadedCellStyle(
						mode,
						backcolor,
						CellStyle.ALIGN_LEFT,
						CellStyle.VERTICAL_TOP,
						(short)0,
						getLoadedFont(getDefaultFont(), forecolor, null),
						gridCell
						);

				createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

				cell = row.createCell(colIndex);
				cell.setCellStyle(cellStyle);

				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 
						(short) colIndex, rowIndex, 
						(short) (colIndex + gridCell.getColSpan()), 
						rowIndex + (isCollapseRowSpan ? 1 : gridCell.getRowSpan()));
				//anchor.setAnchorType(2);
				//pngEncoder.setImage(bi);
				//int imgIndex = workbook.addPicture(pngEncoder.pngEncode(), HSSFWorkbook.PICTURE_TYPE_PNG);
				int imgIndex = workbook.addPicture(JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_PNG), XSSFWorkbook.PICTURE_TYPE_PNG);
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
		XSSFColor backcolor = whiteColor;
		if (frame.getMode() == JRElement.MODE_OPAQUE)
		{
			mode = CellStyle.SOLID_FOREGROUND;
			backcolor = getXSSFColor(frame.getBackcolor());
		}

		XSSFColor forecolor = getXSSFColor(frame.getForecolor());

		CellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				CellStyle.ALIGN_LEFT,
				CellStyle.VERTICAL_TOP,
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

	private final short getSuitablePaperSize(JasperPrint jasP)
	{

		if (jasP == null)
			return -1;

		long width = 0;
		long height = 0;
		short ps = -1;

		if ((jasP.getPageWidth() != 0) && (jasP.getPageHeight() != 0))
		{

			double dWidth = (jasP.getPageWidth() / 72.0);
			double dHeight = (jasP.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by POI Api yet.
			// A3 papersize also not supported by POI Api yet.
			for (int i = 4; i < 6; i++)
			{
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height)))
				{
					if (i == 4)
						ps = PrintSetup.A4_PAPERSIZE;
					else if (i == 5)
						ps = PrintSetup.A5_PAPERSIZE;
					break;
				}
			}
			
			//envelope sizes
			if (ps == -1)
			{
				// ISO 269 sizes - "Envelope DL" (110 � 220 mm)
				if (((width == 110) && (height == 220)) || ((width == 220) && (height == 110)))
				{
					ps = PrintSetup.ENVELOPE_DL_PAPERSIZE;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == -1)
			{
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
				{
					ps = PrintSetup.LETTER_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
				{
					ps = PrintSetup.LEGAL_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				else if (((width == 190) && (height == 254)) || ((width == 254) && (height == 190)))
				{
					ps = PrintSetup.EXECUTIVE_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supported by POI Api yet.
				
			}
		}
		return ps;
	}

	protected static class BoxStyle
	{
		protected static final int TOP = 0;
		protected static final int LEFT = 1;
		protected static final int BOTTOM = 2;
		protected static final int RIGHT = 3;

		protected short[] borderStyle = new short[4];
		protected XSSFColor[] borderColour = new XSSFColor[4];
		private int hash;

		public BoxStyle(int side, JRPen pen)
		{
			borderStyle[side] = getBorderStyle(pen);
			borderColour[side] = getXSSFColor(pen.getLineColor());

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
			borderColour[TOP] = getXSSFColor(box.getTopPen().getLineColor());

			borderStyle[BOTTOM] = getBorderStyle(box.getBottomPen());
			borderColour[BOTTOM] = getXSSFColor(box.getBottomPen().getLineColor());

			borderStyle[LEFT] = getBorderStyle(box.getLeftPen());
			borderColour[LEFT] = getXSSFColor(box.getLeftPen().getLineColor());

			borderStyle[RIGHT] = getBorderStyle(box.getRightPen());
			borderColour[RIGHT] = getXSSFColor(box.getRightPen().getLineColor());

			hash = computeHash();
		}

		public void setPen(JRPen pen)
		{
			if (
				borderStyle[TOP] == CellStyle.BORDER_NONE
				&& borderStyle[LEFT] == CellStyle.BORDER_NONE
				&& borderStyle[BOTTOM] == CellStyle.BORDER_NONE
				&& borderStyle[RIGHT] == CellStyle.BORDER_NONE
				)
			{
				short style = getBorderStyle(pen);
				XSSFColor colour = getXSSFColor(pen.getLineColor());

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
			hashCode = 31*hashCode + (borderColour[TOP] == null ? 0 : borderColour[TOP].hashCode());
			hashCode = 31*hashCode + borderStyle[BOTTOM];
			hashCode = 31*hashCode + (borderColour[BOTTOM] == null ? 0 : borderColour[BOTTOM].hashCode());
			hashCode = 31*hashCode + borderStyle[LEFT];
			hashCode = 31*hashCode + (borderColour[LEFT] == null ? 0 : borderColour[LEFT].hashCode());
			hashCode = 31*hashCode + borderStyle[RIGHT];
			hashCode = 31*hashCode + (borderColour[RIGHT] == null ? 0 : borderColour[RIGHT].hashCode());
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
				(b.borderColour[TOP] == null ? borderColour[TOP] == null : (borderColour[TOP] != null && b.borderColour[TOP].equals(borderColour[TOP]))) &&
				b.borderStyle[BOTTOM] == borderStyle[BOTTOM] &&
				(b.borderColour[BOTTOM] == null ? borderColour[BOTTOM] == null : (borderColour[BOTTOM] != null && b.borderColour[BOTTOM].equals(borderColour[BOTTOM]))) &&
				b.borderStyle[LEFT] == borderStyle[LEFT] &&
				(b.borderColour[LEFT] == null ? borderColour[LEFT] == null : (borderColour[LEFT] != null && b.borderColour[LEFT].equals(borderColour[LEFT]))) &&
				b.borderStyle[RIGHT] == borderStyle[RIGHT] &&
				(b.borderColour[RIGHT] == null ? borderColour[RIGHT] == null : (borderColour[RIGHT] != null && b.borderColour[RIGHT].equals(borderColour[RIGHT])));
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


	protected static class XSSFStyleInfo
	{
		protected final short mode;
		protected final XSSFColor backcolor;
		protected final short horizontalAlignment;
		protected final short verticalAlignment;
		protected final short rotation;
		protected final XSSFFont font;
		protected final BoxStyle box;
		private short dataFormat = -1;
		private int hashCode;

		public XSSFStyleInfo(
			short mode,
			XSSFColor backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			XSSFFont font,
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

		public XSSFStyleInfo(
			short mode,
			XSSFColor backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			XSSFFont font,
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
			hash = 31*hash + (backcolor == null ? 0 : backcolor.hashCode());
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
			XSSFStyleInfo s = (XSSFStyleInfo) o;

			return s.mode == mode
					&& (s.backcolor == null ? backcolor == null : (backcolor != null && s.backcolor.equals(backcolor)))
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
