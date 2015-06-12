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
package net.sf.jasperreports.components.headertoolbar.actions;

import net.sf.jasperreports.web.actions.ActionException;
import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class ResizeColumnAction extends AbstractVerifiableTableAction {

	public ResizeColumnAction() {
	}
	
	public void setResizeColumnData(ResizeColumnData resizeColumnData) {
		columnData = resizeColumnData;
	}

	public ResizeColumnData getResizeColumnData() {
		return (ResizeColumnData)columnData;
	}

	public String getName() {
		return "resize_column_action";
	}

	public void performAction() throws ActionException
	{
		// execute command
		try {
			getCommandStack().execute(
				new ResetInCacheCommand(
					new ResizeColumnCommand(table, getResizeColumnData()), 
					getJasperReportsContext(), 
					getReportContext(), 
					targetUri
					)
				);
		} catch (CommandException e) {
			 throw new ActionException(e);
		}
	}

	@Override
	public void verify() throws ActionException {
		ResizeColumnData resizeColData = getResizeColumnData();
		if (resizeColData.getWidth() < 0) {
			errors.addAndThrow("net.sf.jasperreports.components.headertoolbar.actions.resize.column.negative.width");
		}
	}

}
