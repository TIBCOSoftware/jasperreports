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
package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


/**
 * @author Narcis Marcu (narcism@users.sourceforge.net)
 * @version $Id: AbstractFieldComparator.java 5043 2012-03-09 09:55:21Z narcism $
 */
public class ResizeColumnAction extends AbstractTableAction
{

	private ResizeColumnData resizeColumnData;
	
	public ResizeColumnAction(){
	}
	
	public void setResizeColumnData(ResizeColumnData resizeColumnData) {
		this.resizeColumnData = resizeColumnData;
	}

	public ResizeColumnData getResizeColumnData() {
		return resizeColumnData;
	}

	public String getName() {
		return "resize_column_action";
	}

	public void performAction() 
	{
		if (resizeColumnData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(resizeColumnData.getUuid()));
			if (target != null)
			{
				JRIdentifiable identifiable = target.getIdentifiable();
				JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
				
				// obtain command stack
				CommandStack commandStack = getCommandStack();
				
				// execute command
				commandStack.execute(
					new ResetInCacheCommand(
						new ResizeColumnCommand(componentElement, resizeColumnData), 
						getJasperReportsContext(), 
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
