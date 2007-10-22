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
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRStyle;
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
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_ELEMENT_GROUP = "elementGroup";
	
	public static final String PROPERTY_HEIGHT = "height";
	
	public static final String PROPERTY_KEY = "key";
	
	public static final String PROPERTY_PRINT_WHEN_EXPRESSION = "printWhenExpression";
	
	public static final String PROPERTY_PRINT_WHEN_GROUP_CHANGES = "printWhenGroupChanges";
	
	public static final String PROPERTY_PARENT_STYLE = "parentStyle";
	
	public static final String PROPERTY_PARENT_STYLE_NAME_REFERENCE = "parentStyleNameReference";
	
	public static final String PROPERTY_Y = "y";


	/**
	 *
	 */
	protected JRDesignElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		positionType = JRElement.POSITION_TYPE_FIX_RELATIVE_TO_TOP;
	}


	/**
	 * Sets the unique identifier for the element.
	 */
	public void setKey(String key)
	{
		Object old = this.key;
		this.key = key;
		getEventSupport().firePropertyChange(PROPERTY_KEY, old, this.key);
	}
		
	/**
	 * Sets the vertical section relative offset for the element.
	 */
	public void setY(int y)
	{
		int old = this.y;
		this.y = y;
		getEventSupport().firePropertyChange(PROPERTY_Y, old, this.y);
	}
	
	/**
	 *
	 */
	public void setHeight(int height)
	{
		int old = this.height;
		this.height = height;
		getEventSupport().firePropertyChange(PROPERTY_HEIGHT, old, this.height);
	}
	
	/**
	 * Sets the print when expression. This expression must always return an instance of <tt>Boolean</tt> and its value
	 * will decide if the element will be displayed.
	 */
	public void setPrintWhenExpression(JRExpression expression)
	{
		Object old = this.printWhenExpression;
		this.printWhenExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_EXPRESSION, old, this.printWhenExpression);
	}
	
	/**
	 * Specifies the group for which an element with a <i>printRepeatedValues</i> attribute set to true will be redisplayed
	 * even if the value has not changed.
	 */
	public void setPrintWhenGroupChanges(JRGroup group)
	{
		Object old = this.printWhenGroupChanges;
		this.printWhenGroupChanges = group;
		getEventSupport().firePropertyChange(PROPERTY_PRINT_WHEN_GROUP_CHANGES, old, this.printWhenGroupChanges);
	}
	
	/**
	 * Specifies the logical group that the element belongs to. More elements can be grouped in order to get the height
	 * of the tallest one.
	 * @see net.sf.jasperreports.engine.JRElement#STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT
	 */
	public void setElementGroup(JRElementGroup elementGroup)
	{
		Object old = this.elementGroup;
		this.elementGroup = elementGroup;
		getEventSupport().firePropertyChange(PROPERTY_ELEMENT_GROUP, old, this.elementGroup);
	}
	
	public void setStyle(JRStyle style)
	{
		Object old = this.parentStyle;
		this.parentStyle = style;
		getEventSupport().firePropertyChange(PROPERTY_PARENT_STYLE, old, this.parentStyle);
	}
	
	/**
	 * Set the name of the external style to be used for this element.
	 * <p/>
	 * An external style is only effective when there is no internal style set for this element,
	 * i.e. {@link #getStyle() getStyle()} returns <code>null</code>
	 * The external style will be resolved at fill time from the templates used in the report.
	 * 
	 * @param styleName the name of the external style
	 * @see #getStyleNameReference()
	 */
	public void setStyleNameReference(String styleName)
	{
		Object old = this.parentStyleNameReference;
		this.parentStyleNameReference = styleName;
		getEventSupport().firePropertyChange(PROPERTY_PARENT_STYLE_NAME_REFERENCE, old, this.parentStyleNameReference);
	}
}
