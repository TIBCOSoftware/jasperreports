/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.components.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRVerifier;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class TableCompiler implements ComponentCompiler
{

	public void collectExpressions(Component component,
			JRExpressionCollector collector)
	{
		TableComponent table = (TableComponent) component;
		
		JRDatasetRun datasetRun = table.getDatasetRun();
		collector.collect(datasetRun);
		
		JRExpressionCollector datasetCollector = collector.getDatasetCollector(
				datasetRun.getDatasetName());
		ColumnExpressionCollector columnCollector = new ColumnExpressionCollector(
				collector, datasetCollector);
		columnCollector.collectColumns(table.getColumns());
	}

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
		TableComponent table = (TableComponent) component;
		StandardTable compiledTable = new StandardTable(table, baseFactory);
		return compiledTable;
	}

	public void verify(Component component, JRVerifier verifier)
	{
		TableComponent table = (TableComponent) component;

		JRDatasetRun datasetRun = table.getDatasetRun();
		if (datasetRun == null)
		{
			verifier.addBrokenRule("No list subdataset run set", table);
		}
		else
		{
			verifier.verifyDatasetRun(datasetRun);
		}
		
		List<BaseColumn> columns = table.getColumns();
		if (columns == null || columns.isEmpty())
		{
			verifier.addBrokenRule("No columns defined in the table", table);
		}
		else
		{
			if (!detectLoops(verifier, columns))
			{
				verifyColumns(verifier, table);
			}
		}
	}

	protected boolean detectLoops(JRVerifier verifier, List<BaseColumn> columns)
	{
		Set<BaseColumn> parents = new HashSet<BaseColumn>();
		return detectLoops(verifier, columns, parents);
	}

	protected boolean detectLoops(final JRVerifier verifier, List<BaseColumn> columns, 
			final Set<BaseColumn> parents)
	{
		boolean loop = false;
		for (BaseColumn column : columns)
		{
			if (parents.contains(column))
			{
				verifier.addBrokenRule("Table column is its own ancestor", column);
				loop = true;
			}
			else
			{
				loop = column.visitColumn(new ColumnVisitor<Boolean>()
				{
					public Boolean visitColumn(Column column)
					{
						return false;
					}

					public Boolean visitColumnGroup(ColumnGroup columnGroup)
					{
						parents.add(columnGroup);
						boolean loopDetected = detectLoops(verifier, 
								columnGroup.getColumns(), parents);
						parents.remove(columnGroup);
						return loopDetected;
					}
				});
			}
			
			if (loop)
			{
				break;
			}
		}
		
		return false;
	}

	protected void verifyColumns(final JRVerifier verifier, final TableComponent table)
	{
		ColumnVisitor<Void> columnVerifier = new ColumnVisitor<Void>()
		{
			public Void visitColumn(Column column)
			{
				verifyColumn(table, column, verifier);
				return null;
			}

			public Void visitColumnGroup(ColumnGroup columnGroup)
			{
				verifyBaseColumn(table, columnGroup, verifier);
				
				List<BaseColumn> subcolumns = columnGroup.getColumns();
				if (subcolumns == null || subcolumns.isEmpty())
				{
					verifier.addBrokenRule("No columns defined in column group", columnGroup);
				}
				else
				{
					for (BaseColumn column : subcolumns)
					{
						column.visitColumn(this);
					}
				}
				return null;
			}
		};
		
		for (BaseColumn column : table.getColumns())
		{
			column.visitColumn(columnVerifier);
		}
	}
	
	protected void verifyBaseColumn(TableComponent table, BaseColumn column, JRVerifier verifier)
	{
		Integer width = column.getWidth();
		if (width == null)
		{
			verifier.addBrokenRule("Column width not set", column);
		}
		else if (width < 0)
		{
			verifier.addBrokenRule("Negative column width", column);
		}
		else
		{
			verifyCell(column.getTableHeader(), width, "table header", verifier);
			verifyCell(column.getTableFooter(), width, "table footer", verifier);
			verifyGroupCells(table, column.getGroupHeaders(), width, "group header", verifier);
			verifyGroupCells(table, column.getGroupFooters(), width, "group footer", verifier);
			verifyCell(column.getColumnHeader(), width, "column header", verifier);
			verifyCell(column.getColumnFooter(), width, "column footer", verifier);
		}
		
		verifier.verifyExpression(column.getPrintWhenExpression(), column, null, 
				"No type set for the column print when expression", 
				Boolean.class, 
				"Class {0} not supported for column print when expression. Use java.lang.Boolean instead.");
		
	}
	
	protected void verifyGroupCells(TableComponent table, List<GroupCell> cells, int width, 
			String cellName, JRVerifier verifier)
	{
		if (cells != null)
		{
			Set<String> groupNames = new HashSet<String>();
			for (GroupCell groupCell : cells)
			{
				String groupName = groupCell.getGroupName();
				if (groupName == null)
				{
					verifier.addBrokenRule("No group name set for table column group cell", groupCell);
				}
				else
				{
					if (!groupNames.add(groupName))
					{
						verifier.addBrokenRule("Duplicate " + cellName + " for group \"" + groupName + "\"", 
								groupCell);
					}
					
					JRDatasetRun datasetRun = table.getDatasetRun();
					if (datasetRun != null)
					{
						JRDesignDataset dataset = (JRDesignDataset) verifier.getReportDesign().getDatasetMap().get(
								datasetRun.getDatasetName());
						if (dataset != null && dataset.getGroupsMap().get(groupName) == null)
						{
							verifier.addBrokenRule("No group named " + groupName 
									+ "\" found in subdataset " + datasetRun.getDatasetName(), 
									groupCell);
						}
					}
				}
				
				verifyCell(groupCell.getCell(), width, cellName, verifier);
			}
		}
	}
	
	protected void verifyCell(Cell cell, int width, String cellName, JRVerifier verifier)
	{
		if (cell == null)
		{
			return;
		}
		
		if (cell.getRowSpan() != null && cell.getRowSpan() < 1)
		{
			verifier.addBrokenRule("Negative or zero cell row span", cell);
		}
		
		Integer height = cell.getHeight();
		if (height == null)
		{
			verifier.addBrokenRule("Cell height not set", cell);
		}
		else if (height < 0)
		{
			verifier.addBrokenRule("Negative cell height", cell);
		}
		else
		{
			JRElement[] elements = cell.getElements();
			if (elements != null && elements.length > 0)
			{
				int topPadding = cell.getLineBox().getTopPadding().intValue();
				int leftPadding = cell.getLineBox().getLeftPadding().intValue();
				int bottomPadding = cell.getLineBox().getBottomPadding().intValue();
				int rightPadding = cell.getLineBox().getRightPadding().intValue();

				int avlblWidth = width - leftPadding - rightPadding;
				int avlblHeight = height - topPadding - bottomPadding;
				
				for (JRElement element : elements)
				{
					verifier.verifyElement(element);
					
					if (element.getX() < 0 || element.getY() < 0)
					{
						verifier.addBrokenRule("Element must be placed at positive coordinates.", 
								element);
					}
					
					if (element.getY() + element.getHeight() > avlblHeight)
					{
						verifier.addBrokenRule("Element reaches outside table " + cellName + " contents height: y = " 
								+ element.getY() + ", height = " + element.getHeight() 
								+ ", cell available height = " + avlblHeight + ".", element);
					}
					
					if (element.getX() + element.getWidth() > avlblWidth)
					{
						verifier.addBrokenRule("Element reaches outside table " + cellName + " contents width: x = " 
								+ element.getX() + ", width = " + element.getWidth() 
								+ ", cell available width = " + avlblWidth + ".", element);
					}
					
				}
			}
		}
	}
	
	protected void verifyColumn(TableComponent table, Column column, JRVerifier verifier)
	{
		verifyBaseColumn(table, column, verifier);
		
		if (column.getWidth() != null)
		{
			Cell detailCell = column.getDetailCell();
			verifyCell(detailCell, column.getWidth(), "detail", verifier);
		}
	}
	
	protected HeadersPart verifyColumnsLayout(List<BaseColumn> columns)
	{
		List<HeadersPart> subparts = new ArrayList<HeadersPart>(columns.size());
		for (BaseColumn column : columns)
		{
			HeadersPart columnHeadersPart = makeColumnHeadersPart(column);
			subparts.add(columnHeadersPart);
		}
		
		//TODO
		return null;
	}

	protected HeadersPart makeColumnHeadersPart(BaseColumn column)
	{
		column.visitColumn(new ColumnVisitor<HeadersPart>()
		{
			public HeadersPart visitColumn(Column column)
			{
				// TODO Auto-generated method stub
				return null;
			}

			public HeadersPart visitColumnGroup(ColumnGroup columnGroup)
			{
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		return null;
	}

	protected static class HeadersPart
	{
		boolean valid;
		int width;
		List<Integer> rowHeights;
		
		public int getRowsCount()
		{
			return rowHeights.size();
		}

		public int getWidth()
		{
			return width;
		}
		
		public boolean isValid()
		{
			return valid;
		}
	}
	
}
