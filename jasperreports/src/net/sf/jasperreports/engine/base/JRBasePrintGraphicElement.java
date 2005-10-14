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
package net.sf.jasperreports.engine.base;

import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRPrintGraphicElement;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBasePrintGraphicElement extends JRBasePrintElement implements JRPrintGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10003;

	/**
	 *
	 */
	protected Byte pen = null;
	protected Byte fill = null;


	/**
	 *
	 */
	public JRBasePrintGraphicElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}


	/**
	 *
	 */
	public byte getPen()
	{
		return JRStyleResolver.getPen(this, JRGraphicElement.PEN_1_POINT);
	}
		
	/**
	 *
	 */
	public Byte getOwnPen()
	{
		return pen;
	}
		
	/**
	 *
	 */
	public void setPen(byte pen)
	{
		this.pen = new Byte(pen);
	}
		
	/**
	 *
	 */
	public void setPen(Byte pen)
	{
		this.pen = pen;
	}
		
	/**
	 *
	 */
	public byte getFill()
	{
		return JRStyleResolver.getFill(this, JRGraphicElement.FILL_SOLID);
	}

	/**
	 *
	 */
	public Byte getOwnFill()
	{
		return fill;
	}

	/**
	 *
	 */
	public void setFill(byte fill)
	{
		this.fill = new Byte(fill);
	}

	/**
	 *
	 */
	public void setFill(Byte fill)
	{
		this.fill = fill;
	}
		

}
