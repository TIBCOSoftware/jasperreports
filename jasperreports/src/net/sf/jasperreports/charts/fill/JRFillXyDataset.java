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

import net.sf.jasperreports.charts.JRXyDataset;
import net.sf.jasperreports.charts.JRXySeries;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.Map;
import java.util.HashMap;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillXyDataset extends JRFillChartDataset implements JRXyDataset
{

	/**
	 *
	 */
	private XYSeriesCollection dataset = new XYSeriesCollection();
	
	protected JRFillXySeries[] xySeries = null;
	protected Map[] labels = null;

	private boolean isIncremented = false;
	
	
	/**
	 *
	 */
	public JRFillXyDataset(
		JRXyDataset xyDataset, 
		JRFillObjectFactory factory
		)
	{
		super(xyDataset, factory);

		/*   */
		JRXySeries[] srcXySeries = xyDataset.getSeries();
		if (srcXySeries != null && srcXySeries.length > 0)
		{
			xySeries = new JRFillXySeries[srcXySeries.length];
			for(int i = 0; i < xySeries.length; i++)
			{
				xySeries[i] = (JRFillXySeries)factory.getXySeries(srcXySeries[i]);
			}
		}
	}
	
	
	/**
	 *
	 */
	public JRXySeries[] getSeries()
	{
		return xySeries;
	}


	/**
	 *
	 */
	protected void initialize()
	{
		dataset = null;
		labels = new Map[ getSeries().length ];
		for( int i = 0; i<labels.length; i++ ){
			if( getSeries()[i].getLabelExpression() != null ){
				labels[i] = new HashMap();
			}
		}
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void evaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				xySeries[i].evaluate(calculator);
			}
		}
		isIncremented = false;
	}

	/**
	 *
	 */
	protected void increment()
	{
		if (dataset == null || dataset.getSeriesCount() == 0)//FIXME NOW too many tests - simplify
		{
			dataset = new XYSeriesCollection();
			if (xySeries != null && xySeries.length > 0)
			{
				for(int i = 0; i < xySeries.length; i++)
				{
					JRFillXySeries crtXySeries = xySeries[i];
					String seriesName = (String)crtXySeries.getSeries();//FIXME NOW use string for series?
					if (seriesName != null)
					{
						XYSeries xySeries = new XYSeries(seriesName);
						dataset.addSeries(xySeries);
					}
				}
			}
		}

		if (dataset.getSeriesCount() > 0 && xySeries != null && xySeries.length > 0)
		{
			for(int i = 0; i < xySeries.length; i++)
			{
				JRFillXySeries crtXySeries = xySeries[i];
				XYSeries xySeries = dataset.getSeries(i);
				if (crtXySeries.getXValue() != null) 
					xySeries.addOrUpdate(
						crtXySeries.getXValue(), 
						crtXySeries.getYValue()
						);//FIXME NOW verify if condifion
				if( labels[i] != null ){
					labels[i].put( crtXySeries.getXValue(), crtXySeries.getLabel() );
				}
			}
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

	/**
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.XY_DATASET;
	}
	
	/**
	 * 
	 */
	public XYDatasetLabelGenerator getLabelGenerator(){
		return new XYDatasetLabelGenerator( labels );	
	}
	
	
	/**
	 * 
	 */
	private static class XYDatasetLabelGenerator extends StandardXYItemLabelGenerator {
		private Map[] labels = null;
		
		public XYDatasetLabelGenerator( Map[] labels ){
			this.labels = labels;
		}
		
		public String generateLabel( XYDataset dataset, int series, int item  ){
			
			if( labels[series] != null ){
				return (String)labels[series].get( ((XYSeriesCollection)dataset).getX( series, item ));
			}
			else {
				return super.generateLabel( dataset, series, item );
			}
		}
	}
	
	
}
