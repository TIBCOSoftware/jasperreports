/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.charts.design;

import java.awt.Color;

import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseValueDisplay;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

/**
 * An object that specifies how a single value should be displayed.  Used with
 * charts such as a meter or thermometer that display a single value.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 */
public class JRDesignValueDisplay extends JRBaseValueDisplay implements JRChangeEventsSupport
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_COLOR = "color";
	
	public static final String PROPERTY_FONT = "font";
	
	public static final String PROPERTY_MASK = "mask";


	/**
	 * Constructs a new value display that is a copy of an existing one.
	 *
	 * @param valueDisplay the display to copy
	 */
	public JRDesignValueDisplay(JRValueDisplay valueDisplay, JRChart chart)//FIXMECHART this should be very similar to font; just a container, with unimportant design object
	{
		super(valueDisplay, chart);
	}

	/**
	 * Sets the color to use when displaying the value.
	 *
	 * @param color the color to use when displaying the value
	 */
	public void setColor(Color color)
	{
		Object old = this.color;
		this.color = color;
		getEventSupport().firePropertyChange(PROPERTY_COLOR, old, this.color);
	}

	/**
	 * Sets the formatting mask to use when displaying the value.  This mask will
	 * be used to create a <code>java.text.DecimalFormat</code> object.
	 *
	 * @param mask the formatting mask to use when displaying the value
	 */
	public void setMask(String mask)
	{
		Object old = this.mask;
		this.mask = mask;
		getEventSupport().firePropertyChange(PROPERTY_MASK, old, this.mask);
	}

	/**
	 * Sets the font to use when displaying the value.
	 *
	 * @param font the font to use when displaying the value
	 */
	public void setFont(JRFont font)
	{
		Object old = this.font;
		this.font = font;
		getEventSupport().firePropertyChange(PROPERTY_FONT, old, this.font);
	}
	
	/**
	 *
	 */
	public Object clone()
	{
		JRDesignValueDisplay clone = (JRDesignValueDisplay)super.clone();
		clone.eventSupport = null;
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
