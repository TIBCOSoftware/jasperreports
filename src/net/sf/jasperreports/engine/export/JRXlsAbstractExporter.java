/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
 * Greg Hilton
 */

package net.sf.jasperreports.engine.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import net.sf.jasperreports.charts.type.EdgeEnum;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintEllipse;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRStringUtil;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.XlsExporterConfiguration;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXlsAbstractExporter<C extends XlsExporterConfiguration, E extends JRExporterContext>  extends JRAbstractExporter<C, OutputStreamExporterOutput, E>
{

	public static final String XLS_EXPORTER_PROPERTIES_PREFIX = JRPropertiesUtil.PROPERTY_PREFIX + "export.xls.";

	/**
	 * Property that stores the formula which has to be applied to a given cell in an excel sheet.
	 */
	public static final String PROPERTY_CELL_FORMULA = XLS_EXPORTER_PROPERTIES_PREFIX + "formula";

	/**
	 * Property that stores the pattern which has to be applied to a given cell in an excel sheet.
	 */
	public static final String PROPERTY_CELL_PATTERN = XLS_EXPORTER_PROPERTIES_PREFIX + "pattern";

	/**
	 * Property used to set the name of the sheet containing a given element. Its value overrides the report-level settings for the sheet name. 
	 * If several elements in the sheet contain this property, the engine will consider the value of the last exported element's property.
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_SHEET_NAME = XLS_EXPORTER_PROPERTIES_PREFIX + "export.xls.sheet.name";

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_WRAP_TEXT}.
	 */
	public static final String PROPERTY_WRAP_TEXT = XlsExporterConfiguration.PROPERTY_WRAP_TEXT;


	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_FIT_WIDTH}.
	 */
	public static final String PROPERTY_FIT_WIDTH = XlsExporterConfiguration.PROPERTY_FIT_WIDTH;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_FIT_HEIGHT}.
	 */
	public static final String PROPERTY_FIT_HEIGHT = XlsExporterConfiguration.PROPERTY_FIT_HEIGHT;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_CELL_LOCKED}.
	 */
	public static final String PROPERTY_CELL_LOCKED = XlsExporterConfiguration.PROPERTY_CELL_LOCKED;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_CELL_HIDDEN}.
	 */
	public static final String PROPERTY_CELL_HIDDEN = XlsExporterConfiguration.PROPERTY_CELL_HIDDEN;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_HEADER_LEFT}.
	 */
	public static final String PROPERTY_SHEET_HEADER_LEFT = XlsExporterConfiguration.PROPERTY_SHEET_HEADER_LEFT;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_HEADER_CENTER}.
	 */
	public static final String PROPERTY_SHEET_HEADER_CENTER = XlsExporterConfiguration.PROPERTY_SHEET_HEADER_CENTER;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_HEADER_RIGHT}.
	 */
	public static final String PROPERTY_SHEET_HEADER_RIGHT = XlsExporterConfiguration.PROPERTY_SHEET_HEADER_RIGHT;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_FOOTER_LEFT}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_LEFT = XlsExporterConfiguration.PROPERTY_SHEET_FOOTER_LEFT;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_FOOTER_CENTER}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_CENTER = XlsExporterConfiguration.PROPERTY_SHEET_FOOTER_CENTER;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_FOOTER_RIGHT}.
	 */
	public static final String PROPERTY_SHEET_FOOTER_RIGHT = XlsExporterConfiguration.PROPERTY_SHEET_FOOTER_RIGHT;

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHEET_DIRECTION}.
	 */
	public static final String PROPERTY_SHEET_DIRECTION = XlsExporterConfiguration.PROPERTY_SHEET_DIRECTION;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_FREEZE_ROW}.
	 */
	public static final String PROPERTY_FREEZE_ROW = XlsExporterConfiguration.PROPERTY_FREEZE_ROW;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_FREEZE_COLUMN}.
	 */
	public static final String PROPERTY_FREEZE_COLUMN = XlsExporterConfiguration.PROPERTY_FREEZE_COLUMN;
	
	/**
	 * This property indicates the horizontal edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_ROW value.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Top</code> - The current row is the first unlocked row in the sheet. All rows above are 'frozen'.</li>
	 * <li><code>Bottom</code> - The current row is the last 'frozen' row in the sheet. All rows below are unlocked.</li>
	 * </ul>
	 */
	public static final String PROPERTY_FREEZE_ROW_EDGE = XLS_EXPORTER_PROPERTIES_PREFIX + "freeze.row.edge";
	
	/**
	 * This property indicates the vertical edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_COLUMN and PROPERTY_FREEZE_SHEET_COLUMNS values.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Left</code> - The current column is the first unlocked column in the sheet. All columns to the left are 'frozen'.</li>
	 * <li><code>Right</code> - The current column is the last 'frozen' column in the sheet. All columns to the right are unlocked.</li>
	 * </ul>
	 */
	public static final String PROPERTY_FREEZE_COLUMN_EDGE = XLS_EXPORTER_PROPERTIES_PREFIX + "freeze.column.edge";
	
	/**
	 * Flag property that indicates whether Excel should autofit the current row height.
	 * Allowed values are:
	 * <ul>
	 * <li><code>true</code></li>
	 * <li><code>false</code> - this is the default value.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	public static final String PROPERTY_AUTO_FIT_ROW = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.fit.row";
	
	/**
	 * Flag property that indicates whether Excel should autofit the current column width.
	 * Allowed values are:
	 * <ul>
	 * <li><code>true</code></li>
	 * <li><code>false</code> - this is the default value.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 * @since 4.5.1
	 */
	public static final String PROPERTY_AUTO_FIT_COLUMN = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.fit.column";
	
	/**
	 * This element-level property is used to indicate the boundaries of the autofilter data range in the current sheet. 
	 * Allowed values are:
	 * <ul>
	 * <li><code>Start</code> - The current cell will be marked as autofilter heading cell, and column data below/to the right of 
	 * this cell can be considered as part of the autofilter data range. The starting point of the autofilter data range will be 
	 * the next cell below the current cell (ie if the current cell reference is B4, the autofilter range will start with the "B5" 
	 * cell reference: "B5:M20").
	 * <br/>
	 * If multiple autofilter <code>Start</code> values are found in the same sheet, only the last one will be considered. If the 
	 * <code>Start</code> value is present but no <code>End</code> value is found in the sheet, then only the current column will 
	 * be considered for the data range.
	 * <br/>
	 * If the autofilter <code>Start</code> value is set on the same row as the autofilter <code>End</code> value, the data range 
	 * will include all data below this heading row, placed between the start column and the end column.</li>
	 * <li><code>End</code> - The current cell will be marked as autofilter ending cell, and column data in this cell and 
	 * above/to the left can be considered as part of the autofilter data range. The ending cell in the data range is the 
	 * current cell reference (ie if the current cell reference is M20, the autofilter range will end in "M20": "B5:M20"). 
	 * The heading cell for the current column will be placed in the same column on the row containing the autofilter <code>Start</code> value. 
	 * <br/>
	 * <b>Caution:</b> If no autofilter <code>Start</code> value is found in the sheet, the autofilter <code>End</code> value will be considered 
	 * as <code>Start</code> value instead. 
	 * <br/>
	 * If multiple autofilter <code>End</code> value are found in the same sheet, only the last one will be considered. 
	 * <br/>
	 * If the autofilter <code>Start</code> value is set on the same row as the autofilter <code>End</code> value, the data range 
	 * will include all data below this heading row, placed between the start column and the end column.</li>
	 * </ul>
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_AUTO_FILTER = XLS_EXPORTER_PROPERTIES_PREFIX + "auto.filter";
	
	/**
	 * Element-level property used to adjust the column width to values suitable for Excel output, taking into account 
	 * that column widths are measured in Excel in Normal style default character width units. The pixel-to-character width 
	 * translation depends on the default normal style character width, so it cannot be always accurately fitted. In this case, 
	 * one can adjust the current column width by setting this property with an integer value measured in pixels. The JR engine 
	 * will perform the pixel-to-character width mapping using this value instead of the element's <code>width</code> attribute.
	 * <br/>
	 * If defined, this property will override the {@link #PROPERTY_COLUMN_WIDTH_RATIO PROPERTY_COLUMN_WIDTH_RATIO} value for the current column
	 * 
	 * @see #PROPERTY_COLUMN_WIDTH_RATIO
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_COLUMN_WIDTH = XLS_EXPORTER_PROPERTIES_PREFIX + "column.width";

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_COLUMN_WIDTH_RATIO}.
	 */
	public static final String PROPERTY_COLUMN_WIDTH_RATIO = XlsExporterConfiguration.PROPERTY_COLUMN_WIDTH_RATIO;
	
	/**
	 * Property prefix used to indicate the current outline row level, and when necessary, the ending row of the current outline row 
	 * group with the given level. The suffix 
	 * of these properties is associated with the outline level, while the property value indicates if the current row group should 
	 * continue or should end. The most recommended practice is to use the outline level itself as property suffix, although this is not 
	 * mandatory. The suffix may take any other string value, but one has to keep in mind that suffixes are used as sorted row level 
	 * descriptors. For instance, because "aaa" &lt; "bbb", the outline level associated with the "aaa" suffix will be smaller than 
	 * the level associated with the "bbb" suffix. The most intuitive representation of the row levels uses the row level as property suffix.
	 * <br/>
	 * In such a case, The <code>net.sf.jasperreports.export.xls.outline.level.2</code> property means that its value is correlated with 
	 * the outline level 2, so the current row belongs to a level 2 row group. Based on Office Open XML specs, allowed values for outline 
	 * levels are positive integers from 1 to 7.
	 * <br/>
	 * The value of this property could be any expression (including <code>null</code>). When such a property occurrence is met, the suffix 
	 * indicates the outline level for that row. If multiple properties with the same prefix are defined for the same row, the deepest 
	 * outline level is considered for that row. To end an outline row group one has to set the related outline level property with the 
	 * <code>End</code> value. This is a special property value instructing the JR engine that the current row group of that level ends 
	 * on the current row. 
	 * 
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_ROW_OUTLINE_LEVEL_PREFIX = XLS_EXPORTER_PROPERTIES_PREFIX + "row.outline.level.";
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_USE_TIMEZONE}.
	 */
	public static final String PROPERTY_USE_TIMEZONE = XlsExporterConfiguration.PROPERTY_USE_TIMEZONE;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_WORKBOOK_TEMPLATE}.
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE = XlsExporterConfiguration.PROPERTY_WORKBOOK_TEMPLATE;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS}.
	 */
	public static final String PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS = XlsExporterConfiguration.PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS;

	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_IGNORE_ANCHORS}.
	 */
	public static final String PROPERTY_IGNORE_ANCHORS = XlsExporterConfiguration.PROPERTY_IGNORE_ANCHORS;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_PAGE_SCALE}.
	 */
	public static final String PROPERTY_PAGE_SCALE = XlsExporterConfiguration.PROPERTY_PAGE_SCALE;
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_FIRST_PAGE_NUMBER}.
	 */
	public static final String PROPERTY_FIRST_PAGE_NUMBER = XlsExporterConfiguration.PROPERTY_FIRST_PAGE_NUMBER;
	

	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_SHOW_GRIDLINES}.
	 */
	public static final String PROPERTY_SHOW_GRIDLINES = XlsExporterConfiguration.PROPERTY_SHOW_GRIDLINES;
	
	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_IMAGE_ANCHOR_TYPE}.
	 */
	public static final String PROPERTY_IMAGE_ANCHOR_TYPE = XlsExporterConfiguration.PROPERTY_IMAGE_ANCHOR_TYPE;

	
	/**
	 * @deprecated Replaced by {@link XlsExporterConfiguration#PROPERTY_IGNORE_HYPERLINK}.
	 */
	public static final String PROPERTY_IGNORE_HYPERLINK = XlsExporterConfiguration.PROPERTY_IGNORE_HYPERLINK;
	

	protected static class TextAlignHolder
	{
		public final HorizontalAlignEnum horizontalAlignment;
		public final VerticalAlignEnum verticalAlignment;
		public final RotationEnum rotation;

		public TextAlignHolder(HorizontalAlignEnum horizontalAlignment, VerticalAlignEnum verticalAlignment, RotationEnum rotation)
		{
			this.horizontalAlignment = horizontalAlignment;
			this.verticalAlignment = verticalAlignment;
			this.rotation = rotation;
		}
	}

	/**
	 *
	 */
	protected List<Object> loadedFonts = new ArrayList<Object>();

	/**
	 *
	 */
	protected ExporterNature nature;

	/**
	 *
	 */
	protected String[] sheetNames;
	
	protected int reportIndex;
	protected int pageIndex;

	/**
	 *
	 */
	protected JRFont defaultFont;

	/**
	 * Used for counting the total number of sheets.
	 */
	protected int sheetIndex;

	/**
	 * Used for iterating through sheet names.
	 */
	protected int sheetNamesIndex;

	/**
	 * Used when indexing the identical sheet name. Contains sheet names as keys and the number of 
	 * occurrences of each sheet name as values.
	 */
	protected Map<String,Integer> sheetNamesMap;

	protected int gridRowFreezeIndex;
	protected int gridColumnFreezeIndex;
	
	protected int maxRowFreezeIndex;
	protected int maxColumnFreezeIndex;
	
	protected boolean isFreezeRowEdge;
	protected boolean isFreezeColumnEdge;
	
	protected String autoFilterStart;		
	protected String autoFilterEnd;		

	protected Float columnWidthRatio;
	protected Integer documentFirstPageNumber;		
	protected boolean firstPageNotSet;
	
	protected Boolean keepTemplateSheets;
	protected String workbookTemplate;
	
	protected boolean ignoreAnchors;
	protected Boolean documentShowGridlines;
	
	protected String invalidCharReplacement;
	
	protected static class SheetInfo
	{
		public String sheetName;
		public Integer sheetFirstPageNumber;		
		public Integer sheetPageScale;		
		public Boolean sheetShowGridlines;
	}

	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		return defaultFont;
	}


	/**
	 * @see #JRXlsAbstractExporter(JasperReportsContext)
	 */
	public JRXlsAbstractExporter()
	{
		this(DefaultJasperReportsContext.getInstance());
	}

	
	/**
	 *
	 */
	public JRXlsAbstractExporter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}
	

	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		/*   */
		ensureJasperReportsContext();
		ensureInput();

		initExport();

		ensureOutput();
		
		OutputStream outputStream = getExporterOutput().getOutputStream();

		try
		{
			exportReportToStream(outputStream);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			getExporterOutput().close();
			resetExportContext();
		}
	}


	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	protected void ensureOutput()
	{
		if (exporterOutput == null)
		{
			exporterOutput = new net.sf.jasperreports.export.parameters.ParametersOutputStreamExporterOutput(getExporterContext());
		}
	}


	@Override
	protected void initExport()
	{
		super.initExport();

		workbookTemplate = workbookTemplate == null ? getPropertiesUtil().getProperty(jasperPrint, PROPERTY_WORKBOOK_TEMPLATE) : workbookTemplate;
		keepTemplateSheets = keepTemplateSheets == null ? getPropertiesUtil().getBooleanProperty(jasperPrint, PROPERTY_WORKBOOK_TEMPLATE_KEEP_SHEETS, false) : keepTemplateSheets;
	}

	
	protected void setExporterHints()
	{
		setSheetNames();
		
		gridRowFreezeIndex = Math.max(0, getPropertiesUtil().getIntegerProperty(jasperPrint, PROPERTY_FREEZE_ROW, 0) - 1);
		gridColumnFreezeIndex = Math.max(0, getColumnIndex(getPropertiesUtil().getProperty(jasperPrint, PROPERTY_FREEZE_COLUMN)));	
		columnWidthRatio = getPropertiesUtil().getFloatProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO, 0f);
		documentFirstPageNumber = getPropertiesUtil().getIntegerProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_FIRST_PAGE_NUMBER, 0);
		ignoreAnchors = getPropertiesUtil().getBooleanProperty(jasperPrint,	PROPERTY_IGNORE_ANCHORS, false);
		if(jasperPrint.hasProperties() && jasperPrint.getPropertiesMap().containsProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS))
		{
			// allows null values for the property
			invalidCharReplacement = jasperPrint.getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS);
		}
		else
		{
			invalidCharReplacement = getPropertiesUtil().getProperty(JRXmlExporter.PROPERTY_REPLACE_INVALID_CHARS, jasperPrint);
		}
		documentShowGridlines = getPropertiesUtil().getBooleanProperty(jasperPrint,	PROPERTY_SHOW_GRIDLINES, true);
	}
	
	protected void exportReportToStream(OutputStream os) throws JRException, IOException
	{
		openWorkbook(os);
		sheetNamesMap = new HashMap<String,Integer>();

		List<ExporterInputItem> items = exporterInput.getItems();

		for(reportIndex = 0; reportIndex < items.size(); reportIndex++)
		{
			ExporterInputItem item = items.get(reportIndex);
			setCurrentExporterInputItem(item);
			
			defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
			
			setExporterHints();

			if(!hasGlobalSheetNames())
			{
				sheetNamesIndex = 0;
			}

			XlsExporterConfiguration configuration = getCurrentConfiguration();

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				PageRange pageRange = getPageRange();
				int startPageIndex = (pageRange == null || pageRange.getStartPageIndex() == null) ? 0 : pageRange.getStartPageIndex();
				int endPageIndex = (pageRange == null || pageRange.getEndPageIndex() == null) ? (pages.size() - 1) : pageRange.getEndPageIndex();

				if (configuration.isOnePagePerSheet())
				{

					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}

						JRPrintPage page = pages.get(pageIndex);
						
						/*   */
						exportPage(page, /*xCuts*/null, /*startRow*/0, /*defaultSheetName*/null);
					}
				}
				else
				{
					/*
					 * Make a pass and calculate the X cuts for all pages on this sheet.
					 * The Y cuts can be calculated as each page is exported.
					 */
					CutsInfo xCuts = 
						JRGridLayout.calculateXCuts(
							getNature(), pages, startPageIndex, endPageIndex,
							jasperPrint.getPageWidth(), 
							configuration.getOffsetX() == null ? 0 : configuration.getOffsetX() 
							);
					
					ExporterFilter filter = configuration.getExporterFilter();
					
					//clear the filter's internal cache that might have built up
					if (filter instanceof ResetableExporterFilter)
					{
						((ResetableExporterFilter)filter).reset();
					}
					int startRow = 0;

					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}
						JRPrintPage page = pages.get(pageIndex);
						startRow = exportPage(page, xCuts, startRow, jasperPrint.getName());
					}
					
					//updateColumns(xCuts);
				}
			}
		}

		closeWorkbook(os);
	}

	/**
	 *
	 * @return the number of rows added.
	 */
	protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow, String defaultSheetName) throws JRException
	{
		XlsExporterConfiguration configuration = getCurrentConfiguration();
		
		int maxRowsPerSheet = configuration.getMaxRowsPerSheet();
		boolean isRemoveEmptySpaceBetweenRows = configuration.isRemoveEmptySpaceBetweenRows();
		boolean isRemoveEmptySpaceBetweenColumns = configuration.isRemoveEmptySpaceBetweenColumns();
		boolean isCollapseRowSpan = configuration.isCollapseRowSpan();
		
		JRGridLayout layout =
			new JRGridLayout(
				getNature(),
				page.getElements(),
				jasperPrint.getPageWidth(),
				jasperPrint.getPageHeight(),
				configuration.getOffsetX() == null ? 0 : configuration.getOffsetX(), 
				configuration.getOffsetY() == null ? 0 : configuration.getOffsetY(),
				xCuts
				);

		Grid grid = layout.getGrid();

		boolean createXCuts = (xCuts == null); 
		
		if (createXCuts) 
		{
			xCuts = layout.getXCuts();
		}

		CutsInfo yCuts = layout.getYCuts();
		
		if (createXCuts || startRow == 0)
		{
			exportSheet(xCuts, yCuts, 0, defaultSheetName);
		}

		XlsRowLevelInfo levelInfo = new XlsRowLevelInfo(); 

		int skippedRows = 0;
		int rowIndex = startRow;
		int rowCount = grid.getRowCount();
		for(int y = 0; y < rowCount; y++)
		{
			rowIndex = y - skippedRows + startRow;

			//if number of rows is too large a new sheet is created and populated with remaining rows
			if(
				(maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
				|| yCuts.isBreak(y) 
				)
			{
				//updateColumns(xCuts);
				
				setRowLevels(levelInfo, null);
				
				exportSheet(xCuts, yCuts, y, defaultSheetName);
				
				startRow = 0;
				rowIndex = 0;
				skippedRows = y;
			}
			
			if (
				yCuts.isCutNotEmpty(y)
				|| ((!isRemoveEmptySpaceBetweenRows || yCuts.isCutSpanned(y))
				&& !isCollapseRowSpan)
				)
			{
				GridRow gridRow = grid.getRow(y);

				int emptyCellColSpan = 0;
				//int emptyCellWidth = 0;

				Cut yCut = yCuts.getCut(y);
				mergeAndSetRowLevels(levelInfo, (SortedMap<String, Boolean>)yCut.getProperty(PROPERTY_ROW_OUTLINE_LEVEL_PREFIX), rowIndex);

				setRowHeight(
					rowIndex,
					isCollapseRowSpan
						?  layout.getMaxRowHeight(y)//FIXME consider putting these in cuts
						: JRGridLayout.getRowHeight(gridRow),
					yCuts.getCut(y),
					levelInfo
					);

				int emptyCols = 0;
				int rowSize = gridRow.size();
				for(int xCutIndex = 0; xCutIndex < rowSize; xCutIndex++)
				{
					boolean isEmptyCol = !(xCuts.isCutNotEmpty(xCutIndex) || xCuts.isCutSpanned(xCutIndex));//FIXMEXLS we could do this only once
					emptyCols += isRemoveEmptySpaceBetweenColumns && isEmptyCol ? 1 : 0;
					
					int colIndex = xCutIndex - emptyCols;
					
					JRExporterGridCell gridCell = gridRow.get(xCutIndex);

					//setCell(gridCell, colIndex, rowIndex);
					
					if (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL)
					{
						if (emptyCellColSpan > 0)
						{
							//tableHelper.exportEmptyCell(gridCell, emptyCellColSpan);
							emptyCellColSpan = 0;
							//emptyCellWidth = 0;
						}

						addOccupiedCell((OccupiedGridCell)gridCell, colIndex, rowIndex);
					}
					else if(gridCell.getType() == JRExporterGridCell.TYPE_ELEMENT_CELL)
					{
						if (emptyCellColSpan > 0)
						{
//							if (emptyCellColSpan > 1)
//							{
//								//sbuffer.append(" colspan=" + emptyCellColSpan);
//								//sheet.addMergedRegion(new Region(y, (short)(x - emptyCellColSpan - 1), y, (short)(x - 1)));
//							}
							emptyCellColSpan = 0;
							//emptyCellWidth = 0;
						}

						JRPrintElement element = gridCell.getElement();
						
						String rowFreeze = getPropertiesUtil().getProperty(element, PROPERTY_FREEZE_ROW_EDGE);
						
						int rowFreezeIndex = rowFreeze == null 
								? 0 
								: (EdgeEnum.BOTTOM.getName().equals(rowFreeze) 
										? rowIndex + gridCell.getRowSpan()
										: rowIndex
										);
							
						String columnFreeze = getPropertiesUtil().getProperty(element, PROPERTY_FREEZE_COLUMN_EDGE);
							
						int columnFreezeIndex = columnFreeze == null 
							? 0
							: (EdgeEnum.RIGHT.getName().equals(columnFreeze) 
									? colIndex + gridCell.getColSpan()
									: colIndex
									);

						if(rowFreezeIndex > 0 || columnFreezeIndex > 0)
						{
							setFreezePane(rowFreezeIndex, columnFreezeIndex, rowFreezeIndex > 0, columnFreezeIndex > 0);
						}

						/* this is no more necessary since the sheet name is stored in CutsInfo */
//						String sheetName = element.getPropertiesMap().getProperty(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME);
//						if(sheetName != null)
//						{
//							setSheetName(sheetName);
//						}


//						boolean start = getPropertiesUtil().getBooleanProperty(element, PROPERTY_AUTO_FILTER_START, false);
//						if(start && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
//						boolean end = getPropertiesUtil().getBooleanProperty(element, PROPERTY_AUTO_FILTER_END, false);
//						if(end && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
						String autofilter = getPropertiesUtil().getProperty(element, PROPERTY_AUTO_FILTER);
						if("Start".equals(autofilter))
						{
							autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
						}
						else if("End".equals(autofilter))
						{
							autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
						}
						
						if (element instanceof JRPrintLine)
						{
							exportLine((JRPrintLine)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintRectangle)
						{
							exportRectangle((JRPrintRectangle)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintEllipse)
						{
							exportRectangle((JRPrintEllipse)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintImage)
						{
							exportImage((JRPrintImage) element, gridCell, colIndex, rowIndex, emptyCols, y, layout);
						}
						else if (element instanceof JRPrintText)
						{
							exportText((JRPrintText)element, gridCell, colIndex, rowIndex);
						}
						else if (element instanceof JRPrintFrame)
						{
							exportFrame((JRPrintFrame) element, gridCell, colIndex, y);//FIXME rowIndex?
						}
						else if (element instanceof JRGenericPrintElement)
						{
							exportGenericElement((JRGenericPrintElement) element, gridCell, colIndex, rowIndex, emptyCols, y, layout);
						}

						//colIndex += gridCell.getColSpan() - 1;
					}
					else
					{
						emptyCellColSpan++;
						//emptyCellWidth += gridCell.getWidth();
						if (!isRemoveEmptySpaceBetweenColumns || !isEmptyCol)
						{
							addBlankCell(gridCell, colIndex, rowIndex);
						}
					}
				}

//				if (emptyCellColSpan > 0)
//				{
//					if (emptyCellColSpan > 1)
//					{
//						//sbuffer.append(" colspan=" + emptyCellColSpan);
//						//sheet.addMergedRegion(new Region(y, (short)x, y, (short)(x + emptyCellColSpan - 1)));
//					}
//				}
				
				//increment row index to return proper value
				++rowIndex;
			}
			else
			{
				skippedRows++;
//				setRowHeight(y, 0);
//
//				for(int x = 0; x < grid[y].length; x++)
//				{
//					addBlankCell(x, y);
//					setCell(x, y);
//				}
			}
		}

		if(autoFilterStart != null)
		{
			setAutoFilter(autoFilterStart + ":" + (autoFilterEnd != null ? autoFilterEnd : autoFilterStart));
		}
		else if(autoFilterEnd != null)
		{
			setAutoFilter(autoFilterEnd + ":" + autoFilterEnd);
		}
		
//		if (createXCuts)
//		{
//			updateColumns(xCuts);
//		}
		
		setRowLevels(levelInfo, null);
		
		JRExportProgressMonitor progressMonitor = configuration.getProgressMonitor();
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
		
		// Return the number of rows added
		return rowIndex;
	}
	

	protected SheetInfo getSheetProps(CutsInfo yCuts, int startCutIndex)
	{
		SheetInfo sheetInfo = new SheetInfo();
		
		XlsExporterConfiguration configuration = getCurrentConfiguration();
		int maxRowsPerSheet = configuration.getMaxRowsPerSheet();
		boolean isRemoveEmptySpaceBetweenRows = configuration.isRemoveEmptySpaceBetweenRows();
		boolean isCollapseRowSpan = configuration.isCollapseRowSpan();
		
		int skippedRows = 0;
		int rowIndex = 0;
		int rowCount = yCuts.size() - 1;
		for(int y = startCutIndex; y < rowCount; y++)
		{
			rowIndex = y - skippedRows;

			if(
				y > startCutIndex &&
				((maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
				|| yCuts.isBreak(y)) 
				)
			{
				break;
			}
			
			if (
				yCuts.isCutNotEmpty(y)
				|| ((!isRemoveEmptySpaceBetweenRows || yCuts.isCutSpanned(y))
				&& !isCollapseRowSpan)
				)
			{
				Cut yCut = yCuts.getCut(y);

				String sheetName = (String)yCut.getProperty(PROPERTY_SHEET_NAME);
				if (sheetName != null)
				{
					sheetInfo.sheetName = sheetName;
				}

				Integer firstPageNumber = (Integer)yCut.getProperty(PROPERTY_FIRST_PAGE_NUMBER);
				if (firstPageNumber != null)
				{
					sheetInfo.sheetFirstPageNumber = firstPageNumber;
				}
				Boolean showGridlines = (Boolean)yCut.getProperty(PROPERTY_SHOW_GRIDLINES);
				if (showGridlines != null)
				{
					sheetInfo.sheetShowGridlines = showGridlines;
				}

				Integer pageScale = (Integer)yCut.getProperty(XlsExporterConfiguration.PROPERTY_PAGE_SCALE);
				sheetInfo.sheetPageScale = (isValidScale(pageScale))
								? pageScale 
								: configuration.getPageScale();
				++rowIndex;
			}
			else
			{
				skippedRows++;
			}
		}
		
		return sheetInfo;
	}
	

	protected void exportSheet(CutsInfo xCuts, CutsInfo yCuts, int startCutIndex, String defaultSheetName)
	{
		SheetInfo sheetInfo = getSheetProps(yCuts, startCutIndex);
		
		sheetInfo.sheetName = getSheetName(sheetInfo.sheetName, defaultSheetName);
		
		createSheet(xCuts, sheetInfo);
		setScale(sheetInfo.sheetPageScale);

		// we need to count all sheets generated for all exported documents
		sheetIndex++;
		sheetNamesIndex++;
		resetAutoFilters();

		setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
		setColumnWidths(xCuts);
	}
	

	protected void mergeAndSetRowLevels(XlsRowLevelInfo levelInfo, SortedMap<String, Boolean> rowLevelMap, int rowIndex)
	{
		SortedMap<String, Integer> crtLevelMap = levelInfo.getLevelMap();

		if (rowLevelMap != null)
		{
			for(String level : rowLevelMap.keySet())
			{
				Boolean isEndMarker = rowLevelMap.get(level);
				
				//check if this level group is already open
				if (crtLevelMap.containsKey(level))
				{
					//the level group is already open
					
					if (isEndMarker)
					{
						//the level group needs to be closed, together with all its child level groups
						setRowLevels(levelInfo, level);

						//clean up current level group and nested level groups as they were closed 
						for(Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();)
						{
							if (level.compareTo(it.next()) <= 0)
							{
								it.remove();
							}
						}
					}
				}
				else
				{
					//the level group is not yet open

					//we check to see if this level is higher than existing levels
					if (crtLevelMap.size() > 0 && level.compareTo(crtLevelMap.firstKey()) < 0)
					{
						//the level is higher than existing levels, so it has to close them all
						setRowLevels(levelInfo, level);
						
						//clean up nested level groups as they were closed; the current one is not yet among them 
						for(Iterator<String> it = crtLevelMap.keySet().iterator(); it.hasNext();)
						{
							if (level.compareTo(it.next()) < 0)
							{
								it.remove();
							}
						}
					}
					
					//create the current level group
//					XlsRowLevelRange range = new XlsRowLevelRange();
//					range.setStartIndex(rowIndex);
					//range.setEndIndex(rowIndex);
					//range.setName(groupName);
					crtLevelMap.put(level, rowIndex);
				}
			}
		}

		levelInfo.setEndIndex(rowIndex);
	}
	
	
	protected void setColumnWidths(CutsInfo xCuts)
	{
		XlsExporterConfiguration configuration = getCurrentConfiguration();
		
		boolean isRemoveEmptySpaceBetweenColumns = configuration.isRemoveEmptySpaceBetweenColumns();

		Map<String, Object> xCutsProperties = xCuts.getPropertiesMap();
		Float ratio = (Float)xCutsProperties.get(XlsExporterConfiguration.PROPERTY_COLUMN_WIDTH_RATIO);
		float sheetRatio = 
			(ratio != null && ratio > 0f) 
			? ratio 
			: (columnWidthRatio > 0f ? columnWidthRatio : 1f);
		
		int emptyCols = 0;
		for(int xCutIndex = 0; xCutIndex < xCuts.size() - 1; xCutIndex++)
		{
			if (!isRemoveEmptySpaceBetweenColumns || (xCuts.isCutNotEmpty(xCutIndex) || xCuts.isCutSpanned(xCutIndex)))
			{
				Integer width = (Integer)xCutsProperties.get(PROPERTY_COLUMN_WIDTH);
				width = 
					width == null 
					? (int)((xCuts.getCutOffset(xCutIndex + 1) - xCuts.getCutOffset(xCutIndex)) * sheetRatio) 
					: width;  
				
				Cut xCut = xCuts.getCut(xCutIndex);
				boolean isAutoFit = xCut.hasProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN) 
						&& (Boolean)xCut.getProperty(JRXlsAbstractExporter.PROPERTY_AUTO_FIT_COLUMN);
				
				setColumnWidth(xCutIndex - emptyCols, width, isAutoFit);
			}
			else
			{
				emptyCols ++;
			}
		}
	}
	
	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		JRStyledTextAttributeSelector selector = textElement.getModeValue() == ModeEnum.OPAQUE 
				? allSelector 
				: noBackcolorSelector;
		return textElement.getFullStyledText(selector);
	}

	/**
	 *
	 */
	protected static TextAlignHolder getTextAlignHolder(JRPrintText textElement)
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

	/**
	 *
	 */
	protected int getImageBorderCorrection(JRPen pen)
	{
		float lineWidth = pen.getLineWidth().floatValue();
		
		if (lineWidth > 0f)
		{
			if (lineWidth >= 2f)
			{
				return 2;
			}

			return 1;
		}
		
		return getCurrentConfiguration().isImageBorderFixEnabled() ? 1 : 0;
	}

	
	/**
	 *
	 */
	private String getSheetName(String sheetName, String defaultSheetName)
	{
		if(sheetName != null)
		{
			if (sheetNames != null && sheetNamesIndex < sheetNames.length)
			{
				sheetNames[sheetNamesIndex] = sheetName;
			}
			return getSheetName(sheetName);
		}
		return getSheetName(defaultSheetName);
	}
	
	private String getSheetName(String sheetName)
	{
		if (sheetNames != null && sheetNamesIndex < sheetNames.length)
		{
			sheetName = sheetNames[sheetNamesIndex];
		}
		
		if (sheetName == null)
		{
			// no sheet name was specified or if it was null
			return "Page " + (sheetIndex + 1);
		}

		// sheet name specified; assuming it is first occurrence
		int crtIndex = Integer.valueOf(1);
		String txtIndex = "";
		String validSheetName = sheetName.length() < 32 ? sheetName : sheetName.substring(0, 31);
		
		if(sheetNamesMap.containsKey(validSheetName))
		{
			// sheet names must be unique; altering sheet name using number of occurrences
			crtIndex = sheetNamesMap.get(validSheetName).intValue() + 1;
			txtIndex = String.valueOf(crtIndex);
		}

		sheetNamesMap.put(validSheetName, crtIndex);

		String name = sheetName;
		if(txtIndex.length() > 0)
		{
			name += " " + txtIndex;
		}
		
		if (name.length() > 31)
		{
			name = (sheetName + " ").substring(0, 31 - txtIndex.length()) + txtIndex;
		}
		
		return name;
	}

	// Berechnungsvorschriften f�r die DIN Formate A, B, und C.
	// Die Angabe der Breite/H�he erfolgt in [mm].

	protected final int calculateWidthForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (-0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinAN(int n)
	{
		return (int) (Math.pow(2.0, (0.25 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinBN(int n)
	{
		return (int) (Math.pow(2.0, -(n / 2.0)) * 1000.0);
	}

	protected final int calculateHeightForDinBN(int n)
	{
		return (int) (Math.pow(2.0, (0.5 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateWidthForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (-0.125 - (n / 2.0))) * 1000.0);
	}

	protected final int calculateHeightForDinCN(int n)
	{
		return (int) (Math.pow(2.0, (0.375 - (n / 2.0))) * 1000.0);
	}

	/**
	 * 
	 */
	protected boolean isWrapText(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(PROPERTY_WRAP_TEXT)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, PROPERTY_WRAP_TEXT, getCurrentConfiguration().isWrapText());
		}
		return getCurrentConfiguration().isWrapText();
	}

	/**
	 * 
	 */
	protected boolean isCellLocked(JRPrintElement element)
	{

		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(PROPERTY_CELL_LOCKED)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, PROPERTY_CELL_LOCKED, getCurrentConfiguration().isCellLocked());
		}
		return getCurrentConfiguration().isCellLocked();
	}

	/**
	 * 
	 */
	protected String getFormula(JRPrintText text)
	{
		String formula = JRPropertiesUtil.getOwnProperty(text, JRXlsAbstractExporter.PROPERTY_CELL_FORMULA);
		if( formula != null)
		{
			formula = formula.trim();
			if(formula.startsWith("="))
			{
				formula = formula.substring(1);
			}
		}
		return formula;
	}

	/**
	 * 
	 */
	protected void setSheetNames()
	{
		String[] sheetNamesArray = getCurrentConfiguration().getSheetNames();
		
		List<String> sheetNamesList = JRStringUtil.split(sheetNamesArray, "/");
		sheetNames = sheetNamesList == null ? null : (String[]) sheetNamesList.toArray(new String[sheetNamesList.size()]);
	}
	
	/**
	 * 
	 */
	protected boolean hasGlobalSheetNames()
	{
		XlsExporterConfiguration itemConfiguration = (XlsExporterConfiguration)crtItem.getConfiguration();
		
		Boolean globalSheetNames = null;
		
		boolean isOverrideHintsDefault = 
			propertiesUtil.getBooleanProperty(
				ExporterConfiguration.PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS
				);
		if (
			exporterConfiguration != null 
			&& exporterConfiguration.getSheetNames() != null
			)
		{
			boolean isExporterConfigOverrideHints = 
				exporterConfiguration.isOverrideHints() == null 
				? isOverrideHintsDefault 
				: exporterConfiguration.isOverrideHints().booleanValue();
			if (isExporterConfigOverrideHints)
			{
				globalSheetNames = true;
			}
		}

		if (globalSheetNames == null)
		{
			if (
				itemConfiguration != null 
				&& itemConfiguration.getSheetNames() != null
				)
			{
				boolean isItemConfigOverrideHints = 
					itemConfiguration.isOverrideHints() == null 
					? isOverrideHintsDefault : 
					itemConfiguration.isOverrideHints().booleanValue();
				if (isItemConfigOverrideHints)
				{
					globalSheetNames = false;
				}
			}
		}

		if (globalSheetNames == null)
		{
			List<PropertySuffix> properties = 
				JRPropertiesUtil.getProperties(
					getCurrentJasperPrint(), 
					XlsExporterConfiguration.PROPERTY_SHEET_NAMES_PREFIX
					);
			globalSheetNames = properties == null || properties.isEmpty();
		}
		
		return globalSheetNames;
	}
	
	/**
	 * 
	 */
	protected boolean isCellHidden(JRPrintElement element)
	{
		if (
			element.hasProperties()
			&& element.getPropertiesMap().containsProperty(PROPERTY_CELL_HIDDEN)
			)
		{
			// we make this test to avoid reaching the global default value of the property directly
			// and thus skipping the report level one, if present
			return getPropertiesUtil().getBooleanProperty(element, PROPERTY_CELL_HIDDEN, getCurrentConfiguration().isCellHidden());
		}
		return getCurrentConfiguration().isCellHidden();
	}

	/**
	 * 
	 */
	protected String getConvertedPattern(JRPrintText text, String pattern)
	{
		String convertedPattern = JRPropertiesUtil.getOwnProperty(text, PROPERTY_CELL_PATTERN);
		if (convertedPattern == null)
		{
			Map<String, String> formatPatternsMap = getCurrentConfiguration().getFormatPatternsMap();
			if (formatPatternsMap != null && formatPatternsMap.containsKey(pattern))
			{
				return formatPatternsMap.get(pattern);
			}
			return pattern;
		}
		return convertedPattern;
	}

	/**
	 * 
	 * @return the calculated column index
	 */
	protected int getColumnIndex(String columnName)
	{
		int index = -1;
		if(columnName != null)
		{
			String upperColumnName = columnName.toUpperCase();
			if(upperColumnName.matches("[A-Z]*"))
			{
				for(int i = 0; i < upperColumnName.length(); i++)
				{
					index += (upperColumnName.charAt(i) - 64) * (int)Math.pow(26, upperColumnName.length() - i - 1);
				}
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @return the calculated column name
	 */
	protected String getColumnName(int columnIndex)
	{
		int i = columnIndex/26 + 64;
		int j = columnIndex%26 + 65;
		return (i > 64 ? String.valueOf((char)i) : "") + (char)j;
	}
	
	protected void setFreezePane(int rowIndex, int colIndex)
	{
		if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
		{
			setFreezePane(rowIndex, colIndex, false, false);
		}
	}
	
	protected void resetAutoFilters()
	{
		autoFilterStart = null;
		autoFilterEnd = null;
	}
	
	protected Date translateDateValue(JRPrintText text, Date value)
	{
		String prop = getPropertiesUtil().getProperty(PROPERTY_USE_TIMEZONE, 
				text, jasperPrint);
		if (JRPropertiesUtil.asBoolean(prop))
		{
			// translate the date to the timezone used at fill time
			TimeZone tz = getTextTimeZone(text);
			value = JRDataUtils.translateToTimezone(value, tz);
		}
		return value;
	}
	
	protected boolean isValidScale(Integer scale)
	{
		return scale != null && scale > 9 && scale < 401;
	}
	
	// property setters
	
	public boolean isWorkbookTemplateKeepSheets() {
		return keepTemplateSheets;
	}


	public void setWorkbookTemplateKeepSheets(boolean keepTemplateSheets) {
		this.keepTemplateSheets = keepTemplateSheets;
	}


	public String getWorkbookTemplate() {
		return workbookTemplate;
	}


	public void setWorkbookTemplate(String workbookTemplate) {
		this.workbookTemplate = workbookTemplate;
	}

	
	public boolean isIgnoreAnchors() {
		return ignoreAnchors;
	}


	public void setIgnoreAnchors(boolean ignoreAnchors) {
		this.ignoreAnchors = ignoreAnchors;
	}
	
	
	protected ExporterNature getNature()
	{
		return nature;
	}


	//abstract methods

	protected abstract void openWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void createSheet(CutsInfo xCuts, SheetInfo sheetInfo);

	protected abstract void closeWorkbook(OutputStream os) throws JRException, IOException;

	protected abstract void setColumnWidth(int col, int width, boolean autoFit);
	
	protected abstract void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException;

//	protected abstract void setCell(JRExporterGridCell gridCell, int colIndex, int rowIndex);

	protected abstract void addBlankCell(JRExporterGridCell gridCell, int colIndex, int rowIndex) throws JRException;

	protected abstract void addOccupiedCell(OccupiedGridCell occupiedGridCell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportText(JRPrintText text, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportImage(JRPrintImage image, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException;

	protected abstract void exportRectangle(JRPrintGraphicElement element, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportLine(JRPrintLine line, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportFrame(JRPrintFrame frame, JRExporterGridCell cell, int colIndex, int rowIndex) throws JRException;

	protected abstract void exportGenericElement(JRGenericPrintElement element, JRExporterGridCell cell, int colIndex, int rowIndex, int emptyCols, int yCutsRow, JRGridLayout layout) throws JRException;

	protected abstract void setFreezePane(int rowIndex, int colIndex, boolean isRowEdge, boolean isColumnEdge);
	
	protected abstract void setSheetName(String sheetName);
	
	protected abstract void setAutoFilter(String autoFilterRange);
	
	protected abstract void setRowLevels(XlsRowLevelInfo levelInfo, String level);
	
	protected abstract void setScale(Integer scale);
	
}
