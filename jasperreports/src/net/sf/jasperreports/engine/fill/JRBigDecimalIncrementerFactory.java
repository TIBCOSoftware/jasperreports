/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
package dori.jasper.engine.fill;

import java.math.BigDecimal;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBigDecimalIncrementerFactory implements JRIncrementerFactory
{


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
	public JRIncrementer getIncrementer(byte calculation)
	{
		JRIncrementer incrementer = null;

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
				incrementer = JRComparableIncrementerFactory.getInstance().getIncrementer(calculation);
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
			{
				incrementer = JRDefaultSystemIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_NOTHING :
			default :
			{
				incrementer = JRDefaultNothingIncrementer.getInstance();
				break;
			}
		}
		
		return incrementer;
	}


}


/**
 *
 */
class JRBigDecimalCountIncrementer implements JRIncrementer
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
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		BigDecimal value = (BigDecimal)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new BigDecimal("0");
		}
		BigDecimal newValue = null;
		if (expressionValue == null)
		{
			newValue = value;
		}
		else
		{
			newValue = value.add(new BigDecimal("1"));
		}
					
		return newValue;
	}
}


/**
 *
 */
class JRBigDecimalSumIncrementer implements JRIncrementer
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
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		BigDecimal value = (BigDecimal)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new BigDecimal("0");
		}
		BigDecimal newValue = (BigDecimal)expressionValue;
		if (newValue == null)
		{
			newValue = new BigDecimal("0");
		}
		newValue = value.add(newValue);
					
		return newValue;
	}
}


/**
 *
 */
class JRBigDecimalAverageIncrementer implements JRIncrementer
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
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		BigDecimal newValue = null;

		long count = ((java.lang.Number)((JRFillVariable)variable.getCountVariable()).getValue()).longValue();
		if (count > 0)
		{
			BigDecimal countValue = BigDecimal.valueOf(count);
			BigDecimal sumValue = (BigDecimal)((JRFillVariable)variable.getSumVariable()).getValue();
			newValue = sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP);
		}
					
		return newValue;
	}
}


/**
 *
 */
class JRBigDecimalStandardDeviationIncrementer implements JRIncrementer
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
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		BigDecimal varianceValue = (BigDecimal)((JRFillVariable)variable.getVarianceVariable()).getValue();
		BigDecimal newValue = new BigDecimal( Math.sqrt(varianceValue.doubleValue()) );
					
		return newValue;
	}
}


/**
 *
 */
class JRBigDecimalVarianceIncrementer implements JRIncrementer
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
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		BigDecimal value = (BigDecimal)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new BigDecimal("0");
		}
		BigDecimal countValue = BigDecimal.valueOf( ((java.lang.Number)((JRFillVariable)variable.getCountVariable()).getValue()).longValue() );
		BigDecimal sumValue = (BigDecimal)((JRFillVariable)variable.getSumVariable()).getValue();
		BigDecimal newValue = (BigDecimal)expressionValue;
	
		if (countValue.intValue() == 1)
		{
			newValue = new BigDecimal("0");
		}
		else
		{
			newValue = 
				countValue.subtract(new BigDecimal("1")).multiply(value).divide(countValue, BigDecimal.ROUND_HALF_UP).add(
					sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue).multiply(
						sumValue.divide(countValue, BigDecimal.ROUND_HALF_UP).subtract(newValue)
						).divide(countValue.subtract(new BigDecimal("1")), BigDecimal.ROUND_HALF_UP)
					);
		}
					
		return newValue;
	}
}
