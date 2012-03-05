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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRValueParameter;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillParameter implements JRValueParameter
{


	/**
	 *
	 */
	protected JRParameter parent;

	/**
	 *
	 */
	private Object value;


	/**
	 *
	 */
	protected JRFillParameter(
		JRParameter parameter, 
		JRFillObjectFactory factory
		)
	{
		factory.put(parameter, this);

		parent = parameter;
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

	public Class<?> getNestedType()
	{
		return parent.getNestedType();
	}

	public String getNestedTypeName()
	{
		return parent.getNestedTypeName();
	}
	
	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return parent.isSystemDefined();
	}
	
	/**
	 *
	 */
	public boolean isForPrompting()
	{
		return parent.isForPrompting();
	}
	
	/**
	 *
	 */
	public JRExpression getDefaultValueExpression()
	{
		return parent.getDefaultValueExpression();
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
