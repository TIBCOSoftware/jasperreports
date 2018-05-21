/*******************************************************************************
 * Copyright (C) 2005 - 2016 TIBCO Software Inc. All rights reserved.
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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRGenericPrintElement;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.export.JRExporterContext;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.util.WebUtil;

/**
 *
 * @author gtoffoli
 */
public class CVUtils
{
	public static String getResourceWebPath(JRHtmlExporterContext context, JasperReportsContext jrContext, String path)
	{
		ReportContext reportContext = null;

		if (context != null && context.getExporterRef() != null)
		{
			reportContext = context.getExporterRef().getReportContext();
		}

		WebUtil webUtil = WebUtil.getInstance(jrContext);

		if (
			!path.toLowerCase().startsWith("http://") 
			&& !path.toLowerCase().startsWith("https://")
			&& !path.toLowerCase().startsWith("://")
			)
		{
			path = webUtil.getResourcePath(path);
			String contextPath = null;
			if (
				reportContext instanceof WebReportContext 
				&& ((WebReportContext) reportContext).getParameterValue(net.sf.jasperreports.web.WebReportContext.APPLICATION_CONTEXT_PATH) != null
				)
			{
				contextPath = "" + ((WebReportContext) reportContext)
						.getParameterValue(net.sf.jasperreports.web.WebReportContext.APPLICATION_CONTEXT_PATH);
			}
			else if (context != null)
			{
				contextPath = getRequestContextPath(context);
			}

			path = contextPath + path;
		}

		return path;
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

	private static String doGetRequestContextPath(JRExporterContext exporterContext)
	{
		HttpServletRequest request = getRequest(exporterContext);

		if (request == null)
		{
			return null;
		}

		return request.getContextPath();
	}

	private static HttpServletRequest getRequest(JRExporterContext exporterContext)
	{
		HttpServletRequest request = null;

		Map<String, Object> values = exporterContext.getValues();
		if (values != null)
		{
			for (Object value : values.values())
			{
				if (value instanceof HttpServletRequest)
				{
					request = (HttpServletRequest) value;
					break;
				}
			}
		}

		if (request == null)
		{
			@SuppressWarnings("deprecation")
			Map<net.sf.jasperreports.engine.JRExporterParameter, Object> params = exporterContext.getExportParameters();
			if (params != null)
			{
				for (Object param : params.values())
				{
					if (param instanceof HttpServletRequest)
					{
						request = (HttpServletRequest) param;
						break;
					}
				}
			}
		}

		return request;
	}
        
        
        /**
         * Check if the element configuration specifies to render this component as png instead of looking for an SVG
         * 
         * @param element
         * @return 
         */
        public static boolean isRenderAsPng(JRGenericPrintElement element) {
            
            if (element.hasParameter(CVPrintElement.CONFIGURATION))
            {
                Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
                if (componentConfiguration.containsKey("renderAsPng"))
                {
                    Object renderAsPngProperty = componentConfiguration.get("renderAsPng");
                    if (renderAsPngProperty != null)
                    {
                        return Boolean.valueOf(renderAsPngProperty.toString()).booleanValue();
                    }
                }
            }
            return false;
        }
        
        
        /**
         * Check if the element configuration specifies the zoom to be used when the export is performed as raster image.
         * Default is 1.0f.
         * Bigger is the zoom, higher is the quality, bigger is the file exported.
         * 
         * @param element
         * @return 
         */
        public static float getZoomFactor(JRGenericPrintElement element) {
            
            if (element.hasParameter(CVPrintElement.CONFIGURATION))
            {
                Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
                if (componentConfiguration.containsKey("render-zoom-factor"))
                {
                    Object property = componentConfiguration.get("render-zoom-factor");
                    if (property != null)
                    {
                        try {
                            return Float.valueOf(property.toString()).floatValue();
                        } catch (Exception ex)
                        {
                            
                        }
                    }
                }
            }
            return 1.0f;
        }
        
        
        
        /**
         * Check if the element configuration specifies the zoom to be used when the export is performed as raster image.
         * Default is 2.0f.
         * Bigger is the zoom, higher is the quality, bigger is the file exported.
         * 
         * @param element
         * @return 
         */
        public static long getTimeout(JRGenericPrintElement element) {
            
            if (element.hasParameter(CVPrintElement.CONFIGURATION))
            {
                Map<String, Object> componentConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);
                if (componentConfiguration.containsKey("render-timeout"))
                {
                    Object property = componentConfiguration.get("render-timeout");
                    if (property != null)
                    {
                        try {
                            return Long.valueOf(property.toString()).longValue();
                        } catch (Exception ex)
                        {
                            
                        }
                    }
                }
            }
            return 3000;
        }
}
