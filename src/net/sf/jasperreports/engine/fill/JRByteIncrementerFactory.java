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
public class JRByteIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	private static JRByteIncrementerFactory mainInstance = null;


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
		if (mainInstance == null)
		{
			mainInstance = new JRByteIncrementerFactory();
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
class JRByteCountIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRByteCountIncrementer mainInstance = null;

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
		if (mainInstance == null)
		{
			mainInstance = new JRByteCountIncrementer();
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
			value = new Byte((byte)0);
		}

		Number newValue = null;
		if (expressionValue == null)
		{
			newValue = value;
		}
		else
		{
			newValue = new Byte((byte)(value.byteValue() + 1));
		}
		
		return newValue;
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
	private static JRByteSumIncrementer mainInstance = null;

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
		if (mainInstance == null)
		{
			mainInstance = new JRByteSumIncrementer();
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
			value = new Byte((byte)0);
		}
		Number newValue = (Number)expressionValue;
		if (newValue == null)
		{
			newValue = new Byte((byte)0);
		}
		newValue = new Byte((byte)(value.byteValue() + newValue.byteValue()));
					
		return newValue;
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
	private static JRByteAverageIncrementer mainInstance = null;

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
		if (mainInstance == null)
		{
			mainInstance = new JRByteAverageIncrementer();
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
			newValue = new Byte((byte)(sumValue.byteValue() / countValue.byteValue()));
		}
					
		return newValue;
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
	private static JRByteStandardDeviationIncrementer mainInstance = null;

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
		if (mainInstance == null)
		{
			mainInstance = new JRByteStandardDeviationIncrementer();
		}
		
		return mainInstance;
	}

	/**
	 *
	 */
	public Object increment(JRFillVariable variable, Object expressionValue) throws JRException
	{
		Number varianceValue = (Number)((JRFillVariable)variable.getVarianceVariable()).getValue();
		Number newValue = new Byte( (byte)Math.sqrt(varianceValue.doubleValue()) );
					
		return newValue;
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
	private static JRByteVarianceIncrementer mainInstance = null;

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
		if (mainInstance == null)
		{
			mainInstance = new JRByteVarianceIncrementer();
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
			value = new Byte((byte)0);
		}
		Number countValue = (Number)((JRFillVariable)variable.getCountVariable()).getValue();
		Number sumValue = (Number)((JRFillVariable)variable.getSumVariable()).getValue();
		Number newValue = (Number)expressionValue;
	
		if (countValue.intValue() == 1)
		{
			newValue = new Byte((byte)0);
		}
		else
		{
			newValue = new Byte((byte)(
				(countValue.byteValue() - 1) * value.byteValue() / countValue.byteValue() +
				( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) *
				( sumValue.byteValue() / countValue.byteValue() - newValue.byteValue() ) /
				(countValue.byteValue() - 1)
				));
		}
					
		return newValue;
	}
}
