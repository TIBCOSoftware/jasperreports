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

import dori.jasper.engine.JRException;
import dori.jasper.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDoubleIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	private static JRDoubleIncrementerFactory mainInstance = new JRDoubleIncrementerFactory();


	/**
	 *
	 */
	private JRDoubleIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRDoubleIncrementerFactory getInstance()
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
				incrementer = JRDoubleCountIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_SUM :
			{
				incrementer = JRDoubleSumIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_AVERAGE :
			{
				incrementer = JRDoubleAverageIncrementer.getInstance();
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
				incrementer = JRDoubleStandardDeviationIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_VARIANCE :
			{
				incrementer = JRDoubleVarianceIncrementer.getInstance();
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
class JRDoubleCountIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDoubleCountIncrementer mainInstance = new JRDoubleCountIncrementer();

	/**
	 *
	 */
	private JRDoubleCountIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDoubleCountIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number value = (Number)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new Double(0);
		}

		Number newValue = null;
		if (expressionValue == null)
		{
			newValue = value;
		}
		else
		{
			newValue = new Double(value.doubleValue() + 1);
		}
		
		return newValue;
	}
}


/**
 *
 */
class JRDoubleSumIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDoubleSumIncrementer mainInstance = new JRDoubleSumIncrementer();

	/**
	 *
	 */
	private JRDoubleSumIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDoubleSumIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number value = (Number)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new Double(0);
		}
		Number newValue = (Number)expressionValue;
		if (newValue == null)
		{
			newValue = new Double(0);
		}
		newValue = new Double(value.doubleValue() + newValue.doubleValue());
					
		return newValue;
	}
}


/**
 *
 */
class JRDoubleAverageIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDoubleAverageIncrementer mainInstance = new JRDoubleAverageIncrementer();

	/**
	 *
	 */
	private JRDoubleAverageIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDoubleAverageIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number newValue = null;

		Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
		if (countValue.longValue() > 0)
		{
			Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
			newValue = new Double(sumValue.doubleValue() / countValue.doubleValue());
		}
					
		return newValue;
	}
}


/**
 *
 */
class JRDoubleStandardDeviationIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDoubleStandardDeviationIncrementer mainInstance = new JRDoubleStandardDeviationIncrementer();

	/**
	 *
	 */
	private JRDoubleStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDoubleStandardDeviationIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number varianceValue = (Number)((JRFillVariable)variable.getVarianceVariable()).getValue();
		Number newValue = new Double( Math.sqrt(varianceValue.doubleValue()) );
					
		return newValue;
	}
}


/**
 *
 */
class JRDoubleVarianceIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDoubleVarianceIncrementer mainInstance = new JRDoubleVarianceIncrementer();

	/**
	 *
	 */
	private JRDoubleVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDoubleVarianceIncrementer getInstance()
	{
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number value = (Number)variable.getValue();
		if (value == null || variable.isInitialized())
		{
			value = new Double(0);
		}
		Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
		Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
		Number newValue = (Number)expressionValue;
	
		if (countValue.intValue() == 1)
		{
			newValue = new Double(0);
		}
		else
		{
			newValue = new Double(
				(countValue.doubleValue() - 1) * value.doubleValue() / countValue.doubleValue() +
				( sumValue.doubleValue() / countValue.doubleValue() - newValue.doubleValue() ) *
				( sumValue.doubleValue() / countValue.doubleValue() - newValue.doubleValue() ) /
				(countValue.doubleValue() - 1)
				);
		}
					
		return newValue;
	}
}
