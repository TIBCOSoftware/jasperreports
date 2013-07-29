/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
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
package net.sf.jasperreports.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.web.util.RequirejsModuleMapping;
import net.sf.jasperreports.web.util.VelocityUtil;
import net.sf.jasperreports.web.util.WebUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Narcis Marcu(nmarcu@users.sourceforge.net)
 * @version $Id$
 */
public class RequirejsConfigServlet extends AbstractServlet
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private static final Log log = LogFactory.getLog(RequirejsConfigServlet.class);
	private static final String REQUIREJS_CONFIG_TEMPLATE = "net/sf/jasperreports/web/servlets/resources/templates/RequirejsConfigTemplate.vm";


	/**
	 *
	 */
	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		response.setContentType("text/javascript; charset=UTF-8");
		setNoExpire(response);

		PrintWriter out = response.getWriter();
        WebUtil webUtil = WebUtil.getInstance(getJasperReportsContext());

        List<RequirejsModuleMapping> requirejsMappings = getJasperReportsContext().getExtensions(RequirejsModuleMapping.class);
        Map<String, String> modulePaths = new LinkedHashMap<String, String>();

        for (RequirejsModuleMapping requirejsMapping : requirejsMappings)
        {
            String modulePath = requirejsMapping.getPath();
            if (requirejsMapping.isClasspathResource())
            {
                modulePath = request.getContextPath() + webUtil.getResourcesBasePath() + modulePath;
            }
            modulePaths.put(requirejsMapping.getName(), modulePath);
        }

        Map<String, Object> contextMap = new HashMap<String, Object>();

        contextMap.put("contextPath", request.getContextPath());
        contextMap.put("modulePaths", modulePaths.entrySet());

        out.write(VelocityUtil.processTemplate(REQUIREJS_CONFIG_TEMPLATE, contextMap));
	}

}
