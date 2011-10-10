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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static final String PROPERTY_AUTO_FILTER = JRProperties.PROPERTY_PREFIX + "export.xls.auto.filter";
	
	
	public static final int MAX_ROW_INDEX = 65535;
	public static final int MAX_COLUMN_INDEX = 255;
	
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
	
	protected String autoFilter;		

	protected int maxRowFreezeIndex;
	protected int maxColumnFreezeIndex;
	
	protected boolean isFreezeRowEdge;
	protected boolean isFreezeColumnEdge;
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
		
		autoFilter = 
				JRProperties.getProperty(
					jasperPrint,
					PROPERTY_AUTO_FILTER
					);
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

						setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
						if(autoFilter != null)
						{
							setAutoFilter(autoFilter);
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

					setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
					if(autoFilter != null)
					{
						setAutoFilter(autoFilter);
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
					
					if (isRemoveEmptySpaceBetweenColumns)
					{
						removeEmptyColumns(xCuts);
					}
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
				if (isRemoveEmptySpaceBetweenColumns)
				{
					removeEmptyColumns(xCuts);
				}
				createSheet(getSheetName(null));
				setColumnWidths(xCuts);
				startRow = 0;
				rowIndex = 0;
				skippedRows = y;
				sheetIndex++;
				sheetNamesIndex++;
				setFreezePane(gridRowFreezeIndex, gridColumnFreezeIndex);
				if(autoFilter != null)
				{
					setAutoFilter(autoFilter);
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

				setRowHeight(
					rowIndex,
					isCollapseRowSpan
						?  layout.getMaxRowHeight(y)
						: JRGridLayout.getRowHeight(gridRow)
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

						String autoFilterRange = JRProperties.getProperty(element, PROPERTY_AUTO_FILTER);
						if(autoFilterRange != null)
						{
							setAutoFilter(autoFilterRange);
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

		if (createXCuts && isRemoveEmptySpaceBetweenColumns)
		{
			removeEmptyColumns(xCuts);
		}
		
		if (progressMonitor != null)
		{
			progressMonitor.afterPageExport();
		}

		// Return the number of rows added
		return rowIndex;
	}


	protected void setColumnWidths(CutsInfo xCuts)
	{
		for(int col = 0; col < xCuts.size() - 1; col++)
		{
			if (
				(!isRemoveEmptySpaceBetweenColumns || (xCuts.isCutNotEmpty(col) || xCuts.isCutSpanned(col)))
				|| !xCuts.isAutoFit(col)
				)
			{
				int width = xCuts.getCutOffset(col + 1) - xCuts.getCutOffset(col);
				setColumnWidth(col, width);
			}
		}
	}

	protected void removeEmptyColumns(CutsInfo xCuts)
	{
		for(int col = xCuts.size() - 1; col >= 0; col--)
		{
			if (!(xCuts.isCutNotEmpty(col) || xCuts.isCutSpanned(col)))
			{
				removeColumn(col);
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
	
	protected void setFreezePane(int rowIndex, int colIndex)
	{
		setFreezePane(rowIndex, colIndex, false, false);
	}
	
	protected abstract ExporterNature getNature();

	protected abstract void openWorkbook(OutputStream os) throws JRException;

	protected abstract void createSheet(String name);

	protected abstract void closeWorkbook(OutputStream os) throws JRException;

	protected abstract void setColumnWidth(int col, int width);

	protected abstract void removeColumn(int col);

	protected abstract void setRowHeight(int rowIndex, int lastRowHeight) throws JRException;

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
	
}
