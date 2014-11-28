/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.web.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRRuntimeException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class RequirejsTemplateConfigContributor implements RequirejsConfigContributor
{

	private static final Log log = LogFactory.getLog(RequirejsTemplateConfigContributor.class);
	
	private String templateName;
	private Map<String, String> paths;
	private Map<String, String> resourcePaths;
	
	public RequirejsTemplateConfigContributor()
	{
		paths = new HashMap<String, String>();
		resourcePaths = new HashMap<String, String>();
	}
	
	@Override
	public void contribute(WebRequestContext context, ObjectNode config)
	{
		Map<String, Object> templateMap = new HashMap<String, Object>();
		RequirejsTemplateConfigContext templateContext = new RequirejsTemplateConfigContext(context, paths, resourcePaths);
		templateMap.put("context", templateContext);
		String configString = VelocityUtil.processTemplate(templateName, templateMap);
		
		if (log.isTraceEnabled())
		{
			log.trace("template " + templateName + " config:\n" + configString);
		}
		
		ObjectNode templateConfig = parseTemplateConfig(configString);
		mergeObject(config, templateConfig);
	}

	protected ObjectNode parseTemplateConfig(String configString)
	{
		ObjectMapper objectMapper = new ObjectMapper();//FIXME reuse RequirejsConfigCreator.objectMapper
		try
		{
			ObjectNode templateConfig = objectMapper.readValue(new StringReader(configString), ObjectNode.class);
			return templateConfig;
		}
		catch (JsonParseException e)
		{
			throw new JRRuntimeException(e);
		} 
		catch (JsonMappingException e)
		{
			throw new JRRuntimeException(e);
		} 
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	//FIXME move to a util class
	protected void mergeObject(ObjectNode dest, ObjectNode source)
	{
		for(Iterator<Entry<String, JsonNode>> it = source.fields(); it.hasNext();)
		{
			Entry<String, JsonNode> fieldEntry = it.next();
			String field = fieldEntry.getKey();
			JsonNode sourceValue = fieldEntry.getValue();
			JsonNode destValue = dest.get(field);
			// only recursively merging if both dest and source fields are objects
			// otherwise we overwrite the field, not appending arrays for now
			if (sourceValue instanceof ObjectNode && destValue instanceof ObjectNode)
			{
				mergeObject((ObjectNode) destValue, (ObjectNode) sourceValue);
			}
			else
			{
				dest.put(field, sourceValue);
			}
		}
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public void addPath(String key, String path)
	{
		paths.put(key, path);
	}

	public void addResourcePath(String key, String resource)
	{
		resourcePaths.put(key, resource);
	}
}
