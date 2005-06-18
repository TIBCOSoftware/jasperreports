/*
 * ============================================================================
 *                   GNU Lesser General Public License
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
package net.sf.jasperreports.engine.design;

import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.base.JRBaseElement;


/**
 * This class provides a skeleton implementation for a design time report element. The difference between design elements
 * and compiled elements is that at design time they are more customizable. This class contains setters for properties
 * that can be only modified at design time.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public abstract class JRDesignElement extends JRBaseElement
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 * Sets the unique identifier for the element.
	 */
	public void setKey(String key)
	{
		this.key = key;
	}
		
	/**
	 * Sets the vertical section relative offset for the element.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 * Sets the print when expression. This expression must always return an instance of <tt>Boolean</tt> and its value
	 * will decide if the element will be displayed.
	 */
	public void setPrintWhenExpression(JRExpression expression)
	{
		this.printWhenExpression = expression;
	}
	
	/**
	 * Specifies the group for which an element with a <i>printRepeatedValues</i> attribute set to true will be redisplayed
	 * even if the value has not changed.
	 */
	public void setPrintWhenGroupChanges(JRGroup group)
	{
		this.printWhenGroupChanges = group;
	}
	
	/**
	 * Specifies the logical group that the element belongs to. More elements can be grouped in order to get the height
	 * of the tallest one.
	 * @see net.sf.jasperreports.engine.JRElement#STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT
	 */
	public void setElementGroup(JRElementGroup elementGroup)
	{
		this.elementGroup = elementGroup;
	}
	

}
