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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRCommonGraphicElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
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
import net.sf.jasperreports.engine.export.data.BooleanTextValue;
import net.sf.jasperreports.engine.export.data.DateTextValue;
import net.sf.jasperreports.engine.export.data.NumberTextValue;
import net.sf.jasperreports.engine.export.data.StringTextValue;
import net.sf.jasperreports.engine.export.data.TextValue;
import net.sf.jasperreports.engine.export.data.TextValueHandler;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
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
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.JxlExporterConfiguration;
import net.sf.jasperreports.export.JxlReportConfiguration;
import net.sf.jasperreports.export.XlsExporterConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jxl.CellView;
import jxl.JXLException;
import jxl.Range;
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
 * @deprecated To be removed. Use {@link JRXlsExporter} or {@link JRXlsxExporter} instead.
 * @author Manuel Paul (mpaul@ratundtat.com)
 */
public class JExcelApiExporter extends JRXlsAbstractExporter<JxlReportConfiguration, JxlExporterConfiguration, JExcelApiExporterContext>
{

	private static final Log log = LogFactory.getLog(JExcelApiExporter.class);

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
	 * {@link GenericElementHandlerEnviroment#getHandler(net.sf.jasperreports.engine.JRGenericElementType, String)}.
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
	
	protected Map<String,List<JExcelApiLocalHyperlinkInfo>> anchorLinks = new HashMap<String,List<JExcelApiLocalHyperlinkInfo>>();
	protected Map<Integer,List<JExcelApiLocalHyperlinkInfo>> pageLinks = new HashMap<Integer,List<JExcelApiLocalHyperlinkInfo>>();
	
	protected class ExporterContext extends BaseExporterContext implements JExcelApiExporterContext
	{
	}


	/**
	 * @see #JExcelApiExporter(JasperReportsContext)
	 */
	public JExcelApiExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JExcelApiExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);

		exporterContext = new ExporterContext();
		maxColumnIndex = 255;
	}

	
	/**
	 *
	 */
	protected Class<JxlExporterConfiguration> getConfigurationInterface()
	{
		return JxlExporterConfiguration.class;
	}

	
	/**
	 *
	 */
	protected Class<JxlReportConfiguration> getItemConfigurationInterface()
	{
		return JxlReportConfiguration.class;
	}
	

	@Override
	protected void initExport()
	{
		super.initExport();

		XlsExporterConfiguration configuration = getCurrentConfiguration();
		
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

		XlsReportConfiguration configuration = getCurrentItemConfiguration();
		
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
		JxlExporterConfiguration configuration = getCurrentConfiguration();
		
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
			anchorLinks.clear();
			pageLinks.clear();
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

	protected void createSheet(CutsInfo xCuts, SheetInfo sheetInfo)
	{
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

		Range[] range = null;
		List<JExcelApiLocalHyperlinkInfo> hyperlinkInfoList = null;
		
		
		for(String href : anchorLinks.keySet()){	// the anchorLinks map contains no entries for reports with ignore anchors == true;
			range = workbook.findByName(href);
			hyperlinkInfoList = anchorLinks.get(href);
			if(range != null && hyperlinkInfoList != null){
				for(JExcelApiLocalHyperlinkInfo hyperlinkInfo : hyperlinkInfoList){
					WritableSheet anchorSheet = workbook.getSheet(range[0].getFirstSheetIndex());
					WritableHyperlink hyperlink = new WritableHyperlink(
							hyperlinkInfo.getCol(),
							hyperlinkInfo.getRow(),
							hyperlinkInfo.getLastCol(),
							hyperlinkInfo.getLastRow(),
							hyperlinkInfo.getDescription(),
							anchorSheet,
							range[0].getTopLeft().getColumn(),
							range[0].getTopLeft().getRow(),
							range[0].getBottomRight().getColumn(),
							range[0].getBottomRight().getRow());
					try {
						hyperlinkInfo.getSheet().addHyperlink(hyperlink);
					} catch (Exception e) {
						throw new JRException(e);
					} 
				}
			}
		}
		
		int index = 0;
		for(Integer linkPage : pageLinks.keySet()){		// the pageLinks map contains no entries for reports with ignore hyperlinks == true;
			hyperlinkInfoList = pageLinks.get(linkPage);
			if(hyperlinkInfoList != null && !hyperlinkInfoList.isEmpty()){
				WritableSheet anchorSheet = null;
				for(JExcelApiLocalHyperlinkInfo hyperlinkInfo : hyperlinkInfoList){
					index = onePagePerSheetMap.get(linkPage-1)!= null 
							? (onePagePerSheetMap.get(linkPage-1)
								? Math.max(0, linkPage - 1)
								: Math.max(0, sheetsBeforeCurrentReportMap.get(linkPage)))
							: 0;
					anchorSheet = workbook.getSheet(index);
					WritableHyperlink hyperlink = new WritableHyperlink(
							hyperlinkInfo.getCol(),
							hyperlinkInfo.getRow(),
							hyperlinkInfo.getLastCol(),
							hyperlinkInfo.getLastRow(),
							hyperlinkInfo.getDescription(),
							anchorSheet,
							0,
							0,
							0,
							0);
					try {
						hyperlinkInfo.getSheet().addHyperlink(hyperlink);
					} catch (Exception e) {
						throw new JRException(e);
					} 
				}
			}
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
		boolean isAutoFit = yCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW) 
				&& (Boolean)yCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_ROW);
		if (!isAutoFit)
		{
			try
			{
				sheet.setRowView(rowIndex, LengthUtil.twip(lastRowHeight));
			}
			catch (RowsExceededException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
						new Object[]{jasperPrint.getName()}, 
						e);
			}
		}
	}
	
	protected void addRowBreak(int rowIndex)
	{
		sheet.addRowPageBreak(rowIndex);
	}

