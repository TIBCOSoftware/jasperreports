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

import java.math.BigDecimal;

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBigDecimalIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	protected static final BigDecimal ZERO = new BigDecimal(0);
	protected static final BigDecimal ONE = new BigDecimal(1);


	/**
	 *
	 */
	private static JRBigDecimalIncrementerFactory mainInstance = new JRBigDecimalIncrementerFactory();


	/**
	 *
	 */
	private JRBigDecimalIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRBigDecimalIncrementerFactory getInstance()
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
				incrementer = JRBigDecimalCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRBigDecimalSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRBigDecimalAverageIncrementer.getInstance();
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
				incrementer = JRBigDecimalStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRBigDecimalVarianceIncrementer.getInstance();
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
class JRBigDecimalCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRBigDecimalCountIncrementer mainInstance = new JRBigDecimalCountIncrementer();

	/**
	 *
	 */
	private JRBigDecimalCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRBigDecimalCountIncrementer getInstance()
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
		BigDecimal value = (BigDecimal)variable.getIncrementedValue();

		if (value == null || variable.isInitialized())
		{
			value = JRBigDecimalIncrementerFactory.ZERO;
		}

		if (expressionValue == null)
		{
			return value;
		}

		return value.add(JRBigDecimalIncrementerFactory.ONE);
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		BigDecimal value = (BigDecimal)calculable.getIncrementedValue();
		BigDecimal combineValue = (BigDecimal) calculableValue.getValue();

		if (value == null || calculable.isInitialized())
		{
			value = JRBigDecimalIncrementerFactory.ZERO;
		}

		if (combineValue == null)
		{
			return value;
		}

		return value.add(combineValue);
	}

	
	public Object initialValue()
	{
		return JRBigDecimalIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRBigDecimalSumIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRBigDecimalSumIncrementer mainInstance = new JRBigDecimalSumIncrementer();

	/**
	 *
	 */
	private JRBigDecimalSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRBigDecimalSumIncrementer getInstance()
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
		BigDecimal value = (BigDecimal)variable.getIncrementedValue();
		BigDecimal newValue = (BigDecimal)expressionValue;

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
			value = JRBigDecimalIncrementerFactory.ZERO;
		}

		return value.add(newValue);
	}

	
	public Object initialValue()
	{
		return JRBigDecimalIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRBigDecimalAverageIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRBigDecimalAverageIncrementer mainInstance = new JRBigDecimalAverageIncrementer();

	/**
	 *
	 */
	private JRBigDecimalAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRBigDecimalAverageIncrementer getInstance()
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
		BigDecimal countValue = (BigDecimal)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
		BigDecimal sumValue = (BigDecimal)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
		return sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP);
	}

	
	public Object initialValue()
	{
		return JRBigDecimalIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRBigDecimalStandardDeviationIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRBigDecimalStandardDeviationIncrementer mainInstance = new JRBigDecimalStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRBigDecimalStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRBigDecimalStandardDeviationIncrementer getInstance()
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
		return new BigDecimal( Math.sqrt(varianceValue.doubleValue()) );
	}

	
	public Object initialValue()
	{
		return JRBigDecimalIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRBigDecimalVarianceIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRBigDecimalVarianceIncrementer mainInstance = new JRBigDecimalVarianceIncrementer();

	/**
	 *
	 */
	private JRBigDecimalVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRBigDecimalVarianceIncrementer getInstance()
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
		BigDecimal value = (BigDecimal)variable.getIncrementedValue();
		BigDecimal newValue = (BigDecimal)expressionValue;
		
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
			return JRBigDecimalIncrementerFactory.ZERO;
		}
		else
		{
			BigDecimal countValue = (BigDecimal)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
			BigDecimal sumValue = (BigDecimal)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
			return
				countValue.subtract(JRBigDecimalIncrementerFactory.ONE).multiply(value).divide(countValue, BigDecimal.ROUND_HALF_UP).add(
					sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue).multiply(
						sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue)
						).divide(countValue.subtract(JRBigDecimalIncrementerFactory.ONE), BigDecimal.ROUND_HALF_UP)
					);
		}
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		BigDecimal value = (BigDecimal)calculable.getIncrementedValue();
		
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
			return (BigDecimal) calculableValue.getIncrementedValue();
		}

		BigDecimal v1 = value;
		BigDecimal c1 = (BigDecimal) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_COUNT));
		BigDecimal s1 = (BigDecimal) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_SUM));

		BigDecimal v2 = (BigDecimal) calculableValue.getIncrementedValue();
		BigDecimal c2 = (BigDecimal) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_COUNT));
		BigDecimal s2 = (BigDecimal) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_SUM));

		c1 = c1.subtract(c2);
		s1 = s1.subtract(s2);
		
		BigDecimal c = c1.add(c2);
		
		BigDecimal x1 = s1.divide(c, BigDecimal.ROUND_HALF_UP);
		BigDecimal x2 = s2.divide(c, BigDecimal.ROUND_HALF_UP);
		BigDecimal x3 = x1.multiply(x2);
		
		return c1.divide(c, BigDecimal.ROUND_HALF_UP).multiply(v1)
			.add(c2.divide(c, BigDecimal.ROUND_HALF_UP).multiply(v2))
			.add(c2.divide(c1, BigDecimal.ROUND_HALF_UP).multiply(x1).multiply(x1))
			.add(c1.divide(c2, BigDecimal.ROUND_HALF_UP).multiply(x2).multiply(x2))
			.subtract(x3).subtract(x3);
	}

	
	public Object initialValue()
	{
		return JRBigDecimalIncrementerFactory.ZERO;
	}
}
