/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.charts.fill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.charts.JRPieSeries;
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
 */
public class JRFillPieDataset extends JRFillChartDataset implements JRPieDataset
{
	/**
	 * 
	 */
	public static final String EXCEPTION_MESSAGE_KEY_DUPLICATED_KEY = "charts.pie.dataset.duplicated.key";
	public static final String EXCEPTION_MESSAGE_KEY_NULL_KEY = "charts.pie.dataset.null.key";

	/**
	 *
	 */
	protected JRFillPieSeries[] pieSeries;

	/**
	 *
	 */
	private Map<Comparable<?>, Number> values;
	private Map<Comparable<?>, String> labels;
	private Map<Comparable<?>, JRPrintHyperlink> sectionHyperlinks;
	private boolean ignoreDuplicatedKey = false;
	
	private Comparable<?> otherKey;
	private String otherLabel;
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

		/*   */
		JRPieSeries[] srcPieSeries = pieDataset.getSeries();
		if (srcPieSeries != null && srcPieSeries.length > 0)
		{
			pieSeries = new JRFillPieSeries[srcPieSeries.length];
			for(int i = 0; i < pieSeries.length; i++)
			{
				pieSeries[i] = (JRFillPieSeries)factory.getPieSeries(srcPieSeries[i]);
			}
		}
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
	public JRPieSeries[] getSeries()
	{
		return pieSeries;
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
	public JRHyperlink getOtherSectionHyperlink()
	{
		return ((JRPieDataset) parent).getOtherSectionHyperlink();
	}

	
	/**
	 *
	 */
	protected void customInitialize()
	{
		values = new LinkedHashMap<Comparable<?>, Number>();
		labels = new HashMap<Comparable<?>, String>();
		sectionHyperlinks = new HashMap<Comparable<?>, JRPrintHyperlink>();
		
		// read property here because fill dataset is null on constructor
		ignoreDuplicatedKey = 
			getFiller().getPropertiesUtil().getBooleanProperty(
				getFillDataset(), 
				PROPERTY_IGNORE_DUPLICATED_KEY, 
				false
				);
	}

	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (pieSeries != null && pieSeries.length > 0)
		{
			for(int i = 0; i < pieSeries.length; i++)
			{
				pieSeries[i].evaluate(calculator);
			}
		}

		otherKey = (String)calculator.evaluate(getOtherKeyExpression());
		otherLabel = (String)calculator.evaluate(getOtherLabelExpression());
		
		if (!JRHyperlinkHelper.isEmpty(getOtherSectionHyperlink()))
		{
			evaluateOtherSectionHyperlink(calculator);
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
		if (pieSeries != null && pieSeries.length > 0)
		{
			for(int i = 0; i < pieSeries.length; i++)
			{
				JRFillPieSeries crtPieSeries = pieSeries[i];
				
				Comparable<?> key = crtPieSeries.getKey();
				if (key == null)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_NULL_KEY,
							(Object[])null 
							);
				}

				if (
					!ignoreDuplicatedKey
					&& values.containsKey(key)
					)
				{
					throw 
						new JRRuntimeException(
							EXCEPTION_MESSAGE_KEY_DUPLICATED_KEY,
							new Object[]{key} 
							);
				}

				values.put(key, crtPieSeries.getValue());

				if (crtPieSeries.getLabelExpression() != null)
				{
					labels.put(key, crtPieSeries.getLabel());
				}
				
				if (crtPieSeries.hasSectionHyperlinks())
				{
					sectionHyperlinks.put(key, crtPieSeries.getPrintSectionHyperlink());
				}
			}
		}
	}

	/**
	 *
	 */
	public Dataset getCustomDataset()
	{
		double total = 0;
		List<Double> sortedValues = new ArrayList<Double>();
		for(Number nv: values.values())
		{
			double dvalue = nv.doubleValue();
			total += dvalue;
			sortedValues.add(dvalue);
		}
		Collections.sort(sortedValues);
		
		Double minValue = null;
		if (getMinPercentage() != null && getMinPercentage().intValue() >= 0 && getMinPercentage().intValue() <= 100)//FIXMENOW why intValue?
		{
			minValue = new Double((getMinPercentage().doubleValue() * total) / 100);
		}
		if (getMaxCount() != null && getMaxCount().intValue() >= 0 && getMaxCount().intValue() <= values.size())
		{
			Double minValue2 = sortedValues.get(sortedValues.size() - getMaxCount().intValue());
			minValue = minValue != null && minValue.doubleValue() > minValue2.doubleValue() ? minValue : minValue2;  
		}
		
		int otherCount = 0;
		Comparable<?> lastOtherKey = null;
		Number lastOtherValue = null;
		double otherTotal = 0;
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(Iterator<Comparable<?>> it = values.keySet().iterator(); it.hasNext();)
		{
			Comparable<?> key = it.next();
			Number value = values.get(key);
			
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


	public Object getLabelGenerator()//FIXMECHART is this OK?
	{
		JRExpression labelExpression = (pieSeries != null && pieSeries.length > 0 ? pieSeries[0].getLabelExpression() : null);
		return (labelExpression == null) ? null : new PieLabelGenerator( labels );
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public Map<Comparable<?>, JRPrintHyperlink> getSectionHyperlinks()
	{
		return sectionHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
