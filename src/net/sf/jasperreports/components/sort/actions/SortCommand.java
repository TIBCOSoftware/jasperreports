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
package net.sf.jasperreports.components.sort.actions;

import java.util.List;

import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElementUtils;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandStack;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id: SortCommand.java 4935 2012-01-25 14:28:59Z teodord $
 */
public class SortCommand implements Command 
{
	
//	private ReportContext reportContext;
	protected JRDesignDataset dataset;
	protected SortData sortData;
//	protected JasperDesignCache cache;
	private CommandStack individualCommandStack;
	
	public SortCommand(JRDesignDataset dataset, SortData sortData) 
	//public AbstractSortCommand(ReportContext reportContext, SortData sortData) 
	{
//		this.reportContext = reportContext;
		this.dataset = dataset;
		this.sortData = sortData;
//		this.cache = JasperDesignCache.getInstance(reportContext);
		this.individualCommandStack = new CommandStack();
	}

	public void execute() 
	{
		SortOrderEnum sortOrder = HeaderToolbarElementUtils.getSortOrder(sortData.getSortOrder());//FIXMEJIVE use labels in JR enum, even if they are longer

		JRDesignSortField newSortField = 
			new JRDesignSortField(
				sortData.getSortColumnName(),
				SortFieldTypeEnum.getByName(sortData.getSortColumnType()),
				sortOrder
				);
		
		JRSortField oldSortField = null;
		List<JRSortField> sortFields = dataset.getSortFieldsList();
		for (JRSortField crtSortField : sortFields)
		{
			if (newSortField.getName().equals(crtSortField.getName()))
			{
				oldSortField = crtSortField;
				break;
			}
		}

		if (oldSortField != null)
		{
			individualCommandStack.execute(new RemoveSortFieldCommand(dataset, oldSortField));
		}
		
		if (sortOrder != null)
		{
			individualCommandStack.execute(new AddSortFieldCommand(dataset, newSortField));
		}
	}
	
	public void undo() 
	{
		individualCommandStack.undoAll();
	}

	public void redo() 
	{
		individualCommandStack.redoAll();
	}
}
