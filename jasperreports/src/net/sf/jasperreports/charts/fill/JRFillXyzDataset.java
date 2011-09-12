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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.charts.JRXyzDataset;
import net.sf.jasperreports.charts.JRXyzSeries;
import net.sf.jasperreports.charts.util.DefaultXYZDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillChartDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.util.Pair;

import org.jfree.data.general.Dataset;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id$
 */
public class JRFillXyzDataset extends JRFillChartDataset implements JRXyzDataset {

	protected JRFillXyzSeries[] xyzSeries;

	private DefaultXYZDataset dataset;
	
	private Map<Comparable<?>, Map<Pair, JRPrintHyperlink>> itemHyperlinks;
	
	
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
		itemHyperlinks = new HashMap<Comparable<?>, Map<Pair, JRPrintHyperlink>>();
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
				
				Comparable<?> seriesName = crtXyzSeries.getSeries();
				if (seriesName == null)
				{
					throw new JRRuntimeException("XYZ series name is null.");
				}

				dataset.addValue(
					crtXyzSeries.getSeries(), 
					crtXyzSeries.getXValue(),
					crtXyzSeries.getYValue(),
					crtXyzSeries.getZValue()
					);
				
				if (crtXyzSeries.hasItemHyperlinks())
				{
					Map<Pair, JRPrintHyperlink> seriesLinks = itemHyperlinks.get(crtXyzSeries.getSeries());
					if (seriesLinks == null)
					{
						seriesLinks = new HashMap<Pair, JRPrintHyperlink>();
						itemHyperlinks.put(crtXyzSeries.getSeries(), seriesLinks);
					}
					Pair xyKey = new Pair(crtXyzSeries.getXValue(), crtXyzSeries.getYValue());
					seriesLinks.put(xyKey, crtXyzSeries.getPrintItemHyperlink());
				}
			}
		}
	}
	
	/**
	 *
	 */
	public Object getLabelGenerator()
	{
		return null;
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
	
	
	public boolean hasItemHyperlinks()
	{
		boolean foundLinks = false;
		if (xyzSeries != null && xyzSeries.length > 0)
		{
			for (int i = 0; i < xyzSeries.length && !foundLinks; i++)
			{
				JRFillXyzSeries serie = xyzSeries[i];
				foundLinks = serie.hasItemHyperlinks();
			}
		}
		return foundLinks;
	}

	
	public Map<Comparable<?>, Map<Pair, JRPrintHyperlink>> getItemHyperlinks()
	{
		return itemHyperlinks;
	}


	public void validate(JRVerifier verifier)
	{
		verifier.verify(this);
	}

}
