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
public class JRDefaultIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	private static JRDefaultIncrementerFactory mainInstance = new JRDefaultIncrementerFactory();


	/**
	 *
	 */
	private JRDefaultIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRDefaultIncrementerFactory getInstance()
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
			case JRVariable.CALCULATION_SYSTEM :
			{
				incrementer = JRDefaultSystemIncrementer.getInstance();
				break;
			}
			case JRVariable.CALCULATION_NOTHING :
			case JRVariable.CALCULATION_COUNT :
			case JRVariable.CALCULATION_SUM :
			case JRVariable.CALCULATION_AVERAGE :
			case JRVariable.CALCULATION_LOWEST :
			case JRVariable.CALCULATION_HIGHEST :
			case JRVariable.CALCULATION_STANDARD_DEVIATION :
			case JRVariable.CALCULATION_VARIANCE :
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
class JRDefaultNothingIncrementer implements JRIncrementer
{


	/**
	 *
	 */
	private static JRDefaultNothingIncrementer mainInstance = new JRDefaultNothingIncrementer();


	/**
	 *
	 */
	private JRDefaultNothingIncrementer()
	{
	}


	/**
	 *
	 */
	public static JRDefaultNothingIncrementer getInstance()
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
		return expressionValue;
	}


}


/**
 *
 */
class JRDefaultSystemIncrementer implements JRIncrementer
{
	/**
	 *
	 */
	private static JRDefaultSystemIncrementer mainInstance = new JRDefaultSystemIncrementer();

	/**
	 *
	 */
	private JRDefaultSystemIncrementer()
	{
	}

	/**
	 *
	 */
	public static JRDefaultSystemIncrementer getInstance()
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
		return variable.getValue();
	}
}
