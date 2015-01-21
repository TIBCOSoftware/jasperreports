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
package net.sf.jasperreports.engine.design;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDefaultStyleProvider;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;


/**
 * This class provides a skeleton implementation for a design time report element. The difference between design elements
 * and compiled elements is that at design time they are more customizable. This class contains setters for properties
 * that can be only modified at design time.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
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
	
	public static final String PROPERTY_PROPERTY_EXPRESSIONS = "propertyExpressions";

	private List<JRPropertyExpression> propertyExpressions = new ArrayList<JRPropertyExpression>();

	/**
	 *
	 */
	protected JRDesignElement(JRDefaultStyleProvider defaultStyleProvider)
	{
		super(defaultStyleProvider);
		
		positionTypeValue = PositionTypeEnum.FIX_RELATIVE_TO_TOP;
	}


	/**
	 *
	 */
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
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
	 * @see StretchTypeEnum#RELATIVE_TO_TALLEST_OBJECT
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

	private void readObject(ObjectInputStream stream)
		throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject();
		
		if (propertyExpressions == null)
		{
			propertyExpressions = new ArrayList<JRPropertyExpression>();
		}
	}
	
	/**
	 * Add a dynamic/expression-based property.
	 * 
	 * @param propertyExpression the property to add
	 * @see #getPropertyExpressions()
	 */
	public void addPropertyExpression(JRPropertyExpression propertyExpression)
	{
		propertyExpressions.add(propertyExpression);
		getEventSupport().fireCollectionElementAddedEvent(PROPERTY_PROPERTY_EXPRESSIONS, 
				propertyExpression, propertyExpressions.size() - 1);
	}

	/**
	 * Remove a property expression.
	 * 
	 * @param propertyExpression the property expression to remove
	 * @see #addPropertyExpression(JRPropertyExpression)
	 */
	public void removePropertyExpression(JRPropertyExpression propertyExpression)
	{
		int idx = propertyExpressions.indexOf(propertyExpression);
		if (idx >= 0)
		{
			propertyExpressions.remove(idx);
			
			getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PROPERTY_EXPRESSIONS, 
					propertyExpression, idx);
		}
	}
	
	/**
	 * Remove a property expression.
	 * 
	 * @param name the name of the property to remove
	 * @return the removed property expression (if found)
	 */
	public JRPropertyExpression removePropertyExpression(String name)
	{
		JRPropertyExpression removed = null;
		for (ListIterator<JRPropertyExpression> it = propertyExpressions.listIterator(); it.hasNext();)
		{
			JRPropertyExpression prop = it.next();
			if (name.equals(prop.getName()))
			{
				removed = prop;
				int idx = it.previousIndex();
				
				it.remove();
				getEventSupport().fireCollectionElementRemovedEvent(PROPERTY_PROPERTY_EXPRESSIONS, 
						removed, idx);
				break;
			}
		}
		return removed;
	}
	
	/**
	 * Returns the list of property expressions.
	 * 
	 * @return the list of property expressions ({@link JRPropertyExpression} instances)
	 * @see #addPropertyExpression(JRPropertyExpression)
	 */
	public List<JRPropertyExpression> getPropertyExpressionsList()
	{
		return propertyExpressions;
	}
	
	public JRPropertyExpression[] getPropertyExpressions()
	{
		JRPropertyExpression[] props;
		if (propertyExpressions.isEmpty())
		{
			props = null;
		}
		else
		{
			props = propertyExpressions.toArray(new JRPropertyExpression[propertyExpressions.size()]);
		}
		return props;
	}


	@Override
	public Object clone()
	{
		JRDesignElement clone = (JRDesignElement) super.clone();
		clone.uuid = null;
		clone.propertyExpressions = JRCloneUtils.cloneList(propertyExpressions);
		return clone;
	}
	
}
