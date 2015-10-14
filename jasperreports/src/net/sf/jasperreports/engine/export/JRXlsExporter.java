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
 * Contributors:
 * Wolfgang - javabreak@users.sourceforge.net
 * Mario Daepp - mdaepp@users.sourceforge.net
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRCommonText;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
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
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.repo.RepositoryUtil;

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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;


/**
 * Exports a JasperReports document to XLS format. It has binary output type and exports the document to
 * a grid-based layout.
 * 
 * @see net.sf.jasperreports.engine.export.JRXlsAbstractExporter
 * @see net.sf.jasperreports.export.XlsExporterConfiguration
 * @see net.sf.jasperreports.export.XlsReportConfiguration
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JRXlsExporter extends JRXlsAbstractExporter<XlsReportConfiguration, XlsExporterConfiguration, JRXlsExporterContext>
{

	private static final Log log = LogFactory.getLog(JRXlsExporter.class);
	
	/**
	 * The exporter key, as used in
	 * {@link GenericElementHandlerEnviroment#getElementHandler(JRGenericElementType, String)}.
	 */
	public static final String XLS_EXPORTER_KEY = JRPropertiesUtil.PROPERTY_PREFIX + "xls";
	public static short MAX_COLOR_INDEX = 56;
	public static short MIN_COLOR_INDEX = 10;	/* Indexes from 0 to 9 are reserved */
	
	private static Map<HSSFColor, short[]> hssfColorsRgbs;
	
	static
	{
		Hashtable<String, HSSFColor> hssfColors = HSSFColor.getTripletHash();
		hssfColorsRgbs = new HashMap<HSSFColor, short[]>();
		for (HSSFColor color : hssfColors.values())
		{
			hssfColorsRgbs.put(color, color.getTriplet());
		}
	}

	protected Map<StyleInfo,HSSFCellStyle> loadedCellStyles = new HashMap<StyleInfo,HSSFCellStyle>();
	protected Map<String,List<Hyperlink>> anchorLinks = new HashMap<String,List<Hyperlink>>();
	protected Map<Integer,List<Hyperlink>> pageLinks = new HashMap<Integer,List<Hyperlink>>();
	protected Map<String,HSSFName> anchorNames = new HashMap<String,HSSFName>();
	protected Map<HSSFSheet,List<Integer>> autofitColumns = new HashMap<HSSFSheet,List<Integer>>();

	/**
	 *
	 */
	protected HSSFWorkbook workbook;
	protected HSSFSheet sheet;
	protected HSSFRow row;
	protected HSSFCell cell;
	protected HSSFCellStyle emptyCellStyle;
	protected CreationHelper createHelper;
	private HSSFPalette palette = null;
	private Map<Color,HSSFColor> hssfColorsCache = new HashMap<Color, HSSFColor>();

	/**
	 *
	 */
	protected short whiteIndex = (new HSSFColor.WHITE()).getIndex();
	protected short blackIndex = (new HSSFColor.BLACK()).getIndex();
	protected short customColorIndex = MIN_COLOR_INDEX;

	protected short backgroundMode = HSSFCellStyle.SOLID_FOREGROUND;

	protected HSSFDataFormat dataFormat;

	protected HSSFPatriarch patriarch;
	
	protected class ExporterContext extends BaseExporterContext implements JRXlsExporterContext
	{
	}

	
	/**
	 * @see #JRXlsExporter(JasperReportsContext)
	 */
	public JRXlsExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}


	/**
	 *
	 */
	public JRXlsExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
		
		exporterContext = new ExporterContext();
		
		maxColumnIndex = 255;
	}


	/**
	 *
	 */
	protected Class<XlsExporterConfiguration> getConfigurationInterface()
	{
		return XlsExporterConfiguration.class;
	}


	/**
	 *
	 */
	protected Class<XlsReportConfiguration> getItemConfigurationInterface()
	{
		return XlsReportConfiguration.class;
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
			new JRXlsExporterNature(
				jasperReportsContext, 
				filter, 
				configuration.isIgnoreGraphics(), 
				configuration.isIgnorePageMargins()
				);
	}


	protected void openWorkbook(OutputStream os)
	{
		XlsExporterConfiguration configuration = getCurrentConfiguration();
		String lcWorkbookTemplate = workbookTemplate == null ? configuration.getWorkbookTemplate() : workbookTemplate;
		if (lcWorkbookTemplate == null)
		{
			workbook = new HSSFWorkbook();
		}
		else
		{
			InputStream templateIs = null;
			try 
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
					workbook = new HSSFWorkbook(new POIFSFileSystem(templateIs));
					boolean keepSheets = keepTemplateSheets == null ? configuration.isKeepWorkbookTemplateSheets() : keepTemplateSheets;
					if (keepSheets)
					{
						sheetIndex += workbook.getNumberOfSheets();
					}
					else
					{
						for(int i = 0; i < workbook.getNumberOfSheets(); i++)
						{
							workbook.removeSheetAt(i);
						}
					}
				}
			} 
			catch (JRException e) 
			{
				throw new JRRuntimeException(e);
			} 
			catch (IOException e) 
			{
				throw new JRRuntimeException(e);
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
		emptyCellStyle = workbook.createCellStyle();
		emptyCellStyle.setFillForegroundColor((new HSSFColor.WHITE()).getIndex());
		emptyCellStyle.setFillPattern(backgroundMode);
		dataFormat = workbook.createDataFormat();
		createHelper = workbook.getCreationHelper();
		firstPageNotSet = true;
		palette =  workbook.getCustomPalette();
		customColorIndex = MIN_COLOR_INDEX; 
		autofitColumns = new HashMap<HSSFSheet,List<Integer>>();
	}


	protected void createSheet(CutsInfo xCuts, SheetInfo sheetInfo)
	{
		sheet = workbook.createSheet(sheetInfo.sheetName);
		patriarch = sheet.createDrawingPatriarch();
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(pageFormat.getOrientation() == OrientationEnum.LANDSCAPE);
		short paperSize = getSuitablePaperSize();

		if(paperSize != -1)
		{
			printSetup.setPaperSize(paperSize);
		}
		
		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		String password = configuration.getPassword();
		if(password != null)
		{
			sheet.protectSheet(password);
		}
		
		sheet.setMargin(Sheet.LeftMargin, 0.0);
		sheet.setMargin(Sheet.RightMargin, 0.0);
		sheet.setMargin(Sheet.TopMargin, 0.0);
		sheet.setMargin(Sheet.BottomMargin, 0.0);

		String sheetHeaderLeft = configuration.getSheetHeaderLeft();
		if(sheetHeaderLeft != null)
		{
			sheet.getHeader().setLeft(sheetHeaderLeft);
		}
		
		String sheetHeaderCenter = configuration.getSheetHeaderCenter();
		if(sheetHeaderCenter != null)
		{
			sheet.getHeader().setCenter(sheetHeaderCenter);
		}
		
		String sheetHeaderRight = configuration.getSheetHeaderRight();
		if(sheetHeaderRight != null)
		{
			sheet.getHeader().setRight(sheetHeaderRight);
		}
		
		String sheetFooterLeft = configuration.getSheetFooterLeft();
		if(sheetFooterLeft != null)
		{
			sheet.getFooter().setLeft(sheetFooterLeft);
		}
		
		String sheetFooterCenter = configuration.getSheetFooterCenter();
		if(sheetFooterCenter != null)
		{
			sheet.getFooter().setCenter(sheetFooterCenter);
		}
		
		String sheetFooterRight = configuration.getSheetFooterRight();
		if(sheetFooterRight != null)
		{
			sheet.getFooter().setRight(sheetFooterRight);
		}
		
		RunDirectionEnum sheetDirection = configuration.getSheetDirection();
		if(sheetDirection != null)
		{
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
		if(!firstPageNotSet && (sheet.getFooter().getCenter() == null || sheet.getFooter().getCenter().length() == 0))
		{
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
//		
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
	
	protected void closeWorkbook(OutputStream os) throws JRException
	{
		try
		{
			for (Object anchorName : anchorNames.keySet())		// the anchorNames map contains no entries for reports with ignore anchors == true;
			{
				HSSFName anchor = anchorNames.get(anchorName);
				List<Hyperlink> linkList = anchorLinks.get(anchorName);
				anchor.setRefersToFormula("'" + workbook.getSheetName(anchor.getSheetIndex()) + "'!"+ anchor.getRefersToFormula());
				
				if(linkList != null && !linkList.isEmpty())
				{
					for(Hyperlink link : linkList)
					{
						link.setAddress(anchor.getRefersToFormula());
					}
				}
			}
			 
			int index = 0;
			for (Integer linkPage : pageLinks.keySet()) {		// the pageLinks map contains no entries for reports with ignore hyperlinks == true 
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
			
			for(int i=0; i < workbook.getNumberOfSheets(); i++)
			{
				HSSFSheet currentSheet = workbook.getSheetAt(i);
				currentSheet.setForceFormulaRecalculation(true);
				List<Integer> autofitList= autofitColumns.get(currentSheet);
				if(autofitList != null)
				{
					for(Integer j : autofitList) 
					{
						currentSheet.autoSizeColumn(j, false);
					}
				}
			}
			
			workbook.write(os);
		}
		catch (IOException e)
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
		if (autoFit)
		{
			//the autofit will be applied before closing workbook, after the sheet completion
			List<Integer> autofitList= autofitColumns.get(sheet) != null ? autofitColumns.get(sheet) : new ArrayList<Integer>();
			autofitList.add(col);
			autofitColumns.put(sheet, autofitList);
		}
		else
		{
			sheet.setColumnWidth(col, Math.min(43 * width, 256*255));
		}
	}

	protected void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo)
	{
		row = sheet.getRow(rowIndex);
		
		if (row == null)
		{
			row = sheet.createRow(rowIndex);
		}

		boolean isAutoFit = yCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) 
				&& (Boolean)yCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW);
		if (!isAutoFit)
		{
			row.setHeightInPoints(lastRowHeight);
		}
	}

	protected void addRowBreak(int rowIndex)
	{
		sheet.setRowBreak(rowIndex);
	}

//	protected void setCell(JRExporterGridCell gridCell, int colIndex, int rowIndex)
//	{
//		HSSFCell emptyCell = row.getCell(colIndex);
//		if (emptyCell == null)
//		{
//			emptyCell = row.createCell(colIndex);
//			emptyCell.setCellStyle(emptyCellStyle);
//		}
//	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		cell = row.createCell(colIndex);

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		
		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getWorkbookColor(gridCell.getCellBackcolor()).getIndex();
		}

		short forecolor = blackIndex;
		if (gridCell.getForecolor() != null)
		{
			forecolor = getWorkbookColor(gridCell.getForecolor()).getIndex();
		}

		HSSFCellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT,
				HSSFCellStyle.VERTICAL_TOP,
				(short)0,
				getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
				gridCell,
				true, 
				true, 
				false, 
				false
				);

		cell.setCellStyle(cellStyle);
	}

	protected void addOccupiedCell(OccupiedGridCell occupiedGridCell, int colIndex, int rowIndex)
	{
	}
	
	/**
	 *
	 */
	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int colIndex, int rowIndex)
	{
		short forecolor = getWorkbookColor(line.getLinePen().getLineColor()).getIndex();

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

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getWorkbookColor(gridCell.getCellBackcolor()).getIndex();
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
				false,
				isCellLocked(line),
				isCellHidden(line),
				isShrinkToFit(line)
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
		short forecolor = getWorkbookColor(element.getLinePen().getLineColor()).getIndex();

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getWorkbookColor(gridCell.getCellBackcolor()).getIndex();
		}

		HSSFCellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT,
				HSSFCellStyle.VERTICAL_TOP,
				(short)0,
				getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
				gridCell,
				isWrapText(element),
				isCellLocked(element),
				isCellHidden(element),
				isShrinkToFit(element)
				);

		createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

		cell = row.createCell(colIndex);
		cell.setCellStyle(cellStyle);
	}



	public void exportText(JRPrintText textElement, JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
		JRStyledText styledText = getStyledText(textElement);

		if (styledText == null)
		{
			return;
		}

		short forecolor = getWorkbookColor(textElement.getForecolor()).getIndex();

		TextAlignHolder textAlignHolder = getTextAlignHolder(textElement);
		short horizontalAlignment = getHorizontalAlignment(textAlignHolder);
		short verticalAlignment = getVerticalAlignment(textAlignHolder);
		short rotation = getRotation(textAlignHolder);

		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getWorkbookColor(gridCell.getCellBackcolor()).getIndex();
		}

		StyleInfo baseStyle = isIgnoreTextFormatting(textElement) 
				? new StyleInfo(
						mode,
						whiteIndex,
						horizontalAlignment,
						verticalAlignment,
						(short)0,
						null,
						(JRExporterGridCell)null, 
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
					gridCell, 
					isWrapText(textElement) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getColumnAutoFit(textElement)),
					isCellLocked(textElement),
					isCellHidden(textElement),
					isShrinkToFit(textElement)
					);
		createTextCell(textElement, gridCell, colIndex, rowIndex, styledText, baseStyle, forecolor);
	}


	protected void createTextCell(final JRPrintText textElement, final JRExporterGridCell gridCell, final int colIndex, final int rowIndex, final JRStyledText styledText, final StyleInfo baseStyle, final short forecolor) throws JRException
	{
		String formula = getFormula(textElement);
		String textStr = styledText.getText();
		
		if (formula != null)
		{	
			try
			{
				TextValue value = getTextValue(textElement, textStr);
				
				if (value instanceof NumberTextValue)
				{
					String convertedPattern = getConvertedPattern(textElement, ((NumberTextValue)value).getPattern());
					if (convertedPattern != null)
					{
						baseStyle.setDataFormat(
							dataFormat.getFormat(convertedPattern)
							);
					}
				}
				else if (value instanceof DateTextValue)
				{
					String convertedPattern = getConvertedPattern(textElement, ((DateTextValue)value).getPattern());
					if (convertedPattern != null)
					{
						baseStyle.setDataFormat(
							dataFormat.getFormat(convertedPattern)
							);
					}
				}
				
				HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(formula);
				endCreateCell(cellStyle);
				return;
			}
			catch(Exception e)//FIXMENOW what exceptions could we get here?
			{
				if(log.isWarnEnabled())
				{
					log.warn(e.getMessage());
				}
			}
		}

		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
		if (configuration.isDetectCellType())
		{
			TextValue value = getTextValue(textElement, textStr);
			value.handle(new TextValueHandler()
			{
				public void handle(StringTextValue textValue)
				{
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					if (JRCommonText.MARKUP_NONE.equals(textElement.getMarkup()))
					{
						setStringCellValue(textValue.getText());
					}
					else
					{
						setRichTextStringCellValue(styledText, forecolor, textElement, getTextLocale(textElement));
					}
					endCreateCell(cellStyle);
				}

				public void handle(NumberTextValue textValue)
				{
					String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
					if (convertedPattern != null)
					{
						baseStyle.setDataFormat(
							dataFormat.getFormat(convertedPattern)
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
					String convertedPattern = getConvertedPattern(textElement, textValue.getPattern());
					if (convertedPattern != null)
					{
						baseStyle.setDataFormat(
							dataFormat.getFormat(convertedPattern)
							);
					}
					HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
					Date date = textValue.getValue();
					if (date == null)
					{
						cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
					}
					else
					{
						date = translateDateValue(textElement, date);
						cell.setCellValue(date);
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
		else
		{
			HSSFCellStyle cellStyle = initCreateCell(gridCell, colIndex, rowIndex, baseStyle);
			if (JRCommonText.MARKUP_NONE.equals(textElement.getMarkup()))
			{
				setStringCellValue(textStr);
			}
			else
			{
				setRichTextStringCellValue(styledText, forecolor, textElement, getTextLocale(textElement));
			}
			endCreateCell(cellStyle);
		}
		
		if(!configuration.isIgnoreAnchors())
		{
			String anchorName = textElement.getAnchorName();
			if(anchorName != null)
			{
				HSSFName aName = workbook.createName();
				aName.setNameName(JRStringUtil.getJavaIdentifier(anchorName));
				aName.setSheetIndex(workbook.getSheetIndex(sheet));
				CellReference cRef = new CellReference(rowIndex, colIndex, true, true);
				aName.setRefersToFormula(cRef.formatAsString());
				anchorNames.put(anchorName, aName);
			}
		}

		setHyperlinkCell(textElement);
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
	
	protected final void setStringCellValue(String textStr)
	{
		//cell.setCellValue(JRStringUtil.replaceDosEOL(textStr));
		//cell.setCellValue(textStr);
		cell.setCellValue(new HSSFRichTextString(textStr));
	}
	
	protected final void setRichTextStringCellValue(JRStyledText styledText, short forecolor, JRFont defaultFont, Locale locale)
	{	
		if(styledText != null)
		{
			cell.setCellValue(getRichTextString(styledText, forecolor, defaultFont, locale));
		}
	}

	protected HSSFRichTextString getRichTextString(JRStyledText styledText, short forecolor, JRFont defaultFont, Locale locale)
	{
		String text = styledText.getText();
		HSSFRichTextString richTextStr = new HSSFRichTextString(text);
		int runLimit = 0;
		AttributedCharacterIterator iterator = styledText.getAttributedString().getIterator();

		while(runLimit < styledText.length() && (runLimit = iterator.getRunLimit()) <= styledText.length())
		{
			Map<Attribute,Object> attributes = iterator.getAttributes();
			JRFont runFont = attributes.isEmpty()? defaultFont : new JRBaseFont(attributes);
			short runForecolor = attributes.get(TextAttribute.FOREGROUND) != null ? 
					getWorkbookColor((Color)attributes.get(TextAttribute.FOREGROUND)).getIndex() :
					forecolor;
			HSSFFont font = getLoadedFont(runFont, runForecolor, attributes, locale);
			richTextStr.applyFont(iterator.getIndex(), runLimit, font);
			iterator.setIndex(runLimit);
		}
		return richTextStr;
	}

	protected void createMergeRegion(JRExporterGridCell gridCell, int colIndex, int rowIndex, HSSFCellStyle cellStyle)
	{
		boolean isCollapseRowSpan = getCurrentItemConfiguration().isCollapseRowSpan();
		int rowSpan = isCollapseRowSpan ? 1 : gridCell.getRowSpan();
		if (gridCell.getColSpan() > 1 || rowSpan > 1)
		{
			sheet.addMergedRegion(new CellRangeAddress(rowIndex, (rowIndex + rowSpan - 1), 
					colIndex, (colIndex + gridCell.getColSpan() - 1)));

			for(int i = 0; i < rowSpan; i++)
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

	private short getVerticalAlignment(TextAlignHolder alignment)
	{
		switch (alignment.verticalAlignment)
		{
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

	private short getRotation(TextAlignHolder alignment)
	{
		switch (alignment.rotation)
		{
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
	protected HSSFColor getWorkbookColor(Color awtColor)
	{
		byte red = (byte)awtColor.getRed();
		byte green = (byte)awtColor.getGreen();
		byte blue = (byte)awtColor.getBlue();
		HSSFColor color = null;

		if (getCurrentConfiguration().isCreateCustomPalette())
		{
			try
			{
				color = palette.findColor(red,green, blue) != null
					? palette.findColor(red,green, blue)
					: palette.addColor(red,green, blue);
			}
			catch(Exception e)
			{
				if(customColorIndex < MAX_COLOR_INDEX)
				{
					palette.setColorAtIndex(customColorIndex, red, green, blue);
					color = palette.getColor(customColorIndex++);
				}
				else
				{
					color = palette.findSimilarColor(red, green, blue);
				}
			}
		}
		
		return color == null ? getNearestColor(awtColor) : color;
	}

	/**
	 *
	 */
	protected HSSFColor getNearestColor(Color awtColor)
	{
		HSSFColor color = hssfColorsCache.get(awtColor);		
		if (color == null)
		{
			int minDiff = Integer.MAX_VALUE;
			for (Map.Entry<HSSFColor, short[]> hssfColorEntry : hssfColorsRgbs.entrySet())
			{
				HSSFColor crtColor = hssfColorEntry.getKey();
				short[] rgb = hssfColorEntry.getValue();
				
				int diff = Math.abs(rgb[0] - awtColor.getRed()) + Math.abs(rgb[1] - awtColor.getGreen()) + Math.abs(rgb[2] - awtColor.getBlue());

				if (diff < minDiff)
				{
					minDiff = diff;
					color = crtColor;
				}
			}

			hssfColorsCache.put(awtColor, color);
		}
		return color;
	}

	/**
	 *
	 */
	protected HSSFFont getLoadedFont(JRFont font, short forecolor, Map<Attribute,Object> attributes, Locale locale)
	{
		HSSFFont cellFont = null;

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
		boolean isFontSizeFixEnabled = getCurrentItemConfiguration().isFontSizeFixEnabled();
		for (int i = 0; i < loadedFonts.size(); i++)
		{
			HSSFFont cf = (HSSFFont)loadedFonts.get(i);

			short fontSize = (short)font.getFontsize();
			if (isFontSizeFixEnabled)
			{
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

			short fontSize = (short)font.getFontsize();
			if (isFontSizeFixEnabled)
			{
				fontSize -= 1;
			}
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

			cellFont.setTypeOffset(superscriptType);
			loadedFonts.add(cellFont);
		}

		return cellFont;
	}


	protected HSSFCellStyle getLoadedCellStyle(StyleInfo style)
	{
		HSSFCellStyle cellStyle = loadedCellStyles.get(style);
		if (cellStyle == null)
		{
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
			
			if (style.hasDataFormat())
			{
				cellStyle.setDataFormat(style.getDataFormat());
			}
			
			boolean isIgnoreCellBorder = Boolean.TRUE.equals(sheetInfo.ignoreCellBorder) || style.box ==null;
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
			JRExporterGridCell gridCell,
			boolean isWrapText,
			boolean isCellLocked,
			boolean isCellHidden,
			boolean isShrinkToFit
			)
	{
		return getLoadedCellStyle(
				new StyleInfo(
						mode, 
						backcolor, 
						horizontalAlignment, 
						verticalAlignment, 
						rotation, 
						font, 
						gridCell, 
						isWrapText, 
						isCellLocked, 
						isCellHidden, 
						isShrinkToFit));
	}

	protected HSSFCellStyle getLoadedCellStyle(
			short mode,
			short backcolor,
			short horizontalAlignment,
			short verticalAlignment,
			short rotation,
			HSSFFont font,
			BoxStyle box,
			boolean isWrapText,
			boolean isCellLocked,
			boolean isCellHidden,
			boolean isShrinkToFit
			)
		{
			StyleInfo style = new StyleInfo(mode, backcolor, horizontalAlignment, verticalAlignment, rotation, font, box, isWrapText, isCellLocked, isCellHidden, isShrinkToFit);
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
			switch (pen.getLineStyleValue())
			{
				case DOUBLE :
				{
					return HSSFCellStyle.BORDER_DOUBLE;
				}
				case DOTTED :
				{
					return HSSFCellStyle.BORDER_DOTTED;
				}
				case DASHED :
				{
					if (lineWidth >= 1f)
					{
						return HSSFCellStyle.BORDER_MEDIUM_DASHED;
					}

					return HSSFCellStyle.BORDER_DASHED;
				}
				case SOLID :
				default :
				{
					if (lineWidth >= 2f)
					{
						return HSSFCellStyle.BORDER_THICK;
					}
					else if (lineWidth >= 1f)
					{
						return HSSFCellStyle.BORDER_MEDIUM;
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

	public void exportImage(
		JRPrintImage element, 
		JRExporterGridCell gridCell, 
		int colIndex, 
		int rowIndex, 
		int emptyCols,
		int yCutsRow,
		JRGridLayout layout
		) throws JRException
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
				int topOffset = 0;
				int leftOffset = 0;
				int bottomOffset = 0;
				int rightOffset = 0;
				
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

						topOffset = topPadding;
						leftOffset = leftPadding;
						bottomOffset = bottomPadding;
						rightOffset = rightPadding;

						imageData = JRImageLoader.getInstance(jasperReportsContext).loadBytesFromAwtImage(bi, ImageTypeEnum.PNG);

						break;
					}
					case FILL_FRAME:
					{
						topOffset = topPadding;
						leftOffset = leftPadding;
						bottomOffset = bottomPadding;
						rightOffset = rightPadding;

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

							topOffset = topPadding + (int) (yalignFactor * (availableImageHeight - normalHeight));
							leftOffset = leftPadding + (int) (xalignFactor * (availableImageWidth - normalWidth));
							bottomOffset = bottomPadding + (int) ((1f - yalignFactor) * (availableImageHeight - normalHeight));
							rightOffset = rightPadding + (int) ((1f - xalignFactor) * (availableImageWidth - normalWidth));

							imageData = renderer.getImageData(jasperReportsContext);
						}

						break;
					}
				}

				XlsReportConfiguration configuration = getCurrentItemConfiguration();

				short mode = backgroundMode;
				short backcolor = whiteIndex;
				if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
				{
					mode = HSSFCellStyle.SOLID_FOREGROUND;
					backcolor = getWorkbookColor(gridCell.getCellBackcolor()).getIndex();
				}

				short forecolor = getWorkbookColor(element.getLineBox().getPen().getLineColor()).getIndex();

				if(element.getModeValue() == ModeEnum.OPAQUE )
				{
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
						gridCell,
						isWrapText(element),
						isCellLocked(element),
						isCellHidden(element),
						isShrinkToFit(element)
						);

				createMergeRegion(gridCell, colIndex, rowIndex, cellStyle);

				cell = row.createCell(colIndex);
				cell.setCellStyle(cellStyle);

				double topPos = getRowRelativePosition(layout, yCutsRow, topOffset);
				double leftPos = getColumnRelativePosition(layout, colIndex, leftOffset);
				double bottomPos = getRowRelativePosition(layout, yCutsRow, element.getHeight() - bottomOffset);
				double rightPos = getColumnRelativePosition(layout, colIndex, element.getWidth() - rightOffset);
				HSSFClientAnchor anchor = 
					new HSSFClientAnchor(
						(int)((leftPos - (int)leftPos) * 1023), //numbers taken from POI source code
						(int)((topPos - (int)topPos) * 255), 
						(int)((rightPos - (int)rightPos) * 1023), 
						(int)((bottomPos - (int)bottomPos) * 255), 
						(short)(colIndex + (int)leftPos), 
						(short)(rowIndex + (int)topPos), 
						//(short) (colIndex + gridCell.getColSpan()), 
						(short)(colIndex + (int)rightPos), 
						//rowIndex + (isCollapseRowSpan ? 1 : gridCell.getRowSpan())
						(short)(rowIndex + (int)bottomPos)
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
				
//				setHyperlinkCell(element);
			}
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
	
	/**
	 *
	 */
	protected double getColumnRelativePosition(JRGridLayout layout, int col, int offset)
	{
		double colRelPos = 0;
		
		int cumulativeColWidth = 0;
		int colIndex = 0;
		while(cumulativeColWidth < offset)
		{
			int colWidth = layout.getColumnWidth(col + colIndex);
			if (cumulativeColWidth + colWidth < offset)
			{
				colIndex++;
			}
			else
			{
				colRelPos += colIndex + ((offset - cumulativeColWidth) / (double) colWidth);
			}
			cumulativeColWidth += colWidth;
		}
		
		return colRelPos;
	}	
	
	/**
	 *
	 */
	protected double getRowRelativePosition(JRGridLayout layout, int row, int offset)
	{
		boolean isCollapseRowSpan = getCurrentItemConfiguration().isCollapseRowSpan();
		double rowRelPos = 0;
		
		//isCollapseRowSpan
		int cumulativeRowHeight = 0;
		int rowIndex = 0;
		while(cumulativeRowHeight < offset)
		{
			int rowHeight = isCollapseRowSpan ? layout.getMaxRowHeight(row + rowIndex) : layout.getRowHeight(row + rowIndex);
			if (cumulativeRowHeight + rowHeight < offset)
			{
				rowIndex++;
			}
			else
			{
				rowRelPos += rowIndex + ((offset - cumulativeRowHeight) / (double) rowHeight);
			}
			cumulativeRowHeight += rowHeight;
		}
		
		return rowRelPos;
	}	

	/**
	 * 
	 */
	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int x, int y)
	{
		short mode = backgroundMode;
		short backcolor = whiteIndex;
		if (frame.getModeValue() == ModeEnum.OPAQUE)
		{
			mode = HSSFCellStyle.SOLID_FOREGROUND;
			backcolor = getWorkbookColor(frame.getBackcolor()).getIndex();
		}

		short forecolor = getWorkbookColor(frame.getForecolor()).getIndex();

		HSSFCellStyle cellStyle =
			getLoadedCellStyle(
				mode,
				backcolor,
				HSSFCellStyle.ALIGN_LEFT,
				HSSFCellStyle.VERTICAL_TOP,
				(short)0,
				getLoadedFont(getDefaultFont(), forecolor, null, getLocale()),
				gridCell,
				isWrapText(frame),
				isCellLocked(frame),
				isCellHidden(frame),
				isShrinkToFit(frame)
				);

		createMergeRegion(gridCell, x, y, cellStyle);

		cell = row.createCell(x);
		cell.setCellStyle(cellStyle);
	}


	protected void exportGenericElement(JRGenericPrintElement element, JRExporterGridCell gridCell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException
	{
		GenericElementXlsHandler handler = (GenericElementXlsHandler) 
			GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
				element.getGenericType(), XLS_EXPORTER_KEY);

		if (handler != null)
		{
			handler.exportElement(exporterContext, element, gridCell, colIndex, rowIndex, emptyCols, yCutsRow, layout);
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


	private final short getSuitablePaperSize()
	{

		if (pageFormat == null)
		{
			return -1;
		}
		long width = 0;
		long height = 0;
		short ps = -1;

		if ((pageFormat.getPageWidth() != 0) && (pageFormat.getPageHeight() != 0))
		{

			double dWidth = (pageFormat.getPageWidth() / 72.0);
			double dHeight = (pageFormat.getPageHeight() / 72.0);

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
					{
						ps = HSSFPrintSetup.A4_PAPERSIZE;
					}
					else if (i == 5)
					{
						ps = HSSFPrintSetup.A5_PAPERSIZE;
					}
					break;
				}
			}
			
			//envelope sizes
			if (ps == -1)
			{
				// ISO 269 sizes - "Envelope DL" (110 � 220 mm)
				if (((width == 110) && (height == 220)) || ((width == 220) && (height == 110)))
				{
					ps = HSSFPrintSetup.ENVELOPE_DL_PAPERSIZE;
				}
			}

			// Compare to common North American Paper Sizes (ANSI X3.151-1987).
			if (ps == -1)
			{
				// ANSI X3.151-1987 - "Letter" (216 � 279 mm)
				if (((width == 216) && (height == 279)) || ((width == 279) && (height == 216)))
				{
					ps = HSSFPrintSetup.LETTER_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Legal" (216 � 356 mm)
				if (((width == 216) && (height == 356)) || ((width == 356) && (height == 216)))
				{
					ps = HSSFPrintSetup.LEGAL_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Executive" (190 � 254 mm)
				else if (((width == 190) && (height == 254)) || ((width == 254) && (height == 190)))
				{
					ps = HSSFPrintSetup.EXECUTIVE_PAPERSIZE;
				}
				// ANSI X3.151-1987 - "Ledger/Tabloid" (279 � 432 mm)
				// Not supported by POI Api yet.
				
			}
		}
		return ps;
	}
	
	protected void setHyperlinkCell(JRPrintHyperlink hyperlink)
	{
		Hyperlink link = null;

		Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK, hyperlink);
		if (ignoreHyperlink == null)
		{
			ignoreHyperlink = getCurrentItemConfiguration().isIgnoreHyperlink();
		}

		//test for ignore hyperlinks done here
		if (!ignoreHyperlink)
		{
			JRHyperlinkProducer customHandler = getHyperlinkProducer(hyperlink);
			if (customHandler == null)
			{
				switch (hyperlink.getHyperlinkTypeValue())
				{
					case REFERENCE:
					{
						String href = hyperlink.getHyperlinkReference();
						if (href != null)
						{
							link = createHelper.createHyperlink(Hyperlink.LINK_URL);
							link.setAddress(href);
						}
						break;
					}
					case LOCAL_ANCHOR :
					{
						//test for ignore anchors done here
						if(!getCurrentItemConfiguration().isIgnoreAnchors())
						{
							String href = hyperlink.getHyperlinkAnchor();
							if (href != null)
							{
								link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
								if(anchorLinks.containsKey(href))
								{
									(anchorLinks.get(href)).add(link);
								}
								else
								{
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
						if (hrefPage != null)
						{
							link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
							if(pageLinks.containsKey(sheetsBeforeCurrentReport+hrefPage))
							{
								pageLinks.get(sheetsBeforeCurrentReport + hrefPage).add(link);
							}
							else
							{
								List<Hyperlink> hrefList = new ArrayList<Hyperlink>();
								hrefList.add(link);
								pageLinks.put(sheetsBeforeCurrentReport + hrefPage, hrefList);
							}
						}
						break;
					}
					case REMOTE_ANCHOR :
					{
						String href = hyperlink.getHyperlinkReference();
						if (href != null && hyperlink.getHyperlinkAnchor() != null)
						{
							href = href + "#" + hyperlink.getHyperlinkAnchor();
							link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
							link.setAddress(href);
							
						}
						break;
						
					}
					case REMOTE_PAGE :
					{
						String href = hyperlink.getHyperlinkReference();
						if (href != null && hyperlink.getHyperlinkPage() != null)
						{
							href = href + "#JR_PAGE_ANCHOR_0_" + hyperlink.getHyperlinkPage().toString();
							link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
							link.setAddress(href);
							
						}
						break;
						
					}
					case NONE:
					default:
					{
					}
				}
			}
			else
			{
				String href = customHandler.getHyperlink(hyperlink);
				if (href != null)
				{
					link = createHelper.createHyperlink(Hyperlink.LINK_URL);
					link.setAddress(href);
				}
			}
			if(link != null)
			{
				//TODO: make tooltips functional
//				if(hyperlink.getHyperlinkTooltip() != null)
//				{
//					link.setLabel(hyperlink.getHyperlinkTooltip());
//				}
				cell.setHyperlink(link);
			}
		}
	}

	@Override
	protected Integer getMaxRowsPerSheet()
	{
		Integer maxRowsPerSheet = super.getMaxRowsPerSheet();
		return maxRowsPerSheet == null || maxRowsPerSheet == 0  || maxRowsPerSheet > 65536 ? 65536 : maxRowsPerSheet;
	}


	/**
	 *
	 */
	public String getExporterKey()
	{
		return XLS_EXPORTER_KEY;
	}
	
	
	/**
	 * 
	 */
	public String getExporterPropertiesPrefix()
	{
		return XLS_EXPORTER_PROPERTIES_PREFIX;
	}

	
	/**
	 * Creates a freeze pane for the current sheet. Freeze pane row and column indexes defined at element level override indexes defined at report level. 
	 * If multiple row freeze indexes are found in the same sheet, their maximum 
	 * value is considered. 
	 * 
	 * @param rowIndex the freeze 0-based row index
	 * @param colIndex the freeze 0-based column index
	 */
	protected void setFreezePane(int rowIndex, int colIndex)
	{
		if(rowIndex > 0 || colIndex > 0)
		{
			sheet.createFreezePane(Math.max(0, colIndex), Math.max(0, rowIndex));
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
		workbook.setSheetName(workbook.getSheetIndex(sheet) , sheetName);
	}

	
	@Override
	protected void setAutoFilter(String autoFilterRange)
	{
		sheet.setAutoFilter(CellRangeAddress.valueOf(autoFilterRange));
	}


	@Override
	protected void setRowLevels(XlsRowLevelInfo levelInfo, String level) 
	{
		Map<String, Integer> levelMap = levelInfo.getLevelMap();
		if(levelMap != null && levelMap.size() > 0)
		{
			for(String l : levelMap.keySet())
			{
				if (level == null || l.compareTo(level) >= 0)
				{
					Integer startIndex = levelMap.get(l);
					if(levelInfo.getEndIndex() >= startIndex)
					{
						sheet.groupRow(startIndex, levelInfo.getEndIndex());
					}
				}
			}
			sheet.setRowSumsBelow(false);
		}
	}
	

	/**
	 * 
	 */
	class BoxStyle
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
			borderStyle[side] = JRXlsExporter.getBorderStyle(pen);
			borderColour[side] = JRXlsExporter.this.getWorkbookColor(pen.getLineColor()).getIndex();

			hash = computeHash();
		}

		public BoxStyle(JRExporterGridCell gridCell)
		{
			if(gridCell != null)
			{
				JRLineBox lineBox = gridCell.getBox();
				if (lineBox != null)
				{
					setBox(lineBox);
				}
				JRPrintElement element = gridCell.getElement();
				if (element instanceof JRCommonGraphicElement)
				{
					setPen(((JRCommonGraphicElement)element).getLinePen());
				}
	
				hash = computeHash();
			}
		}

		public void setBox(JRLineBox box)
		{
			borderStyle[TOP] = JRXlsExporter.getBorderStyle(box.getTopPen());
			borderColour[TOP] = JRXlsExporter.this.getWorkbookColor(box.getTopPen().getLineColor()).getIndex();

			borderStyle[BOTTOM] = JRXlsExporter.getBorderStyle(box.getBottomPen());
			borderColour[BOTTOM] = JRXlsExporter.this.getWorkbookColor(box.getBottomPen().getLineColor()).getIndex();

			borderStyle[LEFT] = JRXlsExporter.getBorderStyle(box.getLeftPen());
			borderColour[LEFT] = JRXlsExporter.this.getWorkbookColor(box.getLeftPen().getLineColor()).getIndex();

			borderStyle[RIGHT] = JRXlsExporter.getBorderStyle(box.getRightPen());
			borderColour[RIGHT] = JRXlsExporter.this.getWorkbookColor(box.getRightPen().getLineColor()).getIndex();

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
				short style = JRXlsExporter.getBorderStyle(pen);
				short colour = JRXlsExporter.this.getWorkbookColor(pen.getLineColor()).getIndex();

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
	
	
	/**
	 * 
	 */
	protected class StyleInfo
	{
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
				JRExporterGridCell gridCell,
				boolean wrapText,
				boolean cellLocked,
				boolean cellHidden,
				boolean shrinkToFit
				)
		{
			this(mode, 
				backcolor, 
				horizontalAlignment, 
				verticalAlignment, 
				rotation, 
				font, 
				(gridCell == null ? null : new BoxStyle(gridCell)), 
				wrapText, 
				cellLocked, 
				cellHidden, 
				shrinkToFit);
		}
		
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
			)
		{
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
	
		protected int computeHash()
		{
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
	
		public void setDataFormat(short dataFormat)
		{
			this.lcDataFormat = dataFormat;
			hashCode = computeHash();
		}
	
		public boolean hasDataFormat()
		{
			return lcDataFormat != -1;
		}
	
		public short getDataFormat()
		{
			return lcDataFormat;
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
					&& s.rotation == rotation && s.lcWrapText == lcWrapText 
					&& s.lcCellLocked == lcCellLocked && s.lcCellHidden == lcCellHidden
					&& s.lcShrinkToFit == lcShrinkToFit;	//FIXME should dataformat be part of equals? it is part of toString()...
		}
	
		public String toString()
		{
			return "(" +
				mode + "," + backcolor + "," +
				horizontalAlignment + "," + verticalAlignment + "," +
				rotation + "," + font + "," +
				box + "," + lcDataFormat + "," + lcWrapText + "," + lcCellLocked + "," + lcCellHidden + "," + lcShrinkToFit + ")";
		}
	}

}
