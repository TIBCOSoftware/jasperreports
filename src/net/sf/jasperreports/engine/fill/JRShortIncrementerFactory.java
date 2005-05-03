/*
 * ============================================================================
 *                   GNU Lesser General Public License
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRShortIncrementerFactory implements JRIncrementerFactory
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
	public JRIncrementer getIncrementer(byte calculation)
	{
		JRIncrementer incrementer = null;

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
				incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(calculation);
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
class JRShortCountIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		) throws JRException
	{
		Number value = (Number)variable.getIncrementedValue();

		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return JRShortIncrementerFactory.ZERO;
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
				value = JRShortIncrementerFactory.ZERO;
			}

			return new Short((short)(value.shortValue() + 1));
		}
	}
}


/**
 *
 */
class JRShortSumIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		) throws JRException
	{
		Number value = (Number)variable.getIncrementedValue();
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
				value = JRShortIncrementerFactory.ZERO;
			}

			return new Short((short)(value.shortValue() + newValue.shortValue()));
		}
	}
}


/**
 *
 */
class JRShortAverageIncrementer implements JRIncrementer
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
			return new Short((short)(sumValue.shortValue() / countValue.shortValue()));
		}
	}
}


/**
 *
 */
class JRShortStandardDeviationIncrementer implements JRIncrementer
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
			return new Short( (short)Math.sqrt(varianceValue.doubleValue()) );
		}
	}
}


/**
 *
 */
class JRShortVarianceIncrementer implements JRIncrementer
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
		JRFillVariable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		) throws JRException
	{
		Number value = (Number)variable.getIncrementedValue();
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
			return JRShortIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return
				new Short((short)(
					(countValue.shortValue() - 1) * value.shortValue() / countValue.shortValue() +
					( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) *
					( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) /
					(countValue.shortValue() - 1)
					));
		}
	}
}
