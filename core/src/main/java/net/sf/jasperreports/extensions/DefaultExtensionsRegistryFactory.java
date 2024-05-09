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
package net.sf.jasperreports.extensions;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.components.iconlabel.IconLabelElement;
import net.sf.jasperreports.components.iconlabel.IconLabelElementCsvHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementDocxHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementGraphics2DHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementHtmlHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementOdsHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementOdtHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementPptxHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementRtfHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementXlsxHandler;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.query.DefaultQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.scriptlets.DefaultScriptletFactory;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;
import net.sf.jasperreports.engine.util.MessageProviderFactory;
import net.sf.jasperreports.engine.util.ResourceBundleMessageProviderFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JacksonReportLoader;
import net.sf.jasperreports.engine.xml.JacksonReportWriterFactory;
import net.sf.jasperreports.engine.xml.ReportLoader;
import net.sf.jasperreports.engine.xml.ReportWriterFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DefaultExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
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
				if (IconLabelElement.ELEMENT_NAME.equals(elementName))
				{
					if (JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return new IconLabelElementGraphics2DHandler();
					}		
					else if (HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementHtmlHandler.getInstance();
					}		
					else if (JRCsvExporter.CSV_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementCsvHandler.getInstance();
					}		
					else if (JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementXlsxHandler.getInstance();
					}		
					else if (JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementDocxHandler.getInstance();
					}		
					else if (JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementPptxHandler.getInstance();
					}		
					else if (JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementOdsHandler.getInstance();
					}		
					else if (JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementOdtHandler.getInstance();
					}		
					else if (JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementRtfHandler.getInstance();
					}		
//					else if (JRXmlExporter.XML_EXPORTER_KEY.equals(exporterKey))
//					{
//						return IconLabelElementXmlHandler.getInstance();
//					}		
				}
				
				return null;
			}
		};

	private static final ExtensionsRegistry defaultExtensionsRegistry = 
		new ExtensionsRegistry()
		{
			@Override
			public <T> List<T> getExtensions(Class<T> extensionType) 
			{
				if (JRQueryExecuterFactoryBundle.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object)DefaultQueryExecuterFactoryBundle.getInstance());
				}
				else if (ScriptletFactory.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object)DefaultScriptletFactory.getInstance());
				}
				else if (GenericElementHandlerBundle.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object)HANDLER_BUNDLE);
				}
				else if (MessageProviderFactory.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object) new ResourceBundleMessageProviderFactory());
				}
				else if (ReportLoader.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList(JacksonReportLoader.instance());
				}
				else if (ReportWriterFactory.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList(JacksonReportWriterFactory.instance());
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
