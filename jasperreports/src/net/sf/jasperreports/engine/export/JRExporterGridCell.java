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

import java.awt.Color;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.type.ModeEnum;
	
	
/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRExporterGridCell
{
	/**
	 *
	 */
	public static final byte TYPE_EMPTY_CELL = 1; 
	public static final byte TYPE_OCCUPIED_CELL = 2; 
	public static final byte TYPE_ELEMENT_CELL = 3; 


	
	private Color backcolor;
	private Color forecolor;
	private JRLineBox box;


	/**
	 *
	 */
	public JRExporterGridCell()
	{
	}

	public abstract GridCellSize getSize();

	public abstract void setSize(GridCellSize size);
	
	public int getWidth()
	{
		return getSize().getWidth();
	}

	public int getHeight()
	{
		return getSize().getHeight();
	}

	public int getColSpan()
	{
		return getSize().getColSpan();
	}

	public int getRowSpan()
	{
		return getSize().getRowSpan();
	}


	public Color getBackcolor()//FIXMENOW who uses this and why?
	{
		return backcolor;
	}


	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}


	/**
	 *
	 */
	public JRLineBox getBox()
	{
		return box;
	}


	public void setBox(JRLineBox box)
	{
		this.box = box;
	}


	public Color getForecolor()
	{
		return forecolor;
	}


	public void setForecolor(Color forecolor)
	{
		this.forecolor = forecolor;
	}


	public Color getCellBackcolor()
	{
		Color color;
		JRPrintElement element = getElement();
		if (element != null && element.getModeValue() == ModeEnum.OPAQUE)
		{
			color = element.getBackcolor();
		}
		else
		{
			color = backcolor;
		}
		return color;
	}
	
	
	public boolean isEmpty()
	{
		return backcolor == null && box == null;
	}

	public abstract byte getType();
	
	public abstract JRPrintElement getElement();
	
	public abstract String getElementAddress();
	
	public abstract boolean isOccupied();

	public abstract String getProperty(String propName);
}
