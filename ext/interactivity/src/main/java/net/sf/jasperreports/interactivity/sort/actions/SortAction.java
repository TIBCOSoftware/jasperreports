/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.interactivity.sort.actions;

import java.util.UUID;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.interactivity.actions.AbstractAction;
import net.sf.jasperreports.interactivity.actions.ActionException;
import net.sf.jasperreports.interactivity.commands.CommandException;
import net.sf.jasperreports.interactivity.commands.CommandStack;
import net.sf.jasperreports.interactivity.commands.CommandTarget;
import net.sf.jasperreports.interactivity.commands.ResetInCacheCommand;
import net.sf.jasperreports.repo.JasperDesignCache;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class SortAction extends AbstractAction 
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private SortData sortData;

	public SortAction() {
	}

	public SortData getSortData() {
		return sortData;
	}

	public void setSortData(SortData sortData) {
		this.sortData = sortData;
	}

	@Override
	public void performAction() throws ActionException
	{
		if (sortData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(sortData.getTableUuid()));
			if (target != null)
			{
				JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), getReportContext());

				JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());
				JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
				
				// obtain command stack
				CommandStack commandStack = getCommandStack();
				
				// execute command
				try {
					commandStack.execute(
						new ResetInCacheCommand(
							new SortCommand(getJasperReportsContext(), dataset, sortData),
							getJasperReportsContext(),
							getReportContext(),
							target.getUri()
							)
						);
				} catch (CommandException e) {
					 throw new ActionException(e);
				}
			}
		}
	}

}
