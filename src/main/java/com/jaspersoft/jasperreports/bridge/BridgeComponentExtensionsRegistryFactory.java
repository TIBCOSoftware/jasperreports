package com.jaspersoft.jasperreports.bridge;

import com.jaspersoft.jasperreports.bridge.export.BridgeElementDocxHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementGraphics2DHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementHtmlHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementJExcelApiHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementJsonHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementOdsHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementOdtHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementPdfHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementPptxHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementRtfHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementXlsHandler;
import com.jaspersoft.jasperreports.bridge.export.BridgeElementXlsxHandler;
import com.jaspersoft.jasperreports.bridge.fill.BridgeFillFactory;
import com.jaspersoft.jasperreports.bridge.xml.BridgeDigester;
import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.component.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 */
public class BridgeComponentExtensionsRegistryFactory implements ExtensionsRegistryFactory
{

	
	
	//private static final ExtensionsRegistry REGISTRY;
	private static BridgeComponentBundle COMPONENT_BUNDLE;

	static
	{
		final BridgeComponentBundle bundle = new BridgeComponentBundle();

		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(BridgeConstants.NAMESPACE);
		parser.setPublicSchemaLocation(BridgeConstants.XSD_LOCATION);
		parser.setInternalSchemaResource(BridgeConstants.XSD_RESOURCE);
		parser.setDigesterConfigurer(new BridgeDigester());
                bundle.setXmlParser(parser);
		
		HashMap<String, ComponentManager> componentManagers = new HashMap<String, ComponentManager>();
		
		BridgeComponentManager componentManager = new BridgeComponentManager();
                componentManager.setDesignConverter(BridgeDesignConverter.getInstance());
		componentManager.setComponentCompiler(new BridgeCompiler());
		componentManager.setComponentFillFactory(new BridgeFillFactory());
                componentManagers.put(BridgeConstants.COMPONENT_NAME, componentManager);

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
                        	return BridgeConstants.NAMESPACE;
			}

			public GenericElementHandler getHandler(String elementName,
					String exporterKey)
			{
                            	if (BridgeConstants.COMPONENT_NAME.equals(elementName))
				{
					if(JRGraphics2DExporter.GRAPHICS2D_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementGraphics2DHandler.getInstance();
					}
					if(JRHtmlExporter.HTML_EXPORTER_KEY.equals(exporterKey) || JRXhtmlExporter.XHTML_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementHtmlHandler.getInstance();
					}
					else if(JRPdfExporter.PDF_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementPdfHandler.getInstance();
					}
                                        else if(JsonExporter.JSON_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementJsonHandler.getInstance();
					}
					else if(JRXlsExporter.XLS_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementXlsHandler.getInstance();
					}
					else if(JExcelApiExporter.JXL_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementJExcelApiHandler.getInstance();
					}
//					else if(JExcelApiMetadataExporter.JXL_METADATA_EXPORTER_KEY.equals(exporterKey))
//					{
//						return BridgeElementJExcelApiMetadataHandler.getInstance();
//					}
					else if(JRXlsxExporter.XLSX_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementXlsxHandler.getInstance();
					}
					else if(JRDocxExporter.DOCX_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementDocxHandler.getInstance();
					}
					else if(JRPptxExporter.PPTX_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementPptxHandler.getInstance();
					}
					else if(JRRtfExporter.RTF_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementRtfHandler.getInstance();
					}
					else if(JROdtExporter.ODT_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementOdtHandler.getInstance();
					}
					else if(JROdsExporter.ODS_EXPORTER_KEY.equals(exporterKey))
					{
						return BridgeElementOdsHandler.getInstance();
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
