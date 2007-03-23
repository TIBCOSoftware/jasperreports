/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Greg Hilton 
 */

package net.sf.jasperreports.engine.export;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.base.JRBaseBox;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRGridLayout
{
	
	public static final ExporterNature UNIVERSAL_EXPORTER = new ExporterNature()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return true;
			}	
		};
		
	public static final ExporterNature NO_IMAGES_EXPORTER = new ExporterNature()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return !(element instanceof JRPrintImage);
			}
		};
			
	public static final ExporterNature TEXT_EXPORTER = new ExporterNature()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return element instanceof JRPrintText;
			}
		};
	
	private final int width;
	private final int height;
	private final int offsetX;
	private final int offsetY;
	private final ExporterNature nature;
	private final boolean deep;
	private final boolean splitSharedRowSpan;
	private final boolean spanCells;
	private final String address;
	
	private List xCuts;
	private List yCuts;
	private JRExporterGridCell[][] grid;
	private boolean[] isRowNotEmpty;
	private boolean[] isColNotEmpty;
	
	private Map boxesCache;

	private int virtualFrameIndex = 0;

	
	/**
	 * Constructor.
	 * 
	 * @param elements the elements that should arranged in a grid
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 * @param elementsExporter implementation of {@link ExporterElements ExporterElements} used to decide which
	 * elements to skip during grid creation
	 * @param deep whether to include in the grid sub elements of {@link JRPrintFrame frame} elements
	 * @param spanCells whether the exporter handles cells span
	 */
	public JRGridLayout(
		List elements, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY, 
		ExporterNature nature, 
		boolean deep,
		boolean splitSharedRowSpan,
		boolean spanCells
		)
	{
		this(
			elements,
			width, 
			height, 
			offsetX, 
			offsetY,
			nature,
			deep, 
			splitSharedRowSpan,
			spanCells,
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
	 * @param elementsExporter implementation of {@link ExporterElements ExporterElements} used to decide which
	 * elements to skip during grid creation
	 * @param deep whether to include in the grid sub elements of {@link JRPrintFrame frame} elements
	 * @param spanCells whether the exporter handles cells span
	 * @param xCuts An optional list of pre-calculated X cuts.
	 */
	public JRGridLayout(
		List elements, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY, 
		ExporterNature nature, 
		boolean deep, 
		boolean splitSharedRowSpan,
		boolean spanCells,
		List xCuts
		)
	{
		this.height = height;
		this.width = width;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.nature = nature;
		this.deep = deep;
		this.splitSharedRowSpan = splitSharedRowSpan;
		this.spanCells = spanCells;
		this.address = null;
		this.xCuts = xCuts;
		
		boxesCache = new HashMap();
		
		virtualFrameIndex = elements.size();

		layoutGrid(createWrappers(elements, address));
		
		if (splitSharedRowSpan)
		{
			splitSharedRowSpanIntoNestedGrids();
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param elements the elements that should arranged in a grid
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 * @param elementsExporter implementation of {@link ExporterElements ExporterElements} used to decide which
	 * elements to skip during grid creation
	 * @param deep whether to include in the grid sub elements of {@link JRPrintFrame frame} elements
	 * @param spanCells whether the exporter handles cells span
	 * @param address element address
	 * @param xCuts An optional list of pre-calculated X cuts.
	 */
	private JRGridLayout(
		ElementWrapper[] wrappers, 
		int width, 
		int height, 
		int offsetX, 
		int offsetY, 
		ExporterNature nature, 
		boolean deep, 
		boolean splitSharedRowSpan,
		boolean spanCells,
		String address,
		List xCuts
		)
	{
		this.height = height;
		this.width = width;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.nature = nature;
		this.deep = deep;
		this.splitSharedRowSpan = splitSharedRowSpan;
		this.spanCells = spanCells;
		this.address = address;
		this.xCuts = xCuts;
		
		boxesCache = new HashMap();
		
		layoutGrid(wrappers);
		
		if (splitSharedRowSpan)
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
		List wrappers = new ArrayList();
		
		for(int row = row1; row < row2; row++)
		{
			for(int col = col1; col < col2; col++)
			{
				JRExporterGridCell gridCell = grid[row][col];
				grid[row][col] = JRExporterGridCell.OCCUPIED_CELL;
				ElementWrapper wrapper = gridCell.getWrapper();
				if (gridCell != JRExporterGridCell.OCCUPIED_CELL && wrapper != null)
				{
					wrappers.add(wrapper);
					frame.addElement(wrapper.getElement());//FIXMEODT do we need this?
				}
			}
		}
		
		frame.setWidth(
			((Integer)xCuts.get(col2)).intValue() 
			- ((Integer)xCuts.get(col1)).intValue()
			);
		frame.setHeight(
			((Integer)yCuts.get(row2)).intValue() 
			- ((Integer)yCuts.get(row1)).intValue()
			);
		
		String virtualAddress = (address == null ? "" : address + "_") + getNextVirtualFrameIndex(); 
		
		JRExporterGridCell gridCell = 
			new JRExporterGridCell(
				new ElementWrapper(frame, virtualAddress, null),
				frame.getWidth(), 
				frame.getHeight(), 
				col2 - col1, 
				row2 - row1
				);
		
		gridCell.setLayout(
			new JRGridLayout(
				(ElementWrapper[]) wrappers.toArray(new ElementWrapper[wrappers.size()]), 
				frame.getWidth(), 
				frame.getHeight(), 
				offsetX -((Integer)xCuts.get(col1)).intValue(),
				offsetY -((Integer)yCuts.get(row1)).intValue(),
				nature, 
				false, //deep 
				splitSharedRowSpan,
				true, //spanCells
				virtualAddress,
				null
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
		if (createXCuts)
		{
			xCuts = new SortedList();
			xCuts.add(new Integer(0));
		}
		
		yCuts = new SortedList();
		yCuts.add(new Integer(0));

		createCuts(wrappers, offsetX, offsetY, createXCuts);
		
		xCuts.add(new Integer(width));
		yCuts.add(new Integer(height));
			
		int colCount = xCuts.size() - 1;
		int rowCount = yCuts.size() - 1;

		grid = new JRExporterGridCell[rowCount][colCount];
		isRowNotEmpty = new boolean[rowCount];
		isColNotEmpty = new boolean[colCount];
				
		for(int row = 0; row < rowCount; row++)
		{ 
			for(int col = 0; col < colCount; col++)
			{
				grid[row][col] = 
					new JRExporterGridCell(
						null,
						((Integer)xCuts.get(col + 1)).intValue() - ((Integer)xCuts.get(col)).intValue(),
						((Integer)yCuts.get(row + 1)).intValue() - ((Integer)yCuts.get(row)).intValue(),
						1,
						1
						);
			}
		}

		setGridElements(wrappers, offsetX, offsetY);
	}


	protected void createCuts(ElementWrapper[] wrappers, int elementOffsetX, int elementOffsetY, boolean createXCuts)
	{
		for(int elementIndex = 0; elementIndex < wrappers.length; elementIndex++)
		{
			ElementWrapper wrapper = wrappers[elementIndex];
			JRPrintElement element = wrapper.getElement();
			
			int x = element.getX() + elementOffsetX;
			int y = element.getY() + elementOffsetY;
			
			if (nature.isToExport(element))
			{
				if (createXCuts)
				{
					xCuts.add(new Integer(x));
					xCuts.add(new Integer((x + element.getWidth())));
				}
				
				yCuts.add(new Integer(y));
				yCuts.add(new Integer((y + element.getHeight())));	
			}
			
			JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame) element : null;
			
			if (deep && frame != null)
			{
				createCuts(
					wrapper.getWrappers(), 
					x + frame.getLeftPadding(), 
					y + frame.getTopPadding(), 
					createXCuts
					);
			}
		}
	}


	protected void setGridElements(ElementWrapper[] wrappers, int elementOffsetX, int elementOffsetY)
	{
		for(int elementIndex = wrappers.length - 1; elementIndex >= 0; elementIndex--)
		{
			ElementWrapper wrapper = wrappers[elementIndex];
			JRPrintElement element = wrapper.getElement();

			boolean toExport = nature.isToExport(element);
			//JRPrintFrame frame = deep && element instanceof JRPrintFrame ? (JRPrintFrame) element : null;
			JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame) element : null;
			
			if (toExport || frame != null)
			{
				int x = element.getX() + elementOffsetX;
				int y = element.getY() + elementOffsetY;
				
				if (deep && frame != null)
				{
					setGridElements(
						wrapper.getWrappers(), 
						x + frame.getLeftPadding(), 
						y + frame.getTopPadding()
						);
				}

				if (toExport)
				{
					int col1 = xCuts.indexOf(new Integer(x));
					int row1 = yCuts.indexOf(new Integer(y));
					int col2 = xCuts.indexOf(new Integer(x + element.getWidth()));
					int row2 = yCuts.indexOf(new Integer(y + element.getHeight()));

					if (!isOverlap(row1, col1, row2, col2))
					{
						setGridElement(wrapper, row1, col1, row2, col2);
					}

					if (deep && frame != null)
					{
						setFrameCellsStyle(frame, row1, col1, row2, col2);
					}
				}
			}
		}
	}


	protected boolean isOverlap(int row1, int col1, int row2, int col2)
	{
		boolean isOverlap = false;
		if (spanCells)
		{
			is_overlap_out:
			for (int row = row1; row < row2; row++)
			{
				for (int col = col1; col < col2; col++)
				{
					if (grid[row][col].getWrapper() != null)
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
		if (spanCells)
		{
			for (int row = row1; row < row2; row++)
			{
				for (int col = col1; col < col2; col++)
				{
					grid[row][col] = JRExporterGridCell.OCCUPIED_CELL;
				}
				isRowNotEmpty[row] = true;
			}

			for (int col = col1; col < col2; col++)
			{
				isColNotEmpty[col] = true;
			}
		}
		else
		{
			isRowNotEmpty[row1] = true;
			isColNotEmpty[col1] = true;
		}

		if (col2 - col1 != 0 && row2 - row1 != 0)
		{
			JRPrintElement element = wrapper.getElement();
			JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame) element : null;
			
			JRExporterGridCell gridCell = 
				new JRExporterGridCell(
					wrapper,
					element.getWidth(), 
					element.getHeight(), 
					col2 - col1, 
					row2 - row1
					);
			
			if (frame != null)//FIXMEODT if deep, does this make sense?
			{
				gridCell.setLayout(
					new JRGridLayout(
						wrapper.getWrappers(), 
						frame.getWidth(), 
						frame.getHeight(), 
						0, //offsetX
						0, //offsetY
						nature, 
						false, //deep 
						splitSharedRowSpan,
						true, //spanCells
						wrapper.getAddress(),
						null
						)
					);
			}
			
			JRBox cellBox = null;
			if (element instanceof JRBox)
			{
				cellBox = (JRBox) element;
			}
			
			gridCell.setBox(cellBox);

			grid[row1][col1] = gridCell;
		}
	}


	protected void setFrameCellsStyle(JRPrintFrame frame, int row1, int col1, int row2, int col2)
	{
		Color backcolor = frame.getMode() == JRElement.MODE_OPAQUE ? frame.getBackcolor() : null;
		
		for (int row = row1; row < row2; row++)
		{	
			for (int col = col1; col < col2; col++)
			{
				JRExporterGridCell cell = grid[row][col];
				
				if (cell.getBackcolor() == null)
				{
					if (frame.getMode() == JRElement.MODE_OPAQUE)
					{
						cell.setBackcolor(backcolor);
					}
				}
				
				if (cell.getForecolor() == null)
				{
					cell.setForecolor(frame.getForecolor());
				}
				
				boolean left = col == col1;
				boolean right = col == col2 - cell.getColSpan();
				boolean top = row == row1;
				boolean bottom = row == row2 - cell.getRowSpan();
					
				if (left || right || top || bottom)
				{
					JRBox cellBox = cell.getBox();
					Object key = new BoxKey(frame, cellBox, left, right, top, bottom);
					JRBox modBox = (JRBox) boxesCache.get(key);
					if (modBox == null)
					{
						modBox = new JRBaseBox(frame, left, right, top, bottom, cellBox);
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
	 * Returns an array containing for each grid row a flag set to true if the row is not empty.
	 * 
	 * @return array of non empty flags for grid rows
	 */
	public boolean[] getIsRowNotEmpty()
	{
		return isRowNotEmpty;
	}

	
	/**
	 * Decides whether a row is empty or not.
	 * 
	 * @param rowIdx the row index
	 * @return <code>true</code> iff the row is not empty
	 */
	public boolean isRowNotEmpty(int rowIdx)
	{
		return isRowNotEmpty[rowIdx];
	}

	/**
	 * Returns an array containing for each grid column a flag set to true if the column is not empty.
	 * 
	 * @return array of non empty flags for grid columns
	 */
	public boolean[] getIsColumnNotEmpty()
	{
		return isColNotEmpty;
	}


	/**
	 * Returns the list of cut points on the X axis for the grid.
	 * 
	 * @return the list of cut points on the X axis for the grid
	 */
	public List getXCuts()
	{
		return xCuts;
	}


	/**
	 * Returns the list of cut points on the Y axis for the grid.
	 * 
	 * @return the list of cut points on the X axis for the grid
	 */
	public List getYCuts()
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
	
	
	public int getRowHeight(int row)
	{
		return ((Integer)yCuts.get(row + 1)).intValue() - ((Integer)yCuts.get(row)).intValue();
	}
	
	
	public static int getRowHeight(JRExporterGridCell[] row)//FIXMEODT are we still using this?
	{
		if (row[0].getRowSpan() == 1 && row[0] != JRExporterGridCell.OCCUPIED_CELL) //quick exit
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
			
			if (cell != JRExporterGridCell.OCCUPIED_CELL)
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
	 * @param offsetX
	 *            horizontal element position offset
	 * @param elementsExporter
	 *            implementation of {@link ExporterElements ExporterElements}
	 *            used to decide which elements to skip during grid creation
	 */
	public static List calculateXCuts(List pages, int startPageIndex, int endPageIndex, int offsetX, ExporterNature nature)
	{
		List xCuts = new SortedList();
		for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++)
		{
			JRPrintPage page = (JRPrintPage) pages.get(pageIndex);
			addXCuts(page.getElements(), offsetX, nature, xCuts);
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
	 * @param elementsExporter
	 *            implementation of {@link ExporterElements ExporterElements}
	 *            used to decide which elements to skip during grid creation
	 * @param xCuts
	 *            The list to which the X cuts are to be added.
	 */
	protected static void addXCuts(List elementsList, int elementOffsetX, ExporterNature nature, List xCuts)
	{
		for (Iterator it = elementsList.iterator(); it.hasNext();)
		{
			JRPrintElement element = ((JRPrintElement) it.next());
			int x = element.getX() + elementOffsetX;

			if (nature.isToExport(element))
			{
				xCuts.add(new Integer(x));
				xCuts.add(new Integer(x + element.getWidth()));
			}

			if (element instanceof JRPrintFrame)
			{
				JRPrintFrame frame = (JRPrintFrame) element;
				addXCuts(frame.getElements(), x + frame.getLeftPadding(), nature, xCuts);
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
	private static ElementWrapper[] createWrappers(List elementsList, String parentAddress)
	{
		ElementWrapper[] wrappers = new ElementWrapper[elementsList.size()];

		for (int elementIndex = 0; elementIndex < elementsList.size(); elementIndex++)
		{
			JRPrintElement element = ((JRPrintElement) elementsList.get(elementIndex));
			
			String address = (parentAddress == null ? "" : parentAddress + "_") + elementIndex;

			wrappers[elementIndex] = 
				new ElementWrapper(
					element, 
					address,
					element instanceof JRPrintFrame
						? createWrappers(((JRPrintFrame)element).getElements(), address)
						: null
					);
		}
		
		return wrappers;
	}
	
	public static interface ExporterNature
	{
		boolean isToExport(JRPrintElement element);
	}
	
	protected static class SortedList extends ArrayList
	{
		private static final long serialVersionUID = 6232843428269907513L;
		
		public boolean add(Object o)
		{
			int idx = Collections.binarySearch(this, o);
			
			if (idx >= 0)
			{
				return false;
			}
			
			add(-idx - 1, o);
			return true;
		}
		
		public int indexOf(Object o)
		{
			int idx = Collections.binarySearch(this, o);
			
			if (idx < 0)
			{
				idx = -1;
			}
			
			return idx;
		}
	}
	
	protected static class BoxKey
	{
		final JRBox box;
		final JRBox cellBox;
		final boolean left;
		final boolean right;
		final boolean top;
		final boolean bottom;
		final int hashCode;
		
		BoxKey(JRBox box, JRBox cellBox, boolean left, boolean right, boolean top, boolean bottom)
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
