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
package net.sf.jasperreports.barcode4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.components.ComponentsManager;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.ListExtensionsRegistry;

/**
 * Extension registry factory that includes built-in component element
 * implementations.
 * 
 * <p>
 * This registry factory is registered by default in JasperReports.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see ListComponent
 */
public class Barcode4JExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{

	public static final String NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports/components";
	public static final String XSD_LOCATION = 
		"http://jasperreports.sourceforge.net/xsd/components.xsd";
	public static final String XSD_RESOURCE = 
		"net/sf/jasperreports/components/components.xsd";
	
	public static final String LIST_COMPONENT_NAME = "list";
	public static final String TABLE_COMPONENT_NAME = "table";
	public static final String BARBECUE_COMPONENT_NAME = "barbecue";
	public static final List<String> BARCODE4J_COMPONENT_NAMES = Collections.unmodifiableList(Arrays.asList(
			"Codabar", "Code128", "EAN128", "DataMatrix", "Code39", "Interleaved2Of5",
			"UPCA", "UPCE", "EAN13", "EAN8", "USPSIntelligentMail", "RoyalMailCustomer", 
			"POSTNET", "PDF417", "QRCode"));
	public static final String SPIDERCHART_COMPONENT_NAME = "spiderChart";
	public static final String MAP_COMPONENT_NAME = "map";
	public static final String SORT_COMPONENT_NAME = "sort";
	public static final String ICONLABEL_COMPONENT_NAME = "iconLabel";
	
	private static final ExtensionsRegistry REGISTRY;
	
	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();

		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(NAMESPACE);
		parser.setPublicSchemaLocation(XSD_LOCATION);
		parser.setInternalSchemaResource(XSD_RESOURCE);
		parser.setDigesterConfigurer(new Barcode4JXmlDigesterConfigurer());
		bundle.setXmlParser(parser);
		
		HashMap<String, ComponentManager> componentManagers = new HashMap<>();
		
		ComponentsManager barcode4jManager = new ComponentsManager();
		barcode4jManager.setDesignConverter(new BarcodeDesignConverter());
		barcode4jManager.setComponentCompiler(new BarcodeCompiler());
		//barcode4jManager.setComponentXmlWriter(xmlHandler);
		barcode4jManager.setComponentFillFactory(new BarcodeFillFactory());
		for (String name : BARCODE4J_COMPONENT_NAMES)
		{
			componentManagers.put(name, barcode4jManager);
		}
		
		bundle.setComponentManagers(componentManagers);
		
		ListExtensionsRegistry registry = new ListExtensionsRegistry();
		registry.add(ComponentsBundle.class, bundle);
		
		REGISTRY = registry;
	}
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return REGISTRY;
	}

}