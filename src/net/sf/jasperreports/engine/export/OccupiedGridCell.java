/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.export;

import net.sf.jasperreports.engine.JRPrintElement;

	
	
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class OccupiedGridCell extends JRExporterGridCell
{


	/**
	 *
	 */
	private JRExporterGridCell occupier;

	/**
	 *
	 */
	public OccupiedGridCell(JRExporterGridCell occupier)
	{
		// TODO lucianc do not store box/backcolor
		super();
		
		this.occupier = occupier;
	}

	public int getWidth()
	{
		return 0;
	}

	public void setWidth(int width)
	{
		// should not happen
		throw new UnsupportedOperationException("Cannot set width on an occupied cell");
	}

	public int getHeight()
	{
		return 0;
	}

	public int getColSpan()
	{
		return 1;
	}

	public void setColSpan(int colSpan)
	{
		// should not happen
		throw new UnsupportedOperationException("Cannot set column span on an occupied cell");
	}

	public int getRowSpan()
	{
		return 1;
	}

	public JRExporterGridCell getOccupier()
	{
		return occupier;
	}


	public byte getType()
	{
		return TYPE_OCCUPIED_CELL;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public boolean isOccupied()
	{
		return true;
	}

	@Override
	public JRPrintElement getElement()
	{
		return null;
	}

	@Override
	public String getElementAddress()
	{
		return null;
	}

	@Override
	public String getProperty(String propName)
	{
		return occupier.getProperty(propName);
	}

}
