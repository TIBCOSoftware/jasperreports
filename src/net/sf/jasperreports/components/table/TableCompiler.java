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
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
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
		if (!detectLoops(verifier, columns))
		{
			verifyColumns(verifier, columns);
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

	private void verifyColumns(JRVerifier verifier, List<BaseColumn> columns)
	{
		// TODO Auto-generated method stub
		
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
