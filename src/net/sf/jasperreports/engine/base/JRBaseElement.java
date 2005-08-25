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

import java.awt.Color;
import java.io.Serializable;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;


/**
 * This class provides a skeleton implementation for a report element. It mostly provides internal variables, representing
 * the most common element properties, and their getter/setter methods. It also has a constructor for initializing
 * these properties.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRBaseElement implements JRElement, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 10001;

	/**
	 *
	 */
	protected String key = null;
	protected byte positionType = POSITION_TYPE_FLOAT;
	protected byte stretchType = STRETCH_TYPE_NO_STRETCH;
	protected boolean isPrintRepeatedValues = true;
	protected byte mode = MODE_OPAQUE;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean isRemoveLineWhenBlank = false;
	protected boolean isPrintInFirstWholeBand = false;
	protected boolean isPrintWhenDetailOverflows = false;
	protected Color forecolor = Color.black;
	protected Color backcolor = Color.white;

	/**
	 *
	 */
	protected JRExpression printWhenExpression = null;
	protected JRGroup printWhenGroupChanges = null;
	protected JRElementGroup elementGroup = null;


	/**
	 * Empty constructor.
	 */
	protected JRBaseElement()
	{
	}
		

	/**
	 * Initializes basic properties of the element.
	 * @param element an element whose properties are copied to this element. Usually it is a
	 * {@link net.sf.jasperreports.engine.design.JRDesignElement} that must be transformed into an
	 * <tt>JRBaseElement</tt> at compile time.
	 * @param factory a factory used in the compile process
	 */
	protected JRBaseElement(JRElement element, JRBaseObjectFactory factory)
	{
		factory.put(element, this);
		
		key = element.getKey();
		positionType = element.getPositionType();
		stretchType = element.getStretchType();
		isPrintRepeatedValues = element.isPrintRepeatedValues();
		mode = element.getMode();
		x = element.getX();
		y = element.getY();
		width = element.getWidth();
		height = element.getHeight();
		isRemoveLineWhenBlank = element.isRemoveLineWhenBlank();
		isPrintInFirstWholeBand = element.isPrintInFirstWholeBand();
		isPrintWhenDetailOverflows = element.isPrintWhenDetailOverflows();
		forecolor = element.getForecolor();
		backcolor = element.getBackcolor();

		printWhenExpression = factory.getExpression(element.getPrintWhenExpression());
		printWhenGroupChanges = factory.getGroup(element.getPrintWhenGroupChanges());
		elementGroup = factory.getElementGroup(element.getElementGroup());
	}
		

	/**
	 *
	 */
	public String getKey()
	{
		return this.key;
	}

	/**
	 *
	 */
	public byte getPositionType()
	{
		return this.positionType;
	}

	/**
	 *
	 */
	public void setPositionType(byte positionType)
	{
		this.positionType = positionType;
	}

	/**
	 *
	 */
	public byte getStretchType()
	{
		return this.stretchType;
	}
		
	/**
	 *
	 */
	public void setStretchType(byte stretchType)
	{
		this.stretchType = stretchType;
	}
		
	/**
	 *
	 */
	public boolean isPrintRepeatedValues()
	{
		return this.isPrintRepeatedValues;
	}
	
	/**
	 *
	 */
	public void setPrintRepeatedValues(boolean isPrintRepeatedValues)
	{
		this.isPrintRepeatedValues = isPrintRepeatedValues;
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
	public boolean isRemoveLineWhenBlank()
	{
		return this.isRemoveLineWhenBlank;
	}
	
	/**
	 *
	 */
	public void setRemoveLineWhenBlank(boolean isRemoveLine)
	{
		this.isRemoveLineWhenBlank = isRemoveLine;
	}
	
	/**
	 *
	 */
	public boolean isPrintInFirstWholeBand()
	{
		return this.isPrintInFirstWholeBand;
	}
	
	/**
	 *
	 */
	public void setPrintInFirstWholeBand(boolean isPrint)
	{
		this.isPrintInFirstWholeBand = isPrint;
	}
	
	/**
	 *
	 */
	public boolean isPrintWhenDetailOverflows()
	{
		return this.isPrintWhenDetailOverflows;
	}
	
	/**
	 *
	 */
	public void setPrintWhenDetailOverflows(boolean isPrint)
	{
		this.isPrintWhenDetailOverflows = isPrint;
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

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return this.printWhenExpression;
	}
	
	/**
	 *
	 */
	public JRGroup getPrintWhenGroupChanges()
	{
		return this.printWhenGroupChanges;
	}
	
	/**
	 *
	 */
	public JRElementGroup getElementGroup()
	{
		return this.elementGroup;
	}
	

}
