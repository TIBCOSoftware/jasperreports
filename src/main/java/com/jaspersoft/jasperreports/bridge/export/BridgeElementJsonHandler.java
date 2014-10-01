/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.bridge.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.jasperreports.bridge.BridgePrintElement;
import com.jaspersoft.jasperreports.bridge.Processor;
import com.jaspersoft.jasperreports.bridge.ScriptsHandler;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.web.util.VelocityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class BridgeElementJsonHandler implements GenericElementJsonHandler
{
	private static final BridgeElementJsonHandler INSTANCE = new BridgeElementJsonHandler();
        private static final Log log = LogFactory.getLog(BridgeElementJsonHandler.class);

	private static final String BRIDGE_ELEMENT_JSON_TEMPLATE = "com/jaspersoft/jasperreports/bridge/resources/require/BridgeElementJsonTemplate.vm";

	public static BridgeElementJsonHandler getInstance()
	{
		return INSTANCE;
	}

	public String getJsonFragment(JsonExporterContext context, JRGenericPrintElement element)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
                contextMap.put("elementId", "element" + element.hashCode());
                
                Map<String, Object> configuration = (Map<String, Object>)element.getParameterValue( BridgePrintElement.CONFIGURATION);
            
                
                if (configuration != null)
                {
                    if (context != null &&
                        context.getExporterRef() != null &&
                        context.getExporterRef().getReportContext() != null)
                    {
                        configuration.put("isInteractiveViewer", true);
                    }
                    else
                    {
                        configuration.put("isInteractiveViewer", false);
                    }
                }
            
                
                ObjectMapper mapper = new ObjectMapper();
                try {
                    
                    if (!configuration.containsKey("instanceData"))
                    {
                        cleanConfigurationForJSON(configuration);
                        String instanceData = mapper.writeValueAsString(configuration);
                    
                        configuration.put("instanceData", instanceData);
                    }
                } catch (Exception ex)
                {
                    log.warn("(JSON): Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
                }
                
                contextMap.put("configuration", configuration);
                    
                // Pre export the object programmatically...
                
                //RepositoryUtil ru = RepositoryUtil.getInstance(context.getJasperReportsContext());
                String s = "{}";
                try {
                    // Loading resource...
                    InputStream is = BridgeElementJsonHandler.class.getClassLoader().getResourceAsStream(BRIDGE_ELEMENT_JSON_TEMPLATE);
//                    if (is == null)
//                    {
//                        System.out.println("Unable to laod the template for json..." + BridgeElementJsonHandler.class.getClassLoader().getResource(BRIDGE_ELEMENT_JSON_TEMPLATE));
//                    }
                    
                    
                    InputStreamReader templateReader = new InputStreamReader( is, "UTF-8");
                    StringWriter output = new StringWriter(128);
                    VelocityUtil.getVelocityEngine().evaluate(new VelocityContext(contextMap), output, BRIDGE_ELEMENT_JSON_TEMPLATE, templateReader);
                    output.flush();
                    output.close();

                    s = output.toString();
                
            
                } catch (Exception e)
                {
                    e.printStackTrace();
                    throw new JRRuntimeException(e);
                }
                return s;
	}

	public boolean toExport(JRGenericPrintElement element)
        {
		return true;
	}

        
        
        /**
         * Clean the configuration object.
         * 
         * This function removes objects in the configuration map that are not
         * convertible in JSON (script object and design element).
         * It also adds some useful properties for the element such as width, height
         * and all the elements properties in form of property.xyz
         * 
         * 
         * @param configuration
         * @return 
         */
        public static Map<String, Object> cleanConfigurationForJSON(Map<String, Object> configuration)
        {
            
            JRTemplateGenericPrintElement element = (JRTemplateGenericPrintElement)configuration.remove("element");
                            
            // Strip out from the configuration all the stuff not useful for the JSON object....
            configuration.remove("scripts");
            
            if (!configuration.containsKey("instanceData"))
            {
                configuration.remove("instanceData");
            }
            
            if (element != null)
            {
                configuration.put(Processor.CONF_WIDTH, element.getWidth());
                configuration.put(Processor.CONF_KEY, ""+ element.hashCode());
                configuration.put(Processor.CONF_HEIGHT, element.getHeight());
                for (String prop : element.getPropertiesMap().getPropertyNames())
                {
                    configuration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
                }
            }
            
            return configuration;
        }
        
}
