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

import java.util.HashMap;

import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.map.fill.MapFillFactory;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentManager;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.ListExtensionsRegistry;
import net.sf.jasperreports.json.export.JsonExporter;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.poi.export.JRXlsExporter;

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
public class MapExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	
	public static final String MAP_COMPONENT_NAME = "map";
	
	private static final ExtensionsRegistry REGISTRY;
	
	private static final GenericElementHandlerBundle HANDLER_BUNDLE = 
		new GenericElementHandlerBundle()
		{
			@Override
			public String getNamespace()
			{
				return JRXmlConstants.JASPERREPORTS_NAMESPACE;
			}
			
			@Override
			public GenericElementHandler getHandler(String elementName,
					String exporterKey)
			{
				if (MapComponent.MAP_ELEMENT_NAME.equals(elementName))
				{
					if (JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementGraphics2DHandler.getInstance();
					}
					else if (HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementHtmlHandler.getInstance();
					}
					else if (JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementPdfHandler.getInstance();
					}
					else if (JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementXlsHandler.getInstance();
					}
					else if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementJsonHandler.getInstance();
					}
					else if(JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementXlsxHandler.getInstance();
					}
					else if(JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementDocxHandler.getInstance();
					}
					else if(JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementPptxHandler.getInstance();
					}
					else if(JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementRtfHandler.getInstance();
					}
					else if(JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementOdtHandler.getInstance();
					}
					else if(JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementOdsHandler.getInstance();
					}
				}
				
				return null;
			}
		};

	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();
		
		HashMap<Class<? extends Component>, ComponentManager> componentManagers = new HashMap<>();
		
		DefaultComponentManager mapManager = new DefaultComponentManager();
		mapManager.setDesignConverter(MapDesignConverter.getInstance());
		mapManager.setComponentCompiler(new MapCompiler());
		mapManager.setComponentFillFactory(new MapFillFactory());
		componentManagers.put(MapComponent.class, mapManager);

		bundle.setComponentManagers(componentManagers);
		
		ListExtensionsRegistry registry = new ListExtensionsRegistry();
		registry.add(ComponentsBundle.class, bundle);
		registry.add(GenericElementHandlerBundle.class, HANDLER_BUNDLE);
		
		REGISTRY = registry;
	}
	
	@Override
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return REGISTRY;
	}
}
