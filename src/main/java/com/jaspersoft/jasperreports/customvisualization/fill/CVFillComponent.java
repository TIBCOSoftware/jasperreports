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
package com.jaspersoft.jasperreports.customvisualization.fill;

import static net.sf.jasperreports.web.util.AbstractWebResourceHandler.PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.jasperreports.customvisualization.CVConstants;
import com.jaspersoft.jasperreports.customvisualization.CVPrintElement;
import com.jaspersoft.jasperreports.customvisualization.Processor;

import net.sf.jasperreports.components.items.ItemData;
import net.sf.jasperreports.components.items.ItemProperty;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.BaseFillComponent;
import net.sf.jasperreports.engine.component.FillContext;
import net.sf.jasperreports.engine.component.FillContextProvider;
import net.sf.jasperreports.engine.component.FillPrepareResult;
import net.sf.jasperreports.engine.fill.JRFillObjectFactory;
import net.sf.jasperreports.engine.fill.JRTemplateGenericElement;
import net.sf.jasperreports.engine.fill.JRTemplateGenericPrintElement;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.repo.RepositoryUtil;

public class CVFillComponent extends BaseFillComponent implements Serializable, FillContextProvider
{
	private static final long serialVersionUID = CVConstants.SERIAL_VERSION_UID;
	private static final Log log = LogFactory.getLog(CVFillComponent.class);

	private final CVComponent component;

	// Fill component elements
	private List<CVFillItemProperty> itemProperties;
	private List<CVFillItemData> itemDataList = new ArrayList<CVFillItemData>();
	private List<List<Map<String, Object>>> datasetsData = new ArrayList<List<Map<String, Object>>>();
	private Processor processor = null;
	private JasperReportsContext context = null;

	// private List<SVGMapFillLayer> fillLayers;
	// private SVGMapFillDataset dataset;
	// private List<SVGMapFillColorRange> colorRanges;

	public CVFillComponent(CVComponent component, JRFillObjectFactory factory)
	{

		this.component = component;
		this.itemProperties = new ArrayList<CVFillItemProperty>();

		if (factory != null)
		{
			// factory.registerElementDataset(this.dataset);
			for (ItemProperty itemProperty : component.getItemProperties())
			{
				this.itemProperties.add(new CVFillItemProperty(itemProperty, factory));
			}

			for (ItemData data : component.getItemData())
			{
				if (data != null)
				{
					itemDataList.add(new CVFillItemData(this, data, factory));
					datasetsData.add(null);
				}
			}

			this.context = factory.getFiller().getJasperReportsContext();
		}

		String processingClass = component.getProcessingClass();
		if (processingClass != null && processingClass.length() > 0)
		{
			try
			{
				Class<?> myClass = JRClassLoader.loadClassForName(processingClass);
				processor = (Processor) myClass.newInstance();
			}
			catch (Exception e)
			{
				throw new JRRuntimeException("Could not create processor instance.", e);
			}
		}
	}

	protected boolean isEvaluateNow()
	{
		return getComponent().getEvaluationTime() == EvaluationTimeEnum.NOW;
	}

	protected CVComponent getComponent()
	{
		return component;
	}

	@Override
	public void evaluate(byte evaluation) throws JRException
	{
		if (isEvaluateNow())
		{
			evaluateComponent(evaluation);
		}

	}

	public void evaluateComponent(byte evaluation) throws JRException
	{
		if (fillContext != null)
		{

			for (CVFillItemProperty itemProperty : this.itemProperties)
			{
				itemProperty.evaluate(fillContext, evaluation);
			}

			for (int i = 0; i < this.itemDataList.size(); ++i)
			{

				CVFillItemData itemData = this.itemDataList.get(i);

				if (itemData != null)
				{

					List<Map<String, Object>> newSet = itemData.getEvaluateItems(evaluation);

					List<Map<String, Object>> reallyNewSet = new ArrayList<Map<String, Object>>();

					for (Map<String, Object> m : newSet)
					{
						// System.out.println(m);
						reallyNewSet.add(m);
					}

					datasetsData.set(i, reallyNewSet);

				}
				else
				{
					datasetsData.set(i, null);
				}
			}
		}
	}

	@Override
	public FillPrepareResult prepare(int availableHeight)
	{
		return FillPrepareResult.PRINT_NO_STRETCH;
	}

