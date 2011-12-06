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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRBoxUtil;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRGridLayout
{
	private final ExporterNature nature;

	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	private final String address;

	private CutsInfo xCuts;
	private CutsInfo yCuts;
	private JRExporterGridCell[][] grid;

	private Map<BoxKey,JRLineBox> boxesCache;

	private int virtualFrameIndex;

	private boolean hasTopMargin = true;
	private boolean hasBottomMargin = true;
	private boolean hasLeftMargin = true;
	private boolean hasRightMargin = true;
	
	private boolean isNested;
	
	/**
	 * Constructor.
	 *
	 * @param elements the elements that should arranged in a grid
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 */
	public JRGridLayout(
		ExporterNature nature,
		List<JRPrintElement> elements,
		int width,
		int height,
		int offsetX,
		int offsetY
		)
	{
		this(
			nature,
			elements,
			width,
			height,
			offsetX,
			offsetY,
			null //xCuts
			);
	}

	/**
	 * Constructor.
	 *
	 * @param elements the elements that should arranged in a grid
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 * @param xCuts An optional list of pre-calculated X cuts.
	 */
	public JRGridLayout(
		ExporterNature nature,
		List<JRPrintElement> elements,
		int width,
		int height,
		int offsetX,
		int offsetY,
		CutsInfo xCuts
		)
	{
		this.nature = nature;
		this.height = height;
		this.width = width;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.address = null;
		this.xCuts = xCuts;

		boxesCache = new HashMap<BoxKey,JRLineBox>();

		virtualFrameIndex = elements.size();

		layoutGrid(createWrappers(null, elements, address));

		if (nature.isSplitSharedRowSpan())
		{
			splitSharedRowSpanIntoNestedGrids();
		}
	}

	/**
	 * Constructor.
	 *
	 * @param wrappers the element wrappers that should arranged in a grid
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 * @param address element address
	 */
	protected JRGridLayout(
		ExporterNature nature,
		ElementWrapper[] wrappers,
		int width,
		int height,
		int offsetX,
		int offsetY,
		String address
		)
	{
		this.nature = nature;
		this.height = height;
		this.width = width;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.address = address;

		//this constructor is called only in nested grids:
		this.isNested = true;

		boxesCache = new HashMap<BoxKey,JRLineBox>();
		
		layoutGrid(wrappers);

		if (nature.isSplitSharedRowSpan())
		{
			splitSharedRowSpanIntoNestedGrids();
		}
	}


	/**
	 *
	 */
	private void createNestedGrid(
		int row1,
		int col1,
		int row2,
		int col2
		)
	{
		JRBasePrintFrame frame = new JRBasePrintFrame(null);
		List<ElementWrapper> wrappers = new ArrayList<ElementWrapper>();

		frame.setWidth(xCuts.getCutOffset(col2) - xCuts.getCutOffset(col1));
		frame.setHeight(yCuts.getCutOffset(row2) - yCuts.getCutOffset(row1));

		String virtualAddress = (address == null ? "" : address + "_") + getNextVirtualFrameIndex();

		JRExporterGridCell gridCell =
			new ElementGridCell(
				new ElementWrapper(null, frame, virtualAddress),
				frame.getWidth(),
				frame.getHeight(),
				col2 - col1,
				row2 - row1
				);

		OccupiedGridCell occupiedGridCell = new OccupiedGridCell(gridCell);
		for(int row = row1; row < row2; row++)
		{
			for(int col = col1; col < col2; col++)
			{
				JRExporterGridCell gCell = grid[row][col];
				grid[row][col] = occupiedGridCell;
				ElementWrapper wrapper = gCell.getWrapper();
				if (gCell.getType() == JRExporterGridCell.TYPE_ELEMENT_CELL)
				{
					wrappers.add(wrapper);
					frame.addElement(wrapper.getElement());//FIXMEODT do we need this?
				}
			}
		}

		gridCell.setLayout(
			new JRGridLayout(
				nature,
				wrappers.toArray(new ElementWrapper[wrappers.size()]),
				frame.getWidth(),
				frame.getHeight(),
				offsetX -xCuts.getCutOffset(col1),
				offsetY -yCuts.getCutOffset(row1),
				virtualAddress
				)
			);

		grid[row1][col1] = gridCell;
	}


	/**
	 * Constructs the element grid.
	 */
	protected void layoutGrid(ElementWrapper[] wrappers)
	{

		boolean createXCuts = (xCuts == null);

		xCuts = createXCuts ? new CutsInfo() : xCuts;
		yCuts = nature.isIgnoreLastRow() ? new CutsInfo(0) : new CutsInfo(height);

		if(!isNested && nature.isIgnorePageMargins()) //FIXMEXLS left and right margins are not ignored when all pages on a single sheet
		{
			setMargins(wrappers);

			if(createXCuts)
			{
				List<Integer> xCutsList = xCuts.getCutOffsets();

				if(hasLeftMargin)
				{
					xCutsList.remove(Integer.valueOf(0));
				}
			}

			List<Integer> yCutsList = yCuts.getCutOffsets();

			if(hasTopMargin)
			{
				yCutsList.remove(Integer.valueOf(0));
			}
			if(hasBottomMargin)
			{
				yCutsList.remove(Integer.valueOf(height));
			}
		}

		createCuts(wrappers, offsetX, offsetY, createXCuts);

		// add a cut at the width if it's a nested grid, or if the right margin
		// is not to be removed and no element goes beyond the width
		if (createXCuts && (isNested
				|| (!(nature.isIgnorePageMargins() && hasRightMargin)
				&& !(xCuts.hasCuts() && xCuts.getLastCutOffset() >= width))))
		{
			xCuts.addCutOffset(width);
		}
		
		xCuts.use();
		yCuts.use();

		int colCount = Math.max(xCuts.size() - 1, 0);
		int rowCount = Math.max(yCuts.size() - 1, 0);

		grid = new JRExporterGridCell[rowCount][colCount];

		for(int row = 0; row < rowCount; row++)
		{
			for(int col = 0; col < colCount; col++)
			{
				grid[row][col] =
					new EmptyGridCell(
						xCuts.getCutOffset(col + 1) - xCuts.getCutOffset(col),
						yCuts.getCutOffset(row + 1) - yCuts.getCutOffset(row),
						1,
						1
						);
			}
		}

		setGridElements(wrappers,
				offsetX, offsetY,
				0, 0, rowCount, colCount);

		width = xCuts.getTotalLength();
		height = yCuts.getTotalLength();
	}

	protected void createCuts(ElementWrapper[] wrappers, int elementOffsetX, int elementOffsetY, boolean createXCuts)
	{
		for(int elementIndex = 0; elementIndex < wrappers.length; elementIndex++)
		{
			ElementWrapper wrapper = wrappers[elementIndex];
			JRPrintElement element = wrapper.getElement();

			if (nature.isToExport(element))
			{
				if (createXCuts)
				{
					xCuts.addCutOffset(element.getX() + elementOffsetX);
					xCuts.addCutOffset(element.getX() + element.getWidth() + elementOffsetX);
				}

				yCuts.addCutOffset(element.getY() + elementOffsetY);
				yCuts.addCutOffset(element.getY() + element.getHeight() + elementOffsetY);
				
				JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame)element : null;
				if (frame != null && nature.isDeep(frame))
				{
					createCuts(
						wrapper.getWrappers(),
						element.getX() + elementOffsetX + frame.getLineBox().getLeftPadding().intValue(),
						element.getY() + elementOffsetY + frame.getLineBox().getTopPadding().intValue(),
						createXCuts
						);
				}
			}
		}
	}

	protected void setMargins(ElementWrapper[] wrappers)
	{
		for(int elementIndex = 0; elementIndex < wrappers.length; elementIndex++)
		{
			ElementWrapper wrapper = wrappers[elementIndex];
			JRPrintElement element = wrapper.getElement();

			if (nature.isToExport(element))
			{
				if(hasLeftMargin && element.getX() <= 0)
				{
					hasLeftMargin = false;
				}

				if(hasRightMargin && element.getX() >= width - element.getWidth())
				{
					hasRightMargin = false;
				}

				if(hasTopMargin && element.getY() <= 0)
				{
					hasTopMargin = false;
				}

				if(hasBottomMargin && element.getY() >= height - element.getHeight())
				{
					hasBottomMargin = false;
				}

			}
		}
	}


	protected void setGridElements(ElementWrapper[] wrappers,
			int elementOffsetX, int elementOffsetY,
			int startRow, int startCol, int endRow, int endCol)
	{
		for(int elementIndex = wrappers.length - 1; elementIndex >= 0; elementIndex--)
		{
			ElementWrapper wrapper = wrappers[elementIndex];
			JRPrintElement element = wrapper.getElement();

			if (nature.isToExport(element))
			{
				int x = element.getX() + elementOffsetX;
				int y = element.getY() + elementOffsetY;

				int col1 = xCuts.indexOfCutOffset(x);
				int row1 = yCuts.indexOfCutOffset(y);
				int col2 = xCuts.indexOfCutOffset(x + element.getWidth());
				int row2 = yCuts.indexOfCutOffset(y + element.getHeight());

				if (!isOverlap(row1, col1, row2, col2))
				{
					JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame)element : null;
					if (frame != null && nature.isDeep(frame))
					{
						setGridElements(
							wrapper.getWrappers(),
							x + frame.getLineBox().getLeftPadding().intValue(),
							y + frame.getLineBox().getTopPadding().intValue(),
							row1, col1, row2, col2
							);
						
						setFrameCellsStyle(frame, row1, col1, row2, col2);
					}
					else
					{
						setGridElement(wrapper, row1, col1, row2, col2);
					}
				}
			}
		}

		if (nature.isHorizontallyMergeEmptyCells())
		{
			horizontallyMergeEmptyCells(startRow, startCol, endRow, endCol);
		}
	}

	protected void horizontallyMergeEmptyCells(int startRow, int startCol, int endRow, int endCol)
	{
		for (int row = startRow; row < endRow; ++row)
		{
			int startSpan = -1;
			int spanWidth = 0;
			int col = startCol;
			for (; col < endCol; ++col)
			{
				JRExporterGridCell cell = grid[row][col];
				if (cell.isEmpty())
				{
					if (startSpan == -1)
					{
						startSpan = col;
					}
					spanWidth += cell.getWidth();
				}
				else
				{
					if (startSpan != -1 && col - startSpan > 1)
					{
						JRExporterGridCell spanCell = grid[row][startSpan];
						spanCell.setColSpan(col - startSpan);
						spanCell.setWidth(spanWidth);
						//TODO set OCCUPIED_CELL?
					}
					startSpan = -1;
					spanWidth = 0;
				}
			}
			if (startSpan != -1 && col - startSpan > 1)
			{
				JRExporterGridCell spanCell = grid[row][startSpan];
				spanCell.setColSpan(col - startSpan);
				spanCell.setWidth(spanWidth);
			}
		}
	}

	protected boolean isOverlap(int row1, int col1, int row2, int col2)
	{
		boolean isOverlap = false;
		if (nature.isSpanCells())
		{
			is_overlap_out:
			for (int row = row1; row < row2; row++)
			{
				for (int col = col1; col < col2; col++)
				{
					if (!grid[row][col].isEmpty())
					{
						isOverlap = true;
						break is_overlap_out;
					}
				}
			}
		}
		else
		{
			isOverlap = grid[row1][col1].getWrapper() != null;
		}
		return isOverlap;
	}


	protected void setGridElement(ElementWrapper wrapper, int row1, int col1, int row2, int col2)
	{
		yCuts.addUsage(row1, Cut.USAGE_NOT_EMPTY);
		xCuts.addUsage(col1, Cut.USAGE_NOT_EMPTY);

		JRPrintElement element = wrapper.getElement();
		JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame) element : null;

		int rowSpan = nature.isSpanCells() ? row2 - row1 : 1;
		int colSpan = nature.isSpanCells() ? col2 - col1 : 1;

		JRExporterGridCell gridCell =
			new ElementGridCell(
				wrapper,
				element.getWidth(),
				element.getHeight(),
				colSpan,
				rowSpan
				);

		Boolean rowAutoFit = nature.getRowAutoFit(element);
		if (rowAutoFit != null)
		{
			yCuts.setAutoFit(row1, rowAutoFit.booleanValue());
		}
		Boolean columnAutoFit = nature.getColumnAutoFit(element);
		if (columnAutoFit != null)
		{
			xCuts.setAutoFit(col1, columnAutoFit.booleanValue());
		}

		Integer columnCustomWidth = nature.getCustomColumnWidth(element);
		if (columnCustomWidth != null && (xCuts.getCustomWidth(col1) == null || xCuts.getCustomWidth(col1) < columnCustomWidth))
		{
			xCuts.setCustomWidth(col1, columnCustomWidth);
		}

		Float widthRatio = nature.getColumnWidthRatio(element);
		if(widthRatio != null && (xCuts.getWidthRatio() == null || xCuts.getWidthRatio() < widthRatio))
		{
			xCuts.setWidthRatio(widthRatio);
		}

		List<JRProperties.PropertySuffix> rowLevelSuffixes = nature.getRowLevelSuffixes(element);
		if(rowLevelSuffixes != null && !rowLevelSuffixes.isEmpty())
		{
			SortedMap<String, Boolean> levelMap = new TreeMap<String, Boolean>();
			for(JRProperties.PropertySuffix suffix : rowLevelSuffixes)
			{
				String level = suffix.getSuffix();
				String marker = suffix.getValue();
				
				levelMap.put(level, "end".equalsIgnoreCase(marker));
			}
			yCuts.setRowLevelMap(row1, levelMap);
		}
		

		if (nature.isSpanCells())
		{
			OccupiedGridCell occupiedGridCell = new OccupiedGridCell(gridCell);
			for (int row = row1; row < row2; row++)
			{
				for (int col = col1; col < col2; col++)
				{
					grid[row][col] = occupiedGridCell;
				}
				yCuts.addUsage(row, Cut.USAGE_SPANNED);
			}

			for (int col = col1; col < col2; col++)
			{
				xCuts.addUsage(col, Cut.USAGE_SPANNED);
			}
		}

		if (col2 - col1 != 0 && row2 - row1 != 0)
		{
			if (frame != null)//FIXMEODT if deep, does this make sense?
			{
				gridCell.setLayout(
					new JRGridLayout(
						nature,
						wrapper.getWrappers(),
						frame.getWidth(),
						frame.getHeight(),
						0, //offsetX
						0, //offsetY
						wrapper.getAddress()
						)
					);
			}

			gridCell.setBox((element instanceof JRBoxContainer)?((JRBoxContainer)element).getLineBox():null);

			if (nature.isBreakBeforeRow(element))
			{
				yCuts.addUsage(row1,  Cut.USAGE_BREAK);
			}
			if (nature.isBreakAfterRow(element))
			{
				yCuts.addUsage(row1 + rowSpan,  Cut.USAGE_BREAK);
			}

			grid[row1][col1] = gridCell;
		}
	}


	protected void setFrameCellsStyle(JRPrintFrame frame, int row1, int col1, int row2, int col2)
	{
		Color backcolor = frame.getModeValue() == ModeEnum.OPAQUE ? frame.getBackcolor() : null;

		for (int row = row1; row < row2; row++)
		{
			for (int col = col1; col < col2; col++)
			{
				JRExporterGridCell cell = grid[row][col];

				if (cell.getBackcolor() == null)
				{
					if (frame.getModeValue() == ModeEnum.OPAQUE)
					{
						cell.setBackcolor(backcolor);
					}
				}

				if (cell.getForecolor() == null)
				{
					cell.setForecolor(frame.getForecolor());
				}

				boolean keepLeft = col == col1;
				boolean keepRight = col == col2 - cell.getColSpan();
				boolean keepTop = row == row1;
				boolean keepBottom = row == row2 - cell.getRowSpan();

				if (keepLeft || keepRight || keepTop || keepBottom)
				{
					JRLineBox cellBox = cell.getBox();
					BoxKey key = new BoxKey(frame.getLineBox(), cellBox, keepLeft, keepRight, keepTop, keepBottom);
					JRLineBox modBox = boxesCache.get(key);
					if (modBox == null)
					{
						modBox = JRBoxUtil.copyBordersNoPadding(frame.getLineBox(), keepLeft, keepRight, keepTop, keepBottom, cellBox);
						boxesCache.put(key, modBox);
					}

					cell.setBox(modBox);
				}
			}
		}
	}

	private void splitSharedRowSpanIntoNestedGrids()
	{
		for(int row = 0; row < grid.length;)
		{
			int rowSpan = getSharedRowSpan(row);
			//negative row span means it is not shared row span
			if (rowSpan > 0)
			{
				splitSharedColSpanIntoNestedGrids(row, row + rowSpan);
			}
			row += Math.abs(rowSpan);
		}
	}

	private void splitSharedColSpanIntoNestedGrids(int row1, int row2)
	{
		for(int col = 0; col < grid[row1].length;)
		{
			int colSpan = getSharedColSpan(row1, row2, col);
			//negative col span means it is not shared col span
			if (colSpan > 0)
			{
				if (
					!(row1 == 0
					&& row2 == grid.length
					&& col == 0
					&& colSpan == grid[0].length)
					)
				{
					this.createNestedGrid(
						row1,
						col,
						row2,
						col + colSpan
						);
				}
			}
			col += Math.abs(colSpan);
		}
	}

	private int getSharedRowSpan(int row1)
	{
		int rowSpan = 1;
		int sharedSpanCount = 0;

		for(int row = 0; row < rowSpan; row++)
		{
			for(int col = 0; col < grid[0].length; col++)
			{
				JRExporterGridCell gridCell = grid[row1 + row][col];
				if (row + gridCell.getRowSpan() > rowSpan)
				{
					sharedSpanCount++;
					rowSpan = row + gridCell.getRowSpan();
				}
			}
		}

		// we have "shared row span" only if at least two merged cells share at least one row;
		// negative row span is used to skip row span that is not shared
		return sharedSpanCount > 1 ? rowSpan : -rowSpan;
	}

	private int getSharedColSpan(int row1, int row2, int col1)
	{
		int colSpan = 1;
		boolean isSharedSpan = false;

		for(int col = 0; col < colSpan; col++)
		{
			for(int row = row1; row < row2; row++)
			{
				JRExporterGridCell gridCell = grid[row][col1 + col];
				if (col + gridCell.getColSpan() > colSpan)
				{
					isSharedSpan = true;
					colSpan = col + gridCell.getColSpan();
				}
				else if (gridCell.getRowSpan() > 1)
				{
					isSharedSpan = true;
				}
			}
		}

		// we have "shared col span" only if at least two merged cells share at least one col;
		// negative col span is used to skip col span that is not shared
		return isSharedSpan ? colSpan : -colSpan;
	}

	/**
	 * Returns the constructed element grid.
	 *
	 * @return the constructed element grid
	 */
	public JRExporterGridCell[][] getGrid()
	{
		return grid;
	}


	/**
	 * Returns the list of cut points on the X axis for the grid.
	 *
	 * @return the list of cut points on the X axis for the grid
	 */
	public CutsInfo getXCuts()
	{
		return xCuts;
	}


	/**
	 * Returns the list of cut points on the Y axis for the grid.
	 *
	 * @return the list of cut points on the Y axis for the grid
	 */
	public CutsInfo getYCuts()
	{
		return yCuts;
	}


	/**
	 * Returns the width available for the grid.
	 *
	 * @return the width available for the grid
	 */
	public int getWidth()
	{
		return width;
	}


	public int getColumnWidth(int col)
	{
		return xCuts.getCutOffset(col + 1) - xCuts.getCutOffset(col);
	}


	public int getRowHeight(int row)
	{
		return yCuts.getCutOffset(row + 1) - yCuts.getCutOffset(row);
	}


	public int getMaxRowHeight(int rowIndex)
	{
		JRExporterGridCell[] row = grid[rowIndex];
		int maxRowHeight = row[0].getHeight();
		for (int col = 0; col < row.length; col++)
		{
			JRExporterGridCell cell = row[col];

			if (cell.getType() != JRExporterGridCell.TYPE_OCCUPIED_CELL)
			{
				if (maxRowHeight < cell.getHeight())
				{
					maxRowHeight = cell.getHeight();
				}
			}
		}
		return maxRowHeight;
	}


	public static int getRowHeight(JRExporterGridCell[] row)//FIXMEODT are we still using this?
	{
		if (row[0].getRowSpan() == 1 && row[0].getType() != JRExporterGridCell.TYPE_OCCUPIED_CELL) //quick exit
		{
			return row[0].getHeight();
		}

		int rowHeight = 0;
		int minSpanIdx = 0;

		int colCount = row.length;

		int col;
		for (col = 0; col < colCount; col++)
		{
			JRExporterGridCell cell = row[col];

			if (cell.getType() != JRExporterGridCell.TYPE_OCCUPIED_CELL)
			{
				if (cell.getRowSpan() == 1)
				{
					rowHeight = cell.getHeight();
					break;
				}

				if (cell.getRowSpan() < row[minSpanIdx].getRowSpan())
				{
					minSpanIdx = col;
				}
			}
		}

		if (col >= colCount) //no cell with rowSpan = 1 was found, getting the height of the cell with min rowSpan
		{
			rowHeight = row[minSpanIdx].getHeight();
		}

		return rowHeight;
	}


	/**
	 * This static method calculates all the X cuts for a list of pages.
	 *
	 * @param pages
	 *            The list of pages.
	 * @param startPageIndex
	 *            The first page to consider.
	 * @param endPageIndex
	 *            The last page to consider.
	 * @param width
	 *            The page width
	 * @param offsetX
	 *            horizontal element position offset
	 */
	public static CutsInfo calculateXCuts(ExporterNature nature, List<JRPrintPage> pages, int startPageIndex, int endPageIndex, int width, int offsetX)
	{
		CutsInfo xCuts = new CutsInfo();

		for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
		{
			JRPrintPage page = pages.get(pageIndex);
			addXCuts(nature, page.getElements(), offsetX, xCuts);
		}
		
		// add a cut at the page width if no element goes beyond the width
		int lastCut = xCuts.getLastCutOffset();
		if (lastCut < width)
		{
			xCuts.addCutOffset(width);
		}

		return xCuts;
	}

	/**
	 * This static method calculates the X cuts for a list of print elements and
	 * stores them in the list indicated by the xCuts parameter.
	 *
	 * @param elementsList
	 *            The list of elements to be used to determine the X cuts.
	 * @param elementOffsetX
	 *            horizontal element position offset
	 * @param xCuts
	 *            The list to which the X cuts are to be added.
	 */
	protected static void addXCuts(ExporterNature nature, List<JRPrintElement> elementsList, int elementOffsetX, CutsInfo xCuts)
	{
		for (Iterator<JRPrintElement> it = elementsList.iterator(); it.hasNext();)
		{
			JRPrintElement element = it.next();

			if (nature.isToExport(element))
			{
				xCuts.addCutOffset(element.getX() + elementOffsetX);
				xCuts.addCutOffset(element.getX() + element.getWidth() + elementOffsetX);
				
				if (element instanceof JRPrintFrame)
				{
					JRPrintFrame frame = (JRPrintFrame) element;
					addXCuts(
						nature,
						frame.getElements(),
						element.getX() + elementOffsetX + frame.getLineBox().getLeftPadding().intValue(),
						xCuts
						);
				}
			}
		}
	}

	/**
	 *
	 */
	protected int getNextVirtualFrameIndex()
	{
		return virtualFrameIndex++;
	}


	/**
	 *
	 */
	private static ElementWrapper[] createWrappers(ElementWrapper parentWrapper, List<JRPrintElement> elementsList, String parentAddress)
	{
		ElementWrapper[] wrappers = new ElementWrapper[elementsList.size()];

		for (int elementIndex = 0; elementIndex < elementsList.size(); elementIndex++)
		{
			JRPrintElement element = elementsList.get(elementIndex);

			String address = (parentAddress == null ? "" : parentAddress + "_") + elementIndex;

			ElementWrapper wrapper = 
				new ElementWrapper(
					parentWrapper,
					element,
					address
					);
			
			if (element instanceof JRPrintFrame)
			{
				wrapper.setWrappers(createWrappers(wrapper, ((JRPrintFrame)element).getElements(), address));
			}

			wrappers[elementIndex] = wrapper;
		}

		return wrappers;
	}
	
	
	/**
	 *
	 */
	protected static class BoxKey
	{
		final JRLineBox box;
		final JRLineBox cellBox;
		final boolean left;
		final boolean right;
		final boolean top;
		final boolean bottom;
		final int hashCode;

		BoxKey(JRLineBox box, JRLineBox cellBox, boolean left, boolean right, boolean top, boolean bottom)
		{
			this.box = box;
			this.cellBox = cellBox;
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;

			int hash = box.hashCode();
			if (cellBox != null)
			{
				hash = 31*hash + cellBox.hashCode();
			}
			hash = 31*hash + (left ? 1231 : 1237);
			hash = 31*hash + (right ? 1231 : 1237);
			hash = 31*hash + (top ? 1231 : 1237);
			hash = 31*hash + (bottom ? 1231 : 1237);
			hashCode = hash;
		}

		public boolean equals(Object obj)
		{
			if (obj == this)
			{
				return true;
			}

			BoxKey b = (BoxKey) obj;

			return b.box.equals(box) &&
				(b.cellBox == null ? cellBox == null : (cellBox != null && b.cellBox.equals(cellBox))) &&
				b.left == left && b.right == right && b.top == top && b.bottom == bottom;
		}

		public int hashCode()
		{
			return hashCode;
		}
	}

}