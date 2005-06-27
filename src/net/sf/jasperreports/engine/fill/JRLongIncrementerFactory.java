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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRLongIncrementerFactory implements JRIncrementerFactory
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
	public JRIncrementer getIncrementer(byte calculation)
	{
		JRIncrementer incrementer = null;

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
				incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(calculation);
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
class JRLongCountIncrementer implements JRIncrementer
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
				return JRLongIncrementerFactory.ZERO;
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
				value = JRLongIncrementerFactory.ZERO;
			}

			return new Long(value.longValue() + 1);
		}
	}
}


/**
 *
 */
class JRLongSumIncrementer implements JRIncrementer
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
				value = JRLongIncrementerFactory.ZERO;
			}

			return new Long(value.longValue() + newValue.longValue());
		}
	}
}


/**
 *
 */
class JRLongAverageIncrementer implements JRIncrementer
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
			return new Long(sumValue.longValue() / countValue.longValue());
		}
	}
}


/**
 *
 */
class JRLongStandardDeviationIncrementer implements JRIncrementer
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
			return new Long( (long)Math.sqrt(varianceValue.doubleValue()) );
		}
	}
}


/**
 *
 */
class JRLongVarianceIncrementer implements JRIncrementer
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
			return JRLongIncrementerFactory.ZERO;
		}
		else
		{
			Number countValue = (Number)valueProvider.getValue((JRFillVariable)variable.getCountVariable());
			Number sumValue = (Number)valueProvider.getValue((JRFillVariable)variable.getSumVariable());
			return
				new Long(
					(countValue.longValue() - 1) * value.longValue() / countValue.longValue() +
					( sumValue.longValue() / countValue.longValue() - newValue.longValue() ) *
					( sumValue.longValue() / countValue.longValue() - newValue.longValue() ) /
					(countValue.longValue() - 1)
					);
		}
	}
}
