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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillVariable implements JRVariable
{


	/**
	 *
	 */
	protected JRVariable parent = null;

	/**
	 *
	 */
	private JRGroup resetGroup = null;
	private JRGroup incrementGroup = null;
	private JRVariable countVariable = null;
	private JRVariable sumVariable = null;
	private JRVariable varianceVariable = null;

	/**
	 *
	 */
	private Object oldValue = null;
	private Object estimatedValue = null;
	private Object incrementedValue = null;
	private Object value = null;
	private boolean isInitialized = false;

	/**
	 *
	 */
	private JRIncrementer incrementer = null;


	/**
	 *
	 */
	protected JRFillVariable(
		JRVariable variable, 
		JRFillObjectFactory factory
		)
	{
		factory.put(variable, this);

		parent = variable;
		
		resetGroup = (JRGroup)factory.getGroup(variable.getResetGroup());
		incrementGroup = (JRGroup)factory.getGroup(variable.getIncrementGroup());
		countVariable = (JRVariable)factory.getVariable(variable.getCountVariable());
		sumVariable = (JRVariable)factory.getVariable(variable.getSumVariable());
		varianceVariable = (JRVariable)factory.getVariable(variable.getVarianceVariable());
	}


	/**
	 *
	 */
	public String getName()
	{
		return this.parent.getName();
	}
		
	/**
	 *
	 */
	public Class getValueClass()
	{
		return this.parent.getValueClass();
	}
		
	/**
	 *
	 */
	public String getValueClassName()
	{
		return this.parent.getValueClassName();
	}
		
	/**
	 *
	 */
	public Class getIncrementerFactoryClass()
	{
		return this.parent.getIncrementerFactoryClass();
	}
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName()
	{
		return this.parent.getIncrementerFactoryClassName();
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return this.parent.getExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression()
	{
		return this.parent.getInitialValueExpression();
	}
		
	/**
	 *
	 */
	public byte getResetType()
	{
		return this.parent.getResetType();
	}
		
	/**
	 *
	 */
	public byte getIncrementType()
	{
		return this.parent.getIncrementType();
	}
		
	/**
	 *
	 */
	public byte getCalculation()
	{
		return this.parent.getCalculation();
	}
		
	/**
	 *
	 */
	public boolean isSystemDefined()
	{
		return this.parent.isSystemDefined();
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
	
	/**
	 *
	 */
	public Object getOldValue()
	{
		return this.oldValue;
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
	public Object getEstimatedValue()
	{
		return this.estimatedValue;
	}
		
	/**
	 *
	 */
	public void setEstimatedValue(Object estimatedValue)
	{
		this.estimatedValue = estimatedValue;
	}

	/**
	 *
	 */
	public Object getIncrementedValue()
	{
		return this.incrementedValue;
	}
		
	/**
	 *
	 */
	public void setIncrementedValue(Object incrementedValue)
	{
		this.incrementedValue = incrementedValue;
	}

	/**
	 *
	 */
	public Object getValue()
	{
		return this.value;
	}
		
	/**
	 *
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 *
	 */
	public boolean isInitialized()
	{
		return this.isInitialized;
	}
		
	/**
	 *
	 */
	public void setInitialized(boolean isInitialized)
	{
		this.isInitialized = isInitialized;
	}

		
	/**
	 *
	 */
	public JRIncrementer getIncrementer()
	{
		if (incrementer == null)
		{
			Class incrementerFactoryClass = getIncrementerFactoryClass();
			if (incrementerFactoryClass == null)
			{
				String valueClassName = getValueClassName();
			
				if (java.math.BigDecimal.class.getName().equals(valueClassName))
				{
					incrementer = JRBigDecimalIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (
					java.lang.Number.class.getName().equals(valueClassName)
					|| java.lang.Double.class.getName().equals(valueClassName)
					)
				{
					incrementer = JRDoubleIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Float.class.getName().equals(valueClassName))
				{
					incrementer = JRFloatIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Long.class.getName().equals(valueClassName))
				{
					incrementer = JRLongIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Integer.class.getName().equals(valueClassName))
				{
					incrementer = JRIntegerIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Short.class.getName().equals(valueClassName))
				{
					incrementer = JRShortIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Byte.class.getName().equals(valueClassName))
				{
					incrementer = JRByteIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else if (java.lang.Comparable.class.isAssignableFrom(getValueClass()))
				{
					incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
				else
				{
					incrementer = JRDefaultIncrementerFactory.getInstance().getIncrementer(getCalculation());
				}
			}
			else
			{
				JRIncrementerFactory incrementerFactory = 
					JRIncrementerFactoryCache.getInstance(incrementerFactoryClass); 
				incrementer = incrementerFactory.getIncrementer(getCalculation());
			}
		}
		
		return incrementer;
	}


}
