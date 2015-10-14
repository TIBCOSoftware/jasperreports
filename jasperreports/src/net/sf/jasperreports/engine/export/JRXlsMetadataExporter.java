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
import java.awt.font.TextAttribute;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
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
import net.sf.jasperreports.engine.base.JRBaseFont;
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
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsMetadataExporterConfiguration;
import net.sf.jasperreports.export.XlsMetadataReportConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 */
public class JRXlsMetadataExporter extends JRXlsAbstractMetadataExporter<XlsMetadataReportConfiguration, XlsMetadataExporterConfiguration, JRXlsExporterContext> 
{

	private static final Log log = LogFactory.getLog(JRXlsMetadataExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String XLS_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "xls";
	public static short MAX_COLOR_INDEX = 56;
	public static short MIN_COLOR_INDEX = 10;	/* Indexes from 0 to 9 are reserved */
	public static String CURRENT_ROW_HEIGHT = "CURRENT_ROW_HEIGHT";
	
	private static Map<Color,HSSFColor> hssfColorsCache = new ReferenceMap();

	protected final DateFormat isoDateFormat = JRDataUtils.getIsoDateFormat();
	
	protected Map<StyleInfo,HSSFCellStyle> loadedCellStyles = new HashMap<StyleInfo,HSSFCellStyle>();
	protected Map<String,List<Hyperlink>> anchorLinks = new HashMap<String,List<Hyperlink>>();
	protected Map<Integer,List<Hyperlink>> pageLinks = new HashMap<Integer,List<Hyperlink>>();
	protected Map<String,HSSFName> anchorNames = new HashMap<String,HSSFName>();

	protected HSSFWorkbook workbook;
	protected HSSFSheet sheet;
	protected HSSFRow row;
	protected HSSFCell cell;
	protected HSSFCellStyle emptyCellStyle;
	protected CreationHelper createHelper;
	private HSSFPalette palette = null;
	protected Map<String, HSSFCellStyle> columnStylesMap;
	protected Map<String, Integer> columnWidths;

	/**
	 *
	 */
	protected short whiteIndex = (new HSSFColor.WHITE()).getIndex();
	protected short blackIndex = (new HSSFColor.BLACK()).getIndex();
	protected short customColorIndex = MIN_COLOR_INDEX;

	protected short backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;

	protected HSSFDataFormat dataFormat;

	protected HSSFPatriarch patriarch;
	
	protected static final String EMPTY_SHEET_NAME = "Sheet1";

	protected class ExporterContext extends BaseExporterContext implements JRXlsExporterContext	
	{
	}

	
	/**
	 * @see #JRXlsMetadataExporter(JasperReportsContext)
	 */
	public JRXlsMetadataExporter(){
		this(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public JRXlsMetadataExporter(JasperReportsContext jasperReportsContext)	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext();
	}


	/**
	 *
	 */
	protected Class<XlsMetadataExporterConfiguration> getConfigurationInterface()
	{
		return XlsMetadataExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<XlsMetadataReportConfiguration> getItemConfigurationInterface()
	{
		return XlsMetadataReportConfiguration.class;
	}
	

	@Override
	protected void initExport() 
	{
		super.initExport();
		
		sheet = null;
	}
	

	@Override
	protected void initReport() 
	{
		super.initReport();

		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		if (!configuration.isWhitePageBackground())
		{
			backgroundMode = HSSFCellStyle.NO_FILL;
		}

		nature = 
			new JRXlsMetadataExporterNature(
				jasperReportsContext, 
				filter, 
				configuration.isIgnoreGraphics(), 
				configuration.isIgnorePageMargins()
				);
	}

	protected void openWorkbook(OutputStream os) throws JRException	{
		XlsMetadataExporterConfiguration configuration = getCurrentConfiguration();
		String lcWorkbookTemplate = workbookTemplate == null ? configuration.getWorkbookTemplate() : workbookTemplate;
		if (lcWorkbookTemplate == null) {
			workbook = new HSSFWorkbook();
		} else {
			InputStream templateIs = null;
			try {
				templateIs = RepositoryUtil.getInstance(jasperReportsContext).getInputStreamFromLocation(lcWorkbookTemplate);
				if (templateIs == null)	{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_TEMPLATE_NOT_FOUND,  
							new Object[]{lcWorkbookTemplate} 
							);
				} else {
					workbook = new HSSFWorkbook(new POIFSFileSystem(templateIs));
					boolean keepSheets = keepTemplateSheets == null ? configuration.isKeepWorkbookTemplateSheets() : keepTemplateSheets;
					if (keepSheets) {
						sheetIndex += workbook.getNumberOfSheets();
					} else {
						for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
							workbook.removeSheetAt(i);
						}
					}
				}
			} catch (JRException e) {
				throw new JRRuntimeException(e);
			} catch (IOException e) {
				throw new JRRuntimeException(e);
			} finally {
				if (templateIs != null)	{
					try	{
						templateIs.close();
					} catch (IOException e)	{
					}
				}
			}
		}
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor((new HSSFColor.WHITE()).getIndex());
		emptyCellStyle.setFillPattern(backgroundMode);
		dataFormat = workbook.createDataFormat();
		createHelper = workbook.getCreationHelper();
		firstPageNotSet = true;
		palette =  workbook.getCustomPalette();
		customColorIndex = MIN_COLOR_INDEX; 
		columnWidths = new HashMap<String, Integer>();
	}

	protected void createSheet(SheetInfo sheetInfo)	{
		this.sheetInfo = sheetInfo;
		sheet = workbook.createSheet(sheetInfo.sheetName);
		patriarch = sheet.createDrawingPatriarch();
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(pageFormat.getOrientation() == OrientationEnum.LANDSCAPE);
		short paperSize = getSuitablePaperSize();
		
		XlsReportConfiguration configuration = getCurrentItemConfiguration();

		if(paperSize != -1)	{
			printSetup.setPaperSize(paperSize);
		}

		String password = configuration.getPassword();
		if(password != null) {
			sheet.protectSheet(password);
		}
		
		sheet.setMargin(Sheet.LeftMargin, 0.0);
		sheet.setMargin(Sheet.RightMargin, 0.0);
		sheet.setMargin(Sheet.TopMargin, 0.0);
		sheet.setMargin(Sheet.BottomMargin, 0.0);

		String sheetHeaderLeft = configuration.getSheetHeaderLeft();
		if(sheetHeaderLeft != null)	{
			sheet.getHeader().setLeft(sheetHeaderLeft);
		}
		
		String sheetHeaderCenter = configuration.getSheetHeaderCenter();
		if(sheetHeaderCenter != null) {
			sheet.getHeader().setCenter(sheetHeaderCenter);
		}
		
		String sheetHeaderRight = configuration.getSheetHeaderRight();
		if(sheetHeaderRight != null) {
			sheet.getHeader().setRight(sheetHeaderRight);
		}
		
		String sheetFooterLeft = configuration.getSheetFooterLeft();
		if(sheetFooterLeft != null) {
			sheet.getFooter().setLeft(sheetFooterLeft);
		}
		
		String sheetFooterCenter = configuration.getSheetFooterCenter();
		if(sheetFooterCenter != null) {
			sheet.getFooter().setCenter(sheetFooterCenter);
		}
		
		String sheetFooterRight = configuration.getSheetFooterRight();
		if(sheetFooterRight != null) {
			sheet.getFooter().setRight(sheetFooterRight);
		}
		
		RunDirectionEnum sheetDirection = configuration.getSheetDirection();
		if(sheetDirection != null) {
			printSetup.setLeftToRight(sheetDirection == RunDirectionEnum.LTR);
			sheet.setRightToLeft(sheetDirection == RunDirectionEnum.RTL);
		}
		
		if(sheetInfo.sheetFirstPageNumber != null && sheetInfo.sheetFirstPageNumber > 0) 
		{
			printSetup.setPageStart((short)sheetInfo.sheetFirstPageNumber.intValue());
			printSetup.setUsePage(true);
			firstPageNotSet = false;
		}
		else 
		{
			Integer documentFirstPageNumber = configuration.getFirstPageNumber();
			if(documentFirstPageNumber != null && documentFirstPageNumber > 0 && firstPageNotSet) 
			{
				printSetup.setPageStart((short)documentFirstPageNumber.intValue());
				printSetup.setUsePage(true);
				firstPageNotSet = false;
			}
		}
		if(!firstPageNotSet && (sheet.getFooter().getCenter() == null || sheet.getFooter().getCenter().length() == 0)) {
			sheet.getFooter().setCenter("Page " + HeaderFooter.page());
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
		sheet.setDisplayGridlines(showGridlines);
		
		backgroundMode = Boolean.TRUE.equals(sheetInfo.whitePageBackground) 
				? HSSFCellStyle.SOLID_FOREGROUND 
				: HSSFCellStyle.NO_FILL;
		
		
//		maxRowFreezeIndex = 0;
//		maxColumnFreezeIndex = 0;

		onePagePerSheetMap.put(sheetIndex, configuration.isOnePagePerSheet());
		sheetsBeforeCurrentReportMap.put(sheetIndex, sheetsBeforeCurrentReport);
	}

	protected void closeSheet()	
	{
		if (sheet == null)
		{
			return;
		}
		
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		
		if (isValidScale(sheetInfo.sheetPageScale))
		{
			printSetup.setScale((short)sheetInfo.sheetPageScale.intValue());
		}
		else
		{
			XlsReportConfiguration configuration = getCurrentItemConfiguration();

			Integer fitWidth = configuration.getFitWidth();
			if (fitWidth != null) 
			{
				printSetup.setFitWidth(fitWidth.shortValue());
				sheet.setAutobreaks(true);
			}

			Integer fitHeight = configuration.getFitHeight();
			fitHeight = 
				fitHeight == null
				? (Boolean.TRUE == configuration.isAutoFitPageHeight() 
					? (pageIndex - sheetInfo.sheetFirstPageIndex)
					: null)
				: fitHeight;
			if (fitHeight != null)
			{
				printSetup.setFitHeight(fitHeight.shortValue());
				sheet.setAutobreaks(true);
			}
		}
	}

	protected void closeWorkbook(OutputStream os) throws JRException {
		try	{
			for (Object anchorName : anchorNames.keySet()) {
				HSSFName anchor = anchorNames.get(anchorName);
				List<Hyperlink> linkList = anchorLinks.get(anchorName);
				anchor.setRefersToFormula("'" + workbook.getSheetName(anchor.getSheetIndex()) + "'!"+ anchor.getRefersToFormula());
				
				if(linkList != null && !linkList.isEmpty())	{
					for(Hyperlink link : linkList) {
						link.setAddress(anchor.getRefersToFormula());
					}
				}
			}
			
			int index = 0;
			for (Integer linkPage : pageLinks.keySet()) {
				List<Hyperlink> linkList = pageLinks.get(linkPage);
				if(linkList != null && !linkList.isEmpty()) {
					for(Hyperlink link : linkList) {
						index = onePagePerSheetMap.get(linkPage-1)!= null 
							? (onePagePerSheetMap.get(linkPage-1)
								? Math.max(0, linkPage - 1)
								: Math.max(0, sheetsBeforeCurrentReportMap.get(linkPage)))
							: 0;
						link.setAddress("'" + workbook.getSheetName(index)+ "'!$A$1");
					}
				}
			}
			
			for(int i=0; i < workbook.getNumberOfSheets(); i++)	{
				HSSFSheet currentSheet = workbook.getSheetAt(i);
				currentSheet.setForceFormulaRecalculation(true);
				for(String columnName : columnNames) {
					Integer columnWidth = columnWidths.get(columnName);
					if (columnWidth != null && columnWidth < Integer.MAX_VALUE) {
						currentSheet.setColumnWidth(columnNamesMap.get(columnName), Math.min(43 * columnWidth, 256*255));
					} else {
						currentSheet.autoSizeColumn(columnNamesMap.get(columnName), false);
					}
				}
			}
			workbook.write(os);
		} catch (IOException e) {
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);
		}
	}

	@Override
	protected void setColumnWidth(int col, int width) {
	}

	protected void setRowHeight(HSSFRow row) {
		Integer rowHeight = (Integer)currentRow.get(CURRENT_ROW_HEIGHT);
		if (row != null && rowHeight != null && rowHeight < Integer.MAX_VALUE) {
			row.setHeightInPoints((Integer)currentRow.get(CURRENT_ROW_HEIGHT));
		}
	}
	
	protected void adjustRowHeight(int rowHeight, Boolean isAutofit) {
		if(isAutofit != null && isAutofit) {
			currentRow.put(CURRENT_ROW_HEIGHT, Integer.MAX_VALUE);
		} else if(!currentRow.containsKey(CURRENT_ROW_HEIGHT) || (Integer)currentRow.get(CURRENT_ROW_HEIGHT) < rowHeight) {
			currentRow.put(CURRENT_ROW_HEIGHT, rowHeight);
		}
	}
	
	protected void adjustColumnWidth(String columnName, int columnWidth, Boolean isAutofit) {
		if(isAutofit != null && isAutofit) {
			columnWidths.put(columnName, Integer.MAX_VALUE);
		} else if(!columnWidths.containsKey(columnName) || columnWidths.get(columnName) < columnWidth) {
			columnWidths.put(columnName, columnWidth);
		}
	}

	protected void addBlankCell(HSSFCellStyle cellStyle, Map<String, Object> cellValueMap, String currentColumnName) throws JRException	{
		HSSFCellStyle currentStyle = cellStyle != null 
			? cellStyle 
			: (columnStylesMap.get(currentColumnName) != null ? columnStylesMap.get(currentColumnName) : emptyCellStyle);
		cellValueMap.put(currentColumnName, new CellSettings(currentStyle));
	}
	
	protected void writeCurrentRow(Map<String, Object> currentRow, Map<String, Object> repeatedValues)  throws JRException {
		row = sheet.createRow(sheet.getPhysicalNumberOfRows());
		setRowHeight(row);
		
		for(int i = 0; i< columnNames.size(); i++) {
			String columnName = columnNames.get(i);
			CellSettings cellSettings = (CellSettings)currentRow.get(columnName) == null 
				? (repeatedValues.get(columnName) != null 
				? (CellSettings)repeatedValues.get(columnName)
				: null)
				: (CellSettings)currentRow.get(columnName);
			cell = row.createCell(i);
			if(cellSettings != null) {
				int type = cellSettings.getCellType();
				cell.setCellType(type);
				Object cellValue = cellSettings.getCellValue();
				if(cellValue != null) {
					if(cellValue instanceof RichTextString) {
						cell.setCellValue((RichTextString)cellSettings.getCellValue());
					} else if (cellValue instanceof Number) {
						cell.setCellValue(((Number)cellSettings.getCellValue()).doubleValue());
					} else if (cellValue instanceof Date) {
						cell.setCellValue((Date)cellSettings.getCellValue());
					} else if(cellValue instanceof Boolean) {
						cell.setCellValue((Boolean)cellSettings.getCellValue());
					}else if(cellValue instanceof ImageSettings){
						ImageSettings imageSettings = (ImageSettings)cellValue;
						try {
							HSSFClientAnchor anchor = 
									new HSSFClientAnchor(
										0, 
										0, 
										0, 
										0, 
										(short)(i), 
										rowIndex, 
										(short)(i + 1), 
										rowIndex+1
										);
							anchor.setAnchorType(imageSettings.getAnchorType());
							patriarch.createPicture(anchor, imageSettings.getIndex());
						} catch (Exception ex) {
							throw 
								new JRException(
									EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
									null,
									ex);
						} catch (Error err) {
							throw 
								new JRException(
									EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
									null,
									err);
						}
					}
				}
				
				if(cellSettings.getCellStyle() != null) {
					cell.setCellStyle(cellSettings.getCellStyle());
				}
				if(cellSettings.getFormula() != null) {
					cell.setCellFormula(cellSettings.getFormula());
				}
				if(cellSettings.getLink() != null) {
					cell.setHyperlink(cellSettings.getLink());
				}
			}
		}
		++rowIndex;
	}
	

	protected void exportLine(JRPrintLine line) throws JRException {
		String currentColumnName = line.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) {
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(line, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			adjustColumnWidth(currentColumnName, line.getWidth(), ((JRXlsExporterNature)nature).getColumnAutoFit(line));
			adjustRowHeight(line.getHeight(), ((JRXlsExporterNature)nature).getRowAutoFit(line));
			
			short forecolor = getWorkbookColor(line.getLinePen().getLineColor()).getIndex();

			int side = BoxStyle.TOP;
			float ratio = line.getWidth() / line.getHeight();
			if (ratio > 1) {
				if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN)	{
					side = BoxStyle.TOP;
				} else {
					side = BoxStyle.BOTTOM;
				}
			} else {
				if (line.getDirectionValue() == LineDirectionEnum.TOP_DOWN) {
					side = BoxStyle.LEFT;
				} else {
					side = BoxStyle.RIGHT;
				}
			}
			BoxStyle boxStyle = new BoxStyle(side, line.getLinePen());

			short mode = backgroundMode;
			short backcolor = whiteIndex;
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && line.getBackcolor() != null) {
				mode = HSSFCellStyle.SOLID_FOREGROUND;
				backcolor = getWorkbookColor(line.getBackcolor()).getIndex();
			}

			HSSFCellStyle cellStyle =
				getLoadedCellStyle(
					mode,
					backcolor,
					HSSFCellStyle.ALIGN_LEFT,
					HSSFCellStyle.VERTICAL_TOP,
					(short)0,
					getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
					boxStyle,
					isCellLocked(line),
					isCellHidden(line),
					isShrinkToFit(line)
					);
			addBlankElement(cellStyle, repeatValue, currentColumnName);
		}
	}

	protected void exportRectangle(JRPrintGraphicElement element) throws JRException {
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		
		if (currentColumnName != null && currentColumnName.length() > 0) {
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			adjustColumnWidth(currentColumnName, element.getWidth(), ((JRXlsExporterNature)nature).getColumnAutoFit(element));
			adjustRowHeight(element.getHeight(), ((JRXlsExporterNature)nature).getRowAutoFit(element));
			
			short forecolor = getWorkbookColor(element.getLinePen().getLineColor()).getIndex();

			short mode = backgroundMode;
			short backcolor = whiteIndex;
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && element.getBackcolor() != null) {
				mode = HSSFCellStyle.SOLID_FOREGROUND;
				backcolor = getWorkbookColor(element.getBackcolor()).getIndex();
			}

			HSSFCellStyle cellStyle =
				getLoadedCellStyle(
					mode,
					backcolor,
					HSSFCellStyle.ALIGN_LEFT,
					HSSFCellStyle.VERTICAL_TOP,
					(short)0,
					getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
					new BoxStyle(element),
					isCellLocked(element),
					isCellHidden(element),
					isShrinkToFit(element)
					);
			addBlankElement(cellStyle, repeatValue, currentColumnName);
		}
	}


	protected void exportText(final JRPrintText textElement) throws JRException {
		String currentColumnName = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) {
			final boolean hasCurrentColumnData = textElement.getPropertiesMap().containsProperty(JRXlsAbstractMetadataExporter.PROPERTY_DATA);
			String currentColumnData = textElement.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_DATA);
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(textElement, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			adjustColumnWidth(currentColumnName, textElement.getWidth(), ((JRXlsExporterNature)nature).getColumnAutoFit(textElement));
			adjustRowHeight(textElement.getHeight(), isWrapText(textElement) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getRowAutoFit(textElement)));
			
			final short forecolor = getWorkbookColor(textElement.getForecolor()).getIndex();

			TextAlignHolder textAlignHolder = getTextAlignHolder(textElement);
			short horizontalAlignment = getHorizontalAlignment(textAlignHolder);
			short verticalAlignment = getVerticalAlignment(textAlignHolder);
			short rotation = getRotation(textAlignHolder);
			
			XlsReportConfiguration configuration = getCurrentItemConfiguration();

			short mode = backgroundMode;
			short backcolor = whiteIndex;
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && textElement.getBackcolor() != null) {
				mode = HSSFCellStyle.SOLID_FOREGROUND;
				backcolor = getWorkbookColor(textElement.getBackcolor()).getIndex();
			}

			final StyleInfo baseStyle = isIgnoreTextFormatting(textElement) 
					? new StyleInfo(
							mode,
							whiteIndex,
							horizontalAlignment,
							verticalAlignment,
							(short)0,
							null,
							(BoxStyle)null, 
							isWrapText(textElement) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getColumnAutoFit(textElement)),
							isCellLocked(textElement),
							isCellHidden(textElement),
							isShrinkToFit(textElement)
							)
					: new StyleInfo(
							mode,
							backcolor,
							horizontalAlignment,
							verticalAlignment,
							rotation,
							getLoadedFont(textElement, forecolor, null, getTextLocale(textElement)),
							new BoxStyle(textElement), 
							isWrapText(textElement) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getColumnAutoFit(textElement)),
							isCellLocked(textElement),
							isCellHidden(textElement),
							isShrinkToFit(textElement)
							);
			
			final JRStyledText styledText;
			final String textStr;
			final String formula;
			final CellSettings cellSettings = new CellSettings();
			if (hasCurrentColumnData)
			{
				styledText = new JRStyledText();
				if (currentColumnData != null) {
					styledText.append(currentColumnData);
				}
				textStr = currentColumnData;
				formula = null;
			} else {
				styledText = getStyledText(textElement);
				if (styledText != null) {
					textStr = styledText.getText();
				} else {
					textStr = null;
				}
				formula = getFormula(textElement);
			}
			
			if (formula != null) {	
				TextValue value = getTextValue(textElement, textStr);
				
				if (value instanceof NumberTextValue) {
					String convertedPattern = getConvertedPattern(textElement, ((NumberTextValue)value).getPattern());
					if (convertedPattern != null) {
						baseStyle.setDataFormat(dataFormat.getFormat(convertedPattern));
					}
				} else if (value instanceof DateTextValue) {
					String convertedPattern = getConvertedPattern(textElement, ((DateTextValue)value).getPattern());
					if (convertedPattern != null) {
						baseStyle.setDataFormat(dataFormat.getFormat(convertedPattern));
					}
				}
				
				cellSettings.importValues(HSSFCell.CELL_TYPE_FORMULA, getLoadedCellStyle(baseStyle), null, formula);
				
			} else if (getCurrentItemConfiguration().isDetectCellType()) {
				TextValue value = getTextValue(textElement, textStr);
				value.handle(new TextValueHandler() {
					public void handle(StringTextValue textValue) {
						if (JRCommonText.MARKUP_NONE.equals(textElement.getMarkup())) {
							cellSettings.importValues(HSSFCell.CELL_TYPE_STRING, getLoadedCellStyle(baseStyle), new HSSFRichTextString(textValue.getText()));
						} else {
							cellSettings.importValues(HSSFCell.CELL_TYPE_STRING, getLoadedCellStyle(baseStyle), getRichTextString(styledText, forecolor, textElement, getTextLocale(textElement)));
						}
					}

					public void handle(NumberTextValue textValue) {
						String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
						if (convertedPattern != null) {
							baseStyle.setDataFormat(dataFormat.getFormat(convertedPattern));
						}
						Number value = null;
						if (hasCurrentColumnData) {
							if (textStr != null) {
								try {
									value = Double.parseDouble(textStr);
								} catch (NumberFormatException nfe) {
									throw new JRRuntimeException(nfe);
								}
							}
						} else {
							value = textValue.getValue();
						}
						cellSettings.importValues(HSSFCell.CELL_TYPE_NUMERIC, getLoadedCellStyle(baseStyle), value);
					}

					public void handle(DateTextValue textValue) {
						String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
						if(convertedPattern != null) {
							baseStyle.setDataFormat(dataFormat.getFormat(convertedPattern));
						}
						Date value = null;
						if (hasCurrentColumnData) {
							if (textStr != null) {
								try {
									value = new Date(Long.parseLong(textStr));
								} catch (NumberFormatException nfe) {
									try {
										value = isoDateFormat.parse(textStr);
									} catch (ParseException pe) {
										throw new JRRuntimeException(pe);
									}
								}
							}
						} else {
							value = textValue.getValue() == null ? null : translateDateValue(textElement, textValue.getValue());
						}
						cellSettings.importValues(HSSFCell.CELL_TYPE_NUMERIC, getLoadedCellStyle(baseStyle), value);
					}

					public void handle(BooleanTextValue textValue) {
						Boolean value = hasCurrentColumnData ? Boolean.valueOf(textStr) : textValue.getValue();
						cellSettings.importValues(HSSFCell.CELL_TYPE_BOOLEAN, getLoadedCellStyle(baseStyle), value);
					}

				});
			} else {
				if (JRCommonText.MARKUP_NONE.equals(textElement.getMarkup())) {
					cellSettings.importValues(HSSFCell.CELL_TYPE_STRING, getLoadedCellStyle(baseStyle), new HSSFRichTextString(textStr));
				} else {
					cellSettings.importValues(HSSFCell.CELL_TYPE_STRING, getLoadedCellStyle(baseStyle), getRichTextString(styledText, forecolor, textElement, getTextLocale(textElement)));
				}
			}
			
			if(!configuration.isIgnoreAnchors()) {
				String anchorName = textElement.getAnchorName();
				if(anchorName != null) {
					HSSFName aName = workbook.createName();
					aName.setNameName(JRStringUtil.getJavaIdentifier(anchorName));
					aName.setSheetIndex(workbook.getSheetIndex(sheet));
					CellReference cRef = new CellReference(rowIndex, columnNamesMap.get(currentColumnName), true, true);
					aName.setRefersToFormula(cRef.formatAsString());
					anchorNames.put(anchorName, aName);
				}
			}

			setHyperlinkCell(textElement, cellSettings);
			addTextElement(cellSettings, textStr, repeatValue, currentColumnName);
		}
	}

	protected void setHyperlinkCell(JRPrintHyperlink hyperlink, CellSettings cellSettings)
	{
		Hyperlink link = null;

		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK, hyperlink);
		if (ignoreHyperlink == null) {
			ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
		}

		if (!ignoreHyperlink) {
			JRHyperlinkProducer customHandler = getHyperlinkProducer(hyperlink);
			if (customHandler == null) {
				switch (hyperlink.getHyperlinkTypeValue()) {
					case REFERENCE: {
						String href = hyperlink.getHyperlinkReference();
						if (href != null) {
							link = createHelper.createHyperlink(Hyperlink.LINK_URL);
							link.setAddress(href);
						}
						break;
					}
					case LOCAL_ANCHOR : {
						if(!getCurrentItemConfiguration().isIgnoreAnchors()) {
							String href = hyperlink.getHyperlinkAnchor();
							if (href != null) {
								link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
								if(anchorLinks.containsKey(href)) {
									(anchorLinks.get(href)).add(link);
								} else {
									List<Hyperlink> hrefList = new ArrayList<Hyperlink>();
									hrefList.add(link);
									anchorLinks.put(href, hrefList);
								}
							}
						}
						break;
					}
					case LOCAL_PAGE :
					{
						Integer hrefPage = (getCurrentItemConfiguration().isOnePagePerSheet() ? hyperlink.getHyperlinkPage() : 0);
						if (hrefPage != null) {
							link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
							if(pageLinks.containsKey(sheetsBeforeCurrentReport+hrefPage)) {
								pageLinks.get(sheetsBeforeCurrentReport + hrefPage).add(link);
							} else {
								List<Hyperlink> hrefList = new ArrayList<Hyperlink>();
								hrefList.add(link);
								pageLinks.put(sheetsBeforeCurrentReport + hrefPage, hrefList);
							}
						}
						break;
					}
					case REMOTE_ANCHOR : {
						String href = hyperlink.getHyperlinkReference();
						if (href != null && hyperlink.getHyperlinkAnchor() != null) {
							href = href + "#" + hyperlink.getHyperlinkAnchor();
							link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
							link.setAddress(href);
							
						}
						break;
					}
					case REMOTE_PAGE : {
						String href = hyperlink.getHyperlinkReference();
						if (href != null && hyperlink.getHyperlinkPage() != null) {
							href = href + "#JR_PAGE_ANCHOR_0_" + hyperlink.getHyperlinkPage().toString();
							link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
							link.setAddress(href);
							
						}
						break;
					}
					case NONE:
					default: {
					}
				}
			} else {
				String href = customHandler.getHyperlink(hyperlink);
				if (href != null) {
					link = createHelper.createHyperlink(Hyperlink.LINK_URL);
					link.setAddress(href);
				}
			}
			
			if(link != null) {
				//TODO: make tooltips functional
//				if(hyperlink.getHyperlinkTooltip() != null)
//				{
//					link.setLabel(hyperlink.getHyperlinkTooltip());
//				}
				cellSettings.setLink(link);
			}
		}
	}
	
	protected void addTextElement(CellSettings cellSettings, String textStr, boolean repeatValue, String currentColumnName) throws JRException {
		if (columnNames.size() > 0) {
			if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) {	// the column is for export but was not read yet and comes in the expected order
				addCell(cellSettings, currentRow, currentColumnName);
			} else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) // the column is for export, was not read yet, but it is read after it should be
					|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) {	// the column is for export and was already read

				if(rowIndex == 1 && getCurrentItemConfiguration().isWriteHeader()) {
					writeReportHeader();
				}
				writeCurrentRow(currentRow, repeatedValues);
				currentRow.clear();
				addCell(cellSettings, currentRow, currentColumnName);
			}
			// set auto fill columns
			if(repeatValue) {
				if ( currentColumnName != null && currentColumnName.length() > 0 && textStr.length() > 0) {
					addCell(cellSettings, repeatedValues, currentColumnName);
				}
			} else {
				repeatedValues.remove(currentColumnName);
			}
		}
	}
	
	protected void addBlankElement(HSSFCellStyle cellStyle, boolean repeatValue, String currentColumnName) throws JRException {
		if (columnNames.size() > 0) {
			if (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && isColumnReadOnTime(currentRow, currentColumnName)) {	// the column is for export but was not read yet and comes in the expected order
				addBlankCell(cellStyle, currentRow, currentColumnName); 
			} else if ( (columnNames.contains(currentColumnName) && !currentRow.containsKey(currentColumnName) && !isColumnReadOnTime(currentRow, currentColumnName)) 	// the column is for export, was not read yet, but it is read after it should be
					|| (columnNames.contains(currentColumnName) && currentRow.containsKey(currentColumnName)) ) {	// the column is for export and was already read
				
				if(rowIndex == 1 && getCurrentItemConfiguration().isWriteHeader()) {
					writeReportHeader();
				}
				
				writeCurrentRow(currentRow, repeatedValues);
				currentRow.clear();
				addBlankCell(cellStyle, currentRow, currentColumnName);
			}
			
			// set auto fill columns
			if(repeatValue) {
				if (repeatValue && currentColumnName != null && currentColumnName.length() > 0 && cellStyle != null) {
					addBlankCell(cellStyle, repeatedValues, currentColumnName);
				}
			} else {
				repeatedValues.remove(currentColumnName);
			}
		}
	}

	protected void addCell(CellSettings cellSettings, Map<String, Object> cellValueMap, String currentColumnName) throws JRException {
		cellValueMap.put(currentColumnName, cellSettings);
	}

	protected HSSFRichTextString getRichTextString(JRStyledText styledText, short forecolor, JRFont defaultFont, Locale locale) {
		String text = styledText.getText();
		HSSFRichTextString richTextStr = new HSSFRichTextString(text);
		int runLimit = 0;
		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length()) {
			Map<Attribute,Object> attributes = iterator.getAttributes();
			JRFont runFont = attributes.isEmpty()? defaultFont : new JRBaseFont(attributes);
			short runForecolor = attributes.get(TextAttribute.FOREGROUND) != null  
				? getWorkbookColor((Color)attributes.get(TextAttribute.FOREGROUND)).getIndex() 
				: forecolor;
			HSSFFont font = getLoadedFont(runFont, runForecolor, attributes, locale);
			richTextStr.applyFont(iterator.getIndex(), runLimit, font);
			iterator.setIndex(runLimit);
		}
		return richTextStr;
	}

	public void exportImage(JRPrintImage element) throws JRException {

		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if (currentColumnName != null && currentColumnName.length() > 0) {
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			
			setColumnName(currentColumnName);
			adjustColumnWidth(currentColumnName, element.getWidth(), ((JRXlsExporterNature)nature).getColumnAutoFit(element));
			adjustRowHeight(element.getHeight(), Boolean.TRUE.equals(((JRXlsExporterNature)nature).getRowAutoFit(element)));
			
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

			Renderable renderer = element.getRenderable();

			if (
				renderer != null &&
				availableImageWidth > 0 &&
				availableImageHeight > 0
				) {
				
				if (renderer.getTypeValue() == RenderableTypeEnum.IMAGE) {
					// Image renderers are all asked for their image data and dimension at some point.
					// Better to test and replace the renderer now, in case of lazy load error.
					renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForImageData(renderer, element.getOnErrorTypeValue());
					if (renderer != null) {
						renderer = RenderableUtil.getInstance(jasperReportsContext).getOnErrorRendererForDimension(renderer, element.getOnErrorTypeValue());
					}
				} else {
					renderer =
						new JRWrappingSvgRenderer(
							renderer,
							new Dimension(element.getWidth(), element.getHeight()),
							ModeEnum.OPAQUE == element.getModeValue() ? element.getBackcolor() : null
							);
				}
			} else {
				renderer = null;
			}

			if (renderer != null) {
				int normalWidth = availableImageWidth;
				int normalHeight = availableImageHeight;

				Dimension2D dimension = renderer.getDimension(jasperReportsContext);
				if (dimension != null) {
					normalWidth = (int) dimension.getWidth();
					normalHeight = (int) dimension.getHeight();
				}

				float xalignFactor = 0f;
				switch (element.getHorizontalImageAlign()) {
					case RIGHT: {
						xalignFactor = 1f;
						break;
					}
					case CENTER: {
						xalignFactor = 0.5f;
						break;
					}
					case LEFT:
					default: {
						xalignFactor = 0f;
						break;
					}
				}

				float yalignFactor = 0f;
				switch (element.getVerticalImageAlign()) {
					case BOTTOM: {
						yalignFactor = 1f;
						break;
					}
					case MIDDLE: {
						yalignFactor = 0.5f;
						break;
					}
					case TOP:
					default: {
						yalignFactor = 0f;
						break;
					}
				}

				byte[] imageData = null;
//				int topOffset = 0;
//				int leftOffset = 0;
//				int bottomOffset = 0;
//				int rightOffset = 0;
//				
				switch (element.getScaleImageValue()) {
					case CLIP: {
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

//						topOffset = topPadding;
//						leftOffset = leftPadding;
//						bottomOffset = bottomPadding;
//						rightOffset = rightPadding;

						imageData = JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, ImageTypeEnum.PNG);

						break;
					}
					case FILL_FRAME: {
//						topOffset = topPadding;
//						leftOffset = leftPadding;
//						bottomOffset = bottomPadding;
//						rightOffset = rightPadding;

						imageData = renderer.getImageData(jasperReportsContext);

						break;
					}
					case RETAIN_SHAPE:
					default: {
						if (element.getHeight() > 0) {
							double ratio = (double) normalWidth / (double) normalHeight;

							if (ratio > (double) availableImageWidth / (double) availableImageHeight) {
								normalWidth = availableImageWidth;
								normalHeight = (int) (availableImageWidth / ratio);
							} else {
								normalWidth = (int) (availableImageHeight * ratio);
								normalHeight = availableImageHeight;
							}

//							topOffset = topPadding + (int) (yalignFactor * (availableImageHeight - normalHeight));
//							leftOffset = leftPadding + (int) (xalignFactor * (availableImageWidth - normalWidth));
//							bottomOffset = bottomPadding + (int) ((1f - yalignFactor) * (availableImageHeight - normalHeight));
//							rightOffset = rightPadding + (int) ((1f - xalignFactor) * (availableImageWidth - normalWidth));

							imageData = renderer.getImageData(jasperReportsContext);
						}
						break;
					}
				}

				XlsMetadataReportConfiguration configuration = getCurrentItemConfiguration();
				
				short mode = backgroundMode;
				short backcolor = whiteIndex;
				if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && element.getBackcolor() != null) {
					mode = HSSFCellStyle.SOLID_FOREGROUND;
					backcolor = getWorkbookColor(element.getBackcolor()).getIndex();
				}

				short forecolor = getWorkbookColor(element.getLineBox().getPen().getLineColor()).getIndex();

				if(element.getModeValue() == ModeEnum.OPAQUE ) {
					backcolor = getWorkbookColor(element.getBackcolor()).getIndex();
				}

				HSSFCellStyle cellStyle =
					getLoadedCellStyle(
						mode,
						backcolor,
						HSSFCellStyle.ALIGN_LEFT,
						HSSFCellStyle.VERTICAL_TOP,
						(short)0,
						getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
						new BoxStyle(element),
						isCellLocked(element),
						isCellHidden(element),
						isShrinkToFit(element)
						);

				addBlankElement(cellStyle, false, currentColumnName);
				
				int colIndex = columnNamesMap.get(currentColumnName);
				try {
					HSSFClientAnchor anchor = 
							new HSSFClientAnchor(
								0, 
								0, 
								0, 
								0, 
								(short)(colIndex), 
								rowIndex, 
								(short)(colIndex + 1), 
								rowIndex+1
								);
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
					anchor.setAnchorType(imageAnchorType.getValue());
					//pngEncoder.setImage(bi);
					//int imgIndex = workbook.addPicture(pngEncoder.pngEncode(), HSSFWorkbook.PICTURE_TYPE_PNG);
					int imgIndex = workbook.addPicture(imageData, HSSFWorkbook.PICTURE_TYPE_PNG);
					patriarch.createPicture(anchor, imgIndex);
					
					// set auto fill columns
					if(repeatValue) {
						CellSettings cellSettings = new CellSettings(cellStyle);
						cellSettings.setCellValue(new ImageSettings(imgIndex, anchor.getAnchorType()));
						addCell(cellSettings, repeatedValues, currentColumnName);
					} else {
						repeatedValues.remove(currentColumnName);
					}
					
//					setHyperlinkCell(element);
				} catch (Exception ex) {
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
							null,
							ex);
				} catch (Error err) {
					throw 
						new JRException(
							EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
							null,
							err);
				}
			}
		}
	}


	protected HSSFCellStyle getLoadedCellStyle(StyleInfo style) {
		HSSFCellStyle cellStyle = loadedCellStyles.get(style);
		if (cellStyle == null) {
			
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(style.backcolor);
			cellStyle.setFillPattern(style.mode);
			cellStyle.setAlignment(style.horizontalAlignment);
			cellStyle.setVerticalAlignment(style.verticalAlignment);
			cellStyle.setRotation(style.rotation);
			if(style.font != null)
			{
				cellStyle.setFont(style.font);
			}
			cellStyle.setWrapText(style.lcWrapText);
			cellStyle.setLocked(style.lcCellLocked);
			cellStyle.setHidden(style.lcCellHidden);
			cellStyle.setShrinkToFit(style.lcShrinkToFit);

			if (style.hasDataFormat()) {
				cellStyle.setDataFormat(style.getDataFormat());
			}

			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBorder) && style.box != null) {
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
			BoxStyle box,
			boolean isCellLocked,
			boolean isCellHidden,
			boolean isShrinkToFit
			) {
		
			return getLoadedCellStyle(new StyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, box, true, isCellLocked, isCellHidden, isShrinkToFit));
		}

	/**
	 *
	 */
	protected static short getBorderStyle(JRPen pen) {
		float lineWidth = pen.getLineWidth().floatValue();

		if (lineWidth > 0f) {
			switch (pen.getLineStyleValue()) {
				case DOUBLE : {
					return HSSFCellStyle.BORDER_DOUBLE;
				}
				case DOTTED : {
					return HSSFCellStyle.BORDER_DOTTED;
				}
				case DASHED : {
					if (lineWidth >= 1f) {
						return HSSFCellStyle.BORDER_MEDIUM_DASHED;
					}
					return HSSFCellStyle.BORDER_DASHED;
				}
				case SOLID :
				default : {
					if (lineWidth >= 2f) {
						return HSSFCellStyle.BORDER_THICK;
					}
					else if (lineWidth >= 1f) {
						return HSSFCellStyle.BORDER_MEDIUM;
					} else if (lineWidth >= 0.5f) {
						return HSSFCellStyle.BORDER_THIN;
					}
					return HSSFCellStyle.BORDER_HAIR;
				}
			}
		}
		return HSSFCellStyle.BORDER_NONE;
	}

	
	public static TextAlignHolder getTextAlignHolder(JRPrintText textElement) {
		HorizontalTextAlignEnum horizontalAlignment;
		VerticalTextAlignEnum verticalAlignment;
		RotationEnum rotation = textElement.getRotationValue();

		switch (textElement.getRotationValue()) {
			case LEFT : {
				switch (textElement.getHorizontalTextAlign()) {
					case LEFT : {
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case CENTER : {
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT : {
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case JUSTIFIED : {
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default : {
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalTextAlign()) {
					case TOP : {
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					case MIDDLE : {
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM : {
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					default : {
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
					}
				}
				break;
			}
			case RIGHT : {
				switch (textElement.getHorizontalTextAlign()) {
					case LEFT : {
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case CENTER : {
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case RIGHT : {
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case JUSTIFIED : {
						verticalAlignment = VerticalTextAlignEnum.JUSTIFIED;
						break;
					}
					default : {
						verticalAlignment = VerticalTextAlignEnum.TOP;
					}
				}

				switch (textElement.getVerticalTextAlign()) {
					case TOP : {
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					case MIDDLE : {
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case BOTTOM : {
						horizontalAlignment = HorizontalTextAlignEnum.LEFT;
						break;
					}
					default : {
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
					}
				}
				break;
			}
			case UPSIDE_DOWN:
			case NONE :
			default : {
				horizontalAlignment = textElement.getHorizontalTextAlign();
				verticalAlignment = textElement.getVerticalTextAlign();
			}
		}
		return new TextAlignHolder(horizontalAlignment, verticalAlignment, rotation);
	}

	protected void exportFrame(JRPrintFrame frame) throws JRException {
		
		for (Object element : frame.getElements()) {
			if (element instanceof JRPrintLine) {
				exportLine((JRPrintLine)element);
			} else if (element instanceof JRPrintRectangle) {
				exportRectangle((JRPrintRectangle)element);
			} else if (element instanceof JRPrintEllipse) {
				exportRectangle((JRPrintEllipse)element);
			} else if (element instanceof JRPrintImage) {
				exportImage((JRPrintImage) element);
			} else if (element instanceof JRPrintText) {
				exportText((JRPrintText)element);
			} else if (element instanceof JRPrintFrame) {
				exportFrame((JRPrintFrame) element);
			} else if (element instanceof JRGenericPrintElement) {
				exportGenericElement((JRGenericPrintElement) element);
			}
		}
	}


	protected void exportGenericElement(JRGenericPrintElement element) throws JRException {
		String currentColumnName = element.getPropertiesMap().getProperty(JRXlsAbstractMetadataExporter.PROPERTY_COLUMN_NAME);
		if(currentColumnName != null && currentColumnName.length() > 0) {
			boolean repeatValue = getPropertiesUtil().getBooleanProperty(element, JRXlsAbstractMetadataExporter.PROPERTY_REPEAT_VALUE, false);
			int colIndex = columnNamesMap.get(currentColumnName);
			
			setColumnName(currentColumnName);
			adjustColumnWidth(currentColumnName, element.getWidth(), ((JRXlsExporterNature)nature).getColumnAutoFit(element));
			adjustRowHeight(element.getHeight(), Boolean.TRUE.equals(((JRXlsExporterNature)nature).getRowAutoFit(element)));
			
	
			GenericElementXlsMetadataHandler handler = 
				(GenericElementXlsMetadataHandler)GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
					element.getGenericType(), 
					XLS_EXPORTER_KEY);
	
			if (handler != null) {
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
			} else {
				if (log.isDebugEnabled()) {
					log.debug("No XLS Metadata generic element handler for " 
							+ element.getGenericType());
				}
			}
		}
	}


	protected ExporterNature getNature() {
		return nature;
	}


	public String getExporterKey() {
		return XLS_EXPORTER_KEY;
	}

	public String getExporterPropertiesPrefix() {
		return XLS_EXPORTER_PROPERTIES_PREFIX;
	}

	protected void setColumnName(String currentColumnName) {
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
	protected void writeReportHeader() throws JRException {
		row = sheet.createRow(0);
		for(int i = 0; i< columnNames.size(); i++) {
			String columnName = columnNames.get(i);
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(columnName));
		}
	}
	
	@Override
	protected void setSheetName(String sheetName) {
		workbook.setSheetName(workbook.getSheetIndex(sheet) , sheetName);
	}
	
	@Override
	protected void setFreezePane(int rowIndex, int colIndex) {
		//FIXME: recalculate freeze pane edges depending on exported columns
		
//		if(rowIndex > 0 || colIndex > 0)
//		{
//			sheet.createFreezePane(Math.max(0, colIndex), Math.max(0, rowIndex));
//		}
	}
	
	/**
	 * @deprecated to be removed; replaced by {@link #setFreezePane(int, int)}
	 */ 
	protected void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge) {
		setFreezePane(rowIndex, colIndex);
	}

	@Override
	protected void setAutoFilter(String autoFilterRange) {
		//TODO: recalculate autoFilterRange depending on exported columns
//		sheet.setAutoFilter(CellRangeAddress.valueOf(autoFilterRange));
	}


	@Override
	protected void setRowLevels(XlsRowLevelInfo levelInfo, String level) {
		//TODO: recalculate row levels depending on exported columns
//		if(levelInfo != null) {
//			Map<String, Integer> levelMap = levelInfo.getLevelMap();
//			if(levelMap != null && levelMap.size() > 0) {
//				for(String l : levelMap.keySet()) {
//					if (level == null || l.compareTo(level) >= 0) {
//						Integer startIndex = levelMap.get(l);
//						if(levelInfo.getEndIndex() >= startIndex) {
//							sheet.groupRow(startIndex, levelInfo.getEndIndex());
//						}
//					}
//				}
//				sheet.setRowSumsBelow(false);
//			}
//		}
	}


	private final short getSuitablePaperSize() {

		if (pageFormat == null) {
			return -1;
		}
		long width = 0;
		long height = 0;
		short ps = -1;

		if ((pageFormat.getPageWidth() != 0) && (pageFormat.getPageHeight() != 0)) {

			double dWidth = (pageFormat.getPageWidth() / 72.0);
			double dHeight = (pageFormat.getPageHeight() / 72.0);

			height = Math.round(dHeight * 25.4);
			width = Math.round(dWidth * 25.4);

			// Compare to ISO 216 A-Series (A3-A5). All other ISO 216 formats
			// not supported by POI Api yet.
			// A3 papersize also not supported by POI Api yet.
			for (int i = 4; i < 6; i++) {
				int w = calculateWidthForDinAN(i);
				int h = calculateHeightForDinAN(i);

				if (((w == width) && (h == height)) || ((h == width) && (w == height))) {
					if (i == 4) {
						ps = HSSFPrintSetup.A4_PAPERSIZE;
					} else if (i == 5) {
						ps = HSSFPrintSetup.A5_PAPERSIZE;
					}
					break;
				}
			}
			
			//envelope sizes
			if (ps == -1) {
				// ISO 269 sizes - "Envelope DL" (110 � 220 mm)
				if (((width == 110) && (height == 220)) || ((width == 220) && (height == 110))) {
					ps = HSSFPrintSetup.ENVELOPE_DL_PAPERSIZE;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == -1) {
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216))) {
					ps = HSSFPrintSetup.LETTER_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216))) {
					ps = HSSFPrintSetup.LEGAL_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				else if (((width == 190) && (height == 254)) || ((width == 254) && (height == 190))) {
					ps = HSSFPrintSetup.EXECUTIVE_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supported by POI Api yet.
			}
		}
		return ps;
	}
	
	private short getHorizontalAlignment(TextAlignHolder alignment) {
		switch (alignment.horizontalAlignment) 
		{
			case RIGHT:
				return HSSFCellStyle.ALIGN_RIGHT;
			case CENTER:
				return HSSFCellStyle.ALIGN_CENTER;
			case JUSTIFIED:
				return HSSFCellStyle.ALIGN_JUSTIFY;
			case LEFT:
			default:
				return HSSFCellStyle.ALIGN_LEFT;
		}
	}

	private short getVerticalAlignment(TextAlignHolder alignment) {
		switch (alignment.verticalAlignment) {
			case BOTTOM:
				return HSSFCellStyle.VERTICAL_BOTTOM;
			case MIDDLE:
				return HSSFCellStyle.VERTICAL_CENTER;
			case JUSTIFIED:
				return HSSFCellStyle.VERTICAL_JUSTIFY;
			case TOP:
			default:
				return HSSFCellStyle.VERTICAL_TOP;
		}
	}

	private short getRotation(TextAlignHolder alignment) {
		switch (alignment.rotation) {
			case LEFT:
				return 90;
			case RIGHT:
				return -90;
			case UPSIDE_DOWN:
			case NONE:
			default:
				return 0;
		}
	}

	/**
	 *
	 */
	protected HSSFColor getWorkbookColor(Color awtColor) {
		HSSFColor color = null;
		if(awtColor != null) {
			byte red = (byte)awtColor.getRed();
			byte green = (byte)awtColor.getGreen();
			byte blue = (byte)awtColor.getBlue();

			XlsExporterConfiguration configuration = getCurrentConfiguration();
			
			if (configuration.isCreateCustomPalette()) {
				try {
					color = palette.findColor(red,green, blue) != null
						? palette.findColor(red,green, blue)
						: palette.addColor(red,green, blue);
				} catch(Exception e) {
					if(customColorIndex < MAX_COLOR_INDEX) {
						palette.setColorAtIndex(customColorIndex, red, green, blue);
						color = palette.getColor(customColorIndex++);
					} else {
						color = palette.findSimilarColor(red, green, blue);
					}
				}
			}
		}
		return color == null ? getNearestColor(awtColor) : color;
	}

	/**
	 *
	 */
	protected static HSSFColor getNearestColor(Color awtColor)
	{
		HSSFColor color = hssfColorsCache.get(awtColor);		
		if (color == null) {
			Map<?,?> triplets = HSSFColor.getTripletHash();
			if (triplets != null) {
				Collection<?> keys = triplets.keySet();
				if (keys != null && keys.size() > 0) {
					Object key = null;
					HSSFColor crtColor = null;
					short[] rgb = null;
					int diff = 0;
					int minDiff = 999;
					for (Iterator<?> it = keys.iterator(); it.hasNext();) {
						key = it.next();
						crtColor = (HSSFColor) triplets.get(key);
						rgb = crtColor.getTriplet();
						diff = Math.abs(rgb[0] - awtColor.getRed()) + Math.abs(rgb[1] - awtColor.getGreen()) + Math.abs(rgb[2] - awtColor.getBlue());
						if (diff < minDiff) {
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
	protected HSSFFont getLoadedFont(JRFont font, short forecolor, Map<Attribute,Object> attributes, Locale locale) {

		HSSFFont cellFont = null;
		String fontName = font.getFontName();
		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext).getFontInfo(fontName, locale);
		if (fontInfo != null) {
			//fontName found in font extensions
			FontFamily family = fontInfo.getFontFamily();
			String exportFont = family.getExportFont(getExporterKey());
			if (exportFont != null) {
				fontName = exportFont;
			}
		}

		boolean isFontSizeFixEnabled = getCurrentItemConfiguration().isFontSizeFixEnabled();
		
		short superscriptType = HSSFFont.SS_NONE;
		
		if( attributes != null && attributes.get(TextAttribute.SUPERSCRIPT) != null) {
			Object value = attributes.get(TextAttribute.SUPERSCRIPT);
			if(TextAttribute.SUPERSCRIPT_SUPER.equals(value)) {
				superscriptType = HSSFFont.SS_SUPER;
			} else if(TextAttribute.SUPERSCRIPT_SUB.equals(value)) {
				superscriptType = HSSFFont.SS_SUB;
			}
		}
		for (int i = 0; i < loadedFonts.size(); i++) {
			HSSFFont cf = (HSSFFont)loadedFonts.get(i);
			short fontSize = (short)font.getFontsize();
			if (isFontSizeFixEnabled) {
				fontSize -= 1;
			}
			if (
				cf.getFontName().equals(fontName) &&
				(cf.getColor() == forecolor) &&
				(cf.getFontHeightInPoints() == fontSize) &&
				((cf.getUnderline() == HSSFFont.U_SINGLE)?(font.isUnderline()):(!font.isUnderline())) &&
				(cf.getStrikeout() == font.isStrikeThrough()) &&
				((cf.getBoldweight() == HSSFFont.BOLDWEIGHT_BOLD)?(font.isBold()):(!font.isBold())) &&
				(cf.getItalic() == font.isItalic()) &&
				(cf.getTypeOffset() == superscriptType)
				) {
				cellFont = cf;
				break;
			}
		}

		if (cellFont == null) {
			
			cellFont = workbook.createFont();
			cellFont.setFontName(fontName);
			cellFont.setColor(forecolor);
			short fontSize = (short)font.getFontsize();
			if (isFontSizeFixEnabled) {
				fontSize -= 1;
			}
			cellFont.setFontHeightInPoints(fontSize);

			if (font.isUnderline()) {
				cellFont.setUnderline(HSSFFont.U_SINGLE);
			}
			if (font.isStrikeThrough()) {
				cellFont.setStrikeout(true);
			}
			if (font.isBold()) {
				cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			}
			if (font.isItalic()) {
				cellFont.setItalic(true);
			}

			cellFont.setTypeOffset(superscriptType);
			loadedFonts.add(cellFont);
		}
		return cellFont;
	}
	
	@Override
	protected void createSheet(CutsInfo xCuts, SheetInfo sheetInfo) {
	}

	@Override
	protected void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException {
	}

	@Override
	protected void addRowBreak(int rowIndex)
	{
		sheet.setRowBreak(rowIndex);
	}

	@Override
	protected void setColumnWidth(int col, int width, boolean autoFit) {
	}
	
	/**
	 * 
	 */
	protected class BoxStyle {
		protected static final int TOP = 0;
		protected static final int LEFT = 1;
		protected static final int BOTTOM = 2;
		protected static final int RIGHT = 3;

		protected short[] borderStyle = new short[4];
		protected short[] borderColour = new short[4];
		private int hash;

		public BoxStyle(int side, JRPen pen) {
			borderStyle[side] = JRXlsMetadataExporter.getBorderStyle(pen);
			borderColour[side] = JRXlsMetadataExporter.this.getWorkbookColor(pen.getLineColor()).getIndex();
			hash = computeHash();
		}

		public BoxStyle(JRPrintElement element) {
			if(element != null)
			{
				if (element instanceof JRBoxContainer) {
					setBox(((JRBoxContainer)element).getLineBox());
				}
				if (element instanceof JRCommonGraphicElement) {
					setPen(((JRCommonGraphicElement)element).getLinePen());
				}
			}
		}

		public void setBox(JRLineBox box) {
			borderStyle[TOP] = JRXlsMetadataExporter.getBorderStyle(box.getTopPen());
			borderColour[TOP] = JRXlsMetadataExporter.this.getWorkbookColor(box.getTopPen().getLineColor()).getIndex();

			borderStyle[BOTTOM] = JRXlsMetadataExporter.getBorderStyle(box.getBottomPen());
			borderColour[BOTTOM] = JRXlsMetadataExporter.this.getWorkbookColor(box.getBottomPen().getLineColor()).getIndex();

			borderStyle[LEFT] = JRXlsMetadataExporter.getBorderStyle(box.getLeftPen());
			borderColour[LEFT] = JRXlsMetadataExporter.this.getWorkbookColor(box.getLeftPen().getLineColor()).getIndex();

			borderStyle[RIGHT] = JRXlsMetadataExporter.getBorderStyle(box.getRightPen());
			borderColour[RIGHT] = JRXlsMetadataExporter.this.getWorkbookColor(box.getRightPen().getLineColor()).getIndex();

			hash = computeHash();
		}

		public void setPen(JRPen pen) {
			if (
				borderStyle[TOP] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[LEFT] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[BOTTOM] == HSSFCellStyle.BORDER_NONE
				&& borderStyle[RIGHT] == HSSFCellStyle.BORDER_NONE
				) {
				short style = JRXlsMetadataExporter.getBorderStyle(pen);
				short colour = JRXlsMetadataExporter.this.getWorkbookColor(pen.getLineColor()).getIndex();

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

		private int computeHash() {
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

		public int hashCode() {
			return hash;
		}

		public boolean equals(Object o) {
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

		public String toString() {
			return "(" +
				borderStyle[TOP] + "/" + borderColour[TOP] + "," +
				borderStyle[BOTTOM] + "/" + borderColour[BOTTOM] + "," +
				borderStyle[LEFT] + "/" + borderColour[LEFT] + "," +
				borderStyle[RIGHT] + "/" + borderColour[RIGHT] + ")";
		}
	}
	

	/**
	 * 
	 */
	protected class StyleInfo {
		protected final short mode;
		protected final short backcolor;
		protected final short horizontalAlignment;
		protected final short verticalAlignment;
		protected final short rotation;
		protected final HSSFFont font;
		protected final BoxStyle box;
		protected final boolean lcWrapText;
		protected final boolean lcCellLocked;
		protected final boolean lcCellHidden;
		protected final boolean lcShrinkToFit;
		private short lcDataFormat = -1;
		private int hashCode;
	
		public StyleInfo(
			short mode,
			short backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			HSSFFont font,
			BoxStyle box,
			boolean wrapText,
			boolean cellLocked,
			boolean cellHidden,
			boolean shrinkToFit
			) {
			this.mode = mode;
			this.backcolor = backcolor;
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
			this.font = font;
	
			this.box = box;
			this.lcWrapText = shrinkToFit ? false : wrapText;
			this.lcCellLocked = cellLocked;
			this.lcCellHidden = cellHidden;
			this.lcShrinkToFit = shrinkToFit;
			hashCode = computeHash();
		}
	
		protected int computeHash() {
			int hash = mode;
			hash = 31*hash + backcolor;
			hash = 31*hash + horizontalAlignment;
			hash = 31*hash + verticalAlignment;
			hash = 31*hash + rotation;
			hash = 31*hash + (font == null ? 0 : font.getIndex());
			hash = 31*hash + (box == null ? 0 : box.hashCode());
			hash = 31*hash + lcDataFormat;
			hash = 31*hash + (lcWrapText ? 0 : 1);
			hash = 31*hash + (lcCellLocked ? 0 : 1);
			hash = 31*hash + (lcCellHidden ? 0 : 1);
			hash = 31*hash + (lcShrinkToFit ? 0 : 1);
			return hash;
		}
	
		public void setDataFormat(short dataFormat) {
			this.lcDataFormat = dataFormat;
			hashCode = computeHash();
		}
	
		public boolean hasDataFormat() {
			return lcDataFormat != -1;
		}
	
		public short getDataFormat() {
			return lcDataFormat;
		}
	
		public int hashCode() {
			return hashCode;
		}
	
		public boolean equals(Object o) {
			StyleInfo s = (StyleInfo) o;
	
			return s.mode == mode
					&& s.backcolor == backcolor
					&& s.horizontalAlignment == horizontalAlignment
					&& s.verticalAlignment == verticalAlignment
					&& s.rotation == rotation
					&& (s.font == null ? font == null : (font != null && s.font.getIndex() == font.getIndex()))
					&& (s.box == null ? box == null : (box != null && s.box.equals(box)))
					&& s.rotation == rotation && s.lcWrapText == lcWrapText 
					&& s.lcCellLocked == lcCellLocked && s.lcCellHidden == lcCellHidden
					&& s.lcShrinkToFit == lcShrinkToFit;	//FIXME should dataformat be part of equals? it is part of toString()...
		}
	
		public String toString() {
			return "(" +
				mode + "," + backcolor + "," +
				horizontalAlignment + "," + verticalAlignment + "," +
				rotation + "," + font + "," +
				box + "," + lcDataFormat + "," + lcWrapText + "," + lcCellLocked + "," + lcCellHidden + "," + lcShrinkToFit + ")";
		}
	}
	
	
	protected class CellSettings {
		private int cellType;
		private HSSFCellStyle cellStyle;
		private Object cellValue;
		private String formula;
		private Hyperlink link;
		
		public CellSettings() {
		}
		public CellSettings(HSSFCellStyle cellStyle) {
			this(HSSFCell.CELL_TYPE_BLANK, cellStyle, null);
		}
		public CellSettings(
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue) {
			this(cellType, cellStyle, cellValue, null);
		}
		public CellSettings(
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue,
				String formula) {
			this(cellType, cellStyle, cellValue, formula, null);
		}
		
		public CellSettings(
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue,
				String formula,
				Hyperlink link) {
			this.cellType = cellType;
			this.cellStyle = cellStyle;
			this.cellValue = cellValue;
			this.formula = formula;
			this.link = link;
		}
		
		public HSSFCellStyle getCellStyle() {
			return cellStyle;
		}
		public void setCellStyle(HSSFCellStyle cellStyle) {
			this.cellStyle = cellStyle;
		}
		public int getCellType() {
			return cellType;
		}
		public void setCellType(int cellType) {
			this.cellType = cellType;
		}
		public Object getCellValue() {
			return cellValue;
		}
		public void setCellValue(Object cellValue) {
			this.cellValue = cellValue;
		}
		public String getFormula() {
			return formula;
		}
		public void setFormula(String formula) {
			this.formula = formula;
		}
		public Hyperlink getLink() {
			return link;
		}
		public void setLink(Hyperlink link) {
			this.link = link;
		}
		
		public void importValues(				
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue) {
			this.importValues(cellType, cellStyle, cellValue, null);
		}
		public void importValues(				
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue,
				String formula) {
			this.importValues(cellType, cellStyle, cellValue, formula, null);
		}
		public void importValues(				
				int cellType,
				HSSFCellStyle cellStyle,
				Object cellValue,
				String formula,
				Hyperlink link) {
			this.cellType = cellType;
			this.cellStyle = cellStyle;
			this.cellValue = cellValue;
			this.formula = formula;
			this.link = link;
		}
	}
}

class ImageSettings {

	private int index;
	private int anchorType;
	
	public ImageSettings() {
	}
	
	public ImageSettings(int index, int anchorType) {
		this.index = index;
		this.anchorType = anchorType;
	}

	public int getIndex() {
		return index;
	}
	
	public int getAnchorType() {
		return anchorType;
	}
}
