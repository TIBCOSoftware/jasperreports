/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPrintElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePrintElement implements JRPrintElement, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 604;

	/**
	 *
	 */
	protected byte mode = JRElement.MODE_OPAQUE;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected Color forecolor = Color.black;
	protected Color backcolor = Color.white;


	/**
	 *
	 */
	public byte getMode()
	{
		return this.mode;
	}
	
	/**
	 *
	 */
	public void setMode(byte mode)
	{
		this.mode = mode;
	}
	
	/**
	 *
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 *
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 *
	 */
	public int getY()
	{
		return this.y;
	}
	
	/**
	 *
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 *
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 *
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/**
	 *
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 *
	 */
	public Color getForecolor()
	{
		return this.forecolor;
	}
	
	/**
	 *
	 */
	public void setForecolor(Color forecolor)
	{
		this.forecolor = forecolor;
	}
	
	/**
	 *
	 */
	public Color getBackcolor()
	{
		return this.backcolor;
	}

	/**
	 *
	 */
	public void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}
	

}
