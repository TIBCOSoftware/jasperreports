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

import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.util.DefaultXYZDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;

import org.jfree.data.general.Dataset;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillXyzDataset extends JRFillChartDataset implements JRXyzDataset {

	protected JRFillXyzSeries[] xyzSeries = null;

	private DefaultXYZDataset dataset = null;
	
	
	public JRFillXyzDataset(JRXyzDataset xyzDataset, JRFillObjectFactory factory)
	{
		super( xyzDataset, factory );
		
		JRXyzSeries[] srcXyzSeries = xyzDataset.getSeries();
		if(srcXyzSeries != null && srcXyzSeries.length > 0)
		{
			xyzSeries = new JRFillXyzSeries[srcXyzSeries.length];
			for(int i = 0; i < xyzSeries.length; i++)
			{
				xyzSeries[i] = (JRFillXyzSeries)factory.getXyzSeries( srcXyzSeries[i]);
			}
		}
	}
	
	public JRXyzSeries[] getSeries(){
		return xyzSeries;
	}
	
	protected void customInitialize()
	{
		dataset = new DefaultXYZDataset();
	}
	
	protected void customEvaluate( JRCalculator calculator ) throws JRExpressionEvalException 
	{
		if (xyzSeries != null && xyzSeries.length > 0)
		{
			for (int i = 0; i < xyzSeries.length; i++)
			{
				xyzSeries[i].evaluate( calculator );
			}
		}
	}
	
	protected void customIncrement()
	{
		if (xyzSeries != null && xyzSeries .length > 0)
		{
			for (int i = 0; i < xyzSeries.length; i++)
			{
				JRFillXyzSeries crtXyzSeries = xyzSeries[i];
				dataset.addValue(
					crtXyzSeries.getSeries(), 
					crtXyzSeries.getXValue(),
					crtXyzSeries.getYValue(),
					crtXyzSeries.getZValue()
					);
			}
		}
	}
	
	public Dataset getCustomDataset() {
		return dataset;
	}

	/**
	 * 
	 */
	public byte getDatasetType() {
		return JRChartDataset.XYZ_DATASET;
	}


	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		collector.collect(this);
	}


}
