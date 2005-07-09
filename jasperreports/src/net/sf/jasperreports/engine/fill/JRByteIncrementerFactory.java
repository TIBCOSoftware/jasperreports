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
public class JRByteIncrementerFactory implements JRIncrementerFactory
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
	public JRIncrementer getIncrementer(byte calculation)
	{
		JRIncrementer incrementer = null;

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
				incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(calculation);
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
class JRByteCountIncrementer implements JRIncrementer
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
				return JRByteIncrementerFactory.ZERO;
			}

			return value;
		}

		if (value == null || variable.isInitialized())
		{
			value = JRByteIncrementerFactory.ZERO;
		}

		return new Byte((byte)(value.byteValue() + 1));
	}
}


/**
 *
 */
class JRByteSumIncrementer implements JRIncrementer
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
			value = JRByteIncrementerFactory.ZERO;
		}

		return new Byte((byte)(value.byteValue() + newValue.byteValue()));
	}
}


/**
 *
 */
class JRByteAverageIncrementer implements JRIncrementer
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
		return new Byte((byte)(sumValue.byteValue() / countValue.byteValue()));
	}
}


/**
 *
 */
class JRByteStandardDeviationIncrementer implements JRIncrementer
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
		return new Byte( (byte)Math.sqrt(varianceValue.doubleValue()) );
	}
}


/**
 *
 */
class JRByteVarianceIncrementer implements JRIncrementer
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
			return JRByteIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return
				new Byte((byte)(
					(countValue.byteValue() - 1) * value.byteValue() / countValue.byteValue() +
					( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) *
					( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) /
					(countValue.byteValue() - 1)
					));
		}
	}
}
