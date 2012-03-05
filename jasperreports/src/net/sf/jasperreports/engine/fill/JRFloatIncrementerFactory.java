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

import net.sf.jasperreports.engine.type.CalculationEnum;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public final class JRFloatIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	protected static final Float ZERO = new Float(0);


	/**
	 *
	 */
	private static JRFloatIncrementerFactory mainInstance = new JRFloatIncrementerFactory();


	/**
	 *
	 */
	public JRFloatIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRFloatIncrementerFactory getInstance()
	{
		return mainInstance;
	}


	/**
	 *
	 */
	public JRExtendedIncrementer getExtendedIncrementer(CalculationEnum calculation)
	{
		JRExtendedIncrementer incrementer = null;

		switch (calculation)
		{
			case COUNT :
			{
				incrementer = JRFloatCountIncrementer.getInstance();
				break;
			}
			case SUM :
			{
				incrementer = JRFloatSumIncrementer.getInstance();
				break;
			}
			case AVERAGE :
			{
				incrementer = JRFloatAverageIncrementer.getInstance();
				break;
			}
			case LOWEST :
			case HIGHEST :
			{
				incrementer = JRComparableIncrementerFactory.getInstance().getExtendedIncrementer(calculation);
				break;
			}
			case STANDARD_DEVIATION :
			{
				incrementer = JRFloatStandardDeviationIncrementer.getInstance();
				break;
			}
			case VARIANCE :
			{
				incrementer = JRFloatVarianceIncrementer.getInstance();
				break;
			}
			case DISTINCT_COUNT :
			{
				incrementer = JRFloatDistinctCountIncrementer.getInstance();
				break;
			}
			case SYSTEM :
			case NOTHING :
			case FIRST :
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
final class JRFloatCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatCountIncrementer mainInstance = new JRFloatCountIncrementer();

	/**
	 *
	 */
	private JRFloatCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatCountIncrementer getInstance()
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
			value = JRFloatIncrementerFactory.ZERO;
		}

		if (expressionValue == null)
		{
			return value;
		}

		return new Float(value.floatValue() + 1);
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		Number value = (Number)calculable.getIncrementedValue();
		Number combineValue = (Number) calculableValue.getValue();

		if (value == null || calculable.isInitialized())
		{
			value = JRFloatIncrementerFactory.ZERO;
		}

		if (combineValue == null)
		{
			return value;
		}

		return new Float(value.floatValue() + combineValue.floatValue());
	}

	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}


/**
 *
 */
final class JRFloatDistinctCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatDistinctCountIncrementer mainInstance = new JRFloatDistinctCountIncrementer();

	/**
	 *
	 */
	private JRFloatDistinctCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatDistinctCountIncrementer getInstance()
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

		return new Float(holder.getCount());
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		DistinctCountHolder holder = 
			(DistinctCountHolder)valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_COUNT));
		
		return new Float(holder.getCount());
	}
	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}


/**
 *
 */
final class JRFloatSumIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatSumIncrementer mainInstance = new JRFloatSumIncrementer();

	/**
	 *
	 */
	private JRFloatSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatSumIncrementer getInstance()
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
			value = JRFloatIncrementerFactory.ZERO;
		}

		return new Float(value.floatValue() + newValue.floatValue());
	}

	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}


/**
 *
 */
final class JRFloatAverageIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatAverageIncrementer mainInstance = new JRFloatAverageIncrementer();

	/**
	 *
	 */
	private JRFloatAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatAverageIncrementer getInstance()
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
		return new Float(sumValue.floatValue() / countValue.floatValue());
	}

	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}


/**
 *
 */
final class JRFloatStandardDeviationIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatStandardDeviationIncrementer mainInstance = new JRFloatStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRFloatStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatStandardDeviationIncrementer getInstance()
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
		return new Float( Math.sqrt(varianceValue.doubleValue()) );
	}

	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}


/**
 *
 */
final class JRFloatVarianceIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRFloatVarianceIncrementer mainInstance = new JRFloatVarianceIncrementer();

	/**
	 *
	 */
	private JRFloatVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRFloatVarianceIncrementer getInstance()
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
			return JRFloatIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
			Number sumValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
			return
				new Float(
					(countValue.floatValue() - 1) * value.floatValue() / countValue.floatValue() +
					( sumValue.floatValue() / countValue.floatValue() - newValue.floatValue() ) *
					( sumValue.floatValue() / countValue.floatValue() - newValue.floatValue() ) /
					(countValue.floatValue() - 1)
					);
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
			return new Float(((Number) calculableValue.getIncrementedValue()).floatValue());
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

		return new Float(
				c1 / c * v1 +
				c2 / c * v2 +
				c2 / c1 * s1 / c * s1 / c +
				c1 / c2 * s2 / c * s2 / c -
				2 * s1 / c * s2 /c
				);
	}

	
	public Object initialValue()
	{
		return JRFloatIncrementerFactory.ZERO;
	}
}
