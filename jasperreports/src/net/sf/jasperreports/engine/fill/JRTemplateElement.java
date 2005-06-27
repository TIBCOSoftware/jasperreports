/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
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
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplateElement implements Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	private byte mode = JRElement.MODE_OPAQUE;
	private int width = 0;
	private Color forecolor = Color.black;
	private Color backcolor = Color.white;


	/**
	 *
	 */
	protected JRTemplateElement()
	{
	}

	/**
	 *
	 */
	protected JRTemplateElement(JRElement element)
	{
		setElement(element);
	}


	/**
	 *
	 */
	protected void setElement(JRElement element)
	{
		setMode(element.getMode());
		setWidth(element.getWidth());
		setForecolor(element.getForecolor());
		setBackcolor(element.getBackcolor());
	}

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
	protected void setMode(byte mode)
	{
		this.mode = mode;
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
	protected void setWidth(int width)
	{
		this.width = width;
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
	protected void setForecolor(Color forecolor)
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
	protected void setBackcolor(Color backcolor)
	{
		this.backcolor = backcolor;
	}
	

}
