/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
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
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.fill;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDistinctCountExtendedIncrementerFactory extends JRAbstractExtendedIncrementerFactory
{


	/**
	 *
	 */
	private static JRDistinctCountExtendedIncrementerFactory mainInstance = new JRDistinctCountExtendedIncrementerFactory();


	/**
	 *
	 */
	public JRDistinctCountExtendedIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRDistinctCountExtendedIncrementerFactory getInstance()
	{
		return mainInstance;
	}


	/**
	 *
	 */
	public JRExtendedIncrementer getExtendedIncrementer(byte calculation)
	{
		return new JRDistinctCountExtendedIncrementer();
	}


	public static JRExtendedIncrementerFactory getFactory (Class valueClass)
	{
		return JRDistinctCountExtendedIncrementerFactory.getInstance();
	}
}


/**
 *
 */
class JRDistinctCountExtendedIncrementer extends JRAbstractExtendedIncrementer
{

	private DistinctCountHolder lastHolder = new DistinctCountHolder();

	
	/**
	 *
	 */
	public JRDistinctCountExtendedIncrementer()
	{
	}


	/**
	 *
	 */
	public Object increment(
		JRCalculable variable, 
		Object expressionValue,
		AbstractValueProvider valueProvider
		)
	{
		DistinctCountHolder holder  = (DistinctCountHolder)variable.getIncrementedValue();

		if (holder == null)
		{
			holder = lastHolder;
		}
		else
		{
			lastHolder = holder;
		}
		
		holder.addLastValue();

		return new DistinctCountHolder(holder, expressionValue);
	}


	/**
	 *
	 */
	public Object combine(JRCalculable calculable1, JRCalculable calculable2, AbstractValueProvider valueProvider)
	{
		Set distinctValues = new HashSet();
		
		DistinctCountHolder holder1  = (DistinctCountHolder)calculable1.getValue();
		if (holder1 != null)
		{
			distinctValues.addAll(holder1.getDistinctValues());
			if (holder1.getLastValue() != null)
				distinctValues.add(holder1.getLastValue());
		}
		
		DistinctCountHolder holder2  = (DistinctCountHolder)calculable2.getValue();
		if (holder2 != null)
		{
			distinctValues.addAll(holder2.getDistinctValues());
			if (holder2.getLastValue() != null)
				distinctValues.add(holder2.getLastValue());
		}
		
		return new DistinctCountHolder(distinctValues);
	}


	/**
	 *
	 */
	public Object initialValue()
	{
		return null;
	}


}
