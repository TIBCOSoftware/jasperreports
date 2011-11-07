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

/*
 * Contributor: Manuel Paul <mpaul@ratundtat.com>,
 *				Rat & Tat Beratungsgesellschaft mbH,
 *				Muehlenkamp 6c,
 *				22303 Hamburg,
 *				Germany.
 */
package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.CellView;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.BoldStyle;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.Pattern;
import jxl.format.RGB;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.CellValue;
import jxl.write.biff.RowsExceededException;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRFontUtil;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class JExcelApiMetadataExporter extends JRXlsAbstractMetadataExporter
{

	private static final Log log = LogFactory.getLog(JExcelApiMetadataExporter.class);

	/**
	 * Boolean property enabling the JExcelApiMetadataExporter to use temporary files when creating large documents.
	 * <p/>
	 * This property is by default not set (<code>false</code>).
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_USE_TEMP_FILE = JRProperties.PROPERTY_PREFIX + "export.jxl.use.temp.file";

	/**
	 * Boolean property specifying whether the cell format pattern is user-defined.
	 * When set to true, the exporter will assume that the specified pattern is well defined. 
	 * If the pattern is invalid, it won't be taken into account by the Excel file viewer.
	 * 
	 * @see JRProperties
	 */
	public static final String PROPERTY_COMPLEX_FORMAT = JRProperties.PROPERTY_PREFIX + "export.jxl.cell.complex.format";

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String JXL_EXPORTER_KEY = JRProperties.PROPERTY_PREFIX + "jxl";
	

	protected static final Colour WHITE = Colour.WHITE;
	protected static final Colour BLACK = Colour.BLACK;

	protected static final String EMPTY_SHEET_NAME = "Sheet1";

	private static Map<Color,Colour> colorsCache = new ReferenceMap();

	private static final Colour[] FIXED_COLOURS = new Colour[] {WHITE, BLACK, Colour.PALETTE_BLACK,
		Colour.DEFAULT_BACKGROUND, Colour.DEFAULT_BACKGROUND1, Colour.AUTOMATIC, Colour.UNKNOWN};

	private Map<StyleInfo,WritableCellFormat> loadedCellStyles = new HashMap<StyleInfo,WritableCellFormat>();

	private WritableWorkbook workbook;

	private WritableSheet sheet;

	private Pattern backgroundMode = Pattern.SOLID;

	private Map<String,NumberFormat> numberFormats;
	private Map<String,DateFormat> dateFormats;

	protected Map<Color,Colour> workbookColours = new HashMap<Color,Colour>();
	protected Map<Colour,RGB> usedColours = new HashMap<Colour,RGB>();
	protected String password;
	
	protected ExporterNature nature;
	protected boolean useTempFile;
	protected boolean complexFormat;
	
	protected class ExporterContext extends BaseExporterContext implements JExcelApiExporterContext
	{
		public String getExportPropertiesPrefix()
		{
			return XLS_EXPORTER_PROPERTIES_PREFIX;
		}
	}
	
	protected JExcelApiExporterContext exporterContext = new ExporterContext();
	

	public JExcelApiMetadataExporter()
	{
		numberFormats = new HashMap<String,NumberFormat>();
		dateFormats = new HashMap<String,DateFormat>();
	}

	protected void setParameters()
	{
		super.setParameters();

		if (createCustomPalette)
		{
			initCustomPalette();
		}

		password = 
			getStringParameter(
				JExcelApiExporterParameter.PASSWORD,
				JExcelApiExporterParameter.PROPERTY_PASSWORD
				);
		
		nature = new JExcelApiExporterNature(filter, isIgnoreGraphics, isIgnorePageMargins);
		
		useTempFile = 
			JRProperties.getBooleanProperty(
				jasperPrint,
				PROPERTY_USE_TEMP_FILE,
				false
				);
		
		complexFormat = 
			JRProperties.getBooleanProperty(
				jasperPrint,
				PROPERTY_COMPLEX_FORMAT,
				false
				);
	}

	protected void initCustomPalette()
	{
		//mark "fixed" colours as always used
		for (int i = 0; i < FIXED_COLOURS.length; i++)
		{
			Colour colour = FIXED_COLOURS[i];
			setColourUsed(colour);
		}
	}

	protected void setColourUsed(Colour colour)
	{
		usedColours.put(colour, colour.getDefaultRGB());
	}

	protected void setColourUsed(Colour colour, Color reportColour)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Modifying palette colour " + colour.getValue() + " to " + reportColour);
		}

		int red = reportColour.getRed();
		int green = reportColour.getGreen();
		int blue = reportColour.getBlue();

		workbook.setColourRGB(colour, red, green, blue);

		RGB customRGB = new RGB(red, green, blue);
		usedColours.put(colour, customRGB);
	}

	protected void setBackground()
	{
		if (isWhitePageBackground)
		{
			this.backgroundMode = Pattern.SOLID;
		}
		else
		{
			this.backgroundMode = Pattern.NONE;
		}
	}

	protected void openWorkbook(OutputStream os) throws JRException
	{
		try
		{
			WorkbookSettings settings = new WorkbookSettings();
			settings.setUseTemporaryFileDuringWrite(useTempFile);
			workbook = Workbook.createWorkbook(os, settings);
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void createSheet(String name)
	{
		sheet = workbook.createSheet(name, Integer.MAX_VALUE);
		setSheetSettings(sheet);
	}

	protected void closeWorkbook(OutputStream os) throws JRException
	{
		if (sheet == null)//empty document
		{
			//creating an empty sheet so that write() doesn't fail
			workbook.createSheet(EMPTY_SHEET_NAME, Integer.MAX_VALUE);
		}

		try
		{
			workbook.write();
			workbook.close();
		}
		catch (IOException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
		catch (WriteException e)
		{
			throw new JRException("Error generating XLS report : " + jasperPrint.getName(), e);
		}
	}

	protected void setColumnWidth(int col, int width, boolean autoFit)
	{
		if (!autoFit)
		{
			CellView cv = sheet.getColumnView(col);
			if(cv == null || cv.getSize() < 43 * width)
			{
				cv = new CellView();
				cv.setSize(43 * width);
				sheet.setColumnView(col, cv);
			}
		}
	}

	protected void updateColumn(int col, boolean autoFit)
	{
		if (autoFit)
		{
			CellView cv = new CellView();
			cv.setAutosize(true);
			sheet.setColumnView(col, cv);
		}
	}

	protected void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException
	{
		if (yCut != null && yCut.isAutoFit())
		{
			try
			{
				CellView cv = sheet.getRowView(rowIndex) == null ? new CellView() : sheet.getRowView(rowIndex);
				cv.setAutosize(true);
				sheet.setRowView(rowIndex, cv);
			}
			catch (RowsExceededException e)
			{
				throw new JRException("Too many rows in sheet " + sheet.getName() + ": " + rowIndex, e);
			}
		}
		else
		{
			try
			{
				CellView cv = sheet.getRowView(rowIndex);
				if(cv == null || cv.getSize() < LengthUtil.twip(lastRowHeight))
				{
					sheet.setRowView(rowIndex, LengthUtil.twip(lastRowHeight));
				}
			}
			catch (RowsExceededException e)
			{
				throw new JRException("Too many rows in sheet " + sheet.getName() + ": " + rowIndex, e);
			}
		}
	}

	protected void removeColumn(int col)
	{
		sheet.removeColumn(col);
	}

	protected void addBlankCell(WritableCellFormat baseStyleFormat, Map<String, Object> cellValueMap, String currentColumnName) throws JRException
	{
		if(baseStyleFormat != null)
		{
			int colIndex = columnNamesMap.get(currentColumnName);
			cellValueMap.put(currentColumnName, new Blank(colIndex, rowIndex, baseStyleFormat));
		}
	}
	
	protected void writeCurrentRow(Map<String, Object> currentRow, Map<String, Object> repeatedValues)  throws JRException
	{
		for(int i = 0; i< columnNames.size(); i++)
		{
			CellValue cellValue = (CellValue)currentRow.get(columnNames.get(i)) == null 
				? (repeatedValues.get(columnNames.get(i)) != null 
						? (CellValue)(((CellValue)repeatedValues.get(columnNames.get(i))).copyTo(i, rowIndex)) 
						: null)
				: (CellValue)currentRow.get(columnNames.get(i));
			if(cellValue != null)
			{
				try
				{
					sheet.addCell(cellValue);
				}
				catch (RowsExceededException e)
				{
					throw new JRException("There are too many rows in the current sheet", e);
				}
				catch (WriteException e)
				{
					throw new JRException(e);
				}
			}
		}
		++rowIndex;
	}
	

	protected void exportLine(JRPrintLine line) throws JRException
	{
		String currentColumnName = line.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = JRProperties.getBooleanProperty(line, JRXlsAbstractMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), line.getWidth());
			setRowHeight(rowIndex, line.getHeight());
			
			Colour forecolor2 = getWorkbookColour(line.getLinePen().getLineColor());
			WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor2.getValue(), getLocale());
			
			Colour backcolor = WHITE;
			Pattern mode = this.backgroundMode;
	
			if (!isIgnoreCellBackground && line.getBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(line.getBackcolor(), true);
			}
			
			int side = BoxStyle.TOP;
			float ratio = line.getWidth() / line.getHeight();
			if (ratio > 1)
			{
				if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
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
				if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)
				{
					side = BoxStyle.LEFT;
				}
				else
				{
					side = BoxStyle.RIGHT;
				}
			}
			BoxStyle boxStyle = new BoxStyle(side, line.getLinePen());
			
			WritableCellFormat cellStyle2 = 
				getLoadedCellStyle(
					mode, 
					backcolor, 
					cellFont2, 
					boxStyle,
					isCellLocked(line)
					);
			
			addBlankElement(cellStyle2, repeatValue, currentColumnName);
		}
	}

	protected void exportRectangle(JRPrintGraphicElement element) throws JRException
	{
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = JRProperties.getBooleanProperty(element, JRXlsAbstractMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), element.getWidth());
			setRowHeight(rowIndex, element.getHeight());
			
			Colour backcolor = WHITE;
			Pattern mode = this.backgroundMode;
	
			if (!isIgnoreCellBackground && element.getBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(element.getBackcolor(), true);
			}
	
			Colour forecolor = getWorkbookColour(element.getLinePen().getLineColor());
			WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor.getValue(), getLocale());
			BoxStyle boxStyle = new BoxStyle(element.getStyle());
			
			WritableCellFormat cellStyle2 = 
				getLoadedCellStyle(
					mode, 
					backcolor, 
					cellFont2, 
					boxStyle,
					isCellLocked(element)
					);
			
			addBlankElement(cellStyle2, repeatValue, currentColumnName);
		}
	}


	protected void exportText(JRPrintText textElement) throws JRException
	{
		String currentColumnName = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			String currentColumnData = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_DATA);
			boolean repeatValue = JRProperties.getBooleanProperty(textElement, JRXlsAbstractMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), textElement.getWidth());
			setRowHeight(rowIndex, textElement.getHeight());

			Color textForecolor = textElement.getForecolor() == null ? Color.BLACK : textElement.getForecolor();
			Colour forecolor = getWorkbookColour(textForecolor);
			WritableFont cellFont = this.getLoadedFont(textElement, forecolor.getValue(), getTextLocale(textElement));

			TextAlignHolder alignment = getTextAlignHolder(textElement);
			int horizontalAlignment = getHorizontalAlignment(alignment);
			int verticalAlignment = getVerticalAlignment(alignment);
			int rotation = getRotation(alignment);

			Pattern mode = this.backgroundMode;
			Colour backcolor = WHITE;

			if (!isIgnoreCellBackground && textElement.getBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(textElement.getBackcolor(), true);
			}

			StyleInfo baseStyle =
				new StyleInfo(
					mode, 
					backcolor,
					horizontalAlignment, 
					verticalAlignment,
					rotation, 
					cellFont,
					textElement,
					isWrapText(textElement),
					isCellLocked(textElement)
					);
			
			String href = null;
			JRHyperlinkProducer customHandler = getHyperlinkProducer(textElement);
			if (customHandler == null)
			{
				switch (textElement.getHyperlinkTypeValue())
				{
					case REFERENCE:
					{
						href = textElement.getHyperlinkReference();
						break;
					}
					case LOCAL_ANCHOR :
					case LOCAL_PAGE :
					case REMOTE_ANCHOR :
					case REMOTE_PAGE :
					case NONE:
					default:
					{
					}
				}
			}
			else
			{
				href = customHandler.getHyperlink(textElement);
			}

			Integer colIndex = columnNamesMap.get(currentColumnName);
			if (href != null)
			{
				try
				{
					URL url = new URL(href);
					WritableHyperlink hyperlink = new WritableHyperlink(colIndex, rowIndex, colIndex, rowIndex, url);
					sheet.addHyperlink(hyperlink);
				}
				catch (MalformedURLException e)
				{
					if (log.isWarnEnabled())
					{
						log.warn("Reference \"" + href + "\" could not be parsed as URL.", e);
					}
				}
				catch (RowsExceededException e)
				{
					throw new JRException("There are too many rows in the current sheet", e);
				}
				catch (WriteException e)
				{
					throw new JRException(e);
				}
			}

			JRStyledText styledText = null;
			String textStr = null;
			
			if (currentColumnData != null)
			{
				styledText = new JRStyledText();
				styledText.append(currentColumnData);
				textStr = currentColumnData;
			} 
			else
			{
				styledText = getStyledText(textElement);
				if (styledText != null)
				{
					textStr = styledText.getText();
				}
			}
			
			addTextElement(textElement, textStr, baseStyle, repeatValue, currentColumnName);
		}
	}

	protected void addTextElement(JRPrintText textElement, String textStr, StyleInfo baseStyle, boolean repeatValue, String currentColumnName) throws JRException
	{
		if (columnNames.size() > 0)
		{
			if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export but was not read yet and comes in the expected order
			{
				addCell(textElement, textStr, baseStyle, currentRow, currentColumnName);
			} 
			else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export, was not read yet, but it is read after it should be
					|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) // the column is for export and was already read
			{
				if(rowIndex == 1 && writeHeader)
				{
					writeReportHeader();
				}
				
				writeCurrentRow(currentRow, repeatedValues);
				currentRow = new HashMap<String, Object>();
				addCell(textElement, textStr, baseStyle, currentRow, currentColumnName);
			}
			// set auto fill columns
			if(repeatValue)
			{
				if ( currentColumnName != null && currentColumnName.length() > 0 && textStr.length() > 0)
				{
					addCell(textElement, textStr, baseStyle, repeatedValues, currentColumnName);
				}
			}
			else
			{
				repeatedValues.remove(currentColumnName);
			}
		}
	}
	
	protected void addBlankElement(WritableCellFormat baseCellFormat, boolean repeatValue, String currentColumnName) throws JRException
	{
		if (columnNames.size() > 0)
		{
			if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export but was not read yet and comes in the expected order
			{
				addBlankCell(baseCellFormat, currentRow, currentColumnName);
			} 
			else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export, was not read yet, but it is read after it should be
					|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) // the column is for export and was already read
			{
				if(rowIndex == 1 && writeHeader)
				{
					writeReportHeader();
				}
				
				writeCurrentRow(currentRow, repeatedValues);
				currentRow = new HashMap<String, Object>();
				addBlankCell(baseCellFormat, currentRow, currentColumnName);
			}
			// set auto fill columns
			if(repeatValue)
			{
				if (repeatValue && currentColumnName != null && currentColumnName.length() > 0 && baseCellFormat != null)
				{
					addBlankCell(baseCellFormat, repeatedValues, currentColumnName);
				}
			}
			else
			{
				repeatedValues.remove(currentColumnName);
			}
		}
	}
	
	protected void addCell(JRPrintText text, String textStr, StyleInfo baseStyle, Map<String, Object> cellValueMap, String currentColumnName) throws JRException
	{
		CellValue cellValue = null;
		TextValue textValue = null;
		
		int colIndex = columnNamesMap.get(currentColumnName) ;

		String textFormula = getFormula(text);
		if( text != null && textFormula != null)
		{
			// if the cell has formula, we try create a formula cell
			textValue = getTextValue(text, textStr);
			cellValue = getFormulaCellValue(colIndex, rowIndex, text, textValue, textFormula, baseStyle, isComplexFormat(text));
		}
		
		if (cellValue == null)
		{
			// there was no formula, or the formula cell creation failed
			if (isDetectCellType)
			{
				if (textFormula == null)
				{
					// there was no formula, so textValue was not created
					textValue = getTextValue(text, textStr);
				}
				cellValue = getDetectedCellValue(colIndex, rowIndex, text, textValue, baseStyle, isComplexFormat(text));
			}
			else
			{
				cellValue = getLabelCell(colIndex, rowIndex, textStr, baseStyle);
			}
		}
		cellValueMap.put(currentColumnName, cellValue);
	}


	protected CellValue getFormulaCellValue(int x, int y, JRPrintText textElement, TextValue textValue, String formula, StyleInfo baseStyle, boolean complexFormat) throws JRException
	{
		FormulaTextValueHandler handler = new FormulaTextValueHandler(x, y, textElement, formula, baseStyle, complexFormat);
		textValue.handle(handler);
		return handler.getResult();
	}


	protected CellValue getDetectedCellValue(int x, int y, JRPrintText textElement, TextValue textValue, StyleInfo baseStyle, boolean complexFormat) throws JRException
	{
		CellTextValueHandler handler = new CellTextValueHandler(x, y, textElement, baseStyle, complexFormat);
		textValue.handle(handler);
		return handler.getResult();
	}


	protected class FormulaTextValueHandler implements TextValueHandler
	{
		private final int x;
		private final int y;
		private final JRPrintText textElement;
		private final String formula;
		private final StyleInfo baseStyle;
		private final boolean cellComplexFormat;
		
		private CellValue result;

		public FormulaTextValueHandler(int x, int y, JRPrintText textElement, String formula, StyleInfo baseStyle, boolean cellComplexFormat)
		{
			this.x = x;
			this.y = y;
			this.textElement = textElement;
			this.formula = formula;
			this.baseStyle = baseStyle;
			this.cellComplexFormat = cellComplexFormat;
		}

		public void handle(StringTextValue textValue) throws JRException
		{
			result = formula();
		}

		public void handle(NumberTextValue textValue) throws JRException
		{
			String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
			if (convertedPattern != null)
			{
				baseStyle.setDisplayFormat(getNumberFormat(convertedPattern, cellComplexFormat));
			}

			result = formula();
		}

		public void handle(DateTextValue textValue) throws JRException
		{
			baseStyle.setDisplayFormat(getDateFormat(getConvertedPattern(textElement, textValue.getPattern())));
			result = formula();
		}

		public void handle(BooleanTextValue textValue) throws JRException
		{
			result = formula();
		}

		protected Formula formula() throws JRException
		{
			try
			{
				return new Formula(x, y, formula, getLoadedCellStyle(baseStyle));
			}
			catch(Exception e)//FIXMENOW what exceptions could we get here?
			{
				if(log.isWarnEnabled())
				{
					log.warn(e.getMessage(), e);
				}
			}
			return null;
		}

		public CellValue getResult()
		{
			return result;
		}
	}

	protected class CellTextValueHandler implements TextValueHandler
	{
		private final int x;
		private final int y;
		private final JRPrintText textElement;
		private final StyleInfo baseStyle;
		private final boolean cellComplexFormat;

		private CellValue result;

		public CellTextValueHandler(int x, int y, JRPrintText textElement, StyleInfo baseStyle, boolean cellComplexFormat)
		{
			this.x = x;
			this.y = y;
			this.textElement = textElement;
			this.baseStyle = baseStyle;
			this.cellComplexFormat = cellComplexFormat;
		}

		public void handle(StringTextValue textValue) throws JRException
		{
			WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
			result = new Label(x, y, textValue.getText(), cellStyle);
		}

		public void handle(NumberTextValue textValue) throws JRException
		{
			String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
			if (convertedPattern != null)
			{
				baseStyle.setDisplayFormat(getNumberFormat(convertedPattern, cellComplexFormat));
			}

			WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
			if (textValue.getValue() == null)
			{
				result = blank(cellStyle);
			}
			else
			{
				result = new Number(x, y, textValue.getValue().doubleValue(), cellStyle);
			}
		}

		public void handle(DateTextValue textValue) throws JRException
		{
			baseStyle.setDisplayFormat(getDateFormat(getConvertedPattern(textElement, textValue.getPattern())));
			WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
			Date date = textValue.getValue();
			if (date == null)
			{
				result = blank(cellStyle);
			}
			else
			{
				date = translateDateValue(textElement, date);
				result = new DateTime(x, y, date, cellStyle);
			}
		}

		public void handle(BooleanTextValue textValue) throws JRException
		{
			WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
			if (textValue.getValue() == null)
			{
				result = blank(cellStyle);
			}
			else
			{
				result = new jxl.write.Boolean(x, y, textValue.getValue().booleanValue(), cellStyle);
			}
		}

		protected Blank blank(WritableCellFormat cellStyle)
		{
			return new Blank(x, y, cellStyle);
		}

		public CellValue getResult()
		{
			return result;
		}
		
		public boolean isCellComplexFormat()
		{
			return cellComplexFormat;
		}
		
		
	}

	protected NumberFormat getNumberFormat(String convertedPattern, boolean isComplexFormat)
	{
		NumberFormat cellFormat = numberFormats.get(convertedPattern);
		if (cellFormat == null)
		{
			if(isComplexFormat)
			{
				cellFormat = new NumberFormat(convertedPattern, NumberFormat.COMPLEX_FORMAT);
			}
			else
			{
				cellFormat = new NumberFormat(convertedPattern);
			}
			numberFormats.put(convertedPattern, cellFormat);
		}
		return cellFormat;
	}

	protected DateFormat getDateFormat(String convertedPattern)
	{
		DateFormat cellFormat = dateFormats.get(convertedPattern);
		if (cellFormat == null)
		{
			cellFormat = new DateFormat(convertedPattern);
			dateFormats.put(convertedPattern, cellFormat);
		}
		return cellFormat;
	}

	protected CellValue getLabelCell(int x, int y, String textStr, StyleInfo baseStyle) throws JRException
	{
		WritableCellFormat cellStyle = null;
		if(baseStyle != null)
		{
			cellStyle = getLoadedCellStyle(baseStyle);
		}
		else
		{
			cellStyle = new WritableCellFormat();
		}
		return new Label(x, y, textStr, cellStyle);
	}


	private int getHorizontalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.horizontalAlignment)
		{
			case RIGHT:
				return Alignment.RIGHT.getValue();
			case CENTER:
				return Alignment.CENTRE.getValue();
			case JUSTIFIED:
				return Alignment.JUSTIFY.getValue();
			case LEFT:
			default:
				return Alignment.LEFT.getValue();
		}
	}

	private int getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
			case BOTTOM:
				return VerticalAlignment.BOTTOM.getValue();
			case MIDDLE:
				return VerticalAlignment.CENTRE.getValue();
			case JUSTIFIED:
				return VerticalAlignment.JUSTIFY.getValue();
			case TOP:
			default:
				return VerticalAlignment.TOP.getValue();
		}
	}

	private int getRotation(TextAlignHolder alignment)
	{
		switch (alignment.rotation)
		{
			case LEFT:
				return Orientation.PLUS_90.getValue();
			case RIGHT:
				return Orientation.MINUS_90.getValue();
			case UPSIDE_DOWN:
			case NONE:
			default:
				return Orientation.HORIZONTAL.getValue();
		}
	}

	public void exportImage(JRPrintImage element) throws JRException
	{

		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = JRProperties.getBooleanProperty(element, JRXlsAbstractMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), element.getWidth());
			setRowHeight(rowIndex, element.getHeight());
			
			int topPadding = 
				Math.max(element.getLineBox().getTopPadding().intValue(), getImageBorderCorrection(element.getLineBox().getTopPen()));
			int leftPadding = 
				Math.max(element.getLineBox().getLeftPadding().intValue(), getImageBorderCorrection(element.getLineBox().getLeftPen()));
			int bottomPadding = 
				Math.max(element.getLineBox().getBottomPadding().intValue(), getImageBorderCorrection(element.getLineBox().getBottomPen()));
			int rightPadding = 
				Math.max(element.getLineBox().getRightPadding().intValue(), getImageBorderCorrection(element.getLineBox().getRightPen()));
			
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
					renderer = JRImageRenderer.getOnErrorRendererForImageData(renderer, element.getOnErrorTypeValue());
					if (renderer != null)
					{
						renderer = JRImageRenderer.getOnErrorRendererForDimension(renderer, element.getOnErrorTypeValue());
					}
				}
				else
				{
					renderer =
						new JRWrappingSvgRenderer(
							renderer,
							new Dimension(element.getWidth(), element.getHeight()),
							ModeEnum.OPAQUE == element.getModeValue() ? element.getBackcolor() : null
							);
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
				switch (element.getHorizontalAlignmentValue())
				{
					case RIGHT:
					{
						xalignFactor = 1f;
						break;
					}
					case CENTER:
					{
						xalignFactor = 0.5f;
						break;
					}
					case LEFT:
					default:
					{
						xalignFactor = 0f;
						break;
					}
				}
	
				float yalignFactor = 0f;
				switch (element.getVerticalAlignmentValue())
				{
					case BOTTOM:
					{
						yalignFactor = 1f;
						break;
					}
					case MIDDLE:
					{
						yalignFactor = 0.5f;
						break;
					}
					case TOP:
					default:
					{
						yalignFactor = 0f;
						break;
					}
				}
				
				byte[] imageData = null;
				
				switch (element.getScaleImageValue())
				{
					case CLIP:
					{
						int dpi = JRProperties.getIntegerProperty(JRRenderable.PROPERTY_IMAGE_DPI, 72);
						double scale = dpi/72d;
						
						BufferedImage bi = 
							new BufferedImage(
								(int)(scale * availableImageWidth), 
								(int)(scale * availableImageHeight), 
								BufferedImage.TYPE_INT_ARGB
								);

						Graphics2D grx = bi.createGraphics();
						grx.scale(scale, scale);
						grx.clip(
							new Rectangle(
								0, 
								0, 
								availableImageWidth, 
								availableImageHeight
								)
							);
						
						renderer.render(
							grx, 
							new Rectangle(
								(int) (xalignFactor * (availableImageWidth - normalWidth)),
								(int) (yalignFactor * (availableImageHeight - normalHeight)),
								normalWidth, 
								normalHeight
								)
							);
	
						imageData = JRImageLoader.loadImageDataFromAWTImage(bi, JRRenderable.IMAGE_TYPE_PNG);
	
						break;
					}
					case FILL_FRAME:
					{
						imageData = renderer.getImageData();
	
						break;
					}
					case RETAIN_SHAPE:
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
	
							imageData = renderer.getImageData();
						}
	
						break;
					}
				}
	
				Pattern mode = this.backgroundMode;
				Colour background = WHITE;
	
				if (!isIgnoreCellBackground && element.getBackcolor() != null)
				{
					mode = Pattern.SOLID;
					background = getWorkbookColour(element.getBackcolor(), true);
				}
	
				if (element.getModeValue() == ModeEnum.OPAQUE)
				{
					background = getWorkbookColour(element.getBackcolor(), true);
				}
	
				Colour forecolor = getWorkbookColour(element.getLineBox().getPen().getLineColor());
	
				WritableFont cellFont2 = this.getLoadedFont(getDefaultFont(), forecolor.getValue(), getLocale());
	
				WritableCellFormat cellStyle2 = 
					getLoadedCellStyle(
						mode, 
						background, 
						cellFont2, 
						new BoxStyle(element),
						isCellLocked(element)
						);
	
				addBlankElement(cellStyle2, repeatValue, currentColumnName);
				try
				{
					int colIndex = columnNamesMap.get(currentColumnName);
					WritableImage image = new WritableImage(colIndex, rowIndex, 1, 1, imageData);
					sheet.addImage(image);
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
		}
	}

	protected Colour getWorkbookColour(Color awtColor, boolean isBackcolor)
	{
		if (isBackcolor && awtColor.getRGB() == Color.black.getRGB())
		{
			return Colour.PALETTE_BLACK;
		}
		return getWorkbookColour(awtColor);
	}

	protected Colour getWorkbookColour(Color awtColor)
	{
		Colour colour;
		if (createCustomPalette)
		{
			colour = workbookColours.get(awtColor);
			if (colour == null)
			{
				colour = determineWorkbookColour(awtColor);
				workbookColours.put(awtColor, colour);
			}
		}
		else
		{
			colour = getNearestColour(awtColor);
		}
		return colour;
	}

	protected Colour determineWorkbookColour(Color awtColor)
	{
		//nearest match
		int minDist = 999;
		Colour minColour = null;

		//nearest match among the available (not used) colours
		int minDistAvailable = 999;
		Colour minColourAvailable = null;

		Colour[] colors = Colour.getAllColours();
		for (int i = 0; i < colors.length; i++)
		{
			Colour colour = colors[i];
			RGB customRGB = usedColours.get(colour);

			RGB rgb = customRGB == null ? colour.getDefaultRGB() : customRGB;
			int dist = rgbDistance(awtColor, rgb);
			if (dist < minDist)
			{
				minDist = dist;
				minColour = colour;
			}

			if (dist == 0)//exact match
			{
				break;
			}

			if (customRGB == null)//the colour is not used
			{
				if (dist < minDistAvailable)
				{
					minDistAvailable = dist;
					minColourAvailable = colour;
				}
			}
		}

		Colour workbookColour;
		if (minDist == 0)//exact match found
		{
			if (!usedColours.containsKey(minColour))
			{
				//if the colour is not marked as used, mark it
				setColourUsed(minColour);
			}
			workbookColour = minColour;
		}
		else if (minColourAvailable == null)//all the colours are used
		{
			if (log.isWarnEnabled())
			{
				log.warn("No more available colours in the palette.  Using the nearest match for " + awtColor);
			}
			workbookColour = minColour;
		}
		else
		{
			//modifying the nearest available colour to the report colour
			setColourUsed(minColourAvailable, awtColor);
			workbookColour = minColourAvailable;
		}
		return workbookColour;
	}

	protected static Colour getNearestColour(Color awtColor)
	{
		Colour color = colorsCache.get(awtColor);

		if (color == null)
		{
			Colour[] colors = Colour.getAllColours();
			if ((colors != null) && (colors.length > 0))
			{
				int minDiff = 999;

				for (int i = 0; i < colors.length; i++)
				{
					Colour crtColor = colors[i];
					int diff = rgbDistance(awtColor, crtColor.getDefaultRGB());

					if (diff < minDiff)
					{
						minDiff = diff;
						color = crtColor;
					}
				}
			}

			colorsCache.put(awtColor, color);
		}

		return color;
	}

	protected static int rgbDistance(Color awtColor, RGB rgb)
	{
		return Math.abs(rgb.getRed() - awtColor.getRed())
			+ Math.abs(rgb.getGreen() - awtColor.getGreen())
			+ Math.abs(rgb.getBlue() - awtColor.getBlue());
	}


	private WritableFont getLoadedFont(JRFont font, int forecolor, Locale locale) throws JRException
	{
		WritableFont cellFont = null;

		if (this.loadedFonts != null && this.loadedFonts.size() > 0)
		{
			for (int i = 0; i < this.loadedFonts.size(); i++)
			{
				WritableFont cf = (WritableFont) this.loadedFonts.get(i);

				int fontSize = font.getFontSize();
				if (isFontSizeFixEnabled)
				{
					fontSize -= 1;
				}
				String fontName = font.getFontName();
				if (fontMap != null && fontMap.containsKey(fontName))
				{
					fontName = fontMap.get(fontName);
				}
				else
				{
					FontInfo fontInfo = JRFontUtil.getFontInfo(fontName, locale);
					if (fontInfo != null)
					{
						//fontName found in font extensions
						FontFamily family = fontInfo.getFontFamily();
						String exportFont = family.getExportFont(getExporterKey());
						if (exportFont != null)
						{
							fontName = exportFont;
						}
					}
				}

				if ((cf.getName().equals(fontName))
						&& (cf.getColour().getValue() == forecolor)
						&& (cf.getPointSize() == fontSize)
						&& (cf.getUnderlineStyle() == UnderlineStyle.SINGLE ? (font.isUnderline()) : (!font.isUnderline()))
						&& (cf.isStruckout() == font.isStrikeThrough())
						&& (cf.getBoldWeight() == BoldStyle.BOLD.getValue() ? (font.isBold()) : (!font.isBold()))
						&& (cf.isItalic() == font.isItalic()))
				{
					cellFont = cf;
					break;
				}
			}
		}

		try
		{
			if (cellFont == null)
			{
				int fontSize = font.getFontSize();
				if (isFontSizeFixEnabled)
				{
					fontSize -= 1;
				}
				String fontName = font.getFontName();
				if (fontMap != null && fontMap.containsKey(fontName))
				{
					fontName = fontMap.get(fontName);
				}

				cellFont =
					new WritableFont(
						WritableFont.createFont(fontName),
						fontSize,
						font.isBold() ? WritableFont.BOLD : WritableFont.NO_BOLD,
						font.isItalic(),
						font.isUnderline() ? UnderlineStyle.SINGLE : UnderlineStyle.NO_UNDERLINE,
						Colour.getInternalColour(forecolor)
						);
				cellFont.setStruckout(font.isStrikeThrough());

				this.loadedFonts.add(cellFont);
			}
		}
		catch (Exception e)
		{
			throw new JRException("Can't get loaded fonts.", e);
		}

		return cellFont;
	}

	protected class BoxStyle
	{
		protected static final int TOP = 0;
		protected static final int LEFT = 1;
		protected static final int BOTTOM = 2;
		protected static final int RIGHT = 3;

		protected BorderLineStyle[] borderStyle = 
			new BorderLineStyle[]{BorderLineStyle.NONE, BorderLineStyle.NONE, BorderLineStyle.NONE, BorderLineStyle.NONE};
		protected Colour[] borderColour = 
			new Colour[]{BLACK, BLACK, BLACK, BLACK};
		private int hash;

		public BoxStyle(int side, JRPen pen)
		{
			borderStyle[side] = getBorderLineStyle(pen);
			borderColour[side] = getWorkbookColour(pen.getLineColor());

			hash = computeHash();
		}

		public BoxStyle(JRBoxContainer element)
		{
			JRLineBox lineBox = element.getLineBox();
			
			if (lineBox != null)
			{
				setBox(lineBox);
			}
			if (element instanceof JRCommonGraphicElement)
			{
				setPen(((JRCommonGraphicElement)element).getLinePen());
			}
			hash = computeHash();
		}

		public void setBox(JRLineBox box)
		{
			borderStyle[TOP] = getBorderLineStyle(box.getTopPen());
			borderColour[TOP] = getWorkbookColour(box.getTopPen().getLineColor());

			borderStyle[BOTTOM] = getBorderLineStyle(box.getBottomPen());
			borderColour[BOTTOM] = getWorkbookColour(box.getBottomPen().getLineColor());

			borderStyle[LEFT] = getBorderLineStyle(box.getLeftPen());
			borderColour[LEFT] = getWorkbookColour(box.getLeftPen().getLineColor());

			borderStyle[RIGHT] = getBorderLineStyle(box.getRightPen());
			borderColour[RIGHT] = getWorkbookColour(box.getRightPen().getLineColor());

			hash = computeHash();
		}

		public void setPen(JRPen pen)
		{
			if (
				borderStyle[TOP] == BorderLineStyle.NONE
				&& borderStyle[LEFT] == BorderLineStyle.NONE
				&& borderStyle[BOTTOM] == BorderLineStyle.NONE
				&& borderStyle[RIGHT] == BorderLineStyle.NONE
				)
			{
				BorderLineStyle style = getBorderLineStyle(pen);
				Colour colour = getWorkbookColour(pen.getLineColor());

				borderStyle[TOP] = style;
				borderStyle[LEFT] = style;
				borderStyle[BOTTOM] = style;
				borderStyle[RIGHT] = style;

				borderColour[TOP] = colour;
				borderColour[LEFT] = colour;
				borderColour[BOTTOM] = colour;
				borderColour[RIGHT] = colour;
			}

			hash = computeHash();
		}

		private int computeHash()
		{
			int hashCode = borderStyle[TOP].hashCode();
			hashCode = 31*hashCode + borderColour[TOP].hashCode();
			hashCode = 31*hashCode + borderStyle[BOTTOM].hashCode();
			hashCode = 31*hashCode + borderColour[BOTTOM].hashCode();
			hashCode = 31*hashCode + borderStyle[LEFT].hashCode();
			hashCode = 31*hashCode + borderColour[LEFT].hashCode();
			hashCode = 31*hashCode + borderStyle[RIGHT].hashCode();
			hashCode = 31*hashCode + borderColour[RIGHT].hashCode();
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
				b.borderStyle[TOP].equals(borderStyle[TOP])
				&& b.borderColour[TOP].equals(borderColour[TOP])
				&& b.borderStyle[LEFT].equals(borderStyle[LEFT])
				&& b.borderColour[LEFT].equals(borderColour[LEFT])
				&& b.borderStyle[BOTTOM].equals(borderStyle[BOTTOM])
				&& b.borderColour[BOTTOM].equals(borderColour[BOTTOM])
				&& b.borderStyle[RIGHT].equals(borderStyle[RIGHT])
				&& b.borderColour[RIGHT].equals(borderColour[RIGHT]);
		}

		public String toString()
		{
			return "(" +
				borderStyle[TOP].getValue() + "/" + borderColour[TOP].getValue() + "," +
				borderStyle[BOTTOM].getValue() + "/" + borderColour[BOTTOM].getValue() + "," +
				borderStyle[LEFT].getValue() + "/" + borderColour[LEFT].getValue() + "," +
				borderStyle[RIGHT].getValue() + "/" + borderColour[RIGHT].getValue() + ")";
		}
	}

	protected class StyleInfo
	{
		protected final Pattern mode;
		protected final Colour backcolor;
		protected final int horizontalAlignment;
		protected final int verticalAlignment;
		protected final int rotation;
		protected final WritableFont font;
		protected final BoxStyle box;
		protected final boolean isWrapText;
		protected final boolean isCellLocked;
		private DisplayFormat displayFormat;
		private int hashCode;

		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				JRBoxContainer element
				)
			{
				this(
					mode, 
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					rotation, 
					font, 
					new BoxStyle(element),
					true
					);
			}
			
		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				JRBoxContainer element,
				boolean wrapText
				)
			{
				this(
					mode, 
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					rotation, 
					font, 
					new BoxStyle(element),
					wrapText
					);
			}
			
		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				JRBoxContainer element,
				boolean wrapText,
				boolean cellLocked
				)
			{
				this(
					mode, 
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					rotation, 
					font, 
					new BoxStyle(element),
					wrapText,
					cellLocked
					);
			}
			
		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				BoxStyle box
				)
			{
			this(
					mode, 
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					rotation, 
					font, 
					box,
					true
					);
			}

		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				BoxStyle box,
				boolean wrapText
				)
			{
			this(
					mode, 
					backcolor, 
					horizontalAlignment, 
					verticalAlignment, 
					rotation, 
					font, 
					box,
					wrapText,
					true
					);
			}

		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				BoxStyle box,
				boolean wrapText,
				boolean cellLocked
				)
			{
				this.mode = mode;
				this.backcolor = backcolor;
				this.horizontalAlignment = horizontalAlignment;
				this.verticalAlignment = verticalAlignment;
				this.rotation = rotation;
				this.font = font;
				
				this.box = box;
				this.isWrapText = wrapText;
				this.isCellLocked = cellLocked;
				
				computeHash();
			}

		protected void computeHash()
		{
			int hash = this.mode.hashCode();
			hash = 31*hash + this.backcolor.hashCode();
			hash = 31*hash + this.horizontalAlignment;
			hash = 31*hash + this.verticalAlignment;
			hash = 31*hash + this.rotation;
			hash = 31*hash + this.font.hashCode();
			hash = 31*hash + (this.box == null ? 0 : this.box.hashCode());
			hash = 31*hash + (this.displayFormat == null ? 0 : this.displayFormat.hashCode());
			hash = 31*hash + (this.isWrapText ? 0 : 1);
			hash = 31*hash + (this.isCellLocked ? 0 : 1);

			hashCode = hash;
		}

		public int hashCode()
		{
			return hashCode;
		}

		public boolean equals(Object o)
		{
			StyleInfo k = (StyleInfo) o;

			return k.mode.equals(mode) && k.backcolor.equals(backcolor) &&
				k.horizontalAlignment == horizontalAlignment && k.verticalAlignment == verticalAlignment &&
				k.rotation == rotation && k.font.equals(font) &&
				(k.box == null ? box == null : (box != null && k.box.equals(box))) &&
				(k.displayFormat == null ? displayFormat == null : (displayFormat!= null && k.displayFormat.equals(displayFormat)) &&
				k.isWrapText == wrapText && k.isCellLocked == cellLocked);
		}

		public DisplayFormat getDisplayFormat()
		{
			return displayFormat;
		}

		public void setDisplayFormat(DisplayFormat displayFormat)
		{
			this.displayFormat = displayFormat;
			computeHash();
		}

		public String toString()
		{
			return "(" +
				mode + "," + backcolor + "," +
				horizontalAlignment + "," + verticalAlignment + "," +
				rotation + "," + font + "," +
				box + "," + displayFormat + "," + wrapText + "," + cellLocked + ")";
		}
	}


			private WritableCellFormat getLoadedCellStyle(
				Pattern mode, 
				Colour backcolor, 
				WritableFont font, 
				BoxStyle box,
				boolean cellLocked
				) throws JRException
			{
				StyleInfo styleKey = 
					new StyleInfo(
						mode, 
						backcolor, 
						Alignment.LEFT.getValue(), 
						VerticalAlignment.TOP.getValue(), 
						Orientation.HORIZONTAL.getValue(),
						font, 
						box,
						true,
						cellLocked
						);
				return getLoadedCellStyle(styleKey);
			}

	protected WritableCellFormat getLoadedCellStyle(StyleInfo styleKey) throws JRException
	{
		WritableCellFormat cellStyle = loadedCellStyles.get(styleKey);

		if (cellStyle == null)
		{
			try
			{
				if (styleKey.getDisplayFormat() == null)
				{
					cellStyle = new WritableCellFormat(styleKey.font);
				}
				else
				{
					cellStyle = new WritableCellFormat(styleKey.font, styleKey.getDisplayFormat());
				}

				cellStyle.setBackground(styleKey.backcolor, styleKey.mode);
				cellStyle.setAlignment(Alignment.getAlignment(styleKey.horizontalAlignment));
				cellStyle.setVerticalAlignment(VerticalAlignment.getAlignment(styleKey.verticalAlignment));
				cellStyle.setOrientation(Orientation.getOrientation(styleKey.rotation));
				cellStyle.setWrap(styleKey.isWrapText);
				cellStyle.setLocked(styleKey.isCellLocked);

				if (!isIgnoreCellBorder)
				{
					BoxStyle box = styleKey.box;
					cellStyle.setBorder(Border.TOP, box.borderStyle[BoxStyle.TOP], box.borderColour[BoxStyle.TOP]);
					cellStyle.setBorder(Border.BOTTOM, box.borderStyle[BoxStyle.BOTTOM], box.borderColour[BoxStyle.BOTTOM]);
					cellStyle.setBorder(Border.LEFT, box.borderStyle[BoxStyle.LEFT], box.borderColour[BoxStyle.LEFT]);
					cellStyle.setBorder(Border.RIGHT, box.borderStyle[BoxStyle.RIGHT], box.borderColour[BoxStyle.RIGHT]);
				}
			}
			catch (Exception e)
			{
				throw new JRException("Error setting cellFormat-template.", e);
			}

			loadedCellStyles.put(styleKey, cellStyle);
		}

		
		return cellStyle;
	}

	/**
	 *
	 */
	protected static BorderLineStyle getBorderLineStyle(JRPen pen) 
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			switch (pen.getLineStyleValue())
			{
				case DOUBLE :
				{
					return BorderLineStyle.DOUBLE;
				}
				case DOTTED :
				{
					return BorderLineStyle.DOTTED;
				}
				case DASHED :
				{
					if (lineWidth >= 1f)
					{
						return BorderLineStyle.MEDIUM_DASHED;
					}

					return BorderLineStyle.DASHED;
				}
				case SOLID :
				default :
				{
					if (lineWidth >= 2f)
					{
						return BorderLineStyle.THICK;
					}
					else if (lineWidth >= 1f)
					{
						return BorderLineStyle.MEDIUM;
					}
					else if (lineWidth >= 0.5f)
					{
						return BorderLineStyle.THIN;
					}

					return BorderLineStyle.HAIR;
				}
			}
		}
		
		return BorderLineStyle.NONE;
	}

	private final void setSheetSettings(WritableSheet sheet)
	{
		rowIndex = 0;
		PageOrientation po;
		PaperSize ps;

		if (jasperPrint.getOrientationValue() == OrientationEnum.PORTRAIT)
		{
			po = PageOrientation.PORTRAIT;
		}
		else
		{
			po = PageOrientation.LANDSCAPE;
		}
		if ((ps = getSuitablePaperSize(jasperPrint)) != null)
		{
			sheet.setPageSetup(po, ps, 0, 0);
		}
		else
		{
			sheet.setPageSetup(po);
		}
		SheetSettings sheets = sheet.getSettings();
		
		if (jasperPrint.getTopMargin() != null)
		{
			sheets.setTopMargin(LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getTopMargin()));
		}

		if (jasperPrint.getLeftMargin() != null)
		{
			sheets.setLeftMargin(LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getLeftMargin()));
		}
		
		if (jasperPrint.getRightMargin() != null)
		{
			sheets.setRightMargin(LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getRightMargin()));
		}

		if (jasperPrint.getBottomMargin() != null)
		{
			sheets.setBottomMargin(LengthUtil.inchNoRound(isIgnorePageMargins ? 0 : jasperPrint.getBottomMargin()));
		}

		sheets.setHeaderMargin(0.0);
		sheets.setFooterMargin(0.0);

		String fitWidth = JRProperties.getProperty(jasperPrint, PROPERTY_FIT_WIDTH);
		if(fitWidth != null && fitWidth.length() > 0)
		{
			sheets.setFitWidth(Integer.valueOf(fitWidth));
			sheets.setFitToPages(true);
		}
		
		String fitHeight = JRProperties.getProperty(jasperPrint, PROPERTY_FIT_HEIGHT);
		if(fitHeight != null && fitHeight.length() > 0)
		{
			sheets.setFitHeight(Integer.valueOf(fitHeight));
			sheets.setFitToPages(true);
		}

		if(password != null)
		{
			sheets.setPassword(password);
			sheets.setProtected(true);
		}
		
		if(sheetHeaderLeft != null)
		{
			sheets.getHeader().getLeft().append(sheetHeaderLeft);
		}
		
		if(sheetHeaderCenter != null)
		{
			sheets.getHeader().getCentre().append(sheetHeaderCenter);
		}
		
		if(sheetHeaderRight != null)
		{
			sheets.getHeader().getRight().append(sheetHeaderRight);
		}
		
		if(sheetFooterLeft != null)
		{
			sheets.getFooter().getLeft().append(sheetFooterLeft);
		}
		
		if(sheetFooterCenter != null)
		{
			sheets.getFooter().getCentre().append(sheetFooterCenter);
		}
		
		if(sheetFooterRight != null)
		{
			sheets.getFooter().getRight().append(sheetFooterRight);
		}
		
		maxRowFreezeIndex = 0;
		maxColumnFreezeIndex = 0;
	}

	private final PaperSize getSuitablePaperSize(JasperPrint jasP)
	{

		if (jasP == null)
		{
			return null;
		}
		long width = 0;
		long height = 0;
		PaperSize ps = null;

		if ((jasP.getPageWidth() != 0) && (jasP.getPageHeight() != 0))
		{

			double dWidth = (jasP.getPageWidth() / 72.0);
			double dHeight = (jasP.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by JExcelApi yet.
			for (int i = 3; i < 6; i++)
			{
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height)))
				{
					if (i == 3)
					{
						ps = PaperSize.A3;
					}
					else if (i == 4)
					{
						ps = PaperSize.A4;
					}
					else if (i == 5)
					{
						ps = PaperSize.A5;
					}
					break;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == null)
			{
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
				{
					ps = PaperSize.LETTER;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
				{
					ps = PaperSize.LEGAL;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				// Not supperted by JExcelApi yet.

				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supperted by JExcelApi yet.
			}
		}
		return ps;
	}


	public static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
	{
		HorizontalAlignEnum horizontalAlignment;
		VerticalAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue())
		{
			case LEFT :
			{
				switch (textElement.getHorizontalAlignmentValue())
				{
					case LEFT :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalAlignmentValue())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
					}
				}

				break;
			}
			case RIGHT :
			{
				switch (textElement.getHorizontalAlignmentValue())
				{
					case LEFT :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalAlignEnum.BOTTOM;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalAlignEnum.TOP;
					}
				}

				switch (textElement.getVerticalAlignmentValue())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalAlignEnum.LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalAlignEnum.RIGHT;
					}
				}

				break;
			}
			case UPSIDE_DOWN:
			case NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalAlignmentValue();
				verticalAlignment = textElement.getVerticalAlignmentValue();
			}
		}

		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}

	protected void exportFrame(JRPrintFrame frame) throws JRException
	{
		
		for (Object element : frame.getElements()) 
		{
			if (element instanceof JRPrintLine)
			{
				exportLine((JRPrintLine)element);
			}
			else if (element instanceof JRPrintRectangle)
			{
				exportRectangle((JRPrintRectangle)element);
			}
			else if (element instanceof JRPrintEllipse)
			{
				exportRectangle((JRPrintEllipse)element);
			}
			else if (element instanceof JRPrintImage)
			{
				exportImage((JRPrintImage) element);
			}
			else if (element instanceof JRPrintText)
			{
				exportText((JRPrintText)element);
			}
			else if (element instanceof JRPrintFrame)
			{
				exportFrame((JRPrintFrame) element);
			}
			else if (element instanceof JRGenericPrintElement)
			{
				exportGenericElement((JRGenericPrintElement) element);
			}
		}
	}


	protected void exportGenericElement(JRGenericPrintElement element) throws JRException
	{
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporterParameter.PROPERTY_COLUMN_NAME);
		if(currentColumnName != null && currentColumnName.length() > 0)
		{
			boolean repeatValue = JRProperties.getBooleanProperty(element, JRXlsAbstractMetadataExporterParameter.PROPERTY_REPEAT_VALUE, false);
			int colIndex = columnNamesMap.get(currentColumnName);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), element.getWidth());
			setRowHeight(rowIndex, element.getHeight());
			
	
			GenericElementJExcelApiMetadataHandler handler = (GenericElementJExcelApiMetadataHandler)GenericElementHandlerEnviroment.getHandler(
					element.getGenericType(), 
					JXL_EXPORTER_KEY);
	
			if (handler != null)
			{
				rowIndex = handler.exportElement(
						exporterContext, 
						element, 
						currentRow, 
						repeatedValues, 
						columnNames,
						columnNamesMap,
						currentColumnName, 
						colIndex, 
						rowIndex, 
						repeatValue
						);
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("No XLS generic element handler for " 
							+ element.getGenericType());
				}
			}
		}
	}


	protected ExporterNature getNature()
	{
		return nature;
	}


	protected String getExporterKey()
	{
		return JXL_EXPORTER_KEY;
	}

	/**
	 * 
	 */
	protected boolean isComplexFormat(JRPrintElement element)
	{
		if (
				element.hasProperties()
				&& element.getPropertiesMap().containsProperty(PROPERTY_COMPLEX_FORMAT)
				)
			{
				// we make this test to avoid reaching the global default value of the property directly
				// and thus skipping the report level one, if present
				return JRProperties.getBooleanProperty(element, PROPERTY_COMPLEX_FORMAT, complexFormat);
			}
		return complexFormat;
	}

	protected void setColumnName(String currentColumnName)
	{
		// when no columns are provided, build the column names list as they are retrieved from the report element property
		if (!hasDefinedColumns)
		{
			if (currentColumnName != null && currentColumnName.length() > 0 && !columnNames.contains(currentColumnName))
			{
				columnNamesMap.put( currentColumnName, columnNames.size());
				columnNames.add(currentColumnName);
			}
		}
	}
	
	/**
	 * Writes the header column names
	 */
	protected void writeReportHeader() throws JRException 
	{
		try
		{
			for(int i=0; i < columnNames.size(); ++i)
			{
				CellValue cellValue = getLabelCell(i, 0, columnNames.get(i), null);
				sheet.addCell(cellValue);
			}
		}
		catch (RowsExceededException e)
		{
			throw new JRException("There are too many rows in the current sheet", e);
		}
		catch (WriteException e)
		{
			throw new JRException(e);
		}
	}

	/**
	 * Creates a freeze pane for the current sheet. Freeze pane row and column indexes defined at element level override indexes defined at report level. 
	 * If multiple row freeze indexes are found in the same sheet, their maximum value is considered. 
	 * 
	 * @param rowIndex the freeze 0-based row index
	 * @param colIndex the freeze 0-based column index
	 * @param isRowEdge specifies if the freeze row index is set at element level
	 * @param isColumnEdge specifies if the freeze column index is set at element level
	 */
	protected void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge)
	{
		int maxRowIndex = isFreezeRowEdge 
				? Math.max(rowIndex, maxRowFreezeIndex) 
				: (isRowEdge ? rowIndex : Math.max(rowIndex, maxRowFreezeIndex));
		int maxColIndex = isFreezeColumnEdge 
				? Math.max(colIndex, maxColumnFreezeIndex) 
				: (isColumnEdge ? colIndex : Math.max(colIndex, maxColumnFreezeIndex));
		
		SheetSettings settings = sheet.getSettings();
		settings.setVerticalFreeze(maxRowIndex);
		settings.setHorizontalFreeze(maxColIndex);
		maxRowFreezeIndex = maxRowIndex;
		maxColumnFreezeIndex = maxColIndex;
		isFreezeRowEdge = isRowEdge;
		isFreezeColumnEdge = isColumnEdge;
	}

	protected void setSheetName(String sheetName)
	{
		sheet.setName(sheetName);
	}
	
	protected void setAutoFilter(String autoFilterRange)
	{
		// TODO support auto filter feature
		
	}

	@Override
	protected void setRowLevels(XlsRowLevelInfo levelInfo, String level) {
		// TODO set row levels
	}
	
}

