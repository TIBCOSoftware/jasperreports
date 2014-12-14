/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * The Custom Visualization Component program and the accompanying materials
 * has been dual licensed under the the following licenses:
 * 
 * Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Custom Visualization Component is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.jaspersoft.jasperreports.customvisualization;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentManager;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.export.GenericElementHandler;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

import com.jaspersoft.jasperreports.customvisualization.export.CVElementDocxHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementGraphics2DHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementHtmlHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementJExcelApiHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementJsonHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementOdsHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementOdtHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementPdfHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementPptxHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementRtfHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementXlsHandler;
import com.jaspersoft.jasperreports.customvisualization.export.CVElementXlsxHandler;
import com.jaspersoft.jasperreports.customvisualization.fill.CVFillFactory;
import com.jaspersoft.jasperreports.customvisualization.xml.CVDigester;

/**
 */
public class CVComponentExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	
	
	//private static final ExtensionsRegistry REGISTRY;
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


        private static final ExtensionsRegistry defaultExtensionsRegistry =
		new ExtensionsRegistry()
		{
			public <T> List<T> getExtensions(Class<T> extensionType)
			{
				if (GenericElementHandlerBundle.class.equals(extensionType))
				{
                                    return (List<T>) Collections.singletonList((Object)HANDLER_BUNDLE);
				}
                                else if (ComponentsBundle.class.equals(extensionType))
                                {
                                    return (List<T>) Collections.singletonList((Object)COMPONENT_BUNDLE);
                                }
				return null;
			}
		};


        private static final GenericElementHandlerBundle HANDLER_BUNDLE =
		new GenericElementHandlerBundle()
		{
			public String getNamespace()
			{
                        	return CVConstants.NAMESPACE;
			}

			public GenericElementHandler getHandler(String elementName,
					String exporterKey)
			{
                            	if (CVConstants.COMPONENT_NAME.equals(elementName))
				{
					if(JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementGraphics2DHandler.getInstance();
					}
					if(JRHtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey) || JRXhtmlExporter.XHTML_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementHtmlHandler.getInstance();
					}
					else if(JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementPdfHandler.getInstance();
					}
                                        else if(JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementJsonHandler.getInstance();
					}
					else if(JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementXlsHandler.getInstance();
					}
					else if(JExcelApiExporter.JXL_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementJExcelApiHandler.getInstance();
					}
//					else if(JExcelApiMetadataExporter.JXL_METADATA_EXPORTER_KEY.equals(exporterKey))
//					{
//						return CVElementJExcelApiMetadataHandler.getInstance();
//					}
					else if(JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementXlsxHandler.getInstance();
					}
					else if(JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementDocxHandler.getInstance();
					}
					else if(JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementPptxHandler.getInstance();
					}
					else if(JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementRtfHandler.getInstance();
					}
					else if(JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementOdtHandler.getInstance();
					}
					else if(JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
					{
						return CVElementOdsHandler.getInstance();
					}
				}
				
				return null;
			}
		};

	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
           	return defaultExtensionsRegistry;
	}

}
