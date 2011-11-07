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
 * Contributors:
 * Greg Hilton
 */

package net.sf.jasperreports.engine.export;

import java.io.File;
import java.io.FileOutputStream;
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
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
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
import net.sf.jasperreports.engine.JRStyledTextAttributeSelector;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRDataUtils;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRXlsAbstractExporter extends JRAbstractExporter
{

	protected static final String XLS_EXPORTER_PROPERTIES_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xls.";

	/**
	 * Property that stores the formula which has to be applied to a given cell in an excel sheet.
	 */
	public static final String PROPERTY_CELL_FORMULA = JRProperties.PROPERTY_PREFIX + "export.xls.formula";

	/**
	 * Property that stores the pattern which has to be applied to a given cell in an excel sheet.
	 */
	public static final String PROPERTY_CELL_PATTERN = JRProperties.PROPERTY_PREFIX + "export.xls.pattern";

	/**
	 * This property indicates whether text wrapping is allowed in a given cell.
	 * <p>
	 * The property itself defaults to <code>true</code>.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_WRAP_TEXT = JRProperties.PROPERTY_PREFIX + "export.xls.wrap.text";


	/**
	 * This property indicates the number of pages wide to fit the sheet in.
	 * <p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FIT_WIDTH = JRProperties.PROPERTY_PREFIX + "export.xls.fit.width";

	/**
	 * This property indicates the number of pages height to fit the sheet in.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_FIT_HEIGHT = JRProperties.PROPERTY_PREFIX + "export.xls.fit.height";

	/**
	 * This property indicates whether the cell is locked.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_CELL_LOCKED = JRProperties.PROPERTY_PREFIX + "export.xls.cell.locked";

	/**
	 * This property indicates whether the cell content is hidden.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_CELL_HIDDEN = JRProperties.PROPERTY_PREFIX + "export.xls.cell.hidden";

	/**
	 * This property stores the text content of the sheet header's left side.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_HEADER_LEFT = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.header.left";

	/**
	 * This property stores the text content of the sheet header's center.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_HEADER_CENTER = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.header.center";

	/**
	 * This property stores the text content of the sheet header's right side.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_HEADER_RIGHT = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.header.right";

	/**
	 * This property stores the text content of the sheet footer's left side.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_FOOTER_LEFT = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.footer.left";

	/**
	 * This property stores the text content of the sheet footer's center.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_FOOTER_CENTER = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.footer.center";

	/**
	 * This property stores the text content of the sheet footer's right side.
	 * </p>
	 * @see JRProperties
	 */
	public static final String PROPERTY_SHEET_FOOTER_RIGHT = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.footer.right";

	/**
	 * This property indicates if the sheet is left-to-right or right-to-left oriented. Possible values are:
	 * <ul>
	 * <li>LTR - meaning left-to-right</li>
	 * <li>RTL - meaning right-to-left</li>
	 * </ul>
	 * The default value is LTR.
	 * @see JRProperties
	 * @see RunDirectionEnum
	 */
	public static final String PROPERTY_SHEET_DIRECTION = JRProperties.PROPERTY_PREFIX + "export.xls.sheet.direction";
	
	/**
	 * Specifies the index of the first unlocked row in document's sheets. All rows above this will be 'frozen'. 
	 * Allowed values are represented by positive integers in the 1..65536 range. Negative values are not considered. 
	 * The property should be used when all sheets in the document have the same freeze row index.
	 */
	public static final String PROPERTY_FREEZE_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.freeze.row";
	
	/**
	 * Indicates the name of the first unlocked column in document's sheets. All columns to the left of this one will be 'frozen'. 
	 * Allowed values are letters or letter combinations representing valid column names in Excel, such as A, B, AB, AC, etc.
	 * The property should be used when all document sheets have the same freeze column name.
	 */
	public static final String PROPERTY_FREEZE_COLUMN = JRProperties.PROPERTY_PREFIX + "export.xls.freeze.column";
	
	/**
	 * This property indicates the horizontal edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_ROW value.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Top</code> - The current row is the first unlocked row in the sheet. All rows above are 'frozen'.</li>
	 * <li><code>Bottom</code> - The current row is the last 'frozen' row in the sheet. All rows below are unlocked.</li>
	 * </ul>
	 */
	public static final String PROPERTY_FREEZE_ROW_EDGE = JRProperties.PROPERTY_PREFIX + "export.xls.freeze.row.edge";
	
	/**
	 * This property indicates the vertical edge of the freeze pane, relative to the current cell. If set, it overrides the 
	 * PROPERTY_FREEZE_COLUMN and PROPERTY_FREEZE_SHEET_COLUMNS values.
	 * Allowed values are:
	 * <ul>
	 * <li><code>Left</code> - The current column is the first unlocked column in the sheet. All columns to the left are 'frozen'.</li>
	 * <li><code>Right</code> - The current column is the last 'frozen' column in the sheet. All columns to the right are unlocked.</li>
	 * </ul>
	 */
	public static final String PROPERTY_FREEZE_COLUMN_EDGE = JRProperties.PROPERTY_PREFIX + "export.xls.freeze.column.edge";
	
	public static final String PROPERTY_AUTO_FIT_ROW = JRProperties.PROPERTY_PREFIX + "export.xls.auto.fit.row";
	public static final String PROPERTY_AUTO_FIT_COLUMN = JRProperties.PROPERTY_PREFIX + "export.xls.auto.fit.column";
	
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
	 * @see JRProperties
	 */
	public static final String PROPERTY_AUTO_FILTER = JRProperties.PROPERTY_PREFIX + "export.xls.auto.filter";
	
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
	 * @see JRProperties
	 */
	public static final String PROPERTY_COLUMN_WIDTH = JRProperties.PROPERTY_PREFIX + "export.xls.column.width";

	/**
	 * Property used to adjust all column widths in a document or sheet with the same width ratio, in order to get column width 
	 * values suitable for Excel output. Usually column widths are measured by Excel in Normal style default character width 
	 * units, while the JR engine uses pixels as default size units. When exporting the report to the Excel output format, the 
	 * pixel-to-character width translation depends on the normal style default character width provided by the Excel instance, 
	 * so it cannot be always accurately fitted. In this case, one can alter the generated column widths by setting this property 
	 * with a float value representing the adjustment ratio. The property can be set:
	 * <ul>
	 * <li>globally - then all the columns in all documents exported to the Excel output format will be adjusted with the same width ratio</li>
	 * <li>at report level - then all the columns in the document will be adjusted with the same width ratio</li>
	 * <li>at element level - then all the columns in the current sheet will be adjusted with the same width ratio</li>
	 * </ul> 
	 * Global settings are overriden by report level settings and report level settings are overriden by element level settings. If 
	 * present, a {@link #PROPERTY_COLUMN_WIDTH PROPERTY_COLUMN_WIDTH} property will override the 
	 * {@link #PROPERTY_COLUMN_WIDTH_RATIO PROPERTY_COLUMN_WIDTH_RATIO} value for that column only.
	 * 
	 * @see #PROPERTY_COLUMN_WIDTH
	 * @see JRProperties
	 */
	public static final String PROPERTY_COLUMN_WIDTH_RATIO = JRProperties.PROPERTY_PREFIX + "export.xls.column.width.ratio";
	
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
	 * @see JRProperties
	 */
	public static final String PROPERTY_ROW_OUTLINE_LEVEL_PREFIX = JRProperties.PROPERTY_PREFIX + "export.xls.row.outline.level.";
	
	public static final int MAX_ROW_INDEX = 65535;
	public static final int MAX_COLUMN_INDEX = 255;

	/**
	 * Property that determines whether date values are to be translated to the timezone
	 * that was used to fill the report.
	 * 
	 * <p>
	 * By default, date values are exported to Excel using the default timezone of the system.
	 * Setting this property to <code>true</code> instructs the exporter to use he report fill
	 * timezone to export date values.
	 * 
	 * <p>
	 * The property only has effect when {@link JRXlsAbstractExporterParameter#IS_DETECT_CELL_TYPE} is set.
	 * 
	 * <p>
	 * The property can be set globally, at report level and at element level.
	 * The default value is <code>false</code>.
	 * 
	 * @since 4.5.0
	 */
	public static final String PROPERTY_USE_TIMEZONE = JRProperties.PROPERTY_PREFIX + "export.xls.use.timezone";
	
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
	protected boolean isOnePagePerSheet;
	protected boolean isRemoveEmptySpaceBetweenRows;
	protected boolean isRemoveEmptySpaceBetweenColumns;
	protected boolean isWhitePageBackground;
	protected boolean isDetectCellType;
	protected boolean isFontSizeFixEnabled;
	protected boolean isImageBorderFixEnabled;
	protected boolean isIgnoreGraphics;
	protected boolean createCustomPalette;
	protected boolean isCollapseRowSpan;
	protected boolean isIgnoreCellBorder;
	protected boolean isIgnoreCellBackground;
	protected boolean wrapText;
	protected boolean cellLocked;
	protected boolean cellHidden;

	protected int maxRowsPerSheet;

	protected String[] sheetNames;
	
	protected String sheetHeaderLeft;
	protected String sheetHeaderCenter;
	protected String sheetHeaderRight;
	
	protected String sheetFooterLeft;
	protected String sheetFooterCenter;
	protected String sheetFooterRight;
	
	protected RunDirectionEnum sheetDirection;

	protected Map<String,String> formatPatternsMap;

	protected JRExportProgressMonitor progressMonitor;

	protected int reportIndex;
	protected int pageIndex;

	/**
	 * @deprecated
	 */
	protected Map<String,String> fontMap;

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

	protected boolean isIgnorePageMargins;

	protected int gridRowFreezeIndex;
	protected int gridColumnFreezeIndex;
	
	protected int maxRowFreezeIndex;
	protected int maxColumnFreezeIndex;
	
	protected boolean isFreezeRowEdge;
	protected boolean isFreezeColumnEdge;
	
	protected String autoFilterStart;		
	protected String autoFilterEnd;		

	protected Float columnWidthRatio;
	/**
	 *
	 */
	protected JRFont getDefaultFont()
	{
		return defaultFont;
	}


	/**
	 *
	 */
	public void exportReport() throws JRException
	{
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);

		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();

			/*   */
			setInput();

			if (!parameters.containsKey(JRExporterParameter.FILTER))
			{
				filter = createFilter(XLS_EXPORTER_PROPERTIES_PREFIX);
			}
			
			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}

			setParameters();

			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				exportReportToStream(os);
			}
			else
			{
				File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
				if (destFile == null)
				{
					String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
					if (fileName != null)
					{
						destFile = new File(fileName);
					}
					else
					{
						throw new JRException("No output specified for the exporter.");
					}
				}

				try
				{
					os = new FileOutputStream(destFile);
					exportReportToStream(os);
					os.flush();
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to file : " + destFile, e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch(IOException e)
						{
						}
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}

	protected void setParameters()
	{
		isOnePagePerSheet = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
				JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET,
				false
				);

		isRemoveEmptySpaceBetweenRows = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				false
				);

		isRemoveEmptySpaceBetweenColumns = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
				JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
				false
				);

		isWhitePageBackground = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,
				false
				);
		setBackground();

		isDetectCellType = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE,
				JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE,
				false
				);

		isFontSizeFixEnabled = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
				JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED,
				false
				);

		isImageBorderFixEnabled = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED,
				JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED,
				false
				);
		
		isIgnoreGraphics = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS,
				JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS,
				false
				);

		createCustomPalette = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.CREATE_CUSTOM_PALETTE, 
				JRXlsAbstractExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE, 
				false
				);

		isCollapseRowSpan = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN,
				JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN,
				false
				);

		isIgnoreCellBorder = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER,
				JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER,
				false
				);

		isIgnoreCellBackground = 
			getBooleanParameter(
				JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BACKGROUND,
				JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND,
				false
				);

		wrapText = 
			JRProperties.getBooleanProperty(
				jasperPrint,
				PROPERTY_WRAP_TEXT,
				true
				);

		cellLocked = 
			JRProperties.getBooleanProperty(
				jasperPrint,
				PROPERTY_CELL_LOCKED,
				true
				);

		cellHidden = 
			JRProperties.getBooleanProperty(
				jasperPrint,
				PROPERTY_CELL_HIDDEN,
				false
				);

		fontMap = (Map<String,String>) parameters.get(JRExporterParameter.FONT_MAP);

		setHyperlinkProducerFactory();

		maxRowsPerSheet = 
			getIntegerParameter(
				JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET,
				JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET,
				0
				);
		
		isIgnorePageMargins = 
			getBooleanParameter(
				JRExporterParameter.IGNORE_PAGE_MARGINS, 
				JRExporterParameter.PROPERTY_IGNORE_PAGE_MARGINS, 
				false
				); 
			
		sheetHeaderLeft = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_HEADER_LEFT
				);
		
		sheetHeaderCenter = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_HEADER_CENTER
				);
		
		sheetHeaderRight = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_HEADER_RIGHT
				);
		
		sheetFooterLeft = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_FOOTER_LEFT
				);
		
		sheetFooterCenter = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_FOOTER_CENTER
				);
		
		sheetFooterRight = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_FOOTER_RIGHT
				);
		
		String sheetDirectionProp = 
			JRProperties.getProperty(
				jasperPrint,
				PROPERTY_SHEET_DIRECTION
				);
		sheetDirection = sheetDirectionProp == null ? RunDirectionEnum.LTR : RunDirectionEnum.getByName(sheetDirectionProp);
		
		formatPatternsMap = (Map<String,String>)getParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP);
		
	}

	protected void setExporterHints()
	{
		setSheetNames();
		
		gridRowFreezeIndex = Math.min(
				Math.max(0, JRProperties.getIntegerProperty(jasperPrint, PROPERTY_FREEZE_ROW, 0) - 1), 
				MAX_ROW_INDEX
				);
		gridColumnFreezeIndex = Math.min(
				Math.max(0, getColumnIndex(JRProperties.getProperty(jasperPrint, PROPERTY_FREEZE_COLUMN))), 
				MAX_COLUMN_INDEX
				);	
		columnWidthRatio = JRProperties.getFloatProperty(jasperPrint, JRXlsAbstractExporter.PROPERTY_COLUMN_WIDTH_RATIO, 0f);
	}

	protected abstract void setBackground();

	protected void exportReportToStream(OutputStream os) throws JRException
	{
		openWorkbook(os);
		sheetNamesMap = new HashMap<String,Integer>();

		for(reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++)
		{
			setJasperPrint(jasperPrintList.get(reportIndex));
			
			defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
			
			setExporterHints();

			if(
				getParameter(JRXlsAbstractExporterParameter.SHEET_NAMES) == null
				|| (getParameterResolver() instanceof ParameterOverriddenResolver
					&& sheetNames != null && sheetNames.length > 0)
				)
			{
				sheetNamesIndex = 0;
			}

			List<JRPrintPage> pages = jasperPrint.getPages();
			if (pages != null && pages.size() > 0)
			{
				if (isModeBatch)
				{
					startPageIndex = 0;
					endPageIndex = pages.size() - 1;
				}

				if (isOnePagePerSheet)
				{

					for(pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
					{
						if (Thread.interrupted())
						{
							throw new JRException("Current thread interrupted.");
						}

						JRPrintPage page = pages.get(pageIndex);

						createSheet(getSheetName(null));

						// we need to count all sheets generated for all exported documents
						sheetIndex++;
						sheetNamesIndex++;
						resetAutoFilters();
						if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
						{
							setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
						}

						/*   */
						exportPage(page, /*xCuts*/null, /*startRow*/0);
					}
				}
				else
				{
					// Create the sheet before looping.
					createSheet(getSheetName(jasperPrint.getName()));

					// we need to count all sheets generated for all exported documents
					sheetIndex++;
					sheetNamesIndex++;
					resetAutoFilters();

					if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
					{
						setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
					}
					
					/*
					 * Make a pass and calculate the X cuts for all pages on this sheet.
					 * The Y cuts can be calculated as each page is exported.
					 */
					CutsInfo xCuts = 
						JRGridLayout.calculateXCuts(
							getNature(), pages, startPageIndex, endPageIndex,
							jasperPrint.getPageWidth(), globalOffsetX
							);
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
						startRow = exportPage(page, xCuts, startRow);
					}
					
					updateColumns(xCuts);
				}
			}
		}

		closeWorkbook(os);
	}

	/**
	 *
	 * @return the number of rows added.
	 */
	protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow) throws JRException
	{
		JRGridLayout layout =
			new JRGridLayout(
				getNature(),
				page.getElements(),
				jasperPrint.getPageWidth(),
				jasperPrint.getPageHeight(),
				globalOffsetX,
				globalOffsetY,
				xCuts
				);

		JRExporterGridCell grid[][] = layout.getGrid();

		boolean createXCuts = (xCuts == null); 
		
		if (createXCuts) 
		{
			xCuts = layout.getXCuts();
			setColumnWidths(xCuts);
		}
		
		if (startRow == 0)
		{
			setColumnWidths(xCuts);
		}

		CutsInfo yCuts = layout.getYCuts();
		
		XlsRowLevelInfo levelInfo = new XlsRowLevelInfo(); 

		int skippedRows = 0;
		int rowIndex = startRow;
		for(int y = 0; y < grid.length; y++)
		{
			rowIndex = y - skippedRows + startRow;

			//if number of rows is too large a new sheet is created and populated with remaining rows
			if(
				(maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet)
				|| yCuts.isBreak(y) 
				)
			{
				updateColumns(xCuts);
				
				setRowLevels(levelInfo, null);

				createSheet(getSheetName(null));
				setColumnWidths(xCuts);
				startRow = 0;
				rowIndex = 0;
				skippedRows = y;
				sheetIndex++;
				sheetNamesIndex++;
				resetAutoFilters();
				
				if(gridRowFreezeIndex > 0 || gridColumnFreezeIndex > 0)
				{
					setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
				}

			}
			
			if (
				yCuts.isCutNotEmpty(y)
				|| ((!isRemoveEmptySpaceBetweenRows || yCuts.isCutSpanned(y))
				&& !isCollapseRowSpan)
				)
			{
				JRExporterGridCell[] gridRow = grid[y];

				int emptyCellColSpan = 0;
				int emptyCellWidth = 0;

				mergeAndSetRowLevels(levelInfo, yCuts.getRowLevelMap(y), rowIndex);

				setRowHeight(
					rowIndex,
					isCollapseRowSpan
						?  layout.getMaxRowHeight(y)//FIXME consider putting these in cuts
						: JRGridLayout.getRowHeight(gridRow),
					yCuts.getCut(y),
					levelInfo
					);

				int emptyCols = 0;
				for(int colIndex = 0; colIndex < gridRow.length; colIndex++)
				{
					emptyCols += (isRemoveEmptySpaceBetweenColumns && (!(xCuts.isCutNotEmpty(colIndex) || xCuts.isCutSpanned(colIndex))) ? 1 : 0);
					
					JRExporterGridCell gridCell = gridRow[colIndex];

					setCell(gridCell, colIndex, rowIndex);
					
					if (gridCell.getType() == JRExporterGridCell.TYPE_OCCUPIED_CELL)
					{
						if (emptyCellColSpan > 0)
						{
							//tableHelper.exportEmptyCell(gridCell, emptyCellColSpan);
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}

						addOccupiedCell((OccupiedGridCell)gridCell, colIndex, rowIndex);
					}
					else if(gridCell.getWrapper() != null)
					{
						if (emptyCellColSpan > 0)
						{
//							if (emptyCellColSpan > 1)
//							{
//								//sbuffer.append(" colspan=" + emptyCellColSpan);
//								//sheet.addMergedRegion(new Region(y, (short)(x - emptyCellColSpan - 1), y, (short)(x - 1)));
//							}
							emptyCellColSpan = 0;
							emptyCellWidth = 0;
						}

						JRPrintElement element = gridCell.getWrapper().getElement();
						
						String rowFreeze = JRProperties.getProperty(element, PROPERTY_FREEZE_ROW_EDGE);
						
						int rowFreezeIndex = rowFreeze == null 
								? 0 
								: (EdgeEnum.BOTTOM.getName().equals(rowFreeze) 
										? rowIndex + gridCell.getRowSpan()
										: rowIndex
										);
							
						String columnFreeze = JRProperties.getProperty(element, PROPERTY_FREEZE_COLUMN_EDGE);
							
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

						String sheetName = element.getPropertiesMap().getProperty(JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAME);
						if(sheetName != null)
						{
							setSheetName(sheetName);
						}

//						boolean start = JRProperties.getBooleanProperty(element, PROPERTY_AUTO_FILTER_START, false);
//						if(start && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
//						boolean end = JRProperties.getBooleanProperty(element, PROPERTY_AUTO_FILTER_END, false);
//						if(end && rowIndex < MAX_ROW_INDEX)
//						{
//							autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
//						}
//						
						if(rowIndex < MAX_ROW_INDEX)
						{
							String autofilter = JRProperties.getProperty(element, PROPERTY_AUTO_FILTER);
							if("Start".equals(autofilter))
							{
								autoFilterStart = getColumnName(colIndex) + (rowIndex + 1);
							}
							else if("End".equals(autofilter))
							{
								autoFilterEnd = getColumnName(colIndex) + (rowIndex + 1);
							}
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
						emptyCellWidth += gridCell.getWidth();
						addBlankCell(gridCell, colIndex, rowIndex);
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
		
		if (createXCuts)
		{
			updateColumns(xCuts);
		}
		
		setRowLevels(levelInfo, null);
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}
		
		// Return the number of rows added
		return rowIndex;
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
		Float ratio = xCuts.getWidthRatio();
		float sheetRatio = 
			(ratio != null && ratio > 0f) 
			? ratio 
			: (columnWidthRatio > 0f ? columnWidthRatio : 1f);
		
		for(int col = 0; col < xCuts.size() - 1; col++)
		{
			if (!isRemoveEmptySpaceBetweenColumns || (xCuts.isCutNotEmpty(col) || xCuts.isCutSpanned(col)))
			{
				int width = xCuts.getCustomWidth(col)!= null 
					? xCuts.getCustomWidth(col) 
					: (int)((xCuts.getCutOffset(col + 1) - xCuts.getCutOffset(col)) * sheetRatio);
				setColumnWidth(col, width, xCuts.isAutoFit(col));
			}
		}
	}

	protected void updateColumns(CutsInfo xCuts)
	{
		for(int col = xCuts.size() - 1; col >= 0; col--)
		{
			Cut xCut = xCuts.getCut(col);
			if (isRemoveEmptySpaceBetweenColumns && (!(xCut.isCutNotEmpty() || xCut.isCutSpanned())))
			{
				removeColumn(col);
			}
			if (xCuts.isAutoFit(col))
			{
				updateColumn(col, xCut.isAutoFit());
			}
		}
	}

	/**
	 *
	 */
	protected JRStyledText getStyledText(JRPrintText textElement)
	{
		return textElement.getFullStyledText(JRStyledTextAttributeSelector.NONE);
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
		
		return isImageBorderFixEnabled ? 1 : 0;
	}

	
	/**
	 *
	 */
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

		if(sheetNamesMap.containsKey(sheetName))
		{
			// sheet names must be unique; altering sheet name using number of occurrences
			crtIndex = sheetNamesMap.get(sheetName).intValue() + 1;
			txtIndex = String.valueOf(crtIndex);
		}

		sheetNamesMap.put(sheetName, crtIndex);

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
			return JRProperties.getBooleanProperty(element, PROPERTY_WRAP_TEXT, wrapText);
		}
		return wrapText;
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
				return JRProperties.getBooleanProperty(element, PROPERTY_CELL_LOCKED, cellLocked);
			}
			return cellLocked;
	}

	/**
	 * 
	 */
	protected String getFormula(JRPrintText text)
	{
		String formula = text.getPropertiesMap().getProperty(JRXlsAbstractExporter.PROPERTY_CELL_FORMULA);
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
		String[] sheetNamesArray = 
			getStringArrayParameter(
				JRXlsAbstractExporterParameter.SHEET_NAMES,
				JRXlsAbstractExporterParameter.PROPERTY_SHEET_NAMES_PREFIX
				);
		if (sheetNamesArray != null)
		{
			List<String> sheetNamesList = new ArrayList<String>();
			for(int i = 0; i < sheetNamesArray.length; i++)
			{
				if (sheetNamesArray[i] == null)
				{
					sheetNamesList.add(null);
				}
				else
				{
					String[] currentSheetNamesArray = sheetNamesArray[i].split("/");
					for(int j = 0; j < currentSheetNamesArray.length; j++)
					{
						sheetNamesList.add(currentSheetNamesArray[j]);
					}
				}
			}
			sheetNames = sheetNamesList.toArray(new String[sheetNamesList.size()]);
		}
		
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
				return JRProperties.getBooleanProperty(element, PROPERTY_CELL_HIDDEN, cellHidden);
			}
			return cellHidden;
	}

	/**
	 * 
	 */
	protected String getConvertedPattern(JRPrintText text, String pattern)
	{
		String convertedPattern = text.getPropertiesMap().getProperty(PROPERTY_CELL_PATTERN);
		if (convertedPattern == null)
		{
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
		setFreezePane(rowIndex, colIndex, false, false);
	}
	
	protected void resetAutoFilters()
	{
		autoFilterStart = null;
		autoFilterEnd = null;
	}
	
	protected Date translateDateValue(JRPrintText text, Date value)
	{
		String prop = JRProperties.getProperty(PROPERTY_USE_TIMEZONE, 
				text, jasperPrint);
		if (JRProperties.asBoolean(prop))
		{
			// translate the date to the timezone used at fill time
			TimeZone tz = getTextTimeZone(text);
			value = JRDataUtils.translateToTimezone(value, tz);
		}
		return value;
	}
	
	protected abstract ExporterNature getNature();

	protected abstract void openWorkbook(OutputStream os) throws JRException;

	protected abstract void createSheet(String name);

	protected abstract void closeWorkbook(OutputStream os) throws JRException;

	protected abstract void setColumnWidth(int col, int width, boolean autoFit);
	
	protected abstract void removeColumn(int col);

	protected abstract void updateColumn(int col, boolean autoFit);

	protected abstract void setRowHeight(int rowIndex, int lastRowHeight, Cut yCut, XlsRowLevelInfo levelInfo) throws JRException;

	protected abstract void setCell(JRExporterGridCell gridCell, int colIndex, int rowIndex);

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
	
}
