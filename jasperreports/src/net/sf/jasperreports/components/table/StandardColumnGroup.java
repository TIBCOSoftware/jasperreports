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
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class StandardColumnGroup extends StandardBaseColumn implements
		ColumnGroup
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private List<BaseColumn> children;
	
	public StandardColumnGroup()
	{
		children = new ArrayList<BaseColumn>();
	}
	
	public StandardColumnGroup(ColumnGroup columnGroup, ColumnFactory factory)
	{
		super(columnGroup, factory);
		
		children = factory.createColumns(columnGroup.getColumns());
	}
	
	public List<BaseColumn> getColumns()
	{
		return children;
	}

	public void addColumn(BaseColumn column)
	{
		children.add(column);
	}

	public <R> R visitColumn(ColumnVisitor<R> visitor)
	{
		return visitor.visitColumnGroup(this);
	}

	@Override
	public Object clone()
	{
		StandardColumnGroup clone = (StandardColumnGroup) super.clone();
		clone.children = JRCloneUtils.cloneList(children);
		return clone;
	}

}
