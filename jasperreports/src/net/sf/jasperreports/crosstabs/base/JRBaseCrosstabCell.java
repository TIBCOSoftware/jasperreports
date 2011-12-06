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
package net.sf.jasperreports.crosstabs.base;

import java.io.Serializable;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * Base read-only implementation of {@link net.sf.jasperreports.crosstabs.JRCrosstabCell JRCrosstabCell}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseCrosstabCell implements JRCrosstabCell, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected Integer width;
	protected Integer height;
	protected String rowTotalGroup;
	protected String columnTotalGroup;
	protected JRCellContents contents;
	
	protected JRBaseCrosstabCell()
	{
	}
	
	public JRBaseCrosstabCell(JRCrosstabCell crosstabCell, JRBaseObjectFactory factory)
	{
		factory.put(crosstabCell, this);
		
		width = crosstabCell.getWidth();
		height = crosstabCell.getHeight();
		
		rowTotalGroup = crosstabCell.getRowTotalGroup();
		columnTotalGroup = crosstabCell.getColumnTotalGroup();
		
		contents = factory.getCell(crosstabCell.getContents());
	}

	public String getRowTotalGroup()
	{
		return rowTotalGroup;
	}

	public String getColumnTotalGroup()
	{
		return columnTotalGroup;
	}

	public JRCellContents getContents()
	{
		return contents;
	}

	public Integer getHeight()
	{
		return height;
	}

	public Integer getWidth()
	{
		return width;
	}

	/**
	 * 
	 */
	public Object clone() 
	{
		JRBaseCrosstabCell clone = null;

		try
		{
			clone = (JRBaseCrosstabCell)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
		
		clone.contents = JRCloneUtils.nullSafeClone(contents);

		return clone;
	}
}