	@Override
	public JRPrintElement fill()
	{
		JRComponentElement element = fillContext.getComponentElement();
		JRTemplateGenericElement template = 
			new JRTemplateGenericElement(
				fillContext.getElementOrigin(),
				fillContext.getDefaultStyleProvider(),
				CVPrintElement.CV_ELEMENT_TYPE
				);
		template = deduplicate(template);

		JRTemplateGenericPrintElement printElement = 
			new JRTemplateGenericPrintElement(
				template,
				printElementOriginator
				);
		printElement.setUUID(element.getUUID());
		printElement.setX(element.getX());
		printElement.setY(fillContext.getElementPrintY());
		printElement.setWidth(element.getWidth());
		printElement.setHeight(element.getHeight());
                
                if (element.hasProperties() )
                {
                    if (element.getPropertiesMap().getProperty("cv.keepTemporaryFiles") != null
			&& element.getPropertiesMap().getProperty("cv.keepTemporaryFiles").equals("true"))
                    {
                            printElement.getPropertiesMap().setProperty("cv.keepTemporaryFiles", "true");
                    }
                    
                    // We also want to transfer to the component all the properties starting with CV_PREFIX
                    for (String ownPropName : element.getPropertiesMap().getOwnPropertyNames())
                    {
                        if (ownPropName.startsWith(CVConstants.CV_PREFIX ))
                        {
                            printElement.getPropertiesMap().setProperty(ownPropName, element.getPropertiesMap().getProperty(ownPropName));
                        }
                    }
                }

		if (isEvaluateNow())
		{
			evaluationPerformed(printElement);
		}
		else
		{
			fillContext.registerDelayedEvaluation(
				printElement, 
				getComponent().getEvaluationTime(),
				getComponent().getEvaluationGroup()
				);
		}

		return printElement;
	}

	@Override
	public void evaluateDelayedElement(JRPrintElement element, byte evaluation) throws JRException
	{
		evaluateComponent(evaluation);
		evaluationPerformed((JRTemplateGenericPrintElement) element);
	}

	/**
	 * The right place to perform fill objects processing after evaluation of
	 * all the expressions.
	 *
	 */
	protected void evaluationPerformed(JRTemplateGenericPrintElement element)
	{

		// Build the processor...
		Map<String, Object> configuration = new HashMap<String, Object>();

		// configuration.put(Processor.CONF_FILL_CONTEXT, this.fillContext);
		configuration.put(Processor.CONF_PRINT_ELEMENT, element);
		element.setParameterValue(CVPrintElement.PARAMETER_ON_ERROR_TYPE, this.component.getOnErrorType().getName());

		List<List<Map<String, Object>>> savedDatasetsData = new ArrayList<List<Map<String, Object>>>();

		for (int i = 0; i < datasetsData.size(); ++i)
		{
			savedDatasetsData.add(datasetsData.get(i));

			datasetsData.set(i, null);
		}

		configuration.put(Processor.CONF_SERIES, savedDatasetsData);

		Object scriptSource = null;
		Object cssSource = null;

		for (CVFillItemProperty p : itemProperties)
		{
			if (CVPrintElement.SCRIPT.equals(p.getName()))
			{
				scriptSource = p.getValue();
			}
			else if (CVPrintElement.CSS.equals(p.getName()))
			{
				cssSource = p.getValue();
			}
			else
			{
				configuration.put(p.getName(), p.getValue());
			}
		}

		if (processor != null)
		{
			try
			{
				configuration = processor.processConfiguration(configuration);
			}
			catch (Throwable t)
			{
				throw new JRRuntimeException("Custom Visualization component processing failed.", t);
			}
		}

		element.setParameterValue(CVPrintElement.CONFIGURATION, configuration);

		// Check the jasperreports property
		// (CV_SCRIPT_FROM_CLASSPATH_ONLY_PROPERTY) to see if we are allowed
		// to load the resource from any location or just from the classpath
		String cpOnly = 
			JRPropertiesUtil.getInstance(this.context)
				.getProperty(CVConstants.CV_SCRIPT_FROM_CLASSPATH_ONLY_PROPERTY);
		boolean classpathOnly = cpOnly != null && "true".equals(cpOnly);

		String script = loadResource(scriptSource, classpathOnly);

		element.setParameterValue(CVPrintElement.SCRIPT, script);

		// We store also the script URI (o resource name) to be passed in the
		// configuration
		// for Visualize.js
		element.setParameterValue(CVPrintElement.SCRIPT_URI, scriptSource);

		if (cssSource != null && !cssSource.equals(""))
		{
			String css = loadResource(cssSource, false);
			element.setParameterValue(CVPrintElement.CSS, css);
		}

		String moduleName = 
			(configuration.containsKey(CVPrintElement.MODULE))
			? (String) configuration.get(CVPrintElement.MODULE) : null;

		// We need a module name. If the module name has not been provided by
		// the user
		// by using the module property, the module name is guessed from the
		// file name.
		if (moduleName == null || moduleName.isEmpty())
		{
			String location = null;
			// Use the base name as module name...
			if (scriptSource instanceof File)
			{
				location = ((File) scriptSource).toURI().toString();
			}
			else if (scriptSource instanceof String)
			{
				location = (String) scriptSource;
			}

			if (location != null)
			{
				File f = new File(location);

				String name = f.getName();
				if (name.toLowerCase().endsWith(".min.js"))
				{
					name = name.substring(0, name.length() - 7);
				}
				else if (name.toLowerCase().endsWith(".js"))
				{
					name = name.substring(0, name.length() - 3);
				}

				// Get just the file name...
				element.setParameterValue(CVPrintElement.MODULE, name);
			}
		}
		else
		{
			element.setParameterValue(CVPrintElement.MODULE, moduleName);
		}

		// If a module name is still missing, we rise an expection...
		if (element.getParameterValue(CVPrintElement.MODULE) == null)
		{
			throw new JRRuntimeException("No 'module' property defined for a Custom Visualization Component.");
		}

	}

