/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.web.commands.Command;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.CommandStack;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortCommand implements Command 
{
	/**
	 * Property that specifies whether additional sort fields should be created automatically to preserve the integrity of dataset groups,
	 * when interactive sorting is performed. The groups need to have simple expressions using single field or single variable reference, for
	 * this feature to work properly.
	 */
	public static final String PROPERTY_CREATE_SORT_FIELDS_FOR_GROUPS = JRPropertiesUtil.PROPERTY_PREFIX + "create.sort.fields.for.groups";
	
//	private ReportContext reportContext;
	private JasperReportsContext jasperReportsContext;
	protected JRDesignDataset dataset;
	protected SortData sortData;
//	protected JasperDesignCache cache;
	private CommandStack individualCommandStack;
	
	public SortCommand(JasperReportsContext jasperReportsContext, JRDesignDataset dataset, SortData sortData) 
	//public AbstractSortCommand(ReportContext reportContext, SortData sortData) 
	{
//		this.reportContext = reportContext;
		this.jasperReportsContext = jasperReportsContext;
		this.dataset = dataset;
		this.sortData = sortData;
//		this.cache = JasperDesignCache.getInstance(reportContext);
		this.individualCommandStack = new CommandStack();
	}

	public void execute() throws CommandException 
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
		if (
			JRPropertiesUtil.getInstance(jasperReportsContext).getBooleanProperty(dataset, PROPERTY_CREATE_SORT_FIELDS_FOR_GROUPS, false)
			&& (sortFields == null || sortFields.isEmpty())
			)
		{
			List<JRGroup> groups = dataset.getGroupsList();
			for (JRGroup group : groups)
			{
				JRExpression expression = group.getExpression();
				if (expression != null)
				{
					JRExpressionChunk[] chunks = expression.getChunks();
					if (chunks != null && chunks.length == 1)
					{
						JRExpressionChunk chunk = chunks[0];
						if (
							chunk.getType() == JRExpressionChunk.TYPE_FIELD
							|| chunk.getType() == JRExpressionChunk.TYPE_VARIABLE
							)
						{
							JRDesignSortField groupSortField = 
								new JRDesignSortField(
									chunk.getText(),
									chunk.getType() == JRExpressionChunk.TYPE_FIELD 
										? SortFieldTypeEnum.FIELD
										: SortFieldTypeEnum.VARIABLE,
									SortOrderEnum.ASCENDING
									);
							individualCommandStack.execute(new AddSortFieldCommand(dataset, groupSortField));
						}
					}
				}
			}
		}

		for (JRSortField crtSortField : sortFields)
		{
			if (
				newSortField.getName().equals(crtSortField.getName())
				&& newSortField.getType() == crtSortField.getType()
				)
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
