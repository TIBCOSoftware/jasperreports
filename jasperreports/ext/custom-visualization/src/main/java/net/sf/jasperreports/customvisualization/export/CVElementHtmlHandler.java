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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.customvisualization.CVConstants;
import net.sf.jasperreports.customvisualization.CVPrintElement;
import net.sf.jasperreports.customvisualization.CVUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.FileHtmlResourceHandler;
import net.sf.jasperreports.engine.export.GenericElementHtmlHandler;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.tabulator.TableCell;
import net.sf.jasperreports.repo.RepositoryContext;
import net.sf.jasperreports.repo.RepositoryUtil;
import net.sf.jasperreports.web.util.VelocityUtil;

/**
 * 
 */
public class CVElementHtmlHandler extends CVElementAbstractGenericHandler implements GenericElementHtmlHandler
{
	private static final CVElementHtmlHandler INSTANCE = new CVElementHtmlHandler();
	private static final Log log = LogFactory.getLog(CVElementHtmlHandler.class);

	private static final String COMPONENT_TEMPLATE = "net/sf/jasperreports/customvisualization/templates/component.vm";

	private final String[] scriptResourceLocations = new String[] {
			"net/sf/jasperreports/customvisualization/resources/require/require.js",
			"net/sf/jasperreports/customvisualization/resources/require/cv-component_static.js"
	};


	public static CVElementHtmlHandler getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void exportElement(JRHtmlExporterContext exporterContext, JRGenericPrintElement element, TableCell cell) 
	{
		try 
		{
			RepositoryContext repositoryContext = exporterContext.getRepository().getRepositoryContext();
			JRPrintImage chartImage = CVElementImageProvider.getInstance().getImage(repositoryContext, element);
			((HtmlExporter)exporterContext.getExporterRef()).writeImage(chartImage, cell);
		}
		catch (Exception e) 
		{
			throw new JRRuntimeException(e);
		}
	}
	
	@Override
	public String getHtmlFragment(
		JRHtmlExporterContext context, 
		JRGenericPrintElement element
		)
	{
		JRPropertiesUtil properties = JRPropertiesUtil.getInstance(context.getJasperReportsContext());
		boolean generateImage = properties.getBooleanProperty(CVConstants.PROPERTY_HTML_GENERATE_IMAGE);
		if (!generateImage)
		{
			return getHtmlFragment(context.getJasperReportsContext(), context, element);
		}
		return null; // let exportElement() method kick-in
	}

	public String getHtmlFragment(
		JasperReportsContext jrContext, 
		JRHtmlExporterContext context,
		JRGenericPrintElement element)
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
		Map<String, Object> configuration = new HashMap<>();
		Map<String, Object> velocityContext = new HashMap<>();

		configuration.putAll(originalConfiguration);
		configuration.put("element", element);

		if (context != null && context.getExporterRef() != null && context.getExporterRef().getReportContext() != null)
		{
			configuration.put("isInteractiveViewer", true);
		}
		else
		{
			configuration.put("isInteractiveViewer", false);

			try
			{
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> jsonConfiguration = CVElementJsonHandler.createConfigurationForJSON(configuration, null);
				String instanceData = mapper.writeValueAsString(jsonConfiguration);
				configuration.put("instanceData", instanceData);
			}
			catch (Exception ex)
			{
				if (log.isWarnEnabled())
				{
					log.warn("Error dumping the JSON for the configuration...: " + ex.getMessage(), ex);
				}
				throw new JRRuntimeException("Error dumping the JSON for the CVC configuration!", ex);
			}

			List<String> scripts = new ArrayList<>();
			HtmlResourceHandler htmlResourceHandler = null;
			HtmlExporter exporter = ((HtmlExporter)context.getExporterRef());
			if (exporter.getExporterOutput() != null && exporter.getExporterOutput().getResourceHandler() != null) {
				htmlResourceHandler = exporter.getExporterOutput().getResourceHandler();
			}

			RepositoryUtil repositoryUtil = RepositoryUtil.getInstance(jrContext);

			for (String scriptResourceLocation: scriptResourceLocations) {
				scripts.add(getResourceURL(scriptResourceLocation, htmlResourceHandler, repositoryUtil));
			}
			scripts.add(getResourceURL((String)element.getParameterValue(CVPrintElement.SCRIPT_URI), htmlResourceHandler, repositoryUtil));

			velocityContext.put("scripts", scripts);
			velocityContext.put("cssUri", getResourceURL((String)element.getParameterValue(CVPrintElement.CSS_URI), htmlResourceHandler, repositoryUtil));
			velocityContext.put("module", element.getParameterValue(CVPrintElement.MODULE));
		}

		velocityContext.put("elementId", CVUtils.getElementId(element));
		velocityContext.put("configuration", configuration);

		return VelocityUtil.processTemplate(COMPONENT_TEMPLATE, velocityContext);
	}

	protected String getResourceURL(String scriptResourceLocation, HtmlResourceHandler htmlResourceHandler, RepositoryUtil repositoryUtil)
	{
		if (scriptResourceLocation != null && htmlResourceHandler != null && !isUrl(scriptResourceLocation))
		{
			if (htmlResourceHandler instanceof FileHtmlResourceHandler)
			{
				String resourceName = CVUtils.getResourceName(scriptResourceLocation);

				try
				{
					byte[] resourceData = repositoryUtil.getBytesFromLocation(scriptResourceLocation);
					htmlResourceHandler.handleResource(resourceName, resourceData);
				}
				catch (JRException e)
				{
					throw new JRRuntimeException(e);
				}

				return htmlResourceHandler.getResourcePath(resourceName);
			}
			else
			{
				return htmlResourceHandler.getResourcePath(scriptResourceLocation);
			}
		}

		return scriptResourceLocation;
	}

}
