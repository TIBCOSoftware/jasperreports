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

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.web.commands.Command;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimpleMoveColumnCommand implements Command 
{
	
	private List<BaseColumn> columns;
	private BaseColumn column;
	private int srcColIndex;
	private int destColIndex;
	
	
	public SimpleMoveColumnCommand(List<BaseColumn> columns, BaseColumn column, int srcColIndex, int destColIndex) 
	{
		this.columns = columns;
		this.column = column;
		this.srcColIndex = srcColIndex;
		this.destColIndex = destColIndex;
	}

	
	public void execute() 
	{
		moveColumns(srcColIndex, destColIndex);
	}
	
	private void moveColumns(int srcSiblingColIndex, int destSiblingColIndex) 
	{
		columns.remove(srcSiblingColIndex);
		if (destSiblingColIndex == columns.size()) 
		{
			columns.add(column);
		} 
		else 
		{
			columns.add(destSiblingColIndex, column);
		}
	}

	public void undo() 
	{
		moveColumns(destColIndex, srcColIndex);
	}

	public void redo() 
	{
		execute();
	}

}
