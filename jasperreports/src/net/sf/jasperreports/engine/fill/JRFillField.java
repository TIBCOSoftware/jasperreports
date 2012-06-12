/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillField implements JRField
{


	/**
	 *
	 */
	protected JRField parent;

	/**
	 *
	 */
	private Object previousOldValue;
	private Object oldValue;
	private Object value;
	private Object savedValue;


	/**
	 *
	 */
	protected JRFillField(
		JRField field, 
		JRFillObjectFactory factory
		)
	{
		factory.put(field, this);

		parent = field;
	}


	/**
	 *
	 */
	public String getName()
	{
		return parent.getName();
	}
		
	/**
	 *
	 */
	public String getDescription()
	{
		return parent.getDescription();
	}
		
	/**
	 *
	 */
	public void setDescription(String description)
	{
	}
	
	/**
	 *
	 */
	public Class<?> getValueClass()
	{
		return parent.getValueClass();
	}
	
	/**
	 *
	 */
	public String getValueClassName()
	{
		return parent.getValueClassName();
	}
	
	/**
	 *
	 */
	public Object getOldValue()
	{
		return oldValue;
	}
		
	/**
	 *
	 */
	public void setOldValue(Object oldValue)
	{
		this.oldValue = oldValue;
	}

	/**
	 *
	 */
	public Object getValue()
	{
		return value;
	}
		
	/**
	 *
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}
		
	public Object getValue(byte evaluation)
	{
		Object returnValue;
		switch (evaluation)
		{
			case JRExpression.EVALUATION_OLD:
				returnValue = oldValue;
				break;
			default:
				returnValue = value;
				break;
		}
		return returnValue;
	}
	
	public void overwriteValue(Object newValue, byte evaluation)
	{
		switch (evaluation)
		{
			case JRExpression.EVALUATION_OLD:
				savedValue = oldValue;
				oldValue = newValue;
				break;
			default:
				savedValue = value;
				value = newValue;
				break;
		}
	}
	
	public void restoreValue(byte evaluation)
	{
		switch (evaluation)
		{
			case JRExpression.EVALUATION_OLD:
				oldValue = savedValue;
				break;
			default:
				value = savedValue;
				break;
		}
		savedValue = null;
	}


	
	public Object getPreviousOldValue()
	{
		return previousOldValue;
	}


	
	public void setPreviousOldValue(Object previousOldValue)
	{
		this.previousOldValue = previousOldValue;
	}

	
	public boolean hasProperties()
	{
		return parent.hasProperties();
	}


	public JRPropertiesMap getPropertiesMap()
	{
		return parent.getPropertiesMap();
	}

	
	public JRPropertiesHolder getParentProperties()
	{
		return null;
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}
}
