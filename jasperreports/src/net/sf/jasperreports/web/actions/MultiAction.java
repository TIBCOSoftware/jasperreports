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
package net.sf.jasperreports.web.actions;

import java.util.List;

import net.sf.jasperreports.web.commands.CommandException;
import net.sf.jasperreports.web.commands.MultiActionCommand;



/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 */
public class MultiAction extends AbstractAction {

	private List<AbstractAction> actions;
	
	public MultiAction(List<AbstractAction> actions) {
		this.actions = actions;
	}

	@Override
	public void performAction() throws ActionException {
		try{
			getCommandStack().execute(new MultiActionCommand(actions, getJasperReportsContext(), getReportContext()));
		} catch (CommandException e) {
			throw new ActionException(e);
		}
	}

}
