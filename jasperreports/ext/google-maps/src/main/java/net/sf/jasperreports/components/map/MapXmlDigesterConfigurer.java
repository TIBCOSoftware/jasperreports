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

import org.apache.commons.digester.Digester;

import net.sf.jasperreports.components.items.Item;
import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemDataXmlFactory;
import net.sf.jasperreports.components.items.ItemDatasetFactory;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.components.items.ItemPropertyXmlFactory;
import net.sf.jasperreports.components.items.ItemXmlFactory;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.component.XmlDigesterConfigurer;
import net.sf.jasperreports.engine.xml.JRExpressionFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;

/**
 * XML digester for built-in component implementations.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see MapExtensionsRegistryFactory
 */
public class MapXmlDigesterConfigurer implements XmlDigesterConfigurer
{
	
	@Override
	public void configureDigester(Digester digester)
	{
		addMapRules(digester);
	}

	protected void addMapRules(Digester digester)
	{
		String mapPattern = "*/componentElement/map";
		digester.addFactoryCreate(mapPattern, MapXmlFactory.class);

		String latitudeExpressionPattern = mapPattern + "/latitudeExpression";
		digester.addFactoryCreate(latitudeExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(latitudeExpressionPattern, "setText", 0);
		digester.addSetNext(latitudeExpressionPattern, "setLatitudeExpression", 
				JRExpression.class.getName());

		String longitudeExpressionPattern = mapPattern + "/longitudeExpression";
		digester.addFactoryCreate(longitudeExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(longitudeExpressionPattern, "setText", 0);
		digester.addSetNext(longitudeExpressionPattern, "setLongitudeExpression", 
				JRExpression.class.getName());
		
		String addressExpressionPattern = mapPattern + "/addressExpression";
		digester.addFactoryCreate(addressExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(addressExpressionPattern, "setText", 0);
		digester.addSetNext(addressExpressionPattern, "setAddressExpression", 
				JRExpression.class.getName());
		
		String zoomExpressionPattern = mapPattern + "/zoomExpression";
		digester.addFactoryCreate(zoomExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(zoomExpressionPattern, "setText", 0);
		digester.addSetNext(zoomExpressionPattern, "setZoomExpression", 
				JRExpression.class.getName());
		
		String languageExpressionPattern = mapPattern + "/languageExpression";
		digester.addFactoryCreate(languageExpressionPattern, 
				JRExpressionFactory.class.getName());
		digester.addCallMethod(languageExpressionPattern, "setText", 0);
		digester.addSetNext(languageExpressionPattern, "setLanguageExpression", 
				JRExpression.class.getName());

		String componentNamespace = digester.getRuleNamespaceURI();
		String jrNamespace = JRXmlConstants.JASPERREPORTS_NAMESPACE;

		String markerDatasetPattern = mapPattern + "/markerDataset";
		digester.addFactoryCreate(markerDatasetPattern, MarkerItemDataXmlFactory.class.getName());
		digester.addSetNext(markerDatasetPattern, "addMarkerData", MarkerItemData.class.getName());

		String markerPattern = markerDatasetPattern + "/marker";
		digester.addFactoryCreate(markerPattern, ItemXmlFactory.class.getName());
		digester.addSetNext(markerPattern, "addItem", Item.class.getName());

		String markerPropertyPattern = markerPattern + "/markerProperty";
		digester.addFactoryCreate(markerPropertyPattern, ItemPropertyXmlFactory.class.getName());
		digester.addSetNext(markerPropertyPattern, "addItemProperty", ItemProperty.class.getName());

		digester.setRuleNamespaceURI(jrNamespace);
		
		String markerPropertyValueExpressionPattern = markerPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression;
		digester.addFactoryCreate(markerPropertyValueExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(markerPropertyValueExpressionPattern, "setText", 0);
		digester.addSetNext(markerPropertyValueExpressionPattern, "setValueExpression", JRExpression.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
		String markerDataPattern = mapPattern + "/markerData";
		digester.addFactoryCreate(markerDataPattern, MarkerItemDataXmlFactory.class.getName());
		digester.addSetNext(markerDataPattern, "addMarkerData", MarkerItemData.class.getName());
		
		addItemRules(digester, markerDataPattern + "/item", "addItem", jrNamespace);
		
		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(markerDataPattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(markerDataPattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);

		String markerSeriesNameExpressionPattern = markerDataPattern + "/seriesNameExpression";
		digester.addFactoryCreate(markerSeriesNameExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(markerSeriesNameExpressionPattern, "setText", 0);
		digester.addSetNext(markerSeriesNameExpressionPattern, "setSeriesNameExpression", JRExpression.class.getName());

		String markeClusteringExpressionPattern = markerDataPattern + "/markerClusteringExpression";
		digester.addFactoryCreate(markeClusteringExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(markeClusteringExpressionPattern, "setText", 0);
		digester.addSetNext(markeClusteringExpressionPattern, "setMarkerClusteringExpression", JRExpression.class.getName());

		String markerSpideringExpressionPattern = markerDataPattern + "/markerSpideringExpression";
		digester.addFactoryCreate(markerSpideringExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(markerSpideringExpressionPattern, "setText", 0);
		digester.addSetNext(markerSpideringExpressionPattern, "setMarkerSpideringExpression", JRExpression.class.getName());

		String legendIconExpressionPattern = markerDataPattern + "/legendIconExpression";
		digester.addFactoryCreate(legendIconExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(legendIconExpressionPattern, "setText", 0);
		digester.addSetNext(legendIconExpressionPattern, "setLegendIconExpression", JRExpression.class.getName());

		// legend rules
		addItemRules(digester, mapPattern + "/legendItem", "setLegend", jrNamespace);
		digester.setRuleNamespaceURI(componentNamespace);

		// resetMap rules
		addItemRules(digester, mapPattern + "/resetMapItem", "setResetMap", jrNamespace);
		digester.setRuleNamespaceURI(componentNamespace);

		String pathStylePattern = mapPattern + "/pathStyle";
		digester.addFactoryCreate(pathStylePattern, ItemDataXmlFactory.class.getName());
		digester.addSetNext(pathStylePattern, "addPathStyle", ItemData.class.getName());
		
		addItemRules(digester, pathStylePattern + "/item", "addItem", jrNamespace);
		
		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(pathStylePattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(pathStylePattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
		String pathDataPattern = mapPattern + "/pathData";
		digester.addFactoryCreate(pathDataPattern, ItemDataXmlFactory.class.getName());
		digester.addSetNext(pathDataPattern, "addPathData", ItemData.class.getName());

		addItemRules(digester, pathDataPattern + "/item", "addItem", jrNamespace);

		digester.setRuleNamespaceURI(jrNamespace);
		digester.addFactoryCreate(pathDataPattern + "/dataset", ItemDatasetFactory.class.getName());
		digester.addSetNext(pathDataPattern + "/dataset", "setDataset", JRElementDataset.class.getName());
		
		digester.setRuleNamespaceURI(componentNamespace);
		
	}

	protected void addItemRules(Digester digester, String itemPattern, String methodName, String namespace) //FIXME7 fix duplication of this method
	{
		digester.addFactoryCreate(itemPattern, ItemXmlFactory.class.getName());
		digester.addSetNext(itemPattern, methodName, Item.class.getName());

		String locationItemPropertyPattern = itemPattern + "/itemProperty";
		digester.addFactoryCreate(locationItemPropertyPattern, ItemPropertyXmlFactory.class.getName());
		digester.addSetNext(locationItemPropertyPattern, "addItemProperty", ItemProperty.class.getName());

		digester.setRuleNamespaceURI(namespace);
		
		String locationItemPropertyValueExpressionPattern = locationItemPropertyPattern + "/" + JRXmlConstants.ELEMENT_valueExpression;
		digester.addFactoryCreate(locationItemPropertyValueExpressionPattern, JRExpressionFactory.class.getName());
		digester.addCallMethod(locationItemPropertyValueExpressionPattern, "setText", 0);
		digester.addSetNext(locationItemPropertyValueExpressionPattern, "setValueExpression", JRExpression.class.getName());
	}
}
