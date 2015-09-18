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
package net.sf.jasperreports.extensions;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.components.headertoolbar.json.HeaderToolbarElementJsonHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElement;
import net.sf.jasperreports.components.iconlabel.IconLabelElementCsvHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementDocxHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementGraphics2DHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementHtmlHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementOdsHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementOdtHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementPdfHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementPptxHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementRtfHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementXlsHandler;
import net.sf.jasperreports.components.iconlabel.IconLabelElementXlsxHandler;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.MapElementDocxHandler;
import net.sf.jasperreports.components.map.MapElementGraphics2DHandler;
import net.sf.jasperreports.components.map.MapElementHtmlHandler;
import net.sf.jasperreports.components.map.MapElementJsonHandler;
import net.sf.jasperreports.components.map.MapElementOdsHandler;
import net.sf.jasperreports.components.map.MapElementOdtHandler;
import net.sf.jasperreports.components.map.MapElementPdfHandler;
import net.sf.jasperreports.components.map.MapElementPptxHandler;
import net.sf.jasperreports.components.map.MapElementRtfHandler;
import net.sf.jasperreports.components.map.MapElementXlsHandler;
import net.sf.jasperreports.components.map.MapElementXlsxHandler;
import net.sf.jasperreports.components.sort.SortElement;
import net.sf.jasperreports.components.sort.SortElementHtmlHandler;
import net.sf.jasperreports.components.sort.SortElementJsonHandler;
import net.sf.jasperreports.crosstabs.interactive.CrosstabInteractiveJsonHandler;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.export.FlashHtmlHandler;
import net.sf.jasperreports.engine.export.FlashPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
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
import net.sf.jasperreports.engine.fill.DefaultChartTheme;
import net.sf.jasperreports.engine.fill.JRFillCrosstab;
import net.sf.jasperreports.engine.query.DefaultQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.scriptlets.DefaultScriptletFactory;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;
import net.sf.jasperreports.engine.util.MessageProviderFactory;
import net.sf.jasperreports.engine.util.ResourceBundleMessageProviderFactory;
import net.sf.jasperreports.engine.xml.JRXmlConstants;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class DefaultExtensionsRegistryFactory implements ExtensionsRegistryFactory
{
	private static final GenericElementHandlerBundle HANDLER_BUNDLE = 
		new GenericElementHandlerBundle()
		{
			public String getNamespace()
			{
				return JRXmlConstants.JASPERREPORTS_NAMESPACE;
			}
			
			public GenericElementHandler getHandler(String elementName,
					String exporterKey)
			{
				@SuppressWarnings("deprecation")
				String depXhtmlKey = net.sf.jasperreports.engine.export.JRXhtmlExporter.XHTML_EXPORTER_KEY;
				@SuppressWarnings("deprecation")
				String depJExcelApiKey = net.sf.jasperreports.engine.export.JExcelApiExporter.JXL_EXPORTER_KEY;
				if (
					FlashPrintElement.FLASH_ELEMENT_NAME.equals(elementName) 
					&& HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey)
					)
				{
					return FlashHtmlHandler.getInstance();
				}
				if (MapComponent.MAP_ELEMENT_NAME.equals(elementName))
				{
					if(JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementGraphics2DHandler.getInstance();
					}
					if(
						HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey) 
						|| depXhtmlKey.equals(exporterKey)
						)
					{
						return MapElementHtmlHandler.getInstance();
					}
					else if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementJsonHandler.getInstance();
					}
					else if(JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementPdfHandler.getInstance();
					}
					else if(JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
					{
						return MapElementXlsHandler.getInstance();
					}
					else if(depJExcelApiKey.equals(exporterKey))
					{
						@SuppressWarnings("deprecation")
						net.sf.jasperreports.components.map.MapElementJExcelApiHandler depHandler = 
							net.sf.jasperreports.components.map.MapElementJExcelApiHandler.getInstance();
						return depHandler;
					}
//					else if(JExcelApiMetadataExporter.JXL_METADATA_EXPORTER_KEY.equals(exporterKey))
//					{
//						return MapElementJExcelApiMetadataHandler.getInstance();
//					}
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
				if (SortElement.SORT_ELEMENT_NAME.equals(elementName))
				{
					if (HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey))
					{
						return new SortElementHtmlHandler();
					} else if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return new SortElementJsonHandler();
					}
				}
				if (HeaderToolbarElement.ELEMENT_NAME.equals(elementName) && JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
				{
					return new HeaderToolbarElementJsonHandler();
				}
				if (IconLabelElement.ELEMENT_NAME.equals(elementName))
				{
					if (JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
					{
						return new IconLabelElementPdfHandler();
					}
					else if (JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return new IconLabelElementGraphics2DHandler();
					}		
					else if (
						HtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey) 
						|| depXhtmlKey.equals(exporterKey)
						)
					{
						return IconLabelElementHtmlHandler.getInstance();
					}		
					else if (JRCsvExporter.CSV_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementCsvHandler.getInstance();
					}		
					else if (depJExcelApiKey.equals(exporterKey))
					{
						@SuppressWarnings("deprecation")
						net.sf.jasperreports.components.iconlabel.IconLabelElementJExcelApiHandler depHandler =
							net.sf.jasperreports.components.iconlabel.IconLabelElementJExcelApiHandler.getInstance();
						return depHandler;
					}		
					else if (JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
					{
						return IconLabelElementXlsHandler.getInstance();
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
				
				if (JRFillCrosstab.CROSSTAB_INTERACTIVE_ELEMENT_NAME.equals(elementName))
				{
					if (JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return new CrosstabInteractiveJsonHandler();
					}
				}
				
				return null;
			}
		};

	private static final ExtensionsRegistry defaultExtensionsRegistry = 
		new ExtensionsRegistry()
		{
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
				else if (ChartThemeBundle.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object)DefaultChartTheme.BUNDLE);
				}
				else if (GenericElementHandlerBundle.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object)HANDLER_BUNDLE);
				}
				else if (MessageProviderFactory.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList((Object) new ResourceBundleMessageProviderFactory());
				}
				return null;
			}
		};
	
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return defaultExtensionsRegistry;
	}
}
