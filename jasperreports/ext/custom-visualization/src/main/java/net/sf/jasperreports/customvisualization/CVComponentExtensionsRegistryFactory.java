/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.customvisualization.export.CVElementDocxHandler;
import net.sf.jasperreports.customvisualization.export.CVElementGraphics2DHandler;
import net.sf.jasperreports.customvisualization.export.CVElementHtmlHandler;
import net.sf.jasperreports.customvisualization.export.CVElementJsonHandler;
import net.sf.jasperreports.customvisualization.export.CVElementOdsHandler;
import net.sf.jasperreports.customvisualization.export.CVElementOdtHandler;
import net.sf.jasperreports.customvisualization.export.CVElementPdfHandler;
import net.sf.jasperreports.customvisualization.export.CVElementPptxHandler;
import net.sf.jasperreports.customvisualization.export.CVElementRtfHandler;
import net.sf.jasperreports.customvisualization.export.CVElementXlsHandler;
import net.sf.jasperreports.customvisualization.export.CVElementXlsxHandler;
import net.sf.jasperreports.customvisualization.fill.CVFillFactory;
import net.sf.jasperreports.customvisualization.xml.CVDigester;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 */
public class CVComponentExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	// private static final ExtensionsRegistry REGISTRY;
	private static CVComponentBundle COMPONENT_BUNDLE;

	static
	{
		final CVComponentBundle bundle = new CVComponentBundle();

		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(CVConstants.NAMESPACE);
		parser.setPublicSchemaLocation(CVConstants.XSD_LOCATION);
		parser.setInternalSchemaResource(CVConstants.XSD_RESOURCE);
		parser.setDigesterConfigurer(new CVDigester());
		bundle.setXmlParser(parser);

		HashMap<String, ComponentManager> componentManagers = new HashMap<String, ComponentManager>();

		CVComponentManager componentManager = new CVComponentManager();
		componentManager.setDesignConverter(CVDesignConverter.getInstance());
		componentManager.setComponentCompiler(new CVCompiler());
		componentManager.setComponentFillFactory(new CVFillFactory());
		componentManagers.put(CVConstants.COMPONENT_NAME, componentManager);

		bundle.setComponentManagers(componentManagers);

		COMPONENT_BUNDLE = bundle;
	}

	private static final ExtensionsRegistry defaultExtensionsRegistry = new ExtensionsRegistry()
	{
		@Override
		public <T> List<T> getExtensions(Class<T> extensionType)
		{
			if (GenericElementHandlerBundle.class.equals(extensionType))
			{
				return (List<T>) Collections.singletonList((Object) HANDLER_BUNDLE);
			}
			else if (ComponentsBundle.class.equals(extensionType))
			{
				return (List<T>) Collections.singletonList((Object) COMPONENT_BUNDLE);
			}
			return null;
		}
	};

	private static final GenericElementHandlerBundle HANDLER_BUNDLE = new GenericElementHandlerBundle()
	{
		@Override
		public String getNamespace()
		{
			return CVConstants.NAMESPACE;
		}

		@Override
		@SuppressWarnings("deprecation")
		public GenericElementHandler getHandler(String elementName, String exporterKey)
		{
			if (CVConstants.COMPONENT_NAME.equals(elementName))
			{
				if (JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementGraphics2DHandler.getInstance();
				}
				if (HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementHtmlHandler.getInstance();
				}
				else if (JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementPdfHandler.getInstance();
				}
				else if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementJsonHandler.getInstance();
				}
				else if (JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementXlsHandler.getInstance();
				}
				else if (JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementXlsxHandler.getInstance();
				}
				else if (JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementDocxHandler.getInstance();
				}
				else if (JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementPptxHandler.getInstance();
				}
				else if (JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementRtfHandler.getInstance();
				}
				else if (JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementOdtHandler.getInstance();
				}
				else if (JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
				{
					return CVElementOdsHandler.getInstance();
				}
			}

			return null;
		}
	};

	@Override
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties)
	{
		return defaultExtensionsRegistry;
	}
}
