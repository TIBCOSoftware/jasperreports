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
package com.jaspersoft.jasperreports.customvisualization.export;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.CVUtils;



public class CVElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final CVElementHtmlHandler INSTANCE = new CVElementHtmlHandler();
        private static final Log log = LogFactory.getLog(CVElementHtmlHandler.class);
        
        private static final String CV_COMPONENT_SCRIPTS_HANDLER = "cv_component_script_handler";
        private static final String CV_COMPONENT_FIRST_ATTEMPT = "cv_component_first_attempt";
        
        private static final String VELOCITY_TEMPLATE = "com/jaspersoft/jasperreports/customvisualization/templates/defaultTemplate.vm";
        private static final String COMPONENT_TEMPLATE = "com/jaspersoft/jasperreports/customvisualization/templates/component.vm";
	
	public static CVElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

        public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
            if (context == null) return "No JasperReports Context found";
            return getHtmlFragment(context.getJasperReportsContext(), context, element);
        }
        
        public String getHtmlFragment(JasperReportsContext jrContext, JRHtmlExporterContext context, JRGenericPrintElement element)
	{
            return getHtmlFragment(jrContext, context, element, false);
        }
        
	public String getHtmlFragment(JasperReportsContext jrContext, JRHtmlExporterContext context, JRGenericPrintElement element, boolean preventAnimations)
	{
            Map<String, Object> originalConfiguration = (Map<String, Object>)element.getParameterValue( CVPrintElement.CONFIGURATION);
            
            
            if (originalConfiguration == null)
            {
                log.warn("Configuration object in the element "+ element + " is NULL!");
                throw new JRRuntimeException("Configuration object in the element "+ element + " is NULL!");
            }
            
            
            // Duplicate the configuration.
            Map<String, Object> configuration = new HashMap<String,Object>();
            configuration.putAll(originalConfiguration);
            
            
            String requireJsPath = null;
            
            if (context != null &&
                context.getExporterRef() != null &&
                context.getExporterRef().getReportContext() != null)
            {
                configuration.put("isInteractiveViewer", true);
                // Load the resource trough a servlet
                requireJsPath = CVUtils.getResourceWebPath(context, jrContext, "com/jaspersoft/jasperreports/customvisualization/resources/require/require.js");
            }
            else
            {
                configuration.put("isInteractiveViewer", false);
                requireJsPath = getRequireJsPath(jrContext);
            }
            
            
            System.out.println("Require JS Path: " + requireJsPath);
            
            ObjectMapper mapper = new ObjectMapper();
            try {
                
                
                
                
                // Let's force the configuration to be regenerated all the times...
                //if (!configuration.containsKey("instanceData"))
                //{
                    Map<String, Object> jsonConfiguration = CVElementJsonHandler.createConfigurationForJSON(configuration);
                    if (preventAnimations)
                    {
                        jsonConfiguration.put("animation", false);
                    }
                    String instanceData = mapper.writeValueAsString(jsonConfiguration);
                    configuration.put("instanceData", instanceData);
                //}
                
            } catch (Exception ex)
            {
                log.warn("Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
                throw new JRRuntimeException("Error dumping the JSON for the configuration...: " + ex.getMessage());
            }
            
            configuration.put("element", element);
            
            Map<String, Object> velocityContext = new HashMap<String, Object>();
            velocityContext.put("configuration", configuration);
            
            
            velocityContext.put("module", element.getParameterValue(CVPrintElement.MODULE));
            velocityContext.put("requirejsPath", requireJsPath);
            
            velocityContext.put("script", element.getParameterValue( CVPrintElement.SCRIPT));
            
            String cssContent = (String)element.getParameterValue( CVPrintElement.CSS);
            if (cssContent != null)
            {
                // Perform a replacement of the /*elid*/ special keyword 
                cssContent = cssContent.replace("/*elid*/", "#element" + element.hashCode() );
                velocityContext.put("css", cssContent);
            }
            else
            {
                velocityContext.put("css", "");
            }
            

            StringBuilder strBuilder = new StringBuilder( );
            
//            String velocityTemplate = COMPONENT_TEMPLATE; //VELOCITY_TEMPLATE;

//            if (configuration.containsKey("velocity.template"))
//            {
//                velocityTemplate = (String)configuration.get("velocity.template");
//            }

            try {
            
                String componentHtml = fillVelocityTemplate(jrContext, COMPONENT_TEMPLATE, velocityContext);
                strBuilder.append(componentHtml);
                
                // TODO: Append extra css
                
                if (isXHtmlExporter(context))
                {
                    strBuilder.append("<style>\n")
                              .append("#element")
                              .append(element.hashCode())
                              .append(" {  position: absolute; left: ")
                              .append(element.getX())
                              .append("px; top: ")
                              .append(element.getY())
                              .append("px;  }")
                              .append("</style>");
                }
            } catch (Exception ex)
            {
                if (context == null)
                {
                    throw new JRRuntimeException(ex);
                }
                else
                {
                    return ""; // In case of error, just return an empty string....
                }
            }

            return strBuilder.toString();
                
                
        }
        
        public boolean toExport(JRGenericPrintElement element) {
		return true;
	}

    /**
     * 
     * @param context
     * @return 
     */
    private boolean isXHtmlExporter(JRHtmlExporterContext context) {
        
        return context != null && context.getExporterRef() instanceof JRXhtmlExporter;
    }

    
    
    public static String fillVelocityTemplate(JasperReportsContext jrContext, String velocityTemplate, Map<String, Object> velocityContext)
    {
            RepositoryUtil ru = RepositoryUtil.getInstance(jrContext);

            try {

                InputStream is = ru.getInputStreamFromLocation(velocityTemplate);
                InputStreamReader templateReader = new InputStreamReader( is, "UTF-8");
                StringWriter output = new StringWriter(128);
                VelocityUtil.getVelocityEngine().evaluate(new VelocityContext(velocityContext), output, velocityTemplate, templateReader);
                output.flush();
                output.close();

                return output.toString();
            
            } catch (Exception e)
            {
                throw new JRRuntimeException(e);
            }
    }

    
    /**
     * Check for a suitable require.js installation...
     * 
     * @param jrContext
     * @return 
     */
    public static String getRequireJsPath(JasperReportsContext jrContext) {
        
         if (jrContext.getProperty(CVConstants.CV_REQUIREJS_PROPERTY) != null)
         {
             return jrContext.getProperty(CVConstants.CV_REQUIREJS_PROPERTY);
         }
         
         if (jrContext.getProperty("com.jaspersoft.jasperreports.highcharts.render.require.js$url") != null)
         {
             return jrContext.getProperty("com.jaspersoft.jasperreports.highcharts.render.require.js$url");
         }
         
         
         log.warn("No property set for the require.js path. Please set the property " + CVConstants.CV_REQUIREJS_PROPERTY);
         
         return "require.js";
        
    }
    
}
