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
package net.sf.jasperreports.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.VersionComparator;
import net.sf.jasperreports.engine.xml.JRXmlBaseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class CastorUtil
{
	private static final Log log = LogFactory.getLog(CastorUtil.class);
	
	public static final String EXCEPTION_MESSAGE_KEY_MAPPINGS_LOADING_ERROR = "util.castor.mappings.loading.error";
	
	/**
	 * 
	 */
	private static final String CASTOR_READ_XML_CONTEXT_KEY = "net.sf.jasperreports.castor.read.xml.context";
	private static final String CASTOR_WRITE_XML_CONTEXT_KEY = "net.sf.jasperreports.castor.write.xml.context";
	
	private JasperReportsContext jasperReportsContext;
	private VersionComparator versionComparator;


	/**
	 *
	 */
	private CastorUtil(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.versionComparator = new VersionComparator();
	}
	
	
	/**
	 *
	 */
	public static CastorUtil getInstance(JasperReportsContext jasperReportsContext)
	{
		return new CastorUtil(jasperReportsContext);
	}
	
	private XMLContext getReadXmlContext()
	{
		return getXmlContext(CASTOR_READ_XML_CONTEXT_KEY, null);//always reading with the last version mappings
	}
	
	private XMLContext getWriteXmlContext()
	{
		String targetVersion = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(
				JRXmlBaseWriter.PROPERTY_REPORT_VERSION);
		if (log.isDebugEnabled())
		{
			log.debug("using write mappings for version " + targetVersion);
		}
		
		return getXmlContext(CASTOR_WRITE_XML_CONTEXT_KEY, targetVersion);
	}
	
	/**
	 *
	 */
	private XMLContext getXmlContext(String contextCacheKey, String version)
	{
		XMLContext xmlContext = (XMLContext)jasperReportsContext.getOwnValue(contextCacheKey);
		if (xmlContext == null)
		{
			xmlContext = new XMLContext();

			Mapping mapping  = xmlContext.createMapping();
			
			List<CastorMapping> castorMappings = getMappings(version);
			for (CastorMapping castorMapping : castorMappings)
			{
				loadMapping(mapping, castorMapping.getPath());
			}
			
			try
			{
				xmlContext.addMapping(mapping);
			}
			catch (MappingException e)
			{
				throw 
					new JRRuntimeException(
						EXCEPTION_MESSAGE_KEY_MAPPINGS_LOADING_ERROR,
						(Object[])null,
						e);
			}
			
			jasperReportsContext.setValue(contextCacheKey, xmlContext);
		}
		return xmlContext;
	}


	protected List<CastorMapping> getMappings(String version)
	{
		List<CastorMapping> castorMappings = jasperReportsContext.getExtensions(CastorMapping.class);
		Map<String, CastorMapping> keyMappings = new HashMap<String, CastorMapping>();
		for (CastorMapping mapping : castorMappings)
		{
			String key = mapping.getKey();
			if (key == null)
			{
				continue;
			}
			
			if (!isEligversionible(mapping, version))
			{
				continue;
			}
			
			CastorMapping existingMapping = keyMappings.get(key);
			if (existingMapping == null || newerThan(mapping, existingMapping))
			{
				keyMappings.put(key, mapping);
			}
		}
		
		List<CastorMapping> activeMappings = new ArrayList<CastorMapping>(castorMappings.size());
		for (CastorMapping mapping : castorMappings)
		{
			String key = mapping.getKey();
			if (key == null // mappings with no keys are always considered active
					// checking if it's the most recent eligible mapping
					|| keyMappings.get(key).equals(mapping))
			{
				activeMappings.add(mapping);
			}
		}
		return activeMappings;
	}

	protected boolean isEligversionible(CastorMapping castorMapping, String targetVersion)
	{
		String mappingVersion = getVersion(castorMapping);
		return versionComparator.compare(targetVersion, mappingVersion) >= 0;
	}
	
	private boolean newerThan(CastorMapping mapping, CastorMapping existingMapping)
	{
		String version = getVersion(mapping);
		String existingVersion = getVersion(existingMapping);
		return versionComparator.compare(version, existingVersion) > 0;
	}

	protected String getVersion(CastorMapping castorMapping)
	{
		String mappingVersion = castorMapping.getVersion();
		if (mappingVersion == null)
		{
			// if the mapping does not specify a version we consider it the initial mapping
			// using a min version to avoid null checks
			mappingVersion = VersionComparator.LOWEST_VERSION;
		}
		return mappingVersion;
	}
	
	/**
	 *
	 */
	private void loadMapping(Mapping mapping, String mappingFile)
	{
		try
		{
			byte[] mappingFileData = JRLoader.loadBytesFromResource(mappingFile);
			InputSource mappingSource = new InputSource(new ByteArrayInputStream(mappingFileData));

			mapping.loadMapping(mappingSource);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(InputStream is, String mappingFile)
	{
		Object object = null;
		
		InputStream mis = null;
		
		try
		{
			mis = JRLoader.getLocationInputStream(mappingFile);

			Mapping mapping = new Mapping();
			mapping.loadMapping(
				new InputSource(mis)
				);
			
			object = read(is, mapping);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (mis != null)
			{
				try
				{
					mis.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		
		return object;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(Node node, String mappingFile)
	{
		Object object = null;
		
		InputStream mis = null;
		
		try
		{
			mis = JRLoader.getLocationInputStream(mappingFile);

			Mapping mapping = new Mapping();
			mapping.loadMapping(
				new InputSource(mis)
				);
			
			object = read(node, mapping);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (mis != null)
			{
				try
				{
					mis.close();
				}
				catch(IOException e)
				{
				}
			}
		}
		
		return object;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(InputStream is, Mapping mapping)
	{
		Object object = null;
		
		try
		{
			Unmarshaller unmarshaller = new Unmarshaller(mapping);//FIXME initialization is not thread safe
			unmarshaller.setWhitespacePreserve(true);
			object = unmarshaller.unmarshal(new InputSource(is));
		}
		catch (MappingException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (MarshalException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (ValidationException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return object;
	}
	

	/**
	 * 
	 */
	public Object read(InputStream is)
	{
		try
		{
			Unmarshaller unmarshaller = getReadXmlContext().createUnmarshaller();//FIXME initialization is not thread safe
			unmarshaller.setWhitespacePreserve(true);
			Object object = unmarshaller.unmarshal(new InputSource(is));
			return object;
		}
		catch (MarshalException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (ValidationException e)
		{
			throw new JRRuntimeException(e);
		}
	}

	public String writeToString(Object object)
	{
		StringWriter writer = new StringWriter();
		write(object, writer);
		return writer.toString();
	}
	
	public void writeToFile(Object object, String filename)
	{
		OutputStream output = null;
		boolean closed = false;
		try
		{
			output = new BufferedOutputStream(new FileOutputStream(filename));
			write(object, output);
			output.close();
			closed = true;
		}
		catch (FileNotFoundException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (output != null && !closed)
			{
				try
				{
					output.close();
				}
				catch (IOException e)
				{
					//NOP
				}
			}
		}
	}
	
	public void write(Object object, OutputStream output)
	{
		try
		{
			Writer writer = new OutputStreamWriter(output, "UTF-8");//hardcoding utf8 instead of the default encoding
			write(object, writer);
		} 
		catch (UnsupportedEncodingException e)
		{
			// should not happen
			throw new JRRuntimeException(e);
		}
	}
	
	public void write(Object object, Writer writer)
	{
		Marshaller marshaller = getWriteXmlContext().createMarshaller();
		try
		{
			marshaller.setWriter(writer);
			marshaller.setMarshalAsDocument(false);
			marshaller.marshal(object);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		} 
		catch (MarshalException e)
		{
			throw new JRRuntimeException(e);
		} 
		catch (ValidationException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(Node node, Mapping mapping)
	{
		Object object = null;
		
		try
		{
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setWhitespacePreserve(true);
			object = unmarshaller.unmarshal(node);
		}
		catch (MappingException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (MarshalException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (ValidationException e)
		{
			throw new JRRuntimeException(e);
		}
		
		return object;
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(InputStream is, Class<?> clazz)
	{
		return read(is, getMappingFileName(clazz));
	}
	
	
	/**
	 * @deprecated Replaced by {@link #read(InputStream)}.
	 */
	public static Object read(Node node, Class<?> clazz)
	{
		return read(node, getMappingFileName(clazz));
	}
	
	
	/**
	 * @deprecated Replaced by {@link #write(Object, Writer)}.
	 */
	public static void write(Object object, String mappingFile, Writer writer)
	{
		InputStream mis = null;
		
		try
		{
			mis = JRLoader.getLocationInputStream(mappingFile);

			Mapping mapping = new Mapping();
			mapping.loadMapping(
				new InputSource(mis)
				);

			write(object, mapping, writer);
		}
		catch (JRException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (mis != null)
			{
				try
				{
					mis.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}
	

	/**
	 * @deprecated Replaced by {@link #write(Object, Writer)}.
	 */
	public static void write(Object object, Mapping mapping, Writer writer)
	{
		try
		{
			Marshaller marshaller = new Marshaller(writer);

			marshaller.setMapping(mapping);
			marshaller.setMarshalAsDocument(false);

			marshaller.marshal(object);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (MappingException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (MarshalException e)
		{
			throw new JRRuntimeException(e);
		}
		catch (ValidationException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	

	/**
	 * @deprecated Replaced by {@link #writeToFile(Object, String)}.
	 */
	public static void write(Object object, String mappingFile, File file)
	{
		Writer writer = null;
		
		try
		{
			writer = new FileWriter(file);
			write(object, mappingFile, writer);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}
	

	/**
	 * @deprecated Replaced by {@link #writeToFile(Object, String)}.
	 */
	public static void write(Object object, Mapping mapping, File file)
	{
		Writer writer = null;
		
		try
		{
			writer = new FileWriter(file);
			write(object, mapping, writer);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{
				}
			}
		}
	}
	

	/**
	 * @deprecated Replaced by {@link #writeToString(Object)}.
	 */
	public static String write(Object object, String mappingFile)
	{
		StringWriter writer = new StringWriter();
		
		try
		{
			write(object, mappingFile, writer);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(IOException e)
			{
			}
		}
		
		return writer.toString();
	}

	
	/**
	 * @deprecated Replaced by {@link #writeToString(Object)}.
	 */
	public static String write(Object object, Mapping mapping)
	{
		StringWriter writer = new StringWriter();
		
		try
		{
			write(object, mapping, writer);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(IOException e)
			{
			}
		}
		
		return writer.toString();
	}

	
	/**
	 * @deprecated Replaced by {@link #writeToString(Object)}.
	 */
	public static String write(Object object)
	{
		StringWriter writer = new StringWriter();
		
		try
		{
			write(object, getMappingFileName(object.getClass()), writer);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch(IOException e)
			{
			}
		}
		
		return writer.toString();
	}

	
	/**
	 *
	 */
	private static String getMappingFileName(Class<?> clazz)
	{
		return clazz.getName().replace(".", "/") + ".xml";
	}

	
}
