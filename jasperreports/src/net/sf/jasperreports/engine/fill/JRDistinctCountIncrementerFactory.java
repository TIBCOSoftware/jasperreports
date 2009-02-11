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

import net.sf.jasperreports.engine.JRVariable;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRDistinctCountIncrementerFactory implements JRIncrementerFactory
{


	/**
	 *
	 */
	private static JRDistinctCountIncrementerFactory mainInstance = new JRDistinctCountIncrementerFactory();


	/**
	 *
	 */
	public JRDistinctCountIncrementerFactory()
	{
	}


	/**
	 *
	 */
	public static JRDistinctCountIncrementerFactory getInstance()
	{
		return mainInstance;
	}


	/**
	 *
	 */
	public JRIncrementer getIncrementer(byte calculation)
	{
		return new JRDistinctCountIncrementer();
	}
}


/**
 *
 */
class JRDistinctCountIncrementer implements JRIncrementer
{

	private DistinctCountHolder lastHolder = new DistinctCountHolder();
	
	
	/**
	 *
	 */
	public JRDistinctCountIncrementer()
	{
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
		DistinctCountHolder holder  = (DistinctCountHolder)variable.getIncrementedValue();

		if (holder == null)
		{
			holder = lastHolder;
		}
		else
		{
			lastHolder = holder;
		}
		
		if (variable.getResetType() == JRVariable.RESET_TYPE_REPORT || variable.isInitialized())
		{
			holder.addLastValue();
		}

		return new DistinctCountHolder(holder, expressionValue);
	}


}


/**
 *
 */
class DistinctCountHolder
{
	private Set distinctValues = null;
	private Object lastValue = null;

	public DistinctCountHolder()
	{
		distinctValues = new HashSet();
	}

	public DistinctCountHolder(Set distinctValues)
	{
		this.distinctValues = distinctValues;
	}

	public DistinctCountHolder(DistinctCountHolder holder, Object lastValue)
	{
		this.distinctValues = holder.getDistinctValues();
		this.lastValue = lastValue;
	}

	public void init()
	{
		distinctValues = new HashSet();
	}

	public Set getDistinctValues()
	{
		return distinctValues;
	}

	public Object getLastValue()
	{
		return lastValue;
	}

	public void addLastValue()
	{
		if (lastValue != null)
		{
			distinctValues.add(lastValue);
		}
		lastValue = null;
	}

	public long getCount()
	{
		return distinctValues.size() + (lastValue == null || distinctValues.contains(lastValue) ? 0 : 1);
	}
}
