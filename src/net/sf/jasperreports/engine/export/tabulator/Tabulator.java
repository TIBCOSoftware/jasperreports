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
package net.sf.jasperreports.engine.export.tabulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.util.Pair;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class Tabulator
{
	private static final Log log = LogFactory.getLog(Tabulator.class);
	
	private final ExporterFilter filter;
	private final List<? extends JRPrintElement> elements;
	
	private Table mainTable;
	
	private ParentCheck parentCheck = new ParentCheck();
	private SpanRangeCheck spanRangeCheck = new SpanRangeCheck();
	private SpanCheck spanCheck = new SpanCheck();
	private CollapseCheck collapseCheck = new CollapseCheck();

	public Tabulator(ExporterFilter filter, List<? extends JRPrintElement> elements)
	{
		this.filter = filter;
		this.elements = elements;
		
		this.mainTable = new Table(this);
	}

	public void tabulate()
	{
		// TODO lucianc force background as different layer
		layoutElements(elements, mainTable, null, 0, 0);
	}

	protected void layoutElements(List<? extends JRPrintElement> elementList, Table table, FrameCell parentCell,
			int xOffset, int yOffset)
	{
		if (log.isTraceEnabled())
		{
			log.trace("laying out " + elements.size() + " elements for parent " + parentCell
					+ " at offsets " + xOffset + ", " + yOffset);
		}
		
		// iterating the list in reverse order so that background band elements come last
		for (ListIterator<? extends JRPrintElement> it = elementList.listIterator(elementList.size()); it.hasPrevious();)
		{
			JRPrintElement element = it.previous();
			if (filter != null && !filter.isToExport(element))
			{
				if (log.isTraceEnabled())
				{
					log.trace("element " + element.getUUID() + " skipped by filter " + element);
				}
				
				continue;
			}
			
			placeElement(table, parentCell, xOffset, yOffset, element, it.nextIndex(), true);
		}
	}
	
	protected boolean placeElement(Table table, FrameCell parentCell, int xOffset, int yOffset,
			JRPrintElement element, int elementIndex, boolean allowOverlap)
	{
		DimensionRange<Column> colRange = table.columns.getRange(element.getX() + xOffset, 
				element.getX() + element.getWidth() + xOffset);
		DimensionRange<Row> rowRange = table.rows.getRange(element.getY() + yOffset, 
				element.getY() + element.getHeight() + yOffset);
		
		if (log.isTraceEnabled())
		{
			log.trace("placing element " + element.getUUID() + " at " + colRange.start + ", " + colRange.end
					+ ", " + rowRange.start + ", " + rowRange.end);
		}
		
		boolean overlap = false;
		int overlapColStart = colRange.start;
		int overlapColEnd = colRange.end;
		int overlapRowStart = rowRange.start;
		int overlapRowEnd = rowRange.end;
		for (Row row : rowRange.rangeSet)
		{
			for (Column col : colRange.rangeSet)
			{
				Cell cell = row.getCell(col);
				if (!canOverwrite(cell, parentCell))
				{
					overlap = true;
					if (!allowOverlap)
					{
						break;
					}

					// TODO lucianc see if we can avoid some of these checks
					Cell overlapParentCell = overlapParentCell(cell, parentCell);
					Pair<Column, Column> colSpanRange = getColumnSpanRange(table, col, row, overlapParentCell);
					Pair<Row, Row> rowSpanRange = getRowSpanRange(table, col, row, overlapParentCell);
					
					if (log.isTraceEnabled())
					{
						log.trace("found overlap with cell " + cell 
								+ ", overlap parent " + overlapParentCell
								+ ", column span range " + colSpanRange.first().startCoord + " to " + colSpanRange.second().startCoord
								+ ", row span range " + rowSpanRange.first().startCoord + " to " + rowSpanRange.second().startCoord);
					}
					
					if (overlapColStart > colSpanRange.first().startCoord)
					{
						overlapColStart = colSpanRange.first().startCoord;
					}
					if (overlapColEnd < colSpanRange.second().startCoord)
					{
						overlapColEnd = colSpanRange.second().startCoord;
					}
					if (overlapRowStart > rowSpanRange.first().startCoord)
					{
						overlapRowStart = rowSpanRange.first().startCoord;
					}
					if (overlapRowEnd < rowSpanRange.second().startCoord)
					{
						overlapRowEnd = rowSpanRange.second().startCoord;
					}
				}
			}
		}
		
		if (!overlap)
		{
			setElementCells(table, parentCell, xOffset, yOffset, element, elementIndex, 
					colRange, rowRange);
			return true;
		}
		
		if (!allowOverlap)
		{
			return false;
		}
		
		placeOverlappedElement(table, parentCell, xOffset, yOffset, 
				element, elementIndex, overlapColStart, overlapColEnd,
				overlapRowStart, overlapRowEnd);
		return true;
	}

	protected void placeOverlappedElement(Table table, FrameCell parentCell, int xOffset, int yOffset, 
			JRPrintElement element, int elementIndex, 
			int overlapColStart, int overlapColEnd, int overlapRowStart, int overlapRowEnd)
	{
		DimensionRange<Column> overlapColRange = table.columns.getRange(overlapColStart, overlapColEnd);
		DimensionRange<Row> overlapRowRange = table.rows.getRange(overlapRowStart, overlapRowEnd);
		DimensionRange<Column> layeredColRange = table.columns.addEntries(overlapColRange);
		DimensionRange<Row> layeredRowRange = table.rows.addEntries(overlapRowRange);
		
		// TODO lucianc expand existing layered cell if smaller than the current element
		boolean placed = false;
		Cell firstOverlapCell = overlapRowRange.floor.getCell(overlapColRange.floor);
		if (firstOverlapCell instanceof LayeredCell)
		{
			LayeredCell layeredCell = (LayeredCell) firstOverlapCell;
			// get the opposite corner cell
			Column lastCol = table.columns.getEntries().lower(layeredColRange.ceiling);
			Row lastRow = table.rows.getEntries().lower(layeredRowRange.ceiling);
			Cell lastCell = lastRow.getCell(lastCol);
			if (lastCell != null && (layeredCell.equals(lastCell) || layeredCell.accept(spanCheck, lastCell)))
			{
				placed = true;
				placeInLayeredCell(parentCell, xOffset, yOffset, element,
						elementIndex, layeredCell, layeredColRange,
						layeredRowRange);
			}
		}
		
		if (!placed)
		{
			createLayeredCell(table, parentCell, xOffset, yOffset, 
					element, elementIndex, 
					layeredColRange, layeredRowRange);
		}
	}

	protected void placeInLayeredCell(FrameCell parentCell, int xOffset,
			int yOffset, JRPrintElement element, int elementIndex,
			LayeredCell layeredCell, DimensionRange<Column> layeredColRange,
			DimensionRange<Row> layeredRowRange)
	{
		// attempt to place it on the first layer
		Table firstLayer = layeredCell.getLayers().get(0);
		boolean placed = placeElement(firstLayer, parentCell, 
				xOffset - layeredColRange.start, yOffset - layeredRowRange.start, 
				element, elementIndex, false);
		if (placed)
		{
			if (log.isTraceEnabled())
			{
				log.trace("placed element on first layer of " + layeredCell);
			}
		}
		else
		{
			// create a new layer
			createOverlappedLayer(parentCell, xOffset, yOffset, 
					layeredCell, element, elementIndex, 
					layeredColRange, layeredRowRange);
		}
	}

	protected void createLayeredCell(Table table, FrameCell parentCell, int xOffset, int yOffset, 
			JRPrintElement element, int elementIndex,
			DimensionRange<Column> layeredColRange, DimensionRange<Row> layeredRowRange)
	{
		if (log.isDebugEnabled())
		{
			log.debug("creating layered cell at " + layeredColRange + ", " + layeredRowRange);
		}
		
		LayeredCell layeredCell = new LayeredCell(parentCell);
		Table firstLayer = new Table(this);
		layeredCell.addLayer(firstLayer);
		copyCellsToLayerTable(table, layeredCell, firstLayer, 
				layeredColRange, layeredRowRange);
		
		setElementCells(layeredColRange, layeredRowRange, layeredCell);
		
		collapseSpanColumns(table, layeredColRange);
		collapseSpanRows(table, layeredRowRange);
		
		createOverlappedLayer(parentCell, xOffset, yOffset, 
				layeredCell, element, elementIndex, 
				layeredColRange, layeredRowRange);
	}

	protected void createOverlappedLayer(FrameCell parentCell, int xOffset, int yOffset,
			LayeredCell layeredCell, JRPrintElement element, int elementIndex,
			DimensionRange<Column> layeredColRange, DimensionRange<Row> layeredRowRange)
	{
		Table overlappedLayer = new Table(this);
		layeredCell.addLayer(overlappedLayer);
		overlappedLayer.columns.addEntry(0);
		overlappedLayer.rows.addEntry(0);
		
		int layerXOffset = xOffset - layeredColRange.start;
		int layerYOffset = yOffset - layeredRowRange.start;
		DimensionRange<Column> layerColRange = overlappedLayer.columns.getRange(element.getX() + layerXOffset, 
				element.getX() + element.getWidth() + layerXOffset);
		DimensionRange<Row> layerRowRange = overlappedLayer.rows.getRange(element.getY() + layerYOffset, 
				element.getY() + element.getHeight() + layerYOffset);
		setElementCells(overlappedLayer, parentCell, layerXOffset, layerYOffset, element, elementIndex, 
				layerColRange, layerRowRange);
	}

	protected void setElementCells(Table table, FrameCell parentCell,
			int xOffset, int yOffset, JRPrintElement element, int elementIndex,
			DimensionRange<Column> colRange, DimensionRange<Row> rowRange)
	{
		DimensionRange<Column> elementColRange = table.columns.addEntries(colRange);
		DimensionRange<Row> elementRowRange = table.rows.addEntries(rowRange);
		
		if (element instanceof JRPrintFrame)
		{
			JRPrintFrame frame = (JRPrintFrame) element;
			FrameCell frameCell = new FrameCell(parentCell, elementIndex);
			setElementCells(elementColRange, elementRowRange, frameCell);
			
			// go deep in the frame
			layoutElements(frame.getElements(), table, frameCell, 
					xOffset + frame.getX(), yOffset + frame.getY());
		}
		else
		{
			ElementCell elementCell = new ElementCell(parentCell, elementIndex);
			setElementCells(elementColRange, elementRowRange, elementCell);
		}
	}

	protected boolean canOverwrite(Cell existingCell, FrameCell currentParent)
	{
		if (existingCell == null)
		{
			return true;
		}
		
		if (existingCell instanceof FrameCell)
		{
			return isParent((FrameCell) existingCell, currentParent);
		}
		
		return false;
	}

	protected boolean isParent(FrameCell parent, FrameCell child)
	{
		if (child == null)
		{
			return false;
		}
		
		boolean foundAncestor = false;
		FrameCell ancestor = parent;
		while (ancestor != null)
		{
			if (ancestor.equals(child))
			{
				foundAncestor = true;
				break;
			}
			ancestor = ancestor.getParent();
		}
		return foundAncestor;
	}
	
	protected Cell overlapParentCell(Cell existingCell, FrameCell currentParent)
	{
		LinkedList<FrameCell> existingParents = new LinkedList<FrameCell>();
		for (FrameCell parent = existingCell.getParent(); parent != null; parent = parent.getParent())
		{
			existingParents.addFirst(parent);
		}
		
		LinkedList<FrameCell> currentParents = new LinkedList<FrameCell>();
		for (FrameCell parent = currentParent; parent != null; parent = parent.getParent())
		{
			currentParents.addFirst(parent);
		}
		
		Iterator<FrameCell> existingIt = existingParents.iterator();
		Iterator<FrameCell> currentIt = currentParents.iterator();
		while (existingIt.hasNext())
		{
			FrameCell existingParent = existingIt.next();
			FrameCell currentCell = currentIt.hasNext() ? currentIt.next() : null;
			if (currentCell == null || !existingParent.equals(currentCell))
			{
				return existingParent;
			}
		}
		
		return existingCell;
	}
	
	protected Pair<Column, Column> getColumnSpanRange(Table table, Column col, Row row, Cell spanned)
	{
		Column startCol = col;
		for (Column headCol : table.columns.getEntries().headSet(col))
		{
			Cell headCell = row.getCell(headCol);
			if (headCell == null || !spanned.accept(spanRangeCheck, headCell))
			{
				break;
			}
			startCol = headCol;
		}
		
		Column endCol = null;
		for (Column tailCol : table.columns.getEntries().tailSet(col))
		{
			endCol = tailCol;
			Cell tailCell = row.getCell(tailCol);
			if (tailCell == null || !spanned.accept(spanRangeCheck, tailCell))
			{
				break;
			}
		}
		assert endCol != null;
		assert startCol.startCoord < endCol.startCoord;
		
		return new Pair<Column, Column>(startCol, endCol);
	}
	
	protected Pair<Row, Row> getRowSpanRange(Table table, Column col, Row row, Cell spanned)
	{
		Row startRow = row;
		for (Row headRow : table.rows.getEntries().headSet(row))
		{
			Cell headCell = headRow.getCell(col);
			if (headCell == null || !spanned.accept(spanRangeCheck, headCell))
			{
				break;
			}
			startRow = headRow;
		}
		
		Row endRow = null;
		for (Row tailRow : table.rows.getEntries().tailSet(row))
		{
			endRow = tailRow;
			Cell tailCell = tailRow.getCell(col);
			if (tailCell == null || !spanned.accept(spanRangeCheck, tailCell))
			{
				break;
			}
		}
		assert endRow != null;
		assert startRow.startCoord < endRow.startCoord;
		
		return new Pair<Row, Row>(startRow, endRow);
	}

	protected void copyCellsToLayerTable(Table table, LayeredCell layeredCell, Table layerTable,
			DimensionRange<Column> colRange, DimensionRange<Row> rowRange)
	{
		// TODO lucianc collapse columns and rows within range
		List<Column> layerColumns = new ArrayList<Column>(colRange.rangeSet.size());
		for (Column column : colRange.rangeSet)
		{
			Column layerCol = layerTable.columns.addEntry(column.startCoord - colRange.start,
					column.endCoord - colRange.start);
			layerColumns.add(layerCol);
		}
		
		for (Row row : rowRange.rangeSet)
		{
			Row layerRow = layerTable.rows.addEntry(row.startCoord - rowRange.start,
					row.endCoord - rowRange.start);
			
			int colIdx = 0;
			for (Column column : colRange.rangeSet)
			{
				Cell cell = row.getCell(column);
				Column layerCol = layerColumns.get(colIdx);
				layerRow.setCell(layerCol, cell);
				
				++colIdx;
			}
		}
	}

	protected void collapseSpanColumns(Table table, DimensionRange<Column> range)
	{
		List<Pair<Column, Column>> removeList = new ArrayList<Pair<Column, Column>>();
		Column prevColumn = null;
		for (Column column : range.rangeSet)
		{
			if (prevColumn == null)
			{
				prevColumn = column;
				continue;
			}
			
			boolean collapse = true;
			for (Row row : table.getRows().getEntries())
			{
				Cell prevCell = row.getCell(prevColumn);
				Cell cell = row.getCell(column);
				boolean collapseCell = prevCell == null ? cell == null
						: (cell != null && prevCell.accept(collapseCheck, cell));
				if (!collapseCell)
				{
					collapse = false;
					break;
				}
			}
			
			if (collapse)
			{
				// removing outside the iteration so that the iterator is not broken
				removeList.add(new Pair<Column, Column>(column, prevColumn));
			}
			else
			{
				prevColumn = column;
			}
		}
		
		for (Pair<Column, Column> removePair : removeList)
		{
			table.removeColumn(removePair.first(), removePair.second());
		}
	}

	protected void collapseSpanRows(Table table, DimensionRange<Row> range)
	{
		List<Pair<Row, Row>> removeList = new ArrayList<Pair<Row, Row>>();
		Row prevRow = null;
		for (Row row : range.rangeSet)
		{
			if (prevRow == null)
			{
				prevRow = row;
				continue;
			}
			
			boolean collapse = true;
			for (Column column : table.getColumns().getEntries())
			{
				Cell prevCell = prevRow.getCell(column);
				Cell cell = row.getCell(column);
				boolean collapseCell = prevCell == null ? cell == null
						: (cell != null && prevCell.accept(collapseCheck, cell));
				if (!collapseCell)
				{
					collapse = false;
					break;
				}
			}
			
			if (collapse)
			{
				// removing outside the iteration so that the iterator is not broken
				removeList.add(new Pair<Row, Row>(row, prevRow));
			}
			else
			{
				prevRow = row;
			}
		}
		
		for (Pair<Row, Row> removePair : removeList)
		{
			table.removeRow(removePair.first(), removePair.second());
		}
	}

	protected void setElementCells(DimensionRange<Column> elementColRange,
			DimensionRange<Row> elementRowRange, Cell elementCell)
	{
		elementRowRange.floor.setCell(elementColRange.floor, elementCell);
		for (Column col : elementColRange.rangeSet.tailSet(elementColRange.floor, false))
		{
			elementRowRange.floor.setCell(col, elementCell.split());
		}
		
		for (Row row : elementRowRange.rangeSet.tailSet(elementRowRange.floor, false))
		{
			for (Column col : elementColRange.rangeSet)
			{
				row.setCell(col, elementCell.split());
			}
		}
	}

	public void addMargins(int width, int height)
	{
		mainTable.columns.addMargins(width);
		mainTable.rows.addMargins(height);
	}
	
	protected Column columnKey(int startCoord)
	{
		// TODO lucianc cache
		return new Column(startCoord);
	}
	
	protected Row rowKey(int startCoord)
	{
		// TODO lucianc cache
		return new Row(startCoord);
	}
	
	protected void columnSplit(Table table, Column splitCol, Column newCol)
	{
		for (Row row : table.rows.getEntries())
		{
			Cell cell = row.getCell(splitCol);
			if (cell != null)
			{
				Cell splitCell = cell.split();
				row.setCell(newCol, splitCell);
			}
		}
	}
	
	protected void rowSplit(Table table, Row splitRow, Row newRow)
	{
		for (Column col : table.columns.getEntries())
		{
			Cell cell = splitRow.getCell(col);
			if (cell != null)
			{
				Cell splitCell = cell.split();
				newRow.setCell(col, splitCell);
			}
		}
	}

	protected boolean isSplitCell(Cell spanned, Cell cell)
	{
		return (cell instanceof SplitCell) 
				&& ((SplitCell) cell).getSourceCell().equals(spanned);
	}
	
	public Table getTable()
	{
		return mainTable;
	}
	
	public JRPrintElement getCellElement(BaseElementCell cell)
	{
		FrameCell parentCell = cell.getParent();
		JRPrintElement element;
		if (parentCell == null)
		{
			element = elements.get(cell.getElementIndex());
		}
		else
		{
			JRPrintFrame parentFrame = (JRPrintFrame) getCellElement(parentCell);
			element = parentFrame.getElements().get(cell.getElementIndex());
		}
		return element;
	}
	
	public boolean[] getFrameCellBorders(Table table, Column col, Row row, FrameCell cell)
	{
		Column prevCol = table.columns.getEntries().lower(col);
		boolean leftBorder = !isParent(cell, row.getCell(prevCol));
		
		Column nextCol = table.columns.getEntries().higher(col);
		boolean rightBorder = !isParent(cell, row.getCell(nextCol));
		
		Row prevRow = table.rows.getEntries().lower(row);
		boolean topBorder = !isParent(cell, prevRow.getCell(col));
		
		Row nextRow = table.rows.getEntries().higher(row);
		boolean bottomBorder = !isParent(cell, nextRow.getCell(col));
		
		// TODO lucianc return in some other form
		return new boolean[]{leftBorder, rightBorder, topBorder, bottomBorder};
	}
	
	protected boolean isParent(FrameCell parent, Cell child)
	{
		if (child == null)
		{
			return false;
		}
		
		return child.accept(parentCheck, parent);
	}

	public int getColumnCellSpan(Table table, Column column, Row row, Cell cell)
	{
		int span = 1;
		for (Column tailCol : table.columns.getEntries().tailSet(column, false))
		{
			Cell tailCell = row.getCell(tailCol);
			if (tailCell == null || !cell.accept(spanCheck, tailCell))
			{
				break;
			}
			++span;
		}
		return span;
	}

	public int getRowCellSpan(Table table, Column column, Row row, Cell cell)
	{
		int span = 1;
		for (Row tailRow : table.rows.getEntries().tailSet(row, false))
		{
			Cell tailCell = tailRow.getCell(column);
			if (tailCell == null || !cell.accept(spanCheck, tailCell))
			{
				break;
			}
			++span;
		}
		return span;
	}
	
	protected class ParentCheck implements CellVisitor<FrameCell, Boolean, RuntimeException>
	{
		@Override
		public Boolean visit(ElementCell cell, FrameCell parentCell)
		{
			return Tabulator.this.isParent(parentCell, cell.getParent());
		}

		@Override
		public Boolean visit(SplitCell cell, FrameCell parentCell)
		{
			return Tabulator.this.isParent(parentCell, cell.getSourceCell().getParent());
		}

		@Override
		public Boolean visit(FrameCell frameCell, FrameCell parentCell)
		{
			return Tabulator.this.isParent(parentCell, frameCell);
		}

		@Override
		public Boolean visit(LayeredCell layeredCell, FrameCell parentCell)
		{
			return Tabulator.this.isParent(parentCell, layeredCell.getParent());
		}		
	}
	
	protected class SpanRangeCheck implements CellVisitor<Cell, Boolean, RuntimeException>
	{
		@Override
		public Boolean visit(ElementCell spanned, Cell cell)
		{
			return spanned.equals(cell) || Tabulator.this.isSplitCell(spanned, cell);
		}

		@Override
		public Boolean visit(SplitCell spanned, Cell cell)
		{
			return spanned.getSourceCell().accept(this, cell);
		}

		@Override
		public Boolean visit(FrameCell spanned, Cell cell)
		{
			return Tabulator.this.isParent(spanned, cell);
		}

		@Override
		public Boolean visit(LayeredCell spanned, Cell cell)
		{
			return spanned.equals(cell) || Tabulator.this.isSplitCell(spanned, cell);
		}
	}
	
	protected class SpanCheck implements CellVisitor<Cell, Boolean, RuntimeException>
	{
		@Override
		public Boolean visit(ElementCell spanned, Cell cell)
		{
			return Tabulator.this.isSplitCell(spanned, cell);
		}

		@Override
		public Boolean visit(SplitCell spanned, Cell cell)
		{
			return false;
		}

		@Override
		public Boolean visit(FrameCell spanned, Cell cell)
		{
			return false;
		}

		@Override
		public Boolean visit(LayeredCell spanned, Cell cell)
		{
			return Tabulator.this.isSplitCell(spanned, cell);
		}
	}
	
	protected class CollapseCheck implements CellVisitor<Cell, Boolean, RuntimeException>
	{
		@Override
		public Boolean visit(ElementCell spanned, Cell cell)
		{
			return Tabulator.this.isSplitCell(spanned, cell);
		}

		@Override
		public Boolean visit(SplitCell spanned, Cell cell)
		{
			// all element/layered split cells are the same
			return spanned.equals(cell);
		}

		@Override
		public Boolean visit(FrameCell spanned, Cell cell)
		{
			// all frame split cells are the same
			return spanned.equals(cell);
		}

		@Override
		public Boolean visit(LayeredCell spanned, Cell cell)
		{
			return Tabulator.this.isSplitCell(spanned, cell);
		}
	}
}