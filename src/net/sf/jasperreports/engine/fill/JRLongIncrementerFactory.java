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
public class JRLongIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	protected static final Long ZERO = new Long(0);


	/**
	 *
	 */
	private static JRLongIncrementerFactory mainInstance = new JRLongIncrementerFactory();


	/**
	 *
	 */
	private JRLongIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRLongIncrementerFactory getInstance()
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
				incrementer = JRLongCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRLongSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRLongAverageIncrementer.getInstance();
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
				incrementer = JRLongStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRLongVarianceIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_DISTINCT_COUNT :
			{
				incrementer = JRLongDistinctCountIncrementer.getInstance();
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
class JRLongCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongCountIncrementer mainInstance = new JRLongCountIncrementer();

	/**
	 *
	 */
	private JRLongCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongCountIncrementer getInstance()
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
			value = JRLongIncrementerFactory.ZERO;
		}

		if (expressionValue == null)
		{
			return value;
		}

		return new Long(value.longValue() + 1);
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		Number value = (Number)calculable.getIncrementedValue();
		Number combineValue = (Number) calculableValue.getValue();

		if (value == null || calculable.isInitialized())
		{
			value = JRLongIncrementerFactory.ZERO;
		}

		if (combineValue == null)
		{
			return value;
		}

		return new Long(value.longValue() + combineValue.longValue());
	}

	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRLongDistinctCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongDistinctCountIncrementer mainInstance = new JRLongDistinctCountIncrementer();

	/**
	 *
	 */
	private JRLongDistinctCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongDistinctCountIncrementer getInstance()
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

		return new Long(holder.getCount());
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider)
	{
		DistinctCountHolder holder = 
			(DistinctCountHolder)valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_COUNT));
		
		return new Long(holder.getCount());
	}
	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRLongSumIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongSumIncrementer mainInstance = new JRLongSumIncrementer();

	/**
	 *
	 */
	private JRLongSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongSumIncrementer getInstance()
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
			value = JRLongIncrementerFactory.ZERO;
		}

		return new Long(value.longValue() + newValue.longValue());
	}

	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRLongAverageIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongAverageIncrementer mainInstance = new JRLongAverageIncrementer();

	/**
	 *
	 */
	private JRLongAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongAverageIncrementer getInstance()
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
		return new Long(sumValue.longValue() / countValue.longValue());
	}

	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRLongStandardDeviationIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongStandardDeviationIncrementer mainInstance = new JRLongStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRLongStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongStandardDeviationIncrementer getInstance()
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
		return new Long( (long)Math.sqrt(varianceValue.doubleValue()) );
	}

	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRLongVarianceIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRLongVarianceIncrementer mainInstance = new JRLongVarianceIncrementer();

	/**
	 *
	 */
	private JRLongVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRLongVarianceIncrementer getInstance()
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
			return JRLongIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
			Number sumValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
			return
				new Long(
					(countValue.longValue() - 1) * value.longValue() / countValue.longValue() +
					( sumValue.longValue() / countValue.longValue() - newValue.longValue() ) *
					( sumValue.longValue() / countValue.longValue() - newValue.longValue() ) /
					(countValue.longValue() - 1)
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
			return new Long(((Number) calculableValue.getIncrementedValue()).longValue());
		}

		double v1 = value.doubleValue();
		double c1 = ((Number) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_COUNT))).doubleValue();
		double s1 = ((Number) valueProvider.getValue(calculable.getHelperVariable(JRCalculable.HELPER_SUM))).doubleValue();

		double v2 = ((Number) calculableValue.getIncrementedValue()).doubleValue();
		double c2 = ((Number) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_COUNT))).doubleValue();
		double s2 = ((Number) valueProvider.getValue(calculableValue.getHelperVariable(JRCalculable.HELPER_SUM))).doubleValue();

		c1 -= c2;
		s1 -= s2;
		
		double c = c1 + c2;

		return new Long(
				(long) (
				c1 / c * v1 +
				c2 / c * v2 +
				c2 / c1 * s1 / c * s1 / c +
				c1 / c2 * s2 / c * s2 / c -
				2 * s1 / c * s2 /c
				));
	}

	
	public Object initialValue()
	{
		return JRLongIncrementerFactory.ZERO;
	}
}
