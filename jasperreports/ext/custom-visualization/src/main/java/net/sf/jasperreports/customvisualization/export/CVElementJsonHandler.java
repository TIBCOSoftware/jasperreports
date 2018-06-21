/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2018 TIBCO Software Inc. All rights reserved.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.Processor;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.JsonExporter;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.web.util.VelocityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giulio Toffoli (gtoffoli@tibco.com)
 */
public class CVElementJsonHandler extends CVElementAbstractGenericHandler implements GenericElementJsonHandler
{
	private static final CVElementJsonHandler INSTANCE = new CVElementJsonHandler();
	private static final Log log = LogFactory.getLog(CVElementJsonHandler.class);

	private static final String CV_ELEMENT_JSON_TEMPLATE = "net/sf/jasperreports/customvisualization/resources/require/CVElementJsonTemplate.vm";

	public static CVElementJsonHandler getInstance()
	{
		return INSTANCE;
	}

	/**
	 * 
	 * Creates the main component configuration. This also includes the
	 * information about which renderer to use.
	 * 
	 * @param context
	 * @param element
	 * @return
	 */
	@Override
	public String getJsonFragment(JsonExporterContext context, JRGenericPrintElement element)
	{
		Map<String, Object> originalConfiguration =
			(Map<String, Object>) element.getParameterValue(CVPrintElement.CONFIGURATION);

		if (originalConfiguration == null)
		{
			log.warn("Configuration object in the element " + element + " is NULL!");
			throw new JRRuntimeException("Configuration object in the element " + element + " is NULL!");
		}

		// Duplicate the configuration.
		Map<String, Object> configuration = new HashMap<String, Object>();
		configuration.putAll(originalConfiguration);

		ObjectMapper mapper = new ObjectMapper();
		try
		{
			if (!configuration.containsKey("instanceData"))
			{
				JsonExporter exporter = ((JsonExporter)context.getExporterRef());
				HtmlResourceHandler htmlResourceHandler = exporter.getExporterOutput() != null ?
						exporter.getExporterOutput().getFontHandler() : null;

				Map<String, Object> jsonConfiguration = createConfigurationForJSON(configuration, htmlResourceHandler);
				String instanceData = mapper.writeValueAsString(jsonConfiguration);

				configuration.put("instanceData", instanceData);
			}
		}
		catch (Exception ex)
		{
			log.warn("(JSON): Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
		}

		configuration.put("module", element.getParameterValue(CVPrintElement.MODULE));

		Map<String, Object> velocityContext = new HashMap<String, Object>();
		velocityContext.put("elementId", "element" + element.hashCode());
		velocityContext.put("configuration", configuration);

		return VelocityUtil.processTemplate(CV_ELEMENT_JSON_TEMPLATE, velocityContext);
	}

	/**
	 * Clean the configuration object.
	 * 
	 * This function removes objects in the configuration map that are not
	 * convertible in JSON (script object and design element). It also adds some
	 * useful properties for the element such as width, height and all the
	 * elements properties in form of property.xyz
	 * 
	 * 
	 * @param configuration
	 * @return
	 */
	public static Map<String, Object> createConfigurationForJSON(Map<String, Object> configuration,
			 								HtmlResourceHandler htmlResourceHandler)
	{
		Map<String, Object> jsonConfiguration = new HashMap<>();

		JRTemplateGenericPrintElement element = (JRTemplateGenericPrintElement) configuration.get("element");

		if (configuration.containsKey("series"))
		{
			jsonConfiguration.put("series", configuration.get("series"));
		}

		if (element != null)
		{
			jsonConfiguration.put(Processor.CONF_WIDTH, element.getWidth());
			jsonConfiguration.put(Processor.CONF_HEIGHT, element.getHeight());

			for (String prop: element.getPropertiesMap().getPropertyNames())
			{
				jsonConfiguration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
				configuration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
			}

			jsonConfiguration.put("id", "element" + element.hashCode());

			if (element.getParameterValue(CVPrintElement.SCRIPT_URI) != null)
			{
				String scriptLocation = getResourceURL((String)element.getParameterValue(CVPrintElement.SCRIPT_URI),
						htmlResourceHandler);
				configuration.put(CVPrintElement.SCRIPT_URI, scriptLocation);
			}

			if (element.getParameterValue(CVPrintElement.CSS_URI) != null)
			{
				String cssLocation = getResourceURL((String)element.getParameterValue(CVPrintElement.CSS_URI),
						htmlResourceHandler);
				configuration.put(CVPrintElement.CSS_URI, cssLocation);
			}
		}

		// Add all the items properties...
		for (String itemPropertyKey : configuration.keySet())
		{
			Object value = configuration.get(itemPropertyKey);

			if (
				itemPropertyKey == null
				|| itemPropertyKey.isEmpty()
				|| itemPropertyKey.equals("element")
				|| itemPropertyKey.equals("series")
				)
			{
				continue;
			}

			if (value != null)
			{
				jsonConfiguration.put(itemPropertyKey, value.toString());
			}
		}

		return jsonConfiguration;
	}

	protected static String getResourceURL(String scriptResourceLocation, HtmlResourceHandler htmlResourceHandler)
	{
		if (htmlResourceHandler != null && !isUrl(scriptResourceLocation))
		{
			return htmlResourceHandler.getResourcePath(scriptResourceLocation);
		}

		return scriptResourceLocation;
	}

}
