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

import java.io.Serializable;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRClassLoader;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseVariable implements JRVariable, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = 608;

	/**
	 *
	 */
	protected String name = null;
	protected String valueClassName = java.lang.String.class.getName();
	protected String incrementerFactoryClassName = null;
	protected byte resetType = RESET_TYPE_REPORT;
	protected byte incrementType = RESET_TYPE_NONE;
	protected byte calculation = CALCULATION_NOTHING;
	protected boolean isSystemDefined = false;

	protected transient Class valueClass = null;
	protected transient Class incrementerFactoryClass = null;

	/**
	 *
	 */
	protected JRExpression expression = null;
	protected JRExpression initialValueExpression = null;
	protected JRGroup resetGroup = null;
	protected JRGroup incrementGroup = null;
	protected JRVariable countVariable = null;
	protected JRVariable sumVariable = null;
	protected JRVariable varianceVariable = null;


	/**
	 *
	 */
	protected JRBaseVariable()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseVariable(JRVariable variable, JRBaseObjectFactory factory)
	{
		factory.put(variable, this);
		
		name = variable.getName();
		valueClassName = variable.getValueClassName();
		incrementerFactoryClassName = variable.getIncrementerFactoryClassName();
		resetType = variable.getResetType();
		incrementType = variable.getIncrementType();
		calculation = variable.getCalculation();
		isSystemDefined = variable.isSystemDefined();
		
		expression = factory.getExpression(variable.getExpression());
		initialValueExpression = factory.getExpression(variable.getInitialValueExpression());

		resetGroup = factory.getGroup(variable.getResetGroup());
		incrementGroup = factory.getGroup(variable.getIncrementGroup());
		countVariable = factory.getVariable(variable.getCountVariable());
		sumVariable = factory.getVariable(variable.getSumVariable());
		varianceVariable = factory.getVariable(variable.getVarianceVariable());
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return this.name;
	}
		
	/**
	 *
	 */
	public Class getValueClass()
	{
		if (valueClass == null)
		{
			if (valueClassName != null)
			{
				try
				{
					valueClass = JRClassLoader.loadClassForName(valueClassName);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return valueClass;
	}
		
	/**
	 *
	 */
	public String getValueClassName()
	{
		return valueClassName;
	}
		
	/**
	 *
	 */
	public Class getIncrementerFactoryClass()
	{
		if (incrementerFactoryClass == null)
		{
			if (incrementerFactoryClassName != null)
			{
				try
				{
					incrementerFactoryClass = JRClassLoader.loadClassForName(incrementerFactoryClassName);
				}
				catch(ClassNotFoundException e)
				{
					throw new JRRuntimeException(e);
				}
			}
		}
		
		return incrementerFactoryClass;
	}
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}
		
	/**
	 *
	 */
	public byte getResetType()
	{
		return this.resetType;
	}
		
	/**
	 *
	 */
	public byte getIncrementType()
	{
		return this.incrementType;
	}
		
	/**
	 *
	 */
	public byte getCalculation()
	{
		return this.calculation;
	}

	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.isSystemDefined;
	}

	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.expression;
	}
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression()
	{
		return this.initialValueExpression;
	}
		
	/**
	 *
	 */
	public JRGroup getResetGroup()
	{
		return this.resetGroup;
	}
		
	/**
	 *
	 */
	public JRGroup getIncrementGroup()
	{
		return this.incrementGroup;
	}
		
	/**
	 *
	 */
	public JRVariable getCountVariable()
	{
		return this.countVariable;
	}

	/**
	 *
	 */
	public JRVariable getSumVariable()
	{
		return this.sumVariable;
	}

	/**
	 *
	 */
	public JRVariable getVarianceVariable()
	{
		return this.varianceVariable;
	}

		
}
