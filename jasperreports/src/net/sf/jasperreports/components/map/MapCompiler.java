/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.items.ItemCompiler;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentCompiler;
import net.sf.jasperreports.engine.design.JRVerifier;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class MapCompiler implements ComponentCompiler
{
	
	private final static Map<String,String> addressMap = new HashMap<String,String>();
	static {
		addressMap.put(MapComponent.ITEM_PROPERTY_latitude, MapComponent.ITEM_PROPERTY_address);
		addressMap.put(MapComponent.ITEM_PROPERTY_longitude, MapComponent.ITEM_PROPERTY_address);
	}
	
	public void collectExpressions(Component component, JRExpressionCollector collector)
	{
		MapComponent map = (MapComponent) component;
		collector.addExpression(map.getLatitudeExpression());
		collector.addExpression(map.getLongitudeExpression());
		collector.addExpression(map.getAddressExpression());
		collector.addExpression(map.getZoomExpression());
		collector.addExpression(map.getLanguageExpression());

		List<ItemData> markerDataList = map.getMarkerDataList();
		if(markerDataList != null && markerDataList.size() > 0) {
			for(ItemData markerData : markerDataList){
				ItemCompiler.collectExpressions(markerData, collector);
			}
		}
		List<ItemData> pathStyleList = map.getPathStyleList();
		if(pathStyleList != null && pathStyleList.size() > 0) {
			for(ItemData pathStyle : pathStyleList){
				ItemCompiler.collectExpressions(pathStyle, collector);
			}
		}
		List<ItemData> pathDataList = map.getPathDataList();
		if(pathDataList != null && pathDataList.size() > 0) {
			for(ItemData pathData : pathDataList){
				ItemCompiler.collectExpressions(pathData, collector);
			}
		}
	}

	/**
	 * @deprecated Replaced by {@link ItemCompiler#collectExpressions(ItemData, JRExpressionCollector)}.
	 */
	public static void collectExpressions(ItemData data, JRExpressionCollector collector)
	{
		ItemCompiler.collectExpressions(data, collector);
	}

	/**
	 * @deprecated Replaced by {@link ItemCompiler#collectExpressions(ItemData, JRExpressionCollector)}.
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
		
		if((map.getLatitudeExpression() == null || map.getLongitudeExpression() == null) && map.getAddressExpression() == null){
			verifier.addBrokenRule("Missing the latitude and/or the longitude expression for the map center. Try to configure them properly, or configure the equivalent addressExpression for this map.", map);
		}
		
		String[] reqNames = new String[]{MapComponent.ITEM_PROPERTY_latitude, MapComponent.ITEM_PROPERTY_longitude};
		List<ItemData> markerDataList = map.getMarkerDataList();
		if (markerDataList != null && markerDataList.size() > 0)
		{
			for(ItemData markerData : markerDataList){
				ItemCompiler.verifyItemData(verifier, markerData, MapComponent.ELEMENT_MARKER_DATA, reqNames, addressMap);
			}
		}
		
		List<ItemData> pathStyleList = map.getPathStyleList();
		if (pathStyleList != null && pathStyleList.size() > 0)
		{
			for(ItemData pathStyle : pathStyleList){
				ItemCompiler.verifyItemData(verifier, pathStyle, MapComponent.ELEMENT_PATH_STYLE, new String[]{MapComponent.ITEM_PROPERTY_name}, null);
			}
		}
		
		List<ItemData> pathDataList = map.getPathDataList();
		if (pathDataList != null && pathDataList.size() > 0)
		{
			for(ItemData pathData : pathDataList){
				ItemCompiler.verifyItemData(verifier, pathData, MapComponent.ELEMENT_PATH_DATA, reqNames, addressMap);
			}
		}
	}
	
}
