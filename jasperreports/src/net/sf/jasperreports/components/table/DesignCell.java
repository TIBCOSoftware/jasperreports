/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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

import net.sf.jasperreports.engine.JRConstants;

/**
 * 
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class DesignCell extends DesignBaseCell implements Cell
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public static final String PROPERTY_ROW_SPAN = "rowSpan";
	
	private Integer rowSpan;

	public DesignCell()
	{
	}
	
	@Override
	public Integer getRowSpan()
	{
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan)
	{
		Object old = this.rowSpan;
		this.rowSpan = rowSpan;
		getEventSupport().firePropertyChange(PROPERTY_ROW_SPAN, 
				old, this.rowSpan);
	}

	@Override
	public Object clone() 
	{
		DesignCell clone = (DesignCell) super.clone();
		return clone;
	}

}
