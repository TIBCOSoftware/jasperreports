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
package net.sf.jasperreports.charts.design;

import net.sf.jasperreports.charts.JRValueDisplay;
import net.sf.jasperreports.charts.base.JRBaseValueDisplay;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRFont;

import java.awt.Color;

/**
 * An object that specifies how a single value should be displayed.  Used with
 * charts such as a meter or thermometer that display a single value.
 *
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignValueDisplay extends JRBaseValueDisplay
{
	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;


	/**
	 * Constructs a new value display that is a copy of an existing one.
	 *
	 * @param valueDisplay the display to copy
	 */
	public JRDesignValueDisplay(JRValueDisplay valueDisplay)
	{
		super(valueDisplay);
	}

	/**
	 * Sets the color to use when displaying the value.
	 *
	 * @param color the color to use when displaying the value
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Sets the formatting mask to use when displaying the value.  This mask will
	 * be used to create a <code>java.text.DecimalFormat</code> object.
	 *
	 * @param mask the formatting mask to use when displaying the value
	 */
	public void setMask(String mask)
	{
		this.mask = mask;
	}

	/**
	 * Sets the font to use when displaying the value.
	 *
	 * @param font the font to use when displaying the value
	 */
	public void setFont(JRFont font)
	{
		this.font = font;
	}
}
