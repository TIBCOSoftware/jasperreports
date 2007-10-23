/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This class contains functionality common to graphic elements at design time. It provides implementation for the methods described
 * in <tt>JRTextElement</tt>.
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRDesignGraphicElement extends JRDesignElement implements JRGraphicElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_FILL = JRBaseStyle.PROPERTY_FILL;
	
	public static final String PROPERTY_PEN = JRBaseStyle.PROPERTY_PEN;

	/**
	 *
	 */
	protected Byte pen;
	protected Byte fill;


	/**
	 *
	 */
	protected JRDesignGraphicElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
	}
		

	/**
	 *
	 */
	public byte getPen()
	{
		return JRStyleResolver.getPen(this, PEN_1_POINT);
	}

	public Byte getOwnPen()
	{
		return this.pen;
	}

	/**
	 *
	 */
	public void setPen(byte pen)
	{
		setPen(new Byte(pen));
	}

	/**
	 *
	 */
	public void setPen(Byte pen)
	{
		Object old = this.pen;
		this.pen = pen;
		getEventSupport().firePropertyChange(PROPERTY_PEN, old, this.pen);
	}

	/**
	 *
	 */
	public byte getFill()
	{
		return JRStyleResolver.getFill(this, FILL_SOLID);
	}

	public Byte getOwnFill()
	{
		return this.fill;
	}

	/**
	 *
	 */
	public void setFill(byte fill)
	{
		setFill(new Byte(fill));
	}

	/**
	 *
	 */
	public void setFill(Byte fill)
	{
		Object old = this.fill;
		this.fill = fill;
		getEventSupport().firePropertyChange(PROPERTY_FILL, old, this.fill);
	}

}
