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
public class JRShortIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	private static JRShortIncrementerFactory mainInstance = null;


	/**
	 *
	 */
	private JRShortIncrementerFactory()
	{
	}


	/**
	 *
	 */
	protected static JRShortIncrementerFactory getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortIncrementerFactory();
		}
		
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
class JRShortCountIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRShortCountIncrementer mainInstance = null;

	/**
	 *
	 */
	private JRShortCountIncrementer()
	{
	}

	/**
	 *
	 */
	protected static JRShortCountIncrementer getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortCountIncrementer();
		}
		
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
			value = new Short((short)0);
		}

		Number newValue = null;
		if (expressionValue == null)
		{
			newValue = value;
		}
		else
		{
			newValue = new Short((short)(value.shortValue() + 1));
		}
		
		return newValue;
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
	private static JRShortSumIncrementer mainInstance = null;

	/**
	 *
	 */
	private JRShortSumIncrementer()
	{
	}

	/**
	 *
	 */
	protected static JRShortSumIncrementer getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortSumIncrementer();
		}
		
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
			value = new Short((short)0);
		}
		Number newValue = (Number)expressionValue;
		if (newValue == null)
		{
			newValue = new Short((short)0);
		}
		newValue = new Short((short)(value.shortValue() + newValue.shortValue()));
					
		return newValue;
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
	private static JRShortAverageIncrementer mainInstance = null;

	/**
	 *
	 */
	private JRShortAverageIncrementer()
	{
	}

	/**
	 *
	 */
	protected static JRShortAverageIncrementer getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortAverageIncrementer();
		}
		
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
			newValue = new Short((short)(sumValue.shortValue() / countValue.shortValue()));
		}
					
		return newValue;
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
	private static JRShortStandardDeviationIncrementer mainInstance = null;

	/**
	 *
	 */
	private JRShortStandardDeviationIncrementer()
	{
	}

	/**
	 *
	 */
	protected static JRShortStandardDeviationIncrementer getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortStandardDeviationIncrementer();
		}
		
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number varianceValue = (Number)((JRFillVariable)variable.getVarianceVariable()).getValue();
		Number newValue = new Short( (short)Math.sqrt(varianceValue.doubleValue()) );
					
		return newValue;
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
	private static JRShortVarianceIncrementer mainInstance = null;

	/**
	 *
	 */
	private JRShortVarianceIncrementer()
	{
	}

	/**
	 *
	 */
	protected static JRShortVarianceIncrementer getInstance()
	{
		if (mainInstance == null)
		{
			mainInstance = new JRShortVarianceIncrementer();
		}
		
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
			value = new Short((short)0);
		}
		Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
		Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
		Number newValue = (Number)expressionValue;
	
		if (countValue.intValue() == 1)
		{
			newValue = new Short((short)0);
		}
		else
		{
			newValue = new Short((short)(
				(countValue.shortValue() - 1) * value.shortValue() / countValue.shortValue() +
				( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) *
				( sumValue.shortValue() / countValue.shortValue() - newValue.shortValue() ) /
				(countValue.shortValue() - 1)
				));
		}
					
		return newValue;
	}
}
