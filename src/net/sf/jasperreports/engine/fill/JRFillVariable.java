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
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillVariable implements JRVariable, JRCalculable
{


	/**
	 *
	 */
	protected JRVariable parent;

	/**
	 *
	 */
	private JRGroup resetGroup;
	private JRGroup incrementGroup;

	/**
	 *
	 */
	private Object previousOldValue;
	private Object oldValue;
	private Object estimatedValue;
	private Object incrementedValue;
	private Object value;
	private boolean isInitialized;
	private Object savedValue;
	
	private JRFillVariable[] helperVariables;

	/**
	 *
	 */
	private JRIncrementer incrementer;


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
		
		resetGroup = factory.getGroup(variable.getResetGroup());
		incrementGroup = factory.getGroup(variable.getIncrementGroup());
		
		helperVariables = new JRFillVariable[JRCalculable.HELPER_SIZE];
	}


	protected void reset()
	{
		previousOldValue = null;
		oldValue = null;
		estimatedValue = null;
		incrementedValue = null;
		value = null;
		isInitialized = false;
		savedValue = null;
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
	public Class<?> getIncrementerFactoryClass()
	{
		return parent.getIncrementerFactoryClass();
	}
		
	/**
	 *
	 */
	public String getIncrementerFactoryClassName()
	{
		return parent.getIncrementerFactoryClassName();
	}
		
	/**
	 *
	 */
	public JRExpression getExpression()
	{
		return parent.getExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getInitialValueExpression()
	{
		return parent.getInitialValueExpression();
	}
		
	/**
	 *
	 */
	public ResetTypeEnum getResetTypeValue()
	{
		return parent.getResetTypeValue();
	}
		
	/**
	 *
	 */
	public IncrementTypeEnum getIncrementTypeValue()
	{
		return parent.getIncrementTypeValue();
	}
		
	/**
	 *
	 */
	public CalculationEnum getCalculationValue()
	{
		return parent.getCalculationValue();
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
	public JRGroup getResetGroup()
	{
		return resetGroup;
	}
		
	/**
	 *
	 */
	public JRGroup getIncrementGroup()
	{
		return incrementGroup;
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
	public Object getEstimatedValue()
	{
		return estimatedValue;
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
		return incrementedValue;
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
		return value;
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
		return isInitialized;
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
			Class<?> incrementerFactoryClass = getIncrementerFactoryClass();
			
			JRIncrementerFactory incrementerFactory;
			if (incrementerFactoryClass == null)
			{
				incrementerFactory = JRDefaultIncrementerFactory.getFactory(getValueClass());
			}
			else
			{
				incrementerFactory = JRIncrementerFactoryCache.getInstance(incrementerFactoryClass); 
			}
			
			incrementer = incrementerFactory.getIncrementer(getCalculationValue().getValue());
		}
		
		return incrementer;
	}

	
	/**
	 * Sets a helper variable.
	 * 
	 * @param helperVariable the helper variable
	 * @param type the helper type
	 * @return the previous helper variable for the type
	 */
	public JRFillVariable setHelperVariable(JRFillVariable helperVariable, byte type)
	{
		JRFillVariable old = helperVariables[type]; 
		helperVariables[type] = helperVariable;
		return old;
	}
	
	
	/**
	 * Returns a helper variable.
	 * 
	 * @param type the helper type
	 * @return the helper variable for the specified type
	 */
	public JRCalculable getHelperVariable(byte type)
	{
		return helperVariables[type];
	}
	
	
	public Object getValue(byte evaluation)
	{
		Object returnValue;
		switch (evaluation)
		{
			case JRExpression.EVALUATION_OLD:
				returnValue = oldValue;
				break;
			case JRExpression.EVALUATION_ESTIMATED:
				returnValue = estimatedValue;
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
			case JRExpression.EVALUATION_ESTIMATED:
				savedValue = estimatedValue;
				estimatedValue = newValue;
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
			case JRExpression.EVALUATION_ESTIMATED:
				estimatedValue = savedValue;
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

	/**
	 *
	 */
	public Object clone() 
	{
		throw new UnsupportedOperationException();
	}

}
