/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
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
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRWrappingSvgRenderer;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.export.type.ImageAnchorTypeEnum;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.JxlExporterConfiguration;
import net.sf.jasperreports.export.JxlMetadataExporterConfiguration;
import net.sf.jasperreports.export.JxlMetadataReportConfiguration;
import net.sf.jasperreports.export.JxlReportConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import jxl.read.biff.BiffException;
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


/**
 * @deprecated Replaced by {@link JRXlsMetadataExporter}.
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JExcelApiMetadataExporter extends JRXlsAbstractMetadataExporter<JxlMetadataReportConfiguration, JxlMetadataExporterConfiguration, JExcelApiExporterContext>
{

	private static final Log log = LogFactory.getLog(JExcelApiMetadataExporter.class);
	public static final String EXCEPTION_MESSAGE_KEY_CURRENT_SHEET_TOO_MANY_ROWS = "export.xls.current.sheet.too.many.rows";
	public static final String EXCEPTION_MESSAGE_KEY_SHEET_TOO_MANY_ROWS = "export.xls.sheet.too.many.rows";

	/**
	 * @deprecated Replaced by {@link JxlExporterConfiguration#PROPERTY_USE_TEMP_FILE}.
	 */
	public static final String PROPERTY_USE_TEMP_FILE = JxlExporterConfiguration.PROPERTY_USE_TEMP_FILE;

	/**
	 * @deprecated Replaced by {@link JxlReportConfiguration#PROPERTY_COMPLEX_FORMAT}.
	 */
	public static final String PROPERTY_COMPLEX_FORMAT = JxlReportConfiguration.PROPERTY_COMPLEX_FORMAT;

	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
	 */
	public static final String JXL_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "jxl";
	

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

	private Map<String,NumberFormat> numberFormats = new HashMap<String,NumberFormat>();
	private Map<String,DateFormat> dateFormats = new HashMap<String,DateFormat>();

	protected Map<Color,Colour> workbookColours = new HashMap<Color,Colour>();
	protected Map<Colour,RGB> usedColours = new HashMap<Colour,RGB>();
	
	protected ExporterNature nature;
	protected final java.text.DateFormat isoDateFormat = JRDataUtils.getIsoDateFormat();
	
	
	protected class ExporterContext extends BaseExporterContext implements JExcelApiExporterContext
	{
	}
	

	/**
	 * @see #JExcelApiMetadataExporter(JasperReportsContext)
	 */
	public JExcelApiMetadataExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public JExcelApiMetadataExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext();
	}


	/**
	 *
	 */
	protected Class<JxlMetadataExporterConfiguration> getConfigurationInterface()
	{
		return JxlMetadataExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<JxlMetadataReportConfiguration> getItemConfigurationInterface()
	{
		return JxlMetadataReportConfiguration.class;
	}
	

	@Override
	protected void initExport()
	{
		super.initExport();

		JxlMetadataExporterConfiguration configuration = getCurrentConfiguration();
		
		if (configuration.isCreateCustomPalette())
		{
			initCustomPalette();
		}
		
		sheet = null;
	}
	

	@Override
	protected void initReport()
	{
		super.initReport();

		JxlMetadataReportConfiguration configuration = getCurrentItemConfiguration();
		
		if (configuration.isWhitePageBackground())
		{
			this.backgroundMode = Pattern.SOLID;
		}
		else
		{
			this.backgroundMode = Pattern.NONE;
		}

		nature = 
			new JExcelApiExporterNature(
				jasperReportsContext, 
				filter, 
				configuration.isIgnoreGraphics(), 
				configuration.isIgnorePageMargins()
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

	protected void openWorkbook(OutputStream os) throws JRException
	{
		JxlMetadataExporterConfiguration configuration = getCurrentConfiguration();
		
		WorkbookSettings settings = new WorkbookSettings();
		settings.setUseTemporaryFileDuringWrite(configuration.isUseTempFile());
		
		InputStream templateIs = null;

		try
		{
			String lcWorkbookTemplate = workbookTemplate == null ? configuration.getWorkbookTemplate() : workbookTemplate;
			if (lcWorkbookTemplate == null)
			{
				workbook = Workbook.createWorkbook(os, settings);
			}
			else
			{
				templateIs = RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(lcWorkbookTemplate);
				if (templateIs == null)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND,  
							new Object[]{lcWorkbookTemplate} 
							);
				}
				else
				{
					Workbook template = Workbook.getWorkbook(templateIs);
					workbook = Workbook.createWorkbook(os, template, settings);
					boolean keepSheets = keepTemplateSheets == null ? configuration.isKeepWorkbookTemplateSheets() : keepTemplateSheets;
					if(!keepSheets)
					{
						for(int i = 0; i < workbook.getNumberOfSheets(); i++)
						{
							workbook.removeSheet(i);
						}
					}
					else
					{
						sheetIndex += workbook.getNumberOfSheets();
					}
				}
			}
			
			firstPageNotSet = true;
		}
		catch (IOException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
		catch (BiffException e) 
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
		finally
		{
			if (templateIs != null)
			{
				try
				{
					templateIs.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}

	protected void createSheet(SheetInfo sheetInfo)
	{
		this.sheetInfo = sheetInfo;
		sheet = workbook.createSheet(sheetInfo.sheetName, Integer.MAX_VALUE);
		setSheetSettings(sheetInfo, sheet);
	}

	protected void closeSheet()
	{
		if (sheet == null)
		{
			return;
		}

		if (sheetInfo.sheetPageScale != null && sheetInfo.sheetPageScale > 9 && sheetInfo.sheetPageScale < 401)
		{
			SheetSettings sheetSettings = sheet.getSettings();
			sheetSettings.setScaleFactor(sheetInfo.sheetPageScale);
			
			/* the scale factor takes precedence over fitWidth and fitHeight properties */			
			sheetSettings.setFitWidth(0);
			sheetSettings.setFitHeight(0);
			sheetSettings.setFitToPages(false);
		}
		else
		{
			JxlReportConfiguration configuration = getCurrentItemConfiguration();

			Integer fitWidth = configuration.getFitWidth();
			Integer fitHeight = configuration.getFitHeight();
			fitHeight = 
				fitHeight == null
				? (Boolean.TRUE == configuration.isAutoFitPageHeight() 
					? (pageIndex - sheetInfo.sheetFirstPageIndex)
					: null)
				: fitHeight;
			if (fitWidth != null || fitWidth != null)
			{
				SheetSettings sheetSettings = sheet.getSettings();
				sheetSettings.setFitWidth(fitWidth == null ? 1 : fitWidth);
				sheetSettings.setFitHeight(fitHeight == null ? 1 : fitHeight);
				sheetSettings.setFitToPages(true);
			}
		}
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
		catch (WriteException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
	}

	protected void setColumnWidth(int col, int width, boolean autoFit)
	{
//		if (autoFit)
//		{
//			CellView cv = new CellView();
//			cv.setAutosize(true);
//			sheet.setColumnView(col, cv);
//		}
//		else
//		{
//			CellView cv = sheet.getColumnView(col);
//			if(cv == null || cv.getSize() < 43 * width)
//			{
//				cv = new CellView();
//				cv.setSize(43 * width);
//				sheet.setColumnView(col, cv);
//			}
//		}
		CellView cv = new CellView();
		if (autoFit)
		{
			cv.setAutosize(true);
		}
		else
		{
			cv.setSize(43 * width);
		}
		sheet.setColumnView(col, cv);
	}

	protected void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException
	{
		boolean isAutoFit = yCut != null 
				&& yCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) 
				&& (Boolean)yCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW);
		if (isAutoFit)
		{
			try
			{
				CellView cv = sheet.getRowView(rowIndex) == null ? new CellView() : sheet.getRowView(rowIndex);
				cv.setAutosize(true);
				sheet.setRowView(rowIndex, cv);
			}
			catch (RowsExceededException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_SHEET_TOO_MANY_ROWS,
						new Object[]{sheet.getName(), rowIndex},
						e);
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
				throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_SHEET_TOO_MANY_ROWS,
					new Object[]{sheet.getName(), rowIndex},
					e);
			}
		}
	}

	protected void addRowBreak(int rowIndex)
	{
		sheet.addRowPageBreak(rowIndex);
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
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CURRENT_SHEET_TOO_MANY_ROWS,
							null,
							e);
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
		String currentColumnName = line.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(line, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), line.getWidth());
			setRowHeight(rowIndex, line.getHeight());
			
			Colour forecolor2 = getWorkbookColour(line.getLinePen().getLineColor());
			WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor2.getValue(), getLocale());
			
			Colour backcolor = WHITE;
			Pattern mode = this.backgroundMode;
	
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && line.getBackcolor() != null)
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
					isWrapText(line),
					isCellLocked(line),
					isShrinkToFit(line)
					);
			
			addBlankElement(cellStyle2, repeatValue, currentColumnName);
		}
	}

	protected void exportRectangle(JRPrintGraphicElement element) throws JRException
	{
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), element.getWidth());
			setRowHeight(rowIndex, element.getHeight());
			
			Colour backcolor = WHITE;
			Pattern mode = this.backgroundMode;
	
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && element.getBackcolor() != null)
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
					isWrapText(element),
					isCellLocked(element),
					isShrinkToFit(element)
					);
			
			addBlankElement(cellStyle2, repeatValue, currentColumnName);
		}
	}


	protected void exportText(JRPrintText textElement) throws JRException
	{
		String currentColumnName = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean hasCurrentColumnData = textElement.getPropertiesMap().containsProperty(JRXlsAbstractMetadataExporter.PROPERTY_DATA);
			String currentColumnData = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_DATA);
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(textElement, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
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

			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && textElement.getBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(textElement.getBackcolor(), true);
			}

			StyleInfo baseStyle = isIgnoreTextFormatting(textElement) 
					? new StyleInfo(
							mode,
							WHITE,
							horizontalAlignment,
							verticalAlignment,
							(short)0,
							null,
							(BoxStyle)null, 
							isWrapText(textElement) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getColumnAutoFit(textElement)),
							isCellLocked(textElement),
							isShrinkToFit(textElement)
							)
					: new StyleInfo(
							mode, 
							backcolor,
							horizontalAlignment, 
							verticalAlignment,
							rotation, 
							cellFont,
							textElement,
							isWrapText(textElement) || Boolean.TRUE.equals(((JExcelApiExporterNature)nature).getColumnAutoFit(textElement)),
							isCellLocked(textElement),
							isShrinkToFit(textElement)
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
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CURRENT_SHEET_TOO_MANY_ROWS,
							null,
							e);
				}
				catch (WriteException e)
				{
					throw new JRException(e);
				}
			}

			JRStyledText styledText = null;
			String textStr = null;
			if(hasCurrentColumnData)
			{
				styledText = new JRStyledText();
				if (currentColumnData != null)
				{
					styledText.append(currentColumnData);
				} 
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
			
			addTextElement(textElement, textStr, baseStyle, repeatValue, currentColumnName, hasCurrentColumnData);
		}
	}

	protected void addTextElement(JRPrintText textElement, String textStr, StyleInfo baseStyle, boolean repeatValue, String currentColumnName, boolean hasCurrentColumnData) throws JRException
	{
		if (columnNames.size() > 0)
		{
			if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export but was not read yet and comes in the expected order
			{
				addCell(textElement, textStr, baseStyle, currentRow, currentColumnName, hasCurrentColumnData);
			} 
			else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export, was not read yet, but it is read after it should be
					|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) // the column is for export and was already read
			{
				if(rowIndex == 1 && getCurrentItemConfiguration().isWriteHeader())
				{
					writeReportHeader();
				}
				
				writeCurrentRow(currentRow, repeatedValues);
				currentRow = new HashMap<String, Object>();
				addCell(textElement, textStr, baseStyle, currentRow, currentColumnName, hasCurrentColumnData);
			}
			// set auto fill columns
			if(repeatValue)
			{
				if ( currentColumnName != null && currentColumnName.length() > 0 && textStr.length() > 0)
				{
					addCell(textElement, textStr, baseStyle, repeatedValues, currentColumnName, hasCurrentColumnData);
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
				if(rowIndex == 1 && getCurrentItemConfiguration().isWriteHeader())
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
	
	protected void addCell(JRPrintText text, String textStr, StyleInfo baseStyle, Map<String, Object> cellValueMap, String currentColumnName, boolean hasCurrentColumnData) throws JRException
	{
		CellValue cellValue = null;
		TextValue textValue = null;
		
		int colIndex = columnNamesMap.get(currentColumnName) ;

		String textFormula = hasCurrentColumnData ? null : getFormula(text);
		if( text != null && textFormula != null)
		{
			// if the cell has formula, we try create a formula cell
			textValue = getTextValue(text, textStr);
			cellValue = getFormulaCellValue(colIndex, rowIndex, text, textValue, textFormula, baseStyle, isComplexFormat(text));
		}
		
		if (cellValue == null)
		{
			JxlReportConfiguration configuration = getCurrentItemConfiguration();
			// there was no formula, or the formula cell creation failed
			if (configuration.isDetectCellType())
			{
				if (textFormula == null)
				{
					// there was no formula, so textValue was not created
					textValue = getTextValue(text, textStr, hasCurrentColumnData);
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
			String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
			if (convertedPattern != null)
			{
				baseStyle.setDisplayFormat(getDateFormat(convertedPattern));
			}
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
			String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
			if (convertedPattern != null)
			{
				baseStyle.setDisplayFormat(getDateFormat(convertedPattern));
			}
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

		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) 
		{
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
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
	
			Renderable renderer = element.getRenderable();
	
			if (
				renderer != null &&
				availableImageWidth > 0 &&
				availableImageHeight > 0
				)
			{
				if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE)
				{
					// Image renderers are all asked for their image data and dimension at some point. 
					// Better to test and replace the renderer now, in case of lazy load error.
					renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForImageData(renderer, element.getOnErrorTypeValue());
					if (renderer != null)
					{
						renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, element.getOnErrorTypeValue());
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
	
				Dimension2D dimension = renderer.getDimension(jasperReportsContext);
				if (dimension != null)
				{
					normalWidth = (int) dimension.getWidth();
					normalHeight = (int) dimension.getHeight();
				}
	
				float xalignFactor = 0f;
				switch (element.getHorizontalImageAlign())
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
				switch (element.getVerticalImageAlign())
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
						int dpi = getPropertiesUtil().getIntegerProperty(Renderable.PROPERTY_IMAGE_DPI, 72);
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
							jasperReportsContext,
							grx, 
							new Rectangle(
								(int) (xalignFactor * (availableImageWidth - normalWidth)),
								(int) (yalignFactor * (availableImageHeight - normalHeight)),
								normalWidth, 
								normalHeight
								)
							);
	
						imageData = JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, ImageTypeEnum.PNG);
	
						break;
					}
					case FILL_FRAME:
					{
						imageData = renderer.getImageData(jasperReportsContext);
	
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
	
							imageData = renderer.getImageData(jasperReportsContext);
						}
	
						break;
					}
				}
	
				Pattern mode = this.backgroundMode;
				Colour background = WHITE;
	
				JxlReportConfiguration configuration = getCurrentItemConfiguration();
				
				if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && element.getBackcolor() != null)
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
						isWrapText(element),
						isCellLocked(element),
						isShrinkToFit(element)
						);
	
				addBlankElement(cellStyle2, repeatValue, currentColumnName);
				try
				{
					int colIndex = columnNamesMap.get(currentColumnName);
					WritableImage image = new WritableImage(colIndex, rowIndex, 1, 1, imageData);
					ImageAnchorTypeEnum imageAnchorType = 
						ImageAnchorTypeEnum.getByName(
							JRPropertiesUtil.getOwnProperty(element, XlsReportConfiguration.PROPERTY_IMAGE_ANCHOR_TYPE)
							);
					if (imageAnchorType == null)
					{
						imageAnchorType = configuration.getImageAnchorType();
						if (imageAnchorType == null)
						{
							imageAnchorType = ImageAnchorTypeEnum.MOVE_NO_SIZE;
						}
					}
					setAnchorType(image, imageAnchorType);
					sheet.addImage(image);
				}
				catch (Exception ex)
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
							null,
							ex);
				}
				catch (Error err)
				{
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
							null,
							err);
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
		JxlExporterConfiguration configuration = getCurrentConfiguration();
		Colour colour;
		if (configuration.isCreateCustomPalette())
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
		boolean isFontSizeFixEnabled = getCurrentItemConfiguration().isFontSizeFixEnabled();

		WritableFont cellFont = null;

		if (this.loadedFonts != null && this.loadedFonts.size() > 0)
		{
			for (int i = 0; i < this.loadedFonts.size(); i++)
			{
				WritableFont cf = (WritableFont) this.loadedFonts.get(i);

				int fontSize = (int)font.getFontsize();
				if (isFontSizeFixEnabled)
				{
					fontSize -= 1;
				}
				
				String fontName = font.getFontName();

				FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontName, locale);
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
				int fontSize = (int)font.getFontsize();
				if (isFontSizeFixEnabled)
				{
					fontSize -= 1;
				}

				String fontName = font.getFontName();

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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_LOADED_FONTS_ERROR,
					null,
					e);
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
			if(element != null)
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
		protected final boolean isShrinkToFit;
		private DisplayFormat displayFormat;
		private int hashCode;

		protected StyleInfo(
				Pattern mode, 
				Colour backcolor, 
				int horizontalAlignment, 
				int verticalAlignment, 
				int rotation, 
				WritableFont font, 
				JRBoxContainer element,
				boolean wrapText,
				boolean cellLocked,
				boolean shrinkToFit
				)
		{
			this(
				mode,
				backcolor,
				horizontalAlignment,
				verticalAlignment,
				rotation,
				font,
				element == null ? null : new BoxStyle(element),
				wrapText,
				cellLocked,
				shrinkToFit
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
				boolean cellLocked,
				boolean shrinkToFit
				)
		{
			this.mode = mode;
			this.backcolor = backcolor;
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font;
			
			this.box = box;
			this.isWrapText = shrinkToFit ? false : wrapText;
			this.isCellLocked = cellLocked;
			this.isShrinkToFit = shrinkToFit;
			
			computeHash();
		}

		protected void computeHash()
		{
			int hash = this.mode.hashCode();
			hash = 31*hash + this.backcolor.hashCode();
			hash = 31*hash + this.horizontalAlignment;
			hash = 31*hash + this.verticalAlignment;
			hash = 31*hash + this.rotation;
			hash = 31*hash + (this.font == null ? 0 : this.font.hashCode());
			hash = 31*hash + (this.box == null ? 0 : this.box.hashCode());
			hash = 31*hash + (this.displayFormat == null ? 0 : this.displayFormat.hashCode());
			hash = 31*hash + (this.isWrapText ? 0 : 1);
			hash = 31*hash + (this.isCellLocked ? 0 : 1);
			hash = 31*hash + (this.isShrinkToFit ? 0 : 1);

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
				k.rotation == rotation && 
				(k.font == null ? font == null : k.font.equals(font)) &&
				(k.box == null ? box == null : (box != null && k.box.equals(box))) &&
				(k.displayFormat == null ? displayFormat == null : (displayFormat!= null && k.displayFormat.equals(displayFormat)) &&
				k.isWrapText == isWrapText && k.isCellLocked == isCellLocked && k.isShrinkToFit == isShrinkToFit);
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
				box + "," + displayFormat + "," + isWrapText + "," + isCellLocked + "," + isShrinkToFit + ")";
		}
	}


			private WritableCellFormat getLoadedCellStyle(
				Pattern mode, 
				Colour backcolor, 
				WritableFont font, 
				BoxStyle box,
				boolean wrapText,
				boolean cellLocked,
				boolean shrinkToFit
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
						wrapText,
						cellLocked,
						shrinkToFit
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
				if(styleKey.font == null)
				{
					cellStyle = new WritableCellFormat();
				}
				else if (styleKey.getDisplayFormat() == null)
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

				if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBorder) && styleKey.box != null)
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
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_CELL_FORMAT_TEMPLATE_ERROR,
						null,
						e);
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

	private final void setSheetSettings(SheetInfo sheetInfo, WritableSheet sheet)
	{
		rowIndex = 0;
		PageOrientation po;
		PaperSize ps;
		
		if (pageFormat.getOrientation() == OrientationEnum.PORTRAIT)
		{
			po = PageOrientation.PORTRAIT;
		}
		else
		{
			po = PageOrientation.LANDSCAPE;
		}
		if ((ps = getSuitablePaperSize()) != null)
		{
			sheet.setPageSetup(po, ps, 0, 0);
		}
		else
		{
			sheet.setPageSetup(po);
		}
		SheetSettings sheets = sheet.getSettings();
		
		JxlReportConfiguration configuration = getCurrentItemConfiguration();
		
		sheets.setTopMargin(0.0);
		sheets.setLeftMargin(0.0);
		sheets.setRightMargin(0.0);
		sheets.setBottomMargin(0.0);

		sheets.setHeaderMargin(0.0);
		sheets.setFooterMargin(0.0);

		String password = configuration.getPassword();
		if(password != null)
		{
			sheets.setPassword(password);
			sheets.setProtected(true);
		}
		
		String sheetHeaderLeft = configuration.getSheetHeaderLeft();
		if(sheetHeaderLeft != null)
		{
			sheets.getHeader().getLeft().append(sheetHeaderLeft);
		}
		
		String sheetHeaderCenter = configuration.getSheetHeaderCenter();
		if(sheetHeaderCenter != null)
		{
			sheets.getHeader().getCentre().append(sheetHeaderCenter);
		}
		
		String sheetHeaderRight = configuration.getSheetHeaderRight();
		if(sheetHeaderRight != null)
		{
			sheets.getHeader().getRight().append(sheetHeaderRight);
		}
		
		String sheetFooterLeft = configuration.getSheetFooterLeft();
		if(sheetFooterLeft != null)
		{
			sheets.getFooter().getLeft().append(sheetFooterLeft);
		}
		
		String sheetFooterCenter = configuration.getSheetFooterCenter();
		if(sheetFooterCenter != null)
		{
			sheets.getFooter().getCentre().append(sheetFooterCenter);
		}
		
		String sheetFooterRight = configuration.getSheetFooterRight();
		if(sheetFooterRight != null)
		{
			sheets.getFooter().getRight().append(sheetFooterRight);
		}
		
		if(sheetInfo.sheetFirstPageNumber != null && sheetInfo.sheetFirstPageNumber > 0)
		{
			sheets.setPageStart(sheetInfo.sheetFirstPageNumber);
			firstPageNotSet = false;
		}
		else
		{
			Integer documentFirstPageNumber = configuration.getFirstPageNumber();
			if(documentFirstPageNumber != null && documentFirstPageNumber > 0 && firstPageNotSet)
			{
				sheets.setPageStart(documentFirstPageNumber);
				firstPageNotSet = false;
			}
		}
		if(!firstPageNotSet && sheets.getFooter().getCentre().empty())
		{
			sheets.getFooter().getCentre().append("Page ");
			sheets.getFooter().getCentre().appendPageNumber();
		}
		
		boolean showGridlines = true;
		if (sheetInfo.sheetShowGridlines == null)
		{
			Boolean documentShowGridlines = configuration.isShowGridLines();
			if (documentShowGridlines != null)
			{
				showGridlines = documentShowGridlines;
			}
		}
		else
		{
			showGridlines = sheetInfo.sheetShowGridlines;
		}
		sheets.setShowGridLines(showGridlines);
		
		this.backgroundMode = Boolean.TRUE.equals(sheetInfo.whitePageBackground) 
				? Pattern.SOLID 
				: Pattern.NONE;
		
//		maxRowFreezeIndex = 0;
//		maxColumnFreezeIndex = 0;
	}

	private final PaperSize getSuitablePaperSize()
	{

		if (pageFormat == null)
		{
			return null;
		}
		long width = 0;
		long height = 0;
		PaperSize ps = null;

		if ((pageFormat.getPageWidth() != 0) && (pageFormat.getPageHeight() != 0))
		{

			double dWidth = (pageFormat.getPageWidth() / 72.0);
			double dHeight = (pageFormat.getPageHeight() / 72.0);

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
		HorizontalTextAlignEnum horizontalAlignment;
		VerticalTextAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue())
		{
			case LEFT :
			{
				switch (textElement.getHorizontalTextAlign())
				{
					case LEFT :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalTextAlign())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
					}
				}

				break;
			}
			case RIGHT :
			{
				switch (textElement.getHorizontalTextAlign())
				{
					case LEFT :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case JUSTIFIED :
					{
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
					}
				}

				switch (textElement.getVerticalTextAlign())
				{
					case TOP :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM :
					{
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					default :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
					}
				}

				break;
			}
			case UPSIDE_DOWN:
			case NONE :
			default :
			{
				horizontalAlignment = textElement.getHorizontalTextAlign();
				verticalAlignment = textElement.getVerticalTextAlign();
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
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if(currentColumnName != null && currentColumnName.length() > 0)
		{
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			int colIndex = columnNamesMap.get(currentColumnName);
			
			setColumnName(currentColumnName);
			setColumnWidth(columnNamesMap.get(currentColumnName), element.getWidth());
			setRowHeight(rowIndex, element.getHeight());
			
	
			GenericElementJExcelApiMetadataHandler handler = 
				(GenericElementJExcelApiMetadataHandler)GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
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


	public String getExporterKey()
	{
		return JXL_EXPORTER_KEY;
	}


	public String getExporterPropertiesPrefix()
	{
		return XLS_EXPORTER_PROPERTIES_PREFIX;
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
			return getPropertiesUtil().getBooleanProperty(element, PROPERTY_COMPLEX_FORMAT, getCurrentItemConfiguration().isComplexFormat());
		}
		return getCurrentItemConfiguration().isComplexFormat();
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
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_CURRENT_SHEET_TOO_MANY_ROWS,
					null,
					e);
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
	 */
	protected void setFreezePane(int rowIndex, int colIndex)
	{
		if(rowIndex > 0 || colIndex > 0)
		{
			SheetSettings settings = sheet.getSettings();
			settings.setVerticalFreeze(Math.max(0, rowIndex));
			settings.setHorizontalFreeze(Math.max(0, colIndex));
		}
	}

	/**
	 * @deprecated to be removed; replaced by {@link #setFreezePane(int, int)}
	 */ 
	protected void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge) {
		setFreezePane(rowIndex, colIndex);
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
	
	protected void setAnchorType(WritableImage image, ImageAnchorTypeEnum anchorType)
	{
		switch (anchorType)
		{
			case MOVE_SIZE: 
				image.setImageAnchor(WritableImage.MOVE_AND_SIZE_WITH_CELLS);
				break;
			case NO_MOVE_NO_SIZE:
				image.setImageAnchor(WritableImage.NO_MOVE_OR_SIZE_WITH_CELLS);
				break;
			case MOVE_NO_SIZE:
			default:
				image.setImageAnchor(WritableImage.MOVE_WITH_CELLS);
				break;
		}
	}
	
	protected TextValue getTextValue(JRPrintText text, String textStr, boolean hasCurrentColumnData)
	{
		if(!hasCurrentColumnData)
		{
			return getTextValue(text,textStr);
		}
		TextValue textValue;
		String valueClassName = text.getValueClassName();
		if (valueClassName == null)
		{
			textValue = getTextValueString(text, textStr);
		}
		else
		{
			try
			{
				Class<?> valueClass = textValueClasses.get(valueClassName);
				if (valueClass == null)
				{
					valueClass = JRClassLoader.loadClassForRealName(valueClassName);
					textValueClasses.put(valueClassName, valueClass);
				}
				
				if (java.lang.Number.class.isAssignableFrom(valueClass))
				{
					textValue = new NumberTextValue(textStr, Double.parseDouble(textStr), text.getPattern());
				}
				else if (Date.class.isAssignableFrom(valueClass))
				{
					textValue = new DateTextValue(textStr, isoDateFormat.parse(textStr), text.getPattern());
				}
				else if (Boolean.class.equals(valueClass))
				{
					textValue = new BooleanTextValue(textStr, Boolean.valueOf(textStr));
				}
				else
				{
					textValue = getTextValueString(text, textStr);
				} 
			}
			catch (ParseException e)
			{
				//log.warn("Error parsing text value", e);
				textValue = getTextValueString(text, textStr);
			}
			catch (ClassNotFoundException e)
			{
				//log.warn("Error loading text value class", e);
				textValue = getTextValueString(text, textStr);
			}			
		}
		return textValue;
	}
	
}

