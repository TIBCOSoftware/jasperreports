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
package net.sf.jasperreports.charts.fill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRPieDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillPieDataset extends JRFillChartDataset implements JRPieDataset
{

	/**
	 *
	 */
	private DefaultPieDataset dataset = new DefaultPieDataset();
	private Map labels = null;
	
	private Comparable key = null;
	private Number value = null;
	private String label = null;
	
	private boolean isIncremented = false;
	
	
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
	protected void initialize()
	{
		dataset = new DefaultPieDataset();
		labels = new HashMap();
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		key = (Comparable)calculator.evaluate(getKeyExpression()); 
		value = (Number)calculator.evaluate(getValueExpression());
		label = (String)calculator.evaluate(getLabelExpression());
		
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void increment()
	{
		if (key != null){
			dataset.setValue(key, value);//FIXME NOW verify if condifion
			labels.put( key, label );
		}
		
		isIncremented = true;
	}

	/**
	 *
	 */
	public Dataset getDataset()
	{
		if (isIncremented == false)
		{
			increment();
		}
		return dataset;
	}


	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRChartDataset#getDatasetType()
	 */
	public byte getDatasetType() {
		return JRChartDataset.PIE_DATASET;
	}


	public PieLabelGenerator getLabelGenerator(){
		return (getLabelExpression() == null) ? null : new PieLabelGenerator( labels );
	}
	
	private static class PieLabelGenerator implements PieSectionLabelGenerator, Serializable
	{
		private Map labels = null;
		
		public PieLabelGenerator( Map labels )
		{
			this.labels = labels;
		}
		
		public String generateSectionLabel(PieDataset arg0, Comparable arg1)
		{
			return (String)labels.get( arg1 );
		}
	}
	

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


}
