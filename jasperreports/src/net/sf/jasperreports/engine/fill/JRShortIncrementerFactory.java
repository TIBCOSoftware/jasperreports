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
public class JRShortIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	protected static final Short ZERO = new Short((short)0);


	/**
	 *
	 */
	private static JRShortIncrementerFactory mainInstance = new JRShortIncrementerFactory();


	/**
	 *
	 */
	private JRShortIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRShortIncrementerFactory getInstance()
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
			case JRVariable.CALCULATION_COUNT :
			{
				incrementer = JRShortCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRShortSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRShortAverageIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_LOWEST :
			case JRVariable.CALCULATION_HIGHEST :
			{
				incrementer = JRComparableIncrementerFactory.getInstance().getExtendedIncrementer(calculation);
				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION :
			{
				incrementer = JRShortStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRShortVarianceIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_DISTINCT_COUNT :
			{
				incrementer = JRShortDistinctCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SYSTEM :
			case JRVariable.CALCULATION_NOTHING :
			case JRVariable.CALCULATION_FIRST :
			default :
			{
				incrementer = JRDefaultIncrementerFactory.getInstance().getExtendedIncrementer(calculation);
				break;
			}
		}
		
		return incrementer;
	}


}


/**
 *
 */
class JRShortCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortCountIncrementer mainInstance = new JRShortCountIncrementer();

	/**
	 *
	 */
	private JRShortCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortCountIncrementer getInstance()
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
		Number value = (Number)variable.getIncrementedValue();

		if (value == null || variable.isInitialized())
		{
			value = JRShortIncrementerFactory.ZERO;
		}

		if (expressionValue == null)
		{
			return value;
		}

		return new Short((short)(value.shortValue() + 1));
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		Number value = (Number)calculable.getIncrementedValue();
		Number combineValue = (Number) calculableValue.getValue();

		if (value == null || calculable.isInitialized())
		{
			value = JRShortIncrementerFactory.ZERO;
		}

		if (combineValue == null)
		{
			return value;
		}

		return new Short((short) (value.shortValue() + combineValue.shortValue()));
	}

	
	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRShortDistinctCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortDistinctCountIncrementer mainInstance = new JRShortDistinctCountIncrementer();

	/**
	 *
	 */
	private JRShortDistinctCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortDistinctCountIncrementer getInstance()
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
		DistinctCountHolder holder = 
			(DistinctCountHolder)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
		
		if (variable.isInitialized())
		{
			holder.init();
		}

		return new Short((short)holder.getCount());
	}

	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRShortSumIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortSumIncrementer mainInstance = new JRShortSumIncrementer();

	/**
	 *
	 */
	private JRShortSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortSumIncrementer getInstance()
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
		Number value = (Number)variable.getIncrementedValue();
		Number newValue = (Number)expressionValue;

		if (newValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}

			return value;
		}

		if (value == null || variable.isInitialized())
		{
			value = JRShortIncrementerFactory.ZERO;
		}

		return new Short((short)(value.shortValue() + newValue.shortValue()));
	}

	
	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRShortAverageIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortAverageIncrementer mainInstance = new JRShortAverageIncrementer();

	/**
	 *
	 */
	private JRShortAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortAverageIncrementer getInstance()
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
		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			return variable.getValue();
		}
		Number countValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
		Number sumValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
		return new Short((short)(sumValue.shortValue() / countValue.shortValue()));
	}

	
	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRShortStandardDeviationIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortStandardDeviationIncrementer mainInstance = new JRShortStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRShortStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortStandardDeviationIncrementer getInstance()
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
		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			return variable.getValue(); 
		}
		Number varianceValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_VARIANCE));
		return new Short( (short)Math.sqrt(varianceValue.doubleValue()) );
	}

	
	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRShortVarianceIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRShortVarianceIncrementer mainInstance = new JRShortVarianceIncrementer();

	/**
	 *
	 */
	private JRShortVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRShortVarianceIncrementer getInstance()
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
		Number value = (Number)variable.getIncrementedValue();
		Number newValue = (Number)expressionValue;
		
		if (newValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			return value;
		}
		else if (value == null || variable.isInitialized())
		{
			return JRShortIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
			Number sumValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
			return
				new Short((short)(
					(countValue.shortValue() - 1) * value.shortValue() / countValue.shortValue() +
					( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) *
					( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) /
					(countValue.shortValue() - 1)
					));
		}
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		Number value = (Number)calculable.getIncrementedValue();
		
		if (calculableValue.getValue() == null)
		{
			if (calculable.isInitialized())
			{
				return null;
			}

			return value;
		}
		else if (value == null || calculable.isInitialized())
		{
			return new Short(((Number) calculableValue.getIncrementedValue()).shortValue());
		}

		float v1 = value.floatValue();
		float c1 = ((Number) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_COUNT))).floatValue();
		float s1 = ((Number) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_SUM))).floatValue();

		float v2 = ((Number) calculableValue.getIncrementedValue()).floatValue();
		float c2 = ((Number) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_COUNT))).floatValue();
		float s2 = ((Number) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_SUM))).floatValue();

		c1 -= c2;
		s1 -= s2;
		
		float c = c1 + c2;

		return new Short(
				(short) (
				c1 / c * v1 +
				c2 / c * v2 +
				c2 / c1 * s1 / c * s1 / c +
				c1 / c2 * s2 / c * s2 / c -
				2 * s1 / c * s2 /c
				));
	}

	
	public Object initialValue()
	{
		return JRShortIncrementerFactory.ZERO;
	}
}
