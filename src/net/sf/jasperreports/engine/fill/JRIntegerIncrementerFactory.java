/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRIntegerIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	protected static final Integer ZERO = new Integer(0);


	/**
	 *
	 */
	private static JRIntegerIncrementerFactory mainInstance = new JRIntegerIncrementerFactory();


	/**
	 *
	 */
	private JRIntegerIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRIntegerIncrementerFactory getInstance()
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
				incrementer = JRIntegerCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRIntegerSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRIntegerAverageIncrementer.getInstance();
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
				incrementer = JRIntegerStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRIntegerVarianceIncrementer.getInstance();
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
class JRIntegerCountIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRIntegerCountIncrementer mainInstance = new JRIntegerCountIncrementer();

	/**
	 *
	 */
	private JRIntegerCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRIntegerCountIncrementer getInstance()
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
		) throws JRException
	{
		Number value = (Number)variable.getValue();

		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return JRIntegerIncrementerFactory.ZERO;
			}
			else
			{
				return value;
			}
		}
		else
		{
			if (value == null || variable.isInitialized())
			{
				value = JRIntegerIncrementerFactory.ZERO;
			}

			return new Integer(value.intValue() + 1);
		}
	}
}


/**
 *
 */
class JRIntegerSumIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRIntegerSumIncrementer mainInstance = new JRIntegerSumIncrementer();

	/**
	 *
	 */
	private JRIntegerSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRIntegerSumIncrementer getInstance()
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
		) throws JRException
	{
		Number value = (Number)variable.getValue();
		Number newValue = (Number)expressionValue;

		if (newValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			else
			{
				return value;
			}
		}
		else
		{
			if (value == null || variable.isInitialized())
			{
				value = JRIntegerIncrementerFactory.ZERO;
			}

			return new Integer(value.intValue() + newValue.intValue());
		}
	}
}


/**
 *
 */
class JRIntegerAverageIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRIntegerAverageIncrementer mainInstance = new JRIntegerAverageIncrementer();

	/**
	 *
	 */
	private JRIntegerAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRIntegerAverageIncrementer getInstance()
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
		) throws JRException
	{
		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			else
			{
				return variable.getValue();
			}
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return new Integer(sumValue.intValue() / countValue.intValue());
		}
	}
}


/**
 *
 */
class JRIntegerStandardDeviationIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRIntegerStandardDeviationIncrementer mainInstance = new JRIntegerStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRIntegerStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRIntegerStandardDeviationIncrementer getInstance()
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
		) throws JRException
	{
		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			else
			{
				return variable.getValue(); 
			}
		}
		else
		{
			Number varianceValue = (Number)valueProvider.getValue((JRFillVariable)variable.getVarianceVariable());
			return new Integer( (int)Math.sqrt(varianceValue.doubleValue()) );
		}
	}
}


/**
 *
 */
class JRIntegerVarianceIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRIntegerVarianceIncrementer mainInstance = new JRIntegerVarianceIncrementer();

	/**
	 *
	 */
	private JRIntegerVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRIntegerVarianceIncrementer getInstance()
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
		) throws JRException
	{
		Number value = (Number)variable.getValue();
		Number newValue = (Number)expressionValue;
		
		if (newValue == null)
		{
			if (variable.isInitialized())
			{
				return null;
			}
			else
			{
				return value;
			}
		}
		else if (value == null || variable.isInitialized())
		{
			return JRIntegerIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return
				new Integer(
					(countValue.intValue() - 1) * value.intValue() / countValue.intValue() +
					( sumValue.intValue() / countValue.intValue() - newValue.intValue() ) *
					( sumValue.intValue() / countValue.intValue() - newValue.intValue() ) /
					(countValue.intValue() - 1)
					);
		}
	}
}