	@Override
	public FillContext getFillContext()
	{
		return fillContext;
	}

	/**
	 * Load a resource from a repository location, input stream, URL or File. If
	 * fromClasspathOnly is set to true, the source is used as a string pointing
	 * to a resource inside the classpath. Classpath access is restricted by
	 * PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX properties inside JasperReports.
	 * 
	 * @param source
	 * @param fromClasspathOnly
	 * @return
	 */
	protected String loadResource(Object source, boolean fromClasspathOnly)
	{
		try
		{
			byte[] scriptBytes = null;

			if (!fromClasspathOnly)
			{
				if (source instanceof InputStream)
				{
					scriptBytes = JRLoader.loadBytes((InputStream) source);
				}
				else if (source instanceof URL)
				{
					scriptBytes = JRLoader.loadBytes((URL) source);
				}
				else if (source instanceof File)
				{
					scriptBytes = JRLoader.loadBytes((File) source);
				}
				else if (source instanceof String)
				{
					String location = (String) source;
					scriptBytes = RepositoryUtil.getInstance(this.context).getBytesFromLocation(location);
				}
			}

			if (scriptBytes == null && (source != null && source instanceof String && ((String) source).length() > 0))
			{
				String location = (String) source;

				if (checkResourceName(this.context, location))
				{
					scriptBytes = 
						JRLoader.loadBytes(Thread.currentThread().getContextClassLoader().getResourceAsStream(location));
				}
			}

			if (scriptBytes == null)
			{
				String message = "No script provided for the Custom Visualization component. ";
				if (fromClasspathOnly)
				{
					message += "(Loading of this resource is restricted to the classpath)";
				}

				throw new JRRuntimeException(message);
			}
			return new String(scriptBytes, "UTF-8");

		}
		catch (Exception ex)
		{
			// Depending on the error type, use a fake image or rise an
			// exception...
			throw new JRRuntimeException("No script provided for the Custom Visualization component.", ex);
		}
	}

	/**
	 * Function taken by the DefaultWebResourceHandler of JasperReports.
	 * 
	 * It checks if a specific resource name inside the classpath can be loaded
	 * or no.
	 * 
	 * @param jasperReportsContext
	 * @param resourceName
	 * @return
	 */
	protected boolean checkResourceName(JasperReportsContext jasperReportsContext, String resourceName)
	{
		boolean matched = false;

		List<JRPropertiesUtil.PropertySuffix> patternProps = 
			JRPropertiesUtil.getInstance(jasperReportsContext)
				.getProperties(PROPERTIES_WEB_RESOURCE_PATTERN_PREFIX);// FIXMESORT cache this
		for (Iterator<JRPropertiesUtil.PropertySuffix> patternIt = patternProps.iterator(); patternIt.hasNext();)
		{
			JRPropertiesUtil.PropertySuffix patternProp = patternIt.next();
			String patternStr = patternProp.getValue();
			if (patternStr != null && patternStr.length() > 0)
			{
				Pattern resourcePattern = Pattern.compile(patternStr);
				if (resourcePattern.matcher(resourceName).matches())
				{
					if (log.isDebugEnabled())
					{
						log.debug("resource " + resourceName + " matched pattern " + resourcePattern);
					}

					matched = true;
					break;
				}
			}
		}

		if (!matched)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Resource " + resourceName + " does not matched any allowed pattern");
			}
		}

		return matched;
	}

}
