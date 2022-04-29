/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2022 TIBCO Software Inc. All rights reserved.
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
package net.sf.jasperreports.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRValueStringUtils;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class JacksonUtil
{
	/**
	 * 
	 */
	private static final String OBJECT_MAPPER_CONTEXT_KEY = "net.sf.jasperreports.jackson.object.mapper";
	private static final String XML_MAPPER_CONTEXT_KEY = "net.sf.jasperreports.jackson.xml.mapper";
	
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	protected JacksonUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
	}
	
	
	/**
	 *
	 */
	public static JacksonUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new JacksonUtil(jasperReportsContext);
	}
	
	
	public ObjectMapper getObjectMapper()
	{
		ObjectMapper mapper = (ObjectMapper)jasperReportsContext.getOwnValue(OBJECT_MAPPER_CONTEXT_KEY);
		if (mapper == null)
		{
			mapper = new ObjectMapper();

			List<JacksonMapping> jacksonMappings = jasperReportsContext.getExtensions(JacksonMapping.class);
			for (JacksonMapping jacksonMapping : jacksonMappings)
			{
				register(mapper, jacksonMapping);
			}

			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			
			jasperReportsContext.setValue(OBJECT_MAPPER_CONTEXT_KEY, mapper);
		}
		return mapper;
	}
	
	
	public XmlMapper getXmlMapper()
	{
		XmlMapper mapper = (XmlMapper)jasperReportsContext.getOwnValue(XML_MAPPER_CONTEXT_KEY);
		if (mapper == null)
		{
			mapper = new XmlMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.setSerializationInclusion(Include.NON_NULL);
			
			List<JacksonMapping> jacksonMappings = jasperReportsContext.getExtensions(JacksonMapping.class);
			for (JacksonMapping jacksonMapping : jacksonMappings)
			{
				register(mapper, jacksonMapping);
			}

			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			
			jasperReportsContext.setValue(XML_MAPPER_CONTEXT_KEY, mapper);
		}
		return mapper;
	}
	
	
	/**
	 *
	 */
	private static void register(ObjectMapper mapper, JacksonMapping mapping)
	{
		try
		{
			Class<?> clazz = Class.forName(mapping.getClassName());
			// in theory, we could register subtypes without specifying a name here, just the class,
			// using properties without suffix in the extension config file.
			// in such case, the name would be provided by the @JsonTypeName annotation in the registered class.
			// but there is really no reason to do that, especially since it would rely on class hierarchy introspection
			// and would thus need additional processing, without any apparent advantage
			mapper.registerSubtypes(new NamedType(clazz, mapping.getName()));
		}
		catch (ClassNotFoundException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	/**
	 *
	 */
	public <T> T loadObject(String jsonData, Class<T> clazz)
	{
		T result = null;
		if (jsonData != null) 
		{
			ObjectMapper mapper = getObjectMapper();
			
			try 
			{
				result = mapper.readValue(jsonData, clazz);
			}
			catch (JsonProcessingException e) 
			{
				throw new JRRuntimeException(e);
			}
		}
		return result;
	}


	/**
	 *
	 */
	public <T> T loadXml(String xmlData, Class<T> clazz)
	{
		T result = null;
		if (xmlData != null) 
		{
			XmlMapper mapper = getXmlMapper();
			
			try 
			{
				result = mapper.readValue(xmlData, clazz);
			}
			catch (JsonProcessingException e) 
			{
				throw new JRRuntimeException(e);
			}
		}
		return result;
	}


	/**
	 *
	 */
	public <T> T loadXml(InputStream is, Class<T> clazz)
	{
		T result = null;
		if (is != null) 
		{
			XmlMapper mapper = getXmlMapper();
			
			try 
			{
				result = mapper.readValue(is, clazz);
			}
			catch (JacksonException e) 
			{
				throw new JacksonRuntimException(e);
			}
			catch (IOException e) 
			{
				throw new JRRuntimeException(e);
			}
		}
		return result;
	}


	/**
	 * 
	 */
	public <T> List<T> loadList(String jsonData, Class<T> clazz)
	{
		List<T> result = null;
		if (jsonData != null) 
		{
			ObjectMapper mapper = getObjectMapper();
			
			try 
			{
				result = mapper.readValue(jsonData, mapper.getTypeFactory().constructParametricType(List.class, clazz));
			}
			catch (IOException e) 
			{
				throw new JRRuntimeException(e);
			}
		}
		return result;
	}


	/*
	 * 
	 */
	public <T> List<T> loadAsList(String jsonData, Class<T> clazz) {
		List<T> result = null;
		
		if (jsonData != null) {
			String trimmedData = jsonData.trim();
			if (trimmedData.startsWith("{")) {
				result = new ArrayList<>();
				result.add(loadObject(trimmedData, clazz));
			} else if (trimmedData.startsWith("[")) {
				result = loadList(trimmedData, clazz);
			}
		}
		
		return result;
	}
	

	/**
	 * 
	 */
	public String getJsonString(Object object) 
	{
		ObjectMapper mapper = getObjectMapper();
		try
		{
			return mapper.writeValueAsString(object);
		} 
		catch (IOException e) 
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 */
	public String getXmlString(Object object) 
	{
		XmlMapper mapper = getXmlMapper();
		try
		{
			return mapper.writeValueAsString(object);
		} 
		catch (IOException e) 
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 *
	 */
	public ObjectNode hyperlinkToJsonObject(JRPrintHyperlink hyperlink)
	{
		ObjectNode hyperlinkNode = getObjectMapper().createObjectNode();

		addProperty(hyperlinkNode, "type", hyperlink.getLinkType());
		addProperty(hyperlinkNode, "typeValue", hyperlink.getHyperlinkTypeValue().getName());
		addProperty(hyperlinkNode, "target", hyperlink.getLinkTarget());
		addProperty(hyperlinkNode, "targetValue", hyperlink.getHyperlinkTargetValue().getHtmlValue(), hyperlink.getLinkTarget());
		addProperty(hyperlinkNode, "tooltip", hyperlink.getHyperlinkTooltip());
		addProperty(hyperlinkNode, "anchor", hyperlink.getHyperlinkAnchor());
		addProperty(hyperlinkNode, "page", String.valueOf(hyperlink.getHyperlinkPage()));
		addProperty(hyperlinkNode, "reference", hyperlink.getHyperlinkReference());

		JRPrintHyperlinkParameters hParams =  hyperlink.getHyperlinkParameters();
		if (hParams != null && hParams.getParameters().size() > 0)
		{
			ObjectNode params = getObjectMapper().createObjectNode();

			for (JRPrintHyperlinkParameter hParam: hParams.getParameters())
			{
				if (hParam.getValue() != null)
				{
					if (Collection.class.isAssignableFrom(hParam.getValue().getClass()))
					{
						ArrayNode paramValues = getObjectMapper().createArrayNode();
						Collection col = (Collection) hParam.getValue();
						for (Iterator it = col.iterator(); it.hasNext();)
						{
							Object next = it.next();
							paramValues.add(JRValueStringUtils.serialize(next.getClass().getName(), next));
						}
						params.set(hParam.getName(), paramValues);
					}
					else
					{
						params.put(hParam.getName(), JRValueStringUtils.serialize(hParam.getValueClass(), hParam.getValue()));
					}
				}
			}

			hyperlinkNode.set("params", params);
		}

		return hyperlinkNode;
	}


	/**
	 *
	 */
	public void addProperty(ObjectNode objectNode, String property, String value)
	{
		addProperty(objectNode, property, value, null);
	}


	/**
	 *
	 */
	public void addProperty(ObjectNode objectNode, String property, String value, String altValue)
	{
		if (value != null && !value.equals("null"))
		{
			objectNode.put(property, value);
		}
		else if (altValue != null && !altValue.equals("null"))
		{
			objectNode.put(property, altValue);
		}
	}
}