//	protected void setCell(JRExporterGridCell gridCell, int x, int y)
//	{
//	}

	protected void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException
	{
		Colour forecolor = BLACK;
		if (gridCell.getForecolor() != null)
		{
			forecolor = getWorkbookColour(gridCell.getForecolor());
		}

		Pattern mode = backgroundMode;
		Colour backcolor = WHITE;
		
		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor(), true);
		}

		WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue(), getLocale());
		WritableCellFormat cellStyle = 
			getLoadedCellStyle(
				mode, 
				backcolor,
				cellFont, 
				gridCell,
				true,
				false,
				false
				);

		try
		{
			sheet.addCell(new Blank(colIndex, rowIndex, cellStyle));
		}
		catch (RowsExceededException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_REPORT_GENERATION_ERROR,
					new Object[]{jasperPrint.getName()}, 
					e);	//FIXMENOW raise same exception everywhere
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
	
	protected void addOccupiedCell(OccupiedGridCell occupiedGridCell, int colIndex, int rowIndex) throws JRException
	{
	}

	protected void exportLine(JRPrintLine line, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour forecolor2 = getWorkbookColour(line.getLinePen().getLineColor());
		WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor2.getValue(), getLocale());
		
		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor(), true);
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
		Blank cell2 = new Blank(col, row, cellStyle2);

		try
		{
			sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
					null,
					e);
		}
	}

	protected void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour backcolor = WHITE;
		Pattern mode = this.backgroundMode;

		if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(gridCell.getCellBackcolor(), true);
		}

		Colour forecolor = getWorkbookColour(element.getLinePen().getLineColor());
		WritableFont cellFont2 = getLoadedFont(getDefaultFont(), forecolor.getValue(), getLocale());
		WritableCellFormat cellStyle2 = 
			getLoadedCellStyle(
				mode, 
				backcolor, 
				cellFont2, 
				gridCell,
				isWrapText(element),
				isCellLocked(element),
				isShrinkToFit(element)
				);
		
		Blank cell2 = new Blank(col, row, cellStyle2);

		try
		{
			sheet.addCell(cell2);
		}
		catch (Exception e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
					null,
					e);
		}
	}

	public void exportText(JRPrintText text, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		JRStyledText styledText = getStyledText(text);

		if (styledText != null)
		{
			Colour forecolor = getWorkbookColour(text.getForecolor());
			WritableFont cellFont = this.getLoadedFont(text, forecolor.getValue(), getTextLocale(text));

			TextAlignHolder alignment = getTextAlignHolder(text);
			int horizontalAlignment = getHorizontalAlignment(alignment);
			int verticalAlignment = getVerticalAlignment(alignment);
			int rotation = getRotation(alignment);

			Pattern mode = this.backgroundMode;
			Colour backcolor = WHITE;

			JxlReportConfiguration configuration = getCurrentItemConfiguration();
			
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
			{
				mode = Pattern.SOLID;
				backcolor = getWorkbookColour(gridCell.getCellBackcolor(), true);
			}

			StyleInfo baseStyle = isIgnoreTextFormatting(text) 
					? new StyleInfo(
							mode,
							WHITE,
							horizontalAlignment,
							verticalAlignment,
							(short)0,
							null,
							(JRExporterGridCell)null, 
							isWrapText(text) || Boolean.TRUE.equals(((JRXlsExporterNature)nature).getColumnAutoFit(text)),
							isCellLocked(text),
							isShrinkToFit(text)
							)
					: new StyleInfo(
							mode, 
							backcolor,
							horizontalAlignment, 
							verticalAlignment,
							rotation, 
							cellFont,
							gridCell,
							isWrapText(text) || Boolean.TRUE.equals(((JExcelApiExporterNature)nature).getColumnAutoFit(text)),
							isCellLocked(text),
							isShrinkToFit(text)
							);

			if (!configuration.isIgnoreAnchors() && text.getAnchorName() != null)
			{
				int lastCol = Math.max(0, col + gridCell.getColSpan() - 1);
				int lastRow = Math.max(0, row + gridCell.getRowSpan() - 1);
				workbook.addNameArea(text.getAnchorName(), sheet, col, row, lastCol, lastRow);
			}

			Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK, text);
			if (ignoreHyperlink == null)
			{
				ignoreHyperlink = configuration.isIgnoreHyperlink();
			}
			
			//test for ignore hyperlink done here
			if(!ignoreHyperlink)
			{
				exportHyperlink(text, styledText.getText(), gridCell, col, row);
			}

			try
			{
				addCell(col, row, text, styledText.getText(), baseStyle);
			}
			catch (Exception e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
						null,
						e);
			}
		}
	}
	

	public void exportHyperlink(JRPrintHyperlink link, String description, JRExporterGridCell gridCell, int col, int row) throws JRException {
		JRHyperlinkProducer customHandler = getHyperlinkProducer(link);
		if (customHandler == null)
		{
			switch (link.getHyperlinkTypeValue())
			{
				case REFERENCE:
				{
					exportHyperlink (link.getHyperlinkReference(), col, row, col, row);
					break;
				}
				case LOCAL_ANCHOR :
				{
					// test for ignore anchor done here
					if(!getCurrentItemConfiguration().isIgnoreAnchors()) {
						String href = link.getHyperlinkAnchor();
						if(href != null){
							int lastCol = Math.max(0, col + gridCell.getColSpan() - 1);
							int lastRow = Math.max(0, row + gridCell.getRowSpan() - 1); 
							JExcelApiLocalHyperlinkInfo hyperlinkInfo = new JExcelApiLocalHyperlinkInfo(description, sheet, col, row, lastCol, lastRow);
							if(anchorLinks.containsKey(href)){
								anchorLinks.get(href).add(hyperlinkInfo);
							}else {
								List<JExcelApiLocalHyperlinkInfo> hyperlinkInfoList = new ArrayList<JExcelApiLocalHyperlinkInfo>();
								hyperlinkInfoList.add(hyperlinkInfo);
								anchorLinks.put(href, hyperlinkInfoList);
							}
						}
					}
					break;
				}
				case LOCAL_PAGE :
				{
					// test for ignore anchor done here
					if(!getCurrentItemConfiguration().isIgnoreAnchors()) {
						Integer href = getCurrentItemConfiguration().isOnePagePerSheet() ? link.getHyperlinkPage() : 0;
						if(href != null){
							int lastCol = Math.max(0, col + gridCell.getColSpan() - 1);
							int lastRow = Math.max(0, row + gridCell.getRowSpan() - 1);
							JExcelApiLocalHyperlinkInfo hyperlinkInfo = new JExcelApiLocalHyperlinkInfo(description, sheet, col, row, lastCol, lastRow);
							if(pageLinks.containsKey(sheetsBeforeCurrentReport+href)){
								pageLinks.get(sheetsBeforeCurrentReport+href).add(hyperlinkInfo);
							}else {
								List<JExcelApiLocalHyperlinkInfo> hyperlinkInfoList = new ArrayList<JExcelApiLocalHyperlinkInfo>();
								hyperlinkInfoList.add(hyperlinkInfo);
								pageLinks.put(sheetsBeforeCurrentReport+href, hyperlinkInfoList);
							}
						}
					}
					break;
				}
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
			exportHyperlink (customHandler.getHyperlink(link), col, row, col, row);
		}
	}

	
	protected void exportHyperlink (String href, int col, int row, int lastCol, int lastRow) throws JRException{
		if (href != null)
		{
			try
			{
				URL url = new URL(href);
				WritableHyperlink hyperlink = new WritableHyperlink(col, row, lastCol, lastRow, url);
				sheet.addHyperlink(hyperlink);
			}
			catch (MalformedURLException e)
			{
				if (log.isWarnEnabled())
				{
					log.warn("Reference \"" + href + "\" could not be parsed as URL.", e);
				}
			} catch (Exception e) {
				throw new JRException(e);
			} 
		}
	}

	protected void addCell(int x, int y, JRPrintText text, String textStr, StyleInfo baseStyle) throws WriteException, RowsExceededException, JRException
	{
		CellValue cellValue = null;

		TextValue textValue = null;

		String textFormula = getFormula(text);
		if( textFormula != null)
		{
			// if the cell has formula, we try create a formula cell
			textValue = getTextValue(text, textStr);
			cellValue = getFormulaCellValue(x, y, text, textValue, textFormula, baseStyle, isComplexFormat(text));
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
					textValue = getTextValue(text, textStr);
				}
				cellValue = getDetectedCellValue(x, y, text, textValue, baseStyle, isComplexFormat(text));
			}
			else
			{
				cellValue = getLabelCell(x, y, textStr, baseStyle);
			}
		}

		sheet.addCell(cellValue);
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

