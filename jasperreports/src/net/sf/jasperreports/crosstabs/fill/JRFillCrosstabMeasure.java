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
package net.sf.jasperreports.crosstabs.fill;

import net.sf.jasperreports.crosstabs.JRCrosstabMeasure;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.fill.JRDefaultIncrementerFactory;
import net.sf.jasperreports.engine.fill.JRExtendedIncrementerFactory;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.fill.JRIncrementerFactoryCache;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillCrosstabMeasure implements JRCrosstabMeasure
{
	protected JRCrosstabMeasure parentMeasure;
	protected JRFillVariable variable;
	protected JRExtendedIncrementerFactory incrementerFactory;
	protected JRPercentageCalculator percentageCalculator;

	public JRFillCrosstabMeasure(JRCrosstabMeasure measure, JRFillCrosstabObjectFactory factory)
	{
		factory.put(measure, this);
		
		parentMeasure = measure;
		
		variable = factory.getVariable(measure.getVariable());
		
		incrementerFactory = createIncrementerFactory();
		percentageCalculator = createPercentageCalculator();
	}

	public String getName()
	{
		return parentMeasure.getName();
	}

	public String getValueClassName()
	{
		return parentMeasure.getValueClassName();
	}

	public Class getValueClass()
	{
		return parentMeasure.getValueClass();
	}

	public JRExpression getValueExpression()
	{
		return parentMeasure.getValueExpression();
	}

	public byte getCalculation()
	{
		return parentMeasure.getCalculation();
	}

	public String getIncrementerFactoryClassName()
	{
		return parentMeasure.getIncrementerFactoryClassName();
	}

	public Class getIncrementerFactoryClass()
	{
		return parentMeasure.getIncrementerFactoryClass();
	}

	public byte getPercentageOfType()
	{
		return parentMeasure.getPercentageOfType();
	}

	public JRVariable getVariable()
	{
		return variable;
	}

	public JRFillVariable getFillVariable()
	{
		return variable;
	}
	
	
	public JRExtendedIncrementerFactory getIncrementerFactory()
	{
		return incrementerFactory;
	}
	
	
	public JRPercentageCalculator getPercentageCalculator()
	{
		return percentageCalculator;
	}

	
	private JRExtendedIncrementerFactory createIncrementerFactory()
	{
		JRExtendedIncrementerFactory incrFactory;

		String incrementerFactoryClassName = getIncrementerFactoryClassName();
		if (incrementerFactoryClassName == null)
		{
			incrFactory = JRDefaultIncrementerFactory.getFactory(getValueClass());
		}
		else
		{
			incrFactory = (JRExtendedIncrementerFactory) JRIncrementerFactoryCache.getInstance(getIncrementerFactoryClass());
		}
		
		return incrFactory;
	}

	public JRPercentageCalculator createPercentageCalculator()
	{
		JRPercentageCalculator percentageCalc;
		
		if (getPercentageOfType() == JRCrosstabMeasure.PERCENTAGE_TYPE_GRAND_TOTAL)
		{
			percentageCalc = JRPercentageCalculatorFactory.getPercentageCalculator(getPercentageCalculatorClass(), getValueClass());
		}
		else
		{
			percentageCalc = null;
		}
		
		return percentageCalc;
	}

	public String getPercentageCalculatorClassName()
	{
		return parentMeasure.getPercentageCalculatorClassName();
	}

	public Class getPercentageCalculatorClass()
	{
		return parentMeasure.getPercentageCalculatorClass();
	}
	
	/**
	 *
	 */
	public Object clone() 
	{
		return null;
	}
}
