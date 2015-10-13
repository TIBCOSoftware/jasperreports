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
package net.sf.jasperreports.components.map;

import java.io.Serializable;

import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @deprecated Replaced by {@link net.sf.jasperreports.components.items.StandardItemProperty}.
 */
public class StandardItemProperty implements ItemProperty, JRChangeEventsSupport, Serializable
{
	
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_VALUE = "value";
	public static final String PROPERTY_VALUE_EXPRESSION = "valueExpression";
	
	private transient JRPropertyChangeSupport eventSupport;

	private String name;
	private String value;
	private JRExpression valueExpression;

	public StandardItemProperty()
	{
	}
	
	public StandardItemProperty(String name, String value, JRExpression valueExpression)
	{
		this.name = name;
		this.valueExpression = valueExpression;
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		Object old = this.name;
		this.name = name;
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}

	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		Object old = this.value;
		this.value = value;
		getEventSupport().firePropertyChange(PROPERTY_VALUE, old, this.value);
	}
	
	public JRExpression getValueExpression()
	{
		return valueExpression;
	}

	public void setValueExpression(JRExpression valueExpression)
	{
		Object old = this.valueExpression;
		this.valueExpression = valueExpression;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_EXPRESSION, old, this.valueExpression);
	}

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

	public Object clone()
	{
		try
		{
			StandardItemProperty clone = (StandardItemProperty) super.clone();
			clone.valueExpression = JRCloneUtils.nullSafeClone(valueExpression);
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			// never
			throw new RuntimeException(e);
		}
	}
}