//		public FormulaTextValueHandler(int x, int y, String formula, StyleInfo baseStyle)
//		{
//			this(x, y, formula, baseStyle, false);
//		}

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

//		public CellTextValueHandler(int x, int y, StyleInfo baseStyle)
//		{
//			this(x, y, baseStyle, false);
//		}

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
		WritableCellFormat cellStyle = getLoadedCellStyle(baseStyle);
		return new Label(x, y, textStr, cellStyle);
	}

	protected void addMergeRegion(JRExporterGridCell gridCell, int x, int y) throws JRException
	{
		boolean isCollapseRowSpan = getCurrentItemConfiguration().isCollapseRowSpan();
		
		if (
			gridCell.getColSpan() > 1 
			|| (gridCell.getRowSpan() > 1 && !isCollapseRowSpan)
			)
		{
			try
			{
				if (isCollapseRowSpan)
				{
					sheet.mergeCells(x, y, (x + gridCell.getColSpan() - 1), y);
				}
				else
				{
					sheet.mergeCells(x, y, (x + gridCell.getColSpan() - 1), (y + gridCell.getRowSpan() - 1));
				}
			}
			catch (JXLException e)
			{
				throw 
					new JRException(
						EXCEPTION_MESSAGE_KEY_CANNOT_MERGE_CELLS,
						null,
						e);
			}
		}
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

	public void exportImage(
		JRPrintImage element, 
		JRExporterGridCell gridCell, 
		int col, 
		int row, 
		int emptyCols,
		int yCutsRow,
		JRGridLayout layout
		) throws JRException
	{
		addMergeRegion(gridCell, col, row);

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

			Pattern mode = this.backgroundMode;
			Colour background = WHITE;

			JxlReportConfiguration configuration = getCurrentItemConfiguration();
			
			if (!Boolean.TRUE.equals(sheetInfo.ignoreCellBackground) && gridCell.getCellBackcolor() != null)
			{
				mode = Pattern.SOLID;
				background = getWorkbookColour(gridCell.getCellBackcolor(), true);
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
					gridCell,
					isWrapText(element),
					isCellLocked(element),
					isShrinkToFit(element)
					);

			if (!configuration.isIgnoreAnchors() && element.getAnchorName() != null)
			{
				int lastCol = Math.max(0, col + gridCell.getColSpan() - 1);
				int lastRow = Math.max(0, row + gridCell.getRowSpan() - 1);
				workbook.addNameArea(element.getAnchorName(), sheet, col, row, lastCol, lastRow);
			}
			
			Boolean ignoreHyperlink = HyperlinkUtil.getIgnoreHyperlink(XlsReportConfiguration.PROPERTY_IGNORE_HYPERLINK, element);
			if (ignoreHyperlink == null)
			{
				ignoreHyperlink = configuration.isIgnoreHyperlink();
			}
			if(!ignoreHyperlink)
			{
				exportHyperlink(element, "", gridCell, col, row);
			}

			try
			{
				sheet.addCell(new Blank(col, row, cellStyle2));
				double leftPos = getColumnRelativePosition(layout, col, leftOffset);
				double topPos = getRowRelativePosition(layout, yCutsRow, topOffset);
				double rightPos = getColumnRelativePosition(layout, col, element.getWidth() - rightOffset);
				double bottomPos = getRowRelativePosition(layout, yCutsRow, element.getHeight() - bottomOffset);
				WritableImage image =
					new WritableImage(
						col + leftPos,
						row + topPos,
						rightPos - leftPos,
						bottomPos - topPos,
						imageData
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


	/*private static Colour getNearestColour(Color awtColor) {
		Colour retVal = null;
		Colour[] colors = Colour.getAllColours();

		int diff = 50;

		if (colors != null && colors.length > 0 ){
			Colour crtColor = null;
			for (int i = 0; i < colors.length; i++) {
				crtColor = colors[i];

				int red = crtColor.getDefaultRGB().getRed();
				if (Math.abs(awtColor.getRed() - red) < diff) {
					int green = crtColor.getDefaultRGB().getGreen();
					if (Math.abs(awtColor.getGreen() - green) < diff) {
						int blue = crtColor.getDefaultRGB().getBlue();
						if (Math.abs(awtColor.getBlue() - blue) < diff) {
							retVal = crtColor;
						}
					}
				}
			}
		}

		return retVal;
	}*/

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
				JRExporterGridCell gridCell,
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
					new BoxStyle(gridCell),
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
				box + "," + displayFormat + "," + isWrapText + "," + isCellLocked  + "," + isShrinkToFit + ")";
		}
	}

	private WritableCellFormat getLoadedCellStyle(
			Pattern mode, 
			Colour backcolor, 
			WritableFont font, 
			JRExporterGridCell gridCell,
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
						gridCell,
						wrapText,
						cellLocked, 
						shrinkToFit
						);
		return getLoadedCellStyle(styleKey);
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
				cellStyle.setShrinkToFit(styleKey.isShrinkToFit);
				
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
		
		onePagePerSheetMap.put(sheetIndex, configuration.isOnePagePerSheet());
		sheetsBeforeCurrentReportMap.put(sheetIndex, sheetsBeforeCurrentReport);
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
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.TOP;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case LEFT :
					case JUSTIFIED :
					default :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
					}
				}

				switch (textElement.getVerticalTextAlign())
				{
					case BOTTOM :
					{
						horizontalAlignment = HorizontalTextAlignEnum.RIGHT;
						break;
					}
					case MIDDLE :
					{
						horizontalAlignment = HorizontalTextAlignEnum.CENTER;
						break;
					}
					case TOP :
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
					case RIGHT :
					{
						verticalAlignment = VerticalTextAlignEnum.BOTTOM;
						break;
					}
					case CENTER :
					{
						verticalAlignment = VerticalTextAlignEnum.MIDDLE;
						break;
					}
					case LEFT :
					case JUSTIFIED :
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

	protected void exportFrame(JRPrintFrame frame, JRExporterGridCell gridCell, int col, int row) throws JRException
	{
		addMergeRegion(gridCell, col, row);

		Colour forecolor = getWorkbookColour(frame.getForecolor());
		Colour backcolor = WHITE;
		Pattern mode = backgroundMode;

		if (frame.getModeValue() == ModeEnum.OPAQUE)
		{
			mode = Pattern.SOLID;
			backcolor = getWorkbookColour(frame.getBackcolor(), true);
		}

		WritableFont cellFont = getLoadedFont(getDefaultFont(), forecolor.getValue(), getLocale());
		WritableCellFormat cellStyle = 
			getLoadedCellStyle(
				mode, 
				backcolor,
				cellFont, 
				gridCell,
				true,
				isCellLocked(frame),
				isShrinkToFit(frame)
				);

		Blank cell = new Blank(col, row, cellStyle);
		try
		{
			sheet.addCell(cell);
		}
		catch (JXLException e)
		{
			throw 
				new JRException(
					EXCEPTION_MESSAGE_KEY_CANNOT_ADD_CELL, 
					null,
					e);
		}
	}


	protected void exportGenericElement(JRGenericPrintElement element, JRExporterGridCell gridCell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException
	{
		GenericElementJExcelApiHandler handler = (GenericElementJExcelApiHandler) 
		GenericElementHandlerEnviroment.getInstance(getJasperReportsContext()).getElementHandler(
				element.getGenericType(), JXL_EXPORTER_KEY);

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
		return JXL_EXPORTER_KEY;
	}

	/**
	 * 
	 */
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
			&& element.getPropertiesMap().containsProperty(JxlReportConfiguration.PROPERTY_COMPLEX_FORMAT)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, JxlReportConfiguration.PROPERTY_COMPLEX_FORMAT, getCurrentItemConfiguration().isComplexFormat());
		}
		return getCurrentItemConfiguration().isComplexFormat();
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
	protected void setRowLevels(XlsRowLevelInfo levelInfo, String level) 
	{
		Map<String, Integer> levelMap = levelInfo.getLevelMap();
		try 
		{
			if(levelMap != null && levelMap.size() > 0)
			{
				for(String l : levelMap.keySet())
				{
					if (level == null || l.compareTo(level) >= 0)
					{
						Integer startIndex = levelMap.get(l);
						if(levelInfo.getEndIndex() >= startIndex)
						{
							sheet.setRowGroup(startIndex, levelInfo.getEndIndex(), false);
						}
					}
				}
			}
		}
		catch (RowsExceededException e) 
		{
			throw new JRRuntimeException(e);
		}
		catch (WriteException e) 
		{
			throw new JRRuntimeException(e);
		}
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
}

