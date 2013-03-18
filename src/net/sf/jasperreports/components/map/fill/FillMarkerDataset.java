/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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

import net.sf.jasperreports.components.map.MapCompiler;
import net.sf.jasperreports.components.map.Marker;
import net.sf.jasperreports.components.map.MarkerDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fill.JRCalculator;
import net.sf.jasperreports.engine.fill.JRExpressionEvalException;
import net.sf.jasperreports.engine.fill.JRFillElementDataset;
import net.sf.jasperreports.engine.fill.JRFillExpressionEvaluator;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;


/**
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id$
 */
public class FillMarkerDataset extends JRFillElementDataset
{

	protected final MarkerDataset markerDataset;
	protected List<FillMarker> markerList;
	protected List<Map<String,Object>> evaluatedMarkers;
	protected JRFillExpressionEvaluator evaluator;
	protected byte evaluation = JRExpression.EVALUATION_DEFAULT;
	
	public FillMarkerDataset(MarkerDataset markerDataset, JRFillObjectFactory factory)
	{
		super(markerDataset, factory);
		this.markerDataset = markerDataset;
		
		List<Marker> srcMarkerList = markerDataset.getMarkers();
		if (srcMarkerList != null && !srcMarkerList.isEmpty())
		{
			markerList = new ArrayList<FillMarker>();
			for(Marker marker : srcMarkerList)
			{
				if(marker != null)
				{
					markerList.add(new FillMarker(marker));
				}
			}
		}
		
	}

	protected void customEvaluate(JRCalculator calculator)
			throws JRExpressionEvalException
	{
		try
		{
			if(markerList != null && ! markerList.isEmpty()) {
				for (FillMarker marker : markerList)
				{
					marker.evaluate(calculator, evaluation);
				}
			}
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
		if(markerList != null && ! markerList.isEmpty()) {
			for (FillMarker marker : markerList)
			{
				Map<String,Object> evaluatedProperties = marker.getEvaluatedProperties();
				if (evaluatedProperties != null)
				{
					if(evaluatedMarkers == null) {
						evaluatedMarkers = new ArrayList<Map<String,Object>>();
					}
					evaluatedMarkers.add(evaluatedProperties);
				}
			}
		}
	}

	protected void customInitialize()
	{
		evaluatedMarkers = null;
	}

	public void collectExpressions(JRExpressionCollector collector)
	{
		MapCompiler.collectExpressions(markerDataset, collector);
	}

	public void increment()
	{
		super.increment();
	}
	
	public List<Map<String,Object>> getEvaluatedMarkers()
	{
		return evaluatedMarkers;
	}
	
	/**
	 * @return the evaluation
	 */
	public byte getEvaluation() {
		return evaluation;
	}

	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(byte evaluation) {
		this.evaluation = evaluation;
	}
}
