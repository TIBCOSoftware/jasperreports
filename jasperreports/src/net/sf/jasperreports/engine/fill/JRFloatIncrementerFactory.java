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
package net.sf.jasperreports.engine.fill;

import net.sf.jasperreports.engine.JRException;
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
		) throws JRException
	{
		Number value = (Number)variable.getValue();

		if (expressionValue == null)
		{
			if (variable.isInitialized())
			{
				return JRFloatIncrementerFactory.ZERO;
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
				value = JRFloatIncrementerFactory.ZERO;
			}

			return new Float(value.floatValue() + 1);
		}
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
				value = JRFloatIncrementerFactory.ZERO;
			}

			return new Float(value.floatValue() + newValue.floatValue());
		}
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
			return new Float(sumValue.floatValue() / countValue.floatValue());
		}
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
			return new Float( Math.sqrt(varianceValue.doubleValue()) );
		}
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
