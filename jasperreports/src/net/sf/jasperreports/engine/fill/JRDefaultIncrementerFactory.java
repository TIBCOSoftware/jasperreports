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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDefaultIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	private static JRDefaultIncrementerFactory mainInstance = new JRDefaultIncrementerFactory();


	/**
	 *
	 */
	private JRDefaultIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRDefaultIncrementerFactory getInstance()
	{
		return mainInstance;
	}


	/**
	 *
	 */
	public JRExtendedIncrementer getExtendedIncrementer(byte calculation)
	{
		JRExtendedIncrementer incrementer = null;

		switch (calculation)
		{
			case JRVariable.CALCULATION_SYSTEM :
			{
				incrementer = JRDefaultSystemIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_FIRST :
			{
				incrementer = JRDefaultFirstIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_NOTHING :
			case JRVariable.CALCULATION_COUNT :
			case JRVariable.CALCULATION_SUM :
			case JRVariable.CALCULATION_AVERAGE :
			case JRVariable.CALCULATION_LOWEST :
			case JRVariable.CALCULATION_HIGHEST :
			case JRVariable.CALCULATION_STANDARD_DEVIATION :
			case JRVariable.CALCULATION_VARIANCE :
			case JRVariable.CALCULATION_DISTINCT_COUNT :
			default :
			{
				incrementer = JRDefaultNothingIncrementer.getInstance();
				break;
			}
		}
		
		return incrementer;
	}


	public static JRExtendedIncrementerFactory getFactory (Class valueClass)
	{
		JRExtendedIncrementerFactory factory;
		
		if (java.math.BigDecimal.class.equals(valueClass))
		{
			factory = JRBigDecimalIncrementerFactory.getInstance();
		}
		else if (
			java.lang.Number.class.equals(valueClass)
			|| java.lang.Double.class.equals(valueClass)
			)
		{
			factory = JRDoubleIncrementerFactory.getInstance();
		}
		else if (java.lang.Float.class.equals(valueClass))
		{
			factory = JRFloatIncrementerFactory.getInstance();
		}
		else if (java.lang.Long.class.equals(valueClass))
		{
			factory = JRLongIncrementerFactory.getInstance();
		}
		else if (java.lang.Integer.class.equals(valueClass))
		{
			factory = JRIntegerIncrementerFactory.getInstance();
		}
		else if (java.lang.Short.class.equals(valueClass))
		{
			factory = JRShortIncrementerFactory.getInstance();
		}
		else if (java.lang.Byte.class.equals(valueClass))
		{
			factory = JRByteIncrementerFactory.getInstance();
		}
		else if (java.lang.Comparable.class.isAssignableFrom(valueClass))
		{
			factory = JRComparableIncrementerFactory.getInstance();
		}
		else
		{
			factory = JRDefaultIncrementerFactory.getInstance();
		}
		
		return factory;
	}
}


/**
 *
 */
class JRDefaultNothingIncrementer extends JRAbstractExtendedIncrementer
{


	/**
	 *
	 */
	private static JRDefaultNothingIncrementer mainInstance = new JRDefaultNothingIncrementer();


	/**
	 *
	 */
	private JRDefaultNothingIncrementer()
	{
	}


	/**
	 *
	 */
	public static JRDefaultNothingIncrementer getInstance()
	{
		return mainInstance;
	}


	/**
	 *
	 */
	public Object increment(
		JRCalculable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		)
	{
		return expressionValue;
	}


	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		if (!calculableValue.isInitialized())
		{
			return calculableValue.getValue();
		}
		
		if (!calculable.isInitialized())
		{
			return calculable.getValue();
		}
		
		return null;
	}

	public Object initialValue()
	{
		return null;
	}
}


/**
 *
 */
class JRDefaultSystemIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRDefaultSystemIncrementer mainInstance = new JRDefaultSystemIncrementer();

	/**
	 *
	 */
	private JRDefaultSystemIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDefaultSystemIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(
		JRCalculable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		)
	{
		return variable.getValue();
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		return calculable.getValue();
	}
	
	public Object initialValue()
	{
		return null;
	}
}

class JRDefaultFirstIncrementer extends JRAbstractExtendedIncrementer
{
	private static final JRDefaultFirstIncrementer instance = new JRDefaultFirstIncrementer();

	private JRDefaultFirstIncrementer()
	{
	}
	
	public static JRDefaultFirstIncrementer getInstance()
	{
		return instance;
	}
	
	public Object initialValue()
	{
		return null;
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		if (!calculable.isInitialized())
		{
			return calculable.getValue();
		}
		
		if (!calculableValue.isInitialized())
		{
			return calculableValue.getValue();
		}
		
		return null;
	}

	public Object increment(JRCalculable calculable, Object expressionValue, AbstractValueProvider valueProvider)
	{
		if (calculable.isInitialized())
		{
			return expressionValue;
		}

		return calculable.getIncrementedValue();
	}
}
