/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
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
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.base.JRBaseBox;

/**
 * Utility class used by grid exporters to create a grid for page layout.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRGridLayout
{
	
	public static interface ExporterElements
	{
		boolean isToExport(JRPrintElement element);
	}
	
	public static final ExporterElements UNIVERSAL_EXPORTER = new ExporterElements()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return true;
			}	
		};
		
	public static final ExporterElements NO_IMAGES_EXPORTER = new ExporterElements()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return !(element instanceof JRPrintImage);
			}
		};
			
	public static final ExporterElements TEXT_EXPORTER = new ExporterElements()
		{
			public boolean isToExport(JRPrintElement element)
			{
				return element instanceof JRPrintText;
			}
		};
	
	private final List elements;
	private final List alterYs;
	private final int width;
	private final int height;
	private final int offsetX;
	private final int offsetY;
	private final ExporterElements elementsExporter;
	private final boolean deep;
	private final boolean spanCells;
	private final boolean setElementIndexes;
	private final Integer[] initialIndex;
	
	private List xCuts;
	private List yCuts;
	private JRExporterGridCell[][] grid;
	private boolean[] isRowNotEmpty;
	private boolean[] isColNotEmpty;
	
	private Map boxesCache;
	
	/**
	 * Constructor.
	 * 
	 * @param elements the elements that should arranged in a grid
	 * @param alterYs list of modified Y element coordinates
	 * @param width the width available for the grid
	 * @param height the height available for the grid
	 * @param offsetX horizontal element position offset
	 * @param offsetY vertical element position offset
	 * @param elementsExporter implementation of {@link ExporterElements ExporterElements} used to decide which
	 * elements to skip during grid creation
	 * @param deep whether to include in the grid sub elements of {@link JRPrintFrame frame} elements
	 * @param spanCells whether the exporter handles cells span
	 * @param setElementIndexes whether to set element indexes
	 * @param initialIndex initial element index
	 */
	public JRGridLayout(List elements, List alterYs, 
			int width, int height, int offsetX, int offsetY, 
			ExporterElements elementsExporter, 
			boolean deep, boolean spanCells,
			boolean setElementIndexes, Integer[] initialIndex)
	{
		this.elements = elements;
		this.alterYs = alterYs;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.elementsExporter = elementsExporter;
		this.deep = deep;
		this.spanCells = spanCells;
		this.setElementIndexes = setElementIndexes;
		this.initialIndex = initialIndex;
		
		boxesCache = new HashMap();

		layoutGrid();
	}


	/**
	 * Constructs the element grid.
	 */
	protected void layoutGrid()
	{
		xCuts = new SortedList();
		yCuts = new SortedList();
		
		xCuts.add(new Integer(0));
		yCuts.add(new Integer(0));

		createCuts(elements, alterYs, offsetX, offsetY);
		
		xCuts.add(new Integer(width));
		yCuts.add(new Integer(height));
			
/*		Collections.sort(xCuts);
		Collections.sort(yCuts);
*/		
		
		int xCellCount = xCuts.size() - 1;
		int yCellCount = yCuts.size() - 1;

		grid = new JRExporterGridCell[yCellCount][xCellCount];
		isRowNotEmpty = new boolean[yCellCount];
		isColNotEmpty = new boolean[xCellCount];
				
		for(int j = 0; j < yCellCount; j++)
		{ 
			for(int i = 0; i < xCellCount; i++)
			{
				grid[j][i] = 
					new JRExporterGridCell(
						null,
						null,
						((Integer)xCuts.get(i + 1)).intValue() - ((Integer)xCuts.get(i)).intValue(),
						((Integer)yCuts.get(j + 1)).intValue() - ((Integer)yCuts.get(j)).intValue(),
						1,
						1
						);
			}
		}

		setGridElements(elements, alterYs, offsetX, offsetY, initialIndex);
	}


	protected void createCuts(List elementsList, List alterYList, int elementOffsetX, int elementOffsetY)
	{
		Iterator alterYIt = alterYList == null ? null : alterYList.iterator();
		for(Iterator it = elementsList.iterator(); it.hasNext();)
		{
			JRPrintElement element = ((JRPrintElement)it.next());
			Integer alterY = alterYIt == null ? null : (Integer) alterYIt.next();
			
			int x0 = element.getX() + elementOffsetX;
			int elementY = alterY == null ? element.getY() : alterY.intValue();
			int y0 = elementY + elementOffsetY;
			
			if (elementsExporter.isToExport(element))
			{
				xCuts.add(new Integer(x0));
				xCuts.add(new Integer((x0 + element.getWidth())));
				
				yCuts.add(new Integer(y0));
				yCuts.add(new Integer((y0 + element.getHeight())));	
			}
			
			if (deep && element instanceof JRPrintFrame)
			{
				createFrameCuts((JRPrintFrame) element, x0, y0);
			}
		}
	}


	protected void createFrameCuts(JRPrintFrame frame, int x0, int y0)
	{
		int topPadding = frame.getTopPadding();
		int leftPadding = frame.getLeftPadding();

		createCuts(frame.getElements(), null, x0 + leftPadding, y0 + topPadding);
	}


	protected void setGridElements(List elementsList, List alterYList, int elementOffsetX, int elementOffsetY, Integer[] parentIndexes)
	{
		for(int i = elementsList.size() - 1; i >= 0; i--)
		{
			JRPrintElement element = (JRPrintElement)elementsList.get(i);

			boolean toExport = elementsExporter.isToExport(element);
			JRPrintFrame frame = deep && element instanceof JRPrintFrame ? (JRPrintFrame) element : null;
			
			if (toExport || frame != null)
			{
				int elementY = alterYList == null ? element.getY() : ((Integer) alterYList.get(i)).intValue();
				int x0 = element.getX() + elementOffsetX;
				int y0 = elementY + elementOffsetY;
				
				if (frame != null)
				{
					setFrameGridElements(frame, x0, y0, i, parentIndexes);
				}

				if (toExport)
				{
					int x1 = xCuts.indexOf(new Integer(x0));
					int y1 = yCuts.indexOf(new Integer(y0));
					int x2 = xCuts.indexOf(new Integer(x0 + element.getWidth()));
					int y2 = yCuts.indexOf(new Integer(y0 + element.getHeight()));

					if (!isOverlap(x1, y1, x2, y2))
					{
						setGridElement(element, x1, y1, x2, y2, i, parentIndexes);
					}

					if (frame != null)
					{
						setFrameCellsStyle(frame, x1, x2, y1, y2);
					}
				}
			}
		}
	}


	protected boolean isOverlap(int x1, int y1, int x2, int y2)
	{
		boolean isOverlap;
		if (spanCells)
		{
			isOverlap = false;

			for (int yi = y1; yi < y2 && !isOverlap; ++yi)
			{
				for (int xi = x1; xi < x2 && !isOverlap; ++xi)
				{
					if (grid[yi][xi].element != null)
					{
						isOverlap = true;
					}
				}
			}
		}
		else
		{
			isOverlap = grid[y1][x1].element != null;
		}
		return isOverlap;
	}


	protected void setGridElement(JRPrintElement element, int x1, int y1, int x2, int y2, int elementIndex, Integer[] parentElements)
	{
		if (spanCells)
		{
			for (int yi = y1; yi < y2; ++yi)
			{
				for (int xi = x1; xi < x2; ++xi)
				{
					grid[yi][xi] = JRExporterGridCell.OCCUPIED_CELL;
				}
				isRowNotEmpty[yi] = true;
			}

			for (int xi = x1; xi < x2; ++xi)
			{
				isColNotEmpty[xi] = true;
			}
		}
		else
		{
			isRowNotEmpty[y1] = true;
			isColNotEmpty[x1] = true;
		}

		if (x2 - x1 != 0 && y2 - y1 != 0)
		{
			grid[y1][x1] = new JRExporterGridCell(
					element,
					getElementIndex(elementIndex, parentElements),
					element.getWidth(), 
					element.getHeight(), 
					x2 - x1, 
					y2 - y1);
			
			JRBox cellBox = null;
			if (element instanceof JRBox)
			{
				cellBox = (JRBox) element;
			}
			
			grid[y1][x1].setBox(cellBox);
		}
	}


	protected Integer[] getElementIndex(int elementIndex, Integer[] parentElements)
	{
		if (!setElementIndexes)
		{
			return null;
		}
		
		Integer[] elementIndexes;
		if (parentElements == null)
		{
			elementIndexes = new Integer[]{new Integer(elementIndex)};
		}
		else
		{
			elementIndexes = new Integer[parentElements.length + 1];
			System.arraycopy(parentElements, 0, elementIndexes, 0, parentElements.length);
			elementIndexes[parentElements.length] = new Integer(elementIndex);
		}
		return elementIndexes;
	}


	protected void setFrameGridElements(JRPrintFrame frame, int x0, int y0, int elementIndex, Integer[] parentIndexes)
	{
		int topPadding = frame.getTopPadding();
		int leftPadding = frame.getLeftPadding();

		setGridElements(frame.getElements(), null, x0 + leftPadding, y0 + topPadding, getElementIndex(elementIndex, parentIndexes));
	}


	protected void setFrameCellsStyle(JRPrintFrame frame, int x1, int x2, int y1, int y2)
	{
		Color backcolor = frame.getMode() == JRElement.MODE_OPAQUE ? frame.getBackcolor() : null;
		
		for (int yi = y1; yi < y2; ++yi)
		{	
			for (int xi = x1; xi < x2; ++xi)
			{
				JRExporterGridCell cell = grid[yi][xi];
				
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
				
				boolean left = xi == x1;
				boolean right = xi == x2 - cell.colSpan;
				boolean top = yi == y1;
				boolean bottom = yi == y2 - cell.rowSpan;
					
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
	
	
	public static int getRowHeight(JRExporterGridCell[][] grid, int rowIdx)
	{
		JRExporterGridCell[] row = grid[rowIdx];
		
		if (row[0].rowSpan == 1 && row[0] != JRExporterGridCell.OCCUPIED_CELL) //quick exit
		{
			return row[0].height;
		}
		
		int rowHeight = 0;
		int minSpanIdx = 0;
		
		int colCount = row.length;
		
		int col;
		for (col = 0; col < colCount; ++col)
		{
			JRExporterGridCell cell = row[col];
			
			if (cell != JRExporterGridCell.OCCUPIED_CELL)
			{
				if (cell.rowSpan == 1)
				{
					rowHeight = cell.height;
					break;
				}

				if (cell.rowSpan < row[minSpanIdx].rowSpan)
				{
					minSpanIdx = col;
				}
			}
		}
		
		if (col >= colCount) //no cell with rowSpan = 1 was found, getting the height of the cell with min rowSpan
		{
			rowHeight = row[minSpanIdx].height;
		}
		
		return rowHeight;
	}

	
	protected static class SortedList extends ArrayList
	{
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
