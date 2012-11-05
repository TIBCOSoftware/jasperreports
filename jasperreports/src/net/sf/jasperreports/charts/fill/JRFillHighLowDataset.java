/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
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
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.charts.JRHighLowDataset;
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
import org.jfree.data.xy.DefaultHighLowDataset;


/**
 * @author Ionut Nedelcu (ionutned@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillHighLowDataset extends JRFillChartDataset implements JRHighLowDataset
{

	/**
	 *
	 */
	private String series;
	private List<HighLowElement> elements = new ArrayList<HighLowElement>();
	private Date date;
	private Number high;
	private Number low;
	private Number open;
	private Number close;
	private Number volume;
	
	private JRPrintHyperlink itemHyperlink;
	private List<JRPrintHyperlink> itemHyperlinks;

	
	/**
	 *
	 */
	public JRFillHighLowDataset(JRHighLowDataset dataset, JRFillObjectFactory factory)
	{
		super(dataset, factory);
	}


	protected void customInitialize()
	{
		elements = new ArrayList<HighLowElement>();
		itemHyperlinks = new ArrayList<JRPrintHyperlink>();
	}


	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		series = (String) calculator.evaluate(getSeriesExpression());
		date = (Date) calculator.evaluate(getDateExpression());
		high = (Number) calculator.evaluate(getHighExpression());
		low = (Number) calculator.evaluate(getLowExpression());
		open = (Number) calculator.evaluate(getOpenExpression());
		close = (Number) calculator.evaluate(getCloseExpression());
		volume = (Number) calculator.evaluate(getVolumeExpression());
		
		if (hasItemHyperlink())
		{
			evaluateSectionHyperlink(calculator);
		}
	}


	protected void evaluateSectionHyperlink(JRCalculator calculator) throws JRExpressionEvalException
	{
		try
		{
			itemHyperlink = JRFillHyperlinkHelper.evaluateHyperlink(getItemHyperlink(), calculator, JRExpression.EVALUATION_DEFAULT);
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


	protected void customIncrement()
	{
		elements.add(new HighLowElement(date, high, low, open, close, volume));
		
		if (hasItemHyperlink())
		{
			itemHyperlinks.add(itemHyperlink);
		}
	}


	public Dataset getCustomDataset()
	{
		int size = elements.size();
		if (size > 0)
		{
			Date[] dateArray = new Date[size];
			double[] highArray = new double[size];
			double[] lowArray = new double[size];
			double[] openArray = new double[size];
			double[] closeArray = new double[size];
			double[] volumeArray = new double[size];

			for (int i = 0; i < elements.size(); i++) {
				HighLowElement bean = elements.get(i);
				dateArray[i] = new Date(bean.getDate().getTime());
				highArray[i] = bean.getHigh().doubleValue();
				lowArray[i] = bean.getLow().doubleValue();
				openArray[i] = bean.getOpen().doubleValue();
				closeArray[i] = bean.getClose().doubleValue();
				volumeArray[i] = bean.getVolume().doubleValue();
			}

			return new DefaultHighLowDataset(series, dateArray, highArray, lowArray, openArray, closeArray, volumeArray);
		}
		
		return null;
	}


	public Object getLabelGenerator()
	{
		return null;
	}


	public JRExpression getSeriesExpression()
	{
		return ((JRHighLowDataset)parent).getSeriesExpression();
	}


	public JRExpression getDateExpression()
	{
		return ((JRHighLowDataset)parent).getDateExpression();
	}


	public JRExpression getHighExpression()
	{
		return ((JRHighLowDataset)parent).getHighExpression();
	}


	public JRExpression getLowExpression()
	{
		return ((JRHighLowDataset)parent).getLowExpression();
	}


	public JRExpression getOpenExpression()
	{
		return ((JRHighLowDataset)parent).getOpenExpression();
	}


	public JRExpression getCloseExpression()
	{
		return ((JRHighLowDataset)parent).getCloseExpression();
	}


	public JRExpression getVolumeExpression()
	{
		return ((JRHighLowDataset)parent).getVolumeExpression();
	}

	/**
	 *
	 */
	private static class HighLowElement
	{
		Date date;
		Number high;
		Number low;
		Number open;
		Number close;
		Number volume;


		public HighLowElement(
			Date date,
			Number high,
			Number low,
			Number open,
			Number close,
			Number volume
			)
		{
			if (date == null)
			{
				throw new JRRuntimeException("Date value is null in high-low series.");
			}
			this.date = date;

			if (high == null)
			{
				throw new JRRuntimeException("High value is null in high-low series.");
			}
			this.high = high;
			
			if (low == null)
			{
				throw new JRRuntimeException("Low value is null in high-low series.");
			}
			this.low = low;
			
			if (open == null)
			{
				throw new JRRuntimeException("Open value is null in high-low series.");
			}
			this.open = open;
			
			if (close == null)
			{
				throw new JRRuntimeException("Close value is null in high-low series.");
			}
			this.close = close;
			
			if (volume == null)
			{
				throw new JRRuntimeException("Volume value is null in high-low series.");
			}
			this.volume = volume;
		}


		public Date getDate()
		{
			return date;
		}

		public Number getHigh()
		{
			return high;
		}

		public Number getLow()
		{
			return low;
		}

		public Number getOpen()
		{
			return open;
		}

		public Number getClose()
		{
			return close;
		}

		public Number getVolume()
		{
			return volume;
		}
	}

	/**
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.HIGHLOW_DATASET;
	}

	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


	public JRHyperlink getItemHyperlink()
	{
		return ((JRHighLowDataset) parent).getItemHyperlink();
	}


	public boolean hasItemHyperlink()
	{
		return !JRHyperlinkHelper.isEmpty(getItemHyperlink()); 
	}

	
	public List<JRPrintHyperlink> getItemHyperlinks()
	{
		return itemHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
