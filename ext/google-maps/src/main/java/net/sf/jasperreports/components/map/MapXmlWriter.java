/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.components.AbstractComponentXmlWriter;
import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.ItemXmlFactory;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlWriteHelper;
import net.sf.jasperreports.engine.util.XmlNamespace;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/**
 * XML writer for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see MapExtensionsRegistryFactory
 */
public class MapXmlWriter extends AbstractComponentXmlWriter
{
	/**
	 * 
	 */
	public MapXmlWriter(JasperReportsContext jasperReportsContext)
	{
		super(jasperReportsContext);
	}

	@Override
	public void writeToXml(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		if (component instanceof MapComponent)
		{
			writeMap(componentElement, reportWriter);
		}
		
	}

	protected void writeMap(JRComponentElement componentElement, JRXmlWriter reportWriter) throws IOException
	{
		Component component = componentElement.getComponent();
		MapComponent map = (MapComponent) component;
		JRXmlWriteHelper writer = reportWriter.getXmlWriteHelper();
		ComponentKey componentKey = componentElement.getComponentKey();
		
		XmlNamespace namespace = new XmlNamespace(
			MapExtensionsRegistryFactory.NAMESPACE, 
			componentKey.getNamespacePrefix(),
			MapExtensionsRegistryFactory.XSD_LOCATION);
		
		writer.startElement("map", namespace);
		
		if (map.getEvaluationTime() != EvaluationTimeEnum.NOW)
		{
			writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationTime, 
				map.getEvaluationTime());
		}
		writer.addAttribute(JRXmlConstants.ATTRIBUTE_evaluationGroup, 
				map.getEvaluationGroup());

		writer.addAttribute(MapXmlFactory.ATTRIBUTE_mapType, map.getMapType(), MapTypeEnum.ROADMAP);
		writer.addAttribute(MapXmlFactory.ATTRIBUTE_mapScale, map.getMapScale(), MapScaleEnum.ONE);
		writer.addAttribute(MapXmlFactory.ATTRIBUTE_imageType, map.getImageType(), MapImageTypeEnum.PNG);
		writer.addAttribute(MapXmlFactory.ATTRIBUTE_onErrorType, map.getOnErrorType(), OnErrorTypeEnum.ERROR);
		
