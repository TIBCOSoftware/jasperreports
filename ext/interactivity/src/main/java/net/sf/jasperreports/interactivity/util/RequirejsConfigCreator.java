/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
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
package net.sf.jasperreports.interactivity.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.velocity.util.VelocityUtil;
import net.sf.jasperreports.web.util.ResourcePathUtil;
import net.sf.jasperreports.web.util.WebRequestContext;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RequirejsConfigCreator
{
	private static final String REQUIREJS_CONFIG_TEMPLATE = "net/sf/jasperreports/web/servlets/resources/templates/RequirejsConfigTemplate.vm";
	
	public static RequirejsConfigCreator getInstance(WebRequestContext context)
	{
		//FIXME consider caching
		return new RequirejsConfigCreator(context);
	}
	
	private WebRequestContext context;
	private ObjectMapper objectMapper;

	protected RequirejsConfigCreator(WebRequestContext context)
	{
		this.context = context;
		this.objectMapper = new ObjectMapper();
	}
	
	public String getRequirejsConfig()
	{
		JasperReportsContext jrContext = context.getJasperReportsContext();
		String contextPath = context.getRequestContextPath();
		
		ObjectNode configRoot = objectMapper.createObjectNode();
		configRoot.put("baseUrl", contextPath);
		
		ObjectNode configPaths = objectMapper.createObjectNode();
		configRoot.set("paths", configPaths);

		setModuleMappings(jrContext, contextPath, configPaths);
		runContributors(jrContext, contextPath, configRoot);
		
		String requirejsConfig = toConfigScript(configRoot);
		return requirejsConfig;
	}

	protected void setModuleMappings(JasperReportsContext jrContext, String contextPath, ObjectNode configPaths)
	{
		ResourcePathUtil resourcePathUtil = ResourcePathUtil.getInstance(jrContext);
		List<RequirejsModuleMapping> requirejsMappings = jrContext.getExtensions(RequirejsModuleMapping.class);
		for (RequirejsModuleMapping requirejsMapping : requirejsMappings)
		{
			String modulePath = requirejsMapping.getPath();
			if (requirejsMapping.isClasspathResource())
			{
				modulePath = contextPath + resourcePathUtil.getResourcesBasePath() + modulePath;
			}
			configPaths.put(requirejsMapping.getName(), modulePath);
		}
	}

	protected void runContributors(JasperReportsContext jrContext, String contextPath, ObjectNode configRoot)
	{
		List<RequirejsConfigContributor> contributors = jrContext.getExtensions(RequirejsConfigContributor.class);
		for (RequirejsConfigContributor contributor : contributors)
		{
			contributor.contribute(context, configRoot);
		}
	}

	protected String toConfigScript(ObjectNode configRoot)
	{
		String configString = toConfigString(configRoot);
		
		Map<String, Object> contextMap = new HashMap<>();
		contextMap.put("config", configString);
		
		// going through a template so that the final output is somewhat customizable
		String requirejsConfig = VelocityUtil.processTemplate(REQUIREJS_CONFIG_TEMPLATE, contextMap);
		return requirejsConfig;
	}

	protected String toConfigString(ObjectNode configRoot)
	{
		CharArrayWriter outWriter = new CharArrayWriter(1024);
		try
		{
			objectMapper.writeValue(outWriter, configRoot);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		
		String configString = outWriter.toString();
		return configString;
	}
	
}
