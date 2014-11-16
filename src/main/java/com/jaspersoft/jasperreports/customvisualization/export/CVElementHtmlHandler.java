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
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.ScriptsHandler;



public class CVElementHtmlHandler implements GenericElementHtmlHandler
{
	private static final CVElementHtmlHandler INSTANCE = new CVElementHtmlHandler();
        private static final Log log = LogFactory.getLog(CVElementHtmlHandler.class);
        
        private static final String CV_COMPONENT_SCRIPTS_HANDLER = "cv_component_script_handler";
        private static final String CV_COMPONENT_FIRST_ATTEMPT = "cv_component_first_attempt";
        private static final String VELOCITY_TEMPLATE = "com/jaspersoft/jasperreports/customvisualization/templates/defaultTemplate.vm";
	
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
            Map<String, Object> originalConfiguration = (Map<String, Object>)element.getParameterValue( CVPrintElement.CONFIGURATION);
            
            
            if (originalConfiguration == null)
            {
                log.warn("Configuration object in the element "+ element + " is NULL!");
                throw new JRRuntimeException("Configuration object in the element "+ element + " is NULL!");
            }
            
            // Duplicate the configuration.
            Map<String, Object> configuration = new HashMap<String,Object>();
            configuration.putAll(configuration);
            
            
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
            
            
            
            ObjectMapper mapper = new ObjectMapper();
            try {
                
                if (!configuration.containsKey("instanceData"))
                {
                    Map<String, Object> jsonConfiguration = CVElementJsonHandler.createConfigurationForJSON(configuration);
                    String instanceData = mapper.writeValueAsString(jsonConfiguration);
                    configuration.put("instanceData", instanceData);
                }
            } catch (Exception ex)
            {
                log.warn("Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
                ex.printStackTrace();
                throw new JRRuntimeException("Error dumping the JSON for the configuration...: " + ex.getMessage());
            }
            
            configuration.put("element", element);
            configuration.put("scripts", new ScriptsHandler(context, element, jrContext));
            
            Map<String, Object> velocityContext = new HashMap<String, Object>();
            velocityContext.put("configuration", configuration);

            StringBuilder strBuilder = new StringBuilder( );
            
            String velocityTemplate = VELOCITY_TEMPLATE;

            if (configuration.containsKey("velocity.template"))
            {
                velocityTemplate = (String)configuration.get("velocity.template");
            }

            try {
            
                String mainHtml = fillVelocityTemplate(jrContext, velocityTemplate, velocityContext);


                strBuilder.append(mainHtml);

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
    
}
