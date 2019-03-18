/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.customvisualization.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.web.util.VelocityUtil;

/**
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public abstract class CVElementAbstractImageDataProvider implements CVElementImageDataProvider
{
	private static final Log log = LogFactory.getLog(CVElementAbstractImageDataProvider.class);
	private static final String PHANTOMJS_COMPONENT_TEMPLATE = "net/sf/jasperreports/customvisualization/templates/phantomjs_component.vm";

	public String getHtmlPage(
			JasperReportsContext jrContext,
			JRGenericPrintElement element,
			List<String> scripts,
			String cssUri
	)
	{
		Map<String, Object> originalConfiguration = (Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);

		if (originalConfiguration == null)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Configuration object in the element " + element + " is NULL!");
			}
			throw new JRRuntimeException("Configuration object in the element " + element + " is NULL!");
		}

		// Duplicate the configuration.
		Map<String, Object> configuration = new HashMap<String, Object>();
		configuration.putAll(originalConfiguration);

		ObjectMapper mapper = new ObjectMapper();
		try
		{
			Map<String, Object> jsonConfiguration = CVElementJsonHandler.createConfigurationForJSON(configuration, null);
			jsonConfiguration.put("animation", false);

			String instanceData = mapper.writeValueAsString(jsonConfiguration);
			configuration.put("instanceData", instanceData);
		}
		catch (Exception ex)
		{
			if (log.isWarnEnabled())
			{
				log.warn("Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
			}
			throw new JRRuntimeException("Error dumping the JSON for the configuration...: " + ex.getMessage());
		}

		configuration.put("element", element);

		Map<String, Object> velocityContext = new HashMap<>();
		velocityContext.put("elementId", CVUtils.getElementId(element));
		velocityContext.put("scripts", scripts);
		velocityContext.put("configuration", configuration);
		velocityContext.put("module", element.getParameterValue(CVPrintElement.MODULE));
		velocityContext.put("cssUri", cssUri);

		return VelocityUtil.processTemplate(PHANTOMJS_COMPONENT_TEMPLATE, velocityContext);
	}
}