		if (isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_6_20_1)) 
		{
			writer.addAttribute(MapXmlFactory.ATTRIBUTE_markerClustering, map.getMarkerClustering(), false);
			writer.addAttribute(MapXmlFactory.ATTRIBUTE_markerSpidering, map.getMarkerSpidering(), false);
		}
		
		writer.writeExpression("latitudeExpression", 
			map.getLatitudeExpression());
		writer.writeExpression("longitudeExpression", 
				map.getLongitudeExpression());
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_5_5_2))
		{
			writer.writeExpression("addressExpression", map.getAddressExpression());
		}
		writer.writeExpression("zoomExpression", 
				map.getZoomExpression());
		writer.writeExpression("languageExpression", 
				map.getLanguageExpression());
		if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_6_20_2)) {
			// write legendItem
			Item legendItem = map.getLegendItem();
			if (legendItem != null) {
				writer.startElement(MapXmlFactory.ELEMENT_legendItem, namespace);
				List<ItemProperty> legendItemProperties = legendItem.getProperties();
				for (ItemProperty property : legendItemProperties) {
					writeItemProperty(property, writer, reportWriter, namespace, componentElement);
				}
				writer.closeElement();
			}

			// write resetMapItem
			Item resetMapItem = map.getResetMapItem();
			if (resetMapItem != null) {
				writer.startElement(MapXmlFactory.ELEMENT_resetMapItem, namespace);
				List<ItemProperty> resetMapItemProperties = resetMapItem.getProperties();
				for (ItemProperty property : resetMapItemProperties) {
					writeItemProperty(property, writer, reportWriter, namespace, componentElement);
				}
				writer.closeElement();
			}

			// write markerData
			List<MarkerItemData> markerDataList = map.getMarkerItemDataList();
			if(markerDataList !=null && markerDataList.size() > 0) {
				for(MarkerItemData markerData : markerDataList) {
					writeMarkerItemData(MapXmlFactory.ELEMENT_markerData, markerData, writer, reportWriter, namespace, componentElement);
				}
			}
		} else if(isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_5_5_2)) {
			@SuppressWarnings("deprecation")
			List<ItemData> markerDataList = map.getMarkerDataList();
			if(markerDataList !=null && markerDataList.size() > 0) {
				for(ItemData markerData : markerDataList) {
					writeItemData(MapXmlFactory.ELEMENT_markerData, markerData, writer, reportWriter, namespace, componentElement);
				}
			}
		}
		
		List<ItemData> pathStyleList = map.getPathStyleList();
		if(pathStyleList !=null && pathStyleList.size() > 0) {
			for(ItemData pathStyle : pathStyleList) {
				writeItemData(MapXmlFactory.ELEMENT_pathStyle, pathStyle, writer, reportWriter, namespace, componentElement);
			}
		}
		
		List<ItemData> pathDataList = map.getPathDataList();
		if(pathDataList !=null && pathDataList.size() > 0) {
			for(ItemData pathData : pathDataList) {
				writeItemData(MapXmlFactory.ELEMENT_pathData, pathData, writer, reportWriter, namespace, componentElement);
			}
		}

		writer.closeElement();
	}

	//FIXME7 fix duplication of these methods
	private void writeItemData(String name, ItemData itemData, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		if (itemData != null)
		{
			writeItemDataContent(name, itemData, writer, reportWriter, namespace, componentElement);
			writer.closeElement();
		}
	}

	private void writeMarkerItemData(String name, MarkerItemData markerItemData, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		if (markerItemData != null)
		{
			writeMarkerItemDataContent(name, markerItemData, writer, reportWriter, namespace, componentElement);
			writer.closeElement();
		}
	}
	
	private void writeItemDataContent(String name, ItemData itemData, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		writer.startElement(name, namespace);
		
		JRElementDataset dataset = itemData.getDataset();
		if (dataset != null)
		{
			reportWriter.writeElementDataset(dataset, false);
		}
		
		/*   */
		List<Item> itemList = itemData.getItems();
		if (itemList != null && !itemList.isEmpty())
		{
			for(Item item : itemList)
			{
				if(item.getProperties() != null && !item.getProperties().isEmpty())
				{
					writeItem(item, writer, reportWriter, namespace, componentElement);
				}
			}
		}
	}

	private void writeMarkerItemDataContent(String name, MarkerItemData markerItemData, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		writeItemDataContent(name, markerItemData, writer, reportWriter, namespace, componentElement);

		writeExpression(MapXmlFactory.ELEMENT_seriesNameExpression, namespace, markerItemData.getSeriesNameExpression(), false, componentElement, reportWriter);
		writeExpression(MapXmlFactory.ELEMENT_markerClusteringExpression, namespace, markerItemData.getMarkerClusteringExpression(), false, componentElement, reportWriter);
		writeExpression(MapXmlFactory.ELEMENT_markerSpideringExpression, namespace, markerItemData.getMarkerSpideringExpression(), false, componentElement, reportWriter);
		writeExpression(MapXmlFactory.ELEMENT_legendIconExpression, namespace, markerItemData.getLegendIconExpression(), false, componentElement, reportWriter);
	}
	
	private void writeItem(Item item, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		writer.startElement(ItemXmlFactory.ELEMENT_item, namespace);
		List<ItemProperty> itemProperties = item.getProperties();
		for(ItemProperty property : itemProperties)
		{
			writeItemProperty(property, writer, reportWriter, namespace, componentElement);
		}
		writer.closeElement();
	}
	
	private void writeItemProperty(ItemProperty itemProperty, JRXmlWriteHelper writer, JRXmlWriter reportWriter, XmlNamespace namespace, JRComponentElement componentElement) throws IOException
	{
		writer.startElement(ItemXmlFactory.ELEMENT_itemProperty, namespace);
		writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_name, itemProperty.getName());
		if(itemProperty.getValue() != null)
		{
			writer.addEncodedAttribute(JRXmlConstants.ATTRIBUTE_value, itemProperty.getValue());
		}
		writeExpression(JRXmlConstants.ELEMENT_valueExpression, JRXmlWriter.JASPERREPORTS_NAMESPACE, itemProperty.getValueExpression(), false, componentElement, reportWriter);
		writer.closeElement();
	}

	@Override
	public boolean isToWrite(JRComponentElement componentElement, JRXmlWriter reportWriter) 
	{
//		ComponentKey componentKey = componentElement.getComponentKey();
//		if (ComponentsExtensionsRegistryFactory.NAMESPACE.equals(componentKey.getNamespace()))
//		{
//			if(ComponentsExtensionsRegistryFactory.SORT_COMPONENT_NAME.equals(componentKey.getName())
//					|| ComponentsExtensionsRegistryFactory.MAP_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_4_1_1);
//			}
//			else if(ComponentsExtensionsRegistryFactory.SPIDERCHART_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_4);
//			}
//			else if(ComponentsExtensionsRegistryFactory.TABLE_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_7_2);
//			}
//			else if(ComponentsExtensionsRegistryFactory.LIST_COMPONENT_NAME.equals(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_1);
//			}
//			else if(ComponentsExtensionsRegistryFactory.BARBECUE_COMPONENT_NAME.equals(componentKey.getName())
//					|| isBarcode4jName(componentKey.getName()))
//			{
//				return isNewerVersionOrEqual(componentElement, reportWriter, JRConstants.VERSION_3_5_2);
//			}
//		}

		return true;
	}
}
