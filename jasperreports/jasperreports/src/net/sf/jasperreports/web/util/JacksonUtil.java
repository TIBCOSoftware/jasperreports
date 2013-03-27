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
package net.sf.jasperreports.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JacksonUtil
{
	/**
	 * 
	 */
	private static final String OBJECT_MAPPER_CONTEXT_KEY = "net.sf.jasperreports.jackson.object.mapper";
	
	private JasperReportsContext jasperReportsContext;


	/**
	 *
	 */
	private JacksonUtil(JasperReportsContext jasperReportsContext)
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
	
	
	private ObjectMapper getObjectMapper()
	{
		ObjectMapper mapper = (ObjectMapper)jasperReportsContext.getValue(OBJECT_MAPPER_CONTEXT_KEY);
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
	
	
	/**
	 *
	 */
	private static void register(ObjectMapper mapper, JacksonMapping mapping)
	{
		try
		{
			Class<?> clazz = Class.forName(mapping.getClassName());
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
				result = new ArrayList<T>();
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
		catch (JsonGenerationException e) 
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
	
	
	/**
	 * 
	 */
	public String getEscapedJsonString(Object object)
	{
		return getJsonString(object).replaceAll("\\\"", "\\\\\\\"");
	}
}
