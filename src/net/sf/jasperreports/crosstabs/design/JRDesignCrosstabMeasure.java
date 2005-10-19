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
package net.sf.jasperreports.crosstabs.design;

import net.sf.jasperreports.crosstabs.base.JRBaseCrosstabMeasure;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignVariable;

/**
 * Crosstab measure implementation to be used for report designing.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRDesignCrosstabMeasure extends JRBaseCrosstabMeasure
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private JRDesignVariable designVariable;
	
	
	/**
	 * Creates a crosstab measure.
	 */
	public JRDesignCrosstabMeasure()
	{
		super();
		
		variable = designVariable = new JRDesignVariable();
		designVariable.setCalculation(JRVariable.CALCULATION_SYSTEM);
		designVariable.setSystemDefined(true);
	}

	
	/**
	 * Sets the calculation type.
	 * 
	 * @param calculation the calculation type
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getCalculation()
	 */
	public void setCalculation(byte calculation)
	{
		this.calculation = calculation;
		setExpressionClass();
	}

	
	/**
	 * Sets the measure value expression.
	 * 
	 * @param expression the measure value expression.
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getValueExpression()
	 */
	public void setValueExpression(JRExpression expression)
	{
		this.expression = expression;
		setExpressionClass();
	}

	
	/**
	 * Sets the incrementer factory class name.
	 * 
	 * @param incrementerFactoryClassName the incrementer factory class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getIncrementerFactoryClassName()
	 */
	public void setIncrementerFactoryClassName(String incrementerFactoryClassName)
	{
		this.incrementerFactoryClassName = incrementerFactoryClassName;
		this.incrementerFactoryClass = null;
	}

	
	/**
	 * Sets the measure name.
	 * 
	 * @param name the measure name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getName()
	 */
	public void setName(String name)
	{
		this.name = name;
		designVariable.setName(name);
	}

	
	/**
	 * Sets the percentage calculation type.
	 * 
	 * @param percentageOfType the percentage calculation type
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getPercentageOfType()
	 */
	public void setPercentageOfType(byte percentageOfType)
	{
		this.percentageOfType = percentageOfType;
	}

	
	/**
	 * Sets the percentage calculator class name.
	 * 
	 * @param percentageCalculatorClassName the percentage calculator class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getPercentageCalculatorClassName()
	 */
	public void setPercentageCalculatorClassName(String percentageCalculatorClassName)
	{
		this.percentageCalculatorClassName = percentageCalculatorClassName;
		this.percentageCalculatorClass = null;
	}

	
	/**
	 * Sets the measure value class name.
	 * 
	 * @param valueClassName the measure value class name
	 * @see net.sf.jasperreports.crosstabs.JRCrosstabMeasure#getValueClassName()
	 */
	public void setValueClassName(String valueClassName)
	{
		this.valueClassName = valueClassName;
		this.valueClass = null;
		setExpressionClass();
	}


	protected void setExpressionClass()
	{
		if (calculation == JRVariable.CALCULATION_COUNT)
		{
			designVariable.setValueClassName(Object.class.getName());
		}
		else
		{
			designVariable.setValueClassName(valueClassName);
		}
	}

}
