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
package net.sf.jasperreports.crosstabs.base;

import java.io.Serializable;

import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.util.JRClassLoader;

/**
 * Base read-only crosstab measure implementation.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRBaseCrosstabMeasure implements JRCrosstabMeasure, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected String name;
	protected String valueClassName;
	protected Class valueClass;
	protected JRExpression expression;
	protected byte calculation = JRVariable.CALCULATION_COUNT;
	protected String incrementerFactoryClassName;
	protected Class incrementerFactoryClass;
	protected byte percentageOfType = JRCrosstabMeasure.PERCENTAGE_TYPE_NONE;
	protected String percentageCalculatorClassName;
	protected Class percentageCalculatorClass;
	protected JRVariable variable;

	protected JRBaseCrosstabMeasure()
	{
	}
	
	public JRBaseCrosstabMeasure(JRCrosstabMeasure measure, JRBaseObjectFactory factory)
	{
		factory.put(measure, this);
		
		this.name = measure.getName();
		this.valueClassName = measure.getValueClassName();
		this.expression = factory.getExpression(measure.getValueExpression());
		this.calculation = measure.getCalculation();
		this.incrementerFactoryClassName = measure.getIncrementerFactoryClassName();
		this.percentageOfType = measure.getPercentageOfType();		
		this.percentageCalculatorClassName = measure.getPercentageCalculatorClassName();
		this.variable = factory.getVariable(measure.getVariable());
	}
	
	public String getName()
	{
		return name;
	}

	public String getValueClassName()
	{
		return valueClassName;
	}

	public JRExpression getValueExpression()
	{
		return expression;
	}

	public byte getCalculation()
	{
		return calculation;
	}

	public String getIncrementerFactoryClassName()
	{
		return incrementerFactoryClassName;
	}

	public byte getPercentageOfType()
	{
		return percentageOfType;
	}

	public Class getIncrementerFactoryClass()
	{
		if (incrementerFactoryClass == null && incrementerFactoryClassName != null)
		{
			try
			{
				incrementerFactoryClass = JRClassLoader.loadClassForName(incrementerFactoryClassName);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRRuntimeException("Could not load measure incrementer class", e);
			}
		}
		
		return incrementerFactoryClass;
	}

	public Class getValueClass()
	{
		if (valueClass == null && valueClassName != null)
		{
			try
			{
				valueClass = JRClassLoader.loadClassForName(valueClassName);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRRuntimeException("Could not load bucket value class", e);
			}
		}
		
		return valueClass;
	}

	public JRVariable getVariable()
	{
		return variable;
	}

	public String getPercentageCalculatorClassName()
	{
		return percentageCalculatorClassName;
	}

	public Class getPercentageCalculatorClass()
	{
		if (percentageCalculatorClass == null && percentageCalculatorClassName != null)
		{
			try
			{
				percentageCalculatorClass = JRClassLoader.loadClassForName(percentageCalculatorClassName);
			}
			catch (ClassNotFoundException e)
			{
				throw new JRRuntimeException("Could not load measure percentage calculator class", e);
			}
		}
		
		return percentageCalculatorClass;
	}
}
