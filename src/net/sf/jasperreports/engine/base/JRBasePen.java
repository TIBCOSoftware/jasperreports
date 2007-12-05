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
package net.sf.jasperreports.engine.base;

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRStyleContainer;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRStyleResolver;


/**
 * This is useful for drawing borders around text elements and images. Boxes can have borders and paddings, which can
 * have different width and colour on each side of the element.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBox.java 1924 2007-10-25 13:31:38Z lucianc $
 */
public class JRBasePen implements JRPen, Serializable, JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_LINE_WIDTH = "lineWidth";
	
	public static final String PROPERTY_LINE_STYLE = "lineStyle";
	
	public static final String PROPERTY_LINE_COLOR = "lineColor";
	

	protected JRPenContainer penContainer = null;

	/**
	 *
	 */
	protected Float lineWidth = null;
	protected Byte lineStyle = null;
	protected Color lineColor = null;

	
	/**
	 *
	 */
	public JRBasePen(JRPenContainer penContainer)
	{
		super();
		
		this.penContainer = penContainer;
	}
	
	
	/**
	 *
	 */
	public JRStyleContainer getStyleContainer()
	{
		return penContainer;
	}

	/**
	 *
	 */
	public Float getLineWidth()
	{
		return JRStyleResolver.getLineWidth(this, penContainer.getDefaultLineWidth());
	}

	/**
	 *
	 */
	public Float getOwnLineWidth()
	{
		return lineWidth;
	}

	/**
	 *
	 */
	public void setLineWidth(float lineWidth)
	{
		setLineWidth(new Float(lineWidth));
	}

	/**
	 *
	 */
	public void setLineWidth(Float lineWidth)
	{
		Object old = this.lineWidth;
		this.lineWidth = lineWidth;
		getEventSupport().firePropertyChange(PROPERTY_LINE_WIDTH, old, this.lineWidth);
	}

	/**
	 *
	 */
	public Byte getLineStyle()
	{
		return JRStyleResolver.getLineStyle(this);
	}

	/**
	 *
	 */
	public Byte getOwnLineStyle()
	{
		return lineStyle;
	}

	/**
	 *
	 */
	public void setLineStyle(byte lineStyle)
	{
		setLineStyle(new Byte(lineStyle));
	}

	/**
	 *
	 */
	public void setLineStyle(Byte lineStyle)
	{
		Object old = this.lineStyle;
		this.lineStyle = lineStyle;
		getEventSupport().firePropertyChange(PROPERTY_LINE_STYLE, old, this.lineStyle);
	}

	/**
	 *
	 */
	public Color getLineColor()
	{
		return JRStyleResolver.getLineColor(this, penContainer.getDefaultLineColor());
	}

	/**
	 *
	 */
	public Color getOwnLineColor()
	{
		return lineColor;
	}

	/**
	 *
	 */
	public void setLineColor(Color lineColor)
	{
		Object old = this.lineColor;
		this.lineColor = lineColor;
		getEventSupport().firePropertyChange(PROPERTY_LINE_COLOR, old, this.lineColor);
	}


	public String getStyleNameReference()
	{
		return null;
	}

	/**
	 * 
	 */
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	public JRPen clone(JRPenContainer penContainer)
	{
		JRBasePen clone = (JRBasePen)this.clone();
		
		clone.penContainer = penContainer;
		
		return clone;
	}
	
	private transient JRPropertyChangeSupport eventSupport;
	
	public JRPropertyChangeSupport getEventSupport()
	{
		synchronized (this)
		{
			if (eventSupport == null)
			{
				eventSupport = new JRPropertyChangeSupport(this);
			}
		}
		
		return eventSupport;
	}
}
