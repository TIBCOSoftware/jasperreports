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
package net.sf.jasperreports.charts;

import java.util.HashMap;

import net.sf.jasperreports.charts.fill.DefaultChartTheme;
import net.sf.jasperreports.components.spiderchart.SpiderChartCompiler;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponentManager;
import net.sf.jasperreports.components.spiderchart.SpiderChartDesignConverter;
import net.sf.jasperreports.components.spiderchart.SpiderChartFillFactory;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.ListExtensionsRegistry;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ChartsExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	public static final String NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports/components";
	public static final String XSD_LOCATION = 
		"http://jasperreports.sourceforge.net/xsd/charts.xsd";
	public static final String XSD_RESOURCE = 
		"net/sf/jasperreports/charts/charts.xsd";
	
	public static final String SPIDERCHART_COMPONENT_NAME = "spiderChart";
	
	private static final ExtensionsRegistry REGISTRY;
	
	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();

		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(NAMESPACE);
		parser.setPublicSchemaLocation(XSD_LOCATION);
		parser.setInternalSchemaResource(XSD_RESOURCE);
		parser.setDigesterConfigurer(new ChartsXmlDigesterConfigurer());
		bundle.setXmlParser(parser);
		
		HashMap<String, ComponentManager> componentManagers = new HashMap<>();
		
		SpiderChartComponentManager spiderChartManager = new SpiderChartComponentManager();
		spiderChartManager.setDesignConverter(new SpiderChartDesignConverter());
		spiderChartManager.setComponentCompiler(new SpiderChartCompiler());
		//spiderChartManager.setComponentXmlWriter(xmlHandler);
		spiderChartManager.setComponentFillFactory(new SpiderChartFillFactory());
		componentManagers.put(SPIDERCHART_COMPONENT_NAME, spiderChartManager);
		
		bundle.setComponentManagers(componentManagers);
		
		ListExtensionsRegistry registry = new ListExtensionsRegistry();
		registry.add(ComponentsBundle.class, bundle);
		registry.add(ChartThemeBundle.class, DefaultChartTheme.BUNDLE);
		
		REGISTRY = registry;
	}
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return REGISTRY;
	}

}
