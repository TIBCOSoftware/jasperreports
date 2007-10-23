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
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.base.JRBaseVariable;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignVariable extends JRBaseVariable implements JRChangeEventsSupport
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CALCULATION = "calculation";
	
	public static final String PROPERTY_EXPRESSION = "expression";
	
	public static final String PROPERTY_INCREMENTER_FACTORY_CLASS_NAME = "incrementerFactoryClassName";
	
	public static final String PROPERTY_INCREMENT_GROUP = "incrementGroup";
	
	public static final String PROPERTY_INCREMENT_TYPE = "incrementType";
	
	public static final String PROPERTY_INITIAL_VALUE_EXPRESSION = "initialValueExpression";
	
	public static final String PROPERTY_NAME = "name";
	
	public static final String PROPERTY_RESET_GROUP = "resetGroup";
	
	public static final String PROPERTY_RESET_TYPE = "resetType";
	
	public static final String PROPERTY_SYSTEM_DEFINED = "systemDefined";
	
	public static final String PROPERTY_VALUE_CLASS_NAME = "valueClassName";

	/**
	 *
	 */
	public void setName(String name)
	{
		String old = this.name;
		this.name = name;
		getEventSupport().firePropertyChange(PROPERTY_NAME, old, this.name);
	}
		
	/**
	 *
	 */
	public void setValueClass(Class clazz)
	{
		setValueClassName(clazz.getName());
	}
		
	/**
	 *
	 */
	public void setValueClassName(String className)
	{
		Object old = this.valueClassName;
		valueClassName = className;
		valueClass = null;
		getEventSupport().firePropertyChange(PROPERTY_VALUE_CLASS_NAME, old, this.valueClassName);
	}
		
	/**
	 *
	 */
	public void setIncrementerFactoryClass(Class clazz)
	{
		setIncrementerFactoryClassName(clazz.getName());
	}
		
	/**
	 *
	 */
	public void setIncrementerFactoryClassName(String className)
	{
		Object old = this.incrementerFactoryClassName;
		incrementerFactoryClassName = className;
		incrementerFactoryClass = null;
		getEventSupport().firePropertyChange(PROPERTY_INCREMENTER_FACTORY_CLASS_NAME, old, this.incrementerFactoryClassName);
	}
		
	/**
	 *
	 */
	public void setResetType(byte resetType)
	{
		byte old = this.resetType;
		this.resetType = resetType;
		getEventSupport().firePropertyChange(PROPERTY_RESET_TYPE, old, this.resetType);
	}
		
	/**
	 *
	 */
	public void setIncrementType(byte incrementType)
	{
		byte old = this.incrementType;
		this.incrementType = incrementType;
		getEventSupport().firePropertyChange(PROPERTY_INCREMENT_TYPE, old, this.incrementType);
	}
		
	/**
	 *
	 */
	public void setCalculation(byte calculation)
	{
		byte old = this.calculation;
		this.calculation = calculation;
		getEventSupport().firePropertyChange(PROPERTY_CALCULATION, old, this.calculation);
	}

	/**
	 *
	 */
	public void setSystemDefined(boolean isSystemDefined)
	{
		boolean old = this.isSystemDefined;
		this.isSystemDefined = isSystemDefined;
		getEventSupport().firePropertyChange(PROPERTY_SYSTEM_DEFINED, old, this.isSystemDefined);
	}

	/**
	 *
	 */
	public void setExpression(JRExpression expression)
	{
		Object old = this.expression;
		this.expression = expression;
		getEventSupport().firePropertyChange(PROPERTY_EXPRESSION, old, this.expression);
	}
		
	/**
	 *
	 */
	public void setInitialValueExpression(JRExpression expression)
	{
		Object old = this.initialValueExpression;
		this.initialValueExpression = expression;
		getEventSupport().firePropertyChange(PROPERTY_INITIAL_VALUE_EXPRESSION, old, this.initialValueExpression);
	}
	
	/**
	 *
	 */
	public void setResetGroup(JRGroup group)
	{
		Object old = this.resetGroup;
		this.resetGroup = group;
		getEventSupport().firePropertyChange(PROPERTY_RESET_GROUP, old, this.resetGroup);
	}
		
	/**
	 *
	 */
	public void setIncrementGroup(JRGroup group)
	{
		Object old = this.incrementGroup;
		this.incrementGroup = group;
		getEventSupport().firePropertyChange(PROPERTY_INCREMENT_GROUP, old, this.incrementGroup);
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
