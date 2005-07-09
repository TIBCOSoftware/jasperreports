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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFloatIncrementerFactory implements JRIncrementerFactory
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
	private JRFloatIncrementerFactory()
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
	public JRIncrementer getIncrementer(byte calculation)
	{
		JRIncrementer incrementer = null;

		switch (calculation)
		{
			case JRVariable.CALCULATION_COUNT :
			{
				incrementer = JRFloatCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRFloatSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRFloatAverageIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_LOWEST :
			case JRVariable.CALCULATION_HIGHEST :
			{
				incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(calculation);
				break;
			}
			case JRVariable.CALCULATION_STANDARD_DEVIATION :
			{
				incrementer = JRFloatStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRFloatVarianceIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SYSTEM :
			case JRVariable.CALCULATION_NOTHING :
			default :
			{
				incrementer = JRDefaultIncrementerFactory.getInstance().getIncrementer(calculation);
				break;
			}
		}
		
		return incrementer;
	}


}


/**
 *
 */
class JRFloatCountIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		)
	{
		Number value = (Number)variable.getIncrementedValue();

		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return JRFloatIncrementerFactory.ZERO;
			}

			return value;
		}

		if (value == null || variable.isInitialized())
		{
			value = JRFloatIncrementerFactory.ZERO;
		}

		return new Float(value.floatValue() + 1);
	}
}


/**
 *
 */
class JRFloatSumIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
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
}


/**
 *
 */
class JRFloatAverageIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
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
		Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
		Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
		return new Float(sumValue.floatValue() / countValue.floatValue());
	}
}


/**
 *
 */
class JRFloatStandardDeviationIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
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
		Number varianceValue = (Number)valueProvider.getValue((JRFillVariable)variable.getVarianceVariable());
		return new Float( Math.sqrt(varianceValue.doubleValue()) );
	}
}


/**
 *
 */
class JRFloatVarianceIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
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
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return
				new Float(
					(countValue.floatValue() - 1) * value.floatValue() / countValue.floatValue() +
					( sumValue.floatValue() / countValue.floatValue() - newValue.floatValue() ) *
					( sumValue.floatValue() / countValue.floatValue() - newValue.floatValue() ) /
					(countValue.floatValue() - 1)
					);
		}
	}
}
