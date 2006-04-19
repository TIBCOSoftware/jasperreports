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
package net.sf.jasperreports.crosstabs.design;

import java.awt.Color;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.engine.JRBox;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

/**
 * Implementation of {@link net.sf.jasperreports.crosstabs.JRCellContents JRCellContents} used for
 * report design.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCellContents extends JRDesignElementGroup implements JRCellContents
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected JRDefaultStyleProvider defaultStyleProvider;
	protected JRStyle style;
	
	protected Byte mode;
	private Color backcolor;
	private JRBox box;
	private int width = JRCellContents.NOT_CALCULATED;
	private int height = JRCellContents.NOT_CALCULATED;

	
	/**
	 * Creates an empty cell contents.
	 */
	public JRDesignCellContents()
	{
		super();
	}
	
	public Color getBackcolor()
	{
		return backcolor;
	}
	
	
	/**
	 * Sets the cell background color.
	 * 
	 * @param color the background color
	 * @see JRCellContents#getBackcolor()
	 */
	public void setBackcolor(Color color)
	{
		backcolor = color;
	}

	public JRBox getBox()
	{
		return box;
	}
	
	
	/**
	 * Sets the cell border.
	 * 
	 * @param box the border
	 * @see JRCellContents#getBox()
	 */
	public void setBox(JRBox box)
	{
		this.box = box;
	}

	public int getHeight()
	{
		return height;
	}

	
	/**
	 * Sets the computed cell height.
	 * 
	 * @param height the cell height
	 * @see JRCellContents#getHeight()
	 */
	protected void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}


	/**
	 * Sets the computed cell width.
	 * 
	 * @param width the cell width
	 * @see JRCellContents#getWidth()
	 */
	protected void setWidth(int width)
	{
		this.width = width;
	}

	public JRDefaultStyleProvider getDefaultStyleProvider()
	{
		return defaultStyleProvider;
	}

	public JRStyle getStyle()
	{
		return style;
	}
	
	
	/**
	 * Sets the style used by this cell.
	 * <p/>
	 * The style is only used for cell background and borders and is not inherited by
	 * elements inside the cell.
	 * 
	 * @param style the style to be used
	 */
	public void setStyle(JRStyle style)
	{
		this.style = style;
	}

	public Byte getMode()
	{
		return mode;
	}

	
	/**
	 * Sets the cell transparency mode.
	 * 
	 * @param mode the transparency mode
	 * @see JRCellContents#getMode()
	 */
	public void setMode(Byte mode)
	{
		this.mode = mode;
	}
}
