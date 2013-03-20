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
package net.sf.jasperreports.components.map;

import java.util.List;

import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
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
		collector.addExpression(map.getLanguageExpression());
		collectExpressions(map.getMarkerData(), collector);
	}

	public static void collectExpressions(ItemData data, JRExpressionCollector collector)
	{
		if (data != null)
		{
			JRExpressionCollector datasetCollector = collector;

			JRElementDataset dataset = data.getDataset();
			JRDatasetRun datasetRun = dataset == null ? null : dataset.getDatasetRun();
			if (datasetRun != null)
			{
				collector.collect(datasetRun);
				datasetCollector = collector.getDatasetCollector(datasetRun.getDatasetName());
			}

			List<Item> items = data.getItems();
			if (items != null && !items.isEmpty())
			{
				for(Item item : items)
				{
					List<ItemProperty> itemProperties = item.getProperties();
					if(itemProperties != null)
					{
						for(ItemProperty property : itemProperties)
						{
							datasetCollector.addExpression(property.getValueExpression());
						}
					}
				}
			}
		}
	}

	/**
	 * @deprecated Replaced by {@link #collectExpressions(ItemData, JRExpressionCollector)}.
	 */
	public static void collectExpressions(MarkerDataset dataset, JRExpressionCollector collector)
	{
		if(dataset != null)
		{
			JRExpressionCollector datasetCollector = collector;

			JRDatasetRun datasetRun = dataset.getDatasetRun();
			if (datasetRun != null)
			{
				collector.collect(datasetRun);
				datasetCollector = collector.getDatasetCollector(datasetRun.getDatasetName());
			}

			List<Marker> markers = dataset.getMarkers();
			if (markers != null && !markers.isEmpty())
			{
				for(Marker marker : markers)
				{
					List<MarkerProperty> markerProperties = marker.getProperties();
					if(markerProperties != null)
					{
						for(MarkerProperty property : markerProperties)
						{
							datasetCollector.addExpression(property.getValueExpression());
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

		ItemData markerData = map.getMarkerData();
		if (markerData != null)
		{
			verifyMarkerData(verifier, markerData);
		}
	}

	protected void verifyMarkerData(JRVerifier verifier, ItemData itemData)
	{
		if (itemData.getDataset() != null)
		{
			verifier.verifyElementDataset(itemData.getDataset());
		}
		
		List<Item> items = itemData.getItems();
		if (items != null)
		{
			for (Item item : items)
			{
				verifyMarker(verifier, item);
			}
		}
	}

	protected void verifyMarker(JRVerifier verifier, Item item)
	{
		boolean hasLatitude = false;
		boolean hasLongitude = false;
		
		List<ItemProperty> itemProperties = item.getProperties();
		
		if (itemProperties != null)
		{
			for (ItemProperty itemProperty : itemProperties)
			{
				if (MapComponent.PROPERTY_latitude.equals(itemProperty.getName()))
				{
					hasLatitude = true;
				}
				else if (MapComponent.PROPERTY_longitude.equals(itemProperty.getName()))
				{
					hasLongitude = true;
				}
			}
		}
		
		if(!hasLatitude) 
		{
			verifier.addBrokenRule("No latitude set for marker.", itemProperties);
		}
		if(!hasLongitude) 
		{
			verifier.addBrokenRule("No longitude set for marker.", itemProperties);
		}
	}
}
