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

import java.util.UUID;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.DefaultFormatFactory;
import net.sf.jasperreports.engine.util.FormatFactory;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.actions.AbstractAction;
import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;

/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class FilterAction extends AbstractAction {
	
	private FilterData filterData;
	protected static FormatFactory formatFactory = new DefaultFormatFactory();
	
	public FilterAction() {
	}

	public FilterData getFilterData() {
		return filterData;
	}

	public void setFilterData(FilterData filterData) {
		this.filterData = filterData;
	}

	public void performAction() throws ActionException {
		
		if (filterData != null) {
			CommandTarget target = getCommandTarget(UUID.fromString(filterData.getTableUuid()));
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
							new FilterCommand(getJasperReportsContext(), dataset, getFilterData()),
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
