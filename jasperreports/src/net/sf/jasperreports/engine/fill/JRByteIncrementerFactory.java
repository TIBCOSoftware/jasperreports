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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRByteIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	protected static final Byte ZERO = new Byte((byte)0);


	/**
	 *
	 */
	private static JRByteIncrementerFactory mainInstance = new JRByteIncrementerFactory();


	/**
	 *
	 */
	private JRByteIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRByteIncrementerFactory getInstance()
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
				incrementer = JRByteCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRByteSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRByteAverageIncrementer.getInstance();
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
				incrementer = JRByteStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRByteVarianceIncrementer.getInstance();
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
class JRByteCountIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRByteCountIncrementer mainInstance = new JRByteCountIncrementer();

	/**
	 *
	 */
	private JRByteCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRByteCountIncrementer getInstance()
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
			value = JRByteIncrementerFactory.ZERO;
		}

		if (expressionValue == null)
		{
			return value;
		}

		return new Byte((byte)(value.byteValue() + 1));
	}

	
	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider) throws JRException
	{
		Number value = (Number)calculable.getIncrementedValue();
		Number combineValue = (Number) calculableValue.getValue();

		if (value == null || calculable.isInitialized())
		{
			value = JRByteIncrementerFactory.ZERO;
		}

		if (combineValue == null)
		{
			return value;
		}

		return new Byte((byte) (value.byteValue() + combineValue.byteValue()));
	}

	
	public Object initialValue()
	{
		return JRByteIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRByteSumIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRByteSumIncrementer mainInstance = new JRByteSumIncrementer();

	/**
	 *
	 */
	private JRByteSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRByteSumIncrementer getInstance()
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
			value = JRByteIncrementerFactory.ZERO;
		}

		return new Byte((byte)(value.byteValue() + newValue.byteValue()));
	}

	
	public Object initialValue()
	{
		return JRByteIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRByteAverageIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRByteAverageIncrementer mainInstance = new JRByteAverageIncrementer();

	/**
	 *
	 */
	private JRByteAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRByteAverageIncrementer getInstance()
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
		return new Byte((byte)(sumValue.byteValue() / countValue.byteValue()));
	}

	
	public Object initialValue()
	{
		return JRByteIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRByteStandardDeviationIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRByteStandardDeviationIncrementer mainInstance = new JRByteStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRByteStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRByteStandardDeviationIncrementer getInstance()
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
		return new Byte( (byte)Math.sqrt(varianceValue.doubleValue()) );
	}

	
	public Object initialValue()
	{
		return JRByteIncrementerFactory.ZERO;
	}
}


/**
 *
 */
class JRByteVarianceIncrementer extends JRAbstractExtendedIncrementer
{
	/**
	 *
	 */
	private static JRByteVarianceIncrementer mainInstance = new JRByteVarianceIncrementer();

	/**
	 *
	 */
	private JRByteVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRByteVarianceIncrementer getInstance()
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
			return JRByteIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_COUNT));
			Number sumValue = (Number)valueProvider.getValue(variable.getHelperVariable(JRCalculable.HELPER_SUM));
			return
				new Byte((byte)(
					(countValue.byteValue() - 1) * value.byteValue() / countValue.byteValue() +
					( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) *
					( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) /
					(countValue.byteValue() - 1)
					));
		}
	}

	public Object combine(JRCalculable calculable, JRCalculable calculableValue, AbstractValueProvider valueProvider) throws JRException
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
			return new Byte(((Number) calculableValue.getIncrementedValue()).byteValue());
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

		return new Byte((byte) (
				c1 / c * v1 +
				c2 / c * v2 +
				c2 / c1 * s1 / c * s1 / c +
				c1 / c2 * s2 / c * s2 / c -
				2 * s1 / c * s2 /c
				));
	}

	
	public Object initialValue()
	{
		return JRByteIncrementerFactory.ZERO;
	}
}
