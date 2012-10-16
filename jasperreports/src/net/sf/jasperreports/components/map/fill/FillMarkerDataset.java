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
package net.sf.jasperreports.components.map.fill;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.MapCompiler;
import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerDataset;
import net.sf.jasperreports.components.map.StandardMarkerDataset;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarkerDataset extends JRFillElementDataset implements MarkerDataset
{

	/**
	 *
	 */
	protected List<Marker> markerList;
	protected FillContext fillContext;

	private StandardMarkerDataset dataset;

	/**
	 *
	 */
	public FillMarkerDataset(
		FillContext fillContext,
		MarkerDataset markerDataset, 
		JRFillObjectFactory factory
		)
	{
		super(markerDataset, factory);
		this.fillContext = fillContext;

		/*   */
		List<Marker> srcMarkerList = markerDataset.getMarkers();
		if (srcMarkerList != null && !srcMarkerList.isEmpty())
		{
			markerList = new ArrayList<Marker>();
			for(Marker marker : srcMarkerList)
			{
				if(marker != null)
				{
					markerList.add(new FillMarker(marker, factory));
				}
			}
		}
	}
	
	
	/**
	 *
	 */
	public List<Marker> getMarkers()
	{
		return markerList;
	}


	/**
	 *
	 */
	protected void customInitialize()
	{
		dataset = null;
		markerList = null;
	}

	/**
	 *
	 */
	protected void customEvaluate(JRCalculator calculator) throws JRExpressionEvalException
	{
		if (markerList != null && !markerList.isEmpty())
		{
			for(Marker marker : markerList)
			{
				//marker.evaluate(calculator);
			}
		}
		
	}
	
	/**
	 *
	 */
	protected void customIncrement()
	{
		if (markerList != null && !markerList.isEmpty())
		{
			if (dataset == null)
			{
				dataset = new StandardMarkerDataset();
			}
			
			for(Marker marker : markerList)
			{
				if(marker!= null)
				{
					dataset.addMarker(marker);
				}
			}
		}
	}

	/**
	 *
	 */
	public StandardMarkerDataset getCustomDataset()
	{
		return dataset;
	}

	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
		MapCompiler.collectExpressions(this, collector);
	}

	public void finishDataset()
	{
		//one last increment is required in certain cases
		increment();
	}

	
}
