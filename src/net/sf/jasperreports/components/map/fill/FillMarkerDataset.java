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
import java.util.Map;

import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.fill.JRFillDataset;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarkerDataset
{

	/**
	 *
	 */
	protected List<Marker> markerList;
	protected MarkerFillDatasetRun datasetRun;
	protected JRFillExpressionEvaluator evaluator;
	
	/**
	 *
	 */
	public FillMarkerDataset(
		FillContext fillContext,
		MarkerDataset markerDataset, 
		JRFillObjectFactory factory
		) throws JRException
	{
		factory.put(markerDataset, this);

		if (markerDataset.getDatasetRun() != null)//FIXMEMAP deal with missing dataset run
		{
			this.datasetRun = new MarkerFillDatasetRun(markerDataset.getDatasetRun(), factory);
			this.evaluator = createDatasetExpressionEvaluator();
		}
		else
		{
			this.evaluator = createDatasetExpressionEvaluator();
		}

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
	protected JRFillExpressionEvaluator createDatasetExpressionEvaluator()
	{
		return new JRFillExpressionEvaluator()
		{
			public Object evaluate(JRExpression expression,
					byte evaluationType) throws JRException
			{
				return datasetRun.evaluateDatasetExpression(
						expression, evaluationType);
			}

			public JRFillDataset getFillDataset()
			{
				return datasetRun.getDataset();
			}
		};
	}
	
	/**
	 *
	 */
	public List<Map<String,Object>> evaluateMarkers(byte evaluation) throws JRException
	{
		if (datasetRun != null)
		{
			datasetRun.evaluate(evaluation);
		}

		List<Map<String,Object>> markers = new ArrayList<Map<String, Object>>();
		
		datasetRun.start();
		
		while(datasetRun.next())
		{
			if (markerList != null)
			{
				for(Marker marker : markerList)
				{
					markers.add(((FillMarker)marker).evaluateProperties(evaluator, evaluation));
				}
			}
		}

		datasetRun.end();
		
		return markers;
	}
}
