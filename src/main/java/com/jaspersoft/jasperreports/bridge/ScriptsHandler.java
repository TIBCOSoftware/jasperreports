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
package com.jaspersoft.jasperreports.bridge;

import com.jaspersoft.jasperreports.bridge.export.BridgeElementHtmlHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.JRExporterContext;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.WebUtil;


/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class ScriptsHandler {
        
        public static final String  IMPORTED_SCRIPTS = "bridge_imported_scripts";
        private static final String VELOCITY_TEMPLATE_REQUIRE_JS = "com/jaspersoft/jasperreports/bridge/resources/require/require.vm";
        
        private JRHtmlExporterContext context = null;
        private JRPrintElement element = null;
        private JasperReportsContext jasperReportsContext = null;
        
        public ScriptsHandler()
        {
        }
        
        public ScriptsHandler(JRHtmlExporterContext context, JRPrintElement element)
        {
            this(context, element, null);
        }
        
        public ScriptsHandler(JRHtmlExporterContext context, JRPrintElement element, JasperReportsContext jrContext)
        {
            this.context = context;
            this.element = element;
            
            if (jrContext == null && context != null)
            {
                this.jasperReportsContext = context.getJasperReportsContext();
            }
            else
            {
                this.jasperReportsContext = jrContext;
            }
        }

        /**
         * @return the context
         */
        public JRHtmlExporterContext getContext() {
            return context;
        }

        /**
         * @param context the context to set
         */
        public void setContext(JRHtmlExporterContext context) {
            this.context = context;
        }
        
        

        public String add(String url)
        {
            return add(url, url);
        }
        
        public String add(String key, String url)
        {
            
            if (getContext() != null)
            {
                TreeMap<String,String> scripts = (TreeMap<String,String>) getContext().getValue(IMPORTED_SCRIPTS);
                if (scripts == null)
                {
                    scripts = new TreeMap<String,String>();
                }
                
                
                // add the script only if it has not been added before...
                if (!scripts.containsKey(url) && !scripts.containsValue(url))
                {
                    scripts.put(key, url);
                    getContext().setValue(IMPORTED_SCRIPTS, scripts);
                }
                else
                {
                    // Nothing to write...
                    return "";
                }
            }
            
            return "<script class=\"jasperreports\" type=\"text/javascript\" src=\"" + url + "\"></script>";
        }
        
        
        
        /**
         * Aggregate all the required javascript files.
         * 
         * 
         * 
         * 
         * @param arguments
         * @return 
         */
        public String require(Object ... objects)
        {
            // Report context is only true when the report is exported in a Jive environment.
            ReportContext reportContext = null;
            if (getContext() != null &&
                getContext().getExporterRef() != null)
            {
                 reportContext = getContext().getExporterRef().getReportContext();
            }
            
            
            if (reportContext == null)
            {
                StringBuilder strBuilder = new StringBuilder();
                
                for (Object obj : objects)
                {
                 
                    String path = null;
                    String pathName = null;
                    if (obj == null) continue;
                    
                    if (obj instanceof Map)
                    {
                        Map  map = (Map)obj;
                        
                        
                        if (map.containsKey("key"))
                        {
                            String propName = BridgeConstants.BRIDGE_SCRIPT_PATH_PROPERTY + map.get("key");
                            path = getJasperReportsContext().getProperty(propName);
                        }
                        
                        if ( (path == null || path.length() == 0) && map.containsKey("path") && map.get("path") != null)
                        {
                            path =  (String)map.get("path");
                        }
                    }
                    else //if (obj instanceof String)
                    {
                        path =  obj.toString();
                    }
                    
                    if (path != null && path.length() > 0)
                    {
                        strBuilder.append( add( path) );
                    }
                }
                
                return strBuilder.toString();
            }
            else
            {
                //
                Map<String, Object> scriptContext = new HashMap<String, Object>();
                scriptContext.put("elementId", element == null ? "" : element.hashCode());
                
                List<Map<String, String>> scriptConfigs = new ArrayList<Map<String, String>>();
                
                for (Object obj : objects)
                {
                    if (obj == null) continue;
                    if (obj instanceof String)
                    {
                        HashMap<String, String> scriptConfig = new HashMap<String, String>();
                        scriptConfig.put("path", (String)obj);
                        scriptConfig.put("name", (String)obj);
                        scriptConfig.put("export", "");
                        scriptConfigs.add(scriptConfig);
                    }
                    else if (obj instanceof Map)
                    {
                        Map  map = (Map)obj;
                        WebUtil webUtil = WebUtil.getInstance(getJasperReportsContext());
                        
                        
                        
                        if (!map.containsKey("path") || map.get("path") == null) continue;
                        
                        HashMap<String, String> scriptConfig = new HashMap<String, String>();
                        
                        String path = (String)map.get("path");
                        
                        if (!path.toLowerCase().startsWith("http://") && 
                            !path.toLowerCase().startsWith("https://") &&
                            !path.toLowerCase().startsWith("://"))
                        {
                            path = webUtil.getResourcePath(path);
                            String contextPath = null;
                            if (reportContext instanceof WebReportContext &&
                                ((WebReportContext)reportContext).getParameterValue( net.sf.jasperreports.web.WebReportContext.APPLICATION_CONTEXT_PATH ) != null)
                            {
                                contextPath = "" + ((WebReportContext)reportContext).getParameterValue( net.sf.jasperreports.web.WebReportContext.APPLICATION_CONTEXT_PATH );
                                
                            }
                            else if (getContext() != null)
                            {
                                contextPath = getRequestContextPath(getContext());
                            
                            }
                            path = contextPath + path;
                        }
                        
                        scriptConfig.put("path", path);
                        scriptConfig.put("name", (map.containsKey("name") && ((String)map.get("name")).length() > 0) ?  (String)map.get("name") : "");
                        scriptConfig.put("export", map.containsKey("export") ?  (String)map.get("export") : "");
                        scriptConfigs.add(scriptConfig);
                    }
                }
                
                scriptContext.put("scripts",  scriptConfigs );
                return BridgeElementHtmlHandler.fillVelocityTemplate(getContext().getJasperReportsContext(), VELOCITY_TEMPLATE_REQUIRE_JS, scriptContext);
            }
            
        }

    /**
     * @return the jasperReportsContext
     */
    public JasperReportsContext getJasperReportsContext() {
        return jasperReportsContext;
    }

    /**
     * @param jasperReportsContext the jasperReportsContext to set
     */
    public void setJasperReportsContext(JasperReportsContext jasperReportsContext) {
        this.jasperReportsContext = jasperReportsContext;
    }

    
    
    public static String getRequestContextPath(JRExporterContext exporterContext)
    {
            try
            {
                    Class.forName("javax.servlet.http.HttpServletRequest");
            }
            catch (ClassNotFoundException e)
            {
                    // not in a servlet container
                    return null;
            }

            return doGetRequestContextPath(exporterContext);
    }

    private static String doGetRequestContextPath(
                    JRExporterContext exporterContext)
    {
            HttpServletRequest request = null;
            Map params = exporterContext.getExportParameters();
            for (Iterator it = params.values().iterator(); it.hasNext();)
            {
                    Object param = it.next();
                    if (param instanceof HttpServletRequest)
                    {
                            request = (HttpServletRequest) param;
                            break;
                    }
            }

            if (request == null)
            {
                    return null;
            }

            return request.getContextPath();
    }

}
