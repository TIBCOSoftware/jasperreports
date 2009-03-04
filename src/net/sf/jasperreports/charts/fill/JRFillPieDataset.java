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
package net.sf.jasperreports.charts.fill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.util.PieLabelGenerator;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkHelper;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillHyperlinkHelper;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillPieDataset extends JRFillChartDataset implements JRPieDataset
{

	/**
	 *
	 */
	private Map values = null;
	private Map labels = null;
	
	private Comparable key = null;
	private Number value = null;
	private String label = null;
	private Comparable otherKey = null;
	private String otherLabel = null;
	
	private Map sectionHyperlinks;
	private JRPrintHyperlink sectionHyperlink;
	private JRPrintHyperlink otherSectionHyperlink;
	
	
	/**
	 *
	 */
	public JRFillPieDataset(
		JRPieDataset pieDataset, 
		JRFillObjectFactory factory
		)
	{
		super(pieDataset, factory);
	}


	/**
	 *
	 */
	public Float getMinPercentage()
	{
		return ((JRPieDataset)parent).getMinPercentage();
	}

	/**
	 *
	 */
	public void setMinPercentage(Float minPercentage)
	{	
	}

	/**
	 *
	 */
	public Integer getMaxCount()
	{
		return ((JRPieDataset)parent).getMaxCount();
	}

	/**
	 *
	 */
	public void setMaxCount(Integer maxCount)
	{	
	}

	/**
	 *
	 */
	public JRExpression getKeyExpression()
	{
		return ((JRPieDataset)parent).getKeyExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getValueExpression()
	{
		return ((JRPieDataset)parent).getValueExpression();
	}
		
	/**
	 *
	 */
	public JRExpression getLabelExpression()
	{
		return ((JRPieDataset)parent).getLabelExpression();
	}
	
	/**
	 *
	 */
	public JRExpression getOtherKeyExpression()
	{
		return ((JRPieDataset)parent).getOtherKeyExpression();
	}

	/**
	 *
	 */
	public JRExpression getOtherLabelExpression()
	{
		return ((JRPieDataset)parent).getOtherLabelExpression();
	}
	
	
	/**
	 *
	 */
	protected void customInitialize()
	{
		values = new HashMap();
		labels = new HashMap();
		sectionHyperlinks = new HashMap();
	}

	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		key = (Comparable)calculator.evaluate(getKeyExpression()); 
		value = (Number)calculator.evaluate(getValueExpression());
		label = (String)calculator.evaluate(getLabelExpression());
		otherKey = (String)calculator.evaluate(getOtherKeyExpression());
		otherLabel = (String)calculator.evaluate(getOtherLabelExpression());
		
		if (!JRHyperlinkHelper.isEmpty(getSectionHyperlink()))
		{
			evaluateSectionHyperlink(calculator);
		}		

		if (!JRHyperlinkHelper.isEmpty(getOtherSectionHyperlink()))
		{
			evaluateOtherSectionHyperlink(calculator);
		}		
	}


	protected void evaluateSectionHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			sectionHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getSectionHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
		}
		catch (JRExpressionEvalException e)
		{
			throw e;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	protected void evaluateOtherSectionHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			otherSectionHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getOtherSectionHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
		}
		catch (JRExpressionEvalException e)
		{
			throw e;
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	/**
	 *
	 */
	protected void customIncrement()
	{
		values.put(key, value);
		labels.put(key, label);
		
		if (!JRHyperlinkHelper.isEmpty(getSectionHyperlink()))
		{
			sectionHyperlinks.put(key, sectionHyperlink);
		}		
	}

	/**
	 *
	 */
	public Dataset getCustomDataset()
	{
		double total = 0;
		ArrayList sortedValues = new ArrayList();
		for(Iterator it = values.values().iterator(); it.hasNext();)
		{
			Number value = (Number)it.next();
			total += value.doubleValue();
			sortedValues.add(value);
		}
		Collections.sort(sortedValues);
		
		Number minValue = null;
		if (getMinPercentage() != null && getMinPercentage().intValue() >= 0 && getMinPercentage().intValue() <= 100)
		{
			minValue = new Double((getMinPercentage().floatValue() * total) / 100);
		}
		if (getMaxCount() != null && getMaxCount().intValue() >= 0 && getMaxCount().intValue() <= values.size())
		{
			Number minValue2 = (Number)sortedValues.get(sortedValues.size() - getMaxCount().intValue());
			minValue = minValue != null && minValue.doubleValue() > minValue2.doubleValue() ? minValue : minValue2;  
		}
		
		int otherCount = 0;
		Comparable lastOtherKey = null;
		Number lastOtherValue = null;
		double otherTotal = 0;
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(Iterator it = values.keySet().iterator(); it.hasNext();)
		{
			Comparable key = (Comparable)it.next();
			Number value = (Number)values.get(key);
			
			if (
				minValue == null
				|| value.doubleValue() >= minValue.doubleValue() 
				)
			{
				dataset.setValue(key, value);
			}
			else
			{
				otherCount++;
				lastOtherKey = key;
				lastOtherValue = value;
				otherTotal += value.doubleValue();
			}
		}
		if (otherCount == 1)
		{
			dataset.setValue(lastOtherKey, lastOtherValue);
		}
		else if (otherCount > 1)
		{
			otherKey = otherKey == null ? "Other" : otherKey;
			dataset.setValue(otherKey, new Double(otherTotal));
			labels.put(otherKey, otherLabel);

			if (!JRHyperlinkHelper.isEmpty(getOtherSectionHyperlink()))
			{
				sectionHyperlinks.put(otherKey, otherSectionHyperlink);
			}		
		}
		
		return dataset;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
	 */
	public byte getDatasetType() {
		return JRChartDataset.PIE_DATASET;
	}


	public Object getLabelGenerator(){
		return (getLabelExpression() == null) ? null : new PieLabelGenerator( labels );
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public JRHyperlink getSectionHyperlink()
	{
		return ((JRPieDataset) parent).getSectionHyperlink();
	}

	
	public JRHyperlink getOtherSectionHyperlink()
	{
		return ((JRPieDataset) parent).getOtherSectionHyperlink();
	}

	
	public Map getSectionHyperlinks()
	{
		return sectionHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
