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
package com.jaspersoft.jasperreports.customvisualization.export;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.Processor;

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.GenericElementJsonHandler;
import net.sf.jasperreports.engine.export.JsonExporterContext;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.web.util.VelocityUtil;

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
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("elementId", "element" + element.hashCode());

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

		if (
			context != null 
			&& context.getExporterRef() != null
			&& context.getExporterRef().getReportContext() != null
			)
		{
			configuration.put("isInteractiveViewer", true);
		}
		else
		{
			configuration.put("isInteractiveViewer", false);
		}

		ObjectMapper mapper = new ObjectMapper();
		try
		{

			if (!configuration.containsKey("instanceData"))
			{
				Map<String, Object> jsonConfiguration = createConfigurationForJSON(configuration);
				String instanceData = mapper.writeValueAsString(jsonConfiguration);

				configuration.put("instanceData", instanceData);
			}
		}
		catch (Exception ex)
		{
			log.warn("(JSON): Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
		}

		configuration.put("module", element.getParameterValue(CVPrintElement.MODULE));

		contextMap.put("configuration", configuration);

		// Pre export the object programmatically...

		// RepositoryUtil ru =
		// RepositoryUtil.getInstance(context.getJasperReportsContext());
		String s = "{}";
		try
		{
			// Loading resource...
			InputStream is = CVElementJsonHandler.class.getClassLoader().getResourceAsStream(CV_ELEMENT_JSON_TEMPLATE);
			// if (is == null)
			// {
			// System.out.println("Unable to laod the template for json..." +
			// CVElementJsonHandler.class.getClassLoader().getResource(CV_ELEMENT_JSON_TEMPLATE));
			// }

			InputStreamReader templateReader = new InputStreamReader(is, "UTF-8");
			StringWriter output = new StringWriter(128);
			VelocityUtil.getVelocityEngine().evaluate(
				new VelocityContext(contextMap), 
				output, 
				CV_ELEMENT_JSON_TEMPLATE,
				templateReader
				);
			output.flush();
			output.close();

			s = output.toString();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new JRRuntimeException(e);
		}
		return s;
	}

	@Override
	public boolean toExport(JRGenericPrintElement element)
	{
		return true;
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
	public static Map<String, Object> createConfigurationForJSON(Map<String, Object> configuration)
	{

		// Create a copy of configuration with all the serializable objects in
		// it...
		Map<String, Object> jsonConfiguration = new HashMap<String, Object>();

		JRTemplateGenericPrintElement element = (JRTemplateGenericPrintElement) configuration.get("element");

		if (configuration.containsKey("series"))
		{
			jsonConfiguration.put("series", configuration.get("series"));
		}

		if (element != null)
		{
			jsonConfiguration.put(Processor.CONF_WIDTH, element.getWidth());
			jsonConfiguration.put(Processor.CONF_HEIGHT, element.getHeight());

			// configuration.put(Processor.CONF_WIDTH, element.getWidth());
			// configuration.put(Processor.CONF_HEIGHT, element.getHeight());

			for (String prop : element.getPropertiesMap().getPropertyNames())
			{
				jsonConfiguration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
				configuration.put("property." + prop, element.getPropertiesMap().getProperty(prop));
			}

			jsonConfiguration.put("id", "element" + element.hashCode());

			// configuration.put(CVPrintElement.MODULE,
			// element.getParameterValue(CVPrintElement.MODULE));
			if (element.getParameterValue(CVPrintElement.SCRIPT_URI) != null)
			{
				configuration.put(CVPrintElement.SCRIPT_URI, element.getParameterValue(CVPrintElement.SCRIPT_URI));
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

}
