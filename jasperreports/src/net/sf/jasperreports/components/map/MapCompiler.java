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
package net.sf.jasperreports.components.map;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class MapCompiler implements ComponentCompiler
{
	
	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
		MapComponent map = (MapComponent) component;
		collector.addExpression(map.getLatitudeExpression());
		collector.addExpression(map.getLongitudeExpression());
		collector.addExpression(map.getZoomExpression());
		collectExpressions(map.getMarkerDataset(), collector);
	}

	public static void collectExpressions(MarkerDataset dataset, JRExpressionCollector collector)
	{
		if(dataset != null)
		{
			collector.collect(dataset);
	
			List<Marker> markers = dataset.getMarkers();
			if (markers != null && !markers.isEmpty())
			{
				JRExpressionCollector markerCollector = collector.getCollector(dataset);
				for(Marker marker : markers)
				{
					Map<String, MarkerProperty> markerProperties = marker.getMarkerProperties();
					if(markerProperties != null)
					{
						for(String name : markerProperties.keySet())
						{
							markerCollector.addExpression(markerProperties.get(name).getValueExpression());
						}
					}
				}
			}
		}
	}

	public Component toCompiledComponent(Component component,
			JRBaseObjectFactory baseFactory)
	{
		MapComponent map = (MapComponent) component;
		return new StandardMapComponent(map, baseFactory);
	}

	public void verify(Component component, JRVerifier verifier)
	{
		MapComponent map = (MapComponent) component;
		
		EvaluationTimeEnum evaluationTime = map.getEvaluationTime();
		if (evaluationTime == EvaluationTimeEnum.AUTO)
		{
			verifier.addBrokenRule("Auto evaluation time is not supported for maps", map);
		}
		else if (evaluationTime == EvaluationTimeEnum.GROUP)
		{
			String evaluationGroup = map.getEvaluationGroup();
			if (evaluationGroup == null || evaluationGroup.length() == 0)
			{
				verifier.addBrokenRule("No evaluation group set for map", map);
			}
			else if (!verifier.getReportDesign().getGroupsMap().containsKey(evaluationGroup))
			{
				verifier.addBrokenRule("Map evalution group \"" 
						+ evaluationGroup + " not found", map);
			}
		}

		MarkerDataset dataset = map.getMarkerDataset();
		if (dataset != null)
		{
			verify(verifier, dataset);
		}
	}

	protected void verify(JRVerifier verifier, MarkerDataset dataset)
	{
		verifier.verifyElementDataset(dataset);
		
		List<Marker> markers = dataset.getMarkers();
		if (markers != null)
		{
			for (Marker marker : markers)
			{
				verifyMarkerProperties(verifier, marker.getMarkerProperties());
			}
		}
	}

	protected void verifyMarkerProperties(JRVerifier verifier, Map<String, MarkerProperty> markerProperties)
	{
		if(markerProperties == null || markerProperties.isEmpty())
		{
			verifier.addBrokenRule("No properties set for marker. Latitude and longitude properties are required.", markerProperties);
		}
		else if(!markerProperties.containsKey(Marker.PROPERTY_latitude) 
				|| (markerProperties.get(Marker.PROPERTY_latitude).getValue() == null && markerProperties.get(Marker.PROPERTY_latitude).getValueExpression() == null))
		{
			verifier.addBrokenRule("No latitude set for marker.", markerProperties);
		}
		else if(!markerProperties.containsKey(Marker.PROPERTY_longitude) 
				|| (markerProperties.get(Marker.PROPERTY_longitude).getValue() == null && markerProperties.get(Marker.PROPERTY_longitude).getValueExpression() == null))
		{
			verifier.addBrokenRule("No longitude set for marker.", markerProperties);
		}
	}
}
