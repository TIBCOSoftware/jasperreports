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
package com.jaspersoft.jasperreports.customvisualization.export;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.Processor;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementJsonHandler implements GenericElementJsonHandler
{
	private static final CVElementJsonHandler INSTANCE = new CVElementJsonHandler();
        private static final Log log = LogFactory.getLog(CVElementJsonHandler.class);

	private static final String CV_ELEMENT_JSON_TEMPLATE = "com/jaspersoft/jasperreports/customvisualization/resources/require/CVElementJsonTemplate.vm";

	public static CVElementJsonHandler getInstance()
	{
		return INSTANCE;
	}

	public String getJsonFragment(JsonExporterContext context, JRGenericPrintElement element)
	{
		Map<String, Object> contextMap = new HashMap<String, Object>();
                contextMap.put("elementId", "element" + element.hashCode());
                
                Map<String, Object> configuration = (Map<String, Object>)element.getParameterValue( CVPrintElement.CONFIGURATION);
            
                if (configuration != null)
                {
                    if ((context != null &&
                        context.getExporterRef() != null &&
                        context.getExporterRef().getReportContext() != null) || 
                        (element.getPropertiesMap().containsProperty("cv.forceRequirejs") &&
                         "true".equals( element.getPropertiesMap().getProperty("cv.forceRequirejs"))))
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
                        Map<String, Object> jsonConfiguration = createConfigurationForJSON(configuration);
                        String instanceData = mapper.writeValueAsString(jsonConfiguration);
                    
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
                    InputStream is = CVElementJsonHandler.class.getClassLoader().getResourceAsStream(CV_ELEMENT_JSON_TEMPLATE);
//                    if (is == null)
//                    {
//                        System.out.println("Unable to laod the template for json..." + CVElementJsonHandler.class.getClassLoader().getResource(CV_ELEMENT_JSON_TEMPLATE));
//                    }
                    
                    
                    InputStreamReader templateReader = new InputStreamReader( is, "UTF-8");
                    StringWriter output = new StringWriter(128);
                    VelocityUtil.getVelocityEngine().evaluate(new VelocityContext(contextMap), output, CV_ELEMENT_JSON_TEMPLATE, templateReader);
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
        public static Map<String, Object> createConfigurationForJSON(Map<String, Object> configuration)
        {
            
            // Create a copy of configuration with all the serializable objects in it...
            Map<String, Object> jsonConfiguration = new HashMap<String,Object>();
                
            JRTemplateGenericPrintElement element = (JRTemplateGenericPrintElement)configuration.remove("element");
                            
            if (configuration.containsKey("instanceData"))
            {
                jsonConfiguration.put("instanceData", configuration.get("instanceData"));
            }
            
            if (element != null)
            {
                jsonConfiguration.put(Processor.CONF_WIDTH, element.getWidth());
                jsonConfiguration.put(Processor.CONF_KEY, ""+ element.hashCode());
                jsonConfiguration.put(Processor.CONF_HEIGHT, element.getHeight());
                
                
                configuration.put(Processor.CONF_WIDTH, element.getWidth());
                configuration.put(Processor.CONF_KEY, ""+ element.hashCode());
                configuration.put(Processor.CONF_HEIGHT, element.getHeight());
                
                
                for (String prop : element.getPropertiesMap().getPropertyNames())
                {
                    jsonConfiguration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
                    configuration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
                }
            }
            
            return configuration;
        }
        
}
