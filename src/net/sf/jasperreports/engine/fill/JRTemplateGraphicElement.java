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

import net.sf.jasperreports.engine.JRGraphicElement;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRTemplateGraphicElement extends JRTemplateElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	private byte pen = JRGraphicElement.PEN_1_POINT;
	private byte fill = JRGraphicElement.FILL_SOLID;


	/**
	 *
	 */
	protected JRTemplateGraphicElement()
	{
	}

	/**
	 *
	 */
	protected JRTemplateGraphicElement(JRGraphicElement graphicElement)
	{
		setGraphicElement(graphicElement);
	}


	/**
	 *
	 */
	protected void setGraphicElement(JRGraphicElement graphicElement)
	{
		super.setElement(graphicElement);
		
		setPen(graphicElement.getPen());
		setFill(graphicElement.getFill());
	}

	/**
	 *
	 */
	public byte getPen()
	{
		return this.pen;
	}
		
	/**
	 *
	 */
	protected void setPen(byte pen)
	{
		this.pen = pen;
	}
		
	/**
	 *
	 */
	public byte getFill()
	{
		return this.fill;
	}
		
	/**
	 *
	 */
	protected void setFill(byte fill)
	{
		this.fill = fill;
	}
	

}
